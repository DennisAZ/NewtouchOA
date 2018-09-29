package yh.core.funcs.message.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHMessageLogic {
  private static Logger log = Logger.getLogger(YHMessageLogic.class);
  
  public int totalPages;
  /**
   * 删除微讯
   * @param conn
   * @param bodyId
   * @param isform
   * @param userId
   * @throws Exception 
   */
  public boolean doDelSms(Connection conn,int bodyId,String isform,int userId) throws Exception{
    String[] fields = new String[]{"SEQ_ID","DELETE_FLAG","REMIND_FLAG","TO_ID"};
    String[] filters = new String[]{"BODY_SEQ_ID = " + bodyId};
    boolean isDe = true;
    String seqIdArr = "";
    try{
      ArrayList<YHDbRecord> db = findFlag(conn, "message", fields, filters);
      for (YHDbRecord dbRecord : db) {
        int seqId = YHUtility.cast2Long(dbRecord.getValueByName("SEQ_ID")).intValue();
        String deleteFlag =(String) dbRecord.getValueByName("DELETE_FLAG");
        String readFlag = (String) dbRecord.getValueByName("REMIND_FLAG");
        int toId = YHUtility.cast2Long(dbRecord.getValueByName("TO_ID")).intValue();
        //如果
        String from_id=getFromId(conn,seqId+"");
        if(from_id.equals(userId+"")){//发信人删除
          isform="2";
        }else if(userId==toId){//收信人删除
          isform="1";
        }else{
          isform="1";
        }
        
        
        boolean b = false;
        if(userId == toId && "1".equals(isform)){
          b = isCanDeleteSms(conn, isform, seqId + "", deleteFlag, readFlag);
        } else if("2".equals(isform)){
          b = isCanDeleteSms(conn, isform, seqId + "", deleteFlag, readFlag);
        }
        if(b){
          if(!"".equals(seqIdArr)){
            seqIdArr += ",";
          }
          seqIdArr += seqId;
        }
        isDe = isDe && b;
      }
      if(!"".equals(seqIdArr.trim())){
        deleteAll(conn, seqIdArr, "message");
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    if(isDe){
      deleteAll(conn,String.valueOf(bodyId), "oa_message_body");
    }
    return isDe;
  }
  
  //获取from——id
  public String getFromId(Connection conn,String seqId)throws Exception{
    String id="";
      Statement stmt=null;
      ResultSet rs=null;
      try{
        String sql=" select from_id from oa_message as message,oa_message_body as message_body where message.seq_id='"+seqId+"' and MESSAGE.BODY_SEQ_ID = message_body.seq_id";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);
        if(rs.next()){
          id=rs.getString(1);
           
          
        }
        
      }catch(Exception e){
        e.printStackTrace();
      }finally{
        YHDBUtility.close(stmt, rs, null);
      }
     
    
    return id;
    
  }
  
  
  
  
  /**
   * 批量删除SMS
   * @param conn
   * @param seqIds
   * @param tableName
   * @throws Exception
   */
  public void deleteAll(Connection conn,String seqIds,String tableName) throws Exception{
    String sql = "DELETE FROM " + tableName + " WHERE SEQ_ID IN(" + seqIds +")";
    PreparedStatement pstmt = null;
    try{
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(pstmt, null, null);
    }
  }
  /**
   * 查看短信是否处于可删除状态

   * 1收件人，2发件人

   * @param conn
   * @param seqId {seqId,seqId,seqId...}
   * @param deletFlag
   * @param readFlag
   * @throws SQLException
   */
  public boolean isCanDeleteSms(Connection conn ,String isFrom,String seqId,String deletFlag,String readFlag) throws Exception{
    int deletbit = Integer.valueOf(deletFlag.trim());
    int readbit = Integer.valueOf(readFlag.trim());
    boolean bool = false;
    //1.收件人删除01/11
    if("1".equals(isFrom)){
      if(deletbit == 0){
        deletbit = 1;
        updateFlag(conn,"REMIND_FLAG",0 + "",seqId,"oa_message");
      }else if(deletbit == 2){
        bool = true;
      }
    } else if("2".equals(isFrom)){
      //2.发件人删除10/11
      
        if(deletbit == 0) {
          if(readbit == 2 || readbit == 1 ){
            bool = true;
          }else if(readbit == 0){
            deletbit = 2;
          }
        }else if(deletbit ==  1) {
          bool = true;
        }
    }
    updateFlag(conn,"DELETE_FLAG",deletbit + "",seqId,"oa_message");
    return bool;
  }
  
  /**
   * 通过body_seq_id和to_id索引所有seq_id,可能有重复,getSmsSeqId方法只能取一条会引起bug
   * @param conn
   * @param toId
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<Integer> getMessageSeqIds(Connection conn, int toId, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    List<Integer> list = new ArrayList<Integer>();
    try {
      stmt = conn.createStatement();
      String queryStr = "select SEQ_ID from oa_message where TO_ID = " + toId + " and BODY_SEQ_ID = " + seqId;            
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
        list.add(rs.getInt("SEQ_ID"));
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public String getPanelInBox(Connection conn,Map request,int userId,int pageNo,int pageSize) throws Exception{
    YHPageDataList data = toInBoxJson(conn, request, userId,pageNo,pageSize,false);
     return data.toJson();
  }
  
  
  public String getPanelInBox1(Connection conn,Map request,int userId,int pageNo,int pageSize) throws Exception{
    YHPageDataList data = toInBoxJsonInbox(conn, request, userId,pageNo,pageSize,false);
     return data.toJson();
  }
  
  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public YHPageDataList toInBoxJson(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
    String whereStr =  "";
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String dbDateFremind = YHDBUtility.getDateFilter("MESSAGE.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");

    String sql =  "select " +
        " MESSAGE.SEQ_ID," +
        "MESSAGE_BODY.SEQ_ID," +
        "MESSAGE.TO_ID," +
        "MESSAGE_BODY.MESSAGE_TYPE," +
        "MESSAGE_BODY.SEND_TIME," +
        "MESSAGE_BODY.CONTENT," +
        "MESSAGE.DELETE_FLAG ," +
        "MESSAGE.REMIND_FLAG," +
        "MESSAGE_BODY.REMIND_URL," +
        "MESSAGE.REMIND_TIME" +
        " FROM " +
        "oa_message as MESSAGE," +
        "oa_message_body as MESSAGE_BODY" +
        " WHERE " +
        " MESSAGE.BODY_SEQ_ID = MESSAGE_BODY.SEQ_ID" +
        " AND MESSAGE_BODY.FROM_ID=" + userId +
        " AND " + dbDateF  +
        " AND (MESSAGE.REMIND_TIME IS NULL OR " + dbDateFremind + ")" ;
    if(isQuery){
      whereStr =  toSearchWhere(request, 1, true);
    }
    String nameStr = "messageId,messageBodyId,fromId,messageType,sendTime,content,deleteFlag,remindFlag,remindUrl,remindTime";
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }else{
      sql += " ORDER BY MESSAGE.REMIND_TIME DESC,SEND_TIME DESC";
    }
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(pageIndex);
     queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList;
  }
  
  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public YHPageDataList toInBoxJsonInbox(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
    String whereStr =  "";
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String dbDateFremind = YHDBUtility.getDateFilter("MESSAGE.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");

    String sql =  "select " +
        " MESSAGE.SEQ_ID," +
        "MESSAGE_BODY.SEQ_ID," +
        "MESSAGE.TO_ID," +
        "MESSAGE_BODY.MESSAGE_TYPE," +
        "MESSAGE_BODY.SEND_TIME," +
        "MESSAGE_BODY.CONTENT," +
        "MESSAGE.DELETE_FLAG ," +
        "MESSAGE.REMIND_FLAG," +
        "MESSAGE_BODY.REMIND_URL," +
        "MESSAGE.REMIND_TIME" +
        " FROM " +
        "oa_message as MESSAGE," +
        "oa_message_body as MESSAGE_BODY" +
        " WHERE " +
        " MESSAGE.BODY_SEQ_ID = MESSAGE_BODY.SEQ_ID" +
        " AND MESSAGE_BODY.FROM_ID=" + userId +
        " AND " + dbDateF  +
        " AND (MESSAGE.REMIND_TIME IS NULL OR " + dbDateFremind + ")" +
        " and DELETE_FLAG!='2' ";
    if(isQuery){
      whereStr =  toSearchWhere(request, 1, true);
    }
    String nameStr = "messageId,messageBodyId,fromId,messageType,sendTime,content,deleteFlag,remindFlag,remindUrl,remindTime";
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }else{
      sql += " ORDER BY MESSAGE.REMIND_TIME DESC,SEND_TIME DESC";
    }
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(pageIndex);
     queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList;
  }
  
  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public YHPageDataList toSendBoxJson2(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
    String whereStr =  "";
    String whereStr2 =  "";
    String userType = request.get("userType") != null ? ((String[])request.get("userType"))[0] : null;
    
      whereStr =  toSearchWhere(request, Integer.parseInt(userType), true);
      whereStr2 =  toSearchWhere(request,  Integer.parseInt(userType), false);
    String bodyIds = getSendBoxBodyId2(conn, userId ,pageIndex,pageSize,whereStr);
    int count = getSendCount(conn, userId,whereStr2);
    if(bodyIds == null || "".equals(bodyIds.trim())){
      return new YHPageDataList();
    }
   
    String sql =  "select " +
        "MESSAGE_BODY.SEQ_ID," +
        "MESSAGE_BODY.FROM_ID," +
        "MESSAGE_BODY.MESSAGE_TYPE," +
        "MESSAGE_BODY.SEND_TIME," +
        "MESSAGE_BODY.CONTENT," +
        "MESSAGE_BODY.REMIND_URL" +
        " FROM " +
        " oa_message_body as MESSAGE_BODY " +
        " WHERE " +
        " MESSAGE_BODY.SEQ_ID in (" + bodyIds + ")";
     //   " AND MESSAGE.TO_ID='" + userId +"'";
    String orderBy = request.get("orderBy") != null ? ((String[])request.get("orderBy"))[0] : null;
    String orderBySeq = request.get("orderBySeq") != null ? ((String[])request.get("orderBySeq"))[0] : null;
    String query = "";
      if(orderBy != null && !"".equals(orderBy)) {
        query += " ORDER BY " + orderBy  + " ";
        if(orderBySeq != null && !"".equals(orderBySeq)) {
          query += orderBySeq;
        }else {
          query += "DESC";
        }
      }else{
        query += " ORDER BY SEND_TIME DESC";
      }

    

    String nameStr = "messageBodyId,fromId,messageType,sendTime,content,remindUrl";
    sql += query;
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(0);
     queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    pageDataList.setTotalRecord(count);
    return pageDataList;
  }
  
  public YHPageDataList toSendBoxJson(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
    String whereStr =  "";
    String whereStr2 =  "";
        whereStr =  toSearchWhere(request, 2, true);
      whereStr2 =  toSearchWhere(request, 2, false);
    String bodyIds = getSendBoxBodyId(conn, userId ,pageIndex,pageSize,whereStr);
    int count = getSendCount(conn, userId,whereStr2);
    if(bodyIds == null || "".equals(bodyIds.trim())){
      return new YHPageDataList();
    }
   
    String sql =  "select " +
        "MESSAGE_BODY.SEQ_ID," +
        "MESSAGE_BODY.FROM_ID," +
        "MESSAGE_BODY.MESSAGE_TYPE," +
        "MESSAGE_BODY.SEND_TIME," +
        "MESSAGE_BODY.CONTENT," +
        "MESSAGE_BODY.REMIND_URL" +
        " FROM " +
        " oa_message_body as MESSAGE_BODY " +
        " WHERE " +
        " MESSAGE_BODY.SEQ_ID in (" + bodyIds + ")";
     //   " AND MESSAGE.TO_ID='" + userId +"'";
    String  query =" ORDER BY SEND_TIME DESC";
    String nameStr = "messageBodyId,fromId,messageType,sendTime,content,remindUrl";
    sql += query;
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(0);
     queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    pageDataList.setTotalRecord(count);
    return pageDataList;
  }
  
  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public YHPageDataList toSendBoxJson1(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
    String whereStr =  "";
    String toId = request.get("toId") != null ? ((String[])request.get("toId"))[0] : null;
    
      whereStr =  toSearchWhere1(toId, userId+"");
   
    String bodyIds = getSendBoxBodyId1(conn, userId ,pageIndex,pageSize,whereStr);
    int count = getSendCount(conn, userId,whereStr);
    if(bodyIds == null || "".equals(bodyIds.trim())){
      return new YHPageDataList();
    }
   
    String sql =  "select " +
        "MESSAGE_BODY.SEQ_ID," +
        "MESSAGE_BODY.FROM_ID," +
        "MESSAGE_BODY.MESSAGE_TYPE," +
        "MESSAGE_BODY.SEND_TIME," +
        "MESSAGE_BODY.CONTENT," +
        "MESSAGE_BODY.REMIND_URL" +
        " FROM " +
        " oa_message_body as MESSAGE_BODY " +
        " WHERE " +
        " MESSAGE_BODY.SEQ_ID in (" + bodyIds + ")";
     //   " AND MESSAGE.TO_ID='" + userId +"'";
    String query = " order by SEND_TIME desc";
    String nameStr = "messageBodyId,fromId,messageType,sendTime,content,remindUrl";
    sql += query;
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(0);
     queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    pageDataList.setTotalRecord(count);
    return pageDataList;
  }
  
  
  /**
   * 
   * @param conn
   * @param bodyId
   * @return
   * @throws Exception
   */
  public String getToIdByBodyId(Connection conn, int bodyId) throws Exception{
    String sql = "SELECT TO_ID FROM oa_message WHERE BODY_SEQ_ID = " + bodyId;
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int toId = rs.getInt(1);
        if(!"".equals(result)){
          result += ",";
        }
        result += toId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public String getContent(Connection conn, String seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String contentDetail = "";
    try {
      String queryStr = "select CONTENT from oa_message_body where SEQ_ID = " + Integer.parseInt(seqId);
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){
        contentDetail = rs.getString("CONTENT");     
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return contentDetail;
  }
  
  /**
   * 
   * @param conn
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public String getSendBoxBodyId(Connection conn ,int userId ,int pageIndex,int pageSize,String whereStr) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "SELECT DISTINCT MESSAGE_BODY.SEQ_ID, MESSAGE_BODY.SEND_TIME,MESSAGE_BODY.MESSAGE_TYPE FROM oa_message_body as MESSAGE_BODY,oa_message as MESSAGE where MESSAGE.BODY_SEQ_ID=MESSAGE_BODY.seq_id  and TO_ID='" + userId + "' and  DELETE_FLAG IN(0) and " + dbDateF + " " ;
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }else{
      sql += " ORDER BY SEND_TIME DESC";
    }
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery() ;
      rs.last();
      int recordCnt = rs.getRow();
      if (recordCnt == 0) {
        return "";
      }
      int pageCnt = recordCnt / pageSize;
      if (recordCnt % pageSize != 0) {
        pageCnt++;
      }
      if (pageIndex < 0) {
        pageIndex = 0;
      }
      if (pageIndex > pageCnt - 1) {
        pageIndex = pageCnt - 1;
      }
      rs.absolute(pageIndex * pageSize + 1);

      for (int i = 0; i < pageSize && !rs.isAfterLast(); i++) {
        int bodyId = rs.getInt(1);
        if(!"".equals(result.trim())){
          result += ",";
        }
        result += bodyId;
        rs.next();
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  
  /**
   * 
   * @param conn
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public String getSendBoxBodyId2(Connection conn ,int userId ,int pageIndex,int pageSize,String whereStr) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "SELECT DISTINCT MESSAGE_BODY.SEQ_ID, MESSAGE_BODY.SEND_TIME,MESSAGE_BODY.MESSAGE_TYPE FROM oa_message_body as MESSAGE_BODY,oa_message as MESSAGE where MESSAGE.BODY_SEQ_ID=MESSAGE_BODY.seq_id  and (TO_ID=" + userId + " or FROM_ID='"+userId+"') and  DELETE_FLAG!='2' and " + dbDateF + " " ;
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }else{
      sql += " ORDER BY SEND_TIME DESC";
    }
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery() ;
      rs.last();
      int recordCnt = rs.getRow();
      if (recordCnt == 0) {
        return "";
      }
      int pageCnt = recordCnt / pageSize;
      if (recordCnt % pageSize != 0) {
        pageCnt++;
      }
      if (pageIndex < 0) {
        pageIndex = 0;
      }
      if (pageIndex > pageCnt - 1) {
        pageIndex = pageCnt - 1;
      }
      rs.absolute(pageIndex * pageSize + 1);

      for (int i = 0; i < pageSize && !rs.isAfterLast(); i++) {
        int bodyId = rs.getInt(1);
        if(!"".equals(result.trim())){
          result += ",";
        }
        result += bodyId;
        rs.next();
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  /**
   * 
   * @param conn
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public String getSendBoxBodyId1(Connection conn ,int userId ,int pageIndex,int pageSize,String whereStr) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "SELECT DISTINCT MESSAGE_BODY.SEQ_ID, MESSAGE_BODY.SEND_TIME,MESSAGE_BODY.MESSAGE_TYPE FROM oa_message_body as MESSAGE_BODY,oa_message as MESSAGE where MESSAGE.BODY_SEQ_ID=MESSAGE_BODY.seq_id   and  DELETE_FLAG IN(0,1) and " + dbDateF + " " ;
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }else{
      sql += " ORDER BY SEND_TIME DESC";
    }
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery() ;
      rs.last();
      int recordCnt = rs.getRow();
      if (recordCnt == 0) {
        return "";
      }
      int pageCnt = recordCnt / pageSize;
      if (recordCnt % pageSize != 0) {
        pageCnt++;
      }
      if (pageIndex < 0) {
        pageIndex = 0;
      }
      if (pageIndex > pageCnt - 1) {
        pageIndex = pageCnt - 1;
      }
      rs.absolute(pageIndex * pageSize + 1);

      for (int i = 0; i < pageSize && !rs.isAfterLast(); i++) {
        int bodyId = rs.getInt(1);
        if(!"".equals(result.trim())){
          result += ",";
        }
        result += bodyId;
        rs.next();
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  
  /**
   * 
   * @param conn
   * @param bodyId
   * @return
   * @throws Exception
   */
  public String getStatusByBodyId(Connection conn, int bodyId) throws Exception{
    String sql = "SELECT DELETE_FLAG,REMIND_FLAG FROM oa_message WHERE BODY_SEQ_ID = " + bodyId;
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean isRead = true;
    boolean isDel = true;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String deleteFlag = rs.getString(1);
        String remindFlag = rs.getString(2);
        if(remindFlag.equals("0")){
          isRead = isRead && true;
        }else {
          isRead = isRead && false;
        }
        if(deleteFlag.equals("1")){
          isDel = isDel && true;
        }else {
          isDel = isDel && false;
        }
      }
      result ="{isRead:\""+ (isRead ? "1" : "0") + "\",isDel:\"" + (isDel ? "1" : "0") + "\"}";
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public int getSendCount(Connection conn ,int userId,String whereStr) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "select distinct MESSAGE_BODY.SEQ_ID ,MESSAGE_BODY.SEND_TIME FROM oa_message_body as MESSAGE_BODY,oa_message as MESSAGE WHERE MESSAGE.BODY_SEQ_ID=MESSAGE_BODY.seq_id  and FROM_ID=" + userId + " and  DELETE_FLAG!='2' and " + dbDateF + " " ;
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery() ;
      rs.last();
      result = rs.getRow();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  public String getMessageTypeDesc(Connection conn,int smsType,String code) throws Exception{
    String sql = "select CLASS_CODE,CLASS_DESC from oa_kind_dict_item where CLASS_CODE = ('" + smsType + "') and CLASS_NO = '" + code + "'";
    PreparedStatement ps = null;
    String result = "";
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String classDesc = rs.getString(2);
        result = classDesc;
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  private String toSearchWhere(Map request,int type,boolean canOrder) throws Exception{
    String whereStr = "";
    String startDateStr = request.get("startDate") != null ? ((String[])request.get("startDate"))[0] : null;
    String endDateStr = request.get("endDate") != null ? ((String[])request.get("endDate"))[0] : null;
    String smsType = request.get("smsType") != null ? ((String[])request.get("smsType"))[0] : null;
    String content = request.get("content") != null ? YHDBUtility.escapeLike(((String[])request.get("content"))[0]) : null;
    String userIdStr = request.get("userId") != null ? ((String[])request.get("userId"))[0] : null;
    String orderBy = request.get("orderBy") != null ? ((String[])request.get("orderBy"))[0] : null;
    String orderBySeq = request.get("orderBySeq") != null ? ((String[])request.get("orderBySeq"))[0] : null;
    String userType = request.get("userType") != null ? ((String[])request.get("userType"))[0] : null;
    
    if(userIdStr != null && !"".equals(userIdStr)){
      if(userIdStr.trim().endsWith(",")){
        userIdStr = userIdStr.trim().substring(0, userIdStr.trim().length() - 1);
      }
      if(type == 1){
        whereStr += " and MESSAGE_BODY.FROM_ID in(" + userIdStr + ")";
      } else if(type == 2){
        whereStr += " and MESSAGE.TO_ID in(" + userIdStr + ")";
      }
    }
  //加上开始日期、截止日期条件

  if(startDateStr != null && !"".equals(startDateStr)){
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", startDateStr, " >= ");
    whereStr += " and " + dbDateF;
  }
  if(endDateStr != null && !"".equals(endDateStr)){
     String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", endDateStr, " <= ");
     whereStr += " and " + dbDateF;
  }
  //加上日志类型条件
  if(smsType != null && !"".equals(smsType)){
    whereStr += " and MESSAGE_TYPE='" + smsType + "'";
  }
  //加上标题条件
  if(content != null && !"".equals(content)) {
    whereStr += " and CONTENT like '%" + content + "%'"  + YHDBUtility.escapeLike()  ;
  }
  if(canOrder){
    if(orderBy != null && !"".equals(orderBy)) {
      whereStr += " ORDER BY " + orderBy  + " ";
      if(orderBySeq != null && !"".equals(orderBySeq)) {
        whereStr += orderBySeq;
      }else {
        whereStr += "DESC";
      }
    }else{
      whereStr += " ORDER BY SEND_TIME DESC";
    }
  }
    return whereStr;
  }
  
  private String toSearchWhere1(String toId,String userId) throws Exception{
    String whereStr = "";
  
    whereStr=" and ((to_Id='"+toId+"' and from_id='"+userId+"' ) or (to_Id='"+userId+"' and from_id='"+toId+"' )) ";
  
    return whereStr;
  }
  
  
  
  
  /**
   * 标记标记位

   * @param conn
   * @param field
   * @param value
   * @param seqId
   * @param tableName
   * @throws SQLException
   */
  public void updateFlag(Connection conn,String field,String value,String seqId,String tableName) throws Exception{
    String sql = "UPDATE " + tableName + " SET " + field + " = '" + value + "'" + " WHERE SEQ_ID IN (" + seqId + ")";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    try{
      pstmt.executeUpdate();
    }catch(Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      YHDBUtility.close(pstmt, null, null);
    }
  }
  public void updateFalg(Connection conn, int userId, int bodyId) throws Exception{
    String sql = "update oa_message set REMIND_FLAG = '0' where BODY_SEQ_ID= " + bodyId + " AND TO_ID=" + userId;
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   *  查找所有标记

   * @param conn
   * @param fields
   * @param filters
   * @throws Exception 
   */
   public ArrayList<YHDbRecord> findFlag(Connection conn,String tableName,String[] fields,String[] filters) throws Exception{
     ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
     String fieldNames = "";
     String filter = "";
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
       if(fields != null){
         for (String field : fields){
           if(!"".equals(fieldNames)){
             fieldNames += ",";
           }
           fieldNames += field;
         }
       }
       if(filters != null){
         for (String f : filters){
           if(!"".equals(filter)){
             filter += " AND ";
           }
           filter += f;
         }
       }
       String sql = "SELECT " + fieldNames + " FROM " + tableName ;
       if(!"".equals(filter)){
         sql += " WHERE " + filter;
       }
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         int seqId = rs.getInt("SEQ_ID");
         String deleteFlag = rs.getString("DELETE_FLAG");
         String readFlag = rs.getString("REMIND_FLAG");
         int toId = rs.getInt("TO_ID");
         YHDbRecord db = new YHDbRecord();
         db.addField("SEQ_ID", seqId);
         db.addField("DELETE_FLAG", deleteFlag);
         db.addField("REMIND_FLAG", readFlag);
         db.addField("TO_ID", toId);
         result.add(db);
       }
    } catch (Exception e) {
       throw e;
    }  finally{
      YHDBUtility.close(ps, rs, null);
    }
     
     return result;
   }
  
  /*
   * 
  */
  public String getUserDeptName(Connection conn,int deptId) throws Exception{
    YHORM orm = new YHORM();
    YHDepartment dept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, deptId);
    if(dept == null){
      return "";
    }
    return dept.getDeptName();
  }
  
  public String getUserName(Connection conn,String deptId) throws Exception{
    String userData="";
    YHORM orm = new YHORM();
    String uid[] = deptId.split(",");
    for(int i=0 ;i<uid.length;i++){
    YHPerson dept = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(deptId));
      if(dept == null){
       continue;
      }else{
        userData+=dept.getUserName();
      }
    }
    if(userData.endsWith(".")){
      userData=userData.substring(0, userData.length()-1);
    }
    return userData;
  }
  
  /**
   * 
   * @param conn
   * @param userId
   * @param pageNo
   * @param pageSize
   * @return
   * @throws Exception
   */
  public StringBuffer getPanelSentBox(Connection conn,Map request,int userId,int pageNo,int pageSize) throws Exception{

    YHPageDataList data = toSendBoxJson(conn, request, userId, pageNo, pageSize, false);
    StringBuffer sb = new StringBuffer("{data:[ ");
    for(int i = 0 ; i < data.getRecordCnt() ; i ++){
      YHDbRecord rec = data.getRecord(i);
      String remindUrl =  (String)rec.getValueByName("remindUrl");
      if(remindUrl == null){
        remindUrl = "";
      }
      String content = (String) rec.getValueByName("content");
      content = content.replaceAll("[\n\r]", "<br>");
      content = YHUtility.encodeSpecial(content);
      sb.append("{");
      sb.append("bodyId:\"" + rec.getValueByName("messageBodyId") + "\"");
      sb.append(",fromId:\"" + rec.getValueByName("fromId") + "\"");
      sb.append(",messageType:\"" + rec.getValueByName("messageType") + "\"");
      sb.append(",content:\"" + content + "\"");
      sb.append(",sendTime:\"" + YHUtility.getDateTimeStr((Date)rec.getValueByName("sendTime")) + "\"");
      sb.append(",remindUrl:\"" + remindUrl + "\"");
      sb.append("},");
     }
     if(sb.toString().trim().endsWith(",")){
       sb.deleteCharAt(sb.length() - 1); 
     }
     sb.append("]");
     sb.append(",sizeNo:").append(data.getTotalRecord());
     sb.append(",pageNo:").append(pageNo);
     sb.append(",pageSize:").append(pageSize);
     sb.append("}");
   
    
     return sb;
  }
  
  /**
   * 
   * @param conn
   * @param smsIds
   * @return
   * @throws Exception
   */
   public List<Map<String, String>> viewDetailsLogic(Connection conn,String smsIds,int userId) throws Exception{
     List<Map<String, String>> l = new ArrayList<Map<String,String>>();
     if(smsIds == null || "".equals(smsIds.trim()) || ",".equals(smsIds.trim())){
       return l;
     }
     smsIds = smsIds.trim();
     if(smsIds.endsWith(",")){
       smsIds = smsIds.substring(0, smsIds.length() -1);
     }
     String sql = "select message.seq_id,message_body.message_TYPE ,message_body.content,message_body.REMIND_URL from oa_message as message,oa_message_body as message_body where message_body.seq_id = message.body_seq_id and message_body.seq_id in(" + smsIds + ") and message.to_id = " + userId;
     PreparedStatement ps =null;
     ResultSet rs = null;
     try {
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       int count = 1;
       int personCount = 0;
       while (rs.next()) {
         int smsId = rs.getInt(1);
         String smsType = rs.getString(2);
         String content = rs.getString(3);
         String remindUrl = rs.getString(4);
         
         if("0".equals(smsType)){
           if(personCount != 0){
             continue;
           }
           remindUrl = "/yh/core/funcs/message/act/YHMessageAct/acceptedSms.act?pageNo=0&pageSize=20";
           personCount ++;
         }
         if(content.length() > 30){
           content = content.substring(0, 30);
         }
         String img = "sms_type" + smsType + ".gif";
         Map<String, String> m = new HashMap<String, String>();
         m.put("text", "工作" + count);
         m.put("url", remindUrl);
         m.put("title", content);
         m.put("img", img);
         l.add(m);
         count ++;
       }
     } catch (Exception e) {
       throw e;
     } finally {
       YHDBUtility.close(ps, rs, null);
     }
     return l;
   }
   
   /**
    * 是否有为提醒的邮件

    * @param conn
    * @param userId
    * @return
    * @throws Exception
    */
   public int isRemind(Connection conn,int userId) throws Exception{
     int result = 0;
     String dateFiler = YHDBUtility.getDateFilter("T0.SEND_TIME", YHUtility.getDateTimeStr(new Date()), "<=");
     String dbDateFremind = YHDBUtility.getDateFilter("T1.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
     String sql = "SELECT COUNT(*) FROM oa_message T1 ,oa_message_body T0 WHERE TO_ID=" + userId + " AND REMIND_FLAG = '1' AND  T1.BODY_SEQ_ID= T0.SEQ_ID  " +
         "and DELETE_FLAG in (0, 2) " +
         "AND " + dateFiler +
         " AND (T1.REMIND_TIME IS NULL OR " + dbDateFremind + ")";
     PreparedStatement ps = null;
     ResultSet rs = null;
     try{
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       if(rs.next()){
         int count = rs.getInt(1);
         result = (count > 0) ? count : 0;
       }
     } catch (Exception e){
       throw e;
     } finally {
       YHDBUtility.close(ps, rs, log);
     }
     return result;
   }
   

  
}

package yh.core.funcs.sms.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
/**
 * 
 * @author Think
 *
 */
public class YHSmsTestLogic {
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
    String dbDateFremind = YHDBUtility.getDateFilter("SMS.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");

    String sql =  "select " +
    		" SMS.SEQ_ID," +
    	  "SMS_BODY.SEQ_ID," +
    		"SMS_BODY.FROM_ID," +
    	  "SMS_BODY.SMS_TYPE," +
    	  "SMS_BODY.SEND_TIME," +
    	  "SMS_BODY.CONTENT," +
    	  "SMS.DELETE_FLAG ," +
    		"SMS.REMIND_FLAG," +
    		"SMS_BODY.REMIND_URL," +
    		"SMS.REMIND_TIME" +
    		" FROM " +
    		"SMS," +
    		" oa_msg_body as SMS_BODY" +
    		" WHERE " +
    		" SMS.BODY_SEQ_ID = SMS_BODY.SEQ_ID" +
    		" AND SMS.TO_ID=" + userId +
    		" AND " + dbDateF  +
    		" AND (SMS.REMIND_TIME IS NULL OR " + dbDateFremind + ")" +
    		" AND DELETE_FLAG IN(0,2) ";
    if(isQuery){
      whereStr =  toSearchWhere(request, 1, true);
    }
    String nameStr = "smsId,smsBodyId,fromId,smsType,sendTime,content,deleteFlag,remindFlag,remindUrl,remindTime";
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }else{
      sql += " ORDER BY SMS.REMIND_TIME DESC,SEND_TIME DESC";
    }
    YHPageQueryParam queryParam = new YHPageQueryParam();
    queryParam.setNameStr(nameStr);
    queryParam.setPageIndex(pageIndex);
     queryParam.setPageSize(pageSize);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList;
  }
  /**
   * 得到导出的数据
   * @param conn
   * @param request
   * @param userId
   * @return
   * @throws Exception
   */
  public ArrayList<YHDbRecord> toInBoxExportData(Connection conn,Map request,int userId) throws Exception{
    ArrayList<YHDbRecord>  result = new ArrayList<YHDbRecord>();
    String whereStr =  "";
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String dbDateFremind = YHDBUtility.getDateFilter("SMS.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");

    String sql =  "select " +
        " SMS.SEQ_ID," +
        "SMS_BODY.SEQ_ID," +
        "PERSON.USER_NAME," +
        "SMS_BODY.SMS_TYPE," +
        "SMS_BODY.SEND_TIME," +
        "SMS_BODY.CONTENT," +
        "SMS.DELETE_FLAG ," +
        "SMS.REMIND_FLAG," +
        "SMS_BODY.REMIND_URL," +
        "SMS.REMIND_TIME" +
        " FROM " +
        "SMS," +
        " oa_msg_body as SMS_BODY," +
        "PERSON " +
        " WHERE " +
        " SMS.BODY_SEQ_ID = SMS_BODY.SEQ_ID" +
        " AND PERSON.SEQ_ID = SMS_BODY.FROM_ID" +
        " AND SMS.TO_ID=" + userId +
        " AND " + dbDateF  +
        " AND (SMS.REMIND_TIME IS NULL OR " + dbDateFremind + ")" +
        " AND DELETE_FLAG IN(0,2) ";
    
    whereStr =  toSearchWhere(request, 1, true);
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }else{
      sql += " ORDER BY SMS.REMIND_TIME DESC,SEND_TIME DESC";
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery() ;
      YHSmsLogic sl = new YHSmsLogic();
      while ( rs.next() ) {
        YHDbRecord dbrc = new YHDbRecord();
        String fromId = rs.getString(3);
        String smsType = rs.getString(4);
        String sendTime = YHUtility.getDateTimeStr(rs.getTimestamp(5));
        String content = rs.getString(6);
        String remindFlag = "1".equals(rs.getString(8)) ? "否" : "是";
        Date remindTime = rs.getTimestamp(10);
        
        if(remindTime != null){
          sendTime =  YHUtility.getDateTimeStr(remindTime);
        }

        String smsTypeStr = "";
        try {
          smsTypeStr = sl.getSmsTypeDesc(conn, Integer.valueOf(smsType), "SMS_REMIND");

        } catch (Exception e) {
          smsTypeStr = "个人短信";

        }
        dbrc.addField("类别", smsTypeStr);
        dbrc.addField("发送人", fromId);
        dbrc.addField("内容", content);
        dbrc.addField("发送时间", sendTime);
        dbrc.addField("提醒", remindFlag);
        result.add(dbrc);
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
   * @param request
   * @param userId
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  public YHPageDataList toNewBoxJson(Connection conn,Map request,int userId,int pageIndex,int pageSize) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String dbDateFremind = YHDBUtility.getDateFilter("SMS.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");

    String sql =  "select " +
        " SMS.SEQ_ID," +
        "SMS_BODY.SEQ_ID," +
        "SMS_BODY.FROM_ID," +
        "SMS_BODY.SMS_TYPE," +
        "SMS_BODY.SEND_TIME," +
        "SMS_BODY.CONTENT," +
        "SMS.DELETE_FLAG ," +
        "SMS.REMIND_FLAG," +
        "SMS_BODY.REMIND_URL," +
        "SMS.REMIND_TIME" +
        " FROM " +
        "SMS," +
        " oa_msg_body  as SMS_BODY" +
        " WHERE " +
        " SMS.BODY_SEQ_ID = SMS_BODY.SEQ_ID" +
        " AND SMS.TO_ID=" + userId +
        " AND DELETE_FLAG IN(0,2) " +
        " AND " + dbDateF  +
        " AND (SMS.REMIND_TIME IS NULL OR " + dbDateFremind + ")" +
        " AND REMIND_FLAG IN(1,2)";
    String nameStr = "smsId,smsBodyId,fromId,smsType,sendTime,content,deleteFlag,remindFlag,remindUrl,remindTime";
    sql += " ORDER BY SMS.REMIND_TIME DESC,SEND_TIME DESC";
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
  public YHPageDataList toSendBoxJson(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
    String whereStr =  "";
    String whereStr2 =  "";
    if(isQuery){
      whereStr =  toSearchWhere(request, 2, true);
      whereStr2 =  toSearchWhere(request, 2, false);
    }
    String bodyIds = getSendBoxBodyId(conn, userId ,pageIndex,pageSize,whereStr);
    int count = getSendCount(conn, userId,whereStr2);
    if(bodyIds == null || "".equals(bodyIds.trim())){
      return new YHPageDataList();
    }
   
    String sql =  "select " +
        "SMS_BODY.SEQ_ID," +
        "SMS_BODY.FROM_ID," +
        "SMS_BODY.SMS_TYPE," +
        "SMS_BODY.SEND_TIME," +
        "SMS_BODY.CONTENT," +
        "SMS_BODY.REMIND_URL" +
        " FROM " +
        "  oa_msg_body as SMS_BODY " +
        " WHERE " +
        " SMS_BODY.SEQ_ID in (" + bodyIds + ")";
    String query = " order by SEND_TIME desc";
    String nameStr = "smsBodyId,fromId,smsType,sendTime,content,remindUrl";
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
   * @param isQuery
   * @return
   * @throws Exception
   */
  public ArrayList<YHDbRecord> toSendBoxExportData(Connection conn,Map request,int userId) throws Exception{
    String whereStr =  "";
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>(50);
    whereStr =  toSearchWhere(request, 2, false);
    String bodyIds = getSendBoxBodyId(conn, userId ,0,200,whereStr);
    if(bodyIds == null || "".equals(bodyIds.trim())){
      return result;
    }
   
    String sql =  "select " +
        "SMS_BODY.SEQ_ID," +
        "SMS_BODY.FROM_ID," +
        "SMS_BODY.SMS_TYPE," +
        "SMS_BODY.SEND_TIME," +
        "SMS_BODY.CONTENT" +
        " FROM " +
        "  oa_msg_body as SMS_BODY " +
        " WHERE " +
        " SMS_BODY.SEQ_ID in (" + bodyIds + ")";
    String query = " order by SEND_TIME desc";
    sql += query;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery() ;
      YHSmsLogic sl = new YHSmsLogic();
      while ( rs.next() ) {
        YHDbRecord dbrc = new YHDbRecord();
        int seqId = rs.getInt(1);
        String toId = sl.getToIdByBodyId(conn, seqId);
        String smsType = rs.getString(3);
        String sendTime = rs.getTimestamp(4) == null ? "":YHUtility.getDateTimeStr(rs.getTimestamp(4));
        String content = rs.getString(5);
        String smsTypeStr = "";
        try {
          smsTypeStr = sl.getSmsTypeDesc(conn, Integer.valueOf(smsType), "SMS_REMIND");

        } catch (Exception e) {
          smsTypeStr = "个人短信";
        }
        dbrc.addField("类别", smsTypeStr);
        dbrc.addField("收信人", getUserName(conn, toId));
        dbrc.addField("内容", content);
        dbrc.addField("发送时间", sendTime);
        result.add(dbrc);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  public String getUserName(Connection conn, String toId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    if(toId != null && !"".equals(toId)){
      if(toId.trim().endsWith(",")){
        toId = toId.trim().substring(0,toId.trim().length() -1);
      }
    }else {
      return "";
    }
    String userId = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select USER_NAME from PERSON where SEQ_ID in( " + toId + ")";            
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        if(!"".equals(userId)){
          userId += ",";
        }
        userId += rs.getString("USER_NAME");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return userId;
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
    String sql =  "SELECT DISTINCT SMS_BODY.SEQ_ID, SMS_BODY.SEND_TIME,SMS_BODY.SMS_TYPE FROM oa_msg_body as SMS_BODY,SMS where SMS.BODY_SEQ_ID=SMS_BODY.seq_id  and FROM_ID=" + userId + " and  DELETE_FLAG IN(0,1) and " + dbDateF + " " ;
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
  
  public String getSendBoxBodyId(Connection conn ,int userId ,String whereStr) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "SELECT DISTINCT SMS_BODY.SEQ_ID  FROM oa_msg_body as SMS_BODY,SMS where SMS.BODY_SEQ_ID=SMS_BODY.seq_id  and FROM_ID=" + userId + " and  DELETE_FLAG IN(0,1) and " + dbDateF + " " ;
    if(whereStr != null && !"".equals(whereStr)){
      sql += whereStr;
    }
   
    return sql;
  }
  public int getSendCount(Connection conn ,int userId,String whereStr) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "select distinct SMS_BODY.SEQ_ID ,SMS_BODY.SEND_TIME FROM oa_msg_body as SMS_BODY,SMS WHERE SMS.BODY_SEQ_ID=SMS_BODY.seq_id  and FROM_ID=" + userId + " and  DELETE_FLAG!='2' and " + dbDateF + " " ;
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
  
  private String toSearchWhere(Map request,int type,boolean canOrder) throws Exception{
    String whereStr = "";
    String startDateStr = request.get("startDate") != null ? ((String[])request.get("startDate"))[0] : null;
    String endDateStr = request.get("endDate") != null ? ((String[])request.get("endDate"))[0] : null;
    String smsType = request.get("smsType") != null ? ((String[])request.get("smsType"))[0] : null;
    String content = request.get("content") != null ? YHDBUtility.escapeLike(((String[])request.get("content"))[0]) : null;
    String userIdStr = request.get("userId") != null ? ((String[])request.get("userId"))[0] : null;
    String orderBy = request.get("orderBy") != null ? ((String[])request.get("orderBy"))[0] : null;
    String orderBySeq = request.get("orderBySeq") != null ? ((String[])request.get("orderBySeq"))[0] : null;
    
    if(userIdStr != null && !"".equals(userIdStr)){
      if(userIdStr.trim().endsWith(",")){
        userIdStr = userIdStr.trim().substring(0, userIdStr.trim().length() - 1);
      }
      if(type == 1){
        whereStr += " and SMS_BODY.FROM_ID in(" + userIdStr + ")";
      } else if(type == 2){
        whereStr += " and SMS.TO_ID in(" + userIdStr + ")";
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
    whereStr += " and SMS_TYPE='" + smsType + "'";
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
}

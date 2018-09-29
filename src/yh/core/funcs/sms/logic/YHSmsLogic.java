package yh.core.funcs.sms.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
import yh.core.funcs.sms.data.YHSms;
import yh.core.funcs.sms.data.YHSmsBody;
import yh.core.funcs.system.ispirit.n12.org.logic.YHIsPiritLogic;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHSmsLogic {
  private static Logger log = Logger.getLogger(YHSmsLogic.class);
  
  public int totalPages;
  public List getToId(Connection conn, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    List idList = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "select TO_ID,DELETE_FLAG, REMIND_FLAG from SMS where BODY_SEQ_ID=" + seqId + "AND DELETE_FLAG in('0','1')";  
      //String queryStr = "select TO_ID,DELETE_FLAG, REMIND_FLAG from SMS where BODY_SEQ_ID=" + seqId+"AND TO_ID="+tr;
      rs = stmt.executeQuery(queryStr);
      idList = new ArrayList();
      while (rs.next()) {  
        idList.add(rs.getString("TO_ID"));
        //idList.add(rs.getString("DELETE_FLAG"));
        //idList.add(rs.getString("REMIND_FLAG"));
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return idList;
  }
  
  public List getToIdSearch(Connection conn, int seqId, String seqStr) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    List idList = null;
    try {
      stmt = conn.createStatement();
      //String queryStr = "select TO_ID,DELETE_FLAG, REMIND_FLAG from SMS where BODY_SEQ_ID=" + seqId;  
      String queryStr = "select TO_ID,DELETE_FLAG, REMIND_FLAG from SMS where BODY_SEQ_ID=" + seqId+"AND TO_ID='"+seqStr+"'";
      //System.out.println(queryStr);
      rs = stmt.executeQuery(queryStr);
      idList = new ArrayList();
      while (rs.next()) {  
        idList.add(rs.getString("TO_ID"));
        //idList.add(rs.getString("DELETE_FLAG"));
        //idList.add(rs.getString("REMIND_FLAG"));
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return idList;
  }
  
  public List getSearchlist(Connection conn, Object object) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    List idList = null;
    YHSmsBody sb = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "select SEQ_ID, FROM_ID, CONTENT, SEND_TIME, SMS_TYPE from oa_msg_body where SEQ_ID=" + object;            
      rs = stmt.executeQuery(queryStr);
      idList = new ArrayList();
      while (rs.next()) {  
        sb = new YHSmsBody();
        sb.setSeqId(rs.getInt("SEQ_ID"));
        sb.setFromId(rs.getInt("FROM_ID"));
        sb.setContent(rs.getString("CONTENT"));
        sb.setSendTime(rs.getDate("SEND_TIME"));
        sb.setSmsType(rs.getString("SMS_TYPE"));
        idList.add(sb);
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return idList;
  }
  
  public String getUserId(Connection conn, String toId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String userId = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select USER_NAME from PERSON where SEQ_ID = " + Integer.parseInt(toId);            
      rs = stmt.executeQuery(queryStr);
      if (rs.next()) {  
        userId = rs.getString("USER_NAME");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return userId;
  }
  
  public String getUserIdDialog(Connection conn, Object object) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String userId = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select USER_NAME from PERSON where SEQ_ID = " + object;            
      rs = stmt.executeQuery(queryStr);
      if (rs.next()) {  
        userId = rs.getString("USER_NAME");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return userId;
  }
  
  public void deleteAllSms(Connection conn, String idStrs) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String[] ids = idStrs.split(",");
      for (int i = 0; i < ids.length; i++) {
        //String queryStr = "delete from SMS where SEQ_ID = " + Integer.parseInt(ids[i]); 
        String queryStr = "update SMS set DELETE_FLAG='2' where SEQ_ID=" + Integer.parseInt(ids[i]);
        stmt.executeUpdate(queryStr);
      }  
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public int getTotalSmsNum(Connection conn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int totalNum = 0;
    try {
      stmt = conn.createStatement();
      String queryStr = "select count(*) from SMS ";            
      rs = stmt.executeQuery(queryStr);
      if (rs.next()) {  
        totalNum = rs.getInt(1);
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return totalNum;
  }
  
  public String getBodySeqIds(Connection conn, String toId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String delFlag = "0";
    String bodySeqIds = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select BODY_SEQ_ID from SMS where TO_ID='" + toId  +"' AND DELETE_FLAG="+delFlag; 
      //System.out.println("++++++++++++++++++++=====================queryStr:" + queryStr);
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {  
        bodySeqIds += rs.getInt("BODY_SEQ_ID") + ",";
      }
      //System.out.println("++++++++++++++++++++=====================bodySeqIds:" + bodySeqIds);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return bodySeqIds;
  }
  /**
   * 得到收发双方的通信记录的短息id
   * @param conn
   * @param toId
   * @return
   * @throws Exception
   */
  public String getBodySeqIds(Connection conn, int toId,int fromId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String delFlag = "0";
    String bodySeqIds = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select BODY_SEQ_ID from SMS where TO_ID IN(" + toId + "," + fromId + ") AND DELETE_FLAG=" + delFlag; 
      //System.out.println("++++++++++++++++++++=====================queryStr:" + queryStr);
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {  
        if(!"".equals(bodySeqIds)){
          bodySeqIds += ",";
        }
        bodySeqIds += rs.getInt("BODY_SEQ_ID");
      }
      //System.out.println("++++++++++++++++++++=====================bodySeqIds:" + bodySeqIds);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return bodySeqIds;
  }
  
  public YHSmsBody getSmsContent(Connection conn, int id) throws Exception {
    YHORM orm = new YHORM();
    YHSmsBody smsContent = null;
    try {
      smsContent = (YHSmsBody) orm.loadObjSingle(conn, YHSmsBody.class, id);
    }catch(Exception ex) {
      throw ex;
    }
    return smsContent;
  }
  
  public ArrayList<YHSms> getSmsByUserId(Connection conn, int toId,int type) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String bodyIds = "";
    YHORM orm = new YHORM();
    ArrayList<YHSmsBody> contentList = new ArrayList<YHSmsBody>();
    ArrayList<YHSms> result = new ArrayList<YHSms>();
    Date now = new Date();
    try {
      if(type == 1){
        bodyIds = getBodyIdByUser(conn, toId," AND DELETE_FLAG IN(0,2)");
      }else if(type == 2){
        bodyIds = getBodyIdByUser(conn, toId," AND DELETE_FLAG IN(0,2) AND REMIND_FLAG IN(1,2)");
      }
      if(bodyIds != null && !"".equals(bodyIds)){
        contentList = (ArrayList<YHSmsBody>) orm.loadListSingle(conn, YHSmsBody.class, new String[]{" SEQ_ID IN(" + bodyIds + ")" + "  ORDER BY SEND_TIME DESC"});
        for (YHSmsBody yhSmsBody : contentList){
          if(yhSmsBody.getSendTime().after(now)){
            continue;
          }
          Map filters = new HashMap();
          filters.put("BODY_SEQ_ID", yhSmsBody.getSeqId());
          filters.put("TO_ID", toId);
          YHSms sms= (YHSms) orm.loadObjSingle(conn, YHSms.class, filters);
          sms.addSmsBodyList(yhSmsBody);
          result.add(sms);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }
    return result;
  }
  
  public ArrayList<YHSms> getSmsBySearchIn(Connection conn, int toId,int type,Map request) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String bodyIds = "";
    YHORM orm = new YHORM();
    ArrayList<YHSmsBody> contentList = new ArrayList<YHSmsBody>();
    ArrayList<YHSms> result = new ArrayList<YHSms>();
    Date now = new Date();
    try {
      if(type == 1){
        bodyIds = getBodyIdByUser(conn, toId," AND DELETE_FLAG IN(0,2)");
      }else if(type == 2){
        bodyIds = getBodyIdByUser(conn, toId," AND DELETE_FLAG IN(0,2) AND REMIND_FLAG IN(1,2)");
      }
      if(bodyIds != null && !"".equals(bodyIds)){
        String whereStr = toSearchWhere(request, type);
        if(!"".equals(whereStr)){
          contentList = (ArrayList<YHSmsBody>) orm.loadListSingle(conn, YHSmsBody.class, new String[]{" SEQ_ID IN(" + bodyIds + ") " + whereStr});
        }else{
          contentList = (ArrayList<YHSmsBody>) orm.loadListSingle(conn, YHSmsBody.class, new String[]{" SEQ_ID IN(" + bodyIds + ")" + "  ORDER BY SEND_TIME DESC"});
        }
       
        for (YHSmsBody yhSmsBody : contentList){
          if(yhSmsBody.getSendTime().after(now)){
            continue;
          }
          Map filters = new HashMap();
          filters.put("BODY_SEQ_ID", yhSmsBody.getSeqId());
          filters.put("TO_ID", toId);
          YHSms sms= (YHSms) orm.loadObjSingle(conn, YHSms.class, filters);
          sms.addSmsBodyList(yhSmsBody);
          result.add(sms);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }
    return result;
  }
  public ArrayList<YHSmsBody> getSmsBodyBySearchOut(Connection conn, int toId,int type,Map request) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String bodyIds = "";
    YHORM orm = new YHORM();
    ArrayList<YHSmsBody> contentList = new ArrayList<YHSmsBody>();
    ArrayList<YHSms> result = new ArrayList<YHSms>();
    try {
      String whereStr = toSearchWhere(request, type);
      if(!"".equals(whereStr)){
        contentList = (ArrayList<YHSmsBody>) orm.loadListSingle(conn, YHSmsBody.class, new String[]{" FROM_ID=" + toId + "  " +  whereStr});
      }else{
        contentList = (ArrayList<YHSmsBody>) orm.loadListSingle(conn, YHSmsBody.class, new String[]{" FROM_ID=" + toId + " order by SEND_TIME DESC" });
      }
    }catch(Exception ex) {
      throw ex;
    }
    return contentList;
  }
  /**
   * 组装查询条件
   * @param request
   * @param type [1|2] [收件|发件]
   * @return
   * @throws Exception
   */
  private String toSearchWhere(Map request,int type) throws Exception{
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
        whereStr += " and FROM_ID in(" + userIdStr + ")";
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
    whereStr += " and CONTENT like '%" + content + "%'"  + YHDBUtility.escapeLike() ;
  }
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
    return whereStr;
  }
  /**
   * 根据TO_ID 得到相关条件下的短息正文ID
   * @param conn
   * @param toId
   * @return
   */
  public String getBodyIdByUser(Connection conn,int toId,String filters) throws Exception{
    String sql = "select distinct BODY_SEQ_ID FROM SMS WHERE TO_ID=" + toId;
    String result = "";
    ResultSet rs = null;
    Statement stmt = null;
    if(filters != null && !"".equals(filters)){
      sql += filters;
    }
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        if(!"".equals(result)){
          result += ",";
        }
        result += rs.getInt(1);
      }
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
  /**
   * 得到发件箱的邮件Id
   * @param conn
   * @param fromId
   * @param filters T0代表SMS表，T1代表SMS_BODY表
   * @return
   * @throws Exception
   */
  public String getBodyIdBySend(Connection conn,int fromId,String filters) throws Exception{
    String sql = " select "
       + " distinct T1.SEQ_ID " 
       + " from " 
       + " SMS T0 " 
       + " ,oa_msg_body T1 " 
       + " where " 
       + " T1.FROM_ID= " + fromId;
    if(filters != null && !"".equals(filters)){
      sql += filters;
    }
    String result = "";
    ResultSet rs = null;
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        if(!"".equals(result)){
          result += ",";
        }
        result += rs.getInt(1);
      }
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
  /**
   * 根据条件的到bodyId
   * @param conn
   * @param filters
   * @return
   * @throws Exception
   */
  public Map<Integer, Integer> getBodyIds(Connection conn,String filters) throws Exception{
    String sql = "select BODY_SEQ_ID ,SEQ_ID FROM SMS ";
    if(filters != null && !"".equals(filters)){
      sql += "where "+ filters;
    }
    Map<Integer, Integer> result = new HashMap<Integer, Integer>();
    ResultSet rs = null;
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        /*if(!"".equals(result)){
          result += ",";
        }*/
        result.put(rs.getInt(1), rs.getInt(2));
      }
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
  public ArrayList<YHSms> getSmsSeqId(Connection conn, int userId) throws Exception {
    YHSms smsContent = null;
    YHORM orm = new YHORM();
    ArrayList<YHSms> smsList = null;
    try {
      //String queryStr = "select SEQ_ID, FROM_ID, CONTENT, SEND_TIME from oa_msg_body where SEQ_ID=" + seqId;  
      //String sql = "select S2.BODY_SEQ_ID from oa_msg_body S1,SMS S2 where S1.FROM_ID="+userId+" AND S2.BODY_SEQ_ID = S1.SEQ_ID AND S2.DELETE_FLAG in('0','1') order by S1.SEND_TIME DESC";
      smsList = (ArrayList<YHSms>) orm.loadListSingle(conn, YHSms.class, new String[]{" FROM_ID=" + userId + " AND DELETE_FLAG in('0','1') ORDER BY SEQ_ID DESC"});
    }catch(Exception ex) {
      throw ex;
    }
    return smsList;
  }
  
  public ArrayList<YHSms> getSmsSeqIdSize(Connection conn, int bodyseqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHSms smsContent = null;
    List list = new ArrayList();
    ArrayList<YHSms> smsListSize = new ArrayList<YHSms>();
    try {
      stmt = conn.createStatement();
      String sql = "select S2.SEQ_ID from oa_msg_body S1,SMS S2 where S2.BODY_SEQ_ID = S1.SEQ_ID AND S2.BODY_SEQ_ID="+bodyseqId;
      rs = stmt.executeQuery(sql);
      while (rs.next()) { 
        smsContent = new YHSms();
        smsContent.setBodySeqId(rs.getInt("SEQ_ID"));
        //System.out.println(rs.getInt("SEQ_ID")+"ooooooo");
        smsListSize.add(smsContent);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return smsListSize;
  }
  
  public int getFirstPageNo() {
    return 1;
  }
  
  public int getLastPageNo() {
    return totalPages;
  }
  
  public int getPreviousPageNo(int pageNo){
    if (pageNo <= 1) {
      return 1;
    }
    return pageNo - 1;
  }
  
  public int getNextPageNo(int pageNo){
    if (pageNo >= totalPages) {
      return totalPages;
    }
    return pageNo + 1;
  }
  
  public int getButtomPageNo() {
    return totalPages;
  }
  
  public int getTotalPages(int totalSmsNo, int pageSize) {
    return totalPages = (totalSmsNo + pageSize - 1) / pageSize;
  }
  
  public int getSmsSeqId(Connection conn, int toId, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int smsSeqId = 0;
    try {
      stmt = conn.createStatement();
      String queryStr = "select SEQ_ID from SMS where TO_ID = " + toId + " and BODY_SEQ_ID = " + seqId;            
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){  
        smsSeqId = rs.getInt("SEQ_ID");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return smsSeqId;
  }
  
  
  /**
   * 通过body_seq_id和to_id索引所有seq_id,可能有重复,getSmsSeqId方法只能取一条会引起bug
   * @param conn
   * @param toId
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<Integer> getSmsSeqIds(Connection conn, int toId, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    List<Integer> list = new ArrayList<Integer>();
    try {
      stmt = conn.createStatement();
      String queryStr = "select SEQ_ID from SMS where TO_ID = " + toId + " and BODY_SEQ_ID = " + seqId;            
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
  
  public int getSmsBoSeqId(Connection conn, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int smsSeqId = 0;
    try {
      stmt = conn.createStatement();
      String queryStr = "select BODY_SEQ_ID from SMS where SEQ_ID = " + seqId;            
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){  
        smsSeqId = rs.getInt("BODY_SEQ_ID");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return smsSeqId;
  }
  /**
   * 得到删除位标记
   * @param conn
   * @param toId
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getdeleteFlag(Connection conn, int toId, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int smsSeqId = 0;
    String deleteFlag = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select SEQ_ID, DELETE_FLAG from SMS where TO_ID = " + toId + " and BODY_SEQ_ID = " + seqId;            
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){  
        deleteFlag = rs.getString("DELETE_FLAG");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return deleteFlag;
  }
/**
 * 得到删除为标记
 * @param conn
 * @param seqId
 * @return
 * @throws Exception
 */
  public String getdeleteFlagSeqId(Connection conn, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int smsSeqId = 0;
    String deleteFlag = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select DELETE_FLAG from SMS where SEQ_ID = " + seqId;            
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){  
        deleteFlag = rs.getString("DELETE_FLAG");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return deleteFlag;
  }
  /**
   * 得到提醒位标记
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getremindFlagSeqId(Connection conn, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int smsSeqId = 0;
    String remindFlag = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select REMIND_FLAG from SMS where SEQ_ID = " + seqId;            
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){  
        remindFlag = rs.getString("REMIND_FLAG");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return remindFlag;
  }
  
  /**
   * 得到提醒位标记
   * @param conn
   * @param toId
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getremindFlag(Connection conn, String toId, int seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int smsSeqId = 0;
    String remindFlag = "";
    try {
      stmt = conn.createStatement();
      String queryStr = "select SEQ_ID, REMIND_FLAG from SMS where TO_ID = '" + toId + "' and BODY_SEQ_ID = " + seqId;            
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){  
        remindFlag = rs.getString("REMIND_FLAG");
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return remindFlag;
  }
  
  
  public String getContent(Connection conn, String seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String contentDetail = "";
    try {
      String queryStr = "select CONTENT from oa_msg_body where SEQ_ID = " + Integer.parseInt(seqId);
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
  
  public String search(Connection dbConn, Object object) {
    String name = "";
    String str = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT SEQ_ID FROM oa_msg_body WHERE SEQ_ID =" + object;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        name = rs.getString("MANAGER");
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return name;
  }
  
  public int testGetConnection(Connection dbConn, Object fromId, String userIdToId, String sendTime) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int seqId = 0;
    try {            
      stmt = dbConn.createStatement();
      String sql = null;
      String dateFilter = YHDBUtility.getDateFilter("SEND_TIME", sendTime, "<=");
      sql = "select SEQ_ID from oa_msg_body where " + dateFilter+ "AND FROM_ID='" + fromId + "'AND FROM_ID='" + userIdToId + "' order by SEND_TIME DESC";
      //String[] bodyStrl = new String[]{"FROM_ID="+ms.get("fromId")+"AND FROM_ID='"+userIdToId+"' AND SEND_TIME<"+ms.get("sendTime")+"order by SEND_TIME DESC"};
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        seqId = rs.getInt("SEQ_ID");
        //System.out.println("seqID=====" + seqId);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    
    return seqId;
  }
  
//  public List<YHEmail> toSendBoxObj(Connection conn,String userId) throws Exception{
//    StringBuffer sub = new StringBuffer();
//    //主题，日期，附件，大小//
//
//    Map filters = new HashMap();
//    YHORM orm = new YHORM();
//    String[] strs = new String[]{"FROM_ID =" + userId ,"ORDER BY SEND_TIME DESC"};
//    List<YHEmailBody> eblist =  orm.loadListSingle(conn, YHEmailBody.class, strs);
//    //String[] strs = new String[]{"TO_ID ='" + userId + "'" ,"BOX_ID = " + boxId,"DELETE_FLAG in('0','3','5') ORDER BY BODY_ID DESC"};
//    ArrayList<YHEmail> temp = new ArrayList<YHEmail>();
//    int len = eblist.size();
//    for (int i = 0; i < len; i++){
//      YHEmailBody emb = eblist.get(i);
//      filters.put("BODY_ID", emb.getSeqId());
//      YHEmail eml = (YHEmail) orm.loadObjSingle(conn, YHEmail.class, filters);
//      if(!"2".equals(eml.getDeleteFlag()) && eml == null){
//          continue;
//      }
//      eml.addEmlb(emb);
//      temp.add(temp.size(), eml);
//    }
//    return temp;
//  }

  public ArrayList<YHSms> removeDuplicate(ArrayList<YHSms> list) {
    for (int i = 0; i < list.size(); i++) {
      YHSms tmp = list.get(i);
      for (int j = list.size() - 1; j > i; j--) {
        YHSms tmp2 = list.get(j);
        if (tmp.getBodySeqId() == tmp2.getBodySeqId()) {
          list.remove(j);
        }
      }
    }
    return list;
  }
  /**
   * 得到历史记录
   * @param conn
   * @param userId
   * @param fromId
   * @return
   * @throws Exception 
   */
  public String getBodyIdsForHis(Connection conn,int userId,int fromId,String dateFilter) throws Exception{
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    Timestamp date = YHUtility.parseTimeStamp(dateFilter);
    //String dateFilter = YHUtility.getCurDateTimeStr("SEND_TIME", dateStr, "<=");
    String sql = "SELECT distinct BODY_SEQ_ID FROM SMS T0,oa_msg_body T1 WHERE (((T0.TO_ID =" 
       + userId + ") AND T1.FROM_ID=" + fromId + ") OR " 
       + "((T0.TO_ID =" + fromId + ") AND T1.FROM_ID=" + userId + ")) AND  T0.BODY_SEQ_ID = T1.SEQ_ID AND T1.SMS_TYPE='0' AND  SEND_TIME <=?";
   
    try{
      ps = conn.prepareStatement(sql);
      ps.setTimestamp(1, date);
      rs = ps.executeQuery();
      int i = 0;
      while(rs.next()){
        if(i > 100){
          break;
        }
        if(!"".equals(result)){
          result += ",";
        }
        result += rs.getInt(1);
        i ++ ;

      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 转换成json数据
   * @param sblist
   * @param toId
   * @return
   */
  public StringBuffer toHistoryData(ArrayList<YHSmsBody> sblist,int toId){
    StringBuffer fields = new StringBuffer();
    StringBuffer sb = new StringBuffer("[");
    int limit = (sblist.size() >= 20) ? 20 : sblist.size(); 
    for (int i = 0 ;i < limit ;i ++){
      YHSmsBody yhSmsBody = sblist.get(i);
      String content = yhSmsBody.getContent();
      content = YHUtility.encodeSpecial(content);
      StringBuffer field = new StringBuffer();
      if(toId == yhSmsBody.getFromId()){
        field.append("{toId:\"").append(toId).append("\",");
      }else{
        field.append("{fromId:\"").append(yhSmsBody.getFromId()).append("\",");
      }
      field.append("content:\"").append(content).append("\",");
      field.append("seqId:\"").append(yhSmsBody.getSeqId()).append("\",");
      field.append("sendTime:\"").append(YHUtility.getDateTimeStr(yhSmsBody.getSendTime())).append("\"}");
      if(!"".equals(fields.toString())){
        fields.append(",");
      }
      fields.append(field);
    }
    sb.append(fields).append("]");
    return sb;
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
    String sql = "SELECT COUNT(*) FROM SMS T1 ,oa_msg_body T0 WHERE TO_ID=" + userId + " AND REMIND_FLAG = '1' AND  T1.BODY_SEQ_ID= T0.SEQ_ID  " +
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
  
  
  public int isRemindNew(int userId) throws Exception{
    int result = 0;
    String flags="";
    flags=YHIsPiritLogic.checkUserSms(userId+"");
    if(!YHUtility.isNullorEmpty(flags)){
      result=Integer.parseInt(flags);
    }
    return result;
  }
  
  public int isRemindNew1(int userId) throws Exception{
    int result = 0;
    String flags="";
    flags=YHIsPiritLogic.checkUserMessage(userId+"");
    if(!YHUtility.isNullorEmpty(flags)){
      result=Integer.parseInt(flags);
    }
    
    
    return result;
  }
  
  public int isRemindNew2(int userId) throws Exception{
    int result = 0;
    result=this.isRemindNew(userId);
    if(result<=0){
      result=this.isRemindNew1(userId);
    }
    
    return result;
  }
  
  /**
   * 是否有为提醒的邮件

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public int isRemind1(Connection conn,int userId) throws Exception{
    int result = 0;
    String dateFiler = YHDBUtility.getDateFilter("T0.SEND_TIME", YHUtility.getDateTimeStr(new Date()), "<=");
    String dbDateFremind = YHDBUtility.getDateFilter("T1.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql = "SELECT COUNT(*) FROM SMS T1 ,oa_msg_body T0 WHERE TO_ID=" + userId + " AND REMIND_FLAG = '1' AND  T1.BODY_SEQ_ID= T0.SEQ_ID  " +
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
    if(result==0){
            sql = "SELECT COUNT(*) FROM oa_message T1 ,oa_message_body T0 WHERE TO_ID=" + userId + " AND REMIND_FLAG = '1' AND  T1.BODY_SEQ_ID= T0.SEQ_ID  " +
            "and DELETE_FLAG in (0, 2) " +
            "AND " + dateFiler +
            " AND (T1.REMIND_TIME IS NULL OR " + dbDateFremind + ")";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
              int count = rs.getInt(1);
              result = (count > 0) ? count : 0;
            } 
      }
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public void getIn(StringBuffer sb ,String fieldName , String id) {
    int b = 1000;
    String[] ids = id.split(",");
    if (ids.length < b) {
      sb.append(fieldName + " in (" + id + ") ");
    } else {
      String id1000 = "";
      String newId = "";
      for (int i = 0 ;i < b ; i++) {
        id1000 += ids[i] + ",";
      } 
      for (int i = b ;i < ids.length ; i++) {
        newId += ids[i] + ",";
      } 
      if (id1000.endsWith(",")) {
        id1000 = id1000.substring( 0 , id1000.length() - 1);
      }
      if (newId.endsWith(",")) {
        newId = newId.substring(0 , newId.length() - 1);
      }
      sb.append(fieldName + " in (" + id1000 + ") or ");
      this.getIn(sb, fieldName, newId);
    }
  }
  public String getIn(String fieldName , String id) {
    StringBuffer sb = new StringBuffer();
    this.getIn(sb, fieldName, id);
    return sb.toString();
  }
  public static void main(String[] args) {
    YHSmsLogic logic = new YHSmsLogic();
    String ss = logic.getIn("aaa", "2,33,4,5,3,2,2,3,3,3,3,3,3,3");
    System.out.println(ss);
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
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String ss = "SEQ_ID IN (" + seqId + ")";
    if ("oracle".equals(dbms)) {
      ss = this.getIn("SEQ_ID", seqId);
    }
    String sql = "UPDATE " + tableName + " SET " + field + " = '" + value + "'" + " WHERE  " + ss ;
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
        if (!YHUtility.isNullorEmpty(seqId)) {
          updateFlag(conn,"REMIND_FLAG",0 + "",seqId,"SMS");
        }
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
    if (!YHUtility.isNullorEmpty(seqId)) {
      updateFlag(conn,"DELETE_FLAG",deletbit + "",seqId,"SMS");
    }
    return bool;
  }
  /**
   * 删除邮件
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
      ArrayList<YHDbRecord> db = findFlag(conn, "SMS", fields, filters);
      for (YHDbRecord dbRecord : db) {
        int seqId = YHUtility.cast2Long(dbRecord.getValueByName("SEQ_ID")).intValue();
        String deleteFlag =(String) dbRecord.getValueByName("DELETE_FLAG");
        String readFlag = (String) dbRecord.getValueByName("REMIND_FLAG");
        int toId = YHUtility.cast2Long(dbRecord.getValueByName("TO_ID")).intValue();
        //如果
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
        deleteAll(conn, seqIdArr, "SMS");
      }
    } catch (Exception e){
      throw e;
    }
    if(isDe){
      deleteAll(conn,String.valueOf(bodyId), "oa_msg_body");
    }
    return isDe;
  }
  /**
   * 
   * @param conn
   * @param bodyId
   * @param isform
   * @param userId
   * @throws Exception
   */
  public void doDelSmsByUser(Connection conn,int bodyId,String isform,int userId,int toUserId) throws Exception{
    String[] fields = new String[]{"SEQ_ID","DELETE_FLAG","REMIND_FLAG","TO_ID"};
    String[] filters = new String[]{"BODY_SEQ_ID = " + bodyId};
    boolean isDe = true;
    String seqIdArr = "";
    try{
      ArrayList<YHDbRecord> db = findFlag(conn, "SMS", fields, filters);
      for (YHDbRecord dbRecord : db) {
        int seqId = YHUtility.cast2Long(dbRecord.getValueByName("SEQ_ID")).intValue();
        String deleteFlag =(String) dbRecord.getValueByName("DELETE_FLAG");
        String readFlag = (String) dbRecord.getValueByName("REMIND_FLAG");
        int toId = YHUtility.cast2Long(dbRecord.getValueByName("TO_ID")).intValue();
        //如果
        boolean b = false;
        if(toUserId == toId && "2".equals(isform)){
          b = isCanDeleteSms(conn, isform, seqId + "", deleteFlag, readFlag);
        }
        if(b){
          deleteAll(conn, seqId + "", "SMS");
        }
        isDe = isDe && b;
        if(!"".equals(seqIdArr)){
          seqIdArr += ",";
        }
        seqIdArr += seqId;
      }
      if(isDe){
        //删除所有邮件
        deleteAll(conn, bodyId + "", "oa_msg_body");
      }
    } catch (Exception e){
      throw e;
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
    * 短息快捷操作，根据条件的到邮件的正文ID
    * @param conn
    * @param userId 用户ID
    * @param shortCutType delType为1时 1删除所有收件箱信息 、2删除所有已读短息信息
    * delType为2时 1表示删除所有发件箱短息、2表示删除所有已提醒收件人短息、3删除所有收件人已删除的邮件
    * @param delType 1代表收件人，2代表发件人
    * @return
   * @throws Exception 
    */
  public String getBodyIdFShortCut(Connection conn ,int userId,int shortCutType,int delType) throws Exception{
    String result = "";
    try{
      if(delType == 1){
        switch (shortCutType){
          case 1:
            result = getBodyIdByUser(conn, userId, " AND DELETE_FLAG IN(0,2)");
            break;
          case 2:
            result = getBodyIdByUser(conn, userId, " AND REMIND_FLAG = 0 AND DELETE_FLAG IN(0,2)");
            break;
        }
      }else if(delType == 2){
        switch (shortCutType){
          case 1:
            result = getBodyIdBySend(conn, userId, " AND T1.SEQ_ID = T0.BODY_SEQ_ID AND T0.DELETE_FLAG IN(0,1)");
            break;
          case 2:
            result = getBodyIdBySend(conn, userId, " AND T1.SEQ_ID = T0.BODY_SEQ_ID AND T0.REMIND_FLAG IN(0,2) AND T0.DELETE_FLAG IN(0,1)");
            break;
          case 3:
            result =  getBodyIdBySend(conn, userId, " AND T1.SEQ_ID = T0.BODY_SEQ_ID AND T0.DELETE_FLAG=1");
            break;
        }
      }
    } catch (Exception e){
      throw e;
    }
    return result;
  } 
  /**
   * 取得发件箱列表
   * @param conn
   * @return
   * @throws Exception 
   */
  public StringBuffer getSendListData(Connection conn,int userId,Map request) throws Exception{
    StringBuffer sub = new StringBuffer();
    String sql =  "select "
      + "T0.SEQ_ID,"
      + "SUBJECT," 
      + "ATTACHMENT_NAME,"
      + "SEND_TIME," 
      + "ENSIZE " 
      + " from " 
      + " oa_msg_body T0" 
      + " where " 
      + " FROM_ID ='" + userId + "'" 
      + " AND NOT T0.SEQ_ID in(select DISTINCT T1.BODY_ID FROM oa_email T1) ";
    String query = " order by T0.SEND_TIME  desc";
    sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    sub.append(pageDataList.toJson());
    return sub;
  }
  
  public void updateFalg(Connection conn, int userId, int bodyId) throws Exception{
    String sql = "update SMS set REMIND_FLAG = '0' where BODY_SEQ_ID= " + bodyId + " AND TO_ID=" + userId;
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
  
  public String getPanelInBox(Connection conn,Map request,int userId,int pageNo,int pageSize) throws Exception{
    YHSmsTestLogic smsLogic = new YHSmsTestLogic();
    YHPageDataList data = smsLogic.toInBoxJson(conn, request, userId,pageNo,pageSize,false);
     return data.toJson();
  }
  
  public String getRemindInBox(Connection conn,int userId) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String dbDateFremind = YHDBUtility.getDateFilter("SMS.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "select " +
        "SMS_BODY.SEQ_ID," +
        "SMS_BODY.FROM_ID," +
        "SMS_BODY.SMS_TYPE," +
        "SMS_BODY.CONTENT," +
        "SMS_BODY.SEND_TIME," +
        "SMS_BODY.REMIND_URL," +
        "SMS.REMIND_TIME," +
        "SMS.SEQ_ID" +
        " FROM " +
        "SMS," +
        " oa_msg_body as SMS_BODY" +
        " WHERE " +
        " SMS.BODY_SEQ_ID = SMS_BODY.SEQ_ID" +
        " AND SMS.TO_ID=" + userId +
        " AND DELETE_FLAG IN(0,2) " +
        " AND " + dbDateF  +
        " AND (SMS.REMIND_TIME IS NULL OR " + dbDateFremind + ")" +
        " AND REMIND_FLAG IN(1,2)";
    sql += " ORDER BY SMS.REMIND_TIME DESC,SEND_TIME DESC";
    StringBuffer sb = new StringBuffer();
    String smsIds = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int seqId = rs.getInt(1);
       
        String fromId = rs.getString(2);
        String smsType = rs.getString(3);
        String content = rs.getString(4);
        Date sendTime = rs.getTimestamp(5);
        String remindUrl = rs.getString(6);
        Date remindTime = rs.getTimestamp(7);
        int smsId = rs.getInt(8);
        if(remindTime != null){
          sendTime = remindTime;
        }
        if(remindUrl == null){
          remindUrl = "";
        }
        if(!"".equals(sb.toString())){
          sb.append(",");
        }      
        sb.append("{");
        sb.append("seqId:\"" + seqId + "\"");
        sb.append(",fromId:\"" + fromId + "\"");
        sb.append(",smsType:\"" + smsType + "\"");
        sb.append(",content:\"" + YHUtility.encodeSpecial(content) + "\"");
        sb.append(",sendTime:\"" + YHUtility.getDateTimeStr(sendTime) + "\"");
        sb.append(",remindUrl:\"" + YHUtility.encodeSpecial(remindUrl) + "\"");
        sb.append("}");
        if(!"".equals(smsIds)){
          smsIds += ",";
        }
        smsIds += smsId;
      }
      if(!"".equals(smsIds)){
        updateFlag(conn, "REMIND_FLAG", "2", smsIds, "SMS");
      }
     
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return "[" + sb.toString() + "]";
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
    YHSmsTestLogic smsLogic = new YHSmsTestLogic();
    YHPageDataList data = smsLogic.toSendBoxJson(conn, request, userId, pageNo, pageSize, false);
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
      sb.append("bodyId:\"" + rec.getValueByName("smsBodyId") + "\"");
      sb.append(",fromId:\"" + rec.getValueByName("fromId") + "\"");
      sb.append(",smsType:\"" + rec.getValueByName("smsType") + "\"");
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
   * @param bodyId
   * @return
   * @throws Exception
   */
  public String getToIdByBodyId(Connection conn, int bodyId) throws Exception{
    String sql = "SELECT TO_ID FROM SMS WHERE BODY_SEQ_ID = " + bodyId;
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
  /**
   * 
   * @param conn
   * @param bodyId
   * @return
   * @throws Exception
   */
  public String getStatusByBodyId(Connection conn, int bodyId) throws Exception{
    String sql = "SELECT DELETE_FLAG,REMIND_FLAG FROM SMS WHERE BODY_SEQ_ID = " + bodyId;
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean isRead = true;
    boolean isDel = true;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
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
  
  public String getSmsTypeDesc(Connection conn,int smsType,String code) throws Exception{
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
  
  public String getPersonInfo(Connection conn,int userId) throws Exception{
    YHORM orm = new YHORM();
    YHPerson person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, userId);
    String result = "";
    if(person != null){
      String deptName = getUserDeptName(conn, person.getDeptId());
      result = "{userId:" + userId + ",userName:\""+person.getUserName()+"\",deptId:" + person.getDeptId() + ",deptName:\"" + deptName + "\"}";
    }
    return result;
  }
  
  public String getUserDeptName(Connection conn,int deptId) throws Exception{
    YHORM orm = new YHORM();
    YHDepartment dept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, deptId);
    if(dept == null){
      return "";
    }
    return dept.getDeptName();
  }
}

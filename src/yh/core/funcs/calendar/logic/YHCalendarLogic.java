package yh.core.funcs.calendar.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDBMapUtil;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHCalendarLogic {
  private static Logger log = Logger.getLogger(YHCalendarLogic.class);
  public int addCalendar(Connection dbConn,YHCalendar calendar) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, calendar);
    //新建后返回最大的SEQ—Id
    return getMaSeqId(dbConn,"CALENDAR");
  }
  public static int getMaSeqId(Connection dbConn,String tableName)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int maxSeqId = 0;
    String sql = "select max(SEQ_ID) as SEQ_ID from " + YHDBMapUtil.getMapDBName(tableName);
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       maxSeqId = rs.getInt("SEQ_ID");
     }
      
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maxSeqId;
  }
  public List<YHCalendar>  selectCalendarByDay(Connection dbConn, String userIds,String dateTime1,String dateTime2 ,String endTime1,String endTime2,String status) throws Exception {    
    List<YHCalendar> calendarList = new ArrayList<YHCalendar>(); 
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Statement stmt = null;
    ResultSet rs = null;
    if(userIds.trim().equals("")){
      return calendarList;
    }
    String newUserIds = "";
    if(!userIds.trim().equals("")){
      String[] userIdArray = userIds.split(",");
      for (int i = 0; i < userIdArray.length; i++) {
        newUserIds = newUserIds + "'" + userIdArray[i] + "',";
      }
      if(userIdArray.length>0){
        newUserIds  = newUserIds.substring(0, newUserIds.length()-1);
      }
    }
    String sql = "select *from oa_schedule WHERE USER_ID in(" + newUserIds +") and "+dateTime2+ " and " + endTime2 ;
    String dateStr = dateFormat.format(new Date());
    //System.out.println(dateStr);
    if(status.equals("1")){
      String temp = YHDBUtility.getDateFilter("CAL_TIME", dateStr, ">=");
      sql = sql + " and " + temp + " and OVER_STATUS='0'";
    }
    if(status.equals("2")){
      String temp1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr, "<=");
      String temp2 = YHDBUtility.getDateFilter("END_TIME", dateStr, ">=");
      sql = sql + " and " +  temp1 + " AND " + temp2 + " and OVER_STATUS='0'";
    }
    if(status.equals("3")){
      String temp = YHDBUtility.getDateFilter("END_TIME", dateStr, "<=");
      sql = sql + " and " + temp + " and OVER_STATUS='0'";
    }
    if(status.equals("4")){
      sql = sql + " and " + " OVER_STATUS='1'";
    }
    sql = sql + " order by CAL_TIME";
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        YHCalendar calendar = new YHCalendar();
        calendar.setSeqId(rs.getInt("SEQ_ID"));
        calendar.setUserId(rs.getString("USER_ID"));
        calendar.setCalType(rs.getString("CAL_TYPE"));
        calendar.setCalTime(dateFormat.parse(rs.getString("CAL_TIME")));
        calendar.setEndTime(dateFormat.parse(rs.getString("END_TIME"))); 
        calendar.setContent(rs.getString("CONTENT"));
        calendar.setCalLevel(rs.getString("CAL_LEVEL"));
        calendar.setOverStatus(rs.getString("OVER_STATUS"));
        calendar.setManagerId(rs.getString("MANAGER_ID"));
        calendarList.add(calendar);
      }  
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
  } 
    return calendarList;
  }
  public List<Map<String,String>>  selectCalendarByWeek(Connection dbConn, int userId,String dateTime1,String dateTime2 ,String endTime1,String endTime2,String status) throws Exception {     
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  List<Map<String,String>> calendarList = new ArrayList<Map<String,String>>();
  String sql = "select *from oa_schedule WHERE USER_ID ='" + userId +"' and "+dateTime2+ " and " + endTime2 ;
  String dateStr = dateFormat.format(new Date());
  //System.out.println(dateStr);
  if(status.equals("1")){
    String temp = YHDBUtility.getDateFilter("CAL_TIME", dateStr, ">=");
    sql = sql + " and " + temp + " and OVER_STATUS='0'";
  }
  if(status.equals("2")){
    String temp1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr, "<=");
    String temp2 = YHDBUtility.getDateFilter("END_TIME", dateStr, ">=");
    sql = sql + " and " +  temp1 + " AND " + temp2 + " and OVER_STATUS='0'";
  }
  if(status.equals("3")){
    String temp = YHDBUtility.getDateFilter("END_TIME", dateStr, "<=");
    sql = sql + " and " + temp + " and OVER_STATUS='0'";
  }
  if(status.equals("4")){
    sql = sql + " and " + " OVER_STATUS='1'";
  }
  //System.out.println(sql);
  Statement stmt = null;
  ResultSet rs = null;
  try {
    stmt = dbConn.createStatement();
    rs = stmt.executeQuery(sql);
    while(rs.next()){
      Map<String,String> map = new HashMap<String,String>();
      YHCalendar calendar = new YHCalendar();
      calendar.setSeqId(rs.getInt("SEQ_ID"));
      calendar.setUserId(rs.getString("USER_ID"));
      calendar.setCalType(rs.getString("CAL_TYPE"));
      calendar.setCalTime(dateFormat.parse(rs.getString("CAL_TIME")));
      calendar.setEndTime(dateFormat.parse(rs.getString("END_TIME"))); 
      calendar.setContent(rs.getString("CONTENT"));
      calendar.setCalLevel(rs.getString("CAL_LEVEL"));
      calendar.setOverStatus(rs.getString("OVER_STATUS"));
      calendar.setManagerId(rs.getString("MANAGER_ID"));
      //calendarList.add(calendar);
    }  
  }catch(Exception ex) {
     throw ex;
  }finally {
    YHDBUtility.close(stmt, rs, log);
} 
  return calendarList;
}
  public List<YHCalendar>  selectCalendarByList(Connection dbConn, String[] str) throws Exception {
    List<YHCalendar> calendarList = new ArrayList<YHCalendar>(); 
    YHORM orm = new YHORM();
    calendarList = orm.loadListSingle(dbConn, YHCalendar.class, str);
    return calendarList;
  }
  public void deleteCalendarById(Connection dbConn,int seqId) throws Exception{
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHCalendar.class, seqId);
  }
  public void deleteCalendar(Connection dbConn,String seqIds) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_schedule WHERE SEQ_ID in (" + seqIds + ")" ;
    //System.out.println(sql);
    try {
        stmt = dbConn.createStatement();
        stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    } 
  }
  public void updateStatusById(Connection dbConn,Map<String,String> map) throws Exception{
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, "calendar",map);
  }
  public YHCalendar selectCalendarById(Connection dbConn,int seqId) throws Exception{
    YHORM orm = new YHORM();
   YHCalendar calendar = (YHCalendar) orm.loadObjSingle(dbConn, YHCalendar.class, seqId);
   return calendar;
  }
  public void updateCalendar(Connection dbConn,YHCalendar calendar) throws Exception{
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn,calendar);
  }

  public List<YHCalendar> selectCalendarByStatus(Connection dbConn, int userId,String status) throws Exception {
    List<YHCalendar> calendarList = new ArrayList<YHCalendar>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    if (status.equals("0")) {
      String str[] = {"USER_ID="+userId+" order by CAL_TIME DESC"};
      calendarList = selectCalendarByList(dbConn, str);
     } else {
      Statement stmt = null;
      ResultSet rs = null;
      String sql = "select *from oa_schedule WHERE USER_ID ='" + userId + "' and ";
      String dateStr = dateFormat.format(new Date());
      //System.out.println(dateStr);
      if(status.equals("1")){
        String temp = YHDBUtility.getDateFilter("CAL_TIME", dateStr, ">=");
        sql = sql + temp + " and OVER_STATUS='0'";
      }
      if(status.equals("2")){
        String temp1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr, "<=");
        String temp2 = YHDBUtility.getDateFilter("END_TIME", dateStr, ">=");
        sql = sql + temp1 + " AND " + temp2 + " and OVER_STATUS='0'";
      }
      if(status.equals("3")){
        String temp = YHDBUtility.getDateFilter("END_TIME", dateStr, "<=");
        sql = sql + temp + " and OVER_STATUS='0'";
      }
      if(status.equals("4")){
        sql = sql + " OVER_STATUS='1'";
      }
      sql = sql + " order by CAL_TIME desc";
      //System.out.println(sql);
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
          YHCalendar calendar = new YHCalendar();
          calendar.setSeqId(rs.getInt("SEQ_ID"));
          calendar.setUserId(rs.getString("USER_ID"));
          calendar.setCalType(rs.getString("CAL_TYPE"));
          calendar.setCalTime(dateFormat.parse(rs.getString("CAL_TIME")));
          calendar.setEndTime(dateFormat.parse(rs.getString("END_TIME")));   
          calendar.setContent(rs.getString("CONTENT"));
          calendar.setCalLevel(rs.getString("CAL_LEVEL"));
          calendar.setOverStatus(rs.getString("OVER_STATUS"));
          calendar.setManagerId(rs.getString("MANAGER_ID"));
          calendarList.add(calendar);
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stmt, rs, log);
      }
    }
    return calendarList;
  }
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,int userId,String status) throws Exception{
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String sql = "select SEQ_ID,CAL_TIME,END_TIME,CONTENT,CAL_TYPE,OVER_STATUS,CAL_LEVEL,MANAGER_ID,USER_ID from oa_schedule WHERE USER_ID ='" + userId +"'" ;
      String dateStr = dateFormat.format(new Date());
      //System.out.println(dateStr);
      if(status.equals("1")){
        String temp = YHDBUtility.getDateFilter("CAL_TIME", dateStr, ">=");
        sql = sql + " and "+ temp + " and OVER_STATUS='0'";
      }
      if(status.equals("2")){
        String temp1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr, "<=");
        String temp2 = YHDBUtility.getDateFilter("END_TIME", dateStr, ">=");
        sql = sql + " and "+ temp1 + " AND " + temp2 + " and OVER_STATUS='0'";
      }
      if(status.equals("3")){
        String temp = YHDBUtility.getDateFilter("END_TIME", dateStr, "<=");
        sql = sql + " and "+ temp + " and OVER_STATUS='0'";
      }
      if(status.equals("4")){
        sql = sql + " and OVER_STATUS='1'";
      }
      sql = sql + " order by CAL_TIME desc";
      //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  public List<YHCalendar>  selectCalendarByTerm(Connection dbConn,String userId,String sendTimeMin,String sendTimeMax,String calLevel,String calType,String overStatus,String content) throws Exception{
    List<YHCalendar> calendarList = new ArrayList<YHCalendar>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    long dateTime = date.getTime();
    String dateStr = dateFormat.format(new Date());
    //System.out.println(dateStr);
    String newUserIds = "";
    if(userId.trim().equals("")){
      return calendarList;
    }
    if(!userId.trim().equals("")){
      String[] userIdArray = userId.split(",");
      for (int i = 0; i < userIdArray.length; i++) {
        newUserIds = newUserIds + "'" + userIdArray[i] + "',";
      }
      if(userIdArray.length>0){
        newUserIds  = newUserIds.substring(0, newUserIds.length()-1);
      }
    }
    String sql = "select *from oa_schedule where USER_ID in (" + newUserIds + ")";
    if(!sendTimeMin.equals("")){     
      sql = sql + " and " + YHDBUtility.getDateFilter("END_TIME", sendTimeMin, ">=");
    }
    if(!sendTimeMax.equals("")){  
      sendTimeMax = sendTimeMax + " 23:59:59";
      sql = sql + " and " + YHDBUtility.getDateFilter("CAL_TIME", sendTimeMax, "<=");
    }
    if(!calLevel.equals("")){
      if(calLevel.equals("0")){
        sql = sql + " and " + " (CAL_LEVEL='" + calLevel +"' or CAL_LEVEL = ' ')" ;
      }else{
        sql = sql + " and " + "CAL_LEVEL='" + calLevel +"'";
      }
    }
    if(!calType.equals("")){
      sql = sql + " and " + "CAL_TYPE='" + calType+"'";
    }
    if(!overStatus.equals("")){
      if(overStatus.equals("1")){
        String temp = YHDBUtility.getDateFilter("CAL_TIME", dateStr, ">=");
        sql = sql + " and " + temp + " and OVER_STATUS='0'";
      }
      if(overStatus.equals("2")){
        String temp1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr, "<=");
        String temp2 = YHDBUtility.getDateFilter("END_TIME", dateStr, ">=");
        sql = sql + " and " + temp1 + " AND " + temp2 + " and OVER_STATUS='0'";
      }
      if(overStatus.equals("3")){
        String temp = YHDBUtility.getDateFilter("END_TIME", dateStr, "<=");
        sql = sql + " and " + temp + " and OVER_STATUS='0'";
      }
      if(overStatus.equals("4")){
        sql = sql + " and " + " OVER_STATUS='1'";
      }
    }
    if(!content.equals("")){
      content = YHDBUtility.escapeLike(content);
      sql = sql + " and " + "CONTENT like '%" + content+ "%' " + YHDBUtility.escapeLike();
    }
    sql = sql + " order by CAL_TIME";
    //System.out.println(sql);
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        YHCalendar calendar = new YHCalendar();
        calendar.setSeqId(rs.getInt("SEQ_ID"));
        calendar.setUserId(rs.getString("USER_ID"));
        calendar.setCalType(rs.getString("CAL_TYPE"));
        if(rs.getString("CAL_TIME")!=null&&!rs.getString("CAL_TIME").equals("")){
          calendar.setCalTime(dateFormat.parse(rs.getString("CAL_TIME")));
        }
        if(rs.getString("END_TIME")!=null&&!rs.getString("END_TIME").equals("")){
          calendar.setEndTime(dateFormat.parse(rs.getString("END_TIME")));  
        }
        calendar.setContent(rs.getString("CONTENT"));
        calendar.setCalLevel(rs.getString("CAL_LEVEL"));
        calendar.setOverStatus(rs.getString("OVER_STATUS"));
        calendar.setManagerId(rs.getString("MANAGER_ID"));
        calendarList.add(calendar);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return calendarList;
  }
  public List<YHPerson> selectPerson(Connection dbConn,String[] str) throws Exception{
    List<YHPerson> personList = new ArrayList<YHPerson>();
    YHORM orm = new YHORM();
    personList = orm.loadListSingle(dbConn, YHPerson.class, str);
    return personList;
  }
  public Map<String,String> selectPersonById(Connection dbConn,int seqId) throws Exception{
    Map<String,String> person = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select up.PRIV_NAME as PRIV_NAME,d.DEPT_NAME as DEPT_NAME,p.USER_NAME as USER_NAME,p.USER_PRIV as USRE_PRIV,p.DEPT_ID as DEPT_ID from PERSON p left outer join oa_department d on p.DEPT_ID = d.SEQ_ID left outer join USER_PRIV up on p.USER_PRIV = cast(up.SEQ_ID as varchar(20)) where p.SEQ_ID= " + seqId;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
         person.put("privName", rs.getString("PRIV_NAME"));
         person.put("deptName", rs.getString("DEPT_NAME"));
         person.put("userName", rs.getString("USER_NAME"));
         person.put("deptId", rs.getString("DEPT_ID"));
         person.put("userPriv", rs.getString("USER_PRIV"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return person;
  }
}

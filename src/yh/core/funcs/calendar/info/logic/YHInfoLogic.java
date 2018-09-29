package yh.core.funcs.calendar.info.logic;

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

import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.calendar.data.YHTask;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHInfoLogic {
  private static Logger log = Logger.getLogger(YHInfoLogic.class);
  public List<YHCalendar>  selectCalendarByDept(Connection dbConn, String userIds,String dateTime1,String dateTime2 ,String endTime1,String endTime2,String status) throws Exception {     
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<YHCalendar> calendarList = new ArrayList<YHCalendar>();
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
    //System.out.println(sql);
    Statement stmt = null;
    ResultSet rs = null;
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
      /*  Map<String,String> map = new HashMap<String,String>();
        map.put("seqId",rs.getString("SEQ_ID"));
        map.put("userId",rs.getString("USER_ID"));
        map.put("calType",rs.getString("CAL_TYPE"));
        map.put("calTime",rs.getString("CAL_TIME"));
        map.put("endTime",rs.getString("END_TIME")); 
        map.put("content",rs.getString("CONTENT"));
        map.put("calLevel",rs.getString("CAL_LEVEL"));
        map.put("overStatus",rs.getString("OVER_STATUS"));
        map.put("managerId",rs.getString("MANAGER_ID"));
        calendarList.add(map);*/
      }  
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
  } 
    return calendarList;
  }
  public List<YHTask> selectTask(Connection dbConn,String[] str) throws Exception{
    List<YHTask> taskList = new ArrayList<YHTask>();
    YHORM orm = new YHORM();
    taskList = orm.loadListSingle(dbConn, YHTask.class, str);
    return taskList;
  }
  public List<Map<String,String>> selectTask(Connection dbConn,String userIds) throws Exception{
    List<Map<String,String>> taskList = new ArrayList<Map<String,String>>();
    Statement stmt = null;
    ResultSet rs = null;
    if(userIds.equals("")){
      return taskList;
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
    String sql = "select *from TASK where USER_ID in(" + newUserIds + ") order by TASK_NO";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map = new HashMap<String,String>();
        map.put("seqId",rs.getString("SEQ_ID"));
        map.put("userId",rs.getString("USER_ID"));
        map.put("content",rs.getString("CONTENT"));
        map.put("beginDate",rs.getString("BEGIN_DATE"));
        map.put("endDate",rs.getString("END_DATE"));
        map.put("managerId",rs.getString("MANAGER_ID"));
        map.put("taskType",rs.getString("TASK_TYPE"));
        map.put("taskNo",rs.getString("TASK_NO"));
        map.put("color", rs.getString("COLOR"));
        map.put("subject",rs.getString("SUBJECT"));
        map.put("rate", rs.getString("RATE"));
        map.put("finishTime", rs.getString("FINISH_TIME"));
        map.put("totalTime",rs.getString("TOTAL_TIME"));
        map.put("useTime", rs.getString("USE_TIME"));
        map.put("calId", rs.getString("CAL_ID"));
        map.put("important",rs.getString("IMPORTANT"));
        map.put("taskStatus", rs.getString("TASK_STATUS"));
        map.put("editTime", rs.getString("EDIT_TIME"));
        taskList.add(map);
      }  
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return taskList;
  }
  public static String getUserIds(String deptId,Connection dbConn) throws Exception {
  //有部门得到所有人员的Ids;
    YHPersonLogic personLogic = new YHPersonLogic();
    List<YHPerson> personList = personLogic.getPersonByDept(Integer.parseInt(deptId), dbConn);
    String userIds = "";
    for (int i = 0; i < personList.size(); i++) {
      userIds = userIds + personList.get(i).getSeqId() + ",";
    }
    if(!userIds.equals("")){
      userIds  = userIds.substring(0, userIds.length()-1);
    }
    //System.out.println(userIds);
    return userIds;
  }
  public static String getUserName(String userIds,Connection dbConn) throws Exception {
    //有部门得到所有人员的Names;
      YHPersonLogic personLogic = new YHPersonLogic();
      String userNames = personLogic.getNameBySeqIdStr(userIds, dbConn);
      return userNames;
    }
  public static String getUserIds2(String deptId,Connection dbConn) throws Exception {
    //有部门得到所有人员的Ids;
      YHPersonLogic personLogic = new YHPersonLogic();
      List<YHPerson> personList = personLogic.getPersonByDept(Integer.parseInt(deptId), dbConn);
      String userIds = "";
      for (int i = 0; i < personList.size(); i++) {
        userIds = userIds + personList.get(i).getSeqId() + ",";
      }
      return userIds;
    }
    public static String getUserName2(String userIds,Connection dbConn) throws Exception {
      //有部门得到所有人员的Names;
        YHPersonLogic personLogic = new YHPersonLogic();
        String userNames = personLogic.getNameBySeqIdStr(userIds, dbConn);
        return userNames;
      }
    /*
     * 根据id字符串得到name字符串

     */
    public  List<YHPerson>  getPersonByIds(Connection dbConn,String ids)throws Exception{
      YHPersonLogic tpl = new YHPersonLogic();
      List<YHPerson> personList = new ArrayList<YHPerson>();
      YHORM orm = new YHORM();
      if(!ids.equals("")){
        String[] str = {"SEQ_ID in (" + ids + ")"};
        personList =orm.loadListSingle(dbConn, YHPerson.class, str );
      }
      return personList;
    }
    public List<YHPerson> getPersonByPriv(Connection dbConn,String priv,String opt,String deptId) throws Exception{
      List<YHPerson> personList = new ArrayList<YHPerson>();
      String sql = "select p.SEQ_ID as seqId,p.USER_NAME as userName,up.PRIV_NO as privNo from PERSON P,USER_PRIV up where p.USER_PRIV=up.SEQ_ID and up.PRIV_NO " + opt + " " + priv + " and p.DEPT_ID in (" + deptId + ")";
      Statement stmt = null;
      ResultSet rs = null;
      if(priv!=null&&!priv.equals("")){
        try {
          stmt = dbConn.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
            YHPerson person = new YHPerson();
            person.setSeqId(rs.getInt("seqId"));
            person.setUserName(rs.getString("seqId"));
            personList.add(person);
          }  
        }catch(Exception ex) {
           throw ex;
        }finally {
          YHDBUtility.close(stmt, rs, log);
        }
      }
      return personList;
    }
}

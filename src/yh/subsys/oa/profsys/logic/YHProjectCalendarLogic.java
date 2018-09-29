package yh.subsys.oa.profsys.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.profsys.data.YHProjectCalendar;

public class YHProjectCalendarLogic {
  private static Logger log = Logger.getLogger(YHProjectCalendarLogic.class);
  /**
   * 新建项目
   * 
   * @return
   * @throws Exception
   */
  public static String addProjectCalendar(Connection dbConn, YHProjectCalendar calendar) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, calendar);
    return getMaSeqId(dbConn,"oa_project_calendar");
  }
  /**
   * 修改项目
   * 
   * @return
   * @throws Exception
   */
  public static void updateProjectCalendar(Connection dbConn,YHProjectCalendar calendar) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, calendar);
  }
  /**
   *修改项目日程状态seqId
   * 
   * @return
   * @throws Exception
   */
  public static void updateStatus(Connection dbConn,String overStatus ,String seqId)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "update oa_project_calendar set OVER_STATUS = '" + overStatus +"'  where SEQ_ID = " +  seqId;
    try{
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /**
   *删除项目
   * 
   * @return
   * @throws Exception
   */
  public static void delProjCalendar(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHProjectCalendar.class,seqId);
  }
  /**
   *详细信息
   * 
   * @return
   * @throws Exception
   */
  public  static YHProjectCalendar getCalendarById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    return (YHProjectCalendar)orm.loadObjSingle(dbConn,YHProjectCalendar.class,Integer.parseInt(seqId));
  }
  /**
   *修改项目日程详细信息seqId
   * 
   * @return
   * @throws Exception
   */
  public static Map<String,String> getCalendarInfoById(Connection dbConn ,String seqId)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    Map<String,String> map  = new HashMap<String, String>(); 
    String sql = "select pc.SEQ_ID,pc.START_TIME,pc.END_TIME,pc.ACTIVE_TYPE,c.CLASS_DESC,pc.USER_ID,p.USER_NAME,"
        + "pc.ACTIVE_PARTNER,pc.ACTIVE_CONTENT,pc.OVER_STATUS,pc.ATTACHMENT_ID,pc.ATTACHMENT_NAME, pc.ACTIVE_LEADER,p2.USER_NAME,pc.PROJ_ID"
    		+ " from  oa_project_calendar pc left outer join PERSON p on pc.USER_ID = p.SEQ_ID "
    	  + " left outer join PERSON p2 on pc.ACTIVE_LEADER = p2.SEQ_ID "
    		+ " left outer join oa_kind_dict_item c on pc.ACTIVE_TYPE = c.SEQ_ID where pc.SEQ_ID = " +  seqId;
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        map.put("seqId", rs.getString(1));
        map.put("startTime", YHUtility.getDateTimeStr(rs.getTimestamp(2)));
        map.put("endTime", YHUtility.getDateTimeStr(rs.getTimestamp(3)));
        String status = "0";// 进行中 判断判断状态
        System.out.println(YHUtility.getDateTimeStr(rs.getTimestamp(2)));
        if (!YHUtility.isNullorEmpty(YHUtility.getDateTimeStr(rs.getTimestamp(2)))&&rs.getString(10)!=null&& !rs.getString(10).toString().equals("1")){
          if (YHUtility.getCurDateTimeStr().compareTo(YHUtility.getDateTimeStr(rs.getTimestamp(2)))<0) {
            status = "1";// 未开始
          }
          if (YHUtility.getCurDateTimeStr().compareTo(YHUtility.getDateTimeStr(rs.getTimestamp(3)))>0) {
            status = "2";// 超时
          }
        }
        map.put("status", status);
        map.put("activeType", rs.getString(4));
        map.put("activeTypeDesc", rs.getString(5));
        map.put("userId", rs.getString(6));
        map.put("userIdDesc", rs.getString(7));
        map.put("activePartner", rs.getString(8));
        map.put("activeContent", rs.getString(9));
        map.put("overStatus", rs.getString(10));
        map.put("attachmentId", rs.getString(11));
        map.put("attachmentName", rs.getString(12));
        map.put("activeLeader", rs.getString(13));
        map.put("activeLeaderName", rs.getString(14));
        map.put("projId", rs.getString(15));
      }
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return map;
  }
  /**
   *返回项目seqId
   * 
   * @return
   * @throws Exception
   */
  public static String getMaSeqId(Connection dbConn,String tableName)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String maxSeqId = "0";
    int seqId = 0;
    String sql = "select max(SEQ_ID) as SEQ_ID from " + tableName;
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        seqId = rs.getInt("SEQ_ID");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    maxSeqId = String.valueOf(seqId);
    return maxSeqId;
  }
  /**
   * 查询项目日程安排
   * 
   * @return
   * @throws Exception
   */
  public static List<YHProjectCalendar> selectProjCalendar(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHProjectCalendar> calendarList = new ArrayList<YHProjectCalendar>();
    calendarList =  orm.loadListSingle(dbConn, YHProjectCalendar.class,str);
    return calendarList;
  }

/**
 
  
  /**
   * 处理上传附件，返回附件id，附件名称



   * 
   * @param request
   *          HttpServletRequest
   * @param
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath() + File.separator  + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath() + File.separator  +"profsys" + File.separator   + hard + File.separator  + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  /***
   * 根据条件查询数据的projId
   * @return
   * @throws Exception 
   */
  public static String queryCalendarToProjId(Connection dbConn,String activeType,String activeContent,String activeLeader,
      String activePartner,String startTime,String startTime1,String endTime,String endTime1,String projCalendarType) throws Exception {
    String sql = "select PROJ_ID from oa_project_calendar"
      + " where PROJ_CALENDAR_TYPE='" + projCalendarType + "'";
    if (!YHUtility.isNullorEmpty(activeType)) {
      sql += " and ACTIVE_TYPE='" + activeType + "'";
    }
    if (!YHUtility.isNullorEmpty(activeContent)) {
      sql += " and ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(activeContent) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(activeLeader)) {
      sql += " and ACTIVE_LEADER = '" +  activeLeader + "'";
    }
    if (!YHUtility.isNullorEmpty(activePartner)) {
      sql += " and ACTIVE_PARTNER like '%" +  YHUtility.encodeLike(activePartner) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(startTime)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("START_TIME", startTime, ">=");
    }
    if (!YHUtility.isNullorEmpty(startTime1)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("START_TIME", startTime1, "<=");
    }
    if (!YHUtility.isNullorEmpty(endTime)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("END_TIME", endTime, ">=");
    }
    if (!YHUtility.isNullorEmpty(endTime1)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("END_TIME", endTime1, "<=");
    }
    sql = sql + " group by PROJ_ID";
    PreparedStatement ps = null;
    ResultSet rs = null;
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    String projId = "";
    while (rs.next()) {
      if(!YHUtility.isNullorEmpty(rs.getString("PROJ_ID"))){
        projId += rs.getString("PROJ_ID") + ",";
      }
      
    }
    if (!YHUtility.isNullorEmpty(projId)) {
      projId = projId.substring(0,projId.length() - 1);
    }
    return projId;
  }
}

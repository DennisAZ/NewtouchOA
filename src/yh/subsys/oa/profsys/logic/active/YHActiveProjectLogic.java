package yh.subsys.oa.profsys.logic.active;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProject;

public class YHActiveProjectLogic {
  private static Logger log = Logger.getLogger(YHActiveProjectLogic.class);
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,String projType,String projNum
      ,String projActiveType,String projStartTime1,String projStartTime2,
      String projGropName,String projVisitType,String projEndTime1,String projEndTime2,
      String projLeader,String deptId,String managerStr,String projStatus,int userId) throws Exception{
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID where p.PROJ_TYPE = '" + projType + "'";
    if(!YHUtility.isNullorEmpty(projStatus)){
      if(projStatus.equals("0")){
        sql = sql + " and (p.PROJ_STATUS = '0' or p.PROJ_STATUS is null)";
      }else{
        sql = sql + " and p.PROJ_STATUS = '" + projStatus + "'";
      }
    }
    if(!YHUtility.isNullorEmpty(managerStr)){
      sql = sql + " and (p.DEPT_ID " + managerStr + " or p.CREATOR = '" + userId + "')";
    }
    if(!YHUtility.isNullorEmpty(projNum)){
      sql = sql + " and p.PROJ_NUM like '%" + YHDBUtility.escapeLike(projNum) + "%'" + YHDBUtility.escapeLike() ;
    }
    if(!YHUtility.isNullorEmpty(projActiveType)){
      sql = sql + " and p.PROJ_ACTIVE_TYPE = '" + projActiveType + "'";
    }

    if(!YHUtility.isNullorEmpty(projStartTime1)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_START_TIME",projStartTime1, ">=");
    }
    if(!YHUtility.isNullorEmpty(projStartTime2)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_START_TIME",projStartTime2 + " 23:59:59", "<=");
    }
    if(!YHUtility.isNullorEmpty(projGropName)){
      sql = sql + " and ba.BUDGET_ITEM like '%" + YHDBUtility.escapeLike(projGropName) + "%'" + YHDBUtility.escapeLike() ;
    }
    if(!YHUtility.isNullorEmpty(projVisitType)){
      sql = sql + " and p.PROJ_VISIT_TYPE ='" + projVisitType + "'";
    }

    if(!YHUtility.isNullorEmpty(projEndTime1)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_END_TIME",projEndTime1, ">=");
    }
    if(!YHUtility.isNullorEmpty(projEndTime2)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_END_TIME",projEndTime2 + " 23:59:59", "<=");
    }
    if(!YHUtility.isNullorEmpty(projLeader)){
      sql = sql + " and p.PROJ_LEADER = '" + projLeader + "'";
    }
    if(!YHUtility.isNullorEmpty(deptId)&& !deptId.equals("0")){
      sql = sql + " and p.DEPT_ID in(" + deptId + ")";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,String projType) throws Exception{
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID where p.PROJ_TYPE = '" + projType + "' order by p.PROJ_START_TIME";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   * 导出
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public static ArrayList<YHDbRecord> toInExp(Connection dbConn,String seqIds) throws Exception{
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    String sql = "select p.SEQ_ID,p.PROJ_ARRIVE_TIME,p.PROJ_LEAVE_TIME,ba.BUDGET_ITEM"
      + ",ci.CLASS_DESC,p.COUNTRY_TOTAL,p.PURPOSE_COUNTRY,pn.USER_NAME,p.P_TOTAL,p.P_COUNCIL,p.P_YX,p.P_GUEST"
      + ",p.PRINT_STATUS"
      + " from oa_project p left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID where p.SEQ_ID in(" + seqIds + ")";
    Statement stmt = null;
    ResultSet rs = null; 
    String budgetItem = "";
    int i = 0;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        YHDbRecord dbrec = new YHDbRecord();
        dbrec.addField("序号",++i);
        dbrec.addField("开始时间",YHUtility.getDateTimeStr(rs.getTimestamp(2)).substring(0, 10));
        dbrec.addField("结束时间",YHUtility.getDateTimeStr(rs.getTimestamp(3)).substring(0, 10));
        dbrec.addField("活动名称",rs.getString(4));
        dbrec.addField("活动类别",rs.getString(5));
        dbrec.addField("国家数",rs.getString(6));
        dbrec.addField("国家名",rs.getString(7));
        dbrec.addField("负责人",rs.getString(8));
        dbrec.addField("参与总人数",rs.getString(9));
        dbrec.addField("理事人数",rs.getString(10));
        dbrec.addField("外宾人数",rs.getString(11));
        dbrec.addField("外办人数",rs.getString(12));
        dbL.add(dbrec);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return dbL;
  }
  /**
   * 查询结果，显示分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public static String profsysSelectActive(Connection dbConn,Map request,YHProject project,Date start,Date end) throws Exception{
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX"
      + ",p.P_TOTAL,p.PRINT_STATUS,p.PROJ_ACTIVE_TYPE,p.BUDGET_ID,p.PROJ_TYPE,p.PROJ_LEADER"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID where p.PROJ_TYPE = '" + project.getProjType() + "'";
    if (!YHUtility.isNullorEmpty(project.getProjNum())) {
      sql += " and p.PROJ_NUM like '%" +  YHDBUtility.escapeLike(project.getProjNum())+ "%' " + YHDBUtility.escapeLike();
    }
    if(!YHUtility.isNullorEmpty(project.getProjActiveType())) {
      sql = sql + " and p.PROJ_ACTIVE_TYPE = '" + project.getProjActiveType() + "'";
    }
    if(project.getProjStartTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(project.getProjStartTime()), ">=");
      sql += " and " + str;
    }
    if(start != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(start), "<=");
      sql += " and " + str;
    }
    if(project.getBudgetId() > 0) {
      sql = sql + " and p.BUDGET_ID = '" + project.getBudgetId() + "'";
    }
    if (!YHUtility.isNullorEmpty(project.getProjLeader())) {
      sql += " and p.PROJ_LEADER = '" +  project.getProjLeader()+ "'";
    }
    
    if(project.getProjEndTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(project.getProjEndTime()), ">=");
      sql += " and " + str;
    }
    if(end != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(end), "<=");
      sql += " and " + str;
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String queryProjectBySeqIds(Connection dbConn,Map request,String projId,String projType) throws Exception {
    if (YHUtility.isNullorEmpty(projId)) {
      projId = "0"; 
    }
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX"
      + ",p.P_TOTAL,p.PRINT_STATUS,p.PROJ_ACTIVE_TYPE,p.BUDGET_ID,p.PROJ_TYPE,p.PROJ_LEADER"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID "
      + " where p.PROJ_TYPE='" + projType + "' and p.SEQ_ID in (" + projId + ")";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页_人员
   * @return
   * @throws Exception 
   */
  public static String queryProjectMem(Connection dbConn,Map request,String projType,String memNum,String memPosition
      ,String memName,String memSex,String unitNum,String unitName,String projMemType) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX"
      + ",p.P_TOTAL,p.PRINT_STATUS,p.PROJ_ACTIVE_TYPE,p.BUDGET_ID,p.PROJ_TYPE,p.PROJ_LEADER"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID "
      + " ,oa_project_member pm "
      + " where pm.proj_id = p.seq_id and p.PROJ_TYPE='" + projType + "'"
      + " and  pm.PROJ_MEM_TYPE='" + projMemType+ "' ";
      if (!YHUtility.isNullorEmpty(memNum)) {
        sql += " and pm.MEM_NUM like '%" +  YHUtility.encodeLike(memNum) + "%' " + YHDBUtility.escapeLike();
      }
      
      if (!YHUtility.isNullorEmpty(memPosition)) {
        sql += " and " + YHDBUtility.findInSet(memPosition, "pm.MEM_POSITION");
      }
      if (!YHUtility.isNullorEmpty(memName)) {
        sql += " and pm.MEM_NAME like '%" + YHUtility.encodeLike(memName) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(unitNum)) {
        sql += " and pm.UNIT_NUM like '%" + YHUtility.encodeLike(unitNum) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(memSex)) {
        sql += " and pm.MEM_SEX='" + memSex + "'";
      }
      if (!YHUtility.isNullorEmpty(unitName)) {
        sql += " and pm.UNIT_NAME like '%" + YHUtility.encodeLike(unitName) + "%' " + YHDBUtility.escapeLike();
      }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页_纪要
   * @return
   * @throws Exception 
   */
  public static String queryProjectComm(Connection dbConn,Map request,String projType,String commNum,String commMemCn,
      String commMemFn,String commName,String commTime,String commPlace,String projCommType) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX"
      + ",p.P_TOTAL,p.PRINT_STATUS,p.PROJ_ACTIVE_TYPE,p.BUDGET_ID,p.PROJ_TYPE,p.PROJ_LEADER"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID "
      + " ,oa_project_COMM pc where pc.SEQ_ID = p.PROJ_ID and p.PROJ_TYPE='" + projType + "'"
      +  " and pc.PROJ_COMM_TYPE='" + projCommType + "' ";
      if (!YHUtility.isNullorEmpty(commNum)) {
        sql += " and pc.COMM_NUM like '%" +  YHUtility.encodeLike(commNum) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(commMemCn)) {
        sql += " and pc.COMM_MEM_CN like '%" +  YHUtility.encodeLike(commMemCn) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(commMemFn)) {
        sql += " and pc.COMM_MEM_FN like '%" +  YHUtility.encodeLike(commMemFn) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(commName)) {
        sql += " and pc.COMM_NAME like '%" +  YHUtility.encodeLike(commName) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(commPlace)) {
        sql += " and pc.COMM_PLACE like '%" +  YHUtility.encodeLike(commPlace) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(commTime)) {
        String str =  YHDBUtility.getDateFilter("pc.COMM_TIME", commTime, "=");
        sql += " and " + str;
      }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页_日程
   * @return
   * @throws Exception 
   */
  public static String queryProjectCalendar(Connection dbConn,Map request,String projType,String activeType,String activeContent,String activeLeader,
      String activePartner,String startTime,String startTime1,String endTime,String endTime1,String projCalendarType) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX"
      + ",p.P_TOTAL,p.PRINT_STATUS,p.PROJ_ACTIVE_TYPE,p.BUDGET_ID,p.PROJ_TYPE,p.PROJ_LEADER"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID "
      + " ,oa_project_calendar pc "
      + " where pc.proj_id = p.seq_id and p.PROJ_TYPE='" + projType + "'"
    + " and pc.PROJ_CALENDAR_TYPE='" + projCalendarType + "'";
    if (!YHUtility.isNullorEmpty(activeType)) {
      sql += " and pc.ACTIVE_TYPE='" + activeType + "'";
    }
    if (!YHUtility.isNullorEmpty(activeContent)) {
      sql += " and pc.ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(activeContent) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(activeLeader)) {
      sql += " and pc.ACTIVE_LEADER = '" +  activeLeader + "'";
    }
    if (!YHUtility.isNullorEmpty(activePartner)) {
      sql += " and pc.ACTIVE_PARTNER like '%" +  YHUtility.encodeLike(activePartner) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(startTime)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("pc.START_TIME", startTime, ">=");
    }
    if (!YHUtility.isNullorEmpty(startTime1)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("pc.START_TIME", startTime1, "<=");
    }
    if (!YHUtility.isNullorEmpty(endTime)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("pc.END_TIME", endTime, ">=");
    }
    if (!YHUtility.isNullorEmpty(endTime1)) {
      sql = sql + " and " + YHDBUtility.getDateFilter("pc.END_TIME", endTime1, "<=");
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页_人员
   * @return
   * @throws Exception 
   */
  public static String queryProjectFile(Connection dbConn,Map request,String projType,String fileNum
      ,String fileName,String fileType,String projCreator,String fileTitle,String projFileType) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX"
      + ",p.P_TOTAL,p.PRINT_STATUS,p.PROJ_ACTIVE_TYPE,p.BUDGET_ID,p.PROJ_TYPE,p.PROJ_LEADER"
      + " from PROJECT p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID "
      + " ,oa_project_attach pf "
      + " where pf.proj_id = p.seq_id and p.PROJ_TYPE='" + projType + "'"
      + " and  pf.PROJ_FILE_TYPE='" + projFileType+ "' ";
      if (!YHUtility.isNullorEmpty(fileNum)) {
        sql += " and pf.FILE_NUM like '%" +  YHUtility.encodeLike(fileNum) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(fileName)) {
        sql += " and pf.FILE_NAME like '%" +  YHUtility.encodeLike(fileName) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(fileType)) {
        sql += " and pf.FILE_TYPE like '%" +  YHUtility.encodeLike(fileType) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(projCreator)) {
        sql += " and pf.PROJ_CREATOR = '" + projCreator + "'";
      }
      if (!YHUtility.isNullorEmpty(fileTitle)) {
        sql += " and pf.ILE_TITLE like '%" +  YHUtility.encodeLike(fileTitle) + "%' " + YHDBUtility.escapeLike();
      }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
}

package yh.subsys.oa.profsys.logic.out;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProject;

public class YHProjectLogic {
  private static Logger log = Logger.getLogger(YHProjectLogic.class);
  /**
   * 新建项目
   * 
   * @return
   * @throws Exception
   */
  public static String addProject(Connection dbConn, YHProject project) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, project);
    return getMaSeqId(dbConn,"oa_project");
  }
  /**
   * 修改项目
   * 
   * @return
   * @throws Exception
   */
  public static void updateProject(Connection dbConn,YHProject project) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, project);
  }
  /**
   *删除项目
   * 
   * @return
   * @throws Exception
   */
  public static void delProj(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHProject.class,seqId);
  }
  /**
   *详细信息
   * 
   * @return
   * @throws Exception
   */
  public static YHProject showDetail(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    return (YHProject)orm.loadObjComplex(dbConn,YHProject.class,seqId);
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
   * 修改状态，结束
   * 
   * @return
   * @throws Exception
   */
  public static void updateStatus(Connection dbConn,int seqId,String status) throws Exception {
    String sql = "update oa_project set PROJ_STATUS=?  WHERE SEQ_ID=?";//,PROJ_END_TIME=?
    PreparedStatement ps = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1,status);
      ps.setInt(2,seqId);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps,null, log);
    }
  }
  /**
   * 修改打印状态
   * 
   * @return
   * @throws Exception
   */
  public static void printOut(Connection dbConn,String printStr) throws Exception {
    String sql = "update oa_project set print_status='1' WHERE SEQ_ID in (" + printStr + ")";
    PreparedStatement ps = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps,null, log);
    }
  }
  /**
   * 打印相应数据
   * @param dbConn
   * @param tableName
   * @return
   * @throws Exception
   */
  public static List<YHProject> projectPrint(Connection dbConn,String[] str) throws Exception{
    YHORM orm = new YHORM();
    List<YHProject> project = new ArrayList<YHProject>();
    project = orm.loadListSingle(dbConn,YHProject.class,str);
    return project;
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String profsysSelect(Connection dbConn,Map request,YHProject project,Date statrTime,Date endTime
      ,String postpriv,String postDept,String deptId) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,b.BUDGET_ITEM,de.DEPT_NAME,code.class_desc,son.user_name,code2.class_desc" 
      + ",p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS,p.PROJ_TYPE"
      + ",p.PROJ_STATUS,p.PROJ_VISIT_TYPE,p.PROJ_LEADER,p.PROJ_ACTIVE_TYPE FROM project p"
      + " left outer join oa_ration_apply b on p.BUDGET_ID=b.SEQ_ID"
      + " left outer join oa_department de on de.seq_id =p.DEPT_ID"
      + " left outer join oa_kind_dict_item code on code.seq_id =p.PROJ_VISIT_TYPE"
      + " left outer join person son on son.seq_id =p.PROJ_LEADER"
      + " left outer join oa_kind_dict_item code2 on code2.seq_id=p.PROJ_ACTIVE_TYPE"
      +	" where p.PROJ_STATUS <> '1' and p.PROJ_TYPE='1'";
    if (postpriv.equals("2")) {
      sql += " and p.DEPT_ID in ("+ postDept +")";
    }
    if (postpriv.equals("0")) {
      sql += " and p.DEPT_ID='" + deptId + "'";
    }
    if (!YHUtility.isNullorEmpty(project.getProjNum())) {
      sql += " and p.PROJ_NUM like '%" + YHUtility.encodeLike(project.getProjNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(project.getProjActiveType())) {
      sql += " and p.PROJ_ACTIVE_TYPE='" + project.getProjActiveType() + "'";
    }
    if (!YHUtility.isNullorEmpty(project.getProjLeader())) {
      sql += " and p.PROJ_LEADER ='" + project.getProjLeader() + "' ";
    }
    if (project.getBudgetId() > 0) {
      sql += " and p.BUDGET_ID='" + project.getBudgetId() + "' ";
    }
    if (!YHUtility.isNullorEmpty(project.getProjVisitType())) {
      sql += " and p.PROJ_VISIT_TYPE='" + project.getProjVisitType() + "'";
    }
    if (!YHUtility.isNullorEmpty(project.getProjDept()) && !project.getProjDept().equals("0")) {
      sql += " and p.DEPT_ID in (" + project.getProjDept() + ")";
    }
    if (project.getProjStartTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(project.getProjStartTime()), ">=");
      sql += " and " + str;
    }
    if (statrTime != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(statrTime), "<=");
      sql += " and " + str;
    }
    if (project.getProjEndTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(project.getProjEndTime()), ">=");
      sql += " and " + str;
    }
    if (endTime != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(endTime), "<=");
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
  public static String profsysHistory(Connection dbConn,Map request,YHProject project,Date statrTime,Date endTime
      ,String postpriv,String postDept,String deptId) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,b.BUDGET_ITEM,de.DEPT_NAME,code.class_desc,son.user_name,code2.class_desc" 
      + ",p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS,p.PROJ_TYPE"
      + ",p.PROJ_STATUS,p.PROJ_VISIT_TYPE,p.PROJ_LEADER,p.PROJ_ACTIVE_TYPE FROM project p"
      + " left outer join oa_ration_apply b on p.BUDGET_ID=b.SEQ_ID"
      + " left outer join oa_department de on de.seq_id =p.DEPT_ID"
      + " left outer join oa_kind_dict_item code on code.seq_id =p.PROJ_VISIT_TYPE"
      + " left outer join person son on son.seq_id =p.PROJ_LEADER"
      + " left outer join oa_kind_dict_item code2 on code2.seq_id=p.PROJ_ACTIVE_TYPE"
      + " where p.PROJ_STATUS='1' and  p.PROJ_TYPE='1'";

    if (postpriv.equals("2")) {
      sql += " and p.DEPT_ID in ("+ postDept +")";
    }
    if (postpriv.equals("0")) {
      sql += " and p.DEPT_ID='" + deptId + "'";
    }

    if (!YHUtility.isNullorEmpty(project.getProjNum())) {
      sql += " and p.PROJ_NUM like '%" + YHUtility.encodeLike(project.getProjNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(project.getProjActiveType())) {
      sql += " and p.PROJ_ACTIVE_TYPE='" + project.getProjActiveType() + "'";
    }
    if (project.getBudgetId() > 0) {
      sql += " and p.BUDGET_ID ='" + project.getBudgetId() + "' ";
    }
    if (!YHUtility.isNullorEmpty(project.getProjVisitType())) {
      sql += " and p.PROJ_VISIT_TYPE='" + project.getProjVisitType() + "'";
    }
    if (project.getProjStartTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(project.getProjStartTime()), ">=");
      sql += " and " + str;
    }
    if (statrTime != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(statrTime), "<=");
      sql += " and " + str;
    }
    if (project.getProjEndTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(project.getProjEndTime()), ">=");
      sql += " and " + str;
    }
    if (endTime != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(endTime), "<=");
      sql += " and " + str;
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /**
   * 导出-lz
   * 
   * */
  public static ArrayList<YHDbRecord> getDbRecord(List<YHProject> list,Connection dbConn) throws Exception{
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHProject pr = new YHProject();
    int sun = 0;//总人数
    int pYx = 0;//友协人数
    int pGuest = 0;//外宾人数;
    int pCouncil = 0;//理事人数
    int countryTotal = 0;//国家总数
    String country = "";
    for (int i = 0; i < list.size(); i++) {
      pr = list.get(i);
      sun = sun + pr.getPTotal();
      pYx = pYx + pr.getPYx();
      pCouncil = pCouncil + pr.getPCouncil();
      pGuest = pGuest + pr.getPGuest();
      countryTotal = countryTotal + pr.getCountryTotal();
      country = country + pr.getPurposeCountry();

      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("序号",i+1);
      dbrec.addField("开始时间",pr.getProjStartTime().toString().substring(0,4) + "年" + pr.getProjStartTime().toString().substring(5,7) + "月" + pr.getProjStartTime().toString().substring(8,10) + "日");
      dbrec.addField("结束时间",pr.getProjEndTime().toString().substring(0,4) + "年" + pr.getProjEndTime().toString().substring(5,7) +  "月" + pr.getProjEndTime().toString().substring(8,10)+ "日");
      dbrec.addField("团组名称",getGroupName(dbConn,String.valueOf(pr.getBudgetId())));
      dbrec.addField("出访类别",getVisitActive(dbConn,pr.getProjVisitType()));
      dbrec.addField("项目类别",getVisitActive(dbConn,pr.getProjActiveType()));
      dbrec.addField("国家数",pr.getCountryTotal());
      dbrec.addField("国家名",pr.getPurposeCountry());
      dbrec.addField("负责人",getUser(dbConn,pr.getProjLeader()));
      //dbrec.addField("结算费用",pr.getBudgetId());
      dbrec.addField("参与总人数",pr.getPTotal());
      dbrec.addField("理事人数",pr.getPGuest());
      dbrec.addField("外宾人数",pr.getPCouncil());
      dbrec.addField("外办人数",pr.getPYx());
      //      dbrec.addField("参与总人数",sun);
      //      dbrec.addField("总理事人数",pGuest);
      //      dbrec.addField("总外宾人数",pCouncil);
      //      dbrec.addField("总友协人数",pYx);
      dbL.add(dbrec);
    }
    return dbL;
  }
  //团组名称
  public static String getGroupName(Connection dbConn,String seqId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    String groupName = null;
    try {
      String sql = "select BUDGET_ITEM from oa_ration_apply where SEQ_ID=" + seqId;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        groupName = rs.getString("BUDGET_ITEM");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return groupName;
  }

  //出访，项目
  public static String getVisitActive(Connection dbConn,String seqId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    String groupName = null;
    try {
      String sql = "select class_desc from oa_kind_dict_item where SEQ_ID=" + seqId;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        groupName = rs.getString("class_desc");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return groupName;
  }
  //负责人
  public static String getUser(Connection dbConn,String seqId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    String groupName = null;
    try {
      String sql = "select user_name from person where SEQ_ID=" + seqId;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        groupName = rs.getString("user_name");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return groupName;
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
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath() + File.separator  + "profsys" + File.separator  + hard + File.separator  + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String profsysSelect2(Connection dbConn,Map request,YHProject project,Date statrTime,Date endTime) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,b.BUDGET_ITEM,de.DEPT_NAME,code.class_desc,son.user_name,code2.class_desc" 
      + ",p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS,p.PROJ_TYPE"
      + ",p.PROJ_STATUS,p.PROJ_VISIT_TYPE,p.PROJ_LEADER,p.PROJ_ACTIVE_TYPE FROM project p"
      + " left outer join oa_ration_apply b on p.BUDGET_ID=b.SEQ_ID"
      + " left outer join oa_department de on de.seq_id =p.DEPT_ID"
      + " left outer join oa_kind_dict_item code on code.seq_id =p.PROJ_VISIT_TYPE"
      + " left outer join person son on son.seq_id =p.PROJ_LEADER"
      + " left outer join oa_kind_dict_item code2 on code2.seq_id=p.PROJ_ACTIVE_TYPE"
      + " where p.PROJ_TYPE='1'";
    // p.PROJ_STATUS <> '1' and
    if (!YHUtility.isNullorEmpty(project.getProjNum())) {
      sql += " and p.PROJ_NUM like '%" + YHUtility.encodeLike(project.getProjNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(project.getProjActiveType())) {
      sql += " and p.PROJ_ACTIVE_TYPE='" + project.getProjActiveType() + "'";
    }
    if (!YHUtility.isNullorEmpty(project.getProjLeader())) {
      sql += " and p.PROJ_LEADER ='" + project.getProjLeader() + "' ";
    }
    if (project.getBudgetId() > 0) {
      sql += " and p.BUDGET_ID='" + project.getBudgetId() + "' ";
    }
    if (!YHUtility.isNullorEmpty(project.getProjVisitType())) {
      sql += " and p.PROJ_VISIT_TYPE='" + project.getProjVisitType() + "'";
    }
    if (!YHUtility.isNullorEmpty(project.getProjDept())) {
      sql += " and p.DEPT_ID in (" + project.getProjDept() + ")";
    }
    if (project.getProjStartTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(project.getProjStartTime()), ">=");
      sql += " and " + str;
    }
    if (statrTime != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_START_TIME", YHUtility.getDateTimeStr(statrTime), "<=");
      sql += " and " + str;
    }
    if (project.getProjEndTime() != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(project.getProjEndTime()), ">=");
      sql += " and " + str;
    }
    if (endTime != null) {
      String str =  YHDBUtility.getDateFilter("p.PROJ_END_TIME", YHUtility.getDateTimeStr(endTime), "<=");
      sql += " and " + str;
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
}

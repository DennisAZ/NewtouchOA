package yh.core.funcs.workflow.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHPrcsRoleUtility {
  public YHDepartment deptParent(YHDepartment dept , int flag ,Connection conn) throws Exception{
    YHORM orm = new YHORM();
    if(dept.getDeptParent() == 0){
      return dept;
    }else{
      YHDepartment parentDept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, dept.getDeptParent());
      if(parentDept == null){
        return dept;
      }else{
        if(flag == 1){
          return parentDept;
        }else{
          return this.deptParent(parentDept, flag , conn);
        }
      }
      
    }
  }
  public int getDeptParent(int deptId , int flag ,Connection conn) throws Exception{
    String sql = "select DEPT_PARENT from oa_department where SEQ_ID ='" + deptId + "'";
    Statement stm = null;
    ResultSet rs = null;
    int deptParent = 0;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      if (rs.next()) {
        deptParent = rs.getInt("DEPT_PARENT");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    if(deptParent == 0){
      return deptId;
    }else{
      if(flag == 1){
        return deptParent;
      }else{
        return this.getDeptParent(deptParent, flag , conn);
      }
    }
  }
  /**
   * 
   * @param flowId 指定流程Id
   * @param checkType 0-检查办理权限；1-检查查询和管理权限
   * @param user 当前用户
   * @return true-有这个权限,false-没有这个权限
   * @throws Exception
   */
  public boolean prcsRole(int flowId  , int prcsId , int checkType , YHPerson user , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowType flowType = (YHFlowType) orm.loadObjSingle(conn, YHFlowType.class, flowId);
    Map filters = new HashMap();
    filters.put("FLOW_SEQ_ID", flowId);
    if(prcsId == 0){
      List<YHFlowProcess> flowProcessList = new ArrayList();
      flowProcessList = orm.loadListSingle(conn, YHFlowProcess.class, filters);
      return this.prcsRole(flowType, flowProcessList, checkType, user , conn);
    }else{
      YHFlowProcessLogic flowPrcsLogic = new  YHFlowProcessLogic();
      //查出相关步骤
      YHFlowProcess flowProcess = flowPrcsLogic.getFlowProcessById(flowId, prcsId+ "" , conn);
      return this.prcsRole(flowType, flowProcess, checkType, user ,conn);
    }
    
  }
  /**
   * 判断当前用户是否有指定流程、指定步骤的权限
   * @param flowType 指定流程对象
   * @param flowProcess 指定步骤对象
   * @param checkType 
   * @param user
   * @return
   * @throws Exception 
   */
  public boolean prcsRole(YHFlowType flowType , YHFlowProcess flowProcess , int checkType  , YHPerson user ,Connection conn) throws Exception{
    List<YHFlowProcess> flowProcessList = new ArrayList();
    flowProcessList.add(flowProcess);
    return this.prcsRole(flowType, flowProcessList, checkType, user , conn);
  }
  
  public boolean prcsRole(YHFlowType flowType , int checkType , YHPerson user , Connection conn) throws Exception {
    boolean flag = false;
    List<YHFlowProcess> flowProcessList  =  null;
    if ("1".equals(flowType.getFlowType())) {
      String query = "select " 
        + " PRCS_ID"
        + " , PRCS_USER"
        + " , PRCS_DEPT"
        + " , PRCS_PRIV"
        + " from oa_fl_process where FLOW_SEQ_ID=" + flowType.getSeqId() + " and PRCS_ID=1";
      
      flowProcessList  = new ArrayList();
      Statement stm = null;
      ResultSet rs = null;
      try{
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          String prcsUser = rs.getString("PRCS_USER");
          String prcsDept = rs.getString("PRCS_DEPT");
          String prcsPriv = rs.getString("PRCS_PRIV");
          int prcsId = rs.getInt("PRCS_ID");
          
          YHFlowProcess fp = new YHFlowProcess();
          fp.setPrcsId(prcsId);
          fp.setPrcsUser(prcsUser);
          fp.setPrcsDept(prcsDept);
          fp.setPrcsPriv(prcsPriv);
          flowProcessList.add(fp);
        } else {
          return false;
        }
      }catch(Exception e){
        e.printStackTrace();
        throw e;
      }finally{
        YHDBUtility.close(stm, rs, null);
      }
      flag = this.prcsRole(flowType, flowProcessList, checkType, user, conn);
    } else {
      flag = this.prcsRole(flowType, flowProcessList, 0, user , conn);
    }
    return flag;
  }
/**
 * 判断当前用户是否有指定流程、指定步骤的权限
 * @param flowType 指定流程对象
 * @param flowProcessList 流程下的所有步骤
 * @param checkType 0-检查办理权限；1-检查查询和管理权限
 * @param user 当前用户
 * @return true-有这个权限,false-没有这个权限
 * @throws Exception 
 */
public boolean prcsRole(YHFlowType flowType , List<YHFlowProcess> flowProcessList , int checkType  , YHPerson user , Connection conn) throws Exception{
  YHFlowProcess flowPrcs = null;
  
  if(flowProcessList != null 
      && flowProcessList.size() == 1){
    flowPrcs = flowProcessList.get(0);
  }
  if(user == null){
    return false;
  }
  //如果是管理员
  if(user != null 
      && user.isAdminRole()){
    return true;
  }
  if(checkType == 0){
    //如是是自由流程    if(flowType.getFlowType().equals("2")){
      //自由流程非第一步骤，所有人都有权限。即只要这个人能看到这个流程，他就可以执行相应的操作（办理、查询等）      if(flowPrcs != null && flowPrcs.getPrcsId() != 1){
        return true;
      }else{
        String newUser = flowType.getNewUser(conn);
        //是不是要加辅助部门ID串.辅助角色等???
        return this.checkPriv(user, newUser);
      }
    }else{
    //固定流程
      for(YHFlowProcess tmp : flowProcessList){
        String prcsUser = (tmp.getPrcsUser() == null ? "" : tmp.getPrcsUser());
        String prcsDept = YHOrgSelectLogic.changeDept(conn, tmp.getPrcsDept()); 
        prcsDept = prcsDept == null ? "" : prcsDept;
        String prcsPriv =  (tmp.getPrcsPriv() == null ? "" : tmp.getPrcsPriv());
        String userPrivOther = user.getUserPrivOther();
        String userDeptIdOther = user.getDeptIdOther();
        if(YHWorkFlowUtility.findId(prcsUser , String.valueOf(user.getSeqId()))){
          return true;
        }
        if(YHWorkFlowUtility.findId(prcsDept , String.valueOf(user.getDeptId()))){
          return true;
        }
        if(YHWorkFlowUtility.findId(prcsPriv,user.getUserPriv())){
          return true;
        }
        if(userPrivOther != null && !YHWorkFlowUtility.checkId(prcsPriv , userPrivOther , true).equals("")){
          return true;
        }
        if(userDeptIdOther != null && !YHWorkFlowUtility.checkId(prcsDept , userDeptIdOther , true).equals("")){
          return true;
        }
      }
    }
  //检查查询和管理权限,即$CHECK_TYPE == 1
  }else{
    String queryUser = flowType.getQueryUser();
    if(this.checkPriv(user, queryUser)){
      return true;
    }
    String queryUserDept = flowType.getQueryUserDept();
    if(this.checkPriv(user, queryUserDept)){
      return true;
    }
    String manageUser = flowType.getManageUser();
    if(this.checkPriv(user, manageUser)){
      return true;
    }
    String manageUserDept = flowType.getManageUserDept();
    if(this.checkPriv(user, manageUserDept)){
      return true;
    }
  }
  return false;
}
/**
 * 判断当前用户是否有指定流程、指定步骤的权限
 * @param flowType 指定流程对象
 * @param flowProcessList 流程下的所有步骤

 * @param checkType 0-检查办理权限；1-检查查询和管理权限
 * @param user 当前用户
 * @return true-有这个权限,false-没有这个权限
 */
public boolean prcsRoleByManager(YHFlowType flowType , List<YHFlowProcess> flowProcessList , int checkType  , YHPerson user){
  YHFlowProcess flowPrcs = null;
  if(flowProcessList != null 
      && flowProcessList.size() == 1){
    flowPrcs = flowProcessList.get(0);
  }
  if(user == null){
    return false;
  }
  //如果是管理员
  if(user != null 
      && user.isAdminRole()){
    return true;
  }
  String manageUser = flowType.getManageUser();
  if(this.checkPriv(user, manageUser)){
    return true;
  }
  String manageUserDept = flowType.getManageUserDept();
  if(this.checkPriv(user, manageUserDept)){
    return true;
  }
  return false;
}
/**
 * 检查有没有这个查询权限,这里只针对固定流程
 * @param flowType
 * @param flowProcessList
 * @param checkType
 * @param user
 * @return
 * @throws Exception 
 */
public boolean prcsRoleByQuery(YHFlowType flowType  , YHPerson user  , Connection conn) throws Exception{
  if(user == null){
    return false;
  }
  //如果是管理员
  if(user != null 
      && user.isAdminRole()){
    return true;
  }
  String query  = "SELECT PRCS_USER , PRCS_DEPT, PRCS_PRIV  from oa_fl_process where FLOW_SEQ_ID=" + flowType.getSeqId();
  Statement stm = null;
  ResultSet rs = null;
  try {
    stm = conn.createStatement();
    rs = stm.executeQuery(query);
    while (rs.next()) {
      String prcsUser = rs.getString("PRCS_USER");
      String prcsDept = rs.getString("PRCS_DEPT");
      String prcsPriv = rs.getString("PRCS_PRIV");
      
      prcsDept = YHOrgSelectLogic.changeDept(conn, prcsDept); 
      String userPrivOther = user.getUserPrivOther();
      String userDeptIdOther = user.getDeptIdOther();
      if(YHWorkFlowUtility.findId(prcsUser , String.valueOf(user.getSeqId()))){
        return true;
      }
      if(YHWorkFlowUtility.findId(prcsDept , String.valueOf(user.getDeptId()))){
        return true;
      }
      if(YHWorkFlowUtility.findId(prcsPriv,user.getUserPriv())){
        return true;
      }
      if(userPrivOther != null && !YHWorkFlowUtility.checkId(prcsPriv , userPrivOther , true).equals("")){
        return true;
      }
      if(userDeptIdOther != null && !YHWorkFlowUtility.checkId(prcsDept , userDeptIdOther , true).equals("")){
        return true;
      }
    }
  } catch(Exception ex) {
    throw ex;
  } finally {
    YHDBUtility.close(stm, rs, null); 
  }
  String manageUser = flowType.getManageUser();
  if(this.checkPriv(user, manageUser)){
    return true;
  }
  String manageUserDept = flowType.getManageUserDept();
  if(this.checkPriv(user, manageUserDept)){
    return true;
  }
  String queryUser = flowType.getQueryUser();
  if(this.checkPriv(user, queryUser)){
    return true;
  }
  String queryUserDept = flowType.getQueryUserDept();
  if(this.checkPriv(user, queryUserDept)){
    return true;
  }
  return false;
}
  /**
   * 判断有没有这个权限
   * @param user
   * @param privStr
   * @return
   */
  public boolean checkPriv(YHPerson user, String privStr){
    if(privStr == null || user == null){
      return false;
    }
    String[] aPriv = privStr.split("\\|");
    String privUser = "";
    if (aPriv.length > 0 ) {
      privUser = aPriv[0];
    }
    String privDept = "";
    if (aPriv.length > 1 ) {
      privDept = aPriv[1];
    }
    String privRole = "";
    if (aPriv.length > 2 ) {
      privRole = aPriv[2];
    }
    if( "0".equals(privDept)
        || "ALL_DEPT".equals(privDept)
        || YHWorkFlowUtility.findId(privUser,String.valueOf(user.getSeqId())) 
        || YHWorkFlowUtility.findId(privDept,String.valueOf(user.getDeptId())) 
        || YHWorkFlowUtility.findId(privRole,user.getUserPriv())
        || !YHWorkFlowUtility.checkId(privRole , user.getUserPrivOther() ,true).equals("")
        || !YHWorkFlowUtility.checkId(privDept , user.getDeptIdOther() ,true).equals("")){
      return true;
    }
    return false;
  }
  public String runRole(int flowRunId , int flowTypeId , int  prcsId , YHPerson user , Connection conn) throws Exception{
    YHFlowRun flowRun = this.getFlowRun(flowRunId, conn);
    int flowId = 0 ;
    if (flowRun != null) {
      flowId = flowRun.getFlowId();
    }
    YHFlowType flowType = this.getFlowType(flowId, conn);
    return runRole(flowRun , flowType ,  prcsId ,user , conn);
  }
  public String runRole(int flowRunId , int  prcsId , YHPerson user , Connection conn) throws Exception{
    YHFlowRun flowRun = this.getFlowRun(flowRunId, conn);
    int flowId = 0 ;
    if (flowRun != null) {
      flowId = flowRun.getFlowId();
    }
    YHFlowType flowType = this.getFlowType(flowId, conn);
    return runRole(flowRun , flowType ,  prcsId ,user , conn);
  }
  public YHFlowType getFlowType(int flowId , Connection conn) throws Exception {
    YHFlowType flowType = null;
    String query = "select QUERY_USER, QUERY_USER_DEPT , MANAGE_USER , MANAGE_USER_DEPT  from oa_fl_type where SEQ_ID=" + flowId;
    Statement stm5 = null;
    ResultSet rs5 = null;
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      if(rs5.next()){
        flowType = new YHFlowType();
        flowType.setSeqId(flowId);
        flowType.setQueryUser(rs5.getString("QUERY_USER"));
        flowType.setQueryUserDept(rs5.getString("QUERY_USER_DEPT"));
        flowType.setManageUser(rs5.getString("MANAGE_USER"));
        flowType.setManageUserDept(rs5.getString("MANAGE_USER_DEPT"));
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, rs5, null); 
    }
    return flowType;
  }
  public YHFlowRun getFlowRun(int runId , Connection conn) throws Exception {
    YHFlowRun flowRun = null;
    String query = "select BEGIN_USER,FLOW_ID from oa_fl_run WHERE RUN_ID = " + runId;
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if(rs4.next()){
        flowRun = new YHFlowRun();
        flowRun.setBeginUser(rs4.getInt("BEGIN_USER"));
        flowRun.setRunId(runId);
        flowRun.setFlowId(rs4.getInt("FLOW_ID"));
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    return flowRun;
  }
  
  public String runRole(int flowRunId , YHPerson user , Connection conn) throws Exception{
    YHFlowRun flowRun = this.getFlowRun(flowRunId, conn);
    int flowId = 0 ;
    if (flowRun != null) {
      flowId = flowRun.getFlowId();
    }
    YHFlowType flowType = this.getFlowType(flowId, conn);
    return runRole(flowRun , flowType ,  0 ,user , conn);
  }
  /**
   * 
   * @param flowRun
   * @param flowType
   * @param prcsId
   * @param user
   * @return
   * @throws Exception
   */
  public String runRole(YHFlowRun flowRun , YHFlowType flowType , int  prcsId , YHPerson user , Connection conn) throws Exception{
    long date1 = System.currentTimeMillis();
    String runRole = "";
    if(flowRun == null){
      return runRole ;
    }
    //--- 系统管理员检查 ---
    if(user.isAdmin()){
      runRole += "1,";
    }
    //检查当前登录用户是否“主办人” ,功能达到,但此地方还有待调整得更好
    String query = "select 1 from oa_fl_run_prcs WHERE user_id =" + user.getSeqId() + " and op_FLAG = 1 AND RUN_ID =" + flowRun.getRunId() ;
    if(prcsId != 0 ){
      query +=  " and prcs_ID =" + prcsId;
    }
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if(rs4.next()){
        runRole += "2,";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    query = "select 1 from oa_fl_run_prcs WHERE user_id =" + user.getSeqId() + " AND RUN_ID =" + flowRun.getRunId() ;
    if(prcsId != 0 ){
      query +=  " and prcs_ID =" + prcsId;
    }
    Statement stm5 = null;
    ResultSet rs5 = null;
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      if(rs5.next()){
        runRole += "4,6,";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, rs5, null); 
    }
    //获取流程发起人所在的部门ID,要加辅助部门ID串?
    int beginUser = flowRun.getBeginUser();
    YHPerson userTmp = null;
    query = "select SEQ_ID , DEPT_ID , USER_PRIV , DEPT_ID_OTHER , USER_PRIV_OTHER from PERSON where seq_id=" + beginUser;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if(rs.next()){
        userTmp = new YHPerson();
        userTmp.setSeqId(rs.getInt("SEQ_ID"));
        userTmp.setDeptId(rs.getInt("DEPT_ID"));
        userTmp.setUserPriv(rs.getString("USER_PRIV"));
        userTmp.setDeptIdOther(rs.getString("DEPT_ID_OTHER"));
        userTmp.setUserPrivOther(rs.getString("USER_PRIV_OTHER"));
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    
    String beginDept = "";
    String myDeptStr = "";
    if(userTmp != null){
      beginDept = String.valueOf(userTmp.getDeptId());
      myDeptStr = this.getMyDept(user.getDeptId(), 0 , conn);
    }
    
    String queryUser = flowType.getQueryUser();
    if(this.checkPriv(user, queryUser)){
    //管理与监控人检查      runRole += "5,";
    }
    String queryUserDept = flowType.getQueryUserDept();
    if(this.checkPriv(user, queryUserDept)){
      if(YHWorkFlowUtility.findId(myDeptStr , beginDept)){
        runRole += "5,";
      }
    }
    String manageUser = flowType.getManageUser();
    if(this.checkPriv(user, manageUser)){
      runRole += "3,";
    }
    String manageUserDept = flowType.getManageUserDept();
    if(this.checkPriv(user, manageUserDept)){
      if(YHWorkFlowUtility.findId(myDeptStr , beginDept)){
        runRole += "3,";
      }
    }
    return runRole;
  }
  public String getMyDept(int deptId ,int lower , Connection conn) throws Exception{
    StringBuffer sb = new StringBuffer();
    if(lower == 0){
      sb.append(deptId + ",");
    }
    this.getDeptByParentId(deptId, sb , conn);
    return sb.toString();
  }
  public void getDeptByParentId(int deptId , StringBuffer ids , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select SEQ_ID from oa_department where DEPT_PARENT=" + deptId; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        int id = rs.getInt("SEQ_ID");
        ids.append(id + ",");
        this.getDeptByParentId(id, ids , conn);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt , rs , null);
    }
    
  } 
  public String flowOtherSql(String prcsPriv ) throws Exception{
    if(prcsPriv == null || "".equals(prcsPriv)){
      return "";
    }
    String query = "";
    String[] aPriv = prcsPriv.split(",");
    for(String temp : aPriv){
      if(!"".equals(temp)){
        query += " or USER_PRIV_OTHER like '"+ YHUtility.encodeLike(temp) +",%' "  + YHDBUtility.escapeLike() + " or USER_PRIV_OTHER like '%,"+ YHUtility.encodeLike(temp) +",%' " + YHDBUtility.escapeLike() ;
      }
    }
    return query;
  }
  public String flowDeptOtherSql(String prcsDept) throws Exception{
    if(prcsDept == null || "".equals(prcsDept)){
      return "";
    }
    String query = "";
    String[] aPriv = prcsDept.split(",");
    for(String temp : aPriv){
      if(!"".equals(temp)){
        query += " or DEPT_ID_OTHER like '"+ YHUtility.encodeLike(temp) +",%' "+YHDBUtility.escapeLike() +" or DEPT_ID_OTHER like '%,"+ YHUtility.encodeLike(temp) +",%' " + YHDBUtility.escapeLike() ;
      }
    }
    return query;
  }
}             

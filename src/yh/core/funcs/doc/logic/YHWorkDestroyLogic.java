package yh.core.funcs.doc.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.office.ntko.data.YHNtkoCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

/**
 * 工作流销毁逻辑层
 * @author Think
 *
 */
public class YHWorkDestroyLogic {
  private static Logger log = Logger.getLogger(YHWorkDestroyLogic.class);
 /**
  * 得到所有流程
  * @param conn
  * @return
  * @throws Exception
  */
  public String getFlow(Connection conn , String sortId) throws Exception{
    String result = "";
    String tmp = "";
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      tmp = " and FLOW_TYPE.flow_sort in (" + sortId + ") ";
    }
    String sql = "SELECT FLOW_TYPE.SEQ_ID,FLOW_NAME,FREE_OTHER from "+ YHWorkFlowConst.FLOW_TYPE +" FLOW_TYPE,"+ YHWorkFlowConst.FLOW_SORT +" FLOW_SORT where FLOW_TYPE.FLOW_SORT = FLOW_SORT.SEQ_ID "+ tmp +" order by SORT_NO,FLOW_NO";
    
    PreparedStatement ps = null ; 
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      while(rs.next()){
        int flowId = rs.getInt(1);
        String flowName = rs.getString(2);
        String freeOther = rs.getString(3);
        if(!"".equals(result)){
          result += ",";
        }
        result += "{" 
          + "flowId:" + flowId  + "," 
          + "flowName:\"" + flowName  + "\"," 
          + "flowId:\""  + freeOther  + "\"" 
          + "}";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return "[" + result + "]";
  }
  
  public StringBuffer getWorkListLogic(Connection conn,YHPerson user,Map request) throws Exception{
    StringBuffer result = new StringBuffer();
    try {
      String sql =" SELECT " 
          + " FLOW_RUN.SEQ_ID "
          + " ,RUN_ID "
          + " ,RUN_NAME "
          + " ,BEGIN_TIME "
          + " ,ATTACHMENT_ID "
          + " ,ATTACHMENT_NAME "
          + " ,FLOW_ID "
      		+ " FROM " 
      		+ " "+ YHWorkFlowConst.FLOW_RUN +" FLOW_RUN "
      		+ " ,"+ YHWorkFlowConst.FLOW_TYPE +" FLOW_TYPE "
      	  + " ,PERSON"
      		+ " WHERE DEL_FLAG = 1 " 
      	  + " and FLOW_TYPE.SEQ_ID=FLOW_RUN.FLOW_ID "
      	  + " AND PERSON.SEQ_ID = FLOW_RUN.BEGIN_USER";
 
      String where = toSerachWhere(conn,user,request);
      if(!"".equals(where)){
        sql += where;
      }
      String query = " order by RUN_ID desc";
      sql += query;
      //System.out.println(sql);
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      result.append(pageDataList.toJson());
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 得到所有日志的seqId
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
public StringBuffer getAlldeleteRunId(Connection conn,YHPerson user,Map request) throws Exception{
  StringBuffer result = new StringBuffer();
    String sql =" SELECT " 
        + " RUN_ID "
        + " FROM " 
        + " "+ YHWorkFlowConst.FLOW_RUN +" FLOW_RUN "
        + " , "+ YHWorkFlowConst.FLOW_TYPE +" FLOW_TYPE "
        + " ,PERSON"
        + " WHERE DEL_FLAG = 1 "
        + " and FLOW_TYPE.SEQ_ID=FLOW_RUN.FLOW_ID "
        + " AND PERSON.SEQ_ID = FLOW_RUN.BEGIN_USER";
  PreparedStatement ps = null;
  ResultSet rs = null;
  try {
    String where = toSerachWhere(conn,user,request);
    if(!"".equals(where)){
      sql += where;
    }
    ps = conn.prepareStatement(sql);
    rs = ps.executeQuery();
    while(rs.next()){
      int seqId = rs.getInt(1);
      if(!"".equals(result.toString())){
        result.append(",");
      }
      result.append(seqId);
    }
  } catch (Exception e) {
    throw e;
  } finally {
    YHDBUtility.close(ps, rs, log);
  }
  return result;
}
  /**
   * 组装查询条件
   * @param request
   * @return
   * @throws Exception 
   */
  private String toSerachWhere(Connection conn , YHPerson user,Map request) throws Exception{
    String whereStr = "";
    String flowId = request.get("flowId") != null ? ((String[])request.get("flowId"))[0] : null;
    String runId = request.get("runId") != null ? ((String[])request.get("runId"))[0] : null;
    String runName = request.get("runName") != null ? ((String[])request.get("runName"))[0] : null;
    String startTime = request.get("startTime") != null ? ((String[])request.get("startTime"))[0] : null;
    String endTime = request.get("endDate") != null ? ((String[])request.get("endDate"))[0] : null;
    String toId = request.get("toId") != null ? ((String[])request.get("toId"))[0] : null;
    String sortId = request.get("sortId") != null ? ((String[])request.get("sortId"))[0] : "";
    
    if(flowId != null && !"".equals(flowId)){
      whereStr +=  " and FLOW_ID=" + flowId +"";     
    }
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      whereStr +=  " and FLOW_TYPE.FLOW_SORT IN (" + sortId +") ";     
    }
    // 如果流水号 不等于空   加条件
    if(!"".equals(runId) && runId != null){
      whereStr +=   " and RUN_ID=" + runId +""; 
    }
    // 如果文件名称不为空的 话   加条件
    if(!"".equals(runName) && runName != null){
      whereStr +=  " and RUN_NAME like " + "'%" +  YHUtility.encodeLike(runName) + "%' " + YHDBUtility.escapeLike() ;
    }
    // --- “日期范围”条件，对应流程实例的创建时间BEGIN_TIME ---
    if(startTime != null && !"".equals(startTime)){
      startTime +=  " 00:00:00";
      String dbDateF = YHDBUtility.getDateFilter("BEGIN_TIME", startTime, " >= ");
      whereStr += " and " + dbDateF;
    }
    if(endTime != null && !"".equals(endTime)){
      endTime +=  " 23:59:59";
      String dbDateF = YHDBUtility.getDateFilter("BEGIN_TIME", endTime, " <= ");
      whereStr += " and " + dbDateF;
    }
    if(toId != null && !"".equals(toId)){
      whereStr += " AND  " + YHDBUtility.findInSet(toId,"BEGIN_USER");
    }
    String managerSql = getMyManageSql(conn, user);
    if(!"".equals(managerSql)){
      whereStr += " AND (" + managerSql + ")";
    }
    return whereStr;
  }
  /**
   * 得到管理权限
   * @param conn
   * @param user
   * @return
   * @throws Exception
   */
  public String getMyManageSql(Connection conn,YHPerson user) throws Exception{
    
    String result = "";
    if(user.isAdmin()){
      return "";
    }
    YHFlowWorkSearchLogic fw = new YHFlowWorkSearchLogic();
    String myDeptstr = fw.getMyDept(conn,user.getDeptId());
    String myRunId = fw.getMyFlowRun(conn,user.getSeqId());
    int loginUserId = user.getSeqId();
    String loginUserDept = String.valueOf(user.getDeptId());
    String loginUserPriv = user.getUserPriv();
    //全局监控权限
    result = YHDBUtility.findInSet(String.valueOf(loginUserId),fw.subStringIndex("|","MANAGE_USER",1,1))
       + " or "
       + YHDBUtility.findInSet(loginUserDept,fw.subStringIndex("|","MANAGE_USER",1,2))
       + " or "
       + YHDBUtility.findInSet("0",fw.subStringIndex("|","MANAGE_USER",1,2))
        + " or "
       + YHDBUtility.findInSet("ALL_DEPT",fw.subStringIndex("|","MANAGE_USER",1,2))
       + " or "
       + YHDBUtility.findInSet(loginUserPriv,fw.subStringIndex("|","MANAGE_USER",1,3));

    //部门监控、查询权限
    result += " or (PERSON.DEPT_ID IN (" + myDeptstr + ") "
      + " AND ("
      + YHDBUtility.findInSet(String.valueOf(loginUserId),fw.subStringIndex("|","MANAGE_USER_DEPT",1,1))
      + " or "
      + YHDBUtility.findInSet(loginUserDept,fw.subStringIndex("|","MANAGE_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet("0",fw.subStringIndex("|","MANAGE_USER_DEPT",1,2))
       + " or "
      + YHDBUtility.findInSet("ALL_DEPT",fw.subStringIndex("|","MANAGE_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet(loginUserPriv,fw.subStringIndex("|","MANAGE_USER_DEPT",1,3)) + "))"; 
    
    return result;
  }
 
  /**
   * 销毁工作
   * @throws Exception 
   */
  public int destroyFlowWork(Connection conn , String module,String runIds,int userId,String ip) throws Exception{
    int result = 0;
    if(runIds == null || "".equals(runIds)){
      return result;
    } 
    String[] runIdArray = runIds.split(",");
     for(int i = 0 ; i < runIdArray.length ; i++ ){
       if("".equals(runIdArray[i])){
         continue;
       }
       int runId = Integer.parseInt(runIdArray[i]);
       destroyFlowWork(conn, module, runId, userId ,ip);
       result ++;
     }
     return result;
  }
  public void destroyFlowWork(Connection conn , String module,int runId,int userId,String ip) throws Exception{
    //工作流日志
    String content = "销毁此工作";
    YHFlowRunLogLogic frll = new YHFlowRunLogLogic();
    frll.runLog(runId, 0, 0, userId, 4, content, ip, conn);
    //删除附件
    deleteAttachByWork(conn, YHWorkFlowConst.FLOW_RUN, runId, module);
    //删除会签附件
    deleteAttachByWork(conn, YHWorkFlowConst.FLOW_RUN_FEEDBACK, runId, module);
    //删除记录
    deleteFlowWorkUtil(conn, YHWorkFlowConst.FLOW_RUN_FEEDBACK, runId);
    deleteFlowWorkUtil(conn, YHWorkFlowConst.FLOW_RUN_DATA, runId);
    deleteFlowWorkUtil(conn, YHWorkFlowConst.FLOW_RUN_PRCS, runId);
    deleteFlowWorkUtil(conn, YHWorkFlowConst.FLOW_RUN, runId);
  }
  
  public void deleteFlowWorkUtil(Connection conn , String tableName, int runId) throws Exception{
    String sql = "delete from " + tableName + " where RUN_ID=" + runId;
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 删除附件 工作附件或者会签附件
   * @param conn
   * @param tableName
   * @param runId
   * @param module
   * @throws Exception
   */
  public void deleteAttachByWork(Connection conn , String tableName , int runId,String module) throws Exception{
    //删除附件
    String sql = "SELECT ATTACHMENT_ID,ATTACHMENT_NAME from " + tableName + " where RUN_ID=" + runId;
    PreparedStatement ps = null;
    ResultSet rs = null; 
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        String attachmentId = rs.getString(1);
        String attachmentName = rs.getString(2);
        if(attachmentId == null || "".equals(attachmentId.trim())){
          continue;
        }
        deleteAttach(conn, module, attachmentId, attachmentName);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
  }
  /**
   * 删除附件 工作附件或者会签附件
   * @param conn
   * @param tableName
   * @param runId
   * @param module
   * @throws Exception
   */
  public int recoverWork(Connection conn  , String runIds,String module,int userId) throws Exception{
    //删除附件
    int result = 0;
    if(runIds == null || "".equals(runIds)){
      return result;
    }
    String sql = "update "+ YHWorkFlowConst.FLOW_RUN +" set DEL_FLAG=0 where RUN_ID IN (" + runIds + ")";
    PreparedStatement ps = null;
    ResultSet rs = null; 
    try {
      ps = conn.prepareStatement(sql);
      result = ps.executeUpdate();
      String[] runIdArray = runIds.split(",");
      for (int i = 0; i < runIdArray.length; i++) {
        int runId = Integer.parseInt(runIdArray[i]);
        String content = "还原此工作";
        YHFlowRunLogLogic frll = new YHFlowRunLogLogic();
        frll.runLog(runId, 0, 0, userId, 5, content, "", conn);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 删除单个附件
   * @param conn
   * @param module
   * @param attachmentId
   * @param attachmentName
   * @throws Exception
   */
  public void deleteAttachSing(Connection conn,String module,String attachmentId,String attachmentName) throws Exception{
    String fileName = "";
    String path = "";
    if(attachmentName.trim().endsWith("*")){
      attachmentName = attachmentName.trim().substring(0,attachmentName.trim().length() - 1);
    }
    if(attachmentId.trim().endsWith(",")){
      attachmentId = attachmentId.trim().substring(0,attachmentId.trim().length() - 1);
    }
    if(attachmentId != null && !"".equals(attachmentId)){
      String attIds[] = attachmentId.split("_");
      fileName = attIds[1] + "_" + attachmentName;
      path = YHNtkoCont.ATTA_PATH + File.separator+ module+ File.separator+  attIds[0]+ File.separator+ fileName;
    }
    File file = new File(path);
    if(file.exists()){
      file.delete();
    }
    String sql = "delete from oa_attachment_access where ATTACHMENT_ID='" + attachmentId + "'";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 
   * @param conn
   * @param module
   * @param attachmentId
   * @param attachmentName
   * @throws Exception
   */
  public void deleteAttach(Connection conn,String module,String attachmentId,String attachmentName) throws Exception{
    String[] attachmentIds = attachmentId.split(",");
    String[] attachmentNames = attachmentName.split("\\*");
    for (int i = 0 ; i < attachmentIds.length ; i ++) {
      if("".equals(attachmentIds[i])){
        continue;
      }
      deleteAttachSing(conn, module, attachmentIds[i], attachmentNames[i]);
    }
  }
  
}

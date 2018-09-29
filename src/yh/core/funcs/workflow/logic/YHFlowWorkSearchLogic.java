package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.praser.YHPraseData2FormUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHFlowWorkSearchLogic {
  public StringBuffer getWorkList( Connection conn, Map request,YHPerson user , String sortId)
  throws Exception {
    StringBuffer resualt = new StringBuffer();
    String sql = "";
    String query = "";
    try {
      String flowId = request.get("flowList") != null ? ((String[])request.get("flowList"))[0] : null;
      String filedList = "";
      if (flowId != null && !"".equals(flowId) && !"0".equals(flowId)) {
        YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
        if (!YHUtility.isNullorEmpty(flowId)) {
          filedList = YHWorkFlowUtility.getOutOfTail(myWorkLogic.getFildList(conn, flowId));
        }
      }
      
      sql = "select" 
        + " FLOW_RUN.RUN_ID " 
        + " ,RUN_NAME " 
        + " ,BEGIN_TIME " 
        + " ,END_TIME "
        + " ,ATTACHMENT_ID " 
        + " ,ATTACHMENT_NAME ";
       
      if (!YHUtility.isNullorEmpty(filedList)) {
        String[] sss = filedList.split(",");
        for (int i = 0 ;i < sss.length ; i++) {
          sql += " , 1";
        }
      } 
      sql +=  ",FLOW_TYPE.SEQ_ID "
        + " ,LIST_FLDS_STR " 
        + " ,FLOW_NAME " 
        + " ,FREE_OTHER "
        + " ,FLOW_TYPE " 
        + " ,MANAGE_USER " 
        + " ,QUERY_USER " 
        + " ,COMMENT_PRIV " 
        + " ,FOCUS_USER " 
        + " ,EDIT_PRIV"
        + " ,FLOW_TYPE"
        + " , MANAGE_USER_DEPT"
        + ", QUERY_USER_DEPT"
        + " from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,PERSON  " 
        + " WHERE  " 
        + " FLOW_RUN.BEGIN_USER=PERSON.SEQ_ID " 
        + " and FLOW_TYPE.SEQ_ID=FLOW_RUN.FLOW_ID  "
        + " and FLOW_RUN.DEL_FLAG=0 ";
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        sql += " and FLOW_TYPE.FLOW_SORT in (" + sortId + ") ";
      }
      String whereStr = toWhereString(conn,request, user);
      query = " order by FLOW_RUN.RUN_ID desc"; // 按照流水号（实例ID）倒排序
      
      
      sql += whereStr + query;
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        this.getRunPrcs(record, conn,  user.getSeqId());
        this.getCommentPriv(record, conn, user);
        this.getEditPriv(record, conn, user);
        this.getFocusPriv(record, conn, user);
        this.getAttach(record, conn, user);
        
        if (!YHUtility.isNullorEmpty(filedList)) {
          this.getFileListData(conn, record, filedList);
        }
      }
      resualt.append(pageDataList.toJson());
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return resualt;
  }
  public void getFileListData(Connection conn ,YHDbRecord record , String filedList) throws Exception {
    YHFlowRunUtility ut = new YHFlowRunUtility();
    int runId = YHUtility.cast2Long(record.getValueByName("runId")).intValue();
    int flowId = YHUtility.cast2Long(record.getValueByName("flowId")).intValue();
    
    Map map = ut.getRunData(runId, conn, 0, flowId, filedList);
    Set<String> keys = map.keySet();
    for (String key : keys) {
      if (!YHUtility.isNullorEmpty(key)) {
        String value = YHUtility.encodeSpecial(YHUtility.null2Empty((String)map.get(key)));
        record.updateField(key, value);
      }
    }
    StringBuffer sb = new StringBuffer();
    
  }
  public void getAttach(YHDbRecord r , Connection conn , YHPerson user) throws Exception{
    String attachmentId = (String)r.getValueByName("attachId");
    String attachmentName = (String)r.getValueByName("attachName");
    String flowType = (String)r.getValueByName("flowType");
    int runId = YHUtility.cast2Long(r.getValueByName("runId")).intValue();
    int flowId = YHUtility.cast2Long(r.getValueByName("flowId")).intValue();
    if (attachmentName == null || "".equals(attachmentName.trim())) {
      return ;
    }
    if (attachmentId == null || "".equals(attachmentId.trim())) {
      return ;
    }
    String priv = "";
    if ("1".equals(flowType)) {
      YHAttachmentLogic logic = new YHAttachmentLogic();
      priv = logic.getDownPrintPriv((int)runId, (int)flowId, user.getSeqId(), conn);
    } else {
      priv = "1,1";
    }
    
    String[] attachsName = attachmentName.split("\\*");
    String[] attachsId = attachmentId.split(",");
    StringBuffer sb = new StringBuffer("[");
    for ( int i = 0 ;i < attachsId.length ;i ++ ) {
      String tmp = attachsId[i];
      String name = attachsName[i];    
      sb.append("{attachmentName:\"" + YHUtility.encodeSpecial(name) + "\"");
      sb.append(",attachmentId:'" + tmp + "'" +
          ",ext:'" +  YHFileUtility.getFileExtName(name) + "',priv:'"+ priv +"'},");
    }
    if ( attachsId.length > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    r.addField("attach2", sb.toString());
  }
  public void getFocusPriv(YHDbRecord r , Connection conn , YHPerson user) throws Exception{
    //关注该流程实例的用户中，没有当前用户 并且 流程未结束，显示“关注”超链接
    String focusUser = (String)r.getValueByName("focusUser");
    Timestamp endTime = (Timestamp)r.getValueByName("endTime");
    if (endTime == null && !YHWorkFlowUtility.findId(focusUser , String.valueOf(user.getSeqId()))) {
      r.addField("hasFocus", "1");
      r.addField("hasCalFocus", "0");
    } else if (YHWorkFlowUtility.findId(focusUser , String.valueOf(user.getSeqId()))) {
      r.addField("hasFocus", "0");
      r.addField("hasCalFocus", "1");
    } else {
      r.addField("hasFocus", "0");
      r.addField("hasCalFocus", "0");
    }
  }
  public void getEditPriv(YHDbRecord r , Connection conn , YHPerson user) throws Exception{
    Timestamp endTime = (Timestamp)r.getValueByName("endTime");
    String privStr = (String)r.getValueByName("editPriv");
    if (endTime != null) {
      if(privStr == null || user == null){
        return ;
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
      privDept = YHOrgSelectLogic.changeDept(conn, privDept); 
      if (user.isAdminRole() 
          || YHWorkFlowUtility.findId(privUser,String.valueOf(user.getSeqId())) 
          || YHWorkFlowUtility.findId(privDept,String.valueOf(user.getDeptId())) 
          || YHWorkFlowUtility.findId(privRole,user.getUserPriv())) {
        r.addField("hasEdit", "1");
      } else {
        r.addField("hasEdit", "0");
      }
    } else {
      r.addField("hasEdit", "0");
    }
  }
  
  public void getCommentPriv(YHDbRecord r , Connection conn , YHPerson user) throws Exception {
    //显示“点评”超链接 begin
    String commentPriv = (String)r.getValueByName("commentPriv");
    String manageUser = (String)r.getValueByName("manageUser");
    String queryUser = (String)r.getValueByName("queryUser");
    String queryUserDept =  (String)r.getValueByName("queryUserDept");
    String manageUserDept =  (String)r.getValueByName("manageUserDept");
    
    if (manageUser == null) {
      manageUser = "";
    }
    if (queryUser == null) {
      queryUser = "";
    }
    if (manageUserDept == null) {
      manageUserDept = "";
    }
    if (queryUserDept == null) {
      queryUserDept = "";
    }
    YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
    
    boolean mUserPriv = pu.checkPriv(user, manageUser) ;
    boolean qUserPriv = pu.checkPriv(user, queryUser) ;
    boolean qUserDeptPriv = pu.checkPriv(user, queryUserDept) ;
    boolean mUserDeptPriv = pu.checkPriv(user, manageUserDept) ;
    
    if (("3".equals(commentPriv) && (mUserPriv || qUserPriv || qUserDeptPriv|| mUserDeptPriv ))
        || ("2".equals(commentPriv) && (qUserPriv || qUserDeptPriv))
        || ("1".equals(commentPriv) && (mUserPriv || mUserDeptPriv))) {
      r.addField("hasComPriv", "1");
    } else {
      r.addField("hasComPriv", "0");
    }
  }
  public void getRunPrcs(YHDbRecord record , Connection conn , int userId) throws Exception {
    String opFlag = "";//主办人标识，用于显示委托、收回超链接
    int runId = YHUtility.cast2Long(record.getValueByName("runId")).intValue();
    String freeOther = (String)record.getValueByName("freeOther");
    String query = "select  PRCS_ID , PRCS_FLAG , FLOW_PRCS , OP_FLAG from  oa_fl_run_prcs where CHILD_RUN = 0 AND  RUN_ID=" + runId  + " and USER_ID=" + userId + " and PRCS_FLAG <>'4'  order by PRCS_FLAG " ;//“limit 1”的作用？针对并发或者子流程？
    Statement stm1 = null;
    ResultSet rs1 = null;
    int prcsId = 0 ;
    int flowPrcs = 0 ;
    String prcsFlag = "";
    try {
      stm1 = conn.createStatement();
      rs1 = stm1.executeQuery(query);
      if ( rs1.next()) {
        prcsId = rs1.getInt("PRCS_ID");//实际步骤顺序号
        prcsFlag= rs1.getString("PRCS_FLAG");//处理标识（1-未接收；2-办理中；3-转交下一步，下一步经办人无人接收；4-已办结）
        flowPrcs = rs1.getInt("FLOW_PRCS");//步骤定义ID
        opFlag = rs1.getString("OP_FLAG");//主办人标识
      } 
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, rs1, null); 
    }
    record.addField("prcsId", prcsId);
    record.addField("opFlag", opFlag);
    record.addField("flowPrcs", flowPrcs);
    record.addField("prcsFlag", prcsFlag);
    
    if ("1".equals(prcsFlag) || "2".equals(prcsFlag)) {
      record.addField("hasHandler", "1");
      if (("1".equals(freeOther) && "1".equals(opFlag))
          || "2".equals(freeOther)
          || "3".equals(freeOther)) {
        record.addField("hasOther", "1");
      } else {
        record.addField("hasOther", "0");
      }
      record.addField("hasCallback", "0");
    } else if ("3".equals(prcsFlag) && "1".equals(opFlag)) {//转交下一步，下一步经办人无人接收 并且当前用户是主办人
      record.addField("hasCallback", "1");
      record.addField("hasHandler", "0");
      record.addField("hasOther", "0");
    } else {
      record.addField("hasCallback", "0");
      record.addField("hasHandler", "0");
      record.addField("hasOther", "0");
    }
  }
  /**
   * 组装查询条件语句
   * @param request
   * @param user
   * @return
   * @throws Exception
   */
  public String toWhereString(Connection conn ,Map request,YHPerson user) throws Exception{
    String whereStr = "";
    String startDateStr = request.get("startTime") != null ? ((String[])request.get("startTime"))[0] : null;
    String endDateStr = request.get("endTime") != null ? ((String[])request.get("endTime"))[0] : null;
    String flowId = request.get("flowList") != null ? ((String[])request.get("flowList"))[0] : null;
    String toIdStr = request.get("toId") != null ? ((String[])request.get("toId"))[0] : null;
    String runId = request.get("runId") != null ? ((String[])request.get("runId"))[0] : null;
    String runName = request.get("runName") != null ? YHDBUtility.escapeLike(((String[])request.get("runName"))[0]) : null;
    String flowStatus = request.get("flowStatus") != null ? ((String[])request.get("flowStatus"))[0] : null;
    String flowQueryType = request.get("flowQueryType") != null ? ((String[])request.get("flowQueryType"))[0] : null;
    //System.out.println(startDateStr+"::"+endDateStr+"::"+flowId+"::"+toIdStr);
    /*
     * 选中的流程定义ID 如果选了一个流程定义ID，则SQL语句中加上这个字段的条件 flowlist相当于 选中的id
     */
    if (flowId != null && !"".equals(flowId) && !"0".equals(flowId)) {
      whereStr += " and FLOW_TYPE.SEQ_ID="+ flowId ;
    }
    // --- “流水号”条件 ---
    if (runId != null && !"".equals(runId)) {
      whereStr += " and FLOW_RUN.RUN_ID=" + runId ;
    }
    // --- “工作名称/文号”条件 ---
    if (runName != null && !"".equals(runName) ) {
      whereStr  += " and FLOW_RUN.RUN_NAME like " + "'%" + YHUtility.encodeLike(runName) + "%' "  + YHDBUtility.escapeLike();
    }
    // --- “日期范围”条件，对应流程实例的创建时间BEGIN_TIME ---
    if(startDateStr != null && !"".equals(startDateStr)){
      startDateStr +=  " 00:00:00";
      String dbDateF = YHDBUtility.getDateFilter("BEGIN_TIME", startDateStr, " >= ");
      whereStr += " and " + dbDateF;
    }
    if(endDateStr != null && !"".equals(endDateStr)){
      endDateStr +=  " 23:59:59";
      String dbDateF = YHDBUtility.getDateFilter("BEGIN_TIME", endDateStr, " <= ");
      whereStr += " and " + dbDateF;
    }
    // --- “流程状态”条件 ---
    if (flowStatus != null && !flowStatus.equals(("ALL"))) {
      if (flowStatus.equals("0")) {
        whereStr +=  " and FLOW_RUN.END_TIME is null";
      } else {
        whereStr +=  " and FLOW_RUN.END_TIME is not null";
      }
    }
    if (flowQueryType != null && !flowQueryType.equals((""))) {
      
      int toId = -1;
      if (YHUtility.isInteger(toIdStr)) {
        toId = Integer.parseInt(toIdStr);
      } 
      whereStr += fqt2WhereString(conn,flowQueryType ,user,toId);
    }
    return whereStr;
  }
  /**
   * 查询权限范围
   * @param flowQueryType
   * @param user
   * @return
   * @throws Exception 
   */
  public String fqt2WhereString(Connection conn,String flowQueryType,YHPerson user,int toId) throws Exception{
    String result = "";
    int beginUser = -1;
    String myDeptstr = getMyDept(conn,user.getDeptId());
    String myRunId  = " select  DISTINCT(FLOW_RUN.RUN_ID) from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID AND USER_ID=" + user.getSeqId() + " and PRCS_FLAG <> 5 ";
      //getMyFlowRun(conn,user.getSeqId());
    if("".equals(myRunId)){
      myRunId = "-1";
    }
    /*--- 查询范围 ---
     *"ALL"---所有范围
     *"1"---我发起的
     *"2" ---我经办的
     *"3" ---我管理的
     *"4" ---我关注的
     *"5" ---指定发起人
    */
    if ("0".equals(flowQueryType) && !user.isAdminRole()) {
      result +=" and (FLOW_RUN.RUN_ID in (" + myRunId + ") or " + getMyManageSql(myDeptstr,myRunId,user.getUserPriv(),user.getSeqId(),String.valueOf(user.getDeptId())) + ")";
    } else if ("1".equals(flowQueryType)) {
      beginUser = user.getSeqId();
    } else if ("2".equals(flowQueryType)) {
      result += " and FLOW_RUN.RUN_ID in (" + myRunId + ")";
    } else if ("3".equals(flowQueryType) && !user.isAdminRole()) {
      result += " and (" + getMyManageSql(myDeptstr,myRunId,user.getUserPriv(),user.getSeqId(),String.valueOf(user.getDeptId())) + ")";
    } else if ("4".equals(flowQueryType)) {
      result += " and " + YHDBUtility.findInSet(String.valueOf(user.getSeqId()),"FOCUS_USER");
    } else if ("5".equals(flowQueryType)) {
      if (!user.isAdminRole()) {
        result +=  " and (" + getMyManageSql(myDeptstr,myRunId,user.getUserPriv(),user.getSeqId(),String.valueOf(user.getDeptId())) + ")";
      } 
      beginUser = toId;
    } 
    if(beginUser > 0){
      result += " and BEGIN_USER=" + beginUser + "";
    }
    return result;
  }
  /**
   * 得到自己的管理权限
   * @param myDeptstr
   * @param myRunId
   * @param loginUserPriv
   * @param loginUserId
   * @param loginUserDept
   * @return
   * @throws SQLException
   */
  public String getMyManageSql(String myDeptstr,String myRunId,String loginUserPriv,int loginUserId,String loginUserDept) throws SQLException{
    String result = "";
    //全局监控权限
    result = YHDBUtility.findInSet(String.valueOf(loginUserId),subStringIndex("|","MANAGE_USER",1,1))
       + " or "
       + YHDBUtility.findInSet(loginUserDept,subStringIndex("|","MANAGE_USER",1,2))
       + " or "
       + YHDBUtility.findInSet("0",subStringIndex("|","MANAGE_USER",1,2))
       + " or "
       + YHDBUtility.findInSet("ALL_DEPT",subStringIndex("|","MANAGE_USER",1,2))
       + " or "
       + YHDBUtility.findInSet(loginUserPriv,subStringIndex("|","MANAGE_USER",1,3));

    //全局查询权限
    result += " or "
      + YHDBUtility.findInSet(String.valueOf(loginUserId),subStringIndex("|","QUERY_USER",1,1))
      + " or "
      + YHDBUtility.findInSet(loginUserDept,subStringIndex("|","QUERY_USER",1,2))
      + " or "
       + YHDBUtility.findInSet("0",subStringIndex("|","QUERY_USER",1,2))
       + " or "
       + YHDBUtility.findInSet("ALL_DEPT",subStringIndex("|","QUERY_USER",1,2))
      + " or "
      + YHDBUtility.findInSet(loginUserPriv,subStringIndex("|","QUERY_USER",1,3));
    //部门监控、查询权限
    result += " or (PERSON.DEPT_ID IN (" + myDeptstr + ") "
      + " AND ("
      + YHDBUtility.findInSet(String.valueOf(loginUserId),subStringIndex("|","MANAGE_USER_DEPT",1,1))
      + " or "
      + YHDBUtility.findInSet(loginUserDept,subStringIndex("|","MANAGE_USER_DEPT",1,2)) 
      + " or "
      + YHDBUtility.findInSet("0",subStringIndex("|","MANAGE_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet("ALL_DEPT",subStringIndex("|","MANAGE_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet(loginUserPriv,subStringIndex("|","MANAGE_USER_DEPT",1,3))
      + " or "
      + YHDBUtility.findInSet(String.valueOf(loginUserId),subStringIndex("|","QUERY_USER_DEPT",1,1))
      + " or "
      + YHDBUtility.findInSet(loginUserDept,subStringIndex("|","QUERY_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet("0",subStringIndex("|","QUERY_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet("ALL_DEPT",subStringIndex("|","QUERY_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet(loginUserPriv,subStringIndex("|","QUERY_USER_DEPT",1,3)) + "))"; 
    return result;
  }
  /**
   * 的到flowRunId
   * @return
   * @throws Exception 
   */
  public String getMyFlowRun(Connection conn ,int loginUserId) throws Exception{
    String result = "";
    String sql = "";
    sql = "select  DISTINCT(FLOW_RUN.RUN_ID) from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID AND USER_ID=" + loginUserId + " and PRCS_FLAG <> 5";
    sql += " order by FLOW_RUN.RUN_ID desc";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      ArrayList<Integer> temp = new ArrayList<Integer>();
      while (rs.next()) {
        int runId = rs.getInt(1);
       // if(temp.contains(runId)){
          //continue;
        //}
        //temp.add(runId);
        if(!"".equals(result)){
          result += ",";
        }
        result += runId;
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  /**
   * 
   * @param conn
   * @param loginDeptId
   * @return
   * @throws Exception
   */
  public String getMyDept(Connection conn ,int loginDeptId) throws Exception{
    String result = "";
    String preantDeptId = getParentDept(conn, loginDeptId);
    if(!"".equals(preantDeptId)){
      result += loginDeptId + "," + preantDeptId;
    }else{
      result += loginDeptId ;
    }
    return result;
  }

/**
 * 
 * mysql subString_index 处理
 * @param str
 * @param dbFieldName
 * @param index
 * @param startIndex
 * @return
 * @throws SQLException
 */
  public String subStringIndex(String str,String dbFieldName,int startIndex,int index) throws SQLException{
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String result = "";
    if (dbms.equals("sqlserver")) {
      result = "dbo.getTokenByIndex(" + dbFieldName + ",'"+ str + "'," + index + ")";
    }else if (dbms.equals("mysql")) {
      if(index == 1){
        result = "SUBSTRING_INDEX(" + dbFieldName + ",'"+ str + "',1)";
      }else if(index == 2){
        result = "SUBSTRING_INDEX(SUBSTRING_INDEX(" + dbFieldName + ",'"+ str + "',2),'"+ str + "',-1)";
      }else if(index == 3){
        result = "SUBSTRING_INDEX(" + dbFieldName + ",'"+ str + "',-1)";
      }
    }else if (dbms.equals("oracle")) {
      dbFieldName = dbFieldName + "||'|'";
      if(index == 1){
         startIndex = 0;
      }
      result = "substr(" + dbFieldName + ",instr(" + dbFieldName + ",'" + str + "'," + startIndex + "," + ((index -1) == 0 ? 1:index-1) + ") + 1,instr(" + dbFieldName + ",'" + str + "',1," + index + ")-1 -instr(" + dbFieldName + ",'" + str + "'," + startIndex + "," + ((index -1) == 0 ? 1:index-1) + ")) ";
      }else {
      throw new SQLException("not accepted dbms");
    }
    
    return result;
  }
  /**
   * 判断parentDeptId是否是childDeptId的上级部门
   * 
   * @param parentDeptId
   * @param childDeptId
   * @return
   * @throws Exception
   */
  public  String getParentDept(Connection conn,
      int childDeptId) throws Exception {
    String result = "";
    String sql = "select " + " SEQ_ID " + " from " + " oa_department "
        + " where " + " DEPT_PARENT = " + childDeptId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()) {
        int dept = rs.getInt(1);
        if(dept != 0){
          if(!"".equals(result)){
            result += ",";
          }
          result += dept;
          String temp =  getParentDept(conn, dept);
          if(temp != null && !"".equals(temp)){
            if(!"".equals(result)){
              result += ",";
            }
            result += temp;
          }
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  public StringBuffer getManagerRoleLogic(Connection conn , YHPerson user , String runId ) throws Exception{
    String sql = " SELECT PRCS_ID,PRCS_FLAG,FLOW_PRCS,OP_FLAG from oa_fl_run_prcs WHERE RUN_ID=" + runId + " AND USER_ID=" + user.getSeqId() + " and not PRCS_FLAG = '4' order by PRCS_FLAG ";
    StringBuffer sb = new StringBuffer("{");
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int prcsId = rs.getInt(1);
        String prcsFlag = rs.getString(2);
        String flowPrcs = rs.getString(3);
        String opFlag = rs.getString(4);
        sb.append("prcsFlag:\"").append(prcsFlag).append("\"");
        sb.append(",opFlag:\"").append(opFlag).append("\"");
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
    if(!sb.toString().endsWith("{")){
      sb.append(",");
    }
    sb.append("userRole:\"").append(user.getUserPriv()).append("\"");
    sb.append(",userDept:").append(user.getDeptId());
    sb.append(",isAdmin:").append(user.isAdmin());
    sb.append(",userId:").append(user.getSeqId());
    return sb.append("}");
  }
  public int getCountBySql(Connection conn , String sql) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    int result = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      while (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return result;
  }
  public Map getWorkCount(int flowId , int userId , Connection conn) throws Exception {
    Map result = new HashMap();
    String query = "select count(*) from oa_fl_run WHERE DEL_FLAG='0' and END_TIME is null AND FLOW_ID=" + flowId;
    int handler = this.getCountBySql(conn, query);
     query = "select count(*) from oa_fl_run WHERE DEL_FLAG='0' and END_TIME is not null AND FLOW_ID=" + flowId;
    int end = this.getCountBySql(conn, query);
    String query2 = "select FLOW_RUN_PRCS.PRCS_FLAG,FLOW_RUN.RUN_ID from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS WHERE FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID AND FLOW_RUN.DEL_FLAG='0' AND FLOW_RUN_PRCS.CHILD_RUN='0' AND FLOW_RUN.FLOW_ID='"+flowId+"' AND FLOW_RUN_PRCS.USER_ID=" + userId;
    Statement stm2 = null;
    ResultSet rs2 = null;
    int newCount = 0 ;
    int dealCount = 0;
    int overCount = 0 ;
    String runIds = "";
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query2);
      while (rs2.next()) {
        String prcsFlag = rs2.getString("PRCS_FLAG");
        int runId =  rs2.getInt("RUN_ID");
        String sRunId =  String.valueOf(runId);
        if ("1".equals(prcsFlag)) {
          newCount++;
        } else if ("2".equals(prcsFlag)) {
          dealCount++;
        } else if (("3".equals(prcsFlag) || "4".equals(prcsFlag))
            && !YHWorkFlowUtility.findId(runIds,sRunId)) {
          overCount++ ;
          runIds += sRunId + ",";
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null);
    }
    result.put("handlerCount", handler);
    result.put("endCount",end);
    result.put("newCount", newCount);
    result.put("dealCount", dealCount);
    result.put("overCount", overCount);
    return result;
  }
  public void getDeptTreeName(int deptId , StringBuffer sb , int level , String value, Connection conn) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    YHDeptLogic logic = new YHDeptLogic();
    List<YHDepartment> list = logic.getDeptByParentId(deptId , conn);
    for(int i = 0 ;i < list.size() ;i ++){
      String flag = "├";
      if(i == list.size() - 1 ){
        flag = "└";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += "│";
      }
      flag = tmp + flag;
      YHDepartment dp = list.get(i);
      String dept = String.valueOf(dp.getSeqId());
      sb.append("<option value='" + dp.getDeptName()  + "' " );
      if (dept.equals(value)) {
        sb.append(" selected ");
      }
      sb.append(">");
      sb.append(flag + dp.getDeptName());
      sb.append("</option>");
      this.getDeptTreeName(dp.getSeqId(), sb, level + 1 , value , conn);
    }
  }
  public String getFormItem(int flowId, YHPerson user, Connection conn, int versionNo) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    String query = "select form_seq_id , query_item from oa_fl_type where seq_id=" + flowId;
    Statement stm1 = null; 
    ResultSet rs1 = null; 
    int formId = 0;
    String queryItem = "";
    try { 
      stm1 = conn.createStatement(); 
      rs1 = stm1.executeQuery(query); 
      if (rs1.next()){ 
        formId = rs1.getInt("form_seq_id");
        queryItem = rs1.getString("query_item");
        if (queryItem == null) {
          queryItem = "";
        }
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm1, rs1, null); 
    } 
    sb.append("{queryItem:'" + queryItem + "',items:");
    YHORM orm = new YHORM();
    Map formItemQuery = new HashMap();
    YHFormVersionLogic lo = new YHFormVersionLogic();
    int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
    formItemQuery.put("FORM_ID", formSeqId);
    List<YHFlowFormItem> list = orm.loadListSingle(conn, YHFlowFormItem.class , formItemQuery);
    sb.append("[");
    int count = 0;
    for (YHFlowFormItem item : list) {
      String tag = item.getTag();
      String content = item.getContent();
      String eValue = item.getValue();
      String title = item.getTitle();
      String clazz = item.getClazz();
      int itemId = item.getItemId();
      
      title = title.replace("<", "&lt");
      title = title.replace(">", "&gt");
      if ("DATE".equals(clazz) || "USER".equals(clazz)) {
        continue;
      }
      if ("INPUT".equals(tag)) {
        if (content.indexOf("type=checkbox") != -1) {
          content = "<SELECT id='DATA_"+itemId+"'  name='DATA_"+itemId+"'  class=SmallSelect><option value='SELECT_ALL_VALUE'>所有</option><option value='CHECKBOX_ON'>是</option><option value='CHECKBOX_OFF'>否 </option> </SELECT>";
          tag = "SELECT";
        } else {
          content = "<INPUT id='DATA_"+itemId+"'  name='DATA_"+itemId+"' value='' type=text size=30 class=SmallInput>";
        }
      } else if ("SELECT".equals(tag)) {
        if (!"AUTO".equals(clazz)) {
          content = content.replace("SELECT", "select");
          int pos = content.indexOf(">") + 1;
          int pos1 = content.indexOf("</select>", pos);
          eValue = content.substring(pos, pos1);
          String selected = "selected";
          if (eValue.indexOf("selected") > 0) {
            //selected = "selected";
            eValue = eValue.replace("selected", "");
          }
          content = "<SELECT name='DATA_"+itemId+"'  class=SmallSelect>";
          content += "<option value='SELECT_ALL_VALUE' "+selected+">所有</option>";
          content += eValue;
          content += "</SELECT>";
        } else {
          int autoValueCount = 0 ;
          String dataFld = item.getDatafld();
          content = "<SELECT name='DATA_"+itemId+"' class=SmallSelect>"
                        +"<option value='SELECT_ALL_VALUE'>所有</option>";
          if ("SYS_LIST_DEPT".equals(dataFld)) {
            StringBuffer sb2 = new StringBuffer();
            this.getDeptTreeName(0, sb2, 0 , "", conn);
            content += sb2.toString();
          } else if ("SYS_LIST_USER".equals(dataFld) 
              || "SYS_LIST_PRCSUSER1".equals(dataFld)
              || "SYS_LIST_PRCSUSER2".equals(dataFld)) {
            String queryAuto = "select " 
              + " PERSON.SEQ_ID " 
              + ", USER_NAME " 
              + " from  PERSON , USER_PRIV where "
              + " PERSON.USER_PRIV = USER_PRIV.SEQ_ID "
              + " order by PRIV_NO , USER_NO , USER_NAME ";
            Statement stm = null;
            ResultSet rs = null ;
            try {
              stm = conn.createStatement();
              rs = stm.executeQuery(queryAuto);
              while (rs.next()) {
                int userId = rs.getInt("SEQ_ID");
                String userName = rs.getString("USER_NAME");
                content += "<option value ='" + userName + "' ";
                //String sUserId = String.valueOf(userId);
                content += ">" + userName + "</option>";
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm, rs, null); 
            }
          } else if ("SYS_LIST_PRIV".equals(dataFld)) {
            String queryAuto = "SELECT SEQ_ID " 
              + " ,PRIV_NAME " 
              + "  from USER_PRIV  " 
              + " order by PRIV_NO";
            Statement stm = null;
            ResultSet rs = null ;
            try {
              stm = conn.createStatement();
              rs = stm.executeQuery(queryAuto);
              while (rs.next()) {
                int userPriv = rs.getInt("SEQ_ID");
                String privsName = rs.getString("PRIV_NAME");
                content += "<option value ='" + privsName + "' ";
                content += ">" + privsName + "</option>";
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm, rs, null); 
            }
          } else if ("SYS_LIST_SQL".equals(dataFld)) {
            String dataStr = item.getDatasrc();
            content = content.replaceAll(dataStr , "");
            dataStr = YHPraseData2FormUtility.replaceSql(conn, user, dataStr);
            Statement stm2 = null;
            ResultSet rs2 = null ;
            try {
              stm2 = conn.createStatement();
              rs2 = stm2.executeQuery(dataStr);
              while (rs2.next()) {
                String autoValueSql = rs2.getString(1);
                content += "<option value ='" + autoValueSql + "' ";
                content += ">" + autoValueSql + "</option>";
                autoValueCount++;
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm2, rs2, null); 
            }
            if (autoValueCount > 50 || autoValueCount == 0){
              content = "<INPUT name='DATA_"+itemId+"'  value='' type=text size=30 class=SmallInput>";
              tag = "INPUT";
            }
          }
          if (!"SYS_LIST_SQL".equals(dataFld) && (autoValueCount > 50 || autoValueCount == 0)) {
            content += "</SELECT>";
          }
          if (autoValueCount > 50 || autoValueCount == 0){
            tag = "INPUT";
          }
        }
      } else if ("SIGN".equals(clazz)) {
        continue;
      } else if ("TEXTAREA".equals(tag) || "LIST_VIEW".equals(clazz)) {
        content = "<INPUT name='DATA_"+itemId+"'  value='' type=text size=30 class=SmallInput>";
        tag = "INPUT";
        if ("LIST_VIEW".equals(clazz)) {
          String lvTitle = item.getLvTitle();
          String listView = title + "~" + itemId + "~" + lvTitle+ "^";
        }
      } 
      boolean hasAll = false;
      if ("INPUT".equals(tag)) {
        hasAll = true;
      }
      count++;
      sb.append("{");
      sb.append("itemId:" + itemId);
      sb.append(",title:'" + title + "'");
      sb.append(", hasAll:" + hasAll);
      sb.append(",content:\"" + content + "\"");
      sb.append("},");
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]}");
    return sb.toString();
  }
  
  
}

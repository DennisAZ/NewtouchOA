package yh.user.taiji.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHWorkListLogic {
  /**
   * 工作列表
   * @param conn
   * @param userId
   * @param pwd
   * @param returnLength
   * @param flowIdStr
   * @param sortId
   * @return
   * @throws Exception 
   * @throws Exception
   */
  public String[][] getHandleList(Connection dbConn ,String userId  , int returnLength  , String type  , String startDateStr ,String endDateStr , String flowIdStr ,String runName ,String flowStatus ,String flowQueryType , String toId) throws Exception  {
      YHPerson person = this.getPerson(dbConn, userId);
      if (person == null) {
        return null;
      }
      List<Map> list = this.getWorkList2(dbConn  ,person
          , returnLength
          , type
          , ""
          ,  startDateStr , endDateStr ,  flowIdStr , runName , flowStatus , flowQueryType ,  toId);
      String[][] result = this.listToArray(list);
      return result;
  }
  /**
   * 取得相关人员
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  private YHPerson getPerson(Connection conn , String userId ) throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("USER_ID", userId);
    YHPerson person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, map);
    return person;
  }
  private List<Map> getWorkList2( Connection conn  , YHPerson user
      , int showLen
      , String type
      , String opFlag
      , String startDateStr ,String endDateStr , String flowIdStr ,String runName ,String flowStatus ,String flowQueryType , String toId) throws Exception {
      String sql = "";
      String query = "";
      Statement stm = null;
      ResultSet rs = null;
      List<Map> list = new ArrayList();
      try {
        sql = "select" 
          + " FLOW_RUN.RUN_ID " 
          + " ,RUN_NAME " 
          + " ,BEGIN_TIME " 
          + " ,END_TIME "
          + " ,ATTACHMENT_ID " 
          + " ,ATTACHMENT_NAME "
          
          + " ,FLOW_TYPE.SEQ_ID AS FLOW_ID"
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
          + " from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,PERSON " 
          + " WHERE  " 
          + " FLOW_RUN.BEGIN_USER=PERSON.SEQ_ID  " 
          + " and FLOW_TYPE.SEQ_ID=FLOW_RUN.FLOW_ID  "
          + " and FLOW_RUN.DEL_FLAG=0 ";
        String whereStr = this.toWhereString(conn, user, startDateStr, endDateStr, flowIdStr, runName, flowStatus, flowQueryType, toId);
        query = " order by FLOW_RUN.RUN_ID desc"; // 按照流水号（实例ID）倒排序
  
        sql += whereStr + query;
        
        stm = conn.createStatement();
        rs = stm.executeQuery(sql);
        int count = 0;
        while (rs.next() && count < showLen) {
          Map mapTmp = new HashMap();
          int runId = rs.getInt("RUN_ID");
          mapTmp.put("runId", runId);
          Date date = rs.getDate("END_TIME");
          String state = "办理中";
          if(date != null){
            state = "已结束";
          }
          mapTmp.put("state", state);
          int flowId = rs.getInt("FLOW_ID");
          mapTmp.put("flowId", flowId);
          String flowName = rs.getString("FLOW_NAME");
          mapTmp.put("flowName", flowName);
          String runNameStr = rs.getString("RUN_NAME");
          runNameStr = YHWorkFlowUtility.getRunName(runNameStr);
          mapTmp.put("runName", runNameStr);
          String flowType = rs.getString("FLOW_TYPE");
          mapTmp.put("flowType", flowType);
          String freeOther = rs.getString("FREE_OTHER");
          mapTmp.put("freeOther", freeOther);
          boolean flag = this.getRunPrcs(conn, user.getSeqId(), type, runId, freeOther, mapTmp);
          if (!flag) {
            continue;
          }
          count++;
          list.add(mapTmp);
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      } 
      return list;
  }
  public boolean getRunPrcs(Connection conn , int userId , String type , int runId , String freeOther , Map map) throws Exception {
    String opFlag = "";//主办人标识，用于显示委托、收回超链接
    String tmp = "";
    if ("1".equals(type) || "2".equals(type)) {
      tmp = " and PRCS_FLAG = '" + type + "' ";
    } else if ("3".equals(type)) {
      tmp = " and (PRCS_FLAG = '3' or  PRCS_FLAG = '4') ";
    }
    String query = "select  PRCS_ID , PRCS_FLAG , FLOW_PRCS , OP_FLAG from  oa_fl_run_prcs where RUN_ID=" + runId  + " and USER_ID=" + userId + tmp + "  order by PRCS_FLAG , PRCS_TIME Desc" ;//“limit 1”的作用？针对并发或者子流程？
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
      } else {
        return false;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, rs1, null); 
    }
    
    map.put("prcsId", prcsId);
    map.put("opFlag", opFlag);
    map.put("flowPrcs", flowPrcs);
    map.put("prcsFlag", prcsFlag);
    return true;
  }
    /**
     * 组装查询条件语句
     * @param request
     * @param user
     * @return
     * @throws Exception
     */
    public String toWhereString(Connection conn ,YHPerson user , String startDateStr ,String endDateStr ,String  flowId ,String runName ,String flowStatus ,String flowQueryType , String toIdStr) throws Exception{
      String whereStr = "";
      //System.out.println(startDateStr+"::"+endDateStr+"::"+flowId+"::"+toIdStr);
      /*
       * 选中的流程定义ID 如果选了一个流程定义ID，则SQL语句中加上这个字段的条件 flowlist相当于 选中的id
       */
      if (flowId != null && !"".equals(flowId) && !"0".equals(flowId)) {
        whereStr += " and FLOW_TYPE.SEQ_ID="+ flowId ;
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
      if (flowStatus != null && !flowStatus.equals(("ALL")) && !"".equals(flowStatus)) {
        if (flowStatus.equals("0")) {
          whereStr +=  " and FLOW_RUN.END_TIME is null";
        } else {
          whereStr +=  " and FLOW_RUN.END_TIME is not null";
        }
      }
      if (flowQueryType != null && !"".equals(flowQueryType)) {
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
      String myRunId = getMyFlowRun(conn,user.getSeqId());
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
      } else if ("2".equals(flowQueryType) || "".equals(flowQueryType) || flowQueryType == null) {
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
       /* if(startIndex == -1){
          index = -1;
        }
        result = "SUBSTRING_INDEX(" + dbFieldName + ",'" + str + "'," + index + ")";*/
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
    /**
     * 的到flowRunId
     * @return
     * @throws Exception 
     */
    public String getMyFlowRun(Connection conn ,int loginUserId) throws Exception{
      String result = "";
      String sql = "";
      sql = "select FLOW_RUN.RUN_ID from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID AND USER_ID=" + loginUserId + " and not PRCS_FLAG=5";
      sql += " group by FLOW_RUN.RUN_ID";
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        while (rs.next()) {
          int runId = rs.getInt(1);
          if(temp.contains(runId)){
            continue;
          }
          temp.add(runId);
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
   * 取得工作列表
   * @param user
   * @param flowIdStr
   * @param showLen
   * @param opFlag
   * @param sortId
   * @param conn
   * @param type
   * @return
   * @throws Exception
   */
  private List<Map> getWorkList( YHPerson user
      , String flowIdStr
      , int showLen
      , String opFlag
      , String sortId
      , Connection conn 
      , String type) throws Exception {
   // TODO Auto-generated method stub
   List<Map> list = new ArrayList();
   Statement stm =null;
   ResultSet rs = null;//结果集
   try {
     int pageCount = 0;//页码数
     int recordCount = 0;//总记录数
     int pgStartRecord = 0;//开始索引
     int pgEndRecord = 0;//结束索引
     int userId = user.getSeqId();//用户ID
     
     String tmp = "";
     if(opFlag != null && ("1".equals(opFlag) || "0".equals(opFlag))){
       tmp = " and f.OP_FLAG = '"+ opFlag +"' ";
     }
     String typeStr = "";
     //未接收的
     if ("1".equals(type)) {
       typeStr =  " and  f.PRCS_FLAG='1' ";
       //正在办理的
     } else if ("2".equals(type)) {
       typeStr =  " and  f.PRCS_FLAG='2' ";
     } else if ("3".equals(type)) {
       typeStr =  " and  ( f.PRCS_FLAG='3' or f.PRCS_FLAG = '4' ) ";
     } 
     stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
     String queryFlowRunPrcsSql ="select r.FLOW_ID"
       + ",f.FLOW_PRCS"
       + ",r.RUN_NAME"
       + ",p.PRCS_NAME"
       + ",f.PRCS_ID"
       + ",r.RUN_ID"
       + ",t.FLOW_NAME"
       + ",f.SEQ_ID"
       + ",f.OP_FLAG"
       + ",r.END_TIME"
       + ",u.USER_NAME"
       + ",u.SEQ_ID as userId" 
       + ",t.FLOW_TYPE"
       + " from oa_fl_type t,oa_fl_run r,oa_fl_run_prcs f,oa_fl_process p, PERSON u , oa_fl_sort s where "
       + " t.SEQ_ID=r.FLOW_ID "
       + " and s.SEQ_ID=t.FLOW_SORT "
       + " and ((t.FLOW_TYPE = '1' AND t.SEQ_ID=p.FLOW_SEQ_ID  AND f.FLOW_PRCS=p.PRCS_ID) OR t.FLOW_TYPE = '2' )"
       + " and r.BEGIN_USER = u.SEQ_ID "
       + " and f.RUN_ID=r.RUN_ID "
       + " and r.DEL_FLAG<>'1'" 
       + typeStr
       + tmp 
       + " and f.USER_ID="+ userId ;
     if(YHUtility.isInteger(flowIdStr) && !"0".equals(flowIdStr)) {
       queryFlowRunPrcsSql = queryFlowRunPrcsSql + " and t.SEQ_ID='" + Integer.parseInt(flowIdStr)+ "'";
     }
     if (!YHUtility.isNullorEmpty(sortId)) {
       sortId = YHWorkFlowUtility.getOutOfTail(sortId);
       queryFlowRunPrcsSql += " and s.SEQ_ID IN (" + sortId + ")";
     }
     queryFlowRunPrcsSql += " order by r.RUN_ID desc , f.PRCS_ID desc";
     rs = stm.executeQuery(queryFlowRunPrcsSql); 
    
     String seqIdStr = "";
     String runIdStr = "";
     int count = 0 ;
     while (rs.next() && count < showLen) {
       int seqId = rs.getInt("SEQ_ID");
       int runId = rs.getInt("RUN_ID");
       if (!YHWorkFlowUtility.findId(seqIdStr, String.valueOf(seqId))
           && !YHWorkFlowUtility.findId(runIdStr, String.valueOf(runId))) {
         count++;
         seqIdStr += rs.getInt("SEQ_ID") + ",";
         runIdStr += runId + ",";
         
         Date date = rs.getDate("END_TIME");
         String state = "办理中";
         if(date != null){
           state = "已结束";
         }
         Map mapTmp = new HashMap();
         mapTmp.put("seqId", seqId);
         mapTmp.put("runId", runId);
         mapTmp.put("state", state);
         int flowId = rs.getInt("FLOW_ID");
         mapTmp.put("flowId", flowId);
         String flowName = rs.getString("FLOW_NAME");
         mapTmp.put("flowName", flowName);
         String runName = rs.getString("RUN_NAME");
         mapTmp.put("userId", rs.getInt("userId"));
         String userName = rs.getString("USER_NAME");
         mapTmp.put("userName", userName);
         int prcsId = rs.getInt("PRCS_ID");
         mapTmp.put("prcsId", prcsId);
         String op = rs.getString("OP_FLAG");
         int opFlag1  = 0;
         if (!YHUtility.isNullorEmpty(op)) {
           opFlag1  = Integer.parseInt(op);
         }
         mapTmp.put("opFlag", opFlag1);
         String prcsName = rs.getString("PRCS_NAME");
         int flowPrcs = rs.getInt("FLOW_PRCS");
         mapTmp.put("flowPrcs", flowPrcs);
         String flowType = rs.getString("FLOW_TYPE");
         mapTmp.put("flowType", flowType);
         runName = YHWorkFlowUtility.getRunName(runName);
         mapTmp.put("runName", runName);
         if ("2".equals(flowType)) {
           prcsName = "自由流程";
         }
         if ("1".equals(flowType)) {
           Statement stm4 = null;
           ResultSet rs4 = null;
           prcsName = "流程步骤已删除";
           try {
             String queryStr = "SELECT PRCS_NAME from oa_fl_process WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs; 
             stm4 = conn.createStatement();
             rs4 = stm4.executeQuery(queryStr);
             if (rs4.next()) {
               prcsName = rs4.getString("PRCS_NAME");
             }
           }catch(Exception ex) {
             throw ex;
           }finally {
             YHDBUtility.close(stm4 , rs4 , null);
           }
           mapTmp.put("prcsName"  , prcsName);
         } else {
           mapTmp.put("prcsName"  , "自由流程");
         }
         list.add(mapTmp);
       }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return list;
 }
  private String[][] listToArray(List<Map> list) {
    int widCount =  list.size();
    int count = 20;
    String[][] arrayList = new String[widCount][count];
    for (int i = 0 ;i < list.size() ;i++) {
      Map map = list.get(i);
//      String seqId = (Integer)map.get("seqId") + "";
//      arrayList[i][0] = seqId;
      String runId = (Integer)map.get("runId") + "";
      arrayList[i][0] = runId;
      String flowId = (Integer)map.get("flowId") + "";
      arrayList[i][1] = flowId;
      String flowName = (String)map.get("flowName") ;
      arrayList[i][2] = flowName;
      String runName = (String)map.get("runName") ;
      arrayList[i][3] = runName;
//      String userId = (Integer)map.get("userId") + "";
//      arrayList[i][5] = userId;
      String prcsId = (Integer)map.get("prcsId") + "";
      arrayList[i][4] = prcsId;
      String flowType = (String)map.get("flowType") ;
      arrayList[i][5] = flowType;
      String freeOther = (String)map.get("freeOther") ;
      arrayList[i][6] = freeOther;
//      String userName = (String)map.get("userName") ;
//      arrayList[i][9] = userName;
      String flowPrcs = (Integer)map.get("flowPrcs") + "";
      arrayList[i][7] = flowPrcs;
      String opFlag = (String)map.get("opFlag") ;
      arrayList[i][8] = opFlag;
      String prcsName = (String)map.get("prcsName") ;
      arrayList[i][9] = prcsName;
      String state = (String)map.get("state") ;
      arrayList[i][10] = state;
    }
    return arrayList;
  }
//  private List<Map> getMyWorkList( YHPerson user , String flowIdStr  , int showLen , String typeStr , String sortId , Connection conn) throws Exception{
//    Statement stm =null;
//    ResultSet rs = null;//结果集
//    List<Map> list = new ArrayList();
//    try {
//      int pageCount = 0;//页码数
//
//      int recordCount = 0;//总记录数
//      int pgStartRecord = 0;//开始索引
//
//      int pgEndRecord = 0;//结束索引
//      int userId = user.getSeqId();//用户ID
//      Map nameMap = new HashMap();
//      String query = "SELECT "
//        + " a.SEQ_ID "
//        + " ,a.RUN_ID "
//        + " ,a.PRCS_ID "
//        + " ,a.FLOW_PRCS "
//        + " ,a.PRCS_FLAG "
//        + " ,a.OP_FLAG "
//        + " ,a.PRCS_TIME "
//        + " ,a.CREATE_TIME "
//        + " ,b.FREE_OTHER "
//        + " , b.SEQ_ID As FLOW_ID"
//        + " ,b.FLOW_NAME "
//        + " ,b.FLOW_TYPE "
//        + " ,b.LIST_FLDS_STR "
//        + " ,b.FORM_SEQ_ID "
//        + " ,c.RUN_NAME "
//        + " , p.USER_NAME"
//        + " , c.BEGIN_USER as userId"
//        + "  from FLOW_RUN_PRCS  a, FLOW_TYPE  b, FLOW_RUN  c  , PERSON p , FLOW_SORT s"
//        + " WHERE  "
//        + " p.SEQ_ID = c.BEGIN_USER AND"
//        + "  a.RUN_ID=c.RUN_ID and  "
//        + "  c.FLOW_ID=b.SEQ_ID and  "
//        + "  s.SEQ_ID=b.FLOW_SORT and  "
//        + "  c.DEL_FLAG='0' and  "
//        + "  a.USER_ID=" + user.getSeqId()
//        + "  and a.PRCS_FLAG='"+ typeStr +"' ";
//      if (flowIdStr != null 
//          && !"".equals(flowIdStr) 
//          && !"0".equals(flowIdStr) ) {
//        query += " and b.SEQ_ID = " + flowIdStr;
//      }
//      if (!YHUtility.isNullorEmpty(sortId)) {
//        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
//        query += " and s.SEQ_ID IN (" + sortId + ")";
//      }
//      query += " order by a.RUN_ID DESC,a.PRCS_TIME DESC";
//      
//      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//      rs = stm.executeQuery(query);
//      YHMyWorkLogic log = new YHMyWorkLogic();
//      int count = 0;
//      while (rs.next() && count < showLen) {
//        count++;
//        Map map = new HashMap();
//        int seqId = rs.getInt("SEQ_ID");
//        map.put("seqId", seqId);
//        int runId = rs.getInt("RUN_ID");
//        map.put("runId", runId);
//        int flowId = rs.getInt("FLOW_ID");
//        map.put("flowId", flowId);
//        String flowName = rs.getString("FLOW_NAME");
//        map.put("flowName", flowName);
//        String runName = rs.getString("RUN_NAME");
//        int userId1= rs.getInt("userId");
//        map.put("userId", userId1);
//        int prcsId = rs.getInt("PRCS_ID");
//        map.put("prcsId" , prcsId);
//        String flowType = rs.getString("FLOW_TYPE");
//        map.put("flowType"  , flowType);
//        String freeOther = rs.getString("FREE_OTHER");
//        map.put("freeOther" , freeOther);
//        String userName = rs.getString("USER_NAME");
//        map.put("userName" , userName);
//        boolean isHaveDelPriv = log.isHaveDelPriv(runId, user, conn);
//        runName = YHWorkFlowUtility.getRunName(runName);
//        map.put("runName" , runName);
//        Statement stm3 = null;
//        ResultSet rs3 = null;
//        int flowPrcs = 0 ;
//        int opFlag = 1;
//        try {
//          String queryStr = "select PRCS_ID , FLOW_PRCS , OP_FLAG , PRCS_FLAG from FLOW_RUN_PRCS WHERE RUN_ID="+ runId +" AND USER_ID=" + user.getSeqId() + "  AND PRCS_FLAG =" + typeStr + " order by prcs_id desc"; 
//          stm3 = conn.createStatement();
//          rs3 = stm3.executeQuery(queryStr);
//          if(rs3.next()) {
//            prcsId = rs3.getInt("PRCS_ID");
//            flowPrcs = rs3.getInt("FLOW_PRCS");
//            opFlag = rs3.getInt("OP_FLAG");
//          }
//        }catch(Exception ex) {
//          throw ex;
//        }finally {
//          YHDBUtility.close(stm3 , rs3 , null);
//        }
//        map.put("prcsId", prcsId);
//        map.put("flowPrcs" , flowPrcs);
//        map.put("opFlag",opFlag);
//        if ("1".equals(flowType)) {
//          Statement stm4 = null;
//          ResultSet rs4 = null;
//          String prcsName = "流程步骤已删除";
//          try {
//            String queryStr = "SELECT PRCS_NAME from FLOW_PROCESS WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs; 
//            stm4 = conn.createStatement();
//            rs4 = stm4.executeQuery(queryStr);
//            if (rs4.next()) {
//              prcsName = rs4.getString("PRCS_NAME");
//            }
//          }catch(Exception ex) {
//            throw ex;
//          }finally {
//            YHDBUtility.close(stm4 , rs4 , null);
//          }
//          map.put("prcsName"  , prcsName);
//        } else {
//          map.put("prcsName"  , "自由流程");
//        }
//        list.add(map);
//      }
//    } catch (Exception ex) {
//      throw ex;
//    } finally {
//      YHDBUtility.close(stm, rs, null);;
//    }
//    return list;
//  }
}

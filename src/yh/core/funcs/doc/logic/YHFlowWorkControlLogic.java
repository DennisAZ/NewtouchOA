package yh.core.funcs.doc.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.exps.YHInvalidParamException;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowRunPrcs;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHFlowWorkControlLogic {
  public StringBuffer getFlowRunManager( Connection conn, Map request,YHPerson user , String sortId)
      throws Exception {
  /*
    select r.begin_user, s.user_id, s.user_name, p.prcs_id, p.PRCS_NAME, t.FLOW_TYPE, r.RUN_ID,f.PRCS_ID,f.USER_ID,f.PRCS_FLAG, f.prcs_time,f.FLOW_PRCS, f.create_time, t.flow_name,r.RUN_NAME 
    from flow_run_prcs f, flow_run r, flow_type t, flow_process p, person s 
    where s.seq_id = r.begin_user and t.seq_id= p.FLOW_SEQ_ID and t.seq_id=r.FLOW_ID  and f.RUN_ID=r.RUN_ID  and r.DEL_FLAG=0  
    and PRCS_FLAG in('1','2')  and (OP_FLAG=1 or TOP_FLAG=2) and r.FLOW_ID='41' 
    order by f.RUN_ID, f.prcs_id, FLOW_PRCS
   */
    StringBuffer result = new StringBuffer();
    try {
      
      String sql ="select f.RUN_ID"
        + " ,max(f.PRCS_ID) "
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + ",1"
        + " , 1"
        + " , 1 "
        + ",1"
        + " ,1"
        + " ,1"
        + " from "+ YHWorkFlowConst.FLOW_RUN +" r, "+ YHWorkFlowConst.FLOW_RUN_PRCS +" f , "+ YHWorkFlowConst.FLOW_TYPE +" t, person s where "
        + " s.seq_id = r.begin_user"
        + " and t.seq_id=r.FLOW_ID "
        + " and f.RUN_ID=r.RUN_ID "
        + " and r.DEL_FLAG=0 "
        + " and (f.PRCS_FLAG = '1' or f.PRCS_FLAG = '2') "
        + " and (f.OP_FLAG=1 or f.TOP_FLAG=2) ";
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        sql += " and t.flow_sort in (" + sortId + ") ";
      }
      String where = toSerachWhere(conn,request,user);
      if(!"".equals(where)){
        sql += where;
      }
      String query = " group by f.RUN_ID order by f.RUN_ID desc";
      sql += query;
      long date1 = System.currentTimeMillis();
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      long date2 = System.currentTimeMillis();
      long date3 = date2 - date1;
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        setDataToRecord(conn, record);
        setTimeToRecord( conn ,  record) ;
      }
      result.append(pageDataList.toJson());
    } catch (Exception e) {
      throw e;
    }
    return result;   
  }
  public void setDataToRecord(Connection conn , YHDbRecord record ) throws Exception {
    int prcsId = YHUtility.cast2Long(record.getValueByName("prcsId")).intValue();
    int runId = YHUtility.cast2Long(record.getValueByName("runId")).intValue();
    String sql2 = "select "
      + "t.FLOW_NAME"
      + ",r.RUN_NAME"
      + ",1"
      + ",s.USER_NAME"
      + ",f.PRCS_FLAG"
      + ",f.PRCS_TIME"
      + ",f.FLOW_PRCS"
      + ",f.CREATE_TIME"
      + ",t.FLOW_TYPE"
      + ",f.USER_ID"
      + ",f.DELIVER_TIME"
      + " , 1"
      + " , 1 "
      + ",t.FREE_OTHER"
      + " ,r.FLOW_ID"
      + " ,END_TIME"
      + ",f.OP_FLAG"
      + " from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" f, "+ YHWorkFlowConst.FLOW_RUN +" r, "+ YHWorkFlowConst.FLOW_TYPE +" t, person s where "
      + " s.seq_id = f.user_id"
      + " and t.seq_id=r.FLOW_ID "
      + " and f.RUN_ID=r.RUN_ID and f.run_id = " + runId 
      + " and f.prcs_ID =" + prcsId;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(sql2);
      while (rs2.next()) {
        String flowName = rs2.getString("FLOW_NAME");
        record.updateField("flowName", flowName);
        String runName = rs2.getString("RUN_NAME");
        record.updateField("runName", runName);
        int flowId = rs2.getInt("FLOW_ID");
        record.updateField("flowId", flowId);
        
        int opFlag = rs2.getInt("OP_FLAG");
        if (opFlag == 1) {
          rsToRecord( rs2  ,  record );
        } else {
          Timestamp prcsTime = rs2.getTimestamp("PRCS_TIME");
          if (prcsTime == null) {
            rsToRecord( rs2  ,  record );
          }
        }          
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null);
    }
  }
  public void rsToRecord(ResultSet rs2  , YHDbRecord record ) throws Exception {
    Timestamp prcsTime = rs2.getTimestamp("PRCS_TIME");
    String userName = rs2.getString("USER_NAME");
    record.updateField("userName", userName);
    int prcsFlag = rs2.getInt("PRCS_FLAG");
    record.updateField("prcsFlag", prcsFlag);
    record.updateField("prcsTime", prcsTime);
    int flowPrcs = rs2.getInt("FLOW_PRCS");
    record.updateField("flowPrcs", flowPrcs);
    Timestamp createTime = rs2.getTimestamp("CREATE_TIME");
    record.updateField("createTime", createTime);
    String flowType = rs2.getString("FLOW_TYPE");
    record.updateField("flowType", flowType);
    int userId = rs2.getInt("USER_ID");
    record.updateField("userId", userId);
    Timestamp deliverTime = rs2.getTimestamp("DELIVER_TIME");
    record.updateField("deliverTime", deliverTime);
    Timestamp endTime = rs2.getTimestamp("END_TIME");
    record.updateField("endTime", endTime);
    String freeOther = rs2.getString("FREE_OTHER");
    record.updateField("freeOther", freeOther);
  }
  public void setTimeToRecord(Connection conn , YHDbRecord record ) throws Exception {
    String flowType = (String)record.getValueByName("flowType");
    int prcsId = YHUtility.cast2Long(record.getValueByName("prcsId")).intValue();
    if ("1".equals(flowType)) {
      int flowId = YHUtility.cast2Long(record.getValueByName("flowId")).intValue();
      String query1 = "select PRCS_NAME , TIME_OUT , TIME_OUT_TYPE from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_SEQ_ID=" + flowId + " and prcs_ID=" + prcsId;
      Statement stm = null;
      ResultSet rs = null;
      String prcsName = "";
      String timeOut = "";
      String timeOutType = "";
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query1);
        if (rs.next()) {
          prcsName = rs.getString("PRCS_NAME");
          timeOut = rs.getString("TIME_OUT");
          timeOutType = rs.getString("TIME_OUT_TYPE");
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      record.updateField("prcsName", prcsName);
      record.updateField("timeOut", timeOut);
      record.updateField("timeOutType", timeOutType);
    } else {
      record.updateField("prcsName", "第" + prcsId + "步");
      record.updateField("timeOut", "");
      record.updateField("timeOutType", "");
    }
  }
  /**
   * 
   * @param conn
   * @param request
   * @param user
   * @return
   * @throws Exception
   */
  private String toSerachWhere(Connection conn,Map request , YHPerson user) throws Exception{
    String whereStr = "";
    String flowList = request.get("flowList") != null ? ((String[])request.get("flowList"))[0] : null;
    String runId = request.get("runId") != null ? ((String[])request.get("runId"))[0] : null;
    String runName = request.get("runName") != null ? ((String[])request.get("runName"))[0] : null;
    String userType = request.get("userType") != null ? ((String[])request.get("userType"))[0] : null;
    String user1 = request.get("user") != null ? ((String[])request.get("user"))[0] : null;
    
    // 判断 如果流程类型不等于 All 的话  加条件    if(!flowList.equals("All")){
      whereStr +=  " and t.SEQ_ID=" + flowList;     
    }
    // 如果流水号 不等于空   加条件
    if(!"".equals(runId) && runId != null){
      whereStr +=   " and r.RUN_ID='" + runId +"'"; 
    }
    // 如果文件名称不为空的 话   加条件
    if(!"".equals(runName) && runName != null){
      runName = runName.replace("'", "''");
      whereStr +=  " and r.RUN_NAME like " + "'%" + YHUtility.encodeLike(runName) + "%'"  + YHDBUtility.escapeLike();
    }
    // 如果用户类型不为空 0 ，1 （0当前主办人）1（流程发起人）    if(!"".equals(userType) && userType != null && !"".equals(user1) && user1 != null ){
     if(userType.equals("0")){
       whereStr +=   " and f.USER_ID=" + user1;
    }else {
      whereStr += " and r.BEGIN_USER="+ user1 ;
    }
   }
    String managerSql = getMyManageSql(conn, user);
    if(!"".equals(managerSql)){
      whereStr += " AND " + managerSql ;
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
    YHFlowWorkSearchLogic fw = new YHFlowWorkSearchLogic();
    String myDeptstr = fw.getMyDept(conn,user.getDeptId());
    int loginUserId = user.getSeqId();
    String loginUserDept = String.valueOf(user.getDeptId());
    String loginUserPriv = user.getUserPriv();
    //全局监控权限
    result = "(" + YHDBUtility.findInSet(String.valueOf(loginUserId),fw.subStringIndex("|","MANAGE_USER",1,1))
       + " or "
       + YHDBUtility.findInSet(String.valueOf(loginUserDept),fw.subStringIndex("|","MANAGE_USER",1,2))
        + " or "
       + YHDBUtility.findInSet("0",fw.subStringIndex("|","MANAGE_USER",1,2))
         + " or "
       + YHDBUtility.findInSet("ALL_DEPT",fw.subStringIndex("|","MANAGE_USER",1,2))
       + " or "
       + YHDBUtility.findInSet(loginUserPriv,fw.subStringIndex("|","MANAGE_USER",1,3));

    //部门监控、查询权限
    result += " or (s.DEPT_ID IN (" + myDeptstr + ") "
      + " AND ("
      + YHDBUtility.findInSet(String.valueOf(loginUserId),fw.subStringIndex("|","MANAGE_USER_DEPT",1,1))
      + " or "
      + YHDBUtility.findInSet(loginUserDept,fw.subStringIndex("|","MANAGE_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet("0",fw.subStringIndex("|","MANAGE_USER_DEPT",1,2))
        + " or "
      + YHDBUtility.findInSet("ALL_DEPT",fw.subStringIndex("|","MANAGE_USER_DEPT",1,2))
      + " or "
      + YHDBUtility.findInSet(loginUserPriv,fw.subStringIndex("|","MANAGE_USER_DEPT",1,3)) + ")))"; 
    return result;
  }
  public void doInsertTmp(String ids , Connection conn) throws Exception{
    String[] idss = ids.split(",");
    String create = "CREATE GLOBAL TEMPORARY TABLE FLOW_RUN_SEQ_ID ( SEQ_ID INT ) ON COMMIT delete ROWS";
    Statement stm = null;
    try {
      stm = conn.createStatement();
       stm.executeUpdate(create);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null);
    }
    Statement stm2 = null;
    
    try {
      stm2 = conn.createStatement();
      for (String id : idss) {
        if ("".equals(id)) {
          continue;
        }
        stm2.addBatch("insert into "+ YHWorkFlowConst.FLOW_RUN_SEQ_ID +" values (" + id + ")");
      }
      stm2.executeBatch();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, null, null);
    }
  }
  public StringBuffer getFlowRunManager1(Connection conn, Map request,
      YHPerson user  , String sortId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer result = new StringBuffer();
    try {
      long date1 = System.currentTimeMillis();
      String sql ="select f.RUN_ID"
        + ",f.PRCS_FLAG"
        + ",f.PRCS_TIME"
        + ",f.USER_ID"
        + " ,f.PRCS_ID"
        + " , f.seq_id"
        + " , f.OP_FLAG "
        + " , f.TOP_FLAG "
        + " from "+ YHWorkFlowConst.FLOW_RUN +" r, "+ YHWorkFlowConst.FLOW_RUN_PRCS +" f , "+ YHWorkFlowConst.FLOW_TYPE +" t, person s where "
        + " s.seq_id = r.begin_user"
        + " and t.seq_id=r.FLOW_ID "
        + " and f.RUN_ID=r.RUN_ID "
        + " and r.DEL_FLAG=0 "
        + " and (f.PRCS_FLAG = '1' or f.PRCS_FLAG = '2') "
        + " and (f.OP_FLAG=1 or f.TOP_FLAG=2)";
      String where = toSerachWhere(conn,request,user);
      if(!"".equals(where)){
        sql += where;
      }
      String query = " order by f.RUN_ID desc, f.FLOW_PRCS desc";
      sql += query;
      String seqIds = this.getSeqIds(sql, conn);
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
      }else if (dbms.equals("mysql")) {
      }else if (dbms.equals("oracle")) {
          this.doInsertTmp(seqIds, conn);
          seqIds = " select SEQ_ID from "+ YHWorkFlowConst.FLOW_RUN_SEQ_ID;
      }else {
        throw new SQLException("not accepted dbms");
      }
      long date2 = System.currentTimeMillis();
      long date3 = date2 - date1;
      
      String sql2 = "select f.RUN_ID"
        + ",t.FLOW_NAME"
        + ",r.RUN_NAME"
        + ",1"
        + ",s.USER_NAME"
        + ",f.PRCS_FLAG"
        + ",f.PRCS_TIME"
        + ",f.FLOW_PRCS"
        + ",f.CREATE_TIME"
        + ",t.FLOW_TYPE"
        + ",f.USER_ID"
        + ",f.DELIVER_TIME"
       // + ",p.TIME_OUT"
        + " , 1"
        + " , 1 "
       // + ",p.TIME_OUT_TYPE"
        + ",t.FREE_OTHER"
        + " ,f.PRCS_ID"
        + " ,r.FLOW_ID"
        + " ,END_TIME"
        + " from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" f, "+ YHWorkFlowConst.FLOW_RUN +" r, "+ YHWorkFlowConst.FLOW_TYPE +" t, person s where "
        + " s.seq_id = f.user_id"
        //+ " and t.seq_id= p.FLOW_SEQ_ID "
        + " and t.seq_id=r.FLOW_ID "
        + " and f.RUN_ID=r.RUN_ID ";
       // + " and f.flow_prcs = p.prcs_id ";
      if (seqIds.endsWith(",")) {
        seqIds = seqIds.substring(0, seqIds.length() - 1);
      }
      if (!"".equals(seqIds)) {
        sql2 += " and f.seq_id in (" + seqIds + ") ";
      } else {
        sql2 += " and 1<>1 ";
      }
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        sql2 += " and t.flow_sort in (" + sortId + ") ";
      }
      sql2 += query;
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql2);
      if (dbms.equals("oracle")) {
        String query1 = " drop table "+ YHWorkFlowConst.FLOW_RUN_SEQ_ID;
        Statement stm2 = null;
        try {
          stm2 = conn.createStatement();
          stm2.executeUpdate(query1);
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, null, null);
        }
      }
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        String flowType = (String)record.getValueByName("flowType");
        if ("1".equals(flowType)) {
          int prcsId = YHUtility.cast2Long(record.getValueByName("prcsId")).intValue();
          int flowId = YHUtility.cast2Long(record.getValueByName("flowId")).intValue();
          String query1 = "select PRCS_NAME , TIME_OUT , TIME_OUT_TYPE from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_SEQ_ID=" + flowId + " and prcs_ID=" + prcsId;
          Statement stm = null;
          ResultSet rs = null;
          String prcsName = "";
          String timeOut = "";
          String timeOutType = "";
          try {
            stm = conn.createStatement();
            rs = stm.executeQuery(query1);
            if (rs.next()) {
              prcsName = rs.getString("PRCS_NAME");
              timeOut = rs.getString("TIME_OUT");
              timeOutType = rs.getString("TIME_OUT_TYPE");
            }
          } catch (Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm, rs, null);
          }
          record.updateField("prcsName", prcsName);
          record.updateField("timeOut", timeOut);
          record.updateField("timeOutType", timeOutType);
        } else {
          int prcsId = YHUtility.cast2Long(record.getValueByName("prcsId")).intValue();
          record.updateField("prcsName", "第" + prcsId + "步");
          record.updateField("timeOut", "");
          record.updateField("timeOutType", "");
        }
      }
      result.append(pageDataList.toJson());
    } catch (Exception e) {
      throw e;
    }
    return result;    
  }
  public String getSeqIds(String sql  , Connection conn) throws Exception {
    String seqIds = "";
    String runIds = "";
    Statement stm2 = null;
    ResultSet rs2 = null;
    YHDocFlowRunPrcs tmp = new YHDocFlowRunPrcs();
    YHDocFlowRunPrcs tmp2 = new YHDocFlowRunPrcs();
    boolean flag = false;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(sql);
      while(rs2.next()){
        int runId = rs2.getInt("RUN_ID");
        if (YHWorkFlowUtility.findId(runIds, String.valueOf(runId))) {
          //取出来便于比较          tmp2  = new YHDocFlowRunPrcs();
          this.rsToObject(rs2, tmp2);
          if (tmp2.getPrcsId() == tmp.getPrcsId()) {
            if ("1".equals(tmp2.getOpFlag())) {
              tmp = tmp2;
              flag = true;
            } else {
              if (tmp2.getPrcsTime() == null) {
                tmp = tmp2;
                flag = true;
              }
            }
          } else if(tmp2.getPrcsId() > tmp.getPrcsId()) {
            tmp = tmp2;
            flag = true;
          }
        } else {
          //不是第一个          if (tmp.getSeqId() != 0) {
            seqIds += tmp.getSeqId() + ",";
          }
          //更新临时为最新的
          tmp = new YHDocFlowRunPrcs();
          this.rsToObject(rs2, tmp);
          flag = true;
          //加入最近的runId
          runIds += tmp.getRunId() + ",";
        }
      }
      //最后一个
      if (flag) {
        seqIds += tmp.getSeqId() + ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return seqIds;
  }
  public void rsToObject(ResultSet rs ,YHDocFlowRunPrcs tmp) throws SQLException {
    int seqId = rs.getInt("SEQ_ID");
    int runId = rs.getInt("RUN_ID");
    int prcsId = rs.getInt("PRCS_ID");
    int userId = rs.getInt("USER_ID");
    String prcsFlag = rs.getString("PRCS_FLAG");
    String opFlag = rs.getString("OP_FLAG");
    String topFlag = rs.getString("TOP_FLAG");
    Timestamp time = rs.getTimestamp("PRCS_TIME");
    
    tmp.setSeqId(seqId);
    tmp.setRunId(runId);
    tmp.setUserId(userId);
    tmp.setPrcsId(prcsId);
    tmp.setPrcsFlag(prcsFlag);
    tmp.setOpFlag(opFlag);
    tmp.setTopFlag(topFlag);
    tmp.setPrcsTime(time);
  }
  
}

package yh.core.funcs.doc.util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.send.data.YHDocFlowRun;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHDocUtility {
  /**
   * 是否具有全局权限
   * @param userId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean haveAllRight(YHPerson user , Connection conn) throws Exception{
    return haveAllRight( user.getSeqId() ,  user.getUserPriv() ,  user.getUserPrivOther() ,  conn) ;
  }
  /**
   * 是否具有全局权限
   * @param userId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean haveAllRight(int userId , String userPriv , String userPrivOther , Connection conn) throws Exception{
    String query = "select * from oa_officialdoc_recv_priv where dept_id = '-1'";
    Statement stm = null; 
    ResultSet rs = null; 
    String role = "";
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      if (rs.next()){ 
        role = YHUtility.null2Empty(rs.getString("USER_ID"));
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    if (YHWorkFlowUtility.findId(role, userPriv)
        || !"".equals(YHWorkFlowUtility.checkId(role, userPrivOther, true))
    ) {
      return true;
    } else {
      return false;
    }
  }
  public boolean haveEsbRecRight(YHPerson user , Connection conn) throws Exception {
    String deptIds = "";
    String privIds ="";
    String userIds = "";
    String query = "select USER_ID , DEPT_ID, USER_PRIV from oa_esb_rec_person";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        deptIds =YHUtility.null2Empty(rs.getString("DEPT_ID"));
        privIds =YHUtility.null2Empty(rs.getString("USER_PRIV"));
        userIds = YHUtility.null2Empty(rs.getString("USER_ID"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if(("ALL_DEPT".equals(deptIds) || "0".equals(deptIds))
        || YHWorkFlowUtility.findId(userIds , String.valueOf(user.getSeqId()))
        || YHWorkFlowUtility.findId(deptIds , String.valueOf(user.getDeptId()))
        || YHWorkFlowUtility.findId(privIds , user.getUserPriv())){
      return true;
    }
    return false;
  }
  /**
   * 具有那些部门的权限
   * @param userId
   * @param conn
   * @return
   * @throws Exception
   */
  public String deptRight(int userId , Connection conn) throws Exception{
    String query = "select * from oa_officialdoc_recv_priv where dept_id <> '-1' and " + YHDBUtility.findInSet(userId + "", "USER_ID");
    Statement stm = null; 
    ResultSet rs = null; 
    String dept = "";
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      while (rs.next()){ 
        String deptId = YHUtility.null2Empty(rs.getString("dept_id"));
        if (!YHWorkFlowUtility.findId(dept, deptId)) {
          dept += "'" +  deptId + "',";
        }
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return dept;
  }
  public Map getDeptByUser(int userId , Connection conn) throws Exception{
    String query = "select DEPT_ID , DEPT_NAME from oa_department,PERSON where oa_department.SEQ_ID = PERSON.DEPT_ID AND PERSON.SEQ_ID = " + userId;
    Statement stmt = null;
    ResultSet rs = null;
    String deptName = "";
    int deptId = 0 ;
    Map map = new HashMap();
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        deptName = rs.getString("DEPT_NAME");
        deptId = rs.getInt("DEPT_ID");
        map.put("DEPT_NAME", deptName);
        map.put("DEPT_ID", deptId);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return map;
  }
  public YHDocFlowRun getSendDocByRunId(int runId , Connection conn) throws Exception{
    HashMap map = new HashMap();
    map.put("RUN_ID", runId);
    YHORM orm = new YHORM();
    YHDocFlowRun flowRun = (YHDocFlowRun) orm.loadObjSingle(conn, YHDocFlowRun.class, map);
    return flowRun;
  }
  public String getSortIds(String sortName , Connection dbConn) throws Exception {
    String sortNamesNew = "";
    if (!YHUtility.isNullorEmpty(sortName)) {
      String[] news = sortName.split(",");
      for (String tmp : news) {
        if (!YHUtility.isNullorEmpty(tmp)) {
          sortNamesNew += "'" + tmp + "',";
        }
      }
    }
    if (sortNamesNew.endsWith(",")) {
      sortNamesNew = sortNamesNew.substring(0, sortNamesNew.length() - 1);
    }
    String result = "";
    try {
      if (!"".equals(sortNamesNew)) {
        String sql = "select seq_id from "+ YHWorkFlowConst.FLOW_SORT +" where sort_name in (" + sortNamesNew + ")"; 
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = dbConn.createStatement();
          rs = stm.executeQuery(sql);
          while (rs.next()) {
            result += rs.getInt("seq_id") + ",";
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm , rs , null);
        }
      }
    }catch(Exception ex) {
      throw ex;
    } 
    return result;
  }
  public Map getFlowBySortIds(String sortIds , Connection dbConn , YHPerson user) throws Exception {
    Map<String , String> map = new HashMap<String , String>();
    sortIds = YHWorkFlowUtility.getOutOfTail(sortIds);
    try {
      if (!"".equals(sortIds)) {
        String sql = "select  SEQ_ID ,FLOW_NO, flow_name , flow_Type , NEW_USER ,query_User,query_User_Dept,manage_User,manage_User_Dept from "+ YHWorkFlowConst.FLOW_TYPE +" where flow_sort in (" + sortIds + ")"; 
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = dbConn.createStatement();
          rs = stm.executeQuery(sql);
          YHPrcsRoleUtility tru = new YHPrcsRoleUtility();
          while (rs.next()) {
            YHDocFlowType ft = new YHDocFlowType();
            int seqId = rs.getInt("SEQ_ID");
            String flowName = rs.getString("flow_name");
            String flowType = rs.getString("flow_Type");
            String newUser = rs.getString("NEW_USER");
            String queryUser = rs.getString("query_User");
            String queryUserDept = rs.getString("query_User_Dept");
            String manageUser = rs.getString("manage_User");
            String manageUserDept = rs.getString("manage_User_Dept");
            ft.setFlowType(flowType);
            ft.setFlowName(flowName);
            ft.setSeqId(seqId);
            ft.setNewUser(newUser);
            ft.setQueryUser(queryUser);
            ft.setQueryUserDept(queryUserDept);
            ft.setManageUser(manageUser);
            ft.setManageUserDept(manageUserDept);
            boolean flag = false;
            flag = tru.prcsRole(ft, 0, user, dbConn);
            if (flag) {
              map.put(seqId + "", flowName);
            }
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm , rs , null);
        }
      }
    }catch(Exception ex) {
      throw ex;
    } 
    return map;
  }
  public static boolean usingEsb() {
    String usingEsb = YHSysProps.getProp("USING_ESB");
    if ("1".equals(usingEsb) ) {
      return true;
    } else {
      return false;
    }
  }
}

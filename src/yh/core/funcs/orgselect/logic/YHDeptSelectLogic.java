package yh.core.funcs.orgselect.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHDeptSelectLogic {
  /**
   * 取得所有部门的Json数据
   * @param depts
   * @param procId
   * @return
   * @throws Exception
   */
  public StringBuffer getDeptJson(List depts) throws Exception{
    return this.getDeptJson(depts, 0);
  }
  /**
   * 取得部门的Json数据
   * @param depts
   * @param procId
   * @param deptId
   * @return
   * @throws Exception
   */
  public StringBuffer getDeptJson(List depts , int deptId) throws Exception{
    StringBuffer sb = new StringBuffer("[");
    YHDepartment dept = new YHDepartment();
    if(deptId != 0){
      for(int i = 0 ;i < depts.size();i ++){
        dept = (YHDepartment) depts.get(i);
        if(dept.getSeqId() == deptId){
          break;
        }
      }
      this.setDeptSingle(dept, depts, sb , 0);
    }else{
      for(int i = 0 ;i < depts.size();i ++){
        dept = (YHDepartment) depts.get(i);
        if(dept.getDeptParent() == 0){
          this.setDeptSingle(dept, depts, sb , 0);
        }
      }
    }
    
    sb.deleteCharAt(sb.length() - 1);
    
    sb.append("]");
    return sb;
  }
  
  /**
   * 取得一个部门的节点定义
   * @param dept
   * @param depts
   * @param sb
   * @param level
   */
  public void setDeptSingle(YHDepartment dept , List depts, StringBuffer sb ,int level){
    String deptName = dept.getDeptName();
    int deptId = dept.getSeqId();
    boolean isChecked = false;
    String nbsp = "├";
    for(int i = 0 ;i < level;i++){
      nbsp = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + nbsp;
    }
    sb.append("{");
    sb.append("deptName:\"" + nbsp + YHUtility.encodeSpecial(deptName) + "\",");
    sb.append("deptId:'" + deptId + "',");
    sb.append("isChecked:" + isChecked) ;
    sb.append("},");
    //depts.remove(dept);
    
    level++;
    for(int i = 0 ;i < depts.size() ; i++){
      YHDepartment  deptTmp = (YHDepartment) depts.get(i);
      if(deptTmp.getDeptParent() == deptId){
        setDeptSingle(deptTmp, depts, sb, level);
      }
    }
  }
  /**
   * 判段id是不是在str里面
   * @param str
   * @param id
   * @return
   */
  public  boolean findId(String str, String id) {
    if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
    return false;
  }
  public String getParentDept(Connection conn , int myDept) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    int deptParent = 0 ;
    String deptStr = myDept + "";
    try{
      String sql = "SELECT DEPT_PARENT FROM oa_department WHERE SEQ_ID = " + myDept;
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()){
        deptParent = rs.getInt("DEPT_PARENT") ;
        deptStr += "," + deptParent;
      }
    } catch (Exception e) {
      throw e ;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
    if (deptParent != 0) {
      PreparedStatement ps2 = null;
      ResultSet rs2 = null;
      try{
        String sql = "SELECT DEPT_PARENT FROM oa_department WHERE SEQ_ID = " + deptParent;
        ps2 = conn.prepareStatement(sql);
        rs2 = ps2.executeQuery();
        if (rs2.next()){
          deptParent = rs2.getInt("DEPT_PARENT"); 
          deptStr += "," + deptParent;
        }
      } catch (Exception e) {
        throw e ;
      } finally{
        YHDBUtility.close(ps2, rs2, null);
      }
    }
    return deptStr;
  }
  public String getDefUserDept(Connection conn,YHMyPriv mp , int myDept) throws Exception{
    //指定人员
    String result = getParentDept(conn ,  myDept);
    if (!"".equals(result) 
        && !result.endsWith(",")) {
      result += ",";
    }
    if( "3".equals(mp.getDeptPriv())){
      String users = mp.getUserId();
      if (users != null) {
        String[] userIds = users.split(",");
        for (int i = 0; i < userIds.length; i++) {
          if(!"".equals(userIds[i].trim())){
            int userId = Integer.valueOf(userIds[i].trim());
            int deptId = getDeptId(conn, userId);
            if (!this.findId(result, String.valueOf(deptId))) {
              result += deptId + ",";
            }
          }
        }
      }
      return result;
      //指定部门
    }else if( "2".equals(mp.getDeptPriv())){
      String depts = mp.getDeptId();
      StringBuffer sb = new StringBuffer();
      if (depts != null) {
        String[] aDept = depts.split(",");
        for (int i = 0 ;i < aDept.length ; i++ ){
          if (YHUtility.isInteger(aDept[i])) {
            int deptTmp = Integer.parseInt(aDept[i]);
            String rss =  getParentDept(conn ,  deptTmp);
            result += rss + ",";
            this.getAllChildDept(deptTmp, conn, sb);
          }
        }
        if (!"".equals(result) && !result.endsWith(",")) {
          result = result + "," + sb.toString();
        } else {
          result += sb.toString();
        }
        if (depts.endsWith(",")) {
          depts = depts.substring(0, depts.length() - 1);
        }
        if (!"".equals(result) && !result.endsWith(",")) {
          depts = result + "," + depts;
        } else {
          depts = result + depts;
        }
        return depts;
      } else {
        return "";
      }
    } else if ("0".equals(mp.getDeptPriv())) {
      StringBuffer sb = new StringBuffer();
      this.getAllChildDept(myDept, conn, sb);
      result += sb.toString();
      if (result.endsWith(",")) {
        result = result.substring(0, result.length() - 1);
      }
      return result;
    } 
    return "";
  }
  /**
   * 根据用户ID取得用户的部门ID
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public Integer getDeptId(Connection conn, int userId) throws Exception{
    int result = 0;
    String sql = "select DEPT_ID FROM PERSON WHERE SEQ_ID=" + userId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
         result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  public void getAllChildDept(int deptId , Connection conn , StringBuffer sb) throws Exception {
    String s = sb.toString();
    if (!this.findId(s, String.valueOf(deptId))) {
      sb.append(deptId + ",");
    }
    String query = "select SEQ_ID  from oa_department where DEPT_PARENT=" + deptId;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        getAllChildDept(seqId , conn , sb);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
}

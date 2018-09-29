package yh.core.funcs.doc.group.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHDeptGroupLogic {
  public void addDeptGroup(Connection conn , String groupName , String orderNo , String userStr) throws Exception {
    String sql = "INSERT INTO  oa_dept_team (GROUP_NAME ,ORDER_NO , DEPT_STR) VALUES (? , ? , ?) ";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, groupName);
      pstmt.setString(2, orderNo);
      pstmt.setString(3, userStr);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  public String getDeptGroup(Connection conn , int seqId) throws Exception {
    String sql = "select *  from oa_dept_team WHERE SEQ_ID =" + seqId;
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        sb.append("groupName:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("GROUP_NAME")))).append("\"");
        sb.append(",orderNo:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("ORDER_NO")))).append("\"");
        String dept = rs.getString("DEPT_STR");
        YHDeptLogic logic = new YHDeptLogic();
        String deptName = logic.getNameByIdStr(dept, conn);
        sb.append(",dept:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(dept))).append("\"");
        sb.append(",deptDesc:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(deptName))).append("\"");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    sb.append("}");
    return sb.toString();
  }
  public String getDeptGroups(Connection conn) throws Exception {
    String sql = "select *  from  oa_dept_team order by ORDER_NO ";
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int count = 0 ;
    YHDeptLogic logic = new YHDeptLogic();
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        sb.append("{seqId:" + rs.getInt("SEQ_ID"));
        sb.append(",groupName:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("GROUP_NAME")))).append("\"");
        sb.append(",orderNo:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("ORDER_NO")))).append("\"");
        String dept = rs.getString("DEPT_STR");
        String deptName = logic.getNameByIdStr(dept, conn);
        sb.append(",dept:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(dept))).append("\"");
        sb.append(",deptDesc:\"").append(YHUtility.encodeSpecial(YHUtility.null2Empty(deptName))).append("\"},");
        count++;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public void deleteDeptGroup(Connection conn, String seqId) throws Exception {
    String sql = "DELETE FROM oa_dept_team WHERE SEQ_ID IN(" + seqId + ")";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  public void updateDeptGroup(Connection conn, String groupName,
      String orderNo, String userStr, String seqId) throws Exception {
    // TODO Auto-generated method stub
    String sql = "update oa_dept_team set GROUP_NAME=?, ORDER_NO=? , DEPT_STR=? where SEQ_ID =" + seqId;
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, groupName);
      pstmt.setString(2, orderNo);
      pstmt.setString(3, userStr);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  public String getDeptByGroup(Connection conn, String groupId) throws Exception {
    // TODO Auto-generated method stub
    String sql = "select *  from oa_dept_team WHERE SEQ_ID =" + groupId;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String dept = "";
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      if (rs.next()) {
         dept = YHUtility.null2Empty(rs.getString("DEPT_STR"));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    if (YHUtility.isNullorEmpty(dept)) {
      return "[]";
    }
    if (dept.endsWith(",")) {
      dept = dept.substring(0, dept.length() - 1);
    }
    String query = "select DEPT_NAME , oa_department.SEQ_ID FROM oa_department  WHERE SEQ_ID IN ("  + dept + ")";
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    PreparedStatement pstmt2 = null;
    ResultSet rs2 = null;
    int count = 0 ;
    try {
      pstmt2 = conn.prepareStatement(query);
      rs2 = pstmt2.executeQuery();
      while (rs2.next()) {
        sb.append("{");
        sb.append("deptName:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(rs2.getString("DEPT_NAME"))) + "\",");
        sb.append("deptId:'" + rs2.getInt("SEQ_ID") + "'");
        sb.append("},");
        count++;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt2, rs2, null);
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
}

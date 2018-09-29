package yh.core.funcs.dept.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.org.data.YHOrganization;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHDeptTreeAct {

  private static Logger log = Logger
      .getLogger("yh.core.funcs.dept.act.YHDeptTreeAct");

  public String getTree(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String idStr = request.getParameter("id");
   // int id = 0;
    String orgId = "organizationNodeId";
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHOrganization> org = new ArrayList();
      YHDeptLogic dls = new YHDeptLogic();
      org = dls.getOrganization(dbConn);
      if ((idStr == null || "".equals(idStr) || "0".equals(idStr)) && !orgId.equals(idStr)) {
        for (YHOrganization orgs : org) {
          String name = YHUtility.encodeSpecial(orgs.getUnitName());
          if(YHUtility.isNullorEmpty(name)){
            name = "";
          }
          String imgAddress = "/yh/core/styles/style1/img/dtree/system.gif";
          sb.append("{");
          sb.append("nodeId:\"" + orgId + "\"");
          sb.append(",name:\"" + name + "\"");
          sb.append(",isHaveChild:" + 1 + "");
          sb.append(",imgAddress:\"" + imgAddress + "\"");
          sb.append(",title:\"" + name + "\"");
          sb.append("},");
        }
      } else  {
        if(orgId.equals(idStr)) {
          idStr = "0";
        }
        String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT = " + idStr + " order by DEPT_NO ASC, DEPT_NAME asc";
        ArrayList<YHDepartment> depts = new ArrayList();
        ArrayList<YHDepartment> deptStr = new ArrayList();
        Statement stm4 = null;
        ResultSet rs4 = null;
        try {
          stm4 = dbConn.createStatement();
          rs4 = stm4.executeQuery(query);
          while (rs4.next()) {
            YHDepartment dept = new YHDepartment();
            dept.setSeqId(rs4.getInt("SEQ_ID"));
            dept.setDeptName(rs4.getString("DEPT_NAME"));
            depts.add(dept);
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs4, null);
        }

        // deptStr = (ArrayList<YHDepartment>) dls.deleteDeptMul(dbConn, id);
        // depts.addAll(deptStr);

        YHPerson person = (YHPerson) request.getSession().getAttribute(
            "LOGIN_USER");
        String deptIdOther = person.getDeptIdOther();
        String loginUserPriv = person.getUserPriv();
        String deptIdLogin = String.valueOf(person.getDeptId());
        String postDept = person.getPostDept();
        String postPriv = person.getPostPriv();
        YHPersonLogic dl = new YHPersonLogic();
        String data = "";
        String deptStrs = "";
        boolean isOaAdmin = person.isAdminRole();
        if ("0".equals(postPriv)) {
          deptStrs = dls.getChildDeptId(dbConn, person.getDeptId());
          postDept = deptStrs + deptIdLogin;

        } else if ("2".equals(postPriv)) {
          String[] postFunc = postDept.split(",");
          for (int x = 0; x < postFunc.length; x++) {
            int deptFunc = Integer.parseInt(postFunc[x]);
            if ("".equals(deptStrs)) {
              deptStrs = dls.getChildDeptId(dbConn, deptFunc);
            } else {
              deptStrs += "," + dls.getChildDeptId(dbConn, deptFunc);
            }
          }
          postDept += "," + deptStrs;
        }
        for (YHDepartment d : depts) {
          if (d.getSeqId() != Integer.parseInt(idStr)) {
            int nodeId = d.getSeqId();
            String deptId = String.valueOf(nodeId);
            String name = YHUtility.encodeSpecial(d.getDeptName());
            int isHaveChild = IsHaveChild(dbConn, d.getSeqId());
            String extData = "";

            if (person.isAdminRole() || postPriv.equals("1")) {
              extData = "isPriv";
            } else {
              if (dl.findId(postDept, deptId)) {
                extData = "isPriv";
              } else {
                extData = "";
              }
            }
            String imgAddress = "/yh/core/styles/style1/img/dtree/node_dept.gif";
            sb.append("{");
            sb.append("nodeId:\"" + nodeId + "\"");
            sb.append(",name:\"" + name + "\"");
            sb.append(",isHaveChild:" + isHaveChild + "");
            sb.append(",extData:\"" + extData + "\"");
            sb.append(",imgAddress:\"" + imgAddress + "\"");
            sb.append(",title:\"" + name + "\"");
            sb.append("},");
          }
        }
        
      }
      if (sb.length() > 1) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      // long date2 = System.currentTimeMillis();
      // long date3 = date2 - date1;
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getTree1(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String idStr = request.getParameter("id");
    String deptId = request.getParameter("deptId");
    int id = 0;
    if (idStr != null && !"".equals(idStr)) {
      id = Integer.parseInt(idStr);
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT = "
          + id;
      ArrayList<YHDepartment> depts = new ArrayList();
      Statement stm4 = null;
      ResultSet rs4 = null;
      try {
        stm4 = dbConn.createStatement();
        rs4 = stm4.executeQuery(query);
        while (rs4.next()) {
          YHDepartment dept = new YHDepartment();
          dept.setSeqId(rs4.getInt("SEQ_ID"));
          dept.setDeptName(rs4.getString("DEPT_NAME"));
          depts.add(dept);
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm4, rs4, null);
      }
      StringBuffer sb = new StringBuffer("[");
      for (YHDepartment d : depts) {
        int nodeId = d.getSeqId();
        String name = d.getDeptName();
        int isHaveChilds = IsHaveChild(dbConn, Integer.parseInt(deptId));
        if (isHaveChilds != 0) {
          if (d.getSeqId() != Integer.parseInt(deptId)) {
            int isHaveChild = IsHaveChild(dbConn, d.getSeqId());
            String extData = "";
            String imgAddress = "/yh/core/styles/style1/img/dtree/node_dept.gif";
            sb.append("{");
            sb.append("nodeId:\"" + nodeId + "\"");
            sb.append(",name:\"" + name + "\"");
            sb.append(",isHaveChild:" + isHaveChild + "");
            sb.append(",extData:\"" + extData + "\"");
            sb.append(",imgAddress:\"" + imgAddress + "\"");
            sb.append("},");
          }
        } else {
          int isHaveChild = IsHaveChild(dbConn, d.getSeqId());
          String extData = "";
          String imgAddress = "/yh/core/styles/style1/img/dtree/node_dept.gif";
          sb.append("{");
          sb.append("nodeId:\"" + nodeId + "\"");
          sb.append(",name:\"" + name + "\"");
          sb.append(",isHaveChild:" + isHaveChild + "");
          sb.append(",extData:\"" + extData + "\"");
          sb.append(",imgAddress:\"" + imgAddress + "\"");
          sb.append("},");
        }
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public int IsHaveChild(Connection conn, int id) throws Exception {
    boolean flag = false;
    String query = "select 1 from oa_department where DEPT_PARENT = " + id;
    ArrayList<YHDepartment> depts = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if (rs4.next()) {
        flag = true;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null);
    }

    if (flag) {
      return 1;
    } else {
      return 0;
    }
  }
}

package yh.core.module.org_select.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.org.data.YHOrganization;
import yh.core.funcs.orgselect.logic.YHDeptSelectLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHOrgSelectModule {
  public String getTree(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String idStr = request.getParameter("id");
    String orgId = "organizationNodeId";
    // int id = 0;
    // if (!YHUtility.isNullorEmpty(idStr)) {
    // id = Integer.parseInt(idStr);
    // }
    String moduleId = request.getParameter("MODULE_ID");
    String privNoFlagStr = request.getParameter("privNoFlag");
    int privNoFlag = 2;
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
      privNoFlag = Integer.parseInt(privNoFlagStr);
    }
    String hrFlag = request.getParameter("hrFlag");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      StringBuffer record = new StringBuffer();
      StringBuffer sb = new StringBuffer();
      ArrayList<YHOrganization> org = new ArrayList();
      YHDeptLogic dls = new YHDeptLogic();
      org = dls.getOrganization(dbConn);
      if ((idStr == null || "".equals(idStr) || "0".equals(idStr))
          && !orgId.equals(idStr)) {
        for (YHOrganization orgs : org) {
          String name = orgs.getUnitName();
          String imgAddress = "/yh/core/styles/style1/img/dtree/system.gif";
          record.append("{");
          record.append("nodeId:\"" + orgId + "\"");
          record.append(",name:\"" + name + "\"");
          record.append(",isHaveChild:" + 1 + "");
          record.append(",imgAddress:\"" + imgAddress + "\"");
          record.append(",title:\"" + name + "\"");
          record.append("},");
        }
      } else {
        if (orgId.equals(idStr)) {
          idStr = "0";
        }
        String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT = "
            + idStr + " order by DEPT_NO ASC, DEPT_NAME ASC";
        ArrayList<YHDepartment> depts = new ArrayList<YHDepartment>();
        ArrayList<YHPerson> persons = new ArrayList<YHPerson>();
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
        YHDeptSelectLogic dsl = new YHDeptSelectLogic();
        boolean hasModule = false;
        if (moduleId != null && !"".equals(moduleId)) {
          hasModule = true;
        }
        String noLoginInStr = request.getParameter("noLoginIn");
        boolean noLoginIn = false;
        if (!YHUtility.isNullorEmpty(noLoginInStr)) {
          noLoginIn = true;
        }
        YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
        if (Integer.parseInt(idStr) != 0) {
          persons = osl.getDeptUser(dbConn, Integer.parseInt(idStr)  , noLoginIn);
        }
        String allDef = "";
        YHMyPriv mp = new YHMyPriv();
        mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
        allDef = dsl.getDefUserDept(dbConn, mp, person.getDeptId());
        String contextPath = request.getContextPath();
        YHPerson person1 = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
        for (YHPerson per : persons) {
          int seqId = per.getSeqId();
          String username = per.getUserName();
          //判断是否为HR模块参数，isHrManageDept方法为判断当前登录人员的HR权限，用于人力资源年休假设置
          if("1".equals(hrFlag)){
            if( !"1".equals(person.getUserPriv()) && !YHPrivUtil.isHrManageDept(dbConn, per.getDeptId(), person)){
              continue;
            }
          }
          else{
            if (!YHPrivUtil.isUserPriv(dbConn, seqId, mp, person)) {//添加也可以看到本部门自己的工作日志
            	if(!((per.getSeqId() == person1.getSeqId()) || (per.getUserId().equals(person1.getUserId())))){
            		continue;
            	}
            }
          }
          int deptId = per.getDeptId();
          String deptName = osl.getDeptName(dbConn, deptId);
          String email = per.getEmail();
          int roleId = Integer.parseInt(per.getUserPriv());
          String roleName = osl.getRoleName(dbConn, roleId);
          String telNoDept = per.getTelNoDept();
          if (YHUtility.isNullorEmpty(email)) {
            email = "";
          }
          if (YHUtility.isNullorEmpty(telNoDept)) {
            telNoDept = "";
          }
          String oicq = per.getOicq();
          if (YHUtility.isNullorEmpty(oicq)) {
            oicq = "";
          }
          if (!"".equals(record.toString())) {
            record.append(",");
          }
          String myStatus = per.getMyStatus();
          String myState = "";
          if (YHUtility.isNullorEmpty(myStatus)) {
            myState = "";
          } else {
            myState = "\\n人员状态:" + myStatus + "";
          }
          String userId = per.getUserId(); // cc 20100617
          record.append("{");
          record.append("nodeId:\"r" + seqId + "\"");
          record.append(",name:\"" + YHUtility.encodeSpecial(per.getUserName())
              + "\"");
          record.append(",isHaveChild:" + 0);
          record.append(",extData:\"" + userId + "\"");
          record.append(",imgAddress:\"" + request.getContextPath()
              + "/core/styles/style1/img/dtree/0-1.gif\"");
          record.append(",title:\"部门:" + YHUtility.encodeSpecial(deptName)
              + "\\n角色:" + YHUtility.encodeSpecial(roleName) + "\\n工作电话:"
              + telNoDept + "\\nemail:" + YHUtility.encodeSpecial(email)
              + "\\nQQ:" + oicq + myState + "\"");
          record.append("}");
        }
        for (YHDepartment d : depts) {
          int nodeId = d.getSeqId();
          String name = d.getDeptName();
          int isHaveChild = IsHaveChild(dbConn, d.getSeqId());
          boolean extData = false;
          if (YHPrivUtil.isDeptPriv(dbConn, nodeId, mp, person)) {
            extData = true;
          }
          String imgAddress = contextPath
              + "/core/styles/style1/img/dtree/node_dept.gif";
          if (!"".equals(record.toString())) {
            record.append(",");
          }
          record.append("{");
          record.append("nodeId:\"" + nodeId + "\"");
          record.append(",name:\"" + YHUtility.encodeSpecial(name) + "\"");
          record.append(",isHaveChild:" + isHaveChild + "");
          record.append(",title:\"" + YHUtility.encodeSpecial(name) + "\"");
          record.append(",extData:" + extData);
          record.append(",imgAddress:\"" + YHUtility.encodeSpecial(imgAddress)
              + "\"");
          record.append("}");
        }
      }
      sb.append("[").append(record).append("]");
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

  public String getPersonTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String idStr = request.getParameter("id");
    String orgId = "organizationNodeId";
    // int id = 0;
    // if (!YHUtility.isNullorEmpty(idStr)) {
    // id = Integer.parseInt(idStr);
    // }
    String moduleId = request.getParameter("MODULE_ID");
    String privNoFlagStr = request.getParameter("privNoFlag");
    int privNoFlag = 2;
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
      privNoFlag = Integer.parseInt(privNoFlagStr);
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      StringBuffer record = new StringBuffer();
      StringBuffer sb = new StringBuffer();
      ArrayList<YHOrganization> org = new ArrayList();
      YHDeptLogic dls = new YHDeptLogic();
      org = dls.getOrganization(dbConn);
      if ((idStr == null || "".equals(idStr) || "0".equals(idStr))
          && !orgId.equals(idStr)) {
        for (YHOrganization orgs : org) {
          String name = orgs.getUnitName();
          String imgAddress = "/yh/core/styles/style1/img/dtree/system.gif";
          record.append("{");
          record.append("nodeId:\"" + orgId + "\"");
          record.append(",name:\"" + name + "\"");
          record.append(",isHaveChild:" + 1 + "");
          record.append(",imgAddress:\"" + imgAddress + "\"");
          record.append(",title:\"" + name + "\"");
          record.append("},");
        }
      } else {
        if (orgId.equals(idStr)) {
          idStr = "0";
        }
        String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT = "
            + idStr + " order by DEPT_NO ASC , DEPT_NAME asc";
        ArrayList<YHDepartment> depts = new ArrayList<YHDepartment>();
        ArrayList<YHPerson> persons = new ArrayList<YHPerson>();
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
        YHDeptSelectLogic dsl = new YHDeptSelectLogic();
        boolean hasModule = false;
        if (moduleId != null && !"".equals(moduleId)) {
          hasModule = true;
        }
        YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
        YHOrgSelectLogic osl2 = new YHOrgSelectLogic();
        if (Integer.parseInt(idStr) != 0) {
          persons = osl.getDeptUser2(dbConn, Integer.parseInt(idStr));
        }
        String allDef = "";
        YHMyPriv mp = new YHMyPriv();
        mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
        allDef = dsl.getDefUserDept(dbConn, mp, person.getDeptId());
        String contextPath = request.getContextPath();
        String ipFlag = getIpState(request, response, dbConn);
        for (YHPerson per : persons) {
          int seqId = per.getSeqId();
          if (!YHPrivUtil.isUserPriv(dbConn, seqId, mp, person)) {
            continue;
          }
          int deptId = per.getDeptId();
          String deptName = osl.getDeptName(dbConn, deptId);
          String email = per.getEmail();
          int roleId = Integer.parseInt(per.getUserPriv());
          String roleName = osl.getRoleName(dbConn, roleId);
          String telNoDept = per.getTelNoDept();
          if (YHUtility.isNullorEmpty(email)) {
            email = "";
          }
          if (YHUtility.isNullorEmpty(telNoDept)) {
            telNoDept = "";
          }
          String oicq = per.getOicq();
          if (YHUtility.isNullorEmpty(oicq)) {
            oicq = "";
          }
          if (!"".equals(record.toString())) {
            record.append(",");
          }
          String myStatus = per.getMyStatus();
          String myState = "";
          if (!YHUtility.isNullorEmpty(myStatus)) {
            myState = "\\n人员状态:" + YHUtility.encodeSpecial(myStatus) + "";
          }
          
          String showIp = "";
          if("1".equals(ipFlag)){
            if(person.isAdminRole()){
              String ip = osl2.getShowIp(dbConn, YHLogConst.LOGIN, seqId);
              ip = YHUtility.encodeSpecial(ip);
              showIp = "\\n最后登录IP:" + ip +"";
            }
          }else if("2".equals(ipFlag)){
            String ip = osl2.getShowIp(dbConn, YHLogConst.LOGIN,seqId);
            ip = YHUtility.encodeSpecial(ip);
            showIp = "\\n最后登录IP:" + ip +"";
          }
          
          String userId = per.getUserId(); // cc 20100617
          record.append("{");
          record.append("nodeId:\"r" + seqId + "\"");
          record.append(",name:\"" + YHUtility.encodeSpecial(per.getUserName())
              + "\"");
          record.append(",isHaveChild:" + 0);
          record.append(",extData:\"" + userId + "\"");
          record.append(",imgAddress:\"" + request.getContextPath()
              + "/core/styles/style1/img/dtree/0-1.gif\"");
          record.append(",title:\"部门:" + YHUtility.encodeSpecial(deptName)
              + "\\n角色:" + YHUtility.encodeSpecial(roleName) + "\\n工作电话:"
              + telNoDept + "\\nemail:" + YHUtility.encodeSpecial(email)
              + "\\nQQ:" + oicq + myState + showIp + "\"");
          record.append("}");
        }
        for (YHDepartment d : depts) {
          int nodeId = d.getSeqId();
          String name = d.getDeptName();
          int isHaveChild = IsHaveChild(dbConn, d.getSeqId());
          boolean extData = false;
          if (YHPrivUtil.isDeptPriv(dbConn, nodeId, mp, person)) {
            extData = true;
          }
          String imgAddress = contextPath
              + "/core/styles/style1/img/dtree/node_dept.gif";
          if (!"".equals(record.toString())) {
            record.append(",");
          }
          record.append("{");
          record.append("nodeId:\"" + nodeId + "\"");
          record.append(",name:\"" + YHUtility.encodeSpecial(name) + "\"");
          record.append(",isHaveChild:" + isHaveChild + "");
          record.append(",title:\"" + YHUtility.encodeSpecial(name) + "\"");
          record.append(",extData:" + extData);
          record.append(",imgAddress:\"" + YHUtility.encodeSpecial(imgAddress)
              + "\"");
          record.append("}");
        }
      }
      sb.append("[").append(record).append("]");
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

  /**
   * 判断是否有子部门
   * 
   * @param dbConn
   * @param response
   * @param id
   * @return
   * @throws Exception
   */
  // 判断是否有子部门和本部门是否有人:1为有子部门或者本部门有人0为都不是
  public int IsHaveChild(Connection dbConn, int id) throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("DEPT_PARENT", id);
    // 判断是否有子部门
    YHOrgSelectLogic osl = new YHOrgSelectLogic();
    ArrayList<YHDepartment> list = osl.getDepartmentList(dbConn, id);

    // List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class,
    // map);
    // 判断本部门是否有人    // System.out.println(list.size()+"=FGHJT");
    String[] str = { "DEPT_ID =" + id };
    String whereStr = "DEPT_ID =" + id;
    // List<YHPerson> personList = orm.loadListSingle(dbConn,
    // YHPerson.class,str);
    List<YHPerson> personList = osl.getPersonList(dbConn, whereStr);
    if (list.size() > 0 || personList.size() > 0) {
      return 1;
    } else {
      return 0;
    }
  }

  public boolean findId(String str, String id) {
    if (str == null || id == null || "".equals(str) || "".equals(id)) {
      return false;
    }
    String[] aStr = str.split(",");
    for (String tmp : aStr) {
      if (tmp.equals(id)) {
        return true;
      }
    }
    return false;
  }
  public String getIpState(HttpServletRequest request,
      HttpServletResponse response, Connection dbConn) throws Exception {
    String showIpFlag = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHOrgSelectLogic osl = new YHOrgSelectLogic();
      showIpFlag = osl.getSecrityShowIp(dbConn);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return showIpFlag;
  }
}

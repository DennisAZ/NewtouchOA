package yh.core.esb.client.act;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHEsbMessage;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.task.data.YHJhTaskLog;
import yh.subsys.jtgwjh.task.logic.YHJhTaskLogLogic;

public class YHDeptTreeAct {

  private static Logger log = Logger.getLogger(YHDeptTreeAct.class);
  
  public String getTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String idStr = request.getParameter("id");
    String orgId = "organizationNodeId";
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      if ((idStr == null || "".equals(idStr) || "0".equals(idStr)) && !orgId.equals(idStr)) {
        String name = YHUtility.encodeSpecial("组织机构");
        String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/system.gif";
        sb.append("{");
        sb.append("nodeId:\"" + orgId + "\"");
        sb.append(",name:\"" + name + "\"");
        sb.append(",isHaveChild:" + 1 + "");
        sb.append(",imgAddress:\"" + imgAddress + "\"");
        sb.append(",title:\"" + name + "\"");
        sb.append("},");
      } else  {
        if(orgId.equals(idStr)) {
          idStr = "0";
        }
        String query = "select DEPT_ID,DEPT_NAME  from oa_dept_ext where DEPT_PARENT = '" + idStr + "' order by DEPT_NO ASC, DEPT_NAME asc";
        ArrayList<YHExtDept> depts = new ArrayList();
        Statement stm4 = null;
        ResultSet rs4 = null;
        try {
          stm4 = dbConn.createStatement();
          rs4 = stm4.executeQuery(query);
          while (rs4.next()) {
            YHExtDept dept = new YHExtDept();
            dept.setDeptId(rs4.getString("DEPT_ID"));
            dept.setDeptName(rs4.getString("DEPT_NAME"));
            depts.add(dept);
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs4, null);
        }
        for (YHExtDept d : depts) {
          String nodeId = d.getDeptId()+"";
          String name = YHUtility.encodeSpecial(d.getDeptName());
          int isHaveChild = IsHaveChild(dbConn, d.getDeptId()+"");
          String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/node_dept.gif";
          sb.append("{");
          sb.append("nodeId:\"" + nodeId + "\"");
          sb.append(",name:\"" + name + "\"");
          sb.append(",isHaveChild:" + isHaveChild + "");
          sb.append(",imgAddress:\"" + imgAddress + "\"");
          sb.append(",title:\"" + name + "\"");
          sb.append("},");
        }
        
      }
      if (sb.length() > 1) {
        sb.deleteCharAt(sb.length() - 1);
      }
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
  
  public int IsHaveChild(Connection conn, String id) throws Exception {
    boolean flag = false;
    String query = "select 1 from oa_dept_ext where DEPT_PARENT = '" + id + "'";
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
  
  public String getTreePermissions(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String idStr = request.getParameter("id");
    String orgId = "organizationNodeId";
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      if ((idStr == null || "".equals(idStr) || "0".equals(idStr)) && !orgId.equals(idStr)) {
        String name = YHUtility.encodeSpecial("组织机构");
        String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/system.gif";
        sb.append("{");
        sb.append("nodeId:\"" + orgId + "\"");
        sb.append(",name:\"" + name + "\"");
        sb.append(",isHaveChild:" + 1 + "");
        sb.append(",imgAddress:\"" + imgAddress + "\"");
        sb.append(",title:\"" + name + "\"");
        sb.append("},");
      } else  {
        if(orgId.equals(idStr)) {
          idStr = "0";
        }
        
        YHDeptTreeLogic logic = new YHDeptTreeLogic();
        String deptPermisssions = logic.getPermissions(dbConn);
        String query = "select DEPT_ID,DEPT_NAME  from oa_dept_ext where DEPT_PARENT = '" + idStr + "' and DEPT_STATUE = 1 and DEPT_ID in ("+deptPermisssions+") order by DEPT_NO ASC, DEPT_NAME asc";
        ArrayList<YHExtDept> depts = new ArrayList();
        Statement stm4 = null;
        ResultSet rs4 = null;
        try {
          stm4 = dbConn.createStatement();
          rs4 = stm4.executeQuery(query);
          while (rs4.next()) {
            YHExtDept dept = new YHExtDept();
            dept.setDeptId(rs4.getString("DEPT_ID"));
            dept.setDeptName(rs4.getString("DEPT_NAME"));
            depts.add(dept);
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs4, null);
        }
        for (YHExtDept d : depts) {
          String nodeId = d.getDeptId()+"";
          String name = YHUtility.encodeSpecial(d.getDeptName());
          int isHaveChild = IsHaveChildPermissions(dbConn, d.getDeptId()+"", deptPermisssions);
          String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/node_dept.gif";
          sb.append("{");
          sb.append("nodeId:\"" + nodeId + "\"");
          sb.append(",name:\"" + name + "\"");
          sb.append(",isHaveChild:" + isHaveChild + "");
          sb.append(",imgAddress:\"" + imgAddress + "\"");
          sb.append(",title:\"" + name + "\"");
          sb.append("},");
        }
        
      }
      if (sb.length() > 1) {
        sb.deleteCharAt(sb.length() - 1);
      }
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
  
  public String getTreePermissions2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String idStr = request.getParameter("id");
    String orgId = "organizationNodeId";
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      if ((idStr == null || "".equals(idStr) || "0".equals(idStr)) && !orgId.equals(idStr)) {
        String name = YHUtility.encodeSpecial("组织机构");
        String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/system.gif";
        sb.append("{");
        sb.append("nodeId:\"" + orgId + "\"");
        sb.append(",name:\"" + name + "\"");
        sb.append(",isHaveChild:" + 1 + "");
        sb.append(",imgAddress:\"" + imgAddress + "\"");
        sb.append(",title:\"" + name + "\"");
        sb.append("},");
      } else  {
        if(orgId.equals(idStr)) {
          idStr = "0";
        }
        String query = "select DEPT_ID,DEPT_NAME  from oa_dept_ext where DEPT_PARENT = '" + idStr + "' and DEPT_STATUE = 1 order by DEPT_NO ASC, DEPT_NAME asc";
        ArrayList<YHExtDept> depts = new ArrayList();
        Statement stm4 = null;
        ResultSet rs4 = null;
        try {
          stm4 = dbConn.createStatement();
          rs4 = stm4.executeQuery(query);
          while (rs4.next()) {
            YHExtDept dept = new YHExtDept();
            dept.setDeptId(rs4.getString("DEPT_ID"));
            dept.setDeptName(rs4.getString("DEPT_NAME"));
            depts.add(dept);
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs4, null);
        }
        for (YHExtDept d : depts) {
          String nodeId = d.getDeptId()+"";
          String name = YHUtility.encodeSpecial(d.getDeptName());
          int isHaveChild = IsHaveChildPermissions2(dbConn, d.getDeptId()+"");
          String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/node_dept.gif";
          sb.append("{");
          sb.append("nodeId:\"" + nodeId + "\"");
          sb.append(",name:\"" + name + "\"");
          sb.append(",isHaveChild:" + isHaveChild + "");
          sb.append(",imgAddress:\"" + imgAddress + "\"");
          sb.append(",title:\"" + name + "\"");
          sb.append("},");
        }
        
      }
      if (sb.length() > 1) {
        sb.deleteCharAt(sb.length() - 1);
      }
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
  
  public String getTreePermissions3(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String idStr = request.getParameter("id");
    String reciveDept = request.getParameter("reciveDept");
    String orgId = "organizationNodeId";
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      if ((idStr == null || "".equals(idStr) || "0".equals(idStr)) && !orgId.equals(idStr)) {
        String name = YHUtility.encodeSpecial("组织机构");
        String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/system.gif";
        sb.append("{");
        sb.append("nodeId:\"" + orgId + "\"");
        sb.append(",name:\"" + name + "\"");
        sb.append(",isHaveChild:" + 1 + "");
        sb.append(",imgAddress:\"" + imgAddress + "\"");
        sb.append(",title:\"" + name + "\"");
        sb.append("},");
      } else  {
        if(orgId.equals(idStr)) {
          idStr = "0";
        }
        
        YHDeptTreeLogic logic = new YHDeptTreeLogic();
        String deptPermisssions = logic.getPermissions3(dbConn, reciveDept);
        String query = "select DEPT_ID,DEPT_NAME  from oa_dept_ext where DEPT_PARENT = '" + idStr + "' and DEPT_STATUE = 1 and DEPT_ID in ("+deptPermisssions+") order by DEPT_NO ASC, DEPT_NAME asc";
        ArrayList<YHExtDept> depts = new ArrayList();
        Statement stm4 = null;
        ResultSet rs4 = null;
        try {
          stm4 = dbConn.createStatement();
          rs4 = stm4.executeQuery(query);
          while (rs4.next()) {
            YHExtDept dept = new YHExtDept();
            dept.setDeptId(rs4.getString("DEPT_ID"));
            dept.setDeptName(rs4.getString("DEPT_NAME"));
            depts.add(dept);
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs4, null);
        }
        for (YHExtDept d : depts) {
          String nodeId = d.getDeptId()+"";
          String name = YHUtility.encodeSpecial(d.getDeptName());
          int isHaveChild = IsHaveChildPermissions2(dbConn, d.getDeptId()+"");
          String imgAddress = request.getContextPath() + "/core/styles/style1/img/dtree/node_dept.gif";
          sb.append("{");
          sb.append("nodeId:\"" + nodeId + "\"");
          sb.append(",name:\"" + name + "\"");
          sb.append(",isHaveChild:" + isHaveChild + "");
          sb.append(",imgAddress:\"" + imgAddress + "\"");
          sb.append(",title:\"" + name + "\"");
          sb.append("},");
        }
        
      }
      if (sb.length() > 1) {
        sb.deleteCharAt(sb.length() - 1);
      }
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
  
  public int IsHaveChildPermissions(Connection conn, String id, String deptPermisssions) throws Exception {
    
    boolean flag = false;
    String query = "select 1 from oa_dept_ext where DEPT_PARENT = '" + id + "' and DEPT_STATUE = 1 and DEPT_ID in ("+deptPermisssions+")";
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
  
  public int IsHaveChildPermissions2(Connection conn, String id) throws Exception {
    boolean flag = false;
    String query = "select 1 from oa_dept_ext where DEPT_PARENT = '" + id + "' and DEPT_STATUE = 1";
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
  
  public String dept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptId = request.getParameter("deptId");
      YHDeptTreeLogic deptLogic = new YHDeptTreeLogic();
      YHExtDept dept = deptLogic.getDept(dbConn, deptId);
      StringBuffer sb = YHFOM.toJson(dept);
      String ss = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, ss);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String selectDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDeptTreeLogic deptLogic = new YHDeptTreeLogic();
      String data = "";
      String deptId = request.getParameter("deptId");
       data = deptLogic.getDeptTreeJson("0" , deptId, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String updateDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptNo  = request.getParameter("DEPT_NO");
      String deptName  = request.getParameter("DEPT_NAME");
      String esbUser  = request.getParameter("ESB_USER");
      String deptParent  = request.getParameter("DEPT_PARENT");
      String deptDesc  = request.getParameter("DEPT_DESC");
      String deptId = request.getParameter("DEPT_ID");
      String deptStatue = request.getParameter("deptStatue");
      String deptTelLine = request.getParameter("deptTelLine");
      String deptCode = request.getParameter("deptCode");
      String deptFullName = request.getParameter("deptFullName");
      String deptPasscard = request.getParameter("deptPasscard");
      String deptGroup = request.getParameter("deptGroup");
      YHExtDept de = new YHExtDept(deptNo, deptName, esbUser, deptParent, deptDesc, deptStatue, deptTelLine, deptCode, deptFullName, deptPasscard, deptGroup,"");
      YHDeptTreeLogic deptLogic = new YHDeptTreeLogic();
      String flag = deptLogic.saveDept(dbConn , de , deptId);
      if("1".equals(flag)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      }
      else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "数据交换平台标识已配置！");
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 同步所有的esb用户的部门
   */
  public YHWSCaller caller = new YHWSCaller();
  public String broadcast(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH);
      String extDept = "sysDept.xml";
      String filePath = config.getCachePath() + File.separator + extDept;
      YHDeptTreeLogic logic = new YHDeptTreeLogic();
      String data = logic.getDepts2(dbConn);
      YHEsbMessage ms = new YHEsbMessage();
      ms.setData(data);
      ms.setMessage("sysDept");
      YHFileUtility.storeString2File(filePath, ms.toXml());
      caller.setWS_PATH(config.getWS_PATH());
      

      /**
       * 同步所有的单位，广播消息
       */
      String ret = caller.broadcast(filePath, config.getToken());
      // String ret = caller.send (filePath, "client2", config.getToken());
       Map map = YHFOM.json2Map(ret);
       YHDeptTreeLogic dtl = new YHDeptTreeLogic();
       YHExtDept ed = dtl.getDeptByEsbUser(dbConn,(config.getUserId()));//根据发送部门，查询组织机构
       String fromDept = "";
       String fromDeptName = "";
       if(ed != null){
         fromDept = ed.getDeptId()+"";//发送部门GUID
         fromDeptName = ed.getDeptName();//发送部门名称
       }
       /*
        * 获取所有接收单位，从组织机构上查询
        */
       String[] str = {"ESB_USER <> ''" ,"ESB_USER is not null"};
       List<YHExtDept>  listDept = YHDeptTreeLogic.select(dbConn, str);
       if(!YHUtility.isNullorEmpty(fromDept)){
         String code  = "";
         String guId = "";
         if(map != null){
            code = (String) map.get("code");
            guId = ((String)map.get("guid"));
         }
         Date date = new Date();
         for (int i = 0; i < listDept.size(); i++) {
           YHJhTaskLog task = new YHJhTaskLog();
           YHExtDept toDeptObj  = listDept.get(i);
           String toDept = "";
           String toDeptName = "";
           if(toDeptObj != null){
             toDept = toDeptObj.getDeptId()+"";//部门GUID
             toDeptName = toDeptObj.getDeptName();//部门名称
           }
           if(fromDept.equals(toDept)){//如果是本单位不需要发送
             continue;
           }
           task.setUserId(user.getSeqId() + "");
           task.setUserName(user.getUserName());
           task.setType("1");//1-组织机构 ;2-公章;3-公告
           task.setStatus("0");//发送
           task.setOptTime(date);
           task.setGuid(guId);
           task.setFromDept(fromDept);//发送部门
           task.setFromDeptName(fromDeptName);//发送部门名称
           task.setToDept(toDept);//接收部门
           task.setToDeptName(toDeptName);//
           YHJhTaskLogLogic.add(dbConn, task);
         }
         //系统日志
         YHSysLogLogic.addSysLog(dbConn, "62", "同步全体单位公章" ,user , request.getRemoteAddr());
         
       }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "同步成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId  = request.getParameter("deptId");
      YHDeptTreeLogic logic = new YHDeptTreeLogic();
      logic.delDept(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getDeptsByDeptParent(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptId  = request.getParameter("deptId");
      YHDeptTreeLogic logic = new YHDeptTreeLogic();
      String deptPermisssions = logic.getPermissions(dbConn);
      
      StringBuffer sb  = new StringBuffer();
      logic.getDeptsByDeptParent(dbConn, deptId ,0, sb, deptPermisssions);
      String data = sb.toString();
      String flag = "1";
      if ("".equals(data.trim())) {
        flag = "0";
      } else {
        data = data.substring(0 , data.length() - 1);
      }
      data = "[" + data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_MSRG, flag);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptsByDeptParent2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptId  = request.getParameter("deptId");
      YHDeptTreeLogic logic = new YHDeptTreeLogic();
      StringBuffer sb  = new StringBuffer();
      logic.getDeptsByDeptParent2(dbConn, deptId ,0, sb);
      String data = sb.toString();
      String flag = "1";
      if ("".equals(data.trim())) {
        flag = "0";
      } else {
        data = data.substring(0 , data.length() - 1);
      }
      data = "[" + data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_MSRG, flag);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 设置外发单位权限 -- yyb
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setPermissions(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String dept  = request.getParameter("dept");
    String permissions  = request.getParameter("permissions");
    String sendDept  = request.getParameter("sendDept");
    String sendDeptDesc  = request.getParameter("sendDeptDesc");
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDeptTreeLogic logic = new YHDeptTreeLogic();
      logic.setPermissions(dbConn, dept, permissions, sendDept, sendDeptDesc);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getGroupDept(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String flag  = request.getParameter("flag");
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDeptTreeLogic logic = new YHDeptTreeLogic();
      String data = logic.getGroupDept(dbConn, flag);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
}

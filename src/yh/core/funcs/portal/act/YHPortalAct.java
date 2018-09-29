package yh.core.funcs.portal.act;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.portal.data.YHPort;
import yh.core.funcs.portal.data.YHPortStyle;
import yh.core.funcs.portal.data.YHPortal;
import yh.core.funcs.portal.data.YHPortalPort;
import yh.core.funcs.portal.logic.YHPortalLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;


public class YHPortalAct {
  private String sp = System.getProperty("file.separator");
  private String webPath = "core" + sp 
    + "funcs" + sp 
    + "portal" + sp 
    + "modules" + sp;
  
  /**
   * 新建模块的时候   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String newPort(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String script = request.getParameter("script");
      String name = request.getParameter("title");
      String sql = request.getParameter("sql");
      String ruleList = request.getParameter("ruleList");
      String deptId = request.getParameter("deptId");
      String userId = request.getParameter("userId");
      String roleId =request.getParameter("roleId");
      deptId = (deptId == null) ? "" : deptId;
      userId = (userId == null) ? "" : userId;
      roleId = (roleId == null) ? "" : roleId;
      
      if (YHUtility.isNullorEmpty(script) || YHUtility.isNullorEmpty(name)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递数据或者信息不全");
        return "/core/inc/rtjson.jsp";
      }
      
      String sp = System.getProperty("file.separator");
      String path = request.getSession().getServletContext().getRealPath(sp) 
        + webPath 
        + name + ".js"; 
      File file = new File(path);
      if (file.exists()) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "该名称的模块已经存在,请重命名!");
        return "/core/inc/rtjson.jsp";
      }
      
      if (!YHUtility.isNullorEmpty(sql)) {
        String dataSql = "{\"sql\":\"" + sql + "\" , \"ruleList\":" + ruleList +"}";
        String dataPath = request.getSession().getServletContext().getRealPath(sp) 
          + webPath
          + "data" + sp 
          + "data.properties"; 
        try {
          File dataFile = new File(dataPath);
          if (!dataFile.exists()) {
            dataFile.createNewFile();
          }
          Map<String , String> dataMap = new HashMap();
          YHFileUtility.load2Map(dataPath, dataMap);
          dataMap.put(name, dataSql);
          Set<String> set = dataMap.keySet();
          String str = "";
          for (String key : set) {
            String value = dataMap.get(key);
            str += key + " = " + value + "\r\n";
          }
          YHFileUtility.storeString2File(dataPath, str);
        } catch (Exception ex) {
          throw ex;
        }
      }
      
      YHFileUtility.storeString2File(path, script);
      YHPort port = new YHPort();
      port.setFileName(name + ".js");
      port.setStatus(1);
      port.setDeptId(deptId);
      port.setPrivId(roleId);
      port.setUserId(userId);
      
      YHPortalLogic logic = new YHPortalLogic();
      logic.newPort(dbConn, port);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 新建门户布局
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateUserPortal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String portalStr = request.getParameter("portal");
      String portalName = request.getParameter("name");
      String idStr = request.getParameter("id");
      
      int id = -1;
      try {
        id = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        
      }
      
      if (YHUtility.isNullorEmpty(portalStr)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未传递字符串");
        return "/core/inc/rtjson.jsp";
      }
      
      YHPortalLogic logic = new YHPortalLogic();
      if (id >= 0) {
        YHPortal portal = logic.queryPortal(dbConn, id);
        
        if (portal == null) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "门户id错误, 无此门户");
          return "/core/inc/rtjson.jsp";
        }
        
        String path = request.getSession().getServletContext().getRealPath(sp) 
        + webPath + "portals" + sp
        + portal.getFileName();
        
        YHFileUtility.storeString2File(path, portalStr);
      }
      else {
        /*
        if (!logic.checkPortalName(dbConn, portalName)) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "门户名称重复或者未定义");
          return "/core/inc/rtjson.jsp";
        }*/
        YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHPortal portal = logic.queryPersonalPortal(dbConn, user.getSeqId());
        if (portal != null) {
          String path = request.getSession().getServletContext().getRealPath(sp) 
          + webPath + "portals" + sp
          + portal.getFileName();
          id = portal.getSeqId();
          YHFileUtility.storeString2File(path, portalStr);
        }
        else {
          portal = new YHPortal();
          portal.setUserId(user.getSeqId() + "");
          portalName = user.getUserName() + "的个人门户";
          String guid = YHGuid.getGuid();
          String fileName = guid + ".js";
          String path = request.getSession().getServletContext().getRealPath(sp) 
          + webPath + "portals" + sp + fileName;
          
          YHFileUtility.storeString2File(path, portalStr);
          
          portal.setFileName(fileName);
          portal.setPortalName(portalName);
          id = logic.newPortal(dbConn, portal);
        }
      }
      
      String data = "{id: " + id + "}";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加门户布局");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 检查重名
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkPortalName(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String name = request.getParameter("name");
      if ( YHUtility.isNullorEmpty(name)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        return "/core/inc/rtjson.jsp";
      }
      String query = "select * from portal where portal_name =  '" + name + "'";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        } else  {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得门户备注
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPortalRemark(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String id = request.getParameter("id");
      String query = "select remark from portal where seq_id =  " + id ;
      Statement stm = null;
      ResultSet rs = null;
      String remark = "";
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          remark = rs.getString("remark");
          if (remark == null) {
            remark = "";
          }
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      request.setAttribute(YHActionKeys.RET_DATA, "'"+remark+"'");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 保存门户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String savePortal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String name = request.getParameter("portalName");
      String id = request.getParameter("id");
      String remark = request.getParameter("remark");
      String query = "";
      String guId = YHGuid.getGuid() + ".js";
      if (YHUtility.isNullorEmpty(id)) {
        query = "insert into" +
        		" oa_portal (FILE_NAME, REMARK, STATUS, USER_ID, DEPT_ID, PRIV_ID, PORTAL_NAME)" +
        		" values ('"+ guId +"' , '" + remark +"' , 1 , null , null , null , '" + name + "')"; 
      } else {
        query = "update oa_portal set PORTAL_NAME='" + name + "' , REMARK = '"+ remark +"' where seq_id=" + id;
      }
      Statement stm = null;
      try {
        stm = dbConn.createStatement();
        stm.executeUpdate(query);
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, null, null);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 新建模块的时候
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updatePort(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String script = request.getParameter("script");
      String name = request.getParameter("title");
      String sql = request.getParameter("sql");
      String ruleList = request.getParameter("ruleList");
      String deptId = request.getParameter("deptId");
      String userId = request.getParameter("userId");
      String roleId =request.getParameter("roleId");
      String type = request.getParameter("type");
      
      deptId = (deptId == null) ? "" : deptId;
      userId = (userId == null) ? "" : userId;
      roleId = (roleId == null) ? "" : roleId;
      
      String oldName = request.getParameter("oldName");
      if (YHUtility.isNullorEmpty(script) || YHUtility.isNullorEmpty(name)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递数据或者信息不全");
        return "/core/inc/rtjson.jsp";
      }
      String sp = System.getProperty("file.separator");
      String path = request.getSession().getServletContext().getRealPath(sp) 
        + webPath 
        + name + ".js"; 
      if (!name.equals(oldName)) {
        File file = new File(path);
        if (file.exists()) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "该名称的模块已经存在,请重命名!");
          return "/core/inc/rtjson.jsp";
        }
        String oldPath = request.getSession().getServletContext().getRealPath(sp) 
          + webPath 
          + oldName + ".js"; 
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
          oldFile.delete();
        }
      }
      String dataPath = request.getSession().getServletContext().getRealPath(sp) 
      + webPath
      + "data" + sp 
      + "data.properties"; 
      File dataFile = new File(dataPath);
      if (!dataFile.exists()) {
        dataFile.createNewFile();
      }
      Map<String , String> dataMap = new HashMap();
      YHFileUtility.load2Map(dataPath, dataMap);
      if ("sql".equals(type)) {
        String dataSql = "{\"sql\":\"" + sql + "\" , \"ruleList\":" + ruleList +"}";
        try {
          dataMap.put(name, dataSql);
          if (!name.equals(oldName)) {
            dataMap.remove(oldName);
          }
        } catch (Exception ex) {
          throw ex;
        }
      } else {
        dataMap.remove(oldName);
      }
      Set<String> set = dataMap.keySet();
      String str = "";
      for (String key : set) {
        String value = dataMap.get(key);
        str += key + " = " + value + "\r\n";
      }
      YHFileUtility.storeString2File(dataPath, str);
      
      String idStr = request.getParameter("id");
      int id = 0 ;
      if (YHUtility.isInteger(idStr)) {
        id = Integer.parseInt(idStr);
      }
      YHFileUtility.storeString2File(path, script);
      YHPort port = new YHPort();
      port.setSeqId(id);
      port.setFileName(name + ".js");
      port.setDeptId(deptId);
      port.setPrivId(roleId);
      port.setUserId(userId);
      YHPortalLogic logic = new YHPortalLogic();
      logic.updatePort(dbConn, port);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 修改模块权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updatePortPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String id = request.getParameter("portId");
      String role = request.getParameter("role");
      String dept = request.getParameter("dept");
      String user = request.getParameter("user");
      
      YHPortalLogic logic = new YHPortalLogic();
      YHPort port = new YHPort();
      port.setSeqId(Integer.parseInt(id));
      port.setUserId(user);
      port.setDeptId(dept);
      port.setPrivId(role);
      logic.updatePortPriv(dbConn, port);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除模块权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delPort(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String idStr = request.getParameter("id");
      int id = 0 ;
      if (YHUtility.isInteger(idStr)) {
        id = Integer.parseInt(idStr);
      }
      String rootPath = request.getSession().getServletContext().getRealPath(sp) +  webPath;
      YHPortalLogic logic = new YHPortalLogic();
      logic.deletePort(dbConn, id, rootPath);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除门户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delPortal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String idStr = request.getParameter("id");
      int id = 0 ;
      if (YHUtility.isInteger(idStr)) {
        id = Integer.parseInt(idStr);
      }
      YHPortalLogic logic = new YHPortalLogic();
      logic.deletePortal(dbConn, id);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 设置门户权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setPortalPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String idStr = request.getParameter("id");
      int id = 0 ;
      if (YHUtility.isInteger(idStr)) {
        id = Integer.parseInt(idStr);
      }
      YHPortalLogic logic = new YHPortalLogic();
      String role = request.getParameter("role");
      String user = request.getParameter("user");
      String dept = request.getParameter("dept");
      if (role == null) {
        role = "";
      }
      if (user == null) {
        user = "";
      }
      if (dept == null) {
        dept = "";
      }
      logic.setPortalPriv(dbConn, id , role , user ,dept);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得门户权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPortalPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String idStr = request.getParameter("id");
      int id = 0 ;
      if (YHUtility.isInteger(idStr)) {
        id = Integer.parseInt(idStr);
      }
      YHPortalLogic logic = new YHPortalLogic();
      String data = logic.getPortalPriv(dbConn, id );
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取得权限");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 列出属于用户的所有模块,考虑了权限问题   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listPorts(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String idStr = request.getParameter("id");
      int id = -1;
      try {
        id = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        
      }
      
      if ("personal".equals(idStr)) {
        id = -2;
      }
      
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHPortalLogic logic = new YHPortalLogic();
      String data = logic.listPorts(dbConn, user, id);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 列出门户，没有考虑权限问题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listPortal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      YHPortalLogic logic = new YHPortalLogic();
      String data = logic.listPortal(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得模块数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getModData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String idStr = request.getParameter("id");
      int id = 0 ;
      if (YHUtility.isInteger(idStr)) {
        id = Integer.parseInt(idStr);
      }
      YHPortalLogic logic = new YHPortalLogic();
      String wPath =  request.getSession().getServletContext().getRealPath(sp) +  webPath;
      String data = logic.getModData(dbConn, id, wPath);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 列出用户拥有权限的所有模块   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listAllPorts(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String priv = user.getUserPriv();
      String privOther = user.getUserPrivOther();
      int dept = user.getDeptId();
      String deptOther = user.getDeptIdOther();
      
      Set<String> deptSet = YHPortalAct.string2Set(deptOther, String.valueOf(dept));
      Set<String> privSet = YHPortalAct.string2Set(privOther, String.valueOf(priv));
      
      YHPortalLogic logic = new YHPortalLogic();
      List<YHPort> portList = logic.listPort(dbConn, userId, deptSet, privSet);
      List<Map<String, String>> list = new ArrayList<Map<String, String>>();
      for (YHPort p : portList) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(p.getSeqId()));
        map.put("file", p.getFileName());
        list.add(map);
      }
      
      String data = "{records:"
      + logic.toJson(list) + "}";
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 列出用户拥有权限的所有模块   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listAllPortals(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String priv = user.getUserPriv();
      String privOther = user.getUserPrivOther();
      int dept = user.getDeptId();
      String deptOther = user.getDeptIdOther();
      
      Set<String> deptSet = YHPortalAct.string2Set(deptOther, String.valueOf(dept));
      Set<String> privSet = YHPortalAct.string2Set(privOther, String.valueOf(priv));
      
      YHPortalLogic logic = new YHPortalLogic();
      List<YHPortal> portalList = logic.listPortal(dbConn, userId, deptSet, privSet);
      List<Map<String, String>> list = new ArrayList<Map<String, String>>();
      for (YHPortal p : portalList) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(p.getSeqId()));
        map.put("file", p.getFileName());
        map.put("remark", p.getRemark());
        map.put("name", p.getPortalName());
        list.add(map);
      }
      
      String data = "{records:"
        + logic.toJson(list) + "}";
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 设置默认门户(带权限判断)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setDefaultPortal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
      String idStr = request.getParameter("id");
      int id = -1;
      try {
        id = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "门户参数传递错误");
        return "/core/inc/rtjson.jsp";
      }
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String priv = user.getUserPriv();
      String privOther = user.getUserPrivOther();
      int dept = user.getDeptId();
      String deptOther = user.getDeptIdOther();
      
      Set<String> deptSet = YHPortalAct.string2Set(deptOther, String.valueOf(dept));
      Set<String> privSet = YHPortalAct.string2Set(privOther, String.valueOf(priv));
      
      YHPortalLogic logic = new YHPortalLogic();
      List<YHPortal> portalList = logic.listPortal(dbConn, userId, deptSet, privSet);
      List<Map<String, String>> list = new ArrayList<Map<String, String>>();
      for (YHPortal p : portalList) {
        if (id == p.getSeqId()) {
          logic.setDefaultPortal(dbConn, user, id);
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
          return "/core/inc/rtjson.jsp";
        }
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "无权限或者id传递有误!");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 检查重名   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkName(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    try {
      String name = request.getParameter("title");
      if ( YHUtility.isNullorEmpty(name)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        return "/core/inc/rtjson.jsp";
      }
      String sp = System.getProperty("file.separator");
      String path = request.getSession().getServletContext().getRealPath(sp) 
        + "core" + sp 
        + "funcs" + sp 
        + "portal" + sp 
        + "modules" + sp 
        + name + ".js"; 
      File file = new File(path);
      if (file.exists()) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        return "/core/inc/rtjson.jsp";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 更新门户的模块   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updatePortalPorts(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
     
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String idStr = request.getParameter("id");
      int portalId = -1;
      try {
        portalId = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未传递ID或者传递有误");
        return "/core/inc/rtjson.jsp";
      }
      
      YHPortalLogic logic = new YHPortalLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHPortalPort> list = logic.listPortalPort(dbConn, portalId);

      List<Integer> idList = new ArrayList<Integer>();
      for (YHPortalPort up : list) {
        idList.add(up.getPortId());
      }
      
      Map<String, String[]> params = request.getParameterMap();
      for (YHPortalPort up : list) {
        if (params.get(String.valueOf(up.getPortId()) + "[]") == null) {
          logic.deletePortStyle(dbConn, up.getStyleId());
          logic.deletePortalPort(dbConn, up.getSeqId());
          continue;
        }
      }
      
      for (Iterator<Entry<String,String[]>> it = params.entrySet().iterator(); it.hasNext();){
        Entry<String,String[]> e = it.next();
        try {
          int id = Integer.parseInt(e.getKey().trim().replace("[]", ""));
          String[] style = e.getValue();
          if (style.length <  6) {
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
            request.setAttribute(YHActionKeys.RET_MSRG, "ID为" + e.getKey() + "的模块样式传递错误");
            return "/core/inc/rtjson.jsp";
          }
          
          List<String> temp = new ArrayList<String>(Arrays.asList(style)).subList(2, 6);
          
          if (!idList.contains(id)) {
            String guid = YHGuid.getGuid();
            YHPortStyle ps = strings2PortStyle(temp, guid);
            YHPortalPort pp = new YHPortalPort();
            pp.setContainer(style[0]);
            pp.setPortId(id);
            pp.setSortNo(Integer.parseInt(style[1]));
            pp.setStyleId(guid);
            pp.setPortalId(portalId);
            logic.addPortStyle(dbConn, ps);
            logic.addPortalPort(dbConn, pp);
          }
          else {
            YHPortalPort up = list.get(idList.indexOf(id));
            String styleId = up.getStyleId();
            up.setSortNo(Integer.parseInt(style[1]));
            YHPortStyle portStyle = strings2PortStyle(temp, styleId);
            logic.updatePortStyle(dbConn, portStyle);
            logic.updatePortalPort(dbConn, style[0], up.getSeqId(), up.getSortNo());
          }
        } catch (NumberFormatException ex) {
          continue;
        }
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 用逗号分隔的String转化为set(不重复的id串)
   * @param strs 多个String
   * @return
   */
  public static Set<String> string2Set(String ... strs) {
    Set<String> set = new HashSet<String>();
    for (String s : strs) {
      if (!YHUtility.isNullorEmpty(s)) {
        set.addAll(Arrays.asList(s.split(",")));
      }
    }
    return set;
  }
  
  /**
   * 获取模块的路径,为了避免路径上出现中文)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String viewPort(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String file = request.getParameter("file");
    String idStr = request.getParameter("id");
    Connection dbConn;
    int id = -1;
    try {
      try {
        id = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        
      }
      if (id >= 0) {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPortalLogic logic = new YHPortalLogic();
        YHPort port = logic.queryPort(dbConn, id);
        if (port != null) {
          file = port.getFileName();
        }
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/portal/modules/" + file;
  }
  
  /**
   * 获取模块的路径,为了避免路径上出现中文)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String viewLayout(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String file = request.getParameter("file");
    
    return "/core/funcs/portal/modules/layouts/" + file;
  }
  
  private YHPortStyle strings2PortStyle(List<String> style, String guid) {
    YHPortStyle ps = new YHPortStyle();
    ps.setGuid(guid);
    ps.setWidth(style.get(0));
    ps.setHeight(style.get(1));
    try {
      ps.setPosX(Integer.parseInt(style.get(2)));
      ps.setPosY(Integer.parseInt(style.get(3)));
    } catch (NumberFormatException ex) {
      ps.setPosX(0);
      ps.setPosY(0);
    }
    return ps;
  }
  
}
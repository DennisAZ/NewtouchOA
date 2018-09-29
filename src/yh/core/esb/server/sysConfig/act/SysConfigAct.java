package yh.core.esb.server.sysConfig.act;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.server.logic.YHEsbServerLogic;
import yh.core.esb.server.sysConfig.data.ClientConfig;
import yh.core.esb.server.sysConfig.data.SysConfig;
import yh.core.esb.server.sysConfig.logic.SysConfigLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class SysConfigAct {
  private SysConfigLogic logic = new SysConfigLogic();
  /**
   * 读取系统配置
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSysConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SysConfig sysConfig = (SysConfig)this.logic.getSysConfigLogic(dbConn , request);
      StringBuffer data = YHFOM.toJson(sysConfig);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateSysConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = YHFOM.buildMap(request.getParameterMap());
    String contexPath = request.getContextPath();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.updateSysConfigLogic(dbConn , request , map);
      
      //向状态表插入配置文件下载数据
      //YHEsbServerLogic serlogic = new YHEsbServerLogic();
      //serlogic.broadcastConfig(dbConn, request.getSession().getServletContext().getRealPath(File.separator) + "WEB-INF\\config\\esbconfig.properties" , "" , "");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    response.sendRedirect(contexPath + "/core/esb/server/user/success.jsp");
    return null;
  }
  
  public String getClientConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      ClientConfig clientConfig = (ClientConfig)this.logic.getClientConfigLogic(dbConn , request);
      StringBuffer data = YHFOM.toJson(clientConfig);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateClientConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = YHFOM.buildMap(request.getParameterMap());
    String contexPath = request.getContextPath();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.updateClientConfigLogic(dbConn , request , map);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    response.sendRedirect(contexPath + "/core/esb/server/user/success.jsp");
    return null;
  }
}

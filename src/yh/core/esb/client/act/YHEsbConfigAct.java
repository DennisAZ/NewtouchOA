package yh.core.esb.client.act;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.esb.frontend.services.YHEsbServiceLocal;
import yh.core.esb.server.logic.YHEsbServerLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHEsbConfigAct {
  public YHWSCaller caller = new YHWSCaller();
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
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      //request.setAttribute(YHActionKeys.RET_DATA, data.toString());
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
      //this.logic.updateSysConfigLogic(dbConn , request , map);
      
      //向状态表插入配置文件下载数据
      YHEsbServerLogic serlogic = new YHEsbServerLogic();
      serlogic.broadcastConfig(dbConn, request.getSession().getServletContext().getRealPath(File.separator) + "WEB-INF\\config\\esbconfig.properties" , "" , "");
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
      YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
      StringBuffer data = YHFOM.toJson(config);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String isOnline(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
      
      boolean flag = false;
      if ("1".equals(config.getLocal())) {
        YHEsbServiceLocal local = new YHEsbServiceLocal();
        flag =  local.isOnline();
      } else {
        caller.setWS_PATH(config.getWS_PATH());
        flag = caller.isOnline(config.getToken());
      }      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, flag + "");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String updateClientConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
      config.setCachePath(YHUtility.null2Empty(request.getParameter("cachePath")));
      config.setESBHOST(YHUtility.null2Empty(request.getParameter("ESBHOST")));
      config.setESBPORT(YHUtility.null2Empty(request.getParameter("ESBPORT")));
      config.setESBSERVER(YHUtility.null2Empty(request.getParameter("ESBSERVER")));
      String s = request.getParameter("ESBSERVERPORT");
      config.setESBSERVERPORT(YHUtility.null2Empty(s));
      
      config.setLocal(YHUtility.null2Empty(request.getParameter("isLocal")));
      config.setUserId(YHUtility.null2Empty(request.getParameter("userId")));
      config.setPassword(YHUtility.null2Empty(request.getParameter("password")));
      config.setOAHOST(YHUtility.null2Empty(request.getParameter("OAHOST")));
      config.setOAPORT(YHUtility.null2Empty(request.getParameter("OAPORT")));
      config.setToken(YHUtility.null2Empty(request.getParameter("token")));
      config.store(request.getRealPath("/") + YHEsbConst.CONFIG_PATH);
      caller.setWS_PATH(config.getWS_PATH());
      if ("1".equals(config.getLocal())) {
        YHEsbServiceLocal local = new YHEsbServiceLocal();
        local.config(config.getESBSERVER(), Integer.parseInt(config.getESBSERVERPORT()), config.getUserId(), config.getPassword(), config.getWebserviceUri(), config.getCachePath() , config.getLocal());
      } else {
        caller.config(config.getESBSERVER(), Integer.parseInt(config.getESBSERVERPORT()), config.getUserId(), config.getPassword(), config.getWebserviceUri(), config.getCachePath() , config.getToken());
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}

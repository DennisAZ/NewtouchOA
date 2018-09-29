package yh.core.servlet;


import java.io.IOException;
import java.lang.reflect.Method;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.OpenAccount;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRunThread;
import yh.core.data.YHDBMapExcelReader;
import yh.core.data.YHDataSources;
import yh.core.data.YHRequestDbConn;
import yh.core.data.YHSessionPool;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHConfigLoader;
import yh.core.util.YHUtility;

/**
 * 
 * @author shenrm
 * 
 * 2013-01-07
 *
 */
public class YHActionServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static Logger log = Logger.getLogger(YHActionServlet.class);
  /**
   * 系统初始化
   */
  public void init() throws ServletException {
	    super.init();
	    try {
	    	/*初始化excel 数据库映射*/
	    	new YHDBMapExcelReader().initYHDBMapExcelReader();
	      String rootPath = getServletContext().getRealPath("/");
	      YHConfigLoader.loadInit(rootPath);
	      
	      /*开账*/
	      OpenAccount.init(rootPath);
	    }catch (Exception ex) {
	      log.debug(ex.getMessage(), ex);
	    }
	  }
  
  /**
   * 释放资源
   */
  public void  destroy() {
    System.out.println("YH start destroy...");
    log.debug("YH start destroy...");
    YHSessionPool.stopReleaseThread();
    YHDataSources.closeConnPool();
    YHAutoRunThread.stopRun();
    System.out.println("YH end destroy.");
    log.debug("YH end destroy.");
  }
  
  /**
   * <p>Process an HTTP "GET" request.</p>
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public void doGet(HttpServletRequest request,
            HttpServletResponse response)
      throws IOException, ServletException {
    response.setHeader("PRagma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 
    response.setDateHeader("Expires", 0); 
    request.setAttribute(YHActionKeys.ACT_CTX_PATH, YHServletUtility.getWebAppDir(this.getServletContext()));
    String className = request.getParameter(YHActionKeys.ACT_CLASS);
    String methodName = request.getParameter(YHActionKeys.ACT_METHOD);
    if (YHUtility.isNullorEmpty(className)) {
      String qUri = request.getRequestURI();
      qUri = qUri.substring(request.getContextPath().length() + 1);
      int tmpIndex = qUri.lastIndexOf(".");
      if (tmpIndex > 0) {
        int tmpIndex2 = qUri.lastIndexOf("/");
        if (tmpIndex2 > 0) {
          className = qUri.substring(0, tmpIndex2);
          methodName = qUri.substring(tmpIndex2 + 1, tmpIndex);
        }
      }
    }
    String rtType = YHServletUtility.getParam(request, YHActionKeys.RET_TYPE);
    String rtUrl = null;
    if (rtType.equals(YHConst.RET_TYPE_XML)) {
      rtUrl = "/core/inc/rtxml.jsp";
    }else {
      rtUrl = "/core/inc/rtjson.jsp";
    }
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try {
      if (YHUtility.isNullorEmpty(className)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递处理类名称");
        YHServletUtility.forward(rtUrl, request, response);
        return;
      }
      if (YHUtility.isNullorEmpty(methodName)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递处理方法名称");
        YHServletUtility.forward(rtUrl, request, response);
        return;
      }
      Class classObj = Class.forName(className.replace("/", "."));
      Class[] paramTypeArray = new Class[]{HttpServletRequest.class, HttpServletResponse.class};
      Method methodObj = classObj.getMethod(methodName, paramTypeArray);
      String forWardUrl = (String)methodObj.invoke(classObj.newInstance(), new Object[]{request, response});
      if (requestDbConn != null) {
        requestDbConn.commitAllDbConns();
      }
      if (!YHUtility.isNullorEmpty(forWardUrl)) {
        String retMethod = (String)request.getAttribute(YHActionKeys.RET_METHOD);
        if (YHUtility.isNullorEmpty(retMethod)) {
          YHServletUtility.forward(forWardUrl, request, response);
        }else {
          String contextPath = request.getContextPath();
          if (YHUtility.isNullorEmpty(contextPath)) {
            contextPath = "/yh";
          }
          response.sendRedirect(contextPath + forWardUrl);
        }
      }
    }catch(Throwable t) {
      t.printStackTrace();
      log.debug(t.getMessage(), t);
      if (requestDbConn != null) {
        requestDbConn.rollbackAllDbConns();
      }
      if (!response.isCommitted()) {
        String forwardPath = (String)request.getAttribute(YHActionKeys.FORWARD_PATH);//YHActionKeys.FORWARD_PATH);
        if (forwardPath == null) {
          forwardPath = rtUrl;
        }
        YHServletUtility.forward(forwardPath, request, response);
      }
    }finally {
    }
  }
  
  /**
   * <p>Process an HTTP "POST" request.</p>
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public void doPost(HttpServletRequest request,
             HttpServletResponse response)
      throws IOException, ServletException {

      doGet(request, response);
  }
}

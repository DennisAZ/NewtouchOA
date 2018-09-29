package yh.core.servlet;

import java.util.Map;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

/**
 * 请求监听者
 * @author jpt
 *
 */
public class YHServletRequestListener implements ServletRequestListener {
  public void requestInitialized(ServletRequestEvent sre) {
    HttpServletRequest request = (HttpServletRequest)sre.getServletRequest();
    try {
      if (YHServletUtility.isGbkCode(request)) {
        request.setCharacterEncoding("GBK");
      }else {
        request.setCharacterEncoding(YHConst.DEFAULT_CODE);
      }
    }catch(Exception ex) {      
    }
    YHRequestDbConn requestDbConn = new YHRequestDbConn("");
    request.setAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR, requestDbConn);
    request.getSession().setAttribute(YHBeanKeys.CURR_REQUEST_FLAG, true);
    request.getSession().setAttribute(YHBeanKeys.CURR_REQUEST_ADDRESS, request.getRemoteAddr());
    
    
    Map<String, String[]> paramMap = request.getParameterMap();
    String kiloSplitHidden = request.getParameter("kiloSplitCntrlIds");
    if (kiloSplitHidden != null && kiloSplitHidden.length() > 1) {
      if (kiloSplitHidden.startsWith(",")) {
        kiloSplitHidden = kiloSplitHidden.substring(1);
      }
      if (kiloSplitHidden.endsWith(",")) {
        kiloSplitHidden = kiloSplitHidden.substring(0, kiloSplitHidden.length() - 1);
      }
      String[] kiloSplitArray = kiloSplitHidden.split(",");
      for (int i = 0; i < kiloSplitArray.length; i++) {
        String inputName = kiloSplitArray[i].trim();
        if (YHUtility.isNullorEmpty(inputName)) {
          continue;
        }
        String[] paramValueArray = paramMap.get(inputName);
        if (paramValueArray != null) {
          String paramValue = paramValueArray[0];
          if (!YHUtility.isNullorEmpty(paramValue) && paramValue.indexOf(",") > 0) {
            paramValueArray[0] = paramValue.replace(",", "");
          }
        }
      }
    }
  }
  public void requestDestroyed(ServletRequestEvent sre) {    
    HttpServletRequest request = (HttpServletRequest)sre.getServletRequest();
    request.getSession().removeAttribute(YHBeanKeys.CURR_REQUEST_FLAG);
    request.getSession().removeAttribute(YHBeanKeys.CURR_REQUEST_ADDRESS);
    
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    if (requestDbConn != null) {
      requestDbConn.closeAllDbConns();
    }
  }
}

package yh.rad.docs.iframe.c2s;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;

public class YHClient2ServerDemo {

  /**
   * ajax方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String ajax1(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    try {
      String data = "连接成功后返回的数据.";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "服务端异常");
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
  /**
   * ajax方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String ajax2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String data = "连接成功后返回的数据.";
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception e) {
      throw e;
    }
    return null;
  }
  /**
   * 传统的处理方式,非ajax方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String tradition(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      request.setCharacterEncoding(YHConst.DEFAULT_CODE);
      response.setCharacterEncoding(YHConst.DEFAULT_CODE);
      request.setAttribute("rtData", "连接成功后返回的数据.");
    } catch (Exception e) {
      throw e;
    }
    return "/rad/docs/module/c2s/rtTest.jsp";
  }
}

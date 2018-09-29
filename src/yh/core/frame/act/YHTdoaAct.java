package yh.core.frame.act;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yh.core.data.YHRequestDbConn;
import yh.core.frame.logic.YHTdoaLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHTdoaAct {
  
  public String updateUserParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      String oaItem = request.getParameter("oaItem");
      
      YHTdoaLogic logic = new YHTdoaLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      person = logic.updateUserParam(dbConn, oaItem, person);
      
      request.getSession().setAttribute("LOGIN_USER",person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改");
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String showParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHTdoaLogic logic = new YHTdoaLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String data = logic.getUserParamOaItem(dbConn, person,request.getContextPath());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateUserParamStyle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      String oaStyle = request.getParameter("oaStyle");
      
      YHTdoaLogic logic = new YHTdoaLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      logic.updateUserParamStyle(dbConn, oaStyle, person);
      
      request.getSession().setAttribute("LOGIN_USER",person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改");
      HttpSession session = request.getSession(true);
      session.setAttribute("OA_STYLE", oaStyle);
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String showParamOAstyle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHTdoaLogic logic = new YHTdoaLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String data = logic.getUserParamOaStyle(dbConn, person);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
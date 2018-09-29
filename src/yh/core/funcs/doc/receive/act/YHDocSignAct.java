package yh.core.funcs.doc.receive.act;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.receive.data.YHDocReceive;
import yh.core.funcs.doc.receive.logic.YHDocReceiveLogic;
import yh.core.funcs.doc.receive.logic.YHDocReceiveRegLogic;
import yh.core.funcs.doc.receive.logic.YHDocSignLogic;
import yh.core.funcs.doc.receive.logic.YHDocSmsLogic;
import yh.core.funcs.doc.send.logic.YHDocLogic;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
public class YHDocSignAct{
  /**
   * 取得有权限的收文
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRegList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      PrintWriter pw = response.getWriter();
      YHDocSignLogic doc = new YHDocSignLogic();
      String isSign = request.getParameter("isSign");
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String data = doc.getSendMesage(user, dbConn, request.getParameterMap(), request.getRealPath("/") , isSign);
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 取得有权限的收文
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRegListDesktop(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDocSignLogic doc = new YHDocSignLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String data = doc.getSendMesageDesktop(user, dbConn, request.getParameterMap(), request.getRealPath("/") );
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String sign(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHDocSignLogic doc = new YHDocSignLogic();
      doc.sign(dbConn ,seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String comeback(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHDocSignLogic doc = new YHDocSignLogic();
      doc.comeback(dbConn ,seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

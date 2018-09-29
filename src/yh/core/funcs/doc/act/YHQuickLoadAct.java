package yh.core.funcs.doc.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.logic.YHDelegateLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.logic.YHQuickLoadLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHQuickLoadAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.act.YHQuickLoadAct");
  public String getQuickLoad(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(request.getParameter("runId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      int itemId = Integer.parseInt(request.getParameter("itemId"));
      String selectedItem = request.getParameter("selectedItem");
      YHQuickLoadLogic logic = new YHQuickLoadLogic();
      String str = logic.getQuickLoad(dbConn, flowId, runId, itemId, user, selectedItem);
      str = str.replaceAll("\r", "&#13;");
      str = str.replaceAll("\n", "&#10;");
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "ok");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.toString());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

package yh.core.funcs.doc.flowrunRec.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.flowrunRec.logic.YHFlowManageLogic;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHFlowManageAct {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.doc.flowrunRec.act.YHFlowManageAct");


  public String focus(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String sRunId = request.getParameter("runId");
    int runId = Integer.parseInt(sRunId);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson u = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic manage = new YHFlowManageLogic();
      String focusUser = manage.getFocusUser(runId, dbConn);
      if (YHWorkFlowUtility.findId(focusUser, String.valueOf(u.getSeqId()))) {
        this.setRequestSuccess(request, "您已经关注了此工作！");
        return "/core/inc/rtjson.jsp";
      } else {
        manage.focus(u, focusUser, runId, request.getContextPath(), dbConn);
      }
      this.setRequestSuccess(request, "操作成功！");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }


  /**
   * 设置错误信息
   * 
   * @param request
   * @param message
   */
  public void setRequestError(HttpServletRequest request, String message) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }

  /**
   * 设置成功信息
   * 
   * @param request
   * @param message
   */
  public void setRequestSuccess(HttpServletRequest request, String message) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }

  /**
   * 设置成功信息
   * 
   * @param request
   * @param message
   * @param data
   */
  public void setRequestSuccess(HttpServletRequest request, String message,
      String data) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
    request.setAttribute(YHActionKeys.RET_DATA, data);
  }

}
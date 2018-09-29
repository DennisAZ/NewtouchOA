package yh.core.funcs.doc.flowrunRec.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowSort;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.flowrunRec.logic.YHFlowWorkSearchLogic;
import yh.core.funcs.doc.logic.YHFlowSortLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.logic.YHFlowWorkAdSearchLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHWorkQueryAct {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.doc.flowrunRec.act.YHWorkQueryAct");
  public String getWorkList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String sSortId =  request.getParameter("sortId");
      YHFlowWorkSearchLogic myWorkLogic = new YHFlowWorkSearchLogic();
      StringBuffer result = myWorkLogic.getWorkList(dbConn,request.getParameterMap(), loginUser , sSortId , request.getRealPath("/"));
      PrintWriter pw = response.getWriter();
      pw.println( result.toString());
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}

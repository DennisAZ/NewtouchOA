package yh.core.funcs.doc.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

/**
 * 与流程管理相关的一些功能
 * @author 刘涵
 *
 */
public class YHFlowTypeCorAct {
  private static Logger log = Logger.getLogger(YHFlowTypeCorAct.class);
  /**
   * 校验
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String sFlowId = request.getParameter("flowId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic logic = new YHFlowTypeLogic();
      String data = logic.checkFlowType(Integer.parseInt(sFlowId) , dbConn);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getCloneMsg(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String sFlowId = request.getParameter("flowId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic logic = new YHFlowTypeLogic();
      String data = logic.getCloneMsg(Integer.parseInt(sFlowId), dbConn);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String clone(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String sFlowId = request.getParameter("flowId");
    String flowName = request.getParameter("flowName");
    String sFlowNo = request.getParameter("flowNo");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic logic = new YHFlowTypeLogic();
      logic.clone(Integer.parseInt(sFlowId), flowName, sFlowNo, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String trans(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String flowIdStr = request.getParameter("flowIdStr");
    String toId = request.getParameter("toId");
    String userId = request.getParameter("userId");
    String beginRun = request.getParameter("beginRun");
    String endRun = request.getParameter("endRun");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic logic = new YHFlowTypeLogic();
      logic.trans(dbConn, flowIdStr, toId, userId , beginRun , endRun);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "移交成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String search(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String search = request.getParameter("searchKey");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      StringBuffer sb = new StringBuffer("[");
      String data = "";
      StringBuffer workCounts = new StringBuffer("[");
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      List<YHDocFlowType> typeList = flowTypeLogic.getFlowTypeList(dbConn);
      int count = 0 ;
      YHWorkFlowUtility w = new YHWorkFlowUtility();
      YHPerson u = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      for (YHDocFlowType flowType  : typeList) {
        if (flowType.getFlowName().contains(search)){
          if (!w.isHaveRight(flowType.getDeptId(), u, dbConn)) {
            continue;
          }
          sb.append(flowType.toJson() + ",");
          //取得工作数量
          int workCount = flowTypeLogic.getWorkCountByFlowId(flowType.getSeqId() , dbConn);
          int delCount = flowTypeLogic.getDelWorkCountByFlowId(flowType.getSeqId() , dbConn);
          workCounts.append("{workCount:" + workCount);
          workCounts.append(",delCount:" + delCount + "},");
          count ++ ;
        }
      }
      if (count >  0) {
        sb.deleteCharAt(sb.length() - 1);
        workCounts.deleteCharAt(workCounts.length() - 1);
      }
      workCounts.append("]");
      sb.append("]");
      data = "{flowList:" + sb.toString() + ",workCounts:" + workCounts.toString() + "}";
      request.setAttribute(YHActionKeys.RET_DATA, data );
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

package yh.core.funcs.system.resManage.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.resManage.logic.YHEmailResManageLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHEmailResManageAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.system.resManage.act.YHEmailResManageAct");
  
  public String searchEmail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String toId = request.getParameter("toId");
      String copyToId = request.getParameter("copyToId");
      String subject = request.getParameter("subject");
      String attachmentName = request.getParameter("attachmentName");
      String content = request.getParameter("content");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      
      YHEmailResManageLogic logic = new YHEmailResManageLogic();
      String data = logic.searchEmail(dbConn , toId , copyToId , subject , attachmentName , content , beginDate , endDate);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String deleteEmail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String idStr = request.getParameter("emailId");
      YHEmailResManageLogic logic = new YHEmailResManageLogic();
      logic.deleteEmail(dbConn , idStr);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getEmail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String emailId = request.getParameter("emailId");
      YHEmailResManageLogic logic = new YHEmailResManageLogic();
      String data = logic.readEmail(dbConn , emailId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

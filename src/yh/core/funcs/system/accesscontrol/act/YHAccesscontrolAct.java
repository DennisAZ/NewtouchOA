package yh.core.funcs.system.accesscontrol.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.accesscontrol.data.YHAccessControl;
import yh.core.funcs.system.accesscontrol.logic.YHAccesscontrolLogic;
import yh.core.funcs.system.diary.logic.YHDiaryLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHAccesscontrolAct {
  private static Logger log = Logger.getLogger(YHAccesscontrolAct.class);
  
  public String updateAccessControl(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAccesscontrolLogic orgLogic = new YHAccesscontrolLogic();
      //System.out.println("OOOOOOOOOOOO");
      String accessControlId = request.getParameter("accessControlId");
      //System.out.println(accessControlId+"EEEEEEEe");
      orgLogic.updateAccessControl(dbConn, accessControlId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addAccessControl(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDiaryLogic orgLogic = new YHDiaryLogic();
      String statrTime = request.getParameter("statrTime");
      String endTime = request.getParameter("endTime");
      //Date d =new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
      String days = request.getParameter("days");
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String sumStr = statrTime.substring(0,10)+","+endTime.substring(0,10)+","+days;
      //System.out.println(sumStr);
      orgLogic.add(dbConn, sumStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getAccessControl(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAccesscontrolLogic orgLogic = new YHAccesscontrolLogic();
      YHAccessControl org = null;
      String data = null;
      org = orgLogic.getAccessControl(dbConn);
      if (org == null) {
        org = new YHAccessControl();
      }
      data = YHFOM.toJson(org).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

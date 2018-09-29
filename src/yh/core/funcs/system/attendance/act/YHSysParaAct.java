package yh.core.funcs.system.attendance.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.attendance.logic.YHSysParaLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSysParaAct {
  /**
   * 
   * 更新或者添加时间参数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String update_addInteval(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String dutyIntervalBefore1 = request.getParameter("dutyIntervalBefore1");
      String dutyIntervalAfter1  = request.getParameter("dutyIntervalAfter1");
      String dutyIntervalBefore2 = request.getParameter("dutyIntervalBefore2");
      String dutyIntervalAfter2  = request.getParameter("dutyIntervalAfter2");
      YHSysParaLogic yhpl = new YHSysParaLogic();
      yhpl.update_addPara(dbConn, "DUTY_INTERVAL_BEFORE1", dutyIntervalBefore1);
      yhpl.update_addPara(dbConn, "DUTY_INTERVAL_AFTER1", dutyIntervalAfter1);
      yhpl.update_addPara(dbConn, "DUTY_INTERVAL_BEFORE2", dutyIntervalBefore2);
      yhpl.update_addPara(dbConn, "DUTY_INTERVAL_AFTER2", dutyIntervalAfter2);
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/attendance/index.jsp";
  }
  /**
   * 
   * 查询时间参数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectParaInteval(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysParaLogic yhpl = new YHSysParaLogic();
      String dutyIntervalBefore1 = yhpl.selectPara(dbConn, "DUTY_INTERVAL_BEFORE1");
      String dutyIntervalAfter1 = yhpl.selectPara(dbConn, "DUTY_INTERVAL_AFTER1");
      String dutyIntervalBefore2 = yhpl.selectPara(dbConn, "DUTY_INTERVAL_BEFORE2");
      String dutyIntervalAfter2 = yhpl.selectPara(dbConn, "DUTY_INTERVAL_AFTER2");
      if(dutyIntervalBefore1==null){
        dutyIntervalBefore1 = "";
      }
      if(dutyIntervalAfter1==null){
        dutyIntervalAfter1 = "";
      }
      if(dutyIntervalBefore2==null){
        dutyIntervalBefore2 = "";
      }
      if(dutyIntervalAfter2==null){
        dutyIntervalAfter2 = "";
      }
      String data = "{";
      data = data + "dutyIntervalBefore1:\"" + dutyIntervalBefore1 +"\"," +"dutyIntervalAfter1:\"" + dutyIntervalAfter1 +"\","
      + "dutyIntervalBefore2:\"" + dutyIntervalBefore2 +"\"," + "dutyIntervalAfter2:\"" + dutyIntervalAfter2 +"\"}";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 查询免签人员参数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectParaNoDutyUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysParaLogic yhpl = new YHSysParaLogic();
      String noDutyUserId = yhpl.selectPara(dbConn, "NO_DUTY_USER");
      String noDutyUserName = yhpl.getNamesByIds(dbConn, "NO_DUTY_USER");
      String data = "{user:\"" + noDutyUserId + "\",userDesc:\"" + noDutyUserName +  "\"}";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 更新或者添加免签人员参数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String update_addNoDutyUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String noDutyUserId = request.getParameter("user");
      //System.out.println(noDutyUserId);
      YHSysParaLogic yhpl = new YHSysParaLogic();
      yhpl.update_addPara(dbConn, "NO_DUTY_USER", noDutyUserId);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/attendance/index.jsp";
  }
}

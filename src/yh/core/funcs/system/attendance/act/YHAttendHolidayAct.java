package yh.core.funcs.system.attendance.act;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.funcs.system.attendance.data.YHAttendHoliday;
import yh.core.funcs.system.attendance.logic.YHAttendConfigLogic;
import yh.core.funcs.system.attendance.logic.YHAttendHolidayLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHAttendHolidayAct {
  /**
   * 
   * 添加节假日
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addHoliday(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendHoliday holiday = new YHAttendHoliday();
      YHAttendHolidayLogic yhahl = new YHAttendHolidayLogic();
      YHFOM fom = new YHFOM();
      holiday =  (YHAttendHoliday)fom.build(request.getParameterMap());
      yhahl.addHoliday(dbConn, holiday);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/attendance/holiday.jsp";
  }
  /**
   * 查询所有holiday 返回Data
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectHoliday(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendHoliday holiday = new YHAttendHoliday();
      YHAttendHolidayLogic yhahl = new YHAttendHolidayLogic();
      String data = "[";
      Map map = new HashMap();
      String[] str = {YHDBUtility.getDateFilter("BEGIN_DATE", "1990-01-01", ">=")+"order by BEGIN_DATE desc"};
      List<YHAttendHoliday> holidayList = yhahl.selectHoliday(dbConn, str);
      for (int i = 0; i < holidayList.size(); i++) {
        data = data+(YHFOM.toJson(holidayList.get(i))).toString() + ",";
      }
      if(holidayList.size() > 0 ){
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      
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
   * 查询所有holiday 返回List
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public List<YHAttendHoliday> selectHolidayList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendHoliday holiday = new YHAttendHoliday();
      YHAttendHolidayLogic yhahl = new YHAttendHolidayLogic();
      Map map = new HashMap();
      String[] str = {YHDBUtility.getDateFilter("BEGIN_DATE", "1990-01-01", ">=")+"order by BEGIN_DATE desc"};
      List<YHAttendHoliday> holidayList = yhahl.selectHoliday(dbConn, str);
      return holidayList;
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  /**
   * 查询holiday ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectHolidayById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHAttendHoliday holiday = new YHAttendHoliday();
      YHAttendHolidayLogic yhahl = new YHAttendHolidayLogic();
      String data = "";
      //System.out.println(seqId);
      if(!seqId.equals("")&&!seqId.equals("null")){
        holiday = yhahl.selectHolidayById(dbConn, seqId);
        data = data+(YHFOM.toJson(holiday)).toString();
      }else{
        data = data + "{}";
      }
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
   * 更新节假日 ById
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateHolidayById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendHoliday holiday = new YHAttendHoliday();
      YHAttendHolidayLogic yhahl = new YHAttendHolidayLogic();
      YHFOM fom = new YHFOM();
      holiday =  (YHAttendHoliday)fom.build(request.getParameterMap());
      yhahl.updateHoliday(dbConn, holiday);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/attendance/holiday.jsp";
  }
  /**
  * 删除节假日 ById
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String deleteHolidayById(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     String seqId = request.getParameter("seqId");
     YHAttendHoliday holiday = new YHAttendHoliday();
     YHAttendHolidayLogic yhahl = new YHAttendHolidayLogic();
     yhahl.deleteHoliday(dbConn, seqId);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
     //request.setAttribute(YHActionKeys.RET_DATA, "data");
   }catch(Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/funcs/system/attendance/holiday.jsp";
 }
 /**
  * 删除所有节假日 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String deleteAllHoliday(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
     YHAttendHoliday holiday = new YHAttendHoliday();
     YHAttendHolidayLogic yhahl = new YHAttendHolidayLogic();
     yhahl.deleteAllHoliday(dbConn);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "全部删除成功！");
     //request.setAttribute(YHActionKeys.RET_DATA, "data");
   }catch(Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/funcs/system/attendance/holiday.jsp";
 }
}

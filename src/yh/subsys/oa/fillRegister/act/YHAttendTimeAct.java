package yh.subsys.oa.fillRegister.act;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.fillRegister.data.YHAttendTime;
import yh.subsys.oa.fillRegister.logic.YHAttendTimeLogic;

public class YHAttendTimeAct {
  private YHAttendTimeLogic logic = new YHAttendTimeLogic();

  /**
   * 获取迟到时间管理列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAttendTimeList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String dutyIdStr = request.getParameter("dutyId");
      int dutyId = 0;
      if(!YHUtility.isNullorEmpty(dutyIdStr)){
        dutyId = Integer.parseInt(dutyIdStr);
      }
      String data = this.logic.getAttendTimeListLogic(dbConn, dutyId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 增加迟到时间管理项
   * 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addAttendTimeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String, String[]> map = request.getParameterMap();
      YHAttendTime attendTime = (YHAttendTime) YHFOM.build(map,
          YHAttendTime.class, "");
      this.logic.addAttendTimeItemLogic(dbConn, attendTime);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 更新迟到时间管理项
   * 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateAttendTimeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    String minLateTimeStr = request.getParameter("minLateTime");
    String maxLateTimeStr = request.getParameter("maxLateTime");
    String scoreStr = request.getParameter("score");
    String dutyIdStr = request.getParameter("dutyId");

    int seqId = 0;
    if (!YHUtility.isNullorEmpty(seqIdStr)) {
      seqId = Integer.parseInt(seqIdStr);
    }
    double score = 0.0;
    int minLateTime = 0;
    int maxLateTime = 0;
    if (YHUtility.isNumber(scoreStr)) {
      score = Double.parseDouble(scoreStr);
    }
    if (YHUtility.isNumber(minLateTimeStr)) {
      minLateTime = Integer.parseInt(minLateTimeStr);
    }
    if (YHUtility.isNumber(maxLateTimeStr)) {
      maxLateTime = Integer.parseInt(maxLateTimeStr);
    }

    int dutyId = 0;
    if (!YHUtility.isNullorEmpty(dutyIdStr)) {
      dutyId = Integer.parseInt(dutyIdStr);
    }

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String, String[]> map = request.getParameterMap();
      YHAttendTime attendTime = (YHAttendTime) YHFOM.build(map,
          YHAttendTime.class, "");
      attendTime.setSeqId(seqId);
      attendTime.setMinLateTime(minLateTime);
      attendTime.setMaxLateTime(maxLateTime);
      attendTime.setScore(score);
      attendTime.setDutyId(dutyId);
      this.logic.updateAttendTimeItemLogic(dbConn, attendTime);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 更新迟到时间管理项
   * 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delAttendTimeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    int seqId = 0;
    if (!YHUtility.isNullorEmpty(seqIdStr)) {
      seqId = Integer.parseInt(seqIdStr);
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String, String[]> map = request.getParameterMap();
      YHAttendTime attendTime = (YHAttendTime) YHFOM.build(map,
          YHAttendTime.class, "");
      attendTime.setSeqId(seqId);
      this.logic.delAttendTimeItemLogic(dbConn, attendTime);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 根据条件获取迟到时间管理列表(排班类型)
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAttendTimeListById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String dutyIdStr = request.getParameter("dutyId");
    String dutyType = request.getParameter("dutyType");
    String registerType = request.getParameter("registerType");

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = this.logic.getAttendTimeListByIdLogic(dbConn, dutyIdStr,
          dutyType, registerType);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 增加迟到时间管理项(排班类型)
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addAttendTimeItemById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String minLateTimeStr = request.getParameter("minLateTime");
    String maxLateTimeStr = request.getParameter("maxLateTime");
    String scoreStr = request.getParameter("score");

    String dutyIdStr = request.getParameter("dutyId");
    String dutyType = request.getParameter("dutyType");
    String registerType = request.getParameter("registerType");

    int minLateTime = 0;
    int maxLateTime = 0;
    double score = 0;
    if (!YHUtility.isNullorEmpty(minLateTimeStr)) {
      minLateTime = Integer.parseInt(minLateTimeStr);
    }
    if (!YHUtility.isNullorEmpty(maxLateTimeStr)) {
      maxLateTime = Integer.parseInt(maxLateTimeStr);
    }
    if (!YHUtility.isNullorEmpty(scoreStr)) {
      score = Double.parseDouble(scoreStr);
    }
    int dutyId = 0;
    if (!YHUtility.isNullorEmpty(dutyIdStr)) {
      dutyId = Integer.parseInt(dutyIdStr);
    }
    if (YHUtility.isNullorEmpty(dutyType)) {
      dutyType = "";
    }
    if (YHUtility.isNullorEmpty(registerType)) {
      registerType = "";
    }

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String, String[]> map = request.getParameterMap();
      YHAttendTime attendTime = new YHAttendTime();
      attendTime.setMinLateTime(minLateTime);
      attendTime.setMaxLateTime(maxLateTime);
      attendTime.setDutyId(dutyId);
      attendTime.setDutyType(dutyType);
      attendTime.setRegisterType(registerType);

      attendTime.setScore(score);
      this.logic.addAttendTimeItemLogic(dbConn, attendTime);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

}

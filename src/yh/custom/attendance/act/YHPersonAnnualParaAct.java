package yh.custom.attendance.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.custom.attendance.data.YHPersonAnnualPara;
import yh.custom.attendance.logic.YHAnnualLeaveLogic;
import yh.custom.attendance.logic.YHPersonAnnualParaLogic;

public class YHPersonAnnualParaAct {
  /**
   * 
   * 查询用户年假ByUserId
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectAnnualLeavePara(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      YHPersonAnnualParaLogic logic = new YHPersonAnnualParaLogic();
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      if(YHUtility.isNullorEmpty(userId)){
        userId = "";
      }
      String[] str = {"USER_ID = '" + userId + "'"};
      List< YHPersonAnnualPara> leaveList = logic.selectAnnualLeavePara(dbConn, str);
      String data = "";
      String type = "0";
      YHPersonAnnualPara annualPara = null;
      if(leaveList.size()>0){
        annualPara = leaveList.get(0);
        data = data + YHFOM.toJson(annualPara).toString();
        type = "1";
      }else{
        data = "{}";
      }
      YHAnnualLeaveLogic personanLogic = new YHAnnualLeaveLogic();
      String annualSumDays =  personanLogic.selectPersonAnnualDays(dbConn, userId, year + "");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,annualSumDays);
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
   * 更新或者新建年休假设置
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUpdateAnnualLeavePara(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String annualDays = request.getParameter("annualDays");
      String changeDate = request.getParameter("changeDate");
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      YHPersonAnnualParaLogic logic = new YHPersonAnnualParaLogic();

      if(!YHUtility.isNullorEmpty(userId)){
        String[] str = {"USER_ID = '" + userId + "'"};
        List< YHPersonAnnualPara> leaveList = logic.selectAnnualLeavePara(dbConn, str);
        YHPersonAnnualPara annualPara =  new YHPersonAnnualPara();
        if(leaveList.size()>0){
          annualPara = leaveList.get(0);
          if(!YHUtility.isNullorEmpty(changeDate)){
            annualPara.setChangeDate(format.parse(changeDate));
          }
          if(!YHUtility.isNullorEmpty(annualDays)&&YHUtility.isInteger(annualDays)){
            annualPara.setAnnualDays(Integer.parseInt(annualDays));
          }
          logic.updateAnnualLeavePara(dbConn, annualPara);
        }else{
          if(!YHUtility.isNullorEmpty(changeDate)){
            annualPara.setChangeDate(format.parse(changeDate));
          }
          if(!YHUtility.isNullorEmpty(annualDays)&&YHUtility.isInteger(annualDays)){
            annualPara.setAnnualDays(Integer.parseInt(annualDays));
          }
          annualPara.setUserId(userId);
          logic.addAnnualLeavePara(dbConn, annualPara);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

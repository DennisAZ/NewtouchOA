package yh.custom.attendance.act;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.custom.attendance.logic.YHPersonAnnualBeachLogic;

/**
 * 年休假批量设置
 * @author Administrator
 *
 */
public class YHPersonAnnualBeanchAct{
  private YHPersonAnnualBeachLogic logic = new YHPersonAnnualBeachLogic();
  /**
   * 年休假批量设置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String insertBeanch(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIds = request.getParameter("userId");
      String annualDays = request.getParameter("annualDays");
      String changeDate = request.getParameter("changeDate");
      Date d = null;
      if(!YHUtility.isNullorEmpty(changeDate)){
        d = YHUtility.parseDate(changeDate);
      }
      int id = 0;
      if(!YHUtility.isNullorEmpty(annualDays)){
        id = Integer.parseInt(annualDays);
      }
      logic.insertBeanch(dbConn, userIds, id, d);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

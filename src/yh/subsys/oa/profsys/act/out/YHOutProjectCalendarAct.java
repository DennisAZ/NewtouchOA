package yh.subsys.oa.profsys.act.out;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.oa.profsys.data.YHProjectCalendar;
import yh.subsys.oa.profsys.logic.out.YHOutProjectCalendarLogic;

public class YHOutProjectCalendarAct {
  /**
   * 项目日程查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String profsysSelectCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      YHProjectCalendar calendar = new YHProjectCalendar();
      String activeType = request.getParameter("activeType");
      String activeContent = request.getParameter("activeContent");
      String activeLeader = request.getParameter("activeLeader");
      String activePartner = request.getParameter("activePartner");
      String startTime = request.getParameter("startTime");
      String startTime1 = request.getParameter("startTime1");
      String endTime = request.getParameter("endTime");
      String endTime1 = request.getParameter("endTime1");
      String projCalendarType = request.getParameter("projCalendarType");
      calendar.setActiveType(activeType);
      calendar.setActiveContent(activeContent);
      calendar.setActiveLeader(activeLeader);
      calendar.setActivePartner(activePartner);
      calendar.setProjCalendarType(projCalendarType);
     
      //String projId = YHOutProjectCalendarLogic.profsysSelectCalendar(dbConn,calendar, startTime, startTime1,endTime,endTime1);
      //通用查询数据
      String data = YHOutProjectCalendarLogic.profsysCalendarList(dbConn,request.getParameterMap(),projCalendarType,calendar,startTime,startTime1,endTime,endTime1);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}

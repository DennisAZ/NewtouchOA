package yh.subsys.oa.assessmentScore.act;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.assessmentScore.logic.YHAssessmentScoreLogic;
import yh.subsys.oa.coefficient.logic.YHCoefficientLogic;

public class YHAssessmentScoreAct {
  public static final String attachmentFolder = "assessmentScore";
  private YHAssessmentScoreLogic logic = new YHAssessmentScoreLogic();
  private YHCoefficientLogic cof = new YHCoefficientLogic();
  
  public String getMonthScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = request.getParameter("userId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      StringBuffer sb = new StringBuffer();
      Date date = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateStr = dateFormat.format(date);
      int yearNow = Integer.parseInt(dateStr.substring(0,4));
      int monthNow = Integer.parseInt(dateStr.substring(5,7));
      if(yearNow >= Integer.parseInt(year)){
        double reduceAttendScore = 0;
        double directorScore = 0;
        double staffScore = 0;
        double monthScore = 0;
        if(monthNow >= Integer.parseInt(month)){
          reduceAttendScore = this.logic.getAttendScore(dbConn, year, month, userId, person);  //获取月考勤分数
          directorScore = this.logic.getDirectorScore(dbConn, year, month, userId);            //处长月考核分数
          staffScore = this.logic.getMonthScoreLogic(dbConn, year, month, userId);             //月奖惩分
          monthScore = this.logic.getMonthTotalScore(dbConn, year, month, reduceAttendScore, staffScore, directorScore);  //月考核分
          sb.append("{");
          sb.append("monthScore:\"" +  monthScore + "\"");
          sb.append(",directorScore:\"" + directorScore+ "\"");
          sb.append(",attendScore:\"" + reduceAttendScore + "\"");
          sb.append(",staffScore:\"" + staffScore + "\"");
          sb.append("},");
        }else{
          sb.append("{");
          sb.append("monthScore:\"" +  0.0 + "\"");
          sb.append(",directorScore:\"" + 0.0 + "\"");
          sb.append(",attendScore:\"" + 0.0 + "\"");
          sb.append(",staffScore:\"" + 0.0 + "\"");
          sb.append("},");
        }
      }else{
          sb.append("{");
          sb.append("monthScore:\"" + 0.0 + "\"");
          sb.append(",directorScore:\"" + 0.0 + "\"");
          sb.append(",attendScore:\"" + 0.0 + "\"");
          sb.append(",staffScore:\"" + 0.0 + "\"");
          sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getAttendScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = request.getParameter("userId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      StringBuffer sb = new StringBuffer();
      Date date = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateStr = dateFormat.format(date);
      int yearNow = Integer.parseInt(dateStr.substring(0,4));
      int monthNow = Integer.parseInt(dateStr.substring(5,7));
      if(yearNow >= Integer.parseInt(year)){
        double reduceAttendScore = 0;
        double directorScore = 0;
        double staffScore = 0;
        double monthScore = 0;
        if(monthNow >= Integer.parseInt(month)){
          reduceAttendScore = this.logic.getAttendScore(dbConn, year, month, userId, person);  //获取月考勤分数
          sb.append("{");
          sb.append("monthScore:\"" +  monthScore + "\"");
          sb.append(",directorScore:\"" + directorScore+ "\"");
          sb.append(",attendScore:\"" + reduceAttendScore + "\"");
          sb.append(",staffScore:\"" + staffScore + "\"");
          sb.append("},");
        }else{
          sb.append("{");
          sb.append("monthScore:\"" +  0.0 + "\"");
          sb.append(",directorScore:\"" + 0.0 + "\"");
          sb.append(",attendScore:\"" + 0.0 + "\"");
          sb.append(",staffScore:\"" + 0.0 + "\"");
          sb.append("},");
        }
      }else{
          sb.append("{");
          sb.append("monthScore:\"" + 0.0 + "\"");
          sb.append(",directorScore:\"" + 0.0 + "\"");
          sb.append(",attendScore:\"" + 0.0 + "\"");
          sb.append(",staffScore:\"" + 0.0 + "\"");
          sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  public String getYearScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = request.getParameter("userId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      StringBuffer sb = new StringBuffer();
      Date date = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateStr = dateFormat.format(date);
      int yearNow = Integer.parseInt(dateStr.substring(0,4));
      if(yearNow >= Integer.parseInt(year)){
        double chiefCof = cof.getChiefScoreLogic(dbConn);  //处长主关分系数        double monthCof = cof.getMonthScoreLogic(dbConn);
        double yearCof = cof.getYearScoreLogic(dbConn);
        double awardCof = cof.getAwardScoreLogic(dbConn);
        double directorScore = this.logic.getDirectorYearScore(dbConn, year, month, userId);            //年终处长考核分数
        double monthTotalScore = this.logic.getYearTotalScore(dbConn, year, userId, person);            //12个月的月考核总分
        double staffScore = this.logic.getStaffYearScoreLogic(dbConn, year, userId);                    //年奖惩分
        double noCheckMonth = this.logic.getNoCheckInfo(dbConn, year);                                  //参加考核的月份        BigDecimal avg = YHUtility.divide(monthTotalScore, noCheckMonth, 2);
        double yearScore = YHUtility.parseDouble(avg)*monthCof + directorScore*yearCof + staffScore;    //年终考核总分
        sb.append("{");
        sb.append("yearScore:\"" + yearScore + "\"");
        sb.append(",monthScoreAvg:\"" + monthTotalScore/noCheckMonth + "\"");
        sb.append(",directorScore:\"" + directorScore + "\"");
        sb.append(",staffScore:\"" + staffScore + "\"");
        sb.append("},");
      }else{
        sb.append("{");
        sb.append("yearScore:\"" + 0.0 + "\"");
        sb.append(",monthScoreAvg:\"" + 0.0 + "\"");
        sb.append(",directorScore:\"" + 0.0 + "\"");
        sb.append(",staffScore:\"" + 0.0 + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}

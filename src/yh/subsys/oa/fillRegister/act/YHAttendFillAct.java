package yh.subsys.oa.fillRegister.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.fillRegister.data.YHAttendFill;
import yh.subsys.oa.fillRegister.logic.YHAttendFillLogic;


public class YHAttendFillAct {
  public static final String attachmentFolder = "attendFill";
  private YHAttendFillLogic logic = new YHAttendFillLogic();
  
  public String add(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      YHAttendFill fill = (YHAttendFill) YHFOM.build(map, YHAttendFill.class, "");
      String registerStr = request.getParameter("idStrs");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
//      if (scoreFlow.getBeginDate() == null) {
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//        scoreFlow.setBeginDate(YHUtility.parseSqlDate(sf.format(new Date())));
//      }
      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
      long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginDate),dateFormat1.parse(endDate))+1;
      //得到之间的天数数组
      List daysList = new ArrayList();
      String days = "";
      Calendar calendar = new GregorianCalendar();
      String[] register = registerStr.split(",");
      for(int i = 0;i < daySpace; i++){
        if(this.logic.isWeekend(beginDate)){
          continue;
        }
        calendar.setTime(dateFormat1.parse(beginDate));
        calendar.add(Calendar.DATE,+i) ;
        Date dateTemp = calendar.getTime();
        String dateTempStr = dateFormat1.format(dateTemp);
        for(int x = 0; x < register.length; x++){
          fill.setFillTime(YHUtility.parseSqlDate(dateTempStr));
          fill.setRegisterType(register[x]);
          fill.setAssessingStatus("0");
          fill.setAttendFlag("0");
          SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
          fill.setAssessingTime(YHUtility.parseSqlDate(sf.format(new Date())));
          this.logic.addAttendFill(dbConn, fill);//添加数据
        }
      }
      String smsSJ = request.getParameter("smsSJ");//手机短信
      String smsflag = request.getParameter("smsflag");//内部短信
      if (smsflag.equals("1")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("62");
        sb.setContent("请查看补登记审批！");
        sb.setSendDate(new java.util.Date());
        sb.setFromId(person.getSeqId());
        sb.setToId(fill.getAssessingOfficer());
        sb.setRemindUrl("/subsys/oa/fillRegister/approval/index.jsp?openFlag=1&openWidth=820&openHeight=600");
        YHSmsUtil.smsBack(dbConn,sb);
      }
      //手机消息提醒
      if (smsSJ.equals("1")) {
        YHMobileSms2Logic sb2 = new YHMobileSms2Logic();
        sb2.remindByMobileSms(dbConn,fill.getAssessingOfficer(),person.getSeqId(),"请查看考核任务！" ,new java.util.Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addVolume(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      YHAttendFill fill = (YHAttendFill) YHFOM.build(map, YHAttendFill.class, "");
      String registerStr = request.getParameter("idStrs");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String proposerStr = request.getParameter("proposer");
      String[] proposer = proposerStr.split(",");
      String[] register = registerStr.split(",");
      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
      long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginDate),dateFormat1.parse(endDate))+1;
      //得到之间的天数数组
      List daysList = new ArrayList();
      String days = "";
      Calendar calendar = new GregorianCalendar();
      for(int i = 0; i < daySpace; i++){
        calendar.setTime(dateFormat1.parse(beginDate));
        calendar.add(Calendar.DATE,+i) ;
        Date dateTemp = calendar.getTime();
        String dateTempStr = dateFormat1.format(dateTemp);
        for(int x = 0; x < register.length; x++){
          for(int y = 0; y < proposer.length; y++){
            fill.setProposer(proposer[y]);
            fill.setFillTime(YHUtility.parseSqlDate(dateTempStr));
            fill.setRegisterType(register[x]);
            fill.setAssessingStatus("1");
            fill.setAttendFlag("0");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            fill.setAssessingTime(YHUtility.parseSqlDate(sf.format(new Date())));
            this.logic.addAttendFill(dbConn, fill);//添加数据
          }
        }
      }
//      String smsSJ = request.getParameter("smsSJ");//手机短信
//      String smsflag = request.getParameter("smsflag");//内部短信
//      if (smsflag.equals("1")) {
//        YHSmsBack sb = new YHSmsBack();
//        sb.setSmsType("15");
//        sb.setContent("请查看补登记审批！");
//        sb.setSendDate(new java.util.Date());
//        sb.setFromId(person.getSeqId());
//        sb.setToId(fill.getAssessingOfficer());
//        sb.setRemindUrl("/subsys/oa/hr/score/flow/index1.jsp&openFlag=1&openWidth=820&openHeight=600");
//        //YHSmsUtil.smsBack(dbConn,sb);
//      }
//      //手机消息提醒
//      if (smsSJ.equals("1")) {
//        YHMobileSms2Logic sb2 = new YHMobileSms2Logic();
//        //sb2.remindByMobileSms(dbConn,fill.getAssessingOfficer(),person.getSeqId(),"请查看考核任务！" ,new java.util.Date());
//      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 出国培训补登记
   * @param request
   * @param response
   * @param configId
   * @param dutyType
   * @param registerType
   * @return
   * @throws Exception
   */
  public String addAttendScore(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
     
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String startDateStr = request.getParameter("beginDate");
      String endDateStr = request.getParameter("endDate");
      String proposer = request.getParameter("proposer");
      this.logic.addAttendScore(dbConn, person, startDateStr, endDateStr, proposer);
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

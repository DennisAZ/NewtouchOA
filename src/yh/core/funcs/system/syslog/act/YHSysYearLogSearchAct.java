package yh.core.funcs.system.syslog.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysYearLogSearchLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSysYearLogSearchAct {
  private static Logger log = Logger.getLogger(YHSysYearLogSearchAct.class);
  
  public String getMySysLog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson personLogin = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysYearLogSearchLogic syslog =new YHSysYearLogSearchLogic();
        String years = request.getParameter("year");
        String month = request.getParameter("month");
        List<String> list = syslog.getMySysYearLog(dbConn, 233);
        StringBuffer sb = new StringBuffer("[");
        for(String s : list){
        sb.append("{year:");
        sb.append(s);
        sb.append("},");
        }
        if(list.size() > 0)
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出日志");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 本nian访问量  以及每月访问量
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMySysYearLog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson personLogin = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysYearLogSearchLogic syslog =new YHSysYearLogSearchLogic();
        String years = request.getParameter("year");
        String month = request.getParameter("month");
        List<String>  list1 = syslog.getMyymSys(dbConn, 233,years,month);
        int sum1 = 0;
        StringBuffer sb1 = new StringBuffer("{months:[");
        for(String s : list1){
            sb1.append("{month:");
            sb1.append(s);      // 通过month. 求出每个的量
            sb1.append("},");
            sum1 += Integer.parseInt(s);
           //System.out.println(sum1+"::::sum"); // 一年十二个月的总量
        }
        sb1.deleteCharAt(sb1.length() - 1);
        sb1.append("],sum:"); // 在页面中通过sum.sum1 获取他的总量
        sb1.append(String.valueOf(sum1));
        sb1.append("}");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出日志");
        request.setAttribute(YHActionKeys.RET_DATA, sb1.toString());
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 本月访问量  以及每天访问量
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMySysyueLog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson personLogin = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysYearLogSearchLogic syslog =new YHSysYearLogSearchLogic();
        String years = request.getParameter("year");
        String month = request.getParameter("month");
        // List<String>  list = syslog.getMyydSys(dbConn, personLogin.getSeqId(),years,month);
        List<String>  list = syslog.getMyydSys(dbConn, 233,years,month);
        int sum1 = 0;
        StringBuffer sb1 = new StringBuffer("{days:[");
        for(String s : list){
            sb1.append("{day:");
            sb1.append(s);
            sb1.append("},");
            sum1 += Integer.parseInt(s);
        }
        sb1.deleteCharAt(sb1.length() - 1);
        sb1.append("],sum:");
        sb1.append(String.valueOf(sum1));
        sb1.append("}");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出日志");
        request.setAttribute(YHActionKeys.RET_DATA, sb1.toString());
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 时间段/总时间段
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getMySysHourLog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       YHPerson personLogin = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
       YHSysYearLogSearchLogic syslog =new YHSysYearLogSearchLogic();
       String years = request.getParameter("year");
       String month = request.getParameter("month");
       Map<String,String>  map = syslog.getMyhourSysLog(dbConn, 233,years,month);
       // map 存放的数据是{hour:7,value:19}, {hour:08,value:0},以下s是对map的拼串       List<Map> list = new ArrayList();
       int sum1=0;
       StringBuffer sb = new StringBuffer("[");
       for(int i = 0; i < 24 ; i++){
           String h="";
         if(i<10){
             h = "0";
          }
         if(map.get("h" + h + i) == null){ //如果获取的第i个小时数据等于空   就让值等于0
             String s = "{hour:" + h+ i + ",value:0},";
             sb.append(s);
         }else{  //h+i 是和if == null 对应（如07=null和 7==null map.get(i)  取得是不一样的）                // else 就获取第i个小时中的值            String s = "{hour:"+ i + ",value:" + map.get("h" + h + i) + "},";
            sb.append(s);
          }
      }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出日志");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
 }

package yh.core.funcs.sms.act;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.logic.YHExportLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.logic.YHSmsTestLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSmsTestAct {
  public String notConfirm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHPageDataList data = null;
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsTestLogic smsLogic = new YHSmsTestLogic();
      data = smsLogic.toNewBoxJson(dbConn, request.getParameterMap(), toId,pageNo,pageSize);
      sizeNo = data.getTotalRecord();
      request.setAttribute("contentList", data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/sms/notConSms2.jsp?sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  
  public String acceptedSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHPageDataList data = null;
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    String queryType = request.getParameter("queryType");
    boolean isQuery = false;
    String url = "";
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    if("1".equals(queryType)){
      isQuery = true;
      url =  "/core/funcs/sms/searchForIn.jsp?";
    }else{
      url =  "/core/funcs/sms/accepte.jsp?";
    }
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsTestLogic smsLogic = new YHSmsTestLogic();
      data = smsLogic.toInBoxJson(dbConn, request.getParameterMap(), toId,pageNo,pageSize,isQuery);
      sizeNo = data.getTotalRecord();
      request.setAttribute("contentList", data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return url + "sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  
  public String sentSmsList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHPageDataList data = null;
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    String queryType = request.getParameter("queryType");
    boolean isQuery = false;
    String url = "";
    if("1".equals(queryType)){
      isQuery = true;
      url =  "/core/funcs/sms/searchForOut.jsp?";
    }else{
      url =  "/core/funcs/sms/sentsms1.jsp?";
    }
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsTestLogic smsLogic = new YHSmsTestLogic();
      data = smsLogic.toSendBoxJson(dbConn, request.getParameterMap(), toId,pageNo,pageSize,isQuery);
      sizeNo = data.getTotalRecord();
      request.setAttribute("contentList", data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return url + "sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  
  public String exportExcel(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    OutputStream ops = null;
    ArrayList<YHDbRecord > dbL = null;
    try {
      String userType = request.getParameter("userType");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String fileName = URLEncoder.encode("内部短消息.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
     

      YHSmsTestLogic stl = new YHSmsTestLogic();
      if("1".equals(userType)){
        dbL = stl.toInBoxExportData(dbConn, request.getParameterMap(), person.getSeqId());
      }else if("2".equals(userType)){
        dbL = stl.toSendBoxExportData(dbConn, request.getParameterMap(),  person.getSeqId());
      }
      //dbL = stl.toInBoxExportData(dbConn, request.getParameterMap(), person.getSeqId());
      //System.out.println(dbL);
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
}

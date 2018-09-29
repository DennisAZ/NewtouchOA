package yh.core.funcs.doc.act;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.logic.YHConfigLogic;
import yh.core.funcs.doc.logic.YHFlowReportLogic;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;

public class YHFlowReportAct {
  YHFlowReportLogic logic=new YHFlowReportLogic();
  
  public String getReportAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String fId = request.getParameter("fId");
      String data="";
      data=this.logic.getReportLogic(dbConn,fId);
      data="{data:["+data+"]}";
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getListItemAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String fId = request.getParameter("fId");
      String data="";
      data=this.logic.getListItemLogic(dbConn,fId);
      data="{data:["+data+"]}";
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String editReportAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String rId = request.getParameter("rId");
      String data="";
      data=this.logic.editReportLogic(dbConn,rId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getFidByRidAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String rId = request.getParameter("rId");
      String data="";
      data=this.logic.getFidByRidLogic(dbConn,rId);
      data="{fId:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getReportPrivByRidAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String rId = request.getParameter("rId");
      String data="";
      data=this.logic.getReportPrivByRidLogic(dbConn,rId);
      data="{data:["+data+"]}";
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getReportPrivByPidAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String pId = request.getParameter("pId");
      String data="";
      data=this.logic.getReportPrivByPidLogic(dbConn,pId);

      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getReportByRidAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);      
      String rId = request.getParameter("rId");
      String data="";
      data=this.logic.getReportByRidLogic(dbConn,rId,person);

      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addReportAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String fId = request.getParameter("fId"); 
      Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.addReportLogic(dbConn,fileForm,person);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);     
   
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowreport/newRemind.jsp?fId=" + fId;
  }
  public String addReportPrivAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      String rid="";
      String rId=request.getParameter("rid");
      String fId = request.getParameter("fId");
      Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      dbConn = requestDbConn.getSysDbConn();
       rid=request.getParameter("rid");
      this.logic.addReportPrivLogic(dbConn,fileForm,person);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);     
   
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }     
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowreport/set_priv/newRemind.jsp";
  }
  public String delReportByIdAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String rId = request.getParameter("rId");    
     this.logic.delReportByIdLogic(dbConn,rId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);

    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String delReportPrivByIdAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String pId = request.getParameter("pId");    
     this.logic.delReportPrivByIdLogic(dbConn,pId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);

    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateReportAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String rId = request.getParameter("rId");
    String fId = request.getParameter("fId");
      Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.updateReportLogic(dbConn,fileForm,person);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);     
   
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowreport/editRemind.jsp?rId="+rId+"&fId="+ fId;
  }
  public String updateReportPrivAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String pId=request.getParameter("pId");
    String rId=request.getParameter("rid");
    String fId = request.getParameter("fId");
      Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.updateReportPrivLogic(dbConn,fileForm,person);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);     
   
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowreport/set_priv/editRemind.jsp?rid="+rId+"&fId="+fId+"&pId=" + pId;
  }
  
  public String getReportListAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      String data=this.logic.getReportListLogic(dbConn,person);
      data="{data:["+data+"]}";
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";
  }
  
  public String getTableListAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();

      String data=this.logic.getTableListLogic(dbConn,person,request.getParameterMap());

    
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";
  }
  public String getChartAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String data=this.logic.getChartLogic(dbConn,request.getParameterMap());
      data="{data:'"+YHUtility.encodeSpecial(data)+"'}";

      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";
  }
  
  public String toExcel(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    OutputStream ops = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<LinkedList<String>> data=this.logic.toExcel(dbConn,person,request.getParameterMap());
    
      String fileName = URLEncoder.encode("数据报表.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream(); 
      ArrayList<YHDbRecord > dbL = this.logic.convertList(data);
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      ops.close();
    }
    return null;
  }
  
  
}

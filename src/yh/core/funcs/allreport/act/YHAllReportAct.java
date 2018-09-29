package yh.core.funcs.allreport.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.allreport.logic.YHAllReportLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileUploadForm;

public class YHAllReportAct {

  YHAllReportLogic logic =new YHAllReportLogic();
  /**
   * 主要是流程管理时，右边的滑动菜单
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMenuList(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
  
    try{
      String actionUrl = request.getContextPath() + "/yh/core/funcs/allreport/act/YHAllReportAct/getReportsById.act?sortId=";
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = "";
      data=this.logic.getMenuList(dbConn,actionUrl);
      data="["+data+"]";
      
      
     // System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data);      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 根据id获取报表
   * 
   * 
   * */

  public String getReportsById(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String sortId = request.getParameter("sortId");
   
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = "";
     
      data=this.logic.getReportsById(dbConn,sortId);
      data="["+data+"]";
      
      
     // System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data);      
    }catch(Exception ex) {
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
      String rId = request.getParameter("rId");
      String data="";
      data=this.logic.getSelectOptionLogic(dbConn,rId);
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
  
  public String getCalListItemAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String rId = request.getParameter("rId");
      String data="";
      data=this.logic.getSelectCalOptionLogic(dbConn,rId);
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
  public String addReportAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
   
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
    return "/core/funcs/allreport/newRemind.jsp";
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
  
  public String updateReportAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
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
    return "/core/funcs/allreport/editRemind.jsp";
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
  
 
}

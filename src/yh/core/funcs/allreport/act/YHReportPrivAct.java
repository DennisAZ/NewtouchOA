package yh.core.funcs.allreport.act;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.allreport.logic.YHReportPrivLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileUploadForm;

public class YHReportPrivAct {

	YHReportPrivLogic logic = new YHReportPrivLogic();
	
	
	/**
	  * 报表权限设置
	  */
	 public String getReportPrivByRidAct(HttpServletRequest request,
		      HttpServletResponse response) throws Exception {
		    Connection dbConn = null;
		    try{
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      String rId = request.getParameter("rId");
		      String data="";
		      data=logic.getReportPrivByRidLogic(dbConn,rId);
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
	 /**
	  * 删除权限
	  */
	 public String delReportPrivByIdAct(HttpServletRequest request,
		      HttpServletResponse response) throws Exception {
		    Connection dbConn = null;
		    try{
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      String seqId = request.getParameter("seqId");    
		      logic.delReportPrivByIdLogic(dbConn,seqId);
		      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);

		    } catch (Exception ex){
		       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		    return "/core/inc/rtjson.jsp";
		  }
	 /**
	  * 添加权限
	  */
	 public String addReportPrivAct(HttpServletRequest request,
		      HttpServletResponse response) throws Exception {
		      String rid="";
		      String rId=request.getParameter("rid");
		      Connection dbConn = null;
		    try{
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      YHFileUploadForm fileForm = new YHFileUploadForm();
		      fileForm.parseUploadRequest(request);
		      dbConn = requestDbConn.getSysDbConn();
		       //rid=request.getParameter("rid");
		      logic.addReportPrivLogic(dbConn,fileForm,person);
		      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);     
		   
		    } catch (Exception ex){
		       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }     
		    return "/core/funcs/allreport/set_priv/newRemind.jsp";
		  }
	 /**
	  * 编辑权限
	  */
	 public String updateReportPrivAct(HttpServletRequest request,
		      HttpServletResponse response) throws Exception {
	       
		      Connection dbConn = null;
		    try{
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      dbConn = requestDbConn.getSysDbConn();
		      String seqId = request.getParameter("seqId");
          String userstr = request.getParameter("userId");
          //String deptstr = request.getParameter("deptvalue");
    
		      logic.updateReportPrivLogic(dbConn,seqId,userstr,person);
		      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);     
		   
		    } catch (Exception ex){
		       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		    return "/core/funcs/allreport/set_priv/editRemind.jsp";
		  }
	 
	 public String getReportPrivByPidAct(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try{
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      String seqId = request.getParameter("seqId");
	      String data="";
	      data=logic.getReportPrivByPidLogic(dbConn,seqId);
        //System.out.println(seqId);
	      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
	      
	      request.setAttribute(YHActionKeys.RET_DATA,data);
	    } catch (Exception ex){
	       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	      throw ex;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	 
}

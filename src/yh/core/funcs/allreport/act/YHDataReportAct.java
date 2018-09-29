package yh.core.funcs.allreport.act;

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
import yh.core.funcs.allreport.logic.YHDataReportLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHDataReportAct {

	YHDataReportLogic logic=new YHDataReportLogic();
	
	/**
	 * 数据报表初始页面时查出的数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 public String getReportListAct(HttpServletRequest request,
		      HttpServletResponse response) throws Exception{
		    
		    Connection dbConn = null;
		    try{
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      dbConn = requestDbConn.getSysDbConn();
		      
		      String data=logic.getReportListLogic(dbConn,person);
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
	 /**
	   * 导出excel表
	   */
	  public String toExcel(HttpServletRequest request,
	      HttpServletResponse response) throws Exception{
	    Connection dbConn = null;
	    OutputStream ops = null;
	    try{
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
	      List<List<String>> data=logic.getExcelListLogic(dbConn,person,request.getParameterMap());
	      String fileName = URLEncoder.encode("数据报表.xls","UTF-8");
	      fileName = fileName.replaceAll("\\+", "%20");
	      response.setHeader("Cache-control","private");
	      response.setContentType("application/vnd.ms-excel");
	      response.setHeader("Accept-Ranges","bytes");
	      response.setHeader("Cache-Control","maxage=3600");
	      response.setHeader("Pragma","public");
	      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
	      ops = response.getOutputStream(); 
	      ArrayList<YHDbRecord > dbL = logic.convertList(data);
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

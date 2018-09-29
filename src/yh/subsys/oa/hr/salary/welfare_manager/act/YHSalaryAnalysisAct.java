package yh.subsys.oa.hr.salary.welfare_manager.act;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.oa.hr.salary.welfare_manager.logic.YHSalaryAnalysisLogic;


public class YHSalaryAnalysisAct {
  /**
   * 新建福利信息
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doAnalysis(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String salYear = request.getParameter("salYear");
    String salMonth = request.getParameter("salMonth");
    String deptstr = request.getParameter("dept");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSalaryAnalysisLogic logic = new YHSalaryAnalysisLogic();
      String str = logic.querySalary(dbConn, deptstr, salYear, salMonth);
      if (str == null) {
        str = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";                           
  }
  public String doSaleReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String beginTime = request.getParameter("beginTime");
	    String endTime = request.getParameter("endTime");
	    String saleType=request.getParameter("saleType");
	    Connection dbConn = null;
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHSalaryAnalysisLogic logic = new YHSalaryAnalysisLogic();
	      
	      String str="";
	      if("saleperson".equals(saleType)){
	      str= logic.querySale(dbConn, beginTime, endTime);
	      }else if("custorm".equals(saleType)){
	    	  str= logic.queryCust(dbConn, beginTime, endTime);
	    	  
	    	  
	      }
	      if (str == null) {
	        str = "{}";
	      }
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
	      request.setAttribute(YHActionKeys.RET_DATA, str);
	    } catch (Exception ex) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	      throw ex;
	    }
	    return "/core/inc/rtjson.jsp";                           
	  }

public String doPurchaseReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String beginTime = request.getParameter("beginTime");
	String endTime = request.getParameter("endTime");
	String purType=request.getParameter("purType");
	Connection dbConn = null;
	try {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		YHSalaryAnalysisLogic logic = new YHSalaryAnalysisLogic();
		
		String str="";
		if("purperson".equals(purType)){
			str= logic.queryPurchase(dbConn, beginTime, endTime);
		}else if("supplier".equals(purType)){
			str= logic.querySupplier(dbConn, beginTime, endTime);
			
			
		}else if("product".equals(purType)){
			str= logic.queryProduct(dbConn, beginTime, endTime);
			
		}
		if (str == null) {
			str = "{}";
		}
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		request.setAttribute(YHActionKeys.RET_DATA, str);
	} catch (Exception ex) {
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		throw ex;
	}
	return "/core/inc/rtjson.jsp";                           
}
public String doPurCompareReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String beginTime = request.getParameter("beginTime");
	String endTime = request.getParameter("endTime");
	String proId=request.getParameter("proId");
	Connection dbConn = null;
	try {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		YHSalaryAnalysisLogic logic = new YHSalaryAnalysisLogic();
		
		String str="";
			str= logic.queryPro(dbConn, beginTime, endTime,proId);
			
		if (str == null) {
			str = "{}";
		}
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		request.setAttribute(YHActionKeys.RET_DATA, str);
	} catch (Exception ex) {
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		throw ex;
	}
	return "/core/inc/rtjson.jsp";                           
}
}

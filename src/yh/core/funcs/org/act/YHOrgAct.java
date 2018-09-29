package yh.core.funcs.org.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.org.data.YHOrganization;
import yh.core.funcs.org.logic.YHOrgLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHOrgAct {
  private static Logger log = Logger.getLogger(YHOrgAct.class);
  
  YHOrgLogic orgLogic = new YHOrgLogic();
  
  public String getOrganization(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHOrganization org = null;
      String data = null;
            
      org = orgLogic.get(dbConn);
      if (org == null) {
        org = new YHOrganization();
      }
      
      data = YHFOM.toJson(org).toString();
      //System.out.println(data);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateOrganization(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String unitName = request.getParameter("unitName");
      if(unitName == null || "".equals(unitName)) {
        return "/core/inc/rtjson.jsp";
      }
      YHOrganization org = (YHOrganization)YHFOM.build(request.getParameterMap());  
      
    //判断当前用户数量
    Statement stmt = null;
	  ResultSet rs = null;
	    try {
	      String queryUserCountsql = "select unit_name from oa_organization";
	      stmt = dbConn.createStatement();
	      rs = stmt.executeQuery(queryUserCountsql);
	    
	    	  orgLogic.update(dbConn, org);
	     
	    }catch (Exception ex) {
	    	ex.printStackTrace();
	    }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"单位信息已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addOrganization(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String unitName = request.getParameter("unitName");
      if(unitName == null || "".equals(unitName)) {
        return "/core/inc/rtjson.jsp";
      }
      YHOrganization org = (YHOrganization)YHFOM.build(request.getParameterMap());
      orgLogic.add(dbConn, org);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"单位信息已添加");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
}

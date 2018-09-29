package yh.core.funcs.utilapps.info.bus.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.utilapps.info.bus.data.YHBus;
import yh.core.funcs.utilapps.info.bus.logic.YHBusLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHBusAct {
  
  public void searchBus(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String address = (String)request.getParameter("address");
      String start = (String)request.getParameter("start");
      String end = (String)request.getParameter("end");
      String lineId = (String)request.getParameter("lineId");
      
      
      String sql = " select seq_id,address,line_id,start_time,end_time,pass_by,bus_type from oa_public_transport where address='" + address + "' ";
      if(start != null){
        sql += " and pass_by like '%" + start + "%'";
      }
      if(end != null){
        sql += " and pass_by like '%" + end + "%'";
      }
      if(lineId != null){
        sql += " and line_id like '%" + lineId + "%'";
      }
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap());
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      
      PrintWriter out = response.getWriter();
      out.println(pageDataList.toJson());
      out.flush();
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
  }
  
  public String addBus(HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    YHBusLogic logic = new YHBusLogic();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map map = request.getParameterMap();
      YHBus bus = (YHBus)YHFOM.build(map);
      
      logic.addBus(dbConn, bus);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功新增");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String updateBus(HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    YHBusLogic logic = new YHBusLogic();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map map = request.getParameterMap();
      YHBus bus = (YHBus)YHFOM.build(map);
      
      logic.updateBus(dbConn, bus);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功修改");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}

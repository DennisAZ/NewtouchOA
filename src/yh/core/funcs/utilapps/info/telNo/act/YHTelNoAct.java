package yh.core.funcs.utilapps.info.telNo.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.utilapps.info.telNo.logic.YHTelNoLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class YHTelNoAct {

  public String showMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHTelNoLogic logic = new YHTelNoLogic();
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String province = logic.getProvince(dbConn);
      province = province.substring(1, province.length()-1);
      String area = logic.getArea(dbConn);
      area = area.substring(1, area.length()-1);
      String json = "{\"province\":\""+province+"\",\"area\":\""+area+"\"}";
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询");
      request.setAttribute(YHActionKeys.RET_DATA, json);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public void searchProvince(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String province = request.getParameter("province");
      String sql = " select city,county,town,tel_no from oa_post_tel_num pt "
                 + " where pt.province = '" + province.trim() + "' and pt.tel_no is not null "
                 + " order by tel_no ";
      
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
  
  public void searchArea(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String area = request.getParameter("area");
      String sql = " select city,county,town,tel_no from oa_post_tel_num pt "
                 + " where pt.province = '" + area.trim() + "' and pt.tel_no is not null "
                 + " order by tel_no ";
      
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
  
  public void searchAreaInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String area = request.getParameter("area");
      String sql = " select city,county,town,tel_no from oa_post_tel_num pt "
                 + " where pt.province like '%" + area + "%' or pt.city like '%" + area + "%' or pt.county like '%" + area + "%' or pt.town like '%" + area + "%' "
                 + " order by pt.tel_no ";
      
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
  
  public void searchTelNo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String telNo = request.getParameter("telNo");
      String sql = " select city,county,town,tel_no from oa_post_tel_num pt "
                 + " where pt.tel_no like '%" + telNo + "%' "
                 + " order by pt.tel_no ";
      
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
}

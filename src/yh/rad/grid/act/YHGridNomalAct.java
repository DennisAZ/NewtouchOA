package yh.rad.grid.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;
import yh.core.util.db.YHDTJ;
import yh.core.util.db.YHORM;
import yh.core.util.db.YHStringFormat;

public class YHGridNomalAct {
  /**
   * log                                               
   */
  private static Logger log = Logger.getLogger(YHGridNomalAct.class);
  
  public String jsonTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //1.得到tabName,pageNum,pageRows
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    //2.通过tabName,pageNum,pageRows得到json数据
    String tabNo = request.getParameter("tabNo");
    String pageNumStr = request.getParameter("pageNum");
    String pageRowsStr = request.getParameter("pageRows");
    String filedName = request.getParameter("filterName");
    String fieldValue = request.getParameter("filterValue");
    String orderBy = request.getParameter("orderBy");
    int pageNum = 0;
    if(pageNumStr != null)
      try{
        pageNum = Integer.parseInt(pageNumStr);
     }catch(Exception e){
    }
    int pageRows=0;
    if(!"1".equals(request.getParameter("flag"))){
      pageRows = Integer.parseInt(pageRowsStr);
    }
    if(filedName!=null)
    filedName = YHStringFormat.format(filedName,false);
    YHDTJ dtj = new YHDTJ();
    String[] filters;
    String filtersStr = "";
    String orderByStr = "";
    filters = new String[2];
    if(fieldValue != null && !"".equals(fieldValue)){
      filtersStr = "T0." + filedName + " like" + "'%" + fieldValue + "%'";
      filters[0] = filtersStr;
    }
    if(orderBy!= null && !"".equals(orderBy)){
      orderByStr = " 1=1 " + orderBy;
      filters[1] = orderByStr;
    }
   
    try {
      YHORM t = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      String d = "";
      if("1".equals(request.getParameter("flag"))){
        String categoryNo = request.getParameter("categoryNo");
        d  = dtj.toJson(dbConn, request.getParameterMap(), categoryNo);
      }
      else{
        d  = dtj.toJson(dbConn, tabNo, pageNum,pageRows,filters);
      }
      //System.out.println(d.toString());
      //dbConn.close();
      PrintWriter pw = response.getWriter();
      pw.println(d);
      pw.flush();
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  public String doquery(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    //1.得到tabName,pageNum,pageRows
    request.setCharacterEncoding("UTF-8");
   response.setCharacterEncoding("UTF-8");
    //2.通过tabName,pageNum,pageRows得到json数据
    String tabNo = request.getParameter("tabNo");
    String pageNumStr = request.getParameter("pageNum");
    String pageRowsStr = request.getParameter("pageRows");
    String filedName = request.getParameter("filterName");
    String fieldValue = request.getParameter("filterValue");
    String orderBy = request.getParameter("orderBy");
    String[] filterVa = request.getParameterValues("grid_filterVa") ;
    String[] filterSco = request.getParameterValues("grid_filter") ;
    ArrayList<String> filtersList = new ArrayList<String>();
    String vstr = "";
    if(filterSco != null){
    for (String str : filterSco) {
      vstr = "T0." + str;
      //System.out.println("grid_filter  =  " + "T0." + str);
      filtersList.add(vstr);
    }
    }
    if(filterVa != null){
    for (String str : filterVa) {
      String[] strs = str.split(",");
      if(strs.length == 2){
        vstr = "T0." + strs[0] + " like" + "'%" + strs[1] + "%'";
        filtersList.add(vstr);
      }
      //System.out.println("grid_filterVa  =  " + str);
    }
    }
    int pageNum = 0;
    if(pageNumStr != null)
      try{
        pageNum = Integer.parseInt(pageNumStr);
     }catch(Exception e){
     }
    int pageRows = Integer.parseInt(pageRowsStr);
    if(filedName!=null)
    filedName = YHStringFormat.format(filedName,false);
    YHDTJ dtj = new YHDTJ();
    String[] filters = null;
    if(fieldValue == null){
      filters = null;
    }else{
      //fieldValue = new String(fieldValue.getBytes("ISO-8859-1"),"utf-8");
      //System.out.println("fieldValue:   " + fieldValue);
     // filters = new String[]{"T0." + filedName + " like" + "'%" + fieldValue + "%'"};
    }
    if(filtersList.size() > 0){
      filters = new String[filtersList.size()];
      for (int i = 0; i < filtersList.size(); i++) {
        filters[i] = filtersList.get(i);
      }
     //filters = (String[]) filtersList.toArray(filters);
    }else {
      filters = null;
    }

    try {
      YHORM t = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      String d  = dtj.toJson(dbConn, tabNo, pageNum,pageRows,filters);
      //System.out.println(d.toString());
      dbConn.close();
      PrintWriter pw = response.getWriter();
      pw.println(d);
      pw.flush();
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    //System.out.println(fieldValue);
    return null;
  }
  public String jsondDetailTest(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    //1.得到tabName,pageNum,pageRows
   response.setCharacterEncoding("UTF-8");
    //2.通过tabName,pageNum,pageRows得到json数据
    String tabNo = request.getParameter("tabNo");
    String pageNumStr = request.getParameter("pageNum");
    String pageRowsStr = request.getParameter("pageRows");
    String filedName = request.getParameter("filterName");
    String fieldValue = request.getParameter("filterValue");
    String orderBy = request.getParameter("orderBy");
    String seq = request.getParameter("userNo");
    int pageNum = Integer.parseInt(pageNumStr);
    int pageRows = Integer.parseInt(pageRowsStr);
    YHDTJ dtj = new YHDTJ();
    try {
      YHORM t = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      String[] filters = new String[]{"T0.USER_NO = "+seq};
      StringBuffer d  = dtj.toJson(dbConn, tabNo,filters);
      //System.out.println(d.toString());
      dbConn.close();
      PrintWriter pw = response.getWriter();
      pw.println(d.toString());
      pw.flush();
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}

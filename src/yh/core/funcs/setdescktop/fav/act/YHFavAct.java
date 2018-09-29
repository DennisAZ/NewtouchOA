package yh.core.funcs.setdescktop.fav.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.setdescktop.fav.logic.YHFavLogic;
import yh.core.funcs.setdescktop.userinfo.act.YHUserinfoAct;
import yh.core.funcs.system.url.data.YHUrl;
import yh.core.funcs.system.url.logic.YHUrlLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.menu.data.YHSysMenu;

public class YHFavAct {
  private YHFavLogic logic = new YHFavLogic();
  
  /**
   * 新增url
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUrl(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map map = request.getParameterMap();
      YHUrl url = (YHUrl)YHFOM.build(map);
      
      url.setUrl(YHUtility.encodeSpecial(url.getUrl()));
      url.setUrlDesc(YHUtility.encodeSpecial(url.getUrlDesc()));
      
      url.setUser(String.valueOf(user.getSeqId()));
      this.logic.addUrl(dbConn,url);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 修改url
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String modifyUrl(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map map = request.getParameterMap();
      YHUrl url = (YHUrl)YHFOM.build(map);
      url.setUser(String.valueOf(user.getSeqId()));
      this.logic.modifyUrl(dbConn,url);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 修改url
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String list(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      List<Map<String,String>> list = this.logic.list(dbConn, user.getSeqId());
      
      StringBuffer sb = new StringBuffer("[");
      
      for(Map<String,String> m : list){
        sb.append(YHFOM.toJson(m));
        sb.append(",");
      }
        
      if(list.size()>0){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      
      PrintWriter pw = response.getWriter();
      pw.println(sb.toString().trim());
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return null;
  }
  
  /**
   * 删除记录url
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteUrl(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      
      this.logic.deleteUrl(dbConn, Integer.parseInt(seqId));
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  public String getPage(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 

    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String sql = "";
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        sql = "select SEQ_ID" +
        ",URL_NO" +
        ",URL_DESC" +
        ",URL" +
        ",charindex(URL,'1:') as OPEN_TYPE" +
        " from URL" +
        " where [USER] = '" + 
        user.getSeqId() + "'" +
        " order by URL_NO";
      }
      else if (dbms.equals("mysql")){
        sql = "select SEQ_ID" +
        ",URL_NO" +
        ",URL_DESC" +
        ",URL" +
        ",instr(URL,'1:') as OPEN_TYPE" +
        " from URL" +
        " where USER = '" + 
        user.getSeqId() + "'" +
        " order by URL_NO";
      }
      else if (dbms.equals("oracle")){
        sql = "select SEQ_ID" +
        ",URL_NO" +
        ",URL_DESC" +
        ",URL" +
        ",instr(URL,'1:') as OPEN_TYPE" +
        " from URL" +
        " where \"USER\" = '" + 
        user.getSeqId() + "'" +
        " order by URL_NO";
      }
      //System.out.println(sql);
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
      queryParam, 
      sql);
      
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
  
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
}
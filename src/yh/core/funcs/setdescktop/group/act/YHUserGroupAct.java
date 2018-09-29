package yh.core.funcs.setdescktop.group.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.util.form.YHFOM;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.setdescktop.fav.logic.YHFavLogic;
import yh.core.funcs.setdescktop.group.data.YHUserGroup;
import yh.core.funcs.setdescktop.group.logic.YHUserGroupLogic;
import yh.core.funcs.system.url.data.YHUrl;
import yh.core.funcs.system.url.logic.YHUrlLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.menu.data.YHSysMenu;

public class YHUserGroupAct {
  private YHUserGroupLogic logic = new YHUserGroupLogic();
  
  /**
   * 新增userGroup
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String add(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map map = request.getParameterMap();
      YHUserGroup ug = (YHUserGroup)YHFOM.build(map, YHUserGroup.class, "");
      
      ug.setUserId(String.valueOf(user.getSeqId()));
      
      this.logic.add(dbConn, ug);
      
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
   * 修改userGroup
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String update(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map map = request.getParameterMap();
      YHUserGroup ug = (YHUserGroup)YHFOM.build(map, YHUserGroup.class, "");
      ug.setUserId(String.valueOf(user.getSeqId()));
      this.logic.update(dbConn, ug);
      
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
   * 删除userGroup
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delete(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String seqId = request.getParameter("seqId");
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      if (this.logic.delete(dbConn, Integer.parseInt(seqId), user.getSeqId())){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"删除成功");
      }
      else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,"删除失败");
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 设置userGroup
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String userId = request.getParameter("userId");
    String seqId = request.getParameter("seqId");
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      this.logic.setUser(dbConn, Integer.parseInt(seqId), userId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "设置成功");
    }catch(NumberFormatException e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "id有误");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 设置userGroup
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userStr = this.logic.queryUser(dbConn, Integer.parseInt(seqId));
      StringBuffer sb = new StringBuffer();
      if (userStr != null && !"".equals(userStr.trim())){
        sb.append("{\"userStr\":\"");
        sb.append(userStr);
        sb.append("\",\"userDesc\":\"");
        for (String s : userStr.split(",")){
          try{
            sb.append(this.logic.queryUserName(dbConn, Integer.parseInt(s)));
            sb.append(",");
          }catch(NumberFormatException e){
            
          }
        }
        if (sb.charAt(sb.length() - 1) == ','){
          sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("\"}");
      }
      else{
        sb.append("{\"userStr\":\"\",\"userDesc\":\"\"}");
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "设置成功");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(NumberFormatException e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "id有误");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /** 
   * 取得管理用户组并分页
   * @param request 
   * @param response 
   * @return 
   * @throws Exception 
   */ 
   public String getPage(HttpServletRequest request, 
   HttpServletResponse response) throws Exception { 

     Connection dbConn = null; 
     try { 
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn(); 
       
       YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
       
       String sql = "select SEQ_ID" +
       		 ",ORDER_NO" +
       		 ",GROUP_NAME" +
       		 ",USER_STR" +
           " from oa_person_group" +
           " where USER_ID = " +
           user.getSeqId() +
           " order by ORDER_NO";
       YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
       YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
       queryParam, sql); 
     
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
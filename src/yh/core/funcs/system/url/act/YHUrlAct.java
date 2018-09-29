package yh.core.funcs.system.url.act;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.system.data.YHSysFunction;
import yh.core.funcs.system.data.YHSysMenu;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;

public class YHUrlAct {
  private static Logger log = Logger.getLogger(YHUrlAct.class);
  public String doLoginIn(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      //dologin
      String userName = request.getParameter("userName");
      String pwd = request.getParameter("pwd");
      if(userName == null){
        userName  = "";
      }
      if(pwd == null){
        pwd = "";
      }
      Map query = new  HashMap();
      query.put("user_id", userName);
      
      YHORM t = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson person = (YHPerson)t.loadObjSingle(dbConn , YHPerson.class , query);
      if(person != null && person.getPassword() == null){
        person.setPassword("");
      }
      if(person == null 
          || !pwd.equals(person.getPassword())){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "用户名或密码错误!");
      }else{
        request.getSession().setAttribute("LOGIN_USER", person);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }finally{
      if(dbConn != null){
       // dbConn.close();
      }
    }
    return "/core/inc/rtjson.jsp";
  }
  public String doLoginOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      //doLoginOut
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      request.getSession().removeAttribute("LOGIN_USER");
      person = null;
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }finally{
      if(dbConn != null){
       // dbConn.close();
      }
    }
    return "/core/funcs/display/login.jsp";
  }
  public String getMenu(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      //doLoginOut
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      
      Map query = new  HashMap();
      query.put("SEQ_ID", person.getUserPriv());
      
      YHORM t = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      
      YHUserPriv priv = (YHUserPriv)t.loadObjSingle(dbConn , YHUserPriv.class , query);
      
      String funcIdStr = priv.getFuncIdStr();
      String[] menuIds = funcIdStr.split(",");
      StringBuffer sb = new StringBuffer("[");
      
      int i,j; 
      for(j = 0;j < menuIds.length;j++){ 
        for( i = j + 1 ;i < menuIds.length  ; i++){
          if(!menuIds[i].equals("") && !menuIds[j].equals("")){
            int iMenuId = Integer.parseInt(menuIds[i]);
            int iMenuIdNext = Integer.parseInt(menuIds[j]);
            if (iMenuId < iMenuIdNext){
              String tmp  = menuIds[i]; 
              menuIds[i] = menuIds[j]; 
              menuIds[j] = tmp;
            } 
          }
        } 
      }
      
      

      
      for(String menuId : menuIds){
        if(menuId.length() == 2){
          this.getMenuStringBuffer(dbConn, sb, menuId, menuIds, request.getContextPath());
        }
      }
      if(menuIds.length > 0){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA , sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }finally{
      if(dbConn != null){
       // dbConn.close();
      }
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
  var menuData = [{name:'menuName',attachCtrl:true,isHaveChild:true,childMenu:[
         { name:'ddd',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
         , '-'
         , {name:'dd',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
         , {name:'sss',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
         , '-'
         ,{name:'dssss',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
  ]},{name:'dddd',isHaveChild:true,attachCtrl:true,childMenu:[{name:'adad',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                            , '-'
                                            , {name:'dada',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                            , {name:'dafdas',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                            , '-'
                                            , {name:'daf',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                            ]}
  ,{name:'dddddd',isHaveChild:false,address:''}];
  */

  public void getMenuStringBuffer(Connection dbConn, StringBuffer sb, String menuId ,String[] menuIds , String contextPath) throws Exception{
    Map query = new  HashMap();
    query.put("MENU_ID", menuId);
    YHORM t = new YHORM();
    YHSysMenu menu = (YHSysMenu)t.loadObjSingle(dbConn , YHSysMenu.class , query);
    if(menu != null){
      sb.append("{name:'" + menu.getMenuName() + "',attachCtrl:true,isHaveChild:true,childMenu:[");
      
      String[] funcArray = menuIds;
      boolean isFirst = true;
      //查找二级
      for(int i = 0 ; i < funcArray.length ; i++){
        String funcTmp = funcArray[i];
        boolean isChild = funcTmp.startsWith(menuId);
        int funCount = 0 ;
        
        if(isChild && funcTmp.length() == 4 && !funcTmp.equals(menuId)){
          int count = 0 ;
          //查找三级
          Map funcQuery = new HashMap();
          funCount++;
          if(!isFirst){
            //加分割符
            sb.append(",'-',");
          }
          isFirst = false;
          funcQuery.put("MENU_ID", funcTmp);
          YHSysFunction functionTmp = (YHSysFunction)t.loadObjSingle(dbConn , YHSysFunction.class , funcQuery);
          
          for(int j = 0 ; j < funcArray.length ; j++){
            String funcTmp2 = funcArray[j];
            isChild = funcTmp2.startsWith(funcTmp);
            if(isChild && funcTmp2.length() == 6 && !funcTmp2.equals(funcTmp)){
              funcQuery.put("MENU_ID", funcTmp2);
              YHSysFunction function = (YHSysFunction)t.loadObjSingle(dbConn , YHSysFunction.class , funcQuery);
              String funcAddress = null;
              String imageAddress = null;
              imageAddress = contextPath + "/core/funcs/display/img/org.gif";
              if (function.getFuncCode().startsWith("/")) {
                funcAddress = contextPath + function.getFuncCode();
              }else {
                funcAddress = contextPath + "/core/funcs/" + function.getFuncCode() + "/";
              }
              
              sb.append("{name:'" + function.getFuncName() + "'" +
                  ",action:test" +
                  ",icon:'"+  imageAddress +"'" +
                  ",label:'" + functionTmp.getFuncName() +"',extData:'" + funcAddress +"'},");
              count++;
            }
          }
          if(count == 0){
            String funcAddress = null;
            String imageAddress = null;
            if (functionTmp.getFuncCode().startsWith("/")) {
              funcAddress = contextPath + functionTmp.getFuncCode() ;
              imageAddress = contextPath + "/core/funcs/display/img/org.gif";
            }else {
              funcAddress = contextPath + "/core/funcs/" + functionTmp.getFuncCode() + "/";
              imageAddress = contextPath + "/core/funcs/display/img/" + functionTmp.getFuncCode() + ".gif";
            }
            sb.append("{name:'" + functionTmp.getFuncName() + "'" +
                ",action:test" +
                ",icon:'"+  imageAddress +"'" +
                ",extData:'" + funcAddress +"'},");
            
          }
        }
        if(funCount > 0){
          sb.deleteCharAt(sb.length() - 1);
        }
      }
      sb.append("]},");
    }
  }
  
  /**
   * 添加公共网址
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addPublicUrl(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //int seqId = person.getSeqId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String urlType = request.getParameter("urlType");
      String urlNo = request.getParameter("urlNo");
      String urlDesc = request.getParameter("urlDesc");
      String url = request.getParameter("url");
      String subType = request.getParameter("subType");
      //System.out.println(urlType+"SSS");
      if(urlType.equals("")){
        subType = "";
      }else {
        subType = "1";
      }
     
      Map m =new HashMap();
      m.put("urlNo", urlNo);
      m.put("urlType", urlType);
      m.put("urlDesc", urlDesc);
      m.put("url", url);
      m.put("subType", subType);
      YHORM t = new YHORM();

      t.saveSingle(dbConn, "url", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}

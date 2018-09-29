package yh.core.frame.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.frame.logic.YHClassicInterfaceLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.picture.act.YHImageUtil;
import yh.core.funcs.setdescktop.shortcut.logic.YHShortcutLogic;
import yh.core.funcs.setdescktop.syspara.logic.YHSysparaLogic;
import yh.core.funcs.system.act.YHSystemAct;
import yh.core.funcs.system.data.YHMenu;
import yh.core.funcs.system.interfaces.data.YHInterFaceCont;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.oa.tools.StaticData;

public class YHClassicInterfaceAct {
  /**
   * 获取ietitle
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getInterfaceInfo(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Connection dbConn = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHClassicInterfaceLogic logic = new YHClassicInterfaceLogic();
      Map<String, String> map = logic.getInterfaceInfo(dbConn);
      
      if (map == null) {
        map = new HashMap<String, String>();
      }
      
      if (YHUtility.isNullorEmpty(map.get("title"))) {
        map.put("title", StaticData.SOFTTITLE);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, YHFOM.toJson(map).toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      YHClassicInterfaceLogic logic = new YHClassicInterfaceLogic();
      Map<String, String> map = logic.queryInfo(dbConn, user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询桌面属性");
      request.setAttribute(YHActionKeys.RET_DATA, String.valueOf(YHFOM.toJson(map)));
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询菜单快捷项

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listShortCut(HttpServletRequest request, HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      String contextPath = request.getContextPath();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      PrintWriter pw = response.getWriter();
      String shortcut = user.getShortcut();
      List<String> list = YHSystemAct.listUserMenu(dbConn, user);
      if (null == shortcut || "".equals(shortcut) || !shortcut.contains(",")){
        pw.println("[]");
        pw.flush();
      }
      else{
        StringBuffer sb = new StringBuffer("[");
        for (String s : user.getShortcut().split(",")){
          try {
            if (!list.contains(s)) {
              continue;
            }
            YHShortcutLogic logic = new YHShortcutLogic();
            YHMenu menu = logic.queryShortcut(dbConn, s);
            
            if (menu != null && menu.getUrl() != null){
              YHSystemAct.parseMenuIcon(menu);
              menu.setUrl(YHSystemAct.parseMenuUrl(menu.getUrl(), contextPath, request));
              menu.setLeaf(1);
              sb.append(YHFOM.toJson(menu));
              sb.append(",");
            }
          } catch (NumberFormatException e) {
            continue;
          }
        }
        
        if (sb.charAt(sb.length() - 1) == ','){
          sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        
        pw.println(sb.toString().trim());
        pw.flush();
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public String queryLogoutText(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      String data = logic.queryLogoutText(dbConn);
      data = "\"" + data.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "") + "\"";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryStatusText(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      String text = logic.queryStatusText(dbConn);
      String marquee = logic.queryStatusMarquee(dbConn);
      
      if (text == null) {
        text = "";
      }
      
      if (marquee == null) {
        marquee = "";
      }
      
      StringBuffer sb = new StringBuffer("{\"TEXT\":\"");
      sb.append(text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r\n", "<br>"));
      sb.append("\",\"MARQUEE\":\"");
      sb.append(marquee);
      sb.append("\"}");
      
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryHeaderImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    
    int styleIndex = 1;
    Integer styleInSession = (Integer)request.getSession().getAttribute("STYLE_INDEX");
    if (styleInSession != null) {
      styleIndex = styleInSession;
    }
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      Map<String,String> map = logic.queryHeaderImg(dbConn);
      String w = map.get("width");
      String h = map.get("height");
      
      int width = Integer.parseInt(w);
      int height = Integer.parseInt(h);
      
      if (width <= 10) {
        width = 10;
      }
      
      if (height <=10 ) {
        height = 10;
      }
      
      String path = YHInterFaceCont.ATTA_PATH + File.separator+ "system" + File.separator+  map.get("id")+ File.separator +  map.get("name");
      String newPath = path.replaceAll("\\..*", width + "-" + height + ".jpg");
      //map.put("path", path.replace("\\", "\\\\"));
      //String data = this.toJson(map);
      
      YHImageUtil iu = new YHImageUtil();
      
      if (!new File(path).exists()) {
        return "/core/styles/style" + styleIndex + "/img/banner/logo_bg.jpg";
      }
      if (!new File(newPath).exists()) {
        try {
          iu.saveImageAsUser(path, newPath, width, height);
        } catch (NumberFormatException e) {
          iu.saveImageAsUser(path, newPath, 300, 50);
        }
      }
      
      FileInputStream fis = new FileInputStream(newPath);
      response.setContentType("image/" + map.get("name").replaceAll(".*\\.", ""));
      OutputStream out = response.getOutputStream();
      
      byte[] b = new byte[1024];  
      int i = 0;  
      
      while((i = fis.read(b)) > 0) {  
      out.write(b, 0, i);
      }
      
      out.flush();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch (FileNotFoundException ex) {
      return "/core/styles/style" + styleIndex + "/img/banner/logo_bg.jpg";
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      return "/core/styles/style" + styleIndex + "/img/banner/logo_bg.jpg";
    }
    //return "/core/inc/rtjson.jsp";
    return "";
  }
  
  public String queryUserCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      int count = logic.queryUserCount(dbConn);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, count + "");
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
package yh.core.funcs.setdescktop.theme.act;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sanselan.Sanselan;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.setdescktop.theme.logic.YHThemeLogic;
import yh.core.funcs.setdescktop.userinfo.logic.YHUserinfoLogic;
import yh.core.funcs.system.act.YHSystemAct;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.menu.data.YHSysMenu;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
public class YHThemeAct {
  public final static String SOUND_PATH = "theme"+ File.separator +"sound" + File.separator ;
  public final static String AVATAR_PATH = "core"+ File.separator +"styles"+ File.separator +"imgs"+ File.separator +"avatar" + File.separator ;
  private YHThemeLogic logic = new YHThemeLogic();
  
  /**
   * 初始化表单使用
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String initForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
   
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map<String,String> map = logic.getForm(dbConn, user.getSeqId());
      
      String wr[] = logic.getWeatherRss(dbConn);
      boolean show = logic.isAllowTheme(dbConn);
      
      map.putAll(YHFOM.json2Map(user.getParamSet()));
      map.put("RSS", wr[0]);
      map.put("WEATHER", wr[1]);
      map.put("SHOW_THEME", show ? "1" : "0");
      String rtData = this.toJson(map).toString();
      request.setAttribute(YHActionKeys.RET_DATA, rtData);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
            
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取默认展开菜单的备选项
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMenu(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      List<YHSysMenu> list = new ArrayList<YHSysMenu>();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      List<String> menuList = YHSystemAct.listUserMenu(dbConn, person);
      
      for (String s : menuList){
        if (s.trim().length() == 2){
          YHSysMenu menu = this.logic.getMenuList(dbConn, s);
          if (menu != null){
            list.add(menu);
          }
        }
      }
      StringBuffer sb = new StringBuffer("[");
      for(YHSysMenu o : list){
        sb.append("{");
        sb.append("\"seqId\":\"");
        sb.append(o.getMenuId());
        sb.append("\",");
        sb.append("\"menuName\":\"");
        sb.append(o.getMenuName());
        sb.append("\"}");
        sb.append(",");
      }
        
      if(list.size()>0){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]"); 
      
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  public String changeWeatherCity(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    String city = request.getParameter("WEATHER_CITY");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      user.setWeatherCity(city);
      
      YHUserinfoLogic logic = new YHUserinfoLogic();
      logic.updateWeatherCity(dbConn, user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 更新修改
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setTheme(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    
    Iterator<String> iKeys = fileForm.iterateFileFields();
    
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
    
    String fileName = "";
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String sp = System.getProperty("file.separator");
      String filePath = request.getSession().getServletContext().getRealPath(sp) + SOUND_PATH;
      fileName = user.getSeqId() + ".swf";
      fileForm.saveFile(fieldName, filePath + fileName);
    }
    
    Map<String,String> map = fileForm.getParamMap();
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      user.setTheme(map.get("THEME"));
      user.setMenuImage(map.get("MENU_IMAGE"));
      user.setMenuType(map.get("MENU_TYPE"));
      user.setMenuExpand(map.get("MENU_EXPAND"));
      user.setPanel(map.get("PANEL"));
      user.setCallSound(map.get("CALL_SOUND"));
      user.setSmsOn(map.get("SMS_ON"));
      user.setShowRss("on".equals(map.get("SHOW_RSS")) ? "1" : "0");
      user.setCallSound(map.get("CALL_SOUND"));
      user.setWeatherCity(map.get("WEATHER_CITY"));
      user.setNevMenuOpen(map.get("NEV_MENU_OPEN"));
      YHUserinfoLogic logic = new YHUserinfoLogic();
      Map param = new HashMap<String, String>();
      param.put("fx", map.get("FX"));
      param.put("SHOW_WEATHER", map.get("SHOW_WEATHER"));
      logic.addUserParam(dbConn, param, user);
      
      request.getSession().setAttribute("STYLE_INDEX", YHSystemAct.getStyleIndex(request));
      
      this.logic.setTheme(dbConn, user);
    } catch(Exception ex) {
      throw ex;
    }
    return "/core/funcs/setdescktop/theme/update.jsp";
  }
  
  /**
   * 设置昵称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setAvatarNickName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    YHFileUploadForm fileForm = new YHFileUploadForm();
    
    
    fileForm.parseUploadRequest(request);
    
    Iterator<String> iKeys = fileForm.iterateFileFields();
    InputStream is = fileForm.getInputStream((String)fileForm.iterateFileFields().next());
    
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
    
    String fileName = "";
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      if (YHUtility.isNullorEmpty(fieldName)) {
        continue;
      }
      fileName = "upload-" + user.getSeqId();
      String sp = System.getProperty("file.separator");
      String filePath = request.getSession().getServletContext().getRealPath(sp) + AVATAR_PATH;
      File file = new File(filePath + fileName + ".gif");
      if (file.exists()) {
        file.delete();
      }
      fileForm.saveFile(fieldName, filePath + fileName + ".gif");
    }
    
    Map map = fileForm.getParamMap();
    
    //头像是否改变的标志
    String avatarVal = String.valueOf(map.get("avatarVal"));
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map<String,String> conf = this.logic.getAvatarConfig(dbConn);
      
      BufferedImage srcImage;
      try {
        srcImage = ImageIO.read(is);
        
      } catch (Exception ex) {
        srcImage = Sanselan.getBufferedImage(is);
      }
      
      if (srcImage != null) {
        int imageWidth = srcImage.getWidth(null);
        int imageHeight = srcImage.getHeight(null);
        if (Integer.parseInt(conf.get("width")) < imageWidth || Integer.parseInt(conf.get("height")) < imageHeight) {
          request.setAttribute("msg", "图片大小超过" + conf.get("width") + "*" + conf.get("height"));
          return "/core/funcs/setdescktop/avatar/update.jsp";
        }
      }
      
      
      if (!YHUtility.isNullorEmpty(avatarVal)) {
        user.setAuatar(avatarVal);
        map.put("AVATAR", avatarVal);
      }
      else {
        map.put("AVATAR", fileName);
        user.setAuatar(fileName);
      }
      
      if (this.logic.hasNickName(dbConn, map, user.getSeqId())){
        request.setAttribute("msg", "昵称已经存在");
        return "/core/inc/rtjson.jsp";
      }
      else{
        this.logic.setAvatarNickName(dbConn, map, user.getSeqId());
      }
      request.setAttribute("msg", "添加成功");
      return "/core/funcs/setdescktop/avatar/update.jsp";
    }catch(Exception ex) {
      request.setAttribute("msg", "添加未成功");
      throw ex;
    }finally{
      
    }
  }
  
  /**
   * 检查昵称是否重复
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkNickName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String nickName = request.getParameter("NICK_NAME");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int amount = this.logic.checkNickName(dbConn, nickName);
      
      request.setAttribute(YHActionKeys.RET_DATA, amount + "");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功修改昵称和头像");
      
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取昵称,用于表单上的显示
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAvatarNickName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map<String,String> map = logic.getAvatarNickName(dbConn, user.getSeqId());
      
      String rtData = this.toJson(map).toString();
      request.setAttribute(YHActionKeys.RET_DATA, rtData);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
            
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 获取头像属性(是否允许上传/宽/高)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAvatarConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map<String,String> map = logic.getAvatarConfig(dbConn);
      
      String rtData = this.toJson(map).toString();
      request.setAttribute(YHActionKeys.RET_DATA, rtData);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取头像属性(是否允许上传/宽/高)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String resetAvatar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      user.setAuatar("1");
      
      this.logic.resetAvatar(dbConn, user.getSeqId());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  private String toJson(Map<String,String> m) throws Exception {
    StringBuffer sb = new StringBuffer("{");
    for (Iterator<Entry<String,String>> it = m.entrySet().iterator(); it.hasNext();){
      Entry<String,String> e = it.next();
      sb.append(e.getKey());
      sb.append(":\"");
      sb.append(e.getValue());
      sb.append("\",");
    }
    if (sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb.toString();
  }
}
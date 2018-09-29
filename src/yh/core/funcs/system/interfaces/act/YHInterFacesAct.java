package yh.core.funcs.system.interfaces.act;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.picture.act.YHImageUtil;
import yh.core.funcs.setdescktop.theme.logic.YHThemeLogic;
import yh.core.funcs.system.interfaces.data.YHInterFaceCont;
import yh.core.funcs.system.interfaces.data.YHInterface;
import yh.core.funcs.system.interfaces.data.YHSysPara;
import yh.core.funcs.system.interfaces.logic.YHInterFacesLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHInterFacesAct {
  private static Logger log = Logger.getLogger(YHInterFacesAct.class);
  
  public String changeTheme(HttpServletRequest request,HttpServletResponse response) throws Exception
  {
	  String theme = request.getParameter("theme");
	  log.debug("theme:"+theme);
	  if(null == theme && "".equals(theme)){
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "返回结果失败！");
	      request.setAttribute(YHActionKeys.RET_DATA, "0");
	  }
	  else{
		  
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  Connection dbConn = requestDbConn.getSysDbConn();
		  YHInterFacesLogic logic = new YHInterFacesLogic();
		  logic.updateTheme(dbConn, theme);
		  request.getSession().setAttribute("STYLE_INDEX", Integer.parseInt(theme));
		  YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
		  StringBuffer sb = new StringBuffer("[{userName:\""+person.getUserName()+"\",pwd:\""+person.getPassword()+"\"}]");
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
	      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
	  }
	  return "/core/inc/rtjson.jsp";
  }
  
  public String updateAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      String onlineView = request.getParameter("onlineView");
      String ATTACHMENT = request.getParameter("ATTACHMENT");
      String ATTACHMENT1 = request.getParameter("ATTACHMENT1");
      
      changeWebOSLOGO(request, fileForm);
      changeDefaultStyle(request, fileForm);
      updateInterFace(request, response, fileForm);
      updateBk(request, response, fileForm);
      updateMiibeian(request, response, fileForm);
      updateOnlineView(request, response, fileForm, onlineView);
      updateLogOutText(request, response, fileForm);
      YHInterface wm  = (YHInterface) YHFOM.build(fileForm.getParamMap(), YHInterface.class, null); 
      if(ATTACHMENT.equals("1")){
        updatePicture(request, response, fileForm, wm);
      }
      if(ATTACHMENT1.equals("2")){
        updatePicture1(request, response, fileForm, wm);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, "");
    }catch (SizeLimitExceededException e) {
      return "/core/funcs/system/interface/title/sizeEx.jsp";
    }catch(Exception ex) {
      return "/core/funcs/system/interface/title/ex.jsp";
    }
    return "/core/funcs/system/interface/title/update.jsp";
  }
  
  /**
   * 更改缺省界面风格(webos/经典)
   * @param request
   * @param fileForm
   * @throws Exception
   */
  private void changeDefaultStyle(HttpServletRequest request, YHFileUploadForm fileForm) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String style = fileForm.getParameter("style");      
      if (!"1".equals(style) && !"2".equals(style)) {
        style = "0";
      }
      YHInterFacesLogic logic = new YHInterFacesLogic();
      logic.changeDefaultStyle(dbConn, style);
    }catch(Exception ex) {
      ex.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  /**
   * 更改weboslogo图标
   * @param request
   * @param fileForm
   * @throws Exception
   */
  private void changeWebOSLOGO(HttpServletRequest request, YHFileUploadForm fileForm) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String width = fileForm.getParameter("imgWidth2");      
      String height = fileForm.getParameter("imgHeight2");   
      
      int w = -1, h = -1;
      try {
        w = Integer.parseInt(width);
        h = Integer.parseInt(height);
      } catch (NumberFormatException e) {
        
      }
      
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        if(fieldName.equals("ATTACHMENT2")){
          YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
          String guid = YHGuid.getRawGuid();
          
          String filePath = YHInterFaceCont.ATTA_PATH 
          + File.separator + YHInterFaceCont.MODULE 
          + File.separator + guid 
          + File.separator;
          
          fileForm.saveFile(fieldName, filePath + fileName);
          
          if (w > 0 && h > 0) {
            YHImageUtil iu = new YHImageUtil();
            iu.saveImageAsUser(filePath + fileName, filePath + "n-" + fileName, w, h);
            File f = new File(filePath + fileName);
            
            f.delete();
          }
          YHInterFacesLogic logic = new YHInterFacesLogic();
          logic.changeWebOSLOGO(dbConn, guid);
        }

      }
    }catch(Exception ex) {
      ex.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  /**
   * 默认界面主题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTheme(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String filePath = YHSysProps.getStylePath();
    try {
      StringBuffer sb = new StringBuffer("[");
      File file = new File(filePath.trim());
      if (file.exists()) {
        File[] files = file.listFiles();
        for (File f : files) {
          if (f.getName().startsWith("style")) {
            String value = f.getName().substring(f.getName().length()-1);
            List lineList = new ArrayList();
            YHFileUtility.loadLine2Array(f.getAbsolutePath() + File.separator+ "theme.ini", lineList);
            for (int i = 0; i < lineList.size(); i++) {
              String line = lineList.get(i).toString().trim();
              sb.append("{");
              sb.append("value:\"" + value + "\"");
              sb.append(",text:\"" + line + "\"");
              sb.append("},");
            }
          }
        }
        if (sb.length() > 1) {
          sb.deleteCharAt(sb.length() - 1);
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 默认界面布局
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUi(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String filePath = YHSysProps.getUiPath();
    try {
      StringBuffer sb = new StringBuffer("[");
      File file = new File(filePath.trim());
      if (file.exists()) {
        File[] files = file.listFiles();
        for (File f : files) {
          String path = filePath  + File.separator+f.getName();
          File fileStr = new File(path.trim());
          if (fileStr.exists()) {
            List lineList = new ArrayList();
            YHFileUtility.loadLine2Array(f.getAbsolutePath()  + File.separator+ "ui.ini", lineList);
            for (int i = 0; i < lineList.size(); i++) {
              String line = lineList.get(i).toString().trim();
              sb.append("{");
              sb.append("value:\"" + f.getName() + "\"");
              sb.append(",text:\"" + line + "\"");
              sb.append("},");
            }
          }
        }
        if (sb.length() > 1) {
          sb.deleteCharAt(sb.length() - 1);
        }
      }
      if (!file.exists()) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 登录界面模板
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTemplate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String filePath = YHSysProps.getUiTemplatePath();
    try {
      StringBuffer sb = new StringBuffer("[");
      File file = new File(filePath.trim());
      if (file.exists()) {
        List lineList = new ArrayList();
        YHFileUtility.loadLine2Array(file.getAbsolutePath()  + File.separator+ "template.ini", lineList);
        for (int i = 0; i < lineList.size(); i++) {
          String line = lineList.get(i).toString().trim();
          String equalSign = line.substring(0, line.indexOf("="));
          String text = line.substring(line.indexOf("=") + 1, line.length());
          sb.append("{");
          sb.append("value:\"" + equalSign + "\"");
          sb.append(",text:\"" + text + "\"");
          sb.append("},");
        }
        if (sb.length() > 1) {
          sb.deleteCharAt(sb.length() - 1);
        }
      }
      if (!file.exists()) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getStatusText(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic dl = new YHInterFacesLogic();
      String data = dl.getStatusTextLogic(dbConn);
      if (data != null){ 
        data = data.replace("\r\n", "&#13;&#10;").replace("\n", "&#13;&#10;").replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "&#13;&#10;"); 
      } 
      data = "\"" + (data == null || "null".equals(data) ? "" : data) + "\"";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 界面设置部分数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getInterFaces(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map filters = new HashMap();
      filters = null;
      String data = null;
      List< YHInterface> intface = orm.loadListSingle(dbConn, YHInterface.class, filters);
      if(intface.size() == 0){
        data = YHFOM.toJson(new YHInterface()).toString();
      }else{
        data = YHFOM.toJson(intface.get(0)).toString();
      }
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
  
  /**
   * 获取weboslogo图标
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getWebosLogo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic logic = new YHInterFacesLogic();
      String logo = logic.queryWebOSLOGO(dbConn);
      String data = "\"1\"";
      if (YHUtility.isNullorEmpty(logo)) {
        data = "\"0\"";
      }
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
  
  /**
   * 获取缺省界面风格(webos/经典界面)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDefaultStyle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic logic = new YHInterFacesLogic();
      String style = logic.queryDefaultStyle(dbConn);
      String data = null;
      if (YHUtility.isNullorEmpty(style)) {
        data = "\"0\"";
      }
      else {
        data = "\"" + style + "\"";
      }
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
  
  public String deleteWebosLogo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic logic = new YHInterFacesLogic();
      logic.changeWebOSLOGO(dbConn, "");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑界面设置数据
   * @param request
   * @param response
   * @param fileForm
   * @return
   * @throws Exception
   */
  
  public String updateInterFace(HttpServletRequest request,
      HttpServletResponse response, YHFileUploadForm fileForm) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String seqId = fileForm.getParameter("seqIds");
      String ieTitle = fileForm.getParameter("ieTitle");
      String bannerText = fileForm.getParameter("bannerText");
      String bannerFont = fileForm.getParameter("styleDis");
      String statusText = fileForm.getParameter("statusText");
      String imgWidth = fileForm.getParameter("imgWidth");
      String imgHeight = fileForm.getParameter("imgHeight");
      String loginInterface = fileForm.getParameter("loginInterface");
      if(!YHUtility.isNullorEmpty(loginInterface)){
        loginInterface = "1";
      }else{
        loginInterface = "0";
      }
      String ui = fileForm.getParameter("ui");
      String theme = fileForm.getParameter("theme");
      String template = fileForm.getParameter("template");
      String themeSelect = fileForm.getParameter("themeSelect");
      if(!YHUtility.isNullorEmpty(themeSelect)){
        themeSelect = "1";
      }else{
        themeSelect = "0";
      }
      String avatarUpload = fileForm.getParameter("avatarUpload");
      if(!YHUtility.isNullorEmpty(avatarUpload)){
        avatarUpload = "1";
      }else{
        avatarUpload = "0";
        YHThemeLogic.resetAllAvatar(dbConn);
      }
      String avatarWidth = fileForm.getParameter("avatarWidth");
      int avatarHeight = Integer.parseInt(fileForm.getParameter("avatarHeight"));
      
      Map m =new HashMap();
      if(!"0".equals(seqId)){
        m.put("seqId", seqId);
      }
      m.put("ieTitle", ieTitle);
      m.put("bannerText", bannerText);
      m.put("bannerFont", bannerFont);
      m.put("statusText", statusText);
      m.put("imgWidth", imgWidth);
      m.put("imgHeight", imgHeight);
      m.put("loginInterface", loginInterface);
      m.put("themeSelect", themeSelect);
      m.put("ui", ui);
      m.put("theme", theme);
      m.put("template", template);
      m.put("avatarUpload", avatarUpload);
      m.put("avatarWidth", avatarWidth);
      m.put("avatarHeight", avatarHeight);
      
      YHORM t = new YHORM();
      if(!"0".equals(seqId)){
        t.updateSingle(dbConn, "interface", m);
      }else{
        t.saveSingle(dbConn, "interface", m);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 自定义桌面背景图片
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getTableGround(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      YHSysPara org = null;
      String data = null;
      org = orgLogic.getSysPara(dbConn);
      if (org == null) {
        org = new YHSysPara();
      }
      data = YHFOM.toJson(org).toString();
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

  /**
   * 编辑自定义桌面背景图片
   * @param request
   * @param response
   * @param fileForm
   * @throws Exception
   */
  
  public void updateBk(HttpServletRequest request,
      HttpServletResponse response, YHFileUploadForm fileForm) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      String myTableBkGround = fileForm.getParameter("myTableBkGround");
      //Date d =new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
      int seqId = Integer.parseInt(fileForm.getParameter("seqIdBk"));
      orgLogic.updateBk(dbConn, seqId, myTableBkGround);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 网站备案号
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getMiibeian(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      YHSysPara org = null;
      String data = null;
      org = orgLogic.getMiibeian(dbConn);
      if (org == null) {
        org = new YHSysPara();
      }
      data = YHFOM.toJson(org).toString();
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
  
  /**
   * 编辑网站备案号
   * @param request
   * @param response
   * @param fileForm
   * @throws Exception
   */
  
  public void updateMiibeian(HttpServletRequest request,
      HttpServletResponse response, YHFileUploadForm fileForm) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      String miibeian = fileForm.getParameter("miibeian");
      orgLogic.updateMiibeian(dbConn, miibeian);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"界面设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 在线人员显示方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getOnlineView(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      YHSysPara org = null;
      String data = null;
      org = orgLogic.getOnlineView(dbConn);
      if (org == null) {
        org = new YHSysPara();
      }
      data = YHFOM.toJson(org).toString();
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
  
  /**
   * 编辑在线人员显示方式
   * @param request
   * @param response
   * @param fileForm
   * @param onlineView
   * @throws Exception
   */
  
  public void updateOnlineView(HttpServletRequest request,
      HttpServletResponse response, YHFileUploadForm fileForm, String onlineView) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      orgLogic.updateOnlineView(dbConn, onlineView);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"界面设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 用户点击注销时，显示这里设置的文字
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getLogOutText(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      YHSysPara org = null;
      String data = null;
      org = orgLogic.getLogOutText(dbConn);
      if (org == null) {
        org = new YHSysPara();
      }
      data = YHFOM.toJson(org).toString();
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
  
  /**
   * 编辑用户点击注销时，显示这里设置的文字
   * @param request
   * @param response
   * @param fileForm
   * @throws Exception
   */
  
  public void updateLogOutText(HttpServletRequest request,
      HttpServletResponse response, YHFileUploadForm fileForm) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHInterFacesLogic orgLogic = new YHInterFacesLogic();
      String logOutText = fileForm.getParameter("logOutText");
      //logOutText = YHUtility.encodeSpecial(logOutText);
      orgLogic.updateLogOutText(dbConn, logOutText);
      
      //Map m =new HashMap();
      //m.put("PARA_NAME", "LOG_OUT_TEXT");
      //m.put("PARA_VALUE", orderNo);
      
      //YHORM t = new YHORM();
      //t.updateSingle(dbConn, "userGroup", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"界面设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 更新主界面-顶部图标
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public void updatePicture(HttpServletRequest request,
      HttpServletResponse response, YHFileUploadForm fileForm,YHInterface wm) throws Exception {
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String seqIds = fileForm.getParameter("seqIds");
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      fileForm.parseUploadRequest(request);
      YHInterFacesLogic ifl = new YHInterFacesLogic();
      ifl.updateLogic(conn, fileForm, person.getSeqId(), YHInterFaceCont.ATTA_PATH, seqIds, wm);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, "");
    }catch(Exception ex) {
      ex.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/system/wordmodel/success.jsp?seqId=" + seqId;
    //return "/core/inc/rtjson.jsp";
  }
  
  public void updatePicture1(HttpServletRequest request,
      HttpServletResponse response, YHFileUploadForm fileForm,YHInterface wm) throws Exception {
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String seqIds = fileForm.getParameter("seqIds");
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      fileForm.parseUploadRequest(request);
      YHInterFacesLogic ifl = new YHInterFacesLogic();
      ifl.updateLogic1(conn, fileForm, person.getSeqId(), YHInterFaceCont.ATTA_PATH, seqIds, wm);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, "");
    }catch(Exception ex) {
      ex.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/system/wordmodel/success.jsp?seqId=" + seqId;
    //return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 显示主界面-顶部图标 图片名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showModel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String idStr = request.getParameter("seqId");
      if(YHUtility.isNullorEmpty(idStr)){
        idStr = "0";
      }
      int id = Integer.valueOf(idStr);
      YHORM orm = new YHORM();
      YHInterface wm = (YHInterface) orm.loadObjSingle(conn, YHInterface.class, id);
      StringBuffer data = YHFOM.toJson(wm);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateAttachMent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInterFacesLogic wml = new YHInterFacesLogic();
      String attachmentIds = request.getParameter("attachmentId");
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String attachmentId = "";
      String attachmentName = "";
      
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("attachmentId", attachmentId);
      m.put("attachmentName", attachmentName);
      
      YHORM orm = new YHORM();
      orm.updateSingle(conn, "interface", m);
      wml.doDelete(conn, YHInterFaceCont.ATTA_PATH, attachmentIds);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String updateAttachMent1(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInterFacesLogic wml = new YHInterFacesLogic();
      String attachmentIds = request.getParameter("attachmentId1");
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String attachmentId1 = "";
      String attachmentName1 = "";
      
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("attachmentId1", attachmentId1);
      m.put("attachmentName1", attachmentName1);
      
      YHORM orm = new YHORM();
      orm.updateSingle(conn, "interface", m);
      wml.doDelete1(conn, YHInterFaceCont.ATTA_PATH, attachmentIds);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

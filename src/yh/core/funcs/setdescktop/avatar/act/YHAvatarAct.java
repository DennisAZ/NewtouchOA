package yh.core.funcs.setdescktop.avatar.act;

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
import yh.core.funcs.setdescktop.avatar.logic.YHAvatarLogic;
import yh.core.funcs.setdescktop.theme.logic.YHThemeLogic;
import yh.core.funcs.setdescktop.userinfo.logic.YHUserinfoLogic;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;

public class YHAvatarAct {
  public final static String SOUND_PATH = "theme"+File.separator+"sound"+File.separator+"";
  public final static String AVATAR_PATH = "attachment"+File.separator+"avatar"+File.separator+"";
  public final static String PHOTO_PATH = "attachment"+File.separator+"photo"+File.separator+"";
  private YHAvatarLogic logic = new YHAvatarLogic();
  
  
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
    
    Map map = fileForm.getParamMap();
    
    //头像是否改变的标志
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
      
      YHAvatarLogic logic = new YHAvatarLogic();
      
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String sp = System.getProperty("file.separator");
        String filePath = "";
        
        if (YHUtility.isNullorEmpty(fieldName)) {
          continue;
        }
        String fileName = fileForm.getFileName(fieldName);
        if (YHUtility.isNullorEmpty(fileName) || !fileName.contains(".")) {
          continue;
        }
        
        String[] names = fileName.split("\\.");
        
        fileName = user.getSeqId() + "." + names[names.length - 1];
        if ("avatar".equals(fieldName)) {
          filePath = request.getSession().getServletContext().getRealPath(sp) + AVATAR_PATH;
          logic.setAvatar(dbConn, user.getSeqId(), fileName);
          YHMsgPusher.pushAvatar(String.valueOf(user.getSeqId()), fileName);
          YHMsgPusher.updateOrg(dbConn);
        }
        else {
          filePath = request.getSession().getServletContext().getRealPath(sp) + PHOTO_PATH;
          logic.setPhoto(dbConn, user.getSeqId(), fileName);
        }
        
        File file = new File(filePath + fileName);
        if (file.exists()) {
          file.delete();
        }
        fileForm.saveFile(fieldName, filePath + fileName);
      }
      
      if (this.logic.hasNickName(dbConn, map, user.getSeqId())){
        request.setAttribute("msg", "昵称已经存在");
        return "/core/funcs/setdescktop/avatar/update.jsp";
      }
      else{
        this.logic.setAvatarNickName(dbConn, map, user.getSeqId());
      }
      request.setAttribute("msg", "修改成功");
      return "/core/funcs/setdescktop/avatar/update.jsp";
    } catch(Exception ex) {
      request.setAttribute("msg", "修改未成功");
    }finally{
      
    }
    return "/core/funcs/setdescktop/avatar/update.jsp";
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
   * 删除头像
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
      
      user.setAuatar("");
      
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
  
  /**
   * 删除照片
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String resetPhoto(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      user.setPhoto("");
      
      this.logic.resetPhoto(dbConn, user.getSeqId());
      
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
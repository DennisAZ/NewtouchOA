package yh.core.funcs.news.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.news.data.YHImgNews;
import yh.core.funcs.news.logic.YHFindNewaImageLogic;
import yh.core.funcs.news.util.YHImageUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;


/**
 * 查找图片新闻用的类
 * @author qwx110
 *
 */
public class YHImgNewsAct{
  private static String[] imageType = {"gif","jpg","jpeg","png","bmp","iff","jp2","jpx","jb2","jpc","xbm","wbmp"};
  public String getNews(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String newsId = request.getParameter("newsId");
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHFindNewaImageLogic logic = new YHFindNewaImageLogic();
      YHPerson user =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHImgNews news = logic.getImageNews(newsId, dbConn , user);
      String attachmentId = news.getAttachmentId();
      String attachmentName= news.getAttachmentName();
      String[] attrName = null;
      String[] attrId = null;
      StringBuffer sb =  new StringBuffer();
      sb.append("{\"images\":[");
      int count = 0 ;
      if(!YHUtility.isNullorEmpty(attachmentName) 
          && !YHUtility.isNullorEmpty(attachmentId)){
        attrName = attachmentName.split("[*]");
        attrId = attachmentId.split(",");
        
        for(int i=0; i<attrId.length; i++){
          if(!YHUtility.isNullorEmpty(attrName[i]) 
              && isImageType(YHFileUtility.getFileExtName(attrName[i]))
              && !YHUtility.isNullorEmpty(attrId[i])){
            String bigPath = news.getPicPath(attrName[i], attrId[i]);
            File file = new File(bigPath);
            if (file.exists()) {
              String url = news.getSmallPicPath(attrName[i], attrId[i]);
              File smallImageFile = new File(url);
              if (!smallImageFile.exists()) {
                YHImageUtility.saveImageAsJpg(bigPath,url, 200,160);
              }
              sb.append("{\"path\":\"" + YHUtility.encodeURL(bigPath) + "\"");
              sb.append(",\"id\":\"" + attrId[i] + "\"");
              sb.append(",\"smallPath\":\"" + YHUtility.encodeURL(url)+ "\"},");
              count++;
            }
          }
        }      
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      sb.append(",\"subject\":\"" + YHUtility.encodeSpecial(news.getSubject())+ "\"");
      String content = news.getContent();
      if (content == null) {
        content = "";
      }
      content = content.replaceAll("<P>", "");
      content = content.replaceAll("</P>", "");
      
      sb.append(",\"content\":\"" +YHUtility.encodeSpecial(content) + "\"");
      Timestamp newsTime = news.getNewsTime();
      SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
      String time = "";
      if (newsTime !=null) {
         time = sd.format(newsTime);
      }
      
      sb.append(",\"time\":\""+ time +"\"");
      sb.append(",\"address\":[\"海淀\",\"西城\"]");
      sb.append(",\"org\":[\"信息中心\"]");
      sb.append(",\"names\":[\"张三\",\"李四\",\"李四\",\"李四\",\"李四\",\"李四\",\"李四\",\"李四\"]");
      sb.append(",\"nextTitle\":\""+ YHUtility.encodeSpecial(news.getNextTitle()) +"\"");
      sb.append(",\"nextId\":\""+ (news.getNextId() == 0 ? "" :news.getNextId()) +"\"");
      sb.append("}");
      ajax(sb.toString(), response);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String getNewsList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      YHFindNewaImageLogic logic = new YHFindNewaImageLogic();
      List<YHImgNews> list = logic.findNewsAndPictures(dbConn, user);
      StringBuffer sb =  new StringBuffer();
      sb.append("{\"list\":[");
      int count = 0;
      for (YHImgNews news : list) {
        String attId = news.getAttachmentId();
        String attName = news.getAttachmentName();
        if (attId == null || attName == null) {
          continue;
        }
        String subject = YHUtility.encodeSpecial(news.getSubject());
        int seqId = news.getSeqId();
        String[] attIds = attId.split(",");
        String[] attNames = attName.split("[*]");
        
        if (attIds.length > 0 && attNames.length > 0 ) {
          String aId = attIds[0];
          String aName = attNames[0];
          if(!YHUtility.isNullorEmpty(aName) 
              && isImageType(YHFileUtility.getFileExtName(aName))
              && !YHUtility.isNullorEmpty(aId)){
            String bigPath = news.getPicPath(aName, aId);
            File file = new File(bigPath);
            if (file.exists()) {
              String url = news.getSmallPicPath(aName, aId);
              File smallImageFile = new File(url);
              if (!smallImageFile.exists()) {
                YHImageUtility.saveImageAsJpg(bigPath,url, 200,160);
              }
              sb.append("{\"id\":\"" + seqId + "\"");
              sb.append(",\"title\":\"" + subject + "\"");
              sb.append(",\"smallpath\":\"" + YHUtility.encodeURL(url)+ "\"},");
              count++;
            }
          }
        }
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]}");
      ajax(sb.toString(), response);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public static boolean isImageType(String ext) {
    for (int i = 0;i < imageType.length ;i++ ) {
      String imageExt = imageType[i];
      if (imageExt.equalsIgnoreCase(ext)) {
        return true;
      }
    }
    return false;
  }
  /**
   * 桌面模块图片新闻
   * @param request
   * @param response
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public String findNewsAndPicturesAjax(HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHFindNewaImageLogic imgLogic = new YHFindNewaImageLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); 
      List<YHImgNews> imgNews = imgLogic.findNewsAndPictures(dbConn, user);
      String newsJsons = toJson(imgNews);
      //YHOut.println(newsJsons);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功");       
      request.setAttribute(YHActionKeys.RET_DATA, newsJsons);
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;   
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查找图片的绝对路径 
   * @param request
   * @param response
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public String findNewsAndPicturesPathAjax(HttpServletRequest request, HttpServletResponse response)throws Exception, SQLException{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHFindNewaImageLogic imgLogic = new YHFindNewaImageLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); 
      List<YHImgNews> imgNews = imgLogic.findNewsAndPictures(dbConn, user);
      String newsJsons = toJson2(imgNews);
      //YHOut.println(newsJsons);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功");       
      request.setAttribute(YHActionKeys.RET_DATA, newsJsons);
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;   
    }
    return "/core/inc/rtjson.jsp";
  }
  
/**
 * 把一个list转化为字符串 
 * @param imgNews
 * @return
 * @throws Exception 
 */
  public String toJson( List<YHImgNews> imgNews) throws Exception {
    StringBuffer sb = new StringBuffer();
     sb.append("[");
       if(imgNews !=null && imgNews.size() >0){
         for(int i=0; i<imgNews.size(); i++){
          sb.append(imgNews.get(i).toJson());
          if(i < imgNews.size()-1){
            sb.append(",");
          }
         }
       }
     sb.append("]");
    
    return sb.toString();
  }
  
  /**
   * 把一个list转化为字符串 
   * @param imgNews
   * @return
   * @throws Exception 
   */
    public String toJson2( List<YHImgNews> imgNews) throws Exception{
      StringBuffer sb = new StringBuffer();
       sb.append("[");
         if(imgNews !=null && imgNews.size() >0){
           for(int i=0; i<imgNews.size(); i++){
            sb.append(imgNews.get(i).toJson2());
            if(i < imgNews.size()-1){
              sb.append(",");
            }
           }
         }
       sb.append("]");
      
      return sb.toString();
    }
    
    /**
     * 最新的图片新闻模块
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public String leatImagNews(HttpServletRequest request, HttpServletResponse response)throws Exception, SQLException{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
      Connection dbConn = null;
      try{
        dbConn = requestDbConn.getSysDbConn();
        YHFindNewaImageLogic imgLogic = new YHFindNewaImageLogic();
        YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");         
        String imgJsons = imgLogic.findImgToJsonDesk(dbConn, user, request);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功");       
        request.setAttribute(YHActionKeys.RET_DATA, imgJsons);
      } catch (Exception ex){
        String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
        throw ex;   
      }
      return "/core/inc/rtjson.jsp";
    }
    
    public static void ajax(String str, HttpServletResponse response) throws Exception{
      PrintWriter pw = response.getWriter();    
      String rtData = str;
      pw.println(rtData);    
      pw.flush();
    }
}

package yh.subsys.inforesouce.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.news.util.YHImageUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;
import yh.subsys.inforesouce.data.YHSignFile;
import yh.subsys.inforesouce.db.YHMetaDbHelper;
import yh.subsys.inforesouce.logic.YHImageMetaLogic;
import yh.subsys.inforesouce.logic.YHMateTypeLogic;
import yh.subsys.inforesouce.util.YHAjaxUtil;
import yh.subsys.inforesouce.util.YHFileMateConstUtil;
import yh.subsys.inforesouce.util.YHStringUtil;

/**
 * 图片浏览<br>
 * 调用的类有YHMetaDbHelper, YHMateTypeLogic, YHImageMetaLogic
 * @see yh.subsys.inforesouce.db.YHMetaDbHelper
 * @see yh.subsys.inforesouce.logic.YHMateTypeLogic
 * @see yh.subsys.inforesouce.logic.YHImageMetaLogic
 * @author lh
 *
 */
public class YHImageMetaAct{
  public static String profix = System.getProperty("file.separator");
  /**
   * <fieldset>
   * <legend>查找图片列表</legend>
   * <p>
   * 先点左边得先看右边的树有没有选中的，如果有选中的则把值代过来<br>
   * 先点右边的树先看有没有点击左边的列表，如果点击了，则把值带过来,调用YHMateTypeLogic.findNumberId<br>
   * 返回人名，地名，组织机构名，主题词等元数据的编号, 调用YHMetaDbHelper.searchImageList返回与元数据相关的图片列表</p>
   * </fieldset>
   * @see yh.subsys.inforesouce.logic.YHMateTypeLogic#findNumberId(Connection, String)
   * @see yh.subsys.inforesouce.db.YHMetaDbHelper#searchImageList(Connection, List, Map)
   * @param request
   * @param response
   * @return null
   * @throws Exception
   */
  public String findImageList(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    try{
      Connection dbConn = null;
      dbConn = requestDbConn.getSysDbConn();
      YHMetaDbHelper helper = new YHMetaDbHelper();
      List<String> moduleList = new ArrayList<String>();
      moduleList.add("news");
      //String modules = request.getParameter("modules");             //从左侧带过来的类型，如notify,news,mails等的标志
//      if(!YHUtility.isNullorEmpty(modules)){
//        String[] module = modules.split(",");
//        for(int i=0; i<module.length; i++){
//          if(!YHUtility.isNullorEmpty(module[i])){
//            moduleList.add(module[i]);
//          }
//        }
//      }
      String type = request.getParameter("type");
      YHMateTypeLogic mateLogic = new YHMateTypeLogic();
      String number = "";
      if ("address".equals(type)) {
        number = mateLogic.findNumberId(dbConn, YHFileMateConstUtil.areaName);
      } else if ("org".equals(type)) {
        number =  mateLogic.findNumberId(dbConn, YHFileMateConstUtil.Org);
      } else if ("meta".equals(type)){
        number = request.getParameter("prop");
      } else {
        number = mateLogic.findNumberId(dbConn, YHFileMateConstUtil.userName);
      }
      String value = request.getParameter("value");                //从右侧带过来的值串，如：M12-M23-123,M23-asdfd,M43-,等的值
      String metas = number + "-" + value;
      metas = YHUtility.decodeURL(metas);
      Map<String, String> metaFilters = null;
      if(!YHUtility.isNullorEmpty(metas)){
        metaFilters = YHStringUtil.toMap(metas.trim());
      }
      
      List<YHSignFile> fileList = helper.searchImageList(dbConn, moduleList, metaFilters);   //图片列表       
      StringBuffer sb =  new StringBuffer();
      sb.append("{\"images\":[");
      int count = 0 ;
      for (YHSignFile signFile : fileList) {
        String bigPath = signFile.getFilePath();
        File file = new File(bigPath);
        if (file.exists()) {
          String url = getSmallPicPath(bigPath);
          File smallImageFile = new File(url);
          if (!smallImageFile.exists()) {
            YHImageUtility.saveImageAsJpg(bigPath,url, 200,160);
          }
          sb.append("{\"path\":\"" + YHUtility.encodeURL(bigPath) + "\"");
          sb.append(",\"id\":\"" + signFile.getFileId() + "\"");
          sb.append(",\"smallPath\":\"" + YHUtility.encodeURL(url)+ "\"},");
          count++;
        }
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      sb.append("}");
      YHAjaxUtil.ajax(sb.toString(), response);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 通过文件id返回图片的元数据
   * 调用YHImageMetaLogic的getImageMeta方法返回图片的元数据
   * @see yh.subsys.inforesouce.logic.YHImageMetaLogic#getImageMeta(Connection, String)
   * @param request
   * @param response
   * @return null
   * @throws Exception
   */
  public String getImageMeta(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    try{
      Connection dbConn = null;
      dbConn = requestDbConn.getSysDbConn();
      String fileId = request.getParameter("fileId");
      
      YHImageMetaLogic logic = new YHImageMetaLogic();
      String xml = logic.getImageMeta(dbConn, fileId);
      response.setContentType("text/xml");
      PrintWriter pw = response.getWriter();  
      pw.println(xml);    
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String getSmallPicPath(String bigPath) {
    String path = YHFileUtility.getFilePath(bigPath);
    String fileName = YHFileUtility.getFileName(bigPath);
    path +=  profix + "smallPic" ;
    File file = new File(path);
    if (!file.exists()) {
      file.mkdir();
    }
    String smallPath = path  + profix + fileName; 
    return smallPath ; 
  }
  public static void main(String[] args) {
    try {
//      Connection conn = TestDbUtil.getConnection(false, "TD_OA2");
//      YHMetaDbHelper helper = new YHMetaDbHelper();
//      List<String> moduleList = new ArrayList();
//      moduleList.add("news");
//      Map<String , String> metaFilters = new HashMap();
//      metaFilters.put("MEX150", "广角");
//      List<YHSignFile> fileList = helper.searchImageList(conn, moduleList, metaFilters);   //图片列表  
//      System.out.println(fileList.size());
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}

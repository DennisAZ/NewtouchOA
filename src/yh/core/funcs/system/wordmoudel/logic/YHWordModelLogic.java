package yh.core.funcs.system.wordmoudel.logic;

import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.system.wordmoudel.data.YHWordModel;
import yh.core.funcs.system.wordmoudel.data.YHWordModelCont;
import yh.core.load.YHPageLoader;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
/**
 * 套红模板业务逻辑
 * @author tlk
 *
 */
public class YHWordModelLogic {
  private static Logger log = Logger.getLogger(YHWordModelLogic.class);
  /**
   * 保存模板
   * @param conn
   * @param fileForm
   * @param userId
   * @param filePath
   * @throws Exception
   */
  public void saveLogic(Connection conn,YHFileUploadForm fileForm,int userId, String filePath ) throws Exception{
    try {
      YHORM orm = new YHORM();
      YHWordModel wm = (YHWordModel) YHFOM.build(fileForm.getParamMap(), YHWordModel.class, null);
      String userStr = fileForm.getParameter("user") == null ? "" : fileForm.getParameter("user");
      String roleStr = fileForm.getParameter("role") == null ? "" : fileForm.getParameter("role");
      String deptStr = fileForm.getParameter("dept") == null ? "" : fileForm.getParameter("dept");
      String privStr = userStr + "|" + deptStr + "|" + roleStr ;
      wm.setPrivStr(privStr);
      wm.setUserId(userId);
      wm.setCreateTime(new Date());
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      String attachmentId = "";
      String attachmentName = "";
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
        String rand = YHGuid.getRawGuid();
        attachmentId = hard + "_" + rand;
        attachmentName = fileName;
        fileForm.saveFile(fieldName, filePath +  File.separator  + YHWordModelCont.MODULE +  File.separator  + hard + File.separator  + rand + "." + fileName);
      }
      wm.setAttachmentId(attachmentId);
      wm.setAttachmentName(attachmentName);
      //System.out.println(wm);
      orm.saveSingle(conn, wm);
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 更新模板
   * @param conn
   * @param fileForm
   * @param userId
   * @param filePath
   * @throws Exception
   */
  public void updateLogic(Connection conn,YHFileUploadForm fileForm,int userId, String filePath ) throws Exception{
    try {
      YHORM orm = new YHORM();
      YHWordModel wm = (YHWordModel) YHFOM.build(fileForm.getParamMap(), YHWordModel.class, null);
      String userStr = fileForm.getParameter("user") == null ? "" : fileForm.getParameter("user");
      String roleStr = fileForm.getParameter("role") == null ? "" : fileForm.getParameter("role");
      String deptStr = fileForm.getParameter("dept") == null ? "" : fileForm.getParameter("dept");
      String privStr = userStr + "|" + deptStr + "|" + roleStr ;
      wm.setPrivStr(privStr);
      wm.setUserId(userId);
      wm.setCreateTime(new Date());
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      String attachmentId = "";
      String attachmentNameNew = "";
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
        String rand = YHGuid.getRawGuid();
        attachmentId = hard + "_" + rand;
        attachmentNameNew = fileName;
        fileForm.saveFile(fieldName, filePath +  File.separator  + YHWordModelCont.MODULE +  File.separator  + hard +  File.separator  + rand + "." + fileName);
      }
      if(attachmentNameNew != null && !"".equals(attachmentNameNew)){
        wm.setAttachmentId(attachmentId);
        wm.setAttachmentName(attachmentNameNew);
      }
      //System.out.println(wm);
      orm.updateSingle(conn, wm);
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 列出所有套红文件
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String listWordModel(Connection conn,Map request) throws Exception{
    String result = "";
    String sql =  "select SEQ_ID,MODEL_NAME,ATTACHMENT_NAME,ATTACHMENT_ID,CREATE_TIME,PRIV_STR from oa_word_model where 1=1 ";
    String query = "";
    sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result = pageDataList.toJson();
    return result;
  }
  /**
   * 列出所有套红文件
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String listWordModelSearch(Connection conn,Map request) throws Exception{
    String result = "";
    String sql =  "select SEQ_ID,MODEL_NAME,ATTACHMENT_NAME,ATTACHMENT_ID,CREATE_TIME,PRIV_STR from oa_word_model where 1=1 ";
    String query = " order by  CREATE_TIME ";
    String whereStr = getWhere(request);
    if(!"".equals(whereStr)){
      sql += whereStr;
    }
    sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    result = pageDataList.toJson();
    return result;
  }
  private String getWhere(Map request) throws Exception{
    String where = "";
    String beginTime = ((String[])request.get("beginTime"))[0];
    String endTime = ((String[])request.get("endTime"))[0];
    String modelName = ((String[])request.get("modelName"))[0];
    modelName = YHDBUtility.escapeLike(modelName);
    if(beginTime != null && !"".equals(beginTime)){
      beginTime += " 00:00:00";
      String dbDateF = YHDBUtility.getDateFilter("CREATE_TIME", beginTime, " >= ");
      where += " and " + dbDateF;
    }
    if(endTime != null && !"".equals(endTime)){
      endTime += " 23:59:59";
      String dbDateF = YHDBUtility.getDateFilter("CREATE_TIME", endTime, " <= ");
      where += " and " + dbDateF;
    }
    if(modelName != null && !"".equals(modelName)){
      where += " AND MODEL_NAME LIKE \'%" + YHDBUtility.escapeLike(modelName) + "%\' "  + YHDBUtility.escapeLike() ;
    }
    return where;
  }
  
  public void doDelete(Connection conn,int id,String attaPath) throws Exception{
    YHORM orm = new YHORM();
    YHWordModel wm = (YHWordModel) orm.loadObjSingle(conn, YHWordModel.class, id);
    String attachmentId = wm.getAttachmentId();
    String attachmentName =wm.getAttachmentName();
    orm.deleteSingle(conn, YHWordModel.class, id);
    String fileName = "";
    String path = "";
    String pathTest = "";
    if(attachmentName.trim().endsWith("*")){
      attachmentName = attachmentName.trim().substring(0,attachmentName.trim().length() - 1);
    }
    if(attachmentId.trim().endsWith(",")){
      attachmentId = attachmentId.trim().substring(0,attachmentId.trim().length() - 1);
    }
    if(attachmentId != null && !"".equals(attachmentId)){
      String attIds[] = attachmentId.split("_");
      fileName = attIds[1] + "_" + attachmentName;
      path = attaPath  +  YHWordModelCont.MODULE +  File.separator + attIds[0] +  File.separator   + fileName;
     // pathTest = attaPath +  File.separator + attIds[0] + File.separator  + fileName;
    }
    File file = new File(path);
    
    if(file.exists()){
      file.delete();
    }
    
  }
}

package yh.core.funcs.workplan.logic;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.workplan.data.YHWorkPlanCont;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;

public class YHWorkPlanManageLogic {
  private static Logger log = Logger.getLogger(YHWorkPlanManageLogic.class);
  //private YHNotifyManageUtilLogic notifyManageUtil = new YHNotifyManageUtilLogic();
  /**
 
  
  /**
   * 处理上传附件，返回附件id，附件名称

   * 
   * @param request
   *          HttpServletRequest
   * @param
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath() +File.separator+ hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath()  +File.separator+ YHWorkPlanCont.MODULE  +File.separator+ hard  +File.separator+fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 附件批量上传页面处理
   * @return
  * @throws Exception 
   */
  public StringBuffer uploadMsrg2Json( YHFileUploadForm fileForm) throws Exception{
    StringBuffer sb = new StringBuffer();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try{
      attr = fileUploadLogic(fileForm);
      Set<String> attrKeys = attr.keySet();
      for (String key : attrKeys){
        String fileName = attr.get(key);
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      long size = getSize(fileForm);
      sb.append("{");
      sb.append("'attachmentId':").append("\"").append(attachmentId).append("\",");
      sb.append("'attachmentName':").append("\"").append(attachmentName).append("\",");
      sb.append("'size':").append("").append(size);
      sb.append("}");
   } catch (Exception e){
     e.printStackTrace();
     throw e;
   }
    return sb;
  }
  
  
  /**
   * 附件批量上传页面处理
   * @return
  * @throws Exception 
   */
  public Map uploadMsrg2Map( YHFileUploadForm fileForm,String pathP) throws Exception{
    Map<String, String> map = new HashMap();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try{
      attr = fileUploadLogic(fileForm);
      Set<String> attrKeys = attr.keySet();
      for (String key : attrKeys){
        String fileName = attr.get(key);
        String file = fileName.split("_")[1];
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      long size = getSize(fileForm);
      map.put("attachmentId", attachmentId);
      map.put("attachmentName", attachmentName);
   } catch (Exception e){
     e.printStackTrace();
     throw e;
   }
    return map;
  }
  
  public long getSize( YHFileUploadForm fileForm) throws Exception{
    long result = 0l;
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      result += fileForm.getFileSize(fieldName);
    }
    return result;
  }
}

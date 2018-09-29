package yh.core.funcs.office.ntko.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.office.ntko.data.YHNtkoCont;
import yh.core.funcs.office.ntko.data.YHOcLog;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.wordmoudel.data.YHWordModel;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHNtkoLogic {
  /**
   * 得到锁标记
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public String getLock(Connection conn,String fiName) throws Exception {
    String result = "";
    String sql = "select "
      + " PARA_VALUE "
      + " from " 
      + " SYS_PARA "
      + " where "
      + " PARA_NAME = '" + fiName + "' ";

    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String paraValue = rs.getString(1);
        if(paraValue != null){
          result = paraValue;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  /**
   * 取得套红模板
   * @param conn
   * @param person
   * @return
   * @throws Exception
   */
  public String getWordModel(Connection conn,YHPerson person) throws Exception{
    String[] filters = null;
    YHORM orm = new YHORM();
    String result = "";
    ArrayList<YHWordModel> wmlist = (ArrayList<YHWordModel>) orm.loadListSingle(conn, YHWordModel.class, filters);
    for (YHWordModel wm : wmlist) {
      String privStr = wm.getPrivStr();
      if(isRuleWordModel(privStr,person)){
        String filed = "{attachmentId:\"" + wm.getAttachmentId() + "\""
          + ",docName:\"" + YHUtility.encodeSpecial(wm.getModelName()) + "\""
          + ",attachmentName:\"" + YHUtility.encodeSpecial(wm.getAttachmentName()) + "\"}";
        if(!"".equals(result)){
          result += ",";
        }
        result += filed;
      }
    }
    return "[" + result + "]";
  }
  
  /**
   * 判断是否在当前用户的权限范围之类
   * @param privStr
   * @param person
   * @return
   */
  public boolean isRuleWordModel(String privStr,YHPerson person){
    boolean bool = false;
    if(privStr != null && !"".equals(privStr)){
      String privDept = "";
      String privUser = "";
      String privRole = "";
      String[] privStrs = privStr.split("\\|");
      if (privStrs.length > 0 ) {
        privUser = privStrs[0];
      }
      if (privStrs.length > 1 ) {
        privDept = privStrs[1];
      }
      if (privStrs.length > 2 ) {
        privRole = privStrs[2];
      }
      if(YHNtkoCont.ALL_DEPT.equals(privDept) 
          || findId(privUser,String.valueOf(person.getSeqId()))
          || findId(privDept,String.valueOf(person.getDeptId()))
          || findId(privRole,String.valueOf(person.getUserPriv())) 
          || !YHWorkFlowUtility.checkId(privRole, person.getUserPrivOther(), true).equals("")
          || !YHWorkFlowUtility.checkId(privDept, person.getDeptIdOther(), true).equals("")
          ){
        bool = true;
      }

    }
    return bool;
  }
  
  /**
   * 查找ID
   * @param str
   * @param id
   * @return
   */
  private boolean findId(String str,String id){
    boolean bool = false;
    String[] strs = str.split(",");
    for (int i = 0; i < strs.length; i++) {
      if(id.equals(strs[i])){
        bool = true;
      }
    }
    return bool;
  }
  /**
   * 附件是否可编辑
   * @param conn
   * @param userId
   * @param attrId
   * @return
   * @throws Exception 
   */
  public String isCanEditLogic(Connection conn , int userId,String attrId) throws Exception{
    String result = "";
    String sysTimeStr = YHSysProps.getProp("$ATTACH_LOCK_REF_SEC");
    long timeTemp = 0l;
    try {
      timeTemp = Long.valueOf(sysTimeStr) ;
    } catch (Exception e) {
      timeTemp = 180;
    }
    long sysTime = (timeTemp + 10) * 1000; //毫秒数
    Date update = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Date lastVisit = null;
    int visitUserId = -1;
    String sql = "select "
      + " USER_ID "
      + " ,LAST_VISIT "
      + " FROM "
      + " oa_attachment_access "
      + " WHERE "
      + " ATTACHMENT_ID = '" + attrId + "'";
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        visitUserId = rs.getInt(1);
        lastVisit = rs.getTimestamp(2);
        Date now = new Date();
        long time = lastVisit.getTime() + sysTime;
        Date newDate = new Date(time);
        update = now;
        if(visitUserId == userId){
          //附件使用人是本人，可以编辑修改表
          updateAttrEdit(conn, attrId, userId, update);
        } else{
          //附件使用人不是本人
          if(now.after(newDate)){
            //可以编辑，修改表
            updateAttrEdit(conn, attrId, userId, update);
          }else{
            //不可以编辑
            result = "{isCanEidt:1,userId:" + visitUserId + ",sysTime:" + timeTemp  + "}";
            return result;
          }
        }
      }else{
        //可以编辑，创建记录
        saveAttrEdit(conn, attrId, userId, update);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    result = "{isCanEidt:0,userId:" + visitUserId + ",sysTime:" + timeTemp + "}";
    return result;
  }
  /**
   * 创建一条记录
   * @param conn
   * @param attrId
   * @param userId
   * @param lastVisit
   * @throws Exception
   */
  public void saveAttrEdit(Connection conn, String attrId,int userId,Date lastVisit) throws Exception{
    String sql = "insert into oa_attachment_access (ATTACHMENT_ID,USER_ID,LAST_VISIT) values(?,?,?)";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, attrId);
      ps.setInt(2, userId);
      ps.setTimestamp(3,YHUtility.parseTimeStamp(YHUtility.getDateTimeStr(lastVisit)));
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 修改一条记录
   * @param conn
   * @param attrId
   * @param userId
   * @param lastVisit
   * @throws Exception
   */
  public void updateAttrEdit(Connection conn, String attrId,int userId,Date lastVisit) throws Exception{
    String sql = "update oa_attachment_access set USER_ID = ? ,LAST_VISIT = ? where ATTACHMENT_ID ='" + attrId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userId);
      ps.setTimestamp(2,YHUtility.parseTimeStamp(lastVisit.getTime()));
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public String lockRefLogic(Connection conn , int userId,String attrId,String op) throws Exception{
    Date update = null;
    String result = "";
    String sysTimeStr = YHSysProps.getProp("$ATTACH_LOCK_REF_SEC");
    long timeTemp = 0l;
    try {
      timeTemp = Long.valueOf(sysTimeStr) ;
    } catch (Exception e) {
      timeTemp = 180;
    }
    if(op != null && "1".equals(op)){
      //用户关闭附件，更新操作
      long sysTime = (timeTemp) * 1000; //毫秒数
      update = new Date((new Date()).getTime() - sysTime);
    }else{
      update = new Date();
    }
    updateAttrEdit(conn, attrId, userId, update);
    result = "{sysTime:" + timeTemp + "}";
    return result;
  }
  /**
   * 保存附件操作日志
   * @throws Exception 
   */
  public void saveOcLog(Connection conn,int userId,String ip,String attrId,String attrName,int type) throws Exception{
     YHORM orm = new YHORM();
     YHOcLog ol = new YHOcLog();
     ol.setAttachId(attrId);
     ol.setAttachName(attrName);
     ol.setLogUid(userId);
     ol.setLogIp(ip);
     ol.setLogTime(new Date());
     ol.setLogType(type);
     orm.saveSingle(conn, ol);
     //System.out.println(ol);
  }
  public String getLogList(Connection conn,Map request) throws Exception{
    String result = "";
    String attachId = ((String[])request.get("attachId"))[0];
    String sql =  "select SEQ_ID,LOG_TIME,LOG_UID,LOG_TYPE,LOG_IP from oa_oc_log where 1=1 AND ATTACH_ID='" + attachId + "'";
    String query = " order by LOG_TIME desc ";
    sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
      YHDbRecord record = pageDataList.getRecord(i);
      String ip =(String) record.getValueByName("ip");
      if ("0:0:0:0:0:0:0:1".equals(ip)) {
        ip = "127.0.0.1";
      }
      record.updateField("ip", ip);
    }
    result = pageDataList.toJson();
    return result;
  }
  /**
   * 得到HTML头信息
   * @param fileName
   * @param directView
   * @return
   */
  public Map<String, String> getAttachHeard(String fileName ,String directView){
    HashMap<String, String> result = new HashMap<String, String>();
    String contentTypeDesc = "";
    Integer contentType = 0;
    String extName = "";
    int extNameIndex = fileName.lastIndexOf(".");
    if(extNameIndex > 0){
      extName = fileName.substring(extNameIndex).toLowerCase();
    }
    int arrayIndex = -1;
    String[] extFileArray = {".jpg",".jpeg",".bmp"
        ,".gif",".png",".html",".htm",".wmv"
        ,".wav",".mid",".mht",".pdf",".swf"};
    String[] extDocArray = {".doc",".dot",".xls"
        ,".xlc",".xll",".xlm",".xlw",".csv"
        ,".ppt",".pot",".pps",".ppz",".docx"
        ,".dotx",".xlsx",".xltx",".pptx",".potx",".ppsx",".rm",".rmvb"};
    
    if( directView != null && "1".equals(directView.trim())){
       arrayIndex = getIndex(extFileArray, extName);
       switch(arrayIndex){
         case 0:
         case 1:
                      contentType = 1;
                      contentTypeDesc  =  "image/jpeg";
                      break;
         case 2:
                      contentType = 1;
                      contentTypeDesc = "image/bmp";
                      break;
         case 3:
                      contentType = 1;
                      contentTypeDesc = "image/gif";
                      break;
         case 4:
                      contentType = 1;
                      contentTypeDesc = "image/png";
                      break;
         case 5:
         case 6:
                      contentType = 1;
                      contentTypeDesc = "text/html";
                      break;
         case 7:
         case 8:
         case 9:
         case 10:
                      contentType = 1;
                      contentTypeDesc = "application/octet-stream";
                      break;
         case 11:
                      contentType = 1;
                      contentTypeDesc = "application/pdf";
                      break;
         case 12:
                      contentType = 1;
                      contentTypeDesc = "application/x-shockwave-flash";
                      break;
         default:
                      contentType = 0;
                      contentTypeDesc = "application/octet-stream";
                      break;
       }
    } else {
      arrayIndex = getIndex(extDocArray, extName);
       switch(arrayIndex){
         case 0:
         case 1:
                      contentType =  1;
                      contentTypeDesc = "application/octet-stream";
                      break;
         case 2:    contentType = 0;
                    contentTypeDesc = "application/octet-stream";
                    break;
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
                      contentType = 1;
                      contentTypeDesc = "application/octet-stream";
                      break;
         case 8:
         case 9:
         case 10:
         case 11:
                      contentType =  0;
                      contentTypeDesc = "application/octet-stream";
                      break;
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
                      contentType =  1;
                      contentTypeDesc =  "application/octet-stream";
                      break;
         case 19:
         case 20:
                      contentType = 1;
                      contentTypeDesc = "audio/x-pn-realaudio";
                      break;
         default:
                      contentType = 0;
                      contentTypeDesc = "application/octet-stream";
                      break;
       }
    }
    result.put("contentTypeDesc",contentTypeDesc);
    result.put("contentType",contentType.toString());
    return result;
  }
  
  private int getIndex(String[] array,String indexStr){
    int result = -1;
    if(array != null){
      for (int i = 0 ; i < array.length ; i++) {
        if(array[i].equals(indexStr.trim())){
          result = i;
        }
      }
    }
    return result;
  }
  /**
   * 打包下载
   * @param map
   * @param baos
   * @return
   * @throws IOException
   */
  public OutputStream zip(Map map,OutputStream baos)
    throws IOException {
    
    InputStream inOs = null;
    ZipOutputStream zos = new ZipOutputStream(baos);
    zos.setEncoding("GBK");
    ZipEntry zipEntry = null;
    for (Object keyObj : map.keySet()) {
      String key = (String)keyObj;
      Object inputObj = map.get(key);
      if (inputObj instanceof String) {
        File inputFile = new File((String)inputObj);
        if (inputFile.isFile() && inputFile.exists() && inputFile.length() > 0) {
          inOs = new FileInputStream(inputFile);
        }
      }else {
        inOs = (InputStream)inputObj;
      }
      if (inOs == null) {
        continue;
      }
      try {
        zipEntry = new ZipEntry(key);
        zipEntry.setTime(System.currentTimeMillis());
        if(map.get(key) == null){
          zos.putNextEntry(zipEntry);
          continue;
        }
        zos.putNextEntry(zipEntry);
        
        int length = 0;
        byte[] b = new byte[4096];
        while ((length = inOs.read(b)) > 0) {
          zos.write(b, 0, length);
        }
      }catch(IOException ex) {
        throw ex;
      }finally {
        try {
          if (inOs != null) {
            inOs.close();
          }
        }catch(Exception ex) {
        }
      }
    }
    zos.flush();
    zos.close();
    return baos;
  }
  /**
   * 
   * @param attachmentName
   * @param attachmentId
   * @param module
   * @return
   * @throws Exception
   */
  public HashMap<String, String> toZipInfoMap(String attachmentName,String attachmentId,String module) throws Exception{
    HashMap<String, String> result = new HashMap<String, String>();
    String[] attachmentArray = attachmentName.split("\\*");
    String[] attachmentIdArray = attachmentId.split(",");
    HashMap<String, Integer> filesName = new HashMap<String, Integer>();
    for (int i = 0; i < attachmentIdArray.length; i++) {
      if ("".equals(attachmentIdArray[i].trim()) || "".equals(attachmentArray[i].trim()) ) {
        continue;
      }
      String attachName = attachmentArray[i].trim();
      String temp = getAttachBytes(attachName, attachmentIdArray[i].trim(), module);
      String fileName = "";
      if(temp != null){
        if(filesName.keySet().contains(attachName.trim())){
          int count = filesName.get(attachName.trim());
          String extName = attachName.substring(attachName.lastIndexOf("."), attachName.length());
          String preName = attachName.substring(0,attachName.lastIndexOf("."));
          fileName = preName + "_" + count + extName;
          filesName.put(attachName.trim(), count + 1);
        }else{
          filesName.put(attachName.trim(), 1);
          fileName = attachName;
        }
        result.put(fileName,temp);
      }
    }
    return result;
  }
  
  
  /**
   * 
   * @param fileNames
   * @param paths
   * @return
   * @throws Exception
   */
  public HashMap toZipInfoMap(String fileNames,String paths) throws Exception{
    HashMap result = new HashMap();
    if("".equals(paths.trim())){
      return result;
    }
    if(!paths.trim().endsWith("/")){
      paths += "/";
    }
    String[] fileNamesArray = fileNames.split("\\*");
    for (int i = 0; i < fileNamesArray.length; i++) {
      if ("".equals(fileNamesArray[i].trim()) || "".equals(paths.trim()) ) {
        continue;
      }
      String fileName = fileNamesArray[i].trim();

      String path = paths + fileName;
      File file = new File(path);
      if(!file.exists()){
        continue;
      }
      result.put(fileName, path);
    }
    return result;
  }
  /**
   * 
   * @param attachmentName
   * @param attachmentId
   * @param module
   * @return
   * @throws Exception
   */
  public String getAttachBytes(String attachmentName,String attachmentId,String module) throws Exception{
    String result = null;
    String path  = "";
    String fileName = "";
    File file = null;
    if(attachmentId != null && !"".equals(attachmentId)){
      if(attachmentId.indexOf("_") > 0){
        String attIds[] = attachmentId.split("_");
        fileName = attIds[1] + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH  +File.separator+ module +File.separator+ attIds[0]  +File.separator+ fileName;
      }else{
        fileName = attachmentId + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH  +File.separator+  module +File.separator+  fileName;
      }
      
      file = new File(path);
      if(!file.exists()){
        if(attachmentId.indexOf("_") > 0){
          String attIds[] = attachmentId.split("_");
          fileName = attIds[1] + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH +File.separator+  module  +File.separator+  attIds[0]  +File.separator+  fileName;
        }else{
          fileName = attachmentId + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH  +File.separator+  module  +File.separator+  fileName;
        }
        file = new File(path);
      }
      if(!file.exists()){
        return result;
      }
      //this.fileName = fileName;
     String trushAttachName = "";
     result = path;
    }
    return result;
  }
  public void downFileByUrl(){
    
  }
  public void downFileByLocal(){
    
  }
}

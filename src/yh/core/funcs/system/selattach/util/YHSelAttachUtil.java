package yh.core.funcs.system.selattach.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.file.YHFileUploadForm;

public class YHSelAttachUtil {
  private static String file_moudel = "file_folder";
  private String attachIdInfo = "";
  private String attachNameInfo = "";
  private HashMap<String,String> attachInfo = new HashMap<String,String>();
  public static final String ATTACH_NAME = "ATTACH_NAME";
  public static final String ATTACH_DIR = "ATTACH_DIR";
  public static final String DISK_ID = "DISK_ID";
  private int attachCnt ;
  private YHSelAttachUtil(){}
  /**
   * 构造方法
   * @param fuf
   * @param moudel
   * @throws Exception 
   */
  public YHSelAttachUtil(YHFileUploadForm fuf,String moudel) throws Exception{
    try{
    String  attachName = fuf.getParameter(ATTACH_NAME);
    String  attachId = fuf.getParameter(ATTACH_DIR);
    String  attachDiskId = fuf.getParameter(DISK_ID);
    init(attachName, attachId, attachDiskId, moudel);
    }catch (Exception e) {
      throw new Exception("所选公共文件柜/网络硬盘文件不存在，请联系管理员!");
    }
  }
  /**
   * 构造方法
   * @param fuf
   * @param moudel
   * @throws Exception
   */
  public YHSelAttachUtil(Map<String, String> fuf,String moudel) throws Exception{
    init(fuf, moudel);
  }
  /**
   * 构造方法  
   * @param request
   * @param moudel
   * @throws Exception
   */
  public YHSelAttachUtil(HttpServletRequest request,String moudel) throws Exception{
     Map mp = request.getParameterMap();
     init(mp, moudel);
  }
  /**
   * 初始化方法
   * @param mp
   * @param moudel
   * @throws Exception
   */
  private void init(Map mp,String moudel) throws Exception{
    String  attachName = "";
    String  attachId = "";
    String  attachDiskId = "";
    try {
      attachName =  (String) mp.get(ATTACH_NAME);
      attachId = (String) mp.get(ATTACH_DIR);
      attachDiskId = (String) mp.get(DISK_ID);
    } catch (Exception e) {
      attachName =  ((String[]) mp.get(ATTACH_NAME))[0];
      attachId = ((String[]) mp.get(ATTACH_DIR))[0];
      attachDiskId = ((String[]) mp.get(DISK_ID))[0];
    }
   init(attachName, attachId, attachDiskId, moudel);
  }
  /**
   * 初始化方法
   * @param attachName
   * @param attachId
   * @param attachDiskId
   * @param moudel
   * @throws Exception
   */
  private void init(String attachName,String attachId,String attachDiskId,String moudel) throws Exception{
    if(attachName == null || "".equals(attachName.trim())
        || attachId == null || "".equals(attachId.trim())
        || attachId == null || "".equals(attachDiskId.trim())){
      return;
    }
    String[] attachNames = split(attachName, "*");
    String[] attachIds = split(attachId, "*");
    String[] diskIds = split(attachDiskId, "*");
    for (int i = 0; i < diskIds.length; i++) {
      if("".equals(diskIds[i])){
        saveFileByFileFolder(attachNames[i], attachIds[i], moudel);
      }else{
        saveFileByDisk(attachNames[i], attachIds[i], diskIds[i], moudel);
      }
    }
    listToString(attachInfo, null);
  }
  /**
   * 从网络硬盘中选
   * @param attachName
   * @param attachId
   * @param diskId
   * @param moudel
   * @throws Exception 
   */
  private void saveFileByDisk(String attachName,String path,String diskId,String moudel) throws Exception{
    if(!path.endsWith( File.separator ) && !path.endsWith("//")){
      path += File.separator ;
    }
    String filePath = path + attachName;
    String copyPath = YHSysProps.getAttachPath();
    if(!copyPath.endsWith( File.separator )&& !copyPath.endsWith("//")){
      copyPath +=  File.separator ;
    }
    String tureCopypath = "";
    String hard = "";
    Calendar cld = Calendar.getInstance();
    int year =  cld.get(Calendar.YEAR)%100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10?month+"":"0"+month;
    hard = year + mon ;
    String rand = YHGuid.getRawGuid(); 
    String trusFileName = rand + "." + attachName;
    tureCopypath += copyPath + moudel +   File.separator  + hard + File.separator + trusFileName;
    attachInfo.put(hard + "_" + rand , attachName);
    saveFile(filePath, tureCopypath);
  }
  /**
   * 从公共/个人文件柜中选
   * @param attachName
   * @param attachId
   * @param moudel
   * @throws Exception 
   */
  private void saveFileByFileFolder(String attachName,String attachId,String moudel) throws Exception{
    String path = YHSysProps.getAttachPath();
    String filePath = "";
    String copypath = "";
    String tureFilePath = "";
    String tureCopypath = "";
    if(moudel == null || "".equals(moudel)){
      return;
    }
    if(!path.endsWith( File.separator )&& !path.endsWith("//")){
      path +=  File.separator ;
    }
    filePath += path + file_moudel;
    copypath += path + moudel;
    String truePath = "";
    String relx = ".";
    String hard = "";
    if(attachId != null && !"".equals(attachId)){
      if(attachId.indexOf("_") > 0){
        String attIds[] = attachId.split("_");
        hard = attIds[1];
        tureFilePath = filePath +  File.separator  + attIds[0] + File.separator  ;
      }else{
        hard = attachId;
        tureFilePath = filePath +  File.separator  ;
      }
      truePath = tureFilePath + hard + relx + attachName;
      File file = new File(truePath);
      if(!file.exists()){
        relx = "_";
        truePath = tureFilePath + hard + relx + attachName;
      }
      Calendar cld = Calendar.getInstance();
      int year =  cld.get(Calendar.YEAR)%100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10?month+"":"0"+month;
      hard = year + mon ;
      String rand = YHGuid.getRawGuid(); 
      String trusFileName = rand + "." + attachName;
      tureCopypath += copypath +  File.separator  + hard + File.separator  + trusFileName;
      attachInfo.put(hard + "_" + rand , attachName);
    }
    saveFile(truePath, tureCopypath);
  }
  /**
   * 保存文件
   * @param filePath
   * @param copypath
   * @throws Exception
   */
  private void saveFile(String filePath,String copypath) throws Exception{
    if (filePath == null) {
      return;
    }
    if (copypath == null) {
      return;
    }
    File fileItem = new File(filePath);
    File outFile = new File(copypath);
    File outPath = outFile.getParentFile();
    if (!outPath.exists()) {
      outPath.mkdirs();
    }
    if (!outFile.exists()) {
      outFile.createNewFile();
    }
    InputStream in = null;
    OutputStream out = null;
    try {
      in = new BufferedInputStream(new FileInputStream(fileItem));
      out = new BufferedOutputStream(
          new FileOutputStream(outFile));
      byte[] buff = new byte[YHConst.M];
      int length = 0;
      while ((length = in.read(buff)) > 0) {
        out.write(buff, 0, length);
        out.flush();
      }
    }catch(IOException ex) {
      throw ex;
    }finally {
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {        
      }
      try {
        if (out != null) {
          out.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  /**
   * 字符串分解成字符数组
   * @param spment
   * @param reg
   * @return
   */
  private static String[] split(String spment,String reg){
    
    ArrayList<String> temp = new ArrayList<String>();
    int length = spment.length();
    int start = 0;
    for (int i = 0; i < length; ) {
      int end = spment.indexOf(reg,start);
      if(start == end){
        temp.add("");
      }else if(end == -1){
        //System.out.println(end);
        String sub = spment.substring(start, spment.length());
        temp.add(sub);
        break;
      }else{
        String sub = spment.substring(start, end);
        temp.add(sub);
      }
      //System.out.println(temp);
      start = end + 1;
      i = start;
    }
    String[] result = new String[temp.size()];
    //spment.indexOf(reg);
    return temp.toArray(result);
  }
  /**
   * 得到附件数
   * @return
   */
  public int getAttachCnt(){
    int result = 0;
    if(attachInfo != null){
      result = attachInfo.size();
    }
    return result;
  }
  /**
   * 得到附件的ID串
   * @param reg
   * @return
   */
  public String getAttachIdToString(String reg){
    return attachIdInfo;
  }
  /**
   *得到附件name串
   * @param reg
   * @return
   */
  public String getAttachNameToString(String reg){
    return attachNameInfo;
  }
  
  public HashMap<String, String> getAttachInFo(){
    return attachInfo;
  }
  /**
   * 根据reg分隔符将list转换成特定的字符串
   * @param list
   * @param reg
   * @return
   */
  private void listToString(HashMap<String, String> hm,String reg){
    StringBuffer result = new StringBuffer();
    if(hm == null){
      return;
    }else{
      Set<String> keyset = hm.keySet();
      for (String key : keyset) {
         String attName = hm.get(key);
         if(!"".equals(attachIdInfo)){
           attachIdInfo += ",";
           attachNameInfo += "*";
         }
         attachIdInfo += key;
         attachNameInfo += attName;
      }
    }
  }
}

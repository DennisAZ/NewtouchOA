package yh.core.funcs.system.ispirit.n12.file.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.news.data.YHNewsCont;
import yh.core.funcs.office.ntko.data.YHNtkoCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.n12.file.data.YHImOfflineFile;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;

public class YHImOffLineLogic {

  /**
   * 处理上传附件，返回附件id，附件名称


  //点击单文件上传时调用的方法

   * @param request  HttpServletRequest
   * @param 
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception 
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm,
      String pathPx) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String filePath = pathPx;
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName).replaceAll("\\'", "");
        //转换编码
      //  fileName= YHUtility.transferCode(fileName, "GBK", "UTF-8");
       //  fileName = new String(fileName.getBytes("ISO-8859-1"),"UTF-8");        
        
        String fileNameV = fileName;
     //   System.out.println(fileName+"*************"+fileNameV);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        
        while (YHDiaryUtil.getExist(filePath + File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, filePath + File.separator +"im" + File.separator + hard + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  /**
   * 
   * 添加离线文件
   * 
   * */
  
  public String addOffLineFile(Connection conn,YHPerson person,String destUid,String attrId,String attrName,String fileSize)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String maxId="";
    try{
      YHImOfflineFile ImOffLineFile=new YHImOfflineFile();
      ImOffLineFile.setDestUid(destUid);
      attrId=attrId.replace(",", "");
      attrName=attrName.replace("*", "");
      ImOffLineFile.setFileName("*"+attrId+"."+attrName); 
      ImOffLineFile.setFile_size(Integer.parseInt(fileSize));
      ImOffLineFile.setFlag(0);
      ImOffLineFile.setSrcUid(person.getSeqId());
      ImOffLineFile.setTime(new Date());
      YHORM orm = new YHORM();
      orm.saveSingle(conn, ImOffLineFile);  
      maxId=this.getMaxId(conn, " select max(id) from oa_im_offline_attach ");
    }catch(Exception e){
      e.printStackTrace();
    }
    return maxId;
  }
  
  /**
   *获取最大ID
   */
  public String getMaxId(Connection conn ,String sql)throws Exception{
    String id="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        id=rs.getString(1);
      }
      
    }catch(Exception e){
      e.printStackTrace();
    }
    
    
    return id;
  }
  
  /**
   * 获取文件路径
   */
  public String getFileName(Connection conn,YHPerson person,String fId)throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String filePath="";
    try{
      String sql=" select FILE_NAME from oa_im_offline_attach where ID='"+fId+"' and DEST_UID='"+person.getSeqId()+"' ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        filePath=rs.getString("FILE_NAME");
      }
      filePath=getFilePath(filePath);
    }catch(Exception e){
      e.printStackTrace();
    }
    return filePath;
  }
  
  public String getFilePath(String filePath){
    if(YHUtility.isNullorEmpty(filePath)){
      return "";
    }
    String attId="";
    String attName="";
    if("*".equals(filePath.substring(0, 1))){
      attId=filePath.substring(1, filePath.indexOf("."));
      attName=filePath.substring(filePath.indexOf(".")+1, filePath.length());
    }
    
    String fileName="";
    String path  = "";
    if(attId != null && !"".equals(attId)){
      if(attId.indexOf("_") > 0){
        String attIds[] = attId.split("_");
        fileName = attIds[1] + "_"+ attName;
        path = YHNtkoCont.ATTA_PATH +File.separator+ "im"  +File.separator+  attIds[0]  + File.separator+  fileName;
      }else{
        fileName = attId +  "_" + attName;
        path = YHNtkoCont.ATTA_PATH  +File.separator+ "im"  +File.separator+  fileName;
      }
      
      File file = new File(path);
      if(!file.exists()){
        if(attName.indexOf("_") > 0){
          String attIds[] = attId.split("_");
          fileName = attIds[1] + "_" + attName;
          path = YHNtkoCont.ATTA_PATH  +File.separator+  "im"  +File.separator+  attIds[0]  +File.separator+  fileName;
        }else{
          fileName = attId + "_" + attName;
          path = YHNtkoCont.ATTA_PATH  +File.separator+ "im"  +File.separator+  fileName;
        }
        file = new File(path);
      }
     
      filePath=path;
    }
    
     return filePath;
  }
  
}

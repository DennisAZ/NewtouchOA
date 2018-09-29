package yh.core.funcs.office.ntko.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import srvSeal.SrvSealUtil;

public class YHNtkoStream {
  private String attachmentName;
  private String attachmentId;
  private String module;
  private String fileName;
  private Long fileSize = 0l;
  public String getModule() {
    return module;
  }
  public void setModule(String module) {
    this.module = module;
  }
  private FileInputStream fio;
  public String getAttachmentName() {
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
//    try {
//      attachmentName = new String(attachmentName.getBytes("ISO-8859-1"), "UTF-8");
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
    if(attachmentName.trim().endsWith("*")){
      attachmentName = attachmentName.trim().substring(0,attachmentName.trim().length() - 1);
    }
    this.attachmentName = attachmentName;
  }
  public String getAttachmentId() {
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    if(attachmentId.trim().endsWith(",")){
      attachmentId = attachmentId.trim().substring(0,attachmentId.trim().length() - 1);
    }
    this.attachmentId = attachmentId;
  }
  public FileInputStream getFileStream() throws FileNotFoundException {
    String path  = "";
    if(attachmentId != null && !"".equals(attachmentId)){
      if(attachmentId.indexOf("_") > 0){
        String attIds[] = attachmentId.split("_");
        fileName = attIds[1] + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH +File.separator+ module  +File.separator+  attIds[0]  + File.separator+  fileName;
      }else{
        fileName = attachmentId + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH  +File.separator+ module  +File.separator+  fileName;
      }
      
      File file = new File(path);
      if(!file.exists()){
        if(attachmentId.indexOf("_") > 0){
          String attIds[] = attachmentId.split("_");
          fileName = attIds[1] + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH  +File.separator+  module  +File.separator+  attIds[0]  +File.separator+  fileName;
        }else{
          fileName = attachmentId + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH  +File.separator+ module  +File.separator+  fileName;
        }
        file = new File(path);
      }
      if(!file.exists()){
        return null;
      }
      //this.fileName = fileName;
      this.fileSize = file.length();
      fio = new FileInputStream(file);
    }
    return fio;
  }
  
  public FileInputStream getFileStreamWord() throws FileNotFoundException {
    String path  = "";
    if(attachmentId != null && !"".equals(attachmentId)){
      if(attachmentId.indexOf("_") > 0){
        String attIds[] = attachmentId.split("_");
        fileName = attIds[1] + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH +File.separator+ module  +File.separator+  attIds[0]  + File.separator+  fileName;
      }else{
        fileName = attachmentId + "." + attachmentName;
        path = YHNtkoCont.ATTA_PATH  +File.separator+ module  +File.separator+  fileName;
      }
      File file = new File(path);
      if(!file.exists()){
        if(attachmentId.indexOf("_") > 0){
          String attIds[] = attachmentId.split("_");
          fileName = attIds[1] + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH  +File.separator+  module  +File.separator+  attIds[0]  +File.separator+  fileName;
        }else{
          fileName = attachmentId + "_" + attachmentName;
          path = YHNtkoCont.ATTA_PATH  +File.separator+ module  +File.separator+  fileName;
        }
        file = new File(path);
      }
      if(!file.exists()){
        return null;
      }
      String newPath = null;
      path = path.toLowerCase();
      if (path.endsWith(".doc") || path.endsWith("docx")) {
        SrvSealUtil ssu = new SrvSealUtil();
        int nObjID = ssu.openObj("", 0, 0);
        ssu.login(nObjID, 4, "HWSEALDEMOXX", "");
        ssu.addPage(nObjID, path, "");
        int tmpIndex = path.lastIndexOf(".docx");
        if (tmpIndex < 0) {
          tmpIndex = path.lastIndexOf(".doc");
        }
        newPath = path.substring(0, tmpIndex) + "_2.doc";
        ssu.saveFile(nObjID, newPath, "doc");
      }
      if (newPath != null) {
        File newFile = new File(newPath);
        this.fileSize = newFile.length();
        fio = new FileInputStream(newFile);
      }else {
        this.fileSize = file.length();
        fio = new FileInputStream(file);
      }
    }
    return fio;
  }
  
  public String getFileName() {
    return fileName;
  }
  public Long getFileSize() {
    return fileSize;
  }
  @Override
  public String toString() {
    return "YHNtkoStream [attachmentId=" + attachmentId + ", attachmentName="
        + attachmentName + ", fileName=" + fileName + ", fileSize=" + fileSize
        + ", fio=" + fio + ", module=" + module + "]";
  }
  
}

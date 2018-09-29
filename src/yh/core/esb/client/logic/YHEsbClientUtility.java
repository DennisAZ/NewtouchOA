package yh.core.esb.client.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;

public class YHEsbClientUtility {
  public static void output(InputStream in ,  org.apache.tools.zip.ZipOutputStream out  , String fileName) {
    byte[] buf = new byte[1024];
    try {
      org.apache.tools.zip.ZipEntry ss =  new org.apache.tools.zip.ZipEntry(fileName);
      out.putNextEntry(ss);
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      out.closeEntry();
      out.flush();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static String[] getNewAttachPath(  String fileName ,String module) throws Exception {
    String pathPx = YHSysProps.getAttachPath();
    String filePath = pathPx + File.separator + module;
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    String trusFileName = "";
    String attachmentId = YHGuid.getRawGuid();
    trusFileName = attachmentId + "_" + fileName;
    String trusPath = filePath + File.separator +  hard + File.separator + trusFileName;
    File storeDir = new File(filePath + File.separator +  hard);
    if (!storeDir.exists()) {
      storeDir.mkdirs();
    }
    String attId =  hard + "_" + attachmentId;
    String[] atts = {attId , trusPath};
    return atts;
  }
  public static String getAttachPath(String aId , String aName  , String moduleDesc) throws Exception {
    String pathPx = YHSysProps.getAttachPath();
    String filePath = pathPx + File.separator +  moduleDesc;
    int index = aId.indexOf("_");
    String hard = "";
    String str = "";
    if (index > 0) {
      hard = aId.substring(0, index);
      str = aId.substring(index + 1);
    } else {
      hard = "all";
      str = aId;
    }
    String path = filePath + File.separator +  hard + File.separator +  str + "_" + aName;
    return path;
  }
  public static Map<String , ByteArrayBuffer> getFileList(String path) throws Exception {
    //FileInputStream in = new FileInputStream(new File(path));
    Map<String , ByteArrayBuffer> map = new HashMap();
    ZipFile zipFile = new ZipFile(path);
    InputStream inputStream = null;
    for(Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();){ 
      ZipEntry entry = (ZipEntry)entries.nextElement(); 
      if(entry.isDirectory()){ 
      } else {
        inputStream = zipFile.getInputStream(entry); 
        ByteArrayBuffer bab = new ByteArrayBuffer(inputStream.available());
        byte[] tmp = new byte[1024];
        for (int i = 0; (i = inputStream.read(tmp)) > 0;) {
          bab.append(tmp, 0, i);
        }
        map.put(entry.getName(), bab);
      }
    }
    if (inputStream != null) {
      inputStream.close();
    }
    zipFile.close();
    return map;
  }
  public static void main(String args[]) throws Exception {
    String path = "D:\\cache\\1205_4d60e76c4c773599becd9c96faa70607.zip";
    String file = "D:\\cache";
    Map<String , ByteArrayBuffer> map = getFileList(path);
    for (String ss : map.keySet()) {
      String filepath = file + File.separator + ss;
      File file1 = new File(filepath);
      ByteArrayBuffer bab = map.get(ss);
      writeFile(file1 , bab.toByteArray());
    }
    
  }
   static public void writeFile(File file ,  byte[] bts) throws IOException, InterruptedException {
    FileOutputStream out = new FileOutputStream(file);
    out.write(bts);
    out.flush();
    out.close();
  }
}

package yh.core.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import yh.core.data.YHAuthKeys;
import yh.core.exps.YHInvalidParamException;
import yh.core.global.YHConst;
import yh.core.util.YHSecurityUtility;
import yh.core.util.YHUtility;

public class YHZipFileUtility {
 
  /**
   * 把指定目录压缩成zip文件
   * @param srcFile         被压缩的文件或者文件包完全路径名称
   * @param destFile        输出的压缩文件完全路径名称
   * @throws Exception
   */
  public static void doZip(String srcFile,
      String destFile) throws Exception {
    
    doZip(srcFile, destFile, null, null, 0, 0);
  }
  
  /**
   * 把指定目录压缩成zip文件
   * @param srcFile         被压缩的文件或者文件包完全路径名称
   * @param out             输出的压缩文件输出流
   * @throws Exception
   */
  public static void doZip(String srcFile,
      OutputStream out) throws Exception {

    ZipOutputStream zipOut = new ZipOutputStream(out);
    zipOut.setEncoding("GBK");
    doZip(srcFile, zipOut, null, null, 0, 0);
  }
  
  /**
   * 把指定目录压缩成zip文件
   * @param srcFile         被压缩的文件或者文件包完全路径名称
   * @param destFile        输出的压缩文件完全路径名称
   * @throws Exception
   */
  public static void doCiphZip(String srcFile,
      String destFile) throws Exception {
    
    String fileName = YHFileUtility.getFileNameNoExt(destFile);
    doCiphZip(srcFile, destFile,
        fileName.toCharArray(),
        YHAuthKeys.getSalt(null),
        YHAuthKeys.getItCnt(null),
        Cipher.ENCRYPT_MODE);
  }
  /**
   * 把指定目录压缩成zip文件
   * @param srcFile         被压缩的文件或者文件包完全路径名称
   * @param destFile        输出的压缩文件完全路径名称
   * @throws Exception
   */
  public static void doCiphZip(String srcFile,
      String destFile,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    if (passWord == null || passWord.length < 1) {
      throw new YHInvalidParamException("没有传递密码！");
    }
    doZip(srcFile, destFile, passWord, salt, itCnt, mode);
  }
  
  /**
   * 把指定目录压缩成zip文件
   * @param srcFile         被压缩的文件或者文件包完全路径名称
   * @param destFile        输出的压缩文件完全路径名称
   * @throws Exception
   */
  private static void doZip(String srcFile,
      String destFile,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    ZipOutputStream out = null;
    try {
      File file = new File(destFile);  
      File outDir = file.getParentFile();
      if (!outDir.exists()) {
        outDir.mkdirs();
      }
      if (!file.exists()) {
        file.createNewFile();
      }
      out = new ZipOutputStream(new FileOutputStream(destFile));
      out.setEncoding("GBK");
      doZip(srcFile,
          out,
          passWord,
          salt,
          itCnt,
          mode);
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      }catch(Exception ex) {        
      } 
    }
  }
  
  /**
   * 把指定目录压缩成zip文件
   * @param srcFile         被压缩的文件或者文件包完全路径名称
   * @param destFile        输出的压缩文件完全路径名称
   * @throws Exception
   */
  private static void doZip(String srcFile,
      ZipOutputStream out,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    try {
      File file = new File(srcFile);
      if (!file.exists()) {
        return;
      }
      File[] fileList = file.listFiles();
      for (int i = 0; i < fileList.length; i++) {
        putFile2Zip(fileList[i],
            out,
            fileList[i].getName(),
            passWord,
            salt,
            itCnt,
            mode);
      }
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 在zip输出流中增加文件
   * @param srcFile
   * @param out
   * @throws Exception
   */
  private static void putFile2Zip(
      File srcFile,
      ZipOutputStream out,
      String base,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {

    base = YHUtility.null2Empty(base);

    if (srcFile.isFile()) {
      InputStream in = null;
      try {
        ZipEntry entry = 
          new ZipEntry(base);
        entry.setMethod(ZipEntry.DEFLATED);
        out.putNextEntry(entry);
        in = new FileInputStream(srcFile);
        if (passWord != null) {
          in = YHSecurityUtility.buildPassWordInputStream(
              passWord, salt, itCnt, mode,
              in);
        }
        byte[] buff = new byte[1024];
        int readLength = 0;
        while ((readLength = in.read(buff)) > 0) {
          out.write(buff, 0, readLength);
          out.flush();
        }
      }finally {
        try {
          if (in != null) {
            in.close();
          }
        }catch(Exception ex) {        
        }        
      }
    }else if (srcFile.isDirectory()) {
      ZipEntry entry = new ZipEntry(
          base + "/");
      out.putNextEntry(entry);
      base = base.length() == 0 ? "" : base + "/";
      File[] fileList = srcFile.listFiles();
      for (int i = 0; i < fileList.length; i++) {
        putFile2Zip(fileList[i],
            out,
            base + fileList[i].getName(),
            passWord, salt, itCnt, mode);
      }
    }
  }
  
  /**
   * 测试zip包中是否存在指定路径的文件
   * @param srcFile
   * @param entryPath
   * @return
   * @throws Exception
   */
  public static boolean isExistsEntry(String srcFile,
      String entryPath) throws Exception {

    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(new File(srcFile));
      ZipEntry entry = zipFile.getEntry(entryPath);
      if (entry == null) {
        return false;
      }
      return true;
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (zipFile != null) {
        zipFile.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  
  /**
   * 在压缩包中提取文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZipFile(String srcFile,
      String desDir,
      ArrayList validExtList,
      String acceptFile) throws Exception {

    List acceptFileList = new ArrayList();
    acceptFileList.add(acceptFile);
    FileFilter filter = new YHNameFileFilter(acceptFileList, true);
    unZipCiphFile(srcFile, desDir, filter, validExtList, null, null, 0, 0);
  }
  
  /**
   * 在压缩包中提取文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZipFile(String srcFile,
      String desDir,
      ArrayList validExtList,
      ArrayList acceptFileList) throws Exception {

    FileFilter filter = new YHNameFileFilter(acceptFileList, true);
    unZipCiphFile(srcFile, desDir, filter, validExtList, null, null, 0, 0);
  }
    
  /**
   * 解压缩文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZipFile(String srcFile,
      String desDir,
      ArrayList validExtList) throws Exception {

    unZipCiphFile(srcFile, desDir, validExtList, null, null, 0, 0);
  }
  
  /**
   * 解压缩文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZipCiphFile(String srcFile,
      String destDir) throws Exception {
    
    String fileName = YHFileUtility.getFileNameNoExt(srcFile);
    YHZipFileUtility.unZipCiphFile(
        srcFile, destDir, null,
        fileName.toCharArray(),
        YHAuthKeys.getSalt(null),
        YHAuthKeys.getItCnt(null),
        Cipher.DECRYPT_MODE);
  }
  
  /**
   * 解压缩文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZipCiphFile(String srcFile,
      String destDir,
      ArrayList validExtList,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    unZipCiphFile(srcFile, destDir, null, validExtList, passWord, salt, itCnt, mode);
  }
  
  /**
   * 解压缩文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZipCiphFile(
      String srcFile,
      String destDir,
      FileFilter nameFilter, 
      ArrayList validExtList,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    BufferedOutputStream dest = null;
    InputStream in = null;    
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(new File(srcFile));
      java.util.Enumeration e = zipFile.getEntries();
      
      ZipEntry entry = null;
      while(e.hasMoreElements()) {
        entry = (ZipEntry)e.nextElement();
        String zipFileName = entry.getName();
        
        if (entry.isDirectory()) {
          continue;
        }
        
        if (nameFilter != null && !nameFilter.accept(new File(zipFileName))) {
          continue;
        }
        
        int count;
        byte data[] = new byte[YHConst.DEFAULT_IO_BUFF_SIZE];
        
        String fileOutName = destDir + YHFileConst.PATH_SPLIT_FILE_WIN
          + YHFileUtility.unix2Windows(zipFileName);
        String filePath = YHFileUtility.getFilePath(fileOutName)
          + YHFileConst.PATH_SPLIT_FILE_WIN;
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
          fileDir.mkdirs();
        }
        String extName = YHFileUtility.getFileExtName(fileOutName);
        if (validExtList != null 
            && !validExtList.contains(extName)
            && !validExtList.contains(extName.toUpperCase())) {
//          System.out.println(extName);
          continue;
        }
        File file = new File(fileOutName);
        if (!file.exists()) {
          file.createNewFile();
        }
        in = zipFile.getInputStream(entry);
        if (passWord != null) {
          in = YHSecurityUtility.buildPassWordInputStream(
              passWord, salt, itCnt, mode, in);
        }
        
        FileOutputStream fos = new FileOutputStream(file);
        dest = new BufferedOutputStream(fos, YHConst.DEFAULT_IO_BUFF_SIZE);
        while ((count = in.read(data, 0, YHConst.DEFAULT_IO_BUFF_SIZE)) > 0) {
          dest.write(data, 0, count);
          dest.flush();
        }
        in.close();
        dest.close();
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (zipFile != null) {
          zipFile.close();
        }
      }catch(Exception ex) {        
      }
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {        
      }
      try {
        if (dest != null) {
          dest.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  
  /**
   * 把zip包中的指定文件加载到哈希表中
   * @param zipFileName            zip文件全路径
   * @param dataFilePath           指定文件在zip包中的路径
   * @return
   * @throws Exception
   */
  public static Map loadzipFile2Props(String zipFileName,
      String dataFilePath) throws Exception {
    
    Map rtMap = new HashMap();
    
    List strList = loadzipFile2List(zipFileName, dataFilePath);
    for (int i = 0; i < strList.size(); i++) {
      String line = (String)strList.get(i);
      if (line.trim().length() < 1 || line.trim().startsWith("#")) {
        continue;
      }
      String[] propArray = line.split("=");
      rtMap.put(propArray[0].trim(), propArray[1].trim());
    }
    
    return rtMap;
  }
  
  /**
   * 把zip包中的指定文件加载到列表中

   * @param zipFileName               zip文件全路径

   * @param dataFilePath              指定文件在zip包中的路径

   * @return
   * @throws Exception
   */
  public static List loadzipFile2List(String zipFileName,
      String dataFilePath,
      String fileNameEncode,
      String contentEncode) throws Exception {
    
    return loadZipFile2List(zipFileName, dataFilePath, null, null, 0, 0, fileNameEncode, contentEncode);
  }
  /**
   * 把zip包中的指定文件加载到列表中
   * @param zipFileName               zip文件全路径
   * @param dataFilePath              指定文件在zip包中的路径
   * @return
   * @throws Exception
   */
  public static List loadzipFile2List(String zipFileName,
      String dataFilePath) throws Exception {
    
    return loadZipFile2List(zipFileName, dataFilePath, null, null, 0, 0);
  }
  
  /**
   * 把zip包中的指定文件加载到列表中

   * @param zipFileName                  zip文件全路径

   * @param dataFilePath                 指定文件在zip包中的全路径
   * @param passWord                     加密/解密用密码，如果=null则不加密/解密
   * @param salt                         加密/解密用干扰

   * @param itCnt                        加密/解密用迭代次数

   * @param mode                         加密/解密用模式

   * @return
   * @throws Exception
   */
  private static List loadZipFile2List(
      String zipFileName,
      String dataFilePath,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    return loadZipFile2List(zipFileName, dataFilePath, passWord, salt, itCnt, mode, null, null);
  }
  
  /**
   * 把zip包中的指定文件加载到列表中
   * @param zipFileName                  zip文件全路径
   * @param dataFilePath                 指定文件在zip包中的全路径
   * @param passWord                     加密/解密用密码，如果=null则不加密/解密
   * @param salt                         加密/解密用干扰
   * @param itCnt                        加密/解密用迭代次数
   * @param mode                         加密/解密用模式
   * @return
   * @throws Exception
   */
  private static List loadZipFile2List(
      String zipFileName,
      String dataFilePath,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode,
      String fileNameEncode,
      String contentEncode) throws Exception {
    
    List rtList = new ArrayList();
    
    InputStream in = null; 
    BufferedReader reader = null;
    ZipFile zipFile = null;
    try {
      if (YHUtility.isNullorEmpty(fileNameEncode)) {
        zipFile = new ZipFile(new File(zipFileName));
      }else {
        zipFile = new ZipFile(new File(zipFileName), fileNameEncode);
      }
      
      ZipEntry zipEntry = zipFile.getEntry(dataFilePath);
      if (zipEntry == null) {
        return rtList;
      }
      in = zipFile.getInputStream(zipEntry);
      if (passWord != null) {
        in = YHSecurityUtility.buildPassWordInputStream(
            passWord, salt, itCnt, mode, in);
      }
      reader = new BufferedReader(new InputStreamReader(in, contentEncode));
      String line = null;
      while ((line = reader.readLine()) != null) {
        rtList.add(line);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (zipFile != null) {
          zipFile.close();
        }
      }catch(Exception ex) {        
      }
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {        
      }
    }
    return rtList;
  }
}



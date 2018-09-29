package yh.core.util.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class YHZipFileTool {
  
  /**
   * 把指定目录压缩成zip文件
   * @param srcFile         被压缩的文件或者文件包完全路径名称
   * @param destFile        输出的压缩文件完全路径名称
   * @throws Exception
   */
  public static void doZip(String srcFile,
      String destFile) throws Exception {
    
    OutputStream outStream = null;
    ZipOutputStream out = null;
    try {
      outStream = new FileOutputStream(destFile);
      File file = new File(srcFile);
      if (!file.exists()) {
        return;
      }
      out = new ZipOutputStream(outStream);
      out.setEncoding("GBK");
      File[] fileList = file.listFiles();
      for (int i = 0; i < fileList.length; i++) {
        putFile2Zip(fileList[i],
            out,
            fileList[i].getName());
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      }catch(Exception ex) {        
      } 
      try {
        if (outStream != null) {
          outStream.close();
        }
      }catch(Exception ex) {
        
      }
    }
  }
  
  /**
   * 解压缩文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZip(
      String srcFile,
      String destDir) throws Exception {
    unZip(srcFile, destDir, null, null);
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
      String base) throws Exception {

    base = base == null ? "" : base;

    if (srcFile.isFile()) {
      InputStream in = null;
      try {
        ZipEntry entry = 
          new ZipEntry(base);
        entry.setMethod(ZipEntry.DEFLATED);
        out.putNextEntry(entry);
        in = new FileInputStream(srcFile);
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
            base + fileList[i].getName());
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
   * 解压缩文件，仅支持zip格式, 能够处理中文
   * @param srcFile         源文件，zip压缩格式
   * @param desDir          目标目录
   * @param validExtList    合法的文件名后缀，null:=不控制
   * @throws Exception
   */
  public static void unZip(
      String srcFile,
      String destDir,
      FileFilter nameFilter, 
      ArrayList validExtList) throws Exception {
    
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
        byte data[] = new byte[1024 * 768];
        
        String fileOutName = destDir + "\\"
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
        if (!file.canWrite()) {
          file.delete();
        }
        in = zipFile.getInputStream(entry);
        
        FileOutputStream fos = new FileOutputStream(file);
        dest = new BufferedOutputStream(fos, 1024 * 10);
        while ((count = in.read(data, 0, 1024 * 10)) > 0) {
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
}

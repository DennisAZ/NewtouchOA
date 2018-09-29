package yh.core.util.file;

import java.io.File;
import java.io.FileFilter;

public class YHFileDifFilter implements FileFilter {
  /**
   * 新目录
   */
  private String newDir = null;
  /**
   * 原目录
   */
  private String oldDir = null;
  
  /**
   * 构造函数
   * @param newDir
   * @param oldDir
   */
  public YHFileDifFilter(String newDir, String oldDir) {
    this.newDir = newDir;
    this.oldDir = oldDir;
  }
  
  /**
   * 是否选择该文件
   */
  public boolean accept(File file) {
    try {
      File oldFile= new File(
          oldDir + file.getAbsolutePath().substring(newDir.length()));
      if (!oldFile.exists()) {
        return true;
      }
      return !YHFileUtility.isFileEqual(file, oldFile);
    }catch(Exception ex) {
      return false;
    }
  }
}

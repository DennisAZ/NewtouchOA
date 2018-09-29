package yh.core.util.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

/**
 * 按时间过滤的文件过滤器
 * @author jpt
 * @version 1.0
 * @date 2007-2-13
 */
public class YHTimeFileFilter implements FileFilter {
  /** 参与比较的时间 **/
  private Date cpTime = null;
  /** 时间区间 **/
  private boolean acceptNew = true;
  
  /**
   * 构造函数
   * @param cpTime               比较时间
   * @param acceptNew            选择新文件标志true=选择日期更新的文件；false=选择日期更旧的文件
   */
  public YHTimeFileFilter(Date cpTime, boolean acceptNew) {
    this.cpTime = cpTime;
    this.acceptNew = acceptNew;
  }
  
  /**
   * 是否选择该文件
   */
  public boolean accept(File file) {
     if (acceptNew) {
       if (file.lastModified() > cpTime.getTime()) {
         return true;
       }
       return false;
     }
     if (file.lastModified() > cpTime.getTime()) {
       return false;
     }
     return true;
  }
}

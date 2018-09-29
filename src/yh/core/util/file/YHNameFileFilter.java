package yh.core.util.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class YHNameFileFilter implements FileFilter {
  /**
   * 过滤字符串列表
   */
  private List filters = new ArrayList();
  
  private boolean isAcceptFilters = false;
  
  /**
   * 构造方法
   * @param filters
   * @param isAcceptFilters
   */
  public YHNameFileFilter(List filters, boolean isAcceptFilters) {
    this.filters.addAll(filters);
    this.isAcceptFilters = isAcceptFilters;
  }
  
  /**
   * 是否选择该文件
   */
  public boolean accept(String fileName) {
    if (filters.contains(fileName)) {
      if (isAcceptFilters) {
        return true;
      }else {
        return false;
      }
    }else {
      if (isAcceptFilters) {
        return false;
      }else {
        return true;
      }
    }
  }
  
  /**
   * 是否选择该文件
   */
  public boolean accept(File file) {
    return accept(file.getName());
  }
}

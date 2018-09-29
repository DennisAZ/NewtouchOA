package yh.subsys.inforesouce.db;

import yh.core.util.YHUtility;

/**
 * 大于过滤器
 * @author jpt
 *
 */
public class YHMetaGTFilter implements YHIMetaFilter {
  private String exprStr = null;
  /**
   * 解析表达式
   * @param exprStr
   */
  public void parse(String exprStr) {
    this.exprStr = exprStr;
  }
  /**
   * 是否匹配
   * @param valueStr
   * @return
   */
  public boolean isMatch(String valueStr) {
    if (YHUtility.isNullorEmpty(this.exprStr)) {
      return true;
    }
    if (valueStr == null) {
      valueStr = "";
    }
    return valueStr.compareToIgnoreCase(this.exprStr) >= 0;
  }
}

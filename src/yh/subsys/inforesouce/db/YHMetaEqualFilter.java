package yh.subsys.inforesouce.db;

import yh.core.util.YHUtility;

/**
 * 等值过滤器
 * @author jpt
 *
 */
public class YHMetaEqualFilter implements YHIMetaFilter {
//表达式字符串
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
    return this.exprStr.equalsIgnoreCase(valueStr);
  }
}

package yh.subsys.inforesouce.db;

/**
 * 元数据筛选器
 * @author jpt
 *
 */
public interface YHIMetaFilter {
  /**
   * 解析表达式
   * @param exprStr
   */
  public void parse(String exprStr);
  /**
   * 是否匹配
   * @param valueStr
   * @return
   */
  public boolean isMatch(String valueStr);
}

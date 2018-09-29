package yh.core.exps;

/**
 * 无效的数据表异常
 * @author jpt
 * @version 1.0
 * @date 2006-8-14
 */
public class YHInvalidTableException extends Exception {
  /**
   * 构造方法
   * @param msrg
   */
  public YHInvalidTableException(String msrg) {
    super(msrg);
  }
}

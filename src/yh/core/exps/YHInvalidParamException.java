package yh.core.exps;

/**
 * 无效的参数异常
 * @author jpt
 * @version 1.0
 * @date 2006-8-14
 */
public class YHInvalidParamException extends Exception {
  
  /**
   * 构造方法
   * @param msrg
   */
  public YHInvalidParamException(String msrg) {
    super(msrg);
  }
}

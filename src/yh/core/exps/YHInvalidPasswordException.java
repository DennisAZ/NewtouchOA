package yh.core.exps;

/**
 * 非法的用户密码异常
 * @author jpt
 * @version 1.0
 * @date 2006-7-31
 */
public class YHInvalidPasswordException extends Exception {
  /**
   * 构造方法
   * @param msrg
   */
  public YHInvalidPasswordException(String msrg) {
    super(msrg);
  }
}

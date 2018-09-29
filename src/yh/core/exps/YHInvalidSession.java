package yh.core.exps;

import javax.servlet.jsp.JspException;

public class YHInvalidSession extends JspException {
  /**
   * 构造方法
   * @param msrg
   */
  public YHInvalidSession(String msrg) {
    super(msrg);
  }
}

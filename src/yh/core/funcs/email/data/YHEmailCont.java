package yh.core.funcs.email.data;

import yh.core.global.YHSysProps;

public class YHEmailCont{
  private static String upload;
  static {
    upload = YHSysProps.getAttachPath();
  }
  public static final String IMGSRC = "";
  public static final String EMAIL_HOME_ACT = "/yh/yh/core/funcs/email/act/YHInnerEMailAct/";
  public static final String EMAIL_HOME_UTILACT = "/yh/yh/core/funcs/email/act/YHInnerEMailUtilAct/";
  public static final String UPLOAD_HOME = upload;
  public static final String MODULE = "email";
  public static final int webLimit = 10;//M
}

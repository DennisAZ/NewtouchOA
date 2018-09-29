package yh.core.funcs.system.act.common;

import yh.oa.tools.StaticData;

public class YHLoginErrorConst {
  
  public static final String LOGIN_NOTEXIST_USER = "不存在用户";
  public static final int LOGIN_NOTEXIST_USER_CODE = 0;
  
  public static final String LOGIN_FORBID_LOGIN = "用户禁止登录";
  public static final int LOGIN_FORBID_LOGIN_CODE = 1;
  
  public static final String LOGIN_IP_RULE_LIMIT = "登陆IP不符合IP规则";
  public static final int LOGIN_IP_RULE_LIMIT_CODE = 2;
  
  public static final String LOGIN_BIND_IP = "登陆IP和用户绑定IP不符";
  public static final int LOGIN_BIND_IP_CODE = 3;
  
  public static final String LOGIN_USBKEY_ERROR = "使用USBKEY登录失败";
  public static final int LOGIN_USBKEY_ERROR_CODE = 4;
  
  public static final String LOGIN_PASSWORD_ERROR = "密码错误";
  public static final int LOGIN_PASSWORD_ERROR_CODE = 5;
  
  public static final String LOGIN_INITIAL_PW = "修改初始密码";
  public static final int LOGIN_INITIAL_PW_CODE = 6;
  
  public static final String LOGIN_PW_EXPIRED = "密码过期";
  public static final int LOGIN_PW_EXPIRED_CODE = 7;
  
  public static final String LOGIN_RETRY_ERROR = "重新登录错误";
  public static final int LOGIN_RETRY_ERROR_CODE = 8;
  
  public static final String REPEAT_LOGIN_ERROR = "用户已经登录,无法重复登录";
  public static final int REPEAT_LOGIN_ERROR_CODE = 9;
  
  public static final String LOGIN_PASSWORD_ERROR_SECURE_CARD_1 = "动态加密字符串有错";
  public static final int LOGIN_PASSWORD_ERROR_CODE_SECURE_CARD_1 = 10;
  
  public static final String LOGIN_PASSWORD_ERROR_SECURE_CARD_2 = "动态密码未知内部错误";
  public static final int LOGIN_PASSWORD_ERROR_CODE_SECURE_CARD_2 = 11;
  
  public static final String LOGIN_PASSWORD_ERROR_SECURE_CARD_3 = "动态密码错误";
  public static final int LOGIN_PASSWORD_ERROR_CODE_SECURE_CARD_3 = 12;
  
  public static final String VERIFICATION_CODE_ERROR = "验证码错误";
  public static final int VERIFICATION_CODE_CODE = 14;
  
  public static final String SOFTWARE_EXPIRED = "已经超过最大免费试用期限！\\n继续使用，请联系"+StaticData.SOFTCOMPANY+"。";
  public static final int SOFTWARE_EXPIRED_CODE = 13;
}

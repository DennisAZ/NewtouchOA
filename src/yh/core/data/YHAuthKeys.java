package yh.core.data;

import java.util.Map;

public class YHAuthKeys {
  /**
   * 机器码
   */
  public static final String MACHINE_CODE = "machineCode";
  /**
   * 软件唯一标识序列号，技术支持用于检查客户的合法性
   */
  public static final String SERIAL_ID = "serial.id";
  /**
   * 系统名称
   */
  public static final String SYS_NAME = "sysname";
  /**
   * YH平台安装时间
   */
  public static final String INSTALL_TIME = "install.time";
  /**
   * 注册给机构名称
   */
  public static final String REGIST_ORG = "regist.org";
  /**
   * 系统用户数
   */
  public static final String USER_CNT = "user.cnt";
  /**
   * 系统-YH
   */
  public static final String SYS_YH = "yh";

  /**
   * 属性签名名称-用于注册
   */
  public static final String REGIST_PROPKEY_DIGIST_POSTFIX = ".Didgist";
  /**
   * 取得加密密码
   * @param params     用于取得密码的参数，用于扩展用
   * @return
   */
  public static char[] getPassword(Map params) {
    return "BLMDfSiSEUSeRnwxL89HnBbCUgBsYBjDbvHJGA==".toCharArray();
  }
  
  /**
   * 取得MD5算法干扰字节长度
   * @param params     用于取得MD5算法干扰字节长度，用于扩展用
   * @return
   */
  public static int getMD5SaltLength(Map params) {
    return 12;
  }
  
  /**
   * 取得Salt
   * @param params     用于取得Salt的参数，用于扩展用
   * @return
   */
  public static byte[] getSalt(Map params) { 
    byte[] salt = new byte[]{
        (byte)1, (byte)2, (byte)3, (byte)4, 
        (byte)5, (byte)6, (byte)7, (byte)8};
    return salt;
  }
  /**
   * 取得迭代次数
   * @param params     用于取得迭代次数的参数，用于扩展用
   * @return
   */
  public static int getItCnt(Map params) {
    return 3;
  }
  /**
   * 取得最大试用天数
   * @param params     用于最大试用天数的参数，用于扩展用
   * @return
   */
  public static int getMaxEvalueDayCnt(Map params) {
    return 100;
  }
}

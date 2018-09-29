package yh.core.global;

import yh.core.util.YHUtility;

/**
 * 系统通用常量定义
 * @author jpt
 * @version 1.0
 * @date 2007-1-30
 */
public class YHConst {
  
  /**
   * 秒
   */
  public static final long DT_S = 1000;
  /**
   * 分
   */
  public static final long DT_MINIT = DT_S * 60;
  /**
   * 时
   */
  public static final long DT_H = DT_MINIT * 60;
  /**
   * 天
   */
  public static final long DT_D = DT_H * 24;
  /**
   * 月
   */
  public static final long DT_MONTH = DT_D * 30;
  /**
   * 年
   */
  public static final long DT_Y = DT_D * 365;
  /**
   * 1k
   */
  public static final int K = 1024;
  /**
   * 1M
   */
  public static final int M = K * K;
  /**
   * 1G
   */
  public static final int G = M * M;
  /**
   * 1T
   */
  public static final int T = G * G;
  /**
   * 64K
   */
  public static final int K64 = K * 64;
  /**
   * 最大一个2字节无符号数字
   */
  public static final int MAX_2_BYTES = K64 - 1;
  
  /**
   * 中文编码名称
   */
  public static final String CHINA_CODE_NAME = "GBK";
  /**
   * 缺省编码
   */
  public static final String DEFAULT_CODE = YHUtility.isNullorEmpty(YHSysProps.getProp(YHSysPropKeys.DEFAULT_CODE)) ? "UTF-8" : YHSysProps.getProp(YHSysPropKeys.DEFAULT_CODE);
  /**
   * csv文件导入导出编码
   */
  public static final String CSV_FILE_CODE = YHUtility.isNullorEmpty(YHSysProps.getProp("csvFileCode")) ? "GBK" : YHSysProps.getProp("csvFileCode");
  
  /**
   * 通用代码维护path前缀
   */
  public static final String COMMON_CODE_PATH_PREFIX = "basecode";
  
  /**
   * 错误返回
   */
  public static final String RETURN_ERROR = "1";
  /**
   * 正确返回
   */
  public static final String RETURN_OK = "0";
  
  /**
   * 第三类数值返回(半对)(正确执行,但是未完全执行)
   */
  public static final String RETURN_MIDDLE = "-1";
  
  /**
   * 一般返回
   */
  public static final String RETURNCHECK = "returnCheck";
  /**
   * AJAX返回
   */
  public static final String RETURN_JSON = "returnJson";
  /**
   * json返回
   */
  public static final String RET_TYPE_JSON = "json";
  /**
   * XML返回
   */
  public static final String RET_TYPE_XML = "xml";
  
  
  /**
   * 常量-是
   */
  public static final String YES = "1";
  /**
   * 常量-否
   */
  public static final String NO = "0";
  /**
   * 默认输入输出缓冲大小
   */
  public static final int DEFAULT_IO_BUFF_SIZE = K * 10;
 
  /**
   * 打开方式定义-默认，到主客户区
   */
  public static final String OPEN_MANNER_CLIENT_MAIN = "0";
  /**
   * 打开方式定义-默认，弹开新窗口
   */
  public static final String OPEN_MANNER_POPNEW = "1";
  /**
   * 是否用作北方海外系统-是
   */
  public static final String USED_FOR_NORINCO_YES = "1";
  /**
   * 终端业务是否与总帐集成-是 0=不生成凭证;1=工具栏显示生成凭证/调阅凭证按钮;2=工具栏不显示生成凭证/调阅凭证按钮
   */
  public static final String RUN_WITH_VOUCH_DISPONTOOLBAR = "1";
  /**
   * 是否允许文件上传-允许
   */
  public static final String ALLOW_FILE_UPLOAD_NOT_YES = "0";
  /**
   * 是否允许文件上传-显示控件，但不允许上传
   */
  public static final String ALLOW_FILE_UPLOAD_NOT_NO = "1";
  /**
   * 是否允许文件上传-不显示控件
   */
  public static final String ALLOW_FILE_UPLOAD_NOT_DISP = "2";
  /**
   * 数据库管理系统-MSSqlServer
   */
  public static final String DBMS_SQLSERVER = "sqlserver";
  /**
   * 数据库管理系统-MySql
   */
  public static final String DBMS_MYSQL = "mysql";
  /**
   * 数据库管理系统-Oracle
   */
  public static final String DBMS_ORACLE = "oracle";
  /**
   * 系统登陆用户session里的关键字 --- add by lh 1月22日
   */
  public static final String LOGIN_USER = "LOGIN_USER";
  
  /**
   * UsbKey 的系统识别码
   */
  public static final String KEY_TD_SIGN = "C2C238A0";
}

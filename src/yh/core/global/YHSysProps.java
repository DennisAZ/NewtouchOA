package yh.core.global;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import yh.core.util.YHUtility;

/**
 * 系统属性 
 * @author jpt
 * @version 1.0
 * @date 2006-8-3
 */
public class YHSysProps {
  private static String adminName = null;
  //全局的配置属性信息，这样的信息应该尽量的少，因为需要常驻内存
  private static Properties props = null;
  //操作系统名字
  private static String osName = System.getProperty("os.name");
  
  /**
   * 取得系统配置属性
   * @param key
   * @return
   */
  public static String getProp(String key) {
    if (props == null) {
      return "";
    }
    String prop = (String)props.getProperty(key);
    if (prop != null) {
      prop = prop.trim();
    }else {
      prop = "";
    }
    return prop;
  }
  
  /**
   * 取得字符串类型的值
   * @param key
   * @return
   */
  public static String getString(String key) {
    return getProp(key);
  }
  
  /**
   * 取得整型值
   * @param key
   * @return
   */
  public static int getInt(String key) {
    int rtInt = 0;
    try {
      String strValue = getString(key);
      if (!YHUtility.isNullorEmpty(strValue)) {
        rtInt = Integer.parseInt(strValue);
      }
    }catch(Exception ex) {
    }
    return rtInt;
  }
  
  /**
   * 取得长整型值
   * @param key
   * @return
   */
  public static long getLong(String key) {
    long rtLong = 0;
    try {
      String strValue = getString(key);
      if (!YHUtility.isNullorEmpty(strValue)) {
        rtLong = Long.parseLong(strValue);
      }
    }catch(Exception ex) {      
    }
    return rtLong;
  }

  /**
   * 设置系统配置属性
   * @param props
   */
  public static void setProps(Properties props) {
    YHSysProps.props = props;
  }
  
  public static void updateProp(Object key, Object value) {
    YHSysProps.props.put(key, value);
  }
  
  /**
   * 设置系统配置属性
   * @param props
   */
  public static void addProps(Map aProps) {
    if (aProps == null) {
      return;
    }
    Iterator iKeys = aProps.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = (String)iKeys.next();
      String value = (String)aProps.get(key);
      props.put(key, value);
    }
  }
  
  /**
   * 获取数据库ID字段的名称
   * @return
   */
  public static String getIdName() {
    String fieldName = getString(YHSysPropKeys.ID_FIELD_NAME);
    if (YHUtility.isNullorEmpty(fieldName )) {
      return "SEQ_ID";
    }else {
      return fieldName;
    }
  }
  
  /**
   * 取得安装路径
   * @return
   */
  public static String getRootPath() {
    return getString(YHSysPropKeys.ROOT_DIR);
  }
  
  /**
   * 取得WEB-INF物理路径
   * @return
   */
  public static String getWebPath() {
    return getString(YHSysPropKeys.ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.WEB_ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.JSP_ROOT_DIR);
  }
  /**
   * 取得WEB-INF物理路径
   * @return
   */
  public static String getWebInfPath() {
    return getString(YHSysPropKeys.ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.WEB_ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.JSP_ROOT_DIR)
      + File.separator + "WEB-INF";
  }
  
  /**
   * 取得系统管理员的名字
   * @return
   */
  public static String getAdminName() {
    if (adminName != null) {
      return adminName;
    }
    adminName = getString(YHSysPropKeys.SYS_ADMIN_NAME);
    if (YHUtility.isNullorEmpty(adminName)) {
      adminName = "admin";
    }
    return adminName;
  }
  
  /**
   * 取得WEB-INF物理路径
   * @return
   */
  public static String getWebInfPathRela() {
    return getString(YHSysPropKeys.WEB_ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.JSP_ROOT_DIR)
      + File.separator + "WEB-INF";
  }
  
  /**
   * 取得Styles物理路径
   * @return
   */
  public static String getStylePath() {
    return getString(YHSysPropKeys.ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.WEB_ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.JSP_ROOT_DIR)
      + File.separator + "core" + File.separator + "styles";
  }
  /**
   * 取得UI配置物理路径
   * @return
   */
  public static String getUiPath() {
    return getString(YHSysPropKeys.ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.WEB_ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.JSP_ROOT_DIR)
      + File.separator + "core" + File.separator + "ui";
  }
  /**
   * 取得UI模板配置物理路径
   * @return
   */
  public static String getUiTemplatePath() {
    return getString(YHSysPropKeys.ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.WEB_ROOT_DIR)
      + File.separator + getString(YHSysPropKeys.JSP_ROOT_DIR)
      + File.separator + "core" + File.separator + "templates";
  }
  
  /**
   * 取得Log配置文件路径
   * @return
   */
  public static String getLogConfigPath() {
    return getWebInfPath()
      + File.separator + "classes" + File.separator + "log4j.properties";
  }
  /**
   * 取得系统静态配置文件路径
   * @return
   */
  public static String getSysConfigPath() {
    return getWebInfPath()
      + File.separator + "config" + File.separator + "sysconfig.properties";
  }
  /**
   * 取得系统数据库名称
   * @return
   */
  public static String getSysDbName() {
    String sysDbName = getString(YHSysPropKeys.SYS_DB_NAME);
    if (sysDbName == null) {
      sysDbName = "TDSYS";
    }
    return sysDbName;
  }

  /**
   * 取得系统库数据源名称
   * @return
   */
  public static String getSysDbDsName() {
    String sysDbDsName = getString(YHSysPropKeys.DBCONN_DATASOURCE_SYS_DS_NAME);
    if (sysDbDsName == null) {
      sysDbDsName = "mssql/sys/TDSYS";
    }
    return sysDbDsName;
  }
  /**
   * 取得附件上传缓存路径
   * @return
   */
  public static String getUploadCatchPath() {
    String cachPath = getString(YHSysPropKeys.FILE_UPLOAD_TEMP_DIR);
    if (cachPath == null) {
      cachPath = getString(YHSysPropKeys.ROOT_DIR) + File.separator + "catch";
    }
    if (cachPath.indexOf(":") == 1 || cachPath.indexOf("/") == 0) {
      return cachPath;
    }
    //相对路径
    cachPath = getString(YHSysPropKeys.ROOT_DIR + File.separator + cachPath);
    return cachPath;
  }
  /**
   * 取得附件上传路径
   * @return
   */
  public static String getAttachPath() {
    String attachPath = getString(YHSysPropKeys.ATTACH_FILES_PATH);
    if (attachPath == null) {
      attachPath = getString(YHSysPropKeys.ROOT_DIR) + File.separator + "attach";
    }
    if (attachPath.indexOf(":") == 1 || attachPath.indexOf("/") == 0) {
      return attachPath;
    }
    //相对路径
    attachPath = getString(YHSysPropKeys.ROOT_DIR) + File.separator + attachPath;
    return attachPath;
  }
  /**
   * 取得限制上传的文件定义
   * @return
   */
  public static String getLimitUploadFiles() {
    String allowUploadFiles = getString(YHSysPropKeys.LIMIT_UPLOAD_FILES);
    if (allowUploadFiles == null) {
      allowUploadFiles = "";
    }
    return allowUploadFiles;
  }
  
  /**
   * 获取前缀的子表
   * @return
   */
  public static Map startsWith(String prefix) {
    return YHUtility.startsWithMap(props, prefix);
  }
  /**
   * 获取后缀的子表
   * @return
   */
  public static Map endsWith(String postFix) {
    return YHUtility.endsWithMap(props, postFix);
  }
  
  public static boolean isWindows() {
    return YHUtility.null2Empty(osName).toLowerCase().indexOf("windows") >= 0;
  }
  public static boolean isLinux() {
    return YHUtility.null2Empty(osName).toLowerCase().indexOf("linux") >= 0;
  }
}

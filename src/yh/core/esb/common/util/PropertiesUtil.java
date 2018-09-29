package yh.core.esb.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;


import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

public class PropertiesUtil {
 
  private static Properties props = new Properties();
  /**
   * 客户端获取系统配置属性   * @param request
   * @param props
   * @return
   */
  private static void getClientProperties(){
    try{
      FileInputStream fis = new FileInputStream(new File(YHSysProps.getWebPath() + "\\WEB-INF\\config\\esbconfig.properties"));
      Reader re = new InputStreamReader(fis , "UTF-8");
      props.load(re);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
 
  /**
   * 客户端获取系统配置属性
   * @param request
   * @param props
   * @return
   */
  public static void store(){
    try{
      FileOutputStream fos = new FileOutputStream(new File(YHSysProps.getWebPath() + "\\WEB-INF\\config\\esbconfig.properties"));
      props.store(fos, "esbconfig.properties");
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  /**
   * 取得系统配置属性   * @param key
   * @return
   */
  public static String getProp(String key) {
    if (props.isEmpty()) {
      getClientProperties();
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
   * 设置系统配置属性


   * @param props
   */
  public static void setProps(Properties props) {
    PropertiesUtil.props = props;
  }
  
  public static void updateProp(Object key, Object value) {
    PropertiesUtil.props.put(key, value);
  }
  
  
  /**
   * 刷新系统配置属性   * @return
   */ 
  public static void refresh(){
    getClientProperties();
  }
  
  /**
   * 获取单个上传或上载并发数
   * @return
   */
  public static int getPerConcurrencyLimits() {
    String s = getProp("PER_CONCURRENCY_LIMITS");
    if (YHUtility.isNullorEmpty(s)) {
      return 10;
    }
    return Integer.parseInt(s);
  }
  
  public static long getUploadPartSize() {
    String size = getProp("UPLOAD_PART_SIZE");
    if (YHUtility.isNullorEmpty(size) || !YHUtility.isNumber(size)) {
      return 1024 * 10;
    }
    return Long.parseLong(size);
  }
  
  public static long getDownloadPartSize() {
    String size = getProp("DOWNLOAD_PART_SIZE");
    if (YHUtility.isNullorEmpty(size) || !YHUtility.isNumber(size)) {
      return 1024 * 10;
    }
    return Long.parseLong(size);
  }
  
  public static String getUploadPath() {
    String dir = getProp("UPLOAD_CACHE_DIR");
    if (YHUtility.isNullorEmpty(dir)) {
      dir = "d:\\httpclient\\uploads";
    }
    return dir;
  }
  public static long getMaxUploadTime() {
    String uploadTime = getProp("MAX_UPLOAD_TIME");
    long time = 1000 * 60 * 60;
    if (!YHUtility.isNullorEmpty(uploadTime)) {
      time = Long.parseLong(uploadTime);
    }
    return time;
  }
  public static long getMaxDownloadTime() {
    String uploadTime = getProp("MAX_DOWNLOAD_TIME");
    long time = 1000 * 60 * 60;
    if (!YHUtility.isNullorEmpty(uploadTime)) {
      time = Long.parseLong(uploadTime);
    }
    return time;
  }
}

package yh.core.esb.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import org.apache.http.HttpHost;

import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

public class ClientPropertiesUtil {
   
  private static Properties props = new Properties();
  
  
  /**
   * 客户端配置属性   * @param request
   * @param props
   * @return
   */
  private static void getClientProperties(){
    try{
      FileInputStream fis = new FileInputStream(new File(YHSysProps.getWebPath() + "\\WEB-INF\\config\\esbselfconfig.properties"));
      Reader re = new InputStreamReader(fis , "UTF-8");
      props.load(re);
    }catch(Exception e){
      //e.printStackTrace();
    }
  }
  
  /**
   * 取得客户端属性

   * @param key
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
    ClientPropertiesUtil.props = props;
  }
  
  public static void updateProp(Object key, Object value) {
    ClientPropertiesUtil.props.put(key, value);
  }
  
  /**
   * 刷新客户端配置属性

   * @return
   */ 
  public static void refresh(){
    getClientProperties();
  }
  
  /**
   * 将系统配置属性写入到配置文件

   * @param request
   * @param props
   * @return
   * @throws Exception 
   */
  public static void store() {
    Writer wr =  null;
    try{
      FileOutputStream fos = new FileOutputStream(new File(YHSysProps.getWebPath() + "\\WEB-INF\\config\\esbselfconfig.properties"));
     wr = new OutputStreamWriter(fos , "UTF-8");
     props.store(fos, "esbselfconfig.properties");
    }catch(Exception e){
      e.printStackTrace();
    } finally  {
      try {
        if(wr != null ) {
          wr.flush();
          wr.close();
        }
      }catch(Exception e){
        e.printStackTrace();
      }
    }
  }
  
  /**
   * 获取缓存目录地址
   * @return
   */
  public static String getCacheDir() {
    String dir = getProp("cacheDir");
    if (YHUtility.isNullorEmpty(dir)) {
      dir = "C:\\ESB-CACHE\\";
    }
    return dir;
  }
  
  public static int getHostPort() {
    String s = getProp("port");
    if (YHUtility.isNullorEmpty(s) || !YHUtility.isNumber(s)) {
      return 80;
    }
    return Integer.parseInt(s);
  }
  
  public static String getHost() {
    return getProp("host");
  }
  
  public static String getWebServiceUri() {
    String s = getProp("webserviceUri");
    if (YHUtility.isNullorEmpty(s)) {
      return "http://127.0.0.1/webservice/server.php/ESBMessage?WSDL";
    }
    return s;
  }
  
  public static HttpHost getHttpHost() {
    try {
      return new HttpHost(getHost(), getHostPort());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

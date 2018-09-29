package yh.core.esb.client.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;

import yh.core.util.YHUtility;

public class YHEsbClientConfig {
  
  public String getESBHOST() {
    return ESBHOST;
  } 
  public void setESBHOST(String esbhost) {
    ESBHOST = esbhost;
  }
  public String getESBPORT() {
    return ESBPORT;
  }
  public void setESBPORT(String esbport) {
    ESBPORT = esbport;
  }
  public String getOAHOST() {
    return OAHOST;
  }
  public void setOAHOST(String oahost) {
    OAHOST = oahost;
  }
  public String getOAPORT() {
    return OAPORT;
  }
  public void setOAPORT(String oaport) {
    OAPORT = oaport;
  }
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  public String getESBSERVER() {
    return ESBSERVER;
  }
  public void setESBSERVER(String esbserver) {
    ESBSERVER = esbserver;
  }
  public String getESBSERVERPORT() {
    return ESBSERVERPORT;
  }
  public void setESBSERVERPORT(String esbserverport) {
    ESBSERVERPORT = esbserverport;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getCachePath() {
    return cachePath;
  }
  public void setCachePath(String cachePath) {
    this.cachePath = cachePath;
  }
  public String getWS_PATH() {
    WS_PATH = "http://" + this.ESBHOST + ":" + this.ESBPORT + "/yh/services/YHEsbService";
    return WS_PATH;
  }
  public void setWS_PATH(String ws_path) {
    WS_PATH = ws_path;
  }
  public static YHEsbClientConfig getConfig() {
    return config;
  }
  public static void setConfig(YHEsbClientConfig config) {
    YHEsbClientConfig.config = config;
  }
  
  private String local;
  public String getLocal() {
    return local;
  }
  public void setLocal(String string) {
    local = string;
  }

  private String ESBHOST;
  private String ESBPORT;
  private String OAHOST;
  private String OAPORT;
  private String token;
  
  private String ESBSERVER;
  private String ESBSERVERPORT;
  private String password;
  private String userId;
  private String cachePath;
  private String WS_PATH;
  private String webserviceUri;
  
  public String getWebserviceUri() {
    webserviceUri = "http://" + this.OAHOST + ":" + this.OAPORT + "/yh/services/OAWebservice";
    return webserviceUri;
  }
  public void setWebserviceUri(String webserviceUri) {
    this.webserviceUri = webserviceUri;
  }
  public void load(String path) throws FileNotFoundException, IOException {
    Properties p = new Properties();
    Reader rd = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");  
    p.load(rd);
    this.ESBHOST = p.getProperty(YHEsbConst.ESB_HOST);
    this.ESBPORT = p.getProperty(YHEsbConst.ESB_PORT);
    if (YHUtility.isNullorEmpty(ESBPORT)){
      this.ESBPORT = "8089";
    }
    
    this.OAHOST = p.getProperty(YHEsbConst.OA_HOST);
    this.OAPORT = p.getProperty(YHEsbConst.OA_PORT);
    if (YHUtility.isNullorEmpty(OAPORT)){
      this.OAPORT = "80";
    }
    this.token = p.getProperty(YHEsbConst.TOKEN);
    
    this.ESBSERVER =  p.getProperty(YHEsbConst.ESB_SERVER_HOST);
    this.ESBSERVERPORT =  p.getProperty(YHEsbConst.ESB_SERVER_PORT);
    if (YHUtility.isNullorEmpty(ESBSERVERPORT)){
      this.ESBSERVERPORT = "8088";
    }
    this.password =  p.getProperty(YHEsbConst.PASSWORD);
    this.userId =  p.getProperty(YHEsbConst.USER_ID);
    this.cachePath =  p.getProperty(YHEsbConst.CACHE_PATH);
    this.local = p.getProperty(YHEsbConst.IS_LOCAL);
    if (YHUtility.isNullorEmpty(this.local)  ) {
      this.local = "0";
    }
    p.clear();
    rd.close();
  }
  public void store(String path)throws Exception {
    Properties p = new Properties();
    p.put(YHEsbConst.ESB_HOST, this.ESBHOST);
    p.put(YHEsbConst.ESB_PORT, this.ESBPORT);
    p.put(YHEsbConst.ESB_SERVER_HOST, this.ESBSERVER);
    p.put(YHEsbConst.ESB_SERVER_PORT, this.ESBSERVERPORT);
    p.put(YHEsbConst.USER_ID, this.userId);
    p.put(YHEsbConst.OA_HOST, this.OAHOST);
    p.put(YHEsbConst.OA_PORT, this.OAPORT);
    p.put(YHEsbConst.PASSWORD, this.password);
    p.put(YHEsbConst.TOKEN, this.token);
    p.put(YHEsbConst.CACHE_PATH, this.cachePath);
    p.put(YHEsbConst.IS_LOCAL, this.local);
    FileOutputStream fo =  new FileOutputStream(new File(path));
    Writer wr = new OutputStreamWriter(fo , "UTF-8");
    p.store(wr,new Date().toString());
    wr.flush();
    wr.close();
    p.clear();
  }
  public static YHEsbClientConfig config = null; 
  public static YHEsbClientConfig builder(String path) throws Exception {
    if (config == null ) {
      config = new YHEsbClientConfig();
      config.load(path);
    }
    return config;
  }
}

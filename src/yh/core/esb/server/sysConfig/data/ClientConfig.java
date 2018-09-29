package yh.core.esb.server.sysConfig.data;

public class ClientConfig {
  
  private String host = "";
  private String username = "";
  private String password = "";
  private String cacheDir = "";
  private String port = "";
  public String getHost() {
    return host;
  }
  public void setHost(String host) {
    this.host = host;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getCacheDir() {
    return cacheDir;
  }
  public void setCacheDir(String cacheDir) {
    this.cacheDir = cacheDir;
  }
  public String getPort() {
    return port;
  }
  public void setPort(String port) {
    this.port = port;
  }
  
  
}

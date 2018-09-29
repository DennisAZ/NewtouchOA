package yh.core.esb.server.sysConfig.data;

public class SysConfig {

  private String maxUploadTime = "";
  private String maxDownloadTime = "";
  private String uploadCacheDir = "";
  private String uploadPartSize = "";
  private String downloadPartSize = "";
  
  public String getMaxUploadTime() {
    return maxUploadTime;
  }
  public void setMaxUploadTime(String maxUploadTime) {
    this.maxUploadTime = maxUploadTime;
  }
  public String getMaxDownloadTime() {
    return maxDownloadTime;
  }
  public void setMaxDownloadTime(String maxDownloadTime) {
    this.maxDownloadTime = maxDownloadTime;
  }
  public String getUploadCacheDir() {
    return uploadCacheDir;
  }
  public void setUploadCacheDir(String uploadCacheDir) {
    this.uploadCacheDir = uploadCacheDir;
  }
  public String getUploadPartSize() {
    return uploadPartSize;
  }
  public void setUploadPartSize(String uploadPartSize) {
    this.uploadPartSize = uploadPartSize;
  }
  public String getDownloadPartSize() {
    return downloadPartSize;
  }
  public void setDownloadPartSize(String downloadPartSize) {
    this.downloadPartSize = downloadPartSize;
  }
  
  
}

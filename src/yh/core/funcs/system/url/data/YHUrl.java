package yh.core.funcs.system.url.data;

import java.util.ArrayList;
import java.util.Iterator;

import yh.core.data.YHDsField;

public class YHUrl {
  private int seqId;
  private int urlNo;
  private String urlDesc;
  private String url;
  private String user;
  private String urlType;
  private String subType;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getUrlNo() {
    return urlNo;
  }
  public void setUrlNo(int urlNo) {
    this.urlNo = urlNo;
  }
  public String getUrlDesc() {
    return urlDesc;
  }
  public void setUrlDesc(String urlDesc) {
    this.urlDesc = urlDesc;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getUser() {
    return user;
  }
  public void setUser(String user) {
    this.user = user;
  }
  public String getUrlType() {
    return urlType;
  }
  public void setUrlType(String urlType) {
    this.urlType = urlType;
  }
  public String getSubType() {
    return subType;
  }
  public void setSubType(String subType) {
    this.subType = subType;
  }
}

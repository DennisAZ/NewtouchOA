package yh.core.funcs.system.sealmanage.data;

import java.util.ArrayList;
import java.util.Date;

public class YHSealLog {
  private int seqId;
  private String sId;
  private String userId;
  private Date logTime;
  private String logType;
  private String result;
  private String macAdd;
  private String ipAdd;
  private String clientType;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getSId() {
    return sId;
  }
  public void setsId(String sId) {
    this.sId = sId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public Date getLogTime() {
    return logTime;
  }
  public void setLogTime(Date logTime) {
    this.logTime = logTime;
  }
  public String getLogType() {
    return logType;
  }
  public void setLogType(String logType) {
    this.logType = logType;
  }
  public String getResult() {
    return result;
  }
  public void setResult(String result) {
    this.result = result;
  }
  public String getMacAdd() {
    return macAdd;
  }
  public void setMacAdd(String macAdd) {
    this.macAdd = macAdd;
  }
  public String getIpAdd() {
    return ipAdd;
  }
  public void setIpAdd(String ipAdd) {
    this.ipAdd = ipAdd;
  }
  public String getClientType() {
    return clientType;
  }
  public void setClientType(String clientType) {
    this.clientType = clientType;
  }
}

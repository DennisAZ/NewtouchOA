package yh.core.funcs.seclog.data;

import java.util.Date;

public class YHSeclog {
  
  private int seqId;
  private String userSeqId;
  private Date opTime;
  private String clientIp;
  private String opType;
  private String opObject;
  private String opDesc;
  private String userName;
  private String opResult;
  
  
  public String getOpResult() {
    return opResult;
  }
  public void setOpResult(String opResult) {
    this.opResult = opResult;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserSeqId() {
    return userSeqId;
  }
  public void setUserSeqId(String userSeqId) {
    this.userSeqId = userSeqId;
  }
  public Date getOpTime() {
    return opTime;
  }
  public void setOpTime(Date opTime) {
    this.opTime = opTime;
  }
  public String getClientIp() {
    return clientIp;
  }
  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }
  public String getOpType() {
    return opType;
  }
  public void setOpType(String opType) {
    this.opType = opType;
  }
  public String getOpObject() {
    return opObject;
  }
  public void setOpObject(String opObject) {
    this.opObject = opObject;
  }
  public String getOpDesc() {
    return opDesc;
  }
  public void setOpDesc(String opDesc) {
    this.opDesc = opDesc;
  }
  
  

}

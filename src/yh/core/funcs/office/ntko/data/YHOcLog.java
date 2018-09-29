package yh.core.funcs.office.ntko.data;

import java.util.Date;

public class YHOcLog {
  private int seqId;
  private int logUid;
  private Date logTime;
  private String logIp;
  private int logType;
  private String attachId;
  private String attachName;
  private String backupFile;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getLogUid() {
    return logUid;
  }
  public void setLogUid(int logUid) {
    this.logUid = logUid;
  }
  public Date getLogTime() {
    return logTime;
  }
  public void setLogTime(Date logTime) {
    this.logTime = logTime;
  }
  public String getLogIp() {
    return logIp;
  }
  public void setLogIp(String logIp) {
    this.logIp = logIp;
  }
  public int getLogType() {
    return logType;
  }
  public void setLogType(int logType) {
    this.logType = logType;
  }
  public String getAttachId() {
    return attachId;
  }
  public void setAttachId(String attachId) {
    this.attachId = attachId;
  }
  public String getAttachName() {
    return attachName;
  }
  public void setAttachName(String attachName) {
    this.attachName = attachName;
  }
  public String getBackupFile() {
    return backupFile;
  }
  public void setBackupFile(String backupFile) {
    this.backupFile = backupFile;
  }
  @Override
  public String toString() {
    return "YHOcLog [attachId=" + attachId + ", attachName=" + attachName
        + ", backupFile=" + backupFile + ", logIp=" + logIp + ", logTime="
        + logTime + ", logType=" + logType + ", logUid=" + logUid + ", seqId="
        + seqId + "]";
  }
  
}

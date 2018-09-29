package yh.core.esb.server.data;

import java.util.Date;

public class YHEsbSysMsg {
  private int seqId;
  private String guid;
  private String content;
  private String toId;
  private Date time;
  private String status;
  
  
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
  }
  public Date getTime() {
    return time;
  }
  public void setTime(Date time) {
    this.time = time;
  }
  
  
}

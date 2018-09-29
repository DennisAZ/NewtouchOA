package yh.core.esb.frontend.data;

public class YHEsbUploadTask {
   public int seqId;
   public String fileName;
   public String guid;
   public int status;
   public String toId;
   public String optGuid;
   public String message;
   
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getOptGuid() {
    return optGuid;
  }
  public void setOptGuid(String optGuid) {
    this.optGuid = optGuid;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public int getStatus() {
    return status;
  }
  public void setStatus(int status) {
    this.status = status;
  }
}

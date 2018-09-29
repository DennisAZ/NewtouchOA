package yh.subsys.oa.meeting.data;

public class YHMeetingRoom {

  private int seqId;
  private String mrName;
  private String mrCapacity;
  private String mrDevice;
  private String mrDesc;
  private String mrPlace;
  private String operator;
  private String secretToId;
  private String toId;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getMrName() {
    return mrName;
  }
  public void setMrName(String mrName) {
    this.mrName = mrName;
  }
  public String getMrCapacity() {
    return mrCapacity;
  }
  public void setMrCapacity(String mrCapacity) {
    this.mrCapacity = mrCapacity;
  }
  public String getMrDevice() {
    return mrDevice;
  }
  public void setMrDevice(String mrDevice) {
    this.mrDevice = mrDevice;
  }
  public String getMrDesc() {
    return mrDesc;
  }
  public void setMrDesc(String mrDesc) {
    this.mrDesc = mrDesc;
  }
  public String getMrPlace() {
    return mrPlace;
  }
  public void setMrPlace(String mrPlace) {
    this.mrPlace = mrPlace;
  }
  public String getOperator() {
    return operator;
  }
  public void setOperator(String operator) {
    this.operator = operator;
  }
  public String getSecretToId() {
    return secretToId;
  }
  public void setSecretToId(String secretToId) {
    this.secretToId = secretToId;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
  }

}

package yh.core.funcs.utilapps.info.bus.data;

import java.io.Serializable;

public class YHBus implements Serializable{
  
  private int seqId;
  
  private String lineId;
  
  private String passBy;
  
  private String startTime;
  
  private String endTime;
  
  private String busType;

  private String address;



  public String getLineId() {
    return lineId;
  }

  public void setLineId(String lineId) {
    this.lineId = lineId;
  }

  public String getPassBy() {
    return passBy;
  }

  public int getSeqId() {
    return seqId;
  }

  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  public void setPassBy(String passBy) {
    this.passBy = passBy;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getBusType() {
    return busType;
  }

  public void setBusType(String busType) {
    this.busType = busType;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
  
}

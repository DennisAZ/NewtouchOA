package yh.subsys.oa.vehicle.data;

import java.util.Date;

public class YHVehicle {
  private int seqId;
  private String vModel;
  private String vNum;
  private String vEngineNum;
  private String vDriver;
  private String vType;
  private Date vDate;
  private String vPrice;
  private String vStatus;
  private String vRemark;
  private String attachmentId;
  private String attachmentName;
  private String useingFlag;
  private String carUser;
  private String history;
  private String attachId;
  private String attachName;
  private Date insuranceDate;
  private int beforeDay;
  private Date lastInsuranceDate;
  private int insuranceFlag;
  
  public Date getInsuranceDate() {
    return insuranceDate;
  }
  public void setInsuranceDate(Date insuranceDate) {
    this.insuranceDate = insuranceDate;
  }
  public int getBeforeDay() {
    return beforeDay;
  }
  public void setBeforeDay(int beforeDay) {
    this.beforeDay = beforeDay;
  }
  public Date getLastInsuranceDate() {
    return lastInsuranceDate;
  }
  public void setLastInsuranceDate(Date lastInsuranceDate) {
    this.lastInsuranceDate = lastInsuranceDate;
  }
  public int getInsuranceFlag() {
    return insuranceFlag;
  }
  public void setInsuranceFlag(int insuranceFlag) {
    this.insuranceFlag = insuranceFlag;
  }
  public String getCarUser() {
    return carUser;
  }
  public void setCarUser(String carUser) {
    this.carUser = carUser;
  }
  public String getHistory() {
    return history;
  }
  public void setHistory(String history) {
    this.history = history;
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

  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getVModel() {
    return vModel;
  }
  public void setVModel(String vModel) {
    this.vModel = vModel;
  }
  public String getVNum() {
    return vNum;
  }
  public void setVNum(String vNum) {
    this.vNum = vNum;
  }
  public String getVEngineNum() {
    return vEngineNum;
  }
  public void setVEngineNum(String vEngineNum) {
    this.vEngineNum = vEngineNum;
  }
  public String getVDriver() {
    return vDriver;
  }
  public void setVDriver(String vDriver) {
    this.vDriver = vDriver;
  }
  public String getVType() {
    return vType;
  }
  public void setVType(String vType) {
    this.vType = vType;
  }
  public Date getVDate() {
    return vDate;
  }
  public void setVDate(Date vDate) {
    this.vDate = vDate;
  }
  public String getVPrice() {
    return vPrice;
  }
  public void setVPrice(String vPrice) {
    this.vPrice = vPrice;
  }
  public String getVStatus() {
    return vStatus;
  }
  public void setVStatus(String vStatus) {
    this.vStatus = vStatus;
  }
  public String getVRemark() {
    return vRemark;
  }
  public void setVRemark(String vRemark) {
    this.vRemark = vRemark;
  }
  public String getAttachmentId() {
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName() {
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }
  public String getUseingFlag() {
    return useingFlag;
  }
  public void setUseingFlag(String useingFlag) {
    this.useingFlag = useingFlag;
  }
}

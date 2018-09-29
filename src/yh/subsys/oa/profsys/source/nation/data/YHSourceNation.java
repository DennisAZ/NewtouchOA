package yh.subsys.oa.profsys.source.nation.data;

public class YHSourceNation {
  private int seqId;//SEQ_ID  int 流水号 Y   自增
  private String natNum;//NAT_NUM varchar(40) 国家编号      
  private String natName;//NAT_NAME  varchar(40) 国家名称      
  private String natStatus;//NAT_STATUS  CLOB  国家情况      
  private String natCustom;//NAT_CUSTOM  CLOB  风土人情      
  private String natBackground;//NAT_BACKGROUND  CLOB  政治背景      
  private String natNote;//NAT_NOTE  CLOB  备注      
  private String attachmentId;//ATTACHMENT_ID CLOB  附件ID字符串     
  private String attachmentName;//ATTACHMENT_NAME CLOB  附件名称字符串     
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getNatNum() {
    return natNum;
  }
  public void setNatNum(String natNum) {
    this.natNum = natNum;
  }
  public String getNatName() {
    return natName;
  }
  public void setNatName(String natName) {
    this.natName = natName;
  }
  public String getNatStatus() {
    return natStatus;
  }
  public void setNatStatus(String natStatus) {
    this.natStatus = natStatus;
  }
  public String getNatCustom() {
    return natCustom;
  }
  public void setNatCustom(String natCustom) {
    this.natCustom = natCustom;
  }
  public String getNatBackground() {
    return natBackground;
  }
  public void setNatBackground(String natBackground) {
    this.natBackground = natBackground;
  }
  public String getNatNote() {
    return natNote;
  }
  public void setNatNote(String natNote) {
    this.natNote = natNote;
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

}

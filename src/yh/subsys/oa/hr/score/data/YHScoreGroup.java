package yh.subsys.oa.hr.score.data;

public class YHScoreGroup {
  private int seqId;//SEQ_ID  int 流水号
  private String groupName;//GROUP_NAME  Varchar（40） 考核指标集名称
  private String groupDesc;//GROUP_DESC  CLOB  考核指标集描述
  private String groupRefer;//GROUP_REFER CLOB  设定考核依据模块工作日志和日程
  
  private String groupFlag;
  
  private String userPriv;
 
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getGroupName() {
    return groupName;
  }
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }
  public String getGroupDesc() {
    return groupDesc;
  }
  public void setGroupDesc(String groupDesc) {
    this.groupDesc = groupDesc;
  }
  public String getGroupRefer() {
    return groupRefer;
  }
  public void setGroupRefer(String groupRefer) {
    this.groupRefer = groupRefer;
  }
  public String getGroupFlag() {
    return groupFlag;
  }
  public void setGroupFlag(String groupFlag) {
    this.groupFlag = groupFlag;
  }
  public String getUserPriv() {
    return userPriv;
  }
  public void setUserPriv(String userPriv) {
    this.userPriv = userPriv;
  }
}

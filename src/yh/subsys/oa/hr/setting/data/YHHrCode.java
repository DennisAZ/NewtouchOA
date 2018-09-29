package yh.subsys.oa.hr.setting.data;

public class YHHrCode {
  private int seqId;//SEQ_ID  int
  private String codeNo;//CODE_NO varchar(40) 
  private String codeName;//CODE_NAME VARCHAR(40)
  private String codeOrder;//CODE_ORDER  VARCHAR(40)
  private String parentNo;//PARENT_NO VARCHAR(40)
  private String codeFlag;//CODE_FLAG VARCHAR(10)
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getCodeNo() {
    return codeNo;
  }
  public void setCodeNo(String codeNo) {
    this.codeNo = codeNo;
  }
  public String getCodeName() {
    return codeName;
  }
  public void setCodeName(String codeName) {
    this.codeName = codeName;
  }
  public String getCodeOrder() {
    return codeOrder;
  }
  public void setCodeOrder(String codeOrder) {
    this.codeOrder = codeOrder;
  }
  public String getParentNo() {
    return parentNo;
  }
  public void setParentNo(String parentNo) {
    this.parentNo = parentNo;
  }
  public String getCodeFlag() {
    return codeFlag;
  }
  public void setCodeFlag(String codeFlag) {
    this.codeFlag = codeFlag;
  }

}

package yh.subsys.oa.asset.data;

public class YHCpAssetType {
  private int seqId;//SEQ_ID  INT
  private String typeName;//TYPE_NAME VARCHAR(200)
  private int typeNo;//TYPE_NO Int
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getTypeName() {
    return typeName;
  }
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
  public int getTypeNo() {
    return typeNo;
  }
  public void setTypeNo(int typeNo) {
    this.typeNo = typeNo;
  }
}

package yh.subsys.oa.vote.data;

public class YHVoteData {
  private int seqId;//  SEQ_ID  int 流水号
  private int itemId;//  ITEM_ID Int 投票记录ID
  private String fieldName;//  FIELD_NAME  VARCHAR(20) ？？？
  private String fieldData;//String;//  FIELD_DATA  CLON  评论内容
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getItemId() {
    return itemId;
  }
  public void setItemId(int itemId) {
    this.itemId = itemId;
  }
  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  public String getFieldData() {
    return fieldData;
  }
  public void setFieldData(String fieldData) {
    this.fieldData = fieldData;
  }

}

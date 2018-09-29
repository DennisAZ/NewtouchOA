package yh.subsys.oa.hr.score.data;

public class YHScoreItem {
  private int seqId;//SEQ_ID  int 流水号
  private int groupId;//GROUP_ID  int   
  private String itemName;//ITEM_NAME Varchar(200)  考核项目
  private double max;//MAX decimal(11,2) 最大-分值范围
  private double min;//MIN decimal(11,2) 最小-分值范围
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getGroupId() {
    return groupId;
  }
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
  public String getItemName() {
    return itemName;
  }
  public void setItemName(String itemName) {
    this.itemName = itemName;
  }
  public double getMax() {
    return max;
  }
  public void setMax(double max) {
    this.max = max;
  }
  public double getMin() {
    return min;
  }
  public void setMin(double min) {
    this.min = min;
  }

}

package yh.core.funcs.dept.data;

public class YHPositionPerson  {

  private int seqId = 0;
  private int positionSeqId = 0;
  private String positionUsers = null;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getPositionSeqId() {
    return positionSeqId;
  }
  public void setPositionSeqId(int positionSeqId) {
    this.positionSeqId = positionSeqId;
  }
  public String getPositionUsers() {
    return positionUsers;
  }
  public void setPositionUsers(String positionUsers) {
    this.positionUsers = positionUsers;
  }
  @Override
  public String toString() {
    return "YHPositionPerson [positionSeqId=" + positionSeqId
        + ", positionUsers=" + positionUsers + ", seqId=" + seqId + "]";
  }
  
}

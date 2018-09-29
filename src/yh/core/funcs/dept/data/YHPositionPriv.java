package yh.core.funcs.dept.data;

public class YHPositionPriv {

  private int seqId = 0;
  private int positionSeqId = 0;
  private String positionPriv = null;
  
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
  public String getPositionPriv() {
    return positionPriv;
  }
  public void setPositionPriv(String positionPriv) {
    this.positionPriv = positionPriv;
  }
  @Override
  public String toString() {
    return "YHPositionPriv [positionPriv=" + positionPriv + ", positionSeqId="
        + positionSeqId + ", seqId=" + seqId + "]";
  }
}

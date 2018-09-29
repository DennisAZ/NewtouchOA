package yh.pda.diary.data;

import java.util.Date;

public class YHPdaDiary {

  private int seqId ;
  private Date diaDate ;
  private int diaType ;
  private String content ;
  
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public Date getDiaDate() {
    return diaDate;
  }
  public void setDiaDate(Date diaDate) {
    this.diaDate = diaDate;
  }
  public int getDiaType() {
    return diaType;
  }
  public void setDiaType(int diaType) {
    this.diaType = diaType;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
}

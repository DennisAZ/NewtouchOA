package yh.core.funcs.calendar.data;

import java.util.Date;

public class YHCalendarDiary {
  private int seqId;
  private String calendarId;
  private int diaryId;
  private Date calDiaDate;
  public Date getCalDiaDate() {
    return calDiaDate;
  }
  public void setCalDiaDate(Date calDiaDate) {
    this.calDiaDate = calDiaDate;
  }

  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getCalendarId() {
    return calendarId;
  }
  public void setCalendarId(String calendarId) {
    this.calendarId = calendarId;
  }
  public int getDiaryId() {
    return diaryId;
  }
  public void setDiaryId(int diaryId) {
    this.diaryId = diaryId;
  }

}

package yh.core.funcs.diary.data;

import java.util.Calendar;
import java.util.Date;
/**
 * lock日志全局设置上锁
 * @author Think
 *
 */
public class YHDiaryLock {
  private Date startDate;
  private Date endDate;
  private Date prelockDate;
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public Date getPrelockDate() {
    return prelockDate;
  }
  /**
   * 设置prelockDate这前的天数
   * @param prelockDate
   */
  public void setPrelockDate(int prelockDate) {
    if(prelockDate == 0){
      return;
    }
    Calendar   c   =   Calendar.getInstance(); 
    c.add(c.DATE,-prelockDate);//得到今天的前一百天   
    Date   d   =   c.getTime();   
    this.prelockDate = d;
  }
  /**
   * 判断是否上锁
   *   isLock1 在开始日期之后的
   *   isLock2 在开始日期之前的
   *   isLock3
   * @param date
   * @return false 代表没上锁，true代表上锁
   */
  public boolean isLock(Date date){
    boolean result = false;
    boolean isLock1 = false;
    boolean isLock2 = false;
    boolean isLock3 = false;
    if (date == null) {
      return result;
    }
    
    isLock1 = (this.startDate != null) ? date.after(this.startDate)||date.equals(this.startDate) : false;
    isLock2 = (this.endDate != null) ? date.before(this.endDate) ||date.equals(this.endDate) : false;
    isLock3 = (this.prelockDate != null) ? date.before(this.prelockDate) ||date.equals(this.prelockDate): false;
    result = ( isLock1 && isLock2) || isLock3;
    return result;
  }
  @Override
  public String toString() {
    return "YHDiaryLock [endDate=" + endDate + ", prelockDate=" + prelockDate
        + ", startDate=" + startDate + ", getEndDate()=" + getEndDate()
        + ", getPrelockDate()=" + getPrelockDate() + ", getStartDate()="
        + getStartDate() + ", getClass()=" + getClass() + ", hashCode()="
        + hashCode() + ", toString()=" + super.toString() + "]";
  }
  
  
}

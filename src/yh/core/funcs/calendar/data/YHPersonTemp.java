package yh.core.funcs.calendar.data;

public class YHPersonTemp implements Comparable{
  private int seqId;
  private String userName;
  private String birthday;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getBirthday() {
    return birthday;
  }
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }
 
  public int compareTo(Object obj) {
    YHPersonTemp temp = (YHPersonTemp)obj;
    return this.getBirthday().compareTo(temp.getBirthday());
  }
}  
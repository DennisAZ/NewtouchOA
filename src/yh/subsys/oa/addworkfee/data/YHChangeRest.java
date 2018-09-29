package yh.subsys.oa.addworkfee.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 调休
 * @author Administrator
 *
 */
public class YHChangeRest{
  private int seqId;   //主键
  private int year;    //年份
  private Date beginDate; //开始日期
  private Date endDate;   //结束日期
  private String name; //调休日期
  private int type;    //类型
  private String reason;
  private int userId;
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public int getYear(){
    return year;
  }
  public void setYear(int year){
    this.year = year;
  }
  public Date getBeginDate(){
    return beginDate;
  }
  public void setBeginDate(Date beginDate){
    this.beginDate = beginDate;
  }
  public Date getEndDate(){
    return endDate;
  }
  public void setEndDate(Date endDate){
    this.endDate = endDate;
  }
  public String getName(){
    return name;
  }
  public void setName(String name){
    this.name = name;
  }
  public int getType(){
    return type;
  }
  public void setType(int type){
    this.type = type;
  }
  public String getReason(){
    return reason;
  }
  public void setReason(String reason){
    this.reason = reason;
  }
  public int getUserId(){
    return userId;
  }
  public void setUserId(int userId){
    this.userId = userId;
  }
  private String beginDateStr;
  private String endDateStr;
  public String getBeginDateStr(){
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    return sf.format(beginDate);
  }
  public String getEndDateStr(){
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    return sf.format(endDate);
  }
}

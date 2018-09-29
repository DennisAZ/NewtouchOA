package yh.subsys.oa.addworkfee.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import yh.core.util.YHUtility;

/**
 * 节假日
 * @author Administrator
 *
 */
public class YHFestival{
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
  public String getFeName(){
    return feName;
  }
  public void setFeName(String feName){
    this.feName = feName;
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
  public int getFeType(){
    return feType;
  }
  public void setFeType(int feType){
    this.feType = feType;
  }
  private int seqId;               //主键
  private int year;                //日期年份
  private String feName;           //节假日名称
  private Date beginDate;          //开始日期
  private Date endDate;            //结束日期
  private int feType;              //假日类型 1 法定假日 ， 2 普通假日
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

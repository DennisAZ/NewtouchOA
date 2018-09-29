package yh.subsys.inforesouce.data;

import java.util.Date;

import yh.subsys.inforesouce.util.YHStringUtil;

/**
 * 临时文件（假数据使用）
 * @author qwx110
 *
 */
public  class TempFile{
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public String getName(){
    return name;
  }
  public void setName(String name){
    this.name = name;
  }
  public double getSize(){
    return size;
  }
  public void setSize(double size){
    this.size = size;
  }
  public String getType(){
    return type;
  }
  public void setType(String type){
    this.type = type;
  }
  public Date getChangeDate(){
    return changeDate;
  }
  public void setChangeDate(Date changeDate){
    this.changeDate = changeDate;
  }
  public String getDept(){
    return dept;
  }
  public void setDept(String dept){
    this.dept = dept;
  }
  public String getOper(){
    return oper;
  }
  public void setOper(String oper){
    this.oper = oper;
  }
  private int seqId;
  private String name;
  private double size;
  private String type;
  private Date changeDate;
  private String dept;
  private String oper;
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
       sb.append("seqId:").append("'").append(seqId).append("',");
       sb.append("name:").append("'").append(name).append("',");
       sb.append("size:").append("'").append(size).append("',");
       sb.append("type:").append("'").append(type).append("',");
       sb.append("changeDate:").append("'").append(YHStringUtil.dateFormat(changeDate)).append("',");
       sb.append("dept:").append("'").append(dept).append("',");
       sb.append("oper:").append("'").append(oper).append("'");
    sb.append("}");
    return sb.toString();
  }
}

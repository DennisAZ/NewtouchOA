package yh.subsys.oa.addworkfee.data;

public class YHDateJuge{
  
  private double timeDiff;
  private int dateType;
  private double totalFee;
  
  
  public double getTotalFee(){
    return totalFee;
  }



  public void setTotalFee(double totalFee){
    this.totalFee = totalFee;
  }



  public double getTimeDiff(){
    return timeDiff;
  }



  public void setTimeDiff(double timeDiff){
    this.timeDiff = timeDiff;
  }



  public int getDateType(){
    return dateType;
  }



  public void setDateType(int dateType){
    this.dateType = dateType;
  }



  public String toJson(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
      sb.append("diff:").append("\"").append(timeDiff).append("\",");
      sb.append("type:").append("\"").append(dateType).append("\",");
      sb.append("money:").append("\"").append(totalFee).append("\"");
    sb.append("}");
    return sb.toString();
  }
  
  public static void main(String[] args){
    YHDateJuge ju = new YHDateJuge();
    ju.setDateType(1);
    ju.setTimeDiff(20);
    System.out.println(ju.toJson());
  }
}

package yh.core.funcs.system.syslog.data;

import java.util.Date;

public class YHSysLog {
  private int seqId;
  private int userId;
  private Date time;
  private String ip;
  private String type;
  private String remark;
  private String userName;
  
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public Date getTime() {
    return time;
  }
  public void setTime(Date time) {
    this.time = time;
  }
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  @Override
  public String toString() {
    return "YHSysLog [ip=" + ip + ", remark=" + remark + ", seqId=" + seqId
        + ", time=" + time + ", type=" + type + ", userId=" + userId + "]";
  }
  public static void main(String[] args){
   
    int ary[]={1,2,3,4,5,3,2};
    int ary1[] = new int[ary.length];
    boolean bln = false;
    int num = 0;
    for(int i = 0; i < ary.length; i ++){
      for(int j = 0; j<ary1.length; j ++){
        //System.out.println(ary[i]+"===="+ary1[j]);
        if(ary[i] == ary1[j]){
          bln = true; 
          break;
        }
      }
      if(bln == false){
       ary1[num] = ary[i];
       num ++;
      }
      bln = false;
    }
    for(int m = 0; m < ary1.length; m ++){
      if(ary1[m] != 0){
        System.out.println(ary1[m]);
      }
    }
    
    /*int[] a={1,1,2,10,10,22,22,2,3,3,4,5,6,6,7,8,9,8}; 
    List list=new ArrayList(); 
    Arrays.sort(a); 
    for(int i=a.length-1;i>0;i--){ 
     if(a[i]!=a[i-1]){ 
      list.add(a[i]); 
     } 
    } 
     list.add(a[0]); 
     //System.out.print("重新整理后的顺序是");
    for(int j=0;j<list.size();j++)
    {
     //System.out.print(list.get(j)+"  ");
    }*/
  }
}

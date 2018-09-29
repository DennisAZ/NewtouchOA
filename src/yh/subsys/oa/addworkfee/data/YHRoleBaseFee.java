package yh.subsys.oa.addworkfee.data;
/**
 * 角色加班费基数表
 * @author Administrator
 *
 */
public class YHRoleBaseFee{
  private int seqId;         //主键
  private int roleId;        //角色id
  private double normalAdd;  //平时加班费（按照小时计算）倍数
  private double festivalAdd;//节日加班费
  private double weekAdd;    //周末加班费
  private String roleIds;
  private String name;
  private double baseAdd;   //加班费基数
  public double getBaseAdd(){
    return baseAdd;
  }
  public void setBaseAdd(double baseAdd){
    this.baseAdd = baseAdd;
  }
  public String getName(){
    return name;
  }
  public void setName(String name){
    this.name = name;
  }
  public String getRoleIds(){
    return roleIds;
  }
  public void setRoleIds(String roleIds){
    this.roleIds = roleIds;
  }
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public int getRoleId(){
    return roleId;
  }
  public void setRoleId(int roleId){
    this.roleId = roleId;
  }
  public double getNormalAdd(){
    return normalAdd;
  }
  public void setNormalAdd(double normalAdd){
    this.normalAdd = normalAdd;
  }
  public double getFestivalAdd(){
    return festivalAdd;
  }
  public void setFestivalAdd(double festivalAdd){
    this.festivalAdd = festivalAdd;
  }
  public double getWeekAdd(){
    return weekAdd;
  }
  public void setWeekAdd(double weekAdd){
    this.weekAdd = weekAdd;
  }
  
}

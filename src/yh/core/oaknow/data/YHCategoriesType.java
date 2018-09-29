package yh.core.oaknow.data;

import java.io.Serializable;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;

/**
 * oa知道问题分类
 * 
 * @author qwx110
 * 
 */
public class YHCategoriesType implements Serializable{
  private int                    seqId;       //主健id
  private String                 name;         // 分类名称
  private int                    pearentId = 0; // 父分类id
  private int                    orderId   = 0; // 排序号
  private String                 managers;     // 管理员的id，用','进行分割
  private List<YHCategoriesType> list;          // 用来存储子类
  private List<String> usersName ;
  private String managerNames = "";
  public List<String> getUsersName(){
    return usersName;
  }
  public void setUsersName(List<String> usersName){
    this.usersName = usersName;
  }
  public String getManagerNames(){    
    return managerNames;
  }

  public void setManagerNames(String managerNames){
    this.managerNames = managerNames;
  }

  public int getSeqId(){
    return seqId;
  }

  public void setSeqId(int seqId){
    this.seqId = seqId;
  }

  public String getName(){
    return name;
  }

  public List<YHCategoriesType> getList(){
    return list;
  }

  public void setList(List<YHCategoriesType> list){
    this.list = list;
  }

  public void setName(String name){
    this.name = name;
  }

  public int getPearentId(){
    return pearentId;
  }

  public void setPearentId(int pearentId){
    this.pearentId = pearentId;
  }

  public int getOrderId(){
    return orderId;
  }

  public void setOrderId(int orderId){
    this.orderId = orderId;
  }

  public String getManagers(){
    return managers;
  }

  public void setManagers(String managers){
    this.managers = managers;
  }
  
  public String toString(){
    StringBuffer sb = new StringBuffer();
      sb.append("{");
         sb.append("seqId:").append(seqId).append(",");
         if(name != null){
          sb.append("name:'").append(YHUtility.encodeSpecial(name)).append("',");
         }
         sb.append("pearentId:").append(pearentId).append(",");
         if(managers != null){
           sb.append("managers:").append(managers).append(",");
         }
         if(list != null && list.size() !=0 ){
           sb.append("list:[");
             for(int i=0; i<list.size(); i++){
               if(i < list.size()-1 ){
                 sb.append(list.get(i).toString()).append(",");
               }else{
                 sb.append(list.get(list.size()-1).toString());
               }
             }
           sb.append("]");
         }else{
           sb.append("list:[]");
         }
      sb.append("}");
    return sb.toString();
  }
}

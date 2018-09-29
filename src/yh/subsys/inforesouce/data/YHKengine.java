package yh.subsys.inforesouce.data;

import java.util.ArrayList;
import java.util.List;

import yh.core.util.YHUtility;

/**
 * 知识搜索内容
 * @author 
 *
 */
public class YHKengine{
  private String userName;
  private String areaName;
  private String orgName;
  private String subJect;
  private String keyWord;  
  List list = new ArrayList();
  public String getUserName() {
    /*if(userName == null){
      userName ="";
    }else{
     String names[] = userName.split(","); 
    }*/
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getAreaName() {
    return areaName;
  }
  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }
  public String getOrgName() {
    return orgName;
  }
  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }
  public String getSubJect() {
    return subJect;
  }
  public void setSubJect(String subJect) {
    this.subJect = subJect;
  }
  public String getKeyWord() {
    return keyWord;
  }
  public void setKeyWord(String keyWord) {
    this.keyWord = keyWord;
  }
  /**
   * 返回list 中 以"," 分割的名字
   * @return
   */
  public List<String> getUserNameList(){
    return splitStr(this.userName);
  }
  public List<String> getAreaNameList(){
    return splitStr(this.areaName);
  }
  public List<String> getOrgNameList(){
    return splitStr(this.orgName);
  }
  public List<String> getSubJectList(){
    return splitStr(this.subJect);
  }
  public List<String> getKeyWordList(){
    return splitStr(this.keyWord);
  }
  /**
   * 把一个长串以","分割  然后保存到List中
   * @param str
   * @return
   */
  public List<String> splitStr(String str){
    if(YHUtility.isNullorEmpty(str)){
      return null;
    }
    List list = new ArrayList();
    String[] after = str.split(",");
    if(after.length <=1){
      after = str.split(" ");
    }
    for(int i=0; i<after.length; i++){
      if(!YHUtility.isNullorEmpty(after[i])){
        list.add(after[i]);
      }
    }
    return list;
  }

  // public String toString(){
 /*   StringBuffer sb = new StringBuffer();
    sb.append("{");
       sb.append("seqId:").append("'").append(seqId).append("'").append(",");
       sb.append("userId:").append("'").append(userId).append("'").append(",");
       sb.append("nodes:").append("'").append(nodes).append("'").append(",");
       sb.append("tagName:").append("'").append(tagName).append("'");
    sb.append("}");
    return sb.toString();*/
  //}
}

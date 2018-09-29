package yh.core.oaknow.data;

import java.io.Serializable;

/**
 * OA知道用户
 * 
 * @author qwx110
 * 
 */
public class YHOAKnowUser implements Serializable{
  private static final long serialVersionUID = 1L;
  private int               score;                // 积分
  private String            tder_flag;            // 用于判断是管理员还是普通用户
  private String name;
  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  
  public int getScore(){
    return score;
  }

  public void setScore(int score){
    this.score = score;
  }

  public String getTder_flag(){
    return tder_flag;
  }

  public void setTder_flag(String tderFlag){
    tder_flag = tderFlag;
  }

  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append("score:").append("'" + score + "'").append(",");
    sb.append("tder_flag:").append("'" + tder_flag + "'");
    sb.append("}");
    return sb.toString();
  }
}

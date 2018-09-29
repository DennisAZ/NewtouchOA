package yh.subsys.inforesouce.data;



import java.io.Serializable;
import java.util.List;
/**
 * 信息管理
 * @author qwx110
 *
 */
public class YHMateShow implements Serializable{
  private int SEQ_ID;         //主键
  private int USER_ID;   //用户选择记录
  private String PR_ID;     //记录父元素  private String IDSTR;     //所有元素字符串（父元素，子元素，值域）
  private String typeId;

  public String getTypeId(){
    return typeId;
  }
  public void setTypeId(String typeId){
    this.typeId = typeId;
  }
  public int getSEQ_ID() {
    return SEQ_ID;
  }
  public void setSEQ_ID(int sEQID) {
    SEQ_ID = sEQID;
  }
  public int getUSER_ID() {
    return USER_ID;
  }
  public void setUSER_ID(int uSERID) {
    USER_ID = uSERID;
  }
  public String getPR_ID() {
    return PR_ID;
  }
  public void setPR_ID(String pRID) {
    PR_ID = pRID;
  }
  public String getIDSTR() {
    return IDSTR;
  }
  public void setIDSTR(String iDSTR) {
    IDSTR = iDSTR;
  }
  
}

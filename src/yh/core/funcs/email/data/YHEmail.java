package yh.core.funcs.email.data;

import java.util.ArrayList;
import java.util.Iterator;

public class YHEmail{
  private int seqId;
  private String toId;
  private String readFlag = "0";
  private String deleteFlag = "0";
  private int boxId = 0;
  private int bodyId;
  private String receipt;
  private ArrayList<YHEmailBody> emlb = null;
  
  public ArrayList<YHEmailBody> getEmlb(){
    return emlb;
  }
  public void addEmlb(YHEmailBody e){
    if(emlb == null){
      emlb = new ArrayList<YHEmailBody>();
    }
    if(e != null){
      emlb.add(e);
    }
  }
  public Iterator itE(){
    if(emlb == null){
      return null;
    }
    return emlb.iterator();
  }
  public void setEmlb(ArrayList<YHEmailBody> emlb){
    this.emlb = emlb;
  }
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public String getToId(){
    return toId;
  }
  public void setToId(String toId){
    this.toId = toId;
  }
  public int getBoxId(){
    return boxId;
  }
  public void setBoxId(int boxId){
    this.boxId = boxId;
  }
  public int getBodyId(){
    return bodyId;
  }
  public void setBodyId(int bodyId){
    this.bodyId = bodyId;
  }
  public String getReadFlag(){
    return readFlag;
  }
  public void setReadFlag(String readFlag){
    this.readFlag = readFlag;
  }
  public String getDeleteFlag(){
    return deleteFlag;
  }
  public void setDeleteFlag(String deleteFlag){
    this.deleteFlag = deleteFlag;
  }
  public String getReceipt(){
    return receipt;
  }
  public void setReceipt(String receipt){
    this.receipt = receipt;
  }
  @Override
  public String toString(){
    return "YHEmail [bodyId=" + bodyId + ", boxId=" + boxId + ", deleteFlag="
        + deleteFlag + ", emlb=" + emlb + ", readFlag=" + readFlag
        + ", receipt=" + receipt + ", seqId=" + seqId + ", toId=" + toId + "]";
  }
  
}

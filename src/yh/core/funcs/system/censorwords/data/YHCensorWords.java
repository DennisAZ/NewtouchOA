package yh.core.funcs.system.censorwords.data;

import java.util.ArrayList;
import java.util.Iterator;

import yh.core.data.YHDsField;

public class YHCensorWords {
  private int seqId;
  private int userId;
  private String find;
  private String replacement;
  
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
  public String getFind() {
    return find;
  }
  public void setFind(String find) {
    this.find = find;
  }
  public String getReplacement() {
    return replacement;
  }
  public void setReplacement(String replacement) {
    this.replacement = replacement;
  }
}

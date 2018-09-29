package yh.core.funcs.system.data;

import java.util.ArrayList;
import java.util.List;

public class YHMenu{
  private int seqId;
  private int expand;
  private String id;
  private String text;
  private String icon;
  private int leaf;
  private String url;
  private String openFlag;
  
  public int getExpand() {
    return expand;
  }
  public void setExpand(int expand) {
    this.expand = expand;
  }
  public String getOpenFlag() {
    return openFlag;
  }
  public void setOpenFlag(String openFlag) {
    this.openFlag = openFlag;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public String getIcon() {
    return icon;
  }
  public void setIcon(String icon) {
    this.icon = icon;
  }
  public int getLeaf() {
    return leaf;
  }
  public void setLeaf(int leaf) {
    this.leaf = leaf;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  
}
package yh.core.esb.common;

import java.io.Serializable;
import java.util.BitSet;

import org.apache.http.HttpHost;

import yh.core.esb.common.util.YHSerializer;

public class YHEsbTaskInfo implements Serializable {
  private String guid;
  private int partSize;
  private HttpHost host;
  private BitSet bitset = new BitSet();
  
  synchronized public void done(int index) {
    bitset.set(index, true);
  }
  public String hasDone() {
    String ss = "";
    for(int i=bitset.nextSetBit(0); i>=0; i=bitset.nextSetBit(i+1)) { 
      ss += i + ",";
    }
    if (ss.endsWith(","))
      ss = ss.substring(0 , ss.length() - 1);
    return ss;
  }
  
  public boolean isDone(int index) {
    return bitset.get(index);
  }

  
  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public int getPartSize() {
    return partSize;
  }

  public void setPartSize(int partSize) {
    this.partSize = partSize;
  }

  public HttpHost getHost() {
    return host;
  }

  public void setHost(HttpHost host) {
    this.host = host;
  }
  
  public String progress() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bitset.length(); i++) {
      sb.append(bitset.get(i) ? "1" : "0");
    }
    return sb.toString();
  }
  
  
  public static void main(String[] args) {
    YHSerializer<YHEsbTaskInfo> s = new YHSerializer<YHEsbTaskInfo>();
  }
}

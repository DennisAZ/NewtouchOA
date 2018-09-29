//
package yh.core.funcs.doc.data;
public class YHDocFlowRunData {
  private int seqId;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  private int runId;
  private int itemId;
  private String itemData;
  public int getRunId() {
    return this.runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public int getItemId() {
    return this.itemId;
  }
  public void setItemId(int itemId) {
    this.itemId = itemId;
  }
  public String getItemData() {
    return this.itemData;
  }
  public void setItemData(String itemData) {
    this.itemData = itemData;
  }
}

package yh.core.funcs.workflow.data;

public class YHFlowSort {
  private int seqId;
  private int sortNo;
  private String sortName;
  private int sortParent;
  private String haveChild;
  private int deptId;

  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getSortNo() {
    return sortNo;
  }
  public void setSortNo(int sortNo) {
    this.sortNo = sortNo;
  }
  public String getSortName() {
    return sortName;
  }
  public void setSortName(String sortName) {
    this.sortName = sortName;
  }
  public int getSortParent() {
    return sortParent;
  }
  public void setSortParent(int sortParent) {
    this.sortParent = sortParent;
  }
  public String getHaveChild() {
    return haveChild;
  }
  public void setHaveChild(String haveChild) {
    this.haveChild = haveChild;
  }
  public int getDeptId() {
    return deptId;
  }
  public void setDeptId(int deptId) {
    this.deptId = deptId;
  }
  public String toJsonSimple(){
    StringBuffer sb = new StringBuffer("{");
    sb.append("seqId:" + this.seqId + ",");
    sb.append("sortNo:" + this.sortNo + ",");
    sb.append("sortName:'" + this.sortName + "'");
    sb.append("}");
    return sb.toString();
  }
}

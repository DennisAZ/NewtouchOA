package yh.subsys.oa.finance.data;

public class YHBudgetDeptTotal {
  private int seqId;
  private int curYear;
  private String deptId;
  private double total;

  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  public int getCurYear() {
    return curYear;
  }
  public void setCurYear(int curYear) {
    this.curYear = curYear;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public double getTotal() {
    return total;
  }
  public void setTotal(double total) {
    this.total = total;
  }

}

package yh.subsys.oa.finance.data;
import java.io.Serializable;
import java.util.Date;
public class YHFinanceApplyRecord implements Serializable{
  private int seqId;//主键
  private String applyClaimer;//领用人名
  private Date applyDate;//领用时间
  private int applyYear;//年
  private String applyItem;//金来源
  private String budgetId;//外键（BUDGET_APPLY）
  private double applyMoney;//领用钱
  private String applyMemo;//用途
  private String operator;
  private Date operateDate;
  private String deptDirector;
  private Date deptDirectorDate;
  private String deptDirectorContent;
  private String financeDirector;//财务人
  private Date financeDirectorDate;
  private String financeDirectorContent;
  private String financeSignatory;
  private Date signDate;
  private int runId;
  private int signType;
  private String chequeAccount;//支票号
  private String expense;
  private String isPrint;
  private String deptId;
  private String chargeId;//报销记录ID
  private String applyProject;//APPLY_PROJECT
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getApplyClaimer() {
    return applyClaimer;
  }
  public void setApplyClaimer(String applyClaimer) {
    this.applyClaimer = applyClaimer;
  }
  public Date getApplyDate() {
    return applyDate;
  }
  public void setApplyDate(Date applyDate) {
    this.applyDate = applyDate;
  }
  public int getApplyYear() {
    return applyYear;
  }
  public void setApplyYear(int applyYear) {
    this.applyYear = applyYear;
  }
  public String getApplyItem() {
    return applyItem;
  }
  public void setApplyItem(String applyItem) {
    this.applyItem = applyItem;
  }
  public String getBudgetId() {
    return budgetId;
  }
  public void setBudgetId(String budgetId) {
    this.budgetId = budgetId;
  }
  public double getApplyMoney() {
    return applyMoney;
  }
  public void setApplyMoney(double applyMoney) {
    this.applyMoney = applyMoney;
  }
  public String getApplyMemo() {
    return applyMemo;
  }
  public void setApplyMemo(String applyMemo) {
    this.applyMemo = applyMemo;
  }
  public String getOperator() {
    return operator;
  }
  public void setOperator(String operator) {
    this.operator = operator;
  }
  public Date getOperateDate() {
    return operateDate;
  }
  public void setOperateDate(Date operateDate) {
    this.operateDate = operateDate;
  }
  public String getDeptDirector() {
    return deptDirector;
  }
  public void setDeptDirector(String deptDirector) {
    this.deptDirector = deptDirector;
  }
  public Date getDeptDirectorDate() {
    return deptDirectorDate;
  }
  public void setDeptDirectorDate(Date deptDirectorDate) {
    this.deptDirectorDate = deptDirectorDate;
  }
  public String getDeptDirectorContent() {
    return deptDirectorContent;
  }
  public void setDeptDirectorContent(String deptDirectorContent) {
    this.deptDirectorContent = deptDirectorContent;
  }
  public String getFinanceDirector() {
    return financeDirector;
  }
  public void setFinanceDirector(String financeDirector) {
    this.financeDirector = financeDirector;
  }
  public Date getFinanceDirectorDate() {
    return financeDirectorDate;
  }
  public void setFinanceDirectorDate(Date financeDirectorDate) {
    this.financeDirectorDate = financeDirectorDate;
  }
  public String getFinanceDirectorContent() {
    return financeDirectorContent;
  }
  public void setFinanceDirectorContent(String financeDirectorContent) {
    this.financeDirectorContent = financeDirectorContent;
  }
  public String getFinanceSignatory() {
    return financeSignatory;
  }
  public void setFinanceSignatory(String financeSignatory) {
    this.financeSignatory = financeSignatory;
  }
  public Date getSignDate() {
    return signDate;
  }
  public void setSignDate(Date signDate) {
    this.signDate = signDate;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public int getSignType() {
    return signType;
  }
  public void setSignType(int signType) {
    this.signType = signType;
  }
  public String getChequeAccount() {
    return chequeAccount;
  }
  public void setChequeAccount(String chequeAccount) {
    this.chequeAccount = chequeAccount;
  }
  public String getExpense() {
    return expense;
  }
  public void setExpense(String expense) {
    this.expense = expense;
  }
  public String getIsPrint() {
    return isPrint;
  }
  public void setIsPrint(String isPrint) {
    this.isPrint = isPrint;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public String getChargeId() {
    return chargeId;
  }
  public void setChargeId(String chargeId) {
    this.chargeId = chargeId;
  }
  public String getApplyProject() {
    return applyProject;
  }
  public void setApplyProject(String applyProject) {
    this.applyProject = applyProject;
  }

}

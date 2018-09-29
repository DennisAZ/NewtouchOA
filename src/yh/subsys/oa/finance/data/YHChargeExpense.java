package yh.subsys.oa.finance.data;

import java.util.Date;

public class YHChargeExpense {
  private int seqId;//  SEQ_ID  INT ID
  private String deptId;//  DEPT_ID Varchar(20) 部门名称
  private String chargeUser;//  CHARGE_USER Varchar(30) 报销姓名
  private Date chargeDate;//  CHARGE_DATE Datetime  报销日期
  private String chargeItem;//  CHARGE_ITEM Varchar(200)  报销项目
  private double chargeMoney;//  CHARGE_MONEY  Money 报销金额
  private String chargeMemo;//  CHARGE_MEMO Varchar(200)  备注
  private String deptAuditUser;//  DEPT_AUDIT_USER Varchar(100)  部门审批人
  private Date deptAuditDate;//  DEPT_AUDIT_USER Datetime  部门审批时间
  private String deptAuditContent;//  DEPT_AUDIT_CONTENT  Text  部门审批内容
  private String financeAuditUser;//  FINANCE_AUDIT_USER  Varchar(100)  财务审批人
  private Date financeAuditDate;//  FINANCE_AUDIT_DATE  Datetime  财务审批时间
  private String financeAuditContent;//  FINANCE_AUDIT_CONTENT CLOB  财务审批内容
  private int runId;//  RUN_ID  Int 工作流ID
  private int chargeYear;//  CHARGE_YEAR Year(4) 报销年份
  private int projId;//  PROJ_ID Int 团组ID
  private int costId;//  COST_ID Int 费用ID
  private int projSign;//  PROJ_SIGN Int 团组类别
  private String isPrint;//  IS_PRINT  Int 是否打印
  private String expense;//  EXPENSE Int 是否报销
  private String ofEx;//  OF_EX Int 
  private String budgetId;//  BUDGET_ID Int 对应的预算项目Id
  private String makeWaste;//  MAKE_WASTE  Int 
  private String settleFlag;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public String getChargeUser() {
    return chargeUser;
  }
  public void setChargeUser(String chargeUser) {
    this.chargeUser = chargeUser;
  }
  public Date getChargeDate() {
    return chargeDate;
  }
  public void setChargeDate(Date chargeDate) {
    this.chargeDate = chargeDate;
  }
  public String getChargeItem() {
    return chargeItem;
  }
  public void setChargeItem(String chargeItem) {
    this.chargeItem = chargeItem;
  }
  public double getChargeMoney() {
    return chargeMoney;
  }
  public void setChargeMoney(double chargeMoney) {
    this.chargeMoney = chargeMoney;
  }
  public String getChargeMemo() {
    return chargeMemo;
  }
  public void setChargeMemo(String chargeMemo) {
    this.chargeMemo = chargeMemo;
  }
  public String getDeptAuditUser() {
    return deptAuditUser;
  }
  public void setDeptAuditUser(String deptAuditUser) {
    this.deptAuditUser = deptAuditUser;
  }
  public Date getDeptAuditDate() {
    return deptAuditDate;
  }
  public void setDeptAuditDate(Date deptAuditDate) {
    this.deptAuditDate = deptAuditDate;
  }
  public String getDeptAuditContent() {
    return deptAuditContent;
  }
  public void setDeptAuditContent(String deptAuditContent) {
    this.deptAuditContent = deptAuditContent;
  }
  public String getFinanceAuditUser() {
    return financeAuditUser;
  }
  public void setFinanceAuditUser(String financeAuditUser) {
    this.financeAuditUser = financeAuditUser;
  }
  public Date getFinanceAuditDate() {
    return financeAuditDate;
  }
  public void setFinanceAuditDate(Date financeAuditDate) {
    this.financeAuditDate = financeAuditDate;
  }
  public String getFinanceAuditContent() {
    return financeAuditContent;
  }
  public void setFinanceAuditContent(String financeAuditContent) {
    this.financeAuditContent = financeAuditContent;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public int getChargeYear() {
    return chargeYear;
  }
  public void setChargeYear(int chargeYear) {
    this.chargeYear = chargeYear;
  }
  public int getProjId() {
    return projId;
  }
  public void setProjId(int projId) {
    this.projId = projId;
  }
  public int getCostId() {
    return costId;
  }
  public void setCostId(int costId) {
    this.costId = costId;
  }
  public int getProjSign() {
    return projSign;
  }
  public void setProjSign(int projSign) {
    this.projSign = projSign;
  }
  public String getIsPrint() {
    return isPrint;
  }
  public void setIsPrint(String isPrint) {
    this.isPrint = isPrint;
  }
  public String getExpense() {
    return expense;
  }
  public void setExpense(String expense) {
    this.expense = expense;
  }
  public String getOfEx() {
    return ofEx;
  }
  public void setOfEx(String ofEx) {
    this.ofEx = ofEx;
  }
  public String getBudgetId() {
    return budgetId;
  }
  public void setBudgetId(String budgetId) {
    this.budgetId = budgetId;
  }
  public String getMakeWaste() {
    return makeWaste;
  }
  public void setMakeWaste(String makeWaste) {
    this.makeWaste = makeWaste;
  }
  public String getSettleFlag() {
    return settleFlag;
  }
  public void setSettleFlag(String settleFlag) {
    this.settleFlag = settleFlag;
  }
  
}

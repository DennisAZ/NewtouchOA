package yh.setup.fis.acset.data;

/**
 * 创建帐套用参数
 * @author jpt
 * @date   2007-7-29
 */
public class TDAcsetBuildParam {
  private String accountDbDesc = null;
  private String periodCntrl = null;
  private String makeYM = null;
  private String acctYear = null;
  private String startYM = null;
  private String deptId= null;
  private String contextPath = null;
  private String ifWaibihesuan = null;
  private int rateLength = 0;
  private int rateDecimalLength = 0;
  private int moneyLength = 0;
  private int moneyDecimalLength = 0;
  private String homeCurrDesc = null;
  private String homeCurrSign = null;
  private int firstPeriod = 1;

  public int getFirstPeriod() {
    return firstPeriod;
  }
  public void setFirstPeriod(int firstPeriod) {
    this.firstPeriod = firstPeriod;
  }
  public String getAccountDbDesc() {
    return accountDbDesc;
  }
  public void setAccountDbDesc(String accountDbDesc) {
    this.accountDbDesc = accountDbDesc;
  }
  public String getAcctYear() {
    return acctYear;
  }
  public void setAcctYear(String acctYear) {
    this.acctYear = acctYear;
  }
  public String getContextPath() {
    return contextPath;
  }
  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public String getIfWaibihesuan() {
    return ifWaibihesuan;
  }
  public void setIfWaibihesuan(String ifWaibihesuan) {
    this.ifWaibihesuan = ifWaibihesuan;
  }
  public String getMakeYM() {
    return makeYM;
  }
  public void setMakeYM(String makeYM) {
    this.makeYM = makeYM;
  }
  public int getMoneyDecimalLength() {
    return moneyDecimalLength;
  }
  public void setMoneyDecimalLength(int moneyDecimalLength) {
    this.moneyDecimalLength = moneyDecimalLength;
  }
  public int getMoneyLength() {
    return moneyLength;
  }
  public void setMoneyLength(int moneyLength) {
    this.moneyLength = moneyLength;
  }
  public String getPeriodCntrl() {
    return periodCntrl;
  }
  public void setPeriodCntrl(String periodCntrl) {
    this.periodCntrl = periodCntrl;
  }
  public int getRateDecimalLength() {
    return rateDecimalLength;
  }
  public void setRateDecimalLength(int rateDecimalLength) {
    this.rateDecimalLength = rateDecimalLength;
  }
  public int getRateLength() {
    return rateLength;
  }
  public void setRateLength(int rateLength) {
    this.rateLength = rateLength;
  }
  public String getStartYM() {
    return startYM;
  }
  public void setStartYM(String startYM) {
    this.startYM = startYM;
  }
  public String getHomeCurrDesc() {
    return homeCurrDesc;
  }
  public void setHomeCurrDesc(String homeCurrDesc) {
    this.homeCurrDesc = homeCurrDesc;
  }
  public String getHomeCurrSign() {
    return homeCurrSign;
  }
  public void setHomeCurrSign(String homeCurrSign) {
    this.homeCurrSign = homeCurrSign;
  }
}

package yh.setup.fis.acset.data;

import yh.core.util.YHUtility;
import yh.setup.fis.acset.global.TDAcsetParamFlagConst;
import yh.setup.fis.acset.global.TDAuthConst;


/**
 * 系统参数
 * @author jpt
 * @version 1.0
 * @date 2006-9-12
 */
public class TDCSystemParams {
  /** 帐套数据库编码 **/
  private String acsetDbNo = null;
  //帐套编码
  private String acsetNo = null;
  //帐套名称
  private String acsetName = null;
  //帐务年月
  private String acctYM = null;
  //会计年度
  private String acctYear = null;
  //会计期间
  private int acctPeriod = 1;
  //已经记帐的会计期间数
  private int acctedPeriodCnt = 0;
  //当前会计期间的起始日期
  private String periodStartDate = null;
  //当前会计期间的终止日期
  private String periodEndDate = null;
  //一级科目长度
  private int topSubLength = 0;
  //编码分隔符
  private String codeSplit = null;
  //外币核算
  private int multiCurrFlag = 0;
  //是否采用固定汇率
  private int useFixExchRate = 0;
  //辅币币
  private int aidCurrent = 0;
  //辅币名称
  private String aidCurrentDesc = null;
  //本位币
  private int homeCurrent = 0;
  //本位币名称
  private String homeCurrentDesc = null;
  //汇率金额计算方式
  private int exchRateExpress = 0;
  //汇率处理长度
  private int exchRateLength = 0;
  //汇率小数位
  private int exchRateDecimalLength = 0;
  //金额处理长度
  private int amtLength = 18;
  //金额小数位长度
  private int amtDecimalLength = 2;
  //制单处理
  private int canSaveUnEqualVouch = 0;
  //凭证格式
  private int vouchFormat = 0;
  //凭证管理
  private int canMakeOhtersVouch = 0;
  //送退复核
  private int useCheckPrintVouch = 0;
  //摘要是否携带
  private int copySummary = 0;
  //科目栏目长度
  private int vouchSubjectColLength = 0;
  //明细栏目长度
  private int vouchDetailColLength = 0;
  //金额栏目长度
  private int vouchAmtColLength = 0;
  //分录行高度
  private int entryRowHeight = 0;
  //外币金额长度
  private int vouchForgnAmtLength = 0;
  //本位币金额长度
  private int vouchHomeAmtLenth = 0;
  //凭证摘要长度
  private int vouchSumLength = 0;
  //凭证左侧边距
  private int vouchMarginLeft = 0;
  //凭证上侧边距
  private int vouchMarginTop = 0;
  //凭证编号
  private int vouchNoRule = 0;
  //凭证纸张
  private String vouchPaper = null;
  //凭证纸张格式
  private int vouchPaperFormat = 0;
  //凭证标题字号
  private int vouchTitleTexSize = 0;
  //凭证表头字号
  private int vouchTableHeaderTextSize = 0;
  //凭证正文字号
  private int vouchTextSize = 0;
  //账册标题字号
  private int acctTitleTexSize = 0;
  //账册表头字号
  private int acctTableHeaderTextSize = 0;
  //账册凭证正文字号
  private int acctTextSize = 0;
  //账册摘要长度
  private int acctVouchSumLength = 0;
  //账册外币金额长度
  private int acctForgnAmtLength = 0;
  //账册本位币金额长度
  private int acctHomeAmtLength = 0;
  //账册是否使用千位符
  private int acctUseKiloSplit = 0;
  //账册正文输出行数
  private int acctTexRowCnt = 0;
  //帐册页码
  private int acctBookNoRule = 0;
  //汇率价差科目
  private String exchRateDiffSub = null;
  //损益转入科目
  private String profitLoss2Sub = null;
  //会计期间设置规则
  private int periodRule = 0;
  //凭证年度（长度为 4）
  private String vouchYear = null;
  //凭证月份（长度为 2）
  private String vouchMonth = null;
  //凭证日期（长度为 2）
  private String vouchDate = null;
  //凭证的会计期间
  private String vouchPeriod = null;
  //当前最近的结账时间，格式是：yyyy-mm-dd
  private String maxAcctDate = null;
  //当前会计年度的最大的会计期间
  private int maxPeriod = 0;
  //具有帐务环境的最大的会计年度
  private String maxAcctYear = null;
  //是否打印大写金额
  private int useChineseMoney = 0;
  //存在未记帐凭证时，转汇率价差的控制方式
  //是否已经注册
  private boolean hasRegisted = false;
  //注册类别
  private String registSort = TDAuthConst.REGIST_SORT_NOT_REGIST;
  //帐套机构ID号
  private int acsetOrgSeqId = 0;
  //集成版财务的标记
  private  boolean  fisDependsOnOaEa = false;
  //存在未登账凭证，是否允许转汇率价差
  private int canExchDiffNonAcctedVouch = 0;
  //启用出纳签字
  private boolean isUsedCasherSign = true;
  //允许复核本人制作的凭证
  private boolean isPermitCheckSelf = false;
  //是否已经关闭期初
  private boolean isCloseInit = false;
  //试用期限
  private int remainDays = 0;
  //注册提示信息
  private String registAlertMsrg = null;
  //授权最大账套数
  private int maxAcsetCntPriv = 3;
  //账套版本
  private String acsetVersion = null;
  
  public String getAcsetVersion() {
    return acsetVersion;
  }
  public void setAcsetVersion(String acsetVersion) {
    this.acsetVersion = acsetVersion;
  }
  public int getMaxAcsetCntPriv() {
    return maxAcsetCntPriv;
  }
  public void setMaxAcsetCntPriv(int maxAcsetCntPriv) {
    this.maxAcsetCntPriv = maxAcsetCntPriv;
  }
  public String getRegistAlertMsrg() {
    return registAlertMsrg;
  }
  public void setRegistAlertMsrg(String registAlertMsrg) {
    this.registAlertMsrg = registAlertMsrg;
  }
  public int getRemainDays() {
    return remainDays;
  }
  public void setRemainDays(int remainDays) {
    this.remainDays = remainDays;
  }
  public String getProfitLoss2Sub() {
    return profitLoss2Sub;
  }
  public void setProfitLoss2Sub(String profitLoss2Sub) {
    this.profitLoss2Sub = profitLoss2Sub;
  }
  public boolean isCloseInit() {
    return isCloseInit;
  }
  public void setCloseInit(String closeInitFlag) {
    int intCloseInitFlag = 0;
    if (!YHUtility.isNullorEmpty(closeInitFlag)) {
      intCloseInitFlag = Integer.parseInt(closeInitFlag);
    }
    this.isCloseInit = (intCloseInitFlag == TDAcsetParamFlagConst.CLOSE_INIT_YES);
  }
  public boolean isUsedCasherSign() {
    return isUsedCasherSign;
  }
  public void setUsedCasherSign(String usedCasherAsign) {
    int intUsedCasherAsign = 1;
    if (!YHUtility.isNullorEmpty(usedCasherAsign)) {
      intUsedCasherAsign = Integer.parseInt(usedCasherAsign);
    }
    this.isUsedCasherSign = (intUsedCasherAsign == TDAcsetParamFlagConst.USE_CASHER_ASIGN_YES);
  }
  public boolean isPermitCheckSelf() {
    return isPermitCheckSelf;
  }
  public void setPermitCheckSelf(String permitCheckSelf) {
    int intPermitCheckSelf = 0;
    if (!YHUtility.isNullorEmpty(permitCheckSelf)) {
      intPermitCheckSelf = Integer.parseInt(permitCheckSelf);
    }
    this.isPermitCheckSelf = (intPermitCheckSelf == TDAcsetParamFlagConst.PERMIT_CHECK_SELF_VOUCH);
  }
  public int getAcsetOrgSeqId() {
    return acsetOrgSeqId;
  }
  public void setAcsetOrgSeqId(int acsetOrgSeqId) {
    this.acsetOrgSeqId = acsetOrgSeqId;
  }
  //是否免费注册
  public boolean isFreeRegisted() {
    return registSort.equals(TDAuthConst.REGIST_SORT_FREE_REGIST);
  }
  //是否合作注册
  public boolean isCooperateRegisted() {
    return registSort.equals(TDAuthConst.REGIST_SORT_COOPERATE_REGIST);
  }
  //是否购买注册
  public boolean isBuyRegisted() {
    return registSort.equals(TDAuthConst.REGIST_SORT_BUY_REGIST);
  }
  public boolean isHasRegisted() {
    return hasRegisted;
  }
  public void setHasRegisted(boolean hasRegisted) {
    this.hasRegisted = hasRegisted;
  }
  public String getRegistSort() {
    return registSort;
  }
  public void setRegistSort(String registSort) {
    this.registSort = registSort;
  }
  
  public String getAcsetDbNo() {
    return acsetDbNo;
  }
  public void setAcsetDbNo(String acsetDbNo) {
    this.acsetDbNo = acsetDbNo;
  }
  public String getCodeSplit() {
    return codeSplit;
  }
  public void setCodeSplit(String codeSplit) {
    this.codeSplit = codeSplit;
  }
  public String getAcctYear() {
    return acctYear;
  }
  public void setAcctYear(String acctYear) {
    this.acctYear = acctYear;
  }
  public int getAcctPeriod() {
    return acctPeriod;
  }
  public void setAcctPeriod(int acctPeriod) {
    this.acctPeriod = acctPeriod;
  }
  public String getAcctYM() {
    return acctYM;
  }
  public void setAcctYM(String acctYM) {
    this.acctYM = acctYM;
  }
  public String getVouchMonth() {
    return vouchMonth;
  }
  public void setVouchMonth(String vouchMonth) {
    this.vouchMonth = vouchMonth;
  }
  public String getVouchYear() {
    return vouchYear;
  }
  public void setVouchYear(String vouchYear) {
    this.vouchYear = vouchYear;
  }
  public String getDefaultVouchDate() {
    return this.getVouchYear() + "-" + this.getVouchMonth() + "-" + this.getVouchDate();
  }
  public String getVouchDate() {
    return vouchDate;
  }
  public void setVouchDate(String vouchDate) {
    this.vouchDate = vouchDate;
  }
  public String getMaxAcctDate() {
    return maxAcctDate;
  }
  public void setMaxAcctDate(String maxAcctDate) {
    this.maxAcctDate = maxAcctDate;
  }
  public String getHomeCurrentDesc() {
    return homeCurrentDesc;
  }
  public void setHomeCurrentDesc(String homeCurrentDesc) {
    this.homeCurrentDesc = homeCurrentDesc;
  }
  public String getVouchPeriod() {
    return vouchPeriod;
  }
  public void setVouchPeriod(String vouchPeriod) {
    this.vouchPeriod = vouchPeriod;
  }
  public String getAcsetName() {
    return acsetName;
  }
  public void setAccountName(String acsetName) {
    this.acsetName = acsetName;
  }
  public String getPeriodStartDate() {
    return periodStartDate;
  }
  public void setPeriodStartDate(String periodStartDate) {
    this.periodStartDate = periodStartDate;
  }
  public String getPeriodEndDate() {
    return periodEndDate;
  }
  public void setPeriodEndDate(String periodEndDate) {
    this.periodEndDate = periodEndDate;
  }
  public boolean isUseChineseMoney() {
    return this.useChineseMoney == TDAcsetParamFlagConst.USE_CHINESE_MONEY_YES;
  }
  public void setUseChineseMoney(int useChineseMoney) {
    this.useChineseMoney = useChineseMoney;
  }
  public String getMaxAcctYear() {
    return maxAcctYear;
  }
  public void setMaxAcctYear(String maxAcctYear) {
    this.maxAcctYear = maxAcctYear;
  }
  public boolean isCanExchDiffNonAcctedVouch() {
    return canExchDiffNonAcctedVouch == TDAcsetParamFlagConst.EXCH_DIFF_NON_ACCTED_VOUCH_YES;
  }
  public void setCanExchDiffNonAcctedVouch(int canExchDiffNonAcctedVouch) {
    this.canExchDiffNonAcctedVouch = canExchDiffNonAcctedVouch;
  }
  public boolean isCanMakeOhtersVouch() {
    return canMakeOhtersVouch == TDAcsetParamFlagConst.CAN_VOUCH_OTHERS_YES;
  }
  public void setCanMakeOhtersVouch(int canMakeOhtersVouch) {
    this.canMakeOhtersVouch = canMakeOhtersVouch;
  }
  public boolean isCanSaveUnEqualVouch() {
    return canSaveUnEqualVouch == TDAcsetParamFlagConst.VOUCH_SAVE_UNEQUAL_YES;
  }
  public void setCanSaveUnEqualVouch(int canSaveUnEqualVouch) {
    this.canSaveUnEqualVouch = canSaveUnEqualVouch;
  }
  public boolean isCopySummary() {
    return copySummary == TDAcsetParamFlagConst.COPY_SUMMARY_YES;
  }
  public void setCopySummary(int copySummary) {
    this.copySummary = copySummary;
  }
  public int getCopySummary() {
    return copySummary;
  }
  public String getExchRateDiffSub() {
    return exchRateDiffSub;
  }
  public void setExchRateDiffSub(String exchRateDiffSub) {
    this.exchRateDiffSub = exchRateDiffSub;
  }
  public int getMultiCurrFlag() {
    return multiCurrFlag;
  }
  public void setMultiCurrFlag(int multiCurrFlag) {
    this.multiCurrFlag = multiCurrFlag;
  }
  public boolean isUseCheckPrintVouch() {
    return useCheckPrintVouch == TDAcsetParamFlagConst.VOUCH_PRINT_CHECK_YES;
  }
  public void setUseCheckPrintVouch(int useCheckPrintVouch) {
    this.useCheckPrintVouch = useCheckPrintVouch;
  }
  public boolean isUseFixExchRate() {
    return useFixExchRate == TDAcsetParamFlagConst.FIX_EXCH_RATE_YES;
  }
  public void setUseFixExchRate(int useFixExchRate) {
    this.useFixExchRate = useFixExchRate;
  }
  public boolean isVouchNoRuleAuto() {
    return vouchNoRule == TDAcsetParamFlagConst.VOUCH_NO_RULE_AUTO;
  }
  public void setVouchNoRule(int vouchNoRule) {
    this.vouchNoRule = vouchNoRule;
  }
  public int getExchRateDecimalLength() {
    return exchRateDecimalLength;
  }
  public void setExchRateDecimalLength(int exchRateDecimalLength) {
    this.exchRateDecimalLength = exchRateDecimalLength;
  }
  public int getExchRateLength() {
    return exchRateLength;
  }
  public void setExchRateLength(int exchRateLength) {
    this.exchRateLength = exchRateLength;
  }
  public int getHomeCurrent() {
    return homeCurrent;
  }
  public void setHomeCurrent(int homeCurrent) {
    this.homeCurrent = homeCurrent;
  }
  public int getMaxPeriod() {
    return maxPeriod;
  }
  public void setMaxPeriod(int maxPeriod) {
    this.maxPeriod = maxPeriod;
  }
  public int getTopSubLength() {
    return topSubLength;
  }
  public void setTopSubLength(int topSubLength) {
    this.topSubLength = topSubLength;
  }
  public int getVouchSumLength() {
    return vouchSumLength;
  }
  public void setVouchSumLength(int vouchSumLength) {
    this.vouchSumLength = vouchSumLength;
  }
  public void setAcsetName(String acsetName) {
    this.acsetName = acsetName;
  }
  public int getAidCurrent() {
    return aidCurrent;
  }
  public void setAidCurrent(int aidCurrent) {
    this.aidCurrent = aidCurrent;
  }
  public String getAidCurrentDesc() {
    return aidCurrentDesc;
  }
  public void setAidCurrentDesc(String aidCurrentDesc) {
    this.aidCurrentDesc = aidCurrentDesc;
  }
  public int getPeriodRule() {
    return periodRule;
  }
  public void setPeriodRule(int periodRule) {
    this.periodRule = periodRule;
  }
  public boolean isExchRateExpressDiv() {
    return exchRateExpress == TDAcsetParamFlagConst.EXCH_RATE_EXPRESS_DIV;
  }
  public int getExchRateExpress() {
    return exchRateExpress;
  }
  public void setExchRateExpress(int exchRateExpress) {
    this.exchRateExpress = exchRateExpress;
  }
  public int getVouchFormat() {
    return vouchFormat;
  }
  public void setVouchFormat(int vouchFormat) {
    this.vouchFormat = vouchFormat;
  }
  public int getAcctBookNoRule() {
    return acctBookNoRule;
  }
  public void setAcctBookNoRule(int acctBookNoRule) {
    this.acctBookNoRule = acctBookNoRule;
  }
  public int getAcctForgnAmtLength() {
    return acctForgnAmtLength;
  }
  public void setAcctForgnAmtLength(int acctForgnAmtLength) {
    this.acctForgnAmtLength = acctForgnAmtLength;
  }
  public int getAcctHomeAmtLength() {
    return acctHomeAmtLength;
  }
  public void setAcctHomeAmtLength(int acctHomeAmtLength) {
    this.acctHomeAmtLength = acctHomeAmtLength;
  }
  public int getAcctTableHeaderTextSize() {
    return acctTableHeaderTextSize;
  }
  public void setAcctTableHeaderTextSize(int acctTableHeaderTextSize) {
    this.acctTableHeaderTextSize = acctTableHeaderTextSize;
  }
  public int getAcctTexRowCnt() {
    return acctTexRowCnt;
  }
  public void setAcctTexRowCnt(int acctTexRowCnt) {
    this.acctTexRowCnt = acctTexRowCnt;
  }
  public int getAcctTextSize() {
    return acctTextSize;
  }
  public void setAcctTextSize(int acctTextSize) {
    this.acctTextSize = acctTextSize;
  }
  public int getAcctTitleTexSize() {
    return acctTitleTexSize;
  }
  public void setAcctTitleTexSize(int acctTitleTexSize) {
    this.acctTitleTexSize = acctTitleTexSize;
  }
  public int getAcctUseKiloSplit() {
    return acctUseKiloSplit;
  }
  public boolean isUseKiloSplit() {
    return this.acctUseKiloSplit == TDAcsetParamFlagConst.USE_KILO_SPLIT_YES;
  }
  public void setAcctUseKiloSplit(int acctUseKiloSplit) {
    this.acctUseKiloSplit = acctUseKiloSplit;
  }
  public int getAcctVouchSumLength() {
    return acctVouchSumLength;
  }
  public void setAcctVouchSumLength(int acctVouchSumLength) {
    this.acctVouchSumLength = acctVouchSumLength;
  }
  public int getEntryRowHeight() {
    return entryRowHeight;
  }
  public void setEntryRowHeight(int entryRowHeight) {
    this.entryRowHeight = entryRowHeight;
  }
  public int getVouchAmtColLength() {
    return vouchAmtColLength;
  }
  public void setVouchAmtColLength(int vouchAmtColLength) {
    this.vouchAmtColLength = vouchAmtColLength;
  }
  public int getVouchDetailColLength() {
    return vouchDetailColLength;
  }
  public void setVouchDetailColLength(int vouchDetailColLength) {
    this.vouchDetailColLength = vouchDetailColLength;
  }
  public int getVouchForgnAmtLength() {
    return vouchForgnAmtLength;
  }
  public void setVouchForgnAmtLength(int vouchForgnAmtLength) {
    this.vouchForgnAmtLength = vouchForgnAmtLength;
  }
  public int getVouchHomeAmtLenth() {
    return vouchHomeAmtLenth;
  }
  public void setVouchHomeAmtLenth(int vouchHomeAmtLenth) {
    this.vouchHomeAmtLenth = vouchHomeAmtLenth;
  }
  public String getVouchPaper() {
    return vouchPaper;
  }
  public void setVouchPaper(String vouchPaper) {
    this.vouchPaper = vouchPaper;
  }
  public int getVouchPaperFormat() {
    return vouchPaperFormat;
  }
  public void setVouchPaperFormat(int vouchPaperFormat) {
    this.vouchPaperFormat = vouchPaperFormat;
  }
  public int getVouchSubjectColLength() {
    return vouchSubjectColLength;
  }
  public void setVouchSubjectColLength(int vouchSubjectColLength) {
    this.vouchSubjectColLength = vouchSubjectColLength;
  }
  public int getVouchTableHeaderTextSize() {
    return vouchTableHeaderTextSize;
  }
  public void setVouchTableHeaderTextSize(int vouchTableHeaderTextSize) {
    this.vouchTableHeaderTextSize = vouchTableHeaderTextSize;
  }
  public int getVouchTextSize() {
    return vouchTextSize;
  }
  public void setVouchTextSize(int vouchTextSize) {
    this.vouchTextSize = vouchTextSize;
  }
  public int getVouchTitleTexSize() {
    return vouchTitleTexSize;
  }
  public void setVouchTitleTexSize(int vouchTitleTexSize) {
    this.vouchTitleTexSize = vouchTitleTexSize;
  }
  public int getVouchMarginTop() {
    return vouchMarginTop;
  }
  public void setVouchMarginTop(int vouchMarginTop) {
    this.vouchMarginTop = vouchMarginTop;
  }
  public int getVouchMarginLeft() {
    return vouchMarginLeft;
  }
  public void setVouchMarginLeft(int vouchMarginLeft) {
    this.vouchMarginLeft = vouchMarginLeft;
  }
  public int getVouchNoRule() {
    return vouchNoRule;
  }
  public String getAcsetNo() {
    return acsetNo;
  }
  public void setAcsetNo(String acsetNo) {
    this.acsetNo = acsetNo;
  }
  public int getAmtDecimalLength() {
    return amtDecimalLength;
  }
  public void setAmtDecimalLength(int amtDecimalLength) {
    this.amtDecimalLength = amtDecimalLength;
  }
  public int getAmtLength() {
    return amtLength;
  }
  public void setAmtLength(int amtLength) {
    this.amtLength = amtLength;
  }
  public int getAcctedPeriodCnt() {
    return acctedPeriodCnt;
  }
  public void setAcctedPeriodCnt(int acctedPeriodCnt) {
    this.acctedPeriodCnt = acctedPeriodCnt;
  }
  public boolean isFisDependsOnOaEa() {
    return fisDependsOnOaEa;
  }
  public void setFisDependsOnOaEa(boolean fisDependsOnOaEa) {
    this.fisDependsOnOaEa = fisDependsOnOaEa;
  }
}

package yh.subsys.oa.asset.data;

import java.sql.Date;

public class YHCpCptlRecord { 
  private int seqId;
  private int cptlId; //CPTL_ID int 资产名称 跟cp_cptl_info表 SEQ_ID 有关系
  private int cpreQty;//CPRE_QTY int 数量
  private String cprePlace;//CPRE_PLACE varchar(200) 地点
  private String cpreUser;//CPRE_USER varchar(200)  登录用户名昵称
  private String cpreKeeper;//CPRE_KEEPER varchar(200)
  private Date cpreDate;//CPRE_DATE date 创建时间
  private String cpreRecorder;//CPRE_RECORDER varchar(200) 登录用户名
  private String cpreMemo;//CPRE_MEMO  text 备注
  private String cpreReason;//CPRE_REASON  varchar(200)原因
  private String cpreFlag;//CPRE_FLAG int(反库单是2，利用单是1)
  private int deptId;//DEPT_ID int （1）
  private int runId;//RUN  int    （0）
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getCptlId() {
    return cptlId;
  }
  public void setCptlId(int cptlId) {
    this.cptlId = cptlId;
  }
  public int getCpreQty() {
    return cpreQty;
  }
  public void setCpreQty(int cpreQty) {
    this.cpreQty = cpreQty;
  }
  public String getCprePlace() {
    return cprePlace;
  }
  public void setCprePlace(String cprePlace) {
    this.cprePlace = cprePlace;
  }
  public String getCpreUser() {
    return cpreUser;
  }
  public void setCpreUser(String cpreUser) {
    this.cpreUser = cpreUser;
  }
  public String getCpreKeeper() {
    return cpreKeeper;
  }
  public void setCpreKeeper(String cpreKeeper) {
    this.cpreKeeper = cpreKeeper;
  }
  public Date getCpreDate() {
    return cpreDate;
  }
  public void setCpreDate(Date cpreDate) {
    this.cpreDate = cpreDate;
  }
  public String getCpreRecorder() {
    return cpreRecorder;
  }
  public void setCpreRecorder(String cpreRecorder) {
    this.cpreRecorder = cpreRecorder;
  }
  public String getCpreMemo() {
    return cpreMemo;
  }
  public void setCpreMemo(String cpreMemo) {
    this.cpreMemo = cpreMemo;
  }
  public String getCpreReason() {
    return cpreReason;
  }
  public void setCpreReason(String cpreReason) {
    this.cpreReason = cpreReason;
  }
  public String getCpreFlag() {
    return cpreFlag;
  }
  public void setCpreFlag(String cpreFlag) {
    this.cpreFlag = cpreFlag;
  }
  public int getDeptId() {
    return deptId;
  }
  public void setDeptId(int deptId) {
    this.deptId = deptId;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
}

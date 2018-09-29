package yh.setup.fis.acset.data;

/**
 * 帐套定义类
 * @author jpt
 * @version 1.0
 * @date 2006-10-18
 */
public class TDAcset {
  /** 流水号 **/
  private int seqId = 0;
  /** 组织流水号 **/
  private int orgSeqId = 0;
  /** 帐套编码 **/
  private String acsetNo = null;
  /** 帐套数据库编码 **/
  private String acsetDbNo = null;
  /** 帐套描述 **/
  private String acsetDesc = null;
  /** 税号 **/
  private String taxNo = null;
  /** 使用标记 **/
  private String usedFlag = null;
  /** 创建年月 **/
  private String makeAcYm = null;
  /** 会计年度 **/
  private String pAcctYear = null;
  /** 帐务年月 **/
  private String acctYm = null;

  /**
   *
   */
  public int getSeqId() {
    return this.seqId;
  }

  /**
   *
   * @param seqId
   */
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  /**
   *
   */
  public int getOrgSeqId() {
    return this.orgSeqId;
  }

  /**
   *
   * @param orgSeqId
   */
  public void setOrgSeqId(int orgSeqId) {
    this.orgSeqId = orgSeqId;
  }

  /**
   *
   */
  public String getAcsetNo() {
    return this.acsetNo;
  }

  /**
   *
   * @param acsetNo
   */
  public void setAcsetNo(String acsetNo) {
    this.acsetNo = acsetNo;
  }

  /**
   *
   */
  public String getAcsetDbNo() {
    return this.acsetDbNo;
  }

  /**
   *
   * @param acsetDbNo
   */
  public void setAcsetDbNo(String acsetDbNo) {
    this.acsetDbNo = acsetDbNo;
  }

  /**
   *
   */
  public String getAcsetDesc() {
    return this.acsetDesc;
  }

  /**
   *
   * @param acsetDesc
   */
  public void setAcsetDesc(String acsetDesc) {
    this.acsetDesc = acsetDesc;
  }

  /**
   *
   */
  public String getTaxNo() {
    return this.taxNo;
  }

  /**
   *
   * @param taxNo
   */
  public void setTaxNo(String taxNo) {
    this.taxNo = taxNo;
  }

  /**
   *
   */
  public String getUsedFlag() {
    return this.usedFlag;
  }

  /**
   *
   * @param usedFlag
   */
  public void setUsedFlag(String usedFlag) {
    this.usedFlag = usedFlag;
  }

  /**
   *
   */
  public String getMakeAcYm() {
    return this.makeAcYm;
  }

  /**
   *
   * @param makeAcYm
   */
  public void setMakeAcYm(String makeAcYm) {
    this.makeAcYm = makeAcYm;
  }

  /**
   *
   */
  public String getPAcctYear() {
    return this.pAcctYear;
  }

  /**
   *
   * @param pAcctYear
   */
  public void setPAcctYear(String pAcctYear) {
    this.pAcctYear = pAcctYear;
  }

  /**
   *
   */
  public String getAcctYm() {
    return this.acctYm;
  }

  /**
   *
   * @param acctYm
   */
  public void setAcctYm(String acctYm) {
    this.acctYm = acctYm;
  }
}

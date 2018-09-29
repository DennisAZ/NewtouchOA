package yh.core.data;

public class YHDatabase {
  /**  **/
  private int seqId = 0;
  /**  **/
  private String dbNo = null;
  /**  **/
  private String dbName = null;
  /**  **/
  private String dbDesc = null;
  /** 数据源名称 **/
  private String dsName = null; 
  /** 数据库管理系统 **/
  private String dbmsName = null;
  /** 数据库密码 **/
  private String password = null;

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
  public String getDbNo() {
    return this.dbNo;
  }

  /**
   *
   * @param dbNo
   */
  public void setDbNo(String dbNo) {
    this.dbNo = dbNo;
  }

  /**
   *
   */
  public String getDbName() {
    return this.dbName;
  }

  /**
   *
   * @param dbName
   */
  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  /**
   *
   */
  public String getDbDesc() {
    return this.dbDesc;
  }

  /**
   *
   * @param dbDesc
   */
  public void setDbDesc(String dbDesc) {
    this.dbDesc = dbDesc;
  }

  public String getDsName() {
    return dsName;
  }

  public void setDsName(String dsName) {
    this.dsName = dsName;
  }

  public String getDbmsName() {
    return dbmsName;
  }

  public void setDbmsName(String dbmsName) {
    this.dbmsName = dbmsName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

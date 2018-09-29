package yh.setup.fis.acset.data;


public class TDCCodeRecord {
  /** 流水号 **/
  protected int seqId = 0;
  /** 编码 **/
  protected String code = null;
  /** 名称 **/
  protected String name = null;
  /** 名称 **/
  protected String desc = null;
  /** 字段值 **/
  private String fieldValue = null;
  /** 父级记录编码 **/
  protected String parentCode = null;

  public String getCode() {
    return code == null ? String.valueOf(seqId) : code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getDesc() {
    return desc == null ? name : desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public int getSeqId() {
    return seqId > 0 ? seqId : (code == null ? 0 : Integer.parseInt(code));
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getParentCode() {
    return parentCode;
  }
  public void setParentCode(String parentCode) {
    this.parentCode = parentCode;
  }
  public String getName() {
    return name == null ? desc : name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getFieldValue() {
    return fieldValue;
  }
  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }
}

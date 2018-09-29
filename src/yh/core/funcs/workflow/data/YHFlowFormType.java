package yh.core.funcs.workflow.data;

import java.util.Date;

public class YHFlowFormType {
  private int seqId;
  private String formName;
  private String printModel;
  private String printModelShort;
  private int deptId;
  private String script;
  private String css;
  private int itemMax;
  private int formId;
  private int versionNo;
  public int getFormId() {
    return formId;
  }
  public void setFormId(int formId) {
    this.formId = formId;
  }
  public int getVersionNo() {
    return versionNo;
  }
  public void setVersionNo(int versionNo) {
    this.versionNo = versionNo;
  }
  public Date getVersionTime() {
    return versionTime;
  }
  public void setVersionTime(Date versionTime) {
    this.versionTime = versionTime;
  }
  private Date versionTime;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getFormName() {
    return formName;
  }
  public void setFormName(String formName) {
    this.formName = formName;
  }
  public String getPrintModel() {
    return printModel;
  }
  public void setPrintModel(String printModel) {
    this.printModel = printModel;
  }
  public String getPrintModelShort() {
    return printModelShort;
  }
  public void setPrintModelShort(String printModelShort) {
    this.printModelShort = printModelShort;
  }
  public int getDeptId() {
    return deptId;
  }
  public void setDeptId(int deptId) {
    this.deptId = deptId;
  }
  public String getScript() {
    return script;
  }
  public void setScript(String script) {
    this.script = script;
  }
  public String getCss() {
    return css;
  }
  public void setCss(String css) {
    this.css = css;
  }
  public int getItemMax() {
    return itemMax;
  }
  public void setItemMax(int itemMax) {
    this.itemMax = itemMax;
  }
  @Override
  public String toString(){
    return "YHFlowFormType [css=" + css + ", deptId=" + deptId + ", formName="
        + formName + ", itemMax=" + itemMax + ", printModel=" + printModel
        + ", printModelShort=" + printModelShort + ", script=" + script
        + ", seqId=" + seqId + "]";
  }
  
}

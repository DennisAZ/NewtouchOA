package yh.subsys.oa.asset.data;
public class YHCpcptl {
  private int seqId;
  private String cptlId;
  private String cptlNo;//编号
  private String cptlName;//固定资产名称 
  private String cptlSpec;//规格型号
  private int cptlQty;//数量
  private String useDept;//使用地点
  private String cpreUser;// 使用人签字
  private String keeper;// 专管员签字
  private String cpreMemo;// 备注 
  private String deptId;
  private String typeId;
  private String useFor;
  private String useState;
  private String useUser; 
  private String cpreFlag;
  private int runId;
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public String getCpreFlag() {
    return cpreFlag;
  }
  public void setCpreFlag(String cpreFlag) {
    this.cpreFlag = cpreFlag;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public String getTypeId() {
    return typeId;
  }
  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }
  public String getUseFor() {
    return useFor;
  }
  public void setUseFor(String useFor) {
    this.useFor = useFor;
  }
  public String getUseState() {
    return useState;
  }
  public void setUseState(String useState) {
    this.useState = useState;
  }
  public String getUseUser() {
    return useUser;
  }
  public void setUseUser(String useUser) {
    this.useUser = useUser;
  }
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getCptlId() {
    return cptlId;
  }
  public void setCptlId(String cptlId) {
    this.cptlId = cptlId;
  }
  public String getCptlNo() {
    return cptlNo;
  }
  public void setCptlNo(String cptlNo) {
    this.cptlNo = cptlNo;
  }
  public String getCptlName() {
    return cptlName;
  }
  public void setCptlName(String cptlName) {
    this.cptlName = cptlName;
  }
  public String getCptlSpec() {
    return cptlSpec;
  }
  public void setCptlSpec(String cptlSpec) {
    this.cptlSpec = cptlSpec;
  }
  public int getCptlQty() {
    return cptlQty;
  }
  public void setCptlQty(int cptlQty) {
    this.cptlQty = cptlQty;
  }
  public String getUseDept() {
    return useDept;
  }
  public void setUseDept(String useDept) {
    this.useDept = useDept;
  }
  public String getCpreUser() {
    return cpreUser;
  }
  public void setCpreUser(String cpreUser) {
    this.cpreUser = cpreUser;
  }
  public String getKeeper() {
    return keeper;
  }
  public void setKeeper(String keeper) {
    this.keeper = keeper;
  }
  public String getCpreMemo() {
    return cpreMemo;
  }
  public void setCpreMemo(String cpreMemo) {
    this.cpreMemo = cpreMemo;
  }
}

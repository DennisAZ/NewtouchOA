package yh.subsys.oa.asset.data;

import java.io.Serializable;
import java.util.Date;

public class YHCpCptlInfo implements Serializable {
  private int seqId; // 主键
  private String cptlNo;//固定资产编号
  private String cptlName;//名称
  private String typeId;//分类代码
  private double cptlVal;//number 0.00
  private String cptlSpec;//资产类别 
  private int cptlQty;//1
  private Date createDate;//2009-09-29 09:43:59 
  private String safekeeping;//存放地点 
  private String keeper;//管理人
  private String remark;//备注
  private Date listDate;//单据日期 
  private String noDeal;//未处置
  private String inFinance;//已入帐
  private String useUser;//登录名
  private String useState;//未使用/不需用/在用
  private String useFor;//使用方向
  private Date afterIndate;//保修截至日 
  private Date getDate;//取得日期 
  private String useDept;//部门
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
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
  public String getTypeId() {
    return typeId;
  }
  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }
  public double getCptlVal() {
    return cptlVal;
  }
  public void setCptlVal(double cptlVal) {
    this.cptlVal = cptlVal;
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
  public Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  public String getSafekeeping() {
    return safekeeping;
  }
  public void setSafekeeping(String safekeeping) {
    this.safekeeping = safekeeping;
  }
  public String getKeeper() {
    return keeper;
  }
  public void setKeeper(String keeper) {
    this.keeper = keeper;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public Date getListDate() {
    return listDate;
  }
  public void setListDate(Date listDate) {
    this.listDate = listDate;
  }
  public String getNoDeal() {
    return noDeal;
  }
  public void setNoDeal(String noDeal) {
    this.noDeal = noDeal;
  }
  public String getInFinance() {
    return inFinance;
  }
  public void setInFinance(String inFinance) {
    this.inFinance = inFinance;
  }
  public String getUseUser() {
    return useUser;
  }
  public void setUseUser(String useUser) {
    this.useUser = useUser;
  }
  public String getUseState() {
    return useState;
  }
  public void setUseState(String useState) {
    this.useState = useState;
  }
  public String getUseFor() {
    return useFor;
  }
  public void setUseFor(String useFor) {
    this.useFor = useFor;
  }
  public Date getAfterIndate() {
    return afterIndate;
  }
  public void setAfterIndate(Date afterIndate) {
    this.afterIndate = afterIndate;
  }
  public Date getGetDate() {
    return getDate;
  }
  public void setGetDate(Date getDate) {
    this.getDate = getDate;
  }
  public String getUseDept() {
    return useDept;
  }
  public void setUseDept(String useDept) {
    this.useDept = useDept;
  }

}

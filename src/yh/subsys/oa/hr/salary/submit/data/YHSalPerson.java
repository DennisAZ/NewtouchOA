package yh.subsys.oa.hr.salary.submit.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class YHSalPerson {
  
  private int hsdId;
  private int sdId;
  private int userId;
  private String userName;
  private int flowId;
  private Map<String,Double> Smap;
  private List<String> Slist;
  
  private double allBase;
  private double pensionBase;
  private double pensionU; 
  private double pensionP; 
  private double medicalBase; 
  private double medicalU;
  private double medicalP; 
  private double fertilityBase;
  private double fertilityU; 
  private double unemploymentBase; 
  private double unemploymentU; 
  private double unemploymentP; 
  private double injuriesBase;
  private double injuriesU; 
  private double housingBase;
  private double housingU; 
  private double housingP;
  
  private Date insuranceDate;
  private String memo;
  private String insuranceOther;
  
  public int getHsdId() {
    return hsdId;
  }
  public void setHsdId(int hsdId) {
    this.hsdId = hsdId;
  }
  public int getSdId() {
    return sdId;
  }
  public void setSdId(int sdId) {
    this.sdId = sdId;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public int getFlowId() {
    return flowId;
  }
  public void setFlowId(int flowId) {
    this.flowId = flowId;
  }
  public Map<String, Double> getSmap() {
    return Smap;
  }
  public void setSmap(Map<String, Double> smap) {
    Smap = smap;
  }
  public List<String> getSlist() {
    return Slist;
  }
  public void setSlist(List<String> slist) {
    Slist = slist;
  }
public double getAllBase() {
	return allBase;
}
public void setAllBase(double allBase) {
	this.allBase = allBase;
}
public double getPensionBase() {
	return pensionBase;
}
public void setPensionBase(double pensionBase) {
	this.pensionBase = pensionBase;
}
public double getPensionU() {
	return pensionU;
}
public void setPensionU(double pensionU) {
	this.pensionU = pensionU;
}
public double getPensionP() {
	return pensionP;
}
public void setPensionP(double pensionP) {
	this.pensionP = pensionP;
}
public double getMedicalBase() {
	return medicalBase;
}
public void setMedicalBase(double medicalBase) {
	this.medicalBase = medicalBase;
}
public double getMedicalU() {
	return medicalU;
}
public void setMedicalU(double medicalU) {
	this.medicalU = medicalU;
}
public double getMedicalP() {
	return medicalP;
}
public void setMedicalP(double medicalP) {
	this.medicalP = medicalP;
}
public double getFertilityBase() {
	return fertilityBase;
}
public void setFertilityBase(double fertilityBase) {
	this.fertilityBase = fertilityBase;
}
public double getFertilityU() {
	return fertilityU;
}
public void setFertilityU(double fertilityU) {
	this.fertilityU = fertilityU;
}
public double getUnemploymentBase() {
	return unemploymentBase;
}
public void setUnemploymentBase(double unemploymentBase) {
	this.unemploymentBase = unemploymentBase;
}
public double getUnemploymentU() {
	return unemploymentU;
}
public void setUnemploymentU(double unemploymentU) {
	this.unemploymentU = unemploymentU;
}
public double getUnemploymentP() {
	return unemploymentP;
}
public void setUnemploymentP(double unemploymentP) {
	this.unemploymentP = unemploymentP;
}
public double getInjuriesBase() {
	return injuriesBase;
}
public void setInjuriesBase(double injuriesBase) {
	this.injuriesBase = injuriesBase;
}
public double getInjuriesU() {
	return injuriesU;
}
public void setInjuriesU(double injuriesU) {
	this.injuriesU = injuriesU;
}
public double getHousingBase() {
	return housingBase;
}
public void setHousingBase(double housingBase) {
	this.housingBase = housingBase;
}
public double getHousingU() {
	return housingU;
}
public void setHousingU(double housingU) {
	this.housingU = housingU;
}
public double getHousingP() {
	return housingP;
}
public void setHousingP(double housingP) {
	this.housingP = housingP;
}
public Date getInsuranceDate() {
  return insuranceDate;
}
public void setInsuranceDate(Date insuranceDate) {
  this.insuranceDate = insuranceDate;
}
public String getMemo() {
  return memo;
}
public void setMemo(String memo) {
  this.memo = memo;
}
public String getInsuranceOther() {
  return insuranceOther;
}
public void setInsuranceOther(String insuranceOther) {
  this.insuranceOther = insuranceOther;
}

  
  
}

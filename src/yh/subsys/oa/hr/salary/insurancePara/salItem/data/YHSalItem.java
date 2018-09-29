package yh.subsys.oa.hr.salary.insurancePara.salItem.data;

public class YHSalItem {
 private int seqId;
 private int slaitemId;
private String itemName;
 private String isprint;
 private String iscomputer;
 private String	formula;
 private String	formulaname;
 private String	isreport;
public int getSeqId() {
	return seqId;
}
public void setSeqId(int seqId) {
	this.seqId = seqId;
}



public int getSlaitemId() {
	return slaitemId;
}
public void setSlaitemId(int slaitemId) {
	
	this.slaitemId = slaitemId;
}
public String getItemName() {
	if(itemName==null){
		return "";
	}
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
public String getIsprint() {
	if(isprint==null){
		return "";
	}
	return isprint;
}
public void setIsprint(String isprint) {
	this.isprint = isprint;
}
public String getIscomputer() {
	if(iscomputer==null){
		return "";
	}
	return iscomputer;
}
public void setIscomputer(String iscomputer) {
	this.iscomputer = iscomputer;
}
public String getFormula() {
	if(formula==null){
		return "";
	}
	return formula;
}
public void setFormula(String formula) {
	this.formula = formula;
}
public String getFormulaname() {
	if(formulaname==null){
		return "";
	}
	return formulaname;
}
public void setFormulaname(String formulaname) {
	this.formulaname = formulaname;
}
public String getIsreport() {
	if(isreport==null){
		return "";
	}
	return isreport;
}
public void setIsreport(String isreport) {
	this.isreport = isreport;
}

}

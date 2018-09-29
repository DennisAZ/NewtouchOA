package yh.subsys.oa.officeProduct.officeType.data;

import java.util.List;

public class YHOfficeDepository {
	private int seqId ;	//自增长
	private String depositoryName;	//库名称
	private String officeTypeId;		//????
	private String deptId; //	所属部门ID
	private String manager; //		库管理员
	private String proKeeper; //物品调度员
	private List<YHOfficeType> officeTypes;
	
	public List<YHOfficeType> getOfficeTypes() {
		return officeTypes;
	}

	public void setOfficeTypes(List<YHOfficeType> officeTypes) {
		this.officeTypes = officeTypes;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getDepositoryName() {
		return depositoryName;
	}

	public void setDepositoryName(String depositoryName) {
		this.depositoryName = depositoryName;
	}

	public String getOfficeTypeId() {
		return officeTypeId;
	}

	public void setOfficeTypeId(String officeTypeId) {
		this.officeTypeId = officeTypeId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getProKeeper() {
		return proKeeper;
	}

	public void setProKeeper(String proKeeper) {
		this.proKeeper = proKeeper;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

/**
 * 
 */
package yh.core.funcs.netdisk.data;

/**
 * @author Administrator
 * 
 */
public class YHNetdisk {

	private int seqId;
	private String diskName;
	private String diskPath;
	private String newUser;
	private String manageUser;
	private String userId;
	private int diskNo;
	private int spaceLimit;
	private String orderBy;
	private String ascDesc;
	private String downUser;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getDiskName() {
		return diskName;
	}

	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}

	public String getDiskPath() {
		return diskPath;
	}

	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}

	public String getNewUser() {
		return newUser;
	}

	public void setNewUser(String newUser) {
		this.newUser = newUser;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getManageUser() {
		return manageUser;
	}

	public void setManageUser(String manageUser) {
		this.manageUser = manageUser;
	}

	public int getDiskNo() {
		return diskNo;
	}

	public void setDiskNo(int diskNo) {
		this.diskNo = diskNo;
	}

	public int getSpaceLimit() {
		return spaceLimit;
	}

	public void setSpaceLimit(int spaceLimit) {
		this.spaceLimit = spaceLimit;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getAscDesc() {
		return ascDesc;
	}

	public void setAscDesc(String ascDesc) {
		this.ascDesc = ascDesc;
	}

	public String getDownUser() {
		return downUser;
	}

	public void setDownUser(String downUser) {
		this.downUser = downUser;
	}

}

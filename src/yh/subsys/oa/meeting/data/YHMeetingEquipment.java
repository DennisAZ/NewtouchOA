package yh.subsys.oa.meeting.data;

public class YHMeetingEquipment {

  private int seqId;
  private String equipmentNo;        //设备编号
  private String equipmentName;   //设备名称/型号
  private String equipmentStatus;    //设备状态
  private String remark;   //设备描述
  private int groupYn;            //同类设备
  private int mrId;               //所属会议室   
  private String groupNo;       //同类设备名称
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public String getEquipmentNo() {
		return equipmentNo;
	}
	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public String getEquipmentStatus() {
		return equipmentStatus;
	}
	public void setEquipmentStatus(String equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getGroupYn() {
		return groupYn;
	}
	public void setGroupYn(int groupYn) {
		this.groupYn = groupYn;
	}
	public int getMrId() {
		return mrId;
	}
	public void setMrId(int mrId) {
		this.mrId = mrId;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
  
  

}

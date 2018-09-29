package yh.subsys.oa.fillRegister.data;

public class YHAttendTime {
	private int seqId;
	private int minLateTime;
	private int maxLateTime;
	private double score;
	
	private int dutyId;
	private String dutyType;
	private String registerType;
	
	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}


	public int getMinLateTime() {
		return minLateTime;
	}

	public void setMinLateTime(int minLateTime) {
		this.minLateTime = minLateTime;
	}

	public int getMaxLateTime() {
		return maxLateTime;
	}

	public void setMaxLateTime(int maxLateTime) {
		this.maxLateTime = maxLateTime;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getDutyId() {
		return dutyId;
	}

	public void setDutyId(int dutyId) {
		this.dutyId = dutyId;
	}

	public String getDutyType() {
		return dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

}

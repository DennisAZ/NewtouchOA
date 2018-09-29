package yh.subsys.oa.training.data;

import java.util.Date;

public class YHHrTrainingRecord {

	private int seqId;
	private String createUserId;
	private int createDeptId;
	private String staffUserId;
	private String tPlanNo;
	private String tPlanName;
	private String tInstitutionName;
	private double  trainningCost;
	private String dutySituation;
	private String trainningSituation;
	private double tExamResults;
	private int tExamLevel;
	private String tComment;
	private String remark;
	
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getCreateUserId() {
    return createUserId;
  }
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }
  public int getCreateDeptId() {
    return createDeptId;
  }
  public void setCreateDeptId(int createDeptId) {
    this.createDeptId = createDeptId;
  }
  public String getStaffUserId() {
    return staffUserId;
  }
  public void setStaffUserId(String staffUserId) {
    this.staffUserId = staffUserId;
  }
  public String getTPlanNo() {
    return tPlanNo;
  }
  public void setTPlanNo(String tPlanNo) {
    this.tPlanNo = tPlanNo;
  }
  public String getTPlanName() {
    return tPlanName;
  }
  public void setTPlanName(String tPlanName) {
    this.tPlanName = tPlanName;
  }
  public String getTInstitutionName() {
    return tInstitutionName;
  }
  public void setTInstitutionName(String tInstitutionName) {
    this.tInstitutionName = tInstitutionName;
  }
  public double getTrainningCost() {
    return trainningCost;
  }
  public void setTrainningCost(double trainningCost) {
    this.trainningCost = trainningCost;
  }
  public String getDutySituation() {
    return dutySituation;
  }
  public void setDutySituation(String dutySituation) {
    this.dutySituation = dutySituation;
  }
  public String getTrainningSituation() {
    return trainningSituation;
  }
  public void setTrainningSituation(String trainningSituation) {
    this.trainningSituation = trainningSituation;
  }
  public double getTExamResults() {
    return tExamResults;
  }
  public void setTExamResults(double tExamResults) {
    this.tExamResults = tExamResults;
  }
  public int getTExamLevel() {
    return tExamLevel;
  }
  public void setTExamLevel(int tExamLevel) {
    this.tExamLevel = tExamLevel;
  }
  public String getTComment() {
    return tComment;
  }
  public void setTComment(String tComment) {
    this.tComment = tComment;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}

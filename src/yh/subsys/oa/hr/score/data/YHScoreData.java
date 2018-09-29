package yh.subsys.oa.hr.score.data;

import java.util.Date;

public class YHScoreData {
	private int seqId;// SEQ_ID int 流水号	private int flowId;// FLOW_ID int
	private String rankman;// RANKMAN Varchar(200) 系统登录用户ID
	private String participant;// PARTICIPANT Varchar(200) 用户ID
	private String score;// SCORE Clob 分数字符串	private String memo;// MEMO Clob 批注字符串	private Date rankDate;// RANK_DATE Date 新建时间
	private String answer;// 答案的字符串A,B
	private String checkFlag;// CHECKFLAG varchar(20)

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public String getRankman() {
		return rankman;
	}

	public void setRankman(String rankman) {
		this.rankman = rankman;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getRankDate() {
		return rankDate;
	}

	public void setRankDate(Date date) {
		this.rankDate = date;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

}

package yh.subsys.oa.hr.score.data;

import java.util.Date;

public class YHScoreShow {
	private int seqId;// SEQ_ID int 流水号	private int groupId;// FLOW_ID int
	private String rankman;// RANKMAN Varchar(200) 系统登录用户ID
	private String participant;// PARTICIPANT Varchar(200) 用户ID
	private String score;// SCORE Clob 分数字符串	private String memo;// MEMO Clob 批注字符串	private Date rankDate;// RANK_DATE Date 新建时间
	private String answer;// 答案的字符串A,B
	private String checkFlag;// CHECKFLAG varchar(20) 0月考核，1年考核
	private String checkEnd;// 0－考核为结束， 1－考核结束
	private String scoreTime;

	public String getScoreTime() {
    return scoreTime;
  }
  public void setScoreTime(String scoreTime) {
    this.scoreTime = scoreTime;
  }
  public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public int getGroupId() {
    return groupId;
  }
  public void setGroupId(int groupId) {
    this.groupId = groupId;
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
	public String getCheckEnd() {
    return checkEnd;
  }
  public void setCheckEnd(String checkEnd) {
    this.checkEnd = checkEnd;
  }
  public String getAnswer() {
    return answer;
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }
}

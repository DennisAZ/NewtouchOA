package yh.core.funcs.message.weixun_share.data;

import java.util.Date;
public class YHWeixunShare{
	
	private int seqId;
	private int userId;
	private String content;
	private String addTime;
	private String topics;
	private String mentionedIds;
	private String broadcastIds;
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getTopics() {
		return topics;
	}
	public void setTopics(String topics) {
		this.topics = topics;
	}
	public String getMentionedIds() {
		return mentionedIds;
	}
	public void setMentionedIds(String mentionedIds) {
		this.mentionedIds = mentionedIds;
	}
	public String getBroadcastIds() {
		return broadcastIds;
	}
	public void setBroadcastIds(String broadcastIds) {
		this.broadcastIds = broadcastIds;
	}
}
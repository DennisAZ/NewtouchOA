/**
 * 
 */
package oa.core.funcs.bbs.act;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lanjinsheng
 *
 */
public class BbsComment {
	int commentId;
	int boardId;
	String lastAuthor;
	String lastTime;
	String userId;
	String authorName;
	String subject;
	String content;
	
	String attachmentId;
	String attachmentName;
	String submitTime;
	String replyCont;
	String readCont;
	String parent;
	String oldSubmitTime;
	String top;
	String jing;
	String readeder;
	String signedYn;
	String lockYn;
	int isAuthor;
	int hadRead;
	List<BbsComment> replyComments=new ArrayList<BbsComment>();

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getReplyCont() {
		return replyCont;
	}
	public void setReplyCont(String replyCont) {
		this.replyCont = replyCont;
	}
	public String getReadCont() {
		return readCont;
	}
	public void setReadCont(String readCont) {
		this.readCont = readCont;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getOldSubmitTime() {
		return oldSubmitTime;
	}
	public void setOldSubmitTime(String oldSubmitTime) {
		this.oldSubmitTime = oldSubmitTime;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getJing() {
		return jing;
	}
	public void setJing(String jing) {
		this.jing = jing;
	}
	public String getReadeder() {
		return readeder;
	}
	public void setReadeder(String readeder) {
		this.readeder = readeder;
	}
	public String getSignedYn() {
		return signedYn;
	}
	public void setSignedYn(String signedYn) {
		this.signedYn = signedYn;
	}
	public String getLockYn() {
		return lockYn;
	}
	public void setLockYn(String lockYn) {
		this.lockYn = lockYn;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getLastAuthor() {
		return lastAuthor;
	}
	public void setLastAuthor(String lastAuthor) {
		this.lastAuthor = lastAuthor;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public List<BbsComment> getReplyComments() {
		return replyComments;
	}
	public void setReplyComments(List<BbsComment> replyComments) {
		this.replyComments = replyComments;
	}
	public String[] getAttachmentNameList() {
		return BBSUtil.split(this.attachmentName, "*");
	}

	public String[] getAttachmentIdList() {
		return BBSUtil.split(this.attachmentId, ",");
	}
	public int getIsAuthor() {
		return isAuthor;
	}
	public void setIsAuthor(int isAuthor) {
		this.isAuthor = isAuthor;
	}
	public int getHadRead() {
		return hadRead;
	}
	public void setHadRead(int hadRead) {
		this.hadRead = hadRead;
	}

}

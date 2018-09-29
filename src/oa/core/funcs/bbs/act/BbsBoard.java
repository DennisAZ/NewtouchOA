/**
 * 
 */
package oa.core.funcs.bbs.act;

/**
 * @author lanjinsheng
 *BOARD_NAME,ANONYMITY_YN,WELCOME_TEXT,BOARD_HOSTER,DEPT_ID,PRIV_ID,USER_ID,BOARD_NO
 *
 *
 *
 */
public class BbsBoard {
private int boarderId;
private int zhuti;
private int tieshu;
private String lastTime="";
private String lastAuthor="";
private String boardName="";
private String anonymity;
private String welcome="";
private String boaderHoster;
private String boaderHosterName="";
private String deptId;
private String deptName="";
private String privId;
private String privName="";
private String userId;
private String userName="";
private int boardNo;
public String getBoardName() {
	return boardName;
}
public void setBoardName(String boardName) {
	this.boardName = boardName;
}
public String getAnonymity() {
	return anonymity;
}
public void setAnonymity(String anonymity) {
	this.anonymity = anonymity;
}
public String getWelcome() {
	return welcome;
}
public void setWelcome(String welcome) {
	this.welcome = welcome;
}
public String getBoaderHoster() {
	return boaderHoster;
}
public void setBoaderHoster(String boaderHoster) {
	this.boaderHoster = boaderHoster;
}
public String getDeptId() {
	return deptId;
}
public void setDeptId(String deptId) {
	this.deptId = deptId;
}
public String getPrivId() {
	return privId;
}
public void setPrivId(String privId) {
	this.privId = privId;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public int getBoardNo() {
	return boardNo;
}
public void setBoardNo(int boardNo) {
	this.boardNo = boardNo;
}
public String getBoaderHosterName() {
	return boaderHosterName;
}
public void setBoaderHosterName(String boaderHosterName) {
	this.boaderHosterName = boaderHosterName;
}
public String getDeptName() {
	return deptName;
}
public void setDeptName(String deptName) {
	this.deptName = deptName;
}
public String getPrivName() {
	return privName;
}
public void setPrivName(String privName) {
	this.privName = privName;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}
public int getBoarderId() {
	return boarderId;
}
public void setBoarderId(int boarderId) {
	this.boarderId = boarderId;
}
public int getZhuti() {
	return zhuti;
}
public void setZhuti(int zhuti) {
	this.zhuti = zhuti;
}
public int getTieshu() {
	return tieshu;
}
public void setTieshu(int tieshu) {
	this.tieshu = tieshu;
}
public String getLastTime() {
	return lastTime;
}
public void setLastTime(String lastTime) {
	this.lastTime = lastTime;
}
public String getLastAuthor() {
	return lastAuthor;
}
public void setLastAuthor(String lastAuthor) {
	this.lastAuthor = lastAuthor;
}
}

package com.psit.struts.entity;

/**
 * 潜在已成客户统计实体 <br>
 * create_date: Aug 27, 2010,3:22:21 PM<br>
 * @author csg
 */

public class StatIsCusSuc implements java.io.Serializable {

	// Fields
	private Long userCode;
	private String userSeName;
	private Long poteCusCount;
	private Long isSusCusCount;
	// Constructors

	/** default constructor */
	public StatIsCusSuc() {
	}


	/** full constructor */
	public StatIsCusSuc(Long userId,String userName,Long poteCusCount,Long isSusCusCount) {
		this.userCode = userId;
		this.userSeName = userName;
		this.poteCusCount = poteCusCount;
		this.isSusCusCount = isSusCusCount;
	}

	public Long getPoteCusCount() {
		return poteCusCount;
	}


	public void setPoteCusCount(Long poteCusCount) {
		this.poteCusCount = poteCusCount;
	}


	public Long getIsSusCusCount() {
		return isSusCusCount;
	}


	public void setIsSusCusCount(Long isSusCusCount) {
		this.isSusCusCount = isSusCusCount;
	}


	public Long getUserCode() {
		return userCode;
	}


	public void setUserCode(Long userCode) {
		this.userCode = userCode;
	}


	public String getUserSeName() {
		return userSeName;
	}


	public void setUserSeName(String userSeName) {
		this.userSeName = userSeName;
	}



}
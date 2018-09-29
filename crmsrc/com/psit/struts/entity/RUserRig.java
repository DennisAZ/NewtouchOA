package com.psit.struts.entity;

/**
 * 
 * 权限分配表实体 <br>
 *
 * create_date: Aug 27, 2010,3:32:50 PM<br>
 * @author zjr
 */

public class RUserRig implements java.io.Serializable {

	// Fields

	private Long rurId;
	private LimRight limRight;
	private LimUser limUser;
	private String rurType;

	// Constructors

	/** default constructor */
	public RUserRig() {
	}

	/** minimal constructor */
	public RUserRig(Long rurId) {
		this.rurId = rurId;
	}

	/** full constructor */
	public RUserRig(Long rurId, LimRight limRight, LimUser limUser,String rurType) {
		this.rurId = rurId;
		this.limRight = limRight;
		this.limUser = limUser;
		this.rurType=rurType;
	}

	// Property accessors

	public LimRight getLimRight() {
		return this.limRight;
	}

	public void setLimRight(LimRight limRight) {
		this.limRight = limRight;
	}

	public LimUser getLimUser() {
		return limUser;
	}

	public void setLimUser(LimUser limUser) {
		this.limUser = limUser;
	}

	public Long getRurId() {
		return rurId;
	}

	public void setRurId(Long rurId) {
		this.rurId = rurId;
	}

	public String getRurType() {
		return rurType;
	}

	public void setRurType(String rurType) {
		this.rurType = rurType;
	}


}
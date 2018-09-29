package com.psit.struts.entity;

/**
 * 
 * 权限分配表实体（未使用） <br>
 *
 * create_date: Aug 27, 2010,3:31:32 PM<br>
 * @author zjr
 */

public class RRoleRig implements java.io.Serializable {

	// Fields

	private Long rrrId;
	private LimRight limRight;
	private LimRole limRole;

	// Constructors

	/** default constructor */
	public RRoleRig() {
	}

	/** minimal constructor */
	public RRoleRig(Long rrrId) {
		this.rrrId = rrrId;
	}

	/** full constructor */
	public RRoleRig(Long rrrId, LimRight limRight, LimRole limRole) {
		this.rrrId = rrrId;
		this.limRight = limRight;
		this.limRole = limRole;
	}

	// Property accessors

	public Long getRrrId() {
		return this.rrrId;
	}

	public void setRrrId(Long rrrId) {
		this.rrrId = rrrId;
	}

	public LimRight getLimRight() {
		return this.limRight;
	}

	public void setLimRight(LimRight limRight) {
		this.limRight = limRight;
	}

	public LimRole getLimRole() {
		return this.limRole;
	}

	public void setLimRole(LimRole limRole) {
		this.limRole = limRole;
	}

}
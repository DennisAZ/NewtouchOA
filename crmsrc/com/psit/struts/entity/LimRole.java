package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 职级实体 <br>
 */

public class LimRole implements java.io.Serializable {

	// Fields

	private Long rolId;
	private int rolLev;
	private String rolName;
	private String roleDesc;
	private Set RRoleRigs = new HashSet(0);
	private Set limUsers = new HashSet(0);
	private Set salEmps = new HashSet(0);
	// Constructors

	/** default constructor */
	public LimRole() {
	}

	/** minimal constructor */
	public LimRole(Long id) {
		this.rolId = id;
	}

	/** full constructor */
	public LimRole( int rolLev, String rolName, String roleDesc,
			Set RRoleRigs,Set limUsers) {
		this.rolLev = rolLev;
		this.rolName = rolName;
		this.roleDesc = roleDesc;
		this.RRoleRigs = RRoleRigs;
		this.limUsers = limUsers;
	}

	// Property accessors

	public Long getRolId() {
		return this.rolId;
	}

	public void setRolId(Long rolId) {
		this.rolId = rolId;
	}

	public int getRolLev() {
		return this.rolLev;
	}

	public void setRolLev(int rolLev) {
		this.rolLev = rolLev;
	}

	public String getRolName() {
		return this.rolName;
	}

	public void setRolName(String rolName) {
		this.rolName = rolName;
	}

	public Set getRRoleRigs() {
		return this.RRoleRigs;
	}

	public void setRRoleRigs(Set RRoleRigs) {
		this.RRoleRigs = RRoleRigs;
	}

	public Set getLimUsers() {
		return this.limUsers;
	}

	public void setLimUsers(Set limUsers) {
		this.limUsers = limUsers;
	}

	public String getRoleDesc() {
		return StringFormat.toBlank(roleDesc);
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Set getSalEmps() {
		return salEmps;
	}

	public void setSalEmps(Set salEmps) {
		this.salEmps = salEmps;
	}

}
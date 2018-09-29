package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 权限表实体 <br>
 *
 * create_date: Aug 27, 2010,3:23:45 PM<br>
 * @author zjr
 */

public class LimRight implements java.io.Serializable {

	// Fields

	private String rigCode;
	private LimOperate limOperate;
	private LimFunction limFunction;
	private String rigWmsName;
	private String enabledType=" ";
	private Set RRoleRigs = new HashSet(0);

	// Constructors

	/** default constructor */
	public LimRight() {
	}

	/** minimal constructor */
	public LimRight(String rigCode) {
		this.rigCode = rigCode;
	}

	/** full constructor */
	public LimRight(String rigCode, LimOperate limOperate,
			LimFunction limFunction,  String enabledType,String rigWmsName, Set RRoleRigs) {
		this.rigCode = rigCode;
		this.limOperate = limOperate;
		this.limFunction = limFunction;
		this.rigWmsName=rigWmsName;
		this.enabledType = enabledType;
		this.RRoleRigs = RRoleRigs;
	}

	// Property accessors

	public String getRigCode() {
		return this.rigCode;
	}

	public void setRigCode(String rigCode) {
		this.rigCode = rigCode;
	}

	public LimOperate getLimOperate() {
		return this.limOperate;
	}

	public void setLimOperate(LimOperate limOperate) {
		this.limOperate = limOperate;
	}

	public LimFunction getLimFunction() {
		return this.limFunction;
	}

	public void setLimFunction(LimFunction limFunction) {
		this.limFunction = limFunction;
	}

	public Set getRRoleRigs() {
		return this.RRoleRigs;
	}

	public void setRRoleRigs(Set RRoleRigs) {
		this.RRoleRigs = RRoleRigs;
	}

	public String getEnabledType() {
		return enabledType;
	}

	public void setEnabledType(String enabledType) {
		this.enabledType = enabledType;
	}

	public String getRigWmsName() {
		return rigWmsName;
	}

	public void setRigWmsName(String rigWmsName) {
		this.rigWmsName = rigWmsName;
	}

}
package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

import yh.core.funcs.person.data.YHPerson;

import com.psit.struts.util.format.StringFormat;

/**
 * 账号实体 <br>
 */

public class LimUser implements java.io.Serializable {

	// Fields
	private YHPerson person;
	public YHPerson getPerson() {
		return person;
	}
	public void setPerson(YHPerson person) {
		this.person = person;
	}
	private String userCode;
	private LimRole limRole;
	private SalEmp salEmp;
	private SalOrg salOrg;
	private LimUser limUser;
	private String userLoginName;
	private String userPwd;
	private String userLev;
	private String userSeName;
	private String userDesc;
	private String userIsenabled;
	private String userNum;
	private String userIsLogin;
	private String userIp;
	private Set limUsers = new HashSet(0);
	private Set RUserRigs = new HashSet(0);
	// Constructors

	/** default constructor */
	public LimUser() {
	}
    public LimUser(String userCode)
    {
    	this.userCode=userCode;
    }

	/** full constructor */
	public LimUser(LimRole limRole, SalEmp salEmp,String userIsLogin,
			SalOrg salOrg, LimUser limUser, String userLoginName,String userIp,
			String userPwd, String userLev, String userSeName, String userDesc,
			String userIsenabled, String userNum, Set limUsers, Set RUserRigs) {
		this.limRole = limRole;
		this.salEmp = salEmp;
		this.salOrg = salOrg;
		this.limUser = limUser;
		this.userLoginName = userLoginName;
		this.userPwd = userPwd;
		this.userLev = userLev;
		this.userSeName = userSeName;
		this.userDesc = userDesc;
		this.userIsenabled = userIsenabled;
		this.userNum = userNum;
		this.userIsLogin=userIsLogin;
		this.userIp=userIp;
		this.limUsers = limUsers;
		this.RUserRigs=RUserRigs;
	}

	// Property accessors

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public LimRole getLimRole() {
		return this.limRole;
	}

	public void setLimRole(LimRole limRole) {
		this.limRole = limRole;
	}

	public SalEmp getSalEmp() {
		return this.salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

	public SalOrg getSalOrg() {
		return this.salOrg;
	}

	public void setSalOrg(SalOrg salOrg) {
		this.salOrg = salOrg;
	}

	public LimUser getLimUser() {
		return this.limUser;
	}

	public void setLimUser(LimUser limUser) {
		this.limUser = limUser;
	}

	public String getUserLoginName() {
		return this.userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getUserPwd() {
		return this.userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserLev() {
		return this.userLev;
	}

	public void setUserLev(String userLev) {
		this.userLev = userLev;
	}

	public String getUserSeName() {
		return this.userSeName;
	}

	public void setUserSeName(String userSeName) {
		this.userSeName = userSeName;
	}

	public String getUserDesc() {
		return StringFormat.toBlank(this.userDesc);
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public String getUserIsenabled() {
		return this.userIsenabled;
	}

	public void setUserIsenabled(String userIsenabled) {
		this.userIsenabled = userIsenabled;
	}
	public String getUserNum() {
		return this.userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

//	public Set getLimRoles() {
//		return this.limRoles;
//	}
//
//	public void setLimRoles(Set limRoles) {
//		this.limRoles = limRoles;
//	}

	public Set getLimUsers() {
		return this.limUsers;
	}

	public void setLimUsers(Set limUsers) {
		this.limUsers = limUsers;
	}

	public Set getRUserRigs() {
		return RUserRigs;
	}
	public void setRUserRigs(Set userRigs) {
		RUserRigs = userRigs;
	}
	public String getUserIsLogin() {
		return userIsLogin;
	}
	public void setUserIsLogin(String userIsLogin) {
		this.userIsLogin = userIsLogin;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

}
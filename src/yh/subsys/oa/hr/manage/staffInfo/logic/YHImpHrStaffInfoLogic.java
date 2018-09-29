package yh.subsys.oa.hr.manage.staffInfo.logic;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;

public class YHImpHrStaffInfoLogic {
	private static Logger log = Logger.getLogger(YHImpHrStaffInfoLogic.class);

	public ArrayList<YHDbRecord> downCSVTempletLogic() throws Exception {
		ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
		YHDbRecord record = new YHDbRecord();
		try {
		  record.addField("用户名", "");
		  record.addField("部门", "");
		  record.addField("编号", "");
      record.addField("工号", "");
			record.addField("姓名", "");
			record.addField("英文名", "");
			record.addField("身份证号码", "");
			record.addField("出生日期", "");
			record.addField("年龄", "");
			record.addField("性别", "");

			record.addField("籍贯", "");
			record.addField("民族", "");
			record.addField("婚姻状况", "");
			record.addField("健康状况", "");
			record.addField("政治面貌", "");
			record.addField("入党时间", "");
			record.addField("户口类别", "");
			record.addField("户口所在地", "");
			
			record.addField("工种", "");
      record.addField("行政级别", "");
      record.addField("员工类型", "");
      record.addField("职务", "");
      record.addField("职称", "");
      record.addField("入职时间", "");
      record.addField("本单位工龄", "");
      record.addField("起薪时间", "");
			record.addField("在职状态", "");
			record.addField("总工龄", "");
			record.addField("参加工作时间", "");
			
			record.addField("联系电话", "");
			record.addField("手机号码", "");
			record.addField("MSN", "");
			record.addField("电子邮件", "");
			record.addField("家庭地址", "");
			record.addField("QQ", "");
			record.addField("其他联系方式", "");
			record.addField("社保号", "");
			
			record.addField("学历", "");
			record.addField("学位", "");
			record.addField("毕业时间", "");
			record.addField("毕业院校", "");
			record.addField("专业", "");
			record.addField("计算机水平", "");
			record.addField("外语语种1", "");
			record.addField("外语语种2", "");
			record.addField("外语语种3", "");
			record.addField("外语水平1", "");
			record.addField("外语水平2", "");
			record.addField("外语水平3", "");
			record.addField("特长", "");
			
			record.addField("职务情况", "");
			record.addField("担保记录", "");
			record.addField("社保缴纳情况", "");
			record.addField("体检记录", "");
			record.addField("备注", "");
			record.addField("简历", "");

			result.add(record);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	/**
	 * 导入人员档案信息
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public Map<Object, Object>  impStaffInfoToCsvLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person,StringBuffer buffer) throws Exception {
		YHORM orm = new YHORM();
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		int isCount = 0;
		int updateCount = 0;
		try {
			Map<Object, Object>bufferMap = new HashMap<Object, Object>();
			String infoStr = "";
			InputStream is = fileForm.getInputStream();
			ArrayList<YHDbRecord> dbRecords = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
			for (YHDbRecord record : dbRecords) {
			  String userId =  YHUtility.null2Empty((String) record.getValueByName("用户名"));
				String deptName =  YHUtility.null2Empty((String) record.getValueByName("部门"));
				String staffNo =  YHUtility.null2Empty((String) record.getValueByName("编号"));
				String workNo =  YHUtility.null2Empty((String) record.getValueByName("工号"));
				String staffName =  YHUtility.null2Empty((String) record.getValueByName("姓名"));
				String staffEName =  YHUtility.null2Empty((String) record.getValueByName("英文名"));
				String staffCardNo =  YHUtility.null2Empty((String) record.getValueByName("身份证号码"));
				String staffBirth =  YHUtility.null2Empty((String) record.getValueByName("出生日期"));
				String staffAge =  YHUtility.null2Empty((String) record.getValueByName("年龄"));
				String staffSex =  YHUtility.null2Empty((String) record.getValueByName("性别"));

				String staffNativePlace =  YHUtility.null2Empty((String) record.getValueByName("籍贯"));
				String staffNationality =  YHUtility.null2Empty((String) record.getValueByName("民族"));
				String staffMaritalStatus = YHUtility.null2Empty((String) record.getValueByName("婚姻状况"));
				String staffHealth =  YHUtility.null2Empty((String) record.getValueByName("健康状况"));
				String staffPoliticalStatus = YHUtility.null2Empty((String) record.getValueByName("政治面貌"));
				String joinPartyTime =  YHUtility.null2Empty((String) record.getValueByName("入党时间"));
				String staffType =  YHUtility.null2Empty((String) record.getValueByName("户口类别"));
				String staffDomicilePlace = YHUtility.null2Empty( (String) record.getValueByName("户口所在地"));
				
				String workType = YHUtility.null2Empty( (String) record.getValueByName("工种"));
				String administrationLevel =  YHUtility.null2Empty((String) record.getValueByName("行政级别"));
				String staffOccupation =  YHUtility.null2Empty((String) record.getValueByName("员工类型"));
				String jobPosition =  YHUtility.null2Empty((String) record.getValueByName("职务"));
				String presentPosition =  YHUtility.null2Empty((String) record.getValueByName("职称"));
				String datesEmployed =  YHUtility.null2Empty((String) record.getValueByName("入职时间"));
				String jobAge = YHUtility.null2Empty( (String) record.getValueByName("本单位工龄"));
				String beginSalsryTime =  YHUtility.null2Empty((String) record.getValueByName("起薪时间"));
				String workStatus =  YHUtility.null2Empty((String) record.getValueByName("在职状态"));
				String workAge =  YHUtility.null2Empty((String) record.getValueByName("总工龄"));
				String jobBeginning =  YHUtility.null2Empty((String) record.getValueByName("参加工作时间"));
				
				String staffPhone =  YHUtility.null2Empty((String) record.getValueByName("联系电话"));
				String staffMobile =  YHUtility.null2Empty((String) record.getValueByName("手机号码"));
				String staffMsn =  YHUtility.null2Empty((String) record.getValueByName("MSN"));
				String staffEmail =  YHUtility.null2Empty((String) record.getValueByName("电子邮件"));
				String homeAddress =  YHUtility.null2Empty((String) record.getValueByName("家庭地址"));
				String staffQq =  YHUtility.null2Empty((String) record.getValueByName("QQ"));
				String otherContact =  YHUtility.null2Empty((String) record.getValueByName("其他联系方式"));
				String insureNum = YHUtility.null2Empty( (String) record.getValueByName("社保号"));
				
				String staffHighestSchool =  YHUtility.null2Empty((String) record.getValueByName("学历"));
				String staffHighestDegree =  YHUtility.null2Empty((String) record.getValueByName("学位"));
				String graduationDate =  YHUtility.null2Empty((String) record.getValueByName("毕业时间"));
				String graduationSchool = YHUtility.null2Empty( (String) record.getValueByName("毕业院校"));
				String staffMajor =  YHUtility.null2Empty((String) record.getValueByName("专业"));
				String computerLevel = YHUtility.null2Empty( (String) record.getValueByName("计算机水平"));
				String foreignLanguage1 =  YHUtility.null2Empty((String) record.getValueByName("外语语种1"));
				String foreignLanguage2 =  YHUtility.null2Empty((String) record.getValueByName("外语语种2"));
				String foreignLanguage3 =  YHUtility.null2Empty((String) record.getValueByName("外语语种3"));
				String foreignLevel1 =  YHUtility.null2Empty((String) record.getValueByName("外语水平1"));
				String foreignLevel2 =  YHUtility.null2Empty((String) record.getValueByName("外语水平2"));
				String foreignLevel3 =  YHUtility.null2Empty((String) record.getValueByName("外语水平3"));
				String staffSkills =  YHUtility.null2Empty((String) record.getValueByName("特长"));
				
				String certificate = YHUtility.null2Empty( (String) record.getValueByName("职务情况"));
				String surety =  YHUtility.null2Empty((String) record.getValueByName("担保记录"));
				String insure =  YHUtility.null2Empty((String) record.getValueByName("社保缴纳情况"));
				String bodyExamim =  YHUtility.null2Empty((String) record.getValueByName("体检记录"));
				String remark = YHUtility.null2Empty( (String) record.getValueByName("备注"));
				String resume =  YHUtility.null2Empty((String) record.getValueByName("简历"));

				if (YHUtility.isNullorEmpty(userId)) {
					// <font color=red>用户名为空，未导入</font>
					infoStr = "用户名为空，未导入";
					bufferMap.put("staffName", staffName);
					bufferMap.put("staffSex", staffSex);
					bufferMap.put("staffBirth", staffBirth);
					bufferMap.put("staffNationality", staffNationality);
					bufferMap.put("staffNativePlace", staffNativePlace);
					bufferMap.put("staffPoliticalStatus", staffPoliticalStatus);
					bufferMap.put("staffCardNo", staffCardNo);
					bufferMap.put("remark", remark);
					bufferMap.put("infoStr", infoStr);
					bufferMap.put("color", "red");
					sbStrJson(buffer,bufferMap);
					continue;
				}

				if (YHUtility.isNullorEmpty(staffName)) {
					// <font color=red>姓名为空，未导入</font>
					infoStr = "姓名为空，未导入";
					bufferMap.put("staffName", staffName);
					bufferMap.put("staffSex", staffSex);
					bufferMap.put("staffBirth", staffBirth);
					bufferMap.put("staffNationality", staffNationality);
					bufferMap.put("staffNativePlace", staffNativePlace);
					bufferMap.put("staffPoliticalStatus", staffPoliticalStatus);
					bufferMap.put("staffCardNo", staffCardNo);
					bufferMap.put("remark", remark);
					bufferMap.put("infoStr", infoStr);
					bufferMap.put("color", "red");
					sbStrJson(buffer,bufferMap);
					continue;
				}
				staffSex = YHUtility.null2Empty(staffSex);
				if ("男".equals(staffSex)) {
					staffSex = "0";
				} else {
					staffSex = "1";
				}

				if ("未婚".equals(staffMaritalStatus.trim())) {
					staffMaritalStatus = "0";
				} else if ("已婚".equals(staffMaritalStatus.trim())) {
					staffMaritalStatus = "1";
				} else if ("离异".equals(staffMaritalStatus.trim())) {
					staffMaritalStatus = "2";
				} else if ("丧偶".equals(staffMaritalStatus.trim())) {
					staffMaritalStatus = "3";
				} else {
					staffMaritalStatus = "";
				}
				if (!YHUtility.isNullorEmpty(staffNativePlace)) {
					staffNativePlace = this.getHrCode2(dbConn, "AREA", staffNativePlace);
				}
				if (!YHUtility.isNullorEmpty(workStatus)) {
					workStatus = this.getHrCode2(dbConn, "WORK_STATUS", workStatus);
				}
				if (!YHUtility.isNullorEmpty(staffPoliticalStatus)) {
					staffPoliticalStatus = this.getHrCode2(dbConn, "STAFF_POLITICAL_STATUS", staffPoliticalStatus);
				}

				if (!YHUtility.isNullorEmpty(staffType)) {
					staffType = this.getHrCode2(dbConn, "HR_STAFF_TYPE", staffType);
				}
				if (!YHUtility.isNullorEmpty(staffHighestSchool)) {
					staffHighestSchool = this.getHrCode2(dbConn, "STAFF_HIGHEST_SCHOOL", staffHighestSchool);
				}

				if (!YHUtility.isNullorEmpty(staffHighestDegree)) {
					staffHighestDegree = this.getHrCode2(dbConn, "EMPLOYEE_HIGHEST_DEGREE", staffHighestDegree);
				}

				if (!YHUtility.isNullorEmpty(presentPosition)) {
					presentPosition = this.getHrCode2(dbConn, "PRESENT_POSITION", presentPosition);
				}
				if (!YHUtility.isNullorEmpty(staffOccupation)) {
					staffOccupation = this.getHrCode2(dbConn, "STAFF_OCCUPATION", staffOccupation);
				}

				// 是否为oa用户
				Map<Object, Object> isOaMap = this.isOaUser(dbConn, userId);
				boolean isOaFlag = (Boolean) isOaMap.get("isOaFlag");
				String dbUserId = (String) isOaMap.get("dbUserId");
				String dbDeptId = (String) isOaMap.get("dbDeptId");
				if (isOaFlag) {

					boolean isStaffUser = this.isHrStaffUser(dbConn, dbUserId);// 档案库中是否已存在用户
					if (!isStaffUser) {
						YHHrStaffInfo hrStaffInfo = new YHHrStaffInfo();
						hrStaffInfo.setUserId(userId);
						hrStaffInfo.setStaffName(staffName);
						hrStaffInfo.setDeptId(Integer.parseInt(dbDeptId));

						hrStaffInfo.setStaffNo(staffNo);
						hrStaffInfo.setWorkNo(workNo);
						hrStaffInfo.setWorkType(workType);
						hrStaffInfo.setStaffEName(staffEName);
						hrStaffInfo.setStaffCardNo(staffCardNo);

						hrStaffInfo.setStaffSex(staffSex);
						hrStaffInfo.setStaffBirth(YHUtility.parseDate(staffBirth));
						hrStaffInfo.setStaffAge(staffAge);
						hrStaffInfo.setStaffNativePlace(staffNativePlace);
						hrStaffInfo.setStaffDomicilePlace(staffDomicilePlace);
						hrStaffInfo.setStaffNationality(staffNationality);
						hrStaffInfo.setStaffMaritalStatus(staffMaritalStatus);
						hrStaffInfo.setStaffPoliticalStatus(staffPoliticalStatus);
						hrStaffInfo.setJoinPartyTime(YHUtility.parseDate(joinPartyTime));
						hrStaffInfo.setStaffPhone(staffPhone);
						hrStaffInfo.setStaffMobile(staffMobile);

						hrStaffInfo.setStaffEmail(staffEmail);
						hrStaffInfo.setStaffMsn(staffMsn);
						hrStaffInfo.setStaffQq(staffQq);
						hrStaffInfo.setHomeAddress(homeAddress);
						hrStaffInfo.setOtherContact(otherContact);
						hrStaffInfo.setInsureNum(insureNum);
						hrStaffInfo.setJobBeginning(YHUtility.parseDate(jobBeginning));
						hrStaffInfo.setWorkAge(workAge);
						hrStaffInfo.setStaffHealth(staffHealth);
						hrStaffInfo.setStaffHighestSchool(staffHighestSchool);

						hrStaffInfo.setStaffHighestDegree(staffHighestDegree);
						hrStaffInfo.setGraduationDate(YHUtility.parseDate(graduationDate));
						hrStaffInfo.setGraduationSchool(graduationSchool);
						hrStaffInfo.setStaffMajor(staffMajor);
						hrStaffInfo.setComputerLevel(computerLevel);
						hrStaffInfo.setForeignLanguage1(foreignLanguage1);
						hrStaffInfo.setForeignLevel1(foreignLevel1);
						hrStaffInfo.setForeignLanguage2(foreignLanguage2);
						hrStaffInfo.setForeignLevel2(foreignLevel2);
						hrStaffInfo.setForeignLanguage3(foreignLanguage3);

						hrStaffInfo.setForeignLevel3(foreignLevel3);
						hrStaffInfo.setStaffSkills(staffSkills);
						hrStaffInfo.setStaffOccupation(staffOccupation);
						hrStaffInfo.setAdministrationLevel(administrationLevel);
						hrStaffInfo.setJobPosition(jobPosition);
						hrStaffInfo.setPresentPosition(presentPosition);
						hrStaffInfo.setDatesEmployed(YHUtility.parseDate(datesEmployed));
						hrStaffInfo.setJobAge(jobAge);
						hrStaffInfo.setBeginSalsryTime(YHUtility.parseDate(beginSalsryTime));
						hrStaffInfo.setWorkStatus(workStatus);
						hrStaffInfo.setRemark(remark);
						hrStaffInfo.setResume(resume);

						hrStaffInfo.setStaffType(staffType);
						hrStaffInfo.setCertificate(certificate);
						hrStaffInfo.setSurety(surety);
						hrStaffInfo.setInsure(insure);

						hrStaffInfo.setBodyExamim(bodyExamim);
						orm.saveSingle(dbConn, hrStaffInfo);
						isCount ++;
						
						infoStr = "成功";
						bufferMap.put("color", "green");
					} else {
						Map updateMap = new HashMap();
						updateMap.put("USER_ID", userId);
						YHHrStaffInfo hrStaffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, updateMap);
						if (hrStaffInfo != null) {
							hrStaffInfo.setStaffNo(staffNo);
							hrStaffInfo.setWorkNo(workNo);
							hrStaffInfo.setWorkType(workType);
							hrStaffInfo.setStaffEName(staffEName);
							hrStaffInfo.setStaffCardNo(staffCardNo);

							hrStaffInfo.setStaffSex(staffSex);
							if (YHUtility.isDay(staffBirth)) {
								hrStaffInfo.setStaffBirth(YHUtility.parseDate(staffBirth));
							}
							hrStaffInfo.setStaffAge(staffAge);
							hrStaffInfo.setStaffNativePlace(staffNativePlace);
							hrStaffInfo.setStaffDomicilePlace(staffDomicilePlace);
							hrStaffInfo.setStaffNationality(staffNationality);
							hrStaffInfo.setStaffMaritalStatus(staffMaritalStatus);
							hrStaffInfo.setStaffPoliticalStatus(staffPoliticalStatus);
							if (YHUtility.isDay(joinPartyTime)) {
								hrStaffInfo.setJoinPartyTime(YHUtility.parseDate(joinPartyTime));
							}
							hrStaffInfo.setStaffPhone(staffPhone);
							hrStaffInfo.setStaffMobile(staffMobile);

							hrStaffInfo.setStaffEmail(staffEmail);
							hrStaffInfo.setStaffMsn(staffMsn);
							hrStaffInfo.setStaffQq(staffQq);
							hrStaffInfo.setHomeAddress(homeAddress);
							hrStaffInfo.setOtherContact(otherContact);
							hrStaffInfo.setInsureNum(insureNum);
							if (YHUtility.isDay(jobBeginning)) {
								hrStaffInfo.setJobBeginning(YHUtility.parseDate(jobBeginning));
							}
							
							hrStaffInfo.setWorkAge(workAge);
							hrStaffInfo.setStaffHealth(staffHealth);
							hrStaffInfo.setStaffHighestSchool(staffHighestSchool);

							hrStaffInfo.setStaffHighestDegree(staffHighestDegree);
							if (YHUtility.isDay(graduationDate)) {
								hrStaffInfo.setGraduationDate(YHUtility.parseDate(graduationDate));
							}
							hrStaffInfo.setGraduationSchool(graduationSchool);
							hrStaffInfo.setStaffMajor(staffMajor);
							hrStaffInfo.setComputerLevel(computerLevel);
							hrStaffInfo.setForeignLanguage1(foreignLanguage1);
							hrStaffInfo.setForeignLevel1(foreignLevel1);
							hrStaffInfo.setForeignLanguage2(foreignLanguage2);
							hrStaffInfo.setForeignLevel2(foreignLevel2);
							hrStaffInfo.setForeignLanguage3(foreignLanguage3);

							hrStaffInfo.setForeignLevel3(foreignLevel3);
							hrStaffInfo.setStaffSkills(staffSkills);
							hrStaffInfo.setStaffOccupation(staffOccupation);
							hrStaffInfo.setAdministrationLevel(administrationLevel);
							hrStaffInfo.setJobPosition(jobPosition);
							hrStaffInfo.setPresentPosition(presentPosition);
							hrStaffInfo.setDatesEmployed(YHUtility.parseDate(datesEmployed));
							hrStaffInfo.setJobAge(jobAge);
							if (YHUtility.isDay(beginSalsryTime)) {
								hrStaffInfo.setBeginSalsryTime(YHUtility.parseDate(beginSalsryTime));
							}
							hrStaffInfo.setWorkStatus(workStatus);
							hrStaffInfo.setRemark(remark);
							hrStaffInfo.setResume(resume);

							hrStaffInfo.setStaffType(staffType);
							hrStaffInfo.setCertificate(certificate);
							hrStaffInfo.setSurety(surety);
							hrStaffInfo.setInsure(insure);

							hrStaffInfo.setBodyExamim(bodyExamim);
							orm.updateSingle(dbConn, hrStaffInfo);
							updateCount ++;
							
							infoStr = "已有用户，资料已更新";
							bufferMap.put("color", "green");
						}
						Map personMap = new HashMap();
						personMap.put("USER_ID", userId);
						YHPerson dbPerson = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, personMap);
						if (dbPerson != null) {
							dbPerson.setSex(staffSex);
							orm.updateSingle(dbConn, dbPerson);
						}
					}

				} else {// 不是oa用户
				  
					boolean isPersonUser = this.isPersonUser(dbConn, userId);
					if (isPersonUser) {
						// <font color=red>用户已存在，未导入</font>
						infoStr = "用户已存在，未导入";
						bufferMap.put("color", "red");
						continue;
					}
					
					int userPriv = this.getlowestRole(dbConn);
					Map<Object, Object> deptMap = this.isDeptUser(dbConn, deptName);
					boolean isDeptFlag = (Boolean) deptMap.get("isDeptFlag");
					int dbDeptId1 = (Integer) deptMap.get("dbDeptId");
					if (!isDeptFlag || YHUtility.isNullorEmpty(deptName)) {
						// <font color=red>部门不存在，未导入</font>
						infoStr = "部门不存在，未导入";
						bufferMap.put("staffName", staffName);
						bufferMap.put("staffSex", staffSex);
						bufferMap.put("staffBirth", staffBirth);
						bufferMap.put("staffNationality", staffNationality);
						bufferMap.put("staffNativePlace", staffNativePlace);
						bufferMap.put("staffPoliticalStatus", staffPoliticalStatus);
						bufferMap.put("staffCardNo", staffCardNo);
						bufferMap.put("remark", remark);
						bufferMap.put("infoStr", infoStr);
						bufferMap.put("color", "red");
						sbStrJson(buffer,bufferMap);
						continue;
					} else {
						YHPerson dbPerson = new YHPerson();
						dbPerson.setUserId(userId);

						dbPerson.setUserName(staffName);
						dbPerson.setSex(staffSex);
						dbPerson.setPassword(YHPassEncrypt.encryptPass(""));
						dbPerson.setUserPriv(String.valueOf(userPriv));
						dbPerson.setPostPriv("0");
						dbPerson.setPostDept("");
						dbPerson.setCanbroadcast(0);
						dbPerson.setDeptId(dbDeptId1);
						//dbPerson.setAuatar("1");
						dbPerson.setCallSound("1");

						dbPerson.setDutyType(1);
						dbPerson.setSmsOn("1");
						dbPerson.setEmailCapacity(0);
						dbPerson.setFolderCapacity(0);
						dbPerson.setUserPrivOther("");
						if (YHUtility.isNumber(staffNo)) {
							dbPerson.setUserNo(Integer.parseInt(staffNo));
						} else {
							dbPerson.setUserNo(10);
						}
						dbPerson.setNotLogin("1");
						dbPerson.setNotViewUser("1");
						dbPerson.setNotViewTable("1");
						dbPerson.setByname("");
						if (YHUtility.isDay(staffBirth)) {
							dbPerson.setBirthday(YHUtility.parseDate(staffBirth));
						}
						dbPerson.setTheme("1");
						dbPerson.setMobilNo("");
						dbPerson.setMobilNoHidden("");
						orm.saveSingle(dbConn, dbPerson);

						
	          boolean isStaffUser = this.isHrStaffUser(dbConn, userId);// 档案库中是否已存在用户
	          YHHrStaffInfo hrStaffInfo = null;
	          if (isStaffUser) {
	            Map map = new HashMap();
	            map.put("USER_ID", userId);
	            hrStaffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, map);
	          }
	          else{
	            hrStaffInfo = new YHHrStaffInfo();
	          }
	            
						hrStaffInfo.setUserId(userId);
						hrStaffInfo.setStaffName(staffName);
						hrStaffInfo.setCreateUserId(String.valueOf(person.getSeqId()));
						hrStaffInfo.setCreateDeptId(person.getDeptId());
						hrStaffInfo.setDeptId(dbDeptId1);

						hrStaffInfo.setStaffNo(staffNo);
						hrStaffInfo.setWorkNo(workNo);
						hrStaffInfo.setWorkType(workType);
						hrStaffInfo.setStaffEName(staffEName);
						hrStaffInfo.setStaffCardNo(staffCardNo);

						hrStaffInfo.setStaffSex(staffSex);
						if (YHUtility.isDay(staffBirth)) {
							hrStaffInfo.setStaffBirth(YHUtility.parseDate(staffBirth));
						}
						hrStaffInfo.setStaffAge(staffAge);
						hrStaffInfo.setStaffNativePlace(staffNativePlace);
						hrStaffInfo.setStaffDomicilePlace(staffDomicilePlace);
						hrStaffInfo.setStaffNationality(staffNationality);
						hrStaffInfo.setStaffMaritalStatus(staffMaritalStatus);
						hrStaffInfo.setStaffPoliticalStatus(staffPoliticalStatus);
						if (YHUtility.isDay(joinPartyTime)) {
							hrStaffInfo.setJoinPartyTime(YHUtility.parseDate(joinPartyTime));
						}
						hrStaffInfo.setStaffPhone(staffPhone);
						hrStaffInfo.setStaffMobile(staffMobile);

						hrStaffInfo.setStaffEmail(staffEmail);
						hrStaffInfo.setStaffMsn(staffMsn);
						hrStaffInfo.setStaffQq(staffQq);
						hrStaffInfo.setHomeAddress(homeAddress);
						hrStaffInfo.setOtherContact(otherContact);
						hrStaffInfo.setInsureNum(insureNum);
						if (YHUtility.isDay(jobBeginning)) {
							hrStaffInfo.setJobBeginning(YHUtility.parseDate(jobBeginning));
						}
						hrStaffInfo.setWorkAge(workAge);
						hrStaffInfo.setStaffHealth(staffHealth);
						hrStaffInfo.setStaffHighestSchool(staffHighestSchool);

						hrStaffInfo.setStaffHighestDegree(staffHighestDegree);
						if (YHUtility.isDay(graduationDate)) {
							hrStaffInfo.setGraduationDate(YHUtility.parseDate(graduationDate));
						}
						hrStaffInfo.setGraduationSchool(graduationSchool);
						hrStaffInfo.setStaffMajor(staffMajor);
						hrStaffInfo.setComputerLevel(computerLevel);
						hrStaffInfo.setForeignLanguage1(foreignLanguage1);
						hrStaffInfo.setForeignLevel1(foreignLevel1);
						hrStaffInfo.setForeignLanguage2(foreignLanguage2);
						hrStaffInfo.setForeignLevel2(foreignLevel2);
						hrStaffInfo.setForeignLanguage3(foreignLanguage3);

						hrStaffInfo.setForeignLevel3(foreignLevel3);
						hrStaffInfo.setStaffSkills(staffSkills);
						hrStaffInfo.setStaffOccupation(staffOccupation);
						hrStaffInfo.setAdministrationLevel(administrationLevel);
						hrStaffInfo.setJobPosition(jobPosition);
						hrStaffInfo.setPresentPosition(presentPosition);
						if (YHUtility.isDay(datesEmployed)) {
							hrStaffInfo.setDatesEmployed(YHUtility.parseDate(datesEmployed));
						}
						hrStaffInfo.setJobAge(jobAge);
						if (YHUtility.isDay(beginSalsryTime)) {
							hrStaffInfo.setBeginSalsryTime(YHUtility.parseDate(beginSalsryTime));
						}
						hrStaffInfo.setWorkStatus(workStatus);
						hrStaffInfo.setRemark(remark);
						hrStaffInfo.setResume(resume);

						hrStaffInfo.setStaffType(staffType);
						hrStaffInfo.setCertificate(certificate);
						hrStaffInfo.setSurety(surety);
						hrStaffInfo.setInsure(insure);

						hrStaffInfo.setBodyExamim(bodyExamim);
						
						if (isStaffUser) {
						  orm.updateSingle(dbConn, hrStaffInfo);
						}
						else{
						  orm.saveSingle(dbConn, hrStaffInfo);
						}
						isCount ++;
						infoStr = "成功";
						bufferMap.put("color", "green");
					}
				}
				bufferMap.put("staffName", staffName);
				bufferMap.put("staffSex", staffSex);
				bufferMap.put("staffBirth", staffBirth);
				bufferMap.put("staffNationality", staffNationality);
				bufferMap.put("staffNativePlace", staffNativePlace);
				bufferMap.put("staffPoliticalStatus", staffPoliticalStatus);
				bufferMap.put("staffCardNo", staffCardNo);
				bufferMap.put("remark", remark);
				bufferMap.put("infoStr", infoStr);
				sbStrJson(buffer,bufferMap);
			}
			returnMap.put("isCount",isCount);
			returnMap.put("updateCount",updateCount);
			return returnMap;
		} catch (Exception e) {
			throw e;
		}
	}

	public String sbStrJson(StringBuffer sb, Map<Object, Object>map) {
		
			String staffName = (String) map.get("staffName");
			String staffSex = (String) map.get("staffSex");
			String staffBirth = (String) map.get("staffBirth");
			String staffNationality = (String) map.get("staffNationality");
			String staffNativePlace = (String) map.get("staffNativePlace");
			String staffPoliticalStatus = (String) map.get("staffPoliticalStatus");
			String staffCardNo = (String) map.get("staffCardNo");
			String remark = (String) map.get("remark");
			String infoStr = (String) map.get("infoStr");
			String color = (String) map.get("color");
		
		
		sb.append("{");
		sb.append("staffName:\"" + YHUtility.null2Empty(staffName) + "\"");  //姓名
		sb.append(",staffSex:\"" + YHUtility.null2Empty(staffSex) + "\"");//性别
		sb.append(",staffBirth:\"" + YHUtility.null2Empty(staffBirth) + "\"");//出生日期
		sb.append(",staffNationality:\"" + YHUtility.null2Empty(staffNationality) + "\"");//民族
		sb.append(",staffNativePlace:\"" + YHUtility.null2Empty(staffNativePlace) + "\"");//籍贯
		sb.append(",staffPoliticalStatus:\"" + YHUtility.null2Empty(staffPoliticalStatus) + "\"");//政治面貌
		sb.append(",staffCardNo:\"" + YHUtility.null2Empty(staffCardNo) + "\"");//身份证号码
		sb.append(",remark:\"" + YHUtility.null2Empty(remark) + "\"");//备注  
		sb.append(",infoStr:\"" + YHUtility.null2Empty(infoStr) + "\"");//信息
		sb.append(",color:\"" + YHUtility.null2Empty(color) + "\"");//颜色
		sb.append("},");
		return sb.toString();
	}
	/**
   * 获取设置信息
   * 
   * @param dbConn
   * @param parentNo
   * @param codeName
   * @return
   * @throws Exception
   */
  public String getHrCode2(Connection dbConn, String parentNo, String codeName) throws Exception {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String dataStr = "";
    try {
      String sql = "select SEQ_ID from oa_pm_code where PARENT_NO=? and CODE_NAME =?";
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1, parentNo);
      stmt.setString(2, codeName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        dataStr = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return dataStr;
  }
	/**
	 * 获取设置信息
	 * 
	 * @param dbConn
	 * @param parentNo
	 * @param codeName
	 * @return
	 * @throws Exception
	 */
	public String getHrCode3(Connection dbConn, String parentNo, String codeName) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dataStr = "";
		try {
			String sql = "select CODE_NO from oa_pm_code where PARENT_NO=? and CODE_NAME =?";
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, parentNo);
			stmt.setString(2, codeName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				dataStr = rs.getString(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return dataStr;
	}

	/**
	 * 判断是否为oa用户（与部门表关联）
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> isOaUser(Connection dbConn, String userIdStr) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dbUserId = "";
		String dbDeptId = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		boolean isOaFlag = false;
		try {
			String sql = "SELECT a.USER_ID,a.DEPT_ID from PERSON a left outer join oa_department b on a.DEPT_ID=b.SEQ_ID where a.USER_ID=?";
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userIdStr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				isOaFlag = true;
				dbUserId = rs.getString(1);
				dbDeptId = rs.getString(2);
			}
			map.put("isOaFlag", isOaFlag);
			map.put("dbUserId", dbUserId);
			map.put("dbDeptId", dbDeptId);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return map;
	}

	/**
	 * 判断名称是否已存在（Staff_Info表）
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public boolean isHrStaffUser(Connection dbConn, String userIdStr) throws Exception {
		String sql = "SELECT count(SEQ_ID) from oa_pm_employee_info where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int counter = 0;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userIdStr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
			boolean isHave = false;
			if (counter > 0) {
				isHave = true;
			}
			return isHave;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 判断名称是否已存在（person表）
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public boolean isPersonUser(Connection dbConn, String userIdStr) throws Exception {
		String sql = "SELECT count(SEQ_ID) from PERSON where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int counter = 0;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userIdStr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
			boolean isHave = false;
			if (counter > 0) {
				isHave = true;
			}
			return isHave;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 获取最小角色id
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public int getlowestRole(Connection dbConn) throws Exception {
		String sql = "select SEQ_ID from USER_PRIV order by PRIV_NO DESC";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int counter = 0;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				counter = rs.getInt(1);
				break;
			}
			return counter;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 判断部门是否存在
	 * 
	 * @param dbConn
	 * @param deptName
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> isDeptUser(Connection dbConn, String deptName) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int dbDeptId = 0;
		Map<Object, Object> map = new HashMap<Object, Object>();
		boolean isOaFlag = false;
		try {
			String sql = "select SEQ_ID from oa_department where DEPT_NAME=?";
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, deptName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				isOaFlag = true;
				dbDeptId = rs.getInt(1);
			}
			map.put("isDeptFlag", isOaFlag);
			map.put("dbDeptId", dbDeptId);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return map;
	}

}

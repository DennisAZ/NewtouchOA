package yh.subsys.oa.hr.manage.staffInfo.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.relatives.data.YHHrStaffRelatives;
import yh.subsys.oa.hr.manage.staffInfo.act.YHHrStaffInfoAct;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;

public class YHHrStaffInfoLogic {
	private static Logger log = Logger.getLogger(YHHrStaffInfoLogic.class);

	public int setNewHrStaffInfoValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		int maxSeqId = 0;
		String createUserId = String.valueOf(person.getSeqId());
		int createDeptId = person.getDeptId();
		String userId = YHUtility.null2Empty(fileForm.getParameter("userId"));
		String deptIdStr = fileForm.getParameter("deptId");//
		String userPriv = fileForm.getParameter("userPriv");
		String staffNo = fileForm.getParameter("staffNo");
		String workNo = fileForm.getParameter("workNo");
		String workType = fileForm.getParameter("workType");
		String staffName = fileForm.getParameter("staffName");
		String staffEName = fileForm.getParameter("staffEName");
		String staffCardNo = fileForm.getParameter("staffCardNo");

		String staffSex = fileForm.getParameter("staffSex");
		String staffBirth = fileForm.getParameter("staffBirth");
		String staffAge = YHUtility.null2Empty(fileForm.getParameter("staffAgeStr"));//
		String staffNativePlace = fileForm.getParameter("staffNativePlace");
		String staffDomicilePlace = fileForm.getParameter("staffDomicilePlace");
		String staffNationality = fileForm.getParameter("staffNationality");
		String staffMaritalStatus = fileForm.getParameter("staffMaritalStatus");
		String staffPoliticalStatus = fileForm.getParameter("staffPoliticalStatus");
		String joinPartyTime = fileForm.getParameter("joinPartyTime");
		String staffPhone = fileForm.getParameter("staffPhone");//
		String staffMobile = fileForm.getParameter("staffMobile");

		String staffLittleSmart = fileForm.getParameter("staffLittleSmart");
		String staffEmail = fileForm.getParameter("staffEmail");
		String staffMsn = fileForm.getParameter("staffMsn");
		String staffQq = fileForm.getParameter("staffQq");
		String homeAddress = fileForm.getParameter("homeAddress");
		String otherContact = fileForm.getParameter("otherContact");
		String jobBeginning = fileForm.getParameter("jobBeginning");
		String workAge = fileForm.getParameter("workAge");
		String staffHealth = fileForm.getParameter("staffHealth");
		String staffHighestSchool = fileForm.getParameter("staffHighestSchool");

		String staffHighestDegree = fileForm.getParameter("staffHighestDegree");
		String graduationDate = fileForm.getParameter("graduationDate");
		String graduationSchool = fileForm.getParameter("graduationSchool");
		String staffMajor = fileForm.getParameter("staffMajor");
		String computerLevel = fileForm.getParameter("computerLevel");
		String foreignLanguage1 = fileForm.getParameter("foreignLanguage1");
		String foreignLevel1 = fileForm.getParameter("foreignLevel1");
		String foreignLanguage2 = fileForm.getParameter("foreignLanguage2");
		String foreignLevel2 = fileForm.getParameter("foreignLevel2");
		String foreignLanguage3 = fileForm.getParameter("foreignLanguage3");

		String foreignLevel3 = fileForm.getParameter("foreignLevel3");
		String staffSkills = fileForm.getParameter("staffSkills");
		String staffOccupation = fileForm.getParameter("staffOccupation");
		String administrationLevel = fileForm.getParameter("administrationLevel");
		String jobPosition = fileForm.getParameter("jobPosition");
		String presentPosition = fileForm.getParameter("presentPosition");
		String datesEmployed = fileForm.getParameter("datesEmployed");
		String jobAge = fileForm.getParameter("jobAge");
		String beginSalsryTime = fileForm.getParameter("beginSalsryTime");
		// String recordDate = fileForm.getParameter("recordDate");

		String workStatus = fileForm.getParameter("workStatus");
		// String staffCs = fileForm.getParameter("staffCs");
		// String staffCtr = fileForm.getParameter("staffCtr");
		String remark = fileForm.getParameter("remark");
		// String staffCompany = fileForm.getParameter("staffCompany");
		// String photoName = fileForm.getParameter("photoName");
		// String attachmentId = fileForm.getParameter("attachmentId");
		// String attachmentName = fileForm.getParameter("attachmentName");
		String resume = fileForm.getParameter("resume");
		String leaveTypeStr = fileForm.getParameter("leaveType");

		String staffType = fileForm.getParameter("staffType");
		String yesOrNot = fileForm.getParameter("yesOrNot");
		// String userdef1 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef1"));
		// String userdef2 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef2"));
		// String userdef3 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef3"));
		// String userdef4 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef4"));
		// String userdef5 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef5"));
		String certificate = fileForm.getParameter("certificate");
		String surety = fileForm.getParameter("surety");
		String insure = fileForm.getParameter("insure");

		String bodyExamim = fileForm.getParameter("bodyExamim");
		
		String insureNum = fileForm.getParameter("insureNum");

		int deptId = 0;
		if (!YHUtility.isNullorEmpty(deptIdStr) && YHUtility.isNumber(deptIdStr)) {
			deptId = Integer.parseInt(deptIdStr);
		}
		int leaveType = 0;
		if (!YHUtility.isNullorEmpty(leaveTypeStr) && YHUtility.isNumber(leaveTypeStr)) {
			leaveType = Integer.parseInt(leaveTypeStr);
		}

		String notLogin = "";
		if ("on".equals(yesOrNot.trim())) {
			yesOrNot = "1";
			notLogin = "0";

		} else {
			yesOrNot = "0";
			notLogin = "1";
		}
		try {

			Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrStaffInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");
			String photoName = (String) map.get("photoName");

			YHHrStaffInfo hrStaffInfo = new YHHrStaffInfo();
			hrStaffInfo.setCreateUserId(createUserId);
			hrStaffInfo.setCreateDeptId(createDeptId);
			hrStaffInfo.setUserId(userId);
			hrStaffInfo.setDeptId(deptId);
			hrStaffInfo.setStaffNo(staffNo);
			hrStaffInfo.setWorkNo(workNo);
			hrStaffInfo.setWorkType(workType);
			hrStaffInfo.setStaffName(staffName);
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

			hrStaffInfo.setStaffLittleSmart(staffLittleSmart);
			hrStaffInfo.setStaffEmail(staffEmail);
			hrStaffInfo.setStaffMsn(staffMsn);
			hrStaffInfo.setStaffQq(staffQq);
			hrStaffInfo.setHomeAddress(homeAddress);
			hrStaffInfo.setOtherContact(otherContact);
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
			// hrStaffInfo.setRecordDate(YHUtility.parseDate(recordDate));

			hrStaffInfo.setWorkStatus(workStatus);
			// hrStaffInfo.setStaffCs(YHUtility.parseDate(staffCs));
			// hrStaffInfo.setStaffCtr(YHUtility.parseDate(staffCtr));
			hrStaffInfo.setRemark(remark);
			// hrStaffInfo.setStaffCompany(staffCompany);
			if (!YHUtility.isNullorEmpty(photoName)) {
				hrStaffInfo.setPhotoName(photoName);
			}
			if (attachFlag) {
				hrStaffInfo.setAttachmentId(attachmentIds);
				hrStaffInfo.setAttachmentName(attachmentNames);
			}
			hrStaffInfo.setResume(resume);
			hrStaffInfo.setLeaveType(leaveType);

			hrStaffInfo.setStaffType(staffType);
			hrStaffInfo.setYesOrNot(yesOrNot);
			// hrStaffInfo.setUserdef1(userdef1);
			// hrStaffInfo.setUserdef2(userdef2);
			// hrStaffInfo.setUserdef3(userdef3);
			// hrStaffInfo.setUserdef4(userdef4);
			// hrStaffInfo.setUserdef5(userdef5);
			hrStaffInfo.setCertificate(certificate);
			hrStaffInfo.setSurety(surety);
			hrStaffInfo.setInsure(insure);

			hrStaffInfo.setBodyExamim(bodyExamim);
			
			hrStaffInfo.setInsureNum(insureNum);
			orm.saveSingle(dbConn, hrStaffInfo);
			maxSeqId = this.getMaxSeqId(dbConn);
			boolean personUserFlag = this.checkPersonUserLogic(dbConn, userId);
			if (!personUserFlag) {
				YHPerson user = new YHPerson();
				user.setUserId(userId);
				user.setUserName(staffName);
				user.setSex(staffSex);
				user.setPassword(YHPassEncrypt.encryptPass(""));
				user.setUserPriv(userPriv);
				user.setPostPriv("0");
				user.setPostDept("");
				user.setCanbroadcast(0);
				user.setDeptId(deptId);
				//user.setAuatar(staffSex);

				user.setCallSound("1");
				user.setDutyType(1);
				user.setSmsOn("1");
				user.setMenuType("2");
				user.setEmailCapacity(0);
				user.setFolderCapacity(0);
				user.setUserPrivOther("");
				user.setUserNo(10);
				user.setNotLogin(notLogin);
				user.setByname("");

				user.setBirthday(YHUtility.parseDate(staffBirth));
				user.setTheme("1");
				user.setMobilNo("");
				user.setMobilNoHidden("0");
				orm.saveSingle(dbConn, user);
			}
			return maxSeqId;
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 处理上传附件，返回附件id，附件名称--wyw
	 * 
	 * @param fileForm
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> fileUploadLogic(YHFileUploadForm fileForm, String attachmentFolder) throws Exception {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, attachmentFolder);
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");
			boolean fromFolderFlag = false;
			String forlderAttchId = "";
			String forlderAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				forlderAttchId = attIdStr + ",";
				forlderAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
			Iterator<String> iKeys = fileForm.iterateFileFields();
			boolean uploadFlag = false;
			String uploadAttchId = "";
			String uploadAttchName = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + attachmentFolder + separator + currDate;
			String headPicPath = YHSysProps.getAttachPath() + separator + YHHrStaffInfoAct.headPicFolder;

			String headPicName = YHUtility.null2Empty(fileForm.getParameter("userId"));
			String hrPoolAttachmentId = YHUtility.null2Empty(fileForm.getParameter("hrPoolAttachmentId"));
			String hrPoolAttachmentName = YHUtility.null2Empty(fileForm.getParameter("hrPoolAttachmentName"));
			

			String photoName = "";
			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				if (!YHUtility.isNullorEmpty(fieldName) && "headPic".equals(fieldName)) {

					String type = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					photoName = headPicName + type;
					fileForm.saveFile(fieldName, headPicPath + File.separator + photoName);

				} else {
					YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
					String rand = emul.getRandom();
					uploadAttchId += currDate + "_" + rand + ",";
					uploadAttchName += fileName + "*";
					uploadFlag = true;
					fileName = rand + "_" + fileName;
					fileForm.saveFile(fieldName, filePath + File.separator + fileName);
				}
			}
			boolean attachFlag = false;
			String attachmentIds = "";
			String attachmentNames = "";
			if (fromFolderFlag && uploadFlag) {
				attachmentIds = forlderAttchId + uploadAttchId;
				attachmentNames = forlderAttchName + uploadAttchName;
				attachFlag = true;
			} else if (fromFolderFlag) {
				attachmentIds = forlderAttchId;
				attachmentNames = forlderAttchName;
				attachFlag = true;
			} else if (uploadFlag) {
				attachmentIds = uploadAttchId;
				attachmentNames = uploadAttchName;
				attachFlag = true;
			}
			if (!YHUtility.isNullorEmpty(hrPoolAttachmentName)) {
				attachmentIds += hrPoolAttachmentName;
				attachmentNames += hrPoolAttachmentName;
				attachFlag = true;
			}
			result.put("attachFlag", attachFlag);
			result.put("attachmentIds", attachmentIds.trim());
			result.put("attachmentNames", attachmentNames.trim());
			result.put("photoName", photoName);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 获取角色名称
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getUserPrivLogic(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		StringBuffer buffer = new StringBuffer();
		try {
			boolean isHave = false;
			List<YHUserPriv> userPrivs = (List<YHUserPriv>) orm.loadListSingle(dbConn, YHUserPriv.class, new HashMap());
			if (userPrivs != null && userPrivs.size() > 0) {
				buffer.append("[");
				for (YHUserPriv userPriv : userPrivs) {
					String privName = YHUtility.null2Empty(userPriv.getPrivName());
					buffer.append("{");
					buffer.append("seqId:\"" + userPriv.getSeqId() + "\"");
					buffer.append(",privName:\"" + YHUtility.encodeSpecial(privName) + "\"");
					buffer.append("},");
					isHave = true;
				}
				if (isHave) {
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("]");
			} else {

				buffer.append("[]");

			}

		} catch (Exception e) {
			throw e;
		}
		return buffer.toString();
	}

	/**
	 * 判断名称是否已存在（Staff_Info表）
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public String checkUserLogic(Connection dbConn, String userIdStr) throws Exception {
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
			int isHave = 0;
			if (counter > 0) {
				isHave = 1;
			}
			String data = "{isHave:\"" + isHave + "\"}";
			return data;
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
	public boolean checkPersonUserLogic(Connection dbConn, String userIdStr) throws Exception {
		String sql = "SELECT count(SEQ_ID) from PERSON where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int counter = 0;
		boolean isHave = false;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userIdStr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
			if (counter > 0) {
				isHave = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return isHave;
	}

	/**
	 * 人事档案信息列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getStaffInfoListJsonLogic(Connection dbConn, Map request, YHPerson person, int deptId) throws Exception {
		try {
			String sql = "select " + " a.SEQ_ID" + ", a.STAFF_NAME" + ", a.STAFF_SEX" + ", a.STAFF_BIRTH" + ", a.STAFF_NATIONALITY"
					+ ", a.STAFF_NATIVE_PLACE" + ", a.STAFF_POLITICAL_STATUS" + ", a.STAFF_CARD_NO" + " from oa_pm_employee_info a"
					+ " LEFT OUTER JOIN PERSON b ON a.USER_ID = b.USER_ID " + " LEFT OUTER JOIN USER_PRIV  c ON b.USER_PRIV=c.SEQ_ID "
					+ " LEFT OUTER JOIN oa_department d ON b.DEPT_ID=d.SEQ_ID " + " where d.SEQ_ID=" + deptId;
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据hr表的seqId获取信息
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public String getPersonInfoLogic(Connection dbConn, int seqId) throws Exception {
		String sql = "SELECT SEQ_ID, USER_NAME,DEPT_ID,USER_PRIV from PERSON where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int deptId = 0;
		int personSeqId = 0;
		String userName = "";
		String deptName = "";
		String userPriv = "";
		try {
			String userId = "";
			YHHrStaffInfo staffInfo = this.getObjSingle(dbConn, seqId);
			if (staffInfo != null) {
				userId = YHUtility.null2Empty(staffInfo.getUserId());
			}else {
				userId =String.valueOf(seqId);
			}
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				userName = YHUtility.null2Empty(rs.getString("USER_NAME"));
				personSeqId = rs.getInt("SEQ_ID");
				deptId = rs.getInt("DEPT_ID");
				userPriv = rs.getString("USER_PRIV");
			}
			deptName = this.getDeptNameById(dbConn, deptId);
			String data = "{userName:\"" + YHUtility.encodeSpecial(userName) + "\",personSeqId:\"" + personSeqId + "\",userPriv:\"" + YHUtility.null2Empty(userPriv) + "\",deptId:\"" + deptId + "\",deptName:\""
					+ YHUtility.encodeSpecial(deptName) + "\" }";
			return data;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 根据person表的userId获取信息
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public String getPersonInfoByUserIdLogic(Connection dbConn, String userId) throws Exception {
		String sql = "SELECT SEQ_ID,USER_NAME,DEPT_ID,USER_PRIV from PERSON where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int personSeqId = 0;
		int deptId = 0;
		String userName = "";
		String deptName = "";
		String userPriv = "";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				userName = YHUtility.null2Empty(rs.getString("USER_NAME"));
				personSeqId = rs.getInt("SEQ_ID");
				deptId = rs.getInt("DEPT_ID");
				userPriv = rs.getString("USER_PRIV");
			}
			deptName = this.getDeptNameById(dbConn, deptId);
			String data = "{userName:\"" + YHUtility.encodeSpecial(userName)+ "\",personSeqId:\"" + personSeqId +  "\",userPriv:\"" + YHUtility.null2Empty(userPriv) + "\",deptId:\"" + deptId + "\",deptName:\""
					+ YHUtility.encodeSpecial(deptName) + "\" }";
			return data;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}
	/**
	 * 根据person表的userId获取信息
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public YHHrStaffInfo getStaffInfoByUserIdLogic(Connection dbConn, String seqIdStr) throws Exception {
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		YHORM orm = new YHORM();
		
		try {
			YHHrStaffInfo staffInfo = new YHHrStaffInfo();
			YHPerson person = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, seqId);
			if (person!=null) {
				String userIdStr = YHUtility.null2Empty(person.getUserId());
				if (!YHUtility.isNullorEmpty(userIdStr)) {
					Map filters = new HashMap();
					filters.put("USER_ID", userIdStr);
					staffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, filters);
				}
				
			}
			return staffInfo;
		} catch (Exception e) {
			throw e;
		} 
	}

	public YHHrStaffInfo getObjSingle(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		try {
			return (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, seqId);
		} catch (Exception e) {
			throw e;
		}
	}

	public String getDeptNameById(Connection dbConn, int deptId) throws Exception {
		YHORM orm = new YHORM();
		String deptName = "";
		try {
			YHDepartment department = (YHDepartment) orm.loadObjSingle(dbConn, YHDepartment.class, deptId);
			if (department != null) {
				deptName = YHUtility.null2Empty(department.getDeptName());
			}
		} catch (Exception e) {
			throw e;
		}
		return deptName;
	}

	/**
	 * 根据person表的userId获取信息
	 * 
	 * @param dbConn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public YHHrStaffInfo getStaffInfoByIdLogic(Connection dbConn, String userIdStr) throws Exception {
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			map.put("USER_ID", userIdStr);
			return (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, map);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 编辑人事档案
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public int updateHrStaffInfoValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		int updateSeqId = 0;
		String createUserId = String.valueOf(person.getSeqId());
		int createDeptId = person.getDeptId();
		String userId = YHUtility.null2Empty(fileForm.getParameter("userId"));
		String deptIdStr = fileForm.getParameter("deptId");//
		String userPriv = fileForm.getParameter("userPriv");
		String staffNo = fileForm.getParameter("staffNo");
		String workNo = fileForm.getParameter("workNo");
		String workType = fileForm.getParameter("workType");
		String staffName = fileForm.getParameter("staffName");
		String staffEName = fileForm.getParameter("staffEName");
		String staffCardNo = fileForm.getParameter("staffCardNo");

		String staffSex = fileForm.getParameter("staffSex");
		String staffBirth = fileForm.getParameter("staffBirth");
		String staffAge = YHUtility.null2Empty(fileForm.getParameter("staffAge"));
		String staffNativePlace = fileForm.getParameter("staffNativePlace");
		String staffDomicilePlace = fileForm.getParameter("staffDomicilePlace");
		String staffNationality = fileForm.getParameter("staffNationality");
		String staffMaritalStatus = fileForm.getParameter("staffMaritalStatus");
		String staffPoliticalStatus = fileForm.getParameter("staffPoliticalStatus");
		String joinPartyTime = fileForm.getParameter("joinPartyTime");
		String staffPhone = fileForm.getParameter("staffPhone");//
		String staffMobile = fileForm.getParameter("staffMobile");

		String staffLittleSmart = fileForm.getParameter("staffLittleSmart");
		String staffEmail = fileForm.getParameter("staffEmail");
		String staffMsn = fileForm.getParameter("staffMsn");
		String staffQq = fileForm.getParameter("staffQq");
		String homeAddress = fileForm.getParameter("homeAddress");
		String otherContact = fileForm.getParameter("otherContact");
		String jobBeginning = fileForm.getParameter("jobBeginning");
		String workAge = fileForm.getParameter("workAge");
		String staffHealth = fileForm.getParameter("staffHealth");
		String staffHighestSchool = fileForm.getParameter("staffHighestSchool");

		String staffHighestDegree = fileForm.getParameter("staffHighestDegree");
		String graduationDate = fileForm.getParameter("graduationDate");
		String graduationSchool = fileForm.getParameter("graduationSchool");
		String staffMajor = fileForm.getParameter("staffMajor");
		String computerLevel = fileForm.getParameter("computerLevel");
		String foreignLanguage1 = fileForm.getParameter("foreignLanguage1");
		String foreignLevel1 = fileForm.getParameter("foreignLevel1");
		String foreignLanguage2 = fileForm.getParameter("foreignLanguage2");
		String foreignLevel2 = fileForm.getParameter("foreignLevel2");
		String foreignLanguage3 = fileForm.getParameter("foreignLanguage3");

		String foreignLevel3 = fileForm.getParameter("foreignLevel3");
		String staffSkills = fileForm.getParameter("staffSkills");
		String staffOccupation = fileForm.getParameter("staffOccupation");
		String administrationLevel = fileForm.getParameter("administrationLevel");
		String jobPosition = fileForm.getParameter("jobPosition");
		String presentPosition = fileForm.getParameter("presentPosition");
		String datesEmployed = fileForm.getParameter("datesEmployed");
		String jobAge = fileForm.getParameter("jobAge");
		String beginSalsryTime = fileForm.getParameter("beginSalsryTime");
		// String recordDate = fileForm.getParameter("recordDate");

		String workStatus = fileForm.getParameter("workStatus");
		// String staffCs = fileForm.getParameter("staffCs");
		// String staffCtr = fileForm.getParameter("staffCtr");
		String remark = fileForm.getParameter("remark");
		// String staffCompany = fileForm.getParameter("staffCompany");
		// String photoName = fileForm.getParameter("photoName");
		// String attachmentId = fileForm.getParameter("attachmentId");
		// String attachmentName = fileForm.getParameter("attachmentName");
		String resume = fileForm.getParameter("resume");
		String leaveTypeStr = fileForm.getParameter("leaveType");

		String staffType = fileForm.getParameter("staffType");
		String yesOrNot = fileForm.getParameter("yesOrNot");
		// String userdef1 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef1"));
		// String userdef2 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef2"));
		// String userdef3 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef3"));
		// String userdef4 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef4"));
		// String userdef5 =
		// YHUtility.null2Empty(fileForm.getParameter("userdef5"));
		String certificate = fileForm.getParameter("certificate");
		String surety = fileForm.getParameter("surety");
		String insure = fileForm.getParameter("insure");

		String bodyExamim = fileForm.getParameter("bodyExamim");
		String insureNum = fileForm.getParameter("insureNum");

		int deptId = -1;
		if (!YHUtility.isNullorEmpty(deptIdStr) && YHUtility.isNumber(deptIdStr)) {
			deptId = Integer.parseInt(deptIdStr);
		}
		int leaveType = 0;
		if (!YHUtility.isNullorEmpty(leaveTypeStr) && YHUtility.isNumber(leaveTypeStr)) {
			leaveType = Integer.parseInt(leaveTypeStr);
		}

		String notLogin = "";
		if ("on".equals(yesOrNot.trim())) {
			yesOrNot = "1";
			notLogin = "0";
		} else {
			yesOrNot = "0";
			notLogin = "1";
		}
		try {
			Map map = new HashMap();
			map.put("USER_ID", userId);
			YHHrStaffInfo hrStaffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, map);
			if (hrStaffInfo != null) {
				Map<Object, Object> attMap = this.fileUploadLogic(fileForm, YHHrStaffInfoAct.attachmentFolder);
				boolean attachFlag = (Boolean) attMap.get("attachFlag");
				String attachmentIds = (String) attMap.get("attachmentIds");
				String attachmentNames = (String) attMap.get("attachmentNames");
				String photoName = (String) attMap.get("photoName");

				// YHHrStaffInfo hrStaffInfo = new YHHrStaffInfo();
				hrStaffInfo.setCreateUserId(createUserId);
				hrStaffInfo.setCreateDeptId(createDeptId);
				hrStaffInfo.setUserId(userId);
				hrStaffInfo.setDeptId(deptId);
				hrStaffInfo.setStaffNo(staffNo);
				hrStaffInfo.setWorkNo(workNo);
				hrStaffInfo.setWorkType(workType);
				hrStaffInfo.setStaffName(staffName);
				hrStaffInfo.setStaffEName(staffEName);
				hrStaffInfo.setStaffCardNo(staffCardNo);

				hrStaffInfo.setStaffSex(staffSex);
				hrStaffInfo.setStaffBirth(YHUtility.parseDate(staffBirth));
				hrStaffInfo.setStaffAge(String.valueOf(staffAge));
				hrStaffInfo.setStaffNativePlace(staffNativePlace);
				hrStaffInfo.setStaffDomicilePlace(staffDomicilePlace);
				hrStaffInfo.setStaffNationality(staffNationality);
				hrStaffInfo.setStaffMaritalStatus(staffMaritalStatus);
				hrStaffInfo.setStaffPoliticalStatus(staffPoliticalStatus);
				hrStaffInfo.setJoinPartyTime(YHUtility.parseDate(joinPartyTime));
				hrStaffInfo.setStaffPhone(staffPhone);
				hrStaffInfo.setStaffMobile(staffMobile);

				hrStaffInfo.setStaffLittleSmart(staffLittleSmart);
				hrStaffInfo.setStaffEmail(staffEmail);
				hrStaffInfo.setStaffMsn(staffMsn);
				hrStaffInfo.setStaffQq(staffQq);
				hrStaffInfo.setHomeAddress(homeAddress);
				hrStaffInfo.setOtherContact(otherContact);
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
				// hrStaffInfo.setRecordDate(YHUtility.parseDate(recordDate));

				hrStaffInfo.setWorkStatus(workStatus);
				// hrStaffInfo.setStaffCs(YHUtility.parseDate(staffCs));
				// hrStaffInfo.setStaffCtr(YHUtility.parseDate(staffCtr));
				hrStaffInfo.setRemark(remark);
				// hrStaffInfo.setStaffCompany(staffCompany);
				if (!YHUtility.isNullorEmpty(photoName)) {
					hrStaffInfo.setPhotoName(photoName);
				}
				if (attachFlag) {
					String dbAttaIds = YHUtility.null2Empty(hrStaffInfo.getAttachmentId());
					String dbAttaNames = YHUtility.null2Empty(hrStaffInfo.getAttachmentName());
					hrStaffInfo.setAttachmentId(dbAttaIds + attachmentIds);
					hrStaffInfo.setAttachmentName(dbAttaNames + attachmentNames);
				}
				hrStaffInfo.setResume(resume);
				hrStaffInfo.setLeaveType(leaveType);

				hrStaffInfo.setStaffType(staffType);
				hrStaffInfo.setYesOrNot(yesOrNot);
				// hrStaffInfo.setUserdef1(userdef1);
				// hrStaffInfo.setUserdef2(userdef2);
				// hrStaffInfo.setUserdef3(userdef3);
				// hrStaffInfo.setUserdef4(userdef4);
				// hrStaffInfo.setUserdef5(userdef5);
				hrStaffInfo.setCertificate(certificate);
				hrStaffInfo.setSurety(surety);
				hrStaffInfo.setInsure(insure);

				hrStaffInfo.setBodyExamim(bodyExamim);
				hrStaffInfo.setInsureNum(insureNum);
				orm.updateSingle(dbConn, hrStaffInfo);
				updateSeqId = hrStaffInfo.getSeqId();

				Map personMap = new HashMap();
				personMap.put("USER_ID", userId);
				YHPerson user = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, personMap);
				if (user != null) {
					user.setSex(staffSex);
					if (!YHUtility.isNullorEmpty(userPriv)) {
						user.setUserPriv(userPriv);
					}
					orm.updateSingle(dbConn, user);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return updateSeqId;
	}

	/**
	 * 获取最大的SeqId值
	 * 
	 * 
	 * @param dbConn
	 * @return
	 */
	public int getMaxSeqId(Connection dbConn) {
		String sql = "select SEQ_ID from oa_pm_employee_info where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_employee_info )";
		int seqId = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				seqId = rs.getInt("SEQ_ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return seqId;
	}

	/**
	 * 删除
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @throws Exception
	 */
	public void delHrStaffInfoLogic(Connection dbConn, String seqIdStr) throws Exception {
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "";
		}
		YHORM orm = new YHORM();
		try {
			String[] seqIdArry = seqIdStr.split(",");
			if (seqIdArry != null && seqIdArry.length > 0) {
				for (String seqId : seqIdArry) {
					YHHrStaffInfo staffInfo = this.getObjSingle(dbConn, Integer.parseInt(seqId));
					if (staffInfo != null) {
						String photoName = YHUtility.null2Empty(staffInfo.getPhotoName());
						if (!YHUtility.isNullorEmpty(photoName)) {
							this.deleHeadPic(photoName);
						}
						orm.deleteSingle(dbConn, staffInfo);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 删除部门下的所有人事档案信息
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @throws Exception
	 */
	public void delHrStaffInfoByDeptIdLogic(Connection dbConn, String deptIdStr) throws Exception {
		int deptId = -1;
		if (YHUtility.isNumber(deptIdStr)) {
			deptId = Integer.parseInt(deptIdStr);
		}
		YHORM orm = new YHORM();
		try {
			Map filters = new HashMap();
			filters.put("DEPT_ID", deptId);
			List<YHHrStaffInfo> hrStaffInfos = orm.loadListSingle(dbConn, YHHrStaffInfo.class, filters);
			if (hrStaffInfos!=null && hrStaffInfos.size()>0) {
				for(YHHrStaffInfo hrStaffInfo:hrStaffInfos){
					String photoName = YHUtility.null2Empty(hrStaffInfo.getPhotoName());
					if (!YHUtility.isNullorEmpty(photoName)) {
						this.deleHeadPic(photoName);
					}
					orm.deleteSingle(dbConn, hrStaffInfo);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean deleHeadPic(String photoName) throws Exception {
		String filePath = YHSysProps.getAttachPath() + File.separator + YHHrStaffInfoAct.headPicFolder + File.separator + photoName;
		boolean delFlag = false;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				YHFileUtility.deleteAll(file.getAbsoluteFile());
				delFlag = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return delFlag;
	}

	/**
	 * 取得部门人员名称--wyw
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getDeptNameLogic(Connection dbConn, String seqIdStr) throws Exception {
		String result = "";
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			return result;
		}
		if (seqIdStr.endsWith(",")) {
			seqIdStr = seqIdStr.substring(0, seqIdStr.length() - 1);
		}
		String sql = "select DEPT_NAME from oa_department where SEQ_ID in(" + seqIdStr + ")";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result += toId + ",";
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		if (result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	public String getNotLoginInfoLogic(Connection dbConn, String seqIdStr) throws Exception {
		YHORM orm = new YHORM();
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		String userIdStr = "";
		String notLoginStr = "";
		try {
			YHHrStaffInfo staffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, seqId);
			if (staffInfo != null) {
				userIdStr = YHUtility.null2Empty(staffInfo.getUserId());
			}
			if (!YHUtility.isNullorEmpty(userIdStr)) {
				Map filters = new HashMap();
				filters.put("USER_ID", userIdStr);
				YHPerson person = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, filters);
				if (person != null) {
					notLoginStr = YHUtility.null2Empty(person.getNotLogin());
				}
			}
			return notLoginStr;

		} catch (Exception e) {
			throw e;
		}
	}

	//

	/**
	 * 人事档案查询
	 * 
	 * @param dbConn
	 * @param request
	 * @param map
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String queryStaffInfoListLogic(Connection dbConn, Map request, YHPerson person, Map map) throws Exception {
		String userId = (String) map.get("userId");
		String staffName = (String) map.get("staffName");
		String staffEName = (String) map.get("staffEName");
		String staffNo = (String) map.get("staffNo");
		String workNo = (String) map.get("workNo");
		String deptId = (String) map.get("deptId");
		String staffCardNo = (String) map.get("staffCardNo");
		String staffBirth = (String) map.get("staffBirth");
		String staffSex = (String) map.get("staffSex");
		String staffHighestSchool = (String) map.get("staffHighestSchool");

		String staffHighestDegree = (String) map.get("staffHighestDegree");
		String staffMajor = (String) map.get("staffMajor");
		String staffNationality = (String) map.get("staffNationality");
		String staffNativePlace = (String) map.get("staffNativePlace");
		String staffMaritalStatus = (String) map.get("staffMaritalStatus");
		String staffOccupation = (String) map.get("staffOccupation");
		String staffPoliticalStatus = (String) map.get("staffPoliticalStatus");
		String workType = (String) map.get("workType");
		String datesEmployed = (String) map.get("datesEmployed");
		String joinPartyTime = (String) map.get("joinPartyTime");

		String staffPhone = (String) map.get("staffPhone");
		String administrationLevel = (String) map.get("administrationLevel");
		String staffMobile = (String) map.get("staffMobile");
		String staffEmail = (String) map.get("staffEmail");
		String staffLittleSmart = (String) map.get("staffLittleSmart");
		String staffMsn = (String) map.get("staffMsn");
		String staffQq = (String) map.get("staffQq");
		String homeAddress = (String) map.get("homeAddress");
		String jobPosition = (String) map.get("jobPosition");
		String presentPosition = (String) map.get("presentPosition");


		String jobBeginning = (String) map.get("jobBeginning");
		String graduationDate = (String) map.get("graduationDate");
		String jobAge = (String) map.get("jobAge");
		String workAge = (String) map.get("workAge");
		String staffHealth = (String) map.get("staffHealth");
		String staffDomicilePlace = (String) map.get("staffDomicilePlace");
		String graduationSchool = (String) map.get("graduationSchool");
		String computerLevel = (String) map.get("computerLevel");
		String beginSalsryTime = (String) map.get("beginSalsryTime");
		String foreignLanguage1 = (String) map.get("foreignLanguage1");

		String foreignLanguage2 = (String) map.get("foreignLanguage2");
		String foreignLanguage3 = (String) map.get("foreignLanguage3");
		String foreignLevel1 = (String) map.get("foreignLevel1");
		String foreignLevel2 = (String) map.get("foreignLevel2");
		String foreignLevel3 = (String) map.get("foreignLevel3");
		String staffSkills = (String) map.get("staffSkills");
		
		String insureNum = (String) map.get("insureNum");

		String conditionStr = "";
		String sql = "";
		try {
			if (!YHUtility.isNullorEmpty(userId)) {
				conditionStr = " and a.USER_ID ='" + YHDBUtility.escapeLike(userId) + "'";
			}

			if (!YHUtility.isNullorEmpty(staffName)) {
				conditionStr += " and a.STAFF_NAME ='" + YHDBUtility.escapeLike(staffName) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffNo)) {
				conditionStr += " and a.STAFF_NO ='" + YHDBUtility.escapeLike(staffNo) + "'";
			}

			if (!YHUtility.isNullorEmpty(workNo)) {
				conditionStr += " and a.WORK_NO ='" + YHDBUtility.escapeLike(workNo) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffEName)) {
				conditionStr += " and a.STAFF_E_NAME ='" + YHDBUtility.escapeLike(staffEName) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffCardNo)) {
				conditionStr += " and a.STAFF_CARD_NO ='" + YHDBUtility.escapeLike(staffCardNo) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffSex)) {
				conditionStr += " and a.STAFF_SEX ='" + YHDBUtility.escapeLike(staffSex) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffBirth)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.STAFF_BIRTH", staffBirth, "=");
			}
			if (!YHUtility.isNullorEmpty(staffNativePlace)) {
				conditionStr += " and a.STAFF_NATIVE_PLACE ='" + YHDBUtility.escapeLike(staffNativePlace) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffNationality)) {
				conditionStr += " and a.STAFF_NATIONALITY ='" + YHDBUtility.escapeLike(staffNationality) + "'";
			}

			if (!YHUtility.isNullorEmpty(staffMaritalStatus)) {
				conditionStr += " and a.STAFF_MARITAL_STATUS ='" + YHDBUtility.escapeLike(staffMaritalStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffPoliticalStatus)) {
				conditionStr += " and a.STAFF_POLITICAL_STATUS ='" + YHDBUtility.escapeLike(staffPoliticalStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(joinPartyTime)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOIN_PARTY_TIME", joinPartyTime, "=");
			}
			if (!YHUtility.isNullorEmpty(staffPhone)) {
				conditionStr += " and a.STAFF_PHONE ='" + YHDBUtility.escapeLike(staffPhone) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffMobile)) {
				conditionStr += " and a.STAFF_MOBILE ='" + YHDBUtility.escapeLike(staffMobile) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffLittleSmart)) {
				conditionStr += " and a.STAFF_LITTLE_SMART ='" + YHDBUtility.escapeLike(staffLittleSmart) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffMsn)) {
				conditionStr += " and a.STAFF_MSN ='" + YHDBUtility.escapeLike(staffMsn) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffQq)) {
				conditionStr += " and a.STAFF_QQ ='" + YHDBUtility.escapeLike(staffQq) + "'"; 
			}

			if (!YHUtility.isNullorEmpty(staffEmail)) {
				conditionStr += " and a.STAFF_EMAIL ='" + YHDBUtility.escapeLike(staffEmail) + "'";
			}
			if (!YHUtility.isNullorEmpty(homeAddress)) {
				conditionStr += " and a.HOME_ADDRESS ='" + YHDBUtility.escapeLike(homeAddress) + "'";
			}

			if (!YHUtility.isNullorEmpty(jobBeginning)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOB_BEGINNING", jobBeginning, "=");
			}
			if (!YHUtility.isNullorEmpty(workAge)) {
				conditionStr += " and a.WORK_AGE ='" + YHDBUtility.escapeLike(workAge) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffHealth)) {
				conditionStr += " and a.STAFF_HEALTH ='" + YHDBUtility.escapeLike(staffHealth) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffDomicilePlace)) {
				conditionStr += " and a.STAFF_DOMICILE_PLACE ='" + YHDBUtility.escapeLike(staffDomicilePlace) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffHighestSchool)) {
				conditionStr += " and a.STAFF_HIGHEST_SCHOOL ='" + YHDBUtility.escapeLike(staffHighestSchool) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffHighestDegree)) {
				conditionStr += " and a.STAFF_HIGHEST_DEGREE ='" + YHDBUtility.escapeLike(staffHighestDegree) + "'";
			}
			if (!YHUtility.isNullorEmpty(graduationDate)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.GRADUATION_DATE", graduationDate, "=");
			}
			if (!YHUtility.isNullorEmpty(graduationSchool)) {
				conditionStr += " and a.GRADUATION_SCHOOL ='" + YHDBUtility.escapeLike(graduationSchool) + "'";
			}

			if (!YHUtility.isNullorEmpty(staffMajor)) {
				conditionStr += " and a.STAFF_MAJOR ='" + YHDBUtility.escapeLike(staffMajor) + "'";
			}
			if (!YHUtility.isNullorEmpty(computerLevel)) {
				conditionStr += " and a.COMPUTER_LEVEL ='" + YHDBUtility.escapeLike(computerLevel) + "'";
			}

			if (!YHUtility.isNullorEmpty(foreignLanguage1)) {
				conditionStr += " and a.FOREIGN_LANGUAGE1 ='" + YHDBUtility.escapeLike(foreignLanguage1) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage2)) {
				conditionStr += " and a.FOREIGN_LANGUAGE2 ='" + YHDBUtility.escapeLike(foreignLanguage2) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage3)) {
				conditionStr += " and a.FOREIGN_LANGUAGE3 ='" + YHDBUtility.escapeLike(foreignLanguage3) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLevel1)) {
				conditionStr += " and a.FOREIGN_LEVEL1 ='" + YHDBUtility.escapeLike(foreignLevel1) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLevel2)) {
				conditionStr += " and a.FOREIGN_LEVEL2 ='" + YHDBUtility.escapeLike(foreignLevel2) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLevel3)) {
				conditionStr += " and a.FOREIGN_LEVEL3 ='" + YHDBUtility.escapeLike(foreignLevel3) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffSkills)) {
				conditionStr += " and a.STAFF_SKILLS ='" + YHDBUtility.escapeLike(staffSkills) + "'";
			}
			if (!YHUtility.isNullorEmpty(workType)) {
				conditionStr += " and a.WORK_TYPE ='" + YHDBUtility.escapeLike(workType) + "'";
			}

			if (!YHUtility.isNullorEmpty(deptId)) {
				conditionStr += " and a.DEPT_ID ='" + YHDBUtility.escapeLike(deptId) + "'";
			}
			if (!YHUtility.isNullorEmpty(administrationLevel)) {
				conditionStr += " and a.ADMINISTRATION_LEVEL ='" + YHDBUtility.escapeLike(administrationLevel) + "'";
			}

			if (!YHUtility.isNullorEmpty(jobPosition)) {
				conditionStr += " and a.JOB_POSITION ='" + YHDBUtility.escapeLike(jobPosition) + "'";
			}
			if (!YHUtility.isNullorEmpty(presentPosition)) {
				conditionStr += " and a.PRESENT_POSITION ='" + YHDBUtility.escapeLike(presentPosition) + "'";
			}
			if (!YHUtility.isNullorEmpty(datesEmployed)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.DATES_EMPLOYED", datesEmployed, "=");
			}
			if (!YHUtility.isNullorEmpty(jobAge)) {
				conditionStr += " and a.JOB_AGE ='" + YHDBUtility.escapeLike(jobAge) + "'";
			}

			if (!YHUtility.isNullorEmpty(beginSalsryTime)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.BEGIN_SALSRY_TIME", beginSalsryTime, "=");
			}
			if (!YHUtility.isNullorEmpty(staffOccupation)) {
				conditionStr += " and a.STAFF_OCCUPATION ='" + YHDBUtility.escapeLike(staffOccupation) + "'";
			}
      if (!YHUtility.isNullorEmpty(insureNum)) {
        conditionStr += " and a.INSURE_NUM ='" + YHDBUtility.escapeLike(insureNum) + "'";
      }

			sql = "select " 
				+ " a.SEQ_ID" 
				+ ", a.USER_ID" 
				+ ", b.USER_NAME" 
				+ ", a.STAFF_NO" 
				+ ", a.WORK_NO" 
				+ ", a.STAFF_BIRTH"
				+ ", a.STAFF_SEX"
				+ " from oa_pm_employee_info a " 
				+ " LEFT OUTER JOIN PERSON b ON a.USER_ID = b.USER_ID"
				+ " LEFT OUTER JOIN USER_PRIV  c ON b.USER_PRIV=c.SEQ_ID"
				+ " LEFT OUTER JOIN oa_department d ON a.DEPT_ID=d.SEQ_ID"
				+ " where 1=1" + conditionStr 
				+ " order by c.PRIV_NO,b.USER_NO,b.USER_NAME";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getStaffAgeLogic(Connection dbConn,int seqId) throws Exception{
		YHORM orm = new YHORM();
		String staffAge = "";
		try {
			YHHrStaffInfo staffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, seqId);
			if (staffInfo!=null) {
				Date staffBirth = staffInfo.getStaffBirth();
				if (staffBirth !=null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String staffBirthStr = format.format(staffBirth);
					if (!"0000-00-00".equals(staffBirthStr)) {
						Calendar nowCalendar = Calendar.getInstance();
						nowCalendar.setTime(new Date());
						int thisYear = nowCalendar.get(Calendar.YEAR);
						
						nowCalendar.setTime(staffBirth);
						int staffYear = nowCalendar.get(Calendar.YEAR);
						staffAge = String.valueOf(thisYear - staffYear);
					}
					
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return staffAge;
	}
	
	public boolean checkDate(Connection dbConn, int retireAge, Date staffBirth) throws Exception {
		boolean flag = false;
		try {
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			int thisYear = now.get(Calendar.YEAR);
			int yearMin = thisYear - retireAge;
			int thisMaxDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
			now.set(Calendar.YEAR, yearMin);
			now.set(Calendar.DATE, 1);
			Date minDate = now.getTime();

			now.set(Calendar.DATE, thisMaxDay);
			Date maxDate = now.getTime();

			if (minDate.compareTo(staffBirth) <= 0 && staffBirth.compareTo(maxDate) <= 0) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
	
	/**
	 * 删除头像图片
	 * @param dbConn
	 * @param seqIdStr
	 * @throws Exception
	 */
	public void deletePhotoLogic(Connection dbConn, String seqIdStr) throws Exception {
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		YHORM orm = new YHORM();
		try {
			YHHrStaffInfo staffInfo = this.getObjSingle(dbConn, seqId);
			if (staffInfo != null) {
				String photoName = YHUtility.null2Empty(staffInfo.getPhotoName());
				if (!YHUtility.isNullorEmpty(photoName)) {
					boolean delFlag = this.deleHeadPic(photoName);
					if (delFlag) {
						staffInfo.setPhotoName("");
						orm.updateSingle(dbConn, staffInfo);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	 /**
   * 取得当前部门的所有用户(不对禁止登录的用户进行控制)

   * @param conn
   * @param deptId
	 * @param noLoginIn 
   * @return
   * @throws Exception
   */
  public  ArrayList<YHPerson> getDeptUser2(Connection conn, int deptId) throws Exception{
//    String query = "select SEQ_ID, USER_NAME, DEPT_ID, USER_PRIV, TEL_NO_DEPT, EMAIL, ICQ, MY_STATUS, USER_ID from PERSON where DEPT_ID=0";
    String query = "SELECT PERSON.SEQ_ID as personId,HR_STAFF_INFO.USER_ID as staffUserId, USER_NAME,PERSON.DEPT_ID as personDeptId,USER_PRIV,TEL_NO_DEPT,EMAIL , ICQ, MY_STATUS from USER_PRIV ,PERSON  left join oa_pm_employee_info as HR_STAFF_INFO  on PERSON.USER_ID=HR_STAFF_INFO.USER_ID where PERSON.DEPT_ID=0 and HR_STAFF_INFO.DEPT_ID!=0 and HR_STAFF_INFO.DEPT_ID=" + deptId + " and PERSON.USER_PRIV=USER_PRIV.SEQ_ID";
    ArrayList<YHPerson> persons = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHPerson person = new YHPerson();
        person.setSeqId(rs4.getInt("personId"));
        person.setUserName(rs4.getString("USER_NAME"));
        person.setDeptId(rs4.getInt("personDeptId"));
        person.setUserPriv(rs4.getString("USER_PRIV"));
        person.setTelNoDept(rs4.getString("TEL_NO_DEPT"));
        person.setEmail(rs4.getString("EMAIL"));
        person.setIcq(rs4.getString("ICQ"));
        person.setMyStatus(rs4.getString("MY_STATUS"));
        person.setUserId(rs4.getString("staffUserId"));
        persons.add(person);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return persons;
  }
	
  public void syncDept(Connection dbConn) throws Exception {
    try {
      List<String> list = listUserId(dbConn);
      for (String s : list) {
        updateDeptId(dbConn, s);
      }
      
    } catch (Exception e) {
      throw e;
    } finally {
    }
  }
  
  private List<String> listUserId(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    List<String> list = new ArrayList<String>();
    try {
      String sql = "select USER_ID from oa_pm_employee_info";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      while (rs.next()) {
        list.add(rs.getString("USER_ID"));
      }
      return list;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(st , rs , null);
    }
    
  }
  
  public void updateDeptId(Connection dbConn, String userId) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update oa_pm_employee_info set DEPT_ID = (select DEPT_ID from PERSON where USER_ID = ?) where USER_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, userId);
      ps.setString(2, userId);
      ps.executeUpdate();
      
    } catch (Exception e) {
      YHDBUtility.close(ps , null , null);
    }
  }
  
  public void updateDeptId(Connection dbConn, int seqId, String deptId) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update oa_pm_employee_info set DEPT_ID = ? where USER_ID = (select USER_ID from PERSON where SEQ_ID = ?)";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, deptId);
      ps.setInt(2, seqId);
      ps.executeUpdate();
      
    } catch (Exception e) {
      YHDBUtility.close(ps , null , null);
    }
  }
  
  /**
   * 更新附件数据
   * 
   * @param dbConn
   * @param seqIdStr
   * @param attachId
   * @return
   * @throws Exception
   */
  public boolean updateFloadFile(Connection dbConn, String seqIdStr, String attachId) throws Exception {
    boolean returnFlag = false;
    YHORM orm = new YHORM();
    int seqId = 0;
    if (!YHUtility.isNullorEmpty(seqIdStr)) {
      String sql = "SELECT SEQ_ID from oa_pm_employee_info where USER_ID=?";
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        stmt = dbConn.prepareStatement(sql);
        stmt.setString(1, seqIdStr);
        rs = stmt.executeQuery();
        if (rs.next()) {
          seqId = rs.getInt("SEQ_ID");
        }
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(stmt, rs, log);
      }
    }
    try {
      YHHrStaffInfo staffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, seqId);
      String dbAttachId = "";
      String dbAttachName = "";
      if (staffInfo != null) {
        dbAttachId = YHUtility.null2Empty(staffInfo.getAttachmentId());
        dbAttachName = YHUtility.null2Empty(staffInfo.getAttachmentName());
        String[] dbAttachIdArrays = dbAttachId.split(",");
        String[] dbAttachNameArrays = dbAttachName.split("\\*");
        String attachmentId = "";
        String attachmentName = "";
        if (!YHUtility.isNullorEmpty(attachId) && dbAttachIdArrays.length > 0) {
          for (int i = 0; i < dbAttachIdArrays.length; i++) {
            if (attachId.equals(dbAttachIdArrays[i])) {
              continue;
            }
            attachmentId += dbAttachIdArrays[i] + ",";
            attachmentName += dbAttachNameArrays[i] + "*";
          }
          staffInfo.setAttachmentId(attachmentId.trim());
          staffInfo.setAttachmentName(attachmentName.trim());
          orm.updateSingle(dbConn, staffInfo);
          returnFlag = true;
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return returnFlag;
  }
  
}

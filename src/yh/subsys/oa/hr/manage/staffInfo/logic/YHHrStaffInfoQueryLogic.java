package yh.subsys.oa.hr.manage.staffInfo.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;
import yh.subsys.oa.hr.setting.data.YHHrCode;
import yh.subsys.oa.hr.setting.logic.YHHrCodeLogic;

public class YHHrStaffInfoQueryLogic {

	private static Logger log = Logger.getLogger(YHHrStaffInfoQueryLogic.class);

	public YHHrStaffInfo getObjSingle(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		try {
			return (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, seqId);
		} catch (Exception e) {
			throw e;
		}
	}

	public YHHrStaffInfo getStaffInfoByPersonId(Connection dbConn, int personSeqId) throws Exception {
		YHORM orm = new YHORM();
		YHHrStaffInfo staffInfo = new YHHrStaffInfo();
		try {
			YHPerson person = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, personSeqId);
			if (person != null) {
				String userId = YHUtility.null2Empty(person.getUserId());
				staffInfo = this.getStaffInfoByUserId(dbConn, userId);
			}
			return staffInfo;

		} catch (Exception e) {
			throw e;
		}

	}

	public YHHrStaffInfo getStaffInfoByUserId(Connection dbConn, String userId) throws Exception {
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			map.put("USER_ID", userId);
			return (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, map);
		} catch (Exception e) {
			throw e;
		}
	}

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
		String staffDept = (String) map.get("staffDept");
		String staffName = (String) map.get("staffName");
		String staffEName = (String) map.get("staffEName");
		String workStatus = (String) map.get("workStatus");
		String staffNo = (String) map.get("staffNo");
		String workNo = (String) map.get("workNo");
		String staffSex = (String) map.get("staffSex");
		String staffCardNo = (String) map.get("staffCardNo");
		String birthdayMin = (String) map.get("birthdayMin");
		String birthdayMax = (String) map.get("birthdayMax");

		String ageMin = (String) map.get("ageMin");
		String ageMax = (String) map.get("ageMax");
		String staffNationality = (String) map.get("staffNationality");
		String staffNativePlace = (String) map.get("staffNativePlace");
		String workType = (String) map.get("workType");
		String staffDomicilePlace = (String) map.get("staffDomicilePlace");
		String staffMaritalStatus = (String) map.get("staffMaritalStatus");
		String staffHealth = (String) map.get("staffHealth");
		String staffPoliticalStatus = (String) map.get("staffPoliticalStatus");
		String administrationLevel = (String) map.get("administrationLevel");

		String staffOccupation = (String) map.get("staffOccupation");
		String computerLevel = (String) map.get("computerLevel");
		String staffHighestSchool = (String) map.get("staffHighestSchool");
		String staffHighestDegree = (String) map.get("staffHighestDegree");
		String staffMajor = (String) map.get("staffMajor");
		String graduationSchool = (String) map.get("graduationSchool");
		String jobPosition = (String) map.get("jobPosition");
		String presentPosition = (String) map.get("presentPosition");
		String graduationMin = (String) map.get("graduationMin");
		String graduationMax = (String) map.get("graduationMax");

		String joinPartyMin = (String) map.get("joinPartyMin");
		String joinPartyMax = (String) map.get("joinPartyMax");
		String beginningMin = (String) map.get("beginningMin");
		String beginningMax = (String) map.get("beginningMax");
		String employedMin = (String) map.get("employedMin");
		String employedMax = (String) map.get("employedMax");
		String workAgeMin = (String) map.get("workAgeMin");
		String workAgeMax = (String) map.get("workAgeMax");
		String jobAgeMin = (String) map.get("jobAgeMin");
		String jobAgeMax = (String) map.get("jobAgeMax");

		String leaveTypeMin = (String) map.get("leaveTypeMin");
		String leaveTypeMax = (String) map.get("leaveTypeMax");
		String foreignLanguage1 = (String) map.get("foreignLanguage1");
		String foreignLanguage2 = (String) map.get("foreignLanguage2");
		String foreignLanguage3 = (String) map.get("foreignLanguage3");
		String foreignLevel1 = (String) map.get("foreignLevel1");
		String foreignLevel2 = (String) map.get("foreignLevel2");
		String foreignLevel3 = (String) map.get("foreignLevel3");

		String conditionStr = "";
		String sql = "";
		try {
			if (!YHUtility.isNullorEmpty(staffName)) {
				conditionStr = " and a.STAFF_NAME like'%" + YHDBUtility.escapeLike(staffName) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffEName)) {
				conditionStr = " and a.STAFF_E_NAME like'%" + YHDBUtility.escapeLike(staffEName) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(workStatus)) {
				conditionStr = " and a.WORK_STATUS ='" + YHDBUtility.escapeLike(workStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffNo)) {
				conditionStr = " and a.STAFF_NO like'%" + YHDBUtility.escapeLike(staffNo) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(workNo)) {
				conditionStr = " and a.WORK_NO like'%" + YHDBUtility.escapeLike(workNo) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(staffSex)) {
				conditionStr = " and a.STAFF_SEX ='" + YHDBUtility.escapeLike(staffSex) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffCardNo)) {
				conditionStr = " and a.STAFF_CARD_NO like'%" + YHDBUtility.escapeLike(staffCardNo) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(birthdayMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.STAFF_BIRTH", birthdayMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(birthdayMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.STAFF_BIRTH", birthdayMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(staffNationality)) {
				conditionStr = " and a.STAFF_NATIONALITY like'%" + YHDBUtility.escapeLike(staffNationality) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(staffNativePlace)) {
				conditionStr = " and a.STAFF_NATIVE_PLACE ='" + YHDBUtility.escapeLike(staffNativePlace) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffDomicilePlace)) {
				conditionStr = " and a.STAFF_DOMICILE_PLACE like'%" + YHDBUtility.escapeLike(staffDomicilePlace) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(workType)) {
				conditionStr = " and a.WORK_TYPE like'%" + YHDBUtility.escapeLike(workType) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffMaritalStatus)) {
				conditionStr = " and a.STAFF_MARITAL_STATUS ='" + YHDBUtility.escapeLike(staffMaritalStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffHealth)) {
				conditionStr = " and a.STAFF_HEALTH like'%" + YHDBUtility.escapeLike(staffHealth) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffPoliticalStatus)) {
				conditionStr = " and a.STAFF_POLITICAL_STATUS ='" + YHDBUtility.escapeLike(staffPoliticalStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(administrationLevel)) {
				conditionStr = " and a.ADMINISTRATION_LEVEL like'%" + YHDBUtility.escapeLike(administrationLevel) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(staffOccupation)) {
				conditionStr = " and a.STAFF_OCCUPATION ='" + YHDBUtility.escapeLike(staffOccupation) + "'";
			}

			if (!YHUtility.isNullorEmpty(computerLevel)) {
				conditionStr = " and a.COMPUTER_LEVEL like'%" + YHDBUtility.escapeLike(computerLevel) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffHighestSchool)) {
				conditionStr = " and a.STAFF_HIGHEST_SCHOOL ='" + YHDBUtility.escapeLike(staffHighestSchool) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffHighestDegree)) {
				conditionStr = " and a.STAFF_HIGHEST_DEGREE ='" + YHDBUtility.escapeLike(staffHighestDegree) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffMajor)) {
				conditionStr = " and a.STAFF_MAJOR like'%" + YHDBUtility.escapeLike(staffMajor) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(graduationSchool)) {
				conditionStr = " and a.GRADUATION_SCHOOL like'%" + YHDBUtility.escapeLike(graduationSchool) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(jobPosition)) {
				conditionStr = " and a.JOB_POSITION ='" + YHDBUtility.escapeLike(jobPosition) + "'";
			}
			if (!YHUtility.isNullorEmpty(presentPosition)) {
				conditionStr = " and a.PRESENT_POSITION ='" + YHDBUtility.escapeLike(presentPosition) + "'";
			}
			if (!YHUtility.isNullorEmpty(graduationMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.GRADUATION_DATE", graduationMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(graduationMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.GRADUATION_DATE", graduationMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(joinPartyMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOIN_PARTY_TIME", joinPartyMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(joinPartyMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOIN_PARTY_TIME", joinPartyMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(beginningMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOB_BEGINNING", beginningMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(beginningMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOB_BEGINNING", beginningMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(employedMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.DATES_EMPLOYED", employedMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(employedMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.DATES_EMPLOYED", employedMax + " 23:59:59", "<=");
			}

			if (!YHUtility.isNullorEmpty(workAgeMin)) {
				conditionStr = " and a.WORK_AGE >='" + YHDBUtility.escapeLike(workAgeMin) + "'";
			}
			if (!YHUtility.isNullorEmpty(workAgeMax)) {
				conditionStr = " and a.WORK_AGE <='" + YHDBUtility.escapeLike(workAgeMax) + "'";
			}
			if (!YHUtility.isNullorEmpty(jobAgeMin)) {
				conditionStr = " and a.JOB_AGE >='" + YHDBUtility.escapeLike(jobAgeMin) + "'";
			}
			if (!YHUtility.isNullorEmpty(jobAgeMax)) {
				conditionStr = " and a.JOB_AGE <='" + YHDBUtility.escapeLike(jobAgeMax) + "'";
			}
			if (!YHUtility.isNullorEmpty(leaveTypeMin)) {
				conditionStr = " and a.LEAVE_TYPE >='" + YHDBUtility.escapeLike(leaveTypeMin) + "'";
			}
			if (!YHUtility.isNullorEmpty(leaveTypeMax)) {
				conditionStr = " and a.LEAVE_TYPE <='" + YHDBUtility.escapeLike(leaveTypeMax) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage1)) {
				conditionStr = " and a.FOREIGN_LANGUAGE1 ='" + YHDBUtility.escapeLike(foreignLanguage1) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage2)) {
				conditionStr = " and a.FOREIGN_LANGUAGE2 ='" + YHDBUtility.escapeLike(foreignLanguage2) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage3)) {
				conditionStr = " and a.FOREIGN_LANGUAGE3 ='" + YHDBUtility.escapeLike(foreignLanguage3) + "'";
			}

			if (!YHUtility.isNullorEmpty(foreignLevel1)) {
				conditionStr = " and a.FOREIGN_LEVEL1 ='" + YHDBUtility.escapeLike(foreignLevel1) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLevel2)) {
				conditionStr = " and a.FOREIGN_LEVEL2 ='" + YHDBUtility.escapeLike(foreignLevel2) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLevel3)) {
				conditionStr = " and a.FOREIGN_LEVEL3 ='" + YHDBUtility.escapeLike(foreignLevel3) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffDept)) {
				if (!"ALL_DEPT".equals(staffDept) && !"0".equals(staffDept)) {
					if (staffDept.endsWith(",")) {
						staffDept = staffDept.substring(0, staffDept.length()-1);
					}
					conditionStr += " and a.DEPT_ID IN( " + staffDept + ")";
				}
			}			
			
			sql = "select " 
				+ " a.SEQ_ID" 
				+ ",d.DEPT_NAME" 
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
	
	/**
	 * 导出信息
	 * @param dbConn
	 * @param selectValue
	 * @return
	 * @throws Exception
	 */
	public ArrayList<YHDbRecord> exportToCSVLogic(Connection dbConn,Map<Object, Object> map ) throws Exception {
		ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
		String staffDept = (String) map.get("staffDept");
		String staffName = (String) map.get("staffName");
		String staffEName = (String) map.get("staffEName");
		String workStatus = (String) map.get("workStatus");
		String staffNo = (String) map.get("staffNo");
		String workNo = (String) map.get("workNo");
		String staffSex = (String) map.get("staffSex");
		String staffCardNo = (String) map.get("staffCardNo");
		String birthdayMin = (String) map.get("birthdayMin");
		String birthdayMax = (String) map.get("birthdayMax");

		String ageMin = (String) map.get("ageMin");
		String ageMax = (String) map.get("ageMax");
		String staffNationality = (String) map.get("staffNationality");
		String staffNativePlace = (String) map.get("staffNativePlace");
		String workType = (String) map.get("workType");
		String staffDomicilePlace = (String) map.get("staffDomicilePlace");
		String staffMaritalStatus = (String) map.get("staffMaritalStatus");
		String staffHealth = (String) map.get("staffHealth");
		String staffPoliticalStatus = (String) map.get("staffPoliticalStatus");
		String administrationLevel = (String) map.get("administrationLevel");

		String staffOccupation = (String) map.get("staffOccupation");
		String computerLevel = (String) map.get("computerLevel");
		String staffHighestSchool = (String) map.get("staffHighestSchool");
		String staffHighestDegree = (String) map.get("staffHighestDegree");
		String staffMajor = (String) map.get("staffMajor");
		String graduationSchool = (String) map.get("graduationSchool");
		String jobPosition = (String) map.get("jobPosition");
		String presentPosition = (String) map.get("presentPosition");
		String graduationMin = (String) map.get("graduationMin");
		String graduationMax = (String) map.get("graduationMax");

		String joinPartyMin = (String) map.get("joinPartyMin");
		String joinPartyMax = (String) map.get("joinPartyMax");
		String beginningMin = (String) map.get("beginningMin");
		String beginningMax = (String) map.get("beginningMax");
		String employedMin = (String) map.get("employedMin");
		String employedMax = (String) map.get("employedMax");
		String workAgeMin = (String) map.get("workAgeMin");
		String workAgeMax = (String) map.get("workAgeMax");
		String jobAgeMin = (String) map.get("jobAgeMin");
		String jobAgeMax = (String) map.get("jobAgeMax");

		String leaveTypeMin = (String) map.get("leaveTypeMin");
		String leaveTypeMax = (String) map.get("leaveTypeMax");
		String foreignLanguage1 = (String) map.get("foreignLanguage1");
		String foreignLanguage2 = (String) map.get("foreignLanguage2");
		String foreignLanguage3 = (String) map.get("foreignLanguage3");
		String foreignLevel1 = (String) map.get("foreignLevel1");
		String foreignLevel2 = (String) map.get("foreignLevel2");
		String foreignLevel3 = (String) map.get("foreignLevel3");
		String selectValues = YHUtility.null2Empty((String) map.get("selectValue"));
		
		if (selectValues.endsWith(",")) {
			selectValues = selectValues.substring(0, selectValues.length()-1);
		}
		
		
		
		String [] valueArry = selectValues.trim().split(",");
		String conditionStr = "";
		String sql = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (!YHUtility.isNullorEmpty(staffName)) {
				conditionStr = " and a.STAFF_NAME like'%" + YHDBUtility.escapeLike(staffName) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffEName)) {
				conditionStr = " and a.STAFF_E_NAME like'%" + YHDBUtility.escapeLike(staffEName) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(workStatus)) {
				conditionStr = " and a.WORK_STATUS ='" + YHDBUtility.escapeLike(workStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffNo)) {
				conditionStr = " and a.STAFF_NO like'%" + YHDBUtility.escapeLike(staffNo) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(workNo)) {
				conditionStr = " and a.WORK_NO like'%" + YHDBUtility.escapeLike(workNo) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(staffSex)) {
				conditionStr = " and a.STAFF_SEX ='" + YHDBUtility.escapeLike(staffSex) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffCardNo)) {
				conditionStr = " and a.STAFF_CARD_NO like'%" + YHDBUtility.escapeLike(staffCardNo) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(birthdayMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.STAFF_BIRTH", birthdayMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(birthdayMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.STAFF_BIRTH", birthdayMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(staffNationality)) {
				conditionStr = " and a.STAFF_NATIONALITY like'%" + YHDBUtility.escapeLike(staffNationality) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(staffNativePlace)) {
				conditionStr = " and a.STAFF_NATIVE_PLACE ='" + YHDBUtility.escapeLike(staffNativePlace) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffDomicilePlace)) {
				conditionStr = " and a.STAFF_DOMICILE_PLACE like'%" + YHDBUtility.escapeLike(staffDomicilePlace) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(workType)) {
				conditionStr = " and a.WORK_TYPE like'%" + YHDBUtility.escapeLike(workType) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffMaritalStatus)) {
				conditionStr = " and a.STAFF_MARITAL_STATUS ='" + YHDBUtility.escapeLike(staffMaritalStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffHealth)) {
				conditionStr = " and a.STAFF_HEALTH like'%" + YHDBUtility.escapeLike(staffHealth) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffPoliticalStatus)) {
				conditionStr = " and a.STAFF_POLITICAL_STATUS ='" + YHDBUtility.escapeLike(staffPoliticalStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(administrationLevel)) {
				conditionStr = " and a.ADMINISTRATION_LEVEL like'%" + YHDBUtility.escapeLike(administrationLevel) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(staffOccupation)) {
				conditionStr = " and a.STAFF_OCCUPATION ='" + YHDBUtility.escapeLike(staffOccupation) + "'";
			}

			if (!YHUtility.isNullorEmpty(computerLevel)) {
				conditionStr = " and a.COMPUTER_LEVEL like'%" + YHDBUtility.escapeLike(computerLevel) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(staffHighestSchool)) {
				conditionStr = " and a.STAFF_HIGHEST_SCHOOL ='" + YHDBUtility.escapeLike(staffHighestSchool) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffHighestDegree)) {
				conditionStr = " and a.STAFF_HIGHEST_DEGREE ='" + YHDBUtility.escapeLike(staffHighestDegree) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffMajor)) {
				conditionStr = " and a.STAFF_MAJOR like'%" + YHDBUtility.escapeLike(staffMajor) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(graduationSchool)) {
				conditionStr = " and a.GRADUATION_SCHOOL like'%" + YHDBUtility.escapeLike(graduationSchool) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(jobPosition)) {
				conditionStr = " and a.JOB_POSITION ='" + YHDBUtility.escapeLike(jobPosition) + "'";
			}
			if (!YHUtility.isNullorEmpty(presentPosition)) {
				conditionStr = " and a.PRESENT_POSITION ='" + YHDBUtility.escapeLike(presentPosition) + "'";
			}
			if (!YHUtility.isNullorEmpty(graduationMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.GRADUATION_DATE", graduationMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(graduationMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.GRADUATION_DATE", graduationMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(joinPartyMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOIN_PARTY_TIME", joinPartyMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(joinPartyMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOIN_PARTY_TIME", joinPartyMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(beginningMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOB_BEGINNING", beginningMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(beginningMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.JOB_BEGINNING", beginningMax + " 23:59:59", "<=");
			}
			if (!YHUtility.isNullorEmpty(employedMin)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.DATES_EMPLOYED", employedMin, ">=");
			}
			if (!YHUtility.isNullorEmpty(employedMax)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("a.DATES_EMPLOYED", employedMax + " 23:59:59", "<=");
			}

			if (!YHUtility.isNullorEmpty(workAgeMin)) {
				conditionStr = " and a.WORK_AGE >='" + YHDBUtility.escapeLike(workAgeMin) + "'";
			}
			if (!YHUtility.isNullorEmpty(workAgeMax)) {
				conditionStr = " and a.WORK_AGE <='" + YHDBUtility.escapeLike(workAgeMax) + "'";
			}
			if (!YHUtility.isNullorEmpty(jobAgeMin)) {
				conditionStr = " and a.JOB_AGE >='" + YHDBUtility.escapeLike(jobAgeMin) + "'";
			}
			if (!YHUtility.isNullorEmpty(jobAgeMax)) {
				conditionStr = " and a.JOB_AGE <='" + YHDBUtility.escapeLike(jobAgeMax) + "'";
			}
			if (!YHUtility.isNullorEmpty(leaveTypeMin)) {
				conditionStr = " and a.LEAVE_TYPE >='" + YHDBUtility.escapeLike(leaveTypeMin) + "'";
			}
			if (!YHUtility.isNullorEmpty(leaveTypeMax)) {
				conditionStr = " and a.LEAVE_TYPE <='" + YHDBUtility.escapeLike(leaveTypeMax) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage1)) {
				conditionStr = " and a.FOREIGN_LANGUAGE1 ='" + YHDBUtility.escapeLike(foreignLanguage1) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage2)) {
				conditionStr = " and a.FOREIGN_LANGUAGE2 ='" + YHDBUtility.escapeLike(foreignLanguage2) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage3)) {
				conditionStr = " and a.FOREIGN_LANGUAGE3 ='" + YHDBUtility.escapeLike(foreignLanguage3) + "'";
			}

			if (!YHUtility.isNullorEmpty(foreignLevel1)) {
				conditionStr = " and a.FOREIGN_LEVEL1 ='" + YHDBUtility.escapeLike(foreignLevel1) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLevel2)) {
				conditionStr = " and a.FOREIGN_LEVEL2 ='" + YHDBUtility.escapeLike(foreignLevel2) + "'";
			}
			if (!YHUtility.isNullorEmpty(foreignLevel3)) {
				conditionStr = " and a.FOREIGN_LEVEL3 ='" + YHDBUtility.escapeLike(foreignLevel3) + "'";
			}
			if (!YHUtility.isNullorEmpty(staffDept)) {
				if (!"ALL_DEPT".equals(staffDept) && !"0".equals(staffDept)) {
					if (staffDept.endsWith(",")) {
						staffDept = staffDept.substring(0, staffDept.length()-1);
					}
					conditionStr += " and a.DEPT_ID IN( " + staffDept + ")";
				}
			}
			
			
			sql = "select " 
				+ " a.USER_ID as USER_ID" 
				+ ", a.STAFF_NAME as STAFF_NAME" 
				+ ", a.STAFF_E_NAME as STAFF_E_NAME" 
				+ ", a.DEPT_ID as DEPT_ID" 
				+ ", a.STAFF_SEX as STAFF_SEX" 
				+ ", a.STAFF_NO as STAFF_NO" 
				+ ", a.WORK_NO as WORK_NO" 
				+ ", a.STAFF_CARD_NO as STAFF_CARD_NO"
				+ ", a.STAFF_BIRTH as STAFF_BIRTH"
				+ ", a.STAFF_AGE as STAFF_AGE"
				+ ", a.STAFF_NATIVE_PLACE as STAFF_NATIVE_PLACE"
				+ ", a.STAFF_NATIONALITY as STAFF_NATIONALITY"
				+ ", a.STAFF_MARITAL_STATUS as STAFF_MARITAL_STATUS"
				+ ", a.STAFF_POLITICAL_STATUS as STAFF_POLITICAL_STATUS"
				+ ", a.WORK_STATUS as WORK_STATUS"
				+ ", a.JOIN_PARTY_TIME as JOIN_PARTY_TIME"
				+ ", a.STAFF_PHONE as STAFF_PHONE"
				+ ", a.STAFF_MOBILE as STAFF_MOBILE"
				+ ", a.STAFF_LITTLE_SMART as STAFF_LITTLE_SMART"
				+ ", a.STAFF_MSN as STAFF_MSN"
				+ ", a.STAFF_QQ as STAFF_QQ"
				+ ", a.STAFF_EMAIL as STAFF_EMAIL"
				+ ", a.HOME_ADDRESS as HOME_ADDRESS"
				+ ", a.JOB_BEGINNING as JOB_BEGINNING"
				+ ", a.OTHER_CONTACT as OTHER_CONTACT"
				+ ", a.WORK_AGE as WORK_AGE"
				+ ", a.STAFF_HEALTH as STAFF_HEALTH"
				+ ", a.STAFF_DOMICILE_PLACE as STAFF_DOMICILE_PLACE"
				+ ", a.STAFF_TYPE as STAFF_TYPE"
				+ ", a.DATES_EMPLOYED as DATES_EMPLOYED"
				+ ", a.STAFF_HIGHEST_SCHOOL as STAFF_HIGHEST_SCHOOL"
				+ ", a.STAFF_HIGHEST_DEGREE as STAFF_HIGHEST_DEGREE"
				+ ", a.STAFF_HIGHEST_DEGREE as STAFF_HIGHEST_DEGREE"
				+ ", a.GRADUATION_DATE as GRADUATION_DATE"
				+ ", a.STAFF_MAJOR as STAFF_MAJOR"
				+ ", a.GRADUATION_SCHOOL as GRADUATION_SCHOOL"
				+ ", a.COMPUTER_LEVEL as COMPUTER_LEVEL"
				+ ", a.FOREIGN_LANGUAGE1 as FOREIGN_LANGUAGE1"
				+ ", a.FOREIGN_LANGUAGE2 as FOREIGN_LANGUAGE2"
				+ ", a.FOREIGN_LANGUAGE3 as FOREIGN_LANGUAGE3"
				+ ", a.FOREIGN_LEVEL1 as FOREIGN_LEVEL1"
				+ ", a.FOREIGN_LEVEL2 as FOREIGN_LEVEL2"
				+ ", a.FOREIGN_LEVEL3 as FOREIGN_LEVEL3"
				+ ", a.STAFF_SKILLS as STAFF_SKILLS"
				+ ", a.WORK_TYPE as WORK_TYPE"
				+ ", a.ADMINISTRATION_LEVEL as ADMINISTRATION_LEVEL"
				+ ", a.STAFF_OCCUPATION as STAFF_OCCUPATION"
				+ ", a.JOB_POSITION as JOB_POSITION"
				+ ", a.PRESENT_POSITION as PRESENT_POSITION"
				+ ", a.JOB_AGE as JOB_AGE"
				+ ", a.BEGIN_SALSRY_TIME as BEGIN_SALSRY_TIME"
				+ ", a.LEAVE_TYPE as LEAVE_TYPE"
				+ ", a.RESUME as RESUME"
				+ ", a.SURETY as SURETY"
				+ ", a.CERTIFICATE as CERTIFICATE"
				+ ", a.BODY_EXAMIM as BODY_EXAMIM"
				+ ", a.INSURE as INSURE"
				+ ", a.REMARK as REMARK"
				+ " from oa_pm_employee_info a " 
				+ " LEFT OUTER JOIN PERSON b ON a.USER_ID = b.USER_ID" 
				+ " LEFT OUTER JOIN USER_PRIV  c ON b.USER_PRIV=c.SEQ_ID"
				+ " where 1=1" + conditionStr ;
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			YHHrCodeLogic codeLogic = new YHHrCodeLogic();
			while(rs.next()){
				YHDbRecord record = new YHDbRecord();
				String dbUserId = YHUtility.null2Empty(rs.getString("USER_ID"));
				String dbStaffName = YHUtility.null2Empty(rs.getString("STAFF_NAME"));
				String dbStaffEName = YHUtility.null2Empty(rs.getString("STAFF_E_NAME"));
				
				String dbDeptId = YHUtility.null2Empty(rs.getString("DEPT_ID"));
				String dbStaffSex = YHUtility.null2Empty(rs.getString("STAFF_SEX"));
				String dbStaffNo = YHUtility.null2Empty(rs.getString("STAFF_NO"));
				String dbWorkNo = YHUtility.null2Empty(rs.getString("WORK_NO"));
				String dbStaffCardNo = YHUtility.null2Empty(rs.getString("STAFF_CARD_NO"));
				String dbStaffBirth = YHUtility.null2Empty(rs.getString("STAFF_BIRTH"));
				String dbStaffAge = YHUtility.null2Empty(rs.getString("STAFF_AGE"));
				String dbStaffNativePlace = YHUtility.null2Empty(rs.getString("STAFF_NATIVE_PLACE"));
				String dbStaffNationality = YHUtility.null2Empty(rs.getString("STAFF_NATIONALITY"));
				String dbStaffMaritalStatus = YHUtility.null2Empty(rs.getString("STAFF_MARITAL_STATUS"));
				String dbtaffPoliticalStatus = YHUtility.null2Empty(rs.getString("STAFF_POLITICAL_STATUS"));
				String dbWorkStatus = YHUtility.null2Empty(rs.getString("WORK_STATUS"));
				String dbStaffPoliticalStatus = YHUtility.null2Empty(rs.getString("STAFF_POLITICAL_STATUS"));
				String dbJoinPartyTime = YHUtility.null2Empty(rs.getString("JOIN_PARTY_TIME"));
				String dbStaffPhone = YHUtility.null2Empty(rs.getString("STAFF_PHONE"));
				String dbStaffMobile = YHUtility.null2Empty(rs.getString("STAFF_MOBILE"));
				String dbStaffLittleSmart = YHUtility.null2Empty(rs.getString("STAFF_LITTLE_SMART"));
				String dbStaffMsn = YHUtility.null2Empty(rs.getString("STAFF_MSN"));
				String dbStaffQq = YHUtility.null2Empty(rs.getString("STAFF_QQ"));
				String dbstaffEmail = YHUtility.null2Empty(rs.getString("STAFF_EMAIL"));
				
				String dbHomeAddress = YHUtility.null2Empty(rs.getString("HOME_ADDRESS"));
				String dbJobBeginning = YHUtility.null2Empty(rs.getString("JOB_BEGINNING"));
				String dbOtherContact = YHUtility.null2Empty(rs.getString("OTHER_CONTACT"));
				String dbWorkAge = YHUtility.null2Empty(rs.getString("WORK_AGE"));
				String dbStaffHealth = YHUtility.null2Empty(rs.getString("STAFF_HEALTH"));
				String dbStaffDomicilePlace = YHUtility.null2Empty(rs.getString("STAFF_DOMICILE_PLACE"));
				String dbStaffType = YHUtility.null2Empty(rs.getString("STAFF_TYPE"));
				String dbDatesEmployed = YHUtility.null2Empty(rs.getString("DATES_EMPLOYED"));
				String dbStaffHighestSchool = YHUtility.null2Empty(rs.getString("STAFF_HIGHEST_SCHOOL"));
				String dbStaffHighestDegree = YHUtility.null2Empty(rs.getString("STAFF_HIGHEST_DEGREE"));
				String dbGraduationDate = YHUtility.null2Empty(rs.getString("GRADUATION_DATE"));
				String dbStaffMajor = YHUtility.null2Empty(rs.getString("STAFF_MAJOR"));
				String dbGraduationSchool = YHUtility.null2Empty(rs.getString("GRADUATION_SCHOOL"));
				String dbComputerLevel = YHUtility.null2Empty(rs.getString("COMPUTER_LEVEL"));
				String dbForeignLanguage1 = YHUtility.null2Empty(rs.getString("FOREIGN_LANGUAGE1"));
				String dbForeignLanguage2 = YHUtility.null2Empty(rs.getString("FOREIGN_LANGUAGE2"));
				String dbForeignLanguage3 = YHUtility.null2Empty(rs.getString("FOREIGN_LANGUAGE3"));
				String dbForeignLevel1 = YHUtility.null2Empty(rs.getString("FOREIGN_LEVEL1"));
				String dbForeignLevel2 = YHUtility.null2Empty(rs.getString("FOREIGN_LEVEL2"));
				String dbForeignLevel3 = YHUtility.null2Empty(rs.getString("FOREIGN_LEVEL3"));
				String dbStaffSkills = YHUtility.null2Empty(rs.getString("STAFF_SKILLS"));
				String dbWorkType = YHUtility.null2Empty(rs.getString("WORK_TYPE"));
				String dbAdministrationLevel = YHUtility.null2Empty(rs.getString("ADMINISTRATION_LEVEL"));
				String dbStaffOccupation = YHUtility.null2Empty(rs.getString("STAFF_OCCUPATION"));
				String dbJobPosition = YHUtility.null2Empty(rs.getString("JOB_POSITION"));
				String dbPresentPosition = YHUtility.null2Empty(rs.getString("PRESENT_POSITION"));
				String dbJobAge = YHUtility.null2Empty(rs.getString("JOB_AGE"));
				String dbBeginSalsryTime = YHUtility.null2Empty(rs.getString("BEGIN_SALSRY_TIME"));
				String dbLeaveType = YHUtility.null2Empty(rs.getString("LEAVE_TYPE"));
				String dbResume = YHUtility.null2Empty(rs.getString("RESUME"));
				String dbSurety = YHUtility.null2Empty(rs.getString("SURETY"));
				String dbCertificate = YHUtility.null2Empty(rs.getString("CERTIFICATE"));
				String dbBodyExamim = YHUtility.null2Empty(rs.getString("BODY_EXAMIM"));
				String dbInsure = YHUtility.null2Empty(rs.getString("INSURE"));
				String dbRemark = YHUtility.null2Empty(rs.getString("REMARK"));
				
				
				record.addField("用户名", dbUserId);
				if (valueArry!=null && valueArry.length>0) {
					
					for(String selectValue:valueArry){
						if ("staffName".equals(selectValue)) {
							record.addField("姓名", dbStaffName);
						}
						if ("staffEName".equals(selectValue)) {
							record.addField("英文名", dbStaffEName);
						}
						if ("deptId".equals(selectValue)) {
							String deptNameStr = this.getDeptNameLogic(dbConn, dbDeptId);
							record.addField("部门", deptNameStr);
						}
						if ("staffSex".equals(selectValue)) {
							String str = "";
							if ("0".equals(dbStaffSex)) {
								str = "男";
							}else if ("1".equals(dbStaffSex)) {
								str = "女";
							}
							record.addField("性别", str);
						}
						
						if ("staffNo".equals(selectValue)) {
							record.addField("编号", dbStaffNo);
						}
						if ("workNo".equals(selectValue)) {
							record.addField("工号", dbWorkNo);
						}
						if ("staffCardNo".equals(selectValue)) {
							record.addField("身份证号码", "\t" +dbStaffCardNo);
						}
						if ("staffBirth".equals(selectValue)) {
							String str = "";
							if (YHUtility.isNullorEmpty(dbStaffBirth)) {
								str = "";
							}else {
								str = dbStaffBirth.substring(0, 10);
							}
							record.addField("出生日期", str);
						}
						if ("staffAge".equals(selectValue)) {
							record.addField("年龄",dbStaffAge );
						}
						if ("staffNativePlace".equals(selectValue)) {
              if(YHUtility.isNumber(dbStaffNativePlace)){
                record.addField("籍贯", getHrCode(dbConn, Integer.parseInt(dbStaffNativePlace)));
              }
              else{
                record.addField("籍贯", dbStaffNativePlace);
              }
						}
						if ("staffNationality".equals(selectValue)) {
							record.addField("民族", dbStaffNationality);
						}
						if ("staffMaritalStatus".equals(selectValue)) {
							String str = "";
							if ("0".equals(dbStaffMaritalStatus)) {
								str = "未婚";
							}else if ("1".equals(dbStaffMaritalStatus)) {
								str = "已婚";
							}else if ("2".equals(dbStaffMaritalStatus)) {
								str = "离异";
							}else if ("3".equals(dbStaffMaritalStatus)) {
								str = "丧偶";
							}
							record.addField("婚姻状况", str);
						}
						
						if ("staffPoliticalStatus".equals(selectValue)) {
							String str = "";
							if (!YHUtility.isNullorEmpty(dbStaffPoliticalStatus)) {
								YHHrCode hrCode = codeLogic.getCodeById(dbConn, dbStaffPoliticalStatus);
								if (hrCode!=null) {
									str = YHUtility.null2Empty(hrCode.getCodeName());
								}
							}
							record.addField("政治面貌", str);
						}
						
						
						if ("workStatus".equals(selectValue)) {
							String str = "";
							if (!YHUtility.isNullorEmpty(dbWorkStatus)) {
								YHHrCode hrCode = codeLogic.getCodeById(dbConn, dbWorkStatus);
								if (hrCode!=null) {
									str = YHUtility.null2Empty(hrCode.getCodeName());
								}
							}
							record.addField("在职状态",str );
						}
						if ("joinPartyTime".equals(selectValue)) {
              String str = "";
              if (YHUtility.isNullorEmpty(dbJoinPartyTime)) {
                str = "";
              }else {
                str = dbJoinPartyTime.substring(0, 10);
              }
							record.addField("入党时间", str );
						}
						if ("staffPhone".equals(selectValue)) {
							record.addField("联系电话", dbStaffPhone);
						}
						if ("staffMobile".equals(selectValue)) {
							record.addField("手机号码", dbStaffMobile);
						}
						if ("staffLittleSmart".equals(selectValue)) {
							record.addField("小灵通", dbStaffLittleSmart);
						}
						if ("staffQq".equals(selectValue)) {
							record.addField("QQ", dbStaffQq);
						}
						if ("staffMsn".equals(selectValue)) {
							record.addField("MSN", dbStaffMsn);
						}
						if ("staffEmail".equals(selectValue)) {
							record.addField("电子邮件", dbstaffEmail);
						}
						if ("homeAddress".equals(selectValue)) {
							record.addField("家庭地址",dbHomeAddress );
						}
						if ("jobBeginning".equals(selectValue)) {
              String str = "";
              if (YHUtility.isNullorEmpty(dbJobBeginning)) {
                str = "";
              }else {
                str = dbJobBeginning.substring(0, 10);
              }
							record.addField("参加工作时间", str);
						}
						if ("otherContact".equals(selectValue)) {
							record.addField("其他联系方式", dbOtherContact);
						}
						if ("workAge".equals(selectValue)) {
							record.addField("总工龄", dbWorkAge);
						}
						if ("staffHealth".equals(selectValue)) {
							record.addField("健康状况",dbStaffHealth );
						}
						if ("staffDomicilePlace".equals(selectValue)) {
							record.addField("户口所在地", dbStaffDomicilePlace);
						}
						if ("staffType".equals(selectValue)) {
              if(YHUtility.isNumber(dbStaffType)){
                record.addField("户口类别", getHrCode(dbConn, Integer.parseInt(dbStaffType)));
              }
              else{
                record.addField("户口类别", dbStaffType);
              }
						}
						if ("datesEmployed".equals(selectValue)) {
              String str = "";
              if (YHUtility.isNullorEmpty(dbDatesEmployed)) {
                str = "";
              }else {
                str = dbDatesEmployed.substring(0, 10);
              }
							record.addField("入职时间",str );
						}
						if ("staffHighestSchool".equals(selectValue)) {
							String str = "";
							if (!YHUtility.isNullorEmpty(dbStaffHighestSchool)) {
								YHHrCode hrCode = codeLogic.getCodeById(dbConn, dbStaffHighestSchool);
								if (hrCode!=null) {
									str = YHUtility.null2Empty(hrCode.getCodeName());
								}
							}
							record.addField("学历",str );
						}
						if ("staffHighestDegree".equals(selectValue)) {
							String str = "";
							if (!YHUtility.isNullorEmpty(dbStaffHighestDegree)) {
								YHHrCode hrCode = codeLogic.getCodeById(dbConn, dbStaffHighestDegree);
								if (hrCode!=null) {
									str = YHUtility.null2Empty(hrCode.getCodeName());
								}
							}
							record.addField("学位", str);
						}
						if ("graduationDate".equals(selectValue)) {
              String str = "";
              if (YHUtility.isNullorEmpty(dbGraduationDate)) {
                str = "";
              }else {
                str = dbGraduationDate.substring(0, 10);
              }
							record.addField("毕业时间",str );
						}
						if ("staffMajor".equals(selectValue)) {
							record.addField("专业", dbStaffMajor);
						}
						if ("graduationSchool".equals(selectValue)) {
							String str = "";
							if (!YHUtility.isNullorEmpty(dbGraduationSchool)) {
							  str = dbGraduationSchool;
							}
							record.addField("毕业院校",str);
						}
						
						if ("computerLevel".equals(selectValue)) {
							record.addField("计算机水平", dbComputerLevel);
						}
						if ("foreignLanguage1".equals(selectValue)) {
							record.addField("外语语种1",dbForeignLanguage1 );
						}
						if ("foreignLanguage2".equals(selectValue)) {
							record.addField("外语语种2",dbForeignLanguage2 );
						}
						if ("foreignLanguage3".equals(selectValue)) {
							record.addField("外语语种3",dbForeignLanguage3 );
						}
						if ("foreignLevel1".equals(selectValue)) {
							record.addField("外语水平1", dbForeignLevel1);
						}
						if ("foreignLevel2".equals(selectValue)) {
							record.addField("外语水平2", dbForeignLevel2);
						}
						if ("foreignLevel3".equals(selectValue)) {
							record.addField("外语水平3", dbForeignLevel3);
						}
						if ("staffSkills".equals(selectValue)) {
							record.addField("特长", dbStaffSkills);
						}
						if ("workType".equals(selectValue)) {
							record.addField("工种", dbWorkType);
						}
						if ("administrationLevel".equals(selectValue)) {
							record.addField("行政级别",dbAdministrationLevel );
						}
						if ("staffOccupation".equals(selectValue)) {
              if(YHUtility.isNumber(dbStaffOccupation)){
                record.addField("员工类型", getHrCode(dbConn, Integer.parseInt(dbStaffOccupation)));
              }
              else{
                record.addField("员工类型", dbStaffOccupation);
              }
						}
						if ("jobPosition".equals(selectValue)) {
							record.addField("职务", dbJobPosition);
						}
						if ("presentPosition".equals(selectValue)) {
						  if(YHUtility.isNumber(dbPresentPosition)){
						    record.addField("职称", getHrCode(dbConn, Integer.parseInt(dbPresentPosition)));
						  }
						  else{
						    record.addField("职称", dbPresentPosition);
						  }
						}
						if ("jobAge".equals(selectValue)) {
							record.addField("本单位工龄", dbJobAge);
						}
						if ("beginSalsryTime".equals(selectValue)) {
              String str = "";
              if (YHUtility.isNullorEmpty(dbBeginSalsryTime)) {
                str = "";
              }else {
                str = dbBeginSalsryTime.substring(0, 10);
              }
							record.addField("起薪时间", str);
						}
						if ("leaveType".equals(selectValue)) {
							record.addField("年休假", dbLeaveType);
						}
						if ("resume".equals(selectValue)) {
							record.addField("简历",dbResume );
						}
						if ("surety".equals(selectValue)) {
							record.addField("担保记录", dbSurety);
						}
						if ("certificate".equals(selectValue)) {
							record.addField("职务情况",dbCertificate );
						}
						if ("insure".equals(selectValue)) {
							record.addField("社保缴纳情况",dbInsure );
						}
						if ("bodyExamim".equals(selectValue)) {
							record.addField("体检记录", dbBodyExamim);
						}
						if ("remark".equals(selectValue)) {
							record.addField("备 注", dbRemark);
						}
					}
				}else {
					record.addField("姓名", dbStaffName);
					record.addField("英文名", dbStaffEName);
					String deptNameStr = this.getDeptNameLogic(dbConn, dbDeptId);
					record.addField("部门", deptNameStr);
					String staffSexStr = "";
					if ("0".equals(dbStaffSex)) {
						staffSexStr = "男";
					}else if ("1".equals(dbStaffSex)) {
						staffSexStr = "女";
					}
					record.addField("性别", staffSexStr);
					record.addField("编号", dbStaffNo);
					record.addField("工号", dbWorkNo);
					record.addField("身份证号码", "\t" +dbStaffCardNo);
					String staffBirthStr = "";
					if (YHUtility.isNullorEmpty(dbStaffBirth)) {
						staffBirthStr = "";
					}else {
						staffBirthStr = dbStaffBirth;
					}
					record.addField("出生日期", staffBirthStr);
					record.addField("年龄",dbStaffAge );
					record.addField("籍贯",dbStaffNativePlace );
					record.addField("民族", dbStaffNationality);
					
					
					
					
					
					
				}
				result.add(record);
			}
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return result;

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
	
	/**
	 * 离职人员信息列表
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getRetireeListLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
		try {
			String sql = "select " 
				+ " h.SEQ_ID" 
				+ ", h.DEPT_ID" 
				+ ", p.USER_NAME" 
				+ ", h.STAFF_SEX" 
				+ ", h.STAFF_BIRTH"
				+ ", p.SEQ_ID as userId" 
				+ " from oa_pm_employee_info h,PERSON p"
				+ " where p.USER_ID=h.USER_ID AND p.DEPT_ID=0";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getHrCode(Connection dbConn, int seqId){
	   String sql = "select CODE_NAME from oa_pm_code where SEQ_ID =" + seqId;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String data = "";
	    try {
	      ps = dbConn.prepareStatement(sql);
	      rs = ps.executeQuery();
	      if (rs.next()) {
	        data = rs.getString("CODE_NAME");
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      YHDBUtility.close(ps, rs, log);
	    }
	    return data;
	}
}

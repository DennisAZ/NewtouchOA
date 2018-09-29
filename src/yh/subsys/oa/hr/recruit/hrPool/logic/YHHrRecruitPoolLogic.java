package yh.subsys.oa.hr.recruit.hrPool.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;
import yh.subsys.oa.hr.recruit.hrPool.act.YHHrRecruitPoolAct;
import yh.subsys.oa.hr.recruit.hrPool.data.YHHrRecruitPool;
import yh.subsys.oa.hr.recruit.plan.data.YHHrRecruitPlan;

public class YHHrRecruitPoolLogic {
	private static Logger log = Logger.getLogger(YHHrRecruitPoolLogic.class);
	
	public String getHrPoolListJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception{
		
		try {
			String sql =  "select " +
   		"SEQ_ID" +
   		",PLAN_NAME" +
   		",EMPLOYEE_NAME" +
   		",EMPLOYEE_BIRTH" +
   		",EMPLOYEE_PHONE" +
   		",EMPLOYEE_HIGHEST_SCHOOL" +
   		",EMPLOYEE_MAJOR" +
   		",POSITION" +
   		",ADD_TIME" +
       " from " +
   		"oa_pm_enroll_set where " + this.getHrPriv(dbConn, person, "") + " order by ADD_TIME desc"; 
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<YHHrRecruitPlan> getHrRecruitPlanListLogic(Connection dbConn,YHPerson person,String keyWord) throws Exception{
		List<YHHrRecruitPlan> plans = new ArrayList<YHHrRecruitPlan>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SEQ_ID,PLAN_NO,PLAN_NAME from oa_pm_enroll_plan where 1=1 ";
		if (!YHUtility.isNullorEmpty(keyWord)) {
			sql += " and PLAN_NAME like '%" + YHDBUtility.escapeLike(keyWord) + "%'";
		}
		sql += " order by PLAN_NO";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int counter = 0;
			while(rs.next() && ++counter<50){
				YHHrRecruitPlan plan = new YHHrRecruitPlan();
				plan.setSeqId(rs.getInt("SEQ_ID"));
				plan.setPlanNo(rs.getString("PLAN_NO"));
				plan.setPlanName(rs.getString("PLAN_NAME"));
				plans.add(plan);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return plans;
	}
	
	/**
	 * 新建才档案
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setNewHrPoolInfoValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		
		String createUserId = String.valueOf(person.getSeqId());
		int createDeptId = person.getDeptId();
		String planName = YHUtility.null2Empty(fileForm.getParameter("PLAN_NAME"));
		String planNo = fileForm.getParameter("PLAN_NO");//
		String employeeName = fileForm.getParameter("EMPLOYEE_NAME");
		String employeeSex = fileForm.getParameter("EMPLOYEE_SEX");
		String employeeBirth = fileForm.getParameter("EMPLOYEE_BIRTH");
		String employeeNationality = fileForm.getParameter("EMPLOYEE_NATIONALITY");
		String residencePlace = fileForm.getParameter("RESIDENCE_PLACE");
		String employeePhone = fileForm.getParameter("EMPLOYEE_PHONE");
		String employeeEmail = fileForm.getParameter("EMPLOYEE_EMAIL");
		
		String employeeNativePlace = fileForm.getParameter("EMPLOYEE_NATIVE_PLACE");
		String employeeDomicilePlace = fileForm.getParameter("EMPLOYEE_DOMICILE_PLACE");
		String employeeMaritalStatus = fileForm.getParameter("EMPLOYEE_MARITAL_STATUS");
		String employeePoliticalStatus = fileForm.getParameter("EMPLOYEE_POLITICAL_STATUS");
		String employeeHealth = fileForm.getParameter("EMPLOYEE_HEALTH");
		String jobBeginning = fileForm.getParameter("JOB_BEGINNING");
		String jobCategory = fileForm.getParameter("JOB_CATEGORY");
		String jobIndustry = fileForm.getParameter("JOB_INDUSTRY");
		String jobIntension = fileForm.getParameter("JOB_INTENSION");
		String workCity = fileForm.getParameter("WORK_CITY");
		
		String expectedSalary = fileForm.getParameter("EXPECTED_SALARY");
		String position = fileForm.getParameter("POSITION");
		String startWorking = fileForm.getParameter("START_WORKING");
		String graduationDate = fileForm.getParameter("GRADUATION_DATE");
		String graduationSchool = fileForm.getParameter("GRADUATION_SCHOOL");
		String employeeMajor = fileForm.getParameter("EMPLOYEE_MAJOR");
		String employeeHighestSchool = fileForm.getParameter("EMPLOYEE_HIGHEST_SCHOOL");
		String employeeHighestDegree = fileForm.getParameter("EMPLOYEE_HIGHEST_DEGREE");
		String foreignLanguage1 = fileForm.getParameter("FOREIGN_LANGUAGE1");
		String foreignLanguage2 = fileForm.getParameter("FOREIGN_LANGUAGE2");
		
		
		String foreignLanguage3 = fileForm.getParameter("FOREIGN_LANGUAGE3");
		String foreignLevel1 = fileForm.getParameter("FOREIGN_LEVEL1");
		String foreignLevel2 = fileForm.getParameter("FOREIGN_LEVEL2");
		String foreignLevel3 = fileForm.getParameter("FOREIGN_LEVEL3");
		String computerLevel = fileForm.getParameter("COMPUTER_LEVEL");
		String employeeAge = fileForm.getParameter("EMPLOYEE_AGE");
		String employeeSkills = fileForm.getParameter("EMPLOYEE_SKILLS");
		String careerSkills = fileForm.getParameter("CAREER_SKILLS");
		String workExperience = fileForm.getParameter("WORK_EXPERIENCE");
		String projectExperience = fileForm.getParameter("PROJECT_EXPERIENCE");
		
		
		String remark = fileForm.getParameter("REMARK");
		String resume = fileForm.getParameter("resume");
		
		YHORM orm = new YHORM();
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrRecruitPoolAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");
			String photoName = (String) map.get("photoName");
			
			YHHrRecruitPool pool = new YHHrRecruitPool();
			pool.setCreateUserId(createUserId);
			pool.setCreateDeptId(createDeptId);
			pool.setPlanNo(planNo);
			pool.setPlanName(planName);
			pool.setPosition(position);
			pool.setAddTime(YHUtility.parseTimeStamp());
			pool.setEmployeeName(employeeName);
			pool.setEmployeeSex(employeeSex);
			
			if(!YHUtility.isNullorEmpty(employeeBirth)){
				pool.setEmployeeBirth(YHUtility.parseTimeStamp(employeeBirth));
			}
			pool.setEmployeeNativePlace(employeeNativePlace);
			
			pool.setEmployeeDomicilePlace(employeeDomicilePlace);
			pool.setEmployeeMaritalStatus(employeeMaritalStatus);
			pool.setEmployeePoliticalStatus(employeePoliticalStatus);
			pool.setEmployeeNationality(employeeNationality);
			pool.setEmployeePhone(employeePhone);
			pool.setEmployeeEmail(employeeEmail);
			if(!YHUtility.isNullorEmpty(jobBeginning)){
				pool.setJobBeginning(YHUtility.parseTimeStamp(jobBeginning));
			}
			pool.setEmployeeHealth(employeeHealth);
			pool.setJobIntension(jobIntension);
			pool.setForeignLanguage1(foreignLanguage1);
			
			pool.setForeignLevel1(foreignLevel1);
			pool.setForeignLanguage2(foreignLanguage2);
			pool.setForeignLevel2(foreignLevel2);
			pool.setForeignLanguage3(foreignLanguage3);
			pool.setForeignLevel3(foreignLevel3);
			pool.setEmployeeHighestSchool(employeeHighestSchool);
			pool.setEmployeeHighestDegree(employeeHighestDegree);
			if(!YHUtility.isNullorEmpty(graduationDate)){
				pool.setGraduationDate(YHUtility.parseTimeStamp(graduationDate));
			}
			pool.setGraduationSchool(graduationSchool);
			pool.setEmployeeMajor(employeeMajor);
			
			pool.setComputerLevel(computerLevel);
			pool.setEmployeeSkills(employeeSkills);
			pool.setCareerSkills(careerSkills);
			pool.setWorkExperience(workExperience);
			pool.setProjectExperience(projectExperience);
			pool.setResume(resume);
			pool.setRemark(remark);
			if (attachFlag){
				pool.setAttachmentId(attachmentIds);
				pool.setAttachmentName(attachmentNames);
			}
			pool.setStartWorking(startWorking);
			
			pool.setExpectedSalary(expectedSalary);
			pool.setWorkCity(workCity);
			pool.setJobIndustry(jobIndustry);
			pool.setJobCategory(jobCategory);
			pool.setResidencePlace(residencePlace);
			pool.setEmployeeAge(employeeAge);
			if (!YHUtility.isNullorEmpty(photoName)) {
				pool.setPhotoName(photoName);
			}
			orm.saveSingle(dbConn, pool);
		} catch (Exception e) {
			throw e;
		}
		
	}
	/**
	 * 编辑人才档案
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setUpdateHrPoolInfoValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person,String seqIdStr) throws Exception {
		String createUserId = String.valueOf(person.getSeqId());
		int createDeptId = person.getDeptId();
		String planName = YHUtility.null2Empty(fileForm.getParameter("PLAN_NAME"));
		String planNo = fileForm.getParameter("PLAN_NO");
		String employeeName = fileForm.getParameter("EMPLOYEE_NAME");
		String employeeSex = fileForm.getParameter("employeeSex");
		String employeeBirth = fileForm.getParameter("employeeBirth");
		String employeeNationality = fileForm.getParameter("employeeNationality");
		String residencePlace = fileForm.getParameter("residencePlace");
		String employeePhone = fileForm.getParameter("employeePhone");
		String employeeEmail = fileForm.getParameter("employeeEmail");
		
		String employeeNativePlace = fileForm.getParameter("employeeNativePlace");
		String employeeDomicilePlace = fileForm.getParameter("employeeDomicilePlace");
		String employeeMaritalStatus = fileForm.getParameter("employeeMaritalStatus");
		String employeePoliticalStatus = fileForm.getParameter("employeePoliticalStatus");
		String employeeHealth = fileForm.getParameter("employeeHealth");
		String jobBeginning = fileForm.getParameter("jobBeginning");
		String jobCategory = fileForm.getParameter("jobCategory");
		String jobIndustry = fileForm.getParameter("jobIndustry");
		String jobIntension = fileForm.getParameter("jobIntension");
		String workCity = fileForm.getParameter("workCity");
		
		String expectedSalary = fileForm.getParameter("expectedSalary");
		String position = fileForm.getParameter("position");
		String startWorking = fileForm.getParameter("startWorking");
		String graduationDate = fileForm.getParameter("graduationDate");
		String graduationSchool = fileForm.getParameter("graduationSchool");
		String employeeMajor = fileForm.getParameter("employeeMajor");
		String employeeHighestSchool = fileForm.getParameter("employeeHighestSchool");
		String employeeHighestDegree = fileForm.getParameter("employeeHighestDegree");
		String foreignLanguage1 = fileForm.getParameter("foreignLanguage1");
		String foreignLanguage2 = fileForm.getParameter("foreignLanguage2");
		
		
		String foreignLanguage3 = fileForm.getParameter("foreignLanguage3");
		String foreignLevel1 = fileForm.getParameter("foreignLevel1");
		String foreignLevel2 = fileForm.getParameter("foreignLevel2");
		String foreignLevel3 = fileForm.getParameter("foreignLevel3");
		String computerLevel = fileForm.getParameter("computerLevel");
		String employeeAge = fileForm.getParameter("employeeAge");
		String employeeSkills = fileForm.getParameter("employeeSkills");
		String careerSkills = fileForm.getParameter("careerSkills");
		String workExperience = fileForm.getParameter("workExperience");
		String projectExperience = fileForm.getParameter("projectExperience");
		
		
		String remark = fileForm.getParameter("remark");
		String resume = fileForm.getParameter("resume");
		
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		YHORM orm = new YHORM();
		try {
			
			YHHrRecruitPool pool = (YHHrRecruitPool) orm.loadObjSingle(dbConn, YHHrRecruitPool.class, seqId);
			if (pool!=null) {
				Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrRecruitPoolAct.attachmentFolder);
				boolean attachFlag = (Boolean) map.get("attachFlag");
				String attachmentIds = (String) map.get("attachmentIds");
				String attachmentNames = (String) map.get("attachmentNames");
				String photoName = (String) map.get("photoName");
				
				String dbAttachmentIds= YHUtility.null2Empty(pool.getAttachmentId());
				String dbAttachmentNames= YHUtility.null2Empty(pool.getAttachmentName());
				
				
				
				pool.setCreateUserId(createUserId);
				pool.setCreateDeptId(createDeptId);
				pool.setPlanNo(planNo);
				pool.setPlanName(planName);
				pool.setPosition(position);
				pool.setAddTime(YHUtility.parseTimeStamp());
				pool.setEmployeeName(employeeName);
				pool.setEmployeeSex(employeeSex);
				
				if(!YHUtility.isNullorEmpty(employeeBirth)){
					pool.setEmployeeBirth(YHUtility.parseTimeStamp(employeeBirth));
				}
				pool.setEmployeeNativePlace(employeeNativePlace);
				
				pool.setEmployeeDomicilePlace(employeeDomicilePlace);
				pool.setEmployeeMaritalStatus(employeeMaritalStatus);
				pool.setEmployeePoliticalStatus(employeePoliticalStatus);
				pool.setEmployeeNationality(employeeNationality);
				pool.setEmployeePhone(employeePhone);
				pool.setEmployeeEmail(employeeEmail);
				if(!YHUtility.isNullorEmpty(jobBeginning)){
					pool.setJobBeginning(YHUtility.parseTimeStamp(jobBeginning));
				}
				pool.setEmployeeHealth(employeeHealth);
				pool.setJobIntension(jobIntension);
				pool.setForeignLanguage1(foreignLanguage1);
				
				pool.setForeignLevel1(foreignLevel1);
				pool.setForeignLanguage2(foreignLanguage2);
				pool.setForeignLevel2(foreignLevel2);
				pool.setForeignLanguage3(foreignLanguage3);
				pool.setForeignLevel3(foreignLevel3);
				pool.setEmployeeHighestSchool(employeeHighestSchool);
				pool.setEmployeeHighestDegree(employeeHighestDegree);
				if(!YHUtility.isNullorEmpty(graduationDate)){
					pool.setGraduationDate(YHUtility.parseTimeStamp(graduationDate));
				}
				pool.setGraduationSchool(graduationSchool);
				pool.setEmployeeMajor(employeeMajor);
				
				pool.setComputerLevel(computerLevel);
				pool.setEmployeeSkills(employeeSkills);
				pool.setCareerSkills(careerSkills);
				pool.setWorkExperience(workExperience);
				pool.setProjectExperience(projectExperience);
				pool.setResume(resume);
				pool.setRemark(remark);
				pool.setAttachmentId(attachmentIds + dbAttachmentIds );
				pool.setAttachmentName(attachmentNames + dbAttachmentNames);
				pool.setStartWorking(startWorking);
				
				pool.setExpectedSalary(expectedSalary);
				pool.setWorkCity(workCity);
				pool.setJobIndustry(jobIndustry);
				pool.setJobCategory(jobCategory);
				pool.setResidencePlace(residencePlace);
				pool.setEmployeeAge(employeeAge);
				if (!YHUtility.isNullorEmpty(photoName)) {
					pool.setPhotoName(photoName);
				}
				orm.updateSingle(dbConn, pool);
			}
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
			String headPicPath = YHSysProps.getAttachPath() + separator + YHHrRecruitPoolAct.headPicFolder;

			String headPicName = YHUtility.null2Empty(fileForm.getParameter("EMPLOYEE_NAME"));

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
			result.put("attachFlag", attachFlag);
			result.put("attachmentIds", attachmentIds);
			result.put("attachmentNames", attachmentNames);
			result.put("photoName", photoName);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	/**
	 * 管理权限或查看权限(创建人、oa管理员、部门人力资源管理员)
	 * @param dbConn
	 * @param person
	 * @param curStaffField 表字段名
	 * @return
	 * @throws Exception
	 */
	public String getHrPriv(Connection dbConn,YHPerson person,String curStaffField) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String privWhereStr = "";
		try {
			String hrDeptIdStr = this.getHrDeptIdStr(dbConn, person);
			String sql = "SELECT SEQ_ID from PERSON where " + YHDBUtility.findInSet(hrDeptIdStr, "DEPT_ID");
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			String userIdStr = "";
			while(rs.next()){
				userIdStr += rs.getInt("SEQ_ID") + ",";
			}
			if (userIdStr.endsWith(",")) {
				userIdStr = userIdStr.substring(0, userIdStr.length()-1);
			}
			if (YHUtility.isNullorEmpty(userIdStr)) {
				userIdStr = "0";
			}
			if (person.isAdminRole()) {
				return privWhereStr = " 1=1";
			}
			if (!YHUtility.isNullorEmpty(curStaffField)) {
				return privWhereStr = " (CREATE_USER_ID='" + person.getSeqId() + "' or " + YHDBUtility.findInSet(userIdStr, curStaffField) + " )"; 
			}else {
				return privWhereStr = " (CREATE_USER_ID='" + person.getSeqId() + "' or " + YHDBUtility.findInSet(hrDeptIdStr, "CREATE_DEPT_ID") + " )";
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
	}
	/**
	 * 获取HR_MANAGER表下的DEPT_ID
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getHrDeptIdStr(Connection dbConn,YHPerson person) throws Exception{
		String deptIdStr = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT DEPT_ID from oa_pm_manager where " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "DEPT_HR_MANAGER") ;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				deptIdStr += rs.getInt("DEPT_ID") + ",";
			}
			if (deptIdStr.endsWith(",")) {
				deptIdStr = deptIdStr.substring(0, deptIdStr.length()-1);
			}
			if (YHUtility.isNullorEmpty(deptIdStr)) {
				deptIdStr = "-1";
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return deptIdStr;
	}
	
	/**
	 * 人才档案查询列表
	 * @param dbConn
	 * @param request
	 * @param person
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryHrPoolListJsonLogic(Connection dbConn, Map request, YHPerson person,Map map) throws Exception{
		String planNo = (String) map.get("planNo");
		String employeeName = (String) map.get("employeeName");
		String employeeSex = (String) map.get("employeeSex");
		String employeeNativePlace = (String) map.get("employeeNativePlace");
		String employeePoliticalStatus = (String) map.get("employeePoliticalStatus");//
		String position = (String) map.get("position");
		String jobCategory = (String) map.get("jobCategory");//
		String jobIntension = (String) map.get("jobIntension");
		String workCity = (String) map.get("workCity");
		String expectedSalary = (String) map.get("expectedSalary");
		
		String startWorking = (String) map.get("startWorking");
		String employeeMajor = (String) map.get("employeeMajor");
		String employeeHighestSchool = (String) map.get("employeeHighestSchool");
		String residencePlace = (String) map.get("residencePlace");
		String employeeNationality = (String) map.get("employeeNationality");
		String employeeHealth = (String) map.get("employeeHealth");//
		String employeeMaritalStatus = (String) map.get("employeeMaritalStatus");
		String employeeDomicilePlace = (String) map.get("employeeDomicilePlace");
		String graduationSchool = (String) map.get("graduationSchool");
		String computerLevel = (String) map.get("computerLevel");
		
		String foreignLanguage1 = (String) map.get("foreignLanguage1");
		String foreignLanguage2 = (String) map.get("foreignLanguage2");
		String foreignLevel1 = (String) map.get("foreignLevel1");
		String foreignLevel2 = (String) map.get("foreignLevel2");
		
		String conditionStr = "";
		try {
			if (!YHUtility.isNullorEmpty(planNo)) {
				conditionStr += " and PLAN_NO ='" + YHDBUtility.escapeLike(planNo) + "'";
			}
			if (!YHUtility.isNullorEmpty(employeeName)) {
				conditionStr += " and EMPLOYEE_NAME ='" + YHDBUtility.escapeLike(employeeName) + "'";
			}
			if (!YHUtility.isNullorEmpty(employeeSex)) {
				conditionStr += " and EMPLOYEE_SEX ='" + YHDBUtility.escapeLike(employeeSex) + "'";
			}
			if (!YHUtility.isNullorEmpty(employeeNativePlace)) {
				conditionStr += " and EMPLOYEE_NATIVE_PLACE ='" + YHDBUtility.escapeLike(employeeNativePlace) + "'";
			}
			if (!YHUtility.isNullorEmpty(employeePoliticalStatus)) {
				conditionStr += " and EMPLOYEE_POLITICAL_STATUS ='" + YHDBUtility.escapeLike(employeePoliticalStatus) + "'";
			}
			if (!YHUtility.isNullorEmpty(position)) {
				conditionStr += " and POSITION like '%" + YHDBUtility.escapeLike(position) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(jobCategory)) {
				conditionStr += " and JOB_CATEGORY ='" + YHDBUtility.escapeLike(jobCategory) + "'";
			}
		
			if (!YHUtility.isNullorEmpty(jobIntension)) {
				conditionStr += " and JOB_INTENSION like '%" + YHDBUtility.escapeLike(jobIntension) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(workCity)) {
				conditionStr += " and WORK_CITY like '%" + YHDBUtility.escapeLike(workCity) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(expectedSalary)) {
				conditionStr += " and EXPECTED_SALARY ='" + YHDBUtility.escapeLike(expectedSalary) + "'";
			}
			if (!YHUtility.isNullorEmpty(startWorking)) {
				conditionStr += " and START_WORKING ='" + YHDBUtility.escapeLike(startWorking) + "'";
			}
			if (!YHUtility.isNullorEmpty(employeeMajor)) {
				conditionStr += " and EMPLOYEE_MAJOR like '%" + YHDBUtility.escapeLike(employeeMajor) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(employeeHighestSchool)) {
				conditionStr += " and EMPLOYEE_HIGHEST_SCHOOL ='" + YHDBUtility.escapeLike(employeeHighestSchool) + "'";
			}
			if (!YHUtility.isNullorEmpty(residencePlace)) {
				conditionStr += " and RESIDENCE_PLACE like '%" + YHDBUtility.escapeLike(residencePlace) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(employeeNationality)) {
				conditionStr += " and EMPLOYEE_NATIONALITY ='" + YHDBUtility.escapeLike(employeeNationality) + "'";
			}
			if (!YHUtility.isNullorEmpty(employeeHealth)) {
				conditionStr += " and EMPLOYEE_HEALTH like '%" + YHDBUtility.escapeLike(employeeHealth) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(employeeMaritalStatus)) {
				conditionStr += " and EMPLOYEE_MARITAL_STATUS ='" + YHDBUtility.escapeLike(employeeMaritalStatus) + "'";
			}
			
			
			if (!YHUtility.isNullorEmpty(employeeDomicilePlace)) {
				conditionStr += " and EMPLOYEE_DOMICILE_PLACE like '%" + YHDBUtility.escapeLike(employeeDomicilePlace) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(graduationSchool)) {
				conditionStr += " and GRADUATION_SCHOOL like '%" + YHDBUtility.escapeLike(graduationSchool) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(computerLevel)) {
				conditionStr += " and COMPUTER_LEVEL like '%" + YHDBUtility.escapeLike(computerLevel) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage1)) {
				conditionStr += " and FOREIGN_LANGUAGE1 like '%" + YHDBUtility.escapeLike(foreignLanguage1) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(foreignLanguage2)) {
				conditionStr += " and FOREIGN_LANGUAGE2 like '%" + YHDBUtility.escapeLike(foreignLanguage2) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(foreignLevel1)) {
				conditionStr += " and FOREIGN_LEVEL1 like '%" + YHDBUtility.escapeLike(foreignLevel1) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(foreignLevel2)) {
				conditionStr += " and FOREIGN_LEVEL2 like '%" + YHDBUtility.escapeLike(foreignLevel2) + "%'" + YHDBUtility.escapeLike();
			}
			
			String sql =  "select " +
   		"SEQ_ID" +
   		",PLAN_NAME" +
   		",EMPLOYEE_NAME" +
   		",EMPLOYEE_BIRTH" +
   		",EMPLOYEE_PHONE" +
   		",EMPLOYEE_HIGHEST_SCHOOL" +
   		",EMPLOYEE_MAJOR" +
   		",POSITION" +
   		",ADD_TIME" +
       " from " +
   		"oa_pm_enroll_set where " +
   		this.getHrPriv(dbConn, person, "") + 
   		conditionStr +
   		" order by ADD_TIME desc"; 
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 删除人才档案
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @throws Exception
	 */
	public void deleteHrPoolLogic(Connection dbConn, String seqIdStr,String filePath) throws Exception {
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "";
		}
		YHORM orm = new YHORM();
		try {
			String[] seqIdArry = seqIdStr.split(",");
			if (seqIdArry != null && seqIdArry.length > 0) {
				for (String seqId : seqIdArry) {
					YHHrRecruitPool recruitPool = (YHHrRecruitPool) orm.loadObjSingle(dbConn,YHHrRecruitPool.class,Integer.parseInt(seqId) );
					if (recruitPool != null) {
						String photoName = YHUtility.null2Empty(recruitPool.getPhotoName());
						String attachmentId = YHUtility.null2Empty(recruitPool.getAttachmentId());
						String attachmentName = YHUtility.null2Empty(recruitPool.getAttachmentName());
						if (!YHUtility.isNullorEmpty(photoName)) {
							this.deleHeadPic(photoName);
						}
						if (!YHUtility.isNullorEmpty(attachmentId)&& !YHUtility.isNullorEmpty(attachmentName)) {
							this.delteAttaIdFile(filePath, attachmentId, attachmentName);
						}
						orm.deleteSingle(dbConn, recruitPool);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	public boolean deleHeadPic(String photoName) throws Exception {
		String filePath = YHSysProps.getAttachPath() + File.separator + YHHrRecruitPoolAct.headPicFolder + File.separator + photoName;
		boolean delFlag = false;
		try {
			File file = new File(filePath);
			if (file!=null && file.exists()) {
				YHFileUtility.deleteAll(file.getAbsoluteFile());
				delFlag = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return delFlag;
	}
	
	/**
	 * 删除附件文件
	 * @param filePath
	 * @param attachmentId
	 * @param attachmentName
	 * @throws Exception
	 */
	public void delteAttaIdFile(String filePath,String attachmentId,String attachmentName) throws Exception{
		StringBuffer attIdBuffer = new StringBuffer();
		StringBuffer attNameBuffer = new StringBuffer();
		try {
			attIdBuffer.append(attachmentId.trim());
			attNameBuffer.append(attachmentName.trim());
			String[] attIdArray = {};
			String[] attNameArray = {};
			if (!YHUtility.isNullorEmpty(attIdBuffer.toString()) && !YHUtility.isNullorEmpty(attNameBuffer.toString()) && attIdBuffer.length() > 0) {
				attIdArray = attIdBuffer.toString().trim().split(",");
				attNameArray = attNameBuffer.toString().trim().split("\\*");
			}
			if (attIdArray != null && attIdArray.length > 0) {
				for (int i = 0; i < attIdArray.length; i++) {
					String attachmentIdStr = attIdArray[i];
					String attachmentNameStr = attNameArray[i];
					String attaIdStr = this.getAttaId(attachmentIdStr);
					String fileNameValue = attaIdStr + "_" + attachmentNameStr;
					String fileFolder = this.getFilePathFolder(attachmentIdStr);
					String oldFileNameValue = attaIdStr + "." + attachmentNameStr;
					File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
					File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);
					if (file.exists()) {
						YHFileUtility.deleteAll(file.getAbsoluteFile());
					} else if (oldFile.exists()) {
						YHFileUtility.deleteAll(oldFile.getAbsoluteFile());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 得到附件的Id 兼老数据--wyw
	 * 
	 * @param keyId
	 * @return
	 */
	public String getAttaId(String keyId) {
		String attaId = "";
		if (keyId != null && !"".equals(keyId)) {
			if (keyId.indexOf('_') != -1) {
				String[] ids = keyId.split("_");
				if (ids.length > 0) {
					attaId = ids[1];
				}

			} else {
				attaId = keyId;
			}
		}
		return attaId;
	}

	/**
	 * 得到该文件的文件夹名--wyw
	 * 
	 * @param key
	 * @return
	 */
	public String getFilePathFolder(String key) {
		String folder = "";
		if (key != null && !"".equals(key)) {
			if (key.indexOf('_') != -1) {
				String[] str = key.split("_");
				for (int i = 0; i < str.length; i++) {
					folder = str[0];
				}
			} else {
				folder = "all";
			}
		}
		return folder;
	}
	/**
	 * 更新附件数据
	 * @param dbConn
	 * @param seqIdStr
	 * @param attachId
	 * @return
	 * @throws Exception
	 */
	public boolean updateFloadFile(Connection dbConn, String seqIdStr, String attachId) throws Exception {
		boolean returnFlag = false;
		YHORM orm =new YHORM();
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			
			YHHrRecruitPool recruitPool = (YHHrRecruitPool) orm.loadObjSingle(dbConn, YHHrRecruitPool.class, seqId);
			String dbAttachId = "";
			String dbAttachName = "";
			if (recruitPool!=null) {
				dbAttachId = YHUtility.null2Empty(recruitPool.getAttachmentId());
				dbAttachName = YHUtility.null2Empty(recruitPool.getAttachmentName());
				String[] dbAttachIdArrays = dbAttachId.split(",");
				String[] dbAttachNameArrays = dbAttachName.split("\\*");
				String attachmentId = "";
				String attachmentName = "";
				if (!YHUtility.isNullorEmpty(attachId) && dbAttachIdArrays.length > 0) {
					for(int i=0;i<dbAttachIdArrays.length;i++){
						if (attachId.trim().equals(dbAttachIdArrays[i])) {
							continue;
						}
						attachmentId += dbAttachIdArrays[i] + ",";
						attachmentName += dbAttachNameArrays[i] + "*";
					}
					recruitPool.setAttachmentId(attachmentId.trim());
					recruitPool.setAttachmentName(attachmentName.trim());
					orm.updateSingle(dbConn, recruitPool);
					returnFlag = true;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return returnFlag;
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
			YHHrRecruitPool recruitPool = (YHHrRecruitPool) orm.loadObjSingle(dbConn, YHHrRecruitPool.class, seqId);
			if (recruitPool != null) {
				String photoName = YHUtility.null2Empty(recruitPool.getPhotoName());
				if (!YHUtility.isNullorEmpty(photoName)) {
					boolean delFlag = this.deleHeadPic(photoName);
					if (delFlag) {
						recruitPool.setPhotoName("");
						orm.updateSingle(dbConn, recruitPool);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	

}

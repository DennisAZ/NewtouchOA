package yh.subsys.oa.hr.manage.leave.logic;

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
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.leave.data.YHHrStaffLeave;
import yh.subsys.oa.hr.manage.logic.YHHrStaffIncentiveLogic;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;
import yh.subsys.oa.hr.manage.staffInfo.logic.YHHrStaffInfoLogic;
import yh.subsys.oa.hr.setting.act.YHHrSetOtherAct;

public class YHHrStaffLeaveLogic {
	private static Logger log = Logger.getLogger(YHHrStaffLeaveLogic.class);

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

			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
				String rand = emul.getRandom();
				uploadAttchId += currDate + "_" + rand + ",";
				uploadAttchName += fileName + "*";
				uploadFlag = true;

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath + File.separator + fileName);
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
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 新建员工离职
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setNewStaffLeaveValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String leavePerson = fileForm.getParameter("leavePerson");
		String position = fileForm.getParameter("position");
		String quitType = fileForm.getParameter("quitType");
		String applicationDateStr = fileForm.getParameter("applicationDate");
		String quitTimePlanStr = fileForm.getParameter("quitTimePlan");
		String quitTimeFactStr = fileForm.getParameter("quitTimeFact");
		String lastSalaryTimeStr = fileForm.getParameter("lastSalaryTime");
		String leaveDept = fileForm.getParameter("leaveDept");
		String trace = fileForm.getParameter("trace");
		String materialsCondition = fileForm.getParameter("materialsCondition");
		String remark = fileForm.getParameter("remark");
		String quitReason = fileForm.getParameter("quitReason");
		String smsRemind = fileForm.getParameter("smsRemind");
		String smsRemind1 = fileForm.getParameter("smsRemind1");

		Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
		boolean attachFlag = (Boolean) map.get("attachFlag");
		String attachmentIds = (String) map.get("attachmentIds");
		String attachmentNames = (String) map.get("attachmentNames");

		try {
			YHHrStaffLeave staffLeave = new YHHrStaffLeave();
			staffLeave.setLeavePerson(leavePerson);
			staffLeave.setPosition(position);
			staffLeave.setQuitType(quitType);
			if (!YHUtility.isNullorEmpty(applicationDateStr)) {
				Date applicationDate = YHUtility.parseDate("yyyy-MM-dd", applicationDateStr);
				staffLeave.setApplicationDate(applicationDate);
			}
			if (!YHUtility.isNullorEmpty(quitTimePlanStr)) {
				Date quitTimePlan = YHUtility.parseDate("yyyy-MM-dd", quitTimePlanStr);
				staffLeave.setQuitTimePlan(quitTimePlan);
			}
			if (!YHUtility.isNullorEmpty(quitTimeFactStr)) {
				Date quitTimeFact = YHUtility.parseDate("yyyy-MM-dd", quitTimeFactStr);
				staffLeave.setQuitTimeFact(quitTimeFact);
			}
			if (!YHUtility.isNullorEmpty(lastSalaryTimeStr)) {
				Date lastSalaryTime = YHUtility.parseDate("yyyy-MM-dd", lastSalaryTimeStr);
				staffLeave.setLastSalaryTime(lastSalaryTime);
			}
			staffLeave.setLeaveDept(leaveDept);
			staffLeave.setTrace(trace);
			staffLeave.setMaterialsCondition(materialsCondition);
			staffLeave.setRemark(remark);
			staffLeave.setAddTime(YHUtility.parseTimeStamp());
			staffLeave.setCreateUserId(String.valueOf(person.getSeqId()));
			staffLeave.setCreateDeptId(person.getDeptId());
			staffLeave.setQuitReason(quitReason);
			if (attachFlag) {
				staffLeave.setAttachmentId(attachmentIds);
				staffLeave.setAttachmentName(attachmentNames);
			}
			orm.saveSingle(dbConn, staffLeave);
			int maxSeqId = this.getMaxSeqId(dbConn);
			String remindUrl = "/subsys/oa/hr/manage/staffLeave/detail.jsp?seqId=" + maxSeqId + "&openFlag=1&openWidth=860&openHeight=650";
			String smsContent = "请查看员工离职信息！";
			// 短信提醒
			if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
				this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), smsRemind1, "64", remindUrl, new Date());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取最大的SeqId值
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @return
	 */
	public int getMaxSeqId(Connection dbConn) {
		String sql = "select SEQ_ID from oa_pm_employee_leave where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_employee_leave )";
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
	 * 员工离职 通用列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getStaffLeaveJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
		try {
	    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
	    String deptIdStr = logic.getHrManagerPriv(dbConn, person);
		  
			String sql = " select l1.SEQ_ID, p1.USER_NAME TRANSFER_PERSON ,l1.POSITION ,l1.QUIT_TYPE, l1.QUIT_TIME_PLAN, l1.LAST_SALARY_TIME "
					       + " from oa_pm_employee_leave l1 " 
					       + " join PERSON p1 on l1.LEAVE_PERSON = p1.SEQ_ID " 
					       + " where CREATE_USER_ID = "+ person.getSeqId()
				         + " or CREATE_DEPT_ID in "+ deptIdStr
					       + " ORDER BY l1.ADD_TIME desc  ";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取单位员工用户名称
	 * 
	 * @param conn
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public String getUserNameLogic(Connection conn, String userIdStr) throws Exception {
		if (YHUtility.isNullorEmpty(userIdStr)) {
			userIdStr = "-1";
		}
		if (userIdStr.endsWith(",")) {
			userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
		}
		String result = "";
		String sql = " select USER_NAME from PERSON where SEQ_ID IN (" + userIdStr + ")";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String toId = rs.getString(1);
				if (!"".equals(result)) {
					result += ",";
				}
				result += toId;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return result;
	}

	/**
	 * 删除文件--wyw
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @throws Exception
	 */
	public void deleteFileLogic(Connection dbConn, String seqIdStr, String filePath) throws Exception {
		YHORM orm = new YHORM();
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "";
		}
		try {
			String seqIdArry[] = seqIdStr.split(",");
			if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
				for (String seqId : seqIdArry) {
					StringBuffer attIdBuffer = new StringBuffer();
					StringBuffer attNameBuffer = new StringBuffer();
					YHHrStaffLeave staffLeave = (YHHrStaffLeave) orm.loadObjSingle(dbConn, YHHrStaffLeave.class, Integer.parseInt(seqId));
					String attachmentId = YHUtility.null2Empty(staffLeave.getAttachmentId());
					String attachmentName = YHUtility.null2Empty(staffLeave.getAttachmentName());
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
							Map<String, String> map = this.getFileName(attIdArray[i], attNameArray[i]);
							if (map.size() != 0) {
								Set<String> set = map.keySet();
								// 遍历Set集合
								for (String keySet : set) {
									String key = keySet;
									String keyValue = map.get(keySet);
									String attaIdStr = this.getAttaId(keySet);
									String fileNameValue = attaIdStr + "_" + keyValue;
									String fileFolder = this.getFilePathFolder(key);
									String oldFileNameValue = attaIdStr + "." + keyValue;
									File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
									File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);
									if (file.exists()) {
										YHFileUtility.deleteAll(file.getAbsoluteFile());
									} else if (oldFile.exists()) {
										YHFileUtility.deleteAll(oldFile.getAbsoluteFile());
									}
								}
							}
						}
					}
					// 删除数据库信息
					orm.deleteSingle(dbConn, staffLeave);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 拼接附件Id与附件名--wyw
	 * 
	 * @param attachmentId
	 * @param attachmentName
	 * @return
	 */
	public Map<String, String> getFileName(String attachmentId, String attachmentName) {
		Map<String, String> map = new HashMap<String, String>();
		if (YHUtility.isNullorEmpty(attachmentId) || YHUtility.isNullorEmpty(attachmentName)) {
			return map;
		}
		if (!"".equals(attachmentId.trim()) && !"".equals(attachmentName.trim())) {
			String attachmentIds[] = attachmentId.split(",");
			String attachmentNames[] = attachmentName.split("\\*");
			if (attachmentIds.length != 0 && attachmentNames.length != 0) {
				for (int i = 0; i < attachmentIds.length; i++) {
					map.put(attachmentIds[i], attachmentNames[i]);
				}
			}
		}
		return map;
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
	 * 获取详情
	 * 
	 * @param conn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public YHHrStaffLeave getLeaveDetailLogic(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHHrStaffLeave) orm.loadObjSingle(conn, YHHrStaffLeave.class, seqId);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 员工离职查询
	 * 
	 * @param dbConn
	 * @param request
	 * @param map
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String queryLeaveListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, person);
	  
		String leavePerson = (String) map.get("leavePerson");
		String quitType = (String) map.get("quitType");
		String quitTimePlan1 = (String) map.get("quitTimePlan1");
		String quitTimePlan2 = (String) map.get("quitTimePlan2");
		String quitTimeFact1 = (String) map.get("quitTimeFact1");
		String quitTimeFact2 = (String) map.get("quitTimeFact2");
		String quitReason = (String) map.get("quitReason");
		String materialsCondition = (String) map.get("materialsCondition");
		String conditionStr = "";
		String sql = "";
		try {
			if (!YHUtility.isNullorEmpty(leavePerson)) {
				conditionStr = " and l1.LEAVE_PERSON ='" + YHDBUtility.escapeLike(leavePerson) + "'";
			}
			if (!YHUtility.isNullorEmpty(quitType)) {
				conditionStr = " and l1.QUIT_TYPE ='" + YHDBUtility.escapeLike(quitType) + "'";
			}
			if (!YHUtility.isNullorEmpty(quitTimePlan1)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("QUIT_TIME_PLAN", quitTimePlan1, ">=");
			}
			if (!YHUtility.isNullorEmpty(quitTimePlan2)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("QUIT_TIME_PLAN", quitTimePlan2, "<=");
			}
			if (!YHUtility.isNullorEmpty(quitTimeFact1)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("QUIT_TIME_FACT", quitTimeFact1, ">=");
			}
			if (!YHUtility.isNullorEmpty(quitTimeFact2)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("QUIT_TIME_FACT", quitTimeFact2, "<=");
			}
			if (!YHUtility.isNullorEmpty(quitReason)) {
				conditionStr += " and l1.QUIT_REASON like '%" + YHDBUtility.escapeLike(quitReason) + "%'";
			}
			if (!YHUtility.isNullorEmpty(materialsCondition)) {
				conditionStr += " and l1.MATERIALS_CONDITION like '%" + YHDBUtility.escapeLike(materialsCondition) + "%'";
			}
			sql = "  select l1.SEQ_ID, p1.USER_NAME TRANSFER_PERSON ,l1.POSITION ,l1.QUIT_TYPE, l1.QUIT_TIME_PLAN, l1.LAST_SALARY_TIME "
					+ " from oa_pm_employee_leave l1 " + " join PERSON p1 on l1.LEAVE_PERSON = p1.SEQ_ID " 
			    + " where  (CREATE_USER_ID = "+ person.getSeqId()
          + " or CREATE_DEPT_ID in "+ deptIdStr + ")"
			    + conditionStr
					+ " ORDER BY l1.ADD_TIME desc";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 员工离职信息
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void updateLeaveInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String seqIdStr = fileForm.getParameter("seqId");
		String leavePerson = fileForm.getParameter("leavePerson");
		String position = fileForm.getParameter("position");
		String quitType = fileForm.getParameter("quitType");
		String applicationDateStr = fileForm.getParameter("applicationDate");
		String quitTimePlanStr = fileForm.getParameter("quitTimePlan");
		String quitTimeFactStr = fileForm.getParameter("quitTimeFact");
		String lastSalaryTimeStr = fileForm.getParameter("lastSalaryTime");
		String leaveDept = fileForm.getParameter("leaveDept");
		String trace = fileForm.getParameter("trace");
		String materialsCondition = fileForm.getParameter("materialsCondition");
		String remark = fileForm.getParameter("remark");
		String quitReason = fileForm.getParameter("quitReason");
		String smsRemind = fileForm.getParameter("smsRemind");
		String smsRemind1 = fileForm.getParameter("smsRemind1");

		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
		boolean attachFlag = (Boolean) map.get("attachFlag");
		String attachmentIds = (String) map.get("attachmentIds");
		String attachmentNames = (String) map.get("attachmentNames");

		try {
			YHHrStaffLeave staffLeave = (YHHrStaffLeave) orm.loadObjSingle(dbConn, YHHrStaffLeave.class, seqId);
			if (staffLeave != null) {
				String dbAttachId = YHUtility.null2Empty(staffLeave.getAttachmentId());
				String dbAttachName = YHUtility.null2Empty(staffLeave.getAttachmentName());

				staffLeave.setLeavePerson(leavePerson);
				staffLeave.setPosition(position);
				staffLeave.setQuitType(quitType);
				if (!YHUtility.isNullorEmpty(applicationDateStr)) {
					Date applicationDate = YHUtility.parseDate("yyyy-MM-dd", applicationDateStr);
					staffLeave.setApplicationDate(applicationDate);
				}
				if (!YHUtility.isNullorEmpty(quitTimePlanStr)) {
					Date quitTimePlan = YHUtility.parseDate("yyyy-MM-dd", quitTimePlanStr);
					staffLeave.setQuitTimePlan(quitTimePlan);
				}
				if (!YHUtility.isNullorEmpty(quitTimeFactStr)) {
					Date quitTimeFact = YHUtility.parseDate("yyyy-MM-dd", quitTimeFactStr);
					staffLeave.setQuitTimeFact(quitTimeFact);
				}
				if (!YHUtility.isNullorEmpty(lastSalaryTimeStr)) {
					Date lastSalaryTime = YHUtility.parseDate("yyyy-MM-dd", lastSalaryTimeStr);
					staffLeave.setLastSalaryTime(lastSalaryTime);
				}
				staffLeave.setLeaveDept(leaveDept);
				staffLeave.setTrace(trace);
				staffLeave.setMaterialsCondition(materialsCondition);
				staffLeave.setRemark(remark);
				staffLeave.setAddTime(YHUtility.parseTimeStamp());
				staffLeave.setCreateUserId(String.valueOf(person.getSeqId()));
				staffLeave.setQuitReason(quitReason);
				if (attachFlag) {
					staffLeave.setAttachmentId(dbAttachId.trim() + attachmentIds.trim());
					staffLeave.setAttachmentName(dbAttachName.trim() + attachmentNames.trim());
				}
				orm.updateSingle(dbConn, staffLeave);
				String remindUrl = "/subsys/oa/hr/manage/staffLeave/detail.jsp?seqId=" + seqId + "&openFlag=1&openWidth=860&openHeight=650";
				String smsContent = "请查看员工离职信息！";
				// 短信提醒
				if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
					this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), smsRemind1, "64", remindUrl, new Date());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 短信提醒(带时间)
	 * 
	 * @param conn
	 * @param content
	 * @param fromId
	 * @param toId
	 * @param type
	 * @param remindUrl
	 * @param sendDate
	 * @throws Exception
	 */
	public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
			throws Exception {
		YHSmsBack sb = new YHSmsBack();
		sb.setContent(content);
		sb.setFromId(fromId);
		sb.setToId(toId);
		sb.setSmsType(type);
		sb.setRemindUrl(remindUrl);
		sb.setSendDate(sendDate);
		YHSmsUtil.smsBack(conn, sb);
	}

	/**
	 * 返回两个日期的相隔月份
	 * 
	 * 
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<String> getDateValue(String startDateStr, String endDateStr) throws Exception {
		List<String> list = new ArrayList<String>();
		if (YHUtility.isNullorEmpty(endDateStr) && !YHUtility.isNullorEmpty(startDateStr)) {
			endDateStr = startDateStr;
			list.add(startDateStr);
			list.add(endDateStr);
			return list;
		} else if (YHUtility.isNullorEmpty(startDateStr) && !YHUtility.isNullorEmpty(endDateStr)) {
			startDateStr = endDateStr;
			list.add(startDateStr);
			list.add(endDateStr);
			return list;
		}
		try {
			if (!YHUtility.isNullorEmpty(startDateStr) && !YHUtility.isNullorEmpty(endDateStr)) {
				String startDateArry[] = startDateStr.split("-");
				String endDateArry[] = endDateStr.split("-");
				int startYear = Integer.parseInt(startDateArry[0]);
				int startMonth = Integer.parseInt(startDateArry[1]);
				int endMonth = Integer.parseInt(endDateArry[1]);
				String result = "";
				if (startMonth < endMonth) {
					list.add(startDateStr);
					int tmp = endMonth - startMonth;
					if (tmp <= 11) {
						for (int i = 1; i < tmp; i++) {
							int tmpMonth = startMonth + i;
							String str = "";
							if (tmpMonth < 10) {
								str = "0";
							}
							result = startYear + "-" + str + tmpMonth;
							list.add(result);
						}
					}
					list.add(endDateStr);
				} else if (startMonth == endMonth) {
					list.add(startDateStr);
					list.add(endDateStr);

				} else if (startMonth > endMonth) {
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return list;
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
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			YHHrStaffLeave staffLeave = (YHHrStaffLeave) orm.loadObjSingle(dbConn, YHHrStaffLeave.class, seqId);
			String dbAttachId = "";
			String dbAttachName = "";
			if (staffLeave != null) {
				dbAttachId = YHUtility.null2Empty(staffLeave.getAttachmentId());
				dbAttachName = YHUtility.null2Empty(staffLeave.getAttachmentName());
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
					staffLeave.setAttachmentId(attachmentId.trim());
					staffLeave.setAttachmentName(attachmentName.trim());
					orm.updateSingle(dbConn, staffLeave);
					returnFlag = true;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return returnFlag;
	}

	public void setPersonDeptLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		try {
			YHPerson person2 = new YHPerson();
			person2.setSeqId(Integer.parseInt(fileForm.getParameter("leavePerson")));
//			if (!YHUtility.isNullorEmpty(fileForm.getParameter("leaveDept"))) {
				person2.setDeptId(0);
//			}
			person2.setNotLogin("1");
			orm.updateSingle(dbConn, person2);

			String personSeqId = fileForm.getParameter("leavePerson");
			YHHrStaffInfoLogic staffInfoLogic = new YHHrStaffInfoLogic();
			YHHrStaffInfo staffInfo = staffInfoLogic.getStaffInfoByUserIdLogic(dbConn, personSeqId);
			YHPerson staffPerson = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, Integer.parseInt(personSeqId));
			String perSonUserIdStr = "";
			if (staffPerson != null) {
				perSonUserIdStr = YHUtility.null2Empty(staffPerson.getUserId());
			}
			if (staffInfo == null) {
				staffInfo = new YHHrStaffInfo();
				staffInfo.setCreateUserId(person.getSeqId() + "");
				staffInfo.setCreateDeptId(person.getDeptId());
				staffInfo.setUserId(perSonUserIdStr);
				staffInfo.setStaffName(perSonUserIdStr);
				if (!YHUtility.isNullorEmpty(fileForm.getParameter("leaveDept"))) {
					staffInfo.setDeptId(Integer.parseInt(fileForm.getParameter("leaveDept")));
				}
				staffInfo.setStaffEName(fileForm.getParameter("leavePerson"));
				staffInfo.setWorkStatus(fileForm.getParameter("quitType"));
				orm.saveSingle(dbConn, staffInfo);
			} else {
				if (!YHUtility.isNullorEmpty(fileForm.getParameter("leaveDept"))) {
					staffInfo.setDeptId(Integer.parseInt(fileForm.getParameter("leaveDept")));
				}
				staffInfo.setWorkStatus(fileForm.getParameter("quitType"));
				orm.updateSingle(dbConn, staffInfo);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	 /**
   * 查看领用物品
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getSelectGoods(Connection dbConn, Map request, String SeqId) throws Exception {
    try {
      String sql = " select p1.PRO_NAME,t1.TRANS_QTY,p1.PRO_UNIT,t1.TRANS_FLAG,t1.TRANS_DATE "
                 + " from oa_office_trans_records t1 "
                 + " join oa_office_goods p1 on t1.PRO_ID = p1.SEQ_ID "
                 + " where t1.BORROWER =" + SeqId;
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 新建员工离职(人事档案页面调用“离职”页面)
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void addStaffLeaveInfoByIdLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String leavePerson = fileForm.getParameter("leavePerson");
    String position = fileForm.getParameter("position");
    String quitType = fileForm.getParameter("quitType");
    String applicationDateStr = fileForm.getParameter("applicationDate");
    String quitTimePlanStr = fileForm.getParameter("quitTimePlan");
    String quitTimeFactStr = fileForm.getParameter("quitTimeFact");
    String lastSalaryTimeStr = fileForm.getParameter("lastSalaryTime");
    String leaveDeptStr = fileForm.getParameter("leaveDept");
    String trace = fileForm.getParameter("trace");
    String materialsCondition = fileForm.getParameter("materialsCondition");
    String remark = fileForm.getParameter("remark");
    String quitReason = fileForm.getParameter("quitReason");
    String smsRemind = fileForm.getParameter("smsRemind");
    String smsRemind1 = fileForm.getParameter("smsRemind1");
    try {
      int newDeptId = 0;
      String newUserName = "";
      int leaveDept = 0;
      if (!YHUtility.isNullorEmpty(leaveDeptStr)) {
        leaveDept = Integer.parseInt(leaveDeptStr);
      }

      Map filters = new HashMap();
      filters.put("SEQ_ID", YHUtility.null2Empty(leavePerson));
      YHPerson leaveUser = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, filters);
      if (!YHUtility.isNullorEmpty(leavePerson)) {
        if (leaveUser != null) {
          newDeptId = leaveUser.getDeptId();
          newUserName = YHUtility.null2Empty(leaveUser.getUserName());
        }
      }

      if (!YHUtility.isNullorEmpty(quitReason.trim())) {
        Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
        boolean attachFlag = (Boolean) map.get("attachFlag");
        String attachmentIds = (String) map.get("attachmentIds");
        String attachmentNames = (String) map.get("attachmentNames");

        YHHrStaffLeave staffLeave = new YHHrStaffLeave();
        staffLeave.setCreateUserId(String.valueOf(person.getSeqId()));
        staffLeave.setCreateDeptId(person.getDeptId());
        if (!YHUtility.isNullorEmpty(quitTimePlanStr)) {
          staffLeave.setQuitTimePlan(YHUtility.parseDate("yyyy-MM-dd", quitTimePlanStr));
        }
        staffLeave.setQuitType(quitType);
        staffLeave.setQuitReason(quitReason);
        if (!YHUtility.isNullorEmpty(lastSalaryTimeStr)) {
          staffLeave.setLastSalaryTime(YHUtility.parseDate("yyyy-MM-dd", lastSalaryTimeStr));
        }
        staffLeave.setTrace(trace);
        staffLeave.setRemark(remark);
        if (!YHUtility.isNullorEmpty(quitTimeFactStr)) {
          staffLeave.setQuitTimeFact(YHUtility.parseDate("yyyy-MM-dd", quitTimeFactStr));
        }
        staffLeave.setLeavePerson(leavePerson);
        staffLeave.setMaterialsCondition(materialsCondition);
        staffLeave.setPosition(position);
        staffLeave.setAttachmentId(attachmentIds);
        staffLeave.setAttachmentName(attachmentNames);
        if (!YHUtility.isNullorEmpty(applicationDateStr)) {
          staffLeave.setApplicationDate(YHUtility.parseDate("yyyy-MM-dd", applicationDateStr));
        }
        staffLeave.setLeaveDept(String.valueOf(leaveDept));
        staffLeave.setAddTime(new Date());
        orm.saveSingle(dbConn, staffLeave);

        String leaveUserId = "";
        if (leaveUser != null) {
          leaveUser.setDeptId(0);
          leaveUser.setNotLogin("1");
          orm.updateSingle(dbConn, leaveUser);
          leaveUserId = YHUtility.null2Empty(leaveUser.getUserId());
        }
        int workStatus = 0;
        if (!YHUtility.isNullorEmpty(quitType)) {
          workStatus = Integer.parseInt(quitType) + 1;
        }
        Map filters2 = new HashMap();
        filters2.put("USER_ID", YHUtility.null2Empty(leaveUserId));
        YHHrStaffInfo staffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, filters2);
        if (staffInfo != null) {
          staffInfo.setDeptId(leaveDept);
          staffInfo.setWorkStatus(String.valueOf(workStatus));
          orm.updateSingle(dbConn, staffInfo);

        } else {
           staffInfo = new YHHrStaffInfo();
          int createUserId = person.getSeqId();
          staffInfo.setCreateUserId(String.valueOf(createUserId));
          staffInfo.setCreateDeptId(person.getDeptId());
          staffInfo.setUserId(leavePerson);
          staffInfo.setDeptId(leaveDept);
          staffInfo.setStaffName(newUserName);
          staffInfo.setWorkStatus(String.valueOf(workStatus));
          orm.saveSingle(dbConn, staffInfo);
        }
        int maxSeqId = this.getMaxSeqId(dbConn);
        String remindUrl = "/subsys/oa/hr/manage/staffLeave/detail.jsp?seqId=" + maxSeqId + "&openFlag=1&openWidth=860&openHeight=650";
        String smsContent = "请查看员工离职信息！";
        // 短信提醒
        if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
          this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), smsRemind1, "64", remindUrl, new Date());
        }
      }

    } catch (Exception e) {
      throw e;
    }
  }
  
  
  
  
}

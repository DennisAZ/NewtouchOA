package yh.subsys.oa.hr.manage.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
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
import yh.subsys.oa.hr.manage.data.YHHrStaffIncentive;
import yh.subsys.oa.hr.setting.act.YHHrSetOtherAct;

public class YHHrStaffIncentiveLogic {
	private static Logger log = Logger.getLogger(YHHrStaffIncentiveLogic.class);

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
	 * 新建奖惩信息
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setNewStaffincentiveValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String staffNameStr = fileForm.getParameter("staffName");
		String incentiveItem = fileForm.getParameter("incentiveItem");
		String incentiveTimeStr = fileForm.getParameter("incentiveTime");
		String salaryMonth = fileForm.getParameter("salaryMonth");
		String incentiveType = fileForm.getParameter("incentiveType");
		String incentiveAmountStr = fileForm.getParameter("incentiveAmount");
		String remark = fileForm.getParameter("remark");
		String incentiveDescription = fileForm.getParameter("incentiveDescription");
		String addScoreStr = fileForm.getParameter("addScore");
		String reduceScoreStr = fileForm.getParameter("reduceScore");
		String yearScoreStr = fileForm.getParameter("yearScore");
		String smsRemind = fileForm.getParameter("smsRemind");
		String sms2Remind = fileForm.getParameter("sms2Remind");

		Date incentiveTime = YHUtility.parseDate("yyyy-MM-dd", incentiveTimeStr);
		double incentiveAmount = 0;
		double yearScore = 0;
		if (YHUtility.isNumber(incentiveAmountStr)) {
			incentiveAmount = Double.parseDouble(incentiveAmountStr);
		}
		if (YHUtility.isNumber(yearScoreStr)) {
			yearScore = Double.parseDouble(yearScoreStr);
		}
		Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
		boolean attachFlag = (Boolean) map.get("attachFlag");
		String attachmentIds = (String) map.get("attachmentIds");
		String attachmentNames = (String) map.get("attachmentNames");

		double addScore = 0;
		double reduceScore = 0;
		if (YHUtility.isNumber(addScoreStr) && "1".equals(incentiveType.trim())) {
			addScore = Double.parseDouble(addScoreStr);
		}
		if (YHUtility.isNumber(reduceScoreStr) && "2".equals(incentiveType.trim())) {
			reduceScore = Double.parseDouble(reduceScoreStr);
		}
		try {
			String[] staffNameArry = staffNameStr.split(",");
			if (staffNameArry != null && staffNameArry.length > 0) {
				for (String staffName : staffNameArry) {
					YHHrStaffIncentive staffIncentive = new YHHrStaffIncentive();
					staffIncentive.setStaffName(staffName);
					staffIncentive.setIncentiveItem(incentiveItem);
					staffIncentive.setIncentiveTime(incentiveTime);
					staffIncentive.setSalaryMonth(salaryMonth);
					staffIncentive.setIncentiveType(incentiveType);
					staffIncentive.setIncentiveAmount(incentiveAmount);
					staffIncentive.setRemark(remark);
					staffIncentive.setAddTime(YHUtility.parseTimeStamp());
					staffIncentive.setIncentiveDescription(incentiveDescription);
					staffIncentive.setCreateUserId(String.valueOf(person.getSeqId()));
					staffIncentive.setCreateDeptId(person.getDeptId());
					staffIncentive.setAddScore(addScore);
					staffIncentive.setReduceScore(reduceScore);
					staffIncentive.setYearScore(yearScore);
					if (attachFlag) {
						staffIncentive.setAttachmentId(attachmentIds);
						staffIncentive.setAttachmentName(attachmentNames);
					}
					orm.saveSingle(dbConn, staffIncentive);
					int maxSeqId = this.getMaxSeqId(dbConn);
					String incentiveTypeName = "";
					if ("1".equals(incentiveType.trim())) {
						incentiveTypeName = "奖励";
					}
					if ("2".equals(incentiveType.trim())) {
						incentiveTypeName = "惩罚";
					}
					YHMobileSms2Logic sbl = new YHMobileSms2Logic();
					String remindUrl = "/subsys/oa/hr/manage/staffIncentive/incentiveDetail.jsp?seqId=" + maxSeqId + "&openFlag=1&openWidth=860&openHeight=650";
					String smsContent = "请查看" + incentiveTypeName + "信息！";
					// 短信提醒
					if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
						this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), staffNameStr, "58", remindUrl, new Date());
					}
					// 手机提醒
					if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
						smsContent = "OA奖惩管理:" + incentiveTypeName + " " + getUserNameLogic(dbConn, staffNameStr) + " " + incentiveAmount;
						sbl.remindByMobileSms(dbConn, staffNameStr, person.getSeqId(), smsContent, new Date());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 奖惩信息 通用列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getStaffincentiveJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
		try {
	    String deptIdStr = getHrManagerPriv(dbConn, person);
		  
			String sql1 = " select  SEQ_ID, STAFF_NAME, INCENTIVE_ITEM, INCENTIVE_TIME, INCENTIVE_TYPE, INCENTIVE_AMOUNT "
					       + " from oa_pm_employee_encouragement " 
					       + " where CREATE_USER_ID = "+ person.getSeqId()
					       + " or CREATE_DEPT_ID in "+ deptIdStr
					       + " ORDER BY SEQ_ID desc ";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql1);
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
					YHHrStaffIncentive staffIncentive = (YHHrStaffIncentive) orm.loadObjSingle(dbConn, YHHrStaffIncentive.class, Integer.parseInt(seqId));
					String attachmentId = YHUtility.null2Empty(staffIncentive.getAttachmentId());
					String attachmentName = YHUtility.null2Empty(staffIncentive.getAttachmentName());
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
					orm.deleteSingle(dbConn, staffIncentive);
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
	public YHHrStaffIncentive getIncentiveDetailLogic(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHHrStaffIncentive) orm.loadObjSingle(conn, YHHrStaffIncentive.class, seqId);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 奖惩信息查询
	 * 
	 * @param dbConn
	 * @param request
	 * @param map
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String queryIncentiveListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
	  
	  String deptIdStr = getHrManagerPriv(dbConn, person);
    
		String staffName = (String) map.get("staffName");
		String incentiveTime1 = (String) map.get("incentiveTime1");
		String incentiveTime2 = (String) map.get("incentiveTime2");
		String incentiveItem = (String) map.get("incentiveItem");
		String incentiveType = (String) map.get("incentiveType");
		String conditionStr = "";
		String sql1 = "";
		try {
			if (!YHUtility.isNullorEmpty(staffName)) {
				conditionStr = " and STAFF_NAME ='" + YHDBUtility.escapeLike(staffName) + "'";
			}
			if (!YHUtility.isNullorEmpty(incentiveItem)) {
				conditionStr += " and INCENTIVE_ITEM ='" + YHDBUtility.escapeLike(incentiveItem) + "'";
			}
			if (!YHUtility.isNullorEmpty(incentiveType)) {
				conditionStr += " and INCENTIVE_TYPE ='" + YHDBUtility.escapeLike(incentiveType) + "'";
			}
			if (!YHUtility.isNullorEmpty(incentiveTime1)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("INCENTIVE_TIME", incentiveTime1, ">=");
			}
			if (!YHUtility.isNullorEmpty(incentiveTime2)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("INCENTIVE_TIME", incentiveTime2, "<=");
			}
			sql1 = " select SEQ_ID, STAFF_NAME, INCENTIVE_ITEM, INCENTIVE_TIME, INCENTIVE_TYPE, INCENTIVE_AMOUNT"
					+ " from oa_pm_employee_encouragement " 
			    +	" where (CREATE_USER_ID = "+ person.getSeqId()
          + " or CREATE_DEPT_ID in "+ deptIdStr + ")" 
			    + conditionStr 
			    + " ORDER BY ADD_TIME desc";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql1);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取分值
	 * 
	 * @param dbConn
	 * @param year
	 * @param month
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public double getScoreLogic(Connection dbConn, String year, String month, String userIdStr) throws Exception {
		if (YHUtility.isNullorEmpty(userIdStr)) {
			userIdStr = "0";
		}
		if (userIdStr.endsWith(",")) {
			userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
		}
		String ymd = "";
		if (YHUtility.isNullorEmpty(year)) {
			ymd = year + "-" + month + "-" + "07";
		} else {
			ymd = year + "-" + month + "-" + "07";
		}
		double addScore = 0;
		double reduceScore = 0;
		double score = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select ADD_SCORE,REDUCE_SCORE from oa_pm_employee_encouragement where STAFF_NAME in(" + userIdStr + ") and "
					+ YHDBUtility.getMonthFilter("INCENTIVE_TIME", YHUtility.parseDate(ymd));
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				addScore += rs.getDouble("ADD_SCORE");
				reduceScore += rs.getDouble("REDUCE_SCORE");
			}
			score = addScore - reduceScore;
			return score;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 编辑奖惩信息
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void updateIncentiveInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String seqIdStr = fileForm.getParameter("seqId");
		String staffNameStr = fileForm.getParameter("staffName");
		String incentiveItem = fileForm.getParameter("incentiveItem");
		String incentiveTimeStr = fileForm.getParameter("incentiveTime");
		String salaryMonth = fileForm.getParameter("salaryMonth");
		String incentiveType = fileForm.getParameter("incentiveType");
		String incentiveAmountStr = fileForm.getParameter("incentiveAmount");
		String remark = fileForm.getParameter("remark");
		String incentiveDescription = fileForm.getParameter("incentiveDescription");
		String addScoreStr = fileForm.getParameter("addScore");
		String reduceScoreStr = fileForm.getParameter("reduceScore");
		String yearScoreStr = fileForm.getParameter("yearScore");
		String smsRemind = fileForm.getParameter("smsRemind");
		String sms2Remind = fileForm.getParameter("sms2Remind");

		Date incentiveTime = YHUtility.parseDate("yyyy-MM-dd", incentiveTimeStr);
		double incentiveAmount = 0;
		double yearScore = 0;
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (YHUtility.isNumber(incentiveAmountStr)) {
			incentiveAmount = Double.parseDouble(incentiveAmountStr);
		}
		if (YHUtility.isNumber(yearScoreStr)) {
			yearScore = Double.parseDouble(yearScoreStr);
		}
		Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
		boolean attachFlag = (Boolean) map.get("attachFlag");
		String attachmentIds = (String) map.get("attachmentIds");
		String attachmentNames = (String) map.get("attachmentNames");
		double addScore = 0;
		double reduceScore = 0;
		if (YHUtility.isNumber(addScoreStr) && "1".equals(incentiveType.trim())) {
			addScore = Double.parseDouble(addScoreStr);
		}
		if (YHUtility.isNumber(reduceScoreStr) && "2".equals(incentiveType.trim())) {
			reduceScore = Double.parseDouble(reduceScoreStr);
		}
		
		try {
			YHHrStaffIncentive staffIncentive = (YHHrStaffIncentive) orm.loadObjSingle(dbConn, YHHrStaffIncentive.class, seqId);
			if (staffIncentive != null) {
				String dbAttachId = YHUtility.null2Empty(staffIncentive.getAttachmentId());
				String dbAttachName = YHUtility.null2Empty(staffIncentive.getAttachmentName());
				staffIncentive.setStaffName(staffNameStr);
				staffIncentive.setIncentiveItem(incentiveItem);
				staffIncentive.setIncentiveTime(incentiveTime);
				staffIncentive.setSalaryMonth(salaryMonth);
				staffIncentive.setIncentiveType(incentiveType);
				staffIncentive.setIncentiveAmount(incentiveAmount);
				staffIncentive.setRemark(remark);
				staffIncentive.setAddTime(YHUtility.parseTimeStamp());
				staffIncentive.setIncentiveDescription(incentiveDescription);
				staffIncentive.setCreateUserId(String.valueOf(person.getSeqId()));
				staffIncentive.setAddScore(addScore);
				staffIncentive.setReduceScore(reduceScore);
				staffIncentive.setYearScore(yearScore);
				if (attachFlag) {
					staffIncentive.setAttachmentId(dbAttachId.trim() + attachmentIds.trim());
					staffIncentive.setAttachmentName(dbAttachName.trim() + attachmentNames.trim());
				}
				orm.updateSingle(dbConn, staffIncentive);
				String incentiveTypeName = "";
				if ("1".equals(incentiveType.trim())) {
					incentiveTypeName = "奖励";
				}
				if ("2".equals(incentiveType.trim())) {
					incentiveTypeName = "惩罚";
				}
				YHMobileSms2Logic sbl = new YHMobileSms2Logic();
				String remindUrl = "/subsys/oa/hr/manage/staffIncentive/incentiveDetail.jsp?seqId=" + seqId + "&openFlag=1&openWidth=860&openHeight=650";
				String smsContent = "请查看" + incentiveTypeName + "信息！";
				// 短信提醒
				if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
					this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), staffNameStr, "58", remindUrl, new Date());
				}
				// 手机提醒
				if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
					smsContent = "OA奖惩管理:" + incentiveTypeName + " " + getUserNameLogic(dbConn, staffNameStr) + " " + incentiveAmount;
					sbl.remindByMobileSms(dbConn, staffNameStr, person.getSeqId(), smsContent, new Date());
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
	 * 获取最大的SeqId值
	 * 
	 * @param dbConn
	 * @return
	 */
	public int getMaxSeqId(Connection dbConn) {
		String sql = "select SEQ_ID from oa_pm_employee_encouragement where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_employee_encouragement )";
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
	 * 返回两个日期的相隔月份
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
			YHHrStaffIncentive staffIncentive = (YHHrStaffIncentive) orm.loadObjSingle(dbConn, YHHrStaffIncentive.class, seqId);
			String dbAttachId = "";
			String dbAttachName = "";
			if (staffIncentive != null) {
				dbAttachId = YHUtility.null2Empty(staffIncentive.getAttachmentId());
				dbAttachName = YHUtility.null2Empty(staffIncentive.getAttachmentName());
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
					staffIncentive.setAttachmentId(attachmentId.trim());
					staffIncentive.setAttachmentName(attachmentName.trim());
					orm.updateSingle(dbConn, staffIncentive);
					returnFlag = true;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return returnFlag;
	}

	public String getHrManagerPriv(Connection dbConn, YHPerson person) throws SQLException{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = " select DEPT_ID from oa_pm_manager where "+YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "DEPT_HR_MANAGER");
    String deptIdStr = "";
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        deptIdStr = deptIdStr + rs.getInt("DEPT_ID")+",";
      }
      if(deptIdStr.length() > 0){
        deptIdStr = "("+deptIdStr.substring(0, deptIdStr.length()-1)+")";
      }
      else{
        deptIdStr = "(0)";
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return deptIdStr;
	}
}

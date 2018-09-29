package yh.subsys.oa.hr.manage.staff_license.logic;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.hr.manage.hrIdtransName.hrPublicIdTransName;
import yh.subsys.oa.hr.manage.logic.YHHrStaffIncentiveLogic;
import yh.subsys.oa.hr.manage.staffWorkExperience.data.YHHrStaffWorkExperience;
import yh.subsys.oa.hr.manage.staff_license.act.YHNewLicenseInfoAct;
import yh.subsys.oa.hr.manage.staff_license.data.YHHrStaffLicense;

public class YHNewLicenseInfoLogic {
	public static int newLicenseInfo(Connection conn, YHPerson person,
			YHHrStaffLicense license) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String sql = "insert into oa_pm_employee_certificate(CREATE_USER_ID,CREATE_DEPT_ID,STAFF_NAME,LICENSE_TYPE,LICENSE_NO,LICENSE_NAME,NOTIFIED_BODY,GET_LICENSE_DATE"
				+ ",EFFECTIVE_DATE,EXPIRATION_PERIOD,EXPIRE_DATE,STATUS,ATTACHMENT_ID,ATTACHMENT_NAME,REMARK,ADD_TIME) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, license.getCreateUserId());
			ps.setInt(2, license.getCreateDeptId());
			ps.setString(3, license.getStaffName());
			ps.setString(4, license.getLicenseType());
			ps.setString(5, license.getLicenseNo());
			ps.setString(6, license.getLicenseName());
			ps.setString(7, license.getNotifiedBody());
			ps.setDate(8, new java.sql.Date(
					license.getGetLicenseDate() == null ? 0 : license
							.getGetLicenseDate().getTime()));
			ps.setDate(9, new java.sql.Date(
					license.getEffectiveDate() == null ? 0 : license
							.getEffectiveDate().getTime()));
			ps.setString(10, license.getExpirationPeriod());
			ps.setDate(11, new java.sql.Date(
					license.getExpireDate() == null ? 0 : license
							.getExpireDate().getTime()));
			ps.setString(12, license.getStatus());
			ps.setString(13, license.getAttachmentId());
			ps.setString(14, license.getAttachmentName());
			ps.setString(15, license.getRemark());
			ps.setString(16, license.getAddTime());
			ok = ps.executeUpdate();

		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}

		return ok;
	}
  /**
   * 添加附件上传
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
	public void setNewLicenseInfoValueLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String userName = fileForm.getParameter("userName");
		String userId = fileForm.getParameter("userId");
		
		String smsRemind = fileForm.getParameter("SMS_REMIND");//短信提醒
		String licenseType = fileForm.getParameter("licenseType");
		String licenseNo = fileForm.getParameter("LICENSE_NO");
		String licenseName = fileForm.getParameter("LICENSE_NAME");//
		String getLicenseDate = fileForm.getParameter("GET_LICENSE_DATE");// 取证日期
		String effectiveDate = fileForm.getParameter("EFFECTIVE_DATE"); // 生效日期
		String licenseState = fileForm.getParameter("licenseState");// 证照状态
		String expirationPeriod = fileForm.getParameter("EXPIRATION_PERIOD");//期限限制
		
		String expireDate = fileForm.getParameter("EXPIRE_DATE"); // 到期日期
		String remindTime = fileForm.getParameter("REMIND_TIME");// 提醒日期
		String notifiedBody = fileForm.getParameter("NOTIFIED_BODY"); // 发证机构
		String remark = fileForm.getParameter("REMARK");// 备注
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");

			YHHrStaffLicense license = new YHHrStaffLicense();
			license.setStaffName(userId);
			license.setLicenseType(licenseType);
			license.setLicenseNo(licenseNo);
			license.setLicenseName(licenseName);
			if (!YHUtility.isNullorEmpty(getLicenseDate)) {
				license.setGetLicenseDate(YHUtility.parseDate("yyyy-MM-dd",
						getLicenseDate));
			}
			if (!YHUtility.isNullorEmpty(effectiveDate)) {
				license.setEffectiveDate(YHUtility.parseDate("yyyy-MM-dd",
						effectiveDate));
			}
			license.setStatus(licenseState);
			license.setExpirationPeriod(expirationPeriod);
			if (!YHUtility.isNullorEmpty(expireDate)) {
				license.setExpireDate(YHUtility.parseDate("yyyy-MM-dd",
						expireDate));
			}
			
			Date currentTime = new Date();    
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		    String dateString = formatter.format(currentTime); //系统登录时间	
		    license.setAddTime(dateString);
			//license.setAddTime(remindTime);// 
			license.setNotifiedBody(notifiedBody);
			license.setRemark(remark);

			if (attachFlag) {
				license.setAttachmentId(attachmentIds);
				license.setAttachmentName(attachmentNames);
			}

			license.setCreateUserId(String.valueOf(person.getSeqId()));
			license.setCreateDeptId(person.getDeptId());
			orm.saveSingle(dbConn, license);
			String licenseInfo = userName +" 证照信息已到期！";
			String licensePerson = userName +" 新建了一份证照！";
			if(!YHUtility.isNullorEmpty(smsRemind)){
			   YHNewLicenseInfoLogic.sendSms(person, dbConn, licensePerson, null, userId, null);
			}
			if(!YHUtility.isNullorEmpty(remindTime)){
				
			  YHNewLicenseInfoLogic.sendSms(person, dbConn, licenseInfo, null, userId,YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", remindTime));
			}
		} catch (Exception e) {
			throw e;
		}
         
	}
	/**
	 * 修改附件上传
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setUpLicenseInfoValueLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String smsRemind = fileForm.getParameter("SMS_REMIND");//短信提醒
		String seqid = fileForm.getParameter("seqid");
		String userName = fileForm.getParameter("userName");
		String userId = fileForm.getParameter("userId");
		String licenseType = fileForm.getParameter("licenseType");
		String licenseNo = fileForm.getParameter("LICENSE_NO");
		String licenseName = fileForm.getParameter("LICENSE_NAME");//
		String getLicenseDate = fileForm.getParameter("GET_LICENSE_DATE");// 取证日志
		String effectiveDate = fileForm.getParameter("EFFECTIVE_DATE"); // 生效日期
		String licenseState = fileForm.getParameter("licenseState");// 证照状态
		String expireDate = fileForm.getParameter("EXPIRE_DATE"); // 到期日期
		String remindTime = fileForm.getParameter("REMIND_TIME");// 提醒日期
		String notifiedBody = fileForm.getParameter("NOTIFIED_BODY"); // 发证机构
		String remark = fileForm.getParameter("REMARK");// 备注
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");
			YHHrStaffLicense license = new YHHrStaffLicense();
			license.setSeqId(Integer.valueOf(seqid));
			license.setStaffName(userId);
			license.setLicenseType(licenseType);
			license.setLicenseNo(licenseNo);
			license.setLicenseName(licenseName);
			if (!YHUtility.isNullorEmpty(getLicenseDate)) {
				license.setGetLicenseDate(YHUtility.parseDate("yyyy-MM-dd",
						getLicenseDate));
			}
			if (!YHUtility.isNullorEmpty(effectiveDate)) {
				license.setEffectiveDate(YHUtility.parseDate("yyyy-MM-dd",
						effectiveDate));
			}
			license.setStatus(licenseState);
			if (!YHUtility.isNullorEmpty(expireDate)) {
				license
						.setExpireDate(YHUtility
								.parseDate("yyyy-MM-dd", expireDate));
			}
			Date currentTime = new Date();    
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		    String dateString = formatter.format(currentTime); //系统登录时间	
		    license.setAddTime(dateString);//系统当前时间
			//license.setAddTime(remindTime);// 提醒日期
			license.setNotifiedBody(notifiedBody);
			license.setRemark(remark);
			if (attachFlag) {
				license.setAttachmentId(attachmentIds);
				license.setAttachmentName(attachmentNames);
			}

      license.setCreateUserId(String.valueOf(person.getSeqId()));
			license.setCreateDeptId(person.getDeptId());
			orm.updateSingle(dbConn, license);

			String licenseInfo = userName +" 证照信息已到期！";
			String licensePerson = userName +" 修改了一份证照！";
			if(!YHUtility.isNullorEmpty(smsRemind)){
			   YHNewLicenseInfoLogic.sendSms(person, dbConn, licensePerson, null, userId, null);
			}
			if(!YHUtility.isNullorEmpty(remindTime)){
				
			  YHNewLicenseInfoLogic.sendSms(person, dbConn, licenseInfo, null, userId,YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", remindTime));
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
	public Map<Object, Object> fileUploadLogic(YHFileUploadForm fileForm,
			String attachmentFolder) throws Exception {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm,
					attachmentFolder);
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
			String filePath = YHSysProps.getAttachPath() + separator
					+ attachmentFolder + separator + currDate;

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
	 * 修改证照的信息
	 * 
	 * @param conn
	 * @param person
	 * @param license
	 * @return
	 * @throws Exception
	 */
	public static int updateLicenseInfo(Connection conn, YHPerson person,
			YHHrStaffLicense license) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String updateSql = "update oa_pm_employee_certificate set STAFF_NAME=?,LICENSE_TYPE=?,LICENSE_NO=?,LICENSE_NAME=?,NOTIFIED_BODY=?,GET_LICENSE_DATE=?"
				+ ",EFFECTIVE_DATE=?,EXPIRATION_PERIOD=?,EXPIRE_DATE=?,STATUS=?,ATTACHMENT_ID=?,ATTACHMENT_NAME=?,REMARK=?,ADD_TIME=? where SEQ_ID=?";
		try {
			ps = conn.prepareStatement(updateSql);
			ps.setString(1, license.getStaffName());
			ps.setString(2, license.getLicenseType());
			ps.setString(3, license.getLicenseNo());
			ps.setString(4, license.getLicenseName());
			ps.setString(5, license.getNotifiedBody());
			ps.setDate(6, new java.sql.Date(
					license.getGetLicenseDate() == null ? 0 : license
							.getGetLicenseDate().getTime()));
			ps.setDate(7, new java.sql.Date(
					license.getEffectiveDate() == null ? 0 : license
							.getEffectiveDate().getTime()));
			ps.setString(8, license.getExpirationPeriod());
			ps.setDate(9, new java.sql.Date(license.getExpireDate() == null ? 0
					: license.getExpireDate().getTime()));
			ps.setString(10, license.getStatus());
			ps.setString(11, license.getAttachmentId());
			ps.setString(12, license.getAttachmentName());
			ps.setString(13, license.getRemark());
			ps.setString(14, license.getAddTime());
			ps.setInt(15, license.getSeqId());
			ok = ps.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}

		return ok;
	}

	/*
	 * 查询共有的条数
	 */
	public int count(Connection dbConn, YHPerson user) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	  
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select COUNT(*) from oa_pm_employee_certificate"
               + " where CREATE_USER_ID = "+ user.getSeqId()
               + " or CREATE_DEPT_ID in "+ deptIdStr;

		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return 0;

	}
	/*
	 * 查询共有的条数
	 */
	public int licenseCount(Connection dbConn, YHPerson user,YHHrStaffLicense license,String expireDate1,String expireDate2) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	  
		PreparedStatement ps = null;
		ResultSet rs = null;
		String conditionStr = "";
		try {
			if (!YHUtility.isNullorEmpty(license.getStaffName())) {
				conditionStr = " and STAFF_NAME ='"
						+ YHDBUtility.escapeLike(license.getStaffName()) + "'";
			}

			if (!YHUtility.isNullorEmpty(license.getLicenseType())) {
				conditionStr += " and LICENSE_TYPE ='"
						+ YHDBUtility.escapeLike(license.getLicenseType())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(license.getLicenseNo())) {
				conditionStr += " and LICENSE_NO ='"
						+ YHDBUtility.escapeLike(license.getLicenseNo()) + "'";
			}
			if (!YHUtility.isNullorEmpty(license.getLicenseName())) {
				conditionStr += " and LICENSE_NAME ='"
						+ YHDBUtility.escapeLike(license.getLicenseName())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(license.getStatus())) {
				conditionStr += " and STATUS ='"
						+ YHDBUtility.escapeLike(license.getStatus()) + "'";
			}

			if (!YHUtility.isNullorEmpty(expireDate1)) {
				conditionStr += " and "
						+ YHDBUtility.getDateFilter("EXPIRE_DATE", expireDate1,
								">=");
			}
			if (!YHUtility.isNullorEmpty(expireDate2)) {
				conditionStr += " and "
						+ YHDBUtility.getDateFilter("EXPIRE_DATE",
								expireDate2, "<=");
			}
			String sql = " select COUNT(*) from oa_pm_employee_certificate " 
                 + " where (CREATE_USER_ID = "+ user.getSeqId()
                 + " or CREATE_DEPT_ID in "+ deptIdStr + ")"
      				 	 + conditionStr;

			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return 0;

	}
	/**
	 * 证照查询带分页的
	 * 
	 * @param dbConn
	 * @param user
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<YHHrStaffLicense> findLicenses(Connection dbConn, YHPerson user, YHPage page) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
    
		PreparedStatement ps = null;
		ResultSet rs = null;
		// String sql =
		// "select SEQ_ID, STAFF_NAME,STAFF_CONTRACT_NO,CONTRACT_TYPE,MAKE_CONTRACT,STATUS from hr_staff_contract where 1=1";
		String sql = "select SEQ_ID, STAFF_NAME,LICENSE_TYPE,LICENSE_NO,LICENSE_NAME,STATUS from oa_pm_employee_certificate " 
               + " where CREATE_USER_ID = "+ user.getSeqId()
               + " or CREATE_DEPT_ID in "+ deptIdStr
               + " order by SEQ_ID desc";
		List<YHHrStaffLicense> license = new ArrayList<YHHrStaffLicense>();
		try {
			ps = dbConn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());// 游标用法，预显示最大行数
			rs = ps.executeQuery();
			rs.first();// 如指针用法，指向最上面位置
			rs.relative((page.getCurrentPageIndex() - 1) * page.getPageSize()
					- 1);// 相对于求出每页显示的长度
			while (rs.next()) {
				YHHrStaffLicense licenseC = new YHHrStaffLicense();
				licenseC.setSeqId(rs.getInt("SEQ_ID"));
				licenseC.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
				licenseC.setLicenseType(hrPublicIdTransName.getCodeUserName(dbConn, rs.getString("LICENSE_TYPE")));
				licenseC.setLicenseNo(rs.getString("LICENSE_NO"));
				licenseC.setLicenseName(rs.getString("LICENSE_NAME"));
				licenseC.setStatus(hrPublicIdTransName.getCodeUserName(dbConn, rs.getString("STATUS")));
				license.add(licenseC);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return license;
	}

	/**
	 * 查询证照详细信息
	 * 
	 * @param dbConn
	 * @param user
	 * @param ContractSeqId
	 * @return
	 * @throws Exception
	 */

	public List<YHHrStaffLicense> licenseXxInfo(Connection dbConn, YHPerson user,
			int licenseSeqId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,STAFF_NAME,LICENSE_TYPE,LICENSE_NO,LICENSE_NAME,NOTIFIED_BODY,GET_LICENSE_DATE"
				+ ",EFFECTIVE_DATE,EXPIRATION_PERIOD,EXPIRE_DATE,STATUS,ATTACHMENT_ID,ATTACHMENT_NAME,REMARK,ADD_TIME from oa_pm_employee_certificate where SEQ_ID="
				+ licenseSeqId;

		List<YHHrStaffLicense> license = new ArrayList<YHHrStaffLicense>();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				YHHrStaffLicense lcen = new YHHrStaffLicense();
				lcen.setSeqId(rs.getInt("SEQ_ID"));
				lcen.setStaffName(rs.getString("STAFF_NAME"));
				lcen.setLicenseType(rs.getString("LICENSE_TYPE"));
				lcen.setLicenseNo(rs.getString("LICENSE_NO"));
				lcen.setLicenseName(rs.getString("LICENSE_NAME"));
				lcen.setNotifiedBody(rs.getString("NOTIFIED_BODY"));
				lcen.setGetLicenseDate(rs.getDate("GET_LICENSE_DATE"));
				lcen.setEffectiveDate(rs.getDate("EFFECTIVE_DATE"));
				lcen.setExpirationPeriod(rs.getString("EXPIRATION_PERIOD"));
				lcen.setExpireDate(rs.getDate("EXPIRE_DATE"));
				lcen.setStatus(rs.getString("STATUS"));
				lcen.setAttachmentId(rs.getString("ATTACHMENT_ID"));
				lcen.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
				lcen.setRemark(rs.getString("REMARK"));
				lcen.setAddTime(rs.getString("ADD_TIME"));

				license.add(lcen);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return license;
	}

	/**
	 * 删除证照单条信息
	 * 
	 * @param conn
	 * @param person
	 * @param noHiddenId
	 * @return
	 * @throws Exception
	 */
	public static int delLicenseInfo(Connection conn, YHPerson person,
			int noHiddenId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String deleteSql = "delete from oa_pm_employee_certificate where seq_id ="
				+ noHiddenId;

		try {
			ps = conn.prepareStatement(deleteSql);
			ok = ps.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}

		return ok;
	}

	/**
	 * 删除证照所选的信息
	 * 
	 * @param dbConn
	 * @param loginperson
	 * @param deleteStr
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public int deleteLicenseInfo(Connection dbConn, String deleteStr)
			throws IOException, Exception {
		String deletenewsCommentSql = "";
		String deletenewsSql = "";
		Statement stmt = null;
		ResultSet rs = null;

		if (!YHUtility.isNullorEmpty(deleteStr)) {
			String[] deleteStrs = deleteStr.split(",");
			deleteStr = "";
			for (int i = 0; i < deleteStrs.length; i++) {
				deleteStr += deleteStrs[i] + ",";
			}
			deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
		}
		int success = 0;
		try {
			stmt = dbConn.createStatement();
			deletenewsCommentSql = "delete  from oa_pm_employee_certificate  where seq_id in  ("
					+ deleteStr + ")";
			success = stmt.executeUpdate(deletenewsCommentSql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return success;
	}

	/**
	 * 证照信息查询（第三个标签页）
	 * 
	 * @param dbConn
	 * @param user
	 * @param page
	 * @param contract
	 * @param makeContract1
	 * @param makeContract2
	 * @param trailOverTime1
	 * @param trailOverTime2
	 * @param contractEndTime1
	 * @param contractEndTime2
	 * @return
	 * @throws Exception
	 */
	public List<YHHrStaffLicense> queryLicenseInfo(Connection dbConn, YHPerson user,
			YHPage page, YHHrStaffLicense license, String expireDate1,
			String expireDate2) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String conditionStr = "";
		List<YHHrStaffLicense> licenses = new ArrayList<YHHrStaffLicense>();
		try {
			if (!YHUtility.isNullorEmpty(license.getStaffName())) {
				conditionStr = " and STAFF_NAME ='"
						+ YHDBUtility.escapeLike(license.getStaffName()) + "'";
			}

			if (!YHUtility.isNullorEmpty(license.getLicenseType())) {
				conditionStr += " and LICENSE_TYPE ='"
						+ YHDBUtility.escapeLike(license.getLicenseType())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(license.getLicenseNo())) {
				conditionStr += " and LICENSE_NO ='"
						+ YHDBUtility.escapeLike(license.getLicenseNo()) + "'";
			}
			if (!YHUtility.isNullorEmpty(license.getLicenseName())) {
				conditionStr += " and LICENSE_NAME ='"
						+ YHDBUtility.escapeLike(license.getLicenseName())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(license.getStatus())) {
				conditionStr += " and STATUS ='"
						+ YHDBUtility.escapeLike(license.getStatus()) + "'";
			}

			if (!YHUtility.isNullorEmpty(expireDate1)) {
				conditionStr += " and "
						+ YHDBUtility.getDateFilter("EXPIRE_DATE", expireDate1,
								">=");
			}
			if (!YHUtility.isNullorEmpty(expireDate2)) {
				conditionStr += " and "
						+ YHDBUtility.getDateFilter("EXPIRE_DATE",
								expireDate2, "<=");
			}
			String sql = "select SEQ_ID,STAFF_NAME,LICENSE_TYPE,LICENSE_NO,LICENSE_NAME,NOTIFIED_BODY,GET_LICENSE_DATE"
					+ ",EFFECTIVE_DATE,EXPIRATION_PERIOD,EXPIRE_DATE,STATUS,ATTACHMENT_ID,ATTACHMENT_NAME,REMARK,ADD_TIME from oa_pm_employee_certificate "
					+ " where 1=1 " + conditionStr;

			ps = dbConn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());// 游标用法，预显示最大行数
			rs = ps.executeQuery();
			rs.first();// 如指针用法，指向最上面位置
			rs.relative((page.getCurrentPageIndex() - 1) * page.getPageSize()
					- 1);// 相对于求出每页显示的长度
			while (rs.next()) {
				YHHrStaffLicense licen = new YHHrStaffLicense();
				licen.setSeqId(rs.getInt("SEQ_ID"));
				licen.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
				licen.setLicenseType(hrPublicIdTransName.getCodeUserName(dbConn, rs.getString("LICENSE_TYPE")));
				licen.setLicenseNo(rs.getString("LICENSE_NO"));
				licen.setLicenseName(rs.getString("LICENSE_NAME"));
				licen.setStatus(hrPublicIdTransName.getCodeUserName(dbConn, rs.getString("STATUS")));
				licenses.add(licen);

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return licenses;

	}
	/**
	 * 内部短信提醒
	 * @param user
	 * @param dbConn
	 * @param content
	 * @param url
	 * @param toId
	 * @param date
	 * @throws Exception
	 */
	 public static void sendSms(YHPerson user, Connection dbConn,String content, 
		      String url, String toId, Date date) throws Exception{
		    YHSmsBack smsBack = new YHSmsBack();    
		    smsBack.setContent(content);
		    smsBack.setFromId(user.getSeqId());
		    smsBack.setRemindUrl(url);
		    smsBack.setSmsType("0");
		    smsBack.setToId(toId);    
		    if(date != null){
		      smsBack.setSendDate(date);
		    }
		    YHSmsUtil.smsBack(dbConn, smsBack);
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
       YHHrStaffLicense staffLicense = (YHHrStaffLicense) orm.loadObjSingle(dbConn, YHHrStaffLicense.class, seqId);
       String dbAttachId = "";
       String dbAttachName = "";
       if (staffLicense != null) {
         dbAttachId = YHUtility.null2Empty(staffLicense.getAttachmentId());
         dbAttachName = YHUtility.null2Empty(staffLicense.getAttachmentName());
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
           staffLicense.setAttachmentId(attachmentId.trim());
           staffLicense.setAttachmentName(attachmentName.trim());
           orm.updateSingle(dbConn, staffLicense);
           returnFlag = true;
         }
       }
     } catch (Exception e) {
       throw e;
     }
     return returnFlag;
   }

	 
}
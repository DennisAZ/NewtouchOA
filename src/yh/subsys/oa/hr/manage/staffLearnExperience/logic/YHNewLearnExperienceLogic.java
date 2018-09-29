package yh.subsys.oa.hr.manage.staffLearnExperience.logic;

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
import yh.subsys.oa.hr.manage.staffLearnExperience.data.YHHrStaffLearnExperience;
import yh.subsys.oa.hr.manage.staffWorkExperience.data.YHHrStaffWorkExperience;
import yh.subsys.oa.hr.manage.staff_contract.data.YHHrStaffContract;
import yh.subsys.oa.hr.manage.staff_license.act.YHNewLicenseInfoAct;
import yh.subsys.oa.hr.manage.staff_license.data.YHHrStaffLicense;

public class YHNewLearnExperienceLogic {
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
   * 添加附件上传 并且添加学习经历信息
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
		String major = fileForm.getParameter("MAJOR");//所学专业
		String startDate = fileForm.getParameter("START_DATE");
		String endDate = fileForm.getParameter("END_DATE");
		String academyDegree = fileForm.getParameter("ACADEMY_DEGREE");//所获学历
		String degree = fileForm.getParameter("DEGREE");//所获学位
		String position = fileForm.getParameter("POSITION");//曾任班干
		String witness = fileForm.getParameter("WITNESS");//证明人
		String school = fileForm.getParameter("SCHOOL");//所在院校
		String schoolAddress = fileForm.getParameter("SCHOOL_ADDRESS");// 院校所在地
		
		String awarding = fileForm.getParameter("AWARDING");//获奖情况
		String certificates = fileForm.getParameter("CERTIFICATES");//所获证书
		String remark = fileForm.getParameter("REMARK");// 备注
		//String remark = fileForm.getParameter("REMARK");// 备注
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");
            
			YHHrStaffLearnExperience experience = new YHHrStaffLearnExperience();
			experience.setStaffName(userId);
			experience.setMajor(major);
			if (!YHUtility.isNullorEmpty(startDate)) {
				experience.setStartDate(YHUtility.parseDate("yyyy-MM-dd",
						startDate));
			}
			if (!YHUtility.isNullorEmpty(endDate)) {
				experience.setEndDate(YHUtility.parseDate("yyyy-MM-dd",
						endDate));
			}
			experience.setAcademyDegree(academyDegree);
			experience.setDegree(degree);
			experience.setPosition(position);
			experience.setWitness(witness);
			experience.setSchool(school);
			experience.setSchoolAddress(schoolAddress);
			experience.setAwarding(awarding);
			experience.setCertificates(certificates);
			experience.setRemark(remark);
			Date currentTime = new Date();    
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
			 String dateString = formatter.format(currentTime); //系统登记时间
			 experience.setAddTime(dateString);
			if (attachFlag) {
				experience.setAttachmentId(attachmentIds);
				experience.setAttachmentName(attachmentNames);
			}
			experience.setCreateUserId(String.valueOf(person.getSeqId()));
			experience.setCreateDeptId(person.getDeptId());
			orm.saveSingle(dbConn, experience);
		} catch (Exception e) {
			throw e;
		}
         
	}
	/**
	 * 修改附件上传 并且修改学习经历信息
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setUpLearnInfoValueLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String seqId = fileForm.getParameter("seqid");
		String userName = fileForm.getParameter("userName");
		String userId = fileForm.getParameter("userId");
		String major = fileForm.getParameter("MAJOR");//所学专业
		String startDate = fileForm.getParameter("START_DATE");
		String endDate = fileForm.getParameter("END_DATE");
		String academyDegree = fileForm.getParameter("ACADEMY_DEGREE");//所获学历
		String degree = fileForm.getParameter("DEGREE");//所获学位
		String position = fileForm.getParameter("POSITION");//曾任班干
		String witness = fileForm.getParameter("WITNESS");//证明人
		String school = fileForm.getParameter("SCHOOL");//所在院校
		String schoolAddress = fileForm.getParameter("SCHOOL_ADDRESS");// 院校所在地
		
		String awarding = fileForm.getParameter("AWARDING");//获奖情况
		String certificates = fileForm.getParameter("CERTIFICATES");//所获证书
		String remark = fileForm.getParameter("REMARK");// 备注
		
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");

			YHHrStaffLearnExperience experience = new YHHrStaffLearnExperience();
			experience.setSeqId(Integer.valueOf(seqId));
			experience.setStaffName(userId);
			experience.setMajor(major);
			if (!YHUtility.isNullorEmpty(startDate)) {
				experience.setStartDate(YHUtility.parseDate("yyyy-MM-dd",
						startDate));
			}
			if (!YHUtility.isNullorEmpty(endDate)) {
				experience.setEndDate(YHUtility.parseDate("yyyy-MM-dd",
						endDate));
			}
			experience.setAcademyDegree(academyDegree);
			experience.setDegree(degree);
			experience.setPosition(position);
			experience.setWitness(witness);
			experience.setSchool(school);
			experience.setSchoolAddress(schoolAddress);
			experience.setAwarding(awarding);
			experience.setCertificates(certificates);
			experience.setRemark(remark);
			if (attachFlag) {
				experience.setAttachmentId(attachmentIds);
				experience.setAttachmentName(attachmentNames);
			}
			experience.setCreateUserId(String.valueOf(person.getSeqId()));
			experience.setCreateDeptId(person.getDeptId());
		    orm.updateSingle(dbConn, experience);

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

	/*
	 * 查询共有的条数
	 */
	public int count(Connection dbConn, YHPerson user) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	  
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = " select COUNT(*) from oa_pm_employee_learn "
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

	/**
	 * 学习经历查询带分页的
	 * 
	 * @param dbConn
	 * @param user
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<YHHrStaffLearnExperience> findLearnExperienceInfo(Connection dbConn, YHPerson user,	YHPage page) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	  
		PreparedStatement ps = null;
		ResultSet rs = null;
			String learnSql = " select SEQ_ID,STAFF_NAME,MAJOR,EXPIRATION_PERIOD,ACADEMY_DEGREE,SCHOOL,WITNESS from oa_pm_employee_learn "
                      + " where CREATE_USER_ID = "+ user.getSeqId()
                      + " or CREATE_DEPT_ID in "+ deptIdStr
					            + " order by SEQ_ID desc ";
			
		List<YHHrStaffLearnExperience> learn = new ArrayList<YHHrStaffLearnExperience>();
		try {
			ps = dbConn.prepareStatement(learnSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());// 游标用法，预显示最大行数
			rs = ps.executeQuery();
			rs.first();// 如指针用法，指向最上面位置
			rs.relative((page.getCurrentPageIndex() - 1) * page.getPageSize()	- 1);// 相对于求出每页显示的长度
			while (rs.next()) {
				YHHrStaffLearnExperience learnEx = new YHHrStaffLearnExperience();
				learnEx.setSeqId(rs.getInt("SEQ_ID"));
				learnEx.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
				learnEx.setMajor(rs.getString("MAJOR"));
				learnEx.setExpirationPeriod(rs.getString("EXPIRATION_PERIOD"));
				learnEx.setAcademyDegree(rs.getString("ACADEMY_DEGREE"));
				learnEx.setSchool(rs.getString("SCHOOL"));
				learnEx.setWitness(rs.getString("WITNESS"));
				
				learn.add(learnEx);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return learn;
	}

	/**
	 * 查询学习经历的详细信息
	 * 
	 * @param dbConn
	 * @param user
	 * @param ContractSeqId
	 * @return
	 * @throws Exception
	 */

	public List<YHHrStaffLearnExperience> learnXxInfo(Connection dbConn, YHPerson user,
			int learnSeqId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		 List<YHHrStaffLearnExperience> learn = new ArrayList<YHHrStaffLearnExperience>();
		    YHORM orm =new YHORM();
		try {
			YHHrStaffLearnExperience sle= (YHHrStaffLearnExperience) orm.loadObjSingle(dbConn, YHHrStaffLearnExperience.class, learnSeqId);
			//sle.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(sle.getStaffName())));
			learn.add(sle);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return learn;
	}

	/**
	 * 删除学习经历的单条信息
	 * 
	 * @param conn
	 * @param person
	 * @param noHiddenId
	 * @return
	 * @throws Exception
	 */
	public static int delLearnExInfo(Connection conn, YHPerson person,
			int noHiddenId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String deleteSql = "delete from oa_pm_employee_learn where seq_id ="
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
	 * 删除所选的学习经历信息
	 * 
	 * @param dbConn
	 * @param loginperson
	 * @param deleteStr
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public int deleteLearnExInfo(Connection dbConn, String deleteStr)
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
			deletenewsCommentSql = "delete  from oa_pm_employee_learn  where seq_id in  ("
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
	 * 学习经历信息查询（第三个标签页）
	 * （带查询条件分页）
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
	public int learnCount(Connection dbConn, YHPerson user, YHHrStaffLearnExperience learn) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	  
		PreparedStatement ps = null;
		ResultSet rs = null;
		String conditionStr = "";
		List<YHHrStaffLearnExperience> learnEx = new ArrayList<YHHrStaffLearnExperience>();
		try {
			if (!YHUtility.isNullorEmpty(learn.getStaffName())) {
				conditionStr = " and STAFF_NAME ='"
						+ YHDBUtility.escapeLike(learn.getStaffName()) + "'";
			}

			if (!YHUtility.isNullorEmpty(learn.getMajor())) {
				conditionStr += " and MAJOR ='"
						+ YHDBUtility.escapeLike(learn.getMajor())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(learn.getAcademyDegree())) {
				conditionStr += " and ACADEMY_DEGREE ='"
						+ YHDBUtility.escapeLike(learn.getAcademyDegree()) + "'";
			}
			if (!YHUtility.isNullorEmpty(learn.getSchool())) {
				conditionStr += " and SCHOOL ='"
						+ YHDBUtility.escapeLike(learn.getSchool())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(learn.getCertificates())) {
				conditionStr += " and CERTIFICATES ='"
						+ YHDBUtility.escapeLike(learn.getCertificates()) + "'";
			}
			String sql = " select COUNT(*) from oa_pm_employee_learn "
                 + " where (CREATE_USER_ID = "+ user.getSeqId()
                 + " or CREATE_DEPT_ID in "+ deptIdStr + ")"
                 + conditionStr;
			
			ps = dbConn.prepareStatement(sql);
	        rs = ps.executeQuery();      
	        if(rs.next()){
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
     * 查询合同相关信息（第三个标签页）
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
    public List<YHHrStaffLearnExperience> queryLearnExInfo(Connection dbConn,YHPerson user, YHPage page,YHHrStaffLearnExperience learn) throws Exception{
      YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
      String deptIdStr = logic.getHrManagerPriv(dbConn, user);
      
      PreparedStatement ps = null;
      ResultSet rs = null;
      String conditionStr = "";
      List<YHHrStaffLearnExperience> learns = new ArrayList<YHHrStaffLearnExperience>();
      try{//sql += " and ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(activeContent) + "%' " + YHDBUtility.escapeLike();
      	if (!YHUtility.isNullorEmpty(learn.getStaffName())) {
      	  conditionStr = " and STAFF_NAME like '%" + YHDBUtility.escapeLike(learn.getStaffName()) + "%' " + YHDBUtility.escapeLike();
  			}
  
  			if (!YHUtility.isNullorEmpty(learn.getMajor())) {
  				conditionStr += " and MAJOR like '%" + YHDBUtility.escapeLike(learn.getMajor())+ "%' " + YHDBUtility.escapeLike();
  			}
  			if (!YHUtility.isNullorEmpty(learn.getAcademyDegree())) {
  				conditionStr += " and ACADEMY_DEGREE like '%" + YHDBUtility.escapeLike(learn.getAcademyDegree()) + "%' " + YHDBUtility.escapeLike();
  			}
  			if (!YHUtility.isNullorEmpty(learn.getSchool())) {
  				conditionStr += " and SCHOOL like '%" + YHDBUtility.escapeLike(learn.getSchool()) + "%' " + YHDBUtility.escapeLike();
  			}
  			if (!YHUtility.isNullorEmpty(learn.getCertificates())) {
  				conditionStr += " and CERTIFICATES like '%" + YHDBUtility.escapeLike(learn.getCertificates()) + "%' " + YHDBUtility.escapeLike();
  			}
  			String learnSql = " select SEQ_ID,STAFF_NAME,MAJOR,EXPIRATION_PERIOD,SCHOOL,WITNESS from oa_pm_employee_learn "
                        + " where (CREATE_USER_ID = "+ user.getSeqId()
                        + " or CREATE_DEPT_ID in "+ deptIdStr + ")"
                        + conditionStr;
		
			  ps = dbConn.prepareStatement(learnSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
	      ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());//游标用法，预显示最大行数
	      rs = ps.executeQuery();
	      rs.first();//如指针用法，指向最上面位置
	      rs.relative((page.getCurrentPageIndex()-1) * page.getPageSize() -1);//相对于求出每页显示的长度       
	      while(rs.next()){
	    	  YHHrStaffLearnExperience learnEx = new YHHrStaffLearnExperience();
	    	  learnEx.setSeqId(rs.getInt("SEQ_ID"));
	    	  learnEx.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
					learnEx.setMajor(rs.getString("MAJOR"));
					learnEx.setExpirationPeriod(rs.getString("EXPIRATION_PERIOD"));
					learnEx.setSchool(rs.getString("SCHOOL"));
					learnEx.setWitness(rs.getString("WITNESS"));
					learns.add(learnEx);
        }
      } catch (SQLException e){
        throw e;
      }finally{
         YHDBUtility.close(ps, rs, null);
      }    
       return learns;
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
       YHHrStaffLearnExperience staffLearnExperience = (YHHrStaffLearnExperience) orm.loadObjSingle(dbConn, YHHrStaffLearnExperience.class, seqId);
       String dbAttachId = "";
       String dbAttachName = "";
       if (staffLearnExperience != null) {
         dbAttachId = YHUtility.null2Empty(staffLearnExperience.getAttachmentId());
         dbAttachName = YHUtility.null2Empty(staffLearnExperience.getAttachmentName());
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
           staffLearnExperience.setAttachmentId(attachmentId.trim());
           staffLearnExperience.setAttachmentName(attachmentName.trim());
           orm.updateSingle(dbConn, staffLearnExperience);
           returnFlag = true;
         }
       }
     } catch (Exception e) {
       throw e;
     }
     return returnFlag;
   }

}
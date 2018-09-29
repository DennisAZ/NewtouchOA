package yh.subsys.oa.hr.manage.staffLaborSkills.logic;

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
import yh.subsys.oa.hr.manage.staffLaborSkills.data.YHHrStaffLaborSkills;
import yh.subsys.oa.hr.manage.staffWorkExperience.data.YHHrStaffWorkExperience;
import yh.subsys.oa.hr.manage.staff_license.act.YHNewLicenseInfoAct;
import yh.subsys.oa.hr.manage.staff_license.data.YHHrStaffLicense;

public class YHNewLaborSkillsLogic {
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
   * 添加附件上传 并且添加劳动技能信息
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
	public void setNewLaborSkillsInfoValueLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String userName = fileForm.getParameter("userName");
		String userId = fileForm.getParameter("userId");
		String abilityName = fileForm.getParameter("ABILITY_NAME");//技能名称
		String specialWork = fileForm.getParameter("SPECIAL_WORK");//特种作业
		String skillsLevel = fileForm.getParameter("SKILLS_LEVEL");//级别
		String skillsCertificate = fileForm.getParameter("SKILLS_CERTIFICATE");//技能证
		String issueDate = fileForm.getParameter("ISSUE_DATE");//发证日期
		String expires = fileForm.getParameter("EXPIRES");//有效期
		String expireDate = fileForm.getParameter("EXPIRE_DATE");//到期日期
		String issuingAuthority = fileForm.getParameter("ISSUING_AUTHORITY");//发证机关/单位
		String remark = fileForm.getParameter("REMARK");//备注
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");
            
			YHHrStaffLaborSkills skills = new YHHrStaffLaborSkills();
			skills.setStaffName(userId);
			skills.setAbilityName(abilityName);
			skills.setSpecialWork(specialWork);
			skills.setSkillsLevel(skillsLevel);
			skills.setSkillsCertificate(skillsCertificate);
			
			if (!YHUtility.isNullorEmpty(issueDate)) {
				skills.setIssueDate(YHUtility.parseDate("yyyy-MM-dd",
						issueDate));
			}
			skills.setExpires(expires);
			if (!YHUtility.isNullorEmpty(expireDate)) {
				skills.setExpireDate(YHUtility.parseDate("yyyy-MM-dd",
						expireDate));
			}
			skills.setIssuingAuthority(issuingAuthority);
			skills.setRemark(remark);
			if (attachFlag) {
				skills.setAttachmentId(attachmentIds);
				skills.setAttachmentName(attachmentNames);
			}
			Date currentTime = new Date();    
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		    String dateString = formatter.format(currentTime); 

			skills.setAddTime(dateString);//登记时间
	    skills.setCreateUserId(String.valueOf(person.getSeqId()));
	    skills.setCreateDeptId(person.getDeptId());
			orm.saveSingle(dbConn, skills);
		} catch (Exception e) {
			throw e;
		}
         
	}
	/**
	 * 修改附件上传 并且修改劳动技能信息
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setUpLaborSkillInfoValueLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String seqId = fileForm.getParameter("seqid");
		String userName = fileForm.getParameter("userName");
		String userId = fileForm.getParameter("userId");
		String abilityName = fileForm.getParameter("ABILITY_NAME");//技能名称
		String specialWork = fileForm.getParameter("SPECIAL_WORK");//特种作业
		String skillsLevel = fileForm.getParameter("SKILLS_LEVEL");//级别
		String skillsCertificate = fileForm.getParameter("SKILLS_CERTIFICATE");//技能证
		String issueDate = fileForm.getParameter("ISSUE_DATE");//发证日期
		String expires = fileForm.getParameter("EXPIRES");//有效期
		String expireDate = fileForm.getParameter("EXPIRE_DATE");//到期日期
		String issuingAuthority = fileForm.getParameter("ISSUING_AUTHORITY");//发证机关/单位
		String remark = fileForm.getParameter("REMARK");//备注
		
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");
            
			YHHrStaffLaborSkills skills = new YHHrStaffLaborSkills();
			skills.setSeqId(Integer.valueOf(seqId));
			skills.setStaffName(userId);
			skills.setAbilityName(abilityName);
			skills.setSpecialWork(specialWork);
			skills.setSkillsLevel(skillsLevel);
			skills.setSkillsCertificate(skillsCertificate);
			
			if (!YHUtility.isNullorEmpty(issueDate)) {
				skills.setIssueDate(YHUtility.parseDate("yyyy-MM-dd",
						issueDate));
			}
			skills.setExpires(expires);
			if (!YHUtility.isNullorEmpty(expireDate)) {
				skills.setExpireDate(YHUtility.parseDate("yyyy-MM-dd",
						expireDate));
			}
			skills.setIssuingAuthority(issuingAuthority);
			skills.setRemark(remark);
			if (attachFlag) {
				skills.setAttachmentId(attachmentIds);
				skills.setAttachmentName(attachmentNames);
			}
			Date currentTime = new Date();    
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		    String dateString = formatter.format(currentTime); 
		    if (!YHUtility.isNullorEmpty(dateString)) {
				skills.setAddTime(dateString); //登记时间
			}
      skills.setCreateUserId(String.valueOf(person.getSeqId()));
	    skills.setCreateDeptId(person.getDeptId());
	    orm.updateSingle(dbConn, skills);
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
		String sql = " select COUNT(*) from oa_pm_employee_skills "
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
	 * 劳动技能查询带分页的
	 * @param dbConn
	 * @param user
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<YHHrStaffLaborSkills> findLaborSkillsInfo(Connection dbConn, YHPerson user,	YHPage page) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	  
		PreparedStatement ps = null;
		ResultSet rs = null;
    String skillsSql = "select SEQ_ID,STAFF_NAME,ABILITY_NAME,SKILLS_LEVEL,ISSUE_DATE,EXPIRES from oa_pm_employee_skills "
                         + " where CREATE_USER_ID = "+ user.getSeqId()
                         + " or CREATE_DEPT_ID in "+ deptIdStr;
			
		List<YHHrStaffLaborSkills> skill = new ArrayList<YHHrStaffLaborSkills>();
		try {
			ps = dbConn.prepareStatement(skillsSql,	ResultSet.TYPE_SCROLL_INSENSITIVE,	ResultSet.CONCUR_READ_ONLY);
			ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());// 游标用法，预显示最大行数
			rs = ps.executeQuery();
			rs.first();// 如指针用法，指向最上面位置
			rs.relative((page.getCurrentPageIndex() - 1) * page.getPageSize()	- 1);// 相对于求出每页显示的长度
			while (rs.next()) {
				YHHrStaffLaborSkills laborSkills = new YHHrStaffLaborSkills();
				laborSkills.setSeqId(rs.getInt("SEQ_ID"));
				laborSkills.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
				laborSkills.setAbilityName(rs.getString("ABILITY_NAME"));
				laborSkills.setSkillsLevel(rs.getString("SKILLS_LEVEL"));
				laborSkills.setIssueDate(rs.getDate("ISSUE_DATE"));
				laborSkills.setExpires(rs.getString("EXPIRES"));
				skill.add(laborSkills);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return skill;
	}

	/**
	 * 查询劳动技能的详细信息
	 * 
	 * @param dbConn
	 * @param user
	 * @param ContractSeqId
	 * @return
	 * @throws Exception
	 */

	public List<YHHrStaffLaborSkills> laborSkillXxInfo(Connection dbConn, YHPerson user,
			int skillSeqId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		 List<YHHrStaffLaborSkills> skill = new ArrayList<YHHrStaffLaborSkills>();
		    YHORM orm =new YHORM();
		try {
			YHHrStaffLaborSkills sle= (YHHrStaffLaborSkills) orm.loadObjSingle(dbConn, YHHrStaffLaborSkills.class, skillSeqId);
			//sle.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(sle.getStaffName())));
			skill.add(sle);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return skill;
	}

	/**
	 * 删除劳动技能的单条信息
	 * 
	 * @param conn
	 * @param person
	 * @param noHiddenId
	 * @return
	 * @throws Exception
	 */
	public static int delLaborSkillInfo(Connection conn, YHPerson person,
			int noHiddenId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String deleteSql = "delete from oa_pm_employee_skills where seq_id ="
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
	 * 删除所选的劳动技能信息
	 * 
	 * @param dbConn
	 * @param loginperson
	 * @param deleteStr
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public int deleteLaborSkillInfo(Connection dbConn, String deleteStr)
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
			deletenewsCommentSql = "delete  from oa_pm_employee_skills where seq_id in  ("
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
	 * 劳动技能信息查询（第三个标签页）
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
	public int laborSkillCount(Connection dbConn, YHPerson user, YHHrStaffLaborSkills laborSkill,String issueDate1,String issueDate2,String expireDate1,String expireDate2 ) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	  
	  PreparedStatement ps = null;
		ResultSet rs = null;
		String conditionStr = "";
		List<YHHrStaffLaborSkills> laborSkills = new ArrayList<YHHrStaffLaborSkills>();
		try {
			if (!YHUtility.isNullorEmpty(laborSkill.getStaffName())) {
				conditionStr = " and STAFF_NAME ='"
						+ YHDBUtility.escapeLike(laborSkill.getStaffName()) + "'";
			}

			if (!YHUtility.isNullorEmpty(laborSkill.getAbilityName())) {
				conditionStr += " and ABILITY_NAME ='"
						+ YHDBUtility.escapeLike(laborSkill.getAbilityName())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(laborSkill.getIssuingAuthority())) {
				conditionStr += " and ISSUING_AUTHORITY ='"
						+ YHDBUtility.escapeLike(laborSkill.getIssuingAuthority()) + "'";
			}
			if (!YHUtility.isNullorEmpty(issueDate1)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("ISSUE_DATE", issueDate1, ">=");
			}
			if (!YHUtility.isNullorEmpty(issueDate2)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("ISSUE_DATE", issueDate2, "<=");
			}
			if (!YHUtility.isNullorEmpty(expireDate1)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("EXPIRE_DATE", expireDate1, ">=");
			}
			if (!YHUtility.isNullorEmpty(expireDate2)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("EXPIRE_DATE", expireDate2, "<=");
			}
			
			String sql = "select COUNT(*) from oa_pm_employee_skills "
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
     * 查询劳动技能相关信息（第三个标签页）
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
    public List<YHHrStaffLaborSkills> queryLaborSkillInfo(Connection dbConn,YHPerson user, YHPage page,YHHrStaffLaborSkills laborSkill,String issueDate1,String issueDate2,String expireDate1,String expireDate2) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String conditionStr = "";
        List<YHHrStaffLaborSkills> laborSkills = new ArrayList<YHHrStaffLaborSkills>();
        try{//sql += " and ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(activeContent) + "%' " + YHDBUtility.escapeLike();
        	
        	if (!YHUtility.isNullorEmpty(laborSkill.getStaffName())) {
				conditionStr = " and STAFF_NAME ='"
						+ YHDBUtility.escapeLike(laborSkill.getStaffName()) + "'";
			}

			if (!YHUtility.isNullorEmpty(laborSkill.getAbilityName())) {
				conditionStr += " and ABILITY_NAME ='"
						+ YHDBUtility.escapeLike(laborSkill.getAbilityName())
						+ "'";
			}
			if (!YHUtility.isNullorEmpty(laborSkill.getIssuingAuthority())) {
				conditionStr += " and ISSUING_AUTHORITY ='"
						+ YHDBUtility.escapeLike(laborSkill.getIssuingAuthority()) + "'";
			}
			if (!YHUtility.isNullorEmpty(issueDate1)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("ISSUE_DATE", issueDate1, ">=");
			}
			if (!YHUtility.isNullorEmpty(issueDate2)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("ISSUE_DATE", issueDate2, "<=");
			}
			if (!YHUtility.isNullorEmpty(expireDate1)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("EXPIRE_DATE", expireDate1, ">=");
			}
			if (!YHUtility.isNullorEmpty(expireDate2)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("EXPIRE_DATE", expireDate2, "<=");
			}
		    String skillsSql = "select SEQ_ID,STAFF_NAME,ABILITY_NAME,SKILLS_LEVEL,ISSUE_DATE,EXPIRES from oa_pm_employee_skills where 1=1"+ conditionStr ;

			  ps = dbConn.prepareStatement(skillsSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
		      ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());//游标用法，预显示最大行数
		      rs = ps.executeQuery();
		      rs.first();//如指针用法，指向最上面位置
		      rs.relative((page.getCurrentPageIndex()-1) * page.getPageSize() -1);//相对于求出每页显示的长度       
		      while(rs.next()){
		    	  YHHrStaffLaborSkills laborSkillss = new YHHrStaffLaborSkills();
					laborSkillss.setSeqId(rs.getInt("SEQ_ID"));
					laborSkillss.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
					laborSkillss.setAbilityName(rs.getString("ABILITY_NAME"));
					laborSkillss.setSkillsLevel(rs.getString("SKILLS_LEVEL"));
					laborSkillss.setIssueDate(rs.getDate("ISSUE_DATE"));
					laborSkillss.setExpires(rs.getString("EXPIRES"));
					laborSkills.add(laborSkillss);
		        
		        }
			
      } catch (SQLException e){
        throw e;
      }finally{
         YHDBUtility.close(ps, rs, null);
      }    
       return laborSkills;
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
	 //id 转化为name
	 public String getUserName(Connection dbConn, int seqId) throws Exception{ 
		 String sql = "select USER_NAME from person dr where dr.SEQ_ID=" + seqId; 
		 PreparedStatement ps = null; 
		 ResultSet rs = null; 
		 try{ 
				 ps = dbConn.prepareStatement(sql); 
				 rs = ps.executeQuery(); 
			 if(rs.next()){ 
			   return rs.getString("USER_NAME"); 
			 } 
		 } catch (Exception e){ 
		     throw e; 
		 }finally{ 
		     YHDBUtility.close(ps, null, null); 
		 } 
		     return null; 
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
       YHHrStaffLaborSkills staffLaborSkills = (YHHrStaffLaborSkills) orm.loadObjSingle(dbConn, YHHrStaffLaborSkills.class, seqId);
       String dbAttachId = "";
       String dbAttachName = "";
       if (staffLaborSkills != null) {
         dbAttachId = YHUtility.null2Empty(staffLaborSkills.getAttachmentId());
         dbAttachName = YHUtility.null2Empty(staffLaborSkills.getAttachmentName());
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
           staffLaborSkills.setAttachmentId(attachmentId.trim());
           staffLaborSkills.setAttachmentName(attachmentName.trim());
           orm.updateSingle(dbConn, staffLaborSkills);
           returnFlag = true;
         }
       }
     } catch (Exception e) {
       throw e;
     }
     return returnFlag;
   }

}

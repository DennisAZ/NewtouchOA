package yh.subsys.oa.hr.manage.staff_contract.logic;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.data.YHEmailCont;
import yh.core.funcs.email.logic.YHInnerEMailLogic;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.hr.manage.hrIdtransName.hrPublicIdTransName;
import yh.subsys.oa.hr.manage.leave.data.YHHrStaffLeave;
import yh.subsys.oa.hr.manage.logic.YHHrStaffIncentiveLogic;
import yh.subsys.oa.hr.manage.staffWorkExperience.data.YHHrStaffWorkExperience;
import yh.subsys.oa.hr.manage.staff_contract.data.YHHrStaffContract;
import yh.subsys.oa.hr.manage.staff_license.act.YHNewLicenseInfoAct;
import yh.subsys.oa.hr.manage.staff_license.data.YHHrStaffLicense;
import yh.subsys.oa.hr.manage.staff_license.logic.YHNewLicenseInfoLogic;

public class YHNewContractInfoLogic {
	hrPublicIdTransName workTrans = new hrPublicIdTransName();
	public void setNewContractInfoValueLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person , String root) throws Exception {
		 YHORM orm = new YHORM();
         String userId = fileForm.getParameter("userId");
		 String smsRemind = fileForm.getParameter("SMS_REMIND");//短信提醒
		 String userName = fileForm.getParameter("userName");   
		 String contractNo = fileForm.getParameter("STAFF_CONTRACT_NO");
		 String contractType = fileForm.getParameter("contractValue"); //合同类型
		 String specialization = fileForm.getParameter("CONTRACT_SPECIALIZATION");//合同属性
		 String status = fileForm.getParameter("contractState");//??????合同状态
		 String makeContract = fileForm.getParameter("MAKE_CONTRACT"); //合同签订日期
		 String trailEffectiveTime = fileForm.getParameter("TRAIL_EFFECTIVE_TIME");//试用生效日期 
		 String probationAry = fileForm.getParameter("PROBATIONARY_PERIOD");//试用天数
		 String trailOverTime = fileForm.getParameter("TRAIL_OVER_TIME");//试用到期日期
		 String passOrNot = fileForm.getParameter("PASS_OR_NOT"); //合同是否转正
		 
		 String probtionEndDate = fileForm.getParameter("PROBATION_END_DATE"); //合同转正日期
		 String effectiveDate = fileForm.getParameter("PROBATION_EFFECTIVE_DATE");//合同生效日期
		 String contractEndTime = fileForm.getParameter("CONTRACT_END_TIME");//合同到期日期
		 
		 String removeOrNot = fileForm.getParameter("REMOVE_OR_NOT");//合同是否解除
		 String contractRemoveTime = fileForm.getParameter("CONTRACT_REMOVE_TIME");//合同jiechu日期
		 
		 String signTimes = fileForm.getParameter("SIGN_TIMES");//签约次数   
		 String remindTime = fileForm.getParameter("REMIND_TIME");//提醒时间
		 String remark = fileForm.getParameter("REMARK"); 
		 String attachId = YHUtility.null2Empty(fileForm.getParameter("attachmentId"));
		 String attachName = YHUtility.null2Empty(fileForm.getParameter("attachmentName"));
	try{
		     Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			 boolean attachFlag = (Boolean) map.get("attachFlag");
			 String attachmentIds = (String) map.get("attachmentIds");
			 String attachmentNames = (String) map.get("attachmentNames");
				
			 YHHrStaffContract Contract = new YHHrStaffContract();
			 Contract.setStaffName(userId);
			 Contract.setStaffContractNo(contractNo);
			 Contract.setContractType(contractType);
			 Contract.setContractSpecialization(specialization);
			 Contract.setStatus(status);
			 if(!YHUtility.isNullorEmpty(makeContract)){
	        	 Contract.setMakeContract(YHUtility.parseDate("yyyy-MM-dd",makeContract));
			 }
	         if(!YHUtility.isNullorEmpty(trailEffectiveTime)){
	        	 Contract.setTrailEffectiveTime(YHUtility.parseDate("yyyy-MM-dd",trailEffectiveTime));
			 }
			 Contract.setProbationaryPeriod(probationAry);
			 if(!YHUtility.isNullorEmpty(trailOverTime)){
				 Contract.setTrailOverTime(YHUtility.parseDate("yyyy-MM-dd",trailOverTime));
			 }
			 Contract.setPassOrNot(passOrNot);
			 if(!YHUtility.isNullorEmpty(probtionEndDate)){
				 Contract.setProbationEndDate(YHUtility.parseDate("yyyy-MM-dd",probtionEndDate));
			 }
			 if(!YHUtility.isNullorEmpty(effectiveDate)){
				 Contract.setProbationEffectiveDate(YHUtility.parseDate("yyyy-MM-dd",effectiveDate));
			 }
			 if(!YHUtility.isNullorEmpty(contractEndTime)){
				 Contract.setContractEndTime(YHUtility.parseDate("yyyy-MM-dd",contractEndTime));
			 }
			 Contract.setRemoveOrNot(removeOrNot);
			 if(!YHUtility.isNullorEmpty(contractRemoveTime)){
				 Contract.setContractRemoveTime(YHUtility.parseDate("yyyy-MM-dd",contractRemoveTime));
			 }
			    Date currentTime = new Date();    
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
			    String dateString = formatter.format(currentTime); //系统登录时间	
			 Contract.setSignTimes(signTimes);
			 Contract.setAddTime(dateString);
			 Contract.setRemark(remark);
			 if (attachFlag) {
			   attachId += attachmentIds;
			   attachName += attachmentNames;
			 }
			 Contract.setAttachmentId(attachId);
			 Contract.setAttachmentName(attachName);
			 Contract.setCreateUserId(String.valueOf(person.getSeqId()));
			 Contract.setCreateDeptId(person.getDeptId());
			 orm.saveSingle(dbConn, Contract);
			 
		/*	 String seqId ="";
				for(int i=0; i<findContracts.size(); i++){
				   seqId = findContracts.get(i).getStaffName();
				}
				if(!YHUtility.isNullorEmpty(seqId)){
				    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
				    request.setAttribute("userName", userName);
				}*/
			 
			    String contractInfo = userName +" 合同已到期！合同编号：" + contractNo;
				String contractPerson = userName +" 新建了一份合同信息！合同编号：" + contractNo;
				
				int seqId = this.getSeqId(dbConn);
				String remindUrl = root + "/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/contractXxInfo.act?ContractSeqId=" + seqId;
				if(!YHUtility.isNullorEmpty(smsRemind)){
				   YHNewLicenseInfoLogic.sendSms(person, dbConn, contractPerson, remindUrl, userId, null);
				   String deptHrManager = this.getManager(userId, dbConn);
				   if (!YHUtility.isNullorEmpty(deptHrManager)) {
				     YHNewLicenseInfoLogic.sendSms(person, dbConn, contractPerson, remindUrl, deptHrManager, null);
				   }
				}
				if(!YHUtility.isNullorEmpty(remindTime)){
				  YHNewLicenseInfoLogic.sendSms(person, dbConn, contractInfo, null, userId,YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",remindTime));
				  String deptHrManager = this.getManager(userId, dbConn);
          if (!YHUtility.isNullorEmpty(deptHrManager)) {
            YHNewLicenseInfoLogic.sendSms(person, dbConn, contractInfo, null, deptHrManager, YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",remindTime));
          }
				}
		 
	} catch (Exception e) {
				throw e;
	}
		
}
	 /**
   * 附件批量上传页面处理
   * 
   * @return
   * @throws Exception
   */
  public StringBuffer uploadMsrg2Json(YHFileUploadForm fileForm)
      throws Exception {
    StringBuffer sb = new StringBuffer();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try {
      attr = fileUploadLogic1(fileForm);
      Set<String> attrKeys = attr.keySet();
      for (String key : attrKeys) {
        String fileName = attr.get(key);
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      sb.append("{");
      sb.append("'attachmentId':").append("\"").append(attachmentId).append(
          "\",");
      sb.append("'attachmentName':").append("\"").append(attachmentName)
          .append("\"");
      sb.append("}");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return sb;
  }
	/**
   * 处理上传附件，返回附件id，附件名称

   * 
   * @param request
   *          HttpServletRequest
   * @param
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception
   */
  public Map<String, String> fileUploadLogic1(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String filePath = YHSysProps.getAttachPath() ;
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      String trusFileName = "";
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String rand =   YHGuid.getRawGuid();
      trusFileName = rand + "." + fileName;
      result.put(hard + "_" + rand, fileName);
      fileForm.saveFile(fieldName, filePath + File.separator + YHNewLicenseInfoAct.attachmentFolder + File.separator
          + hard + File.separator + trusFileName);
    }
    YHSelAttachUtil selA = new YHSelAttachUtil(fileForm, YHNewLicenseInfoAct.attachmentFolder);
    result.putAll(selA.getAttachInFo());
    return result;
  }
	public int getSeqId(Connection conn) throws Exception {
	  Statement ps = null;
    ResultSet rs = null;
    String sql = "select max(SEQ_ID) as max FROM oa_pm_employee_contract" ;
    int seqId = 0 ;
    try{
      ps = conn.createStatement();
      rs = ps.executeQuery(sql);
      if (rs.next()) {
        seqId = rs.getInt("max");
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return seqId;
	}
	
	public String getManager(String seqId ,Connection conn) throws Exception {
	  Statement ps = null;
    ResultSet rs = null;
    
    String sql = "select DEPT_HR_MANAGER FROM oa_pm_manager WHERE DEPT_ID = (select DEPT_ID FROM PERSON WHERE SEQ_ID = "+seqId+")" ;
    String deptHrManager = "";
    try{
      ps = conn.createStatement();
      rs = ps.executeQuery(sql);
      if (rs.next()) {
        deptHrManager = rs.getString("DEPT_HR_MANAGER");
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return deptHrManager;
	}
	
	public static int newContractInfo(Connection conn, YHPerson person,YHHrStaffContract contract) throws Exception {
		PreparedStatement ps = null;
	    ResultSet rs = null;
	    int ok =0;
		String sql = "insert into oa_pm_employee_contract (CREATE_USER_ID,CREATE_DEPT_ID,STAFF_NAME,STAFF_CONTRACT_NO,CONTRACT_TYPE,CONTRACT_SPECIALIZATION," +
				"MAKE_CONTRACT,TRAIL_EFFECTIVE_TIME,PROBATIONARY_PERIOD,TRAIL_OVER_TIME,PASS_OR_NOT,PROBATION_END_DATE,REMARK,REMOVE_OR_NOT,CONTRACT_REMOVE_TIME," +
				"STATUS,SIGN_TIMES,ATTACHMENT_ID,ATTACHMENT_NAME,ADD_TIME)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
	try{
		ps = conn.prepareStatement(sql);
		ps.setString(1, contract.getCreateUserId());
		ps.setInt(2, contract.getCreateDeptId());
		ps.setString(3, contract.getStaffName());
		ps.setString(4, contract.getStaffContractNo());
		ps.setString(5, contract.getContractType());
		ps.setString(6, contract.getContractSpecialization());
		ps.setDate(7, new java.sql.Date(contract.getMakeContract().getTime()));
		ps.setDate(8, new java.sql.Date(contract.getTrailEffectiveTime()==null?0:contract.getTrailEffectiveTime().getTime()));
		ps.setString(9, contract.getProbationaryPeriod());
		ps.setDate(10, new java.sql.Date(contract.getTrailOverTime()==null?0:contract.getTrailEffectiveTime().getTime()));
		ps.setString(11, contract.getPassOrNot());
		ps.setDate(12, new java.sql.Date(contract.getProbationEndDate()==null?0:contract.getProbationEndDate().getTime()));
		ps.setString(13, contract.getRemark());
		ps.setString(14, contract.getRemoveOrNot());
		ps.setDate(15, new java.sql.Date(contract.getContractRemoveTime()==null?0:contract.getContractRemoveTime().getTime()));
		ps.setString(16, contract.getStatus());
		ps.setString(17, contract.getSignTimes());
		ps.setString(18, contract.getAttachmentId());
		ps.setString(19, contract.getAttachmentName());
		ps.setString(20, contract.getAddTime());
		
	    ok = ps.executeUpdate();
		
	}catch(Exception ex){
	      throw ex;
	}finally{
	      YHDBUtility.close(ps, rs, null);
	}
	    
		return ok;
	}
	/**
	 * 修改合同附件上传
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setUpContractInfoValueLogic(Connection dbConn,
			YHFileUploadForm fileForm, YHPerson person ,String root) throws Exception {
		YHORM orm = new YHORM();
		String smsRemind = fileForm.getParameter("SMS_REMIND");//短信提醒
		String seqId = fileForm.getParameter("seqid");  
		String userId = fileForm.getParameter("userId");
		 String userName = fileForm.getParameter("userName");   
		 String contractNo = fileForm.getParameter("STAFF_CONTRACT_NO");
		// String contractType = fileForm.getParameter("contractValue"); 
		 String contractType = fileForm.getParameter("CONTRACT_TYPE"); 
		 String specialization = fileForm.getParameter("CONTRACT_SPECIALIZATION");//合同属性
		 String status = fileForm.getParameter("STATUS");
		 String makeContract = fileForm.getParameter("MAKE_CONTRACT"); //合同签订日期
		 String effectiveTime = fileForm.getParameter("TRAIL_EFFECTIVE_TIME");//试用生效日期 
     String passOrNot = fileForm.getParameter("PASS_OR_NOT"); //合同是否转正
     String removeOrNot = fileForm.getParameter("REMOVE_OR_NOT");//合同是否解除
		 
		 String probationAry = fileForm.getParameter("PROBATIONARY_PERIOD");//试用天数
		 String trailOverTime = fileForm.getParameter("TRAIL_OVER_TIME");//试用到期日期
		 String probtionEndDate = fileForm.getParameter("PROBATION_END_DATE"); //合同转正日期
		 String effectiveDate = fileForm.getParameter("PROBATION_EFFECTIVE_DATE");//合同生效日期
		 String contractEndTime = fileForm.getParameter("CONTRACT_END_TIME");//合同到期日期
		 String contractRemoveTime = fileForm.getParameter("CONTRACT_REMOVE_TIME");//合同jiechu日期

		 String signTimes = fileForm.getParameter("SIGN_TIMES");//签约次数   
		 String remindTime = fileForm.getParameter("REMIND_TIME");//提醒时间
		 String remark = fileForm.getParameter("REMARK"); 
		try {
			Map<Object, Object> map = this.fileUploadLogic(fileForm,
					YHNewLicenseInfoAct.attachmentFolder);
			boolean attachFlag = (Boolean) map.get("attachFlag");
			String attachmentIds = (String) map.get("attachmentIds");
			String attachmentNames = (String) map.get("attachmentNames");
			
			YHHrStaffContract contractTemp = (YHHrStaffContract) orm.loadObjSingle(dbConn, YHHrStaffContract.class, Integer.parseInt(seqId));
			 String dbAttachId = YHUtility.null2Empty(contractTemp.getAttachmentId());
       String dbAttachName = YHUtility.null2Empty(contractTemp.getAttachmentName());
       
       YHHrStaffContract Contract = new YHHrStaffContract();
			 Contract.setSeqId(Integer.valueOf(seqId));
			 Contract.setStaffName(userId);
			 Contract.setStaffContractNo(contractNo);
			 Contract.setContractType(contractType);
			 Contract.setContractSpecialization(specialization);
			 Contract.setStatus(status);
			 Contract.setPassOrNot(passOrNot);
			 Contract.setRemoveOrNot(removeOrNot);
			 Contract.setMakeContract(YHUtility.parseDate("yyyy-MM-dd",makeContract));
	         if(!YHUtility.isNullorEmpty(effectiveTime)){
	        	 Contract.setMakeContract(YHUtility.parseDate("yyyy-MM-dd",effectiveTime));
			 }
			 Contract.setProbationaryPeriod(probationAry);
			 if(!YHUtility.isNullorEmpty(trailOverTime)){
				 Contract.setTrailOverTime(YHUtility.parseDate("yyyy-MM-dd",trailOverTime));
			 }
			 if(!YHUtility.isNullorEmpty(probtionEndDate)){
				 Contract.setProbationEndDate(YHUtility.parseDate("yyyy-MM-dd",probtionEndDate));
			 }
			 if(!YHUtility.isNullorEmpty(effectiveDate)){
				 Contract.setProbationEffectiveDate(YHUtility.parseDate("yyyy-MM-dd",effectiveDate));
			 }
			 if(!YHUtility.isNullorEmpty(contractEndTime)){
				 Contract.setContractRemoveTime(YHUtility.parseDate("yyyy-MM-dd",contractEndTime));
			 }
			 if(!YHUtility.isNullorEmpty(contractRemoveTime)){
				 Contract.setContractRemoveTime(YHUtility.parseDate("yyyy-MM-dd",contractRemoveTime));
			 }
			 Date currentTime = new Date();    
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
			 String dateString = formatter.format(currentTime); //系统登记时间	
			 Contract.setSignTimes(signTimes);
			 Contract.setAddTime(dateString);
			 Contract.setRemark(remark);
			 if (attachFlag) {
				 Contract.setAttachmentId(dbAttachId.trim() + attachmentIds.trim());
				 Contract.setAttachmentName(dbAttachName.trim() + attachmentNames.trim());
				}
       Contract.setCreateUserId(String.valueOf(person.getSeqId()));
			 Contract.setCreateDeptId(person.getDeptId());
			 
			 String contractInfo = userName +" 合同已到期！合同编号：" + contractNo;
				String contractPerson = userName +" 修改了一份合同信息！合同编号：" + contractNo;
				String remindUrl = root + "/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/contractXxInfo.act?ContractSeqId=" + seqId;
				if(!YHUtility.isNullorEmpty(smsRemind)){
				   YHNewLicenseInfoLogic.sendSms(person, dbConn, contractPerson, remindUrl, userId, null);
				}
				if(!YHUtility.isNullorEmpty(remindTime)){
				  YHNewLicenseInfoLogic.sendSms(person, dbConn, contractInfo, remindUrl, userId,YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",remindTime));
				}
				
			orm.updateSingle(dbConn, Contract);

		} catch (Exception e) {
			throw e;
		}

	}
	
	
	/*
	 * 修改合同信息
	 */
	public static int upContractInfo(Connection conn, YHPerson person,YHHrStaffContract contract) throws Exception {
		PreparedStatement ps = null;
	    ResultSet rs = null;
	    int ok =0;
		//PROBATION_EFFECTIVE_DATE  CONTRACT_END_TIME
		String upSql="update oa_pm_employee_contract set STAFF_NAME=?,STAFF_CONTRACT_NO=?,CONTRACT_TYPE=?,CONTRACT_SPECIALIZATION=?,STATUS=?,MAKE_CONTRACT=?," +
				"TRAIL_EFFECTIVE_TIME=?,PROBATIONARY_PERIOD=?,TRAIL_OVER_TIME=?,PASS_OR_NOT=?,PROBATION_END_DATE=?,PROBATION_EFFECTIVE_DATE=?," +
				"CONTRACT_END_TIME=?,REMOVE_OR_NOT=?,CONTRACT_REMOVE_TIME=?,SIGN_TIMES=?,ADD_TIME=?,REMARK=?,ATTACHMENT_ID=?,ATTACHMENT_NAME=? where SEQ_ID =?";
		
	try{
		ps = conn.prepareStatement(upSql);
		ps.setString(1, contract.getStaffName());
		ps.setString(2, contract.getStaffContractNo());
		ps.setString(3, contract.getContractType());
		ps.setString(4, contract.getContractSpecialization());
		ps.setString(5, contract.getStatus());
		ps.setDate(6, new java.sql.Date(contract.getMakeContract().getTime()));
		ps.setDate(7, new java.sql.Date(contract.getTrailEffectiveTime()==null?0:contract.getTrailEffectiveTime().getTime()));
		ps.setString(8, contract.getProbationaryPeriod());
		ps.setDate(9, new java.sql.Date(contract.getTrailOverTime()==null?0:contract.getTrailOverTime().getTime()));
		ps.setString(10, contract.getPassOrNot());
		ps.setDate(11, new java.sql.Date(contract.getProbationEndDate()==null?0:contract.getProbationEndDate().getTime()));
		ps.setDate(12, new java.sql.Date(contract.getProbationEffectiveDate()==null?0:contract.getProbationEffectiveDate().getTime()));
		//he shang yiyang  ??
		ps.setDate(13, new java.sql.Date(contract.getContractEndTime()==null?0:contract.getContractEndTime().getTime()));
		ps.setString(14, contract.getRemoveOrNot());
		ps.setDate(15, new java.sql.Date(contract.getContractRemoveTime()==null?0:contract.getContractRemoveTime().getTime()));
		ps.setString(16, contract.getSignTimes());
		ps.setString(17, contract.getAddTime());
		ps.setString(18, contract.getRemark());
		ps.setString(19, contract.getAttachmentId());
		ps.setString(20, contract.getAttachmentName());
			ps.setInt(21, contract.getSeqId());
	    ok = ps.executeUpdate();
		
	}catch(Exception ex){
	      throw ex;
	}finally{
	      YHDBUtility.close(ps, rs, null);
	}
	    
		return ok;
	}
	/*
	 * 查询共有的条数
	 */
	 public int count(Connection dbConn,  YHPerson user)throws Exception{
	    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
	    String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	   
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String sql = " select COUNT(*) from oa_pm_employee_contract "
                 + " where CREATE_USER_ID = "+ user.getSeqId()
                 + " or CREATE_DEPT_ID in "+ deptIdStr;
	    try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();      
        if(rs.next()){
          return rs.getInt(1);
        }
      } catch (SQLException e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }    
      return 0;
	 }
	 /**
	  * 合同信息查询（带条件查询）
	  * @param dbConn
	  * @param user
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
	 public int count1(Connection dbConn,YHPerson user, YHHrStaffContract contract,String makeContract1,String makeContract2,String trailOverTime1,String trailOverTime2,String contractEndTime1,String contractEndTime2) throws Exception{
	   YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
	   String deptIdStr = logic.getHrManagerPriv(dbConn, user);
	   
	   PreparedStatement ps = null;
	   ResultSet rs = null;
	   String conditionStr = "";
	   List<YHHrStaffContract> staff = new ArrayList<YHHrStaffContract>();
      try{	 // sql += " and ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(activeContent) + "%' " + YHDBUtility.escapeLike();
      	if (!YHUtility.isNullorEmpty(contract.getStaffName())) {
					conditionStr = " and STAFF_NAME like '%" + YHDBUtility.escapeLike(contract.getStaffName()) + "%' " + YHDBUtility.escapeLike();
				}
      	if (!YHUtility.isNullorEmpty(contract.getStaffContractNo())) {
					conditionStr += " and STAFF_CONTRACT_NO  like '%" + YHDBUtility.escapeLike(contract.getStaffContractNo()) + "%' " + YHDBUtility.escapeLike();
				}
      	if (!YHUtility.isNullorEmpty(contract.getContractType())) {
					conditionStr += " and CONTRACT_TYPE like '%" + YHDBUtility.escapeLike(contract.getContractType()) + "%' " + YHDBUtility.escapeLike();
				}
      	if (!YHUtility.isNullorEmpty(contract.getStatus())) {
					conditionStr += " and STATUS like '%" + YHDBUtility.escapeLike(contract.getStatus()) + "%' " + YHDBUtility.escapeLike();
				}
	        	
				if (!YHUtility.isNullorEmpty(makeContract1)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("MAKE_CONTRACT", makeContract1, ">=");
				}
				if (!YHUtility.isNullorEmpty(makeContract2)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("MAKE_CONTRACT", makeContract2, "<=");
				}
				if (!YHUtility.isNullorEmpty(trailOverTime1)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("TRAIL_OVER_TIME", trailOverTime1, ">=");
				}
				if (!YHUtility.isNullorEmpty(trailOverTime2)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("TRAIL_OVER_TIME", trailOverTime2, "<=");
				}
				if (!YHUtility.isNullorEmpty(contractEndTime1)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("CONTRACT_END_TIME", contractEndTime1, ">=");
				}
				if (!YHUtility.isNullorEmpty(contractEndTime2)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("CONTRACT_END_TIME", contractEndTime2, "<=");
				}
				String sql = "select COUNT(*) from oa_pm_employee_contract "
                   + " where (CREATE_USER_ID = "+ user.getSeqId()
                   + " or CREATE_DEPT_ID in "+ deptIdStr + ")"
      				     + conditionStr;
			
				ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();      
        if(rs.next()){
          return rs.getInt(1);
        }
      } catch (SQLException e){
        throw e;
      }finally{
         YHDBUtility.close(ps, rs, null);
      }    
       return 0;
   } 
	 
	 
	 /**
	  * 合同查询带分页的
	  * @param dbConn
	  * @param user
	  * @param page
	  * @return
	  * @throws Exception
	  */
	 public List<YHHrStaffContract> findContracts(Connection dbConn,  YHPerson user, YHPage page) throws Exception{
     YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
     String deptIdStr = logic.getHrManagerPriv(dbConn, user);
     
     PreparedStatement ps = null;
     ResultSet rs = null;
     String sql = " select SEQ_ID, STAFF_NAME,STAFF_CONTRACT_NO,CONTRACT_TYPE,MAKE_CONTRACT,STATUS from oa_pm_employee_contract "
                + " where CREATE_USER_ID = "+ user.getSeqId()
                + " or CREATE_DEPT_ID in "+ deptIdStr
           		  + "order by ADD_TIME desc";
     List<YHHrStaffContract> staff = new ArrayList<YHHrStaffContract>();
	   try{
	     ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
       ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());//游标用法，预显示最大行数       rs = ps.executeQuery();
       rs.first();//如指针用法，指向最上面位置
       rs.relative((page.getCurrentPageIndex()-1) * page.getPageSize() -1);//相对于求出每页显示的长度       
       while(rs.next()){
    	   YHHrStaffContract staffC = new YHHrStaffContract();
    	   staffC.setSeqId(rs.getInt("SEQ_ID"));
    	   staffC.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
    	   staffC.setStaffContractNo(rs.getString("STAFF_CONTRACT_NO"));
    	   staffC.setContractType(hrPublicIdTransName.getCodeUserName(dbConn,rs.getString("CONTRACT_TYPE")));
    	   staffC.setMakeContract(rs.getDate("MAKE_CONTRACT"));
    	   staffC.setStatus(hrPublicIdTransName.getCodeUserName(dbConn,rs.getString("STATUS")));
    	   staff.add(staffC);
       }
	  } catch (SQLException e){
	    throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }    
    return staff;
  }
	 /**
	  * 查询合同详细信息
	  * @param dbConn
	  * @param user
	  * @param ContractSeqId
	  * @return
	  * @throws Exception
	  */
	 
	 public List<YHHrStaffContract> contractXxInfo(Connection dbConn,  YHPerson user,int ContractSeqId) throws Exception{
		    PreparedStatement ps = null;
		    ResultSet rs = null;
		    List<YHHrStaffContract> staff = new ArrayList<YHHrStaffContract>();
		    
		    YHORM orm =new YHORM();
		    try{
		    	YHHrStaffContract sfc = (YHHrStaffContract) orm.loadObjSingle(dbConn, YHHrStaffContract.class, ContractSeqId);
//		    	ps = dbConn.prepareStatement(sql);
//		        rs = ps.executeQuery();
//		        if(rs.next()){
//		        	YHHrStaffContract sfc = new YHHrStaffContract();
//		        	sfc.setSeqId(rs.getInt("SEQ_ID"));
//		        	sfc.setStaffName(rs.getString("STAFF_NAME"));
//		        	sfc.setStaffContractNo(rs.getString("STAFF_CONTRACT_NO"));
//		        	sfc.setContractType(rs.getString("CONTRACT_TYPE"));
//		        	sfc.setContractSpecialization(rs.getString("CONTRACT_SPECIALIZATION"));
//		        	sfc.setStatus(rs.getString("STATUS"));
//		        	sfc.setMakeContract(rs.getDate("MAKE_CONTRACT"));
//		        	sfc.setTrailEffectiveTime(rs.getDate("TRAIL_EFFECTIVE_TIME"));
//		        	sfc.setTrailOverTime(rs.getDate("TRAIL_OVER_TIME"));
//		        	sfc.setProbationaryPeriod(rs.getString("PROBATIONARY_PERIOD"));//shiyong天数
//		        	sfc.setPassOrNot(rs.getString("PASS_OR_NOT"));//是否转正
//		        	sfc.setProbationEffectiveDate(rs.getDate("PROBATION_EFFECTIVE_DATE"));//合同生效日期
//		        	sfc.setProbationEndDate(rs.getDate("PROBATION_END_DATE"));//合同转正日期  
//		        	sfc.setRemoveOrNot(rs.getString("REMOVE_OR_NOT"));//合同是否解除 1:是 0：否 
//		        	sfc.setContractRemoveTime(rs.getDate("CONTRACT_REMOVE_TIME"));// 
//		        	sfc.setSignTimes(rs.getString("SIGN_TIMES"));
//		        	sfc.setAddTime(rs.getString("ADD_TIME"));
//		        	sfc.setAttachmentId("ATTACHMENT_ID");
//		        	sfc.setAttachmentName("ATTACHMENT_NAME");
		            staff.add(sfc);
//		          }
	  }catch (SQLException e){
		     throw e;
	    }finally{
	      YHDBUtility.close(ps, rs, null);
	    }    
	    return staff;
	}
 
	 
	  /**
	   * 删除合同的基本信息
	   * @param conn
	   * @param person
	   * @return
	   * @throws Exception
	   */
	  public static int delContractInfo(Connection conn, YHPerson person ,int noHiddenId ) throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;   
	    int ok =0;
	    String deleteSql = "delete from oa_pm_employee_contract where seq_id ="+noHiddenId;
	    
	    try{
	       ps = conn.prepareStatement(deleteSql);
	       ok = ps.executeUpdate();
	    }catch(Exception ex){
	      throw ex;
	    }finally{
	      YHDBUtility.close(ps, rs, null);
	    }
	    
	    return ok;
	  }
	  /**
	     * 删除合同所选的信息

	     * @param dbConn
	     * @param loginperson
	     * @param deleteStr
	     * @return
	     * @throws IOException
	     * @throws Exception
	     */
	    public int deleteContractInfo(Connection dbConn,String deleteStr) throws IOException, Exception {
	      String deletenewsCommentSql = "";
	      String deletenewsSql = "";
	      Statement stmt = null;
	      ResultSet rs = null;
	        
	      if(!YHUtility.isNullorEmpty(deleteStr)){
	          String[] deleteStrs = deleteStr.split(",");
	          deleteStr = "";
	          for(int i = 0 ;i < deleteStrs.length ; i++){
	             deleteStr +=   deleteStrs[i]  + ",";
	          }
	             deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
	      }
	      int success = 0;
	      try {
	          stmt = dbConn.createStatement();
	          deletenewsCommentSql = "delete  from oa_pm_employee_contract  where seq_id in  ("+ deleteStr + ")";
	          success = stmt.executeUpdate(deletenewsCommentSql);
	      } catch (Exception e) {
	          e.printStackTrace();
	      }finally {
	           YHDBUtility.close(stmt, rs, null);
	      }
	       return success;
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
  public List<YHHrStaffContract> queryContractInfo(Connection dbConn,YHPerson user, YHPage page,YHHrStaffContract contract,String makeContract1,String makeContract2,String trailOverTime1,String trailOverTime2,String contractEndTime1,String contractEndTime2) throws Exception{
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, user);  
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    String conditionStr = "";
    List<YHHrStaffContract> staff = new ArrayList<YHHrStaffContract>();
    try{	 // sql += " and ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(activeContent) + "%' " + YHDBUtility.escapeLike();
      	if (!YHUtility.isNullorEmpty(contract.getStaffName())) {
					conditionStr = " and STAFF_NAME like '%" + YHDBUtility.escapeLike(contract.getStaffName()) + "%' " + YHDBUtility.escapeLike();
				}
      	if (!YHUtility.isNullorEmpty(contract.getStaffContractNo())) {
					conditionStr += " and STAFF_CONTRACT_NO like '%" + YHDBUtility.escapeLike(contract.getStaffContractNo()) + "%' " + YHDBUtility.escapeLike();
				}
      	if (!YHUtility.isNullorEmpty(contract.getContractType())) {
					conditionStr += " and CONTRACT_TYPE like '%" + YHDBUtility.escapeLike(contract.getContractType()) + "%' " + YHDBUtility.escapeLike();
				}
      	if (!YHUtility.isNullorEmpty(contract.getStatus())) {
					conditionStr += " and STATUS like '%" + YHDBUtility.escapeLike(contract.getStatus()) + "%' " + YHDBUtility.escapeLike();
				}
				if (!YHUtility.isNullorEmpty(makeContract1)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("MAKE_CONTRACT", makeContract1, ">=");
				}
				if (!YHUtility.isNullorEmpty(makeContract2)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("MAKE_CONTRACT", makeContract2, "<=");
				}
				if (!YHUtility.isNullorEmpty(trailOverTime1)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("TRAIL_OVER_TIME", trailOverTime1, ">=");
				}
				if (!YHUtility.isNullorEmpty(trailOverTime2)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("TRAIL_OVER_TIME", trailOverTime2, "<=");
				}
				if (!YHUtility.isNullorEmpty(contractEndTime1)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("CONTRACT_END_TIME", contractEndTime1, ">=");
				}
				if (!YHUtility.isNullorEmpty(contractEndTime2)) {
					conditionStr += " and " + YHDBUtility.getDateFilter("CONTRACT_END_TIME", contractEndTime2, "<=");
				}
				String sql = " select SEQ_ID, STAFF_NAME,STAFF_CONTRACT_NO,CONTRACT_TYPE,CONTRACT_SPECIALIZATION,STATUS,MAKE_CONTRACT"
        	     		 + " ,TRAIL_EFFECTIVE_TIME,TRAIL_OVER_TIME,PROBATIONARY_PERIOD,PASS_OR_NOT,PROBATION_EFFECTIVE_DATE,PROBATION_END_DATE,REMOVE_OR_NOT"
        	    		 + " ,CONTRACT_REMOVE_TIME,SIGN_TIMES,ADD_TIME from oa_pm_employee_contract "
                   + " where (CREATE_USER_ID = "+ user.getSeqId()
                   + " or CREATE_DEPT_ID in "+ deptIdStr + ")"
        	    		 + conditionStr + " order by ADD_TIME desc";
			
			  ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
	      ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());//游标用法，预显示最大行数
	      rs = ps.executeQuery();
	      rs.first();//如指针用法，指向最上面位置
	      rs.relative((page.getCurrentPageIndex()-1) * page.getPageSize() -1);//相对于求出每页显示的长度       
	      while(rs.next()){
	    	  YHHrStaffContract staffC = new YHHrStaffContract();
	    	  staffC.setSeqId(rs.getInt("SEQ_ID"));
	    	  staffC.setStaffName(hrPublicIdTransName.getUserName(dbConn, Integer.valueOf(rs.getString("STAFF_NAME"))));
	    	  staffC.setStaffContractNo(rs.getString("STAFF_CONTRACT_NO"));
	    	  staffC.setContractType(hrPublicIdTransName.getCodeUserName(dbConn, rs.getString("CONTRACT_TYPE")));
	    	  staffC.setMakeContract(rs.getDate("MAKE_CONTRACT"));
	    	  staffC.setStatus(hrPublicIdTransName.getCodeUserName(dbConn, rs.getString("STATUS")));
	    	  staff.add(staffC);
        }
      } catch (SQLException e){
        throw e;
      }finally{
         YHDBUtility.close(ps, rs, null);
      }    
       return staff;
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
        YHHrStaffContract staffContract = (YHHrStaffContract) orm.loadObjSingle(dbConn, YHHrStaffContract.class, seqId);
        String dbAttachId = "";
        String dbAttachName = "";
        if (staffContract != null) {
          dbAttachId = YHUtility.null2Empty(staffContract.getAttachmentId());
          dbAttachName = YHUtility.null2Empty(staffContract.getAttachmentName());
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
            staffContract.setAttachmentId(attachmentId.trim());
            staffContract.setAttachmentName(attachmentName.trim());
            orm.updateSingle(dbConn, staffContract);
            returnFlag = true;
          }
        }
      } catch (Exception e) {
        throw e;
      }
      return returnFlag;
    }
		
}

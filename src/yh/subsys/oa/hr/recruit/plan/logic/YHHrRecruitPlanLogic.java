package yh.subsys.oa.hr.recruit.plan.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import yh.subsys.oa.hr.recruit.plan.data.YHHrRecruitPlan;
import yh.subsys.oa.hr.setting.act.YHHrSetOtherAct;

public class YHHrRecruitPlanLogic {
  private static Logger log = Logger.getLogger(YHHrRecruitPlanLogic.class);
  
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
   * 新建招聘计划
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setNewRecruitPlanValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String planNo = fileForm.getParameter("planNo");
    String planName = fileForm.getParameter("planName");
    String planDitch = fileForm.getParameter("planDitch");
    String planBcws = fileForm.getParameter("planBcws");
    String startDateStr = fileForm.getParameter("startDate");
    String endDateStr = fileForm.getParameter("endDate");
    String planRecrNo = fileForm.getParameter("planRecrNo");
    String approvePerson = fileForm.getParameter("approvePerson");
    String recruitDirection = fileForm.getParameter("recruitDirection");
    String recruitRemark = fileForm.getParameter("recruitRemark");
    String smsRemind = fileForm.getParameter("smsRemind");
    String sms2Remind = fileForm.getParameter("sms2Remind");

    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentIds = (String) map.get("attachmentIds");
    String attachmentNames = (String) map.get("attachmentNames");

    try{
      YHHrRecruitPlan recruitPlan = new YHHrRecruitPlan();
      recruitPlan.setPlanNo(planNo);
      recruitPlan.setPlanName(planName);
      recruitPlan.setPlanDitch(planDitch);
      if(!YHUtility.isNullorEmpty(planBcws)){
        recruitPlan.setPlanBcws(Double.parseDouble(planBcws));
      }
      if(!YHUtility.isNullorEmpty(startDateStr)){
        Date startDate = YHUtility.parseDate("yyyy-MM-dd", startDateStr);
        recruitPlan.setStartDate(startDate);
      }
      if(!YHUtility.isNullorEmpty(endDateStr)){
        Date endDate = YHUtility.parseDate("yyyy-MM-dd", endDateStr);
        recruitPlan.setEndDate(endDate);
      }
      recruitPlan.setPlanRecrNo(planRecrNo);
      recruitPlan.setApprovePerson(approvePerson);
      recruitPlan.setRecruitDirection(recruitDirection);
      recruitPlan.setRecruitRemark(recruitRemark);
      recruitPlan.setPlanStatus(0);
      recruitPlan.setRegisterTime(YHUtility.parseTimeStamp());
      recruitPlan.setAddTime(YHUtility.parseTimeStamp());
      recruitPlan.setCreateUserId(String.valueOf(person.getSeqId()));
      if(attachFlag){
        recruitPlan.setAttachmentId(attachmentIds);
        recruitPlan.setAttachmentName(attachmentNames);
      }
      orm.saveSingle(dbConn, recruitPlan);
      int maxSeqId = this.getMaxSeqId(dbConn);
      YHMobileSms2Logic sbl = new YHMobileSms2Logic();
      String remindUrl = "/subsys/oa/hr/recruit/plan/detail.jsp?seqId=" + maxSeqId + "&openFlag=1&openWidth=860&openHeight=650";
      String smsContent = "请查看招聘计划信息！";
      // 短信提醒
      if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
        this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), approvePerson, "63", remindUrl, new Date());
      }
      // 手机提醒
      if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
        smsContent = "OA招聘计划:请查看招聘计划" + planNo;
        sbl.remindByMobileSms(dbConn, approvePerson, person.getSeqId(), smsContent, new Date());
      }
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 招聘计划  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getRecruitPlanJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " select SEQ_ID, PLAN_NO, PLAN_NAME, PLAN_RECR_NO, START_DATE, PLAN_STATUS "
                 + " from oa_pm_enroll_plan "
                 + " ORDER BY SEQ_ID desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHHrRecruitPlan getRecruitPlanDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrRecruitPlan) orm.loadObjSingle(conn, YHHrRecruitPlan.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 修改招聘需求
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void updateRecruitPlanInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String seqIdStr = fileForm.getParameter("seqId");
    String planNo = fileForm.getParameter("planNo");
    String planName = fileForm.getParameter("planName");
    String planDitch = fileForm.getParameter("planDitch");
    String planBcws = fileForm.getParameter("planBcws");
    String startDateStr = fileForm.getParameter("startDate");
    String endDateStr = fileForm.getParameter("endDate");
    String planRecrNo = fileForm.getParameter("planRecrNo");
    String approvePerson = fileForm.getParameter("approvePerson");
    String recruitDirection = fileForm.getParameter("recruitDirection");
    String recruitRemark = fileForm.getParameter("recruitRemark");
    String smsRemind = fileForm.getParameter("smsRemind");
    String sms2Remind = fileForm.getParameter("sms2Remind");
    int seqId = 0;
    if (!YHUtility.isNullorEmpty(seqIdStr)) {
      seqId = Integer.parseInt(seqIdStr);
    }
    
    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentIds = (String) map.get("attachmentIds");
    String attachmentNames = (String) map.get("attachmentNames");
    
    try {
      YHHrRecruitPlan recruitPlan = (YHHrRecruitPlan) orm.loadObjSingle(dbConn, YHHrRecruitPlan.class, seqId);
      if (recruitPlan != null) {
        String dbAttachId = YHUtility.null2Empty(recruitPlan.getAttachmentId());
        String dbAttachName = YHUtility.null2Empty(recruitPlan.getAttachmentName());
      
        recruitPlan.setPlanNo(planNo);
        recruitPlan.setPlanName(planName);
        recruitPlan.setPlanDitch(planDitch);
        if(!YHUtility.isNullorEmpty(planBcws)){
          recruitPlan.setPlanBcws(Double.parseDouble(planBcws));
        }
        if(!YHUtility.isNullorEmpty(startDateStr)){
          Date startDate = YHUtility.parseDate("yyyy-MM-dd", startDateStr);
          recruitPlan.setStartDate(startDate);
        }
        if(!YHUtility.isNullorEmpty(endDateStr)){
          Date endDate = YHUtility.parseDate("yyyy-MM-dd", endDateStr);
          recruitPlan.setEndDate(endDate);
        }
        recruitPlan.setPlanRecrNo(planRecrNo);
        recruitPlan.setApprovePerson(approvePerson);
        recruitPlan.setRecruitDirection(recruitDirection);
        recruitPlan.setRecruitRemark(recruitRemark);
        recruitPlan.setPlanStatus(0);
        recruitPlan.setRegisterTime(YHUtility.parseTimeStamp());
        recruitPlan.setAddTime(YHUtility.parseTimeStamp());
        recruitPlan.setCreateUserId(String.valueOf(person.getSeqId()));
        if(attachFlag){
          recruitPlan.setAttachmentId(dbAttachId.trim() + attachmentIds.trim());
          recruitPlan.setAttachmentName(dbAttachName.trim() + attachmentNames.trim());
        }
        orm.updateSingle(dbConn, recruitPlan);
        YHMobileSms2Logic sbl = new YHMobileSms2Logic();
        String remindUrl = "/subsys/oa/hr/recruit/plan/detail.jsp?seqId=" + seqId + "&openFlag=1&openWidth=860&openHeight=650";
        String smsContent = "请查看招聘计划信息！";
        // 短信提醒
        if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
          this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), approvePerson, "63", remindUrl, new Date());
        }
        // 手机提醒
        if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
          smsContent = "OA招聘计划:请查看招聘计划" + planNo;
          sbl.remindByMobileSms(dbConn, approvePerson, person.getSeqId(), smsContent, new Date());
        }
      }
    } catch (Exception e) {
      throw e;
    }
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
          YHHrRecruitPlan recruitPlan = (YHHrRecruitPlan) orm.loadObjSingle(dbConn, YHHrRecruitPlan.class, Integer.parseInt(seqId));
          String attachmentId = YHUtility.null2Empty(recruitPlan.getAttachmentId());
          String attachmentName = YHUtility.null2Empty(recruitPlan.getAttachmentName());
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
          orm.deleteSingle(dbConn, recruitPlan);
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }
  
  
  
  /**
   * 获取最大的SeqId值
   * 
   * @param dbConn
   * @return
   */
  public int getMaxSeqId(Connection dbConn) {
    String sql = "select SEQ_ID from oa_pm_enroll_plan where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_enroll_plan )";
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
  
  public String getDeptHrManager(Connection dbConn, YHPerson person) {
    String sql = " select DEPT_HR_MANAGER from oa_pm_manager where DEPT_ID = " + person.getDeptId();
    String deptHrManager = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        deptHrManager = rs.getString("DEPT_HR_MANAGER");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return deptHrManager;
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
      YHHrRecruitPlan recruitPlan = (YHHrRecruitPlan) orm.loadObjSingle(dbConn, YHHrRecruitPlan.class, seqId);
      String dbAttachId = "";
      String dbAttachName = "";
      if (recruitPlan != null) {
        dbAttachId = YHUtility.null2Empty(recruitPlan.getAttachmentId());
        dbAttachName = YHUtility.null2Empty(recruitPlan.getAttachmentName());
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
          recruitPlan.setAttachmentId(attachmentId.trim());
          recruitPlan.setAttachmentName(attachmentName.trim());
          orm.updateSingle(dbConn, recruitPlan);
          returnFlag = true;
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return returnFlag;
  }
  
  /**
   * 审批计划  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getRecruitPlanWaitJsonLogic(Connection dbConn, Map request, YHPerson person, String planStatus) throws Exception {
    try {
      String sql = " select SEQ_ID, PLAN_NO, PLAN_NAME, PLAN_RECR_NO, START_DATE, PLAN_STATUS "
                 + " from oa_pm_enroll_plan "
                 + " where PLAN_STATUS =" + planStatus + " and APPROVE_PERSON =" + person.getSeqId()
                 + " ORDER BY ADD_TIME desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 批准/不批准招聘计划
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public void doApproval(Connection conn, Map map, YHPerson person) throws Exception {
    String seqId = (String)map.get("seqId");
    String flag = (String)map.get("flag");
    String approveDate = (String)map.get("approveDate");
    String approveComment = (String)map.get("approveComment");
    String createUserId = (String)map.get("createUserId");
    String sql = " update oa_pm_enroll_plan set PLAN_STATUS = " + flag + ", APPROVE_DATE = ?,APPROVE_COMMENT = ? ,ADD_TIME = ? where SEQ_ID = " + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      if(YHUtility.isNullorEmpty(approveDate)){
        ps.setTimestamp(1, null);
      }
      else{
        ps.setTimestamp(1, new Timestamp(YHUtility.parseDate(approveDate).getTime()));
      }
      ps.setString(2, approveComment);
      ps.setTimestamp(3, YHUtility.parseTimeStamp());
      ps.executeUpdate();
      String remindUrl = "/subsys/oa/hr/recruit/plan/detail.jsp?seqId=" + seqId + "&openFlag=1&openWidth=860&openHeight=650";
      String smsContent = "";
      if(flag.equals("1")){
        smsContent = "已批准您的招聘计划！"+approveComment;
      }
      else if(flag.equals("2")){
        smsContent = "已驳回您的招聘计划！"+approveComment;
      }
      // 短信提醒
      this.doSmsBackTime(conn, smsContent, person.getSeqId(), createUserId, "63", remindUrl, new Date());
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 招聘计划查询
   * 
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
  public String queryRecruitPlanLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    String planName = (String) map.get("planName");
    String planNo = (String) map.get("planNo");
    String planStatus = (String) map.get("planStatus");
    String approvePerson = (String) map.get("approvePerson");
    String recruitDirection = (String) map.get("recruitDirection");
    String recruitRemark = (String) map.get("recruitRemark");
    String startDate1 = (String) map.get("startDate1");
    String startDate2 = (String) map.get("startDate2");
    String endDate1 = (String) map.get("endDate1");
    String endDate2 = (String) map.get("endDate2");
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(planName)) {
        conditionStr = " and PLAN_NAME like '%" + YHDBUtility.escapeLike(planName) + "%'";
      }
      if (!YHUtility.isNullorEmpty(planNo)) {
        conditionStr = " and PLAN_NO like '%" + YHDBUtility.escapeLike(planNo) + "%'";
      }
      if (!YHUtility.isNullorEmpty(planStatus)) {
        conditionStr = " and PLAN_STATUS ='" + YHDBUtility.escapeLike(planStatus) + "'";
      }
      if (!YHUtility.isNullorEmpty(approvePerson)) {
        conditionStr = " and APPROVE_PERSON ='" + YHDBUtility.escapeLike(approvePerson) + "'";
      }
      if (!YHUtility.isNullorEmpty(recruitDirection)) {
        conditionStr = " and RECRUIT_DIRECTION like '%" + YHDBUtility.escapeLike(recruitDirection) + "%'";
      }
      if (!YHUtility.isNullorEmpty(recruitRemark)) {
        conditionStr = " and RECRUIT_REMARK like '%" + YHDBUtility.escapeLike(recruitRemark) + "%'";
      }
      if (!YHUtility.isNullorEmpty(startDate1)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("START_DATE", startDate1, ">=");
      }
      if (!YHUtility.isNullorEmpty(startDate2)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("START_DATE", startDate2, "<=");
      }
      if (!YHUtility.isNullorEmpty(endDate1)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("END_DATE", endDate1, ">=");
      }
      if (!YHUtility.isNullorEmpty(endDate2)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("END_DATE", endDate2, "<=");
      }
      sql = " select SEQ_ID, PLAN_NO, PLAN_NAME, PLAN_RECR_NO, START_DATE, PLAN_STATUS "
          + " from oa_pm_enroll_plan "
          + " where  APPROVE_PERSON =" + person.getSeqId() + conditionStr + " ORDER BY ADD_TIME desc";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
}

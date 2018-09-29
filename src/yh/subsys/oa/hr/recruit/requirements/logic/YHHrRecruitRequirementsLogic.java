package yh.subsys.oa.hr.recruit.requirements.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.notify.data.YHNotify;
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
import yh.subsys.oa.hr.recruit.requirements.data.YHHrRecruitRequirements;
import yh.subsys.oa.hr.setting.act.YHHrSetOtherAct;

public class YHHrRecruitRequirementsLogic {

  private static Logger log = Logger.getLogger(YHHrRecruitRequirementsLogic.class);

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
   * 新建招聘需求
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setNewRecruitRequirementsValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String requNo = fileForm.getParameter("requNo");
    String requJob = fileForm.getParameter("requJob");
    String requTimeStr = fileForm.getParameter("requTime");
    String requNum = fileForm.getParameter("requNum");
    String requDept = fileForm.getParameter("requDept");
    String remark = fileForm.getParameter("remark");
    String requRequires = fileForm.getParameter("requRequires");
    String smsRemind = fileForm.getParameter("smsRemind");
    String sms2Remind = fileForm.getParameter("sms2Remind");

    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentIds = (String) map.get("attachmentIds");
    String attachmentNames = (String) map.get("attachmentNames");

    try{
      YHHrRecruitRequirements recruitRequirements = new YHHrRecruitRequirements();
      recruitRequirements.setRequNo(requNo);
      recruitRequirements.setRequJob(requJob);
      if(!YHUtility.isNullorEmpty(requTimeStr)){
        Date requTime = YHUtility.parseDate("yyyy-MM-dd", requTimeStr);
        recruitRequirements.setRequTime(requTime);
      }
      recruitRequirements.setRequNum(requNum);
      recruitRequirements.setRequDept(requDept);
      recruitRequirements.setRemark(remark);
      recruitRequirements.setRequRequires(requRequires);
      recruitRequirements.setPetitioner(person.getUserName());
      recruitRequirements.setAddTime(YHUtility.parseTimeStamp());
      recruitRequirements.setCreateUserId(String.valueOf(person.getSeqId()));
      if(attachFlag){
        recruitRequirements.setAttachmentId(attachmentIds);
        recruitRequirements.setAttachmentName(attachmentNames);
      }
      orm.saveSingle(dbConn, recruitRequirements);
      int maxSeqId = this.getMaxSeqId(dbConn);
      YHMobileSms2Logic sbl = new YHMobileSms2Logic();
      String remindUrl = "/subsys/oa/hr/recruit/requirements/detail.jsp?seqId=" + maxSeqId + "&openFlag=1&openWidth=860&openHeight=650";
      String smsContent = "请查看招聘需求信息！";
      String getDeptHrManager = getDeptHrManager(dbConn,person);
      // 短信提醒
      if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
        this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), getDeptHrManager, "56", remindUrl, new Date());
      }
      // 手机提醒
      if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
        smsContent = "OA招聘需求:请查看招聘需求" + requNo;
        sbl.remindByMobileSms(dbConn, getDeptHrManager, person.getSeqId(), smsContent, new Date());
      }
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 招聘需求  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getRecruitRequirementsJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " select SEQ_ID, REQU_NO, REQU_JOB, REQU_NUM, REQU_DEPT, REQU_TIME "
                 + " from HR_RECRUIT_REQUIREMENTS "
                 + " ORDER BY SEQ_ID desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取部门名称
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public String getDeptName(Connection conn, String deptIdStr) throws Exception {
    if (YHUtility.isNullorEmpty(deptIdStr)) {
      deptIdStr = "-1";
    }
    if (deptIdStr.endsWith(",")) {
      deptIdStr = deptIdStr.substring(0, deptIdStr.length() - 1);
    }
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID IN (" + deptIdStr + ")";
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
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHHrRecruitRequirements getRecruitRequirementsDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrRecruitRequirements) orm.loadObjSingle(conn, YHHrRecruitRequirements.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 人事招聘需求
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void updateRecruitRequirementsInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String seqIdStr = fileForm.getParameter("seqId");
    String requNo = fileForm.getParameter("requNo");
    String requJob = fileForm.getParameter("requJob");
    String requTimeStr = fileForm.getParameter("requTime");
    String requNum = fileForm.getParameter("requNum");
    String requDept = fileForm.getParameter("requDept");
    String remark = fileForm.getParameter("remark");
    String requRequires = fileForm.getParameter("requRequires");
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
      YHHrRecruitRequirements recruitRequirements = (YHHrRecruitRequirements) orm.loadObjSingle(dbConn, YHHrRecruitRequirements.class, seqId);
      if (recruitRequirements != null) {
        String dbAttachId = YHUtility.null2Empty(recruitRequirements.getAttachmentId());
        String dbAttachName = YHUtility.null2Empty(recruitRequirements.getAttachmentName());
      
        recruitRequirements.setRequNo(requNo);
        recruitRequirements.setRequJob(requJob);
        if(!YHUtility.isNullorEmpty(requTimeStr)){
          Date requTime = YHUtility.parseDate("yyyy-MM-dd", requTimeStr);
          recruitRequirements.setRequTime(requTime);
        }
        recruitRequirements.setRequNum(requNum);
        recruitRequirements.setRequDept(requDept);
        recruitRequirements.setRemark(remark);
        recruitRequirements.setRequRequires(requRequires);
        recruitRequirements.setPetitioner(person.getUserName());
        recruitRequirements.setAddTime(YHUtility.parseTimeStamp());
        recruitRequirements.setCreateUserId(String.valueOf(person.getSeqId()));
        if(attachFlag){
          recruitRequirements.setAttachmentId(dbAttachId.trim() + attachmentIds.trim());
          recruitRequirements.setAttachmentName(dbAttachName.trim() + attachmentNames.trim());
        }
        orm.updateSingle(dbConn, recruitRequirements);
        YHMobileSms2Logic sbl = new YHMobileSms2Logic();
        String remindUrl = "/subsys/oa/hr/recruit/requirements/detail.jsp?seqId=" + seqId + "&openFlag=1&openWidth=860&openHeight=650";
        String smsContent = "请查看招聘需求信息！";
        String getDeptHrManager = getDeptHrManager(dbConn,person);
        // 短信提醒
        if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
          this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), getDeptHrManager, "56", remindUrl, new Date());
        }
        // 手机提醒
        if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
          smsContent = "OA招聘需求:请查看招聘需求" + requNo;
          sbl.remindByMobileSms(dbConn, getDeptHrManager, person.getSeqId(), smsContent, new Date());
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
          YHHrRecruitRequirements recruitRequirements = (YHHrRecruitRequirements) orm.loadObjSingle(dbConn, YHHrRecruitRequirements.class, Integer.parseInt(seqId));
          String attachmentId = YHUtility.null2Empty(recruitRequirements.getAttachmentId());
          String attachmentName = YHUtility.null2Empty(recruitRequirements.getAttachmentName());
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
          orm.deleteSingle(dbConn, recruitRequirements);
        }
      }
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
   * 获取最大的SeqId值
   * 
   * @param dbConn
   * @return
   */
  public int getMaxSeqId(Connection dbConn) {
    String sql = "select SEQ_ID from oa_pm_employee_transfer where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_employee_transfer )";
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
   * 招聘需求查询
   * @param  dbConn
   * @param map
   * @param person
   * 
   * 
   * */
  public String queryRecruitRequirementsJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    String requNo = (String) map.get("requNo");
    String requNum = (String) map.get("requNum");
    String requJob = (String) map.get("requJob");
    String requDeptId = (String) map.get("requDeptId");   
    String startTime = (String) map.get("startTime");
    String endTime = (String) map.get("endTime");
   
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(requDeptId)) {
     
     
        conditionStr += " and c1.REQU_DEPT like '%" +requDeptId+"%' ";
      }
      if (!YHUtility.isNullorEmpty(requNum)) {
        
        conditionStr += " and " + YHDBUtility.findInSet(requNum, "c1.REQU_NUM");
      }
      if (!YHUtility.isNullorEmpty(requJob)) {
        conditionStr += " and " + YHDBUtility.findInSet(requJob, "c1.REQU_JOB");
      }
   
      if (!YHUtility.isNullorEmpty(requNo)) {
        conditionStr += " and " + YHDBUtility.findInSet(requNo, "c1.REQU_NO");
      }
     
      if (!YHUtility.isNullorEmpty(startTime)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("c1.REQU_TIME", startTime, ">=");
      }
      if (!YHUtility.isNullorEmpty(endTime)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("c1.REQU_TIME", endTime, "<=");
      }
   
    
      sql = " select  c1.SEQ_ID,c1.REQU_NO, c1.REQU_JOB,c1.REQU_NUM,c1.REQU_DEPT,c1.REQU_TIME"
          + " from HR_RECRUIT_REQUIREMENTS c1 "
          + " where 1=1" + conditionStr + " ORDER BY c1.ADD_TIME desc";
   
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  
  /**
   * 查询导出到excel
   * @param conn
   * @param notify
   * @param person
   * @param map
   */
  public List<Map<String, String>> queryToExcel(Connection conn,YHPerson person,Map<Object, Object> map)throws Exception{
    YHORM orm = new YHORM();
    Statement stmt = null;
    ResultSet rs = null;   
    List<Map<String, String>> RecruitRequirementsList = new ArrayList();
    //String seqId= (String) map.get("seqId");
    String requNo = (String) map.get("requNo");
    String requNum = (String) map.get("requNum");
    String requJob = (String) map.get("requJob");
    String requDeptId = (String) map.get("requDeptId");   
    String startTime = (String) map.get("startTime");
    String endTime = (String) map.get("endTime");
    try{
    String conditionStr = "";
    String sql = "";
  //    if (!YHUtility.isNullorEmpty(seqId)) {
   //     seqId=seqId.replaceAll(",","','");
    //    conditionStr += " and c1.SEQ_ID in ('"+seqId+"') ";
   //   }
      if (!YHUtility.isNullorEmpty(requDeptId)) {
        conditionStr += " and " + YHDBUtility.findInSet(requDeptId, "c1.REQU_DEPT");
      }
      if (!YHUtility.isNullorEmpty(requNum)) {        
        conditionStr += " and " + YHDBUtility.findInSet(requNum, "c1.REQU_NUM");
      }
      if (!YHUtility.isNullorEmpty(requJob)) {
        conditionStr += " and " + YHDBUtility.findInSet(requJob, "c1.REQU_JOB");
      }   
      if (!YHUtility.isNullorEmpty(requNo)) {
        conditionStr += " and " + YHDBUtility.findInSet(requNo, "c1.REQU_NO");
      }     
      if (!YHUtility.isNullorEmpty(startTime)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("c1.REQU_TIME", startTime, ">=");
      }
      if (!YHUtility.isNullorEmpty(endTime)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("c1.REQU_TIME", endTime, "<=");
      }    
      sql = " select  c1.SEQ_ID,c1.REQU_NO, c1.REQU_JOB,c1.REQU_NUM,c1.REQU_DEPT,c1.PETITIONER,c1.ATTACHMENT_NAME,c1.REQU_TIME,c1.REMARK,c1.ADD_TIME "
          + " from HR_RECRUIT_REQUIREMENTS c1 "
          + " where 1=1" + conditionStr + " ORDER BY c1.ADD_TIME desc";  

     try {
      stmt = conn.createStatement(); 
      rs = stmt.executeQuery(sql);
     while(rs.next()){
      Map<String, String> Recruittemp = new HashMap<String, String>(); 
        Statement stmtt = null;
        ResultSet rss = null;     
     //   int seqId = rs.getInt("SEQ_ID");
        requNo = rs.getString("REQU_NO");
        requJob = rs.getString("REQU_JOB");
        requNum = rs.getString("REQU_NUM");
        String requDept =rs.getString("REQU_DEPT");
        String petitioner = rs.getString("PETITIONER");
        String attachmentName = rs.getString("ATTACHMENT_NAME");
        Date requTime = rs.getDate("REQU_TIME");
        String remark = rs.getString("REMARK");
        Date addTime = rs.getDate("ADD_TIME");
     
       //得到需求部门的名称
        requDept=this.getDeptName(conn,requDept);        
        
      //  Recruittemp.put("seqId", String.valueOf(seqId));       
        Recruittemp.put("requNo", requNo);       
        Recruittemp.put("requJob", requJob);               
        Recruittemp.put("requNum", requNum);   
        Recruittemp.put("requDept", requDept);       
        Recruittemp.put("petitioner", petitioner);
        Recruittemp.put("attachmentName", attachmentName);       
        Recruittemp.put("requTime",String.valueOf(requTime));        
        Recruittemp.put("remark", remark);      
        Recruittemp.put("addTime",String.valueOf(addTime));
        RecruitRequirementsList.add(Recruittemp);
      }      
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return RecruitRequirementsList;   
  }catch (Exception e) {
    throw e;
  }
}
  /**
   * 导出到excel
   * @param conn
   * @param notify
   * @param person
   * @param map
   */
  public List<Map<String, String>> toExcel(Connection conn,YHPerson person,Map<Object, Object> map)throws Exception{
    YHORM orm = new YHORM();
    Statement stmt = null;
    ResultSet rs = null;   
    List<Map<String, String>> RecruitRequirementsList = new ArrayList();
    String seqId= (String) map.get("seqId");
    String requNo = (String) map.get("requNo");
    String requNum = (String) map.get("requNum");
    String requJob = (String) map.get("requJob");
    String requDeptId = (String) map.get("requDeptId");   
    String startTime = (String) map.get("startTime");
    String endTime = (String) map.get("endTime");
    try{
    String conditionStr = "";
    String sql = "";
     if (!YHUtility.isNullorEmpty(seqId)) {
        seqId=seqId.replaceAll(",","','");
        conditionStr += " and c1.SEQ_ID in ('"+seqId+"') ";
      }
      if (!YHUtility.isNullorEmpty(requDeptId)) {
        conditionStr += " and " + YHDBUtility.findInSet(requDeptId, "c1.REQU_DEPT");
      }
      if (!YHUtility.isNullorEmpty(requNum)) {        
        conditionStr += " and " + YHDBUtility.findInSet(requNum, "c1.REQU_NUM");
      }
      if (!YHUtility.isNullorEmpty(requJob)) {
        conditionStr += " and " + YHDBUtility.findInSet(requJob, "c1.REQU_JOB");
      }   
      if (!YHUtility.isNullorEmpty(requNo)) {
        conditionStr += " and " + YHDBUtility.findInSet(requNo, "c1.REQU_NO");
      }     
      if (!YHUtility.isNullorEmpty(startTime)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("c1.REQU_TIME", startTime, ">=");
      }
      if (!YHUtility.isNullorEmpty(endTime)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("c1.REQU_TIME", endTime, "<=");
      }    
      sql = " select  c1.SEQ_ID,c1.REQU_NO, c1.REQU_JOB,c1.REQU_NUM,c1.REQU_DEPT,c1.PETITIONER,c1.ATTACHMENT_NAME,c1.REQU_TIME,c1.REMARK,c1.ADD_TIME "
          + " from HR_RECRUIT_REQUIREMENTS c1 "
          + " where 1=1" + conditionStr + " ORDER BY c1.ADD_TIME desc";  

     try {
      stmt = conn.createStatement(); 
      rs = stmt.executeQuery(sql);
     while(rs.next()){
      Map<String, String> Recruittemp = new HashMap<String, String>(); 
        Statement stmtt = null;
        ResultSet rss = null;     
     //   int seqId = rs.getInt("SEQ_ID");
        requNo = rs.getString("REQU_NO");
        requJob = rs.getString("REQU_JOB");
        requNum = rs.getString("REQU_NUM");
        String requDept =rs.getString("REQU_DEPT");
        String petitioner = rs.getString("PETITIONER");
        String attachmentName = rs.getString("ATTACHMENT_NAME");
        Date requTime = rs.getDate("REQU_TIME");
        String remark = rs.getString("REMARK");
        Date addTime = rs.getDate("ADD_TIME");
     
       //得到需求部门的名称
        requDept=this.getDeptName(conn,requDept);        
        
      //  Recruittemp.put("seqId", String.valueOf(seqId));       
        Recruittemp.put("requNo", requNo);       
        Recruittemp.put("requJob", requJob);               
        Recruittemp.put("requNum", requNum);   
        Recruittemp.put("requDept", requDept);       
        Recruittemp.put("petitioner", petitioner);
        Recruittemp.put("attachmentName", attachmentName);       
        Recruittemp.put("requTime",String.valueOf(requTime));        
        Recruittemp.put("remark", remark);      
        Recruittemp.put("addTime",String.valueOf(addTime));
        RecruitRequirementsList.add(Recruittemp);
      }      
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return RecruitRequirementsList;   
  }catch (Exception e) {
    throw e;
  }
}
  /**
   * 工具方法，把集合转化为

   * @param list
   * @return
   */
  public ArrayList<YHDbRecord> convertList(List<Map<String, String>> list){
    ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
    if(list != null && list.size() >0){
      for (int i = 0; i < list.size(); i++) {
        YHDbRecord dbrec = new YHDbRecord();
        dbrec.addField("需求编号", list.get(i).get("requNo"));
        dbrec.addField("需求岗位", list.get(i).get("requJob"));
        dbrec.addField("需求人数", list.get(i).get("requNum"));
        dbrec.addField("需求部门", list.get(i).get("requDept"));
        dbrec.addField("申请人", list.get(i).get("petitioner"));       
        dbrec.addField("附件名称", list.get(i).get("attachmentName"));
        dbrec.addField("用工时间", list.get(i).get("requTime"));
        dbrec.addField("注备", list.get(i).get("remark"));
        dbrec.addField("登记时间", list.get(i).get("addTime"));
      
        dbL.add(dbrec);
      }
    }     
    return dbL;    
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
      YHHrRecruitRequirements recruitRequirements = (YHHrRecruitRequirements) orm.loadObjSingle(dbConn, YHHrRecruitRequirements.class, seqId);
      String dbAttachId = "";
      String dbAttachName = "";
      if (recruitRequirements != null) {
        dbAttachId = YHUtility.null2Empty(recruitRequirements.getAttachmentId());
        dbAttachName = YHUtility.null2Empty(recruitRequirements.getAttachmentName());
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
          recruitRequirements.setAttachmentId(attachmentId.trim());
          recruitRequirements.setAttachmentName(attachmentName.trim());
          orm.updateSingle(dbConn, recruitRequirements);
          returnFlag = true;
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return returnFlag;
  }
}

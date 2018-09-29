package yh.subsys.oa.hr.manage.care.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import yh.subsys.oa.hr.manage.care.data.YHHrStaffCare;
import yh.subsys.oa.hr.manage.logic.YHHrStaffIncentiveLogic;
import yh.subsys.oa.hr.setting.act.YHHrSetOtherAct;

public class YHHrStaffCareLogic {
  private static Logger log = Logger.getLogger(YHHrStaffCareLogic.class);

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
   * 新建员工关怀
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setNewStaffCareValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String careType = fileForm.getParameter("careType");
    String careFees = fileForm.getParameter("careFees");
    String byCareStaffsStr = fileForm.getParameter("byCareStaffs");
    String careDateStr = fileForm.getParameter("careDate");
    String careEffects = fileForm.getParameter("careEffects");
    String participants = fileForm.getParameter("participants");
    String careContent = fileForm.getParameter("careContent");
    String smsRemind = fileForm.getParameter("smsRemind");
    String sms2Remind = fileForm.getParameter("sms2Remind");

    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentIds = (String) map.get("attachmentIds");
    String attachmentNames = (String) map.get("attachmentNames");

    try{
      String[] staffNameArry = byCareStaffsStr.split(",");
      if (staffNameArry != null && staffNameArry.length > 0) {
        for (String staffName : staffNameArry) {
          YHHrStaffCare staffCare = new YHHrStaffCare();
          staffCare.setCareType(careType);
          if(!YHUtility.isNullorEmpty(careFees)){
            staffCare.setCareFees(Double.valueOf(careFees));
          }
          staffCare.setByCareStaffs(staffName);
          if(!YHUtility.isNullorEmpty(careDateStr)){
            Date careDate = YHUtility.parseDate("yyyy-MM-dd", careDateStr);
            staffCare.setCareDate(careDate);
          }
          staffCare.setCareEffects(careEffects);
          staffCare.setParticipants(participants);
          staffCare.setCareContent(careContent);
          staffCare.setAddTime(YHUtility.parseTimeStamp());
          staffCare.setCreateUserId(String.valueOf(person.getSeqId()));
          staffCare.setCreateDeptId(person.getDeptId());
          if(attachFlag){
            staffCare.setAttachmentId(attachmentIds);
            staffCare.setAttachmentName(attachmentNames);
          }
          orm.saveSingle(dbConn, staffCare);
        }
      }
      int maxSeqId = this.getMaxSeqId(dbConn);
      YHMobileSms2Logic sbl = new YHMobileSms2Logic();
      String remindUrl = "/subsys/oa/hr/manage/staffCare/detail.jsp?seqId=" + maxSeqId + "&openFlag=1&openWidth=860&openHeight=650";
      String smsContent = "请查看员工关怀信息！";
      // 短信提醒
      if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
        this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), byCareStaffsStr+","+participants, "57", remindUrl, new Date());
      }
      // 手机提醒
      if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
        smsContent = "员工关怀管理: 关怀 " + getUserNameLogic(dbConn, byCareStaffsStr) + "等员工。 ";
        sbl.remindByMobileSms(dbConn, byCareStaffsStr+","+participants, person.getSeqId(), smsContent, new Date());
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
    String sql = "select SEQ_ID from oa_pm_employee_care where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_employee_care )";
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
   * 员工关怀 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getStaffCareJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
      String deptIdStr = logic.getHrManagerPriv(dbConn, person);
      
      String sql = " select c1.SEQ_ID, c1.CARE_TYPE, c1.BY_CARE_STAFFS, c1.CARE_FEES, c1.PARTICIPANTS, c1.CARE_DATE "
                 + " from oa_pm_employee_care c1 "
                 + " where CREATE_USER_ID = "+ person.getSeqId()
                 + " or CREATE_DEPT_ID in "+ deptIdStr
                 + " ORDER BY c1.ADD_TIME desc ";
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
          YHHrStaffCare staffCare = (YHHrStaffCare) orm.loadObjSingle(dbConn, YHHrStaffCare.class, Integer.parseInt(seqId));
          String attachmentId = YHUtility.null2Empty(staffCare.getAttachmentId());
          String attachmentName = YHUtility.null2Empty(staffCare.getAttachmentName());
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

          orm.deleteSingle(dbConn, staffCare);
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
  public YHHrStaffCare getCareDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrStaffCare) orm.loadObjSingle(conn, YHHrStaffCare.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 员工关怀查询
   * 
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
  public String queryCareListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, person);
    
    String careType = (String) map.get("careType");
    String byCareStaffs = (String) map.get("byCareStaffs");
    String careDate1 = (String) map.get("careDate1");
    String careDate2 = (String) map.get("careDate2");
    String careFees1 = (String) map.get("careFees1");
    String careFees2 = (String) map.get("careFees2");
    String participants = (String) map.get("participants");
    String careContent = (String) map.get("careContent");
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(careType)) {
        conditionStr = " and c1.CARE_TYPE ='" + YHDBUtility.escapeLike(careType) + "'";
      }
      if (!YHUtility.isNullorEmpty(byCareStaffs)) {
        
        conditionStr += " and " + YHDBUtility.findInSet(byCareStaffs, "c1.BY_CARE_STAFFS");
      }
      if (!YHUtility.isNullorEmpty(careDate1)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("CARE_DATE", careDate1, ">=");
      }
      if (!YHUtility.isNullorEmpty(careDate2)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("CARE_DATE", careDate2, "<=");
      }
      if (!YHUtility.isNullorEmpty(careFees1)) {
        conditionStr += " and CARE_FEES >= " + careFees1;
      }
      if (!YHUtility.isNullorEmpty(careFees2)) {
        conditionStr += " and CARE_FEES <= " + careFees2;
      }
      if (!YHUtility.isNullorEmpty(participants)) {
        conditionStr += " and " + YHDBUtility.findInSet(participants, "c1.PARTICIPANTS");
      }
      if (!YHUtility.isNullorEmpty(careContent)) {
        conditionStr += " and c1.CARE_CONTENT like '%" + YHDBUtility.escapeLike(careContent) + "%'";
      }
      sql = " select c1.SEQ_ID, c1.CARE_TYPE, c1.BY_CARE_STAFFS, c1.CARE_FEES, c1.PARTICIPANTS, c1.CARE_DATE "
          + " from oa_pm_employee_care c1 "
          + " where (CREATE_USER_ID = "+ person.getSeqId()
          + " or CREATE_DEPT_ID in "+ deptIdStr + ")" 
          + conditionStr 
          + " ORDER BY c1.ADD_TIME desc";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 员工关怀信息
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void updateCareInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String seqIdStr = fileForm.getParameter("seqId");
    String careType = fileForm.getParameter("careType");
    String careFees = fileForm.getParameter("careFees");
    String byCareStaffs = fileForm.getParameter("byCareStaffs");
    String careDateStr = fileForm.getParameter("careDate");
    String careEffects = fileForm.getParameter("careEffects");
    String participants = fileForm.getParameter("participants");
    String careContent = fileForm.getParameter("careContent");
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
      YHHrStaffCare staffCare = (YHHrStaffCare) orm.loadObjSingle(dbConn, YHHrStaffCare.class, seqId);
      if (staffCare != null) {
        String dbAttachId = YHUtility.null2Empty(staffCare.getAttachmentId());
        String dbAttachName = YHUtility.null2Empty(staffCare.getAttachmentName());
      
        staffCare.setCareType(careType);
        staffCare.setCareFees(Double.valueOf(careFees));
        staffCare.setByCareStaffs(byCareStaffs);
        if(!YHUtility.isNullorEmpty(careDateStr)){
          Date careDate = YHUtility.parseDate("yyyy-MM-dd", careDateStr);
          staffCare.setCareDate(careDate);
        }
        staffCare.setCareEffects(careEffects);
        staffCare.setParticipants(participants);
        staffCare.setCareContent(careContent);
        staffCare.setAddTime(YHUtility.parseTimeStamp());
        staffCare.setCreateUserId(String.valueOf(person.getSeqId()));
        if(attachFlag){
          staffCare.setAttachmentId(dbAttachId.trim() + attachmentIds.trim());
          staffCare.setAttachmentName(dbAttachName.trim() + attachmentNames.trim());
        }
        orm.updateSingle(dbConn, staffCare);
        YHMobileSms2Logic sbl = new YHMobileSms2Logic();
        String remindUrl = "/subsys/oa/hr/manage/staffCare/detail.jsp?seqId=" + seqId + "&openFlag=1&openWidth=860&openHeight=650";
        String smsContent = "请查看员工关怀信息！";
        // 短信提醒
        if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
          this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), byCareStaffs+","+participants, "57", remindUrl, new Date());
        }
        // 手机提醒
        if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
          smsContent = "员工关怀管理: 关怀 " + getUserNameLogic(dbConn, byCareStaffs) + "等员工。 ";
          sbl.remindByMobileSms(dbConn, byCareStaffs+","+participants, person.getSeqId(), smsContent, new Date());
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
      YHHrStaffCare staffCare = (YHHrStaffCare) orm.loadObjSingle(dbConn, YHHrStaffCare.class, seqId);
      String dbAttachId = "";
      String dbAttachName = "";
      if (staffCare != null) {
        dbAttachId = YHUtility.null2Empty(staffCare.getAttachmentId());
        dbAttachName = YHUtility.null2Empty(staffCare.getAttachmentName());
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
          staffCare.setAttachmentId(attachmentId.trim());
          staffCare.setAttachmentName(attachmentName.trim());
          orm.updateSingle(dbConn, staffCare);
          returnFlag = true;
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return returnFlag;
  }

  /**
   * 本月过生日员工 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getStaffInfoJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      Calendar calendar = Calendar.getInstance();
      String monthBegin = calendar.get(Calendar.MONTH)+1+"-01";
      if (monthBegin.length() < 5) {
        monthBegin = "0" + monthBegin;
      }
      String monthEnd = calendar.get(Calendar.MONTH)+1+"-31";
      if (monthEnd.length() < 5) {
        monthEnd = "0" + monthEnd;
      }
      String sql = " select i1.SEQ_ID, p1.USER_ID, p1.SEX, i1.STAFF_BIRTH, p1.MOBIL_NO, i1.STAFF_EMAIL "
                 + " from oa_pm_employee_info i1 "
                 + " LEFT OUTER JOIN PERSON p1 ON i1.USER_ID = p1.USER_ID "
                 + " where p1.NOT_LOGIN!=1 " 
                 + " and " + YHDBUtility.getMDFilter("i1.STAFF_BIRTH", monthBegin, ">=")
                 + " and " + YHDBUtility.getMDFilter("i1.STAFF_BIRTH", monthEnd, "<=")
                 + " ORDER BY i1.ADD_TIME desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 员工关怀提醒
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setStaffCareReminderLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    try{
      String reminderStr = fileForm.getParameter("reminder");
      String content = fileForm.getParameter("content");
      String smsRemind = fileForm.getParameter("smsRemind");
      String sms2Remind = fileForm.getParameter("sms2Remind");
      
      YHMobileSms2Logic sbl = new YHMobileSms2Logic();
      String remindUrl = "/subsys/oa/hr/manage/staffCare/careRemind.jsp";
      String smsContent = content;
      // 短信提醒
      if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
        this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), reminderStr, "0", remindUrl, new Date());
      }
      // 手机提醒
      if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
        smsContent = "员工关怀提醒: " + content;
        sbl.remindByMobileSms(dbConn, reminderStr, person.getSeqId(), smsContent, new Date());
      }
    } catch (Exception e) {
      throw e;
    }
  }
}

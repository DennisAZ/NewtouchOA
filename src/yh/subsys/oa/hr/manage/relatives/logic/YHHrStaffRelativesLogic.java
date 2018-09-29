package yh.subsys.oa.hr.manage.relatives.logic;

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
import yh.subsys.oa.hr.manage.logic.YHHrStaffIncentiveLogic;
import yh.subsys.oa.hr.manage.relatives.data.YHHrStaffRelatives;
import yh.subsys.oa.hr.setting.act.YHHrSetOtherAct;

public class YHHrStaffRelativesLogic {
  private static Logger log = Logger.getLogger(YHHrStaffRelativesLogic.class);

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
   * 新建社会关系
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setNewStaffRelativesValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String staffName = fileForm.getParameter("staffName");
    String member = fileForm.getParameter("member");
    String relationship = fileForm.getParameter("relationship");
    String birthdayStr = fileForm.getParameter("birthday");
    String politics = fileForm.getParameter("politics");
    String jobOccupation = fileForm.getParameter("jobOccupation");
    String postOfJob = fileForm.getParameter("postOfJob");
    String personalTel = fileForm.getParameter("personalTel");
    String homeTel = fileForm.getParameter("homeTel");
    String officeTel = fileForm.getParameter("officeTel");
    String workUnit = fileForm.getParameter("workUnit");
    String unitAddress = fileForm.getParameter("unitAddress");
    String homeAddress = fileForm.getParameter("homeAddress");
    String remark = fileForm.getParameter("remark");

    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentIds = (String) map.get("attachmentIds");
    String attachmentNames = (String) map.get("attachmentNames");

    try{
      YHHrStaffRelatives staffRelatives = new YHHrStaffRelatives();
      staffRelatives.setStaffName(staffName);
      staffRelatives.setMember(member);
      staffRelatives.setRelationship(relationship);
      if(!YHUtility.isNullorEmpty(birthdayStr)){
        Date birthday = YHUtility.parseDate("yyyy-MM-dd", birthdayStr);
        staffRelatives.setBirthday(birthday);
      }
      staffRelatives.setPolitics(politics);
      staffRelatives.setJobOccupation(jobOccupation);
      staffRelatives.setPostOfJob(postOfJob);
      staffRelatives.setPersonalTel(personalTel);
      staffRelatives.setHomeTel(homeTel);
      staffRelatives.setOfficeTel(officeTel);
      staffRelatives.setWorkUnit(workUnit);
      staffRelatives.setUnitAddress(unitAddress);
      staffRelatives.setHomeAddress(homeAddress);
      staffRelatives.setRemark(remark);
      staffRelatives.setAddTime(YHUtility.parseTimeStamp());
      staffRelatives.setCreateUserId(String.valueOf(person.getSeqId()));
      staffRelatives.setCreateDeptId(person.getDeptId());
      if(attachFlag){
        staffRelatives.setAttachmentId(attachmentIds);
        staffRelatives.setAttachmentName(attachmentNames);
      }
      orm.saveSingle(dbConn, staffRelatives);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 社会关系 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getStaffRelativesJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, person);
    
    try {
      String sql1 = " select h1.SEQ_ID,p1.USER_NAME STAFF_NAME, h1.MEMBER, h1.RELATIONSHIP, h1.JOB_OCCUPATION, h1.PERSONAL_TEL "
                 + " from oa_pm_employee_correlation h1 "
                 + " join PERSON p1 on h1.STAFF_NAME = p1.SEQ_ID "
                 + " where CREATE_USER_ID = "+ person.getSeqId()
                 + " or CREATE_DEPT_ID in "+ deptIdStr
                 + " ORDER BY h1.SEQ_ID desc";
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
          YHHrStaffRelatives staffRelatives = (YHHrStaffRelatives) orm.loadObjSingle(dbConn, YHHrStaffRelatives.class, Integer.parseInt(seqId));
          String attachmentId = YHUtility.null2Empty(staffRelatives.getAttachmentId());
          String attachmentName = YHUtility.null2Empty(staffRelatives.getAttachmentName());
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

          orm.deleteSingle(dbConn, staffRelatives);
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
  public YHHrStaffRelatives getRelativesDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrStaffRelatives) orm.loadObjSingle(conn, YHHrStaffRelatives.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 社会关系查询
   * 
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
  public String queryRelativesListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, person);
    
    String staffName = (String) map.get("staffName");
    String member = (String) map.get("member");
    String relationship = (String) map.get("relationship");
    String jobOccupation = (String) map.get("jobOccupation");
    String workUnit = (String) map.get("workUnit");
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(staffName)) {
        conditionStr = " and h1.STAFF_NAME ='" + YHDBUtility.escapeLike(staffName) + "'";
      }
      if (!YHUtility.isNullorEmpty(member)) {
        conditionStr += " and h1.MEMBER like '%" + YHDBUtility.escapeLike(member) + "%'";
      }
      if (!YHUtility.isNullorEmpty(relationship)) {
        conditionStr += " and h1.RELATIONSHIP ='" + relationship + "'";
      }
      if (!YHUtility.isNullorEmpty(jobOccupation)) {
        conditionStr += " and h1.JOB_OCCUPATION like '%" + YHDBUtility.escapeLike(jobOccupation) + "%'";
      }
      if (!YHUtility.isNullorEmpty(workUnit)) {
        conditionStr += " and h1.WORK_UNIT like '%" + YHDBUtility.escapeLike(workUnit) + "%'";
      }
      sql = " select h1.SEQ_ID,p1.USER_NAME STAFF_NAME, h1.MEMBER, h1.RELATIONSHIP, h1.JOB_OCCUPATION, h1.PERSONAL_TEL "
          + " from oa_pm_employee_correlation h1 "
          + " join PERSON p1 on h1.STAFF_NAME = p1.SEQ_ID "
          +	" where (CREATE_USER_ID = "+ person.getSeqId()
          + " or CREATE_DEPT_ID in "+ deptIdStr + ")" 
          + conditionStr 
          + " ORDER BY h1.ADD_TIME desc";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 社会关系信息
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void updateRelativesInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String seqIdStr = fileForm.getParameter("seqId");
    String staffName = fileForm.getParameter("staffName");
    String member = fileForm.getParameter("member");
    String relationship = fileForm.getParameter("relationship");
    String birthdayStr = fileForm.getParameter("birthday");
    String politics = fileForm.getParameter("politics");
    String jobOccupation = fileForm.getParameter("jobOccupation");
    String postOfJob = fileForm.getParameter("postOfJob");
    String personalTel = fileForm.getParameter("personalTel");
    String homeTel = fileForm.getParameter("homeTel");
    String officeTel = fileForm.getParameter("officeTel");
    String workUnit = fileForm.getParameter("workUnit");
    String unitAddress = fileForm.getParameter("unitAddress");
    String homeAddress = fileForm.getParameter("homeAddress");
    String remark = fileForm.getParameter("remark");

    int seqId = 0;
    if (!YHUtility.isNullorEmpty(seqIdStr)) {
      seqId = Integer.parseInt(seqIdStr);
    }
    
    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentIds = (String) map.get("attachmentIds");
    String attachmentNames = (String) map.get("attachmentNames");
    
    try {
      YHHrStaffRelatives staffRelatives = (YHHrStaffRelatives) orm.loadObjSingle(dbConn, YHHrStaffRelatives.class, seqId);
      if (staffRelatives != null) {
        String dbAttachId = YHUtility.null2Empty(staffRelatives.getAttachmentId());
        String dbAttachName = YHUtility.null2Empty(staffRelatives.getAttachmentName());
      
        staffRelatives.setStaffName(staffName);
        staffRelatives.setMember(member);
        staffRelatives.setRelationship(relationship);
        if(!YHUtility.isNullorEmpty(birthdayStr)){
          Date birthday = YHUtility.parseDate("yyyy-MM-dd", birthdayStr);
          staffRelatives.setBirthday(birthday);
        }
        staffRelatives.setPolitics(politics);
        staffRelatives.setJobOccupation(jobOccupation);
        staffRelatives.setPostOfJob(postOfJob);
        staffRelatives.setPersonalTel(personalTel);
        staffRelatives.setHomeTel(homeTel);
        staffRelatives.setOfficeTel(officeTel);
        staffRelatives.setWorkUnit(workUnit);
        staffRelatives.setUnitAddress(unitAddress);
        staffRelatives.setHomeAddress(homeAddress);
        staffRelatives.setRemark(remark);
        staffRelatives.setAddTime(YHUtility.parseTimeStamp());
        staffRelatives.setCreateUserId(String.valueOf(person.getSeqId()));
        if(attachFlag){
          staffRelatives.setAttachmentId(dbAttachId.trim() + attachmentIds.trim());
          staffRelatives.setAttachmentName(dbAttachName.trim() + attachmentNames.trim());
        }
        orm.updateSingle(dbConn, staffRelatives);
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
      YHHrStaffRelatives staffRelatives = (YHHrStaffRelatives) orm.loadObjSingle(dbConn, YHHrStaffRelatives.class, seqId);
      String dbAttachId = "";
      String dbAttachName = "";
      if (staffRelatives != null) {
        dbAttachId = YHUtility.null2Empty(staffRelatives.getAttachmentId());
        dbAttachName = YHUtility.null2Empty(staffRelatives.getAttachmentName());
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
          staffRelatives.setAttachmentId(attachmentId.trim());
          staffRelatives.setAttachmentName(attachmentName.trim());
          orm.updateSingle(dbConn, staffRelatives);
          returnFlag = true;
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return returnFlag;
  }

}

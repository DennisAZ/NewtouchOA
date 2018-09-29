package yh.subsys.oa.hr.manage.titleEvaluation.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.logic.YHHrStaffIncentiveLogic;
import yh.subsys.oa.hr.manage.titleEvaluation.data.YHHrStaffTitleEvaluation;

public class YHHrStaffTitleEvaluationLogic {
  private static Logger log = Logger.getLogger(YHHrStaffTitleEvaluationLogic.class);

  /**
   * 处理上传附件，返回附件id，附件名称--wyw
   * 
   * @param fileForm
   * @return
   * @throws Exception
   */
//  public Map<Object, Object> fileUploadLogic(YHFileUploadForm fileForm, String attachmentFolder) throws Exception {
//    Map<Object, Object> result = new HashMap<Object, Object>();
//    try {
//      // 保存从文件柜、网络硬盘选择附件
//      YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, attachmentFolder);
//      String attIdStr = sel.getAttachIdToString(",");
//      String attNameStr = sel.getAttachNameToString("*");
//      boolean fromFolderFlag = false;
//      String forlderAttchId = "";
//      String forlderAttchName = "";
//      if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
//        forlderAttchId = attIdStr + ",";
//        forlderAttchName = attNameStr + "*";
//        fromFolderFlag = true;
//      }
//      Iterator<String> iKeys = fileForm.iterateFileFields();
//      boolean uploadFlag = false;
//      String uploadAttchId = "";
//      String uploadAttchName = "";
//      Date date = new Date();
//      SimpleDateFormat format = new SimpleDateFormat("yyMM");
//      String currDate = format.format(date);
//      String separator = File.separator;
//      String filePath = YHSysProps.getAttachPath() + separator + attachmentFolder + separator + currDate;
//
//      while (iKeys.hasNext()) {
//        String fieldName = iKeys.next();
//        String fileName = fileForm.getFileName(fieldName);
//        if (YHUtility.isNullorEmpty(fileName)) {
//          continue;
//        }
//        YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
//        String rand = emul.getRandom();
//        uploadAttchId += currDate + "_" + rand + ",";
//        uploadAttchName += fileName + "*";
//        uploadFlag = true;
//
//        fileName = rand + "_" + fileName;
//        fileForm.saveFile(fieldName, filePath + "\\" + fileName);
//      }
//      boolean attachFlag = false;
//      String attachmentIds = "";
//      String attachmentNames = "";
//      if (fromFolderFlag && uploadFlag) {
//        attachmentIds = forlderAttchId + uploadAttchId;
//        attachmentNames = forlderAttchName + uploadAttchName;
//        attachFlag = true;
//      } else if (fromFolderFlag) {
//        attachmentIds = forlderAttchId;
//        attachmentNames = forlderAttchName;
//        attachFlag = true;
//      } else if (uploadFlag) {
//        attachmentIds = uploadAttchId;
//        attachmentNames = uploadAttchName;
//        attachFlag = true;
//      }
//      result.put("attachFlag", attachFlag);
//      result.put("attachmentIds", attachmentIds);
//      result.put("attachmentNames", attachmentNames);
//    } catch (Exception e) {
//      throw e;
//    }
//    return result;
//  }

  /**
   * 新建职称评定
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setNewStaffTitleEvaluationValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String byEvaluStaffs = fileForm.getParameter("byEvaluStaffs");
    String approvePerson = fileForm.getParameter("approvePerson");
    String postName = fileForm.getParameter("postName");
    String getMethod = fileForm.getParameter("getMethod");
    String reportTimeStr = fileForm.getParameter("reportTime");
    String receiveTimeStr = fileForm.getParameter("receiveTime");
    String approveNext = fileForm.getParameter("approveNext");
    String approveNextTimeStr = fileForm.getParameter("approveNextTime");
    String employPost = fileForm.getParameter("employPost");
    String employCompany = fileForm.getParameter("employCompany");
    String startDateStr = fileForm.getParameter("startDate");
    String endDateStr = fileForm.getParameter("endDate");
    String remark = fileForm.getParameter("remark");

//    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
//    boolean attachFlag = (Boolean) map.get("attachFlag");
//    String attachmentIds = (String) map.get("attachmentIds");
//    String attachmentNames = (String) map.get("attachmentNames");

    try{
      YHHrStaffTitleEvaluation staffTitleEvaluation = new YHHrStaffTitleEvaluation();
      staffTitleEvaluation.setByEvaluStaffs(byEvaluStaffs);
      staffTitleEvaluation.setApprovePerson(approvePerson);
      staffTitleEvaluation.setPostName(postName);
      staffTitleEvaluation.setGetMethod(getMethod);      
      if(!YHUtility.isNullorEmpty(reportTimeStr)){
        Date reportTime = YHUtility.parseDate("yyyy-MM-dd", reportTimeStr);
        staffTitleEvaluation.setReportTime(reportTime);
      }
      if(!YHUtility.isNullorEmpty(receiveTimeStr)){
        Date receiveTime = YHUtility.parseDate("yyyy-MM-dd", receiveTimeStr);
        staffTitleEvaluation.setReceiveTime(receiveTime);
      }
      staffTitleEvaluation.setApproveNext(approveNext);
      if(!YHUtility.isNullorEmpty(approveNextTimeStr)){
        Date approveNextTime = YHUtility.parseDate("yyyy-MM-dd", approveNextTimeStr);
        staffTitleEvaluation.setApproveNextTime(approveNextTime);
      }     
      staffTitleEvaluation.setEmployPost(employPost);
      staffTitleEvaluation.setEmployCompany(employCompany);
      if(!YHUtility.isNullorEmpty(startDateStr)){
        Date startDate = YHUtility.parseDate("yyyy-MM-dd", startDateStr);
        staffTitleEvaluation.setStartDate(startDate);
      }      
      if(!YHUtility.isNullorEmpty(endDateStr)){
        Date endDate = YHUtility.parseDate("yyyy-MM-dd", endDateStr);
        staffTitleEvaluation.setEndDate(endDate);
      }       
      staffTitleEvaluation.setRemark(remark);
      staffTitleEvaluation.setAddTime(YHUtility.parseTimeStamp());
      staffTitleEvaluation.setCreateUserId(String.valueOf(person.getSeqId()));
      staffTitleEvaluation.setCreateDeptId(person.getDeptId());
      orm.saveSingle(dbConn, staffTitleEvaluation);
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

  /**
   * 职称评定 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getStaffTitleEvaluationJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
      String deptIdStr = logic.getHrManagerPriv(dbConn, person);
      
      String sql = " select t1.SEQ_ID, p1.USER_NAME BY_EVALU_STAFFS ,p2.USER_NAME APPROVE_PERSON ,t1.POST_NAME, t1.GET_METHOD, t1.RECEIVE_TIME "
                 + " from oa_pm_employee_title t1 "
                 + " join PERSON p1 on t1.BY_EVALU_STAFFS = p1.SEQ_ID "
                 + " join PERSON p2 on t1.APPROVE_PERSON = p2.SEQ_ID "
                 + " where CREATE_USER_ID = "+ person.getSeqId()
                 + " or CREATE_DEPT_ID in "+ deptIdStr
                 + " ORDER BY t1.ADD_TIME desc  ";
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
//          StringBuffer attIdBuffer = new StringBuffer();
//          StringBuffer attNameBuffer = new StringBuffer();
          YHHrStaffTitleEvaluation staffTitleEvaluation = (YHHrStaffTitleEvaluation) orm.loadObjSingle(dbConn, YHHrStaffTitleEvaluation.class, Integer.parseInt(seqId));
//          String attachmentId = YHUtility.null2Empty(staffTitleEvaluation.getAttachmentId());
//          String attachmentName = YHUtility.null2Empty(staffTitleEvaluation.getAttachmentName());
//          attIdBuffer.append(attachmentId.trim());
//          attNameBuffer.append(attachmentName.trim());
//          String[] attIdArray = {};
//          String[] attNameArray = {};
//          if (!YHUtility.isNullorEmpty(attIdBuffer.toString()) && !YHUtility.isNullorEmpty(attNameBuffer.toString()) && attIdBuffer.length() > 0) {
//            attIdArray = attIdBuffer.toString().trim().split(",");
//            attNameArray = attNameBuffer.toString().trim().split("\\*");
//          }
//          if (attIdArray != null && attIdArray.length > 0) {
//            for (int i = 0; i < attIdArray.length; i++) {
//              Map<String, String> map = this.getFileName(attIdArray[i], attNameArray[i]);
//              if (map.size() != 0) {
//                Set<String> set = map.keySet();
//                // 遍历Set集合
//                for (String keySet : set) {
//                  String key = keySet;
//                  String keyValue = map.get(keySet);
//                  String attaIdStr = this.getAttaId(keySet);
//                  String fileNameValue = attaIdStr + "_" + keyValue;
//                  String fileFolder = this.getFilePathFolder(key);
//                  String oldFileNameValue = attaIdStr + "." + keyValue;
//                  File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
//                  File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);
//                  if (file.exists()) {
//                    YHFileUtility.deleteAll(file.getAbsoluteFile());
//                  } else if (oldFile.exists()) {
//                    YHFileUtility.deleteAll(oldFile.getAbsoluteFile());
//                  }
//                }
//              }
//            }
//          }
          // 删除数据库信息

          orm.deleteSingle(dbConn, staffTitleEvaluation);
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
  public YHHrStaffTitleEvaluation getTitleEvaluationDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrStaffTitleEvaluation) orm.loadObjSingle(conn, YHHrStaffTitleEvaluation.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 职称评定查询
   * 
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
  public String queryTitleEvaluationListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    YHHrStaffIncentiveLogic logic = new YHHrStaffIncentiveLogic();
    String deptIdStr = logic.getHrManagerPriv(dbConn, person);
    
    String byEvaluStaffs =  (String)map.get("byEvaluStaffs");
    String approvePerson =  (String)map.get("approvePerson");
    String postName =  (String)map.get("postName");
    String getMethod =  (String)map.get("getMethod");
    String reportTime1 =  (String)map.get("reportTime1");
    String reportTime2 =  (String)map.get("reportTime2");
    String receiveTime1 =  (String)map.get("receiveTime1");
    String receiveTime2 =  (String)map.get("receiveTime2");
    String employPost =  (String)map.get("employPost");
    String employCompany =  (String)map.get("employCompany");
    String remark =  (String)map.get("remark");
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(byEvaluStaffs)) {
        conditionStr = " and t1.BY_EVALU_STAFFS ='" + YHDBUtility.escapeLike(byEvaluStaffs) + "'";
      }
      if (!YHUtility.isNullorEmpty(approvePerson)) {
        conditionStr = " and t1.APPROVE_PERSON ='" + YHDBUtility.escapeLike(approvePerson) + "'";
      }
      if (!YHUtility.isNullorEmpty(postName)) {
        conditionStr += " and t1.POST_NAME like '%" + YHDBUtility.escapeLike(postName) + "%'";
      }
      if (!YHUtility.isNullorEmpty(getMethod)) {
        conditionStr = " and t1.GET_METHOD ='" + YHDBUtility.escapeLike(getMethod) + "'";
      }
      if (!YHUtility.isNullorEmpty(reportTime1)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("REPORT_TIME", reportTime1, ">=");
      }
      if (!YHUtility.isNullorEmpty(reportTime2)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("REPORT_TIME", reportTime2, "<=");
      }
      if (!YHUtility.isNullorEmpty(receiveTime1)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("RECEIVE_TIME", receiveTime1, ">=");
      }
      if (!YHUtility.isNullorEmpty(receiveTime2)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("RECEIVE_TIME", receiveTime2, "<=");
      }
      if (!YHUtility.isNullorEmpty(employPost)) {
        conditionStr += " and t1.EMPLOY_POST like '%" + YHDBUtility.escapeLike(employPost) + "%'";
      }
      if (!YHUtility.isNullorEmpty(employCompany)) {
        conditionStr += " and t1.EMPLOY_COMPANY like '%" + YHDBUtility.escapeLike(employCompany) + "%'";
      }
      if (!YHUtility.isNullorEmpty(remark)) {
        conditionStr += " and t1.REMARK like '%" + YHDBUtility.escapeLike(remark) + "%'";
      }
      sql = " select t1.SEQ_ID, p1.USER_NAME BY_EVALU_STAFFS ,p2.USER_NAME APPROVE_PERSON ,t1.POST_NAME, t1.GET_METHOD, t1.RECEIVE_TIME "
          + " from oa_pm_employee_title t1 "
          + " join PERSON p1 on t1.BY_EVALU_STAFFS = p1.SEQ_ID "
          + " join PERSON p2 on t1.APPROVE_PERSON = p2.SEQ_ID "
          + " where (CREATE_USER_ID = "+ person.getSeqId()
          + " or CREATE_DEPT_ID in "+ deptIdStr + ")" 
          + conditionStr 
          + " ORDER BY t1.ADD_TIME desc";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 职称评定信息
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void updateTitleEvaluationInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String seqIdStr = fileForm.getParameter("seqId");
    String byEvaluStaffs = fileForm.getParameter("byEvaluStaffs");
    String approvePerson = fileForm.getParameter("approvePerson");
    String postName = fileForm.getParameter("postName");
    String getMethod = fileForm.getParameter("getMethod");
    String reportTimeStr = fileForm.getParameter("reportTime");
    String receiveTimeStr = fileForm.getParameter("receiveTime");
    String approveNext = fileForm.getParameter("approveNext");
    String approveNextTimeStr = fileForm.getParameter("approveNextTime");
    String employPost = fileForm.getParameter("employPost");
    String employCompany = fileForm.getParameter("employCompany");
    String startDateStr = fileForm.getParameter("startDate");
    String endDateStr = fileForm.getParameter("endDate");
    String remark = fileForm.getParameter("remark");

    int seqId = 0;
    if (!YHUtility.isNullorEmpty(seqIdStr)) {
      seqId = Integer.parseInt(seqIdStr);
    }
    
//    Map<Object, Object> map = this.fileUploadLogic(fileForm, YHHrSetOtherAct.attachmentFolder);
//    boolean attachFlag = (Boolean) map.get("attachFlag");
//    String attachmentIds = (String) map.get("attachmentIds");
//    String attachmentNames = (String) map.get("attachmentNames");
    
    try {
      YHHrStaffTitleEvaluation staffTitleEvaluation = (YHHrStaffTitleEvaluation) orm.loadObjSingle(dbConn, YHHrStaffTitleEvaluation.class, seqId);
      if (staffTitleEvaluation != null) {
      
        staffTitleEvaluation.setByEvaluStaffs(byEvaluStaffs);
        staffTitleEvaluation.setApprovePerson(approvePerson);
        staffTitleEvaluation.setPostName(postName);
        staffTitleEvaluation.setGetMethod(getMethod);      
        if(!YHUtility.isNullorEmpty(reportTimeStr)){
          Date reportTime = YHUtility.parseDate("yyyy-MM-dd", reportTimeStr);
          staffTitleEvaluation.setReportTime(reportTime);
        }
        if(!YHUtility.isNullorEmpty(receiveTimeStr)){
          Date receiveTime = YHUtility.parseDate("yyyy-MM-dd", receiveTimeStr);
          staffTitleEvaluation.setReceiveTime(receiveTime);
        }
        staffTitleEvaluation.setApproveNext(approveNext);
        if(!YHUtility.isNullorEmpty(approveNextTimeStr)){
          Date approveNextTime = YHUtility.parseDate("yyyy-MM-dd", approveNextTimeStr);
          staffTitleEvaluation.setApproveNextTime(approveNextTime);
        }     
        staffTitleEvaluation.setEmployPost(employPost);
        staffTitleEvaluation.setEmployCompany(employCompany);
        if(!YHUtility.isNullorEmpty(startDateStr)){
          Date startDate = YHUtility.parseDate("yyyy-MM-dd", startDateStr);
          staffTitleEvaluation.setStartDate(startDate);
        }      
        if(!YHUtility.isNullorEmpty(endDateStr)){
          Date endDate = YHUtility.parseDate("yyyy-MM-dd", endDateStr);
          staffTitleEvaluation.setEndDate(endDate);
        }       
        staffTitleEvaluation.setRemark(remark);
        staffTitleEvaluation.setAddTime(YHUtility.parseTimeStamp());
        staffTitleEvaluation.setCreateUserId(String.valueOf(person.getSeqId()));
        orm.updateSingle(dbConn, staffTitleEvaluation);
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
//  public boolean updateFloadFile(Connection dbConn, String seqIdStr, String attachId) throws Exception {
//    boolean returnFlag = false;
//    YHORM orm = new YHORM();
//    int seqId = 0;
//    if (!YHUtility.isNullorEmpty(seqIdStr)) {
//      seqId = Integer.parseInt(seqIdStr);
//    }
//    try {
//      YHHrStaffTitleEvaluation staffTitleEvaluation = (YHHrStaffTitleEvaluation) orm.loadObjSingle(dbConn, YHHrStaffTitleEvaluation.class, seqId);
//      String dbAttachId = "";
//      String dbAttachName = "";
//      if (staffTitleEvaluation != null) {
//        dbAttachId = YHUtility.null2Empty(staffTitleEvaluation.getAttachmentId());
//        dbAttachName = YHUtility.null2Empty(staffTitleEvaluation.getAttachmentName());
//        String[] dbAttachIdArrays = dbAttachId.split(",");
//        String[] dbAttachNameArrays = dbAttachName.split("\\*");
//        String attachmentId = "";
//        String attachmentName = "";
//        if (!YHUtility.isNullorEmpty(attachId) && dbAttachIdArrays.length > 0) {
//          for (int i = 0; i < dbAttachIdArrays.length; i++) {
//            if (attachId.equals(dbAttachIdArrays[i])) {
//              continue;
//            }
//            attachmentId += dbAttachIdArrays[i] + ",";
//            attachmentName += dbAttachNameArrays[i] + "*";
//          }
//          staffTitleEvaluation.setAttachmentId(attachmentId.trim());
//          staffTitleEvaluation.setAttachmentName(attachmentName.trim());
//          orm.updateSingle(dbConn, staffTitleEvaluation);
//          returnFlag = true;
//        }
//      }
//    } catch (Exception e) {
//      throw e;
//    }
//    return returnFlag;
//  }

  public String getDeptId(Connection dbConn , String titleEvaluationPerson) throws Exception {

    PreparedStatement ps = null;
    ResultSet rs = null;
    String deptId = "";
    try{
      String sql = " select dept_id from person "
        + " where seq_id =" + titleEvaluationPerson;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        deptId = rs.getString("dept_id");
      }
    }catch(Exception e){
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return deptId;
  }
}

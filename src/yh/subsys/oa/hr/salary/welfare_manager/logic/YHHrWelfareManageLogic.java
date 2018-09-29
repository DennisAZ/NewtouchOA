package yh.subsys.oa.hr.salary.welfare_manager.logic;


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
//import yh.subsys.oa.hr.hrManager.act.YHHrSetOtherAct;


import yh.subsys.oa.hr.salary.welfare_manager.data.YHHrWelfareManage;

public class YHHrWelfareManageLogic {

  public void setNewWelfareInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    
    
    String staffId = fileForm.getParameter("staffId");
    String welfareItem = fileForm.getParameter("welfareItem");
    String paymentDate = fileForm.getParameter("paymentDate");
    String welfareMonth = fileForm.getParameter("welfareMonth");
    String welfarePayment = fileForm.getParameter("welfarePayment");
    String taxAffares = fileForm.getParameter("taxAffares");
    String freeGift = fileForm.getParameter("freeGift");
    String remark = fileForm.getParameter("remark");
    
    try{
    
      YHHrWelfareManage welfaremanage = new YHHrWelfareManage();
          welfaremanage.setStaffName(staffId);
          if(YHUtility.isNumber(welfareItem)){
            welfaremanage.setWelfareItem(welfareItem);
          }
          if(!YHUtility.isNullorEmpty(paymentDate)){
          welfaremanage.setPaymentDate(YHUtility.parseDate("yyyy-MM-dd", paymentDate));
          }
          welfaremanage.setWelfareMonth(welfareMonth);
          
          if(YHUtility.isNumber(welfarePayment)){
          welfaremanage.setWelfarePayment(YHUtility.parseDouble(welfarePayment)); 
          }
          welfaremanage.setTaxAffares(taxAffares);
          if(!YHUtility.isNullorEmpty(freeGift)){
          welfaremanage.setFreeGift(freeGift);
          }
          if(!YHUtility.isNullorEmpty(remark)){
          welfaremanage.setRemark(remark);
          }
          welfaremanage.setAddTime(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", YHUtility.getCurDateTimeStr()));
          welfaremanage.setCreateDeptId(person.getDeptId());
          welfaremanage.setCreateUserId(person.getUserId());
          orm.saveSingle(dbConn,welfaremanage);
         
    } catch (Exception e) {
      throw e;
    }
  }
  
  
  /**
   * 员工福利  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getWelfareJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " select c1.SEQ_ID,c1.STAFF_NAME, c1.WELFARE_ITEM, c1.WELFARE_MONTH, c1.PAYMENT_DATE, c1.WELFARE_PAYMENT"
                 + " from oa_pm_welfares c1 "
                 + " ORDER BY c1.ADD_TIME desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
   
      return pageDataList.toJson();
      
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
   * 删除文件--wyw
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteWelfareLogic(Connection dbConn, String seqIdStr) throws Exception {
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
        
          YHHrWelfareManage welfaremanage = (YHHrWelfareManage) orm.loadObjSingle(dbConn,YHHrWelfareManage.class, Integer.parseInt(seqId));

          orm.deleteSingle(dbConn,welfaremanage);
        }
      }
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
  public YHHrWelfareManage getWelfareDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrWelfareManage) orm.loadObjSingle(conn, YHHrWelfareManage.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  public void updateWelfareInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    String seqIdStr=fileForm.getParameter("seqId");
    String staffId = fileForm.getParameter("staffId");
    String welfareItem = fileForm.getParameter("welfareItem");
    String paymentDate = fileForm.getParameter("paymentDate");  
    String welfareMonth = fileForm.getParameter("welfareMonth");
    String welfarePayment = fileForm.getParameter("welfarePayment");
    String taxAffares = fileForm.getParameter("taxAffares");
    String freeGift = fileForm.getParameter("freeGift");
    String remark = fileForm.getParameter("remark");

    int seqId = 0;
    if (!YHUtility.isNullorEmpty(seqIdStr)) {
      seqId = Integer.parseInt(seqIdStr);
    }
    try {
      YHHrWelfareManage welfaremanage = (YHHrWelfareManage) orm.loadObjSingle(dbConn, YHHrWelfareManage.class, seqId);
      if (welfaremanage != null) {
        welfaremanage.setStaffName(staffId);
        if(YHUtility.isNumber(welfareItem)){
          welfaremanage.setWelfareItem(welfareItem);
        }
        
        welfaremanage.setPaymentDate(YHUtility.parseDate("yyyy-MM-dd",paymentDate));
        welfaremanage.setWelfareMonth(welfareMonth);
        welfaremanage.setWelfarePayment(YHUtility.parseDouble(welfarePayment));
        welfaremanage.setTaxAffares(taxAffares);
        welfaremanage.setFreeGift(freeGift);
        welfaremanage.setRemark(remark);
        orm.updateSingle(dbConn, welfaremanage);
      }
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 员工福利查询
   * 
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
  public String queryWelfareListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    String staffId = (String) map.get("staffId");
    String welfareItem = (String) map.get("welfareItem");
    String welfareMonth = (String) map.get("welfareMonth");
    String welfarePayment = (String) map.get("welfarePayment");
   
    String taxAffares = (String) map.get("taxAffares");
    String startDate = (String) map.get("startDate");
    String endDate=(String)map.get("endDate");
    String freeGift = (String) map.get("freeGift");
  
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(staffId )) {
        staffId=staffId.replace(",","','");
       // conditionStr = " and "+YHDBUtility.findInSet(staffId,"c1.STAFF_NAME");
        conditionStr = " and c1.STAFF_NAME in ('"+staffId+"') ";
      }
      if (!YHUtility.isNullorEmpty(welfareItem)) {
        
        conditionStr += " and " + YHDBUtility.findInSet(welfareItem, "c1.WELFARE_ITEM");
      }
      if (!YHUtility.isNullorEmpty(welfareMonth)) {
        conditionStr += " and " + YHDBUtility.findInSet(welfareMonth, "c1.WELFARE_MONTH");
      }
   
      if (!YHUtility.isNullorEmpty(welfarePayment)) {
        conditionStr += " and " + YHDBUtility.findInSet(welfarePayment, "WELFARE_PAYMENT");
      }
      if (!YHUtility.isNullorEmpty(taxAffares)) {
        conditionStr += " and " + YHDBUtility.findInSet(taxAffares, "TAX_AFFARES");
      }
      if (!YHUtility.isNullorEmpty(startDate)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("PAYMENT_DATE", startDate, ">=");
      }
      if (!YHUtility.isNullorEmpty(endDate)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("PAYMENT_DATE", endDate, "<=");
      }
      if (!YHUtility.isNullorEmpty(freeGift)) {
        conditionStr += " and " + YHDBUtility.findInSet(freeGift, "c1.FREE_GIFT");
      }
    
      sql = " select c1.SEQ_ID, c1.STAFF_NAME, c1.WELFARE_ITEM, c1.WELFARE_MONTH,C1.PAYMENT_DATE, c1.WELFARE_PAYMENT "
          + " from oa_pm_welfares c1 "
          + " where 1=1" + conditionStr + " ORDER BY c1.ADD_TIME desc";
   
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
}

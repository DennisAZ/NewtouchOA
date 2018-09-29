package yh.subsys.oa.profsys.source.org.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.source.org.data.YHSourceOrg;

public class YHSourceOrgLogic {
  private static Logger log = Logger.getLogger(YHSourceOrgLogic.class);
  public static int addOrg(Connection dbConn,YHSourceOrg org) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, org);
    return 0;
  }
  public static void updateOrg(Connection dbConn,YHSourceOrg org) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, org);
  }
  public static void delOrg(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHSourceOrg.class, Integer.parseInt(seqId));
  }
  public static YHSourceOrg selectOrgById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHSourceOrg org = (YHSourceOrg) orm.loadObjSingle(dbConn, YHSourceOrg.class, Integer.parseInt(seqId));
    return org;
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页-人员
   * @return
   * @throws Exception 
   */
  public static String queryOrg(Connection dbConn,Map request,String orgNum,String  orgNation,
      String orgName,String orgLeader,String orgScale,String orgEstablishTime,String orgEstablishTime1) throws Exception {
    String sql = "select seq_id,org_num,org_name,org_nation,org_leader,org_scale,org_establish_time from oa_src_org where 1=1";
      if (!YHUtility.isNullorEmpty(orgNum)) {
        sql += " and org_num like '%" + YHUtility.encodeLike(orgNum) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(orgNation)) {
        sql += " and org_nation like '%" + YHUtility.encodeLike(orgNation) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(orgName)) {
        sql += " and org_name like '%" + YHUtility.encodeLike(orgName) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(orgLeader)) {
        sql += " and org_leader like '%" + YHUtility.encodeLike(orgLeader) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(orgScale)) {
        sql += " and org_scale like '%" + YHUtility.encodeLike(orgScale) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(orgName)) {
        sql += " and org_name like '%" + YHUtility.encodeLike(orgName) + "%' " + YHDBUtility.escapeLike();
      }

      if (!YHUtility.isNullorEmpty(orgEstablishTime)) {
        String str =  YHDBUtility.getDateFilter("org_establish_time", orgEstablishTime, ">=");
        sql += " and " + str;
      }
      if (!YHUtility.isNullorEmpty(orgEstablishTime)) {
        String str =  YHDBUtility.getDateFilter("org_establish_time", orgEstablishTime1, "<=");
        sql += " and " + str;
      }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
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
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath()+ File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath() + File.separator +"profsys"+ File.separator +"source"+ File.separator  + hard + File.separator  + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 更新数据库中的文件

   * @param dbConn
   * @param attachmentId
   * @param attachmentName
   * @param seqId
   * @throws Exception
   */
  public void updateFile(Connection dbConn,String tableName,String attachmentId,String attachmentName,String seqId) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null; 
    try {
      String sql = "update " + tableName + " set ATTACHMENT_ID = ? ,ATTACHMENT_NAME = ? where SEQ_ID=?"   ;
      pstmt = dbConn.prepareStatement(sql);
      pstmt.setString(1, attachmentId);
      pstmt.setString(2,attachmentName);
      pstmt.setString(3, seqId);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
}

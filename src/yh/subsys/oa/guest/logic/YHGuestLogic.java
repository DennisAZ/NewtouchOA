package yh.subsys.oa.guest.logic;

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
import yh.subsys.oa.guest.data.YHGuest;

public class YHGuestLogic {
  private static Logger log = Logger.getLogger(YHGuestLogic.class);
  public static int addGuest(Connection dbConn,YHGuest guest) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, guest);
    return 0;
  }
  public static void updateGuest(Connection dbConn,YHGuest guest) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, guest);
  }
  public static void delGuest(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHGuest.class, Integer.parseInt(seqId));
  }
  public static YHGuest selectGuestById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHGuest guest = (YHGuest) orm.loadObjSingle(dbConn, YHGuest.class, Integer.parseInt(seqId));
    return guest;
  }

  /***
   * 查询所有数据
   * @return
   * @throws Exception 
   */
  public static String queryGuest(Connection dbConn,Map request) throws Exception {
    String sql = "select g.seq_id,g.GUEST_NUM,ci.class_desc,g.GUEST_NAME,g.GUEST_UNIT,"
      +"g.GUEST_PHONE,g.GUEST_DEPT,g.GUEST_ATTEND_TIME,g.GUEST_LEAVE_TIME,g.GUEST_DINER"
      + " from oa_host g left outer join oa_kind_dict_item ci on g.GUEST_TYPE = ci.SEQ_ID where 1=1";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据
   * @return
   * @throws Exception 
   */
  public static String queryGuestTrem(Connection dbConn,Map request,String guestNum,String guestType,String guestName
      ,String guestDiner,String guestUnit,String guestPhone,String guestAttendTime,String guestAttendTime1
      ,String guestLeaveTime,String guestLeaveTime1,String guestCreator,String guestDept,String guestNote) throws Exception {
    String sql = "select g.seq_id,g.GUEST_NUM,ci.class_desc,g.GUEST_NAME,g.GUEST_UNIT,"
      +"g.GUEST_PHONE,g.GUEST_DEPT,g.GUEST_ATTEND_TIME,g.GUEST_LEAVE_TIME,g.GUEST_DINER"
      + " from oa_host g left outer join oa_kind_dict_item ci on g.GUEST_TYPE = ci.SEQ_ID where 1=1";
    if(!YHUtility.isNullorEmpty(guestNum)){
      sql += " and g.guest_num like '%" + YHUtility.encodeLike(guestNum) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(guestType)) {
      sql += " and guest_type = '" + guestType + "'";
    }
    if (!YHUtility.isNullorEmpty(guestName)) {
      sql += " and guest_name like '%" + YHUtility.encodeLike(guestName) + "%' " + YHDBUtility.escapeLike();
    }

    if (!YHUtility.isNullorEmpty(guestDiner)) {
      sql += " and guest_diner like '%" + YHUtility.encodeLike(guestDiner) + "%' " + YHDBUtility.escapeLike();
    }
    
    
    if (!YHUtility.isNullorEmpty(guestUnit)) {
      sql += " and guest_unit like '%" + YHUtility.encodeLike(guestUnit) + "%' " + YHDBUtility.escapeLike();
    }

    if (!YHUtility.isNullorEmpty(guestPhone)) {
      sql += " and guest_phone like '%" + YHUtility.encodeLike(guestPhone) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(guestAttendTime)) {
      String str =  YHDBUtility.getDateFilter("guest_attend_time", guestAttendTime, ">=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(guestAttendTime1)) {
      String str =  YHDBUtility.getDateFilter("guest_attend_time", guestAttendTime1, "<=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(guestAttendTime)) {
      String str =  YHDBUtility.getDateFilter("guest_leave_time", guestLeaveTime, ">=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(guestAttendTime)) {
      String str =  YHDBUtility.getDateFilter("guest_leave_time", guestLeaveTime1, "<=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(guestCreator)) {
      sql += " and guest_creator = '" + guestCreator + "'";
    }
    if (!YHUtility.isNullorEmpty(guestDept)) {
      sql += " and " + YHDBUtility.findInSet(guestDept, "guest_dept");
    }
    if (!YHUtility.isNullorEmpty(guestNote)) {
      sql += " and guest_note like '%" + YHUtility.encodeLike(guestNote) + "%' " + YHDBUtility.escapeLike();
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
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath() + File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath() + File.separator + "guest" + File.separator + hard + File.separator + fileName);
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

package yh.subsys.oa.rollmanage.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.rollmanage.data.YHRmsRoll;

public class YHRmsRollLogic {
  private static Logger log = Logger.getLogger(YHRmsRollLogic.class);

  public void add(Connection conn, YHRmsRoll rmsRollRoom) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(conn, rmsRollRoom);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
 
  public String getRmsRollJson(Connection dbConn, Map request, YHPerson person, String seqId, String flag) throws Exception {
    String sql = "";
    if(person.isAdminRole() || "1".equals(flag)){
     sql = "select "
       + "  RMS_ROLL.SEQ_ID"
       + ", RMS_ROLL.ROLL_CODE"
       + ", RMS_ROLL.ROLL_NAME"
       + ", RMS_ROLL.ROOM_ID"
       + ", RMS_ROLL.CATEGORY_NO"
       + ", RMS_ROLL.CERTIFICATE_KIND"
       + ", RMS_ROLL.SECRET"
       + ", RMS_ROLL.STATUS"
       + " from oa_archives_volume as RMS_ROLL left join  oa_archives_volume_room as RMS_ROLL_ROOM on RMS_ROLL.ROOM_ID = RMS_ROLL_ROOM.SEQ_ID where 1 = 1 ";
    }else{
     sql = "select "
       + " RMS_ROLL.SEQ_ID"
       + ", RMS_ROLL.ROLL_CODE"
       + ", RMS_ROLL.ROLL_NAME"
       + ", RMS_ROLL.ROOM_ID"
       + ", RMS_ROLL.CATEGORY_NO"
       + ", RMS_ROLL.CERTIFICATE_KIND"
       + ", RMS_ROLL.SECRET"
       + ", RMS_ROLL.STATUS"
       + " from oa_archives_volume as RMS_ROLL left join oa_archives_volume_room as RMS_ROLL_ROOM on RMS_ROLL.ROOM_ID = RMS_ROLL_ROOM.SEQ_ID where (RMS_ROLL.ADD_USER = '" + String.valueOf(person.getSeqId()) + "' or RMS_ROLL.MANAGER = '"  + String.valueOf(person.getSeqId()) + "')" ;
    }
    if(!YHUtility.isNullorEmpty(seqId)){
      sql = sql + " and RMS_ROLL_ROOM.SEQ_ID=" + Integer.parseInt(seqId) ;
    }
    sql=sql+" order by (case when PRIORITY is null then 99999 else PRIORITY end)";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  public YHRmsRoll getRmsRollDetail(Connection conn, int seqId)
      throws Exception {

    try {
      YHORM orm = new YHORM();
      return (YHRmsRoll) orm.loadObjSingle(conn, YHRmsRoll.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
 
 /**
  * 编辑案卷
  * @param conn
  * @param rmsRoll
  * @throws Exception
  */
  public void updateRmsRoll(Connection conn, YHRmsRoll rmsRoll)
      throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.updateSingle(conn, rmsRoll);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }

  /**
   * 删除案卷的同时编辑文件的rollId=0
   * 
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void updateRmsFile(Connection conn, int seqId) throws Exception {
    try {
      Map m = new HashMap();
      m.put("seqId", seqId);
      m.put("rollId", 0);
      YHORM orm = new YHORM();
      orm.updateSingle(conn, "rmsFile", m);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
 
 /**
  * 删除一条卷库
  * @param conn
  * @param seqId
  * @throws Exception
  */
  public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHRmsRoll.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }

  public void deleteAll(Connection conn, String loginUserId, YHPerson person)
      throws Exception {
    String sql = "";
    if (person.isAdminRole()) {
      sql = "DELETE FROM oa_archives_volume_room";
    } else {
      sql = "DELETE FROM oa_archives_volume_room WHERE ADD_USER = '" + loginUserId + "'";
    }
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
 
  public void deleteAllRoll(Connection conn, String seqIdStr) throws Exception {
    String sql = "DELETE FROM oa_archives_volume WHERE SEQ_ID IN(" + seqIdStr + ")";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
 
  public void updateAllRoll(Connection dbConn, String seqIdStr)
      throws Exception {
    String sql = "update oa_archives_attach set ROLL_ID=0 WHERE ROLL_ID IN (" + seqIdStr + ")";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
 
  public String getRmsRollRoomNameLogic(Connection conn, int seqId)
      throws Exception {
    String result = "";
    String sql = " select ROOM_NAME from oa_archives_volume_room where SEQ_ID = " + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
 
 /**
  * 获取系统代码表中对映的字段描述
  * @param conn
  * @param classCode
  * @param classNo
  * @return
  * @throws Exception
  */
  public String getCodeNameLogic(Connection conn, String classCode,
      String classNo) throws Exception {
    String result = "";

    String sql = " select CLASS_DESC from OA_KIND_DICT_ITEM where CLASS_CODE = '" + classCode + "' and CLASS_NO = '" + classNo + "'";

    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
 
  public void deleteAll(Connection conn, String seqIds) throws Exception {
    String sql = "DELETE FROM oa_address WHERE SEQ_ID IN(" + seqIds + ")";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
 
 /**
  * 查看文件
  * @param dbConn
  * @param request
  * @param person
  * @return
  * @throws Exception
  */
  public String getRmsFileJosn(Connection dbConn, Map request, YHPerson person, int roomId) throws Exception {
    String sql="SELECT SEQ_ID," +
                    "ROLL_ID," +
                 		"FILE_CODE," +
                 		"FILE_TITLE," +
                 		"SECRET," +
                 		"SEND_UNIT," +
                 		"SEND_DATE," +
                 		"URGENCY from oa_archives_attach where ROLL_ID=" + roomId + " and ( DEL_USER = '' or DEL_USER is null )";

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
 
  public void changeRmsRollSelect(Connection conn, String seqStr, int rollId)
      throws Exception {
    String sql = "update oa_archives_attach SET ROLL_ID = " + rollId + " WHERE SEQ_ID IN(" + seqStr + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }
 
 /**
  * 文件档案导出
  * @param conn
  * @return
  * @throws Exception
  */
 public ArrayList<YHDbRecord> toExportRmsFileData(Connection conn, String seqIdStr) throws Exception{
   ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
   String sql = "SELECT "
         + " FILE_CODE "
         + ",FILE_SUBJECT "
         + ",FILE_TITLE "
         + ",FILE_TITLEO "
         + ",SEND_UNIT"
         + ",SEND_DATE"
         + ",SECRET"
         + ",URGENCY"
         + ",FILE_TYPE"
         + ",FILE_KIND"
         + ",FILE_PAGE"
         + ",PRINT_PAGE"
         + ",REMARK"
         + " from oa_archives_attach where SEQ_ID IN (" + seqIdStr + ") and (DEL_USER = '' or DEL_USER is null)";
   PreparedStatement ps = null;
   ResultSet rs = null;
   try {
     ps = conn.prepareStatement(sql);
     rs = ps.executeQuery() ;
     while (rs.next()) {
       YHDbRecord record = new YHDbRecord();
       String fileCode = rs.getString(1);
       String fileSubject = rs.getString(2);
       String fileTitle = rs.getString(3);
       String fileTitleo = rs.getString(4);
       String sendUnit = rs.getString(5);
       Date sendDate = rs.getTimestamp(6);
       String secret = rs.getString(7);
       String urgency = rs.getString(8);
       String fileType = rs.getString(9);
       String fileKind = rs.getString(10);
       String filePage = rs.getString(11);
       String printPage = rs.getString(12);
       String remark = rs.getString(13);
       
       record.addField("文件号", fileCode);
       record.addField("文件主题词", fileSubject);
       record.addField("文件标题",  fileTitle);
       record.addField("文件副标题", fileTitleo);
       record.addField("发文单位", sendUnit);
       record.addField("发文日期",YHUtility.getDateTimeStrCn(sendDate));
       record.addField("密级",getRmsSecret(conn, secret).toString());
       record.addField("紧急等级",getUrgency(conn, urgency).toString());
       record.addField("文件分类",getFileType(conn, fileType).toString());
       record.addField("公文类别",getFileKind(conn, fileKind).toString());
       record.addField("文件页数",filePage);
       record.addField("打印页数",printPage);
       record.addField("备注",remark);
       result.add(record);
     }
   } catch (Exception e) {
     throw e;
   } finally {
     YHDBUtility.close(ps, rs, null);
   }
   return result;
 }
 
 /**
  * 从系统代码表中获取密级
  * @param conn
  * @param secret
  * @return
  * @throws Exception
  */
  public String getRmsSecret(Connection conn, String secret) throws Exception {
    String result = "";

    String sql = "select CLASS_DESC from OA_KIND_DICT_ITEM where CLASS_CODE='" + secret + "' and CLASS_NO = 'RMS_SECRET'";

    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
 
  public String getFileKind(Connection conn, String fileKind) throws Exception {
    String result = "";

    String sql = "select CLASS_DESC from OA_KIND_DICT_ITEM where CLASS_CODE='" + fileKind + "' and CLASS_NO = 'RMS_FILE_KIND'";

    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
 
  public String getFileType(Connection conn, String fileType) throws Exception {
    String result = "";

    String sql = "select CLASS_DESC from OA_KIND_DICT_ITEM where CLASS_CODE='" + fileType + "' and CLASS_NO = 'RMS_FILE_TYPE'";

    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  public String getUrgency(Connection conn, String urgency) throws Exception {
    String result = "";

    String sql = "select CLASS_DESC from OA_KIND_DICT_ITEM where CLASS_CODE='" + urgency + "' and CLASS_NO = 'RMS_URGENCY'";
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  public String getRmsRollSearchJson(Connection dbConn, Map request, YHPerson person,
      String rollCode, String rollName, String roomId, String years, String beginDate0, String beginDate1, String endDate0, String endDate1, String secret,
      String deadline0, String deadline1, String categoryNo, String catalogNo, String archiveNo, String boxNo, String microNo, String certificateKind,
      String certificateStart0, String certificateStart1, String rollPage0, String rollPage1, String deptId, String remark, String certificateEnd0, String certificateEnd1) throws Exception {
    String sql = "";
    if(person.isAdminRole()){
      sql = "select "
        + "SEQ_ID"
        + ", ROLL_CODE"
        + ", ROLL_NAME"
        + ", ROOM_ID"
        + ", CATEGORY_NO"
        + ", CERTIFICATE_KIND"
        + ", SECRET"
        + ", STATUS"
        + " from oa_archives_volume where 1=1 ";
    }else{
      sql = "select "
        + "SEQ_ID"
        + ", ROLL_CODE"
        + ", ROLL_NAME"
        + ", ROOM_ID"
        + ", CATEGORY_NO"
        + ", CERTIFICATE_KIND"
        + ", SECRET"
        + ", STATUS"
        + " from oa_archives_volume where (ADD_USER = '" + person.getSeqId()+ "' or MANAGER = '" + person.getSeqId() + "')";
    }

    if(!YHUtility.isNullorEmpty(rollCode)){ 
      sql = sql + " and ROLL_CODE like '%" + rollCode + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(rollName)){ 
      sql = sql + " and ROLL_NAME like '%" + rollName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(roomId)){ 
      sql = sql + " and ROOM_ID like '%" + roomId + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(years)){ 
      sql = sql + " and YEARS like '%" + years + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(beginDate0)){ 
      sql = sql + " and "+ YHDBUtility.getDateFilter("BEGIN_DATE", beginDate0, ">=");
    } 
    if(!YHUtility.isNullorEmpty(beginDate1)){ 
      sql = sql + " and "+ YHDBUtility.getDateFilter("BEGIN_DATE", beginDate1, "<=");
    } 
    if(!YHUtility.isNullorEmpty(endDate0)){ 
      sql = sql + " and "+ YHDBUtility.getDateFilter("END_DATE", endDate0, ">=");
    } 
    if(!YHUtility.isNullorEmpty(endDate1)){ 
      sql = sql + " and "+ YHDBUtility.getDateFilter("END_DATE", endDate1, "<=");
    } 
    if(!YHUtility.isNullorEmpty(secret)){ 
      sql = sql + " and SECRET like '%" + secret + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(deadline0)){ 
      sql = sql + " and DEADLINE >= '" + deadline0 + "'";
    } 
    if(!YHUtility.isNullorEmpty(deadline1)){ 
      sql = sql + " and DEADLINE <= '" + deadline1 + "'";
    } 
    if(!YHUtility.isNullorEmpty(categoryNo)){ 
      sql = sql + " and CATEGORY_NO like '%" + categoryNo + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(catalogNo)){ 
      sql = sql + " and CATALOG_NO like '%" + catalogNo + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(archiveNo)){ 
      sql = sql + " and ARCHIVE_NO like '%" + archiveNo + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(boxNo)){ 
      sql = sql + " and BOX_NO like '%" + boxNo + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(microNo)){ 
      sql = sql + " and MICRO_NO like '%" + microNo + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(certificateKind)){ 
      sql = sql + " and CERTIFICATE_KIND like '%" + certificateKind + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(certificateStart0)){ 
      sql = sql + " and CERTIFICATE_START >= '" + certificateStart0 + "'";
    } 
    if(!YHUtility.isNullorEmpty(certificateStart1)){ 
      sql = sql + " and CERTIFICATE_START <= '" + certificateStart1 + "'"; 
    } 
    if(!YHUtility.isNullorEmpty(certificateEnd0)){ 
      sql = sql + " and CERTIFICATE_END >= '" + certificateEnd0 + "'";
    } 
    if(!YHUtility.isNullorEmpty(certificateEnd1)){ 
      sql = sql + " and CERTIFICATE_END <= '" + certificateEnd1 + "'"; 
    } 
    if(!YHUtility.isNullorEmpty(rollPage0)){ 
      sql = sql + " and ROLL_PAGE >= '" + rollPage0 + "'";
    } 
    if(!YHUtility.isNullorEmpty(rollPage1)){ 
      sql = sql + " and ROLL_PAGE <= '" + rollPage1 + "'"; 
    } 
    if(!YHUtility.isNullorEmpty(deptId)){ 
      sql = sql + " and DEPT_ID = " + deptId + ""; 
    } 
    if(!YHUtility.isNullorEmpty(remark)){ 
      sql = sql + " and REMARK like '%" + remark + "%'" + YHDBUtility.escapeLike(); 
    } 
    sql = sql + " order by ROLL_CODE desc";
    
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
     
    return pageDataList.toJson();
  }
}

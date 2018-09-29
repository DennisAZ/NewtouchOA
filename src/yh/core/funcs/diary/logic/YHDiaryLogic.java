package yh.core.funcs.diary.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import org.apache.log4j.Logger;

import yh.core.data.YHDBMapUtil;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.calendar.data.YHCalendarDiary;
import yh.core.funcs.calendar.logic.YHCalendarDiaryLogic;
import yh.core.funcs.diary.data.YHDiary;
import yh.core.funcs.diary.data.YHDiaryCont;
import yh.core.funcs.diary.data.YHDiaryLock;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

/**
 * 工作日志逻辑层
 * 
 * @author TTlang
 * 
 */
public class YHDiaryLogic {

  private static Logger log = Logger
      .getLogger("yh.core.funcs.diary.logic.YHDiaryLogic");

  /**
   * 保存日志的逻辑
   * 
   * @param conn
   * @param userId
   * @param request
   *          request.getParameterMap()
   * @throws Exception
   */
  public void saveLogic(Connection conn, int userId, Map request)
      throws Exception {
    YHORM orm = new YHORM();
    try {
      YHDiary diary = (YHDiary) YHFOM.build(request, YHDiary.class, null);
      //System.out.println("工作日志 ：" + diary);
      String attachmentIdOld = "";
      String attachmentNameOld = "";
      String attachmentId = diary.getAttachmentId();
      String attachmentName = diary.getAttachmentName();
      String cal = request.get("cal") == null ? "" : ((String[])request.get("cal"))[0];
      // 初始化 DIA_TIME CONTENT ATTACHMENT_NAME ATTACHMENT_ID
      diary.setDiaTime(new Date());
      diary.setUserId(userId);
      String content = diary.getContent();
      String Compresscontent = YHDiaryUtil.cutHtml(content);
      diary.setContent(content);
      diary.setCompressContent(Compresscontent);
      try {
        attachmentIdOld = (String) request.get("ATTACHMENT_ID_OLD");
        attachmentNameOld = (String) request.get("ATTACHMENT_NAME_OLD");
      } catch (Exception e) {
        attachmentIdOld = ((String[]) request.get("ATTACHMENT_ID_OLD"))[0];
        attachmentNameOld = ((String[]) request.get("ATTACHMENT_NAME_OLD"))[0];
      }
      if(attachmentId == null){
        attachmentId = "";
        attachmentName = "";
      }
      if(!"".equals(attachmentId) && !attachmentId.endsWith(",")){
        attachmentId += ",";
        attachmentName += "*";
      }
      attachmentId += attachmentIdOld;
      attachmentName += attachmentNameOld;
      YHSelAttachUtil selA = new YHSelAttachUtil(request, YHDiaryCont.MODULE);
      String attrNewId = selA.getAttachIdToString(",");
      String attrNewName = selA.getAttachNameToString("*");
      if(!"".equals(attachmentId) &&!attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      if(!"".equals(attachmentName) && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentId += attrNewId;
      attachmentName += attrNewName;
      
      diary.setAttachmentId(attachmentId);
      diary.setAttachmentName(attachmentName);
      //System.out.println(diary);

      orm.saveSingle(conn, diary);
      int diaryId = getBodyId(conn,"oa_journal");
      if(!"".equals(cal.trim())){
        YHCalendarDiaryLogic cdl = new YHCalendarDiaryLogic();
        YHCalendarDiary caldia = new YHCalendarDiary();
        caldia.setCalDiaDate(diary.getDiaDate());
        caldia.setDiaryId(diaryId);
        caldia.setCalendarId(cal);
        cdl.addCalDiary(conn,caldia);
      }
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 得到email表的SEQ_ID
   * @param conn
   * @return
   * @throws SQLException 
   */
  public int getBodyId(Connection conn,String table) throws Exception{
    String sql = "select Max(SEQ_ID) FROM " + table;
    PreparedStatement pstmt =null;
    ResultSet rs  = null;
    try{
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        return rs.getInt(1);
      }
        return 0;
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
  }
  /**
   * 更新日志的逻辑
   * 
   * @param conn
   * @param userId
   * @param request
   *          request.getParameterMap()
   * @throws Exception
   */
  public void updateLogic(Connection conn, int userId, Map request)
      throws Exception {
    YHORM orm = new YHORM();
    try {
      YHDiary diary = (YHDiary) YHFOM.build(request, YHDiary.class, null);
      //System.out.println("工作日志 ：" + diary);
      String attachmentIdOld = "";
      String attachmentNameOld = "";
      String attachmentId = diary.getAttachmentId();
      String attachmentName = diary.getAttachmentName();
      // 初始化 DIA_TIME CONTENT ATTACHMENT_NAME ATTACHMENT_ID
      diary.setDiaTime(new Date());
      diary.setUserId(userId);
      String content = diary.getContent();
      
     // content = YHDiaryUtil.cutHtml(content);
      String compressContent = YHDiaryUtil.cutHtml(content);
      diary.setContent(content);
      diary.setCompressContent(compressContent);
      try {
        attachmentIdOld = (String) request.get("ATTACHMENT_ID_OLD");
        attachmentNameOld = (String) request.get("ATTACHMENT_NAME_OLD");
      } catch (Exception e) {
        attachmentIdOld = ((String[]) request.get("ATTACHMENT_ID_OLD"))[0];
        attachmentNameOld = ((String[]) request.get("ATTACHMENT_NAME_OLD"))[0];
      }
      if(!"".equals(attachmentId) && !attachmentId.endsWith(",")){
        attachmentId += ",";
        attachmentName += "*";
      }
      attachmentId += attachmentIdOld;
      attachmentName += attachmentNameOld;
      YHSelAttachUtil selA = new YHSelAttachUtil(request, YHDiaryCont.MODULE);
      String attrNewId = selA.getAttachIdToString(",");
      String attrNewName = selA.getAttachNameToString("*");
      if(!"".equals(attachmentId) &&!"".equals(attrNewId) && !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      if(!"".equals(attachmentName) && !"".equals(attrNewName) && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentId += attrNewId;
      attachmentName += attrNewName;
      diary.setAttachmentId(attachmentId);
      diary.setAttachmentName(attachmentName);
      //System.out.println(diary);

      orm.updateSingle(conn, diary);

    } catch (Exception e) {
      throw e;
    }
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
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm,
      String pathPx) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String filePath = pathPx;
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
        fileName = rand + "." + fileName;
        
        while (YHDiaryUtil.getExist(filePath + File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "." + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, filePath + File.separator + YHDiaryCont.MODULE + File.separator + hard + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }

  /**
   * 取得最近十条工作日志，取得当前用户的工作日志
   * 
   * @param conn
   * @param userId
   * @param type 1 代表工作日志，2代表工作日志查询
   * @return
   * @throws Exception
   */
  public List<YHDiary> getLastTenEntryByUserId(Connection conn, int userId,int type)
      throws Exception {
    ArrayList<YHDiary> diaList = null;
    ArrayList<YHDiary> result = new ArrayList<YHDiary>();
    YHORM orm = new YHORM();
    String seqId = getLastTenSeq(conn, userId, type);
    String[] filters = null;
    if(seqId == null || "".equals(seqId) ){
      return result;
    }
    filters = new String[] { " SEQ_ID in(" + seqId + ")"
          + " ORDER BY DIA_DATE DESC ,DIA_TIME DESC " };
    try {
      diaList = (ArrayList<YHDiary>) orm.loadListSingle(conn, YHDiary.class,
          filters);
      if (diaList.size() > 10) {
        result.addAll(diaList.subList(0, 10));
      } else {
        result = diaList;
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  public String getLastTenSeq(Connection conn ,int userId,int type ) throws Exception{
    String sql1 = "select SEQ_ID,USER_ID FROM oa_journal WHERE   USER_ID=" + userId
          + " ORDER BY DIA_DATE DESC ,DIA_TIME DESC ";
    String sql2 = "select SEQ_ID,USER_ID FROM oa_journal WHERE  USER_ID=" + userId + " AND DIA_TYPE = '1' "
          + " ORDER BY DIA_DATE DESC ,DIA_TIME DESC ";
    String result = "";
    PreparedStatement ps = null ;
    ResultSet rs = null;
    try {
      if(type == 1){
        ps = conn.prepareStatement(sql1);
      }else if(type == 2){
        ps = conn.prepareStatement(sql2);
      }
      rs = ps.executeQuery();
      for (int i = 0; i < 10 && rs.next();i++) {
        int seqid = rs.getInt(1);
        if(!"".equals(result)){
          result += ",";
        }
        result += seqid;
      }
    } catch (Exception e) {
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 取得最近十条工作日志，取得当前用户的工作日志
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public List<YHDiary> getLastByDate(Connection conn, int userId, Date date)
      throws Exception {
    ArrayList<YHDiary> diaList = null;
    YHORM orm = new YHORM();
    String ids = getLastDiaIdsByDate(conn, userId, date);
    if ("".equals(ids)) {
      return diaList;
    }
    String[] filters = new String[] { " SEQ_ID IN(" + ids
        + ") ORDER BY DIA_TIME DESC " };
    try {
      diaList = (ArrayList<YHDiary>) orm.loadListSingle(conn, YHDiary.class,
          filters);
    } catch (Exception e) {
      throw e;
    }
    return diaList;
  }

  /**
   * 得到所有当前日期的日志id
   * 
   * @param conn
   * @param userId
   * @param date
   * @return
   * @throws Exception
   */
  public String getLastDiaIdsByDate(Connection conn, int userId, Date date)
      throws Exception {
    String sql = "select " + " SEQ_ID " + " from " + " oa_journal " + " where "
        + " USER_ID = " + userId + " AND " + " DIA_DATE = ?";
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      java.sql.Date sqldate = YHUtility.parseSqlDate(new SimpleDateFormat(
          "yyyy-MM-dd").format(date));
      ps.setDate(1, sqldate);
      rs = ps.executeQuery();
      while (rs.next()) {
        int id = rs.getInt(1);
        if (!"".equals(result)) {
          result += ",";
        }
        result += id;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }

  /**
   * 得到锁标记
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public YHDiaryLock getLock(Connection conn) throws Exception {
    YHDiaryLock diaLoc = new YHDiaryLock();
    String sql = "select " + " PARA_VALUE " + " from " + " SYS_PARA "
        + " where " + " PARA_NAME = 'LOCK_TIME' ";

    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String paraValue = rs.getString(1);
        if(paraValue == null){
          return null;
        }
        String[] params = paraValue.split(",");
        if (!"".equals(params[0])) {
          if(params[0] != null){
            diaLoc.setStartDate(YHUtility.parseDate(params[0]));
          }
        }
        if (!"".equals(params[1])) {
          if(params[1] != null){
            diaLoc.setEndDate(YHUtility.parseDate(params[1]));
          }
        }
        if (!"".equals(params[2])) {
          int dateNum = 0;
          try{
            dateNum = Integer.parseInt(params[2]);
          }catch(Exception e){
            dateNum = 0;
          }
          diaLoc.setPrelockDate(dateNum);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return diaLoc;
  }

  /**
   * @param diaList
   * @param dil
   *          日志锁
   * @return
   * @throws Exception
   */
  public StringBuffer toJson(Connection conn, List<YHDiary> diaList)
      throws Exception {
    StringBuffer result = new StringBuffer();
    StringBuffer field = new StringBuffer();
    YHDiaryLock dil = getLock(conn);
    try {
      for (int i = 0; diaList != null && i < diaList.size(); i++) {
        YHDiary dia = diaList.get(i);
        StringBuffer objJson = YHFOM.toJson(dia);
        boolean isLock = dil == null ? false : dil.isLock(dia.getDiaDate()) ;
        int reviewCou = getReviewCount(conn, dia.getSeqId());
        int lock = 0;
        if (!"".equals(field.toString())) {
          field.append(",");
        }
        if (isLock) {
          lock = 1;
        }
        field.append("{lock:").append(lock).append(",").append("reviewCount:")
            .append(reviewCou).append(",").append("data:").append(objJson)
            .append("}");
      }
    } catch (Exception e) {
      throw e;
    }
    result.append("[").append(field).append("]");
    return result;
  }
  
  /**
   *删除工具类 
   * @param conn
   * @param diaId
   * @throws Exception 
   */
  private void deleteUtilLogic(Connection conn, String tableName ,String filters ) throws Exception {
	  tableName=YHDBMapUtil.getMapDBName(tableName);
	  String sql = " delete from " + tableName ;
    if(filters != null && !"".equals(filters)){
      sql += " where " + filters;
    }
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, null, log);
    }
  } 
  /**
   *删除日志
   * @param conn
   * @param diaId
   * @throws Exception 
   */
  public void deleteDiaryLogic(Connection conn,String ids) throws Exception {
    String filters = " SEQ_ID IN(" + ids + ")";
    String filtersCal = "DIARY_ID IN(" + ids + ")";
    deleteUtilLogic(conn, "DIARY", filters);//删除日志
    try {
      deleteUtilLogic(conn, "CALENDAR_DIARY", filtersCal);//工作日志提醒
    } catch (Exception e) {
    }
  } 
/**
 * 得到评论数
 * @param conn
 * @param diaId
 * @return
 * @throws Exception 
 */
  private int getReviewCount(Connection conn, int diaId) throws Exception {
    int result = 0;
    String sql = "select  count(*) from oa_journal_comment WHERE DIA_ID=" + diaId;
    PreparedStatement ps = null ;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery()  ;
      if(rs.next()){
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e ;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
 /**
  * 取得指定用户已被评论的日志Id号 
  * @param conn
  * @param userId
  * @return
 * @throws Exception 
  */
  public String getCommentDiary(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = "select "
      + " T0.SEQ_ID "
      + " , T0.SUBJECT "
      + " from "
      + " oa_journal T0 "
      + " ,oa_journal_comment T1"
      + " where "
      + " T0.USER_ID = " + userId
      + " AND "
      + " T0.SEQ_ID = T1.DIA_ID "
      + " order by T1.SEND_TIME ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      ArrayList<Integer> ids = new ArrayList<Integer>();
      while(rs.next()){
        int id = rs.getInt(1);
        if(ids.indexOf(id)== -1){
          ids.add(id);
        }else{
          continue;
        }
        if(!"".equals(result)){
          result += ",";
        }
        String subject = rs.getString(2);
        result += "{id:" + id + " , subject:\"" + subject +"\"}";
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return "[" + result + "]";
  }
  /**
   * 工作日志查询取得10条员工日志
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public List<YHDiary> getLastTenEntryBySer(Connection conn, YHPerson per,String moduleId,int privNoFlag )
  throws Exception {
    ArrayList<YHDiary> result = new ArrayList<YHDiary>();
    YHORM orm = new YHORM();
    String[] filters = null;
    String diaId = getLastTenBySerUserId(conn, per, moduleId,privNoFlag);
    if(diaId == null || "".equals(diaId.trim()) ){
      return result;
    }
    
    filters = new String[] { " SEQ_ID IN(" + diaId + ") " + " ORDER BY DIA_DATE DESC ,DIA_TIME DESC " };
    try {
      result = (ArrayList<YHDiary>) orm.loadListSingle(conn, YHDiary.class,filters);
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  public String getLastTenBySerUserId(Connection conn ,YHPerson per,String moduleId,int privNoFlag ) throws Exception{
    ArrayList<Integer> allUserId = YHPrivUtil.getLawfulUserId(conn, per, moduleId, privNoFlag);
    StringBuffer userIdFiltres = new StringBuffer();
    String result = "";
    String userFilter = "";
    if(allUserId.size() <= 0){
      return result;
    }
    if(allUserId.size() > 0 && allUserId.size() < 1000){
      for (Integer integer : allUserId) {
        if(!"".equals(userIdFiltres.toString())){
          userIdFiltres.append(",");
        }
        userIdFiltres.append(integer);
      }
      userFilter = " and USER_ID in (" + userIdFiltres.toString() + ")";
    }
    String sql = "select SEQ_ID,USER_ID FROM oa_journal WHERE DIA_TYPE='1' AND NOT USER_ID=" + per.getSeqId() + " " + userFilter + " ORDER BY DIA_DATE DESC ,DIA_TIME DESC ";
    PreparedStatement ps = null ;
    ResultSet rs = null;
    //System.out.println(sql);
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      YHMyPriv mp = YHPrivUtil.getMyPriv(conn, per , moduleId, privNoFlag);
      for (int i = 0; i < 10 && rs.next();) {
        int userId = rs.getInt(2);
        int seqid = rs.getInt(1);
        if(i < 10 && YHPrivUtil.isUserPriv(conn, userId, mp, per.getPostPriv(), per.getPostDept(), per.getSeqId(), per.getDeptId())){
          if(!"".equals(result)){
            result += ",";
          }
          result += seqid;
          i++;
        }
      }
    } catch (Exception e) {
     throw e;
    }finally{
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request) throws Exception{
    String sql =  "select SEQ_ID,DIA_TIME,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID from oa_journal where 1=1 ";
    String filters = toSearchWhere(request,-1);
    String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    if(!"".equals(filters)){
      sql += filters;
    }
    sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,int userId) throws Exception{
    String sql =  "select SEQ_ID,DIA_TIME,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID from oa_journal where 1=1 ";
    String filters = toSearchWhere(request,userId);
    String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    if(!"".equals(filters)){
      sql += filters;
    }
    sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  
  /**
   * 分页列表 shenrm
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData1(Connection conn,HttpServletRequest request,int userId) throws Exception{
    String sql =  "select SEQ_ID,DIA_TIME,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID from oa_journal where 1=1 ";
    String filters = toSearchWhere1(request,userId);
    String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    if(!"".equals(filters)){
      sql += filters;
    }
    sql += query;
    System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap());
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  /**
   * 查询员工日志（公共事务）分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchDataForInfo(Connection conn,Map request,YHPerson loginPerson,String moduleId ,int privNoFlag) throws Exception{
    String sql = "SELECT " +
    		" oa_journal.SEQ_ID" +
    		",oa_journal.DIA_DATE" +
    		",PERSON.USER_NAME" +
    		",PERSON.DEPT_ID " +
    		",oa_journal.SUBJECT" +
    		",oa_journal.ATTACHMENT_NAME" +
    		",oa_journal.ATTACHMENT_ID" +
    		",oa_journal.USER_ID " +
    		" from " +
    		" oa_journal " +
    		" left join PERSON on " +
    		" PERSON.SEQ_ID = oa_journal.USER_ID" +
    		" where " +
    		" 1=1 ";
    String query = " order by DIA_DATE desc,oa_journal.DIA_TIME DESC ";
    String flawId = getFalwUserId(conn, loginPerson, moduleId, privNoFlag);
    YHMyPriv mp = YHPrivUtil.getMyPriv(conn, loginPerson, moduleId, privNoFlag);
    String filters = toSearchWhereForInfo(conn, request, flawId,mp ,loginPerson);
    if(!"".equals(filters)){
      sql += filters;
    }
    sql += query;
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  /**
   * 组装查询条件
   * @param request
   * @return
   * @throws Exception
   */
  private String toSearchWhere(Map request,int userIdInt) throws Exception{
    String whereStr = "";
    String startDateStr = request.get("startDate") != null ? ((String[])request.get("startDate"))[0] : null;
    String endDateStr = request.get("endDate") != null ? ((String[])request.get("endDate"))[0] : null;
    String userId = "";
    if(userIdInt == -1){
      userId = request.get("userId") != null ? ((String[])request.get("userId"))[0] : null; 
    }else {
      userId = String.valueOf(userIdInt);
    }
    String subject = request.get("subject") != null ? YHDBUtility.escapeLike(((String[])request.get("subject"))[0]) : null;
    String key1 = request.get("key1") != null ? YHDBUtility.escapeLike(((String[])request.get("key1"))[0]) : null;
    String key2 = request.get("key2") != null ? YHDBUtility.escapeLike(((String[])request.get("key2"))[0]) : null;
    String key3 = request.get("key3") != null ? YHDBUtility.escapeLike(((String[])request.get("key3"))[0]) : null;
    String diaType = request.get("diaType") != null ? ((String[])request.get("diaType"))[0] : null;
    String attachmentName = request.get("attachmentName") != null ? YHDBUtility.escapeLike(((String[])request.get("attachmentName"))[0]) : null;

    if(userId != null && !"".equals(userId)){
      whereStr += " and USER_ID = " + userId;
    }
  //加上开始日期、截止日期条件
  if(startDateStr != null && !"".equals(startDateStr)){
    startDateStr += " 00:00:00";
    String dbDateF = YHDBUtility.getDateFilter("DIA_DATE", startDateStr, " >= ");
    whereStr += " and " + dbDateF;
  }
  if(endDateStr != null && !"".equals(endDateStr)){
     endDateStr += " 23:59:59";
     String dbDateF = YHDBUtility.getDateFilter("DIA_DATE", endDateStr, " <= ");
     whereStr += " and " + dbDateF;
  }
  //加上日志类型条件
  if(diaType != null && !"".equals(diaType)){
    if("0".equals(diaType)){
      whereStr += " and (DIA_TYPE='1' or DIA_TYPE='2')";
    } else {
     whereStr += " and DIA_TYPE='" + diaType + "'";
    }
  }
  //加上标题条件
  if(subject != null && !"".equals(subject)) {
    //subject =  new String(subject.getBytes("iso-8859-1"), "UTF-8"); 
    whereStr += " and SUBJECT like '%" + subject + "%'" +   YHDBUtility.escapeLike();
  }
  if(attachmentName != null && !"".equals(attachmentName)) {
    //subject =  new String(subject.getBytes("iso-8859-1"), "UTF-8"); 
    whereStr += " and ATTACHMENT_NAME like '%" + attachmentName + "%'" +   YHDBUtility.escapeLike();;
  }
  //加上三个关键词条件，关键词对应CONTENT字段（CONTENT应该是滤掉了html格式之后的文本内容）
  if((key1 != null && !"".equals(key1))
      || (key2 != null && !"".equals(key2))
      || (key3 != null && !"".equals(key3))){
     if(key1 == null || "".equals(key1)){
         key1 = "!@#$%^&*()__)(*&^%$#@";
     }
     if(key2 == null || "".equals(key2)){
       key2="!@#$%^&*()__)(*&^%$#@";
     }
     if(key3 == null || "".equals(key3)){
       key3="!@#$%^&*()__)(*&^%$#@";
     }
     whereStr +=  " and (CONTENT like '%" + key1 + "%' " +    YHDBUtility.escapeLike() + " or CONTENT like '%" + key2 + "%' " +    YHDBUtility.escapeLike() + " or CONTENT like '%" + key3 + "%' " +    YHDBUtility.escapeLike() + " )";
  }
    return whereStr;
  }
  
  
  /**
   * 组装查询条件
   * shenrm
   * @param request
   * @return
   * @throws Exception
   */
  private String toSearchWhere1(HttpServletRequest request,int userIdInt) throws Exception{
    String whereStr = "";
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    Map map = request.getParameterMap();
    String startDateStr = map.get("startDate") != null ? ((String[])map.get("startDate"))[0] : null;
    String endDateStr = map.get("endDate") != null ? ((String[])map.get("endDate"))[0] : null;
    String userId = "";
    if(userIdInt == -1){
      userId = map.get("userId") != null ? ((String[])map.get("userId"))[0] : null; 
    }else {
      userId = String.valueOf(userIdInt);
    }
    String subject = map.get("subject") != null ? YHDBUtility.escapeLike(((String[])map.get("subject"))[0]) : null;
    String key1 = map.get("key1") != null ? YHDBUtility.escapeLike(((String[])map.get("key1"))[0]) : null;
    String key2 = map.get("key2") != null ? YHDBUtility.escapeLike(((String[])map.get("key2"))[0]) : null;
    String key3 = map.get("key3") != null ? YHDBUtility.escapeLike(((String[])map.get("key3"))[0]) : null;
    String diaType = map.get("diaType") != null ? ((String[])map.get("diaType"))[0] : null;
    String attachmentName = map.get("attachmentName") != null ? YHDBUtility.escapeLike(((String[])map.get("attachmentName"))[0]) : null;

  //添加查询比当前登录人角色低的人SQL拼接 shenrm
    if(userId != null && !"".equals(userId)){
      //whereStr += " and (USER_ID = '"+userId+"' or USER_ID IN (SELECT SEQ_ID FROM PERSON WHERE USER_PRIV IN(SELECT SEQ_ID FROM USER_PRIV WHERE PRIV_NO IN(SELECT PRIV_NO FROM USER_PRIV WHERE PRIV_NO >(SELECT MIN(PRIV_NO) FROM USER_PRIV WHERE SEQ_ID="+ person.getUserPriv()+")))))";
    	whereStr += " and USER_ID = '"+ userId +"'";
    }
  //加上开始日期、截止日期条件

  if(startDateStr != null && !"".equals(startDateStr)){
    startDateStr += " 00:00:00";
    String dbDateF = YHDBUtility.getDateFilter("DIA_DATE", startDateStr, " >= ");
    whereStr += " and " + dbDateF;
  }
  if(endDateStr != null && !"".equals(endDateStr)){
     endDateStr += " 23:59:59";
     String dbDateF = YHDBUtility.getDateFilter("DIA_DATE", endDateStr, " <= ");
     whereStr += " and " + dbDateF;
  }
  //加上日志类型条件
  if(diaType != null && !"".equals(diaType)){
    if("0".equals(diaType)){
      whereStr += " and (DIA_TYPE='1' or DIA_TYPE='2')";
    } else {
     whereStr += " and DIA_TYPE='" + diaType + "'";
    }
  }
  //加上标题条件
  if(subject != null && !"".equals(subject)) {
    //subject =  new String(subject.getBytes("iso-8859-1"), "UTF-8"); 
    whereStr += " and SUBJECT like '%" + subject + "%'" +   YHDBUtility.escapeLike();
  }
  if(attachmentName != null && !"".equals(attachmentName)) {
    //subject =  new String(subject.getBytes("iso-8859-1"), "UTF-8"); 
    whereStr += " and ATTACHMENT_NAME like '%" + attachmentName + "%'" +   YHDBUtility.escapeLike();;
  }
  //加上三个关键词条件，关键词对应CONTENT字段（CONTENT应该是滤掉了html格式之后的文本内容）
  if((key1 != null && !"".equals(key1))
      || (key2 != null && !"".equals(key2))
      || (key3 != null && !"".equals(key3))){
     if(key1 == null || "".equals(key1)){
         key1 = "!@#$%^&*()__)(*&^%$#@";
     }
     if(key2 == null || "".equals(key2)){
       key2="!@#$%^&*()__)(*&^%$#@";
     }
     if(key3 == null || "".equals(key3)){
       key3="!@#$%^&*()__)(*&^%$#@";
     }
     whereStr +=  " and (CONTENT like '%" + key1 + "%' " +    YHDBUtility.escapeLike() + " or CONTENT like '%" + key2 + "%' " +    YHDBUtility.escapeLike() + " or CONTENT like '%" + key3 + "%' " +    YHDBUtility.escapeLike() + " )";
  }
  	//System.out.println(whereStr);
    return whereStr;
  }
  /**
   * 查询出登录用户的所有合法范围的用户ID
   * @param conn
   * @param loginPerson
   * @param moduleId
   * @param privNoFlag
   * @return
   * @throws Exception 
   */
  public String getFalwUserId(Connection conn,YHPerson loginPerson,String moduleId ,int privNoFlag) throws Exception{
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      YHMyPriv mp = YHPrivUtil.getMyPriv(conn, loginPerson ,moduleId,  privNoFlag);
      
      String sql = "select SEQ_ID FROM PERSON WHERE NOT SEQ_ID = " + loginPerson.getSeqId();
      
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int userId = rs.getInt(1);
        if(YHPrivUtil.isUserPriv(conn, userId, mp, loginPerson.getPostPriv(), loginPerson.getPostDept(), loginPerson.getSeqId(), loginPerson.getDeptId())){
          if(!"".equals(result)){
            result += ",";
          }
          result += userId;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 组装查询条件
   * @param request
   * @return
   * @throws Exception
   */
  private String toSearchWhereForInfo(Connection conn,Map request,String flawId,YHMyPriv mp ,YHPerson person) throws Exception{
    String whereStr = "";
    String startDateStr = request.get("startDate") != null ? ((String[])request.get("startDate"))[0] : null;
    String endDateStr = request.get("endDate") != null ? ((String[])request.get("endDate"))[0] : null;
    String subject = request.get("subject") != null ? YHDBUtility.escapeLike(((String[])request.get("subject"))[0]) : null;
    String diaType = request.get("diaType") != null ? ((String[])request.get("diaType"))[0] : null;
    String toId1 = request.get("toId1") != null ? ((String[])request.get("toId1"))[0] : null;
    String toId = request.get("toId") != null ? ((String[])request.get("toId"))[0] : null;
    String privId = request.get("privId") != null ? ((String[])request.get("privId"))[0] : null;
    String copyToId = request.get("copyToId") != null ? ((String[])request.get("copyToId"))[0] : null;
   
  //加上开始日期、截止日期条件
  if(startDateStr != null && !"".equals(startDateStr)){
    startDateStr += " 00:00:00";
    String dbDateF = YHDBUtility.getDateFilter("DIA_DATE", startDateStr, " >= ");
    whereStr += " and " + dbDateF;
  }
  if(endDateStr != null && !"".equals(endDateStr)){
     endDateStr += " 23:59:59";
     String dbDateF = YHDBUtility.getDateFilter("DIA_DATE", endDateStr, " <= ");
     whereStr += " and " + dbDateF;
  }
  //加上日志类型条件
  if(diaType != null && !"".equals(diaType)){
     whereStr += " and DIA_TYPE='" + diaType + "'";
  }else{
     whereStr += " and DIA_TYPE='1'";
  }
  //加上标题条件
  if(subject != null && !"".equals(subject)) {
    whereStr += " and SUBJECT like '%" + subject + "%'" + YHDBUtility.escapeLike(); 
  }
  //作者
  if(toId1 != null && !"".equals(toId1)){
    String[] toIds = toId1.split(",");
    String toIdStr = "";
    for (int i = 0; i < toIds.length; i++) {
      if(!"".equals(toIds[i].trim())){
        int userId = Integer.valueOf(toIds[i].trim());
        if(YHPrivUtil.isUserPriv(conn, userId, mp,person)){
          if(!"".equals(toIdStr)){
            toIdStr += ",";
          }
          toIdStr += toIds[i].trim();
        }
      }
    }
    if(!"".equals(toIdStr.trim())){
      whereStr += " and oa_journal.USER_ID in (" + toIdStr + ")";
    }else{
      whereStr += " and 1 = 2";
    }
  }
  //范围（部门）
  if(toId != null && !"".equals(toId)) {
    if("0".equals(toId)){
      toId = YHOrgSelectLogic.getAlldept(conn);
    }else{
      if(!"".equals(toId) && !toId.endsWith(",")){
        toId += ",";
      }
      toId += YHPrivUtil.getChildDeptId(conn, toId);
    }
    if (toId.trim().endsWith(",")) {
      toId = toId.substring(0, toId.indexOf(","));
    }
    
    String[] toIds = toId.split(",");
    String toIdStr = "";
    for (int i = 0; i < toIds.length; i++) {
      if(!"".equals(toIds[i].trim())){
        int deptId = Integer.valueOf(toIds[i].trim());
        if(YHPrivUtil.isDeptPriv(conn, deptId, mp, person)){
          if(!"".equals(toIdStr)){
            toIdStr += ",";
          }
          toIdStr += toIds[i].trim();
        }
      }
    }
    if(!"".equals(toIdStr.trim())){
      whereStr += " and PERSON.DEPT_ID in (" + toIdStr + ")";
    }else{
      whereStr += " and 1 = 2 ";
    }
  }
  //角色
  if(privId != null && !"".equals(privId)){
     String roleUserId = getUserIdByRole(conn, privId, mp, person);
     if(!"".equals(roleUserId)){
       whereStr += " and PERSON.SEQ_ID in (" + roleUserId + ")";
     }else{
       whereStr += " and 1 = 2 ";
     }
  }
  //人员
  if(copyToId != null && !"".equals(copyToId)){
     copyToId = toFlawId(copyToId, flawId);
     if(!"".equals(copyToId)){
       whereStr += " and PERSON.SEQ_ID in (" + copyToId + ")";
     }else{
       whereStr += " and PERSON.SEQ_ID = -1 ";
     }
  } else{
    if(!"".equals(flawId)){
      if(flawId.endsWith(",")){
        flawId = flawId.substring(0, flawId.length() - 1);
      }
      whereStr += " and PERSON.SEQ_ID in (" + flawId + ")";
    }else {
      whereStr += " and 1=2 ";
    }
  }
    return whereStr;
  }
  /**
   * 从指定的数字串[12,23,34,45...] 找出指定的数字   * @param statement
   * @param userId
   * @return
   */
  public boolean findId(String statement,int userId){
    boolean result = false;
    String[] ids = statement.split(",");
    for (String id : ids) {
      if("".equals(id.trim())){
        continue;
      }
      int comId = Integer.parseInt(id.trim());
      if (comId == userId) {
        result = true;
        break;
      }
    }
    return result;
  }
  
  /**
   * 从指定的数字串[12,23,34,45...] 找出指定的数字   * @param statement
   * @param userId
   * @return
   */
  private String toFlawId(String statement,String flawId){
    String result = "";
    String[] ids = statement.split(",");
    for (String id : ids) {
      if("".equals(id.trim())){
        continue;
      }
      int comId = Integer.parseInt(id.trim());
      if(findId(flawId, comId)){
        if(!"".equals(result)){
          result += ",";
        }
        result += comId;
      }
    }
    return result;
  }
  /**
   * 取得指定ID的日志共享范围   * @param conn
   * @param diaId 日志id
   * @return
   * @throws Exception
   */
  public String getShareLogic(Connection conn , int diaId) throws Exception{
    String result = "";
    String sql = " select TO_ID from oa_journal where SEQ_ID = " + diaId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
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
   * 取得指定ID的日志共享范围   * @param conn
   * @param diaId 日志id
   * @return
   * @throws Exception
   */
  public String setShareLogic(Connection conn , int diaId,String toId) throws Exception{
    String result = "";
    String sql = " update oa_journal set TO_ID = '" + toId + "' where SEQ_ID =" + diaId;
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return result;
  }
/**
 * 取得用户名称
 * @param conn
 * @param userId
 * @return
 * @throws Exception
 */
  public String getUserNameLogic(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
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
 * 取得部门名称
 * @param conn
 * @param deptId
 * @return
 * @throws Exception
 */
  public String getDeptNameLogic(Connection conn , int deptId) throws Exception{
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID = " + deptId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
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
   * 判断当前日志是否已被评论
   * @param conn
   * @param diaId
   * @return
   * @throws Exception
   */
  public int isCommentLogic(Connection conn , int diaId) throws Exception{
    int result = 0;
    String sql = " select count(SEQ_ID) from oa_journal_COMMENT where DIA_ID = " + diaId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        int count = rs.getInt(1);
        result = count;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 取得所有被Share的日志   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getShareDiary(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = "select "
      + " SEQ_ID "
      + " , SUBJECT "
      + " , TO_ID "
      + " from "
      + " oa_journal "
      + " where "
      + " TO_ID LIKE '%" + userId + "%'" + YHDBUtility.escapeLike()
      + " AND NOT USER_ID = " + userId
      + " order by DIA_TIME DESC ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      ArrayList<Integer> ids = new ArrayList<Integer>();
      while(rs.next()){
        String toId = rs.getString(3);
        if(!findId(toId, userId)){
          continue;
        }
        int id = rs.getInt(1);
        if(ids.indexOf(id)== -1){
          ids.add(id);
        }else{
          continue;
        }
        if(!"".equals(result)){
          result += ",";
        }
        String subject = rs.getString(2);
        result += "{id:" + id + " , subject:\"" + subject +"\"}";
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return "[" + result + "]";
  }
  /**
   * 保存已阅读人员   * @param conn
   * @param userId
   * @param diaId
   * @throws Exception
   */
  public void setReader(Connection conn , int userId,int diaId) throws Exception{
    try {
      YHORM orm = new YHORM();
      YHDiary dia = (YHDiary) orm.loadObjSingle(conn, YHDiary.class, diaId);
      String reader = dia.getReaders() ;
      if(reader != null && !"".equals(reader)){
        if( userId != dia.getUserId() && !findId(reader, userId)){
          if(!reader.trim().endsWith(",")){
            reader += ",";
          }
          reader += userId;
        }
      }else{
        reader = String.valueOf(userId);
      }
      dia.setReaders(reader);
      orm.updateSingle(conn, dia);
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 保存已阅读人员   * @param conn
   * @param userId
   * @param diaId
   * @throws Exception
   */
  public String showReader(Connection conn , int diaId) throws Exception{
    String result = "";
    int count = 0;
    String subject = "";
    try {
      YHORM orm = new YHORM();
      YHDiary dia = (YHDiary) orm.loadObjSingle(conn, YHDiary.class, diaId);
      subject = dia.getSubject();
      String reader = dia.getReaders() ;
      HashMap<Integer, String> map = new HashMap<Integer, String>();
      if(reader != null && !"".equals(reader.trim())){
        String[] userIds = reader.split(",");
        for (String userId : userIds) {
          if("".equals(userId.trim())){
            continue;
          }
          Integer deptId = YHDiaryUtil.getDeptIdByUserId(conn, Integer.valueOf(userId));
          if(map.containsKey(deptId)){
            String value = map.get(deptId);
            if(!findId(value, Integer.valueOf(userId))){
              if(!"".equals(value) && value.trim().endsWith(",")){
                value += ",";
              }
              value += userId;
            }
          }
          map.put(deptId, userId);
        }
        Set<Integer> keys = map.keySet();
        for (Integer key : keys) {
          String userKeys = map.get(key);
          String deptName = YHDiaryUtil.getDeptNameLogic(conn, key);
          String userName = "";
          String[] userKeyArray = userKeys.split(",");
          for (String userKey : userKeyArray) {
            if("".equals(userKey)){
              continue;
            }
            String user = YHDiaryUtil.getUserNameLogic(conn, Integer.valueOf(userKey));
            if(!"".equals(userName)){
              userName += ",";
            }
            userName += user;
            count ++;
          }
          if(!"".equals(result)){
            result += ",";
          }
          result += "{userName:\"" + userName + "\",deptName:\"" + deptName + "\"}";
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return "{count:"+ count +",data:[" + result + "],subject:\"" + subject + "\"}";
  }
  /**
   * 
   * @param conn
   * @param diaryId
   * @return
   * @throws Exception
   */
  public String getDiaryById(Connection conn,String diaryId) throws Exception{
    String result = "";
    ArrayList<YHDiary> dias = getDiaryListById(conn, diaryId);
    result = toJson(conn, dias).toString();
    return result;
  }
  /**
   * 
   * @param conn
   * @param diaryId
   * @return
   * @throws Exception
   */
  public ArrayList<YHDiary> getDiaryListById(Connection conn,String diaryId) throws Exception{
    YHORM orm = new YHORM();
    if(diaryId.trim().endsWith(",")){
      diaryId = diaryId.trim().substring(0, diaryId.trim().length() - 1);
    }
    String[] filters = {" SEQ_ID IN (" + diaryId + ")"}; 
    ArrayList<YHDiary> dias = (ArrayList<YHDiary>) orm.loadListSingle(conn, YHDiary.class, filters);
    return dias;
  }
  /**
   * 
   * @param conn
   * @param diaryId
   * @return
   * @throws Exception
   */
  public ArrayList<YHDiary> getDiaryListNowById(Connection conn,String diaryId) throws Exception{
    YHORM orm = new YHORM();
    if(diaryId.trim().endsWith(",")){
      diaryId = diaryId.trim().substring(0, diaryId.trim().length() - 1);
    }
    String date = YHUtility.getCurDateTimeStr("yyyy-MM-dd");
    String startDate = date + " 00:00:00";
    String endDate = date + " 23:59:59";;
    String diaDateFilter = YHDBUtility.getDateFilter("DIA_DATE", startDate, ">=") + " and " +YHDBUtility.getDateFilter("DIA_DATE", endDate, "<=");
    String[] filters = {" SEQ_ID IN (" + diaryId + ") and (" + diaDateFilter + ")"}; 
    ArrayList<YHDiary> dias = (ArrayList<YHDiary>) orm.loadListSingle(conn, YHDiary.class, filters);
    return dias;
  }
  /**
   * 
   * @param conn
   * @param roleId
   * @param mp
   * @param loginPerson
   * @return
   * @throws Exception
   */
  public String getUserIdByRole(Connection conn,String roleId,YHMyPriv mp,YHPerson loginPerson) throws Exception{
    String userId = "";
    ArrayList<Integer> result = new ArrayList<Integer>();
    String[] roleIds = roleId.split(",");
    for (int i = 0; i < roleIds.length; i++) {
      if("".equals(roleIds[i].trim())){
        continue;
      }
      int roleIdInt = Integer.valueOf(roleIds[i].trim());
      ArrayList<Integer> other = getUserIdByOtherRole(conn, roleIdInt, mp, loginPerson); 
      ArrayList<Integer> roleUserid = getUserIdByRole(conn, roleIdInt, mp, loginPerson); 
      result.addAll(other);
      result.addAll(roleUserid);
    }
    
    for (int i = 0; i < result.size(); i++) {
      if(!"".equals(userId)){
        userId += ",";
      }
      userId += result.get(i);
    }
    return userId;
  }
  /**
   * 
   * @param conn
   * @param roleId
   * @param mp
   * @param loginPerson
   * @return
   * @throws Exception
   */
  public ArrayList<Integer> getUserIdByOtherRole(Connection conn, int roleId,YHMyPriv mp,YHPerson loginPerson) throws Exception{
    ArrayList<Integer>  sb = new  ArrayList<Integer> ();
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql  = "select SEQ_ID , USER_PRIV_OTHER FROM PERSON WHERE  USER_PRIV_OTHER LIKE '%" + roleId + "%'" + YHDBUtility.escapeLike();
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
         String userPrivOther = rs.getString(2);
         if(userPrivOther != null 
             && findId(userPrivOther,roleId,",")){
           int userId = rs.getInt(1);
           if(!YHPrivUtil.isUserPriv(conn, userId, mp, loginPerson.getPostPriv(), loginPerson.getPostDept(), loginPerson.getSeqId(), loginPerson.getDeptId())){
             continue;
           }
           sb.add(userId);
         }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return sb;
  }
  /**
   * 
   * @param conn
   * @param roleId
   * @param mp
   * @param loginPerson
   * @return
   * @throws Exception
   */
  public ArrayList<Integer> getUserIdByRole(Connection conn, int roleId,YHMyPriv mp,YHPerson loginPerson) throws Exception{
    ArrayList<Integer>  sb = new  ArrayList<Integer> ();
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql  = "select SEQ_ID  FROM PERSON WHERE   USER_PRIV='" + roleId + "'";
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt(1);
        if(!YHPrivUtil.isUserPriv(conn
            , userId
            , mp
            ,loginPerson)){
          continue;
        }
        sb.add(userId);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return sb;
  }
  /**
   * 查询ID  
   * @param str
   * @param id
   * @param reg
   * @return
   */
  public boolean findId(String str , int id,String reg){
    String[] strs = str.split(reg);
    for (int i = 0; i < strs.length; i++) {
      if (YHUtility.isInteger(strs[i])) {
        int tempId = Integer.parseInt(strs[i]);
        if(tempId == id){
          return true;
        }
      }
    }
    return false;
  }
  /**
   * 
   * @param conn
   * @param userId
   * @param userName
   * @param privId
   * @return
   * @throws Exception
   */
  public String getUserInFo(Connection conn,int userId,String userName,String privId) throws Exception{
    String privName = getRoleName(conn, privId);
    String result = "";
    result = "{userId:" + userId + ",userName:\"" + userName + "\",privName:\"" + privName + "\"}";
    return result;
  }
  /**
   * 
   * @param conn
   * @param privId
   * @return
   * @throws Exception
   */
  public String getRoleName(Connection conn,String privId ) throws Exception{
    String sql = "SELECT PRIV_NAME FROM USER_PRIV WHERE SEQ_ID=" + privId;
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 
   * @param conn
   * @param request
   * @param userId
   * @return
   * @throws Exception 
   */
  public ArrayList<YHDbRecord> toExportDiaData(Connection conn,Map request,int userId) throws Exception{
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql =  "select SEQ_ID,DIA_TIME,DIA_TYPE,SUBJECT,CONTENT,ATTACHMENT_NAME from oa_journal where 1=1 ";
    String filters = toSearchWhere(request,userId);
    String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    if(!"".equals(filters)){
      sql += filters;
    }
    sql += query;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHDbRecord dbrec = new YHDbRecord();
        int diaId = rs.getInt(1);
        dbrec.addField("日期", YHUtility.getDateTimeStrCn(rs.getDate(2)));
        dbrec.addField("日志类型", "1".equals(rs.getString(3))?"工作日志":"个人日志");
        dbrec.addField("日志标题", rs.getString(4));
        dbrec.addField("日志内容", rs.getString(5));
        dbrec.addField("点评", getCommentValue(conn,diaId));
        dbrec.addField("附件名称", rs.getString(6) == null ? "":rs.getString(6).replaceAll("\\*", "\n"));
        result.add(dbrec);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  public byte[] toWordBytes(ArrayList<YHDbRecord> dbls) throws Exception{
    String documentstr = " <table border=\"1\" cellspacing=\"1\" width=\"95%\" class=\"small\" cellpadding=\"3\">"
     + "<tr style=\"BACKGROUND: #D3E5FA; color: #000000; font-weight: bold;\">"
     + "<td nowrap align=\"center\">日期</td>"
     + "<td nowrap align=\"center\">日志类型</td>"
     + "<td nowrap align=\"center\">日志标题</td>"
     + "<td nowrap align=\"center\">日志内容</td>"
     + "<td nowrap align=\"center\">点评</td>"
     + "<td nowrap align=\"center\">附件名称</td>"
     + "</tr>";
    
    for (int i = 0; i < dbls.size(); i++) {
      YHDbRecord dbr = dbls.get(i);
        documentstr += "<tr style=\"BACKGROUND: #FFFFFF;\">"
         + "<td nowrap align=\"center\" width=\"100\">" + dbr.getValueByName("日期") + "</td>"
         + "<td nowrap align=\"center\" width=\"100\">" + dbr.getValueByName("日志类型") + "</td>"
         + "<td>" + dbr.getValueByName("日志标题") + "</td>"
         + "<td>" + dbr.getValueByName("日志内容") + "</td>"
         + "<td>" + dbr.getValueByName("点评") + "</td>"
         + "<td>" + dbr.getValueByName("附件名称").toString().replaceAll("\n\r", "<br>") 
         + " </td>"
         + "</tr>";
      }
    documentstr +=  "</table>";
    return documentstr.getBytes(YHConst.DEFAULT_CODE);
   }
  private String getCommentValue(Connection conn,int diaId) throws Exception{
    String result = "";
    String sql = "select USER_NAME,CONTENT,SEND_TIME from oa_journal_comment,PERSON where DIA_ID = " + diaId + " AND oa_journal_comment.USER_ID=PERSON.SEQ_ID";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String userName = rs.getString(1);
        String content = rs.getString(2);
        String sentTime = YHUtility.getDateTimeStr(rs.getTimestamp(3));
        result += userName + "  " + sentTime + " \n  " + content + "\n";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}


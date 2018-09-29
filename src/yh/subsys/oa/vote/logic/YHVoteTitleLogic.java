package yh.subsys.oa.vote.logic;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vote.data.YHVoteTitle;

public class YHVoteTitleLogic {
  private static Logger log = Logger.getLogger(YHVoteTitleLogic.class);
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String selectTitle(Connection dbConn,Map request) throws Exception {
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select SEQ_ID,PARENT_ID,FROM_ID,TO_ID"
        + ",PRIV_ID,USER_ID,SUBJECT,CONTENT"
        + ",TYPE,MAX_NUM,MIN_NUM,ANONYMITY,VIEW_PRIV"
        + ",SEND_TIME,BEGIN_DATE,END_DATE,PUBLISH,READERS"
        + ",VOTE_NO,ATTACHMENT_ID,ATTACHMENT_NAME,[TOP]";
    }else {
      sql = "select SEQ_ID,PARENT_ID,FROM_ID,TO_ID"
        + ",PRIV_ID,USER_ID,SUBJECT,CONTENT"
        + ",TYPE,MAX_NUM,MIN_NUM,ANONYMITY,VIEW_PRIV"
        + ",SEND_TIME,BEGIN_DATE,END_DATE,PUBLISH,READERS"
        + ",VOTE_NO,ATTACHMENT_ID,ATTACHMENT_NAME,TOP";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String selectVote(Connection dbConn,Map request,int seqId,String userPriv) throws Exception {
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select vo.SEQ_ID,USER_NAME,de.DEPT_NAME,vo.TO_ID"
        + ",vo.PRIV_ID,vo.USER_ID,vo.SUBJECT_MAIN,vo.TYPE,ANONYMITY"
        + ",vo.BEGIN_DATE,vo.END_DATE,vo.PUBLISH,vo.[TOP],MAX_NUM,vo.MIN_NUM"
        + ",vo.SEND_TIME,vo.READERS,vo.VIEW_PRIV,vo.CONTENT"
        + ",vo.VOTE_NO,vo.ATTACHMENT_ID,vo.ATTACHMENT_NAME,vo.PARENT_ID FROM oa_vote_title vo "
        + " left outer join person son on son.seq_id = vo.FROM_ID "
        + " left outer join oa_department de on de.seq_id = son.DEPT_ID"
        + " where vo.PARENT_ID=0 ";
      if (!userPriv.equals("1")) {
        sql += " and vo.FROM_ID='" + seqId + "'";
      }
      sql += " order by vo.[TOP] desc,vo.BEGIN_DATE desc,vo.SEND_TIME desc ";
    }else {
      sql = "select vo.SEQ_ID,USER_NAME,de.DEPT_NAME,vo.TO_ID"
        + ",vo.PRIV_ID,vo.USER_ID,vo.SUBJECT_MAIN,vo.TYPE,ANONYMITY"
        + ",vo.BEGIN_DATE,vo.END_DATE,vo.PUBLISH,vo.TOP,MAX_NUM,vo.MIN_NUM"
        + ",vo.SEND_TIME,vo.READERS,vo.VIEW_PRIV,vo.CONTENT"
        + ",vo.VOTE_NO,vo.ATTACHMENT_ID,vo.ATTACHMENT_NAME,vo.PARENT_ID FROM oa_vote_title vo "
        + " left outer join person son on son.seq_id = vo.FROM_ID "
        + " left outer join oa_department de on de.seq_id = son.DEPT_ID"
        + " where vo.PARENT_ID=0 ";
      if (!userPriv.equals("1")) {
        sql += " and vo.FROM_ID='" + seqId + "'";
      }
      sql += " order by vo.TOP desc,vo.BEGIN_DATE desc,vo.SEND_TIME desc ";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   * 按条件查询
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static List<YHVoteTitle> selectTitle(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHVoteTitle>  itemList = new ArrayList<YHVoteTitle>();
    itemList = orm.loadListSingle(dbConn, YHVoteTitle.class, str);
    return itemList;
  }
  /**
   * 删除ByVoteIds
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delTitleBySeqIds(Connection dbConn,String seqIds) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_vote_title where seq_id in(" + seqIds + ")";
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /**
   * 删除全部
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delAllTitle(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_vote_title";
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /**
   * 清空按钮 更新数据库
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateTitleBySeqIds(Connection dbConn,String seqIds) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "update oa_vote_title set READERS = '' where SEQ_ID in(" + seqIds + ")";
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  /**
   * 取消置顶 更新数据库
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateNoTopBySeqIds(Connection dbConn,String seqIds) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "update oa_vote_title set [top] = '0' where SEQ_ID in(" + seqIds + ")";
    }else {
      sql = "update oa_vote_title set top = '0' where SEQ_ID in(" + seqIds + ")";
    }
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--syl 个人事务
   * @return
   * @throws Exception 
   */
  public static String selectVoteToCurrent(Connection dbConn,Map request,int seqId,int deptId,String userPriv) throws Exception {
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    String top = "TOP";
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      top = "[TOP]";
    }
    String sql = "select vt.SEQ_ID,vt.FROM_ID,de.DEPT_NAME,p.USER_NAME"
      + ",vt.SUBJECT_MAIN,vt.ANONYMITY,vt.BEGIN_DATE,vt.END_DATE"
      +",vt.TYPE,vt.VIEW_PRIV,vt.PUBLISH,vt.READERS,vt." + top
      + " FROM oa_vote_title vt"
      + " left outer join person p on vt.FROM_ID = p.SEQ_ID"
      + " left outer join oa_department de on de.seq_id = p.DEPT_ID"
      + " where PARENT_ID=0 and PUBLISH='1' " 
      + " and ("
      + YHDBUtility.findInSet("ALL_DEPT","vt.TO_ID")
      + " or " + YHDBUtility.findInSet("0","vt.TO_ID")
      + " or " + YHDBUtility.findInSet(deptId+"","vt.TO_ID")
      + " or " + YHDBUtility.findInSet(seqId+"", "vt.USER_ID")
      + " or " + YHDBUtility.findInSet(userPriv, "vt.PRIV_ID") + ")"

      + " and " + YHDBUtility.getDateFilter("vt.BEGIN_DATE", YHUtility.getCurDateTimeStr(), "<=")
      + " and " + "(" +YHDBUtility.getDateFilter("vt.END_DATE", YHUtility.getCurDateTimeStr(), ">")
      +" or vt.END_DATE is null)";
    
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql += " order by vt.[TOP] desc,vt.BEGIN_DATE desc,vt.SEND_TIME desc ";
    }else {
      sql += " order by vt.TOP desc,vt.BEGIN_DATE desc,vt.SEND_TIME desc ";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--syl 个人事务
   * @return
   * @throws Exception 
   */
  public static String selectVoteToHistory(Connection dbConn,Map request,int seqId,int deptId,String userPriv) throws Exception {
    String sql = "select vt.SEQ_ID,vt.FROM_ID,de.DEPT_NAME,p.USER_NAME"
      + ",vt.SUBJECT,vt.ANONYMITY,vt.BEGIN_DATE,END_DATE"
      +",vt.TYPE,vt.VIEW_PRIV,vt.PUBLISH,vt.READERS"
      + " FROM oa_vote_title vt"
      + " left outer join person p on vt.FROM_ID = p.SEQ_ID"
      + " left outer join oa_department de on de.seq_id = p.DEPT_ID"
      + " where PARENT_ID=0 and PUBLISH='1' " 
      + " and ("
      + YHDBUtility.findInSet("ALL_DEPT","vt.TO_ID")
      + " or " + YHDBUtility.findInSet("0","vt.TO_ID")
      + " or " + YHDBUtility.findInSet(deptId+"","vt.TO_ID")
      + " or " + YHDBUtility.findInSet(seqId+"", "vt.USER_ID")
      + " or " + YHDBUtility.findInSet(userPriv, "vt.PRIV_ID") + ")"
      + " and "  +YHDBUtility.getDateFilter("vt.END_DATE", YHUtility.getCurDateTimeStr(), "<=")
      +" and vt.END_DATE is not null";
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql += " order by vt.[TOP] desc,vt.BEGIN_DATE desc,vt.SEND_TIME desc ";
    }else {
      sql += " order by vt.TOP desc,vt.BEGIN_DATE desc,vt.SEND_TIME desc ";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /**
   * 新建项目
   * 
   * @return
   * @throws Exception
   */
  public static String addVote(Connection dbConn,YHVoteTitle title) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, title);
    return getMaSeqId(dbConn,"oa_vote_title");
  }

  /**
   *返回项目seqId
   * 
   * @return
   * @throws Exception
   */
  public static String getMaSeqId(Connection dbConn,String tableName)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String maxSeqId = "0";
    int seqId = 0;
    String sql = "select max(SEQ_ID) as SEQ_ID from " + tableName;
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        seqId = rs.getInt("SEQ_ID");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    maxSeqId = String.valueOf(seqId);
    return maxSeqId;
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
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath()  + File.separator + "vote" + File.separator +  hard  + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * seqId串转换成privName,userName,deptName串
   * 
   * @return
   * @throws Exception
   */ 
  public static String strString(Connection dbConn,String seqId,String tableName,String tdName) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String sql = "select " + tdName + " from " + tableName + " where seq_id in (" + seqId + ")";
//    System.out.println(sql);
    PreparedStatement ps = null;
    ResultSet rs = null;
    String strString = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        strString += rs.getString(tdName) + ",";
      }
      if (strString.length() > 0) {
        strString = strString.substring(0,strString.length()-1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return strString;
  }

  /**
   * seqId串
   * 
   * @return
   * @throws Exception
   */ 
  public static String strSeqId(Connection dbConn,String seqId,String deptId,String privId) throws Exception {
    String sql = null;
    if (deptId.equals("0") || deptId.equals("ALL_DEPT")) {
      sql = "select SEQ_ID from person ";
    } else {
      sql = "select SEQ_ID from person where 1=1 ";
      String fildIdStr  = ""; 
      if(!YHUtility.isNullorEmpty(seqId)){
        if(seqId.endsWith(",")){
          seqId = seqId.substring(0, seqId.length()-1);
        }
        fildIdStr = fildIdStr + " or SEQ_ID in (" + seqId + ")";
      }
      String newDeptId = getNewSeqId(deptId);
      if(!YHUtility.isNullorEmpty(newDeptId)){
        if(newDeptId.endsWith(",")){
          newDeptId = newDeptId.substring(0, newDeptId.length()-1);
        }
        fildIdStr = fildIdStr + " or DEPT_ID in (" + newDeptId + ")";
      }
      String newPrivId = getNewSeqId(privId);
      if(!YHUtility.isNullorEmpty(newPrivId)){
        if(newPrivId.endsWith(",")){
          newPrivId = newPrivId.substring(0, newPrivId.length()-1);
        }
        fildIdStr = fildIdStr + " or USER_PRIV in (" + newPrivId + ")";
      }
      if(!YHUtility.isNullorEmpty(fildIdStr)){
        if(fildIdStr.startsWith(" or")){
          fildIdStr = fildIdStr.substring(3, fildIdStr.length());
        }
        sql = sql + " and (" + fildIdStr + ")";
      }
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    String strSeqId = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        strSeqId += rs.getString("SEQ_ID") + ",";
      }
      if (strSeqId.length() > 0) {
        strSeqId = strSeqId.substring(0,strSeqId.length()-1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return strSeqId;
  }  
  
  /**
   * 得到投票人数（排除禁止登录用户）

   * 
   * @return
   * @throws Exception
   */ 
  public static String getPersonCount(Connection dbConn,String userId,String deptId,String privId) throws Exception {
    String sql = null;
    if (deptId.equals("0") || deptId.equals("ALL_DEPT")) {
      sql = "select count(*) from person where NOT_LOGIN <> '1'";
    } else {
      sql = "select count(*) from person where 1 =1 ";
      String fildIdStr  = ""; 
      if(!YHUtility.isNullorEmpty(userId)){
        if(userId.endsWith(",")){
          userId = userId.substring(0, userId.length()-1);
        }
        fildIdStr = fildIdStr + " or SEQ_ID in (" + userId + ")";
      }
      String newDeptId = getNewSeqId(deptId);
      if(!YHUtility.isNullorEmpty(newDeptId)){
        if(newDeptId.endsWith(",")){
          newDeptId = newDeptId.substring(0, newDeptId.length()-1);
        }
        fildIdStr = fildIdStr + " or DEPT_ID in (" + newDeptId + ")";
      }
      String newPrivId = getNewSeqId(privId);
      if(!YHUtility.isNullorEmpty(newPrivId)){
        if(newPrivId.endsWith(",")){
          newPrivId = newPrivId.substring(0, newPrivId.length()-1);
        }
        fildIdStr = fildIdStr + " or USER_PRIV in (" + newPrivId + ")";
      }
      if(!YHUtility.isNullorEmpty(fildIdStr)){
        if(fildIdStr.startsWith(" or")){
          fildIdStr = fildIdStr.substring(3, fildIdStr.length());
        }
        sql = sql + " and (" + fildIdStr + ")";
      }
      sql = sql + " and  NOT_LOGIN <> '1'";
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    String count = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        if(!YHUtility.isNullorEmpty(rs.getString(1))){
          count = rs.getString(1);
        }
      }
 
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return count;
  }

  public static String getNewSeqId(String seqId)  {
    String newSeqId = "";
    if(!YHUtility.isNullorEmpty(seqId)){
      if(seqId.endsWith(",")){
        seqId = seqId.substring(0, seqId.length()-1);
      }
      String[] seqIdArray = seqId.split(",");
      for (int i = 0; i < seqIdArray.length; i++) {
        newSeqId = newSeqId + "'" + seqIdArray[i] + "',";
      }
      newSeqId = newSeqId.substring(0, newSeqId.length()-1);
    }
    return newSeqId;
  }
  /**
   *详细信息
   * 
   * @return
   * @throws Exception
   */
  public static YHVoteTitle showDetail(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    return (YHVoteTitle)orm.loadObjComplex(dbConn,YHVoteTitle.class,seqId);
  }
  /**
   * 修改项目
   * 
   * @return
   * @throws Exception
   */
  public static void updateVote(Connection dbConn,YHVoteTitle title) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, title);
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String showVote(Connection dbConn,Map request,String parentId) throws Exception {
    String sql = "select SEQ_ID,PARENT_ID,SUBJECT,TYPE,MAX_NUM,MIN_NUM,ANONYMITY FROM oa_vote_title "
      + " WHERE PARENT_ID='" + parentId + "' order by VOTE_NO  ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   * 立即生效,立即终止,恢复终止
   * @return
   * @throws Exception
   */
  public static void updateBeginDate(Connection dbConn,int seqId,String tdName,Date beginDate) throws Exception {
    String sql = "update oa_vote_title set " + tdName + "=? where SEQ_ID=?";
    PreparedStatement ps = null;
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setDate(1,beginDate);
      ps.setInt(2,seqId);
      ps.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps,null, log);
    }
  }
  /**
   * 立即发布
   * @return
   * @throws Exception
   */
  public static void updatePublish(Connection dbConn,int seqId,String publish , int loginUser) throws Exception {
    String sql = "update oa_vote_title set PUBLISH=? where SEQ_ID=?";
    PreparedStatement ps = null;
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setString(1,publish);
      ps.setInt(2,seqId);
      ps.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps,null, log);
    }
    YHORM orm = new YHORM();
    YHVoteTitle voteTitle = (YHVoteTitle)orm.loadObjSingle(dbConn, YHVoteTitle.class, seqId);
    
    if (voteTitle == null) {
      return;
    }
    String query2 ="select * from SYS_PARA where PARA_NAME='SMS_REMIND'";
    Statement stm = null;
    ResultSet rs = null;
    String paraValue = "";
    try {
      stm = dbConn.createStatement();
      rs = stm.executeQuery(query2);
      if(rs.next()) {
        paraValue = rs.getString("PARA_VALUE");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    String[] ss = paraValue.split("\\|");
    String sms = "";
    String sms2 = "";
    if (ss.length < 2) {
      sms = ss[0];
    } else {
      sms = ss[0];
      sms2 = ss[1];
    }
    String s =  voteTitle.getSubject();
    if (s.length() > 100) {
      s = s.substring(0 , 100);
    }
    
    java.util.Date now = new java.util.Date();
    java.util.Date sendTime = new java.util.Date();
    if (now.getTime() < voteTitle.getBeginDate().getTime()) {
      sendTime = voteTitle.getBeginDate();
    }
    String query3 = "";
    if ("ALL_DEPT".equals(voteTitle.getToId()) || "0".equals(voteTitle.getToId())) {
      query3 = "select SEQ_ID from PERSON";
    } else {
      query3 = "select SEQ_ID from PERSON where 1<> 1 ";
      if (!YHUtility.isNullorEmpty(voteTitle.getPrivId())) {
        query3 += " or USER_PRIV in (" + YHWorkFlowUtility.getOutOfTail(voteTitle.getPrivId()) + ")";
      }
      if (!YHUtility.isNullorEmpty(voteTitle.getUserId())) {
        query3 += " or SEQ_ID in (" + YHWorkFlowUtility.getOutOfTail(voteTitle.getUserId()) + ")";
      }
      if (!YHUtility.isNullorEmpty(voteTitle.getToId())) {
        query3 += " or DEPT_ID in (" + YHWorkFlowUtility.getOutOfTail(voteTitle.getToId()) + ")";
      }
    }
    Statement stm3 = null;
    ResultSet rs3 = null;
    String userId = "";
    try {
      stm3 = dbConn.createStatement();
      rs3 = stm3.executeQuery(query3);
      while (rs3.next()) {
        userId += rs3.getInt("SEQ_ID") + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null);
    }
    
    if (yh.core.funcs.doc.util.YHWorkFlowUtility.findId(sms, "11")) {
      
      String sContent = "请查看投票！\n标题：" + s;
      
      String url = "/subsys/oa/vote/show/readVote.jsp?seqId=" + voteTitle.getSeqId() + "&openFlag=1&width=800&height=600";
      if (!"".equals(userId)) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("11");
        sb.setContent(sContent);
        sb.setFromId(loginUser);
        sb.setToId(userId);
        sb.setRemindUrl(url);
        sb.setSendDate(sendTime);
        YHSmsUtil.smsBack(dbConn, sb);
      }
    }
    if (yh.core.funcs.doc.util.YHWorkFlowUtility.findId(sms2, "11")) {
      String query4 =  "select USER_NAME from PERSON where SEQ_ID='"+ loginUser +"'";
      Statement stm4 = null;
      ResultSet rs4= null;
      String userName = "";
      try {
        stm4 = dbConn.createStatement();
        rs4 = stm4.executeQuery( query4);
        if (rs4.next()) {
          userName = rs4.getString("USER_NAME") ;
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm4, rs4, null);
      }
      if (!"".equals(userId)) {
        String content ="OA投票,来自"+ userName +":" + s;
        YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
        ms2l.remindByMobileSms(dbConn, userId ,loginUser, content, sendTime);
      }
    }
  }

  /**
   * 更新数据投票人ID
   * @return
   * @throws Exception
   */
  public static void updateReaders(Connection dbConn,int seqId,String readers) throws Exception {
    String sql = "update oa_vote_title set READERS=? where SEQ_ID=?";
    PreparedStatement ps = null;
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setString(1,readers);
      ps.setInt(2,seqId);
      ps.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps,null, log);
    }
  }
  /***
   * BySeqId
   * @return
   * @throws Exception 
   */
  public static YHVoteTitle selectVoteById(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    YHVoteTitle title = (YHVoteTitle) orm.loadObjSingle(dbConn, YHVoteTitle.class, seqId);
    return title;
  }
  /***
   * 数据导出
   * @return
   * @throws Exception 
   * @throws Exception 
   */
  public static ArrayList<YHDbRecord> getDbRecord(Connection dbConn,List<YHVoteTitle> list) throws Exception{
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHVoteTitle title = new YHVoteTitle();
    int sunNum = 0;
    for (int i = 0; i < list.size(); i++) {
      YHDbRecord dbrec = new YHDbRecord();
      title = list.get(i);
      if (!YHUtility.isNullorEmpty(title.getSubject())) {
        sunNum ++;
        dbrec.addField("标题  ",sunNum + "、" +title.getSubject());
      }else {
        dbrec.addField("标题  ","");
      }
      dbrec.addField("选项  ",title.getContent());  
      dbrec.addField("票数",title.getParentId());
      if (!YHUtility.isNullorEmpty(title.getFromId())) {
        dbrec.addField("投票人",YHVoteTitleLogic.strString(dbConn,title.getFromId(),"PERSON","USER_NAME"));
      }
      if (YHUtility.isNullorEmpty(title.getFromId())) {
        dbrec.addField("投票人","");
      }
      dbL.add(dbrec);
    }
    return dbL;
  }
}

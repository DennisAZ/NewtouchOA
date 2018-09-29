package yh.core.funcs.diary.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import yh.core.funcs.diary.data.YHDiary;
import yh.core.funcs.diary.data.YHDiaryComment;
import yh.core.funcs.diary.data.YHDiaryCommentReply;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHDiaryCommentLogic {

  private static Logger log = Logger
  .getLogger("yh.core.funcs.diary.logic.YHDiaryCommentLogic");
  /**
   * 保存日志评论
   * @param conn
   * @param userId
   * @param request  request.getParameterMap()
   * @throws Exception 
   */
  public void saveCommentLogic(Connection conn ,int userId, Map request,String contextPath) throws Exception{
    YHORM orm = new YHORM();
    try {
      YHDiaryComment dc = (YHDiaryComment) YHFOM.build(request, YHDiaryComment.class, null);
      dc.setSendTime(new Date());
      dc.setCommentFlag("0");
      dc.setUserId(userId);
      String smsRemind = request.get("smsRemind") == null ? "" :((String[])request.get("smsRemind"))[0];
      String sms2Remind = request.get("sms2Remind") == null ? "" :((String[])request.get("sms2Remind"))[0];
      orm.saveSingle(conn, dc);
      String toId  = "";
      YHDiary dia = (YHDiary) orm.loadObjSingle(conn, YHDiary.class, dc.getDiaId());
      toId = String.valueOf(dia.getUserId());
      String contentDate = YHUtility.getDateTimeStrCn(dia.getDiaDate());
      String content = YHDiaryUtil.getUserNameLogic(conn, userId) + "对您 " + contentDate + " 的工作日志“" + dia.getSubject() + "”进行了点评，请查看。";
      if("1".equals(smsRemind.trim())){
        String remindUrl = "/core/funcs/diary/comment/index.jsp?diaId=" + dc.getDiaId();
        doSmsBack(conn, content, userId, toId, String.valueOf(13), remindUrl);
      }
      if("1".equals(sms2Remind.trim())){
        YHMobileSms2Logic ms2l = new YHMobileSms2Logic();
        ms2l.remindByMobileSms(conn, toId, userId, content, new Date());
      }
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 加载评论的逻辑方法
   * @param conn
   * @param diaId
   * @return
   * @throws Exception
   */
  public List<YHDiaryComment> listCommentLogic(Connection conn ,int diaId) throws Exception{
    List<YHDiaryComment> result= null;
    YHORM orm = new YHORM();
    try {
     String[] filters =  new String[]{" DIA_ID = " + diaId + " ORDER BY SEND_TIME ASC"};
     result = orm.loadListSingle(conn, YHDiaryComment.class, filters);
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 加载评论的逻辑方法
   * @param conn
   * @param diaId
   * @return
   * @throws Exception
   */
  public List<YHDiaryCommentReply> listCommentReplyLogic(Connection conn ,int commentId) throws Exception{
    List<YHDiaryCommentReply> result= null;
    YHORM orm = new YHORM();
    try {
     String[] filters =  new String[]{" COMMENT_ID = " + commentId + " ORDER BY REPLY_TIME ASC"};
     result = orm.loadListSingle(conn, YHDiaryCommentReply.class, filters);
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 删除评论
   * @param conn
   * @param commentId
   * @throws Exception 
   */
  public void deleteCommentLogic(Connection conn,int commentId) throws Exception{
    String sql = "delete from oa_journal_comment where SEQ_ID = " + commentId;
   
    //删除评论之前先要删除所有此评论的回复
    String ids = getReplyIdsBycomment(conn, commentId);
    deleteReplyLogic(conn, ids);
    
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 删除评论回复
   * @param conn
   * @param ids
   * @throws Exception 
   */
  public void deleteReplyLogic(Connection conn,String ids) throws Exception{
    if( ids == null || "".equals(ids)){
      return;
    }
    String sql = "delete from oa_journal_reply where SEQ_ID IN(" + ids + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 取得所有当前评论的所有回复
   * @return
   * @throws Exception 
   */
  private String getReplyIdsBycomment(Connection conn,int commentId) throws Exception{
    String result = "";
    String sql = "select SEQ_ID FROM oa_journal_reply WHERE COMMENT_ID = " + commentId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        int replyId = rs.getInt(1);
        if (!"".equals(result)) {
          result += ",";
        }
        result += replyId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 标记当前的评论为已读状态
   * @return
   * @throws Exception 
   */
  public void commentReadedLogic(Connection conn,int commentId) throws Exception{
    String sql = "UPDATE oa_journal_comment SET COMMENT_FLAG='1' WHERE SEQ_ID = " + commentId;
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 保存日志评论回复
   * @param conn
   * @param userId
   * @param request  request.getParameterMap()
   * @throws Exception 
   */
  public void saveCommentReplyLogic(Connection conn ,int userId, Map request,String contextPath) throws Exception{
    YHORM orm = new YHORM();
    try {
      String replyer = String.valueOf(userId);
      YHDiaryCommentReply dcr = (YHDiaryCommentReply) YHFOM.build(request, YHDiaryCommentReply.class, null);
      dcr.setReplyTime(new Date());
      dcr.setReplyer(replyer);
      String smsRemind = "";
      smsRemind = request.get("smsRemind") == null ? "" :((String[])request.get("smsRemind"))[0];
      String sms2Remind = request.get("sms2Remind") == null ? "" :((String[])request.get("sms2Remind"))[0];
      orm.saveSingle(conn, dcr);
      YHDiaryComment diac = (YHDiaryComment) orm.loadObjSingle(conn, YHDiaryComment.class, dcr.getCommentId());
      String content = YHDiaryUtil.getUserNameLogic(conn, userId) + "对您的点评进行了回复，请查看。";
      String toId = String.valueOf(diac.getUserId());
      if("1".equals(smsRemind.trim())){
        //发送内部短信        String remindUrl = "/core/funcs/diary/info/comment.jsp?diaId=" + diac.getDiaId() + "&userId=" + userId;
        doSmsBack(conn, content, userId, toId, String.valueOf(13), remindUrl);
      }
      if("1".equals(sms2Remind.trim())){
        YHMobileSms2Logic ms2l = new YHMobileSms2Logic();
        ms2l.remindByMobileSms(conn, toId, userId, content, new Date());
      }
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 编辑日志回复
   * @param conn
   * @param userId
   * @param request
   * @throws Exception
   */
  public void updateCommentReplyLogic(Connection conn , Map request) throws Exception{
    YHORM orm = new YHORM();
    try {
      String seqIdstr = ((String[]) request.get("commentReplyId"))[0];
      int seqId = Integer.valueOf(seqIdstr);
      YHDiaryCommentReply dcr = (YHDiaryCommentReply) YHFOM.build(request, YHDiaryCommentReply.class, null);
      dcr.setSeqId(seqId);
      String[] smsRemaindParam = (String[])request.get("smsRemind");
      String smsRemind = "";
      if (smsRemaindParam != null && smsRemaindParam.length < 1) {
        smsRemind = smsRemaindParam[0];
      }
      orm.updateSingle(conn, dcr);
      if("1".equals(smsRemind.trim())){
        //发送内部短信
      }
    } catch (Exception e) {
      throw e;
    }
  }
  /**
   * 回复转换成JSON数据
   * @param dcrList
   * @return
   * @throws Exception
   */
  public StringBuffer toJsonFCommentReply(ArrayList<YHDiaryCommentReply> dcrList) throws Exception{
    StringBuffer result = new StringBuffer("[");
    try {
      StringBuffer field = new StringBuffer();
      for (int i = 0 ; dcrList != null && i < dcrList.size(); i++) {
        YHDiaryCommentReply dcr = dcrList.get(i);
        StringBuffer dcrJson = YHFOM.toJson(dcr);
        if(!"".equals(field.toString())){
          field.append(",");
        }
        field.append(dcrJson);
      }
      result.append(field);
    } catch (Exception e) {
      throw e;
    }
    result.append("]");
    return result;
  }
  public void doSmsBack(Connection conn,String content,int fromId,String toId,String type,String remindUrl) throws Exception{
    YHSmsBack sb = new YHSmsBack();
    sb.setContent(content);
    sb.setFromId(fromId);
    sb.setToId(toId);
    sb.setSmsType(type);
    sb.setRemindUrl(remindUrl);
    YHSmsUtil.smsBack(conn, sb);
  }
}

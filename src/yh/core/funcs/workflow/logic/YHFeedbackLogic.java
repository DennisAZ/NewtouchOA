package yh.core.funcs.workflow.logic;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowRunFeedback;
import yh.core.funcs.workflow.data.YHFlowRunPrcs;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.funcs.workflow.util.sort.YHFeedbackComparator;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;

public class YHFeedbackLogic {
  /**
   * 添加会签意见
   * @param feedback
   * @throws Exception
   */
  public void saveFeedback(YHFlowRunFeedback feedback , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    orm.saveSingle(conn, feedback);
  }
  /**
   * 在打印页面取得会签列表
   * @param loginUser
   * @param flowId
   * @param runId
   * @param conn
   * @return "[{feedId:23,userId:23,prcsId:3,content:'ddd',editTime:'2001-12-22 23:12:02',userName:'dd',deptName:'dd',feedName:'ddd',feedDesc:'aaaaa'}]"
   * @throws Exception
   */
  public String getFeedbacksHtml(YHPerson loginUser , int flowId ,int runId , Connection conn) throws Exception{
    Map prcsNameMap = new HashMap();
    Map signLookMap = new HashMap();
    int type = this.getFlowType(conn, flowId);
    this.setPrcsMap(prcsNameMap, signLookMap, runId, flowId , type, conn);
    //取当前流程的、各个步骤的、各个经办人的会签意见
    String query = "SELECT * from oa_fl_run_feedback where RUN_ID="+ runId +" order by PRCS_ID,EDIT_TIME";
    StringBuffer sb = new StringBuffer();
    Statement stm1 = null;
    ResultSet rs1 = null;
    try {
      stm1 = conn.createStatement();
      rs1 = stm1.executeQuery(query);
      int count = 0 ;
      
      while (rs1.next()) {
        int prcsId = rs1.getInt("PRCS_ID");
        int userId = rs1.getInt("USER_ID");
        int flowPrcs = rs1.getInt("FLOW_PRCS");
        String sPrcsId = String.valueOf(prcsId) + ":" + flowPrcs;
        boolean seeSign = true;
        if (type == 1) { //固定流程
          //--- 判断当前用户是否为此步骤实际经办人 ---
          //查询当前流程实例的、当前步骤（实际顺序号）的、登录用户的办理记录
          boolean isPrcsUser = this.isOpUser(prcsId, runId, loginUser.getSeqId(), conn);
          String signLookTmp = (String) signLookMap.get(sPrcsId);
          if ("2".equals(signLookTmp)) {
            if (!isPrcsUser) {//当登录用户不是该步骤经办人
              seeSign = false ;
            }
          } else if ("1".equals(signLookTmp)) {//当前步骤的"会签意见可见性"是“本步骤经办人之间不可见”
            boolean isHoUser = this.isHoUser(prcsId, runId, userId, conn);//是否为主办人
            if (isPrcsUser && userId != loginUser.getSeqId()&& !isHoUser ) {//当前登录用户是该步骤的经办人，主办人并且当前会签意见不是当前登录用户
              seeSign = false ;//设置可见标识为“不可见”
            }
          }
        }
        if (seeSign) {//可见
          count ++;
          
          sb.append("<tr>");
          sb.append("<TD width=\"100%\" align=left>");
          
          String content = rs1.getString("CONTENT");
          //$ATTACHMENT_ID=$ROW["ATTACHMENT_ID"];//附件ID串
         // $ATTACHMENT_NAME=$ROW["ATTACHMENT_NAME"];//附件名称串
          Timestamp date = rs1.getTimestamp("EDIT_TIME");
          String editTime = YHUtility.getDateTimeStr(date);
          int feedFlag = rs1.getInt("FEED_FLAG");
          
//          $SIGN_DATA=$ROW["SIGN_DATA"];//手写签章数据
  //
//          $CONTENT_VIEW=htmlspecialchars($CONTENT);//会签意见，将特殊字符转成HTML的字符串格式
//          $CONTENT_VIEW=UBB2XHTML($CONTENT_VIEW);//转换？？？
//          $CONTENT_VIEW=nl2br($CONTENT_VIEW);//将字符串中的换行符转成HTML的换行符号
          Map nameMap = this.getNames(userId, conn);
          String name = (String)nameMap.get("userName");
          String deptName = (String)nameMap.get("deptName");
          String feedName = "会签";
          if (feedFlag == 1 ) { //1 代表点评意见
            feedName = "<font color=red>点评</font>";
          }
          
          String tmp = "第";
          tmp += prcsId + "步&nbsp;&nbsp;";
          tmp +=  "【" + feedName + "】" +  prcsNameMap.get(sPrcsId)  + "&nbsp;&nbsp;<a href='javascript:' title='" + deptName + "'>" ;
          tmp  += name + "</a>&nbsp;&nbsp;"  + editTime + "&nbsp;&nbsp;" ;
          
         
          sb.append("<div>" + tmp + "</div>");
          if (content == null) {
            content = "";
          }
          sb.append("<div>" + content + "</div>");
          
          sb.append("</TD>");
          sb.append("<tr>");
        }
      }
      
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, rs1, null);
    }
    return sb.toString();
  }
  /**
   * 在打印页面取得会签列表
   * @param loginUser
   * @param flowId
   * @param runId
   * @param conn
   * @return "[{feedId:23,userId:23,prcsId:3,content:'ddd',editTime:'2001-12-22 23:12:02',userName:'dd',deptName:'dd',feedName:'ddd',feedDesc:'aaaaa'}]"
   * @throws Exception
   */
  public String getFeedbacks(YHPerson loginUser , int flowId ,int runId , Connection conn) throws Exception{
    Map prcsNameMap = new HashMap();
    Map signLookMap = new HashMap();
    int type = this.getFlowType(conn, flowId);
    this.setPrcsMap(prcsNameMap, signLookMap, runId, flowId , type, conn);
    //取当前流程的、各个步骤的、各个经办人的会签意见
    String query = "SELECT * from oa_fl_run_feedback where RUN_ID="+ runId +" order by PRCS_ID,EDIT_TIME";
    StringBuffer sb = new StringBuffer();
    Statement stm1 = null;
    ResultSet rs1 = null;
    try {
      stm1 = conn.createStatement();
      rs1 = stm1.executeQuery(query);
      int count = 0 ;
      sb.append("[");
      while (rs1.next()) {
        int prcsId = rs1.getInt("PRCS_ID");
        int flowPrcs = rs1.getInt("FLOW_PRCS");
        int userId = rs1.getInt("USER_ID");
        String sPrcsId = String.valueOf(prcsId) + ":" + flowPrcs;
        boolean seeSign = true;
        if (type == 1) { //固定流程
          //--- 判断当前用户是否为此步骤实际经办人 ---
          //查询当前流程实例的、当前步骤（实际顺序号）的、登录用户的办理记录
          boolean isPrcsUser = this.isOpUser(prcsId, runId, loginUser.getSeqId(), conn);
          String signLookTmp = (String) signLookMap.get(sPrcsId);
          if ("2".equals(signLookTmp)) {
            if (!isPrcsUser) {//当登录用户不是该步骤经办人
              seeSign = false ;
            }
          } else if ("1".equals(signLookTmp)) {//当前步骤的"会签意见可见性"是“本步骤经办人之间不可见”
            boolean isHoUser = this.isHoUser(prcsId, runId, userId, conn);//是否为主办人
            if (isPrcsUser && userId != loginUser.getSeqId()&& !isHoUser ) {//当前登录用户是该步骤的经办人，主办人并且当前会签意见不是当前登录用户
              seeSign = false ;//设置可见标识为“不可见”
            }
          }
        }
        if (seeSign) {//可见
          count ++;
          sb.append("{");
          int feedId = rs1.getInt("SEQ_ID");
          sb.append("feedId:" + feedId);
          sb.append(",userId:" + userId);
          sb.append(",prcsId:" + prcsId);
          String content = rs1.getString("CONTENT");
          if (content == null) {
            content = "";
          }
          content = content.replaceAll("\'", "\\\\'");
          content = content.replaceAll("[\n-\r]", "<br>");
          String attachmentId = rs1.getString("ATTACHMENT_ID");
          String attachmentName = rs1.getString("ATTACHMENT_NAME");
          attachmentId = attachmentId == null ? "" : attachmentId.trim();
          attachmentName = attachmentName == null ? "" : attachmentName.trim();
          sb.append(", attachment:[");
          String[] attachsName = attachmentName.split("\\*");
          String[] attachsId = attachmentId.split(",");
          int count1 = 0 ;
          for ( int i = 0 ;i < attachsId.length ;i++ ) {
            String id = attachsId[i];
            if (!"".equals(id) && i < attachsName.length) {
              String name = attachsName[i];    
              sb.append("{attachmentName:\"" + YHUtility.encodeSpecial(name) + "\"");
              sb.append(",attachmentId:'" + id + "'" +
                  ",ext:'" +  YHFileUtility.getFileExtName(name) + "'},");
              count1++;
            }
          }
          if ( count1 > 0 ) {
            sb.deleteCharAt(sb.length() - 1);
          }
          sb.append("]");
          if (content != null ) {
            sb.append(",content:'" + content + "'");
          } else {
            sb.append(",content:''");
          }
          Timestamp date = rs1.getTimestamp("EDIT_TIME");
          String editTime = YHUtility.getDateTimeStr(date);
          sb.append(",editTime:'" + editTime +"'");
          int feedFlag = rs1.getInt("FEED_FLAG");
          String sign = YHWorkFlowUtility.clob2String(rs1.getClob("SIGN_DATA"));
          if( !"".equals(sign.trim()) ){
            sb.append(",hasSignData:true");
          }
          Map nameMap = this.getNames(userId, conn);
          String name = (String)nameMap.get("userName");
          String deptName = (String)nameMap.get("deptName");
          sb.append(",userName:'" + name +"'");
          sb.append(",deptName:'" + deptName +"'");
          String feedName = "会签";
          if (feedFlag == 1 ) { //1 代表点评意见
            feedName = "<font color=red>点评</font>";
          }
          sb.append(",feedName:'" + feedName + "'");
          sb.append(",feedDesc:'" +  prcsNameMap.get(sPrcsId) + "'");
          sb.append("},");
        }
      }
      if (count > 0 ) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, rs1, null);
    }
    return sb.toString();
  }
  public List<YHFlowRunFeedback> getSignLookFeedback(Map signLookMap , int flowType, int runId  , int userId , Connection conn) throws Exception {
    List<YHFlowRunFeedback> list = new ArrayList();
    String query = "select * from oa_fl_run_feedback where RUN_ID =" + runId + " order by EDIT_TIME DESC";
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      while(rs2.next()){
        boolean seeSign = true;
        int prcsId = rs2.getInt("PRCS_ID");
        int userId1 = rs2.getInt("USER_ID");
        int flowPrcs = rs2.getInt("FLOW_PRCS");
        if (flowType == 1) { //固定流程
          //--- 判断当前用户是否为此步骤实际经办人 ---
          //查询当前流程实例的、当前步骤（实际顺序号）的、登录用户的办理记录
          boolean isPrcsUser = this.isOpUser(prcsId, runId, userId, conn);
          String signLookTmp = (String) signLookMap.get(String.valueOf(prcsId) + ":" + flowPrcs);
          if ("2".equals(signLookTmp)) { 
            if (!isPrcsUser) {//当登录用户不是该步骤经办人
              seeSign = false ;
            }
          } else if ("1".equals(signLookTmp)) {//当前步骤的"会签意见可见性"是“本步骤经办人之间不可见”
            boolean isHoUser = this.isHoUser(prcsId, runId, userId, conn);//是否为主办人
            if (isPrcsUser && userId1 != userId && !isHoUser) {//当前登录用户是该步骤的经办人并且当前会签意见不是当前登录用户并且不是主办人
              seeSign = false ;//设置可见标识为“不可见”
            }
          }
        }
        if (seeSign) {
          YHFlowRunFeedback feedback = new YHFlowRunFeedback();
          feedback.setSeqId(rs2.getInt("SEQ_ID"));
          feedback.setAttachmentId(YHWorkFlowUtility.clob2String(rs2.getClob("ATTACHMENT_ID")));
          feedback.setAttachmentName(YHWorkFlowUtility.clob2String(rs2.getClob("ATTACHMENT_NAME")));
          feedback.setContent(YHWorkFlowUtility.clob2String(rs2.getClob("CONTENT")));
          feedback.setEditTime(rs2.getTimestamp("EDIT_TIME"));
          feedback.setFeedFlag(rs2.getInt("FEED_FLAG"));
          feedback.setPrcsId(prcsId);
          feedback.setRunId(rs2.getInt("RUN_ID"));
          feedback.setSignData(YHWorkFlowUtility.clob2String(rs2.getClob("SIGN_DATA")));
          feedback.setUserId(userId1);
          feedback.setFlowPrcs(flowPrcs);
          list.add(feedback);
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return list;
  }
  public boolean isOpUser(int prcsId , int runId , int userId , Connection conn) throws Exception{ 
    boolean isPrcsUser = false;
    String query = "select 1 from oa_fl_run_prcs where RUN_ID =" + runId + " and PRCS_ID=" + prcsId + " and USER_ID=" + userId;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      if (rs2.next()){
        isPrcsUser = true;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return isPrcsUser;
  }
  /**
   * 是否是主办人
   * @param prcsId
   * @param runId
   * @param userId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean isHoUser(int prcsId , int runId , int userId , Connection conn) throws Exception{ 
    boolean isPrcsUser = false;
    String query = "select 1 from oa_fl_run_prcs where RUN_ID =" + runId + " and PRCS_ID=" + prcsId + " and USER_ID=" + userId + " and OP_FLAG='1'" ;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      if (rs2.next()){
        isPrcsUser = true;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return isPrcsUser;
  }
  public int getFlowType(Connection conn , int flowId) throws Exception {
    int flowType = 0 ;
    String query = "select FLOW_TYPE from oa_fl_type where SEQ_ID =" + flowId;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      while(rs2.next()){
       String  sFlowType = rs2.getString("FLOW_TYPE");
       flowType = Integer.parseInt(sFlowType);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return flowType;
  }
  /**
   * 取出步骤的名字，以及可见性
   * @param prcsNameMap
   * @param signLookMap
   * @param runId
   * @param flowId
   * @param conn
   * @throws Exception
   */
  public void setPrcsMap(Map prcsNameMap , Map signLookMap , int runId , int flowId, int type ,   Connection conn) throws Exception {
    ArrayList<Map> prcsList = new ArrayList();
    String queryPrcs = "select PRCS_ID , FLOW_PRCS from oa_fl_run_prcs where RUN_ID=" + runId;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(queryPrcs);
      while (rs2.next()){
        Map map = new HashMap();
        map.put("flowPrcs", rs2.getInt("FLOW_PRCS"));
        map.put("prcsId", rs2.getInt("PRCS_ID"));
        prcsList.add(map);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    
    for (Map prcs : prcsList) {
      int flowPrcs = (Integer)prcs.get("flowPrcs");
      String query = "select PRCS_NAME " 
        + " , SIGNLOOK "
        + " FROM oa_fl_process WHERE  " 
        + " Flow_SEQ_ID=" + flowId 
        + " and PRCS_ID=" + flowPrcs;
      String prcsName = "";
      String signLook = "";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          prcsName = rs.getString("PRCS_NAME") ;
          signLook = rs.getString("SIGNLOOK");
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      int prcsId = (Integer)prcs.get("prcsId");
      String prcsStr = String.valueOf(prcsId) + ":" + flowPrcs;
      String str = (String) prcsNameMap.get(prcsStr);
      if (str == null 
          ||"".equals(str)) {
        prcsNameMap.put(prcsStr, prcsName);
      }
//      } else if (!YHWorkFlowUtility.findId(prcsStr, prcsName)) {
//        str += "," + prcsName;
//        prcsNameMap.put(prcsStr, str);
//      }
      if (type == 1) {//固定流程
        signLookMap.put(prcsStr , signLook);
      }
    }
  }
  /**
   * 取得用户和部门的名字
   * @param userId
   * @param conn
   * @return
   * @throws Exception
   */
  public Map getNames(int userId , Connection conn) throws Exception {
    Map map = new HashMap();
    String queryName = "SELECT USER_NAME "
      + " ,DEPT_NAME  "
      + " FROM PERSON p , oa_department d WHERE  "
      + " p.SEQ_ID = " + userId
      + "  AND p.DEPT_ID = d.SEQ_ID";
    
    String userName = "";
    String deptName = "";
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(queryName);
      if(rs2.next()){
        userName = rs2.getString("USER_NAME");
        deptName = rs2.getString("DEPT_NAME");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    map.put("userName", userName);
    map.put("deptName", deptName);
    return map;
  }
  /**
   * 取得会签列表
   * @param loginUser
   * @param runId
   * @param prcsId
   * @param flowId
   * @return
   * @throws Exception
   */
  public String getFeedbacks(YHPerson loginUser  , int runId , int prcsId , int flowId , Connection conn) throws Exception{
    Map prcsNameMap = new HashMap();
    Map signLookMap = new HashMap();
    int type = this.getFlowType(conn, flowId);
    this.setPrcsMap(prcsNameMap, signLookMap, runId, flowId , type, conn);
    List<YHFlowRunFeedback> list = this.getSignLookFeedback(signLookMap ,type , runId, loginUser.getSeqId(), conn);
    StringBuffer sb = new StringBuffer();
    for(YHFlowRunFeedback tmp : list){
      sb.append("{seqId:" + tmp.getSeqId());
      sb.append(",prcsId:" + tmp.getPrcsId());
      String signData = tmp.getSignData();
      if (signData != null && !"".equals(signData)) {
        sb.append(",hasSignData:true");
      }
      sb.append(",prcsId:" + tmp.getPrcsId());
      String prcsName = (String)prcsNameMap.get(String.valueOf(tmp.getPrcsId()) + ":" + tmp.getFlowPrcs());
      prcsName = prcsName == null ? "" : prcsName;
      sb.append(",prcsName:'" + prcsName + "'");
      if(tmp.getUserId() == loginUser.getSeqId() 
          && prcsId == tmp.getPrcsId()){
        //有删除修改权限 
        sb.append(",opPriv:1");
      }
      Map nameMap = this.getNames(tmp.getUserId(), conn);
      String userName = (String)nameMap.get("userName");
      String deptName = (String)nameMap.get("deptName");
      String attachmentId = tmp.getAttachmentId() == null ? "" : tmp.getAttachmentId();
      String attachmentName = tmp.getAttachmentName() == null ? "" : tmp.getAttachmentName();
      sb.append(", attachment:[");
      String[] attachsName = attachmentName.split("\\*");
      String[] attachsId = attachmentId.split(",");
      int count = 0 ;
      for ( int i = 0 ;i < attachsId.length ;i ++ ) {
        String id = attachsId[i];
        if (!"".equals(id) && i < attachsName.length) {
          String name = attachsName[i];    
          sb.append("{attachmentName:\"" + YHUtility.encodeSpecial(name) + "\"");
          sb.append(",attachmentId:'" + id + "'" +
              ",ext:'" +  YHFileUtility.getFileExtName(name) + "'},");
          count ++ ;
        }
      }
      if ( count > 0 ) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      sb.append(",userName:'" + userName + "'");
      sb.append(",userId:'" +  tmp.getUserId() + "'");
      sb.append(",deptName:'" + deptName + "'");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      sb.append(",time:'" + sdf.format(tmp.getEditTime()) + "'");
      if (tmp.getContent() != null) {
        String con = tmp.getContent();
        con = con.replaceAll("\'", "\\\\'");
        con = con.replaceAll("\r", "");
        con = con.replaceAll("\n", "<br>");
        sb.append(",content:'" + con + "'},");
      } else {
        sb.append(",content:''},");
      }
      
    }
    if(list.size() > 0){
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  /**
   * 删除
   * @param seqId
   * @throws Exception 
   */
  public void delFeedback(int seqId ,Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowRunFeedback tmp = (YHFlowRunFeedback) orm.loadObjSingle(conn, YHFlowRunFeedback.class, seqId);
    String attachmentId = tmp.getAttachmentId() == null ? "" : tmp.getAttachmentId();
    String attachmentName = tmp.getAttachmentName() == null ? "" : tmp.getAttachmentName();
    YHAttachmentLogic logic = new YHAttachmentLogic();
    logic.deleteAttachments(attachmentId, attachmentName);
    orm.deleteSingle(conn, tmp);
  }
  /**
   * 取得会签
   * @throws Exception 
   */
  public String getFeedback(int seqId ,Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowRunFeedback tmp = (YHFlowRunFeedback) orm.loadObjSingle(conn, YHFlowRunFeedback.class, seqId);
    StringBuffer sb = new StringBuffer();
    sb.append("{seqId:" + tmp.getSeqId());
    if (tmp.getContent() != null) {
      String con = tmp.getContent();
      con = con.replaceAll("\'", "\\\\'");
      con = con.replaceAll("\r", "");
      con = con.replaceAll("\n", "<br>");
      sb.append(",content:'" + con  + "'");
    } else {
      sb.append(",content:''");
    }
    String attachmentId = tmp.getAttachmentId() == null ? "" : tmp.getAttachmentId();
    String attachmentName = tmp.getAttachmentName() == null ? "" : tmp.getAttachmentName();
    sb.append(",attachmentId:'" + attachmentId + "'");
    sb.append(",attachmentName:'" + attachmentName + "'");
    sb.append("}");
    return sb.toString();
  }
  public void updateFeedback(int seqId , String content , String attachmentId , String attachmentName  ,Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowRunFeedback tmp = (YHFlowRunFeedback) orm.loadObjSingle(conn, YHFlowRunFeedback.class, seqId);
    tmp.setContent(content);
    tmp.setAttachmentId(attachmentId);
    tmp.setAttachmentName(attachmentName);
    orm.updateSingle(conn, tmp);
  }
  public String getSignData(int feedId , Connection conn) throws Exception {
    String signData = "";
    String query = "select SIGN_DATA from oa_fl_run_feedback where SEQ_ID = "+ feedId;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      if(rs2.next()){
        signData  = rs2.getString("SIGN_DATA");
        
        if (signData == null) {
          signData = "";
        } else {
          signData = signData.trim();
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return signData; 
  }
  
}

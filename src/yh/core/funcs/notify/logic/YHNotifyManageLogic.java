package yh.core.funcs.notify.logic;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import yh.core.data.YHDbRecord;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.notify.data.YHNotify;
import yh.core.funcs.notify.data.YHNotifyCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.funcs.workflow.util.YHFlowUtil;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHNotifyManageLogic {
  private static Logger log = Logger.getLogger(YHNotifyManageLogic.class);
  private YHNotifyManageUtilLogic notifyManageUtil = new YHNotifyManageUtilLogic();
  public static String filePath = YHSysProps.getAttachPath() + File.separator+ "notify";
  //新建/修改之前调用的方法
  public String beforeAddnotify(Connection dbConn,YHPerson person) throws Exception{
    Statement st = null;
    ResultSet rs = null;
    List auditers = new ArrayList();
    int userId = person.getSeqId();
    StringBuffer sb = new StringBuffer("{");
    String notifyAuditingSingle = "";//标识新建公告是不是需要审批1-需要审批0-不需要审批
    String queryPapaSql = "select PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_SINGLE'";//是否需要审批的checkbox框公告通知是否需要审核1-是

    try {
      st = dbConn.createStatement();
      rs = st.executeQuery(queryPapaSql);
      if(rs.next()) {
       String papaValue =  rs.getString("PARA_VALUE");
       notifyAuditingSingle = papaValue;
       String queryPapa2Sql = "select PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_EXCEPTION'";//是不是在无需审批人员之中
       
       Statement stmt = null;
       ResultSet rss = null;
       stmt = dbConn.createStatement();
       rss = stmt.executeQuery(queryPapa2Sql);
       if(rss.next()) {
         String papaValues = rss.getString(1);
         if(papaValues != null && !"".equals(papaValues)){
           String[] papaValuess = papaValues.split(",");
           for(int j = 0 ;j < papaValuess.length ; j++){
             String papaValuetemp =  papaValuess[j];
             if(papaValuetemp.equals(Integer.toString(person.getSeqId()))){//在不在无需审批人员之中
               notifyAuditingSingle = "0";
             }
           }
         }

       }//end if
      }else {
        notifyAuditingSingle = "0";
      }
      sb.append("notifyAuditingSingle:'").append(notifyAuditingSingle).append("'");
      String queryAuditerSql = "select PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_ALL'";//有公告通知审批权限的人员ID串

      Statement stmttt =dbConn.createStatement();
      ResultSet rsss = stmttt.executeQuery(queryAuditerSql);
      String auditerAllId = "";
      String[] auditerAllIds = null;
      String auditerAllIdtemp = "";
      sb.append(",optionStr:[");
      if(rsss.next()) {
         auditerAllId = rsss.getString("PARA_VALUE");
      }
      if(!"".equals(auditerAllId)&&auditerAllId!=null) {
        auditerAllIds = auditerAllId.split(",");
        for(int i = 0 ;i < auditerAllIds.length; i++) {
          auditerAllIdtemp = auditerAllIds[i];
          if(!"".equals(auditerAllIdtemp.trim())&&auditerAllIdtemp!=null) {
            String queryUserNameSql = "select USER_NAME from PERSON where SEQ_ID=" + auditerAllIdtemp;
            Statement stmtUser = dbConn.createStatement();  
            ResultSet rsUser = stmtUser.executeQuery(queryUserNameSql);
            if(rsUser.next()) {
              YHPerson auditer = new YHPerson();
              auditer.setUserName(rsUser.getString("USER_NAME"));
              auditers.add(auditer);
              sb.append("{");
              sb.append("name:\"" + rsUser.getString("USER_NAME") + "\"");//审批权限人的名称
              sb.append(",value:\"" +auditerAllIdtemp + "\"");//对应的ID
              sb.append("},");
            }
          }
        }
      }
     
      if(auditers.size()>0) {
        sb.deleteCharAt(sb.length() - 1); 
        }
        sb.append("],");
        
      int typeNum = 0;
      String getNotifyTypeSql = "select SEQ_ID,CLASS_DESC from oa_kind_dict_item where CLASS_NO='NOTIFY'";
      Statement typeSt = dbConn.createStatement();
      ResultSet typeRs = typeSt.executeQuery(getNotifyTypeSql);
      sb.append("typeData:[");
      while(typeRs.next()){
        typeNum ++;
        sb.append("{");
        sb.append("typeId:\"" + typeRs.getInt("SEQ_ID") + "\"");//公告类型的id
        sb.append(",typeDesc:\"" + typeRs.getString("CLASS_DESC") + "\"");//公告类型的名称
        sb.append("},");
      }
      if(typeNum >0) {
        sb.deleteCharAt(sb.length() - 1); 
        }
      sb.append("],");
      
      String getSysRemindSql = "select PARA_VALUE from SYS_PARA where PARA_NAME='SMS_REMIND'";//是否需要短息提醒
      Statement sysRemindSt = dbConn.createStatement();
      ResultSet sysRemindRs = sysRemindSt.executeQuery(getSysRemindSql);
      if(sysRemindRs.next()) {
        String sysReminds = sysRemindRs.getString("PARA_VALUE");
//        String[] sysReminds = papaValue.split("|");
//        String defultRemind = papaValues[0];
//        String mobileRemind = papaValues[1];
//        String showRemind = papaValues[2];
//        sb.append("defultRemind:\"" + defultRemind + "\"");
//        sb.append("mobileRemind:\"" + mobileRemind + "\"");
//        sb.append("showRemind:\"" + showRemind + "\"");
        sb.append("sysReminds:\"" + sysReminds + "\"");//1-需要短信提醒0-不需要短信提醒
      }
      sb.append("}");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(st, rs, log);
    }
    return sb.toString();
  }
  
  //点击发布，保存，提交审批时调用的方法
  public int saveMailLogic(Connection conn, YHFileUploadForm fileForm ,YHPerson person,String pathPx, String flag, String subjectFont) throws Exception{
    YHORM orm = new YHORM();
    String urlAdd = "";
    String seqIdStr = "";
    String attachmentId = "";
    String attachmentName = "";
    int bId = -1;
    Map request = fileForm.getParamMap();
    Map<String, String> attr = fileUploadLogic2(fileForm);
    try{
      seqIdStr = ((String[]) request.get("seqId"))[0];
    }catch(Exception e){
      seqIdStr = (String) request.get("seqId");
    }
    try{
      attachmentId = ((String[]) request.get("attachmentId"))[0];
      attachmentName = ((String[]) request.get("attachmentName"))[0];
    }catch(Exception e){
      attachmentId = (String) request.get("attachmentId");
      attachmentName = (String) request.get("attachmentName");
    }
    if(attachmentId != null && !"".equals(attachmentId)){
      if(!(attachmentId.trim()).endsWith(",")){
        attachmentId += ",";
      }
      if(!(attachmentName.trim()).endsWith("*")){
        attachmentName += "*";
      }
    }else {
      attachmentId =  "";
      attachmentName = "";
    }
    try{
      urlAdd = ((String[]) request.get("urlAdd"))[0];
    }catch(Exception e){
      urlAdd = (String) request.get("urlAdd");
    }
    
    Set<String> attrKeys = attr.keySet();
    for (String key : attrKeys){
      String fileName = attr.get(key);
      attachmentId += key + ",";
      attachmentName += fileName + "*";
    }
    YHNotify notify = (YHNotify) YHFOM.build(request, YHNotify.class, null);
    notify.setSubjectFont(subjectFont);
    if("2".equalsIgnoreCase(flag)){
      notify.setContent(notify.getContent());
    }else{
      notify.setContent(notify.getContent());
    }
    
    if("on".equals(notify.getPrint())){
      notify.setPrint("1");
    }
    if("on".equals(notify.getDownload())){
      notify.setDownload("1");
    }
    String mailRemind = "";
    String mobileRemind = "";
    try{
      mailRemind = ((String[]) request.get("mailRemind"))[0];
      mobileRemind = ((String[]) request.get("remind"))[0];
    }catch(Exception e){
      mailRemind = (String) request.get("mailRemind");
      mobileRemind = (String) request.get("remind");
    }
    if("2".equals(notify.getFormat())) {
      notify.setContent(urlAdd);
    }
    if(notify.getSubject() == null || "".equals(notify.getSubject())){
      String subject  = "";
      if(attachmentName != null && !"".equals(attachmentName)) {
       subject = attachmentName.split("\\*")[0];
       subject = subject.substring(subject.indexOf("_") + 1,subject.lastIndexOf("."));     
      }else{
        subject = "[无主题]";
      }
      notify.setSubject(YHUtility.decodeURL(subject));
    }
    int seqId = notify.getSeqId();
    notify.setFromId(Integer.toString(person.getSeqId()));
    if("".equals(notify.getBeginDate())||notify.getBeginDate() == null){
      notify.setBeginDate(new Date());
    }

    notify.setSendTime(new Date());
    if(notify.getToId() == null ||  "".equals(notify.getToId())){
      notify.setToId("-1");
    }
    notify.setAttachmentId(attachmentId);
    notify.setAttachmentName(attachmentName);
    notify.setFromDept(person.getDeptId());
    
    if(seqIdStr != null && !"".equals(seqIdStr)){
      seqId = Integer.valueOf(seqIdStr.trim());
      notify.setSeqId(seqId);
      String fckContent = YHUtility.null2Empty(notify.getContent());      
      notify.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
      notify.setContent(YHDiaryUtil.cutHtml(fckContent));
      if(YHUtility.isNullorEmpty(notify.getTop())){
        notify.setTop("0");
      }
      orm.updateSingle(conn, notify);//修改保存
      bId =  notify.getSeqId();
    }else{
      String fckContent = YHUtility.null2Empty(notify.getContent());      
      notify.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
      notify.setContent(YHDiaryUtil.cutHtml(fckContent));
      orm.saveSingle(conn, notify);//新建保存
      bId = notifyManageUtil.getBodyId(conn);
    }
    String queryFWStr = "";
    String toIdFW = "";
    if("0".equals(notify.getToId())) {  //全体部门
      queryFWStr = "select SEQ_ID from PERSON where NOT_LOGIN!='1'";
    }else {
      queryFWStr += " select SEQ_ID from PERSON where NOT_LOGIN!='1'";
      String toId = notify.getToId();
      if(toId != null && !"".equals(toId.trim())){
        String[] toIds = toId.split(",");
        toId = "";
        for(int j = 0 ;j < toIds.length ; j++){
          toId += toIds[j] + ",";
        }
        toId = toId.substring(0, toId.length() - 1);
      }
      if(!"".equals(toId)&&toId!=null) {
        queryFWStr = queryFWStr + " and (DEPT_ID in (" + toId + ")";
      }
      String privId = notify.getPrivId();
      if(privId != null && !"".equals(privId.trim())){
        String[] privIds = privId.split(",");
        privId = "";
        for(int j = 0 ;j < privIds.length ; j++){
          privId +=  privIds[j]  + ",";
        }
        privId = privId.substring(0, privId.length() - 1);
      }
      if(!"".equals(privId)&&privId!=null) {
        queryFWStr = queryFWStr + " or USER_PRIV in ("+ privId +")";
      }
      String userId = notify.getUserId();
      if(userId != null && !"".equals(userId.trim())){
        String[] userIds = userId.split(",");
        userId = "";
        for(int j = 0 ;j < userIds.length ; j++){
          userId +=  userIds[j]  + ",";
        }
        userId = userId.substring(0, userId.length() - 1);
      }
      if(!"".equals(userId)&&userId!=null) {
        queryFWStr = queryFWStr + " or SEQ_ID in ("+ userId + ")";
      }
      queryFWStr = queryFWStr + ")";
    }
    Statement stmtFW = null;
    ResultSet rsFW = null;
    try {
      stmtFW = conn.createStatement();
      rsFW = stmtFW.executeQuery(queryFWStr);
      while(rsFW.next()) {
        toIdFW = toIdFW + rsFW.getString("SEQ_ID") + ",";
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(stmtFW, rsFW, log);
    }
    if("1".equals(notify.getPublish())&&("on".equals(mailRemind)||"1".equals(mailRemind))) {//如果需要短信提醒（发布）      YHSmsBack smsBack = new YHSmsBack();
      String content = "请查看公告通知！\n标题：" + notify.getSubject();
      String remindUrl = "/core/funcs/notify/show/readNotify.jsp?seqId="+bId + "&openFlag=1";         
      if("2".equalsIgnoreCase(flag)){
        smsBack.setContent(YHUtility.decodeURL(content));
      }else{
        smsBack.setContent(content);
      }
      smsBack.setFromId(person.getSeqId());
      smsBack.setRemindUrl(remindUrl);
      smsBack.setSmsType("1");
      smsBack.setToId(toIdFW);
      if(!"".equals(toIdFW.trim())&&toIdFW!=null&&toIdFW.contains(",")==true){
        YHSmsUtil.smsBack(conn, smsBack);
      }
    }
    if("2".equals(notify.getPublish())&&("on".equals(mailRemind)||"1".equals(mailRemind))) {//如果需要短信提醒（提交审批）      YHSmsBack smsBack = new YHSmsBack();
      String content = "请查看公告审批！\n标题：" + notify.getSubject();
      String remindUrl = "/core/funcs/notify/auditing/index.jsp?openFlag=0&openWidth=800&openHeight=600"; 
      smsBack.setContent(content);
      smsBack.setFromId(person.getSeqId());
      smsBack.setRemindUrl(remindUrl);
      smsBack.setSmsType("1");
      smsBack.setToId(notify.getAuditer());
      if(!"".equals(toIdFW.trim())&&toIdFW!=null&&toIdFW.contains(",")==true){
        YHSmsUtil.smsBack(conn, smsBack);
      }
    }
    if(("2".equals(notify.getPublish())||"1".equals(notify.getPublish())) && "on".equalsIgnoreCase(mobileRemind)){//发短信      String content = "";
      if("2".equals(notify.getPublish())){
        content = "请查看公告审批！\n标题：" + notify.getSubject();
      }else if("1".equals(notify.getPublish())){
        content = "请查看公告审批！\n标题：" + notify.getSubject();
      }       
      YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
      String sms2ToId = notify.getAuditer(); 
      ms2l.remindByMobileSms(conn, sms2ToId==null?"":sms2ToId, person.getSeqId(), content, null);
    }
    return bId;
  } 
  
  
  /**
   * add by 张银友 zyy
 * @param conn
 * @param fileForm
 * @param person
 * @param pathPx
 * @param flag
 * @param subjectFont
 * @return
 * @throws Exception
 * 从工作流发通告保存
 */
  public void saveFlowNotify(Connection conn, Map<String, String[]> requestMap, YHPerson person,String imgPath)throws Exception{
	  YHORM orm = new YHORM();
	  String urlAdd = "";
	  String seqIdStr = "";
	  String runId="";
	  String flowId="";
	  int bId = -1;
	  Map request=new HashMap();
	  
	  for(String s:requestMap.keySet()){
		 String[] str = requestMap.get(s);
		 request.put(s, str[0]);
		 if("runId".equals(s)){
			 runId=str[0];
		 }else if("flowId".equals(s)){
			 flowId=str[0];
		 }
		 System.out.println(s+":"+str[0]);
	  }
	  YHNotify notify = (YHNotify) YHFOM.build(request, YHNotify.class, null);
	
	  if("on".equals(notify.getPrint())){
		  notify.setPrint("1");
	  }
	  if("on".equals(notify.getDownload())){
		  notify.setDownload("1");
	  }
	  String mailRemind = "";
	  String mobileRemind = "";
	  YHFlowUtil util=new YHFlowUtil();
	  try{
		  mailRemind = ((String[]) request.get("mailRemind"))[0];
		  mobileRemind = ((String[]) request.get("remind"))[0];
	  }catch(Exception e){
		  mailRemind = (String) request.get("mailRemind");
		  mobileRemind = (String) request.get("remind");
	  }
	  if("2".equals(notify.getFormat())) {
		  notify.setContent(urlAdd);
	  }
	  if(notify.getSubject() == null || "".equals(notify.getSubject())){
		  String subject  = "[无主题]";
		  notify.setSubject(YHUtility.decodeURL(subject));
	  }
	  int seqId = notify.getSeqId();
	  notify.setFromId(Integer.toString(person.getSeqId()));
	  if("".equals(notify.getBeginDate())||notify.getBeginDate() == null){
		  notify.setBeginDate(new Date());
	  }
	  
	  notify.setSendTime(new Date());
	  if(notify.getToId() == null ||  "".equals(notify.getToId())){
		  notify.setToId("-1");
	  }
	  
	  if(seqIdStr != null && !"".equals(seqIdStr)){
		  seqId = Integer.valueOf(seqIdStr.trim());
		  notify.setSeqId(seqId);
		  String fckContent = YHUtility.null2Empty(notify.getContent());      
		  notify.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
		  notify.setContent(YHDiaryUtil.cutHtml(fckContent));
		  if(YHUtility.isNullorEmpty(notify.getTop())){
			  notify.setTop("0");
		  }
		  orm.updateSingle(conn, notify);//修改保存
		  bId =  notify.getSeqId();
	  }else{
		 
		  String cont= util.getContentFromFlow(person,runId,flowId,conn,imgPath);
		  String fckContent = YHUtility.null2Empty(cont);      
		  notify.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
		  notify.setContent(YHDiaryUtil.cutHtml(fckContent));
		  orm.saveSingle(conn, notify);//新建保存
		  bId = notifyManageUtil.getBodyId(conn);
	  }
	  String queryFWStr = "";
	  String toIdFW = "";
	  if("0".equals(notify.getToId())) {  //全体部门
		  queryFWStr = "select SEQ_ID from PERSON where NOT_LOGIN!='1'";
	  }else {
		  queryFWStr += " select SEQ_ID from PERSON where NOT_LOGIN!='1'";
		  String toId = notify.getToId();
		  if(toId != null && !"".equals(toId.trim())){
			  String[] toIds = toId.split(",");
			  toId = "";
			  for(int j = 0 ;j < toIds.length ; j++){
				  toId += toIds[j] + ",";
			  }
			  toId = toId.substring(0, toId.length() - 1);
		  }
		  if(!"".equals(toId)&&toId!=null) {
			  queryFWStr = queryFWStr + " and (DEPT_ID in (" + toId + ")";
		  }
		  String privId = notify.getPrivId();
		  if(privId != null && !"".equals(privId.trim())){
			  String[] privIds = privId.split(",");
			  privId = "";
			  for(int j = 0 ;j < privIds.length ; j++){
				  privId +=  privIds[j]  + ",";
			  }
			  privId = privId.substring(0, privId.length() - 1);
		  }
		  if(!"".equals(privId)&&privId!=null) {
			  queryFWStr = queryFWStr + " or USER_PRIV in ("+ privId +")";
		  }
		  String userId = notify.getUserId();
		  if(userId != null && !"".equals(userId.trim())){
			  String[] userIds = userId.split(",");
			  userId = "";
			  for(int j = 0 ;j < userIds.length ; j++){
				  userId +=  userIds[j]  + ",";
			  }
			  userId = userId.substring(0, userId.length() - 1);
		  }
		  if(!"".equals(userId)&&userId!=null) {
			  queryFWStr = queryFWStr + " or SEQ_ID in ("+ userId + ")";
		  }
		  queryFWStr = queryFWStr + ")";
	  }
	  Statement stmtFW = null;
	  ResultSet rsFW = null;
	  try {
		  stmtFW = conn.createStatement();
		  rsFW = stmtFW.executeQuery(queryFWStr);
		  while(rsFW.next()) {
			  toIdFW = toIdFW + rsFW.getString("SEQ_ID") + ",";
		  }
	  } catch (Exception e) {
		  throw e;
	  } finally{
		  YHDBUtility.close(stmtFW, rsFW, log);
	  }
	  if("1".equals(notify.getPublish())&&("on".equals(mailRemind)||"1".equals(mailRemind))) {//如果需要短信提醒（发布）
		  YHSmsBack smsBack = new YHSmsBack();
		  String content = "请查看公告通知！\n标题：" + notify.getSubject();
		  String remindUrl = "/core/funcs/notify/show/readNotify.jsp?seqId="+bId + "&openFlag=1";         
		  smsBack.setContent(content);
		  smsBack.setFromId(person.getSeqId());
		  smsBack.setRemindUrl(remindUrl);
		  smsBack.setSmsType("1");
		  smsBack.setToId(toIdFW);
		  if(!"".equals(toIdFW.trim())&&toIdFW!=null&&toIdFW.contains(",")==true){
			  YHSmsUtil.smsBack(conn, smsBack);
		  }
	  }
	 
	  if(("1".equals(notify.getPublish())) && "on".equalsIgnoreCase(mobileRemind)){//发短信
		  String content = "请查看公告通知！\n标题：" + notify.getSubject();
		  YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
		  String sms2ToId = notify.getAuditer(); 
		  ms2l.remindByMobileSms(conn, sms2ToId==null?"":sms2ToId, person.getSeqId(), content, null);
	  }
	  
	  //若有附件，把附件复制过去
	  if(!"".equals(notify.getAttachmentId())){
		  util.copyAttachFlowToWhere(notify.getAttachmentName(),notify.getAttachmentId(),"notify");
	  }
	  
	  
	 
	  System.out.println("bid:"+bId);
	}
  
	

  
  //（处理多文件上传）  public int savettachMailLogic(Connection conn, YHFileUploadForm fileForm ,int fromId,String pathPx) throws Exception{
    YHORM orm = new YHORM();
    String typeId = "";
    String urlAdd = "";
    String copyToId = "";
    String secretToId ="";
    String seqIdStr = "";
    String attachmentId = "";
    String attachmentName = "";
    int bId = -1;
    Map request = fileForm.getParamMap();
    Map<String, String> attr = fileUploadLogic(fileForm);
    try{
      seqIdStr = ((String[]) request.get("seqId"))[0];
    }catch(Exception e){
      seqIdStr = (String) request.get("seqId");
    }
    try{
      attachmentId = ((String[]) request.get("attachmentId"))[0];
      attachmentName = ((String[]) request.get("attachmentName"))[0];
    }catch(Exception e){
      attachmentId = (String) request.get("attachmentId");
      attachmentName = (String) request.get("attachmentName");
    }
    if(attachmentId != null && !"".equals(attachmentId)){
      if(!(attachmentId.trim()).endsWith(",")){
        attachmentId += ",";
      }
      if(!(attachmentName.trim()).endsWith("*")){
        attachmentName += "*";
      }
    }else {
      attachmentId =  "";
      attachmentName = "";
    }
    try{
      urlAdd = ((String[]) request.get("urlAdd"))[0];
    }catch(Exception e){
      urlAdd = (String) request.get("urlAdd");
    }
    
    Set<String> attrKeys = attr.keySet();
    for (String key : attrKeys){
      String fileName = attr.get(key);
      String file = fileName.split("_")[1];
      attachmentId += key + ",";
      attachmentName += fileName + "*";
    }
    YHNotify notify = (YHNotify) YHFOM.build(request, YHNotify.class, null);
    if("on".equals(notify.getPrint())){
      notify.setPrint("1");
    }
    if("on".equals(notify.getDownload())){
      notify.setDownload("1");
    }
    String mailRemind = "";
    String mobileRemind = "";
    try{
      mailRemind = ((String[]) request.get("mailRemind"))[0];
      mobileRemind = ((String[]) request.get("mobileRemind"))[0];
    }catch(Exception e){
      mailRemind = (String) request.get("mailRemind");
      mobileRemind = (String) request.get("mobileRemind");
    }
    if("2".equals(notify.getFormat())) {
      notify.setContent(urlAdd);
    }
//    long size = eb.getEnsize() + getSize(fileForm) ;
//    eb.setEnsize(1l);
    if(notify.getSubject() == null || "".equals(notify.getSubject())){
      String subject  = "";
      if(attachmentName != null && !"".equals(attachmentName)) {
       subject = attachmentName.split("\\*")[0];
       subject = subject.substring(subject.indexOf("_") + 1,subject.lastIndexOf("."));
       //System.out.println(subject);
      }else{
        subject = "[无主题]";
      }
      notify.setSubject(subject);
    }
    int seqId = notify.getSeqId();
    notify.setFromId(Integer.toString(fromId));
    if("".equals(notify.getBeginDate())||notify.getBeginDate() == null){
      notify.setBeginDate(new Date());
    }

    notify.setSendTime(new Date());
    if(notify.getToId() == null ||  "".equals(notify.getToId())){
      notify.setToId("-1");
    }
    notify.setAttachmentId(attachmentId);
    notify.setAttachmentName(attachmentName);
    
    if(seqIdStr != null && !"".equals(seqIdStr)&&!"0".equals(seqIdStr)){
      seqId = Integer.valueOf(seqIdStr.trim());
      String fckContent = YHUtility.null2Empty(notify.getContent());      
      notify.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
      notify.setContent(YHDiaryUtil.cutHtml(fckContent));
      notify.setSeqId(seqId);
      orm.updateSingle(conn, notify);//先保存正文
      bId =  notify.getSeqId();
    }else{
      String fckContent = YHUtility.null2Empty(notify.getContent());      
      notify.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
      notify.setContent(YHDiaryUtil.cutHtml(fckContent));
      orm.saveSingle(conn, notify);
      bId = notifyManageUtil.getBodyId(conn);
    }
    return bId;
  } 
  
  /*列表显示公告内容
   * 查询person表里要设置权限的人，满足以下条件的用户都查出来
   *   不是系统管理员的（他不用设置）
   *   应用到其他用户--“所属角色”里指定的角色相关用户（注："所在部门"条件在while循环里再加，这里可能不好加）
   *   当前正在编辑的用户
   */
  public String getnotifyManagerList(Connection conn,YHPerson loginUser,String type,
      String ascDesc,String field,int showLen,int pageIndex)throws Exception {
    Date currentDate = new Date();
    Statement stmt = null;
    ResultSet rs = null;
    int pageCount = 0;//页码数
    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    int pgEndRecord = 0;//结束索引
    String userPriv = loginUser.getUserPriv();
    String postPriv = loginUser.getPostPriv();
    String userSeqId = Integer.toString(loginUser.getSeqId());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String queryNotifyStr = null;
    List<YHNotify> notifyManagerList = new ArrayList<YHNotify>();
    StringBuffer sb  = new StringBuffer();
    String notifyStatusStr = "";
    sb.append("{");
    sb.append("listData:[");
    try{
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      if("1".equals(userPriv)||"1".equals(postPriv)) {//管理员或者全部部门的人可以看到所有的公告数据
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifyStr = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.[TOP],n.PRIV_ID,n.USER_ID," +
          "n.TYPE_ID,n.PUBLISH,n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,p.USER_NAME,d.DEPT_NAME,n.TOP_DAYS from oa_notify n," +
          "PERSON p,oa_department d where 1=1 and n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
        }else {
          queryNotifyStr = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.TOP,n.PRIV_ID,n.USER_ID," +
          "n.TYPE_ID,n.PUBLISH,n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,p.USER_NAME,d.DEPT_NAME,n.TOP_DAYS from oa_notify n," +
          "PERSON p,oa_department d where 1=1 and n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
        }
      }else {//只能看到本人发布的公告数据        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifyStr = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.[TOP],n.PRIV_ID,n.USER_ID," +
          "n.TYPE_ID,n.PUBLISH,n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,p.USER_NAME,d.DEPT_NAME,n.TOP_DAYS from oa_notify n," +
          "PERSON p,oa_department d where 1=1 and n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.FROM_ID='"+userSeqId+"'";
        }else {
          queryNotifyStr = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.TOP,n.PRIV_ID,n.USER_ID," +
          "n.TYPE_ID,n.PUBLISH,n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,p.USER_NAME,d.DEPT_NAME,n.TOP_DAYS from oa_notify n," +
          "PERSON p,oa_department d where 1=1 and n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.FROM_ID='"+userSeqId+"'";
        }
      }
      
      if("".equals(type)){//选择“无类型“
        queryNotifyStr = queryNotifyStr + " and (n.TYPE_ID='' or n.TYPE_ID=' ' or n.TYPE_ID is null)";
      }else if(!"0".equals(type)) {//选择的不是”所有类型“
        queryNotifyStr = queryNotifyStr + " and (n.TYPE_ID='"+ type + "')";
      }
      if("".equals(field)) {
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifyStr = queryNotifyStr + " order by n.[TOP] desc,n.SEND_TIME desc";
        }else {
          queryNotifyStr = queryNotifyStr + " order by n.TOP desc,n.SEND_TIME desc";
        }
      }else {
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifyStr = queryNotifyStr + " order by n.[TOP] desc, n." + field;
        }else {
          queryNotifyStr = queryNotifyStr + " order by n.TOP desc, n." + field;
        }
        if("1".equals(ascDesc)) {
          queryNotifyStr = queryNotifyStr + " desc";
        }else {
          queryNotifyStr = queryNotifyStr + " asc";
        }
      }
      //YHOut.println(queryNotifyStr);
      rs = stmt.executeQuery(queryNotifyStr); 
      rs.last(); 
      recordCount = rs.getRow(); //总记录数 
      
    //总页数 
      pageCount = recordCount / showLen; 
     if (recordCount % showLen != 0) { 
       pageCount++; 
     } 
     if (pageIndex < 1) { 
       pageIndex = 1; 
     } 
     if (pageIndex > pageCount) { 
       pageIndex = pageCount; 
     } 
     //开始索引
     pgStartRecord = (pageIndex - 1) * showLen + 1;
   
     rs.absolute( (pageIndex - 1) * showLen + 1); 
     int temp = 0;
     for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
       YHNotify notify = new YHNotify(); 
       Statement stmtt = null;
       ResultSet rss = null;
       String toNameTitle = "";
       String toNameStr = "";
       String subjectTitle = "";
       String publishDesc = "";//公告发布状态描述
       String typeName = "";  
       String notifyStatus = "";
       int seqId = rs.getInt("SEQ_ID");
       String fromId = rs.getString("FROM_ID");
       String fromName = rs.getString("USER_NAME");
       String deptName = rs.getString("DEPT_NAME");
       String toId = rs.getString("TO_ID");
       String subject = rs.getString("SUBJECT");
       String format = rs.getString("FORMAT");
       String top = rs.getString("TOP");
       String publish = rs.getString("PUBLISH");
       String privId = rs.getString("PRIV_ID");
       String userId = rs.getString("USER_ID");
       String typeId = rs.getString("TYPE_ID");
       Date sendTime = (Date)rs.getObject("SEND_TIME");
       //System.out.println(sendTime);
       Date beginDate = rs.getDate("BEGIN_DATE");
       Date endDate =rs.getDate("END_DATE");
       int days = rs.getInt("TOP_DAYS");
       if(subject!=null && subject.length()>50) {
         subjectTitle = subject;
         subject = subject.substring(0, 50) + "...";
       }
       if("0".equals(publish)){
         publishDesc = "<font color=red>未发布</font>";
       }
       if("2".equals(publish)){
         publishDesc = "<font color=blue>待审批</font>";
       }
       if("3".equals(publish)){
         publishDesc = "<font color=red>未通过</font><br><a href='#'>审批意见</a>";
       }
       if("1".equals(publish)){
         publishDesc = "";
       }
       
       String endDateStr = "";
       if(!"".equals(endDate)&&endDate!=null&&!"null".equals(endDate)) {
         endDateStr = endDate.toString();
       }
       //根据新闻类型字段的值，获取新闻类型的代码描述
       if(typeId!=null){
          if(!"".equals(typeId.trim())&&!"null".equals(typeId)){
            String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="+typeId;
            stmtt = conn.createStatement(); 
            rss = stmtt.executeQuery(queryTypeNameStr);
             if(rss.next()) {
               typeName = rss.getString("CLASS_DESC");//得到公告类型名称
             }
          }
      }else{
        typeName = "";
      }
       //得到发布范围-部门的名称（串）
       String toName = "";
       String queryToNameStr = "";
       if(toId != null && !"".equals(toId.trim())){
         String[] toIds = toId.split(",");
         toId = "";
         for(int j = 0 ;j < toIds.length ; j++){
           toId += toIds[j] + ",";
         }
         toId = toId.substring(0, toId.length() - 1);
         if("0".equals(toId)||"ALL_DEPT".equals(toId)) {
           toName = "全体部门";
         }else if(!"".equals(toId.trim())&&!"null".equals(toId)){
            queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in (" + toId + ")";
            stmtt = conn.createStatement();
            rss = stmtt.executeQuery(queryToNameStr);
            while(rss.next()) {
              toName = toName + rss.getString("DEPT_NAME") + ",";//部门的名称串
            }
         }else{
           
           toName = "";
         }
       }else{
         
         toName = "";
       }
       
       //得到发布范围-角色的名称（串）
       String privName = "";
       String queryPrivNameStr = "";
       if(privId != null && !"".equals(privId)){
         String[] privIds = privId.split(",");
         privId = "";
         for(int j = 0 ;j < privIds.length ; j++){
           privId +=  privIds[j] + ",";
         }
         privId = privId.substring(0, privId.length() - 1);
       }
       if(privId != null && !"".equals(privId.trim())){
         queryPrivNameStr = "select PRIV_NAME from USER_PRIV where SEQ_ID in (" + privId + ")";
         stmtt = conn.createStatement();
         rss = stmtt.executeQuery(queryPrivNameStr);
         while(rss.next()) {
           privName = privName + rss.getString("PRIV_NAME") + ",";//角色的名称串
         }
       }
  //得到发布范围-人员的姓名（串）
       String userName = "";
       String queryUserNameStr = "";
       if(userId != null && !"".equals(userId)){
         String[] userIds = userId.split(",");
         userId = "";
         for(int j = 0 ;j < userIds.length ; j++){
           userId += userIds[j] + ",";
         }
         userId = userId.substring(0, userId.length() - 1);
       }
       if(userId != null && !"".equals(userId.trim())){
         queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in (" + userId + ")";
         stmtt = conn.createStatement();
         rss = stmtt.executeQuery(queryUserNameStr);
         while(rss.next()) {
           userName = userName + rss.getString("USER_NAME") + ",";//人员的名称串
         }
       }
       
       //设置好部门的名称（串）的显示格式
       if(!"".equals(toName)) {
         toNameTitle = "部门:" + toName;  
         if(toName.length()>20) {
           toName = toName.substring(0, 15)+"...";
         }
         toNameStr = "<font color=#0000FF><b>部门：</b></font>";
         toNameStr += toName;
         toNameStr += "<br>";
       }
      
       //设置好角色的名称（串）的显示格式
       String privNameTitle = "";
       String privNameStr = "";
       if(!"".equals(privName)) {
         privNameTitle =   "角色：" + privName;
         if(privName.length()>20) {
           privName = privName.substring(0, 15)+"...";
         }
         privNameStr =  "<font color=#0000FF><b>角色：</b></font>";
         privNameStr += privName;
         privNameStr += "<br>";
       }
       
    //设置好人员的姓名（串）的显示格式
       String userNameTitle = "";
       String userNameStr = "";
       if(!"".equals(userName)) {
         userNameTitle =  "人员：" + userName;
         if(userName.length()>20) {
           userName = userName.substring(0, 15)+"...";
         }
         userNameStr =  "<font color=#0000FF><b>人员：</b></font>";
         userNameStr += userName;
         userNameStr += "<br>";
       }
       
       if(currentDate.compareTo(beginDate)<=0) {//起始时间晚于当前时间--待生效
         notifyStatus = "1";
         notifyStatusStr = "待生效";
       }else {
         notifyStatus = "2";
         if(!"2".equals(publish)&&!"3".equals(publish)) {//进入发布的判断
           notifyStatusStr = "<font color='#00AA00'><b>生效</font>";
         }else {//进入审批的判断
           if("2".equals(publish)) {
             notifyStatusStr = "<font color='blue'><b>待审批</font>";
           }
           if("3".equals(publish)) {
             notifyStatusStr = "<font color='red'><b>未通过</font><br><a href='javascript:my_affair("+seqId+")'; title='点击查看审批意见'>审批意见</a>"; 
           }
         }
       }
       if((!"".equals(endDate)&&endDate!=null)) {
        if(currentDate.compareTo(endDate)>0) {//如果结束时间小于当前时间，--终止
            notifyStatus = "3";
           notifyStatusStr = "<font color='#FF0000'><b>终止</font>";
         }
       }
       if("0".equals(publish)) {
         notifyStatusStr = "";
       }
       int dayCnt = 0;
       dayCnt = days;
       //System.out.println(YHUtility.getDayAfter(beginDate, dayCnt).getTime() <= YHUtility.getDayAfter(new Date(), 0).getTime());
       if("1".equals(top) && dayCnt!=0 && (YHUtility.getDayAfter(beginDate, dayCnt).getTime() <= YHUtility.getDayAfter(new Date(), 0).getTime())){
       	cancelTop(conn, seqId) ;
       }
       if("1".equals(top) && dayCnt!=0 && (YHUtility.getDayAfter(beginDate, dayCnt).getTime() > YHUtility.getDayAfter(new Date(), 0).getTime())){
         subject = "<font color=red><b>置顶:" + subject + "</b></font>";
       }else if("1".equals(top) && dayCnt==0){
    	   subject = "<font color=red><b>置顶:" + subject + "</b></font>";  
       }
       
       notify.setSeqId(seqId);
       sb.append("{");
       sb.append("seqId:" + seqId);
       sb.append(",fromId:\"" + fromId + "\"");
       sb.append(",fromName:\"" + YHUtility.encodeSpecial(fromName) + "\"");
       sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
       sb.append(",toNameTitle:\"" + YHUtility.encodeSpecial(toNameTitle) + "\"");
       sb.append(",toNameStr:\"" + YHUtility.encodeSpecial(toNameStr) + "\"");
       sb.append(",privNameTitle:\"" + YHUtility.encodeSpecial(privNameTitle) + "\"");
       sb.append(",privNameStr:\"" + YHUtility.encodeSpecial(privNameStr) + "\"");
       sb.append(",userNameTitle:\"" + YHUtility.encodeSpecial(userNameTitle) + "\"");
       sb.append(",userNameStr:\"" + YHUtility.encodeSpecial(userNameStr) + "\"");
       sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle) + "\"");
       sb.append(",notifyStatus:\"" + YHUtility.encodeSpecial(notifyStatus) + "\"");
       sb.append(",notifyStatusStr:\"" + YHUtility.encodeSpecial(notifyStatusStr) + "\"");
       sb.append(",toName:\"" + YHUtility.encodeSpecial(toName) + "\"");
       sb.append(",publishDesc:\"" + YHUtility.encodeSpecial(publishDesc) + "\"");
       sb.append(",deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"");
       sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
       sb.append(",publish:\"" + YHUtility.encodeSpecial(publish) + "\"");
       sb.append(",format:\"" + format + "\"");
       sb.append(",top:\"" + top + "\"");
       sb.append(",sendTime:\"" + YHUtility.getDateTimeStr(sendTime)  + "\"");
       sb.append(",beginDate:\"" + beginDate + "\"");
       sb.append(",endDate:\"" + endDateStr + "\"");
       sb.append("},");
       //YHOut.println(sb.toString()+"========================================");
       notifyManagerList.add(notify);
       rs.next(); 
    } 
     //结束索引
     pgEndRecord =(pageIndex - 1) * showLen + notifyManagerList.size();
     if(notifyManagerList.size()>0) {
     sb.deleteCharAt(sb.length() - 1); 
     }
     sb.append("]");
     sb.append(",pageData:");
     sb.append("{");
     sb.append("pageCount:" + pageCount);
     sb.append(",recordCount:" + recordCount);
     sb.append(",pgStartRecord:" + pgStartRecord);
     sb.append(",pgEndRecord:" + pgEndRecord);
     sb.append("}");
     sb.append("}");
//     returnMap.put("listData", notifyManagerList);
//     returnMap.put("pageData", sb.toString());
     return sb.toString();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 取消置顶
   * @param dbConn
   * @param seqId
   * @throws Exception
   */
  public void cancelTop(Connection dbConn, int seqId) throws Exception{
	    PreparedStatement ps = null;    
	    String sql = " update NOTIFY set TOP ='0' where SEQ_ID =" + seqId;
	      try{
	        ps = dbConn.prepareStatement(sql);
	        int k = ps.executeUpdate(); 
	      } catch (SQLException e){
	        throw e;
	      }finally{
	      YHDBUtility.close(ps, null, log);
	      }
  }
  
  //公告查询
  public String queryNotify(Connection conn, YHNotify notify,YHPerson person,
                           String beginDatetemp,String endDatetemp,String stat) throws Exception{
    YHORM orm = new YHORM();
    Date currentDate = new Date();
    Statement stmt = null;
    ResultSet rs = null;
    String querynewsParam = "";
    String querynewsSql = "";
    int recordCount = 0; 
    int pageCount = 0;
    int pgStartRecord = 0;
    int pgEndRecord = 0;//结束索引
    StringBuffer sb = new StringBuffer();
    List newsManagerList = new ArrayList();
    sb.append("{");
    sb.append("listData:[");
    //   Map<String, String> attr = fileUploadLogic(fileForm, pathPx);
    String fromIdtemp = notify.getFromId();
    if(fromIdtemp!=null && !"".equals(fromIdtemp)&& !"null".equalsIgnoreCase(fromIdtemp))
    querynewsParam = querynewsParam + " and n.FROM_ID in (" + fromIdtemp + ")";
    String formattemp = notify.getFormat();
    if(!"".equals(formattemp)&& formattemp!=null)
    querynewsParam = querynewsParam + " and n.FORMAT ='" + formattemp + "'";
    String typeIdtemp = notify.getTypeId();
    if(!"".equals(typeIdtemp) && typeIdtemp!=null)
    querynewsParam = querynewsParam + " and n.TYPE_ID ='" + typeIdtemp + "'";
    String publishtemp = notify.getPublish();
    if(!"".equals(publishtemp) && publishtemp!=null)
    querynewsParam = querynewsParam + " and n.PUBLISH ='" + publishtemp + "'";
    String toptemp = notify.getTop();
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if(!"".equals(toptemp) && toptemp!=null) {
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        querynewsParam = querynewsParam + " and n.[TOP] ='" + toptemp + "'";
      }else {
        querynewsParam = querynewsParam + " and n.TOP ='" + toptemp + "'";
      }
    }
    String subjecttemp = notify.getSubject();
    if(!"".equals(subjecttemp) && subjecttemp != null)
    querynewsParam = querynewsParam + " and n.SUBJECT like '%" + YHDBUtility.escapeLike(subjecttemp) + "%' "+YHDBUtility.escapeLike();
//    Date beginDatetemp = notify.getBeginDate();
    if(!"".equals(beginDatetemp) && beginDatetemp!= null)
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.SEND_TIME", beginDatetemp, ">=");
//    Date endDatetemp = notify.getEndDate();
    if(!"".equals(endDatetemp) && endDatetemp!=null)
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.SEND_TIME", YHUtility.getDateTimeStr(YHUtility.getDayAfter(endDatetemp,1)), "<"); 
    String contenttemp = notify.getContent(); 
    if(!"".equals(contenttemp))
    querynewsParam = querynewsParam + " and n.CONTENT like '%" + YHDBUtility.escapeLike(contenttemp) + "%' "+ YHDBUtility.escapeLike(); 
    if(!"".equalsIgnoreCase(stat))
    {
       if("1".equalsIgnoreCase(stat))
         querynewsParam +=" and BEGIN_DATE>"+ YHDBUtility.currDateTime() +" AND PUBLISH ='1' ";
       else if("2".equalsIgnoreCase(stat))
         querynewsParam +=" and BEGIN_DATE<="+ YHDBUtility.currDateTime() +" and (END_DATE>"+YHDBUtility.currDateTime() +" or END_DATE IS NULL) AND PUBLISH ='1'";
       else if("3".equalsIgnoreCase(stat))
         querynewsParam +=" and (END_DATE < BEGIN_DATE or END_DATE <= "+ YHDBUtility.currDateTime() +")";
    }
   
    if(!"1".equals(person.getUserPriv())&&!"1".equals(person.getPostPriv())) {
      querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_notify n,PERSON p,oa_department d where n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.FROM_ID='"+person.getSeqId()+"'";
    }else {
      querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_notify n,PERSON p,oa_department d where n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
    }
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      querynewsSql = querynewsSql + querynewsParam + " order by  n.[TOP] desc,n.SEND_TIME desc";    
    }else {
      querynewsSql = querynewsSql + querynewsParam + " order by  n.TOP desc,n.SEND_TIME desc";    
    }
    
    
    
    try {
      stmt = conn.createStatement(); 
      rs = stmt.executeQuery(querynewsSql);
     while(rs.next()){
      YHNotify notifytemp = new YHNotify(); 
        Statement stmtt = null;
        ResultSet rss = null;
        String toNameTitle = "";
        String toNameStr = "";
        String publishDesc = "";
        String typeName = "";  
        String notifyStatus = "";
        String subjectTitle = "";
        String notifyStatusStr = "";
        String fromName = rs.getString("USER_NAME");
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        String fromId = rs.getString("FROM_ID");
        String toId = rs.getString("TO_ID");

        String subject = YHUtility.null2Empty(rs.getString("SUBJECT"));
        String readers = rs.getString("READERS");

        String format = rs.getString("FORMAT");
        String top = rs.getString("TOP");
        String privId = rs.getString("PRIV_ID");
        String userId = rs.getString("USER_ID");
        String typeId = rs.getString("TYPE_ID");
        String publish = rs.getString("PUBLISH");
        Date sendTime = rs.getTimestamp("SEND_TIME");
        Date beginDate = rs.getDate("BEGIN_DATE");
        Date endDate = rs.getDate("END_DATE");
       
        if(!"".equals(subject) && subject.length()>50) {
          subjectTitle = subject;
          subject = subject.substring(0, 50) + "...";
        }

        if("0".equals(publish)){
          publishDesc = "<font color=red>未发布</font>";
        }
        if("2".equals(publish)){
          publishDesc = "<font color=blue>待审批</font>";
        }
        if("3".equals(publish)){
          publishDesc = "<font color=red>未通过</font><br><a href='#'>审批意见</a>";
        }
        if("1".equals(publish)){
          publishDesc = "";
        }
   
        String endDateStr = "";
        if(!"".equals(endDate)&&endDate!=null) {
          endDateStr = endDate.toString();
        }
      //根据新闻类型字段的值，获取新闻类型的代码描述  
        if(typeId!=null){
           if(!"".equals(typeId.trim())&&!"null".equals(typeId)){
             String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="+typeId;
             stmtt = conn.createStatement(); 
             rss = stmtt.executeQuery(queryTypeNameStr);
              if(rss.next()) {
                typeName = rss.getString("CLASS_DESC");
              }
           }
       }else{
         typeName = "";
       }   
       
        //得到发布范围-部门的名称（串）
        String toName = "";
        String queryToNameStr = "";
        if(toId != null && !"".equals(toId)){
          String[] toIds = toId.split(",");
          toId = "";
          for(int j = 0 ;j < toIds.length ; j++){
            toId += toIds[j] + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
        }

        if("".equals(toId) || toId==null){
          toId="";
        }
        if("0".equals(toId)||"ALL_DEPT".equals(toId)) {
          toName = "全体部门";
        }else if(!"".equals(toId.trim())&&toId!=null){
           queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in (" + toId + ")";
           stmtt = conn.createStatement();
           rss = stmtt.executeQuery(queryToNameStr);
           while(rss.next()) {
             toName = toName + rss.getString("DEPT_NAME") + ",";
           }
        }
        
        //得到发布范围-角色的名称（串）
        String privName = "";
        String queryPrivNameStr = "";
        if(privId != null && !"".equals(privId)){
          String[] privIds = privId.split(",");
          privId = "";
          for(int j = 0 ;j < privIds.length ; j++){
            privId += privIds[j] + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if(privId != null && !"".equals(privId.trim())){
          queryPrivNameStr = "select PRIV_NAME from USER_PRIV where SEQ_ID in (" + privId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryPrivNameStr);
          while(rss.next()) {
            privName = privName + rss.getString("PRIV_NAME") + ",";
          }
        }
   //得到发布范围-人员的姓名（串）
        String userName = "";
        String queryUserNameStr = "";
        if(userId != null && !"".equals(userId)){
          String[] userIds = userId.split(",");
          userId = "";
          for(int j = 0 ;j < userIds.length ; j++){
            userId +=  userIds[j] + ",";
          }
          userId = userId.substring(0, userId.length() - 1);
        }
        if(userId != null && !"".equals(userId.trim())){
          queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in (" + userId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryUserNameStr);
          while(rss.next()) {
            userName = userName + rss.getString("USER_NAME") + ",";
          }
        }
        
        //设置好部门的名称（串）的显示格式
        if(!"".equals(toName)) {
          toNameTitle = "部门:" + toName;  
          if(toName.length()>20) {
            toName = toName.substring(0, 15)+"...";
          }
          toNameStr = "<font color=#0000FF><b>部门：</b></font>";
          toNameStr += toName;
          toNameStr += "<br>";
        }
       
        //设置好角色的名称（串）的显示格式
        String privNameTitle = "";
        String privNameStr = "";
        if(!"".equals(privName)) {
          privNameTitle =   "角色：" + privName;
          if(privName.length()>20) {
            privName = privName.substring(0, 15)+"...";
          }
          privNameStr =  "<font color=#0000FF><b>角色：</b></font>";
          privNameStr += privName;
          privNameStr += "<br>";
        }
        
     //设置好人员的姓名（串）的显示格式
        String userNameTitle = "";
        String userNameStr = "";
        if(!"".equals(userName)) {
          userNameTitle =  "人员：" + userName;
          if(userName.length()>20) {
            userName = userName.substring(0, 15)+"...";
          }
          userNameStr =  "<font color=#0000FF><b>人员：</b></font>";
          userNameStr += userName;
          userNameStr += "<br>";
        }
        
        if(currentDate.compareTo(beginDate)<0) {
          notifyStatus = "1";
          notifyStatusStr = "待生效";
        }else {
          notifyStatus = "2";
          if(!"2".equals(publish)&&!"3".equals(publish)) {
            notifyStatusStr = "<font color='#00AA00'><b>生效</font>";
          }else {
            if("2".equals(publish)) {
              notifyStatusStr = "<font color='blue'><b>待审批</font>";
            }
            if("3".equals(publish)) {
              notifyStatusStr = "<font color='red'><b>未通过</font><br><a href='javascript:my_affair("+seqId+")'; title='点击查看审批意见'>审批意见</a>"; 
            }
          }
        }
        if((!"".equals(endDate)&&endDate!=null)) {
          if(currentDate.compareTo(endDate)>0) {
              notifyStatus = "3";
             notifyStatusStr = "<font color='#FF0000'><b>终止</font>";
           }
         }
         if("0".equals(publish)) {
           notifyStatusStr = "";
         }
        if("1".equals(top)){
          subject = "<font color=red><b>置顶:" + subject + "</b></font>";
        }
        
        
        sb.append("{");
        sb.append("seqId:" + seqId);
        sb.append(",fromId:\"" + fromId + "\"");
        sb.append(",fromName:\"" + YHUtility.encodeSpecial(fromName) + "\"");
        sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
        sb.append(",toNameTitle:\"" + YHUtility.encodeSpecial(toNameTitle) + "\"");
        sb.append(",toNameStr:\"" + YHUtility.encodeSpecial(toNameStr) + "\"");
        sb.append(",privNameStr:\"" + YHUtility.encodeSpecial(privNameStr) + "\"");
        sb.append(",userNameStr:\"" + YHUtility.encodeSpecial(userNameStr) + "\"");
        sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle) + "\"");
        sb.append(",notifyStatus:\"" + notifyStatus + "\"");
        sb.append(",notifyStatusStr:\"" + notifyStatusStr + "\"");
        sb.append(",toName:\"" + YHUtility.encodeSpecial(toName) + "\"");
        sb.append(",publishDesc:\"" + YHUtility.encodeSpecial(publishDesc) + "\"");
        sb.append(",deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"");
        sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
        sb.append(",publish:\"" + publish + "\"");
        sb.append(",format:\"" + format + "\"");
        sb.append(",top:\"" + top + "\"");
        sb.append(",sendTime:\"" + sendTime + "\"");
        sb.append(",beginDate:\"" + beginDate + "\"");
        sb.append(",endDate:\"" + endDateStr + "\"");
        sb.append("},");
        newsManagerList.add(notifytemp);
      }
      if(newsManagerList.size()>0) {
        sb.deleteCharAt(sb.length() - 1); 
      }
      sb.append("]");
      sb.append(",pageData:");
      sb.append("{");
      sb.append("pageCount:" + pageCount);
      sb.append(",recordCount:" + recordCount);
      sb.append(",pgStartRecord:" + pgStartRecord);
      sb.append(",pgEndRecord:" + pgEndRecord);
      sb.append("}");
      sb.append("}");
      //System.out.print(sb.toString());
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sb.toString();
  } 

  /**
   * 删除全部
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   * @throws Exception 
   * @TODO 删除附件
   */
  public boolean deleteAllNotify(Connection dbConn,String loginUserId,String loginUserPriv,String postPriv,String ip) throws IOException, Exception {
    String queryAttachlSql = "";
    String deletenotifySql = "";
    String subject = "";
    if("1".equals(loginUserPriv)||"1".equals(postPriv)) {
      queryAttachlSql = "select ATTACHMENT_ID,ATTACHMENT_NAME,SUBJECT from oa_notify where "+ YHDBUtility.getDateFilter("ATTACHMENT_NAME", "", "!=");
    }else {
      queryAttachlSql = "select ATTACHMENT_ID,ATTACHMENT_NAME,SUBJECT from oa_notify where "+ YHDBUtility.getDateFilter("ATTACHMENT_NAME", "", "!=") +" and FROM_ID='"+loginUserId+"'";
    }
    
    boolean success = false;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryAttachlSql);
      while(rs.next()) {
        subject = rs.getString("SUBJECT");
        String remark = "删除公告通知，标题："+ subject;
        YHSysLogLogic.addSysLog(dbConn, YHLogConst.NOTICE_MANA, remark, Integer.parseInt(loginUserId), ip);
      }
      if("1".equals(loginUserPriv)||"1".equals(postPriv)) {
        deletenotifySql = "delete from oa_notify";
      }else {
        deletenotifySql = "delete from oa_notify where FROM_ID='"+ loginUserId + "'";
      }
      stmt = dbConn.createStatement();
      success = stmt.execute(deletenotifySql);
      if("1".equals(loginUserPriv)) {
      }else {
      }
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return success;
  }
  /**
   * 删除所选的公告
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   * @throws Exception 
   * @TODO 删除附件
   */
  public boolean deleteCheckNotify(Connection dbConn,String loginUserId,String loginUserPriv,String postPriv,String deleteStr,String ip) throws IOException, Exception {
    String queryNotifySql = "";
    String deletenotifySql = "";
    String deleteSmsSql = "";
    String attachmentId = "";
    String attachmentName = "";
    String subject = "";
    if(deleteStr != null && !"".equals(deleteStr)){
      String[] deleteStrs = deleteStr.split(",");
      deleteStr = "";
      for(int i = 0 ;i < deleteStrs.length ; i++){
        deleteStr +=  "'" + deleteStrs[i] + "'" + ",";
      }
      deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
    }
    boolean success = false;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      queryNotifySql = "select BEGIN_DATE,SUBJECT,ATTACHMENT_ID,ATTACHMENT_NAME,SUBJECT from oa_notify where SEQ_ID in ("+ deleteStr +")";
      if(!"1".equals(loginUserPriv)&&!"1".equals(postPriv)) {
        queryNotifySql = queryNotifySql + " and FROM_ID='" + loginUserId + "'";
      }
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryNotifySql);
      while(rs.next()) {
        attachmentId = rs.getString("ATTACHMENT_ID");
        attachmentName = rs.getString("ATTACHMENT_NAME");
        subject = rs.getString("SUBJECT");
        //删除消息
        //删除附件
        String remark = "删除公告通知，标题："+ subject;
        YHSysLogLogic.addSysLog(dbConn, YHLogConst.NOTICE_MANA, remark, Integer.parseInt(loginUserId), ip);
      }

      deletenotifySql = "delete  from NOTIFY where SEQ_ID in  ("+ deleteStr + ")";
      if(!"1".equals(loginUserPriv)&&!"1".equals(postPriv)) { 
        deletenotifySql = deletenotifySql +  " and FROM_ID='" + loginUserId + "'";
      }
     //YHOut.println(deletenotifySql);
      
      stmt = dbConn.createStatement();
      success = stmt.execute(deletenotifySql);
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
   
    return success;
  }
  /**
 
  
  /**
   * 处理上传附件，返回附件id，附件名称

   * 处理单文件上传的
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
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath() + File.separator + YHNotifyCont.MODULE + File.separator + hard + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  //点击发布，保存，提交审批，调用的
  public Map<String, String> fileUploadLogic2(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
      YHSelAttachUtil selA = new YHSelAttachUtil(fileForm, YHNotifyCont.MODULE);
      result.putAll(selA.getAttachInFo());
    return result;
  }
  /**
   * 附件批量上传页面处理
   * @return
  * @throws Exception 
   */
  public StringBuffer uploadMsrg2Json( YHFileUploadForm fileForm) throws Exception{
    StringBuffer sb = new StringBuffer();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try{
      attr = fileUploadLogic(fileForm);
      Set<String> attrKeys = attr.keySet();
      for (String key : attrKeys){
        String fileName = attr.get(key);
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      long size = getSize(fileForm);
      sb.append("{");
      sb.append("'attachmentId':").append("\"").append(attachmentId).append("\",");
      sb.append("'attachmentName':").append("\"").append(attachmentName).append("\",");
      sb.append("'size':").append("").append(size);
      sb.append("}");
   } catch (Exception e){
     e.printStackTrace();
     throw e;
   }
    return sb;
  }
  
  
  /**
   * 附件批量上传页面处理
   * @return
  * @throws Exception 
   */
  public Map uploadMsrg2Map( YHFileUploadForm fileForm,String pathP) throws Exception{
    Map<String, String> map = new HashMap();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try{
      attr = fileUploadLogic(fileForm);
      Set<String> attrKeys = attr.keySet();
      for (String key : attrKeys){
        String fileName = attr.get(key);
        String file = fileName.split("_")[1];
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      long size = getSize(fileForm);
      map.put("attachmentId", attachmentId);
      map.put("attachmentName", attachmentName);
   } catch (Exception e){
     e.printStackTrace();
     throw e;
   }
    return map;
  }
  
  public long getSize( YHFileUploadForm fileForm) throws Exception{
    long result = 0l;
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      result += fileForm.getFileSize(fieldName);
    }
    return result;
  }
  
  //终止和生效
  public boolean changeState(Connection dbConn, YHPerson loginUser,String seqId,String operation) throws Exception {
    String changeStateSql = "";
    boolean success = false;
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    
    if("1".equals(operation)) {//如果是立即生效
      changeStateSql = "update oa_notify set BEGIN_DATE=? where SEQ_ID='" + seqId + "'";
    }else if("2".equals(operation)){//如果是终止
      changeStateSql = "update oa_notify set END_DATE= ? where SEQ_ID='"+ seqId + "'";
    }else{
      changeStateSql = "update oa_notify set END_DATE=null  where SEQ_ID='"+ seqId + "'";//如果是生效
    }
    if(!"1".equals(loginUser.getUserPriv())){
      changeStateSql = changeStateSql + " and FROM_ID='"+loginUser.getSeqId()+"'";
    }
    //YHOut.println(changeStateSql);
    ResultSet rs = null;
    PreparedStatement  st = null;
    try {
      st = dbConn.prepareStatement(changeStateSql);    
      if("1".equals(operation)){
      st.setDate(1, new java.sql.Date(new Date().getTime())) ; 
      }else if("2".equals(operation)){
        st.setDate(1, new java.sql.Date(YHUtility.getDayBefore(new Date(), 0).getTime())) ;  
      }
     
      int k = st.executeUpdate();
      if(k!=0){
        return true;
      }
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
    }finally {
      YHDBUtility.close(st, rs, log);
    }
    return false;
  }
  
  //查询页面的批量终止
  public boolean changeStateGroup(Connection dbConn, YHPerson loginUser,String deleteStr,String operation) throws Exception {
    String changeStateSql = "";
    boolean success = false;
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String currentDateStr = sdf.format(currentDate);
    if(deleteStr != null && !"".equals(deleteStr)){
      String[] deleteStrs = deleteStr.split(",");
      deleteStr = "";
      for(int i = 0 ;i < deleteStrs.length ; i++){
        deleteStr +=  "'" + deleteStrs[i] + "'" + ",";
      }
      deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
    }
   if(!"".equals(deleteStr)&&deleteStr!=null){
      changeStateSql = "update oa_notify set END_DATE="+ YHDBUtility.currDateTime() +" where SEQ_ID in ("+ deleteStr + ")";
   }
      //YHOut.println(changeStateSql);
    ResultSet rs = null;
    Statement st = null;
    try {
      st = dbConn.createStatement();
      success = st.execute(changeStateSql);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
    }finally {
      YHDBUtility.close(st, rs, log);
    }
    return success;
  }
  
  /**
   * 取消置顶
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   * @throws Exception 
   * @TODO 
   */
  public void cancelTop(Connection dbConn,String loginUserId,String loginUserPriv,String postPriv,String deleteStr) throws IOException, Exception {
    String queryNotifySql = "";
    String deletenotifySql = "";
    String deleteSmsSql = "";
    Statement stmt = null;
    ResultSet rs = null;
    String attachmentId = "";
    String attachmentName = "";
    if(deleteStr != null && !"".equals(deleteStr)){
      String[] deleteStrs = deleteStr.split(",");
      deleteStr = "";
      for(int i = 0 ;i < deleteStrs.length ; i++){
        deleteStr +=  "'" + deleteStrs[i] + "'" + ",";
      }
      deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
    }

    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String top = "TOP";
    if ("sqlserver".equals(dbms)) {
      top = "[TOP]";
    }
    
    String updateSql = "update oa_notify set " + top + "='0' where SEQ_ID in ("+deleteStr+")";
    if(!"1".equals(loginUserPriv)&&!"1".equals(postPriv)){
      updateSql = updateSql + " and FROM_ID='"+loginUserId+"'";
    }
    //YHOut.println(updateSql);
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(updateSql);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  //审批意见
  public String getNoteById(Connection dbConn,YHPerson person,int seqId) throws Exception{
    ResultSet rs = null;
    Statement st = null;
    YHORM orm = new YHORM();
    YHNotify notify = (YHNotify)orm.loadObjSingle(dbConn, YHNotify.class,seqId);
    String auditier = notify.getAuditer();
    String reason = notify.getReason();
    YHPerson auditierPerson = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, Integer.parseInt(auditier));
    StringBuffer sb = new StringBuffer();
    sb.append("{").append("auditierName:\""+YHUtility.encodeSpecial(auditierPerson.getUserName())+"\"")
       .append(",reason:\""+YHUtility.encodeSpecial(reason)+"\"").append("}");
    return sb.toString();
  }
  
  //查看发布范围详情
  public String showObject(Connection dbConn,YHPerson person,int seqId) throws Exception{
    YHORM orm = new YHORM();
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    YHNotify notify = (YHNotify)orm.loadObjSingle(dbConn, YHNotify.class,seqId);
    String toId = notify.getToId();
    String userId = notify.getUserId();
    String privId = notify.getPrivId();
    ResultSet rs = null;
    Statement st = null;
    try {
      //得到发布范围-部门的名称（串）
      String toName = "";
      String queryToNameStr = "";
      if(toId != null && !"".equals(toId)){
        String[] toIds = toId.split(",");
        toId = "";
        for(int j = 0 ;j < toIds.length ; j++){
          toId += toIds[j] + ",";
        }
        toId = toId.substring(0, toId.length() - 1);
      }
      if("0".equals(toId)) {
        toName = "全体部门";
      }else if(!"".equals(toId.trim())&&toId!=null){
         queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in (" + toId + ")";
         st = dbConn.createStatement();
         rs = st.executeQuery(queryToNameStr);
         while(rs.next()) {
           toName = toName + rs.getString("DEPT_NAME") + ",";
         }
      }
      sb.append("toName:'"+toName+"'");
      //得到发布范围-角色的名称（串）
      String privName = "";
      String queryPrivNameStr = "";
      if(privId != null && !"".equals(privId)){
        String[] privIds = privId.split(",");
        privId = "";
        for(int j = 0 ;j < privIds.length ; j++){
          privId +=  privIds[j] + ",";
        }
        privId = privId.substring(0, privId.length() - 1);
      }
      if(privId != null && !"".equals(privId.trim())){
        queryPrivNameStr = "select PRIV_NAME from USER_PRIV where SEQ_ID in (" + privId + ")";
        st = dbConn.createStatement();
        rs = st.executeQuery(queryPrivNameStr);
        while(rs.next()) {
          privName = privName + rs.getString("PRIV_NAME") + ",";
        }
      }
      sb.append(",privName:'"+privName+"'");
      //得到发布范围-人员的姓名（串）
      String userName = "";
      String queryUserNameStr = "";
      if(userId != null && !"".equals(userId)){
        String[] userIds = userId.split(",");
        userId = "";
        for(int j = 0 ;j < userIds.length ; j++){
          userId += userIds[j] + ",";
        }
        userId = userId.substring(0, userId.length() - 1);
      }
      if(userId != null && !"".equals(userId.trim())){
        queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in (" + userId + ")";
        st = dbConn.createStatement();
        rs = st.executeQuery(queryUserNameStr);
        while(rs.next()) {
          userName = userName + rs.getString("USER_NAME") + ",";
        }
      }
      sb.append(",userName:'"+userName+"'");
      sb.append("}");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
    }
    
    return sb.toString();
  }
  
  /**
   * 得到email表的SEQ_ID
   * @param conn
   * @return
   * @throws SQLException 
   */
  public int getBodyId(Connection conn) throws Exception{
    String sql = "select Max(SEQ_ID) FROM OA_NOTIFY";
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
   * 浮动菜单文件删除
   * 
   * @param dbConn
   * @param attId
   * @param attName
   * @param contentId
   * @throws Exception
   */
  public boolean delFloatFile(Connection dbConn, String attId, String attName, int seqId) throws Exception {
    boolean updateFlag = false;
    if (seqId != 0) {
      YHORM orm = new YHORM();
      YHNotify notify = (YHNotify)orm.loadObjSingle(dbConn, YHNotify.class, seqId);
      String[] attIdArray = {};
      String[] attNameArray = {};
      String attachmentId = notify.getAttachmentId();
      String attachmentName = notify.getAttachmentName();
      if (!"".equals(attachmentId.trim()) && attachmentId != null && attachmentName != null) {
        attIdArray = attachmentId.trim().split(",");
        attNameArray = attachmentName.trim().split("\\*");
        //YHOut.println("id长度："+attIdArray.length+"&&&&&&&文件名称长度："+attNameArray.length);
      }
      String attaId = "";
      String attaName = "";     
      for (int i = 0; i < attIdArray.length; i++) {
        if (attId.equals(attIdArray[i])) {
          continue;
        }        
          attaId += attIdArray[i] + ",";
          attaName += attNameArray[i] + "*";
          //YHOut.println("attaIdoooo==="+attaId+"--------attaName=="+attaName);
        
      }
      //YHOut.println("attaId=="+attaId+"--------attaName=="+attaName);
      notify.setAttachmentId(attaId.trim());
      notify.setAttachmentName(attaName.trim());
      orm.updateSingle(dbConn, notify);
    }
  //处理文件
    String[] tmp = attId.split("_");
    String path = filePath + File.separator  + tmp[0] + File.separator + tmp[1] + "_" + attName;
    File file = new File(path);
    if(file.exists()){
      file.delete();
    } else {
      //兼容老的数据
      String path2 = filePath + File.separator  + tmp[0] + File.separator + tmp[1] + "." + attName;
      File file2 = new File(path2);
      if(file2.exists()){
        file2.delete();
      }
    }
    updateFlag=true;
    return updateFlag;
  }
  
  /**
   * <code>查找该用户的管理范围：0-本部门,1-全体,2-指定部门</code>
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int findPostPriv(Connection dbConn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = "SELECT POST_PRIV from person where seq_id =" + seqId;
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      return rs.getInt(1);
    }
    return 999999;
  }
  
  /**
   * <code>查找seqId用户所在的部门</code>
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int findMyDept(Connection dbConn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = "SELECT DEPT_ID from person where seq_id =" + seqId;
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      return rs.getInt(1);
    }
    return 0;
  }
  
  /**
   * <code>查找管理范围指定的部门</code>
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String findPostDept(Connection dbConn, int seqId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String deptIds = "";
    String sql = "SELECT POST_DEPT from person where seq_id =" + seqId;
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      deptIds = rs.getString(1); 
    }
    return deptIds;
  }  
  
  /**
   * <code>是否是所有的部门</code>
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public  boolean isAllDept(Connection dbConn, int seqId) throws Exception{
    if(findPostPriv(dbConn, seqId)!=1){
      return false;
    }
    return true;
  }
  
  /**
   * <code>是否有指定的管理部门</code>
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public boolean isHavePostDept(Connection dbConn, int seqId)throws Exception{
    if(findPostDept(dbConn, seqId)!="" && findPostDept(dbConn, seqId)!=null && findPostDept(dbConn, seqId)!="null"){
      return true;
    }
    return false;
  }
  
  /**
   * <code>是不是本部门</code>
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public boolean isMyDept(Connection dbConn, int seqId)throws Exception{
    if(findPostPriv(dbConn, seqId)==0){  //管理范围是本部门
      return true;
    }
    return false;
  }
  
  public static void main(String[] agrs){
    String  deptIds = "123,2345,4564,";
    deptIds = deptIds.substring(0, deptIds.lastIndexOf(","));
    //System.out.println(deptIds);
  }
  
  /**
   * 本部门的角色和所选的角色进行交集
   * @param dbConn
   * @param deptIds    部门里角色ids
   * @param roleIds    选择的角色ids
   * @return
   * @throws Exception 
   */
  public String findRoles(Connection dbConn,String deptIds, String roleIds) throws Exception{
    String[] deptRoles = findMyDeptRoleIds(dbConn, deptIds).split(",");
    String[] roles = roleIds.split(",");
    int deptRoleLen = deptRoles.length;
    int rolesLen = roles.length;
    String newRolesIds = "";
    for(int i=0; i< rolesLen; i++){
       for(int k=0; k<deptRoleLen; k++){
        if(roles[i].equalsIgnoreCase(deptRoles[k])){
          newRolesIds += roles[i];
          continue;
        }
       }
    }
    return newRolesIds;
  }
  
  /**
   * 选择出deptIds的角色ids
   * @param dbConn
   * @param deptIds
   * @return
   * @throws Exception
   */
  public String findMyDeptRoleIds(Connection dbConn,String deptIds) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = "select distinct USER_PRIV from person  where DEPT_ID in("+ deptIds +") order by USER_PRIV";
    String ids = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        ids += rs.getString(1)+",";
      }
    } catch (SQLException e){
      throw e;
    }
    return ids.substring(0, ids.lastIndexOf(",")==-1 ?0:ids.lastIndexOf(","));
  }
  
  /**
   * 导出到excel
   * @param conn
   * @param notify
   * @param person
   * @param beginDatetemp
   * @param endDatetemp
   * @return
   * @throws Exception
   */
  public List<Map<String, String>> toExcel(Connection conn, YHNotify notify,YHPerson person,String beginDatetemp,String endDatetemp,String stat)throws Exception{
    YHORM orm = new YHORM();
    Date currentDate = new Date();
    Statement stmt = null;
    ResultSet rs = null;
    String querynewsParam = "";
    String querynewsSql = "";
    int recordCount = 0; 
    int pageCount = 0;
    int pgStartRecord = 0;
    int pgEndRecord = 0;//结束索引
    StringBuffer sb = new StringBuffer();
    List<Map<String, String>> newsManagerList = new ArrayList();
    //   Map<String, String> attr = fileUploadLogic(fileForm, pathPx);
    String fromIdtemp = notify.getFromId();
    if(!"".equals(fromIdtemp)&&fromIdtemp!=null)
    querynewsParam = querynewsParam + " and n.FROM_ID in (" + fromIdtemp + ")";
    String formattemp = notify.getFormat();
    if(!"".equals(formattemp) && formattemp!=null)
    querynewsParam = querynewsParam + " and n.FORMAT ='" + formattemp + "'";
    String typeIdtemp = notify.getTypeId();
    if(!"".equals(typeIdtemp)&& typeIdtemp!=null)
    querynewsParam = querynewsParam + " and n.TYPE_ID ='" + typeIdtemp + "'";
    String publishtemp = notify.getPublish();
    if(!"".equals(publishtemp) && publishtemp!=null)
    querynewsParam = querynewsParam + " and n.PUBLISH ='" + publishtemp + "'";
    String toptemp = notify.getTop();
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if(!"".equals(toptemp) && toptemp!=null) {
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        querynewsParam = querynewsParam + " and n.[TOP] ='" + toptemp + "'";
      }else {
        querynewsParam = querynewsParam + " and n.TOP ='" + toptemp + "'";
      }
    }
    String subjecttemp = notify.getSubject();
    if(!"".equals(subjecttemp) && subjecttemp!=null)
    querynewsParam = querynewsParam + " and n.SUBJECT like '%" +  YHDBUtility.escapeLike(subjecttemp) + "%' " + YHDBUtility.escapeLike();
//    Date beginDatetemp = notify.getBeginDate();
    if(!"".equals(beginDatetemp)&& beginDatetemp!=null)
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.SEND_TIME", beginDatetemp, ">=");
//    Date endDatetemp = notify.getEndDate();
    if(!"".equals(endDatetemp) && endDatetemp!= null)
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.SEND_TIME", YHUtility.getDateTimeStr(YHUtility.getDayAfter(endDatetemp,1)), "<"); 
    String contenttemp = notify.getContent(); 
    if(!"".equals(contenttemp) && contenttemp != null){
      querynewsParam = querynewsParam + " and n.CONTENT like '%" + YHDBUtility.escapeLike(contenttemp) + "%' "+YHDBUtility.escapeLike();
    }
    if(!"".equalsIgnoreCase(stat))
    {
       if("1".equalsIgnoreCase(stat))
         querynewsParam +=" and BEGIN_DATE>"+ YHDBUtility.currDateTime() +" AND PUBLISH ='1' ";
       else if("2".equalsIgnoreCase(stat))
         querynewsParam +=" and BEGIN_DATE<="+ YHDBUtility.currDateTime() +" and (END_DATE>"+YHDBUtility.currDateTime() +" or END_DATE IS NULL) AND PUBLISH ='1'";
       else if("3".equalsIgnoreCase(stat))
         querynewsParam +=" and (END_DATE < BEGIN_DATE or END_DATE <= "+ YHDBUtility.currDateTime() +")";
    }
    
    if(!"1".equals(person.getUserPriv())&&!"1".equals(person.getPostPriv())) {
    querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_notify n,PERSON p,oa_department d where n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.FROM_ID='"+person.getSeqId()+"'";
    }else {
    querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_notify n,PERSON p,oa_department d where n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
    }
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      querynewsSql = querynewsSql + querynewsParam + " order by  n.[TOP] desc,n.SEND_TIME desc"; 
    }else {
      querynewsSql = querynewsSql + querynewsParam + " order by  n.TOP desc,n.SEND_TIME desc"; 
    }
    //YHOut.println(querynewsSql);
    try {
      stmt = conn.createStatement(); 
      rs = stmt.executeQuery(querynewsSql);
     while(rs.next()){
      Map<String, String> notifytemp = new HashMap<String, String>(); 
        Statement stmtt = null;
        ResultSet rss = null;
        String toNameTitle = "";
        String toNameStr = "";
        String publishDesc = "";
        String typeName = "";  
        String notifyStatus = "";
        String subjectTitle = "";
        String notifyStatusStr = "";
        String notifyStatusStr2="";
        int seqId = rs.getInt("SEQ_ID");
        String fromId = rs.getString("FROM_ID");
        String toId = rs.getString("TO_ID");
        String fromName = rs.getString("USER_NAME");
        String subject = YHUtility.null2Empty(rs.getString("SUBJECT"));
        String readers = rs.getString("READERS");
        String deptName = rs.getString("DEPT_NAME");
        String format = rs.getString("FORMAT");
        String top = rs.getString("TOP");
        String privId = rs.getString("PRIV_ID");
        String userId = rs.getString("USER_ID");
        String typeId = rs.getString("TYPE_ID");
        String publish = rs.getString("PUBLISH");
        Date sendTime = rs.getTimestamp("SEND_TIME");
        Date beginDate = rs.getDate("BEGIN_DATE");
        Date endDate = rs.getDate("END_DATE");
        notifytemp.put("subject", subject);
        if("0".equals(publish)){
          publishDesc = "<font color=red>未发布</font>";
        }
        if("2".equals(publish)){
          publishDesc = "<font color=blue>待审批</font>";
        }
        if("3".equals(publish)){
          publishDesc = "<font color=red>未通过</font><br><a href='#'>审批意见</a>";
        }
        if("1".equals(publish)){
          publishDesc = "";
        }
        
        String endDateStr = "";
        if(!"".equals(endDate)&&endDate!=null) {
          endDateStr = endDate.toString();
        }
      //根据新闻类型字段的值，获取新闻类型的代码描述

        if(typeId!=null){
           if(!"".equals(typeId.trim())&&!"null".equals(typeId)){
             String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="+typeId;
             stmtt = conn.createStatement(); 
             rss = stmtt.executeQuery(queryTypeNameStr);
              if(rss.next()) {
                typeName = rss.getString("CLASS_DESC");
              }
           }
       }else{
         typeName = "";
       }
        //得到发布范围-部门的名称（串）
        String toName = "";
        String excelName = "";
        String excelUser = "";
        String excelRole = "";
        String queryToNameStr = "";
        if(toId != null && !"".equals(toId)){
          String[] toIds = toId.split(",");
          toId = "";
          for(int j = 0 ;j < toIds.length ; j++){
            toId += toIds[j] + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
        }
        if("0".equals(toId)||"ALL_DEPT".equals(toId)) {
          toName = "全体部门";
        }else if(!"".equals(toId.trim())&&toId!=null){
           queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in (" + toId + ")";
           stmtt = conn.createStatement();
           rss = stmtt.executeQuery(queryToNameStr);
           while(rss.next()) {
             toName = toName + rss.getString("DEPT_NAME") + ",";
           }
        }
        //得到发布范围-角色的名称（串）
        String privName = "";
        String queryPrivNameStr = "";
        if(privId != null && !"".equals(privId)){
          String[] privIds = privId.split(",");
          privId = "";
          for(int j = 0 ;j < privIds.length ; j++){
            privId += privIds[j] + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if(privId != null && !"".equals(privId.trim())){
          queryPrivNameStr = "select PRIV_NAME from USER_PRIV where SEQ_ID in (" + privId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryPrivNameStr);
          while(rss.next()) {
            privName = privName + rss.getString("PRIV_NAME") + ",";
          }
        }
   //得到发布范围-人员的姓名（串）
        String userName = "";
        String queryUserNameStr = "";
        if(userId != null && !"".equals(userId)){
          String[] userIds = userId.split(",");
          userId = "";
          for(int j = 0 ;j < userIds.length ; j++){
            userId +=  userIds[j] + ",";
          }
          userId = userId.substring(0, userId.length() - 1);
        }
        if(userId != null && !"".equals(userId.trim())){
          queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in (" + userId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryUserNameStr);
          while(rss.next()) {
            userName = userName + rss.getString("USER_NAME") + ",";
          }
        }
        
        //设置好部门的名称（串）的显示格式
        if(!"".equals(toName)) {
        	excelName = toName;
        	toNameTitle = "部门:" + toName;  
        	if(toName.length()>20) {
            toName = toName.substring(0, 15)+"...";
          }
          toNameStr = "<font color=#0000FF><b>部门：</b></font>";
          toNameStr += toName;
          toNameStr += "<br>";
        }
       
        //设置好角色的名称（串）的显示格式
        String privNameTitle = "";
        String privNameStr = "";
        if(!"".equals(privName)) {
        	excelRole = privName;
        	privNameTitle =   "角色：" + privName;
          if(privName.length()>20) {
            privName = privName.substring(0, 15)+"...";
          }
          privNameStr =  "<font color=#0000FF><b>角色：</b></font>";
          privNameStr += privName;
          privNameStr += "<br>";
        }
        
     //设置好人员的姓名（串）的显示格式
        String userNameTitle = "";
        String userNameStr = "";
        if(!"".equals(userName)) {
        	excelUser = userName;
        	userNameTitle =  "人员：" + userName;
        	if(userName.length()>20) {
            userName = userName.substring(0, 15)+"...";
          }
          userNameStr =  "<font color=#0000FF><b>人员：</b></font>";
          userNameStr += userName;
          userNameStr += "<br>";
        }
        
        if(currentDate.compareTo(beginDate)<0) {
          notifyStatus = "1";
          notifyStatusStr = "待生效";
          notifyStatusStr2="待生效";
        }else {
          notifyStatus = "2";
          if(!"2".equals(publish)&&!"3".equals(publish)) {
            notifyStatusStr = "<font color='#00AA00'><b>生效</font>";
            notifyStatusStr2="生效";
          }else {
            if("2".equals(publish)) {
              notifyStatusStr = "<font color='blue'><b>待审批</font>";
              notifyStatusStr2 ="待审批";
            }
            if("3".equals(publish)) {
              notifyStatusStr = "<font color='red'><b>未通过</font><br><a href='javascript:my_affair("+seqId+")'; title='点击查看审批意见'>审批意见</a>"; 
              notifyStatusStr2 ="未通过";
            }
          }
        }
        if((!"".equals(endDate)&&endDate!=null)) {
          if(currentDate.compareTo(endDate)>0) {
              notifyStatus = "3";
             notifyStatusStr = "<font color='#FF0000'><b>终止</font>";
             notifyStatusStr2 = "终止";
           }
         }
         if("0".equals(publish)) {
           notifyStatusStr = "";
         }
        if("1".equals(top)){
          subject = "<font color=red><b>置顶:" + subject + "</b></font>";
        }  
       
        notifytemp.put("seqId", String.valueOf(seqId));
       
        notifytemp.put("fromId", fromId);
       
        notifytemp.put("toId", toId);        
       
        notifytemp.put("sendTime", dateFormat(sendTime));
    
        notifytemp.put("beginDate", dateFormat(beginDate));
        
        notifytemp.put("endDate", dateFormat(endDate));
        notifytemp.put("fromName", fromName);
        
        notifytemp.put("typeName", typeName);
        String fanwei = "";
        if(!YHUtility.isNullorEmpty(excelName)){
        	fanwei += "部门:"+excelName;
        }
        if(!YHUtility.isNullorEmpty(excelUser)){
        	fanwei += "\n"+"人员:"+excelUser;
        }
        if(!YHUtility.isNullorEmpty(excelRole)){
        	fanwei += "\n"+"角色:"+excelRole;
        }
        
        notifytemp.put("toName", fanwei);
       
        notifytemp.put("notifyStatusStr2", notifyStatusStr2);
        newsManagerList.add(notifytemp);
      }      
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return newsManagerList;   
  }
  /**
   * 工具方法，把集合转化为
   * @param list
   * @return
   */
  public ArrayList<YHDbRecord> convertList(List<Map<String, String>> list){
    ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
    if(list != null && list.size() >0){
      for (int i = 0; i < list.size(); i++) {
        YHDbRecord dbrec = new YHDbRecord();
        dbrec.addField("发布人", list.get(i).get("fromName"));
        dbrec.addField("类型", list.get(i).get("typeName"));
        dbrec.addField("发布范围", list.get(i).get("toName"));
        dbrec.addField("标题", list.get(i).get("subject"));
        dbrec.addField("创建时间", list.get(i).get("sendTime"));
        dbrec.addField("生效日期", list.get(i).get("beginDate"));
        dbrec.addField("终止日期", list.get(i).get("endDate"));
        dbrec.addField("状态", list.get(i).get("notifyStatusStr2"));
        dbL.add(dbrec);
      }
    }    
    return dbL;    
  }
  
  public  String dateFormat(Date date){
    if(date != null){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String ds = sdf.format(date);    
      return ds.toString();
    }else{
      return "";
    }    
  }
  
//------------------------begin  以下为公告删除   ------------------------
  public String deleteByMe(Connection conn, YHNotify notify,YHPerson person,  String beginDatetemp,String endDatetemp, String stat) throws Exception{
    YHORM orm = new YHORM();
      Date currentDate = new Date();
      Statement stmt = null;
      ResultSet rs = null;
      String querynewsParam = "";
      String querynewsSql = "";
      int recordCount = 0; 
      int pageCount = 0;
      int pgStartRecord = 0;
      int pgEndRecord = 0;//结束索引
      List newsManagerList = new ArrayList(); 
      //   Map<String, String> attr = fileUploadLogic(fileForm, pathPx);
      String fromIdtemp = notify.getFromId();      
      if(fromIdtemp!=null && !"".equals(fromIdtemp)&& !"null".equalsIgnoreCase(fromIdtemp))
      querynewsParam = querynewsParam + " and n.FROM_ID in (" + fromIdtemp + ")";
      String formattemp = notify.getFormat();
      if(!"".equals(formattemp) && formattemp!=null)
      querynewsParam = querynewsParam + " and n.FORMAT ='" + formattemp + "'";
      String typeIdtemp = notify.getTypeId();
      if(!"".equals(typeIdtemp) && typeIdtemp!=null)
      querynewsParam = querynewsParam + " and n.TYPE_ID ='" + typeIdtemp + "'";
      String publishtemp = notify.getPublish();
      if(!"".equals(publishtemp) && publishtemp!=null)
      querynewsParam = querynewsParam + " and n.PUBLISH ='" + publishtemp + "'";
      String toptemp = notify.getTop();
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      if (!"".equals(toptemp)&& toptemp!=null) {
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          querynewsParam = querynewsParam + " and n.[TOP] ='" + toptemp + "'";
        }else {
          querynewsParam = querynewsParam + " and n.TOP ='" + toptemp + "'";
        }
      }
      String subjecttemp = notify.getSubject();
      if(!"".equals(subjecttemp)&& subjecttemp!=null)
      querynewsParam = querynewsParam + " and n.SUBJECT like '%" + YHDBUtility.escapeLike(subjecttemp) + "%' "+YHDBUtility.escapeLike();
//      Date beginDatetemp = notify.getBeginDate();
      if(!"".equals(beginDatetemp)&&beginDatetemp != null)
      querynewsParam = querynewsParam + " and "+YHDBUtility.getDateFilter("n.SEND_TIME", beginDatetemp, ">=");
//      Date endDatetemp = notify.getEndDate();
      if(!"".equals(endDatetemp) && endDatetemp != null)
      querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.SEND_TIME", YHUtility.getDateTimeStr(YHUtility.getDayAfter(endDatetemp,1)), "<"); 
      String contenttemp = notify.getContent(); 
      if(!"".equals(contenttemp) && contenttemp != null)
      querynewsParam = querynewsParam + " and n.CONTENT like '%" + YHDBUtility.escapeLike(contenttemp) + "%' "+YHDBUtility.escapeLike();
      
      if(!"".equalsIgnoreCase(stat))
      {
         if("1".equalsIgnoreCase(stat))
           querynewsParam +=" and BEGIN_DATE>"+ YHDBUtility.currDateTime() +" AND PUBLISH ='1' ";
         else if("2".equalsIgnoreCase(stat))
           querynewsParam +=" and BEGIN_DATE<="+ YHDBUtility.currDateTime() +" and (END_DATE>"+YHDBUtility.currDateTime() +" or END_DATE IS NULL) AND PUBLISH ='1'";
         else if("3".equalsIgnoreCase(stat))
           querynewsParam +=" and (END_DATE < BEGIN_DATE or END_DATE <= "+ YHDBUtility.currDateTime() +")";
      }
      
      if(!"1".equals(person.getUserPriv())&&!"1".equals(person.getPostPriv())) {
      querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_notify n,PERSON p,oa_department d where n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.FROM_ID='"+person.getSeqId()+"'";
      }else {
      querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_notify n,PERSON p,oa_department d where n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
      } 
      
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        querynewsSql = querynewsSql + querynewsParam + " order by  n.[TOP] desc,n.SEND_TIME desc";
      }else {
        querynewsSql = querynewsSql + querynewsParam + " order by  n.TOP desc,n.SEND_TIME desc";
      }
      try {
        stmt = conn.createStatement(); 
        rs = stmt.executeQuery(querynewsSql);
       while(rs.next()){
        YHNotify notifytemp = new YHNotify();          
          int seqId = rs.getInt("SEQ_ID");         
          newsManagerList.add(seqId);
        }      
      } catch (Exception e) {
        throw e;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
      return toAstring(newsManagerList);
  }
  
  private int count = 0;
  public void setCount(int count){
    this.count = count;
  }
  public int getCount(){
    return this.count;
  }
  public String toAstring(List<Integer> ids){
    String newIds = "";
    if(ids!=null && ids.size()>0){
      setCount(ids.size());
      for(int i=0; i<ids.size(); i++){
        newIds += ids.get(i)+",";
      }
    }else{
      setCount(0);
    }
    return newIds.substring(0, newIds.lastIndexOf(",")==-1?0:newIds.lastIndexOf(","));
  }
  
  public void deleteSelNotify(Connection conn, YHNotify notify,YHPerson person,  String beginDatetemp,String endDatetemp, String stat) throws Exception{
    String ids = deleteByMe(conn, notify, person, beginDatetemp, endDatetemp, stat);
    if(ids != ""){
      deleteSelNotify(conn, ids); 
    }   
  }
  
  public void deleteSelNotify(Connection dbConn, String ids) throws Exception {
    PreparedStatement ps = null;    
    String sql = "delete from oa_notify where SEQ_ID in("+ ids +")";
      
      try{
        ps = dbConn.prepareStatement(sql);
        int k = ps.executeUpdate(); 
      } catch (SQLException e){
        throw e;
      }finally{
      YHDBUtility.close(ps, null, log);
      }
  }
 //----------------------------------end--------------------- 
  
  public int getNotifyTopDay(Connection conn) throws Exception {
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_TOP_DAYS'";
	      stmt = conn.createStatement();
	      rs = stmt.executeQuery(queryStr);
	      //System.out.println(queryStr);
	      if(rs.next()) {
	        return rs.getInt("PARA_VALUE");	        
	      }
	    } catch (Exception ex) {
        } finally {
	      YHDBUtility.close(stmt, rs, log);
	    }
	    return 0;
	  }


}

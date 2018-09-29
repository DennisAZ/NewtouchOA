package yh.core.funcs.news.logic;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.news.data.YHNews;
import yh.core.funcs.news.data.YHNewsCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
/**
 * 新闻管理
 * @author qwx110
 *
 */
public class YHNewsManageLogic {
  private static Logger log = Logger.getLogger(YHNewsManageLogic.class);
  private YHNewsManageUtilLogic newsManageUtil = new YHNewsManageUtilLogic();
  private YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
  public static String filePath = YHSysProps.getAttachPath() + File.separator + "news";
  /**
   * 新建新闻，修改新闻之前
   * @param dbConn
   * @param person
   * @return
   * @throws Exception
   */
  public String beforeAddNews(Connection dbConn,YHPerson person) throws Exception{
    Statement st = null;
    ResultSet rs = null;
    List auditers = new ArrayList();
    int userId = person.getSeqId();
    StringBuffer sb = new StringBuffer("{");
    try{
      int typeNum = 0;
      String getNotifyTypeSql = "select SEQ_ID,CLASS_DESC from oa_kind_dict_item where CLASS_NO='NEWS'";
      Statement typeSt = dbConn.createStatement();
      ResultSet typeRs = typeSt.executeQuery(getNotifyTypeSql);
      sb.append("typeData:[");
      while(typeRs.next()){
        typeNum ++;
        sb.append("{");
        sb.append("typeId:\"" + typeRs.getInt("SEQ_ID") + "\"");
        sb.append(",typeDesc:\"" + typeRs.getString("CLASS_DESC") + "\"");
        sb.append("},");
      }
      if(typeNum >0) {
        sb.deleteCharAt(sb.length() - 1); 
        }
      sb.append("],");
      
      String getSysRemindSql = "select PARA_VALUE from SYS_PARA where PARA_NAME='SMS_REMIND'";
      Statement sysRemindSt = dbConn.createStatement();
      ResultSet sysRemindRs = sysRemindSt.executeQuery(getSysRemindSql);
      if(sysRemindRs.next()) {
        String sysReminds = sysRemindRs.getString("PARA_VALUE");
        sb.append("sysReminds:\"" + sysReminds + "\"");
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
  
  /**
   * 保存新建的新闻
   * 新建的新闻的评论
   * @param anonymityYn：       0:实名评论，1：匿名评论，2:禁止评论
   * @param attachmentId：    附件ID，attachmentName：附件名
   * @param toId：                         部门id串
   * @param privId：                   角色id串
   * @param conn
   * @param fileForm
   * @param person
   * @param pathPx
   * @return
   * @throws Exception
   */
  public int saveMailLogic(Connection conn, YHFileUploadForm fileForm ,YHPerson person ,String pathPx) throws Exception{
    YHORM orm = new YHORM();
    String typeId = "";
    String copyToId = "";
    String secretToId ="";
    String seqIdStr = "";
    String urlAdd = "";
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
      typeId = ((String[]) request.get("typeId"))[0];
    }catch(Exception e){
      typeId = (String) request.get("typeId");
    }
    try{
      urlAdd = ((String[]) request.get("urlAdd"))[0];
    }catch(Exception e){
      urlAdd = (String) request.get("urlAdd");
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
//    System.out.println("$$$$$$$$$$"+typeId);
    Set<String> attrKeys = attr.keySet();
    for (String key : attrKeys){
      String fileName = attr.get(key);
      attachmentId += key + ",";
      attachmentName += fileName + "*";
    }
    //YHOut.println(attachmentId+"**************"+attachmentName);
    
    YHNews news = (YHNews) YHFOM.build(request, YHNews.class, null);
    String subjectFont = (String)request.get("subjectFont");
    news.setSubjectFont(subjectFont);
    
    String mailRemind = "";                                                  //邮件提醒
    String mobileRemind = "";                                                //手机提醒
    try{
      mailRemind = ((String[]) request.get("mailRemind"))[0];
      mobileRemind = ((String[]) request.get("remind"))[0];
    }catch(Exception e){
      mailRemind = (String) request.get("mailRemind");
      mobileRemind = (String) request.get("remind");
    }
    if("2".equals(news.getFormat())) {                                       //新闻格式1：mht格式，2：超级链接格式，0：普通格式
      news.setContent(urlAdd);
    }
//    long size = eb.getEnsize() + getSize(fileForm) ;
//    eb.setEnsize(1l);
    if(news.getSubject() == null || "".equals(news.getSubject())){
      String subject  = "";
      if(attachmentName != null && !"".equals(attachmentName)) {
       subject = attachmentName.split("\\*")[0];
       subject = subject.substring(subject.indexOf("_") + 1,subject.lastIndexOf("."));
       //System.out.println(subject);
      }else{
        subject = "[无主题]";
      }
      news.setSubject(subject);
    }
    if(news.getContent() == null){
      //if(request.get("co"))
    }
    int seqId = news.getSeqId();
    
    news.setProvider(Integer.toString(person.getSeqId()));  
    if(news.getNewsTime() == null){
      news.setNewsTime(new Date());
    }
    if(news.getToId() == null ||  "".equals(news.getToId())){
      news.setToId("-1");
    }
    news.setAttachmentId(attachmentId);
    news.setAttachmentName(attachmentName);
    news.setTypeId(typeId);
    if(seqIdStr != null && !"".equals(seqIdStr)){
      seqId = Integer.valueOf(seqIdStr.trim());
      news.setSeqId(seqId);
      String fckContent = YHUtility.null2Empty(news.getContent());      
      news.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
      news.setContent(YHDiaryUtil.cutHtml(fckContent));
      orm.updateSingle(conn, news);//先保存正文      bId =  news.getSeqId();
    }else{
      String fckContent = YHUtility.null2Empty(news.getContent());      
      news.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
      news.setContent(YHDiaryUtil.cutHtml(fckContent));
      orm.saveSingle(conn, news);
      bId = newsManageUtil.getBodyId(conn);  // ?????????
    }
    
    //如果是发布操作，查询出发布范围的人员的id，拼成id串    Date now = new Date();              //判断现在的时间和发布时间的大小
    Date ptime = news.getNewsTime();
    long cha = ptime.getTime() - now.getTime();  
    //如果发布时间小于当期时间，则发送短信    if("1".equals(news.getPublish())&&("on".equals(mailRemind)||"1".equals(mailRemind))) {//如果是新闻已发布，且是邮件提醒      YHSmsBack smsBack = new YHSmsBack();
      
      String queryFWStr = "";                                                             // 通知的范围的查询语句
      String toIdFW = "";                                                                 // 查询出来的在通知范围内的人员的id串      
    
      if("0".equals(news.getToId())||"ALL_DEPT".equals(news.getToId())) {                 //如果发布范围是所有部门，部门选择是必填的
        queryFWStr = "select SEQ_ID from PERSON where NOT_LOGIN!='1'";
      }else {
//        if(isAllDept(conn, person.getSeqId())==false){//如果不是全体部门
//          queryFWStr = " select SEQ_ID from PERSON where SEQ_ID in( ";
//         }
        queryFWStr = "select SEQ_ID from PERSON where NOT_LOGIN!='1'";
        String toId = news.getToId();
        if(toId != null && !"".equals(toId)){
          String[] toIds = toId.split(",");
          toId = "";
          for(int j = 0 ;j < toIds.length ; j++){
            toId += toIds[j] + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
        }
        if(!"".equals(toId.trim())&&toId!=null) {                                        //如果存在部门id则选择部门号在查询范围里
          queryFWStr = queryFWStr + " and (DEPT_ID in (" + toId + ")";
        }
        String privId = news.getPrivId();                                                //角色
        if(privId != null && !"".equals(privId)){
          String[] privIds = privId.split(",");
          privId = "";
          for(int j = 0 ;j < privIds.length ; j++){
            privId +=  privIds[j] + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if(!"".equals(privId.trim())&&privId!=null) {                                    //如果存在角色id则选择角色号在查询范围里
          queryFWStr = queryFWStr + " or USER_PRIV in ("+ privId +")";
        }
        String userId = news.getUserId();                                                //用户id
        if(userId != null && !"".equals(userId)){
          String[] userIds = userId.split(",");
          userId = "";
          for(int j = 0 ;j < userIds.length ; j++){
            userId += userIds[j] + ",";
          }
          userId = userId.substring(0, userId.length() - 1);
        }
        if(!"".equals(userId.trim())&&userId!=null) {
          queryFWStr = queryFWStr + " or SEQ_ID in ("+ userId + ")";                      //如果存在人员,怎查询这人人员范围
        }
        queryFWStr = queryFWStr + ")";
      }
      //YHOut.println(queryFWStr);
      Statement stmtFW = null;
      ResultSet rsFW = null;
      try {
        stmtFW = conn.createStatement();
        rsFW = stmtFW.executeQuery(queryFWStr);
        while(rsFW.next()) {
          toIdFW = toIdFW + rsFW.getString("SEQ_ID")+",";                                  //拼在发布范围内的人员的id串
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        throw e;
      } finally{
        YHDBUtility.close(stmtFW, rsFW, log);
      }     
      
      String content = "请查看新闻！\n标题：" + news.getSubject();
//      Map map = new HashMap();
//      map.put("SUBJECT", news.getSubject());
//      YHNews tempNews = (YHNews)orm.loadObjSingle(conn, YHNews.class,map);
      String remindUrl = "/core/funcs/news/show/readNews.jsp?seqId="+bId+"&openFlag=1"; 
      smsBack.setContent(content);
      smsBack.setFromId(person.getSeqId());
      smsBack.setRemindUrl(remindUrl);
      smsBack.setSmsType("14");
      smsBack.setToId(toIdFW);  
      if(cha>0){
        smsBack.setSendDate(ptime);
      }     
      
      if(!"".equals(toIdFW.trim())&&toIdFW!=null&&toIdFW.contains(",")==true){
        YHSmsUtil.smsBack(conn, smsBack);                 
      }
      if("on".equalsIgnoreCase(mobileRemind)){
        YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
        String sms2ToId = toIdFW; 
        ms2l.remindByMobileSms(conn, sms2ToId, person.getSeqId(), content, news.getNewsTime());
      }
    }     
    return bId;
  } 
  
  //没用
  public int savettachMailLogic(Connection conn, YHFileUploadForm fileForm ,int fromId,String pathPx) throws Exception{
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
    Map<String, String> attr = fileUploadLogic(fileForm, pathPx);
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
    YHNews news = (YHNews) YHFOM.build(request, YHNews.class, null);
    String mailRemind = "";
    String mobileRemind = "";
    try{
      mailRemind = ((String[]) request.get("mailRemind"))[0];
      mobileRemind = ((String[]) request.get("mobileRemind"))[0];
    }catch(Exception e){
      mailRemind = (String) request.get("mailRemind");
      mobileRemind = (String) request.get("mobileRemind");
    }
    if("2".equals(news.getFormat())) {
      news.setContent(urlAdd);
    }
//    long size = eb.getEnsize() + getSize(fileForm) ;
//    eb.setEnsize(1l);
    if(news.getSubject() == null || "".equals(news.getSubject())){
      String subject  = "";
      if(attachmentName != null && !"".equals(attachmentName)) {
       subject = attachmentName.split("\\*")[0];
       subject = subject.substring(subject.indexOf("_") + 1,subject.lastIndexOf("."));
       //System.out.println(subject);
      }else{
        subject = "[无主题]";
      }
      news.setSubject(subject);
    }
    int seqId = news.getSeqId();
    news.setProvider(Integer.toString(fromId));

    news.setNewsTime(new Date());
    if(news.getToId() == null ||  "".equals(news.getToId())){
      news.setToId("-1");
    }
    news.setAttachmentId(attachmentId);
    news.setAttachmentName(attachmentName);
    String fckContent = YHUtility.null2Empty(news.getContent());      
    news.setCompressContent(fckContent.getBytes(YHConst.DEFAULT_CODE));
    news.setContent(YHDiaryUtil.cutHtml(fckContent));
    if(seqIdStr != null && !"".equals(seqIdStr)&&!"0".equals(seqIdStr)){
      seqId = Integer.valueOf(seqIdStr.trim());
      news.setSeqId(seqId);
      orm.updateSingle(conn, news);//先保存正文
      bId =  news.getSeqId();
    }else{
      orm.saveSingle(conn, news);
      bId = newsManageUtil.getBodyId(conn);
    }
    return bId;
  } 
  /*
   * 查询person表里要设置权限的人，满足以下条件的用户都查出来
   *   不是系统管理员的（他不用设置）
   *   应用到其他用户--“所属角色”里指定的角色相关用户（注："所在部门"条件在while循环里再加，这里可能不好加）
   *   当前正在编辑的用户
   */
  public String getnewsManagerList(Connection conn,YHPerson loginUser,String type,
      String ascDesc,String field,int showLen,int pageIndex,HttpServletRequest request)throws Exception {
    Date currentDate = new Date();
    Statement stmt = null;
    ResultSet rs = null;
    int pageCount = 0;//页码数
    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    int pgEndRecord = 0;//结束索引
    String userPriv = loginUser.getUserPriv();
    String userSeqId = Integer.toString(loginUser.getSeqId());
    String querynewsStr = null;
    List newsManagerList = new ArrayList();
    StringBuffer sb  = new StringBuffer();
    String contextPath = request.getContextPath();
    sb.append("{");
    sb.append("listData:[");
    try{   
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      //角色是“OA管理员”，从新闻表里取所有的新闻（能看到并管理所有的新闻）
      if("1".equals(userPriv)) {
        querynewsStr = "SELECT n.SEQ_ID,n.SUBJECT,n.PROVIDER,n.NEWS_TIME,n.CLICK_COUNT,p.DEPT_ID," +
            "n.FORMAT,n.TYPE_ID,n.PUBLISH,n.ANONYMITY_YN,n.TO_ID,n.PRIV_ID,n.USER_ID,p.USER_NAME,d.DEPT_NAME from oa_news n," +
            "PERSON p,oa_department d where 1=1 and n.PROVIDER=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
      //角色不是“OA管理员”，则只能看到自己发布的新闻
      }else {
        querynewsStr = "SELECT n.SEQ_ID,n.SUBJECT,n.PROVIDER,n.NEWS_TIME,n.CLICK_COUNT,p.DEPT_ID," +
            "n.FORMAT,n.TYPE_ID,n.PUBLISH,n.ANONYMITY_YN,n.TO_ID,n.PRIV_ID,n.USER_ID,p.USER_NAME,d.DEPT_NAME from oa_news n," +
            "PERSON p,oa_department d where 1=1 and n.PROVIDER=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.PROVIDER='"+userSeqId+"'";
      }
      //如果指定了一个新闻类型，则仅查询指定类型的新闻
      
      if("".equals(type)){//选择“无类型“
        querynewsStr = querynewsStr + " and (n.TYPE_ID='' or n.TYPE_ID=' ' or n.TYPE_ID is null)";
      }else if(!"0".equals(type)) {//选择的不是”所有类型“
        querynewsStr = querynewsStr + " and (n.TYPE_ID='"+ type + "')";
      }
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      //加上排序
      if("".equals(field)) {
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          querynewsStr = querynewsStr + " order by [TOP] desc,NEWS_TIME desc";
        }else {
          querynewsStr = querynewsStr + " order by TOP desc,NEWS_TIME desc";
        }
      }else {
        querynewsStr = querynewsStr + " order by " + field;
      //设置升序/降序
      if("1".equals(ascDesc)) {
          querynewsStr = querynewsStr + " desc";
        }else {
          querynewsStr = querynewsStr + " asc";
        }
      }
      rs = stmt.executeQuery(querynewsStr); 
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
    //YHOut.println(pageCount);
     //开始索引    //YHOut.println(querynewsStr+"*****************************************");
     pgStartRecord = (pageIndex - 1) * showLen + 1;
   
     rs.absolute( (pageIndex - 1) * showLen + 1); 
     int temp = 0;
     for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
       YHNews news = new YHNews(); 
       Statement stmtt = null;
       ResultSet rss = null;
       String toNameTitle = "";
       String toNameStr = "";
       String subjectTitle = "";
       String  publishDesc= "";
       int sqlId = rs.getInt("SEQ_ID");
       String providerName = rs.getString("USER_NAME");
       String toId = rs.getString("TO_ID");
       String privId = rs.getString("PRIV_ID");
       String userId = rs.getString("USER_ID");
       String publish = rs.getString("PUBLISH");
       String typeId = rs.getString("TYPE_ID");
       String subject = rs.getString("SUBJECT");
       String deptId = rs.getString("DEPT_ID");
       String format = rs.getString("FORMAT");
       Date newsTime = rs.getTimestamp("NEWS_TIME");
       String clickCount = rs.getString("CLICK_COUNT");
       String anonymityYn = rs.getString("ANONYMITY_YN");
       String strTemp = "";//管理评论的字符串
       
     //新闻标题超链接对应的title属性，显示完整的新闻标题
       if(subject.length()>50) {
         subjectTitle = subject;
         subject = subject.substring(0, 50) + "...";
       }
       
       //根据新闻类型字段的值，获取新闻类型的代码描述
       String typeName = "";
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
            toId += toIds[j]  + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
          if("0".equals(toId)||"ALL_DEPT".equals(toId)) {
            toName = "全体部门";
          }else if(!"".equals(toId.trim())&&!"null".equals(toId)){
             queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in (" + toId + ")";
             stmtt = conn.createStatement();
             rss = stmtt.executeQuery(queryToNameStr);
             while(rss.next()) {
               toName = toName + rss.getString("DEPT_NAME") + ",";
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
            privId +=  privIds[j]  + ",";
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
            userId += userIds[j] + ",";
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
       
     //取出发布人所在部门的“长部门名”，即带有级次的完整的部门名称，形如：xx局/二处/五组
       String deptName = "";
       String queryDeptNameStr = "select DEPT_NAME from oa_department where SEQ_ID='"+ deptId +"'";
       stmtt = conn.createStatement();
       rss = stmtt.executeQuery(queryDeptNameStr);
       while(rss.next()) {
         deptName = rss.getString("DEPT_NAME");
       }
         
     //$PUBLISH=="0"，在状态列显示“未发布”
       if("0".equals(publish)) {
         publishDesc = "<font color=red>未发布</font>";
       }
     //$PUBLISH=="2"，在状态列显示“已终止”，在操作列显示“生效”超链接
       if("2".equals(publish)) {
         publishDesc = "<font color=green>已终止</font>";
         strTemp = "<a href='"+ contextPath +"/yh/core/funcs/news/act/YHNewsHandleAct/changeState.act?seqId="+sqlId+"&pageIndex="+pageIndex+"&showLength="+showLen+"'>生效</a>";
       }
       //$PUBLISH=="1"，在状态列显示“”，在操作列显示“终止”超链接
       if("1".equals(publish))
       {
         publishDesc="";
         strTemp= "<a href='"+ contextPath +"/yh/core/funcs/news/act/YHNewsHandleAct/changeState.act?seqId="+sqlId+"&isEnd=1&pageIndex="+pageIndex+"&showLength="+showLen+"'>终止</a>";
       }
       //System.out.println(strTemp);
       //取得该新闻记录的评论条数
       int commentCount = 0;
       String queryCommentSql = "SELECT count(*) from oa_news_comment where NEWS_ID='" + sqlId + "'";
       stmtt = conn.createStatement();
       rss = stmtt.executeQuery(queryCommentSql);
       while(rss.next()) {
         commentCount = rss.getInt(1);
       } 
       
       news.setSeqId(sqlId);
       
       sb.append("{");
       sb.append("seqId:" + sqlId);
       sb.append(",deptName:\"" +  YHUtility.encodeSpecial(deptName) + "\"");
       sb.append(",publish:\"" +  YHUtility.encodeSpecial(publish) + "\"");
       sb.append(",providerName:\"" + YHUtility.encodeSpecial(providerName) + "\"");
       sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
       sb.append(",toNameTitle:\"" + YHUtility.encodeSpecial(toNameTitle) + "\"");
       sb.append(",toNameStr:\"" + YHUtility.encodeSpecial(toNameStr) + "\"");
       sb.append(",privNameTitle:\"" + YHUtility.encodeSpecial(privNameTitle) + "\"");
       sb.append(",privNameStr:\"" + YHUtility.encodeSpecial(privNameStr) + "\"");
       sb.append(",userNameTitle:\"" + YHUtility.encodeSpecial(userNameTitle) + "\"");
       sb.append(",userNameStr:\"" + YHUtility.encodeSpecial(userNameStr) + "\"");
       sb.append(",format:\"" + YHUtility.encodeSpecial(format) + "\"");
       sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle) + "\"");
       sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
       sb.append(",newsTime:\"" + newsTime + "\"");
       sb.append(",clickCount:\"" + YHUtility.encodeSpecial(clickCount) + "\"");
       sb.append(",commentCount:\"" + commentCount + "\"");
       sb.append(",publishDesc:\"" + YHUtility.encodeSpecial(publishDesc) + "\"");
       sb.append(",strTemp:\"" + YHUtility.encodeSpecial(strTemp) + "\"");
       sb.append(",anonymityYn:\"" + YHUtility.encodeSpecial(anonymityYn) + "\"");
       sb.append("},");
       
       newsManagerList.add(news);
       rs.next(); 
    } 
     //结束索引
     pgEndRecord =(pageIndex - 1) * showLen + newsManagerList.size();
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
//     returnMap.put("listData", newsManagerList);
//     returnMap.put("pageData", sb.toString());
     return sb.toString();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 终止或生效某个新闻
   * @param dbConn
   * @param loginUser
   * @param showLenStr
   * @param pageIndexStr
   * @param seqId
   * @param isEnd
   * @return
   * @throws Exception
   */
  public boolean changeState(Connection dbConn, YHPerson loginUser,int showLenStr,int pageIndexStr,String seqId,String isEnd) throws Exception {
    ResultSet rs = null;
    Statement st = null;
    String changeStateSql = "";
    boolean success = false;
    //public = 2 终止状态，1： 生效状态
    if("1".equals(isEnd)) {
      changeStateSql = "update oa_news set PUBLISH='2' where SEQ_ID='" + seqId + "'";
    }else{
      changeStateSql = "update oa_news set PUBLISH='1' where SEQ_ID='"+ seqId + "'";
    }
    try {
      st = dbConn.createStatement();
      success = st.execute(changeStateSql);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(st, rs, log);
    }
    return success;
  }
  
  //查阅情况
  /**
   * 把所有的查看的人员，角色所在的部门全部列出，如果有人员查看，
   * 怎把查看人列入已看人员列，把未查看的人员列入未看人员列
   * 注意：选择部门时把这个部门，以及这个部门所有的上级部门都列出来。
   */
  public String  showReader(Connection conn,String seqId,String displayAll) throws Exception {
    YHORM orm = new YHORM();
    ResultSet rs = null;
    Statement st = null;
    String providerName = "";
    int deptId = 0;
    String deptName = "";
//    String deptPriv ="";
    String optionText = "";
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    int seqID = 0;
    if(!"".equals(seqId)&&seqId!=null) {
      seqID = Integer.parseInt(seqId);
    }
    int unReaderCount = 0;
    try {
      YHNews news = (YHNews)orm.loadObjSingle(conn, YHNews.class, seqID);
      if (news != null && !YHUtility.null2Empty(news.getFormat()).equals("2")) {
        byte[] byteContent = news.getCompressContent();
        if (byteContent == null) {
          news.setContent("");
        }else {
          news.setContent(new String(byteContent, "UTF-8"));
        }
      }
      String queryUserSql = "SELECT p.USER_NAME,p.DEPT_ID,d.DEPT_NAME from PERSON p,oa_department d where p.DEPT_ID = d.SEQ_ID and p.SEQ_ID='"+news.getProvider()+"'";
      st = conn.createStatement();
      rs = st.executeQuery(queryUserSql);
      if(rs.next()) {
        providerName = rs.getString("USER_NAME");
        deptId = rs.getInt("DEPT_ID");
        deptName = rs.getString("DEPT_NAME");
//      deptPriv = "1";
      }
      sb.append("seqId:" + news.getSeqId());
      sb.append(",subject:\"" + news.getSubject() + "\"");
      sb.append(",deptName:\"" + deptName + "\"");
      sb.append(",providerName:\"" + providerName + "\"");
      sb.append(",newsTime:\"" + news.getNewsTime() + "\"");
      String userNameStr = "";
      String unUser = "";
      
      String toId = news.getToId();
      
      String privId = news.getPrivId();
      if(!"0".equals(toId)){
        if(privId != null && !"".equals(privId)){
          String[] privIds = privId.split(",");
          privId = "";
          for(int j = 0 ;j < privIds.length ; j++){
            privId +=  privIds[j] + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if(privId != null && !"".equals(privId.trim())){
          String queryPrivSql = "SELECT DEPT_ID from PERSON where USER_PRIV in (" + privId + ")";
          Statement privSt = conn.createStatement();
          ResultSet privRs = privSt.executeQuery(queryPrivSql);
          while(privRs.next()) {
            boolean temp = false;
            deptId = privRs.getInt("DEPT_ID");
            if(toId != null && !"".equals(toId)){
              String[] toIds = toId.split(",");
              for(int j = 0 ;j < toIds.length ; j++){
                String toIdTemp =  toIds[j];
                if(toIdTemp.equals(Integer.toString(deptId))){
                  temp = true;
                  break;
                }
              }
            }
            if(temp==false){
              toId = toId + "," +deptId + ",";
            }
          }
        }
      }
      
     String userId = news.getUserId();
     if(!"0".equals(toId)){
       if(userId != null && !"".equals(userId)){
         String[] userIds = userId.split(",");
         userId = "";
         for(int j = 0 ;j < userIds.length ; j++){
           userId +=  userIds[j]  + ",";
         }
         userId = userId.substring(0, userId.length() - 1);
       }
       if(userId != null && !"".equals(userId.trim())){
         queryUserSql = "SELECT DEPT_ID from PERSON where SEQ_ID in (" + userId + ")";
         Statement userSt = conn.createStatement();
         ResultSet userRs = st.executeQuery(queryUserSql);
         while(userRs.next()) {
           boolean temp = false;
           deptId = userRs.getInt("DEPT_ID");
           if(toId != null && !"".equals(toId)){
             String[] toIds = toId.split(",");
             for(int j = 0 ;j < toIds.length ; j++){
               String toIdTemp =  toIds[j];
               if(toIdTemp.equals(Integer.toString(deptId))){
                 temp = true; 
                 break;
               }
             }
           }
           if(temp==false) {
             toId = toId + "," + deptId + ",";
           }
         }
         
       }
     }
     
//     optionText = deptTreeList(conn,0,toId,news,displayAll);
     String temp = getDeptTreeJson(news,0 , conn,toId);
     sb.append(",listData:").append(temp);
     sb.append("}");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(st, rs, log);
    }
    return sb.toString();
  }
  
 //------ 递归显示部门列表，支持按管理范围显示 --------
  public int childInToId(Connection conn,int deptId,String toId) throws Exception{
    ResultSet rs = null;
    Statement st = null;
    int item = 0;
    try {
      String queryDeptSql = "SELECT SEQ_ID from oa_department where DEPT_PARENT='"+ deptId +"'";  
      st = conn.createStatement();
      rs = st.executeQuery(queryDeptSql);
      if(rs.next()){
        int deptIdtemp = rs.getInt("SEQ_ID");
        int item1 = toId.compareTo(Integer.toString(deptIdtemp));
        int item2 = childInToId(conn,deptIdtemp,toId);
        if(item1==1||item2==1){
          item = 1;
        }
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(st, rs, log);
    }
    
    return item;
  }
  
  public String getDeptTreeJson(YHNews news,int deptId , Connection conn,String toId) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    this.getDeptTree(news,deptId, sb, 0 , conn,toId);
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    return sb.toString();
  }
  
  public void getDeptTree(YHNews news,int deptId , StringBuffer sb , int level , Connection conn,String toId) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。

    List<YHDepartment> list = this.getDeptByParentId(deptId , conn,toId);
    
    for(int i = 0 ;i < list.size() ;i ++){
      String flag = "├";
//      if(i == list.size() - 1 ){
//        flag = "└";
//      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += "　";
      }
      flag = tmp + flag;
      
      YHDepartment dp = list.get(i);
      sb.append("{");
      sb.append("deptName:\"" +flag + YHUtility.encodeSpecial(dp.getDeptName()) + "\"");
      sb = getUserById(news, conn,dp.getSeqId(),sb);
    
      this.getDeptTree(news,dp.getSeqId(), sb, level + 1 , conn,toId);
    }
  }
  
  public List<YHDepartment> getDeptByParentId(int deptId ,Connection conn,String toId) throws Exception {
    // TODO Auto-generated method stub
    YHORM orm = new YHORM();
    List<YHDepartment> list = new ArrayList();
    List<YHDepartment> listNew = new ArrayList();
    Map filters = new HashMap();
    filters.put("DEPT_PARENT", deptId);
    list  = orm.loadListSingle(conn ,YHDepartment.class , filters);
    if(!"0".equals(toId)&&!"ALL_DEPT".equals(toId)){//判断是不是全体部门
      for(int i=0;i<list.size();i++) {
        boolean temp = false;
        YHDepartment dept = list.get(i);
        if(toId != null && !"".equals(toId)){
          String[] toIds = toId.split(",");
          for(int j = 0 ;j < toIds.length ; j++){
            String toIdTemp =  toIds[j];
            if(toIdTemp.equals(Integer.toString(dept.getSeqId()))||childToId(conn,toId,Integer.toString(dept.getSeqId()))==true){
              temp = true;
              break;
            }
          }
        }
        if(temp == true) {
          listNew.add(dept);
        }
      }
    }else{
      listNew = list;
    }
    return listNew;
    
  }
  
  public boolean childToId(Connection conn,String toId,String deptId) throws Exception{
    YHORM orm = new YHORM();
    List<YHDepartment> list = new ArrayList();
    Map filters = new HashMap();
    filters.put("DEPT_PARENT", deptId);
    list  = orm.loadListSingle(conn ,YHDepartment.class , filters);
    boolean result = false;
    for(int i=0;i<list.size();i++) {
      boolean temp = false;
      YHDepartment dept = list.get(i);
      if(toId != null && !"".equals(toId)){
        String[] toIds = toId.split(",");
        for(int j = 0 ;j < toIds.length ; j++){
          String toIdTemp =  toIds[j];
          if(toIdTemp.equals(Integer.toString(dept.getSeqId()))){
            temp = true;
            break;
          }
        }
      }
      boolean temp2 = false;
      temp2 = childToId(conn,toId,Integer.toString(dept.getSeqId()));
      if(temp==true||temp2==true){
        result = true;
      }
    }
   
    
    return result;
  }
  
//没有从toId中取
  public StringBuffer getUserById(YHNews news,Connection conn,int deptIdtemp,StringBuffer sb)throws Exception{
    String unUser = "";
    String userNameStr = "";
//    if("ALL_DEPT".equals(news.getToId())){
      String queryUserSql = "select SEQ_ID,USER_PRIV,USER_NAME from PERSON where DEPT_ID='"+deptIdtemp+"'";
      Statement userSt = conn.createStatement();
      ResultSet userRs = userSt.executeQuery(queryUserSql);
      while(userRs.next()){
        int userId = userRs.getInt("SEQ_ID");
        String userPriv = userRs.getString("USER_PRIV");
        String userName = userRs.getString("USER_NAME");
        if(news.getReaders()!=null&& getReaderNames(conn,news.getReaders(),userName)){
          userNameStr = userNameStr + userName + ",";
//          readCount ++;
        }else{
          if("0".equals(news.getToId())
        	  || findToId(news.getToId(),Integer.toString(deptIdtemp)) == true
        	  || findToId(news.getPrivId(),userPriv) == true
        	  || findToId(news.getUserId(),Integer.toString(userId)) == true){
            unUser = unUser + userName + ",";
 //           unReadCount ++;
          }
        }//end if
      }//end while
 //    } //end if
    sb.append(",userNameStr:\"" +userNameStr + "\"");
    sb.append(",unUser:\"" +unUser + "\"");
    sb.append("},");
    return sb;
  }
  
  /**
   * 
   * @param conn
   * @param reader
   * @param userName
   * @return
   * @throws Exception
   */
  public boolean getReaderNames(Connection conn,String reader,String userName) throws Exception{
    String readerNames = "";
    String queryUserNameStr = "";
    Statement st = null;
    ResultSet rs = null;
    boolean temp = false;
    if(reader != null && !"".equals(reader)){
      String[] readers = reader.split(",");
      reader = "";
      for(int j = 0 ;j < readers.length ; j++){
        reader += readers[j] + ",";
      }
      reader = reader.substring(0, reader.length() - 1);
    }
    if(reader != null && !"".equals(reader)){
      queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in (" + reader + ")";
      st = conn.createStatement();
      rs = st.executeQuery(queryUserNameStr);
      while(rs.next()) {
        readerNames = readerNames + rs.getString("USER_NAME") + ",";
      }
    }
    
    if(readerNames != null && !"".equals(readerNames)){
      String[] readerNamess = readerNames.split(",");
      readerNames = "";
      for(int j = 0 ;j < readerNamess.length ; j++){
        readerNames =  readerNamess[j] ;
        if(readerNames.equals(userName)) {
          temp = true;
          break;
        }
      }
    }
    return temp;
  }
  //查阅情况--------------
  public boolean findToId(String object,String object2){
    boolean temp = false;
    if(object != null && !"".equals(object)){
      String[] toIds = object.split(",");
      for(int j = 0 ;j < toIds.length ; j++){
        String toIdTemp =  toIds[j];
        if(toIdTemp.equals(object2)){
          temp = true;
          break;
        }
      }
    }
    return temp;
  }
  
 
  /**
   * 新闻查询--新闻管理
   * @param conn
   * @param news
   * @param loginUser
   * @param beginDate
   * @param endDate
   * @param clickCountMin
   * @param clickCountMax
   * @param showLen
   * @param pageIndex
   * @return
   * @throws Exception
   */
  public String queryNews(Connection conn, YHNews news,YHPerson loginUser,String beginDate,
                        String endDate,String clickCountMin,String clickCountMax,int showLen,int pageIndex) throws Exception{
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
    String formattemp = news.getFormat();
    if(!"".equals(formattemp))
    querynewsParam = querynewsParam + " and n.FORMAT ='" + formattemp + "'";
    String typeIdtemp = news.getTypeId();
    if(!"".equals(typeIdtemp))
    querynewsParam = querynewsParam + " and n.TYPE_ID ='" + typeIdtemp + "'";
    String publish = news.getPublish();
    if(!"".equals(publish))
    querynewsParam = querynewsParam + " and n.PUBLISH ='" + publish + "'";
    String subjecttemp = news.getSubject();
    if(!"".equals(subjecttemp))
    querynewsParam = querynewsParam + " and n.SUBJECT like '%" + YHDBUtility.escapeLike(subjecttemp) + "%' " + YHDBUtility.escapeLike();
    if(!"".equals(beginDate))
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.NEWS_TIME", beginDate, ">=");//to_char(n.NEWS_TIME,'yyyy-mm-dd')>='" + beginDate + "'";
    if(!"".equals(endDate))
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.NEWS_TIME", YHUtility.getDateTimeStr(YHUtility.getDayAfter(endDate,1)), "<"); //to_char(n.NEWS_TIME,'yyyy-mm-dd')<='" + endDate + "'";
    String content = news.getContent(); 
    if(!"".equals(content))
    querynewsParam = querynewsParam + " and n.CONTENT like '%" + YHDBUtility.escapeLike(content) + "%' " + YHDBUtility.escapeLike();
    if(!"".equals(clickCountMin)){
      querynewsParam = querynewsParam + " and n.CLICK_COUNT>=" +  clickCountMin;
    }
    if(!"".equals(clickCountMax)){
      querynewsParam = querynewsParam + " and n.CLICK_COUNT<=" +  clickCountMax;
    }
    if(!"1".equals(loginUser.getUserPriv())) {
      querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_news n,PERSON p,oa_department d where 1=1 and n.PROVIDER=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.PROVIDER='"+loginUser.getSeqId()+"'";
    }else {
      querynewsSql = querynewsSql + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_news n,PERSON p,oa_department d where 1=1 and n.PROVIDER=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
    }
    querynewsSql = querynewsSql + querynewsParam + " order by  n.NEWS_TIME desc";
    //YHOut.println(querynewsSql);
    try {
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      rs = stmt.executeQuery(querynewsSql);
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
     //System.out.println("++++++++++++++++++++++++++++"+pageCount);
     //开始索引
     pgStartRecord = (pageIndex - 1) * showLen + 1;
   
     rs.absolute( (pageIndex - 1) * showLen + 1); 
     for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
        Statement stt = null;
        ResultSet rss = null;
        YHNews newss = new YHNews(); 
        int seqId = rs.getInt("SEQ_ID");//自增ID
        String subject = rs.getString("subject");//新闻标题
        String provider = rs.getString("PROVIDER");//新闻发布人
        Date newsTime = rs.getTimestamp("NEWS_TIME");//新闻发布时间
        int clickCount = rs.getInt("CLICK_COUNT");//点击次数
        String typeId = rs.getString("TYPE_ID");//新闻类型，对应小代码表
        String anonymityYn = rs.getString("ANONYMITY_YN");//评论类型（0-实名评论；1-匿名评论；2-禁止评论）
        String format = rs.getString("FORMAT");//新闻格式（0-普通格式；1-MHT格式；2-超链接）
        String providerName = rs.getString("USER_NAME");
        String deptName = rs.getString("DEPT_NAME");
        news.setSeqId(rs.getInt("SEQ_ID"));
        
        //新闻标题超链接对应的title属性，显示完整的新闻标题
        String subjectTitle = "";
        if(subject.length()>50) {
          subjectTitle = subject;
          subject = subject.substring(0, 50) + "...";
        }
        
        //根据新闻类型字段的值，获取新闻类型的代码描述
        String typeName = "";
        if(typeId!=null){
          if(!"".equals(typeId.trim())&&!"null".equals(typeId)){
            String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="+typeId;
            stt = conn.createStatement(); 
            rss = stt.executeQuery(queryTypeNameStr);
             if(rss.next()) {
               typeName = rss.getString("CLASS_DESC");
             }
          }
      }else{
        typeName = "";
      }
         
        int commentCount = 0;
        String queryCommentCount = "SELECT count(*) from oa_news_comment where NEWS_ID='"+ seqId + "'";
        stt = conn.createStatement();
        rss = stt.executeQuery(queryCommentCount);
        if(rss.next()){
          commentCount = rss.getInt(1);
        }
        
      sb.append("{");
      sb.append("seqId:" + rs.getInt("SEQ_ID"));
      sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle) + "\"");
      sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
      sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
      sb.append(",providerName:\"" + YHUtility.encodeSpecial(providerName) + "\"");
      sb.append(",newsTime:\"" + newsTime + "\"");
      sb.append(",clickCount:\"" + clickCount + "\"");
      sb.append(",commentCount:\"" + commentCount + "\"");
      sb.append(",anonymityYn:\"" + anonymityYn + "\"");
      sb.append(",deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"");
      sb.append("},");
      newsManagerList.add(news);
        rs.next(); 
      }
     //结束索引
     pgEndRecord =(pageIndex - 1) * showLen + newsManagerList.size();
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
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
//    if(newsManagerList.size()>0) {
//      sb.deleteCharAt(sb.length() - 1); 
//      }
    //YHOut.println(sb.toString()+"**********************");
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
  public boolean deleteAllnews(Connection dbConn,String loginUserId,String loginUserPriv,String postPriv) throws IOException, Exception {
    String queryAttachlSql = "";
    String deletenewsSql = "";
    String deleteSmsSql = "";
    Statement stmt = null;
    ResultSet rs = null;
    String attachmentId = "";
    String attachmentName = "";
    if("1".equals(loginUserPriv)||"1".equals(postPriv)) {
      queryAttachlSql = "select ATTACHMENT_ID,ATTACHMENT_NAME from oa_news where " + YHDBUtility.getDateFilter("ATTACHMENT_NAME", "", "=");//to_char(ATTACHMENT_NAME)!=''";
    }else {
      queryAttachlSql = "select ATTACHMENT_ID,ATTACHMENT_NAME from oa_news where "+ YHDBUtility.getDateFilter("ATTACHMENT_NAME", "", "=") +" and PROVIDER='"+loginUserId+"'";
    }
    
    stmt = dbConn.createStatement();
    rs = stmt.executeQuery(queryAttachlSql);
    //YHOut.println(queryAttachlSql+"&&&&&&&&&&&&&&&&&&&");
    while(rs.next()) {
      attachmentId = rs.getString("ATTACHMENT_ID");
      attachmentName = rs.getString("ATTACHMENT_NAME");
    }
    if("1".equals(loginUserPriv)||"1".equals(postPriv)) {
      deletenewsSql = "delete from oa_news";
    }else {
      deletenewsSql = "delete from oa_news where PROVIDER='"+ loginUserId + "'";
    }
    boolean success = false;
    try {
      stmt = dbConn.createStatement();
      success = stmt.execute(deletenewsSql);
    } catch (Exception e) {
      throw new Exception();
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    if("1".equals(loginUserPriv)) {
      deleteSmsSql = "";
    }else {
      deleteSmsSql = "'" + loginUserId + "'";
    }
//    stmt = dbConn.createStatement();
//    stmt.execute(deleteSmsSql);
    return success;
  }
  
  
  /**
   * 删除所选的新闻 
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   * @throws Exception 
   * @TODO 删除附件
   */
  public boolean deleteChecknews(Connection dbConn,String loginUserId,String loginUserPriv,String postPriv,String deleteStr) throws IOException, Exception {
    String querynewsSql = "";
    String deletenewsCommentSql = "";
    String deletenewsSql = "";
    String deleteSmsSql = "";
    Statement stmt = null;
    ResultSet rs = null;
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
    try {
      querynewsSql = "select SEQ_ID,ATTACHMENT_ID,ATTACHMENT_NAME,SUBJECT from oa_news where SEQ_ID in ("+ deleteStr +")";
      if(!"1".equals(loginUserPriv)) {
        querynewsSql = querynewsSql + " and PROVIDER='" + loginUserId + "'";
      }
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(querynewsSql);
      while(rs.next()) {
        attachmentId = rs.getString("ATTACHMENT_ID");
        attachmentName = rs.getString("ATTACHMENT_NAME");
        //删除消息
        //删除附件
        subject = rs.getString("SUBJECT");
        String remark = "删除新闻，标题："+ subject;
      }

      deletenewsCommentSql = "delete  from oa_news_comment  where SEQ_ID in  ("+ deleteStr + ")";
      stmt = dbConn.createStatement();
      success = stmt.execute(deletenewsCommentSql);
  
      deletenewsSql = "delete from oa_news where SEQ_ID in ("+  deleteStr + ")";
      stmt = dbConn.createStatement();
      stmt.execute(deletenewsSql);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return success;
  }
  /**
   * 处理上传附件，返回附件id，附件名称

  //点击单文件上传时调用的方法
   * @param request  HttpServletRequest
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
        String fileName = fileForm.getFileName(fieldName).replaceAll("\\'", "");
        String fileNameV = fileName;
        //YHOut.println(fileName+"*************"+fileNameV);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        
        while (YHDiaryUtil.getExist(filePath + File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, filePath + File.separator + YHNewsCont.MODULE + File.separator + hard + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  
  //文件柜添加附件，点击，发布。保存。时调用的方法
  public Map<String, String> fileUploadLogic2(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    YHSelAttachUtil selA = new YHSelAttachUtil(fileForm, YHNewsCont.MODULE);
    result.putAll(selA.getAttachInFo());
    return result;
  }
  
  /**暂时没用处理多文件上传
   * 附件批量上传页面处理
   * @return
  * @throws Exception 
   */
  public StringBuffer uploadMsrg2Json( YHFileUploadForm fileForm,String pathP) throws Exception{
    StringBuffer sb = new StringBuffer();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try{    
      attr = fileUploadLogic(fileForm, pathP);
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
  //多文件上传时用到的----
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
      YHNews news = (YHNews)orm.loadObjSingle(dbConn, YHNews.class, seqId);
      String[] attIdArray = {};
      String[] attNameArray = {};
      String attachmentId = news.getAttachmentId();
      String attachmentName = news.getAttachmentName();
      //YHOut.println("attachmentId"+attachmentId+"--------attachmentName"+attachmentName);
      if (!"".equals(attachmentId.trim()) && attachmentId != null && attachmentName != null) {
        attIdArray = attachmentId.trim().split(",");
        attNameArray = attachmentName.trim().split("\\*");
      }
      String attaId = "";
      String attaName = "";
  
      for (int i = 0; i < attIdArray.length; i++) {
        if (attId.equals(attIdArray[i])) {
          continue;
        }
        attaId += attIdArray[i] + ",";
        attaName += attNameArray[i] + "*";
      }
      //YHOut.println("attaId=="+attaId+"--------attaName=="+attaName);
      news.setAttachmentId(attaId.trim());
      news.setAttachmentName(attaName.trim());
      orm.updateSingle(dbConn, news);
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
  
  /**
   * 通过查询删除查询
   * @param conn
   * @param news
   * @param loginUser
   * @param beginDate
   * @param endDate
   * @param clickCountMin
   * @param clickCountMax
   * @param showLen
   * @param pageIndex
   * @return
   * @throws Exception
   */
  public String deleteNewBySel(Connection conn, YHNews news, YHPerson loginUser, String beginDate, String endDate, String clickCountMin, String clickCountMax) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String querynewsParam = "";
    String querynewsSql = "";
   
    List<Integer> newsManagerList = new ArrayList<Integer>();
  
    String formattemp = news.getFormat();
    if (!"".equals(formattemp))
      querynewsParam = querynewsParam + " and n.FORMAT ='" + formattemp + "'";
    String typeIdtemp = news.getTypeId();
    if (!"".equals(typeIdtemp))
      querynewsParam = querynewsParam + " and n.TYPE_ID ='" + typeIdtemp + "'";
    String publish = news.getPublish();
    if (!"".equals(publish))
      querynewsParam = querynewsParam + " and n.PUBLISH ='" + publish + "'";
    String subjecttemp = news.getSubject();
    if (!"".equals(subjecttemp))
      querynewsParam = querynewsParam + " and n.SUBJECT like '%" + YHDBUtility.escapeLike(subjecttemp)+ "%' " + YHDBUtility.escapeLike();
    if (!"".equals(beginDate))
      querynewsParam = querynewsParam
          + " and " + YHDBUtility.getDateFilter("n.NEWS_TIME", beginDate, ">=");//to_char(n.NEWS_TIME,'yyyy-mm-dd')>='" + beginDate + "'";
    if (!"".equals(endDate))
      querynewsParam = querynewsParam
          + " and " + YHDBUtility.getDateFilter("n.NEWS_TIME", YHUtility.getDateTimeStr(YHUtility.getDayAfter(endDate,1)), "<");//to_char(n.NEWS_TIME,'yyyy-mm-dd')<='" + endDate + "'";
    String content = news.getContent();
    if (!"".equals(content))
      querynewsParam = querynewsParam + " and n.CONTENT like '%" + YHDBUtility.escapeLike(content) + "%' "+ YHDBUtility.escapeLike();
    if (!"".equals(clickCountMin)){
      querynewsParam = querynewsParam + " and n.CLICK_COUNT>=" + clickCountMin;
    }
    if (!"".equals(clickCountMax)){
      querynewsParam = querynewsParam + " and n.CLICK_COUNT<=" + clickCountMax;
    }
    if (!"1".equals(loginUser.getUserPriv())){
      querynewsSql = querynewsSql
          + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_news n,PERSON p,oa_department d where 1=1 and n.PROVIDER=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.PROVIDER='"
          + loginUser.getSeqId() + "'";
    } else{
      querynewsSql = querynewsSql
          + "SELECT p.USER_NAME,d.DEPT_NAME,n.* from oa_news n,PERSON p,oa_department d where 1=1 and n.PROVIDER=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID";
    }
    querynewsSql = querynewsSql + querynewsParam
        + " order by  n.NEWS_TIME desc";
     //YHOut.println(querynewsSql);
    try{
      stmt = conn.createStatement();
      rs = stmt.executeQuery(querynewsSql); 
      while(rs.next()){
       newsManagerList.add(rs.getInt("SEQ_ID"));
      }
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(stmt, rs, log);
    }  
    return toAstring(newsManagerList);
  }
  
  public String toAstring(List<Integer> ids){
    String newIds = "";
    if(ids!=null && ids.size()>0){
      delCount(ids.size());
      for(int i=0; i<ids.size(); i++){
        newIds += ids.get(i)+",";
      }
    }else{
      delCount(0);
    }
    return newIds.substring(0, newIds.lastIndexOf(",")==-1?0:newIds.lastIndexOf(","));
  }
  
  /**
   * 获得删除了多少条记录
   * @return
   */
  private int count = 0;
  public void delCount(int count){
    this.count = count;
  }
  
  public int getCount(){
    return this.count;
  }
  
  /**
   * 删除id串范围内的新闻
   * @param dbConn
   * @param ids
   * @throws Exception
   */
  public void deleteSelNew(Connection dbConn, String ids) throws Exception {
    PreparedStatement ps = null;    
    String sql = "delete from news where SEQ_ID in("+ ids +")";
      
      try{
        ps = dbConn.prepareStatement(sql);
        int k = ps.executeUpdate(); 
      } catch (SQLException e){
        throw e;
      }finally{
      YHDBUtility.close(ps, null, log);
      }
  }
  
  public void deleteSelNew(Connection conn, YHNews news, YHPerson loginUser, String beginDate, String endDate, 
                            String clickCountMin, String clickCountMax) throws Exception{
    String ids = deleteNewBySel(conn, news, loginUser, beginDate, endDate, clickCountMin, clickCountMax);
    if(ids != ""){
      deleteSelNew(conn, ids); 
    }   
  }

}

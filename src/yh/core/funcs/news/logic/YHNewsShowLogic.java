package yh.core.funcs.news.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.news.data.YHNews;
import yh.core.funcs.news.data.YHNewsComment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
/**
 * 办公桌--新闻
 * @author qwx110
 *
 */
public class YHNewsShowLogic {
  private static Logger log = Logger.getLogger(YHNewsShowLogic.class);
  private YHNewsManageUtilLogic newsManageUtil = new YHNewsManageUtilLogic();
  private YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
 
  /*点击标题进入查看回复页面
   * 查询person表里要设置权限的人，满足以下条件的用户都查出来
   *   不是系统管理员的（他不用设置）
   *   应用到其他用户--“所属角色”里指定的角色相关用户（注："所在部门"条件在while循环里再加，这里可能不好加）
   *   当前正在编辑的用户
   */
  public String getnewsShowList(Connection conn,YHPerson loginUser,int showLen,int pageIndex,String seqId)throws Exception {
    Date currentDate = new Date();
    YHORM orm = new YHORM();
    Statement stmt = null;
    ResultSet rs = null;
    int pageCount = 0;//页码数
    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    int pgEndRecord = 0;//结束索引
    String userPriv = loginUser.getUserPriv();
    String userSeqId = Integer.toString(loginUser.getSeqId());
    List newsShowList = new ArrayList();
    StringBuffer sb  = new StringBuffer();
    int commentCount = 0; 
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    String userLoginName = "";
    String loginNickName = "";
    String userDeptName = "";
    YHNews news = null;
    try {
      news = (YHNews)orm.loadObjSingle(conn, YHNews.class, Integer.parseInt(seqId));
      String queryUserNameSql = "SELECT p.USER_NAME,p.NICK_NAME,d.DEPT_NAME from PERSON p,oa_department d where p.DEPT_ID=d.SEQ_ID and p.SEQ_ID='" + news.getProvider() + "'";
      stmt = conn.createStatement();
      rs =stmt.executeQuery(queryUserNameSql);
      if(rs.next()) {
        userLoginName = rs.getString("USER_NAME");
        loginNickName = rs.getString("NICK_NAME");
        userDeptName = rs.getString("DEPT_NAME");
      }
      String updateSql = "";
      if (news != null && !YHUtility.null2Empty(news.getFormat()).equals("2")) {
        byte[] byteContent = news.getCompressContent();
        if (byteContent == null) {
          news.setContent("");
        }else {
          news.setContent(new String(byteContent, "UTF-8"));
        }
      }
      //根据新闻类型字段的值，获取新闻类型的代码描述      String typeName = "";
      String typeId = news.getTypeId();
      if(typeId!=null){
        if(!"".equals(typeId.trim())&&!"null".equals(typeId)){
          String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="+typeId;
          stmt = conn.createStatement(); 
          rs = stmt.executeQuery(queryTypeNameStr);
           if(rs.next()) {
             typeName = rs.getString("CLASS_DESC");
           }
        }
    }else{
      typeName = "";
    }
      if("".equals(news.getContent())) {
        news.setContent("<br>见附件");
      }
      String readers = news.getReaders();
      if(readers==null){
        readers = "";
      }
      int clickCount = news.getClickCount();
      clickCount ++;
      boolean contains = false;
      if(readers != null && !"".equals(readers)){
        String[] readerList = readers.split(",");
        for(int j = 0 ;j < readerList.length ; j++){
          String reader = readerList[j];
          if(reader.equals(Integer.toString(loginUser.getSeqId()))){
            contains = true;
            break;
          }
        }
      }
      if(!contains){
        readers = readers + loginUser.getSeqId() + ",";
        updateSql = "update oa_news set READERS='"+ readers + "',CLICK_COUNT='" + clickCount + "' where SEQ_ID='" + seqId + "'";
      }else {
        updateSql = "update oa_news set CLICK_COUNT='" + clickCount + "' where SEQ_ID='" + seqId + "'";
      }
      stmt = conn.createStatement();
      stmt.execute(updateSql);
      
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    sb.append("{");
    sb.append("listData:[");
    try{   
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      String queryNewsSql = "SELECT * from oa_news_comment where NEWS_ID='"+seqId+ "' order by RE_TIME desc";
      //YHOut.println(queryNewsSql);
      rs = stmt.executeQuery(queryNewsSql); 
      rs.last(); 
      recordCount = rs.getRow(); //总记录数 
      commentCount = recordCount;
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
    
     pgStartRecord = (pageIndex - 1) * showLen + 1;
   
     rs.absolute( (pageIndex - 1) * showLen + 1); 
     int temp = 0;
     for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
       YHNewsComment newsComment = new YHNewsComment(); 
       Statement stmtt = null;
       ResultSet rss = null;
       String toNameTitle = "";
       String toNameStr = "";
       String subjectTitle = "";
       String  publishDesc= "";
       String deptId = "";
       String deptName = "";
       String userName = "";
       String content1 = "";
       int relayCount = 0;
       int sqlcommentId = rs.getInt("SEQ_ID");
       int parentId = rs.getInt("PARENT_ID");
       String content = rs.getString("CONTENT");
       Date reTime = rs.getTimestamp("RE_TIME");
       String userId = rs.getString("USER_ID");
       String nickName = rs.getString("NICK_NAME");
      
       if("".equals(nickName)||nickName==null) {
         try {
          String queryUserSql = "SELECT u.USER_NAME,u.DEPT_ID,d.DEPT_NAME from PERSON u,oa_department d where u.DEPT_ID=d.SEQ_ID and u.SEQ_ID='"+ userId +"'";
           stmtt = conn.createStatement();
           rss = stmtt.executeQuery(queryUserSql);
           if(rss.next()) {
             deptName = rss.getString("DEPT_NAME");
             userName = "<u title='部门："+deptName+"' style='cursor:hand'>"+ rss.getString("USER_NAME") + "</u>";
           }
        } catch (Exception e) {
          e.printStackTrace();
        }
       }else {
         userName = nickName;
       }
       String queryChildSql = "SELECT CONTENT from oa_news_comment where SEQ_ID='" + parentId  +"'";
       stmtt = conn.createStatement();
       rss = stmtt.executeQuery(queryChildSql);
       if(rss.next()) {
         content1 = rss.getString("CONTENT");
       } 
       String queryChildCountSql = "SELECT count(*) from oa_news_comment where PARENT_ID='" + sqlcommentId + "'";
       stmtt = conn.createStatement();
       rss = stmtt.executeQuery(queryChildCountSql);
       if(rss.next()) {
         relayCount = rss.getInt(1);
       } 
       newsComment.setSeqId(sqlcommentId);
       
       sb.append("{");
       sb.append("sqlcommentId:" + sqlcommentId);
       sb.append(",content:\"" + YHUtility.encodeSpecial(content) + "\"");
       sb.append(",content1:\"" + YHUtility.encodeSpecial(content1) + "\"");
       sb.append(",relayCount:\"" + relayCount + "\"");
       sb.append(",userId:\"" + userId + "\"");
       sb.append(",parentId:\"" + parentId + "\"");
       sb.append(",userName:\"" + YHUtility.encodeSpecial(userName) + "\"");
       sb.append(",reTime:\"" + reTime + "\"");
       sb.append("},");
       
       newsShowList.add(newsComment);
       rs.next(); 
    } 
     //结束索引
     pgEndRecord =(pageIndex - 1) * showLen + newsShowList.size();
     if(newsShowList.size()>0) {
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
     sb.append(",subject:\"" + YHUtility.encodeSpecial(news.getSubject()) + "\"");
     sb.append(",newsId:\"" + news.getSeqId() + "\"");
     if(!"".equals(news.getAttachmentId().trim())&&news.getAttachmentId().trim()!=null){
       sb.append(",attachFile:\"" + news.getAttachmentId().replaceAll("[\\w]+_", "")+"_"+news.getAttachmentName().substring(0, news.getAttachmentName().length()-1) + "\"");
    }
     sb.append(",attachmentId:\"" + news.getAttachmentId() + "\"");
     sb.append(",attachmentName:\"" + YHUtility.encodeSpecial(news.getAttachmentName()) + "\"");
     sb.append(",ym:\"" + hard + "\"");
     sb.append(",newsTime:\"" + news.getNewsTime() + "\"");
     sb.append(",clickCount:\"" + news.getClickCount() + "\"");
     sb.append(",content:\"" + YHUtility.encodeSpecial(news.getContent()) + "\"");
     sb.append(",format:\"" + news.getFormat() + "\"");
     sb.append(",anonymityYn:\"" + news.getAnonymityYn() + "\"");
     sb.append(",publish:\"" + news.getPublish() + "\"");
     sb.append(",commentCount:\"" + commentCount + "\"");
     sb.append(",userSeqId:\"" + userSeqId + "\"");
     sb.append(",userLoginName:\"" + YHUtility.encodeSpecial(userLoginName) + "\"");
     sb.append(",loginNickName:\"" + YHUtility.encodeSpecial(loginNickName) + "\"");
     sb.append(",userDeptName:\"" + YHUtility.encodeSpecial(userDeptName) + "\"");
     sb.append(",subjectFont:\"" + news.getSubjectFont() + "\"");
     sb.append("}");
//     returnMap.put("listData", newsManagerList);
//     returnMap.put("pageData", sb.toString());
     //YHOut.println(sb.toString());
     return sb.toString();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /*点击我的办公桌评论进入的方法
   * 查询person表里要设置权限的人，满足以下条件的用户都查出来
   *   不是系统管理员的（他不用设置）
   *   应用到其他用户--“所属角色”里指定的角色相关用户（注："所在部门"条件在while循环里再加，这里可能不好加）
   *   当前正在编辑的用户
   */
  public String getnewsManagerList(Connection conn,YHPerson loginUser,int showLen,String subject,String format,
                                String anonymityYn,int pageIndex,int seqId)throws Exception {
    Date currentDate = new Date();
    YHORM orm = new YHORM();
    Statement stmt = null;
    ResultSet rs = null;
    int pageCount = 0;//页码数
    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    int pgEndRecord = 0;//结束索引
    String userPriv = loginUser.getUserPriv();
    String userSeqId = Integer.toString(loginUser.getSeqId());
    List newsShowList = new ArrayList();
    StringBuffer sb  = new StringBuffer();
    int commentCount = 0; 
    String userLoginName = loginUser.getUserName();
    String loginNickName = loginUser.getNickName();
    String userDeptName = "";
    YHNews news = null;
    sb.append("{");
    sb.append("listData:[");
    try{   
      news = (YHNews)orm.loadObjSingle(conn,YHNews.class,seqId);
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      String queryNewsSql = "SELECT * from oa_news_comment where NEWS_ID=" + seqId + " order by seq_id desc";
      rs = stmt.executeQuery(queryNewsSql); 
      rs.last(); 
      recordCount = rs.getRow(); //总记录数 
      commentCount = recordCount;
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
     int temp = 0;
     for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
       YHNewsComment newsComment = new YHNewsComment(); 
       Statement stmtt = null;
       ResultSet rss = null;
       String toNameTitle = "";
       String toNameStr = "";
       String subjectTitle = "";
       String  publishDesc= "";
       String deptId = "";
       String deptName = "";
       String userName = "";
       String content1 = "";
       int relayCount = 0;
       int sqlcommentId = rs.getInt("SEQ_ID");
       int parentId = rs.getInt("PARENT_ID");
       String content = YHUtility.encodeSpecial(rs.getString("CONTENT"));
       Date reTime = rs.getTimestamp("RE_TIME");
       String userId = rs.getString("USER_ID");
       String nickName = YHUtility.encodeSpecial(rs.getString("NICK_NAME"));
      
       if("".equals(nickName)||nickName==null) {
         try {
          String queryUserSql = "SELECT u.USER_NAME,u.DEPT_ID,d.DEPT_NAME from PERSON u,oa_department d where u.DEPT_ID=d.SEQ_ID and u.SEQ_ID='"+ userId +"'";
           stmtt = conn.createStatement();
           rss = stmtt.executeQuery(queryUserSql);
           if(rss.next()) {
             deptName = YHUtility.encodeSpecial(rss.getString("DEPT_NAME"));
             userName = "<u title='部门："+deptName+"' style='cursor:hand'>"+ YHUtility.encodeSpecial(rss.getString("USER_NAME")) + "</u>";
           }
        } catch (Exception e) {
          e.printStackTrace();
        }
       }else {
         userName = nickName;
       }
       String queryChildSql = "SELECT CONTENT from oa_news_comment where SEQ_ID='" +   parentId  +"' order by seq_id desc";
       stmtt = conn.createStatement();
       rss = stmtt.executeQuery(queryChildSql);
       if(rss.next()) {
         content1 = YHUtility.encodeSpecial(rss.getString("CONTENT"));
       } 
       String queryChildCountSql = "SELECT count(*) from oa_news_comment where PARENT_ID='" + sqlcommentId + "'";
       stmtt = conn.createStatement();
       rss = stmtt.executeQuery(queryChildCountSql);
       if(rss.next()) {
         relayCount = rss.getInt(1);
       } 
       newsComment.setSeqId(sqlcommentId);   
       sb.append("{");
       sb.append("sqlcommentId:" + sqlcommentId);
       sb.append(",content:\"" + (content) + "\"");
       sb.append(",content1:\"" + (content1) + "\"");
       sb.append(",relayCount:\"" + relayCount + "\"");
       sb.append(",userId:\"" + userId + "\"");
       sb.append(",parentId:\"" + parentId + "\"");
       sb.append(",userName:\"" + (userName) + "\"");
       sb.append(",reTime:\"" + reTime + "\"");
       sb.append("},");
       //YHOut.println(sb.toString());
       newsShowList.add(newsComment);
       rs.next(); 
    } 
     //结束索引
     pgEndRecord =(pageIndex - 1) * showLen + newsShowList.size();
     if(newsShowList.size()>0) {
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
     sb.append(",format:\"" + format + "\"");
     sb.append(",subject:\"" + YHUtility.encodeSpecial(news.getSubject()) + "\"");
     sb.append(",publish:\"" + news.getPublish() + "\"");
     sb.append(",anonymityYn:\"" + anonymityYn + "\"");
     sb.append(",commentCount:\"" + commentCount + "\"");
     sb.append(",userSeqId:\"" + userSeqId + "\"");
     sb.append(",newsId:\"" + seqId + "\"");
     sb.append(",userLoginName:\"" + YHUtility.encodeSpecial(userLoginName) + "\"");
     sb.append(",loginNickName:\"" + YHUtility.encodeSpecial(loginNickName) + "\"");
     sb.append(",userDeptName:\"" + YHUtility.encodeSpecial(userDeptName) + "\"");
     sb.append("}");
//     returnMap.put("listData", newsManagerList);
//     returnMap.put("pageData", sb.toString());
     //YHOut.println(sb.toString());
     return sb.toString();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  //我的办供桌的未读新闻
  public String getnewsNoReadList(Connection conn,YHPerson loginUser,int showLen,String type,String ascDesc,String field, 
                                  int pageIndex)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    boolean temp = false;//判断是否阅读过
    int count = 0;//查询出来的记录数
    int pageCount = 0;//页码数
    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    int pgEndRecord = 0;//结束索引
    int loginDeptId = loginUser.getDeptId();
    String userPriv = loginUser.getUserPriv();
    int seqUserId = loginUser.getSeqId();
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    StringBuffer sb  = new StringBuffer();
    sb.append("{");
    sb.append("listData:[");
    try{
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      String queryNotifySql = null;
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        queryNotifySql = "SELECT SEQ_ID,TO_ID,SUBJECT,[TOP],PRIV_ID,USER_ID,READERS,ANONYMITY_YN,"
          +"TYPE_ID,NEWS_TIME,CLICK_COUNT,FORMAT from oa_news where PUBLISH='1' and (" 
          + YHDBUtility.findInSet(Integer.toString(loginDeptId), "TO_ID") 
          + " or " + YHDBUtility.findInSet(userPriv,"PRIV_ID") +" or " 
          + YHDBUtility.findInSet(Integer.toString(seqUserId),"USER_ID") + " or "
          + YHDBUtility.findInSet("0", "TO_ID")  +") "
          + " and ("+  YHDBUtility.findNoInSet(Integer.toString(seqUserId),"READERS")+" or READERS is null) and NEWS_TIME <= " + YHDBUtility.currDateTime();
      }else {
        queryNotifySql = "SELECT SEQ_ID,TO_ID,SUBJECT,TOP,PRIV_ID,USER_ID,READERS,ANONYMITY_YN,"
          +"TYPE_ID,NEWS_TIME,CLICK_COUNT,FORMAT from oa_news where PUBLISH='1' and (" 
          + YHDBUtility.findInSet(Integer.toString(loginDeptId), "TO_ID") 
          + " or " + YHDBUtility.findInSet(userPriv,"PRIV_ID") +" or " 
          + YHDBUtility.findInSet(Integer.toString(seqUserId),"USER_ID") + " or "
          + YHDBUtility.findInSet("0", "TO_ID")  +") "
          + " and ("+  YHDBUtility.findNoInSet(Integer.toString(seqUserId),"READERS")+" or READERS is null) and NEWS_TIME <= " + YHDBUtility.currDateTime();
      }
      //YHOut.println(queryNotifySql);
      if("".equals(type)){//选择“无类型“
        queryNotifySql = queryNotifySql + " and (TYPE_ID='' or TYPE_ID=' ' or TYPE_ID is null)";
      }else if(!"0".equals(type)) {//选择的不是”所有类型“
        queryNotifySql = queryNotifySql + " and (TYPE_ID='"+ type + "')";
      }
  
      if("".equals(field)) {
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifySql = queryNotifySql + " order by [TOP] desc,NEWS_TIME desc";
        }else {
          queryNotifySql = queryNotifySql + " order by TOP desc,NEWS_TIME desc";
        }
        
      }else {
        queryNotifySql = queryNotifySql + " order by " + field;
        if("1".equals(ascDesc)) {
          queryNotifySql = queryNotifySql + " desc";
        }else {
          queryNotifySql = queryNotifySql + " asc";
        }
      }
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      rs = stmt.executeQuery(queryNotifySql);
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
     for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
          count++;
          Statement stmtt = null;
          ResultSet rss = null;
          String toNameTitle = "";
          String toNameStr = "";
          String subjectTitle = "";
          String publishDesc = "";
          String typeName = "";  
          int seqId = rs.getInt("SEQ_ID");
          String fromName = "";
          String deptName = "";
          String subject = rs.getString("SUBJECT");
          String top = rs.getString("TOP");
          String typeId = rs.getString("TYPE_ID");
          Date newsTime = rs.getTimestamp("NEWS_TIME");
          int clickCount = rs.getInt("CLICK_COUNT");
          String format = rs.getString("FORMAT");
          String anonymityYn = rs.getString("ANONYMITY_YN");
          String readers = rs.getString("READERS");
          if(subject.length()>50) {
            subjectTitle = subject;
            subject = subject.substring(0, 50) + "...";
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
          int commentCount = 0;
          String queryCountSql = "SELECT count(*) from oa_news_comment where NEWS_ID='" + seqId + "'";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryCountSql);
          if(rss.next()) {
            commentCount = rss.getInt(1);
          } 
          
          if("1".equals(top)){
            subject = "<font color=red><b>" + subject + "</b></font>";
          }
          sb.append("{");
          sb.append("seqId:" + seqId);
          sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
          sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle) + "\"");
          sb.append(",newsTime:\"" + newsTime + "\"");
          sb.append(",clickCount:\"" + clickCount + "\"");
          sb.append(",format:\"" + format + "\"");
          sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
          sb.append(",commentCount:\"" + commentCount + "\"");
          sb.append(",anonymityYn:\"" + anonymityYn + "\"");
          sb.append("},");
          rs.next();
        }

         //结束索引
         pgEndRecord =(pageIndex - 1) * showLen + count;
         if(count>0) {
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
    // TODO Auto-generated catch block
    e.printStackTrace();
    } finally {
    YHDBUtility.close(stmt, rs, log);
  }
    return sb.toString();
  }
  
  //我的办公桌的全部新闻
  public String getnewsAllList(Connection conn,YHPerson loginUser,int showLen,String type,String ascDesc,String field, 
      int pageIndex,String sendTimetemp)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    boolean temp = false;
    int count = 0;
    int pageCount = 0;//页码数
    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    int pgEndRecord = 0;//结束索引
    int loginDeptId = loginUser.getDeptId();
    String userPriv = loginUser.getUserPriv();
    int seqUserId = loginUser.getSeqId();
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    StringBuffer sb  = new StringBuffer();
    sb.append("{");
    sb.append("listData:[");
    try{
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      String queryNotifySql = null;
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        queryNotifySql = "SELECT SEQ_ID,TO_ID,SUBJECT,[TOP],PRIV_ID,USER_ID,READERS,ANONYMITY_YN,"
          +"TYPE_ID,NEWS_TIME,CLICK_COUNT,FORMAT from oa_news where PUBLISH='1'and ("
          + YHDBUtility.findInSet(Integer.toString(loginDeptId), "TO_ID")
          + " or " + YHDBUtility.findInSet(userPriv,"PRIV_ID") +" or " 
          + YHDBUtility.findInSet(Integer.toString(seqUserId),"USER_ID") + " or "
          + YHDBUtility.findInSet("0", "TO_ID") +") and NEWS_TIME <=" +YHDBUtility.currDateTime();
      }else {
        queryNotifySql = "SELECT SEQ_ID,TO_ID,SUBJECT,TOP,PRIV_ID,USER_ID,READERS,ANONYMITY_YN,"
          +"TYPE_ID,NEWS_TIME,CLICK_COUNT,FORMAT from oa_news where PUBLISH='1'and ("
          + YHDBUtility.findInSet(Integer.toString(loginDeptId), "TO_ID")
          + " or " + YHDBUtility.findInSet(userPriv,"PRIV_ID") +" or " 
          + YHDBUtility.findInSet(Integer.toString(seqUserId),"USER_ID") + " or "
          + YHDBUtility.findInSet("0", "TO_ID") +") and NEWS_TIME <=" +YHDBUtility.currDateTime();
      }
      if("".equals(type)){//选择“无类型“
        queryNotifySql = queryNotifySql + " and (TYPE_ID='' or TYPE_ID=' ' or TYPE_ID is null)";
      }else if(!"0".equals(type)) {//选择的不是”所有类型“
        queryNotifySql = queryNotifySql + " and (TYPE_ID='"+ type + "')";
      }
      if(!"".equals(sendTimetemp)&&sendTimetemp!=null&&!"null".equals(sendTimetemp)){//
        queryNotifySql = queryNotifySql + " and "+ YHDBUtility.getDayFilter("NEWS_TIME", YHUtility.parseDate(sendTimetemp));//to_char(NEWS_TIME,'yyyy-MM-dd')='"+ sendTimetemp + "'";
      }
      if("".equals(field)) {
        if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
          queryNotifySql = queryNotifySql + " order by [TOP] desc,NEWS_TIME desc";
        }else {
          queryNotifySql = queryNotifySql + " order by TOP desc,NEWS_TIME desc";
        }
      }else {
        queryNotifySql = queryNotifySql + " order by " + field;
        if("1".equals(ascDesc)) {
          queryNotifySql = queryNotifySql + " desc";
        }else {
          queryNotifySql = queryNotifySql + " asc";
        }
      }
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(queryNotifySql);
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
//      for (int i = 0; i < showLen && !rs.isAfterLast(); i++) {
     for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) {  
          count ++;
          int isRead = 0;
          Statement stmtt = null;
          ResultSet rss = null;
          String toNameTitle = "";
          String toNameStr = "";
          String subjectTitle = "";
          String publishDesc = "";
          String typeName = "";  
          int seqId = rs.getInt("SEQ_ID");
          String fromName = "";
          String deptName = "";
          String subject = rs.getString("SUBJECT");
          String top = rs.getString("TOP");
          String typeId = rs.getString("TYPE_ID");
          Date newsTime = rs.getTimestamp("NEWS_TIME");
          int clickCount = rs.getInt("CLICK_COUNT");
          String format = rs.getString("FORMAT");
          String anonymityYn = rs.getString("ANONYMITY_YN");
          String reader = rs.getString("READERS");
          if(subject.length()>50) {
            subjectTitle = subject;
            subject = subject.substring(0, 50) + "...";
          }
          if(reader != null && !"".equals(reader)){
            String[] readers = reader.split(",");
            reader = "";
            for(int j = 0 ;j < readers.length ; j++){
              reader = readers[j];
              if(reader.equals(Integer.toString(loginUser.getSeqId()))){
                isRead = 1; 
                break;
              }
            }
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
         
          int commentCount = 0;
          String queryCountSql = "SELECT count(*) from oa_news_comment where NEWS_ID='" + seqId + "'";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryCountSql);
          if(rss.next()) {
            commentCount = rss.getInt(1);
          } 
          
          if("1".equals(top)){
            subject = "<font color=red><b>" + subject + "</b></font>";
          }
          sb.append("{");
          sb.append("seqId:" + seqId);
          sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
          sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle) + "\"");
          sb.append(",newsTime:\"" + newsTime + "\"");
          sb.append(",clickCount:\"" + clickCount + "\"");
          sb.append(",format:\"" + format + "\"");
          sb.append(",isRead:\"" + isRead + "\"");
          sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
          sb.append(",commentCount:\"" + commentCount + "\"");
          sb.append(",anonymityYn:\"" + anonymityYn + "\"");
          sb.append(",iread:\"" + haveRead(conn, loginUser.getSeqId(), seqId) + "\"");
          sb.append("},");
          rs.next();
        }
       //结束索引
       pgEndRecord =(pageIndex - 1) * showLen + count;
       if(count>0) {
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
    // TODO Auto-generated catch block
    e.printStackTrace();
    } finally {
    YHDBUtility.close(stmt, rs, log);
  }
    return sb.toString();
  }
  
  /**
   * 查询新闻
   * @param conn
   * @param news
   * @param loginUser
   * @param beginDatetemp
   * @param endDatetemp
   * @param showLen
   * @param pageIndex
   * @param ascDesc
   * @param field
   * @return
   * @throws Exception
   */
  public String queryNews(Connection conn, YHNews news,YHPerson loginUser,String beginDatetemp,
      String endDatetemp,int showLen,int pageIndex,String ascDesc,String field) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    int temp = 0;
    int pageCount = 0;//页码数
    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    int pgEndRecord = 0;//结束索引
    int loginDeptId = loginUser.getDeptId();
    String userPriv = loginUser.getUserPriv();
    int seqUserId = loginUser.getSeqId();
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    StringBuffer sb  = new StringBuffer();
    sb.append("{");
    sb.append("listData:[");
    String querynewsParam = "";
    String formattemp = news.getFormat();
    if(!"".equals(formattemp))
    querynewsParam = querynewsParam + " and n.FORMAT ='" + formattemp + "'";
    String typeIdtemp = news.getTypeId();
    if("".equals(typeIdtemp)){//选择“无类型“

    	querynewsParam = querynewsParam + " and (n.TYPE_ID='' or n.TYPE_ID=' ' or n.TYPE_ID is null)";
      }else if(!"0".equals(typeIdtemp)) {//选择的不是”所有类型“

    	  querynewsParam = querynewsParam + " and (n.TYPE_ID='"+ typeIdtemp + "')";
      }
    /*if(!"".equals(typeIdtemp)){
    	querynewsParam = querynewsParam + " and n.TYPE_ID ='" + typeIdtemp + "'";
    }else{
    	querynewsParam = querynewsParam + " and (n.TYPE_ID='' or n.TYPE_ID=' ' or n.TYPE_ID is null)";
    }*/
    String providertemp = news.getProvider();
    if(!"".equals(providertemp))
    querynewsParam = querynewsParam + " and n.PROVIDER in (" + providertemp + ")";
    String subjecttemp = news.getSubject();
    if(!"".equals(subjecttemp))
    querynewsParam = querynewsParam + " and n.SUBJECT like '%" + YHDBUtility.escapeLike(subjecttemp) + "%' "+YHDBUtility.escapeLike();
    if(!"".equals(beginDatetemp))
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.NEWS_TIME", beginDatetemp, ">=");//to_char(n.NEWS_TIME,'yyyy-mm-dd')>='" + beginDatetemp + "'";
    if(!"".equals(endDatetemp))
    querynewsParam = querynewsParam + " and " + YHDBUtility.getDateFilter("n.NEWS_TIME", YHUtility.getDateTimeStr(YHUtility.getDayAfter(endDatetemp,1)), "<");//to_char(n.NEWS_TIME,'yyyy-mm-dd')<='" + endDatetemp + "'";
    String content = news.getContent(); 
    if(!"".equals(content))
    querynewsParam = querynewsParam + " and n.CONTENT like '%" + YHDBUtility.escapeLike(content) + "%' "+YHDBUtility.escapeLike();
    if("".equals(field)) {
      querynewsParam = querynewsParam + " order by n.NEWS_TIME desc";
    }else {
      querynewsParam = querynewsParam + " order by " + field;
      if("1".equals(ascDesc)) {
        querynewsParam = querynewsParam + " desc";
      }else {
        querynewsParam = querynewsParam + " asc";
      }
    }
    try{
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      String querynewsSql = null;
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        querynewsSql = "SELECT n.SEQ_ID,n.TO_ID,n.SUBJECT,n.[TOP],n.PRIV_ID,n.USER_ID,n.READERS,n.ANONYMITY_YN,"
          +"n.TYPE_ID,n.NEWS_TIME,n.CLICK_COUNT,n.FORMAT from oa_news n where n.PUBLISH='1' and ("
         + YHDBUtility.findInSet(Integer.toString(loginDeptId), "n.TO_ID")
         + " or " + YHDBUtility.findInSet(userPriv,"n.PRIV_ID") +" or " 
         + YHDBUtility.findInSet(Integer.toString(seqUserId),"n.USER_ID") + " or "
         + YHDBUtility.findInSet("0", "n.TO_ID") +") ";
      }else {
        querynewsSql = "SELECT n.SEQ_ID,n.TO_ID,n.SUBJECT,n.TOP,n.PRIV_ID,n.USER_ID,n.READERS,n.ANONYMITY_YN,"
          +"n.TYPE_ID,n.NEWS_TIME,n.CLICK_COUNT,n.FORMAT from oa_news n where n.PUBLISH='1' and ("
         + YHDBUtility.findInSet(Integer.toString(loginDeptId), "n.TO_ID")
         + " or " + YHDBUtility.findInSet(userPriv,"n.PRIV_ID") +" or " 
         + YHDBUtility.findInSet(Integer.toString(seqUserId),"n.USER_ID") + " or "
         + YHDBUtility.findInSet("0", "n.TO_ID") +") ";
      }
        String querySql = querynewsSql + querynewsParam;
        //YHOut.println(querySql);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(querySql);
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
       for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) {
          int isRead = 0;
          temp ++;
          Statement stmtt = null;
          ResultSet rss = null;
          String toNameTitle = "";
          String toNameStr = "";
          String subjectTitle = "";
          String publishDesc = "";
          String typeName = "";  
          int seqId = rs.getInt("SEQ_ID");
          String fromName = "";
          String deptName = "";
          String subject = rs.getString("SUBJECT");
          String top = rs.getString("TOP");
          String typeId = rs.getString("TYPE_ID");
          Date newsTime = rs.getTimestamp("NEWS_TIME");
          int clickCount = rs.getInt("CLICK_COUNT");
          String format = rs.getString("FORMAT");
          String anonymityYn = rs.getString("ANONYMITY_YN");
          String reader = rs.getString("READERS");
          if(subject.length()>50) {
            subjectTitle = subject;
            subject = subject.substring(0, 50) + "...";
          }
          if(reader != null && !"".equals(reader)){
            String[] readers = reader.split(",");
            reader = "";
            for(int j = 0 ;j < readers.length ; j++){
              reader = readers[j];
              if(reader.equals(Integer.toString(loginUser.getSeqId()))){
                isRead = 1; 
                break;
              }
            }
          }
          //根据新闻类型字段的值，获取新闻类型的代码描述          if(typeId!=null){
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
          int commentCount = 0;
          String queryCountSql = "SELECT count(*) from oa_news_comment where NEWS_ID='" + seqId + "'";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryCountSql);
          if(rss.next()) {
            commentCount = rss.getInt(1);
          } 
          if("1".equals(top)){
            subject = "<font color=red><b>" + subject + "</b></font>";
          }
          sb.append("{");
          sb.append("seqId:" + seqId);
          sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
          sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle) + "\"");
          sb.append(",newsTime:\"" + newsTime + "\"");
          sb.append(",clickCount:\"" + clickCount + "\"");
          sb.append(",format:\"" + format + "\"");
          sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
          sb.append(",isRead:\"" + isRead + "\"");
          sb.append(",commentCount:\"" + commentCount + "\"");
          sb.append(",anonymityYn:\"" + anonymityYn + "\"");
          sb.append("},");
          rs.next();
        }
        //结束索引
        pgEndRecord =(pageIndex - 1) * showLen + temp;
        if(temp>0) {
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
    } finally {
    YHDBUtility.close(stmt, rs, log);
  }
    return sb.toString();
   } 
  
  /**
   * 插入新的评论
   * @param conn
   * @param newsComment
   * @throws Exception 
   */
  public void saveComment(Connection conn, YHNewsComment newsComment) throws Exception{
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
    String sql = "insert into oa_news_comment(NEWS_ID,USER_ID,NICK_NAME,CONTENT,RE_TIME) values("+ newsComment.getNewsId()+","+newsComment.getUserId() +",?,?,"+ YHDBUtility.currDateTime()+")";
   // YHOut.println(sql);
    PreparedStatement ps = null;  
    ps = conn.prepareStatement(sql);       
    try{
    	ps.setString(1, newsComment.getNickName());
    	ps.setString(2, newsComment.getContent());
      int id = ps.executeUpdate();       
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }    
  }
  
  /**
   * 查找seq_id的自评论的id串
   * @param conn
   * @param seq_id
   * @return
   */
  public String findTreeIds(Connection conn, int seq_id)throws Exception{
    String sql = "select SEQ_ID from oa_news_comment connect by prior seq_id = PARENT_ID start with seq_id="+seq_id;
    PreparedStatement ps = null;
    ResultSet rs = null;
    ps = conn.prepareStatement(sql);
    rs = ps.executeQuery();
    String ids = "";
    while(rs.next()){
      ids += rs.getString("SEQ_ID")+",";
    }
    return ids == "" ?"": ids.substring(0, ids.lastIndexOf(","));
  }
  
  /**
   * 返回某一个评论的id和这个评论的自评论的id串
   * @param conn
   * @param seq_id
   * @return
   * @throws Exception
   */
  public String findIds(Connection conn, int seq_id)throws Exception{
    String sql = "select SEQ_ID from oa_news_comment where seq_id="+seq_id;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String ids = "";
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();    
      if(rs.next()){
        ids += rs.getString("SEQ_ID")+",";
        String sql2 = "select SEQ_ID from oa_news_comment where PARENT_ID=" + seq_id;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try{
          ps2 = conn.prepareStatement(sql2);
          rs2 = ps2.executeQuery();
          while(rs2.next()){
            ids += rs2.getString("SEQ_ID")+",";
          }
        } catch (Exception e){
         throw e;
        }finally{
          YHDBUtility.close(ps2, rs2, null);
        }
      }
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return ids == "" ?"": ids.substring(0, ids.lastIndexOf(","));   
  }
  
  /**
   * 返回阅读人员的id串
   */
   public String readStatus(Connection conn, int id)throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null; 
     String sql = "Select readers from oa_news where seq_id =" + id;
     try{
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       if(rs.next()){
         return  rs.getString(1);
       }
     } catch (Exception e){
      throw e;
     }finally{
       YHDBUtility.close(ps, rs, null);
     }
     return null;
   }
  /**
   * 判端这个人读了这个新闻了么
   * @param conn
   * @param userId
   * @param noId
   * @return
   * @throws Exception
   */
  public String haveRead(Connection conn, int userId, int noId)throws Exception{
    String ids = readStatus(conn, noId);    
    if(ids!=null && ids !=""){
      String[] id = ids.split(",");
      for(int i=0; i<id.length; i++){
        if((userId+"").equalsIgnoreCase(id[i])){        
          return "yes";   //这个人读了        }
      }
    }
    return "no";
  }
  
  public static void main(String[] args){
    String aa = "\\\'\\\'dddddddd\\\'\\\'";
    aa = aa.replaceAll("\\\\","");
  }
}

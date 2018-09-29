package yh.core.funcs.news.logic;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.news.data.YHImgNews;
import yh.core.funcs.news.data.YHNewsCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.portal.util.YHPortalProducer;
import yh.core.funcs.portal.util.rules.YHImgRule;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;



/**
 * 图片新闻 桌面模块
 * @author qwx110
 *
 */
public class YHFindNewaImageLogic{
  private static String imgBasePath = "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?";
  public YHImgNews getImageNews(String id  , Connection conn , YHPerson user ) throws Exception {
    YHImgNews news =new YHImgNews();
    String query = "select seq_id , subject , content , news_time , attachment_id , attachment_name from oa_news where seq_id=" + id;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        int seqId = rs.getInt("seq_id");
        String subject = rs.getString("subject");
        Clob content = rs.getClob("content");
        Timestamp newsTime = rs.getTimestamp("news_time");
        String attachmentId = rs.getString("attachment_id");
        String attachmentName = rs.getString("attachment_name");
        news.setNewsTime(newsTime);
        news.setContent(YHWorkFlowUtility.clob2String(content));
        news.setSeqId(seqId);
        news.setSubject(subject);
        news.setAttachmentId(attachmentId);
        news.setAttachmentName(attachmentName);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    String query2 = "";
    if(user.isAdmin()){//如果是管理员，则可以看到所有的新闻
      query2 = "SELECT seq_id,subject "
        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(conn)+"' and news_time > ?  order by news_time asc";
    }else{    //如果不是则在发布范围之内的人才能看到  
      query2 = "SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(conn)+"' and ("
        + YHDBUtility.findInSet(Integer.toString(user.getDeptId()), "TO_ID")
        + " or " + YHDBUtility.findInSet(user.getUserPriv(),"PRIV_ID") +" or " 
        + YHDBUtility.findInSet(Integer.toString(user.getSeqId()),"USER_ID") + " or "
        + YHDBUtility.findInSet("0", "TO_ID") +") ";
      query2 +=  " and news_time > ? order by news_time asc";
    }
    PreparedStatement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.prepareStatement(query2);
      stm2.setTimestamp(1, news.getNewsTime());
      rs2 = stm2.executeQuery();
      if (rs2.next()) {
        int seqId = rs2.getInt("seq_id");
        String subject = rs2.getString("subject");
        news.setNextId(seqId);
        news.setNextTitle(subject);
      } else {
        news.setNextId(0);
        news.setNextTitle("");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null);
    }
    return news;
  }
  /**
   * 查找图片新闻
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public List<YHImgNews> findNewsAndPictures(Connection dbConn, YHPerson user)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = ""; 
    
    if(user.isAdmin()){//如果是管理员，则可以看到所有的新闻
      sql = "SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' order by NEWS_TIME desc";
    }else{    //如果不是则在发布范围之内的人才能看到  
      sql = "SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' and ("
        + YHDBUtility.findInSet(Integer.toString(user.getDeptId()), "TO_ID")
        + " or " + YHDBUtility.findInSet(user.getUserPriv(),"PRIV_ID") +" or " 
        + YHDBUtility.findInSet(Integer.toString(user.getSeqId()),"USER_ID") + " or "
        + YHDBUtility.findInSet("0", "TO_ID") +") ";
      sql +=  " order by NEWS_TIME desc";
    }
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    List<YHImgNews> ImgNews = new ArrayList<YHImgNews>();
    int cnt = 0;
    while(rs.next() && ++cnt < 10){
      YHImgNews news = new YHImgNews();
      news.setSeqId(rs.getInt("SEQ_ID"));
      news.setSubject(rs.getString("SUBJECT"));
      news.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
      news.setAttachmentId(rs.getString("ATTACHMENT_ID"));
      ImgNews.add(news);
    }
    return ImgNews;    
  }
  public int getImageTypeId(Connection conn) throws Exception {
    String query = "select seq_id from oa_kind_dict_item where class_no = 'NEWS' and class_desc ='图片新闻'";
    int result = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        result = rs.getInt("seq_id");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return result;
  }
  /**
   * 查找图片新闻
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public List<YHImgNews> findPicturePath(Connection dbConn, YHPerson user)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = ""; 
    
    if(user.isAdmin()){//如果是管理员，则可以看到所有的新闻
      sql = "SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' order by NEWS_TIME desc";
    }else{    //如果不是则在发布范围之内的人才能看到  
      sql = "SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' and ("
        + YHDBUtility.findInSet(Integer.toString(user.getDeptId()), "TO_ID")
        + " or " + YHDBUtility.findInSet(user.getUserPriv(),"PRIV_ID") +" or " 
        + YHDBUtility.findInSet(Integer.toString(user.getSeqId()),"USER_ID") + " or "
        + YHDBUtility.findInSet("0", "TO_ID") +") ";
      sql +=  " order by NEWS_TIME desc";
    }
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    List<YHImgNews> ImgNews = new ArrayList<YHImgNews>();
    int cnt = 0;
    while(rs.next() && ++cnt <= 10){
      YHImgNews news = new YHImgNews();
      news.setSeqId(rs.getInt("SEQ_ID"));
      news.setSubject(rs.getString("SUBJECT"));
      news.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
      news.setAttachmentId(rs.getString("ATTACHMENT_ID"));
      ImgNews.add(news);
    }
    return ImgNews;    
  }
  public List<YHImgNews> getNewsList(Connection dbConn, YHPerson user) {
    // TODO Auto-generated method stub
    List<YHImgNews> list = new ArrayList();
    
    
    return list;
  }

  /**
   * 新的图片新闻桌面模块
   * @param dbConn
   * @param user 登陆的用户
   * @return
   * @throws Exception
   */
  public String findImgToJsonDesk(Connection dbConn, YHPerson user, HttpServletRequest request)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = ""; 
   
    String srcPath = "";
    String src = "";
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if(user.isAdmin()){//如果是管理员，则可以看到所有的新闻
    	 if(dbms.equals(YHConst.DBMS_MYSQL)){// add by zyy 取前几条数据
    		 sql = "SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
    			        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' order by NEWS_TIME desc limit 10";
    			    
    	 }else if(dbms.equals(YHConst.DBMS_ORACLE)){
    		 sql = "select *  from (SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
    			        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' order by NEWS_TIME desc ) WHERE ROWNUM<=10 ";
    			    
    	 }
    }else{    //如果不是则在发布范围之内的人才能看到  
    	if(dbms.equals(YHConst.DBMS_MYSQL)){
    		  sql = "SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
    			        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' and ("
    			        + YHDBUtility.findInSet(Integer.toString(user.getDeptId()), "TO_ID")
    			        + " or " + YHDBUtility.findInSet(user.getUserPriv(),"PRIV_ID") +" or " 
    			        + YHDBUtility.findInSet(Integer.toString(user.getSeqId()),"USER_ID") + " or "
    			        + YHDBUtility.findInSet("0", "TO_ID") +") ";
    			      sql +=  " order by NEWS_TIME desc  limit 10";
    	 }else if(dbms.equals(YHConst.DBMS_ORACLE)){
    		  sql = "select * from ( SELECT SEQ_ID,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID "
    			        +" from oa_news where PUBLISH='1' and TYPE_ID='"+this.getImageTypeId(dbConn)+"' and ("
    			        + YHDBUtility.findInSet(Integer.toString(user.getDeptId()), "TO_ID")
    			        + " or " + YHDBUtility.findInSet(user.getUserPriv(),"PRIV_ID") +" or " 
    			        + YHDBUtility.findInSet(Integer.toString(user.getSeqId()),"USER_ID") + " or "
    			        + YHDBUtility.findInSet("0", "TO_ID") +") ";
    			      sql +=  " order by NEWS_TIME desc ) where rownum<=10";
    		 
    	 }
 
    
    }
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    int cnt = 0;
    List<Object> list = new ArrayList<Object>();
    YHPortalProducer rule = new YHPortalProducer();
    while(rs.next() && ++cnt < 10){
      Map<String, String> map = new HashMap<String, String>();
      map.put("SEQ_ID", rs.getString("SEQ_ID"));
      map.put("SUBJECT", rs.getString("SUBJECT"));
      String fileNames = rs.getString("ATTACHMENT_NAME");
      String fileIds  = rs.getString("ATTACHMENT_ID");   
      if(!YHUtility.isNullorEmpty(fileNames) && !YHUtility.isNullorEmpty(fileIds)){
      String[] idsplit = fileIds.split(",");              //文件id
      if(idsplit.length > 0){        
        if(!YHUtility.isNullorEmpty(idsplit[0]) || !"null".equalsIgnoreCase(idsplit[0])){
          String[] namesplit = fileNames.split("[*]");        //文件名          
          srcPath = "attachmentId="+idsplit[0]+"&module=" + YHNewsCont.MODULE + "&attachmentName="+YHUtility.encodeSpecial(namesplit[0]);
          src = request.getContextPath() + imgBasePath + srcPath;
        }
      }
    }
      map.put("src", src);
      String httpPath = request.getContextPath() + "/core/funcs/news/imgNews/imageWindow.jsp?id=" + rs.getString("SEQ_ID");
      map.put("link", httpPath);
      list.add(map);
    }
    rule.setData(list);
    YHImgRule r = new YHImgRule("SUBJECT", "src", "link");
    r.setAttribute("target", "_blank");
    rule.addRule(r);
    //rule.addRule(new YHLinkRule("SUBJECT",  "link"));
    return rule.toJson();
  }
  
  
  
}

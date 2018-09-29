package yh.lucene.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.Document;

import yh.lucene.data.YHRss;
import yh.lucene.logic.YHLuceneIndex;
import yh.cms.content.logic.YHContentLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHAuthenticator;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
public class YHPersonServiceLogic {

  
  public String getLuceneLogic(Connection conn,String content,String rss,String host,int size,int num,String condition,String contextPath)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    YHLuceneIndex index=new YHLuceneIndex();
    String str="";
    try{
      List<Map<String,String>> list=new ArrayList();
     if(YHUtility.isNullorEmpty(rss)){
       list= index.seacherWithHightLighter(content,size,num);
       data=this.getJsonData(list,content,host,conn,contextPath); 
     }else{
       list= index.seacher(content,condition);
       data=this.getRssData(list,content,host,conn);
     }
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null) ;
    }
        return data;

  }
  /**
   * 
   * */
  public String getLuceneByTopLogic(Connection conn,String content,String rss,String host,int size,int num,String contextPath,String stationId)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    YHLuceneIndex index=new YHLuceneIndex();
    String str="";
    try{
      List<Map<String,String>> list=new ArrayList();
     if(YHUtility.isNullorEmpty(rss)){
       list= index.seacherWithFilter(content, size, num, stationId);
       data=this.getJsonData(list,content,host,conn,contextPath); 
     }/*else{
       list= index.seacher(content,condition);
       data=this.getRssData(list,content,host);
     }*/
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null) ;
    }
        return data;

  }
  
  
  
  
  public String getRssData(List<Map<String,String>> list,String content,String host,Connection conn){
    String data="";
    
    
  data+="<?xml version=\"1.0\" encoding=\"utf-8\"?> \n";
  data+="  <rss version=\"2.0\"> \n ";
  data+="  <channel>  \n";
  data+="  <title><![CDATA[全文搜索_"+YHUtility.encodeSpecial(content)+"]]></title>  \n";
  data+="  <link></link>  \n";
  data+="  <description></description>  \n";
  data+="  <copyright></copyright>  \n";
  data+="   <webMaster></webMaster>  \n";
  data+="  <generator></generator>   \n";

  for(int i=0;i<list.size();i++){
    Map<String,String> map=list.get(i);
    String url="";
    try {
      url = this.getDeferURL(map.get("module"), map.get("seqId"), map.get("subinfo"),conn);
    } catch (NumberFormatException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }  

    data+="          <item>  \n";
                      String contentStr=map.get("content");
                    //  contentStr=this.Htmlfilter(contentStr);
                   //   contentStr=contentStr.substring(0, contentStr.length()-50);
                      
                      contentStr=YHUtility.cutHtml(contentStr).replace("&nbsp;", "").replace("&", "").replace("\"", "\\\"");
                      contentStr=YHUtility.encodeSpecial(contentStr);
                      contentStr=contentStr.replace("","");
                    //  contentStr=contentStr.replaceAll("]]>","");
    try {
      data+="              <link>"+url+"</link>  \n";
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    data+="              <title><![CDATA["+YHUtility.encodeSpecial(map.get("title"))+"]]></title>  \n";   
    data+="              <pubDate><![CDATA["+map.get("time")+"]]></pubDate> \n";
    data+="              <description><![CDATA["+contentStr+"]]></description> \n"; 
    data+="         </item>  \n";
 
  }
  
    data+="  </channel>  \n";
    data+="  </rss> \n"; 

    
    return data;
  }
  

  
  public String getJsonData(List<Map<String,String>> list,String content,String host,Connection conn,String contextPath){
    String data="";
    
    String data1="";
    Map<String,String> map1=list.get(list.size()-1);
    data1+="{";
    data1+="pageSize:'"+map1.get("pageSize")+"',";
    data1+="pageCount:'"+map1.get("pageCount")+"',";
    data1+="pageIndex:'"+map1.get("pageIndex")+"',";
    data1+="totalCount:'"+map1.get("totalCount")+"',";
   
    
    
    for(int i=0;i<list.size()-1;i++){
      Map<String,String> map=list.get(i);
      String url="";
      try {
        url = this.getDeferURL(map.get("stationId"), map.get("seqId"), map.get("columnId"),conn,contextPath);
      } catch (NumberFormatException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }  
      String title=map.get("title");
      String info=map.get("content");
  
   /*   if(title.length()>50){
        title=title.substring(0, 49)+".....";
      }*/
      if(YHUtility.isNullorEmpty(title)){
        title="";
      }
      if(YHUtility.isNullorEmpty(info)){
        info="";
      }
      String typeName="";
     // String typeName=this.getTypeName(map.get("module"), map.get("seqId"), map.get("typeId"), map.get("subinfo"));
      String checkstr=YHUtility.encodeSpecial(title)+"titlespanplit"+YHUtility.encodeSpecial(info)+"contentspanplit";
  //    title=title.replace(content, "<font  style=\"font-weight: bold\"  color=\"#FF3333\">"+content+"</font>");

    //  info=info.replace(content, "<font style=\"font-weight: bold\"  color=\"#FF3333\">"+content+"</font>");
      checkstr=checkstr.replace("&", "&amp;");
      checkstr=checkstr.replace("\"", "&quot;");
      data+="{";
      data+="seqId:'"+map.get("seqId")+"',";
      data+="title:'"+YHUtility.encodeSpecial(title)+"',";
      data+="checkbox:'"+YHUtility.encodeSpecial(checkstr)+"',";
      data+="typeName:'"+typeName+"',";
      data+="content:'"+YHUtility.encodeSpecial(info)+"',";
      data+="time:'"+map.get("time")+"',";
      data+="url:'"+YHUtility.encodeSpecial(url)+"'";
      data+="},";
     }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    
    data1+="data:["+data+"]";
    data1+="}";
    return data1;
  } 
  
  public String getDeferURL(String seqId,Connection conn) throws NumberFormatException, Exception{
    String url="";
    YHContentLogic cLogic= new YHContentLogic();
    url= cLogic.getUrls(conn, Integer.parseInt(seqId));
    
    return url;
  }
  
  public String getDeferURL(String stationId,String seqId,String columnId,Connection conn) throws NumberFormatException, Exception{
    String url="";
    YHContentLogic cLogic= new YHContentLogic();
    url= cLogic.getUrls(conn, Integer.parseInt(seqId));
    
    return url;
  }
  public String getDeferURL(String stationId,String seqId,String columnId,Connection conn,String contextPath) throws NumberFormatException, Exception{
    String url="";
    YHContentLogic cLogic= new YHContentLogic();
    url= cLogic.getUrls(conn, Integer.parseInt(seqId));
    
    return url;
  }
  /**
   * 类型判断
   * */
  public String getTypeName(String module,String seqId,String typeId,String subInfo){
    String typeName="";
    if("message".equals(module)){
      typeName="个性服务--留言";
    }else if("subcon".equals(module)){
      typeName="子区 ";
    }else if(module.indexOf("candidate")!=-1){
      typeName="参选人资料";
    }else if("district".equals(module)){
      typeName="选区";
    }else if("plitgroup".equals(module)){
      typeName="政党";
    }else if("newsmessage".equals(module) && "1327".equals(typeId)){
      typeName="视频新闻";
    }else if(module.indexOf("introduction")!=-1){
      typeName="选举介绍"; 
    }else if(module.indexOf("person")!=-1){
      typeName="参选人";
    }else if(module.indexOf("newsmessage")!=-1){
      typeName="新闻消息";
    }else{
      typeName="其他资料";
    }
    if(module.indexOf("ts_")!=-1){
      typeName=typeName+"-委员会";
    }else if(module.indexOf("qyh_")!=-1  || module.indexOf("newsmessage")!=-1){
      typeName=typeName+"-区议会";
    }else if(module.indexOf("xz_")!=-1){
      typeName=typeName+"-特首";
    }
 //   System.out.println(module);
    return typeName;
  }
  
  
  public String getLatestMessageLogic(Connection conn)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    String str="";
    try{
     String sql=" select * from oa_news where module_id='psmessage' order by news_time desc";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     int index=0;
     while(rs.next()){
       int seqId=rs.getInt("seq_id");
       String content=rs.getString("content");
       String time=rs.getString("news_time");
       /*if(content.length()>30){
         content=content.substring(0, 29)+"....";
       }*/
       
       data+="{";
       data+="seqId:'"+seqId+"',";
       data+="content:'"+YHUtility.encodeSpecial(content)+"',";
       data+="time:'"+time.substring(0, 10)+"'";
       data+="},";
       if(index>8){
        break; 
       }
       index++;
     }

    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null) ;
    }
     if(data.endsWith(",")){
       data=data.substring(0, data.length()-1);
     }      
    return data;

  }


  public String getLuceneLog(Connection conn,String str)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
     String sql=" select distinct(lu_str) from lucene_log where lu_str like '%"+str+"%' ";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     int index=0;
     while(rs.next()){
      // int seqId=rs.getInt("seq_id");
       String content=rs.getString("lu_str");
       data+="{";
    //   data+="seqId:'"+seqId+"',";
       data+="content:'"+YHUtility.encodeSpecial(content)+"'";
       data+="},";
       if(index>9){
        break; 
       }
       index++;
     }

    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null) ;
    }
     if(data.endsWith(",")){
       data=data.substring(0, data.length()-1);
     }      
    return data;

  }
  /*获取推荐词汇
   * */
  public String getRecomentWord(Connection conn,String str)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
     String sql=" select distinct(lu_str) from lucene_log where lu_str like '%"+str+"%' ";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     int index=0;
     while(rs.next()){
      // int seqId=rs.getInt("seq_id");
       String content=rs.getString("lu_str");
       data+="{";
    //   data+="seqId:'"+seqId+"',";
       data+="content:'"+YHUtility.encodeSpecial(content)+"'";
       data+="},";
       if(index>5){
        break; 
       }
       index++;
     }

    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null) ;
    }
     if(data.endsWith(",")){
       data=data.substring(0, data.length()-1);
     }      
    return data;

  }
  
  public void getSubmitLogic(YHPerson person,String toId,String url,String name)throws Exception{
    
    String data="";
    Statement stmt=null;

    boolean isApart=false;
    String str="";
    String subject="订阅资料--"+name;
    url="<a href=\""+url+"\">"+url+"</a>";
    String content="<p><em>您好：</em></p><p><em>您订阅的资料，   名称</em>：<span style=\"color: #FF0000; font-weight: bold;\">"+name+"</span></p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;URL:<span style=\"color: #FF0000; font-weight: bold;\">"+url+"</span></p><p>&nbsp;  ";
    content=YHUtility.encodeSpecial(content);
    String time=YHUtility.getCurDateTimeStr();
    try{
     Connection conn=this.getRssConnection();
     if(conn!=null){
     stmt=conn.createStatement();
     
     
     String sql1="insert into oa_email_body(FROM_ID,TO_ID2,COPY_TO_ID,SECRET_TO_ID,SUBJECT,CONTENT,SEND_TIME,ATTACHMENT_ID,ATTACHMENT_NAME,SEND_FLAG,SMS_REMIND,IMPORTANT,SIZE,FROM_WEBMAIL_ID,FROM_WEBMAIL,TO_WEBMAIL,COMPRESS_CONTENT,WEBMAIL_CONTENT,WEBMAIL_FLAG,RECV_FROM_NAME,RECV_FROM,RECV_TO_ID,RECV_TO,IS_WEBMAIL) " +
     		"                        values ('"+toId+"','"+toId+"','0','0','"+subject+"','"+content+"','"+time+"','','','1','1','','0','0','0','0','','','0','0','0','0','0','0')";
     stmt.execute(sql1);
     String body_id=this.getBodyId(conn," select max(body_id) from oa_email_body ");
     String sql="  insert into oa_email(TO_ID,READ_FLAG,DELETE_FLAG,BODY_ID,RECEIPT) values ('"+toId+"','0','0','"+body_id+"','1')";
     stmt.execute(sql);
     
     subject="";
     content="";
     
     String email_id=this.getBodyId(conn, " select max(email_id) from oa_email ");
     
     String remind_url="/general/email/inbox/read_email/read_email.php?BOX_ID=0&BTN_CLOSE=1&FROM=1&EMAIL_ID="+email_id;
     String sql2=" insert into oa_msg_body(FROM_ID,SMS_TYPE,CONTENT,SEND_TIME,REMIND_URL) values ('1','0','"+subject+"','"+time+"','"+remind_url+"')";
     stmt.execute(sql2);
     
     body_id=this.getBodyId(conn," select max(body_id) from oa_msg_body ");
     String sql3="insert into SMS(TO_ID,REMIND_FLAG,DELETE_FLAG,BODY_ID,REMIND_TIME) values('"+toId+"','1','0','"+body_id+"','12345') ";
     stmt.execute(sql3);
     }


    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, null, null) ;
    }
    

  }
  
  
  public void outPerson(Connection conn,YHPerson person,String url,String name)throws Exception{

    try{
      
      
      YHRss rss=new YHRss();
      rss.setUserId(person.getSeqId()+"");
      rss.setRssUrl(url);
      rss.setName(name);
      
      YHORM orm=new YHORM();
      orm.saveSingle(conn, rss);
    }catch(Exception e){
      e.printStackTrace();
    }

  }
  
  
  
  
  
  public String getBodyId(Connection conn,String sql)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      //String sql=" select max(body_id) from email_body ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        data=rs.getInt(1)+"";
      }
      
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }

    return data;
  }
  
  
  
  public String getPersonRssLogic(Connection conn,YHPerson person)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from rss where "+YHDBUtility.findInSet(person.getSeqId()+"", "user_id")+" order by seq_id desc";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){
        int seqId=rs.getInt("seq_id");
        String name=rs.getString("name");
        String url=rs.getString("rss_url");
        
       data+="{";
       data+="seqId:'"+seqId+"'";
       data+=",name:'"+name+"'";
       data+=",url:'"+url+"'";
       data+="},";

      }
      
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }

    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    
    return data;
  }
  
  
  public String getJionResultLogic(Connection conn,String str)throws Exception{
     String data="";
    Map<String,String> map=new HashMap<String,String>();
    try{
      if(str.endsWith(",")){
        str=str.substring(0, str.length()-1);
      }
      String result[]=str.split("contentspanplit");
      for(int i=0;i<result.length;i++){
        String restr=result[i];
        if(!"".equals(restr)){
        String title= restr.substring(0, restr.indexOf("titlespanplit"));
          
        String content=  restr.substring(restr.indexOf("titlespanplit"),restr.length());
        title=title.replace("titlespanplit", "");
        content=content.replace("titlespanplit", "");
        
        data+="<Strong>"+title+"</Strong>";
         data+="<p>"+content+"</p>";
         data+="<br><br>";
        
        
        }
        
      }

      /*YHORM orm=new YHORM();
      YHNews news =new YHNews();
      news.setNewsTime(new Date());
      news.setProvider("1");
      news.setContent(content);
      news.setSubject(title);
      news.setNewsTime(new Date());
      news.setToId("-1");
      news.setModuleId("jionstr");
      orm.saveSingle(conn, news);*/

    }catch(Exception e){
      e.printStackTrace();
    }
    
    return data;
  }
  
  
  public String getMoreMessage(Connection conn,int index,int size)throws Exception{
    String data="";
    String data1="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
          String sql="SELECT * FROM oa_news where module_id='psmessage' order by news_time desc";
        
          int i=0;
          stmt=conn.createStatement();
          rs=stmt.executeQuery(sql);
          rs.last();
          int count=rs.getRow();
          data1+="{";
          data1+="pageSize:'"+size+"',";
          data1+="pageCount:'"+((count + size - 1)/size)+"',";
          data1+="pageIndex:'"+index+"',";
          data1+="totalCount:'"+count+"',";

          rs.absolute((index-1)*size+1);
          while(i<size && !rs.isAfterLast() && count>0){
            String seqId=rs.getString("seq_id");
           String subject=rs.getString("content");
           
           String newsTime = rs.getString("NEWS_TIME");
           data+="{";
           data+="seqId:'"+seqId+"',";
           data+="subject:'"+YHUtility.encodeSpecial(subject)+"',";
           data+="typeName:'留言信息',";
           data+="newsTime:'"+newsTime.substring(0, 10)+"'";
           data+="},";
           i++;
           rs.next();
          }   
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    data1+="data:["+data+"]";
    data1+="}";
    return data1;

  }
//得到更多公告
  public String getMoreNotice(Connection conn,int index,int size)throws Exception{
    String data="";
    String data1="";
    Statement stmt=null;
    ResultSet rs=null;
    int i=0;
    try{
          String sql="SELECT * FROM news where module_id='newsmessage' order by news_time desc";
        
        
          stmt=conn.createStatement();
          rs=stmt.executeQuery(sql);
          rs.last();
          int count=rs.getRow();

          rs.absolute((index-1)*size+1);
          while(i<size && !rs.isAfterLast()  && count>0 ){
            String typeId=rs.getString("type_id");
            String typeName=this.getNewsName(conn, typeId);
            if("公告".equals(typeName)){
            
            
            String seqId=rs.getString("seq_id");
           String subject=rs.getString("subject");
           if(subject.length()>30){
             subject=subject.substring(0, 29);
           }
           String newsTime = rs.getString("NEWS_TIME");
           data+="{";
           data+="seqId:'"+seqId+"',";
           data+="subject:'"+subject+"',";
           data+="typeName:'公告信息',";
           data+="newsTime:'"+newsTime.substring(0, 10)+"'";
           data+="},";
           i++;
          
            } 
            rs.next();
          }   
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    
    data1+="{";
    data1+="pageSize:'"+size+"',";
    data1+="pageCount:'"+((i + size - 1)/size)+"',";
    data1+="pageIndex:'"+index+"',";
    data1+="totalCount:'"+(i)+"',";
    
    data1+="data:["+data+"]";
    data1+="}";
    return data1;

  }
  
  
  
  

  public String getNewsName(Connection conn,String seqId)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
          String sql="SELECT CLASS_DESC FROM oa_kind_dict_item where seq_id='" + seqId + "'";
          stmt=conn.createStatement();
          rs=stmt.executeQuery(sql);
          while(rs.next()){
            data=rs.getString("CLASS_DESC");
          }
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null) ;
    }
    return data;

  }
  //获取实时热点
  public String getHotNews(Connection conn,String contentPath)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
          String sql="SELECT seq_id,content_name from oa_cms_contents order by content_date desc ";
          stmt=conn.createStatement();
          rs=stmt.executeQuery(sql);
          int i=0;
          while(rs.next() && i<6){
            data+="{";
            data+="seqId:'"+rs.getString("seq_id")+"',";
            data+="content:'"+YHUtility.encodeSpecial(rs.getString("content_name"))+"',";
            data+="url:'"+this.getDeferURL("", rs.getString("seq_id"), "", conn, contentPath)+"'";
            data+="},";
            i++;
          }
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null) ;
    }
    
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    return data;

  }
  
  public Connection getRssConnection(){
    Connection conn=null;
    String conurl=YHSysProps.getProp("td_oa.db.jdbc.conurl.mysql");
    String pwd=YHSysProps.getProp("td_oa.db.jdbc.passward.mysql");
    String user=YHSysProps.getProp("td_oa.db.jdbc.userName.mysql");
    String dbName=YHSysProps.getProp("td_oa.db.jdbc.dbName");
    String url=conurl+""+dbName+"?zeroDateTimeBehavior=convertToNULL";

   try{
    String driverName=  YHSysProps.getProp("db.jdbc.driver.mysql");
    Class.forName(driverName).newInstance();
    pwd=YHAuthenticator.ciphDecryptStr(pwd);
    conn=DriverManager.getConnection(url,user,pwd);
    
  }catch(Exception e){
    System.out.println(e.toString());
  }
  
  return conn;
 }
  
  
  public void deleteRssLogic(Connection conn,String seqId)throws Exception{
    
    try{
      YHORM orm=new YHORM();
      orm.deleteSingle(conn, YHRss.class, Integer.parseInt(seqId));
      
    }catch(Exception ex){
      ex.printStackTrace();
    }

  }
  
}

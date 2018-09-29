package yh.lucene.logic;

import java.io.File;    
import java.io.FileReader;    
import java.io.IOException;
import java.io.Reader;    
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;    
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;    
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;    
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;    
import org.apache.lucene.document.Field;    
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexWriter; 
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHAuthenticator;
import yh.core.util.db.YHDBUtility;
import yh.lucene.logic.MutiFilter;
import yh.lucene.logic.strFilter;
import yh.lucene.logic.timeFilter;



public class YHLuceneIndex {

  private Connection TsDbConn;
  /**
   * 获得Ts项目的连接
   * 
   * */
  public Connection getTsDbConn()throws Exception{
     String url=YHSysProps.getProp("ts.db.jdbc.conurl.mysql");
     String pwd=YHSysProps.getProp("ts.db.jdbc.passward.mysql");
     String un =YHSysProps.getProp("ts.db.jdbc.userName.mysql");
     String dbName = YHSysProps.getProp("ts.db.jdbc.dbName");
     String connUrl=url+""+dbName+"?zeroDateTimeBehavior=convertToNULL";

     try{
      String driverName=  YHSysProps.getProp("db.jdbc.driver.mysql");
      Class.forName(driverName).newInstance();
   //   pwd=YHAuthenticator.ciphDecryptStr(pwd);
      this.TsDbConn=DriverManager.getConnection(connUrl,un,pwd);
      
    }catch(Exception e){
      e.printStackTrace();
    }

    return this.TsDbConn;
  }
  
  
  @SuppressWarnings("deprecation")
  public void setIndex(List<Map<String,String>> dataList){
    try{
      String path=YHSysProps.getProp("lucene_path");
      Directory dir = new SimpleFSDirectory(new File(path));
      IndexWriter writer = new IndexWriter(dir,this.getAnalyzer(),true,IndexWriter.MaxFieldLength.UNLIMITED);
      for(int i=0;i<dataList.size();i++){
          Document doc=new Document();
          Map<String,String> map=dataList.get(i);
          String seqId=map.get("seqId");
          String title=map.get("title");
          String content=map.get("content");
          String search=map.get("search");
          String time=map.get("time");
          String stationId=map.get("stationId");
          String columnId=map.get("columnId");
      //    String typeId=map.get("typeId");

          String long_time="0";
          if(!YHUtility.isNullorEmpty(time)){
              long_time=time.replace("-", "").substring(0,8);
           //   System.out.println(time+"-----"+long_time);
       
          }
          
          
         // String dateTimeString = DateTools.timeToString(time, DateTools.Resolution.SECOND);
      //    doc.add(new Field("name", "lighter blog",Field.Store.YES,Field.Index.Index.TOKENIZED));
          doc.add(new Field("seq_id",seqId,Field.Store.YES,Field.Index.NOT_ANALYZED));
          //doc.add(Field.StoreStore("seq_id",seqId));
          doc.add(new Field("title",title,Field.Store.YES,Field.Index.NOT_ANALYZED));
         // doc.add(Field.UnIndexed("title",title));
          doc.add(new Field("content",content,Field.Store.YES,Field.Index.NOT_ANALYZED));
       //  doc.add(Field.UnIndexed("content",content));
          doc.add(new Field("search",search,Field.Store.YES,Field.Index.ANALYZED));
      //    doc.add(Field.UnStored("search",search));
          doc.add(new Field("time",time,Field.Store.YES,Field.Index.NOT_ANALYZED));
          doc.add(new Field("long_time",long_time,Field.Store.YES,Field.Index.NOT_ANALYZED));
       //   doc.add(Field.UnIndexed("time",time));
          doc.add(new Field("columnId",columnId,Field.Store.YES,Field.Index.ANALYZED));
        //  doc.add(Field.UnIndexed("module",module));
         // doc.add(new Field("typeId",typeId,Field.Store.YES,Field.Index.ANALYZED));
         // doc.add(Field.UnIndexed("typeId",typeId));
          doc.add(new Field("stationId",stationId,Field.Store.YES,Field.Index.ANALYZED));
          doc.add(new Field("term","term",Field.Store.YES,Field.Index.NOT_ANALYZED));
      //    doc.add(Field.UnIndexed("subinfo",subinfo));
          writer.addDocument(doc);
        }
      writer.optimize();
      writer.close();
      
    }
    catch(IOException e){
      System.out.println(e);
    }

   
  } 
  /*
   *  建立新闻内容搜索引擎
   * */
  public List<Map<String,String>> setNewsContent(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select seq_id,content_name,content_title,CONTENT_ABSTRACT,CONTENT_DATE,STATION_ID,COLUMN_ID,CONTENT from oa_cms_contents where content_status='5' order by CONTENT_DATE desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String subject=rs.getString("content_name");
          if(YHUtility.isNullorEmpty(subject)){
            subject="";
          }
          String contentTitle=rs.getString("content_title");
          if(YHUtility.isNullorEmpty(contentTitle)){
            contentTitle="";
          }
          String contentAbstract=rs.getString("CONTENT_ABSTRACT");
          if(YHUtility.isNullorEmpty(contentAbstract)){
            contentAbstract="";
          }
          String contentDate=rs.getString("CONTENT_DATE");
          if(YHUtility.isNullorEmpty(contentDate)){
            contentDate="";
          }
          String stationId=rs.getString("STATION_ID");
          if(YHUtility.isNullorEmpty(stationId)){
            stationId="";
          }
          String columnId=rs.getString("COLUMN_ID");
          if(YHUtility.isNullorEmpty(columnId)){
            columnId="";
          }
          String content=rs.getString("CONTENT");
          if(YHUtility.isNullorEmpty(content)){
            content="";
          }
         content= YHDiaryUtil.cutHtml(content);
          if(content.length()>150){
            content=content.substring(0, 140);
          }
          map.put("seqId", seqId+"");
          map.put("title", subject);
          map.put("content",content);
          map.put("search", subject+","+content+","+contentDate);
          map.put("time",contentDate);
          map.put("stationId",stationId);
          map.put("columnId",columnId);
       
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      System.out.println(e);
    }
   
    return dataList;
  } 

  
  public List<Map<String,String>> setNewsIndex(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select seq_id,subject,content,news_time,module_id,subinfo,type_id from oa_news order by news_time desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String subject=rs.getString("subject");
          if(YHUtility.isNullorEmpty(subject)){
            subject="";
          }
          String typeId=rs.getString("type_id");
          if(YHUtility.isNullorEmpty(typeId)){
            typeId="";
          }
          String content=rs.getString("content");
          if(YHUtility.isNullorEmpty(content)){
            content="";
          }
          String time=rs.getString("news_time");
          if(YHUtility.isNullorEmpty(time)){
            time="";
          }
          String module_id=rs.getString("module_id");
          if(YHUtility.isNullorEmpty(module_id)){
            module_id="";
          }
          String subinfo=rs.getString("subinfo");
          if(YHUtility.isNullorEmpty(subinfo)){
            subinfo="";
          }
          
          map.put("seqId", seqId+"");
          map.put("title", subject);
          map.put("content", content+"<br/>:"+time);
          map.put("search", subject+","+content+","+time);
          map.put("typeId",typeId);
          map.put("time",time);
          map.put("module", "qyh_"+module_id);
          map.put("subinfo", subinfo);
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      System.out.println(e);
    }
   
    return dataList;
  } 
  
  
  public List<Map<String,String>> setCandIndex(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from candidate order by seq_id desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String cn_name=rs.getString("cn_name");
          if(YHUtility.isNullorEmpty(cn_name)){
            cn_name="";
          }
          String en_name=rs.getString("en_name");
          if(YHUtility.isNullorEmpty(en_name)){
            en_name="";
          }
          String country=rs.getString("country");
          if(YHUtility.isNullorEmpty(country)){
            country="";
          }
          String profession=rs.getString("profession");
          if(YHUtility.isNullorEmpty(profession)){
            profession="";
          }
          String nation=rs.getString("nation");
          if(YHUtility.isNullorEmpty(nation)){
            nation="";
          }
          String grah_sch=rs.getString("grad_sch");
          if(YHUtility.isNullorEmpty(grah_sch)){
            grah_sch="";
          }
          String c_native=rs.getString("c_native");
          if(YHUtility.isNullorEmpty(c_native)){
            c_native="";
          }
          String birth_place=rs.getString("birth_place");
          if(YHUtility.isNullorEmpty(birth_place)){
            birth_place="";
          }
          
          
          map.put("seqId", seqId+"");
          map.put("title", cn_name);
          map.put("content", cn_name+","+en_name+","+country+","+profession+","+nation+","+grah_sch+","+c_native+","+birth_place);
          map.put("search", cn_name+","+en_name+","+country+","+profession+","+nation+","+grah_sch+","+c_native+","+birth_place);
          map.put("typeId", "");
          map.put("time","");
          map.put("module", "qyh_person");
          map.put("subinfo", "");
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      System.out.println(e);
    }
   
    return dataList;
  } 
  
  /*
   * TS项目的参选人资料搜集
   * */
  public List<Map<String,String>> setCandIndexFromTs(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from candidate order by seq_id desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String cn_name=rs.getString("cn_name");
          if(YHUtility.isNullorEmpty(cn_name)){
            cn_name="";
          }
          String en_name=rs.getString("en_name");
          if(YHUtility.isNullorEmpty(en_name)){
            en_name="";
          }
          String country=rs.getString("country");
          if(YHUtility.isNullorEmpty(country)){
            country="";
          }
          String profession=rs.getString("profession");
          if(YHUtility.isNullorEmpty(profession)){
            profession="";
          }
          String nation=rs.getString("nation");
          if(YHUtility.isNullorEmpty(nation)){
            nation="";
          }
          String grah_sch=rs.getString("grad_sch");
          if(YHUtility.isNullorEmpty(grah_sch)){
            grah_sch="";
          }
          String c_native=rs.getString("c_native");
          if(YHUtility.isNullorEmpty(c_native)){
            c_native="";
          }
          String birth_place=rs.getString("birth_place");
          if(YHUtility.isNullorEmpty(birth_place)){
            birth_place="";
          }
          
          
          map.put("seqId", seqId+"");
          map.put("title", cn_name);
          map.put("content", cn_name+","+en_name+","+country+","+profession+","+nation+","+grah_sch+","+c_native+","+birth_place);
          map.put("search", cn_name+","+en_name+","+country+","+profession+","+nation+","+grah_sch+","+c_native+","+birth_place);
          map.put("typeId", "");
          map.put("time","");
          map.put("module", "ts_person");
          map.put("subinfo", "");
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      System.out.println(e);
    }
   
    return dataList;
  } 
  
  
  /*
   * 行政长官项目的参选人资料搜集
   * */
  public List<Map<String,String>> setCandIndexFromXZ (Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from ts_candidate order by seq_id desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String cn_name=rs.getString("cn_name");
          if(YHUtility.isNullorEmpty(cn_name)){
            cn_name="";
          }
          String en_name=rs.getString("en_name");
          if(YHUtility.isNullorEmpty(en_name)){
            en_name="";
          }
          String country=rs.getString("country");
          if(YHUtility.isNullorEmpty(country)){
            country="";
          }
          String profession=rs.getString("profession");
          if(YHUtility.isNullorEmpty(profession)){
            profession="";
          }
          String nation=rs.getString("nation");
          if(YHUtility.isNullorEmpty(nation)){
            nation="";
          }
          String grah_sch=rs.getString("grad_sch");
          if(YHUtility.isNullorEmpty(grah_sch)){
            grah_sch="";
          }
          String c_native=rs.getString("c_native");
          if(YHUtility.isNullorEmpty(c_native)){
            c_native="";
          }
          String birth_place=rs.getString("birth_place");
          if(YHUtility.isNullorEmpty(birth_place)){
            birth_place="";
          }
          
          
          map.put("seqId", seqId+"");
          map.put("title", cn_name);
          map.put("content", cn_name+","+en_name+","+country+","+profession+","+nation+","+grah_sch+","+c_native+","+birth_place);
          map.put("search", cn_name+","+en_name+","+country+","+profession+","+nation+","+grah_sch+","+c_native+","+birth_place);
          map.put("typeId", "");
          map.put("time","");
          map.put("module", "xz_person");
          map.put("subinfo", "");
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      System.out.println(e);
    }
   
    return dataList;
  } 
  
  
  
   /**
    * 
    * 获取ts项目的news资料
    * */
  public List<Map<String,String>> setNewsIndexFromTs(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select seq_id,subject,content,news_time,module_id,subinfo,type_id from oa_news order by news_time desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String subject=rs.getString("subject");
          if(YHUtility.isNullorEmpty(subject)){
            subject="";
          }
          String typeId=rs.getString("type_id");
          if(YHUtility.isNullorEmpty(typeId)){
            typeId="";
          }
          String content=rs.getString("content");
          if(YHUtility.isNullorEmpty(content)){
            content="";
          }
          String time=rs.getString("news_time");
          if(YHUtility.isNullorEmpty(time)){
            time="";
          }
          String module_id=rs.getString("module_id");
          if(YHUtility.isNullorEmpty(module_id)){
            module_id="";
          }
          String subinfo=rs.getString("subinfo");
          if(YHUtility.isNullorEmpty(subinfo)){
            subinfo="";
          }
          
          map.put("seqId", seqId+"");
          map.put("title", subject);
          map.put("content", content+"<br/>:"+time);
          map.put("search", subject+","+content+","+time);
          map.put("typeId",typeId);
          map.put("time",time);
          map.put("module", "ts_"+module_id);
          map.put("subinfo", subinfo);
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      System.out.println(e);
    }
   
    return dataList;
  } 
  
  
  /**
   * 
   * 获取XZ项目的news资料
   * */
 public List<Map<String,String>> setNewsIndexFromXZ(Connection conn,List<Map<String,String>> dataList){
   Statement stmt=null;
   ResultSet rs=null;
   try{
     String sql=" select seq_id,subject,content,news_time,module_id,subinfo,type_id from ts_news order by news_time desc ";
     stmt = conn.createStatement();
     rs = stmt.executeQuery(sql);
     while(rs.next()){
       Map<String,String> map= new HashMap();
         int seqId=rs.getInt("seq_id");
         String subject=rs.getString("subject");
         if(YHUtility.isNullorEmpty(subject)){
           subject="";
         }
         String typeId=rs.getString("type_id");
         if(YHUtility.isNullorEmpty(typeId)){
           typeId="";
         }
         String content=rs.getString("content");
         if(YHUtility.isNullorEmpty(content)){
           content="";
         }
         String time=rs.getString("news_time");
         if(YHUtility.isNullorEmpty(time)){
           time="";
         }
         String module_id=rs.getString("module_id");
         if(YHUtility.isNullorEmpty(module_id)){
           module_id="";
         }
         String subinfo=rs.getString("subinfo");
         if(YHUtility.isNullorEmpty(subinfo)){
           subinfo="";
         }
         
         map.put("seqId", seqId+"");
         map.put("title", subject);
         map.put("content", content+"<br/>:"+time);
         map.put("search", subject+","+content+","+time);
         map.put("typeId",typeId);
         map.put("time",time);
         map.put("module", "xz_"+module_id);
         map.put("subinfo", subinfo);
         dataList.add(map);
         
       }

     rs.close();
   }catch(SQLException e){
     System.out.println(e);
   }
  
   return dataList;
 } 
  
  
  
  
  public List<Map<String,String>> setDisconIndex(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" SELECT * FROM p_discoun  order by seq_id desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String title=rs.getString("title");
          if(YHUtility.isNullorEmpty(title)){
            title="";
          }
          String content=rs.getString("content");
          if(YHUtility.isNullorEmpty(content)){
            content="";
          }
          String time=rs.getString("add_time");
          if(YHUtility.isNullorEmpty(time)){
            time="";
          }
          
          
          map.put("seqId", seqId+"");
          map.put("title", title);
          map.put("content", content+"<br/>:"+time);
          map.put("search", title+","+content+","+time);
          map.put("typeId", "");
          map.put("time",time);
          map.put("module", "qyh_introduction");
          map.put("subinfo", "");
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      System.out.println(e);
    }
   
    return dataList;
  } 
  
  /**
   * 获取TS项目的资料
   * 
   * */
  public List<Map<String,String>> setDisconIndexFromTs(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" SELECT * FROM p_discoun  order by seq_id desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String title=rs.getString("title");
          if(YHUtility.isNullorEmpty(title)){
            title="";
          }
          String content=rs.getString("content");
          if(YHUtility.isNullorEmpty(content)){
            content="";
          }
          String time=rs.getString("add_time");
          if(YHUtility.isNullorEmpty(time)){
            time="";
          }
          
          
          map.put("seqId", seqId+"");
          map.put("title", title);
          map.put("content", content+"<br/>:"+time);
          map.put("search", title+","+content+","+time);
          map.put("typeId", "");
          map.put("time",time);
          map.put("module", "ts_introduction");
          map.put("subinfo", "");
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      e.printStackTrace();
    }
   
    return dataList;
  } 
  
  /**
   * 获取行政项目的资料
   * 
   * */
  public List<Map<String,String>> setDisconIndexFromXZ(Connection conn,List<Map<String,String>> dataList){
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" SELECT * FROM ts_discoun  order by seq_id desc ";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map= new HashMap();
          int seqId=rs.getInt("seq_id");
          String title=rs.getString("title");
          if(YHUtility.isNullorEmpty(title)){
            title="";
          }
          String content=rs.getString("content");
          if(YHUtility.isNullorEmpty(content)){
            content="";
          }
          String time=rs.getString("add_time");
          if(YHUtility.isNullorEmpty(time)){
            time="";
          }
          
          
          map.put("seqId", seqId+"");
          map.put("title", title);
          map.put("content", content+"<br/>:"+time);
          map.put("search", title+","+content+","+time);
          map.put("typeId", "");
          map.put("time",time);
          map.put("module", "xz_introduction");
          map.put("subinfo", "");
          dataList.add(map);
          
        }

      rs.close();
    }catch(SQLException e){
      e.printStackTrace();
    }
   
    return dataList;
  } 
  
  
  public Analyzer getAnalyzer(){
    Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_30); 
    return luceneAnalyzer;
  }

/*  public IKAnalyzer getIKAnalyzer(){
    IKAnalyzer ikanalyzer = new IKAnalyzer();
  }*/
  
  public List<Map<String,String>> seacher(String queryString,int pageSize,int pageIndex,String condition){
  

    List<Map<String,String>> list =new ArrayList();
    try{
      String indexPath=YHSysProps.getProp("lucene_path");
      File file = new File(indexPath); 
       Directory Index = SimpleFSDirectory.open(file);
      IndexSearcher is = new IndexSearcher(Index);
    //  Query query=QueryParser.parse(queryString,"search",getAnalyzer());
      QueryParser queryParser = new QueryParser(Version.LUCENE_30,"search",this.getAnalyzer());   

      
      //根据关键字构造一个数组   
      String[] key = new String[]{queryString,condition.replace(",", " ")};   
        //同时声明一个与之对应的字段数组   
      String[] fields = {"search","module"};   
        //声明BooleanClause.Occur[]数组,它表示多个条件之间的关系   
       BooleanClause.Occur[] flags=new BooleanClause.Occur[]{BooleanClause.Occur.MUST,BooleanClause.Occur.MUST};   
        //用MultiFieldQueryParser得到query对象   
      Query MultiFieldquery = MultiFieldQueryParser.parse(Version.LUCENE_30,key, fields, flags, this.getAnalyzer());   


      
      Query query = queryParser.parse(queryString);    
     // TopDocs hits = queryParser.search(query, 10);   
      strFilter condFilter=new strFilter("module",condition);   //条件过滤器
      int getNumber = (pageIndex+1)*pageSize;
      TopDocs hits =is.search(MultiFieldquery,condFilter,getNumber);
      Map<String,String> PageMap=new HashMap();
      int number=0;
      int i=0;
      //重新定位
   //   int nn=0;
    /*  while(i<hits.totalHits){
        ScoreDoc scoreDoc = hits.scoreDocs[i];
        Document doc=is.doc(scoreDoc.doc);
        String module=doc.get("module");
        if(condition.indexOf(module)==-1){
         nn++;
        }
        i++;
      }*/
      i=pageSize*pageIndex;
      int total = hits.totalHits;
     while(i<hits.totalHits ){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
        String url="";
        String module=doc.get("module");
       /* if(condition.indexOf(module)!=-1){*/
        String content= doc.get("content");
        if(content.length()>240){
          content= content.substring(0, 239);
        }
    //    content= this.setLightandAnalyzer(this.getAnalyzer(), queryString, content);
        
        String title = doc.get("title");
     //   title= this.setLightandAnalyzer(this.getAnalyzer(), queryString, title);
       /* if(content.length()>240){
          content=content.substring(0, 239);
        }*/
        Map<String,String> map=new HashMap();
        map.put("seqId",doc.get("seq_id"));
        map.put("title",title );
        map.put("content",content);
        map.put("time", doc.get("time"));
        map.put("module", module);
        map.put("subinfo", doc.get("subinfo"));
        map.put("typeId", doc.get("typeId"));
        list.add(map);
        
        if( number>=pageSize-1){
          break;
        }
        number++;
        i++; 
        }
       
 //    }
  /*   i=0;
     number=0;
     while(i<hits.totalHits){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
       String module=doc.get("module");
       if(condition.indexOf(module)!=-1){
         number++;
       }
       i++;
     }*/
     PageMap.put("pageCount", ((total+pageSize-1)/pageSize)+"");
     PageMap.put("pageSize", pageSize+"");
     PageMap.put("pageIndex", pageIndex+"");
     PageMap.put("totalCount",total+"");
      list.add(PageMap);
      is.close();
    }catch(Exception e){
     e.printStackTrace();
    }
    return list;
  }
  
  /**
   * 字段排序
   * 
   * */

  public List<Map<String,String>> seacherWithSortByTimeDesc(String queryString,int pageSize,int pageIndex,String condition){
    

    List<Map<String,String>> list =new ArrayList();
    try{
      String indexPath=YHSysProps.getProp("lucene_path");
      File file = new File(indexPath); 
       Directory Index = SimpleFSDirectory.open(file);
      IndexSearcher is = new IndexSearcher(Index);
    //  Query query=QueryParser.parse(queryString,"search",getAnalyzer());
      QueryParser queryParser = new QueryParser(Version.LUCENE_30,"search",this.getAnalyzer());   
     
         //根据关键字构造一个数组   
        String[] key = new String[]{queryString,condition};   
          //同时声明一个与之对应的字段数组   
        String[] fields = {"search","module"};   
          //声明BooleanClause.Occur[]数组,它表示多个条件之间的关系   
         BooleanClause.Occur[] flags=new BooleanClause.Occur[]{BooleanClause.Occur.MUST,BooleanClause.Occur.MUST};   
          //用MultiFieldQueryParser得到query对象   
        Query MultiFieldquery = MultiFieldQueryParser.parse(Version.LUCENE_30,key, fields, flags, this.getAnalyzer());   

     
      
      Query query = queryParser.parse(queryString);    
     // TopDocs hits = queryParser.search(query, 10);   
      strFilter condFilter=new strFilter("module",condition);   //条件过滤器
      int getNumber = (pageIndex+1)*pageSize;

      Sort timeSort = new Sort(new SortField("long_time",SortField.LONG,true));
      TopDocs hits =is.search(MultiFieldquery,condFilter,getNumber,timeSort);
      Map<String,String> PageMap=new HashMap();
      int number=0;
      int i=0;
      //重新定位
   //   int nn=0;
    /*  while(i<hits.totalHits){
        ScoreDoc scoreDoc = hits.scoreDocs[i];
        Document doc=is.doc(scoreDoc.doc);
        String module=doc.get("module");
        if(condition.indexOf(module)==-1){
         nn++;
        }
        i++;
      }*/
      i=pageSize*pageIndex;
      int total = hits.totalHits;
     while(i<hits.totalHits ){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
        String url="";
        String module=doc.get("module");
       /* if(condition.indexOf(module)!=-1){*/
        String content= doc.get("content");
        if(content.length()>240){
          content= content.substring(0, 239);
        }
    //    content= this.setLightandAnalyzer(this.getAnalyzer(), queryString, content);
        
        String title = doc.get("title");
     //   title= this.setLightandAnalyzer(this.getAnalyzer(), queryString, title);
       /* if(content.length()>240){
          content=content.substring(0, 239);
        }*/
        Map<String,String> map=new HashMap();
        map.put("seqId",doc.get("seq_id"));
        map.put("title",title );
        map.put("content",content);
        map.put("time", doc.get("time"));
        map.put("long_time", doc.get("long_time"));
        map.put("module", module);
        map.put("subinfo", doc.get("subinfo"));
        map.put("typeId", doc.get("typeId"));
        list.add(map);
        
        if( number>=pageSize-1){
          break;
        }
        number++;
        i++; 
        }
       
 //    }
  /*   i=0;
     number=0;
     while(i<hits.totalHits){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
       String module=doc.get("module");
       if(condition.indexOf(module)!=-1){
         number++;
       }
       i++;
     }*/
     PageMap.put("pageCount", ((total+pageSize-1)/pageSize)+"");
     PageMap.put("pageSize", pageSize+"");
     PageMap.put("pageIndex", pageIndex+"");
     PageMap.put("totalCount",total+"");
      list.add(PageMap);
      is.close();
    }catch(Exception e){
     e.printStackTrace();
    }
    return list;
  }


  
  /*高级检索 
   */
  public List<Map<String,String>> seacherWithFilter(String queryString,int pageSize,int pageIndex,String stationId){
     //SortFlag  1 不排序  2 时间升序  3 时间降序
    List<Map<String,String>> list =new ArrayList();
    try{
      String indexPath=YHSysProps.getProp("lucene_path");
      File file = new File(indexPath); 
       Directory Index = SimpleFSDirectory.open(file);
      IndexSearcher is = new IndexSearcher(Index);
    //  Query query=QueryParser.parse(queryString,"search",getAnalyzer());
      QueryParser queryParser = new QueryParser(Version.LUCENE_30,"search",this.getAnalyzer());   

      //根据关键字构造一个数组   
      String[] key = new String[]{queryString,stationId};   
        //同时声明一个与之对应的字段数组   
      String[] fields = {"search","stationId"};   
        //声明BooleanClause.Occur[]数组,它表示多个条件之间的关系   
       BooleanClause.Occur[] flags=new BooleanClause.Occur[]{BooleanClause.Occur.MUST,BooleanClause.Occur.MUST};   
        //用MultiFieldQueryParser得到query对象   
      Query MultiFieldquery = MultiFieldQueryParser.parse(Version.LUCENE_30,key, fields, flags, this.getAnalyzer());   
    //  strFilter condFilter=new strFilter("search",queryString.replace("：", ":"));   //条件过滤器
  
                                       
   //   MutiFilter mutifilter =new  MutiFilter();  // 多条件过滤器
 //     mutifilter.addFilter(condFilter);
   //   MultiFieldquery= mutifilter.getFilterQuery(MultiFieldquery);
      
      int getNumber = (pageIndex+1)*pageSize;
      TopDocs hits=null;
     /* if(!SortFlag.equals("1")){           //使用时间排序  升序
        Sort timeSort= new Sort(new SortField("long_time",SortField.LONG,false));
        
        if(SortFlag.equals("3")){   //时间降序
          timeSort = new Sort(new SortField("long_time",SortField.LONG,true));
        } 
        hits =is.search(MultiFieldquery,getNumber,timeSort);
      }else{  */                             //不使用时间排序
        hits =is.search(MultiFieldquery,getNumber);
    //  }
     
      Map<String,String> PageMap=new HashMap();
      int number=0;
      int i=0;
    
      i=pageSize*pageIndex;
      int total = hits.totalHits;
     while(i<hits.totalHits ){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
        String url="";
        String module=doc.get("module");
        String content= doc.get("content");
        if(content.length()>240){
          content= content.substring(0, 239);
        }
        content= this.setLightandAnalyzer(this.getAnalyzer(), queryString, content);
        
        String title = doc.get("title");
        title= this.setLightandAnalyzer(this.getAnalyzer(), queryString, title);
       /* if(content.length()>240){
          content=content.substring(0, 239);
        }*/
        Map<String,String> map=new HashMap();
        map.put("seqId",doc.get("seq_id"));
        map.put("title",title );
        map.put("content",content);
        map.put("time", doc.get("time"));
        map.put("module", module);
        map.put("subinfo", doc.get("subinfo"));
        map.put("typeId", doc.get("typeId"));
        list.add(map);
        
        if( number>=pageSize-1){
          break;
        }
        number++;
        i++; 
        }
       
 //    }
  /*   i=0;
     number=0;
     while(i<hits.totalHits){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
       String module=doc.get("module");
       if(condition.indexOf(module)!=-1){
         number++;
       }
       i++;
     }*/
     PageMap.put("pageCount", ((total+pageSize-1)/pageSize)+"");
     PageMap.put("pageSize", pageSize+"");
     PageMap.put("pageIndex", pageIndex+"");
     PageMap.put("totalCount",total+"");
      list.add(PageMap);
      is.close();
    }catch(Exception e){
     e.printStackTrace();
    }
    return list;
  }

  
  
  
 /*使用高亮器*/
  public List<Map<String,String>> seacherWithHightLighter(String queryString,int pageSize,int pageIndex){
    List<Map<String,String>> list =new ArrayList();
    try{
      String indexPath=YHSysProps.getProp("lucene_path");
      File file = new File(indexPath); 
       Directory Index = SimpleFSDirectory.open(file);
      IndexSearcher is = new IndexSearcher(Index);
    //  Query query=QueryParser.parse(queryString,"search",getAnalyzer());
      QueryParser queryParser = new QueryParser(Version.LUCENE_30,"search",this.getAnalyzer());   

      //根据关键字构造一个数组   
      String[] key = new String[]{queryString};   
        //同时声明一个与之对应的字段数组   
      String[] fields = {"search"};   
        //声明BooleanClause.Occur[]数组,它表示多个条件之间的关系   
       BooleanClause.Occur[] flags=new BooleanClause.Occur[]{BooleanClause.Occur.MUST};   
        //用MultiFieldQueryParser得到query对象   
      Query MultiFieldquery = MultiFieldQueryParser.parse(Version.LUCENE_30,key, fields, flags, this.getAnalyzer());   

      Query query = queryParser.parse(queryString);    
     // TopDocs hits = queryParser.search(query, 10);   
      strFilter condFilter=new strFilter("stationId","");   //条件过滤器
      int getNumber = (pageIndex+1)*pageSize;
      TopDocs hits =is.search(MultiFieldquery,condFilter,getNumber);
      Map<String,String> PageMap=new HashMap();
      int number=0;
      int i=0;
      //重新定位
   //   int nn=0;
    /*  while(i<hits.totalHits){
        ScoreDoc scoreDoc = hits.scoreDocs[i];
        Document doc=is.doc(scoreDoc.doc);
        String module=doc.get("module");
        if(condition.indexOf(module)==-1){
         nn++;
        }
        i++;
      }*/
      i=pageSize*pageIndex;
      int total = hits.totalHits;
     while(i<hits.totalHits ){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
        String url="";
        String module=doc.get("module");
       /* if(condition.indexOf(module)!=-1){*/
        String content= doc.get("content");
        if(content.length()>240){
          content= content.substring(0, 239);
        }
        content= this.setLightandAnalyzer(this.getAnalyzer(), queryString, content);
        
        String title = doc.get("title");
        title= this.setLightandAnalyzer(this.getAnalyzer(), queryString, title);
       /* if(content.length()>240){
          content=content.substring(0, 239);
        }*/
        Map<String,String> map=new HashMap();
        map.put("seqId",doc.get("seq_id"));
        map.put("title",title );
        map.put("content",content);
        map.put("time", doc.get("time"));
        map.put("module", module);
        map.put("subinfo", doc.get("subinfo"));
        map.put("typeId", doc.get("typeId"));
        list.add(map);
        
        if( number>=pageSize-1){
          break;
        }
        number++;
        i++; 
        }
       
 //    }
  /*   i=0;
     number=0;
     while(i<hits.totalHits){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
       String module=doc.get("module");
       if(condition.indexOf(module)!=-1){
         number++;
       }
       i++;
     }*/
     PageMap.put("pageCount", ((total+pageSize-1)/pageSize)+"");
     PageMap.put("pageSize", pageSize+"");
     PageMap.put("pageIndex", pageIndex+"");
     PageMap.put("totalCount",total+"");
      list.add(PageMap);
      is.close();
    }catch(Exception e){
     e.printStackTrace();
    }
    return list;
  }

  
  
  public List<Map<String,String>> seacher(String queryString,String condition){
  
    List<Map<String,String>> list =new ArrayList();
    try{
      String indexPath=YHSysProps.getProp("lucene_path");
      File file = new File(indexPath); 
       Directory Index = SimpleFSDirectory.open(file);
      IndexSearcher is = new IndexSearcher(Index);
    //  Query query=QueryParser.parse(queryString,"search",getAnalyzer());
      QueryParser queryParser = new QueryParser(Version.LUCENE_30,"search",this.getAnalyzer());   

      //根据关键字构造一个数组   
      String[] key = new String[]{queryString,condition.replace(",", " ")};   
        //同时声明一个与之对应的字段数组   
      String[] fields = {"search","module"};   
        //声明BooleanClause.Occur[]数组,它表示多个条件之间的关系   
       BooleanClause.Occur[] flags=new BooleanClause.Occur[]{BooleanClause.Occur.MUST,BooleanClause.Occur.MUST};   
        //用MultiFieldQueryParser得到query对象   
      Query MultiFieldquery = MultiFieldQueryParser.parse(Version.LUCENE_30,key, fields, flags, this.getAnalyzer());   

      
      Query query = queryParser.parse(queryString);    
     // TopDocs hits = queryParser.search(query, 10);   

      TopDocs hits =is.search(MultiFieldquery,50);
      
     for(int i=0;i<hits.totalHits;i++){
       ScoreDoc scoreDoc = hits.scoreDocs[i];
       Document doc=is.doc(scoreDoc.doc);
        String url="";
        String module=doc.get("module");
        String moduleStr =module.substring(5, module.length());
        if(condition.indexOf(moduleStr)!=-1){
        String content= doc.get("content");
        if(content.length()>240){
          content=content.substring(0, 239);
        }
        Map<String,String> map=new HashMap();
        map.put("seqId",doc.get("seq_id"));
        map.put("title", doc.get("title"));
        map.put("content",content);
        map.put("time", doc.get("time"));
        map.put("module", module);
        map.put("subinfo", doc.get("subinfo"));
        map.put("typeId", doc.get("typeId"));
        list.add(map);
        }
     }
  
      is.close();
    }catch(Exception e){
      System.out.print(e);
    }
    return list;
  }
  
  /*
   *  分词高亮器
   *  
   * */
  public String setLightandAnalyzer(Analyzer analyzer,String queryStr,String content)throws Exception{
     ArrayList strList= this.getAnalyzerStr(analyzer, queryStr);
     for(int i=0;i<strList.size();i++){
       String keyWord =(String) strList.get(i);
       content= this.setHighlighter(keyWord, content, "red");
     }
    return content;
  }
  
  /*分词*/
  public  ArrayList getAnalyzerStr(Analyzer analyzer, String keyWord) throws Exception { 
     ArrayList strList = new ArrayList();
    StringReader reader = new StringReader(keyWord); 
    TokenStream ts =(StopFilter) analyzer.tokenStream(keyWord,reader); 
    CharTermAttribute termAtt = (CharTermAttribute)ts.getAttribute(CharTermAttribute.class);   
      TypeAttribute typeAtt = (TypeAttribute)ts.getAttribute(TypeAttribute.class);   
       while (ts.incrementToken())   
      {   
         strList.add(termAtt.toString());
         //System.out.println("type="+typeAtt.type()+ "     "+termAtt);   
      }   

    return strList;
 } 


  
  
    /*
     * 设置高亮器
     * 
     * */
  public String setHighlighter(String keyWord, String content,String RGB)throws Exception {
       if(YHUtility.isNullorEmpty(RGB)){
         RGB="RED";
       } 
       String  HightWord="<font style=\"font-weight: bold\"  color=\""+RGB+"\">"+keyWord+"</font>";

      content= content.replace(keyWord, HightWord);
    
    return content;
  }


  
}

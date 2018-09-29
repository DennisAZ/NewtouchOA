package yh.core.funcs.message.weixun_share.logic;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.message.weixun_share.data.YHWeixunShare;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHWeiXunShareLogic{
	private static Logger log = Logger.getLogger(YHWeiXunShareLogic.class);
	public void addWeiXunShare(Connection dbConn, YHWeixunShare weiXun) throws Exception {
/*	    try {
	        YHORM orm = new YHORM();
	        orm.saveSingle(dbConn, weiXun);
	      } catch (Exception ex) {
	        throw ex;
	      }*/
		
		PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    /*  //String[] addTime=YHUtility.getDateLimitStr();
	      String sql = "insert into oa_shortmsg_share" +
	          " ( UID,CONTENT,ADDTIME)" +
	          " values( ?,?,?)";
	      ps = dbConn.prepareStatement(sql);
	      ps.setInt(1, weiXun.getuId());
	      ps.setString(2, weiXun.getContent());
	      //ps.setLong(3, addTime);
	      System.out.println(sql);
	      ps.executeUpdate();
	      */
	    	
	    	YHWeixunShare ws =new YHWeixunShare();
	    //	ws.setTopics(topics)
	    //	ws.setAddTime(YHUtility.getCurDateTimeStr());
         //   ws.setContent(content);
	    	
	    	
	    } catch(Exception ex) {
	      throw ex;
	    } finally {
	      YHDBUtility.close(ps, rs, log);
	    }
	}
	
	
	 public String getWeiXunContent(Connection conn,Map request,int userId,int pageNo,int pageSize) throws Exception{
		    YHPageDataList data = toWeiXunJson(conn, request, userId,pageNo,pageSize,false);
		     return data.toJson();
		  }
	 
   public String getWeiXunContentPerson(Connection conn,Map request,int userId,int pageNo,int pageSize) throws Exception{
     YHPageDataList data = toWeiXunJsonPerson(conn, request, userId,pageNo,pageSize,false);
      return data.toJson();
   }
		  
   public String getWeiXunContentMention(Connection conn,Map request,int userId,int pageNo,int pageSize) throws Exception{
     YHPageDataList data = toWeiXunJsonMention(conn, request, userId,pageNo,pageSize,false);
      return data.toJson();
   }
   
   public String getWeiXunContentTopic(Connection conn,Map request,String topic,int pageNo,int pageSize) throws Exception{
     YHPageDataList data = toWeiXunJsonTopic(conn, request, topic,pageNo,pageSize,false);
      return data.toJson();
   }
    

   public  FileInputStream getUserAvatorStream(Connection conn,String seqId,String webPath)throws Exception{
     FileInputStream result = null;
     YHORM orm = new YHORM();
     try{
       YHPerson person = (YHPerson)orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(seqId));
       String avator=person.getAuatar();
       String sex=person.getSex();
       String Filepath=webPath+"/attachment/avatar/"+avator;
       Filepath=this.getFilePath(Filepath);
       File file =new File(Filepath);
       if(!file.exists()){
         String fileStr=webPath+"/core/styles/imgs/avatar/1.gif";
         if("1".equals(sex)){
           fileStr=webPath+"/core/styles/imgs/avatar/g.gif";
         }
         fileStr=this.getFilePath(fileStr);
         file=new File(fileStr);
       }
       result= new FileInputStream(file);
     }catch(Exception ex){
       ex.printStackTrace();
     }
     
     return result;
   }
   
	  
	  /**
	   * 装换路径表示
	   */
	  public String getFilePath(String filePath)throws Exception{

	    return filePath.replaceAll("//", File.separator).replace("/", File.separator).replace("\\", File.separator);
	    
	  }
	  
	 
	 
		 
    public YHPageDataList toWeiXunJson(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
      String whereStr =  "";
      String sql = "select PERSON.SEQ_ID,oa_shortmsg_share.seq_id,oa_shortmsg_share.CONTENT,PERSON.USER_NAME,ADD_TIME,BROADCAST_IDS from oa_shortmsg_share,PERSON " +
      " where  oa_shortmsg_share.USER_ID = PERSON.SEQ_ID and "+YHDBUtility.isFieldNotNull("oa_shortmsg_share.CONTENT")+"  order by ADD_TIME desc";
      String nameStr = "seqId,wxId,content,userName,addtime,broadcastIds";
      YHPageQueryParam queryParam = new YHPageQueryParam();
      queryParam.setNameStr(nameStr);
      queryParam.setPageIndex(pageIndex);
      queryParam.setPageSize(pageSize);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      pageDataList=this.getBroadcast(conn, pageDataList);//获取转播内容
      return pageDataList;
    }
    
    public YHPageDataList toWeiXunJsonPerson(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
      String whereStr =  "";
      String sql = "select PERSON.SEQ_ID,oa_shortmsg_share.seq_id,oa_shortmsg_share.CONTENT,PERSON.USER_NAME,ADD_TIME,BROADCAST_IDS from oa_shortmsg_share,PERSON " +
      " where  oa_shortmsg_share.USER_ID = PERSON.SEQ_ID and  "+YHDBUtility.isFieldNotNull("oa_shortmsg_share.CONTENT")+" and oa_shortmsg_share.user_id='"+userId+"'  order by ADD_TIME desc";
      String nameStr = "seqId,wxId,content,userName,addtime,broadcastIds";
      YHPageQueryParam queryParam = new YHPageQueryParam();
      queryParam.setNameStr(nameStr);
      queryParam.setPageIndex(pageIndex);
      queryParam.setPageSize(pageSize);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      pageDataList=this.getBroadcast(conn, pageDataList);//获取转播内容
      return pageDataList;
    }

    public YHPageDataList toWeiXunJsonMention(Connection conn,Map request,int userId,int pageIndex,int pageSize,boolean isQuery) throws Exception{
      String whereStr =  "";
      String sql = "select PERSON.SEQ_ID,oa_shortmsg_share.seq_id,oa_shortmsg_share.CONTENT,PERSON.USER_NAME,ADD_TIME,BROADCAST_IDS from oa_shortmsg_share,PERSON " +
      " where  oa_shortmsg_share.USER_ID = PERSON.SEQ_ID and "+YHDBUtility.isFieldNotNull("oa_shortmsg_share.CONTENT")+"  and "+YHDBUtility.findInSet(""+userId, "MENTIONED_IDS")+"  order by ADD_TIME desc";
      String nameStr = "seqId,wxId,content,userName,addtime,broadcastIds";
      YHPageQueryParam queryParam = new YHPageQueryParam();
      queryParam.setNameStr(nameStr);
      queryParam.setPageIndex(pageIndex);
      queryParam.setPageSize(pageSize);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      pageDataList=this.getBroadcast(conn, pageDataList);//获取转播内容
      return pageDataList;
    }
    
    
    public YHPageDataList toWeiXunJsonTopic(Connection conn,Map request,String topic,int pageIndex,int pageSize,boolean isQuery) throws Exception{
      String whereStr =  "";
      String sql = "select PERSON.SEQ_ID,oa_shortmsg_share.seq_id,oa_shortmsg_share.CONTENT,PERSON.USER_NAME,ADD_TIME,BROADCAST_IDS from oa_shortmsg_share,PERSON " +
      " where  oa_shortmsg_share.USER_ID = PERSON.SEQ_ID and oa_shortmsg_share.CONTENT like '%"+topic+"%'  order by ADD_TIME desc";
      String nameStr = "seqId,wxId,content,userName,addtime,broadcastIds";
      YHPageQueryParam queryParam = new YHPageQueryParam();
      queryParam.setNameStr(nameStr);
      queryParam.setPageIndex(pageIndex);
      queryParam.setPageSize(pageSize);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      pageDataList=this.getBroadcast(conn, pageDataList);//获取转播内容
      return pageDataList;
    }
	
	public String getweiXunShare(Connection conn){
	  Statement stmt=null;
	  ResultSet rs=null;
	  String data="";
	  try{
	    String sql=" select * from oa_shortmsg_share_topic order by seq_id desc ";
	    stmt=conn.createStatement();
	    rs=stmt.executeQuery(sql);
	    int num=0;
	    while(rs.next()){
	      data+="{";
        data+="seqId:'"+rs.getString("seq_id")+"',";
        data+="name:'"+rs.getString("TOPIC_NAME")+"'";
        data+="},";
        num++;
        if(num>8){
          break;
        }
	    }
	  }catch(Exception e){
	    e.printStackTrace();
	  }
	  if(data.endsWith(",")){
	    data=data.substring(0, data.length()-1);
	  }
	  
	  return data;
	}
	
	/**
	 *brocast 
	 **/
	public YHPageDataList getBroadcast(Connection conn, YHPageDataList data )throws Exception{
	  YHPageDataList reData=new YHPageDataList();
	  reData.setTotalRecord(data.getTotalRecord());
	  for(int i=0;i<data.getRecordCnt();i++){
	     YHDbRecord record = data.getRecord(i);
	     String BroadcastId=(String)record.getValueByName("broadcastIds");
	     String BCContent=this.Broadcast(conn, BroadcastId);//获取转播的内容
	     record.addField("broadcast", BCContent); 
	     reData.addRecord(record);
	  }
   
	  return reData;
	}	
	
	public String Broadcast(Connection dbConn,String wxid) throws NumberFormatException, Exception{
	  if(YHUtility.isNullorEmpty(wxid)){
	    return "";
	  }
	  String bData="";
	   YHORM orm = new YHORM();
	   YHWeixunShare ws =(YHWeixunShare)orm.loadObjSingle(dbConn, YHWeixunShare.class, Integer.parseInt(wxid));
	   
	   bData+=" userName:'"+this.getUserName(dbConn, ws.getUserId()+"")+"', ";
	   bData+=" uid:'"+ws.getUserId()+"', ";
	   bData+=" content:'"+YHUtility.encodeSpecial(ws.getContent())+"', ";
	   bData+=" time:'"+ws.getAddTime()+"', ";
	   bData+=" id:'"+ws.getSeqId()+"', ";
	   bData+=" num:'"+this.getBrocatNum(dbConn, wxid)+"' ";
	   
	   return "{"+bData+"}";
	   
	}
	
	
  public String getBrocatNum(Connection conn,String wxid){
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    try{
      String sql=" select count(*) from oa_shortmsg_share where "+YHDBUtility.findInSet(wxid, "BROADCAST_IDS")+" order by seq_id desc ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        data=rs.getString(1);
      }
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
   
    return data;
  }
	
	 public String getweiXunById(Connection conn,String wxid){
	    Statement stmt=null;
	    ResultSet rs=null;
	    String data="";
	    try{
	      String sql=" select * from oa_shortmsg_share where seq_id='"+wxid+"' order by seq_id desc ";
	      stmt=conn.createStatement();
	      rs=stmt.executeQuery(sql);
	      while(rs.next()){
	        data+="{";
	        data+="seqId:'"+rs.getString("seq_id")+"',";
	     //   data+="name:'"+rs.getString("TOPIC_NAME")+"'";
	        data+="userId:'"+rs.getString("user_id")+"', ";
	        data+="ADD_TIME:'"+rs.getString("ADD_TIME")+"', ";
	        data+="content:'"+rs.getString("content")+"',";
	        data+="userName:'"+this.getUserName(conn, rs.getString("user_id"))+"'";
	        data+="}"; 
	      }
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	   
	    return data;
	  }
	
	
	 public String getDeptName(Connection conn,String dId){
	    String data="";
	    YHORM orm = new YHORM();
	    YHDepartment dp;
      try {
        dp = (YHDepartment)orm.loadObjSingle(conn, YHDepartment.class, Integer.parseInt(dId));
        data=dp.getDeptName();
      } catch (NumberFormatException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	   
	    return data;
	  }
	 
	 
   public String getUserName(Connection conn,String dId){
     String data="";
     YHORM orm = new YHORM();
     YHPerson dp;
     try {
       dp = (YHPerson)orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(dId));
       data=dp.getUserName();
     } catch (NumberFormatException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
     } catch (Exception e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
     }
    
     return data;
   }
	  
}


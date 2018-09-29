package yh.core.oaknow.logic;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

/**
 * 知道管理
 * @author qwx110
 *
 */
public class YHOAKnowManageLogic{

 /**
  * 查找askId的回复的个数
  * @param dbConn
  * @param askId
  * @return
  * @throws Exception
  */
 public int getReplyCount(Connection dbConn, int  askId) throws Exception{
   PreparedStatement ps = null;
   ResultSet rs = null;
   int id = 0;
   String sql = "select count(*) from oa_wiki_ask_answer where ASK_ID =" + askId;
   try{
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      id = rs.getInt(1);
    }
  } catch (SQLException e){
    throw e;
  }finally{
    YHDBUtility.close(ps, rs, null);
  }   
   return id;
 }
 
 /**
  * 查找这个问题的评论的个数
  * @param dbConn
  * @param askId
  * @return
  * @throws Exception
  */
 public int getCommentCount(Connection dbConn, int  askId) throws Exception{
   PreparedStatement ps = null;
   ResultSet rs = null;
   int id = 0;
   String sql = "select count(*) from oa_wiki_comment where ASK_ID =" + askId;
   try{
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      id = rs.getInt(1);
    }
  } catch (SQLException e){
    throw e;
  }finally{
    YHDBUtility.close(ps, rs, null);
  }   
   return id;
 }
 
 /**
  * 知道管理的搜索
  * @param dbConn
  * @param pu
  * @param askId
  * @param status
  * @param startTime
  * @param endTime
  * @param ask
  * @return
  * @throws Exception
  */
 public List<YHOAAsk> getAsks(Connection dbConn, YHPageUtil pu,  String status, String startTime, String endTime, String ask) throws Exception{
   PreparedStatement ps = null;
   ResultSet rs = null;
   List<YHOAAsk> asks = new ArrayList<YHOAAsk>();
   String sql = 
           " select SEQ_ID, ASK, CREATE_TIME, ASK_STATUS,COMMEND from oa_wiki_ask ";
           sql += " where 1=1 ";
           if(YHStringUtil.isNotEmpty(status) && !"-1".equals(status)){
             sql += " and ASK_STATUS = '"+ status +"'";
           }
           if(YHStringUtil.isNotEmpty(endTime)){
             sql += " and " + YHDBUtility.getDateFilter("CREATE_TIME", endTime.trim(), "<=");
           }
           if(YHStringUtil.isNotEmpty(startTime)){
             sql +=  " and " + YHDBUtility.getDateFilter("CREATE_TIME", startTime.trim(), ">=");
           }
           if(YHStringUtil.isNotEmpty(ask.trim())){
             sql += " and ASK like '%"+ YHDBUtility.escapeLike(ask.trim()) +"%' " +YHDBUtility.escapeLike();  
           }
          sql += " order by SEQ_ID DESC" ;   
 // YHOut.println(sql);
  try{
    ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);  
    ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
    rs = ps.executeQuery(); 
    rs.first();   
    rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1);   
    while(rs.next()){      
      YHOAAsk question = new YHOAAsk();
      question.setSeqId(rs.getInt("SEQ_ID"));
      question.setAsk(rs.getString("ASK"));
      question.setCreateDate((Date)rs.getObject("CREATE_TIME"));
      question.setStatus(rs.getInt("ASK_STATUS"));
      question.setCommend(rs.getInt("COMMEND"));
      question.setAskReplyCount(getReplyCount(dbConn, question.getSeqId()));
      if(question.getStatus() == 1){
        question.setCommendCount(getCommentCount(dbConn, question.getSeqId()));
      }else{
        question.setCommendCount(0);
      }
      asks.add(question);
    }
    return asks;
  } catch (SQLException e){
    throw e;
  }finally{
    YHDBUtility.close(ps, rs, null);
  }
 }
 /**
  * 获得总个数
  * @param dbConn
  * @param pu
  * @param status
  * @param startTime
  * @param endTime
  * @param ask
  * @return
  * @throws Exception
  */
 //String temp = YHDBUtility.getDateFilter("SEND_TIME", sendTimeMax, "<="); //to_char(SEND_TIME, 'yyyy-MM-dd hh24:mi:ss')<='2010-07-21 18:02:22'
 //where_str +=" and " + temp;
 public int getCount(Connection dbConn,  String status, String startTime, String endTime, String ask)throws Exception{
   PreparedStatement ps = null;
   ResultSet rs = null;
   String sql=
    
     "select SEQ_ID, ASK, CREATE_TIME, ASK_STATUS from oa_wiki_ask ";
       sql += " where 1=1 ";
       if(YHStringUtil.isNotEmpty(status) && !"-1".equals(status) ){
         sql += " and ASK_STATUS = '"+ status +"'";
       }
       if(YHStringUtil.isNotEmpty(endTime)){
         sql += " and " + YHDBUtility.getDateFilter("CREATE_TIME", endTime.trim(), "<=");
       }
       if(YHStringUtil.isNotEmpty(startTime)){
         sql +=  " and " + YHDBUtility.getDateFilter("CREATE_TIME", startTime.trim(), ">=");
       }
       if(YHStringUtil.isNotEmpty(ask)){
         sql += " and ASK like '%"+ YHDBUtility.escapeLike(ask.trim()) +"%' " + YHDBUtility.escapeLike();  
       }
  sql += " order by SEQ_ID DESC " ;
  try{
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    int cnt = 0;
    while(rs.next()){
      cnt++ ;
    }    
    return cnt;
  } catch (Exception e){
   throw e;
  }finally{
    YHDBUtility.close(ps, rs, null);
  }
 }
 
 /**
  * 获得总数(知道管理)
  * @param dbConn
  * @param status
  * @param startTime
  * @param endTime
  * @param ask
  * @return
  * @throws Exception
  */
 //String temp = YHDBUtility.getDateFilter("SEND_TIME", sendTimeMax, "<="); //to_char(SEND_TIME, 'yyyy-MM-dd hh24:mi:ss')<='2010-07-21 18:02:22'
 //where_str +=" and " + temp;
 public int getManageCount(Connection dbConn,  String status, String startTime, String endTime, String ask)throws Exception{
   PreparedStatement ps = null;
   ResultSet rs = null;
   String sql= "select SEQ_ID, ASK, CREATE_TIME, ASK_STATUS from oa_wiki_ask ";
       sql += " where 1=1 ";
       if(YHStringUtil.isNotEmpty(status) && !"-1".equals(status) ){
         sql += " and ASK_STATUS = '"+ status +"'";
       }
       if(YHStringUtil.isNotEmpty(endTime)){
         sql += " and " + YHDBUtility.getDateFilter("CREATE_TIME", endTime.trim(), "<=");
       }
       if(YHStringUtil.isNotEmpty(startTime)){
         sql +=  " and " + YHDBUtility.getDateFilter("CREATE_TIME", startTime.trim(), ">=");
       }
       if(YHStringUtil.isNotEmpty(ask)){
         sql += " and ASK like '%"+ YHDBUtility.escapeLike(ask.trim()) +"%' " +YHDBUtility.escapeLike();  ;
       }
  sql += " order by SEQ_ID DESC " ;
  try{
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    int cnt =0;
    while(rs.next()){
      cnt ++;
    }
    return cnt;
  } catch (Exception e){
    throw e;
  }finally{
    YHDBUtility.close(ps, rs, null);
  }
 }

 /**
  * 删除问题
  * @param dbConn
  * @param askId
  * @return
  * @throws Exception
  */
 public int deleteAsk(Connection dbConn, int askId, int status)throws Exception{
   PreparedStatement ps = null;
   //ResultSet rs = null;
   String sql =
     "delete from oa_wiki_ask  where SEQ_ID = " + askId;
   //YHOut.println(sql);
   try{
    ps = dbConn.prepareStatement(sql);
    int id = ps.executeUpdate();
    if(status == 0){//如果是未解决的问题，怎直接删除问题和答案     
      deleteAnswer(dbConn, askId, 0);
    }else{         //如果已经解决，则删除答案，删除评论，给用户减分
      int pId =  findUserId(dbConn,askId);
      int k = updatePerson(dbConn, pId);
      if(k != 0){
        //deleteAnswer(dbConn, askId, 1);
        deleteAnswer(dbConn, askId, 0);
      }      
    }
    return id;
  } catch (Exception e){
    throw e;
  }finally{
    YHDBUtility.close(ps, null, null);
  }
 }
 

 /**
  * 删除问题的所有的评论
  * @param dbConn
  * @param askId
  * @return
  * @throws Exception
  */
 public int deleteComment(Connection dbConn, int askId)throws Exception{
   PreparedStatement ps = null;
  // ResultSet rs = null;
   String sql =
     "delete from oa_wiki_comment  where ASK_ID = " + askId;
   //YHOut.println(sql);
   try{
    ps = dbConn.prepareStatement(sql);
    int id = ps.executeUpdate();
    return id;
  } catch (Exception e){
    throw e;
  }finally{
    YHDBUtility.close(ps, null, null);
  }
 }
 /**
  * 删除问题的答案
  * @param dbConn
  * @param askId
  * @param flag    0：一般答案， 1：最佳答案
  * @return
  * @throws Exception
  */
 public int deleteAnswer(Connection dbConn, int askId, int flag)throws Exception{
   PreparedStatement ps = null;
  // ResultSet rs = null;
   int id =0;
   String sql =
     "delete from oa_wiki_ask_answer  where ASK_ID = " + askId;
   if(flag == 1){
     sql += " and GOOD_ANSWER ='1'";
   }
  // YHOut.println(sql);
   ps = dbConn.prepareStatement(sql);
   id = ps.executeUpdate(); 
   return id;
   }
   
 
/**
 * 给用户减少分 
 * @param dbConn
 * @param userId
 * @return
 * @throws Exception
 */
 public int updatePerson(Connection dbConn, int userId)throws Exception{
   PreparedStatement ps = null;
   String sql =
     "update person set SCORE = SCORE - 1 where SEQ_ID =" + userId;
   int id = 0;
  try{
    ps = dbConn.prepareStatement(sql);
    id = ps.executeUpdate();
  } catch (Exception e){
    throw e;
  }finally{
    YHDBUtility.close(ps, null, null); 
  }
   return id;
 }
 /**
  * 查找userid
  * @param dbConn
  * @param askId
  * @return
  * @throws Exception
  */
 public int findUserId(Connection dbConn, int askId)throws Exception{
   PreparedStatement ps = null;
   String sql =
     "select ANSWER_USER from oa_wiki_ask_answer where GOOD_ANSWER='1' and ASK_ID=" + askId;
   ResultSet rs = null;
  try{
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      int id = rs.getInt(1);
      return id;
    }
  } catch (Exception e){
    throw e;
  }finally{
    YHDBUtility.close(ps, null, null); 
  }
  return 0;
 }
}

package yh.core.oaknow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.core.oaknow.data.YHAskAnswer;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.data.YHOAComment;
import yh.core.oaknow.util.YHDateFormatUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;
import yh.core.util.db.YHDBUtility;
/**
 * 与问题回答相关
 * @author qwx110
 *
 */
public class YHOAKnowAnswerLogic{
  /**
   * 查找问题状态
   * @param dbConn
   * @param flag 传进来的问题id
   * @return
   * @throws Exception
   */
  public YHOAAsk findAskStatus(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    YHOAAsk ask = new YHOAAsk();    
    try{
      String sql = "select p.USER_NAME " +
      		"                ,ask.ASK" +
      		"                ,ask.CREATE_TIME" +
      		"                ,ask.ASK_STATUS" +
      		"                ,ask.SEQ_ID" +
      		"                ,ask.CREATOR"+
      		"                ,ask.COMMEND"+
      		"                ,ask.CATEGORIE_ID"+
      		"                ,ask.RELATED_KEYWOED"+
          "                ,ask.RANK"+
          "                ,ask.ASK_COMMENT"+
      		"         from  oa_wiki_ask ask, person p" +
      		"         where ask.CREATOR = p.SEQ_ID" +
      		"         and ask.SEQ_ID = " + askId;
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      if(rs.next()){
         ask.setCreatorName(rs.getString(1));
         ask.setAsk(rs.getString(2));
         ask.setCreateDate((Date)rs.getObject(3));
         ask.setStatus(rs.getInt(4));
         ask.setSeqId(rs.getInt(5));
         ask.setCreator(rs.getString(6));
         ask.setCommend(rs.getInt(7));
         ask.setTypeId(rs.getInt(8));
         ask.setReplyKeyWord(rs.getString(9));
         ask.setRank(rs.getInt(10));
         ask.setAskComment((rs.getString(11)));
      }
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return ask;
  }
  
  /**============================
   * 查找与这个问题相关的问题 
   * @param dbConn
   * @param flag
   * @return
   * @throws Exception
   */
   public List<YHOAAsk> findRefAsk(Connection dbConn, int  flag)throws Exception{
     PreparedStatement ps = null;
     ResultSet rs = null;      
     List<YHOAAsk> askList= new ArrayList<YHOAAsk>(); 
     String sql = "select a.SEQ_ID"
       +",a.CREATOR" 
       +",a.CREATE_TIME"
           +",a.ASK_COMMENT" 
           +",a.ASK" 
           +",a.ASK_STATUS"
         +" from oa_wiki_ask a, oa_wiki_ask b"
         +" where ( a.RELATED_KEYWOED like " + YHStringUtil.dbLike("b.ASK");
     String key = findKeyWord(dbConn, flag);
     if(YHStringUtil.isNotEmpty(key)){
       for(int i=0; i<key.split(" ").length; i++){//concat(concat('%','"+ key.split(" ")[i] +"'),'%')
         sql += "or a.RELATED_KEYWOED like " + YHStringUtil.dbLike("'"+ YHDBUtility.escapeLike(key.split(" ")[i])+"'") + YHDBUtility.escapeLike();
       }                 
     }    
  sql  += " )and b.SEQ_ID = " + flag ;
  sql += " and a.SEQ_ID != b.SEQ_ID";
     
       //YHOut.println(sql);
       ps = dbConn.prepareStatement(sql);
       rs = ps.executeQuery();     
       while(rs.next()){
         YHOAAsk ask = new YHOAAsk();
         ask.setSeqId(rs.getInt(1));
         ask.setCreatorId(rs.getInt(2));
         ask.setCreateDate((Date)rs.getObject(3));
         ask.setAskComment(rs.getString(4));
         ask.setAsk(rs.getString(5));
         ask.setStatus(rs.getInt(6));
         askList.add(ask);
       }
     return askList;
   }
  
 /**
  * 查找这个问题的其他的回答 
  * @param dbConn
  * @param askId 问题id
  * @return
  * @throws Exception
  */
  public List<YHAskAnswer> findOtherAnswer(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;      
    List<YHAskAnswer> answers= new ArrayList<YHAskAnswer>();
    try{
      String sql = "select answer.ANSWER_CONTENT " +
          "                ,answer.GOOD_ANSWER " +
          "                ,answer.ANSWER_USER " +
          "                ,answer.ANSWER_TIME " +
          "               ,answer.SEQ_ID " +
          "               ,p.USER_NAME " +
          "               ,answer.ASK_ID " +
          "        from  person p, oa_wiki_ask_answer answer, oa_wiki_ask ask" +
          "        where p.SEQ_ID = answer.ANSWER_USER" +
          "        and answer.ASK_ID = ask.SEQ_ID" +
          "        and ask.SEQ_ID = " + askId +
          "        and answer.GOOD_ANSWER = 0"+
          "        order by answer.ANSWER_TIME desc";
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHAskAnswer answer = new YHAskAnswer(); 
        answer.setAnswerComment(rs.getString(1));
        answer.setGoodAnswer(rs.getInt(2));
        answer.setAnswerUserId(rs.getString(3));
        answer.setAnswerTime((Date)rs.getObject(4));
        answer.setAnswerId(rs.getInt(5));
        answer.setUserName(rs.getString(6));
        answer.setAskId(rs.getInt(7));
        answers.add(answer);
      }
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return answers;    
  }
  
  /**
   * 查找这个问题的最佳答案
   * @param dbConn
   * @return
   * @throws Exception
   */
  public YHAskAnswer findBetterAnswer(Connection dbConn, int askId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;   
    YHAskAnswer answer = new YHAskAnswer(); 
    String sql = "select answer.ANSWER_CONTENT " +
    		"                ,answer.GOOD_ANSWER " +
    		"                ,answer.ANSWER_USER " +
    		"                ,answer.ANSWER_TIME " +
    		"               ,answer.SEQ_ID " +
    		"               ,p.USER_NAME " +
        "               ,answer.ASK_ID " +
    		"        from  person p, oa_wiki_ask_answer answer, oa_wiki_ask ask" +
    		"        where p.SEQ_ID = answer.ANSWER_USER" +
    		"        and answer.ASK_ID = ask.SEQ_ID" +
    		"        and ask.SEQ_ID = " + askId +
    		"        and answer.GOOD_ANSWER = 1";
    //YHOut.println(sql);
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      answer.setAnswerComment(rs.getString(1));
      answer.setGoodAnswer(rs.getInt(2));
      answer.setAnswerUserId(rs.getString(3));
      answer.setAnswerTime((Date)rs.getObject(4));
      answer.setAnswerId(rs.getInt(5));
      answer.setUserName(rs.getString(6));  
      answer.setAskId(rs.getInt(7));
    }
    return answer;
  }
  
  /**
   * 查找问题的标签
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public String findKeyWord(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    String sql = "select RELATED_KEYWOED from oa_wiki_ask where seq_id = " + askId;
    ResultSet rs = null;
    //YHOut.println(sql);
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      return rs.getString(1);
    }
    return null;    
  }
  
  /**
   * 最佳答案的评论
   * @return
   * @throws Exception
   */
  public List<YHOAComment> findBetterAnswerPingLun(Connection dbConn, int answerId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    List<YHOAComment>  comments = new ArrayList<YHOAComment>();
     try{
      String sql = " select p.user_name" +
       		"                 ,comm.MEMBER" +
       		"                 ,comm.COMMENT_TIME" +
       		"                 ,comm.COMMENTS" +
       		"                 ,comm.ASK_ID" +
       		"                 ,comm.SEQ_ID" +
       		"           from person p ,oa_wiki_comment comm ,oa_wiki_ask_answer ans" +
       		"           where p.SEQ_ID = comm.MEMBER " +
       		"           and  comm.ASK_ID = ans.ASK_ID" +
       		"           and ans.GOOD_ANSWER = 1" +
       		"           and ans.SEQ_ID = " + answerId;
       //YHOut.println(sql);
       ps = dbConn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         YHOAComment comm = new YHOAComment(); 
         comm.setUserName(rs.getString(1));
         comm.setMamber(rs.getString(2));
         comm.setDateTime((Date)rs.getObject(3));
         comm.setComment(rs.getString(4));
         comm.setAskId(rs.getInt(5));
         comm.setCommentId(rs.getInt(6));
         comments.add(comm);
       }
    } catch (Exception e){
     throw e;
    }  finally{
      YHDBUtility.close(ps, rs, null);
    }   
    return comments;
  }
  /**
   * 回答对应id问题
   * @param dbConn
   * @param ans
   * @return
   * @throws Exception
   */
  @SuppressWarnings("deprecation")
  public int  insertAnswer(Connection dbConn, YHAskAnswer ans) throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{      
      String sql = "insert into oa_wiki_ask_answer(ASK_ID, ANSWER_USER, ANSWER_TIME, ANSWER_CONTENT, GOOD_ANSWER) values(?,?,"+ YHDBUtility.currDateTime()+",?,?)" ;
      	
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, ans.getAskId()+"");
      ps.setString(2, ans.getAnswerUserId());     
      ps.setString(3,  ans.getAnswerComment());     
      ps.setString(4, ans.getGoodAnswer()+"");
      id =  ps.executeUpdate();     
      if(id != 0){
         int count = getReplyCount(dbConn, ans.getAskId());
         updateReplyCount(dbConn, ans.getAskId(), count);
      }
    } catch (Exception e){
     throw e;
    }  finally{
      YHDBUtility.close(ps, null, null);
    }      
    return id;
  }
  
  /**
   * 找出问题未askId的问题的回复数
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public int getReplyCount(Connection dbConn, int askId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    try{
      String sql = "select ASK_REPLY_COUNT from oa_wiki_ask where seq_id = " + askId;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
       return rs.getInt(1);
      }
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }    
    return 0;
  }
  /**
   * 更新问题为askId的问题的回复数
   * @param dbConn
   * @param askId
   * @param count
   * @return
   * @throws Exception
   */
  public int updateReplyCount(Connection dbConn, int askId, int count)throws Exception{
    PreparedStatement ps = null;
    int row = 0;
    try{
      String sql  = "update oa_wiki_ask set ASK_REPLY_COUNT =  " + (count++) +" where seq_id ="+askId;
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      row = ps.executeUpdate();
    } catch (Exception e){
     throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }    
    return row;
  }
  
  /**
   * 输入对最佳答案的评论
   * @param dbConn
   * @param comm
   * @return
   * @throws Exception
   */
  public int goodAnswerPingLun(Connection dbConn, YHOAComment comm)throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{     
      String sql = "insert into oa_wiki_comment(MEMBER, ASK_ID, COMMENTS, COMMENT_TIME) values(?,?,?,"+ YHDBUtility.currDateTime()+")";
      		//"         values( '" +comm.getMamber()+"'," + comm.getAskId() +",'" + StringUtil.replaceSQ(comm.getComment()) +"',to_date('" + comm.getDateTimeStr()+"','yyyy-mm-dd hh24:mi:ss'))";
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, comm.getMamber());
      ps.setInt(2, comm.getAskId());
      ps.setString(3, comm.getComment());
      //ps.setDate(3, (java.sql.Date) new Date());
      id = ps.executeUpdate();
    } catch (Exception e){      
      throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }      
    return id;
  }
  /**
   * 把answeId的纪录设置为最佳答案
   * @param dbConn
   * @param askId
   * @param answeId
   * @return
   * @throws Exception
   */
  public int goodAnswer(Connection dbConn,  int answeId)throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "update oa_wiki_ask_answer" +              //把answeId的纪录设置为最佳答案
      		"         set GOOD_ANSWER =1" +
      		 "        where seq_id = "+answeId;
      //YHOut.println(sql);
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
   * 把askId的纪录状态设置为已经解决
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public int changeStatus(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "update oa_wiki_ask" +
      		"         set ASK_STATUS =1," +
      		"         RESOLUTION_TIME = " + YHDBUtility.currDateTime()  +
      		"         where SEQ_ID = "+askId ;
      //YHOut.println(sql);
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
   * 把askId的纪录状态设置为未解决
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public int changeStatusNo(Connection dbConn, int askId)throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "update oa_wiki_ask" +
          "         set ASK_STATUS =0" +
          "         where SEQ_ID = "+askId;
      //YHOut.println(sql);
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
   * 页面上点击采纳答案
   * @param dbConn
   * @param askId
   * @param answeId
   * @return
   * @throws Exception
   */
  public int changeToGoodAnswer(Connection dbConn, int askId, int answeId, int userId) throws Exception{
    int one = goodAnswer(dbConn, answeId);
    if( one !=0 ){
      changeStatus(dbConn, askId);
      addFen(dbConn, userId);
    }
    return one;
  }
 /**
  * 给userId的用户加分
  * @param dbConn
  * @param userId
  * @return
  * @throws Exception
  */
  public int addFen(Connection dbConn, int userId)throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      int fen = findFen(dbConn, userId);
      String sql = "update person " +
      		"         set SCORE = " + ( ++fen )+
      				"     where SEQ_ID = " + userId;
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      //YHOut.println(dbConn.getAutoCommit());
      id = ps.executeUpdate();     
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
  
  /**
   * 给userId的用户减分
   * @param dbConn
   * @param userId
   * @return
   * @throws Exception
   */
   public int delFen(Connection dbConn, int userId)throws Exception{
     PreparedStatement ps = null;
     int id = 0;
     try{
       int fen = findFen(dbConn, userId);
       String sql = "update person " +
           "         set SCORE = " + (--fen)+
               "     where SEQ_ID = " + userId;
       ps = dbConn.prepareStatement(sql);
       id = ps.executeUpdate();
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(ps, null, null);
     }
     return id;
   }
 /**
  * 查找userId用户的分数 
  * @param dbConn
  * @param userId
  * @return
  * @throws Exception
  */
  public int findFen(Connection dbConn, int userId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    int id = 0;
    try{
      String sql = "select SCORE from person where SEQ_ID="+userId;
      ps = dbConn.prepareStatement(sql);
      rs =ps.executeQuery();
      if(rs.next()){
         return rs.getInt(1);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return id;
  }
  
  /**
   * 设置askId的问题为推荐状态 
   * @param dbConn
   * @param askId
   * @param flag : 0 取消推荐， 1 推荐
   * @return
   * @throws Exception
   */
  public int tuiJianStatus(Connection dbConn, int askId, int flag) throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "update oa_wiki_ask" +
      		"         set COMMEND = " + flag +
      		"         where SEQ_ID = " + askId;     
      ps = dbConn.prepareStatement(sql);     
      id = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
  
  /**
   * 删除问题为askId的答案
   * @param dbConn
   * @param answerId
   * @return
   * @throws Exception
   */
  public int deleteAnswer(Connection dbConn, int answerId ) throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "delete from oa_wiki_ask_answer where SEQ_ID = " + answerId;
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
   * 删除最佳答案的评论
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public int deleteCommet(Connection dbConn, int askId) throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "delete from oa_wiki_comment where ASK_ID = " + askId;
      ps = dbConn.prepareStatement(sql);     
      id = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
 
  /**
   * 问题编辑页面的删除问题
   * @param dbConn
   * @param answerId
   * @param askId
   * @param flag
   * @param userId
   * @return
   * @throws Exception
   */
  public int deteteAnswerByFlag(Connection dbConn, int answerId, int askId, int flag, int userId)throws Exception{   
    int id = 0;
    try{
      if(flag == 1){ //删除最佳答案
        int i = deleteAnswer(dbConn, answerId);//删除这个答案
        int k = changeStatusNo(dbConn, askId);//把这个问题设为未解决
        int j = delFen(dbConn, userId);        //回答者减分
        deleteCommet(dbConn, askId);   //删除最佳答案的评论
        tuiJianStatus(dbConn, askId, 0);//把推荐状态设为0
        id = i * j * k;
      }else if(flag == 0){
      id =  deleteAnswer(dbConn, answerId);//一般的答案直接删除
      }      
    }catch(Exception e){
      throw e;
    }
    return id;
  }
  
  /**
   * 问题编辑页面的采纳答案问题
   * @param dbConn
   * @param goodAnswerId 旧的最佳答案
   * @param newAnswerId  新的最佳答案
   * @param oldUserId
   * @param newUserId
   * @param askId
   * @return
   * @throws Exception
   */
  public int agreeToGoodAnswer(Connection dbConn, int goodAnswerId, int newAnswerId, int oldUserId, int newUserId, int oldAskId, int newAskId) throws Exception{
    int id = 0;
    if(goodAnswerId != 0){        //有最佳答案
    id = changeToGoodAnswer(dbConn, newAskId, newAnswerId, newUserId); //1.把新的答案设为已解决，并给回答者加分
    int id2 =  changeToNoGoodAnswerByAdmin(dbConn, goodAnswerId, oldUserId, oldAskId); //2.把以前的最佳答案设为普通答案，并给回答者减分，删除评论
    return id * id2;
    }else{                       //没有最佳答案
      id = changeToGoodAnswer(dbConn, newAskId, newAnswerId, newUserId); 
      return id;
    }
  }
  
  /**
   * 问题编辑页面的管理员采纳答案问题
   * @param dbConn
   * @param oldAnsweId
   * @param oldUserId
   * @param askId
   * @return
   * @throws Exception
   */
  public int changeToNoGoodAnswerByAdmin(Connection dbConn, int oldAnsweId, int oldUserId, int askId)throws Exception{
   int i = changeToNoGoodAnswer(dbConn, oldAnsweId);    //把以前的最佳答案设为普通答案
   int j = delFen(dbConn, oldUserId);                   //并给回答者减分
       deleteCommet(dbConn, askId);                 //删除评论
   return i*j;
  }
  
  /**
   * 把answeId的纪录设置为普通答案
   * @param dbConn
   * @param askId
   * @param answeId
   * @return
   * @throws Exception
   */
  public int changeToNoGoodAnswer(Connection dbConn,  int answeId)throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "update oa_wiki_ask_answer" +              //把answeId的纪录设置为最佳答案
          "         set GOOD_ANSWER =0" +
           "        where seq_id = "+answeId;
      //YHOut.println(sql);
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
   * 删除id为commentId的评论
   * @param dbConn
   * @param commentId
   * @return
   * @throws Exception
   */
  public int deleteComment(Connection dbConn,  int commentId)throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    try{
      String sql = "delete from oa_wiki_comment where SEQ_ID = " + commentId;
      ps = dbConn.prepareStatement(sql);     
      id = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;    
  }
  /**
   * 更新答案的内容
   * @param dbConn
   * @param commentId 答案的id
   * @param answer    答案的内容
   * @return
   * @throws Exception 
   */
  public int changeAnswer(Connection dbConn,  int commentId, String answer) throws Exception{
    PreparedStatement ps = null;
    int id = 0;   
    String sql = "update oa_wiki_ask_answer"
       +" set ANSWER_CONTENT = '" + answer +"'"
       +" where SEQ_ID = " + commentId;
    //YHOut.println(sql);
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
   * 更新问题的内容
   * @param dbConn
   * @param ask
   * @return
   * @throws Exception
   */
  public int changeAsk(Connection dbConn, YHOAAsk ask) throws Exception {
    PreparedStatement ps = null;
    int id = 0;
    String sql = "update oa_wiki_ask"
      + " set ASK =?"
      + " ,CATEGORIE_ID = ?" 
      + " ,RELATED_KEYWOED = ?"
      + " ,ASK_COMMENT = ?" 
      + " where SEQ_ID = " + ask.getSeqId();
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, ask.getAsk());
      ps.setInt(2, ask.getTypeId());
      ps.setString(3, ask.getReplyKeyWord());
      ps.setString(4, ask.getAskComment());    
      id = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
  
  /**
   * 删除问题
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public int deleteAskById(Connection dbConn, int askId) throws Exception {
    PreparedStatement ps = null;
    int id = 0;
    String sql = "delete from oa_wiki_ask where SEQ_ID = " + askId;
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      id = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
 
  /**
   * 删除问题所对应的答案
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public int deleteAnswerById(Connection dbConn, int askId) throws Exception{
    PreparedStatement ps = null;
    int id = 0;
    String sql = "delete from oa_wiki_ask_answer where ASK_ID =" + askId;
    //YHOut.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      id = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return id;
  }
  
  /**
   * 删除
   * @param dbConn
   * @param askId
   * @return
   * @throws Exception
   */
  public int deleteMyAsk(Connection dbConn, int askId) throws Exception{
    int one = 0;
    try{
      one = deleteAskById(dbConn, askId);
      deleteAnswerById(dbConn, askId);
    } catch (Exception e){
     throw e;
    }
    return one;
  }
}

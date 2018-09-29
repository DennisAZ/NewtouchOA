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
import yh.core.oaknow.util.YHOAToJsonUtil;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

/**
 * 增加新的问题
 * @author qwx110
 *
 */
public class YHOAAskQuestionLogic{
  /**
   * 查找与新问题的相关问题
   * @param dbConn
   * @param askName
   * @return
   * @throws SQLException 
   */
  public List<YHOAAsk> findAsksByPage(Connection dbConn, String askName, YHPageUtil pu) throws SQLException{
    List<YHOAAsk> askList;
    PreparedStatement ps = null;
    ResultSet rs = null; 
    try{
      askList = new ArrayList<YHOAAsk>();      
       String sql = 
        		               "select SEQ_ID" 
                          + ",CREATOR" 
                          + " ,CREATE_TIME" 
                          + ",ASK_COMMENT" 
                          + ",ASK" 
                          + ",ASK_REPLY_COUNT" 
                          + ",RELATED_KEYWOED" 
                          + ",ASK_STATUS" 
                          + ",CATEGORIE_ID" 
                          + ",COMMEND"                          
                          + " from oa_wiki_ask"
                          + " where ASK_STATUS = " + 1
                          + " and RELATED_KEYWOED like '%" + YHDBUtility.escapeLike(askName) + "%' " + YHDBUtility.escapeLike() ;  
       //YHOut.println(sql);
       ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
       ps.setMaxRows(pu.getCurrentPage() * pu.getPageSize());
       rs = ps.executeQuery();
       rs.first();   
       rs.relative((pu.getCurrentPage()-1) * pu.getPageSize() -1); 
       while(rs.next()){
         YHOAAsk ask = new YHOAAsk();
         ask.setSeqId(rs.getInt(1));
         ask.setCreator(rs.getString(2));
         ask.setCreateDate((Date)rs.getObject(3));
         ask.setAskComment(rs.getString(4));
         ask.setAsk(rs.getString(5));
         ask.setAskReplyCount(rs.getInt(6));
         ask.setReplyKeyWord(rs.getString(7));
         ask.setStatus(rs.getInt(8));
         ask.setTypeId(rs.getInt(9));
         ask.setCommend(rs.getInt(10));
         askList.add(ask);
       }
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return askList;
  }
  
  public String findAsks(Connection dbConn,String askName, YHPageUtil pu) throws SQLException{
    List<YHOAAsk> asks = findAsksByPage(dbConn, askName, pu);
    return YHOAToJsonUtil.toJson(asks);
  }
 /**
  *  查找与新问题的相关问题的总的记录数
  * @param dbConn
  * @param askName
  * @return
  * @throws Exception
  */
  public int findAsksCount(Connection dbConn, String askName) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = 
               "select SEQ_ID" 
                    + ",CREATOR" 
                    + " ,CREATE_TIME" 
                    + ",ASK_COMMENT" 
                    + ",ASK" 
                    + ",ASK_REPLY_COUNT" 
                    + ",RELATED_KEYWOED" 
                    + ",ASK_STATUS" 
                    + ",CATEGORIE_ID" 
                    + ",COMMEND"   
                    + " from oa_wiki_ask"
                    + " where ASK_STATUS = " + 1
                    + " and RELATED_KEYWOED like '%" 
                    + YHDBUtility.escapeLike(askName) + "%' " + YHDBUtility.escapeLike();  
                   
    //YHOut.println(sql);
   
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      int cnt = 0;
      while(rs.next()){
        cnt ++ ;
      }
      return cnt;
    } catch (SQLException e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }   
  }
}

package yh.core.oaknow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.util.YHDateFormatUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

/**
 * 知道导入
 * @author qwx110
 *
 */
public class YHOAKnowInputLogic{
  
  /**
   * 知道录入
   * @param dbConn
   * @param ask
   * @return
   * @throws Exception
   */
  public int insertNewAsk(Connection dbConn, YHOAAsk ask) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "insert into oa_wiki_ask(CREATOR ,CREATE_TIME ,ASK_COMMENT ,ASK ,RELATED_KEYWOED, CATEGORIE_ID, ask_status, ASK_REPLY_COUNT, RESOLUTION_TIME) values(?,"+ YHDBUtility.currDateTime()+",?,?,?,?,'1',1, "+ YHDBUtility.currDateTime() +")";
    try{
      //YHOut.println(sql);
      String[] str = {"SEQ_ID"};
      ps = dbConn.prepareStatement(sql, str);
      //YHOut.println(ask.getCreator());
      ps.setString(1, ask.getCreator());     
      ps.setString(2, YHStringUtil.replaceSQ(ask.getAskComment()));
      ps.setString(3, YHStringUtil.replaceSQ(ask.getAsk()));
      ps.setString(4, YHStringUtil.replaceSQ(ask.getReplyKeyWord()));
      ps.setInt(5, ask.getTypeId());
 
      int id = ps.executeUpdate(); 
      if(id != 0){
        rs = ps.getGeneratedKeys();
        if(rs.next()){
          ask.setSeqId(rs.getInt(1));
        }
        insertAnswer(dbConn, ask);
        addFen(dbConn, ask);
      }
      return id;
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
  }
  /**
   * 插入答案
   * @param dbConn
   * @param ask
   * @return
   * @throws Exception
   */
  public int insertAnswer(Connection dbConn, YHOAAsk ask)throws Exception{
    PreparedStatement ps = null;  
    String sql = "insert into oa_wiki_ask_answer(ASK_ID,ANSWER_USER,ANSWER_TIME,ANSWER_CONTENT,GOOD_ANSWER) values(?,?,"+ YHDBUtility.currDateTime() +",?,1)";
    try{
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, ask.getSeqId());
      ps.setString(2, ask.getCreator());     
      ps.setString(3, ask.getAnswer());
      int id = ps.executeUpdate();
      return id;
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  /**
   * 用户加分
   * @param dbConn
   * @param ask
   * @return
   * @throws Exception
   */
  public int addFen(Connection dbConn, YHOAAsk ask)throws Exception{
    PreparedStatement ps = null;  
    String sql = "update person set score = score + 1 where seq_id = " + ask.getCreatorId();
    try{
      //YHOut.println(sql);
      ps = dbConn.prepareStatement(sql);
      int id = ps.executeUpdate();
      return id;
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
}

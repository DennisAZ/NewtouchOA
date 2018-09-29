package yh.subsys.oa.vote.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.vote.data.YHVoteItem;

public class YHVoteItemLogic {
  private static Logger log = Logger.getLogger(YHVoteItemLogic.class);
  /**
   * 新增
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static int addItem(Connection dbConn,YHVoteItem item) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, item);
    return 0;
  }
  /**
   * 编辑 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateItem(Connection dbConn,YHVoteItem item) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, item);
  }
  /**
   * 编辑 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateItem(Connection dbConn,String seqId,String itemName) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "update  oa_vote_item set ITEM_NAME = ? where SEQ_ID = " + seqId ;
    try {
      pstmt = dbConn.prepareStatement(sql);
      pstmt.setString(1, itemName);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
  /**
   * 编辑 更新投票人和票数
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateItemUserId(Connection dbConn,int seqId,String anonymity,String voteUser) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "";
    if(!YHUtility.isNullorEmpty(anonymity)&& anonymity.equals("1")){
      sql = "update oa_vote_item set VOTE_COUNT=VOTE_COUNT+1  where SEQ_ID = " + seqId ;
    }else{
      sql = "update oa_vote_item set VOTE_COUNT=VOTE_COUNT+1, VOTE_USER = '"+voteUser+"' where SEQ_ID = " + seqId ;
    }
   try {
      pstmt = dbConn.prepareStatement(sql);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
  /**
   * 删除By seqId
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delItemById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHVoteItem.class, Integer.parseInt(seqId));
  }
  /**
   * 删除ByVoteIds
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delItemByVoteIds(Connection dbConn,String voteIds) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String sql = "delete from oa_vote_item where VOTE_ID in(" + voteIds + ")";
   try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
   }catch(Exception ex) {
     throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
  }
  /**
   * 删除ByVoteIds
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delAllItem(Connection dbConn) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String sql = "delete from oa_vote_item ";
   try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
   }catch(Exception ex) {
     throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
  }
  
  /**
   * 更新itemByVoteIds
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateItemByVoteIds(Connection dbConn,String voteIds) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String sql = "update  oa_vote_item set VOTE_COUNT=0, VOTE_USER='' where vote_id in (" + voteIds + ")";
   try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
   }catch(Exception ex) {
     throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
  }
  /**
   * 查询By seqId
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static YHVoteItem selectItemById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHVoteItem item = (YHVoteItem) orm.loadObjSingle(dbConn, YHVoteItem.class, Integer.parseInt(seqId));
    return item;
  }
  /**
   * 按条件查询
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static List<YHVoteItem> selectItem(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHVoteItem>  itemList = new ArrayList<YHVoteItem>();
    itemList = orm.loadListSingle(dbConn, YHVoteItem.class, str);
    return itemList;
  }
  /**
   * 查询投票数量
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static String getCount(Connection dbConn,String voteId) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String totalCount = "0";
   String maxCount = "0";
   String sql = "select sum(VOTE_COUNT),max(VOTE_COUNT) from oa_vote_item where VOTE_ID = " + voteId;
   try {
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       if(!YHUtility.isNullorEmpty(rs.getString(1))){
         totalCount = rs.getString(1);
         maxCount = rs.getString(2);
       }
     }
   }catch(Exception ex) {
     throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
   return totalCount + "," + maxCount ;
  }
}

package yh.subsys.oa.vote.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.vote.data.YHVoteData;

public class YHVoteDataLogic {
  private static Logger log = Logger.getLogger(YHVoteDataLogic.class);
  /**
   * 新建
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static int addData(Connection dbConn,YHVoteData data) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, data);
    return 0;
  }
  /**
   * 编辑 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateData(Connection dbConn,YHVoteData data) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, data);
  }
  /**
   * 删除BySeqId
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delDataById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHVoteData.class, Integer.parseInt(seqId));
  }
  /**
   * 删除ByItemIds
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delDataByItemIds(Connection dbConn,String itemIds,String type) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String sql = "delete from oa_vote_data where ITEM_ID in(" + itemIds + ")";
   if(!YHUtility.isNullorEmpty(type)){
     if(type.equals("0")){
       sql = sql + " and FIELD_NAME = '0'";
     }else{
       sql = sql + " and FIELD_NAME <> '0'";
     }
   }
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
   * 全部删除
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delAllData(Connection dbConn) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String sql = "delete from oa_vote_data";
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
   * 查询BySeqId
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static YHVoteData selectDataById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHVoteData data = (YHVoteData) orm.loadObjSingle(dbConn, YHVoteData.class, Integer.parseInt(seqId));
    return data;
  }
  /**
   * 删除ByItemIds
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static List<YHVoteData> selectDataByItemId(Connection dbConn,String itemId,String fieldName) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String sql = "select * from oa_vote_data where ITEM_ID = " + itemId;

   List<YHVoteData> dataList = new ArrayList<YHVoteData>();
   if(YHUtility.isInteger(itemId)){
     sql = sql + " and FIELD_NAME = '"+fieldName+"'";
   }
   try {
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     while(rs.next()){
       YHVoteData data =  new YHVoteData();
       data.setSeqId(rs.getInt("seq_id"));
       data.setFieldData(rs.getString("field_data"));
       data.setFieldName(rs.getString("field_name"));
       data.setItemId(rs.getInt("item_id"));
       dataList.add(data);
     }
   }catch(Exception ex) {
     throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
   return dataList;
  }
}

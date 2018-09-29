package yh.subsys.oa.hr.score.logic;

import java.sql.Connection;

import yh.core.util.db.YHORM;
import yh.subsys.oa.hr.score.data.YHScoreItem;

public class YHScoreItemLogic {
  
  /**
   * 增加考核指标集明细--cc
   * @param dbConn
   * @param scoreItem
   * @throws Exception
   */
  public void addScoreFlow(Connection dbConn, YHScoreItem scoreItem) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, scoreItem);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 删除一条记录--cc
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteItem(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHScoreItem.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 修改考核指标集明细--cc
   * @param conn
   * @param scoreItem
   * @throws Exception
   */
  public void updateScoreItem(Connection conn, YHScoreItem scoreItem) throws Exception {
    try {
        YHORM orm = new YHORM();
        orm.updateSingle(conn, scoreItem);
      } catch (Exception ex) {
        throw ex;
      } finally {
    }
  }
  
  public YHScoreItem getItemBId(Connection dbConn,int seqId)throws Exception {
    YHORM orm = new YHORM();
    YHScoreItem item  = (YHScoreItem) orm.loadObjSingle(dbConn, YHScoreItem.class, seqId);
    return item;
  }
  
}

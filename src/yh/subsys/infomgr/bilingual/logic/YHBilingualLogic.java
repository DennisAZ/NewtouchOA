package yh.subsys.infomgr.bilingual.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.infomgr.bilingual.data.YHBilingual;

public class YHBilingualLogic {
        
  private static Logger log = Logger.getLogger(YHBilingualLogic.class);
 
  /**
   * 增加一条记录
   * @param conn
   * @return
   * @throws Exception
   */
  public void addBilingual(Connection conn,YHBilingual bi) throws Exception{
    
    try{
      YHORM orm = new YHORM();
      orm.saveSingle(conn, bi);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  public void modifyBilingual(Connection conn,YHBilingual bi) throws Exception{
    
    try{
      YHORM orm = new YHORM();
      orm.updateSingle(conn, bi);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  /**
   * 删除一条记录
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public boolean deleteRecord(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String sql = "delete from BILINGUAL" +
      		" where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      return ps.executeUpdate() > 0;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 启用/不启用
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public boolean setEnable(Connection conn, int seqId,String enable) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String sql = "update BILINGUAL" +
      		" set ENABLE = ?" +
      		" where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, enable);
      ps.setInt(2, seqId);
      return ps.executeUpdate() > 0;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public YHBilingual queryRecord(Connection conn,int seqId) throws Exception{
    try{
      YHORM orm = new YHORM();
      return (YHBilingual)orm.loadObjSingle(conn, YHBilingual.class, seqId);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
}

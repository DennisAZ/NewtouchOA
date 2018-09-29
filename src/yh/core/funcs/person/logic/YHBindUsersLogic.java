package yh.core.funcs.person.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHBindUsersLogic {
  private static Logger log = Logger.getLogger(YHBindUsersLogic.class);

  /**
   * 查看用户是否已经绑定了其他系统的账号
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public boolean isBind(Connection conn, int userId)throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select count(*)" +
      		" from oa_bind_persons" +
      		" where USER_SEQ_ID = ?"; 
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userId);
      rs = ps.executeQuery();
      
      int num = 0;
      if(rs.next()){      
        num = rs.getInt(1);
      }
      
      return num > 0;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 查看用户是否已经绑定了其他系统的账号
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String queryBindInfo(Connection conn, int userId)throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select USER_ID_OTHER" +
      		",USER_DESC_OTHER" +
      		" from oa_bind_persons" +
      		" where USER_SEQ_ID = ?"; 
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userId);
      rs = ps.executeQuery();
      
      String desc = null;
      String id = null;
      if (rs.next()){
        desc = rs.getString("USER_DESC_OTHER");
        id = rs.getString("USER_ID_OTHER");
      }
      
      if (desc == null){
        desc = "";
      }
      
      if (id == null){
        id = "";
      }
      
      return "{\"desc\":\"" + desc + "\",\"id\":\"" + id + "\"}";
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 绑定用户(插入值)
   * @param conn
   * @param userId
   * @param userIdOther
   * @param userDescOther
   * @param sysId
   * @throws Exception
   */
  public void bindUser(Connection conn, int userId, String userIdOther, String userDescOther,String sysId)throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "insert into oa_bind_persons" +
      		" (USER_SEQ_ID" +
      		", SYS_ID" +
      		", USER_ID_OTHER" +
      		", USER_DESC_OTHER)" +
      		" values (?, ?, ?, ?)";
      
      ps = conn.prepareStatement(sql);
      
      ps.setInt(1, userId);
      ps.setString(2, sysId);
      ps.setString(3, userIdOther);
      ps.setString(4, userDescOther);
      
      ps.executeUpdate();
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 重新绑定用户(更新值)
   * @param conn
   * @param userId
   * @param userIdOther
   * @param userDescOther
   * @param sysId
   * @throws Exception
   */
  public void rebindUser(Connection conn, int userId, String userIdOther, String userDescOther,String sysId)throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update oa_bind_persons" +
      " set SYS_ID = ?" +
      ", USER_ID_OTHER = ?" +
      ", USER_DESC_OTHER = ?" +
      " where USER_SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      
      ps.setString(1, sysId);
      ps.setString(2, userIdOther);
      ps.setString(3, userDescOther);
      ps.setInt(4, userId);
      
      ps.executeUpdate();
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 解除绑定用户
   * @param conn
   * @param userId
   * @param userIdOther
   * @param userDescOther
   * @param sysId
   * @throws Exception
   */
  public void removeBind(Connection conn, int userId)throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "delete from oa_bind_persons" +
      		" where USER_SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      
      ps.setInt(1, userId);
      
      ps.executeUpdate();
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
}

package yh.core.esb.server.system.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import yh.core.esb.server.user.data.TdUser;
import yh.core.util.auth.YHDigestUtility;
import yh.core.util.db.YHDBUtility;

public class SystemLoginLogic {

  public boolean validateUser(Connection dbConn , String userCode){
    
    String sql = " SELECT 1 FROM td_user where user_code ='" + userCode +"'";
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      return rs.next();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return false;
  }
  
  public boolean checkPwd(Connection dbConn , String userCode , String pwd){
    String pwdMd5Str = YHDigestUtility.md5Hex(pwd.getBytes());
    String sql = " SELECT seq_id , password  FROM td_user where user_code ='" + userCode +"'";
    TdUser user = new TdUser();
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        user.setSeqId(rs.getInt("seq_id"));
        user.setPassword(rs.getString("password"));
      }
      return pwdMd5Str.equals(user.getPassword());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return false;
  } 
  
  public TdUser queryPerson(Connection dbConn, String userCode){
    
    String sql = " SELECT * FROM td_user where user_code ='" + userCode +"'";
    TdUser user = new TdUser();
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        user.setSeqId(rs.getInt("seq_id"));
        user.setUserCode(rs.getString("user_code"));
        user.setUserName(rs.getString("user_name"));
        user.setDescription(rs.getString("description"));
        user.setAppId(rs.getInt("app_id"));
        user.setUserType(rs.getInt("user_type"));
        user.setStatus(rs.getInt("status"));
        user.setIsOnline(rs.getInt("is_online"));
        user.setOnlineIp(rs.getString("online_Ip"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return user;
  }
    
}

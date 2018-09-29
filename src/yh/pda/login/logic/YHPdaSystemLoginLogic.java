package yh.pda.login.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import yh.core.esb.server.user.data.TdUser;
import yh.core.util.auth.YHDigestUtility;
import yh.core.util.db.YHDBUtility;

public class YHPdaSystemLoginLogic {
  
  public boolean validateUser(Connection dbConn , String userId){
    String sql = " SELECT 1 FROM person where USER_ID ='" + userId +"'";
    
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
  
  public boolean checkPwd(Connection dbConn , String userId , String pwd){
    String pwdMd5Str = YHDigestUtility.md5Hex(pwd.getBytes());
    String sql = " SELECT seq_id , password FROM person where USER_ID ='" + userId +"'";
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
}

package yh.subsys.portal.bjca.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHBjcaSSOLogic {
  public String getUserNameByCa(Connection conn,String userId) throws Exception{
    String sql = "select USER_ID FROM PERSON WHERE UNIQUE_ID='" + userId + "'";
    Statement st = null;
    ResultSet rs = null;
    String result = "";
    try {
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      if(rs.next()){
        result =  rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(st, rs, null);
    }
    return YHUtility.encodeSpecial(result);
  }
}

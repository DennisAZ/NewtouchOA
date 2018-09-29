package yh.rad.dbexputil.transplant.logic.core.util.raw.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.processor.YHSpecialHandler;
import yh.rad.dbexputil.transplant.logic.core.util.db.YHTransplantUtil;

public class YHSPHandler implements YHSpecialHandler {
  /**
   * 
   * @param oldConn
   * @param value
   * @return
   * @throws Exception
   */
  public String getUserId(Connection oldConn,String value) throws Exception{
    String sql = " select UID from user WHERE USER_ID = '" + value + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String uid = "";
    try {
      ps = oldConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        uid = rs.getString(1);
      }else{
        uid = "-1";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return uid;
  }

  @Override
  public Object excute(Object value, HashMap<String, String> params)
      throws Exception {
    String dbType = params.get("dbType");
    Connection conn = null;
    String result = null;
    try {
      conn = YHTransplantUtil.getDBConn2(false, Integer.valueOf(dbType));
      if(value == null || "".equals(value.toString().trim())){
        return "";
      }
      result = getUserId(conn, value.toString());
    } catch (Exception e) {
      throw e;
    } finally {
      if(conn != null){
        conn.commit();
      }
    }
    return result;
  }
}

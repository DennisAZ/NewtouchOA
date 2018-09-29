package yh.rad.dbexputil.transplant.logic.core.util.raw.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.processor.YHSpecialHandler;
import yh.rad.dbexputil.transplant.logic.core.util.db.YHTransplantUtil;

public class YHSPHandler2 implements YHSpecialHandler {
  /**
   * 
   * @param oldConn
   * @param value
   * @return
   * @throws Exception
   */
  public String getUserId2(Connection oldConn,String value) throws Exception{
    YHSPHandler sphd = new YHSPHandler();
    String userNames = value.toString();
    String[] userNameArray = userNames.split(",");
    String result = "";
    for (String string : userNameArray) {
      if(!"".equals(result)){
        result += ",";
      }
      result += sphd.getUserId(oldConn, string);
    }
    return result;
  }
  @Override
  public Object excute(Object value, HashMap<String, String> params)
      throws Exception {
    String dbType = params.get("dbType");
    Connection conn = null;
    String result = null;
    try {
      conn =  YHTransplantUtil.getDBConn2(false, Integer.valueOf(dbType));
      if(value == null || "".equals(value.toString().trim())){
        return "";
      }
      result = getUserId2(conn, value.toString());
    } catch (Exception e) {
      throw e;
    } finally{
      if(conn != null){
        conn.commit();
      }
    }
    
    return result;
  }

}

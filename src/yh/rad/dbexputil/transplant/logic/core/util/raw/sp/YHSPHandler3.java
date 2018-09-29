package yh.rad.dbexputil.transplant.logic.core.util.raw.sp;

import java.sql.Connection;
import java.util.HashMap;

import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.processor.YHSpecialHandler;
import yh.rad.dbexputil.transplant.logic.core.util.db.YHTransplantUtil;

public class YHSPHandler3 implements YHSpecialHandler{

  public String getUserId3(Connection conn , String value) throws Exception{
    YHSPHandler2 sphd = new YHSPHandler2();
    int index = value.lastIndexOf("|");
    if(index == value.length() - 1){
      return value;
    }
    String userName = value.substring(index,value.length());
    if("".equals(userName.trim())){
      return value;
    }
    String result = sphd.getUserId2(conn, userName);
    return result;
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
      result = getUserId3(conn, value.toString());
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

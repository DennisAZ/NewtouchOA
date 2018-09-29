package yh.rad.dbexputil.transplant.logic.core.util.raw.sp;

import java.sql.Connection;
import java.util.HashMap;

import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.processor.YHSpecialHandler;
import yh.rad.dbexputil.transplant.logic.core.util.db.YHTransplantUtil;

public class YHSPHandler4 implements YHSpecialHandler{
  
  public String getUserId4(Connection conn , String value) throws Exception{
    YHSPHandler3 sphd3 = new YHSPHandler3();
    YHSPHandler sphd = new YHSPHandler();
    String result = "";
    if(!"".equals(value.trim())){
      if(value.indexOf("|") > 0 ){
        result = sphd3.getUserId3(conn, value);
      }else{ 
        result = sphd.getUserId(conn, value);
      }
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
      conn = YHTransplantUtil.getDBConn2(false, Integer.valueOf(dbType));
      if(value == null || "".equals(value.toString().trim())){
        return "";
      }
      result = getUserId4(conn, value.toString());
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

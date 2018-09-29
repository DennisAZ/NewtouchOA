package yh.core.funcs.youhua.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;

public class YHYouhuaLogic {
  public void createIndex(Connection conn , String contextPath) throws Exception {
    String type = YHSysProps.getProp("db.jdbc.dbms");
    String filePath = contextPath  + "core"+ File.separator + "funcs"+ File.separator + "youhua" + File.separator + type + ".sql";
    List<String> sqls = new ArrayList();
    YHFileUtility.loadLine2Array(filePath, sqls);
    Statement stm = null;
    for (String sql : sqls) {
      if (!YHUtility.isNullorEmpty(sql.trim())) {
        sql = sql.trim();
        if (sql.endsWith(";")) {
          sql = sql.substring(0 , sql.length() - 1);
          try {
            stm = conn.createStatement();
            stm.execute(sql);
            //System.out.println(sql);
          } catch (Exception ex) {
            //ex.printStackTrace();
          } finally {
            YHDBUtility.close(stm, null, null);
          }
        }
      }
    }
  }
  
}

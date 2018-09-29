package yh.rad.dbexputil.mysql.logic;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import yh.core.util.db.YHDBUtility;

public class YHExecueMysql {
/**
 * 
 * @param sqlFile
 * @return
 * @throws Exception
 */
  public static List<String> loadSql(String sqlFile) throws Exception {
    List<String> sqlList = new ArrayList<String>();
    try {
      InputStream sqlFileIn = new FileInputStream(sqlFile);
      StringBuffer sqlSb = new StringBuffer();
      byte[] buff = new byte[1024];
      int byteRead = 0;
      while ((byteRead = sqlFileIn.read(buff)) != -1) {
        sqlSb.append(new String(buff, 0, byteRead));
      } // Windows 下换行是 \r\n, Linux 下是 \n
      String[] sqlArr = sqlSb.toString().split("(;\\s*\\r\\n)(;\\s*\\n)");
      for (int i = 0; i < sqlArr.length; i++) {
        String sql = sqlArr[i].replaceAll("--.*", "").trim();
        if (!sql.equals("")) {
          sqlList.add(sql);
        }
      }
      return sqlList;
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
/**
 * 
 * @param conn
 * @param sqlFile
 * @throws Exception
 */
  public static void execueMysql(Connection conn ,String sqlFile) throws Exception{
    Statement smt = null;
    try {
      List<String> sqlList = loadSql(sqlFile);
      smt = conn.createStatement();
      for (String sql : sqlList) {
        smt.addBatch(sql);
      }
      smt.executeBatch();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(smt, null, null);
    }
  }
}

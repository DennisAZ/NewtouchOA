package yh.core.util.db.generics;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.util.db.generics.YHORMDelete;
import yh.core.util.db.generics.YHORMInsert;
import yh.core.util.db.generics.YHORMSelect;
import yh.core.util.db.generics.YHORMUpdate;

public class YHORM2Map {

  /**
   * 将Map提供的数据添加到数据库中 思路：将Map<String,String> formInfo 转换成 Map<String,Object>
   * tableInfo
   * 
   * @param dbConn
   * @param tableName
   * @param formInfo
   *          Map<String,String>
   * @throws Exception
   */
  public static void save(Connection dbConn, String tableName, Map formInfo,boolean isCascade)
      throws Exception {
    Map<String, Object> m = YHSQLExecuteHepler.form2TableInfo(dbConn, tableName,
        formInfo,isCascade);
    YHORMInsert.doInsert(dbConn, m);
  }

  /**
   * 根据Map提供的数据更新指定的数据库表
   * 
   * @param dbConn
   * @param tableName
   * @param formInfo
   * @throws Exception
   */
  public static void update(Connection dbConn, String tableName, Map formInfo,boolean isCascade)
      throws Exception {
    Map<String, Object> m = YHSQLExecuteHepler.form2TableInfo(dbConn, tableName,
        formInfo,isCascade);
    YHORMUpdate.doUpdate(dbConn, m);
  }

  /**
   * 根据seqId删除数据库信息
   * 
   * @param conn
   * @param tableName
   * @param seqId
   * @throws Exception
   */
  public static void delete(Connection conn, String tableName, Map formInfo,boolean isCascade)
      throws Exception {
    Map<String, Object> m = YHSQLExecuteHepler.form2TableInfo(conn, tableName, formInfo,isCascade);
    //System.out.println("==============" + m );
    YHORMDelete.doDelete(conn, m);
  }

  /**
   * 加载数据表中的信息
   * 
   * @param dbConn
   * @param tableName
   * @param tableName
   * @return
   * @throws Exception
   */
  public static Map loadData(Connection dbConn, List<Object> formInfo, Map filters,boolean isCascade)
      throws Exception {

    Map<String, Object> fieldInfo = YHSQLExecuteHepler.form2TableInfo(dbConn, formInfo,isCascade);
    Map<String, Object> m = new HashMap<String, Object>();
    m = YHORMSelect.doSelect(dbConn, fieldInfo, filters);
    return m;
  }

  public static Map loadData(Connection dbConn, List<Object> formInfo, String[] filters,boolean isCascade)
  throws Exception {
    Map<String, Object> fieldInfo = YHSQLExecuteHepler.form2TableInfo(dbConn, formInfo,isCascade);
    Map<String, Object> m = new HashMap<String, Object>();
    m = YHORMSelect.doSelect(dbConn, fieldInfo, filters);
    return m;
  }
}

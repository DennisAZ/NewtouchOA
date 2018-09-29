package yh.rad.dbexputil.transplant.logic.core.data;

import java.util.ArrayList;

/**
 * 得到新旧数据的schema信息
 * @author Think
 *
 */
public class YHSchemaInfo {
  /**
   * 数据库类型
   */
  private int dbType;
  /**
   * 数据库名称
   */
  private String schemaName;
  /**
   * 对应schema的所有表名称
   */
  private ArrayList<String> tableNames;
  
  public int getDbType() {
    return dbType;
  }
  public void setDbType(int dbType) {
    this.dbType = dbType;
  }
  public String getSchemaName() {
    return schemaName;
  }
  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }
  public ArrayList<String> getTableNames() {
    return tableNames;
  }
  public void setTableNames(ArrayList<String> tableNames) {
    this.tableNames = tableNames;
  }

}

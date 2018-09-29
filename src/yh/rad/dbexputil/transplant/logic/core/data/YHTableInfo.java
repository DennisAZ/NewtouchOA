package yh.rad.dbexputil.transplant.logic.core.data;

import java.util.ArrayList;

/**
 * 得到单个表的信息
 * @author Think
 *
 */
public class YHTableInfo {
  private String tableName;
  /**
   * 外键关联的表信息 以逗号隔开
   */
  private String fkrefersNames;
  private String fkrefersColumns;
  /**
   * 所有的列信息
   */
  private ArrayList<YHColumnInfo> columns;
  
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getFkrefersNames() {
    return fkrefersNames;
  }
  public void setFkrefersNames(String fkrefersNames) {
    this.fkrefersNames = fkrefersNames;
  }
  public String getFkrefersColumns() {
    return fkrefersColumns;
  }
  public void setFkrefersColumns(String fkrefersColumns) {
    this.fkrefersColumns = fkrefersColumns;
  }
  public ArrayList<YHColumnInfo> getColumns() {
    return columns;
  }
  public void setColumns(ArrayList<YHColumnInfo> columns) {
    this.columns = columns;
  }
  
  @Override
  public String toString() {
    return "YHTableInfo [columns=" + columns + ", fkrefersColumns="
        + fkrefersColumns + ", fkrefersNames=" + fkrefersNames + ", tableName="
        + tableName + "]";
  }
  
}

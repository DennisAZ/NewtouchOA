package yh.rad.velocity.createtable;

import java.util.ArrayList;
import java.util.List;

public class YHTableInfo {

  private String tableName;
  private String autoIncreaseField;
  private ArrayList<YHTableColumn> columns = new ArrayList<YHTableColumn>();;
  
  public YHTableInfo() {
   
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getAutoIncreaseField() {
    return autoIncreaseField;
  }
  public void setAutoIncreaseField(String autoIncreaseField) {
    this.autoIncreaseField = autoIncreaseField;
  }
  public ArrayList<YHTableColumn> getColumns() {
    return columns;
  }
  public void setColumns(ArrayList<YHTableColumn> columns) {
    this.columns = columns;
  }
  public void addColumn(YHTableColumn tc) {
    this.columns.add(tc);
  }
}

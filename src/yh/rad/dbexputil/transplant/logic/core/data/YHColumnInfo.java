package yh.rad.dbexputil.transplant.logic.core.data;
/**
 * 单个表列的信息
 * @author Think
 *
 */
public class YHColumnInfo {
  private String columnName;
  /**
   * java sql的数据类型
   */
  private int dataType;
  private boolean isNullable;
  private boolean isPrimaryKey;
  /**
   * 特定数据库的数据类型名称
   */
  private String  dbTypeName;
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public int getDataType() {
    return dataType;
  }
  public void setDataType(int dataType) {
    this.dataType = dataType;
  }
  public boolean isNullable() {
    return isNullable;
  }
  public void setNullable(boolean isNullable) {
    this.isNullable = isNullable;
  }
  public boolean isPrimaryKey() {
    return isPrimaryKey;
  }
  public void setPrimaryKey(boolean isPrimaryKey) {
    this.isPrimaryKey = isPrimaryKey;
  }
  public String getDbTypeName() {
    return dbTypeName;
  }
  public void setDbTypeName(String dbTypeName) {
    this.dbTypeName = dbTypeName;
  }
  
  
}

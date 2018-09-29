package yh.rad.velocity.createtable;

public class YHTableColumn {

  private String columnName;
  private String type;
  private String constraint;
  private String foregin;
  private boolean isAutoIncrease = false;
  
  public boolean isAutoIncrease() {
    return isAutoIncrease;
  }
  public void setAutoIncrease(boolean isAutoIncrease) {
    this.isAutoIncrease = isAutoIncrease;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getConstraint() {
    return constraint;
  }
  public void setConstraint(String constraint) {
    this.constraint = constraint;
  }
  public String getForegin() {
    return foregin;
  }
  public void setForegin(String foregin) {
    this.foregin = foregin;
  }
  @Override
  public String toString() {
    return "YHTableColumn [columnName=" + columnName + ", constraint="
        + constraint + ", foregin=" + foregin + ", isAutoIncrease="
        + isAutoIncrease + ", type=" + type + "]";
  }
  
}

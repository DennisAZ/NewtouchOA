package yh.core.funcs.mysqldb.data;

import java.util.Date;
/**
 * 
 * @author Think
 *
 */
public class YHMySqlTabInfo {
  /*
   *表名称 
   */
  private String name;
  /*
   * 总行数
   */
  private int rows;
  /*
   *中数据长度 
   */
  private String dataLength;
  /*
   *创建时间 
   */
  private String  createTime;
  /*
   * 修改时间
   */
  private String  updateTime;
  
  private String engine;
  
  public String getEngine() {
    return engine;
  }
  public void setEngine(String engine) {
    this.engine = engine;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getRows() {
    return rows;
  }
  public void setRows(int rows) {
    this.rows = rows;
  }
  public String getDataLength() {
    return dataLength;
  }
  public void setDataLength(String dataLength) {
    this.dataLength = dataLength;
  }
  public String getCreateTime() {
    return createTime;
  }
  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
  public String getUpdateTime() {
    return updateTime;
  }
  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }
  @Override
  public String toString() {
    return "YHMySqlTabInfo [createTime=" + createTime + ", dataLength="
        + dataLength + ", name=" + name + ", rows=" + rows + ", updateTime="
        + updateTime + "]";
  }
}

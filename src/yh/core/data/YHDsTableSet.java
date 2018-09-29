package yh.core.data;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 数据库表集合
 * @author jpt
 * @version 1.0
 * @date 2006-8-29
 */
public class YHDsTableSet {
  /**
   * 数据库表对象列表
   */
  private HashMap dbTableMap = new LinkedHashMap();
  
  /**
   * 清除数据库表对象
   */
  public void clear() {
    dbTableMap.clear();    
  }
  
  /**
   * 添加数据库表对象
   * @param table
   */
  public void addTable(YHDsTable table) {
    dbTableMap.put(table.getTableNo(), table);
  }
  
  /**
   * 删除表定义
   * @param tableNo
   */
  public void removeTable(String tableNo) {
    dbTableMap.remove(tableNo);
  }
  
  /**
   * 用表编码取表对象
   * @param tableNo
   * @return
   */
  public YHDsTable getTable(String tableNo) {
    return (YHDsTable)dbTableMap.get(tableNo);
  }
  
  /**
   * 用表编码取得表名称
   * @param tableNo
   * @return
   */
  public String getTableName(String tableNo) {
    YHDsTable table = getTable(tableNo);
    if (table == null) {
      return null;
    }
    return table.getTableName();
  }
}

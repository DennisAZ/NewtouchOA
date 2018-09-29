package yh.core.util.db.generics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class YHORMDelete {

  /**
   * delete
   * 
   * @param conn
   * @param fieldInfo
   * @throws Exception
   */
  public static void doDelete(Connection conn, Map<String, Object> fieldInfo)
      throws Exception {
    PreparedStatement pstmt = null;
    if (fieldInfo.size() <= 0) {// 判断是否还有从表 ,如果fieldInfo的size<=0 则表示没有从表
      return;
    } else {
      Iterator iter = fieldInfo.keySet().iterator();
      while (iter.hasNext()) {
        String key = (String) iter.next();
        Object value = fieldInfo.get(key);
        // 判断value的类型是否为list
        if (value != null && List.class.isAssignableFrom(value.getClass())) {
          //System.out.println("value : " + value);
          List<Map<String, Object>> subs = (List<Map<String, Object>>) value;
          for (Map<String, Object> subMap : subs) {
            //System.out.println("subMap : " + subMap);
            doDelete(conn, subMap);
          }
        }
      }
      YHJObject2SQLHepler.javaObject2DeleteSQL(fieldInfo, conn, pstmt);
    }
  }
}

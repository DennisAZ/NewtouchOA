package yh.core.util.db.generics;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yh.core.util.db.YHDBUtility;

public class YHORMUpdate {

  public static void doUpdate(Connection conn, Map<String, Object> fieldInfo)
      throws Exception {

    PreparedStatement pstmt = null;
    String tableName = (String) fieldInfo.get("tableName");
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    int id = (Integer) fieldInfo.get("SEQ_ID");

    if (fieldInfo.size() <= 0) {// 判断是否还有从表 ,如果fieldInfo的size<=0 则表示没有从表
      return;
    } else {
      Iterator iter = fieldInfo.keySet().iterator();
      while (iter.hasNext()) {
        String key = (String) iter.next();
        Object value = fieldInfo.get(key);
        // 判断value的类型是否为list
        if (value != null && List.class.isAssignableFrom(value.getClass())) {
          // 删除所有从表
          List<Map<String, Object>> sublist = new ArrayList<Map<String, Object>>();
          List<Map<String, Object>> subs = (List<Map<String, Object>>) value;
          //System.out.println("删除从表");
          for (Map<String, Object> subMap : subs) {
            String subTableName = (String) subMap.get("tableName");

            String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FIELD_NAME");
            String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FK_RELA_FIELD_NO");
            String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                realFieldNo, "FIELD_NAME");
            Object realValue = null;

            if ("SEQ_ID".equals(realFieldName.trim())) {

              deleteSub(conn, subTableName, fKFieldName, id);
            } else {
              realValue = YHSQLExecuteHepler.getFieldValue(conn, realFieldName,
                  tableName, id);
              deleteSub(conn, subTableName, fKFieldName, realValue);
            }
            //System.out.println("for");
            /*
             * subMap.remove(fKFieldName); subMap.put(fKFieldName, realValue);
             * sublist.add(subMap);
             */
            //System.out.println(subMap);
            //System.out.println("删除字表");

          }
        }
        //System.out.println("while");
      }
      // 跟新主表
      //System.out.println("跟新主表");
      YHJObject2SQLHepler.javaObject2UpdateSQL(fieldInfo, conn, pstmt);
      // 插入所有字表
      //System.out.println("开始插入所有字表");
      iter = fieldInfo.keySet().iterator();
      while (iter.hasNext()) {
        //System.out.println("插入字表");
        String key = (String) iter.next();
        Object value = fieldInfo.get(key);
        // 判断value的类型是否为list
        if (value != null && List.class.isAssignableFrom(value.getClass())) {
          List<Map<String, Object>> subs = (List<Map<String, Object>>) value;
          for (Map<String, Object> subMap : subs) {
            //System.out.println(subMap);
            YHORMInsert.doInsert(conn, subMap);
          }
        }
      }
    }
  }

  public static void deleteSub(Connection conn, String tableName,
      String fkFieldName, Object fkValue) throws Exception {
    String sql = "delete from " + tableName + " where " + fkFieldName + " = ? ";
    //System.out.println("delete from " + tableName + " where " + fkFieldName
//        + " =  " + fkValue);
    PreparedStatement ps = null;
    Object[] param = new Object[1];
    String[] fN = new String[1];
    param[0] = fkValue;
    fN[0] = fkFieldName;
    try {
      ps = conn.prepareStatement(sql);
      DatabaseMetaData dbms = conn.getMetaData();
      YHSQLParamHepler.javaParam2SQLParam(param, ps, tableName);
      //System.out.println("开始executeUpdate 方法");
      int fag = ps.executeUpdate();
      //System.out.println("结束executeUpdate 方法");
      //System.out.println("fag : " + fag);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
    //System.out.println("end YHORMUpdate.deleteSub method ");
  }
}

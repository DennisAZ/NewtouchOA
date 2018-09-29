package yh.core.util.db.generics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yh.core.util.db.YHDBUtility;

public class YHORMInsert{

  /**
   * 2.0版 执行sql语句
   * 
   * @param conn
   *          数据库连接
   * 
   * @param fieldInfo
   *          Object的属性字段---值 map 通过 YHORMReflect的getFieldInfo()得到
   * @throws Exception
   */
  public static void doInsert(Connection conn, Map<String, Object> fieldInfo)
      throws Exception{

    //System.out.println("=======================================");
    //System.out.println("       do YHORMInsert.doInsert method");
    PreparedStatement pstmt = null;
    String tableName = (String) fieldInfo.get("tableName");
    //System.out.println(tableName);
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    //System.out.println("tableName >>>>>>>>>>>> " + tableName);
    try{
      YHJObject2SQLHepler.javaObject2InsertSQL(fieldInfo, conn, pstmt);
      
      if (fieldInfo.size() <= 0){// 判断是否还有从表 ,如果fieldInfo的size<=0 则表示没有从表        return;
      }

      Iterator iter = fieldInfo.keySet().iterator();
      while (iter.hasNext()){
        String key = (String) iter.next();
        Object value = fieldInfo.get(key);
        // 判断value的类型是否为list
        if (value != null && List.class.isAssignableFrom(value.getClass())){
          List<Map<String, Object>> subs = (List<Map<String, Object>>) value;
          for (Map<String, Object> subMap : subs){
            // 加入外键关联
            String subTableName = (String) subMap.get("tableName");
            String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FIELD_NAME");
            // 2.得到对应主表的字段编码            String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FK_RELA_FIELD_NO");
            //System.out.println(realFieldNo);
            // 3.得到对应主表的字段名
            String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                realFieldNo, "FIELD_NAME");
            //System.out.println("fKFieldName :" + fKFieldName + " realFieldNo :"
//                + realFieldNo + " fkTableNo :");
            //System.out.println(tableName);
            if (realFieldName != null && "SEQ_ID".equals(realFieldName.trim())){
              subMap.remove(fKFieldName);
              Object o = YHSQLExecuteHepler.getSeqIdValue(conn, tableName);
              // System.out.println(o);
              subMap.put(fKFieldName, o);
            } else{
              subMap.remove(fKFieldName);
              Object o = fieldInfo.get(realFieldName);
              subMap.put(fKFieldName, o);
            }
            //System.out.println(fieldInfo);
            //System.out.println(subMap);
            doInsert(conn, subMap);
          }
        }
      }
      //System.out.println("=======================================");
    } catch (Exception e){
      throw e;
    } 
  }
}

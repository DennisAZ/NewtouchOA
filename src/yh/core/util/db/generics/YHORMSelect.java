package yh.core.util.db.generics;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHStringFormat;
public class YHORMSelect{

  /**
   *doselect
   * 
   * @param cls
   * @param conn
   * @param fieldInfo
   * @param fielter
   * @return
   * @throws Exception
   */
  public static Object doSelect(Class cls, Connection conn,
      Map<String, Object> fieldInfo, Map<String, Object> filter)
      throws Exception{
    String tableName = (String) fieldInfo.get("tableName");
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    Object pojo = null;
    PreparedStatement pstmt = null;
    ResultSetMetaData rsmd = null;
    ResultSet rs = null;
    try{
      if (fieldInfo.size() <= 0){// 判断是否还有从表 ,如果fieldInfo的size<=0
        // 则表示没有从表        return null;
      } else{
        pstmt = YHJObject2SQLHepler.javaObject2QuerySQL(fieldInfo, conn, filter);
        rs = pstmt.executeQuery();
        rsmd = pstmt.getMetaData();
        while (rs.next()){
          pojo = YHSQLParamHepler.sQLParam2JavaParam(cls, rs, rsmd);
        }
        Iterator iter = fieldInfo.keySet().iterator();
        while (iter.hasNext()){
          String key = (String) iter.next();
          Object value = fieldInfo.get(key);
          // 判断value的类型是否为Map,则表示存在从表
          if (value != null && Map.class.isAssignableFrom(value.getClass())){
            // 得到从表的信息
            Map<String, Object> subMap = (Map<String, Object>) value;
            // 得到从表的表名称
            String subTableName = (String) subMap.get("tableName");
            // 定义一个List 用来存储从表的数据
            // 得到从表的 类对象Class
            Class subClass = (Class) subMap.get("Class");
            // 定义一个查询条件的哈希表
            Map<String, Object> subMapFilter = new HashMap<String, Object>();
            // 得到查询条件
            // 1.得到外键关联的字段名
            String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FIELD_NAME");
            // 2.得到对应主表的字段编码
            String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FK_RELA_FIELD_NO");
            // 3.得到对应主表的字段名
            String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                realFieldNo, "FIELD_NAME");
            // 4.得到外键的值 通过主表的seq_id进行查询
            if (realFieldName == null){
              continue;
            }
            String methodName = YHStringFormat.unformat(realFieldName);
            methodName = "get" + methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);
            Method m = cls.getDeclaredMethod(methodName);
            Object realValue = m.invoke(pojo); // 组装查询条件
            subMapFilter.put(fKFieldName, realValue);

            List l = doSelectList(subClass, conn, subMap, subMapFilter);
            String setMethodName = YHStringFormat.unformat(key);
            setMethodName = "set" + setMethodName.substring(0, 1).toUpperCase()
                + setMethodName.substring(1);

            Method ml = cls.getDeclaredMethod(setMethodName, l.getClass());
            ml.invoke(pojo, l);
          }
        }
      }
      return pojo;
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(pstmt, rs, null);
    }
  }

  /**
   * 加载list
   * 
   * @param cls
   * @param conn
   * @param fieldInfo
   * @param filter
   * @return
   * @throws Exception
   */
  public static List doSelectList(Class cls, Connection conn,
      Map<String, Object> fieldInfo, String[] filter) throws Exception{
    Object pojo = null;
    PreparedStatement pstmt = null;
    ResultSetMetaData rsmd = null;
    ResultSet rs = null;
    List<Object> objectList = new ArrayList<Object>();
    String tableName = (String) fieldInfo.get("tableName");
    // System.out.println("tableName >>>>>>>>>>>> " + tableName);
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    try{
      if (fieldInfo.size() <= 0){// 判断是否还有从表 ,如果fieldInfo的size<=0
        // 则表示没有从表
        return null;
      } else{
        try{
          pstmt = YHJObject2SQLHepler.javaObject2QuerySQL(fieldInfo, conn,
              filter);
          rs = pstmt.executeQuery();
          rsmd = pstmt.getMetaData();
          while (rs.next()){
            pojo = YHSQLParamHepler.sQLParam2JavaParam(cls, rs, rsmd);
            // System.out.println("do : "+pojo);
            // 假定有sql_Id
            Iterator iter = fieldInfo.keySet().iterator();
            while (iter.hasNext()){
              String key = (String) iter.next();
              Object value = fieldInfo.get(key);
              // 判断value的类型是否为Map,则表示存在从表
              if (value != null && Map.class.isAssignableFrom(value.getClass())){
                // 得到从表的信息
                Map<String, Object> subMap = (Map<String, Object>) value;
                // 得到从表的表名称
                String subTableName = (String) subMap.get("tableName");
                // 定义一个List 用来存储从表的数据
                // 得到从表的 类对象Class
                Class subClass = (Class) subMap.get("Class");
                // 定义一个查询条件的哈希表
                Map<String, Object> subMapFilter = new HashMap<String, Object>();
                // 得到查询条件
                // 1.得到外键关联的字段名
                String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                    fkTableNo, subTableName, "FIELD_NAME");
                // 2.得到对应主表的字段编码
                String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                    fkTableNo, subTableName, "FK_RELA_FIELD_NO");
                // 3.得到对应主表的字段名
                String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                    realFieldNo, "FIELD_NAME");
                // 4.得到外键的值 通过主表的seq_id进行查询
                if (realFieldName == null){
                  continue;
                }
                String methodName = YHStringFormat.unformat(realFieldName);
                methodName = "get" + methodName.substring(0, 1).toUpperCase()
                    + methodName.substring(1);
                Method m = cls.getDeclaredMethod(methodName);
                Object realValue = m.invoke(pojo);

                // 组装查询条件
                subMapFilter.put(fKFieldName, realValue);
                // 得到从表的结果集
                List l = doSelectList(subClass, conn, subMap, subMapFilter);
                String setMethodName = YHStringFormat.unformat(key);
                setMethodName = "set"
                    + setMethodName.substring(0, 1).toUpperCase()
                    + setMethodName.substring(1);

                Method ml = cls.getDeclaredMethod(setMethodName, l.getClass());
                ml.invoke(pojo, l);
                // 组装
              }
            }
            objectList.add(pojo);
          }
        } catch (Exception e){
          e.printStackTrace();
          throw e;
        }
      }
      return objectList;
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(pstmt, rs, null);
    }
  }

  /**
   * 加载list
   * 
   * @param cls
   * @param conn
   * @param fieldInfo
   * @param filter
   * @return
   * @throws Exception
   */
  public static List doSelectList(Class cls, Connection conn,
      Map<String, Object> fieldInfo, Map<String, Object> filter)
      throws Exception{
    Object pojo = null;
    PreparedStatement pstmt = null;
    ResultSetMetaData rsmd = null;
    ResultSet rs = null;
    List<Object> objectList = new ArrayList<Object>();
    String tableName = (String) fieldInfo.get("tableName");
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    try{
      if (fieldInfo.size() <= 0){// 判断是否还有从表 ,如果fieldInfo的size<=0
        // 则表示没有从表        return null;
      }
      pstmt = YHJObject2SQLHepler.javaObject2QuerySQL(fieldInfo, conn, filter);
      rs = pstmt.executeQuery();
      rsmd = pstmt.getMetaData();
      while (rs.next()){
        pojo = YHSQLParamHepler.sQLParam2JavaParam(cls, rs, rsmd);
        // System.out.println("do : "+pojo);
        // 假定有sql_Id
        Iterator iter = fieldInfo.keySet().iterator();
        while (iter.hasNext()){
          String key = (String) iter.next();
          Object value = fieldInfo.get(key);
          // 判断value的类型是否为Map,则表示存在从表
          if (value != null && Map.class.isAssignableFrom(value.getClass())){
            // 得到从表的信息
            Map<String, Object> subMap = (Map<String, Object>) value;
            // 得到从表的表名称
            String subTableName = (String) subMap.get("tableName");
            // 定义一个List 用来存储从表的数据
            // 得到从表的 类对象Class
            Class subClass = (Class) subMap.get("Class");
            // 定义一个查询条件的哈希表
            Map<String, Object> subMapFilter = new HashMap<String, Object>();
            // 得到查询条件
            // 1.得到外键关联的字段名
            String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FIELD_NAME");
            // 2.得到对应主表的字段编码
            String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FK_RELA_FIELD_NO");
            // 3.得到对应主表的字段名
            String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                realFieldNo, "FIELD_NAME");
            // 4.得到外键的值 通过主表的seq_id进行查询
            if (realFieldName == null){
              continue;
            }
            String methodName = YHStringFormat.unformat(realFieldName);
            methodName = "get" + methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);
            Method m = cls.getDeclaredMethod(methodName);
            Object realValue = m.invoke(pojo);
            // 组装查询条件
            subMapFilter.put(fKFieldName, realValue);
            // 得到从表的结果集
            List l = doSelectList(subClass, conn, subMap, subMapFilter);
            String setMethodName = YHStringFormat.unformat(key);
            setMethodName = "set" + setMethodName.substring(0, 1).toUpperCase()
                + setMethodName.substring(1);

            Method ml = cls.getDeclaredMethod(setMethodName, l.getClass());
            ml.invoke(pojo, l);
            // 组装
          }
        }
        objectList.add(pojo);
      }
      return objectList;
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(pstmt, rs, null);
    }
  }

  /**
   *doselect
   * 
   * @param cls
   * @param conn
   * @param fieldInfo
   * @param fielter
   * @return
   * @throws Exception
   */
  public static Map<String, Object> doSelect(Connection conn,
      Map<String, Object> fieldInfo, Map<String, Object> filter)
      throws Exception{
    Map<String, Object> m = null;
    List<Object> objectList = new ArrayList<Object>();
    String tableName = (String) fieldInfo.get("tableName");
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    Map<String, Object> res = new HashMap<String, Object>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    try{
      if (fieldInfo.size() <= 0){// 判断是否还有从表 ,如果fieldInfo的size<=0
        // 则表示没有从表        return null;
      } 
      pstmt = YHJObject2SQLHepler
          .javaObject2QuerySQL(fieldInfo, conn, filter);
      rs = pstmt.executeQuery();
      rsmd = pstmt.getMetaData();
      while (rs.next()){
        m = new HashMap<String, Object>();
        YHSQLParamHepler.sQLParam2JavaParam(m, rs, rsmd);

        Iterator iter = fieldInfo.keySet().iterator();
        while (iter.hasNext()){
          String key = (String) iter.next();
          Object value = fieldInfo.get(key);
          // 判断value的类型是否为Map,则表示存在从表
          if (value != null && Map.class.isAssignableFrom(value.getClass())){
            // 得到从表的信息
            Map<String, Object> subMap = (Map<String, Object>) value;
            // 得到从表的表名称
            String subTableName = (String) subMap.get("tableName");
            // 定义一个List 用来存储从表的数据
            // 定义一个查询条件的哈希表
            Map<String, Object> subMapFilter = new HashMap<String, Object>();
            // 得到查询条件
            // 1.得到外键关联的字段名
            String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FIELD_NAME");
            // 2.得到对应主表的字段编码
            //System.out.println(fKFieldName);
            String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FK_RELA_FIELD_NO");
            //System.out.println(realFieldNo);
            // 3.得到对应主表的字段名
            String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                realFieldNo, "FIELD_NAME");
            // 4.得到外键的值 通过主表的seq_id进行查询
            //System.out.println("realFieldName :" + realFieldName);
            if (realFieldName == null){
              continue;
            }

            Object realValue = m
                .get(YHStringFormat.unformat(realFieldName)); // 组装查询条件
            subMapFilter.put(fKFieldName, realValue);

            List l = doSelectList(conn, subMap, subMapFilter);
            m.put(subTableName, l);
            continue;
          }
        } 
        objectList.add(m);
      }
      res.put(tableName, objectList);
      return res;
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(pstmt, rs, null);
    }
  }

  public static Map<String, Object> doSelect(Connection conn,
      Map<String, Object> fieldInfo, String[] filter) throws Exception{
    Map<String, Object> m = null;
    List<Object> objectList = new ArrayList<Object>();
    String tableName = (String) fieldInfo.get("tableName");
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    Map<String, Object> res = new HashMap<String, Object>();
    PreparedStatement pstmt = null;
    ResultSetMetaData rsmd = null;
    ResultSet rs = null;
    try{
      if (fieldInfo.size() <= 0){// 判断是否还有从表 ,如果fieldInfo的size<=0
        // 则表示没有从表        return null;
      } 

      pstmt = YHJObject2SQLHepler
          .javaObject2QuerySQL(fieldInfo, conn, filter);
      rs = pstmt.executeQuery();
      rsmd = pstmt.getMetaData();
      while (rs.next()){
        m = new HashMap<String, Object>();
        YHSQLParamHepler.sQLParam2JavaParam(m, rs, rsmd);
        Iterator iter = fieldInfo.keySet().iterator();
        try{
          while (iter.hasNext()){
            String key = (String) iter.next();
            Object value = fieldInfo.get(key);
            // 判断value的类型是否为Map,则表示存在从表
            if (value != null && Map.class.isAssignableFrom(value.getClass())){
              // 得到从表的信息
              Map<String, Object> subMap = (Map<String, Object>) value;
              // 得到从表的表名称
              String subTableName = (String) subMap.get("tableName");
              // 定义一个List 用来存储从表的数据
              // 定义一个查询条件的哈希表
              Map<String, Object> subMapFilter = new HashMap<String, Object>();
              // 得到查询条件
              // 1.得到外键关联的字段名
              String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                  fkTableNo, subTableName, "FIELD_NAME");
              // 2.得到对应主表的字段编码
              String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                  fkTableNo, subTableName, "FK_RELA_FIELD_NO");
              // 3.得到对应主表的字段名
              String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                  realFieldNo, "FIELD_NAME");
              // 4.得到外键的值 通过主表的seq_id进行查询
              if (realFieldName == null){
                continue;
              }

              Object realValue = m
                  .get(YHStringFormat.unformat(realFieldName)); // 组装查询条件
              subMapFilter.put(fKFieldName, realValue);

              List l = doSelectList(conn, subMap, subMapFilter);
              m.put(subTableName, l);
              continue;
            }
          }
        } catch (Exception e){
          e.printStackTrace();
          throw e;
        }
        objectList.add(m);
      }
      res.put(tableName, objectList);
      return res;
    } catch (Exception e){
      throw e;
    } finally{
      YHDBUtility.close(pstmt, rs, null);
    }
  }

  /**
   * 加载list
   * 
   * @param cls
   * @param conn
   * @param fieldInfo
   * @param filter
   * @return
   * @throws Exception
   */
  public static List doSelectList(Connection conn,
      Map<String, Object> fieldInfo, Map<String, Object> filter)
      throws Exception{
    Map<String, Object> m = null;
    PreparedStatement pstmt = null;
    ResultSetMetaData rsmd = null;
    ResultSet rs = null;
    List<Object> objectList = new ArrayList<Object>();
    String tableName = (String) fieldInfo.get("tableName");
    String fkTableNo = YHSQLExecuteHepler.getTableNo(conn, tableName);
    try{
      if (fieldInfo.size() <= 0){// 判断是否还有从表 ,如果fieldInfo的size<=0
        // 则表示没有从表
        return null;
      } 

      pstmt = YHJObject2SQLHepler.javaObject2QuerySQL(fieldInfo, conn,
          filter);
      rs = pstmt.executeQuery();
      rsmd = pstmt.getMetaData();

      while (rs.next()){
        m = new HashMap<String, Object>();
        YHSQLParamHepler.sQLParam2JavaParam(m, rs, rsmd);

        // 假定有sql_Id
        Iterator iter = fieldInfo.keySet().iterator();
        while (iter.hasNext()){
          String key = (String) iter.next();
          Object value = fieldInfo.get(key);
          // 判断value的类型是否为Map,则表示存在从表
          if (value != null && Map.class.isAssignableFrom(value.getClass())){
            // 得到从表的信息
            Map<String, Object> subMap = (Map<String, Object>) value;
            // 得到从表的表名称
            String subTableName = (String) subMap.get("tableName");
            // 定义一个List 用来存储从表的数据
            // 得到从表的 类对象Class
            // 定义一个查询条件的哈希表
            Map<String, Object> subMapFilter = new HashMap<String, Object>();

            // 得到查询条件
            // 1.得到外键关联的字段名
            String fKFieldName = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FIELD_NAME");
            // 2.得到对应主表的字段编码
            String realFieldNo = YHSQLExecuteHepler.getDsFKFieldName(conn,
                fkTableNo, subTableName, "FK_RELA_FIELD_NO");
            // 3.得到对应主表的字段名
            String realFieldName = YHSQLExecuteHepler.getDsFieldName(conn,
                realFieldNo, "FIELD_NAME");
            // 4.得到外键的值 通过主表的seq_id进行查询
            if (realFieldName == null){
              continue;
            }
            Object realValue = m
                .get(YHStringFormat.unformat(realFieldName));
            // 组装查询条件
            subMapFilter.put(fKFieldName, realValue);
            // 得到从表的结果集
            doSelectList(conn, subMap, subMapFilter);
            // 组装
          }
        }
        objectList.add(m);
      }
    
      return objectList;
    } catch (Exception e){
      throw e;
    } finally{
     YHDBUtility.close(pstmt, rs, null);
    }
  }
}

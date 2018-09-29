package yh.core.util.db.generics;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yh.core.util.YHOut;
import yh.core.util.db.YHStringFormat;

/**
 * 2.0版本
 * 
 * @author TTlang
 * 
 */
public class YHORM2Obj {
  /**
   * 新增加单个对象到数据库表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public static void save(Connection dbConn, Object obj, boolean isCascade) throws Exception {

    Map<String, Object> fieldInfo = null;
    YHORMReflect or = new YHORMReflect();
    fieldInfo = or.getFieldInfo(obj,isCascade);
    //YHOut.println("fieldInfo >> " + fieldInfo);
    YHORMInsert.doInsert(dbConn, fieldInfo);
  }

  /**
   * 更新单个数据表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public static void update(Connection dbConn, Object obj,boolean isCascade) throws Exception {

    Map<String, Object> fieldInfo = null;
    YHORMReflect or = new YHORMReflect();
    fieldInfo = or.getFieldInfo(obj,isCascade);
    YHORMUpdate.doUpdate(dbConn, fieldInfo);
  }

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */

  public static void delete(Connection dbConn, Class cls , int objId,boolean isCascade) throws Exception {
       Object o = cls.newInstance();
       String setMethod = "setSeqId";
       Method m = cls.getDeclaredMethod(setMethod, int.class);
       m.invoke(o, objId);
       delete(dbConn, o, isCascade);
  }

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public static void delete(Connection dbConn, Object obj,boolean isCascade) throws Exception {

    Map<String, Object> fieldInfo = null;
    YHORMReflect or = new YHORMReflect();
    fieldInfo = or.getFieldInfo(obj,isCascade);
    YHORMDelete.doDelete(dbConn, fieldInfo);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  public static Object loadObj(Connection dbConn, Class cls, int objId,boolean isCascade)
      throws Exception {
    Map<String, Object> fieldInfo = null;
    YHORMReflect or = new YHORMReflect();
    fieldInfo = or.getFieldInfo(cls,isCascade);
    Map<String, Object> filter = new HashMap<String, Object>();
    //System.out.println("field : >>> " + fieldInfo);
    filter.put("SEQ_ID", objId);
    Object pojo = YHORMSelect.doSelect(cls, dbConn, fieldInfo, filter);
    return pojo;
  }
  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  public static Object loadObj(Connection dbConn, Class cls,  Map filters,boolean isCascade)
      throws Exception {
    Map<String, Object> fieldInfo = null;
    YHORMReflect or = new YHORMReflect();
    fieldInfo = or.getFieldInfo(cls,isCascade);
    Map<String, Object> filterMap = new HashMap<String, Object>();
    Set<String> keys = filters.keySet();
    for (String key : keys) {
      Object value = filters.get(key);
      //key = YHStringFormat.format(key);
      filterMap.put(key, value);
    }
    Object pojo = YHORMSelect.doSelect(cls, dbConn, fieldInfo, filterMap);
    return pojo;
  }
  /**
   * 加载数据
   * 
   * @param dbConn
   * @param filters
   * @return
   * @throws Exception
   */
  /*
   * public Object loadObj(Connection dbConn, Class cls, String[] filters)
   * throws Exception { YHORMReflect or = new YHORMReflect(); PreparedStatement
   * pstmt = null; ResultSet rs = null; Object result = null;
   * 
   * List<String> fields = or.getTableProl(cls); StringBuffer sql = new
   * StringBuffer("select "); Iterator<String> iter = fields.iterator();
   * while(iter.hasNext()){ sql.append(iter.next()); if(iter.hasNext()){
   * sql.append(" , "); } } sql.append(" from ") .append(or.getTableName());
   * sql.append(" where "); for(int i = 0; i < filters.length; i++){
   * sql.append(filters[i]); if(i < filters.length - 1){ sql.append(" and "); }
   * } pstmt = dbConn.prepareStatement(sql.toString()); rs =
   * pstmt.executeQuery(); result = or.getObject(cls, rs); return result; }
   */
  /**
   * 加载数据
   * 
   * @param dbConndd
   * @param cls
   * @return
   * @throws Exception
   */

  public static List loadList(Connection dbConn, Class cls, Map filters,boolean isCascade)
      throws Exception {
    Map<String, Object> fieldInfo = null;
    YHORMReflect or = new YHORMReflect();
    fieldInfo = or.getFieldInfo(cls,isCascade);
    Map<String, Object> filterMap = new HashMap<String, Object>();
    if(!(filters == null)){
      Set<String> keys = filters.keySet();
      for (String key : keys) {
        Object value = filters.get(key);
       // key = YHStringFormat.format(key);
        filterMap.put(key, value);
      }
    }
    return YHORMSelect.doSelectList(cls, dbConn, fieldInfo, filterMap);
  }
  /**
   * 加载数据
   * 
   * @param dbConndd
   * @param cls
   * @return
   * @throws Exception
   */
  public static List loadList(Connection dbConn, Class cls, String[] filters,boolean isCascade)
      throws Exception {
    Map<String, Object> fieldInfo = null;
    YHORMReflect or = new YHORMReflect();
    fieldInfo = or.getFieldInfo(cls,isCascade);
    Map<String, Object> filterMap = new HashMap<String, Object>();
/*    if(!(filters == null)){
      Set<String> keys = filters.keySet();
      for (String key : keys) {
        Object value = filters.get(key);
       // key = YHStringFormat.format(key);
        filterMap.put(key, value);
      }
    }*/
    return YHORMSelect.doSelectList(cls, dbConn, fieldInfo, filters);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  // public List loadList(Connection dbConn, Class cls, List filters) throws
  // Exception {
  // return null;
  // }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  /*
   * public List loadList(Connection dbConn, Class cls, String[] filters) throws
   * Exception { YHORMReflect or = new YHORMReflect(); PreparedStatement pstmt =
   * null; ResultSet rs = null; Object obj = null; Iterator<String> fieldIter=
   * null; List list = null;
   * 
   * Map<String, String> fieldMap = or.getTableProm(cls); StringBuffer sql = new
   * StringBuffer("select "); fieldIter = fieldMap.values().iterator();
   * 
   * while(fieldIter.hasNext()){ sql.append(fieldIter.next());
   * if(fieldIter.hasNext()){ sql.append(" , "); } } sql.append(" from ")
   * .append(or.getTableName()) .append(" where "); //fieldIter =
   * filters.keySet().iterator(); for(int i = 0; i < filters.length; i++){
   * sql.append(filters[i]); if(i < filters.length - 1){ sql.append(" and "); }
   * } pstmt = dbConn.prepareStatement(sql.toString()); rs =
   * pstmt.executeQuery(); // System.out.println(sql.toString());
   * //System.out.println(rs); list = or.getObjectList(cls, rs); //
   * System.out.println(list.size()); if(rs!=null) rs.close(); //
   * dbConn.close(); return list; }
   */
}

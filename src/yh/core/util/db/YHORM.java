package yh.core.util.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import yh.core.util.db.generics.YHORM2Map;
import yh.core.util.db.generics.YHORM2Obj;

public class YHORM {
  /**
   * 新增加单个对象到数据库表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void saveSingle(Connection dbConn, Object obj) throws Exception {
    YHORM2Obj.save(dbConn, obj, false);
  }

  /**
   * 新增加单个对象到数据库表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void saveComplex(Connection dbConn, Object obj) throws Exception {
    YHORM2Obj.save(dbConn, obj, true);
  }

  /**
   * 新增加单个对象到数据库表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void saveSingle(Connection dbConn, String tableName, Map formInfo)
      throws Exception {
    YHORM2Map.save(dbConn, tableName, formInfo, false);
  }

  /**
   * 新增加主从对象到数据库表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void saveComplex(Connection dbConn, String tableName, Map formInfo)
      throws Exception {
    YHORM2Map.save(dbConn, tableName, formInfo, true);
  }

  /**
   * 更新单个数据表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void updateSingle(Connection dbConn, Object obj) throws Exception {
    YHORM2Obj.update(dbConn, obj, false);
  }

  /**
   * 更新单个数据表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void updateComplex(Connection dbConn, Object obj) throws Exception {
    YHORM2Obj.update(dbConn, obj, true);
  }

  /**
   * 更新单个数据表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   * @throws Exception
   */
  public  void updateSingle(Connection dbConn, String tableName,
      Map formInfo) throws Exception {
    YHORM2Map.update(dbConn, tableName, formInfo, false);
  }

  /**
   * 更新单个数据表
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   * @throws Exception
   */
  public  void updateComplex(Connection dbConn, String tableName,
      Map formInfo) throws Exception {
    YHORM2Map.update(dbConn, tableName, formInfo, true);
  }

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void deleteSingle(Connection dbConn, Class cls, int objId) throws Exception {
     Object o = cls.newInstance();
     Method ml = cls.getDeclaredMethod("setSeqId", int.class);
     ml.invoke(o, objId);
     YHORM2Obj.delete(dbConn, o, false);
  }

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
/*  public void deleteComplex(Connection dbConn, Class cls, int objId) throws Exception {
    
  }*/

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   * @throws Exception
   */
  public  void deleteSingle(Connection conn, String tableName,
      Map formInfo) throws Exception {
    YHORM2Map.delete(conn, tableName, formInfo, false);
  }

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   * @throws Exception
   */
  public  void deleteComplex(Connection conn, String tableName,
      Map formInfo) throws Exception {
    YHORM2Map.delete(conn, tableName, formInfo, true);
  }

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void deleteSingle(Connection dbConn, Object obj) throws Exception {

    YHORM2Obj.delete(dbConn, obj, false);
  }

  /**
   * 删除单个数据表数据
   * 
   * @param dbConn
   * @param obj
   * @throws Exception
   */
  public void deleteComplex(Connection dbConn, Object obj) throws Exception {

    YHORM2Obj.delete(dbConn, obj, true);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  public Object loadObjSingle(Connection dbConn, Class cls, int objId)
      throws Exception {
    return YHORM2Obj.loadObj(dbConn, cls, objId, false);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   * @throws Exception
   */
  public  Map loadDataSingle(Connection dbConn, List<Object> formInfo,
      Map filters) throws Exception {
    return YHORM2Map.loadData(dbConn, formInfo, filters, false);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   * @throws Exception
   */
  public  Map loadDataSingle(Connection dbConn, List<Object> formInfo,
      String[] filters) throws Exception {
    return YHORM2Map.loadData(dbConn, formInfo, filters, false);
  }
  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   * @throws Exception
   */
  public  Map loadDataComplex(Connection dbConn, List<Object> formInfo,
      Map filters) throws Exception {
    return YHORM2Map.loadData(dbConn, formInfo, filters, true);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  public Object loadObjComplex(Connection dbConn, Class cls, int objId)
      throws Exception {
    return YHORM2Obj.loadObj(dbConn, cls, objId, true);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  public Object loadObjSingle(Connection dbConn, Class cls, Map filters)
      throws Exception {
    return YHORM2Obj.loadObj(dbConn, cls, filters, false);
  }

  /**
   * 加载数据
   * 
   * @param dbConn
   * @param cls
   * @return
   * @throws Exception
   */
  public Object loadObjComplex(Connection dbConn, Class cls, Map filters)
      throws Exception {
    return YHORM2Obj.loadObj(dbConn, cls, filters, true);
  }

  /**
   * 加载数据
   * 
   * @param dbConndd
   * @param cls
   * @return
   * @throws Exception
   */
  public List loadListSingle(Connection dbConn, Class cls, Map filters)
      throws Exception {

    return YHORM2Obj.loadList(dbConn, cls, filters, false);
  }
  
  
  public List loadListSingle(Connection dbConn, Class cls, String[] filters)
       throws Exception {

    return YHORM2Obj.loadList(dbConn, cls, filters, false);
  }

  /**
   * 加载数据
   * 
   * @param dbConndd
   * @param cls
   * @return
   * @throws Exception
   */
  public List loadListComplex(Connection dbConn, Class cls, Map filters)
      throws Exception {

    return YHORM2Obj.loadList(dbConn, cls, filters, true);
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
  // public List loadListSingle(Connection dbConn, Class cls, String[] filters)
  // throws Exception {
  //    
  // return list;
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
   * public List loadListComplex(Connection dbConn, Class cls, String[] filters)
   * throws Exception {
   * 
   * return list; }
   */
  public void updateClob(Connection dbConn, Class cls, String[] filters){
    
  }
}

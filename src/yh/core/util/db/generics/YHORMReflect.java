package yh.core.util.db.generics;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.util.db.YHStringFormat;

public class YHORMReflect {

  /**
   * 2.0版 得到数据库对应字段和值的映射，值符合数据的要求
   * 
   * @param obj
   *          数据库表所对应的Java对象
   * @param conn
   *          数据库连接
   * @param fkTableNO
   *          外键关联表编码（如果为从表则加入，为主表则为空null）
   * @return map 数据库对应字段和值的映射
   */
  public Map<String, Object> getFieldInfo(Object obj, boolean isCascade) {
    Map<String, Object> fieldInfo = new HashMap<String, Object>();
    Class cls = obj.getClass();
    Field[] fields = null;
    fields = cls.getDeclaredFields();
    String key = null;
    Object value = null;
    String tableName = YHStringFormat.format(cls.getSimpleName(), "YH");// 得到数据库的表明
    System.out.println(tableName);
    fieldInfo.put("tableName", tableName); //添加数据表的名称
    // 添加外键关联字段
    for (Field field : fields) {
      value = getFieldValue(obj, field);
      if (value == null) {
        continue;
      }
      Type t = field.getGenericType();
      key = YHStringFormat.format(field.getName(),false);
      if (t instanceof ParameterizedType) {
        if (!isCascade) {
          continue;
        }
        Class type = field.getType();
        if (List.class.isAssignableFrom(type)) {// 子接口的问题解决了          // list类型
          List l = (List)getFieldValue(obj, field);
          List subset = new ArrayList();
          for (Object object : l) {
            // System.out.println(" fkTableNo > > "+fkTableNo);
            object = getFieldInfo(object,isCascade);// 迭代字表中的信息
            subset.add(object);
          }
          value = subset;
        }
        /**
         * 此处处理MAP类型 未作处理
         */
        if (Map.class.isAssignableFrom(type)) {
          // map类型
        }
        fieldInfo.put(key, value);
      }else {
        fieldInfo.put(key, value);
      }
    }

    return fieldInfo;
  }
  /**
   * 2.0版 得到数据库对应字段和值的映射，值符合数据的要求
   * 
   * @param obj
   *          数据库表所对应的Java对象
   * @param conn
   *          数据库连接

   * @param fkTableNO
   *          外键关联表编码（如果为从表则加入，为主表则为空null）

   * @return map 数据库对应字段和值的映射
   */
  public Map<String, Object> getFieldInfo(Object obj) {
    Map<String, Object> fieldInfo = new HashMap<String, Object>();
    Class cls = obj.getClass();
    Field[] fields = null;
    fields = cls.getDeclaredFields();
    String key = null;
    Object value = null;
    String tableName = YHStringFormat.format(cls.getSimpleName(), "YH");// 得到数据库的表明
    fieldInfo.put("tableName", tableName); // 添加数据表的名称
    // 添加外键关联字段

    for (Field field : fields) {

      value = getFieldValue(obj, field);
      if (value == null) {
        // System.out.println("fieldName >>>>>> when value = null >>> "+field.getName());
        continue;
      } else {
        // System.out.println("fieldName >>>>>> when value ="+value
        // +" >>> "+field.getName());
      }
      Type t = field.getGenericType();
      key = YHStringFormat.format(field.getName());
      if (t instanceof ParameterizedType) {
        Class type = field.getType();
        if (List.class.isAssignableFrom(type)) {// 子接口的问题解决了

          // list类型
          List l = (List) getFieldValue(obj, field);
          List subset = new ArrayList();
          for (Object object : l) {
            // System.out.println(" fkTableNo > > "+fkTableNo);
            object = getFieldInfo(object);// 迭代字表中的信息
            subset.add(object);
          }
          value = subset;
        }
        /**
         * 此处处理MAP类型 未作处理
         */
        if (Map.class.isAssignableFrom(type)) {
          // map类型
        }
        fieldInfo.put(key, value);
        continue;
      }

      fieldInfo.put(key, value);
      // System.out.println(fieldInfo);
    }

    return fieldInfo;
  }

  /**
   * 根据类信息得到 field的信息
   * 
   * @param cls
   *          需要处理的类信息
   * @param conn
   *          数据库连接
   * @param fkTableNo
   *          外键关联
   * @return
   */
  public Map<String, Object> getFieldInfo(Class cls,boolean isCascade) {
    Map<String, Object> tablePro = null;
    List<String> clsInfo = new ArrayList<String>();

    Field[] fields = null;
    fields = cls.getDeclaredFields();
    tablePro = new HashMap<String, Object>();

    String tableName = YHStringFormat.format(cls.getSimpleName(), "YH");

    tablePro.put("tableName", tableName);
    tablePro.put("Class", cls);
    for (Field field : fields) {
      Object value = null;
      // 判断是否集合类型
      String key = field.getName();
      key = YHStringFormat.format(key,false);
      Type t = field.getGenericType();
      key = YHStringFormat.format(field.getName(),false);
      if (t instanceof ParameterizedType) {
        if (!isCascade) {
          continue;
        }
        ParameterizedType p = (ParameterizedType) t;
        Class type = field.getType();
        if (List.class.isAssignableFrom(type)) {// 子接口的问题解决了
          Class subClass = (Class) p.getActualTypeArguments()[0];
          Map<String, Object> subset = getFieldInfo(subClass, isCascade);
          value = subset;
        }
        tablePro.put(key, value);
      }else{
        clsInfo.add(key);
      }
    }
    tablePro.put("clsInfo", clsInfo);
    return tablePro;
  }

  /**
   * 2.0版
   * 
   * @param obj
   *          传入的对象
   * @param field
   *          字段属性
   * @return 字段属性的值
   */
  private Object getFieldValue(Object obj, Field field) {
    Object value = null;
    String methodName = getMethod2GetName(field);
    try {
      Method m = obj.getClass().getDeclaredMethod(methodName);
      value = m.invoke(obj);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return value;
  }

  /**
   * 2.0版 得到get方法的方法名
   * 
   * @param f
   *          reflect的Field字段
   * @return get方法的名字
   */
  private String getMethod2GetName(Field f) {
    String methodName = null;
    String str = f.getName();
    str = str.substring(0, 1).toUpperCase() + str.substring(1);
    if (f.getType().isInstance(new Boolean(true))
        || f.getType() == boolean.class) {
      methodName = "is" + str;
    } else {
      methodName = "get" + str;
    }
    return methodName;
  }

  /**
   * 2.0版 得到set方法的方法名
   * 
   * @param fieldName
   *          Java对象的属性名
   * @return set方法的方法名
   */
  private String getMethod2SetName(String fieldName) {
    fieldName = fieldName.substring(0, 1).toUpperCase()
        + fieldName.substring(1);
    return "set" + fieldName;
  }
}

package yh.core.util;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 反射工具类
 * @author jpt
 *
 */
public class YHReflectUtility {
  private static Map<String, Class> primClassMap = new HashMap<String, Class>();
  static {
    primClassMap.put("byte", byte.class);
    primClassMap.put("short", short.class);
    primClassMap.put("int", int.class);
    primClassMap.put("long", long.class);
    primClassMap.put("float", float.class);
    primClassMap.put("double", double.class);
    primClassMap.put("boolean", boolean.class);
    primClassMap.put("char", char.class);
  }
  
  /**
   * 取得迭代值
   * @param obj
   * @param fieldName
   * @return
   * @throws Exception
   */
  public static Iterator<Object> itObject(Object obj, String fieldName) throws Exception {
    String methodName = "it" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    return (Iterator)exeMethod(obj, methodName, new Class[]{}, new Object[]{});
  }
  /**
   * 取得值
   * @param obj
   * @param fieldName
   * @return
   * @throws Exception
   */
  public static Object getValue(Object obj, String fieldName) throws Exception {
    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    return exeMethod(obj, methodName, new Class[]{}, new Object[]{});
  }
  /**
   * 新增加对象
   * @param parentObj
   * @param detlObj
   * @param fieldName
   * @return
   */
  public static boolean addObj(Object parentObj, Object detlObj, String fieldName) throws Exception {
    String methodName = null;
    String tmpStr = null;
    if (fieldName.endsWith("List")) {
      tmpStr = fieldName.substring(0, fieldName.length() - 4);
    }else if (fieldName.endsWith("Map")) {
      tmpStr = fieldName.substring(0, fieldName.length() - 3);
    }else {
      return false;
    }
    methodName = "add" + tmpStr.substring(0, 1).toUpperCase() + tmpStr.substring(1);
    exeMethod(parentObj, methodName, new Class[]{detlObj.getClass()}, new Object[]{detlObj});
    
    return true;
  }
  /**
   * 设置值
   * @param obj
   * @param fieldName
   * @param fieldType
   * @param fieldValue
   * @return true=进行了设置，fals=没有进行设置
   */
  public static boolean setValue(Object obj, String fieldName, Class fieldType, String fieldValue) throws Exception {
    Class cls = obj.getClass();
    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    if (fieldType.equals(int.class)) {
      if (YHUtility.isNullorEmpty(fieldValue)) {
        return false;
      }
      fieldValue = fieldValue.trim(); 
      exeMethod(obj, methodName, new Class[]{int.class}, new Object[]{new Integer(fieldValue)});
      return true;
    }else if (fieldType.equals(Integer.class)) {        
      if (YHUtility.isNullorEmpty(fieldValue)) {
        return false;
      }
      fieldValue = fieldValue.trim(); 
      exeMethod(obj, methodName, new Class[]{Integer.class}, new Object[]{new Integer(fieldValue)});
      return true;
    }else if (fieldType.equals(long.class)) {        
      if (YHUtility.isNullorEmpty(fieldValue)) {
        return false;
      }
      fieldValue = fieldValue.trim(); 
      exeMethod(obj, methodName, new Class[]{long.class}, new Object[]{new Long(fieldValue)});
      return true;
    }else if (fieldType.equals(Long.class)) {        
      if (YHUtility.isNullorEmpty(fieldValue)) {
        return false;
      }
      fieldValue = fieldValue.trim(); 
      exeMethod(obj, methodName, new Class[]{Long.class}, new Object[]{new Long(fieldValue)});
      return true;
    }else if (fieldType.equals(double.class)) {        
      if (YHUtility.isNullorEmpty(fieldValue)) {
        return false;
      }
      fieldValue = fieldValue.trim(); 
      exeMethod(obj, methodName, new Class[]{double.class}, new Object[]{new Double(fieldValue)});
      return true;
    }else if (fieldType.equals(Double.class)) {        
      if (YHUtility.isNullorEmpty(fieldValue)) {
        return false;
      }
      fieldValue = fieldValue.trim(); 
      exeMethod(obj, methodName, new Class[]{Double.class}, new Object[]{new Double(fieldValue)});
      return true;
    }else if (fieldType.equals(String.class)) {
      exeMethod(obj, methodName, new Class[]{String.class}, new Object[]{fieldValue});
      return true;
    }else if (fieldType.equals(Date.class)) {
      exeMethod(obj, methodName, new Class[]{Date.class}, new Object[]{YHUtility.parseDate(fieldValue)});
      return true;
    }else if (fieldType.equals(java.sql.Date.class)) {
      exeMethod(obj, methodName, new Class[]{java.sql.Date.class}, new Object[]{YHUtility.parseSqlDate(fieldValue)});
      return true;
    }else if (fieldType.equals(boolean.class)) {
      exeMethod(obj, methodName, new Class[]{boolean.class}, new Object[]{Boolean.valueOf(fieldValue)});
      return true;
    }else if (fieldType.equals(Boolean.class)) {
      exeMethod(obj, methodName, new Class[]{Boolean.class}, new Object[]{Boolean.valueOf(fieldValue)});
      return true;
    }
    return false;
  }
  
  /**
   * 执行一个类的方法
   * @param fullClass
   * @param method
   * @param params
   * @throws Throwable
   */
  public static Object exeMethod(String className, String method, String[] paramTypeNameArray, Object[] params) throws Exception {
    try {
      Class classDef = Class.forName(className);
      Class[] paramTypeArray = null;
      if (paramTypeNameArray == null) {
        paramTypeArray = new Class[]{};
      }else {
        paramTypeArray = new Class[paramTypeNameArray.length];
        for (int i = 0; i < paramTypeNameArray.length; i++) {
          String typeName = paramTypeNameArray[i];
          if (primClassMap.containsKey(typeName)) {
            paramTypeArray[i] = primClassMap.get(typeName);
          }else {
            paramTypeArray[i] = Class.forName(typeName);
          }
        }
      }
      Object classObj = classDef.newInstance();
      Method methodObj = classDef.getMethod(method, paramTypeArray);
      return methodObj.invoke(classObj, params);
    }catch(Exception e) {
      throw e;
    }
  }
  /**
   * 执行一个类的方法
   * @param fullClass
   * @param method
   * @param params
   * @throws Throwable
   */
  public static Object exeMethod(Object classObj, String method, Class[] paramTypeArray, Object[] params) throws Exception {
    Class classDef = classObj.getClass();
    try {
      Method methodObj = classDef.getMethod(method, paramTypeArray);
      return methodObj.invoke(classObj, params);
    }catch(Exception e) {
      throw e;
    }
  }
}

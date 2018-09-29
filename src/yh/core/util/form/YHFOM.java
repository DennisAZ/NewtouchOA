package yh.core.util.form;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yh.core.global.YHActionKeys;
import yh.core.util.YHReflectUtility;
import yh.core.util.YHRegexpUtility;
import yh.core.util.YHUtility;

public class YHFOM {
  /**
   * 从表单提交数据构造对象   * @param request
   * @return
   * @throws Exception
   */
  public static Object build(Map request) throws Exception {
    Object paramObj = request.get(YHActionKeys.DTO_CLASS);    
    String mainClassName = null;
    if (paramObj instanceof String) {
      mainClassName = (String)paramObj;
    }else {
      mainClassName = ((String[])paramObj)[0];
    }
    if (YHUtility.isNullorEmpty(mainClassName)) {
      return null;
    }
    Class mainClass = Class.forName(mainClassName);
    return  build(request, mainClass, null);
  }
  
  /**
   * 从表单创建DTO对象
   * @param request
   * @return
   * @throws Exception
   */
  public static Object build(Map request,
      Class cls, String postFix) throws Exception {
    Object obj = cls.newInstance();
    Field[] fieldArray = cls.getDeclaredFields();
    for (int i = 0; i < fieldArray.length; i++) {
      Field field = fieldArray[i];
      Class fieldType = field.getType(); 
      String fieldName = field.getName();
      //明细对象集合情况
      if (List.class.isAssignableFrom(fieldType)
          || Map.class.isAssignableFrom(fieldType)) {
        String tmpStr = fieldName.endsWith("List") ?
            fieldName.substring(0, fieldName.length() - 4) : 
            fieldName.substring(0, fieldName.length() - 3);
        Object paramObj = request.get(tmpStr + "Class");    
        String className = null;
        String detlCntStr = null;
        if(paramObj!=null) {
          if (paramObj instanceof String) {
            className = (String)paramObj;
          }else {
            className = ((String[])paramObj)[0];
          }
          paramObj = request.get(tmpStr + (YHUtility.isNullorEmpty(postFix) ? "" : "_" + postFix) + "Cnt");    
          
          if (paramObj instanceof String) {
            detlCntStr = (String)paramObj;
          }else {
            detlCntStr = ((String[])paramObj)[0];
          }
        }
        if (YHUtility.isNullorEmpty(className) || YHUtility.isNullorEmpty(detlCntStr)) {
          continue;          
        }
        Class detlClass = Class.forName(className);
        int detlCnt = Integer.parseInt(detlCntStr);
        for (int j = 0; j < detlCnt; j++) {
          String detlPostFix = null;
          if (YHUtility.isNullorEmpty(postFix)) {
            detlPostFix = tmpStr + "_" + String.valueOf(j);
          }else {
            detlPostFix = postFix + "_" + tmpStr + "_" + j;
          }
          Object detlObj = build(request, detlClass, detlPostFix);
          YHReflectUtility.addObj(obj, detlObj, fieldName);
        }
        continue;
      }
      //普通字段情况
      String paramName = fieldName;
      if (!YHUtility.isNullorEmpty(postFix)) {
        paramName += "_" + postFix;
      }
      Object paramObj = request.get(paramName);
      if (paramObj == null) {
        continue;
      }
      String paramValue = null;
      if (paramObj instanceof String) {
        paramValue = (String)paramObj;
      }else {
        paramValue = ((String[])paramObj)[0];
      }
      YHReflectUtility.setValue(obj, fieldName, field.getType(), paramValue);
    }
    return obj;
  }
  /**
   * 从表单提交数据构造对象

   * @param request
   * @return
   * @throws Exception
   */
  public static Map buildMap(Map request) throws Exception {
    Map<String, Object> resualt = new HashMap<String, Object>() ;
  
    //得到所有从表的表明
    Map subTableMap = getValue2Map(request, "detlTable_");
    Set<String> keys = subTableMap.keySet();
    for (String key : keys) {
     // System.out.println(key);
      String[] sp = key.split("_");
      String subIndex = sp[sp.length-1];
      String subTableName = ((String[])subTableMap.get(key))[0];
      request.remove(key);
      Map subFieldMap = getValue2Map(request, "_"+subIndex+"_");
      List<Map<String, Object>> subTable = new ArrayList<Map<String,Object>>();
      int i = 0;
      String reg = "_"+subIndex+"_";
      Map subFieldMapf = getValue2Map(request, reg+i);
      for (; subFieldMapf.size() > 0;) {
       // System.out.println("subFieldMapf : "+subFieldMapf);
        Map<String, Object> sub = new HashMap<String, Object>();
        Set<String> subKeys = subFieldMapf.keySet();
        for (String subKey : subKeys) {
         // System.out.println("subKey : "+subKey);
          String valueTemp = (subKey.split("_"))[0];
          String value = ((String[])subFieldMapf.get(subKey))[0];
          sub.put(valueTemp, value);
          request.remove(subKey);
        }
        subTable.add(sub);
        i++;
        subFieldMapf = getValue2Map(request, reg+i);
      }
      resualt.put(subTableName, subTable);
    }
     keys = request.keySet();
     for (String key : keys) {
      String fieldValue = ((String[])request.get(key))[0];
      resualt.put(key, fieldValue);
    }
    return resualt;
  }
  private static Map getValue2Map(Map m,String reg){
    Map<String, Object> resualt = new HashMap<String, Object>() ;
    Set<String> keys = m.keySet();
    for (String key : keys) {
      if(key.contains(reg)){
        Object value = m.get(key);
        resualt.put(key, value);
      }
    }
    return resualt;
  }
  
  /**
   * 从表单提交数据构造对象

   * @param request
   * @return
   * @throws Exception
   */
  public static List buildList(Map request) throws Exception {
    List<Object> resualt = new  ArrayList<Object>();
    String mainTable = ((String[]) request.get("mainTable"))[0];
    resualt.add(mainTable);
    //得到所有从表的表明
    Map subTableMap = getValue2Map(request, "detlTable_");
    Set<String> keys = subTableMap.keySet();
    List<Object> sub = new  ArrayList<Object>();
    for (String key : keys) {
      String subTableName = ((String[])subTableMap.get(key))[0];
      sub.add(subTableName);
    }
    resualt.add(sub);
    return resualt;
  }
  /**
   * 是否是集合类
   * @param interfaceArray
   * @return
   */
  public static boolean isCollect(Class[] interfaceArray) {
    for (int i = 0; i < interfaceArray.length; i++) {
      Class cls = interfaceArray[i];
      if (cls.equals(Collection.class)
          || cls.equals(Map.class)) {
        return true;
      }
    }
    return false;
  }
  /**
   * 转换成Json对象字符串
   * @param obj
   * @return
   * @throws Exception
   */
  public static StringBuffer toJson(Object obj) throws Exception {
    if (obj == null) {
      return new StringBuffer("{}");
    }
    StringBuffer rtBuf = new StringBuffer("{");
    Class cls = obj.getClass();
    Field[] fieldArray = cls.getDeclaredFields();
    int length = fieldArray.length;
    for (int i = 0; i < length; i++) {
      Field field = fieldArray[i];
      Class fieldType = field.getType(); 
      String fieldName = field.getName();
      rtBuf.append("\""+fieldName+"\"");
      rtBuf.append(":");
      Object value = null;

      if (List.class.isAssignableFrom(fieldType)
          || Map.class.isAssignableFrom(fieldType)) {
        String tmpStr = fieldName.endsWith("List") ?
            fieldName.substring(0, fieldName.length() - 4) : 
            fieldName.substring(0, fieldName.length() - 3);
        
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        Iterator<Object> iObj = YHReflectUtility.itObject(obj, tmpStr);
        int cntFlag = 0;
        while (iObj.hasNext()) {
          Object currObj = iObj.next();
          if (cntFlag > 0) {
            buf.append(",");
          }
          cntFlag++;
          buf.append(toJson(currObj));
        }
        buf.append("]");
        value = buf;
      }else {
        if (int.class.equals(fieldType)
            || Integer.class.equals(fieldType)
            || double.class.equals(fieldType)
            || Double.class.equals(fieldType)) {
          value = YHReflectUtility.getValue(obj, fieldName);
        }else if (Date.class.equals(fieldType)) {
          if((Date)YHReflectUtility.getValue(obj, fieldName)!=null){
            value = "\"" + YHUtility.getDateTimeStr((Date)YHReflectUtility.getValue(obj, fieldName)) + "\"";
          }else{
            value = "\"\"";
          }   
        }else {
          Object valueObj = YHReflectUtility.getValue(obj, fieldName);
          if (valueObj == null) {
            value = "\"\"";
          }else {
            String tmpStr = YHUtility.null2Empty(valueObj.toString());
            tmpStr = encodeSpecial(tmpStr);
            value = "\"" + tmpStr + "\"";
          }
        }
      }
      rtBuf.append(value);
      if (i < length - 1) {
        rtBuf.append(",");
      }
    }
    rtBuf.append("}");
    return rtBuf;
  }
  
  /**
   * 转换成Json对象字符串

   * @param obj
   * @return
   * @throws Exception
   */
  public static StringBuffer toJson2(Object obj) throws Exception {
    if (obj == null) {
      return new StringBuffer("{}");
    }
    StringBuffer rtBuf = new StringBuffer();
    Class cls = obj.getClass();
    Field[] fieldArray = cls.getDeclaredFields();
    int length = fieldArray.length;
    for (int i = 0; i < length; i++) {
      Field field = fieldArray[i];
      Class fieldType = field.getType(); 
      String fieldName = field.getName();
      Object value = null;

      if (List.class.isAssignableFrom(fieldType)
          || Map.class.isAssignableFrom(fieldType)) {
        String tmpStr = fieldName.endsWith("List") ?
            fieldName.substring(0, fieldName.length() - 4) : 
            fieldName.substring(0, fieldName.length() - 3);
        
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        Iterator<Object> iObj = YHReflectUtility.itObject(obj, tmpStr);
        int cntFlag = 0;
        while (iObj.hasNext()) {
          Object currObj = iObj.next();
          if (cntFlag > 0) {
            buf.append(",");
          }
          cntFlag++;
          buf.append(toJson(currObj));
        }
        buf.append("]");
        value = buf;
      }else {
        if (int.class.equals(fieldType)
            || Integer.class.equals(fieldType)
            || double.class.equals(fieldType)
            || Double.class.equals(fieldType)) {
          value = "\""+YHReflectUtility.getValue(obj, fieldName)+"\"";
        }else if (Date.class.equals(fieldType)) {
          if((Date)YHReflectUtility.getValue(obj, fieldName)!=null){
            value = "\"" + YHUtility.getDateTimeStr((Date)YHReflectUtility.getValue(obj, fieldName)) + "\"";
          }else{
            value = "\"\"";
          }   
        }else {
          Object valueObj = YHReflectUtility.getValue(obj, fieldName);
          if (valueObj == null) {
            value = "\"\"";
          }else {
            String tmpStr = YHUtility.null2Empty(valueObj.toString());
            tmpStr = encodeSpecial(tmpStr);
            value = "\"" + tmpStr + "\"";
          }
        }
      }
      rtBuf.append(value);
      if (i < length - 1) {
        rtBuf.append(",");
      }
    }
    return rtBuf;
  }
  
  /**
   * 转换成Json对象字符串
   * @param map
   * @return
   * @throws Exception
   */
  public static StringBuffer toJson(Map<String, String> map) throws Exception {
    if (map == null) {
      return new StringBuffer("{}");
    }
    StringBuffer rtBuf = new StringBuffer("{");
    Iterator<String> iKeys = map.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      String value = map.get(key);
      if (value == null) {
        value = "";
      }
      rtBuf.append(key);
      rtBuf.append(":");
      rtBuf.append("\"" + value + "\",");
    }
    if (rtBuf.length() > 2) {
      rtBuf.delete(rtBuf.length() - 1, rtBuf.length());
    }
    rtBuf.append("}");
    return rtBuf;
  }

  /**
   * 从Json字符串创建Java对象
   * @param json
   * @param cls
   * @return
   * @throws Exception
   */
  public static Object json2Obj(String json, Class cls) throws Exception {
    Map propMap = json2Map(json);
    return build(propMap, cls, null);
  }
  
  /**
   * 从Json字符串创建Java对象
   * @param json
   * @param cls
   * @return
   * @throws Exception
   */
  public static Map json2Map(String json) throws Exception {
    Map propMap = new HashMap<String, String>();
    List<String> tmpArray = YHRegexpUtility.splitJson(json);
    for (String propStr : tmpArray) {
      propStr = propStr.trim();
      int tmpInt = propStr.indexOf(":");
      if (tmpInt < 0) {
        continue;
      }
      String[] tmpArray2 = new String[2];
      tmpArray2[0] = propStr.substring(0, tmpInt);
      tmpArray2[1] = propStr.substring(tmpInt + 1);
      
      if (tmpArray2.length < 2) {
        continue;
      }
      tmpArray2[0] = tmpArray2[0].trim();
      tmpArray2[1] = tmpArray2[1].trim();
      if (tmpArray2[0].charAt(0) == '\"' || tmpArray2[0].charAt(0) == '\'') {
        tmpArray2[0] = tmpArray2[0].substring(1, tmpArray2[0].length() - 1);
      }
      if (tmpArray2[1].charAt(0) == '\"' || tmpArray2[1].charAt(0) == '\'') {
        tmpArray2[1] = tmpArray2[1].substring(1, tmpArray2[1].length() - 1);
      }
      propMap.put(tmpArray2[0], tmpArray2[1]);
    }
    return propMap;
  }
  
  public static String encodeSpecial(String srcStr) {
    if (srcStr == null) {
      return "";
    }
    return srcStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\\\'").replace("\r\n", "\\r\\n").replace("\n", "\\n").replace("\r", "\\r");
  }
}

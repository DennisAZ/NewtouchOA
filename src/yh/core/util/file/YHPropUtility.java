package yh.core.util.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPropField;

public class YHPropUtility {
  //log
  private static Logger log = Logger.getLogger(YHPropUtility.class);

  /**
   * 删除属性文件中的属性
   * @param fromFile
   * @param toFile
   */
  public static void deletePropFile(String toFile, String deleted) {
    try {
      Map deletedMap = new HashMap(); 
      YHFileUtility.load2Map(deleted, deletedMap);
      updatePropFile(toFile, null, deletedMap);
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
  }
  /**
   * 更新属性文件
   * @param toFile
   * @param fromMap
   */
  public static void updatePropFile(String toFile, Map fromMap) {
    if (fromMap == null || fromMap.size() < 1) {
      return;
    }
    try {
      ArrayList fromList = new ArrayList();
      Iterator iKeys = fromMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = (String)iKeys.next();
        String value = (String)fromMap.get(key);
        YHPropField field = new YHPropField();
        field.setFieldName(key);
        field.setFieldValue(value);
        fromList.add(field);
      }
      storeArray2Line(toFile, fromList);
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
  }
  /**
   * 更新属性文件
   * @param fromFile
   * @param toFile
   */
  public static void updatePropFile(String toFile, String fromFile) {
    try {
      Map fromMap = new HashMap(); 
      YHFileUtility.load2Map(fromFile, fromMap);
      ArrayList fromList = new ArrayList();
      Iterator iKeys = fromMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = (String)iKeys.next();
        String value = (String)fromMap.get(key);
        YHPropField field = new YHPropField();
        field.setFieldName(key);
        field.setFieldValue(value);
        fromList.add(field);
      }
      storeArray2Line(toFile, fromList);
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
  }
  
  /**
   * 更新属性文件，这里仅考虑单文件更新，updateProps/deleteProps也仅仅针对单个文件
   * @param propFile
   * @param updateProps
   * @param deleteProps
   */
  private static void updatePropFile(String propFile, Map updateProps, Map deleteProps) {
    try {
      if (updateProps == null) {
        updateProps = new HashMap();
      }
      if (deleteProps == null) {
        deleteProps = new HashMap();
      }
      ArrayList propLineList = new ArrayList();
      YHFileUtility.loadLine2Array(propFile, propLineList);
      for (int i = 0; i < propLineList.size(); i++) {
        String lineStr = ((String)propLineList.get(i)).trim();
        //跳过空白行和注释行
        if (lineStr.length() < 1 || lineStr.startsWith("#")) {
          continue;
        }
        int tmpIndex = lineStr.indexOf("=");
        String tmpStr = "";
        if (tmpIndex > 0) {
          tmpStr = lineStr.substring(0, tmpIndex).trim();
        }
        String propName = tmpStr;
        String updateValue = (String)updateProps.get(propName);
        if (updateValue != null) {
          propLineList.set(i, propName + " = " + updateValue);
          //删除已经更新的属性
          updateProps.remove(propName);
          continue;
        }
        boolean isDelete = deleteProps.get(propName) != null;
        if (isDelete) {
          propLineList.set(i, "#" + lineStr);
          //删除已经删除的属性
          deleteProps.remove(propName);
        }
      }
      //新增加的属性
      Iterator iKeys = updateProps.keySet().iterator();
      while (iKeys.hasNext()) {
        String propName = (String)iKeys.next();
        String propValue = (String)updateProps.get(propName);
        propLineList.add(propName + " = " + propValue);
      }
      YHFileUtility.storeArray2Line(propFile, propLineList);
    }catch(Exception ex) {      
    }
  }
  /**
   * 更新属性文件
   * @param file
   * @return
   * @throws Exception
   */
  public static void storeArray2Line(
      String fileName,
      List fieldList) throws Exception {
    
    ArrayList lineList = new ArrayList(); 
    YHFileUtility.loadLine2Array(fileName, lineList);
    
    storeArray2Line(fileName, lineList, fieldList);
  }
  /**
   * 更新属性数据行
   * @param lineList          属性数据行列表          
   * @param fieldList         字段列表
   */
  private static void updatePropArray(List lineList, List fieldList) {
    
    for (int i = 0; i < fieldList.size(); i++) {
      YHPropField field = (YHPropField)fieldList.get(i);
      boolean isFieldInPros = false;
      for (int j = 0; j < lineList.size(); j++) {
        String propStr = (String)lineList.get(j);
        if (propStr.trim().length() < 1 || propStr.startsWith("#")) {
          continue;
        }
        int tmpIndex = propStr.indexOf("=");
        String tmpKey = "";
        if (tmpIndex > 0) {
          tmpKey = propStr.substring(0, tmpIndex).trim();
        }
        if (!field.getFieldName().equals(tmpKey)) {
          continue;
        }
        isFieldInPros = true;
        if (!field.hasChanged()) {
          continue;
        }
        lineList.set(j, field.getFieldName() + " = " + field.getFieldValue());
      }
      if (!isFieldInPros) {
        lineList.add(field.getFieldName() + " = " + field.getFieldValue());
      }
    }
  }
  /**
   * 更新属性文件
   * @param file
   * @return
   * @throws Exception
   */
  public static void storeArray2Line(
      String fileName,
      ArrayList lineList,
      List fieldList) throws Exception {
    
    updatePropArray(lineList, fieldList);
    YHFileUtility.storeArray2Line(fileName, lineList);
  }
  
  /**
   * 更新属性文件
   * @param fileName
   * @param key
   * @param value
   */
  public static void updateProp(String fileName,
      String key, String value) throws Exception {
    
    ArrayList<YHPropField> fieldList = new ArrayList<YHPropField>();
    YHPropField field = new YHPropField();
    field.setFieldName(key);
    field.setFieldValue(value);
    fieldList.add(field);
    
    storeArray2Line(fileName, fieldList);
  }
}

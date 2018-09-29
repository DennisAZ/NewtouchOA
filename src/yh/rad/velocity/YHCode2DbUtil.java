package yh.rad.velocity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.data.YHDsField;
import yh.core.data.YHDsTable;
import yh.core.data.YHDsType;
import yh.core.util.db.YHORM;
import yh.core.util.db.YHStringFormat;
import yh.rad.velocity.metadata.YHJavaBody;
import yh.rad.velocity.metadata.YHJavaField;
import yh.rad.velocity.metadata.YHJavaHeader;
import yh.rad.velocity.metadata.YHJavaMethod;
import yh.rad.velocity.metadata.YHJavaMethodBody;
import yh.rad.velocity.metadata.YHJavaParam;

public class YHCode2DbUtil {

  public void db2JavaCodefName(Connection conn,String tableName){
    
  }
  /**
   * 
   * @param conn
   * @param tableNo
   * @param packageName
   * @return
   * @throws Exception
   */
  public Map<String, Object> db2JavaCodefNo(Connection conn,String tableNo,String packageName) throws Exception{
    //根据tableNo得到数据表的信息
    YHORM orm = new YHORM();
    Map<String, Object> filters = new  HashMap<String, Object>();
    filters.put("TABLE_NO", tableNo);
    YHDsTable dst = (YHDsTable) orm.loadObjComplex(conn, YHDsTable.class, filters);
    //System.out.println("dst : " + dst);
    return transMap(dst, packageName);
  }
  
  /**
   * 
   * @param dst
   * @param packageName
   * @return
   */
  public Map<String, Object> transMap(YHDsTable dst,String packageName){
    Map<String, Object> result = new HashMap<String, Object>();
    YHJavaHeader jh = new YHJavaHeader();
    YHJavaBody jb = new YHJavaBody();
    String className = dst.getClassName();//得到类名
    List<YHDsField> dsfs = dst.getFieldList();
    
    jb.setClassName(className);
    
    jh.setPackageName(packageName);
    
    for (YHDsField dsf : dsfs) {
      String fieldName = dsf.getPropName();//得到属性名
      String methodName = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);//得到get/set方法名
      int typeInt = dsf.getDataType();
      String typeName = getTypeName(typeInt);//得到类型信息
      String importName = getImportValue(typeInt);//如果为非java.lang包下的类则importName不为空
      String defaultValue = dsf.getDefaultValue();
      
      if(importName != null ) {
        jh.addImportNames(importName);
      }
      
      jb.addFields(YHJavaField.get(fieldName, typeName, "private", defaultValue));
      String getName = "get"+methodName;
      String setName = "";
      if(YHDsType.isBitType(typeInt)){
        setName = "is"+methodName;
      }else{
        setName = "set"+methodName;
      }
      jb.addMethods(YHJavaMethod.get(getName, typeName).setMethodBody(YHJavaMethodBody.get("get", "fieldName", fieldName)));
      jb.addMethods(YHJavaMethod
          .get(setName, "void")
          .addArgs(YHJavaParam.addParam(typeName, fieldName))
          .setMethodBody(YHJavaMethodBody.get("set", "fieldName", fieldName)));
    }
    result.put("className", className);
    result.put("head", jh);
    result.put("body", jb);
    result.put("fileName", className+".java");
    return result;
  }
  /**
   * 
   * @param typeInt
   * @return
   */
  public String getTypeName (int typeInt) {
    String typeName = "";
    if(YHDsType.isBitType(typeInt)) {
      typeName = "boolean";
    } else if(YHDsType.isCharType(typeInt)) {
      typeName = "String";
    } else if(YHDsType.isDateType(typeInt)) {
      typeName = "Date";
    } else if(YHDsType.isDecimalType(typeInt)) {
      typeName =" double";
    } else if(YHDsType.isIntType(typeInt)) {
      typeName = "int";
    } else if(YHDsType.isLongType(typeInt)) {
      typeName = "long";
    }
    return typeName;
  }
  /**
   * 
   * @param typeInt
   * @return
   */
  public String getImportValue (int typeInt) {
    String importValue = null;
    if(YHDsType.isBitType(typeInt)) {
      
    } else if(YHDsType.isCharType(typeInt)) {
      
    } else if(YHDsType.isDateType(typeInt)) {
      importValue = "java.util";
    } else if(YHDsType.isDecimalType(typeInt)) {
      
    } else if(YHDsType.isIntType(typeInt)) {
      
    } else if(YHDsType.isLongType(typeInt)) {
      
    }
    return importValue;
  }
  public static String getFields (Connection conn ,String tableNo) throws Exception {
    String sql = "select FIELD_NAME, FIELD_DESC from oa_field_dicts WHERE TABLE_NO = '" + tableNo + "' order by FIELD_NO ";
    //System.out.println(sql2);
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    StringBuffer fi = new StringBuffer();
    while(rs.next()){
      String value = rs.getString(1);
      String value2 = rs.getString(2);
      if("SEQ_ID".equals(value)){
        continue;
      }
      if(!"".equals(fi.toString())){
        fi.append(",");
      }
      fi.append("{fieldName").append(":'").append(value).append("',").append("fieldDesc").append(":'").append(value2).append("'}");
    }
    return "[" + fi.toString() + "]" ;
  }
}

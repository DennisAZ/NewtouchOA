package yh.rad.dsdef.logic.parser;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

import yh.core.data.YHDsField;
import yh.core.data.YHDsType;
import yh.core.util.db.YHDBUtility;
import yh.rad.dsdef.logic.praserI.YHColumnPraserI;

public class YHMysqlColumnParser implements YHColumnPraserI{
  /**
   * 执行物理表的创建
   * @param tableName
   * @param dsFields
   * @param dbExecType
   * @return
   * @throws Exception 
   */
  public void execPhyicsSql(Connection conn,String tableName,ArrayList<YHDsField> dsFields) throws Exception{
    String tabSql = toPhyicsSql(tableName, dsFields);
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(tabSql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  /**
   * 组装建表语句
   * @param tableName
   * @param dsFields
   * @return
   * @throws Exception 
   */
  public String toPhyicsSql(String tableName,ArrayList<YHDsField> dsFields) throws Exception{
    String result = "";
    String header = "create table " + tableName + " (";
    String content = "";
    for (YHDsField dsField : dsFields) {
      String columnSql = parserColumn(dsField);
      if("".equals(columnSql)){
        continue;
      }
      if(!"".equals(content)){
        content += ",";
      }
      content += columnSql;
    }
    /*ljs20130105 修改引擎从INNODB改为Myisam*/
    String footer = ")ENGINE=MyISAM DEFAULT CHARSET=utf8";
    result = header + content + footer;
    return result;
  }
  /**
   * 组装单个字段
   * @param dsField
   * @return
   * @throws Exception
   */
  public String parserColumn(YHDsField dsField) throws Exception{
    String result = "";
    String fieldName = dsField.getFieldName();//字段名称
    String defaultValue = dsField.getDefaultValue();//默认值
    int typeInt = dsField.getDataType();//数据类型
    int fieldDataLen = dsField.getDisplayLen();//数据长度
    int fieldPrecision = dsField.getFieldPrecision();//数据位长度
    int fieldScale = dsField.getFieldScale();//小数位长度
    String isPrimaryKey = dsField.getIsPrimaryKey();//是否主键
    String isMustFill = dsField.getIsMustFill();//是否必填
    String isIdentity = dsField.getIsIdentity();
    result += fieldName + " ";
    String dataType = "";
    if(YHDsType.isIntType(typeInt)){
      //整数类型  "INT"
      if(fieldPrecision > 0){
        dataType = "INT(" + fieldPrecision + ") ";
      }else{
        dataType = "INT";
      }
    }else if(YHDsType.isDecimalType(typeInt)){
      //带有小数位的数值类型 "number(2,3)";
      if(fieldScale > 0){
        dataType = "DECIMAL(" + fieldPrecision + "," + fieldScale + ") ";
      }else{
        dataType = "INT(" + fieldPrecision + ") ";
      }
    }else if(YHDsType.isDateType(typeInt)){
      //数据类型 "Date"
      if(Types.TIMESTAMP == typeInt ){
        dataType = "DateTime";
      }else if(Types.DATE == typeInt){
        dataType = "Date";
      }else if(Types.TIME == typeInt){
        dataType = "time";
      }
    }else if(YHDsType.isBitType(typeInt)){
      //"char(1)"
      dataType = "char(1)";
    }else if(YHDsType.isCharType(typeInt)){
      //字符串类型 "分为char/varchar"
      if(Types.CHAR == typeInt){
        //"char(1)"
        dataType = "char(" + fieldDataLen + ")";
      }else{
        //"varchar(200)"
        dataType = "varchar(" + fieldDataLen + ")";
      }
    }else if(YHDsType.isClobType(typeInt)){
      //"clob"
      dataType = "text";
    }else if(YHDsType.isLongType(typeInt)){
      //"number(38)"
      dataType = "number(38)";
    }else{
      throw new Exception("oracle数据库中没有找到匹配的数据类型!");
    }
    //默认值处理
    if(defaultValue != null && !"".equals(defaultValue)){
      if(YHDsType.isIntType(typeInt)
          || YHDsType.isLongType(typeInt)
          || YHDsType.isDecimalType(typeInt)){
        dataType += " default " + defaultValue;
      }else{
        dataType += " default '" + defaultValue + "'";
      }
    }
    
    if("1".equals(isPrimaryKey)){
      dataType += " primary key ";
    }
    
    if("1".equals(isMustFill)){
      dataType += " not null ";
    }
    
    if("1".equals(isIdentity)){
      dataType += " unique  auto_increment ";
    }
    result += dataType;
    return result;
  }
}

package yh.rad.velocity.metadata;

import java.sql.Types;

public class YHMsSqlDialect implements YHIDialect{

  public String getTypeName(int typeInt) {
    if (typeInt == Types.TINYINT
        || typeInt == Types.SMALLINT
        || typeInt == Types.BIGINT
        || typeInt == Types.INTEGER
        || typeInt == Types.NUMERIC) {
      return "NUMERIC";
    } else if (typeInt == Types.BIT) {
      return "BIT";
    } else if (typeInt == Types.BINARY ) {
      return "BINARY";
    } else if (typeInt == Types.DOUBLE
        || typeInt == Types.FLOAT) {
      return "DOUBLE";
    } else if (typeInt == Types.DECIMAL) {
      return "DECIMAL";
    } else if (typeInt == Types.REAL ) {
      return "REAL ";
    } else if (typeInt == Types.CHAR) {
      return "CHAR";
    } else if (typeInt == Types.VARCHAR) {
      return "VARCHAR";
    } else if (typeInt == Types.LONGVARCHAR) {
      return "TEXT";
    } else if (typeInt == Types.TIMESTAMP 
        || typeInt == Types.TIME
        || typeInt == Types.DATE) {
      return "DATETIME";
    } else if (typeInt == Types.VARBINARY) {
      return "VARBINARY";
    } else if (typeInt == Types.LONGVARBINARY) {
      return "IMAGE";
    } else if (typeInt == Types.NULL) {
      return "NULL";
    } else if (typeInt == Types.OTHER) {
      return "OTHER";
    } else if (typeInt == Types.JAVA_OBJECT) {
      return "JAVA_OBJECT";
    } else if (typeInt == Types.DISTINCT) {
      return "DISTINCT";
    } else if (typeInt == Types.STRUCT) {
      return "STRUCT";
    } else if (typeInt == Types.ARRAY) {
      return "ARRAY";
    } else if (typeInt == Types.BLOB) {
      return "BLOB";
    } else if (typeInt == Types.CLOB) {
      return "CLOB";
    } else if (typeInt == Types.REF) {
      return "REF";
    } else if (typeInt == Types.DATALINK) {
      return "DATALINK";
    } else if (typeInt == Types.BOOLEAN) {
      return "BOOLEAN";
    }
    return null;
  }

}

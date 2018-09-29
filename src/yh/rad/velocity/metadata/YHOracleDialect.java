package yh.rad.velocity.metadata;

import java.sql.Types;

public class YHOracleDialect implements YHIDialect{

  public String getTypeName(int typeInt) {
    if (typeInt == Types.TINYINT 
        || typeInt == Types.BIGINT
        || typeInt == Types.BIT
        || typeInt == Types.SMALLINT 
        || typeInt == Types.INTEGER) {
      return "NUMBER";
    } else if (typeInt == Types.FLOAT) {
      return "FLOAT";
    } else if (typeInt == Types.REAL) {
      return "REAL";
    } else if (typeInt == Types.DOUBLE) {
      return "DOUBLE";
    } else if (typeInt == Types.DECIMAL 
        || typeInt == Types.NUMERIC) {
      return "DECIMAL";
    } else if (typeInt == Types.CHAR) {
      return "CHAR";
    } else if (typeInt == Types.VARCHAR) {
      return "VARCHAR2";
    } else if (typeInt == Types.LONGVARCHAR) {
      return "NVARCHAR";
    } else if (typeInt == Types.DATE 
        || typeInt == Types.TIME
        || typeInt == Types.TIMESTAMP) {
      return "DATE";
    } else if (typeInt == Types.VARBINARY 
        || typeInt == Types.BINARY) {
      return "RAW";
    } else if (typeInt == Types.LONGVARBINARY) {
      return "LONG RAW";
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

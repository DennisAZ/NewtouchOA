package yh.rad.velocity.metadata;

import java.sql.Types;

public class YHMySqlDialect implements YHIDialect{

  public String getTypeName(int typeInt) {
    if (typeInt == Types.TINYINT
        || typeInt == Types.BIT) {
      return "BIT";
    } else if (typeInt == Types.FLOAT) {
      return "FLOAT";
    } else if (typeInt == Types.SMALLINT) {
      return "SMALLINT";
    } else if (typeInt == Types.BIGINT) {
      return "BIGINT";
    } else if (typeInt == Types.INTEGER) {
      return "INT";
    } else if (typeInt == Types.REAL
        || typeInt == Types.DOUBLE) {
      return "DOUBLE";
    } else if (typeInt == Types.DECIMAL 
        || typeInt == Types.NUMERIC) {
      return "DECIMAL";
    } else if (typeInt == Types.CHAR) {
      return "CHAR";
    } else if (typeInt == Types.VARCHAR) {
      return "VARCHAR";
    } else if (typeInt == Types.LONGVARCHAR) {
      return "TEXT";
    } else if (typeInt == Types.DATE ) {
      return "DATE";
    } else if (typeInt == Types.TIMESTAMP ) {
      return "TIMESTAMP";
    } else if (typeInt == Types.TIME ) {
      return "TIME";
    } else if (typeInt == Types.VARBINARY) {
      return "TINYBLOB";
    } else if (typeInt == Types.LONGVARBINARY) {
      return "BLOB";
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

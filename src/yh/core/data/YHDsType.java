package yh.core.data;

import java.sql.Types;

import yh.core.global.YHDsFieldConst;

public class YHDsType {
  /**
   * 是否是整型
   * 
   * 
   * @param typeInt
   * @return
   */
  public static boolean isIntType(int typeInt) {
    if (typeInt == Types.INTEGER || typeInt == Types.SMALLINT
        || typeInt == Types.TINYINT) {

      return true;
    }
    return false;
  }
  /**
   * 是否是CLOB
   * 
   * 
   * @param typeInt
   * @return
   */
  public static boolean isClobType(int typeInt) {
    if (typeInt == Types.CLOB) {

      return true;
    }
    return false;
  }

  /**
   * 是否是长整型
   * 
   * @param typeInt
   * @return
   */
  public static boolean isLongType(int typeInt) {
    if (typeInt == Types.BIGINT || typeInt == Types.NUMERIC) {
      return true;
    }
    return false;
  }

  /**
   * 是否是日期型
   * 
   * @param typeInt
   * @return
   */
  public static boolean isDateType(int typeInt) {
    if (typeInt == Types.DATE || typeInt == Types.TIME
        || typeInt == Types.TIMESTAMP) {

      return true;
    }
    return false;
  }

  /**
   * 是否是日期型
   * 
   * @param typeInt
   * @return
   */
  public static boolean isDecimalType(int typeInt) {
    if (typeInt == Types.FLOAT
        || typeInt == Types.REAL
        || typeInt == Types.DOUBLE
        || typeInt == Types.DECIMAL
        || typeInt == Types.NUMERIC) {

      return true;
    }
    return false;
  }

  /**
   * 是否是字符串类型
   * 
   * @param typeInt
   * @return
   */
  public static boolean isCharType(int typeInt) {
    if (typeInt == Types.CHAR || typeInt == Types.VARCHAR
        || typeInt == Types.LONGVARCHAR) {

      return true;
    }
    return false;
  }

  /**
   * 是否是字符串类型
   * 
   * @param typeInt
   * @return
   */
  public static boolean isBitType(int typeInt) {
    if (typeInt == Types.BIT) {
      return true;
    }
    return false;
  }

  /**
   * 把JDBC用整型表示的类型转换成文字形式
   * 
   * @param typeInt
   * @return
   */
  public static String getTypeName(int typeInt) {
    if (typeInt == Types.BIT) {
      return "BIT";
    } else if (typeInt == Types.TINYINT) {
      return "TINYINT";
    } else if (typeInt == Types.SMALLINT) {
      return "SMALLINT";
    } else if (typeInt == Types.INTEGER) {
      return "INTEGER";
    } else if (typeInt == Types.BIGINT) {
      return "BIGINT";
    } else if (typeInt == Types.FLOAT) {
      return "FLOAT";
    } else if (typeInt == Types.REAL) {
      return "REAL";
    } else if (typeInt == Types.DOUBLE) {
      return "DOUBLE";
    } else if (typeInt == Types.NUMERIC) {
      return "NUMERIC";
    } else if (typeInt == Types.DECIMAL) {
      return "DECIMAL";
    } else if (typeInt == Types.CHAR) {
      return "CHAR";
    } else if (typeInt == Types.VARCHAR) {
      return "VARCHAR";
    } else if (typeInt == Types.LONGVARCHAR) {
      return "LONGVARCHAR";
    } else if (typeInt == Types.DATE) {
      return "DATE";
    } else if (typeInt == Types.TIME) {
      return "TIME";
    } else if (typeInt == Types.TIMESTAMP) {
      return "TIMESTAMP";
    } else if (typeInt == Types.BINARY) {
      return "BINARY";
    } else if (typeInt == Types.VARBINARY) {
      return "VARBINARY";
    } else if (typeInt == Types.LONGVARBINARY) {
      return "LONGVARBINARY";
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
    return "";
  }

  /**
   * 取得数据类型的种类
   * 
   * @param fieldType
   *          数据类型
   * @return
   */
  public static int getTypeCatalog(int fieldType) {
    if (fieldType == Types.TINYINT) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.SMALLINT) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.INTEGER) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.BIGINT) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.FLOAT) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.REAL) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.DOUBLE) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.NUMERIC) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.DECIMAL) {
      return YHDsFieldConst.TYPE_CATA_NUMBER;
    } else if (fieldType == Types.CHAR) {
      return YHDsFieldConst.TYPE_CATA_STRING;
    } else if (fieldType == Types.VARCHAR) {
      return YHDsFieldConst.TYPE_CATA_STRING;
    } else if (fieldType == Types.LONGVARCHAR) {
      return YHDsFieldConst.TYPE_CATA_STRING;
    } else if (fieldType == Types.DATE) {
      return YHDsFieldConst.TYPE_CATA_DATE;
    } else if (fieldType == Types.TIME) {
      return YHDsFieldConst.TYPE_CATA_TIME;
    } else if (fieldType == Types.BINARY) {
      return YHDsFieldConst.TYPE_CATA_BINARY;
    } else if (fieldType == Types.VARBINARY) {
      return YHDsFieldConst.TYPE_CATA_BINARY;
    } else if (fieldType == Types.LONGVARBINARY) {
      return YHDsFieldConst.TYPE_CATA_BINARY;
    } else if (fieldType == Types.NULL) {
      return YHDsFieldConst.TYPE_CATA_NULL;
    } else if (fieldType == Types.BOOLEAN) {
      return YHDsFieldConst.TYPE_CATA_BOOL;
    } else if (fieldType == Types.TIMESTAMP) {
      return YHDsFieldConst.TYPE_CATA_DATE;
    }
    return YHDsFieldConst.TYPE_CATA_STRING;
  }
}

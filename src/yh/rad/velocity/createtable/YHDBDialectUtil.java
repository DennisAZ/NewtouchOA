package yh.rad.velocity.createtable;

import yh.rad.velocity.metadata.YHIDialect;
import yh.rad.velocity.metadata.YHMsSqlDialect;
import yh.rad.velocity.metadata.YHMySqlDialect;
import yh.rad.velocity.metadata.YHOracleDialect;

public class YHDBDialectUtil {
  public static final String ORACLEDIALECT = "Oracle";
  public static final String MYSQLDIALECT = "MySql";
  public static final String MSSQLDIALECT = "MsSql";
  
  public static YHIDialect getDialect(String dialect) {
    YHIDialect dia = null;
    if(ORACLEDIALECT.equals(dialect)){
      dia = new YHOracleDialect();
    }if(MYSQLDIALECT.equals(dialect)){
      dia = new YHMySqlDialect();
    }if(MSSQLDIALECT.equals(dialect)){
      dia = new YHMsSqlDialect();
    }
    return dia;
  }
}

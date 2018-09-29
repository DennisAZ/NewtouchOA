package yh.core.util;

import java.sql.Connection;

import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.db.YHDBUtility;

/**
 * @author Administrator
 */
public class TestDbUtil {
  private static String sysConfigFile = "D:\\project\\test\\sysconfig.properties";
  
  static {
    YHSysProps.setProps(YHConfigLoader.loadSysProps(sysConfigFile));
  }
  public static Connection getConnection(boolean autoCommit, String dbName) throws Exception {
    YHDBUtility dbUtil = new YHDBUtility();
    return dbUtil.getConnection(autoCommit, dbName);
  }
}

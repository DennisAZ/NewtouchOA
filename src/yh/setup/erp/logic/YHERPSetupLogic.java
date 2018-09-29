package yh.setup.erp.logic;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.auth.YHAuthenticator;
import yh.core.util.file.YHPropUtility;
import yh.setup.util.YHERPSetupUitl;

public class YHERPSetupLogic {
  private static Logger log = Logger.getLogger(YHERPSetupLogic.class);

  /**
   * 创建erp运行平台的数据库 1.判断TDSYS库是否存在，如果存在不按装，否则需要创建TDSYS
   * 1.创建TDSYS数据库
   * 2.注册YH数据库到TDSYS
   * @throws Exception 
   */
  public  void createSysDb(String erpcontextPath) throws Exception {
    String installPath = YHSysProps.getRootPath();
    //String installPath = "D";
    String path = installPath + "\\webroot\\" + erpcontextPath + "\\sqlfiles\\";
    String tdSysDbSQLFILES = YHSysProps.getProp("tdSysDbSQLFILES");
    String yhdbRegDbSqlFiles = YHSysProps.getProp("yhdbRegDbSqlFiles." + YHSysProps.getProp("db.jdbc.dbms"));
    String[] stryhRegSrc = yhdbRegDbSqlFiles.split(",");

    String strSrc[] = tdSysDbSQLFILES.split(",");
    if (YHERPSetupUitl.isDbActive("TDSYS",dbmsName)) {
      try {
        YHERPSetupUitl.backupDb("TDSYS", "installbackup",dbmsName);
      }catch(Exception ex) {
        log.debug(ex.getMessage(), ex);
      }
      try {
        YHERPSetupUitl.forceDropDb("TDSYS",dbmsName);
      }catch(Exception ex) {
        log.debug(ex.getMessage(), ex);
      }        
    }
    YHERPSetupUitl.executionSQLFile(path, "TDSYS", strSrc);
    YHERPSetupUitl.regEaDbms2TDSYS(path, "TDSYS", stryhRegSrc); //注册YH数据库
  }
  
  private String dbmsName = null;
  private Map<String, String> dbConfigPopsMap = null;
  /**
   * 构造方法
   * @param paramMap
   * @param dbmsName
   * @throws Exception 
   */
  public YHERPSetupLogic(Map paramMap,String dbmsName,String contextPath,String erpContextPath) throws Exception{
    String installPath = YHSysProps.getRootPath();
    String yherpsetupConfFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\erp\\yherpsetup.properties";
    
    String yherpSysConfFile = installPath + "\\webroot\\" + erpContextPath + "\\WEB-INF\\config\\sysconfig.properties";
    String yhSysConfFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\sysconfig.properties";

    YHSysProps.addProps(YHConfigLoader.loadSysProps(yherpsetupConfFile));
    
    this.dbmsName = dbmsName;
    dbConfigPopsMap = new HashMap<String, String>();
    
    String conIp = ((String[])paramMap.get("conIp"))[0];
    String conPort = ((String[])paramMap.get("conPort"))[0];
    String driver = ((String[]) paramMap.get("driver"))[0];
    String conurl = "";
    String userName = ((String[]) paramMap.get("userName"))[0];
    String passward = ((String[]) paramMap.get("passward"))[0];
    passward = YHAuthenticator.ciphEncryptStr(passward);
    conurl = "jdbc:microsoft:sqlserver://" + conIp + ":" + conPort + ";";
    dbConfigPopsMap.put("db.jdbc.driver." + dbmsName,driver);
    dbConfigPopsMap.put("db.jdbc.conurl." + dbmsName,conurl);
    dbConfigPopsMap.put("db.jdbc.userName." + dbmsName,userName);
    dbConfigPopsMap.put("db.jdbc.passward." + dbmsName,passward);
    setDbPops(dbConfigPopsMap);
    dbConfigPopsMap = addYHDbConfig(dbConfigPopsMap);
    YHSysProps.updateProp("useYHErp", "1");
//    dbConfigPopsMap.put("useYHErp", "1");
    YHPropUtility.updateProp(yhSysConfFile, "useYHErp", "1");
    YHERPSetupUitl.modifySysConfig(dbConfigPopsMap, yherpSysConfFile);
  }
  
  public  static Map<String, String> addYHDbConfig(Map<String, String> dbConfigPopsMap){
    String yhDbms = YHSysProps.getString("db.jdbc.dbms");
    dbConfigPopsMap.put("db.jdbc.driver." + yhDbms,YHSysProps.getString("db.jdbc.driver." + yhDbms));
    dbConfigPopsMap.put("db.jdbc.conurl." + yhDbms,YHSysProps.getString("db.jdbc.conurl." + yhDbms));
    dbConfigPopsMap.put("db.jdbc.userName." + yhDbms,YHSysProps.getString("db.jdbc.userName." + yhDbms));
    dbConfigPopsMap.put("db.jdbc.passward." + yhDbms,YHSysProps.getString("db.jdbc.passward." + yhDbms));
    return dbConfigPopsMap;
  }
  
  /**
   * 设置数据库的配置项
   * @param paramMap
   * @param dbmsName
   */
  public void setDbPops(Map paramMap){
    YHSysProps.addProps(paramMap);
  }
}

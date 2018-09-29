package yh.setup.fis.logic;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.auth.YHAuthenticator;
import yh.setup.ea.logic.YHEASetupUtil;
import yh.setup.util.YHERPSetupUitl;

public class YHFISSetupLogic {
  private static Logger log = Logger.getLogger(YHFISSetupLogic.class);

  private String dbmsName = null;
  private Map<String, String> dbConfigPopsMap = null;

  /**
   * 构造方法
   * @param paramMap
   * @param dbmsName
   * @param contextPath
   * @throws Exception
   */
  public YHFISSetupLogic(Map paramMap,String dbmsName,String contextPath) throws Exception{
    String installPath = YHSysProps.getRootPath();
    String yherpsetupConfFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\erp\\yherpsetup.properties";
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
  }
  /**
   * 设置数据库的配置项
   * @param paramMap
   * @param dbmsName
   */
  public void setDbPops(Map paramMap){
    YHSysProps.addProps(paramMap);
  }
  /**
   * 创建财务账套的数据库 -- 安装TD01,TD02...库
   * 1.安装TD01,TD02...数据库
   * 2.注册TD01到TDSYS
   * @param erpcontextPath  财务安装路径
   * @param sysdbName 财务系统库，TDSYS
   * @throws Exception 
   */
  public void createFisDb(String erpcontextPath,String sysdbName) throws Exception {
   // tdErpDbSqlFiles
    String installPath = YHSysProps.getRootPath();
    YHFISSetupUtil fisu = new YHFISSetupUtil();
    fisu.createDefaultAcset(installPath, erpcontextPath, dbmsName, sysdbName);
  }
  
  /**
   * 向YH中插入erp的菜单
   * @throws Exception 
   */
  public void insertErpMeun2YH(String path,String[] strSrc,String sysDbName) throws Exception {
    YHERPSetupUitl.deleteErpMenu2YH("87",sysDbName);
    YHEASetupUtil easu  =  new YHEASetupUtil();
    easu.insertErpMeun2YH(path, strSrc,sysDbName);
  }
  
  /**
   * 向YH中插入erp的菜单
   * @throws Exception 
   */
  public void insertErpMeun2YH(String erpcontextPath) throws Exception {
    String installPath = YHSysProps.getRootPath();
    String path = installPath + "\\webroot\\" + erpcontextPath + "\\sqlfiles\\";
    String yhMenuRegDbSqlFiles = YHSysProps.getProp("yhMenuFisRegDbSqlFiles." + YHSysProps.getString("db.jdbc.dbms"));
    String yhsysDbName = YHSysProps.getProp("yhsysDbName." + YHSysProps.getString("db.jdbc.dbms"));

    String[] strMenuSrc = yhMenuRegDbSqlFiles.split(",");
    insertErpMeun2YH(path, strMenuSrc,yhsysDbName);
  }
  
  public void clearFisDbInfo(){
    
  }
}

package yh.setup.ea.logic;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.auth.YHAuthenticator;
import yh.setup.util.YHERPSetupUitl;


/**
 * 进销存安装处理
 * 
 * @author tulaike
 * 
 */
public class YHEASetupUtil {

  private static Logger log = Logger.getLogger(YHEASetupUtil.class);

  public YHEASetupUtil() {
    // TODO Auto-generated constructor stub
  }
  private String dbmsName = null;
  private Map<String, String> dbConfigPopsMap = null;
  /**
   * 构造方法
   * @param paramMap
   * @param dbmsName
   * @throws Exception 
   */
  public YHEASetupUtil(Map paramMap,String dbmsName,String contextPath) throws Exception{
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
   * 创建进销存的数据库 -- 安装TDERP库
   * 1.安装TDERP数据库
   * 2.注册TDERP到TDSYS
   * 
   * @throws Exception 
   */
  public void createErpDb(String erpcontextPath) throws Exception {
   // tdErpDbSqlFiles
    String installPath = YHSysProps.getRootPath();
    String path = installPath + "\\webroot\\" + erpcontextPath + "\\sqlfiles\\";
    String tdErpDbSqlFiles = YHSysProps.getProp("tdErpDbSqlFiles");
    String tdErpRegDbSqlFiles = YHSysProps.getProp("tdErpRegDbSqlFiles");
    String yhMenuRegDbSqlFiles = YHSysProps.getProp("yhMenuEaRegDbSqlFiles." + YHSysProps.getString("db.jdbc.dbms"));
    String yhsysDbName = YHSysProps.getProp("yhsysDbName." + YHSysProps.getString("db.jdbc.dbms"));
    String[] strSrc = tdErpDbSqlFiles.split(",");
    String[] strRegSrc = tdErpRegDbSqlFiles.split(",");
    String[] strMenuSrc = yhMenuRegDbSqlFiles.split(",");
    if (YHERPSetupUitl.isDbActive("TDERP",dbmsName)) {
      try {
        YHERPSetupUitl.backupDb("TDERP", "installbackup",dbmsName);
      }catch(Exception ex) {
        log.debug(ex.getMessage(), ex);
      }
      try {
        YHERPSetupUitl.forceDropDb("TDERP",dbmsName);
      }catch(Exception ex) {
        log.debug(ex.getMessage(), ex);
      }        
    }
    YHERPSetupUitl.executionSQLFile(path, "TDERP", strSrc);//创建TDERP数据库
    YHERPSetupUitl.regEaDbms2TDSYS(path, "TDSYS", strRegSrc);//注册TDSYS数据库
    YHERPSetupUitl.deleteErpMenu2YH("89",yhsysDbName);
    insertErpMeun2YH(path, strMenuSrc,yhsysDbName);
  }
  

  /**
   * 向YH中插入erp的菜单
   * @throws Exception 
   */
  public void insertErpMeun2YH(String path,String[] strSrc,String sysDbName) throws Exception {
    for (int i = 0; i < strSrc.length; i++) {
      String filePath = path + strSrc[i];
      YHERPSetupUitl.exeSql(filePath,YHSysProps.getProp("db.jdbc.dbms"),sysDbName);
    }
  }

  /**
   * 清除数据库
   */
  public void clearEaDbInfo(){
    
  }
  
}

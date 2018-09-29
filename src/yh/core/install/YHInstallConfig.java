package yh.core.install;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipOutputStream;

import yh.core.data.YHAuthKeys;
import yh.core.data.YHPropField;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHAuthenticator;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.file.YHPropUtility;
import yh.core.util.form.YHFOM;
import yh.core.util.net.YHSocketUtil;
import yh.oa.tools.StaticData;

/**
 * 安装配置工具类
 * @author jpt
 *
 */
public class YHInstallConfig {
  private static Logger log = Logger.getLogger(YHInstallConfig.class);
  /**
   * 执行系统初始化配置
   * @param installPath
   * @param contextPath
   * @param params
   * @throws Exception
   */
  public static void configPath(String installPath,
      String contextPath) throws Exception {
    List logList = new ArrayList();
    logList.add("系统的安装路径 >> " + installPath);
    logList.add("YH应用路径 >> " + contextPath);
    //Log4J配置文件
    String logConfig = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\classes\\log4j.properties";
    List<YHPropField> rules = new ArrayList<YHPropField>();
    YHPropField field = new YHPropField();
    field.setFieldName(YHSysPropKeys.ROOT_DIR);
    field.setFieldValue(installPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\webroot\\\\" + contextPath + "\\\\");
    rules.add(field);
    try {
      YHPropUtility.storeArray2Line(logConfig, rules);
      logList.add("Log4j 配置文件正确修改！");
    }catch(Exception ex) {
      log.debug(ex);
    }
    //系统参数配置文件
    String sysFonfig = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\sysconfig.properties";
    rules = new ArrayList();
    //rootDir
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.ROOT_DIR);
    field.setFieldValue(installPath.replaceAll("\\\\", "\\\\\\\\"));
    //dataBaseBackupDir
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.DATABASE_BACKUP_DIR);
    field.setFieldValue(installPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\yh\\\\bak");
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.JSP_ROOT_DIR);
    field.setFieldValue(contextPath);
    //安装时间戳
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.INSTALL_TIME);
    String timeStr = String.valueOf(System.currentTimeMillis());
    field.setFieldValue(timeStr);
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.INSTALL_TIME_PASS);
    field.setFieldValue(YHAuthenticator.encryptBase64(YHAuthKeys.getMD5SaltLength(null) - 2, timeStr));
    field = new YHPropField();
    rules.add(field);
    field.setFieldName("fileUploadTempDir");
    field.setFieldValue(installPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\yh\\\\tmp");
    try {
      YHPropUtility.storeArray2Line(sysFonfig, rules);
      logList.add("系统参数配置文件正确修改！");
    }catch(Exception ex) {
      log.debug(ex);
    }
    //Mysql配置文件 mysql/my.ini
    String mysqlConf = installPath + "\\mysql\\my.ini";
    if (new File(mysqlConf).exists()) {
      List mysqlConfList = new ArrayList();
      YHFileUtility.loadLine2Array(mysqlConf, mysqlConfList);
      for (int i = 0; i < mysqlConfList.size(); i++) {
        String lineStr = mysqlConfList.get(i).toString().trim();
        if (lineStr.startsWith("basedir=")) {
          mysqlConfList.set(i, "basedir=" + installPath.replace("\\", "/") + "/mysql");
        }else if (lineStr.startsWith("datadir=")) {
          mysqlConfList.set(i, "datadir=" + installPath.replace("\\", "/") + "/data");
        }else if (lineStr.startsWith("tmpdir=")) {
          mysqlConfList.set(i, "tmpdir=" + installPath.replace("\\", "/") + "/tmp");
        }
      }
      YHFileUtility.storeArray2Line(mysqlConf, mysqlConfList);
      logList.add("Mysql 配置文件正确修改！");
    }
    
    List cmdList = new ArrayList();
    String filePath = installPath + "\\tomcat\\bin\\registYHService.bat";
    //注册Tomcat服务的文件
    cmdList.add("set JAVA_HOME=" + installPath + "\\jdk");
    cmdList.add("set CATALINA_HOME=" + installPath + "\\tomcat");
    cmdList.add(installPath + "\\tomcat\\bin\\service install YHTomcat");
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //卸载Tomcat服务的文件
    filePath = installPath + "\\tomcat\\bin\\removeYHService.bat";
    cmdList.clear();
    cmdList.add(installPath + "\\tomcat\\bin\\service remove YHTomcat");
    YHFileUtility.storeArray2Line(filePath, cmdList);
    if (new File(mysqlConf).exists()) {
      //注册Mysql服务的文件
      filePath = installPath + "\\tomcat\\bin\\registYHMysql.bat";
      cmdList.clear();
      cmdList.add(installPath + "\\mysql\\bin\\mysqld-nt.exe --install YH_MYSQL");
      YHFileUtility.storeArray2Line(filePath, cmdList);
      //卸载Mysql服务的文件
      filePath = installPath + "\\tomcat\\bin\\removeYHMysql.bat";
      cmdList.clear();
      cmdList.add("net stop YH_MYSQL");
      cmdList.add(installPath + "\\mysql\\bin\\mysqld-nt.exe --remove YH_MYSQL");
      YHFileUtility.storeArray2Line(filePath, cmdList);
    }
    //修改Tomcat服务端口
    filePath = installPath + "\\tomcat\\bin\\updateServPort.bat";
    cmdList.clear();
    cmdList.add("net stop YHTomcat");
    cmdList.add(installPath + "\\tomcat\\bin\\YHCMD.exe \"" + installPath + "\" " + contextPath + " updateTomcatPort 86");
    cmdList.add("net start YHTomcat");
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //修改数据库配置
    filePath = installPath + "\\tomcat\\bin\\updateDbConf.bat";
    cmdList.clear();
    cmdList.add("net stop YHTomcat");
    cmdList.add(installPath + "\\tomcat\\bin\\YHCMD.exe \"" + installPath + "\" " + contextPath + " upddateDbConf -host192.168.0.100 -port3306 -dbmsoracle -useryzq -pass123456 -sysdbTD_OA");
    cmdList.add("net start YHTomcat");
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //启动服务
    filePath = installPath + "\\tomcat\\bin\\startYH.bat";
    cmdList.clear();
    cmdList.add("net start YHTomcat");
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //停止服务
    filePath = installPath + "\\tomcat\\bin\\stopYH.bat";
    cmdList.clear();
    cmdList.add("net stop YHTomcat");
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //探测端口使用情况?
    int port = getTomcatPort(installPath + "\\tomcat\\conf\\server.xml");
    if (YHSocketUtil.isPortUsed(port)) {
      port = YHSocketUtil.getNotUsetPort(86);
      updateTomcatPort(installPath + "\\tomcat\\conf\\server.xml", port);
    }
    logList.add("Web 服务端口 >> " + port);
    
    YHFileUtility.storeArray2Line(installPath + "\\install.log", logList);
  }
  /**
   * 执行配置报表安装路径
   * @param installPath
   * @param contextPath
   * @param params
   * @throws Exception
   */
  public void configRaPath(String installPath) throws Exception {
   
    //修改php.ini目录
    String strSrcFileName = installPath + "\\bin\\php.ini";

    List buffList = new ArrayList();
    try {
      YHFileUtility.loadLine2Array(strSrcFileName, buffList, "GBK");
      int updateCnt = 0;
      for (int i = 0; i < buffList.size(); i++) {
        String lineStr = (String)buffList.get(i);

        if (lineStr.indexOf("D:/") >= 0) {
          lineStr = lineStr.replace("D:/yh/",  installPath.replace("\\", "/") + "/");
          lineStr = lineStr.replace("D:/YH/",  installPath.replace("\\", "/") + "/");

          buffList.set(i, lineStr);
          updateCnt++;
        }
      }
      if (updateCnt > 0) {
        YHFileUtility.storeArray2Line(strSrcFileName, buffList, "GBK");
      }
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
  }
  
  /**
   * 执行配置Discuz的安装路径
   * @param installPath
   * @param contextPath
   * @param params
   * @throws Exception
   */
  public void configHttpdPath(String installPath) throws Exception {
    //修改httpd.conf
    File confs = new File(installPath 
        + File.separatorChar + "conf"
        + File.separatorChar + "extra"
        + File.separatorChar + "vhosts");
    
    File[] files = confs.listFiles(new FileFilter() {

      public boolean accept(File f) {
        if (f != null && f.getName().endsWith(".conf")) {
          return true;
        }
        return false;
      }
    });
    
    List<File> fileList = new ArrayList<File>(Arrays.asList(files));
    fileList.add(new File(installPath + File.separatorChar + "conf" +
        File.separatorChar + "httpd.conf"));
    
    for (File f : fileList) {
      String fileName = f.getAbsolutePath();
      List buffList = new ArrayList();
      try {
        YHFileUtility.loadLine2Array(fileName, buffList, "GBK");
        int updateCnt = 0;
        for (int i = 0; i < buffList.size(); i++) {
          String lineStr = (String)buffList.get(i);
          
          if (lineStr.indexOf("D:/") >= 0) {
            lineStr = lineStr.replace("D:/YH/", installPath.replace("\\", "/") + "/");
            lineStr = lineStr.replace("D:/yh/", installPath.replace("\\", "/") + "/");
            buffList.set(i, lineStr);
            updateCnt++;
          }
        }
        if (updateCnt > 0) {
          YHFileUtility.storeArray2Line(fileName, buffList, "GBK");
        }
      }catch(Exception ex) {
        log.debug(ex.getMessage(), ex);
      }
    }
  }
  
  
  
  /**
   * 执行Esb系统初始化配置   * @param installPath
   * @param contextPath
   * @param params
   * @throws Exception
   */
  public static void configEsbPath(String installPath,
      String contextPath , boolean isServer) throws Exception {
    List logList = new ArrayList();
    logList.add("ESB系统的安装路径 >> " + installPath);
    logList.add("YHESB应用路径 >> " + contextPath);
    //Log4J配置文件
    String logConfig = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\classes\\log4j.properties";
    List<YHPropField> rules = new ArrayList<YHPropField>();
    YHPropField field = new YHPropField();
    field.setFieldName(YHSysPropKeys.ROOT_DIR);
    field.setFieldValue(installPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\webroot\\\\" + contextPath + "\\\\");
    rules.add(field);
    try {
      YHPropUtility.storeArray2Line(logConfig, rules);
      logList.add("Log4j 配置文件正确修改！");
    }catch(Exception ex) {
      log.debug(ex);
    }
    //系统参数配置文件
    String sysFonfig = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\sysconfig.properties";
    rules = new ArrayList();
    //rootDir
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.ROOT_DIR);
    field.setFieldValue(installPath.replaceAll("\\\\", "\\\\\\\\"));
    //dataBaseBackupDir
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.DATABASE_BACKUP_DIR);
    field.setFieldValue(installPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\yh\\\\bak");
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.JSP_ROOT_DIR);
    field.setFieldValue(contextPath);
    //安装时间戳
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.INSTALL_TIME);
    String timeStr = String.valueOf(System.currentTimeMillis());
    field.setFieldValue(timeStr);
    field = new YHPropField();
    rules.add(field);
    field.setFieldName(YHSysPropKeys.INSTALL_TIME_PASS);
    field.setFieldValue(YHAuthenticator.encryptBase64(YHAuthKeys.getMD5SaltLength(null) - 2, timeStr));
    try {
      YHPropUtility.storeArray2Line(sysFonfig, rules);
      logList.add("系统参数配置文件正确修改！");
    }catch(Exception ex) {
      log.debug(ex);
    }
    //Mysql配置文件 mysql/my.ini
      String mysqlConf = installPath + "\\mysql\\my.ini";
      if (new File(mysqlConf).exists()) {
        List mysqlConfList = new ArrayList();
        YHFileUtility.loadLine2Array(mysqlConf, mysqlConfList);
        for (int i = 0; i < mysqlConfList.size(); i++) {
          String lineStr = mysqlConfList.get(i).toString().trim();
          if (lineStr.startsWith("basedir=")) {
            mysqlConfList.set(i, "basedir=" + installPath.replace("\\", "/") + "/mysql");
          }else if (lineStr.startsWith("datadir=")) {
            mysqlConfList.set(i, "datadir=" + installPath.replace("\\", "/") + "/data");
          }else if (lineStr.startsWith("tmpdir=")) {
            mysqlConfList.set(i, "tmpdir=" + installPath.replace("\\", "/") + "/tmp");
          }
        }
        YHFileUtility.storeArray2Line(mysqlConf, mysqlConfList);
        logList.add("Mysql 配置文件正确修改！");
      }
    List cmdList = new ArrayList();
    String filePath = installPath + "\\tomcat\\bin\\registYHService.bat";
    //注册Tomcat服务的文件    cmdList.add("set JAVA_HOME=" + installPath + "\\jdk");
    cmdList.add("set CATALINA_HOME=" + installPath + "\\tomcat");
    String tmp = "YHESBClient";
    if (isServer) {
      tmp = "YHESBServer";
    }
    cmdList.add(installPath + "\\tomcat\\bin\\service install " + tmp);
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //卸载Tomcat服务的文件    filePath = installPath + "\\tomcat\\bin\\removeYHService.bat";
    cmdList.clear();
    cmdList.add("net stop " + tmp);
    cmdList.add(installPath + "\\tomcat\\bin\\service remove " + tmp);
    YHFileUtility.storeArray2Line(filePath, cmdList);
    if (new File(mysqlConf).exists()) {
      //注册Mysql服务的文件      filePath = installPath + "\\tomcat\\bin\\registYHMysql.bat";
      cmdList.clear();
      cmdList.add(installPath + "\\mysql\\bin\\mysqld-nt.exe --install YH_ESBMYSQL");
      YHFileUtility.storeArray2Line(filePath, cmdList);
      //卸载Mysql服务的文件      filePath = installPath + "\\tomcat\\bin\\removeYHMysql.bat";
      cmdList.clear();
      cmdList.add("net stop YH_ESBMYSQL");
      cmdList.add(installPath + "\\mysql\\bin\\mysqld-nt.exe --remove YH_ESBMYSQL");
      YHFileUtility.storeArray2Line(filePath, cmdList);
    }
    //修改Tomcat服务端口
    filePath = installPath + "\\tomcat\\bin\\updateServPort.bat";
    cmdList.clear();
    cmdList.add("net stop " + tmp);
    cmdList.add(installPath + "\\tomcat\\bin\\YHCMD.exe \"" + installPath + "\" " + contextPath + " updateTomcatPort 86");
    cmdList.add("net start " + tmp);
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //修改数据库配置    filePath = installPath + "\\tomcat\\bin\\updateDbConf.bat";
    cmdList.clear();
    cmdList.add("net stop " + tmp);
    cmdList.add(installPath + "\\tomcat\\bin\\YHCMD.exe \"" + installPath + "\" " + contextPath + " upddateDbConf -host192.168.0.100 -port3306 -dbmsoracle -useryzq -pass123456 -sysdbTD_OA");
    cmdList.add("net start " + tmp);
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //启动服务
    filePath = installPath + "\\tomcat\\bin\\startYH.bat";
    cmdList.clear();
    cmdList.add("net start " + tmp);
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //停止服务
    filePath = installPath + "\\tomcat\\bin\\stopYH.bat";
    cmdList.clear();
    cmdList.add("net stop " + tmp);
    YHFileUtility.storeArray2Line(filePath, cmdList);
    //探测端口使用情况?
    int port = getTomcatPort(installPath + "\\tomcat\\conf\\server.xml");
    if (YHSocketUtil.isPortUsed(port)) {
      port = YHSocketUtil.getNotUsetPort(8088);
      updateTomcatPort(installPath + "\\tomcat\\conf\\server.xml", port);
    }
    logList.add("Web 服务端口 >> " + port);
    
    YHFileUtility.storeArray2Line(installPath + "\\install.log", logList);
  }
  
  /**
   * 在心的线程中执行命令
   * @param cmd
   */
  public void exCmdNewThread(String cmd) {
    YHInstallThread t = new YHInstallThread(this, "exCmd", new String[]{cmd});
    t.start();
  }
  
  /**
   * 执行命令
   * @param cmd
   */
  public static void exCmd(String cmd) {
    try {
      Runtime  r = Runtime.getRuntime();
      Process process = r.exec(cmd);      
      java.util.List msrgList = new ArrayList();
      YHStreamPumper sp = new YHStreamPumper(process.getInputStream(), msrgList);
      sp.start();
      process.waitFor();
      sp.join();
      process.destroy();
      for (int i = 0; i < msrgList.size(); i++) {
        System.out.println((String)msrgList.get(i));
      }
    }catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * 取得配置文件
   * @param installPath
   * @return
   * @throws Exception
   */
  public static void updateTomcatPort(String confFilePath, int port) throws Exception {
    List rtList = new ArrayList();
    YHFileUtility.loadLine2Array(confFilePath, rtList);
    boolean hasChanged = false;
    for (int i = 0; i < rtList.size(); i++) {
      String str = (String)rtList.get(i).toString();
      String strTrim = str.trim();
      if (strTrim.startsWith("<Connector")
          && strTrim.indexOf("protocol=\"HTTP/1.1\"") > 0
          && strTrim.indexOf("port=\"") > 0) {
        int tmpInt = str.indexOf("port=\"");
        int tmpInt2 = str.indexOf("\"", tmpInt + 6);
        
        String tmpStr = str.substring(0, tmpInt + 6) + port + str.substring(tmpInt2);
        rtList.set(i, tmpStr);
        hasChanged = true;
        break;
      }
    }
    if (hasChanged) {
      new File(confFilePath).setWritable(true);
      YHFileUtility.storeArray2Line(confFilePath, rtList);
    }
  }
  /**
   * 取得配置文件
   * @param installPath
   * @return
   * @throws Exception
   */
  public static int getTomcatPort(String confFilePath) throws Exception {
    List rtList = new ArrayList();
    YHFileUtility.loadLine2Array(confFilePath, rtList);
    int port = 0;
    for (int i = 0; i < rtList.size(); i++) {
      String str = (String)rtList.get(i).toString();
      String strTrim = str.trim();
      if (strTrim.startsWith("<Connector")
          && strTrim.indexOf("protocol=\"HTTP/1.1\"") > 0
          && strTrim.indexOf("port=\"") > 0) {
        int tmpInt = strTrim.indexOf("port=\"");
        int tmpInt2 = strTrim.indexOf("\"", tmpInt + 6);
        port = Integer.parseInt(strTrim.substring(tmpInt + 6, tmpInt2));
        break;
      }
    }
    return port;
  }
  
  /**
   * 打开IE 
   * @throws Exception
   */
  public void opeIE(String installPath, String contextPath) throws Exception {
    try {
      int port = getTomcatPort(installPath + "\\tomcat\\conf\\server.xml");
      if (port == 0) {
        log.debug("没有发现Tomcat的端口，请检查Tomcat的server.xml是否存在或者是否是"+StaticData.SOFTCOMPANY+"提供的配置文件！");
        return;
      }
      String portStr = "";
      if (port != 80) {
        portStr = ":" + port;
      }
      String netAddress = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE"
        + " http://localhost" + portStr + "/" + contextPath + "/index.jsp";
      exCmdNewThread(netAddress);
    }catch(Exception ex) {
    }
  }
  public void updateVersion(String userVersion , String version , int versionNum) throws Exception {
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String exSql = "UPDATE version set VER='" + version + "', VERSION_NUM=" + versionNum ;
    if (!YHUtility.isNullorEmpty(userVersion)) {
      exSql += " , USER_VERSION='" + userVersion + "'";
    }
    try{
      YHDBUtility dbUtil = new YHDBUtility();
      dbConn =  dbUtil.getConnection(true, YHSysProps.getSysDbName());
      stmt = dbConn.createStatement();
      stmt.executeUpdate(exSql);
    }catch(Exception ex) {
      ex.printStackTrace();
      dbConn.rollback();
      log.debug("exSql >> " + exSql);
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
  }
  
  /**
   * 执行批处理Sql语句
   * @param dbNameArray            数据库名称数组String[]{dbName, }
   * @param sqlList                批处理Sql语句列表
   * @param isUseTrnsact           是否使用事务 true=使用；false=不使用
   * @throws Exception
   */
  public static void exectBatchSql(
      String[] dbNameArray,
      List sqlList,
      boolean useTrns,
      int version) throws Exception {
    
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String exSql = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility();
      /** 对所指定的库执行Sql语句 **/
      for (int i = 0; i < dbNameArray.length; i++) {
        String dbName = dbNameArray[i].trim();
        dbConn = dbUtil.getConnection(!useTrns, dbName);
        stmt = dbConn.createStatement();
        if (version > 0) {
          try {
            String versionSql = "select VERSION_NUM from version";
            rs = stmt.executeQuery(versionSql);
            int currVersion = -1;
            if (rs.next()) {
              String versionStr = rs.getString(1);
              if (versionStr != null) {
                currVersion = Integer.parseInt(versionStr);
              }
            }
            if (currVersion != version) {
              continue;
            }
          }catch(Exception ex) {          
          }
        }
        StringBuffer sqlBuff = new StringBuffer();
        boolean hasBeenExecute = false;
        for (int j = 0; j < sqlList.size(); j++) {
          String sqlStr = ((String)sqlList.get(j)).trim();
          if (sqlStr.length() < 1 || sqlStr.startsWith("#") || sqlStr.startsWith("--")) {
            continue;
          }
          hasBeenExecute = false;
          //当是创建触发器的时候处理;符号
          if (sqlStr.endsWith(";TRIGGER")) {
            sqlBuff.append("\r\n");
            sqlBuff.append(sqlStr.replace(";TRIGGER", ";"));
          } else if (sqlStr.endsWith(";")) {
            sqlBuff.append(sqlStr.substring(0, sqlStr.length() - 1));
            exSql = sqlBuff.toString();
//            log.debug(exSql);
            stmt.execute(exSql);
            sqlBuff.delete(0, sqlBuff.length());
            hasBeenExecute = true;
            continue;
          }else if (sqlStr.toUpperCase().equals("GO")) {
            exSql = sqlBuff.toString();
            stmt.execute(exSql);
            sqlBuff.delete(0, sqlBuff.length());
            hasBeenExecute = true;
            continue;
          }else {
            sqlBuff.append("\r\n");
            sqlBuff.append(sqlStr);
          }          
        }
        if (!hasBeenExecute && sqlBuff.length() > 0) {
          stmt.execute(sqlBuff.toString());
          sqlBuff.delete(0, sqlBuff.length());
        }

        if (useTrns) {
          dbConn.commit();
        }
        YHDBUtility.closeDbConn(dbConn, log);
      }     
    }catch(Exception ex) {
      try {
        if (useTrns) {
          ex.printStackTrace();
          dbConn.rollback();
        }
      }catch(Exception ex2) {  
        Runtime r = Runtime.getRuntime();
        r.exec("");
      }
      log.debug("exSql >> " + exSql);
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
  }
  /**
   * 执行Sql语句
   * @param sqlPath
   */
  public void exeSql(String sqlPath) throws Exception {
    File file = new File(sqlPath);
    if (!file.exists() || !file.isFile()) {
      return;
    }
//    log.debug(sqlPath);
    List sqlList = new ArrayList();
    YHFileUtility.loadLine2Array(sqlPath, sqlList);
    if (sqlList.size() < 1) {
      return;
    }
    Map<String, String> optsMap = new HashMap<String, String>();
    
    String firstLine = sqlList.get(0).toString().trim();
    if (firstLine.startsWith("--")
        && firstLine.indexOf("useTrans") > 0 
        && firstLine.indexOf("versionNum") > 0) {
      firstLine = firstLine.substring(2).trim();
      optsMap = YHFOM.json2Map(firstLine);
    }
    String useTransStr = YHUtility.null2Empty(optsMap.get("useTrans"));
    String versionStr = YHUtility.null2Empty(optsMap.get("versionNum"));
    boolean useTrans = true;
    if (useTransStr.equalsIgnoreCase("true") || useTransStr.equalsIgnoreCase("false")) {
      useTrans = useTransStr.equalsIgnoreCase("true");
    }
    int updateVersion = 0;
    try {
      if (versionStr.length() > 0) {
        updateVersion = Integer.parseInt(versionStr);
      }
    }catch(Exception ex) {      
    }
    exectBatchSql(new String[]{YHSysProps.getSysDbName()}, sqlList, useTrans, updateVersion);
  }
  
  /**
   * 执行Sql语句，按文件名称字符串升序顺序执行
   * @param sqlPath
   */
  public void exeSqlInPath(String sqlPath) throws Exception {
    File pathFile = new File(sqlPath);
    if (!pathFile.isDirectory() || !pathFile.exists()) {
      return;
    }
    String[] fileList = YHFileUtility.sortFileList(pathFile);
    for (int i = 0; i < fileList.length; i++) {
      String filePath = sqlPath + "\\" + fileList[i];
      if (!filePath.endsWith(".sql")) {
        continue;
      }
      if (new File(filePath).isFile()) {
        exeSql(filePath);
      }
    }
  }
  
  /**
   * 更新系统配置
   * @param installPath
   * @param ctxPath
   * @param confPath
   * @throws Exception
   */
  public void updateSysConf(String installPath,
      String contextPath, String confPath) throws Exception {
    String toFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\selfconfig.properties";

    YHPropUtility.updatePropFile(toFile, confPath);
  }
  /**
   * 
   * 
   */
  
  public void updateSysConfByFileName(String installPath,
	      String contextPath, String confPath,String fileName) throws Exception {
	    String toFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\"+fileName ;

	    YHPropUtility.updatePropFile(toFile, confPath);
	  }
  /**
   * 更新系统配置
   * @param installPath
   * @param ctxPath
   * @param confPath
   * @throws Exception
   */
  public void deleteSysConf(String installPath,
      String contextPath, String confPath) throws Exception {
    String toFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\selfconfig.properties";

    YHPropUtility.deletePropFile(toFile, confPath);
  }
  /*
   * 根据文件名删除相应的属性
   */
  public void deleteSysConfByFileName(String installPath,
	      String contextPath, String confPath,String fileName) throws Exception {
	    String toFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\"+fileName;

	    YHPropUtility.deletePropFile(toFile, confPath);
	  }
  /**
   * 更新数据库配置信息
   * @param installPath
   * @param contextPath
   * @param dbConfMap
   * @throws Exception
   */
  public void updateDbConf(String installPath, String contextPath, Map<String, String> dbConfMap) throws Exception {
    String toFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\selfconfig.properties";

    String host = dbConfMap.get("host");
    String port = dbConfMap.get("port");
    String dbms = dbConfMap.get("dbms");
    String user = dbConfMap.get("user");
    String pass = dbConfMap.get("pass");
    String sysdb = dbConfMap.get("sysdb");
    String sysdsname = dbConfMap.get("sysdsname");
    
    Map newPropsMap = new LinkedHashMap();
    if (YHUtility.isNullorEmpty(dbms)) {
      dbms = YHSysProps.getString("db.jdbc.dbms");
    }else {
      newPropsMap.put("db.jdbc.dbms", dbms);
    }
    String dbConurl = null;
    if (!YHUtility.isNullorEmpty(host) || !YHUtility.isNullorEmpty(port)) {
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        String oldUrl = YHSysProps.getString(YHSysPropKeys.DBCONN_CONURL + "." + YHConst.DBMS_SQLSERVER);
        int tmpInt = oldUrl.indexOf("://") + 3;
        int tmpInt2 = oldUrl.indexOf(":", tmpInt);
        String oldHost = oldUrl.substring(tmpInt, tmpInt2).trim();
        String oldPort = oldUrl.substring(tmpInt2 + 1, oldUrl.length() - 1);
        if (YHUtility.isNullorEmpty(host)) {
          host = oldHost;
        }
        if (YHUtility.isNullorEmpty(port)) {
          port = oldPort;
        }
        dbConurl = "jdbc:microsoft:sqlserver://" + host + ":" + port + ";";
      }else if (dbms.equals(YHConst.DBMS_ORACLE)) {
        String oldUrl = YHSysProps.getString(YHSysPropKeys.DBCONN_CONURL + "." + YHConst.DBMS_ORACLE);
        int tmpInt = oldUrl.indexOf("@") + 1;
        int tmpInt2 = oldUrl.indexOf(":", tmpInt);
        int tmpInt3 = oldUrl.indexOf(":", tmpInt2 + 1);
        String oldHost = oldUrl.substring(tmpInt, tmpInt2).trim();
        String oldPort = oldUrl.substring(tmpInt2 + 1, tmpInt3);
        if (YHUtility.isNullorEmpty(host)) {
          host = oldHost;
        }
        if (YHUtility.isNullorEmpty(port)) {
          port = oldPort;
        }
        dbConurl = "jdbc:oracle:thin:@" + host + ":" + port + ":orcl";
        
        if (!YHUtility.isNullorEmpty(user)) {
          newPropsMap.put("sysDatabaseName", user);
        }
        if (!YHUtility.isNullorEmpty(sysdb)) {
          newPropsMap.put("db.jdbc.userName." + dbms, sysdb);
        }
      }else if (dbms.equals(YHConst.DBMS_MYSQL)) {
        String oldUrl = YHSysProps.getString(YHSysPropKeys.DBCONN_CONURL + "." + YHConst.DBMS_MYSQL);
        int tmpInt = oldUrl.indexOf("://") + 3;
        int tmpInt2 = oldUrl.indexOf(":", tmpInt);
        String oldHost = oldUrl.substring(tmpInt, tmpInt2).trim();
        String oldPort = oldUrl.substring(tmpInt2 + 1, oldUrl.length() - 1);
        if (YHUtility.isNullorEmpty(host)) {
          host = oldHost;
        }
        if (YHUtility.isNullorEmpty(port)) {
          port = oldPort;
        }
        dbConurl = "jdbc:mysql://" + host + ":" + port + "/";
      }
      newPropsMap.put(YHSysPropKeys.DBCONN_CONURL + "." + dbms, dbConurl);
    }
    if (!YHUtility.isNullorEmpty(sysdb)) {
      newPropsMap.put("sysDatabaseName", sysdb);
    }
    if (!YHUtility.isNullorEmpty(user)) {
      newPropsMap.put("db.jdbc.userName." + dbms, user);
    }
    if (!YHUtility.isNullorEmpty(pass)) {
      newPropsMap.put("db.jdbc.passward." + dbms, YHAuthenticator.ciphEncryptStr(pass));
    }
    
    if (newPropsMap.size() > 0) {
      YHPropUtility.updatePropFile(toFile, newPropsMap);
    }
  }
  /**
   * 更新Jsp相关类的时间
   * @param installPath
   * @param contextPath
   */
  public static void upddateJspCalss(String installPath, String contextPath) {
    try {
      YHFileUtility.setLastModified(installPath + "\\tomcat\\work\\Catalina\\localhost\\" + contextPath + "\\org", new Date().getTime() + 2 * YHConst.DT_MINIT);
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
  }
  /**
   * 打安装时间戳
   * @param installPath
   * @param contextPath
   */
  public static void timestamp(String installPath, String contextPath) {
    try {
      //系统参数配置文件
      String sysFonfig = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\sysconfig.properties";
      List rules = new ArrayList();
      //rootDir
      YHPropField field = new YHPropField();
      rules.add(field);
      field.setFieldName(YHSysPropKeys.INSTALL_TIME);
      String timeStr = String.valueOf(System.currentTimeMillis());
      field.setFieldValue(timeStr);
      field = new YHPropField();
      rules.add(field);
      field.setFieldName(YHSysPropKeys.INSTALL_TIME_PASS);
      field.setFieldValue(YHAuthenticator.encryptBase64(YHAuthKeys.getMD5SaltLength(null), timeStr));
      
      YHFileUtility.storeArray2Line(sysFonfig, rules);
    }catch(Exception ex) {
      return;
    }
  }
  public int getVersionNum() throws Exception {
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String exSql = "select VERSION_NUM from  version ";
    int num = 0 ;
    try{
      YHDBUtility dbUtil = new YHDBUtility();
      dbConn =  dbUtil.getConnection(false, YHSysProps.getSysDbName());
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(exSql);
      if (rs.next()) {
        num = rs.getInt("VERSION_NUM");
      }
    }catch(Exception ex) {
      ex.printStackTrace();
      log.debug("exSql >> " + exSql);
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
    return num;
  }
  public void updateVersionFile(String path) throws Exception {
    // TODO Auto-generated method stub
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String exSql = "select VERSION_NUM from  version ";
    int num = 0 ;
    try{
      YHDBUtility dbUtil = new YHDBUtility();
      dbConn =  dbUtil.getConnection(false, YHSysProps.getSysDbName());
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(exSql);
      if (rs.next()) {
        num = rs.getInt("VERSION_NUM");
      }
      YHFileUtility.storeString2File(path, "" + num);
    }catch(Exception ex) {
      ex.printStackTrace();
      log.debug("exSql >> " + exSql);
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
  }

  public void exeSqlInPathVersion(String sqlPath  ,Logger log2) throws Exception {
    // TODO Auto-generated method stub
    File pathFile = new File(sqlPath);
    if (!pathFile.isDirectory() || !pathFile.exists()) {
      return;
    }
    int num = this.getVersionNum();
    String[] fileList = YHFileUtility.sortFileList(pathFile);
    for (int i = 0; i < fileList.length; i++) {
      String fileName = fileList[i];
      int fName  = 0;
      if (YHUtility.isInteger(fileName)) {
        fName = Integer.parseInt(fileName);
      }
      if (fName > num) {
        String filePath = sqlPath + "\\" + fileName;
        File fi = new File(filePath);
        if (fi.exists() && fi.isDirectory()) {
          this.exeSqlInPath(filePath);
        }
      }
    }
  }
  
  public static void main(String args[]) {
    YHInstallConfig t = new YHInstallConfig();
    try {
      //t.configHttpdPath("D:\\YHProduct");
      t.bakSysFile("d:\\yh", "10");
     // t.exeSqlInPathVersion("D:\\project\\yh\\webroot\\yh\\update\\sqls\\mysql");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  private void zip(ZipOutputStream out, File inputFile, String base)
  {
   int buffer = 1024;
  try
  {
     if(inputFile.isDirectory())
     {
      File[] file = inputFile.listFiles();
      out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
      //System.out.println("压缩路径：" + base + "/");
      base = (base.length() == 0) ? "" : (base + "/");
   for(int i = 0; i < file.length; i++)
   {
       zip(out, file[i], base + file[i].getName());
   }
     }
     else
     {
      out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
      byte[] data = new byte[buffer];
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile), buffer);
      //System.out.println("正在压缩文件：" + base);
      int cnt;
      while((cnt = bis.read(data, 0 , buffer)) != -1)
      {
       out.write(data, 0, buffer);
      }
      out.flush();
      bis.close();
     }
  }
  catch(Exception e)
  {
     System.out.println(e);
  }
  }
  public void deleteSysConfVersion(String installPath, String contextPath,
      String string) throws Exception {
    // TODO Auto-generated method stub
    String toFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\selfconfig.properties";
    File pathFile = new File(string);
    if (!pathFile.isDirectory() || !pathFile.exists()) {
      return;
    }
    int num = this.getVersionNum();
    String[] fileList = YHFileUtility.sortFileList(pathFile);
    for (int i = 0; i < fileList.length; i++) {
      String fileName = fileList[i];
      int fName  = 0;
      if (YHUtility.isInteger(fileName)) {
        fName = Integer.parseInt(fileName);
      }
      if (fName > num) {
        String filePath = string + "\\" + fileName + "\\deleteSysConf.properties" ;
        File fi = new File(filePath);
        if (fi.exists() && fi.isFile()) {
          YHPropUtility.deletePropFile(toFile, filePath);
        }
      }
    }
    
  }

  public void updateSysConfVersion(String installPath, String contextPath,
      String string) throws Exception {
    // TODO Auto-generated method stub
    String toFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\selfconfig.properties";
    File pathFile = new File(string);
    if (!pathFile.isDirectory() || !pathFile.exists()) {
      return;
    }
    int num = this.getVersionNum();
    String[] fileList = YHFileUtility.sortFileList(pathFile);
    for (int i = 0; i < fileList.length; i++) {
      String fileName = fileList[i];
      int fName  = 0;
      if (YHUtility.isInteger(fileName)) {
        fName = Integer.parseInt(fileName);
      }
      if (fName > num) {
        String filePath = string + "\\" + fileName + "\\updateSysConf.properties" ;
        File fi = new File(filePath);
        if (fi.exists() && fi.isFile()) {
          YHPropUtility.updatePropFile(toFile, filePath);
        }
      }
    }
  }
  public void bakSysFile(String installPath, String versionNo) throws IOException {
    // TODO Auto-generated method stub
    String bakSysFile =  installPath + File.separator + "updateback";
    File file = new File(bakSysFile);
    if (!file.exists()) {
      file.mkdir();
    }
    OutputStream out = new FileOutputStream(new File(bakSysFile + File.separator + "V" + versionNo + ".zip"));
    org.apache.tools.zip.ZipOutputStream zipout = new org.apache.tools.zip.ZipOutputStream(new BufferedOutputStream(out,1024));
    zipout.setEncoding("GBK");
    Map<String , InputStream> map = new HashMap();
    String workFile = "tomcat" + File.separator + "work";
    String workFilePath = installPath + File.separator + workFile ;
    //FileInputStream workin = new  FileInputStream(new File(workFilePath)) ;
    String yhFile = "webroot" + File.separator + "yh";
    String yhFilePath = installPath + File.separator + yhFile;
    //FileInputStream yhFilein = new  FileInputStream(new File(yhFilePath)) ;
    zip(zipout, new File(workFilePath), workFile);
    zip(zipout, new File(yhFilePath), yhFile);
    zipout.close();
    //System.out.println("压缩完毕！");
    // map.put(yhFile, yhFilein);
   // map.put(workFile, workin);
   // Set<String> keys = map.keySet();
   // for (String tmp : keys) {
     // InputStream in = map.get(tmp);
     // YHMyWorkLogic.output(in, zipout, tmp);
   // }
   // zipout.flush();
    //zipout.close();
  }
  public static void output(InputStream in ,  org.apache.tools.zip.ZipOutputStream out  , String fileName) throws IOException {
    byte[] buf = new byte[1024];
    try {
      org.apache.tools.zip.ZipEntry ss =  new org.apache.tools.zip.ZipEntry(fileName);
      out.putNextEntry(ss);
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      out.closeEntry();
      out.flush();
      in.close();
    } catch (IOException e) {
      throw e;
    }
  }
}

package yh.core.install;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.YHUtility;
import yh.core.util.cmd.YHCmdUtility;
import yh.core.util.file.YHFileUtility;

/**
 * YH 外部命令执行主程序
 * @author jpt
 *
 */
public class YHCmdMain {
  private static Logger log = null;
  /**
   * 主程序入口
   * @param args         [0]=installPath; [1]=contextPath; [2]=cmd
   */
  public static void main(String[] args) {
    String cmdLine = "";
    for (int i = 0; i < args.length; i++) {
      cmdLine += "\"" + args[i] + "\"";
      if (i < args.length - 1) {
        cmdLine += " ";
      }
    }
    
    String cmd = null;
    try {
      String installPath = null;
      String contextPath = "yh";

      if (args.length > 0) {
        installPath = args[0];
      }else {
        return;
      }
      if (args.length > 1) {
        contextPath = args[1];
      }else {
        return;
      }
      //创建Log目录，配置Log
      String debugPath = installPath + "\\webroot\\" + contextPath + "\\debug\\yzq";
      File logFile = new File(debugPath);
      if (!logFile.exists()) {
        logFile.mkdirs();
      }
      debugPath = installPath + "\\webroot\\" + contextPath + "\\debug\\yh";
      logFile = new File(debugPath);
      if (!logFile.exists()) {
        logFile.mkdirs();
      }
      debugPath = installPath + "\\webroot\\" + contextPath + "\\debug\\yh\\debug.log";
      Properties log4jProps = new Properties();
      log4jProps.setProperty("log4j.logger.yh", "debug,yh");
      log4jProps.setProperty("log4j.appender.yh", "org.apache.log4j.RollingFileAppender");
      log4jProps.setProperty("log4j.appender.yh.File", debugPath);
      log4jProps.setProperty("log4j.appender.yh.MaxFileSize", "100KB");
      log4jProps.setProperty("log4j.appender.yh.MaxBackupIndex", "100");
      log4jProps.setProperty("log4j.appender.yh.layout", "org.apache.log4j.PatternLayout");
      log4jProps.setProperty("log4j.appender.yh.layout.ConversionPattern", "%d{yyyy-MM-dd : HH-mm-ss-SSS} %t %c %L - %m%n");
      PropertyConfigurator.configure(log4jProps);
      log = Logger.getLogger("yh.core.install.YHCmdMain");
      
      log.debug("Command Line >>" + cmdLine + " " + YHUtility.getCurDateTimeStr());
      
      if (args.length > 2) {
        cmd = args[2];
      }else {
        log.debug("没有传递命令。");
        return;
      }
      log.debug("cmd>>" + cmd + " " + YHUtility.getCurDateTimeStr());

      //加载数据库配置信息
      String sysConfFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\sysconfig.properties";
      YHSysProps.setProps(YHConfigLoader.loadSysProps(sysConfFile));
      String selfConfFile = installPath + "\\webroot\\" + contextPath + "\\WEB-INF\\config\\selfconfig.properties";
      YHSysProps.addProps(YHConfigLoader.loadSysProps(selfConfFile));
      
      YHInstallConfig config = new YHInstallConfig();
      //配置系统路径，sysconfig.properties.rootDir; log4j.properties.rootDir
      if (cmd.equals("update")) {
       // YHUpdateCmd c = new YHUpdateCmd();
        //c.stopYHAndUpdate(installPath, contextPath);
      } else if (cmd.equals("confPath")) {
        YHInstallConfig.configPath(installPath, contextPath);
      }else if (cmd.equals("confEsbPath")) {
        boolean  isServer = false;
        if (args.length >= 4) {
          isServer = true;
        }
        YHInstallConfig.configEsbPath(installPath, contextPath , isServer);
      //修改httpd.conf配置文件
      }else if (cmd.equals("configHttpdPath")) {
        config.configHttpdPath(installPath);
        //执行Sql语句  args[3]=sqlPath;
      }else if (cmd.equals("confRaPath")) {
        config.configRaPath(installPath);
        
      }else if (cmd.equals("exeSql")) {
        if (args.length < 3) {
          log.debug("没有传递Sql文件路径！");
          return;
        }
        String sqlPath = args[3];
        File sqlFile = new File(sqlPath); 
        if (!sqlFile.exists()) {
          log.debug("指定Sql文件" + sqlPath + "不存在");
          return;
        }
        if (!sqlFile.isFile()) {
          log.debug("指定Sql路径" + sqlPath + "不是文件");
          return;
        }
        config.exeSql(sqlPath);
      //执行某个目录下的所有Sql语句 args[3]=sqlPath; args[4]=是否使用是否true | false;
      }else if (cmd.equals("exeSqlInpath")) {
        if (args.length < 3) {
          log.debug("没有传递Sql目录路径！");
          return;
        }
        String sqlPath = args[3];
        File sqlDir = new File(sqlPath); 
        if (!sqlDir.exists()) {
          log.debug("指定Sql目录" + sqlPath + "不存在");
          return;
        }
        if (!sqlDir.isDirectory()) {
          log.debug("指定Sql路径" + sqlPath + "不是目录");
          return;
        }
        config.exeSqlInPath(sqlPath);
      //打开IE
      }else if (cmd.equals("openIE")) {
        config.opeIE(installPath, contextPath);
      //更新Tomcat的端口, args[3]=端口值
      }else if (cmd.equals("updateTomcatPort")) {
        if (args.length < 3) {
          log.debug("没有传递端口值！");
          return;
        }
        int port = 0;
        String portStr = args[3];
        if (!YHUtility.isNullorEmpty(portStr)) {
          try {
            port = Integer.parseInt(portStr);
          }catch(Exception ex) {            
          }
        }
        if (port < 1 || port > 60000) {
          log.debug("请传递有效的端口值[1, 60000]");
          return;
        }
        YHInstallConfig.updateTomcatPort(installPath + "\\tomcat\\conf\\server.xml", port);
      //更新数据库配置，切换数据库
      //-host主机(可选); -port端口(可选); -dbms数据库管理系统(可选); -user用户名(可选); -pass密码(可选); -sysdb系统库名（可选）; -sysdsname系统缺省数据库数据源名称，其中至少一项有效
      }else if (cmd.equals("upddateDbConf")) {
        Map<String, String> optionsMap = YHCmdUtility.parseOptions(args, new String[]{"host", "port", "dbms", "user", "pass", "sysdb", "sysdsname"});
        if (optionsMap.size() < 1) {
          log.debug("没有传递有效的选项参数");
          return;
        }
        config.updateDbConf(installPath, contextPath, optionsMap);
      //更新系统配置，args[3]=更新系统配置源文件
      }else if (cmd.equals("updateSysConf")) {
        if (args.length < 3) {
          log.debug("没有传递参数配置文件路径！");
          return;
        }
        File confFile = new File(args[3]); 
        if (!confFile.exists()) {
          log.debug("指定Sql文件" + confFile + "不存在");
          return;
        }
        if (!confFile.isFile()) {
          log.debug("指定Sql路径" + confFile + "不是文件");
          return;
        }
        config.updateSysConf(installPath, contextPath, args[3]);
      }else if (cmd.equals("bakSysFile")) {
        if (args.length < 3) {
          log.debug("没有传递需要备份的版本号！");
          return;
        }
        String versionNo = args[3];
        try {
          config.bakSysFile(installPath , versionNo);
        } catch (FileNotFoundException fileNotFound) {
          log.debug("文件没有找到！");
          return;
        }
      } else if (cmd.equals("deleteSysConf")) {
        if (args.length < 3) {
          log.debug("没有传递参数配置文件路径！");
          return;
        }
        File confFile = new File(args[3]); 
        if (!confFile.exists()) {
          log.debug("指定Sql文件 " + confFile + " 不存在");
          return;
        }
        if (!confFile.isFile()) {
          log.debug("指定Sql路径 " + confFile + " 不是文件");
          return;
        }
        config.deleteSysConf(installPath, contextPath, args[3]);
      //更新JspClass的时间戳
      }else if (cmd.equals("upddateJspCalss")) {
        YHInstallConfig.upddateJspCalss(installPath, contextPath);
      //设置文件或者路径为可写args[3]=文件路径，必须是相对于installPath的相对路径，安全起见，不处理绝对路径
      }else if (cmd.equals("setWritable")) {
        if (args.length < 3) {
          log.debug("没有传递文件路径！");
          return;
        }
        if (args[3].charAt(1) == ':') {
          log.debug("仅支持相对路径！");
          return;
        }
        String filePath = installPath + "\\" + args[3];
        File setFile = new File(filePath); 
        if (!setFile.exists()) {
          log.debug("指定路径" + filePath + "不存在");
          return;
        }
        YHFileUtility.setWritable(filePath);
      //删除文件args[3]=文件路径，必须是相对于installPath的相对路径，安全起见，不处理绝对路径
      }else if (cmd.equals("removeFile")) {
        if (args.length < 3) {
          log.debug("没有传递文件路径！");
          return;
        }
        if (args[3].charAt(1) == ':') {
          log.debug("仅支持相对路径！");
          return;
        }
        String filePath = installPath + "\\" + args[3];
        File deleteFile = new File(filePath); 
        if (!deleteFile.exists()) {
          log.debug("指定文件 " + filePath + " 不存在");
          return;
        }
        if (!deleteFile.isFile()) {
          log.debug("指定路径 " + filePath + " 不是文件");
          return;
        }
        YHFileUtility.deleteAll(deleteFile);
      //删除路径args[3]=路径，必须是相对于installPath的相对路径，安全起见，不处理绝对路径
      }else if (cmd.equals("removePath")) {
        if (args.length < 3) {
          log.debug("没有传递文件路径！");
          return;
        }
        if (args[3].charAt(1) == ':') {
          log.debug("仅支持相对路径！");
          return;
        }
        String filePath = installPath + "\\" + args[3];
        File deleteFile = new File(filePath); 
        if (!deleteFile.exists()) {
          log.debug("指定目录 " + filePath + " 不存在");
          return;
        }
        YHFileUtility.deleteAll(deleteFile);
        //-versionNo版本号; -version版本; -userVersion用户版本号(可选);
      } else if (cmd.equals("updateVersion")) {
        Map<String, String> optionsMap = YHCmdUtility.parseOptions(args, new String[]{"versionNo", "version", "userVersion"});
        if (optionsMap.size() < 2) {
          log.debug("没有传递有效的选项参数");
          return;
        }
        String userVersion = optionsMap.get("userVersion");
        String version = optionsMap.get("version");
        String versionNumStr = optionsMap.get("versionNo");
        int versionNum = 0;
        if (YHUtility.isInteger(versionNumStr)) {
          versionNum = Integer.parseInt(versionNumStr);
        } else {
          log.debug("版本号必需是数字！");
          return;
        }
        config.updateVersion(userVersion, version, versionNum);
      }else if (cmd.equals("updateVersionFile")) {
        if (args.length < 3) {
          log.debug("没有传递文件路径！");
          return;
        }
        config.updateVersionFile(args[3]);
      } else if (cmd.equals("exeSqlInpathVersion")) {
        if (args.length < 3) {
          log.debug("没有传递Sql目录路径！");
          return;
        }
        String sqlPath = args[3] ;
        String dbType = YHSysProps.getProp("db.jdbc.dbms");
        if (!sqlPath.endsWith("\\")) {
          sqlPath += "\\";
        }
        sqlPath += dbType.trim();
        log.debug(sqlPath);
        File sqlDir = new File(sqlPath); 
        if (!sqlDir.exists()) {
          log.debug("指定Sql目录" + sqlPath + "不存在");
          return;
        }
        if (!sqlDir.isDirectory()) {
          log.debug("指定Sql路径" + sqlPath + "不是目录");
          return;
        }
        config.exeSqlInPathVersion(sqlPath ,  log);
      } else if (cmd.equals("updateSysConfVersion")) {
        if (args.length < 3) {
          log.debug("没有传递参数配置文件路径！");
          return;
        }
        File confFile = new File(args[3]); 
        if (!confFile.exists()) {
          log.debug("指定配置文件夹" + confFile + "不存在");
          return;
        }
        if (!confFile.isDirectory()) {
          log.debug("指定路径" + confFile + "不是文件夹");
          return;
        }
        config.updateSysConfVersion(installPath, contextPath, args[3]);
      } else if (cmd.equals("deleteSysConfVersion")) {
        if (args.length < 3) {
          log.debug("没有传递参数配置文件路径！");
          return;
        }
        File confFile = new File(args[3]); 
        if (!confFile.exists()) {
          log.debug("指定配置文件夹" + confFile + "不存在");
          return;
        }
        if (!confFile.isDirectory()) {
          log.debug("指定路径" + confFile + "不是文件夹");
          return;
        }
        config.deleteSysConfVersion(installPath, contextPath, args[3]);
      } else {
        log.debug("YH 不支持该命令 " + cmd + "。");
        return;
      }  
      log.debug("YH 命令 " + cmd + " 执行成功");
    }catch(Exception ex) {
      ex.printStackTrace();
      log.debug("YH 命令 " + cmd + " 执行失败，" + YHUtility.getCurDateTimeStr());
      log.debug(ex);
    }
  }
}

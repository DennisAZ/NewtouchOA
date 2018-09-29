package yh.core.load;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRunThread;
import yh.core.data.YHDataSources;
import yh.core.data.YHSessionPool;
import yh.core.funcs.system.ispirit.n12.org.act.YHIsPiritOrgAct;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;

public class YHConfigLoader {
  private static Logger log = Logger.getLogger(YHConfigLoader.class);
  /**
   * 从系统配置文件中加载系统配置
   * @param sysPropsFile      系统配置文件
   * @return
   */
  public static Properties loadSysProps(String sysPropsFile) {
    return loadSysProps(new File(sysPropsFile));
  }
  /**
   * 从系统配置文件中加载系统配置
   * @param sysPropsFile      系统配置文件
   * @return
   */
  public static Properties loadSysProps(File sysPropsFile) {
    Properties props = new Properties();
    
    if (!sysPropsFile.exists()) {
      return props;
    }
    InputStream inProps = null;
    try {
      inProps = new BufferedInputStream(
          new FileInputStream(sysPropsFile));
      props.load(inProps);
    } catch (IOException ex) {
    } finally {
      try {
        if (inProps != null) {
          inProps.close();
        }
      } catch (IOException ex) {
      }
    }
    
    return props;
  }
  
  /**
   * 系统初始化
   * @param installPath      系统的安装路径
   * @return
   */
  public static void loadInit(String rootPath) throws Exception {
    YHSessionPool.stopReleaseThread();
    YHDataSources.closeConnPool();
    YHAutoRunThread.stopRun();
    log.info("System init start...");
    File rootPathFile = new File(rootPath);
    String installPath = null;
    String webRoot = null;
    String ctx = null;
    try {
      String realRootPath = rootPathFile.getCanonicalPath();
      int p1 = realRootPath.lastIndexOf(File.separator);
      int p2 = realRootPath.substring(0, p1).lastIndexOf(File.separator);
      installPath = realRootPath.substring(0, p2);
      webRoot = realRootPath.substring(p2 + 1, p1);
      ctx = realRootPath.substring(p1 + 1);
      log.info("安装路径：" + installPath);
    }catch(Exception ex) {
    }

    //从配置文件中加载系统配置
    String sysConfFile = rootPath + "WEB-INF" + File.separator + "config" + File.separator + "sysconfig.properties";
    YHSysProps.setProps(YHConfigLoader.loadSysProps(sysConfFile));
    String selfConfFile = rootPath + "WEB-INF" + File.separator + "config" + File.separator + "selfconfig.properties";
    YHSysProps.addProps(YHConfigLoader.loadSysProps(selfConfFile));
    String patchadeval = rootPath + "WEB-INF" + File.separator + "config" + File.separator + "patchadeval.properties";
    YHSysProps.addProps(YHConfigLoader.loadSysProps(patchadeval));
    String patchDays = rootPath + "WEB-INF" + File.separator + "config" + File.separator + "patchdays.properties";
    YHSysProps.addProps(YHConfigLoader.loadSysProps(patchDays));
    String patchUserCnt = rootPath + "WEB-INF" + File.separator + "config" + File.separator + "patchuser.properties";
    YHSysProps.addProps(YHConfigLoader.loadSysProps(patchUserCnt));
    //设置安装路径、Webroot、contextPath
    Map pathMap = new HashMap<String, String>();
    pathMap.put(YHSysPropKeys.ROOT_DIR, installPath);
    pathMap.put(YHSysPropKeys.WEB_ROOT_DIR, webRoot);
    pathMap.put(YHSysPropKeys.JSP_ROOT_DIR, ctx);
    YHSysProps.addProps(pathMap);
    //构建数据源
    String dbConfPath = rootPath + "WEB-INF" + File.separator + "config" + File.separator + "dbconfig.properties";
    YHDataSources.buildDataSourceMap(dbConfPath);
    
    log.info("System init done.");
    
    //清空在线人员表
    Connection dbConn = null;
    Statement stmt = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility();
      dbConn = dbUtil.getConnection(false, YHSysProps.getSysDbName());
      String sql = "delete from oa_online";
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
      dbConn.commit();
      
    //精灵生成org.xml
      try {
         YHIsPiritOrgAct.getOrgDataStream(dbConn);
      } catch (Exception ex) {
        log.debug(ex.getMessage(), ex);
      }
      
      
    }catch (Exception ex) {
      log.debug(ex.getMessage(), ex);
      try {
        if (dbConn != null) {
          dbConn.rollback();
        }
      }catch(Exception ex2) {        
      }
    }finally {
      YHDBUtility.close(stmt, null, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
    
    
  
    
    
    //启动后台线程
    try {
      int sleepTime = YHSysProps.getInt(YHSysPropKeys.BACK_THREAD_SLEEP_TIME);
      if (sleepTime < 1) {
        sleepTime = 100;
      }
      YHAutoRunThread.startAutoRun(sleepTime);
    } catch (Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
  }
}

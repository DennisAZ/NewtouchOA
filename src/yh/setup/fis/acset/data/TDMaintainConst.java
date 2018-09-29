package yh.setup.fis.acset.data;


/**
 * 系统维护用户常量类
 * @author jpt
 * @version 1.0
 * @date 2007-1-30
 */
public class TDMaintainConst {
  /**
   * 输出文件
   */
  public static final String OUT_PUT_FILE = "outPutFile";
  /**
   * 数据文件中的数据列表
   */
  public static final String OUT_PUT_FILE_DATA_LIST = "outPutFileDataList";
  /**
   * 数据文件编码
   */
  public static final String OUT_PUT_CHAR_SET = "outPutFileCharSet";
  /**
   * 数据文件编码
   */
  public static final String DELETE_AFTER_OUT_PUT = "deleteAfterOutput";
  /**
   * 更新系统时新文件所在路径
   */
  public static final String UPDATE_NEW_DIR = "new";
  /**
   * 更新系统时备份文件所在路径
   */
  public static final String UPDATE_OLD_DIR = "old";
  
  /**
   * 数据库更新对象-系统库
   */
  public static final String UPDATE_DB_SYS = "TDSYS";
  /**
   * 数据库更新对象-帐套库
   */
  public static final String UPDATE_DB_ACSET = "TDACSET";
  //参数文件前缀
  public static final String PARAM_FILE_PREFIX = "param";
  //安装路径
  public static final String INSTALL_PATH = "installPath";
  //上下文路径
  public static final String CONTEXT_PATH = "contextPath";
  //更新文件路径
  public static final String SYS_UPDATE_PATH = "sysUpdatePath";
  //更新维护Sql路径
  public static final String MAINTAIN_SQL_PATH = "maintainSqlPath";
  //更新属性文件路径
  public static final String UPDATE_PROPFILE_PATH = "updatePropFilePath";
  //参数文件所在目录
  public static final String SYS_UPDATE_PARAMFILES_PATH = "sysUpdateParamFilesPath";
  //参数文件路径
  public static final String SYS_UPDATE_PARAMFILE = "sysUpdateParamFile";
  //批处理命令文件路径
  public static final String BATCH_COMMANDS_FILE = "batchCommandFile";
  //beforeStartTomcat类键名
  public static final String BEFORE_START_TOMCAT_CLASS = "beforeStartTomcatClass";
  //默认beforeStartTomcat类
  public static final String DEFAULT_BEFORE_START_TOMCAT_CLASS = "com.td.maintain.util.TDDefaultBeforeStartTomcat";
}

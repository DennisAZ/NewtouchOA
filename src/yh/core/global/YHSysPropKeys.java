package yh.core.global;

public class YHSysPropKeys {
  /**
   * 系统管理员名称
   */
  public static final String SYS_ADMIN_NAME = "sysAdminName";
  /**
   * 超级用户名称
   */
  public static final String SUPER_USER_NAME = "superUserName";
  /**
   * 系统管理员密码
   */
  public static final String SYS_ADMIN_PASS = "sysAdminPass";
  /**
   * 超级用户密码
   */
  public static final String SUPER_USER_PASS = "superUserPass";

  /**
   * 用户密码最小长度
   */
  public static final String MIN_LENGTH_PASS = "minLengthPass";

  /**
   * 系统数据库名称
   */
  public static final String SYS_DB_NAME = "sysDatabaseName";
  /**
   * cookie的生命周期
   */
  public static final String COOKIE_LIVE_SECONDS = "cookieLiveSeconds";
  /**
   * session的生命周期
   */
  public static final String SESSION_VALID_MINUTS = "sessionValidMinuts";
  /**
   * ID字段名称
   */
  public static final String ID_FIELD_NAME = "db.idfield.name"; 
  /**
   * 缺省编码
   */
  public static final String DEFAULT_CODE = "defaultCode";
  /**
   * 加密SALT长度
   */
  public static final String SALT_LENGTH = "saltLength";
  /**
   * 最大允许登录探测次数
   */
  public static final String MAX_LOGIN_TEST_TIMES = "maxLoginTestTimes";
  /**
   * 最大允许登录探测次数校验
   */
  public static final String MAX_LOGIN_TEST_TIMES_DIDGIST = "maxLoginTestTimesDidgist";
  
  /**
   * rootDir路径
   */
  public static final String ROOT_DIR = "rootDir";
  /**
   * webroot路径
   */
  public static final String WEB_ROOT_DIR = "webRootDir";
  /**
   * JSP路径
   */
  public static final String JSP_ROOT_DIR = "jspRootDir";
  /**
   * 数据库备份路径
   */
  public static final String DATABASE_BACKUP_DIR = "dataBaseBackupDir";
  /**
   * 最大的上传文件字节数
   */
  public static final String MAX_UPLOAD_FILE_SIZE = "maxUploadFileSize";
  /**
   * 文件上传临时目录
   */
  public static final String FILE_UPLOAD_TEMP_DIR = "fileUploadTempDir";
  /**
   * log目录
   */
  public static final String DEBUG_DIR = "debugDir";
  /**
   * 系统更新文件上传目录
   */
  public static final String UPDATE_SYS_DIR = "updateSysDir";
  /**
   * 数据库更新目录
   */
  public static final String UPDATE_SQL_DIR = "updateSqlDir";
  /**
   * 系统安装时间
   */
  public static final String INSTALL_TIME = "installTime";
  /**
   * 系统安装时间摘要
   */
  public static final String INSTALL_TIME_PASS = "installTimePass";
  /**
   * 系统安装信息
   */
  public static final String INSTALL_REGIST_INFO = "install.registinfo";
  /**
   * 产品序列号
   */
  public static final String SYS_SERIAL_NO = "sysSerialNo";
  /**
   * 系统注册码
   */
  public static final String REGIST_CODE = "registCode";
  /**
   * 系统版本名称
   */
  public static final String SYS_VERSION_NAME = "sysVersionName";
  /**
   * 系统程序版本
   */
  public static final String SYS_FILES_VERSION = "sysFilesVersion";
  /**
   * 系统数据库版本
   */
  public static final String SYS_DATASTRUCT_VERSION = "sysDataStructVersion";
  /**
   * 清理线程间隔时间
   */
  public static final String BACK_THREAD_SLEEP_TIME = "backThreadSleepTime";
  /**
   * 临时表数据保持时间，单位是分钟
   */
  public static final String TEMP_DATA_RESERVED_TIME = "tempDataReservedTime";
  /**
   * 最大试用时间，单位是天
   */
  public static final String MAX_EVALUE_DAYS = "maxEvalueDays";  
  /**
   * 最大试用时间摘要校验
   */
  public static final String EVALUE_DAYS_DIDGIST = "evalueDaysDidgist";
  /**
   * 最大试用支持期间数
   */
  public static final String MAX_EVALUE_PERIODCNT = "maxEvaluePeriodCnt";
  /**
   * 最大试用支持期间数
   */
  public static final String EVALUE_PERIODCNT_DIDGIST = "evaluePeriodCntDidgist";
  /**
   * 最大用户数
   */
  public static final String MAX_USER_ACCOUNT = "maxUserAccount";
  /**
   * 最大试用时间摘要校验
   */
  public static final String USER_ACCOUNT_DIDGIST = "userAccountDidgist";
  /**
   * 函数名称键前缀
   */
  public static final String USER_FUNC_NAME_PREFIX = "userfunc.class";
  /**
   * 自动备份路径
   */
  public static final String AUTO_DBBACKUP_DIR = "autoDbBackupDir";
  /**
   * 自动备份频率
   */
  public static final String AUTO_DBBACKUP_HOURSLENGTH = "autoDbBackupHourLength";
  /**
   * 客户名称
   */
  public static final String CUSTOMER_NAME = "customerName";
  /**
   * 上传附件的路径
   */
  public static final String ATTACH_FILES_PATH = "attachFilePath";
  /**
   * 是否允许上传附件
   */
  public static final String LIMIT_UPLOAD_FILES = "limitUploadFiles";
  
  /**
   * 数据库连接参数
   */
  public static final String DBMS_MSSQLSERVER = "MSSqlserver";
  public static final String DBMS_MSDE = "MSDE";
  public static final String DBCONN_DATASOURCE_TYPE = "db.jdbc.datasource.type";
  public static final String DBCONN_DATASOURCE_SYS_DS_NAME = "db.jdbc.datasource.sysDsName";
  public static final String DBCONN_DATASOURCE_OA_DS_NAME = "db.jdbc.datasource.oaDsName";
  public static final String DBCONN_DATASOURCE_ERP_DS_NAME = "db.jdbc.datasource.erpDsName";
  public static final String DBCONN_DBMS = "db.jdbc.dbms";
  public static final String DBCONN_DRIVER = "db.jdbc.driver";
  public static final String DBCONN_CONURL = "db.jdbc.conurl";
  public static final String DBCONN_USER_NAME = "db.jdbc.userName";
  public static final String DBCONN_PASSWARD = "db.jdbc.passward";
  public static final String DBCONN_MAX_ACTIVE = "db.jdbc.maxActive";
  public static final String DBCONN_MAX_IDLE = "db.jdbc.maxIdle";
  public static final String DBCONN_MAX_WAIT = "db.jdbc.maxWait";
  public static final String DBCONN_DEFAULT_AUTO_COMMIT = "db.jdbc.defaultAutoCommit";
  public static final String DBCONN_DEFAULT_READONLY = "db.jdbc.defaultReadOnly";  
  
}

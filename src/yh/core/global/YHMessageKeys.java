package yh.core.global;

/**
 * 消息键名
 * @author jpt
 * @version 1.0
 * @date 2006-9-20
 */
public class YHMessageKeys {
  
  /**
   * 插入成功
   */
  public static final String COMMON_INSERT_OK = "common.insert.ok";
  
  /**
   * 插入失败
   */
  public static final String COMMON_INSERT_FAILED = "common.insert.failed";
  
  /**
   * 更新成功
   */
  public static final String COMMON_UPDATE_OK = "common.update.ok";
  
  /**
   * 更新失败
   */
  public static final String COMMON_UPDATE_FAILED = "common.update.failed";
  
  /**
   * 删除成功
   */
  public static final String COMMON_DELETE_OK = "common.delete.ok";
  
  /**
   * 不能删除
   */
  public static final String COMMON_CANT_DELETE = "common.cant.delete";
  
  /**
   * 删除失败
   */
  public static final String COMMON_DELETE_FAILED = "common.delete.failed";
  
  
  
  /**
   * 操作成功
   */
  public static final String COMMON_MSRG_OK = "common.msrg.ok";  
  /**
   * 操作失败
   */
  public static final String COMMON_ERROR_FAILED = "common.error.failed";
  
  /**
   * 数据库更新操作成功
   */
  public static final String COMMON_MSRG_UPDATEOK = "common.msrg.updateok";
  /**
   * 数据库更新操作失败
   */
  public static final String COMMON_ERROR_UPDATEFAILED = "common.error.updatefailed";
  /**
   * 用户名称是空值
   */
  public static final String COMMON_ERROR_USER_NAME_NOTNULL = "common.errors.username.notnull";  
  
  /**
   * 用户不存在
   */
  public static final String COMMON_ERROR_USER_NO_EXIST = "common.errors.username.noexist";
  
  /**
   * 没有传递用户密码
   */
  public static final String COMMON_ERROR_NULL_PASSWORD = "common.error.nullpassword";
  /**
   * 密码过长
   */
  public static final String COMMON_ERROR_TOO_LONG_PASSWORD = "common.errors.tooLongPassword";
  /**
   * 密码长度小于长度下限
   */
  public static final String COMMON_ERROR_TOO_SHORT_PASSWORD = "common.errors.tooShortPassword";
  /**
   * 无效的密码
   */
  
  // by cly 登录者只有财务权限，但是对应的帐套还没有分配。
  public static final String COMMON_ERROR_NO_ACSET_FOR_USER = "common.errors.noAcsetForUser";
  /**
   * 无效的密码
   */
 
  // by cly  登录者的没有财务权限和管理权限。
  public static final String COMMON_ERROR_NO_FINANCE_AND_MANAGE_PRIVE = "common.errors.noFinanceAndManagePrive";
  /**
   * 无效的密码
   */
  public static final String COMMON_ERROR_NAME_PASS_NOT_MATCH = "common.errors.namepassnotmatch";
  /**
   * 登录超过此数限制
   */
  public static final String COMMON_ERROR_OUTOF_MAX_LOGIN_TIMES = "common.errors.outof.max.login.times";
  /**
   * session中没有用户信息Bean
   */
  public static final String COMMON_ERROR_NO_USERIFO = "common.errors.noUserInfo";  
  /**
   * session中没有系统参数Bean
   */
  public static final String COMMON_ERROR_NO_SYS_PARAM = "common.errors.noSysParam";
  /**
   * 数据库中没有设置系统参数
   */
  public static final String COMMON_ERROR_NOTSET_SYS_PARAM = "common.errors.notSetSysParam";
  /**
   * 数据库中的密码不是系统生成或者已经被手工改动
   */
  public static final String COMMON_ERROR_SAVED_PASS_INVALID = "common.errors.savedPsssInvalid";
  /**
   * 数据库配置错误
   */
  public static final String COMMON_ERROR_DBCONN_CONF_ERROR = "common.errors.dbConnConfEorror";
  /**
   * 数据库联接关闭错误
   */
  public static final String COMMON_ERROR_DB_CONN_CLOSE_EXPS = "common.errors.dbConnCloseExps";
  /**
   * 数据库语句关闭错误
   */
  public static final String COMMON_ERROR_DB_STMT_CLOSE_EXPS = "common.errors.dbStmtCloseExps";
  /**
   * 数据库结果集关闭错误
   */
  public static final String COMMON_ERROR_DB_RS_CLOSE_EXPS = "common.errors.dbRsCloseExps";
  /**
   * 登录成功
   */
  public static final String COMMON_MSRG_LOGIN_SUCCESS = "common.msrg.loginsuccess";
  /**
   * 传递的actionId是null值
   */
  public static final String COMMON_ERROR_ACTION_ID_IS_NULL = "common.errors.actionIdIsNull";
  /**
   * 传递的actionId没有对应的Action
   */
  public static final String COMMON_ERROR_ACTION_ID_IS_INVALID = "common.errors.actionIdIsInvalid";
  /**
   * actionId和actionPath都没有传递
   */
  public static final String COMMON_ERROR_ACTION_NO_FORWARD = "common.errors.actionNoForward";
  /**
   * 指定数据库表不存在
   */
  public static final String COMMON_ERROR_TABLE_NOT_EXISTS = "common.errors.tableNotExists";
  /**
   * 无效的数据库连接
   */
  public static final String COMMON_ERROR_INVALID_DB_CONNECTION = "common.errors.invalidDbConnection";
  /**
   * 数据库字段在数据字典中还没有配置
   */
  public static final String COMMON_ERROR_FIELD_NOT_CONFIGED = "common.errors.fieldNotConfiged";
  /**
   * 需要传递表编码
   */
  public static final String COMMON_ERROR_TABLE_NO_IS_REQUIED = "common.errors.tableNoIsRequired";
  /**
   * 指定字段不存在
   */
  public static final String COMMON_ERROR_DB_FIELD_CONF_NOT_EXISTS = "common.errors.dbFieldConfNotExists";
  
  /**
   * 帐套建库失败
   */
  public static final String COMMON_ERROR_DATABASE_CREATE_FAILED = "common.errors.dbCreateFailed";
  /**
   * 帐套初始化失败
   */
  public static final String COMMON_ERROR_DATABASE_INITIAL_FAILED = "common.errors.dbInitialFailed"; 
  
  /**
   * 窗体没有配置
   */
  public static final String COMMON_ERROR_FORM_NOT_CONFIGED = "common.errors.formNotConfiged";
  /**
   * 没有传递窗体编码
   */
  public static final String COMMON_ERROR_FORM_NO_REQUIED = "common.errors.formNoRequied";
  /**
   * 数据源不存在
   */
  public static final String COMMON_ERROR_NON_EXIST_DATASOURCE = "common.errors.nonExistDatasource";
  /**
   * 无效的数据库编码
   */
  public static final String COMMON_ERROR_INVALID_DB_NO = "common.errors.invalidDbNo";
  /**
   * 重复的名称或者编码
   */
  public static final String COMMON_ERROR_NO_NAME_EXISTS = "common.errors.noOrNameExists";
  /**
   * 记录已经被使用
   */
  public static final String COMMON_ERROR_RECORD_HAS_BEEN_USED = "common.errors.recordHasBeenUsed";
  /**
   * 记录编码定义错误
   */
  public static final String COMMON_ERROR_DATA_CODE_RELA_FIELD_NO_ERROR = "common.errors.datacoderela.fieldNoError";
  /**
   * 帐套ok
   */
  public static final String ACSET_MSRG_BUILD_DATABASE_OK = "acset.msrg.build.database.ok";
  
  /**
   * 帐套删除成功
   */
  public static final String ACSET_MSRG_DELETE_DATABASE_OK = "acset.msrg.delete.database.ok";
  
  /**
   * 帐套删除失败
   */
  public static final String ACSET_MSRG_DELETE_DATABASE_FAILED = "acset.msrg.delete.database.failed";
  
  /**
   * 创建角色ok
   */
  public static final String ROLE_MSRG_BUILD_OK = "role.msrg.build.ok";
  
  /**
   * 创建角色failed
   */
  public static final String ROLE_ERROR_BUILD_FAILED = "role.error.build.failed";
  
  /**
   * 该角色名称已经存在
   */
  public static final String ROLE_ERROR_NAME_EXISTED = "role.error.name.existed.failed";
  
  
  /**
   * 删除角色ok
   */
  public static final String ROLE_MSRG_DELETE_OK = "role.msrg.delete.ok";
  
  /**
   * 删除角色failed
   */
  public static final String ROLE_ERROR_DELETE_FAILED = "role.error.delete.failed";
  
  /**
   * 角色树 打开ok
   */
  public static final String ROLE_MSRG_PRIV_OPEN_OK = "role.msrg.priv.open.ok";
  
  /**
   * 角色树 打开failed
   */
  public static final String ROLE_ERROR_PRIV_OPEN_FAILED = "role.error.priv.open.failed";
  
  /**
   * 角色树 保存ok
   */
  public static final String ROLE_MSRG_PRIV_SAVE_OK = "role.msrg.priv.save.ok";
  
  /**
   * 角色树 保存failed
   */
  public static final String ROLE_ERROR_PRIV_SAVE_FAILED = "role.error.priv.save.failed";
  
  /**
   * 桌面菜单树 打开ok
   */
  public static final String DESKTOP_MENU_OPEN_OK = "desktop.menu.open.ok";
  
  /**
   * 桌面菜单树 打开failed
   */
  public static final String DESKTOP_MENU_OPEN_FAILED = "desktop.menu.open.failed";
  
  /**
   * 桌面菜单树 保存ok
   */
  public static final String DESKTOP_MENU_SAVE_OK = "desktop.menu.save.ok";
  
  /**
   * 桌面菜单树 保存failed
   */
  public static final String DESKTOP_MENU_SAVE_FAILED = "desktop.menu.save.failed";
  
  
  /**
   * 注册成功
   */
  public static final String REGISTRY_SAVE_OK = "registry.save.ok";
  
  /**
   * 注册失败
   */
  public static final String REGISTRY_SAVE_FAILED = "registry.save.failed";
  
  /**
   * 注册失败
   */
  public static final String REGISTRY_CODE_NOTMATCH = "registry.not.match";
  /**
   * 非法的序列号
   */
  public static final String REGISTRY_INVALID_SERIALNO = "registry.invalid.serialno";
  
  /**
   * 用户密码 保存ok
   */
  public static final String PASSWORD_SAVE_OK = "password.save.ok";
  
  /**
   * 用户密码 保存failed
   */
  public static final String PASSWORD_SAVE_FAILED = "password.save.failed";
  
  /**
   * 用户旧密码校验有错
   */
  public static final String PASSWORD_INVALID = "password.invalid";
  
  /**
   * 用户添加成功
   */
  public static final String USER_INSERT_OK = "user.insert.ok";
  
  /**
   * 用户添加失败
   */
  public static final String USER_INSERT_FAILED = "user.insert.failed"; 

  /**
   * 该角色名称已经存在
   */
  public static final String USER_ERROR_NAME_EXISTED = "user.error.name.existed.failed";
  

  /**
   * 用户名称重名
   */
  public static final String USER_HAVE_SAME_NAME_ERROR = "user.have_same_name.error";    
  
  /**
   * 用户更新成功
   */
  public static final String USER_UPDATE_OK = "user.update.ok";
  
  /**
   * 用户更新失败
   */
  public static final String USER_UPDATE_FAILED = "user.update.failed";
  
  /**
   * 用户删除成功
   */
  public static final String USER_DELETE_OK = "user.delete.ok";
  
  
  
  /**
   * 用户不能删除
   */
  public static final String USER_CANT_DELETE = "user.cant.delete";
  
  /**
   * 用户删除失败
   */
  public static final String USER_DELETE_FAILED = "user.delete.failed";
  
  
  
  
  /**
   * 科目添加成功
   */
  public static final String SUBJECT_INSERT_OK = "subject.insert.ok";
  
  /**
   * 科目添加失败
   */
  public static final String SUBJECT_INSERT_FAILED = "subject.insert.failed";
  
  /**
   * 科目编码出错，无父级科目
   */
  public static final String SUBJECT_NO_FATHER_SUBJECT_ERROR = "subject.no_father_subject.error";
  
  
  /**
   * 父级科目是明细科目，不能添加子科目
   */
  public static final String SUBJECT_FATHER_NO_VALID_ERROR = "subject.father_no_valid.error";
  
  /**
   * 存在子科目，父科目不能是明细科目
   */
  public static final String SUBJECT_EXIST_CHILD_CANT_DETL_ERROR = "subject.exist_child_cant_detail.error";
  
  /**
   * 在Vouch中(VOUCDETL)中已经使用该科目，科目只能修改"是否使用"的属性.
   */
  public static final String SUBJECT_EXIST_IN_VOUCH = "subject.exist_in_vouch";
  
  /**
   * 科目编码不合法，应全为数字，通过“-”间隔级次
   */
  public static final String SUBJECT_SUBNO_ERROR = "subject.subno.error";
  
  /**
   * 科目编号重名
   */
  public static final String SUBJECT_HAVE_SAME_SUBNO_ERROR = "subject.have_same_subno.error";
  
  /**
   * 科目名称重名
   */
  public static final String SUBJECT_HAVE_SAME_NAME_ERROR = "subject.have_same_name.error";
  
  
  
  /**
   * 科目更新成功
   */
  public static final String SUBJECT_UPDATE_OK = "subject.update.ok";
  
  /**
   * 科目更新失败
   */
  public static final String SUBJECT_UPDATE_FAILED = "subject.update.failed";
  
  /**
   * 科目删除成功
   */
  public static final String SUBJECT_DELETE_OK = "subject.delete.ok";
  
  /**
   * 科目不能删除
   */
  public static final String SUBJECT_CANT_DELETE = "subject.cant.delete";
  
  /**
   * 科目删除失败
   */
  public static final String SUBJECT_DELETE_FAILED = "subject.delete.failed";
  
  
  /**
   * 现金流量项目添加成功
   */
  public static final String ADDINF_INSERT_OK = "addinf.insert.ok";
  
  /**
   * 现金流量项目添加失败
   */
  public static final String ADDINF_INSERT_FAILED = "addinf.insert.failed";
  
  /**
   * 现金流量项目编码出错，无父级现金流量项目
   */
  public static final String ADDINF_NO_FATHER_ADDINF_ERROR = "addinf.no_father_addinf.error";
  
  
  /**
   * 父级现金流量项目是明细现金流量项目，不能添加子现金流量项目
   */
  public static final String ADDINF_FATHER_NO_VALID_ERROR = "addinf.father_no_valid.error";
  
  /**
   * 存在子现金流量项目，父现金流量项目不能是明细现金流量项目
   */
  public static final String ADDINF_EXIST_CHILD_CANT_DETL_ERROR = "addinf.exist_child_cant_detail.error";
  
  /**
   * 在明细表中已经使用该现金流量项目，现金流量项目只能修改"是否使用"的属性.
   */
  public static final String ADDINF_EXIST_IN_DETL = "addinf.exist_in_detl";
  
  /**
   * 现金流量项目编码不合法，应全为数字，通过“-”间隔级次
   */
  public static final String ADDINF_NO_ERROR = "addinf.no.error";
  
  /**
   * 现金流量项目编号重名
   */
  public static final String ADDINF_HAVE_SAME_NO_ERROR = "addinf.have_same_no.error";
  
  /**
   * 现金流量项目名称重名
   */
  public static final String ADDINF_HAVE_SAME_NAME_ERROR = "addinf.have_same_name.error";
  
  
  /**
   * 现金流量项目更新成功
   */
  public static final String ADDINF_UPDATE_OK = "addinf.update.ok";
  
  /**
   * 现金流量项目更新失败
   */
  public static final String ADDINF_UPDATE_FAILED = "addinf.update.failed";
  
  /**
   * 现金流量项目删除成功
   */
  public static final String ADDINF_DELETE_OK = "addinf.delete.ok";
  
  /**
   * 现金流量项目不能删除
   */
  public static final String ADDINF_CANT_DELETE = "addinf.cant.delete";
  
  /**
   * 现金流量项目删除失败
   */
  public static final String ADDINF_DELETE_FAILED = "addinf.delete.failed";
  
  
  /**
   * 辅助核算项目类型编码没有传递
   */
  public static final String ACCTITEM_ERROR_ITEM_TYPE_NO_REQUIED = "acct.item.error.item.typeno.requied";
  /**
   * 无效的过滤条件参数
   */
  public static final String ACCTITEM_ERROR_INVALID_ITEM_FILTER = "acct.item.error.invalid.itemfilter";
  
  /**
   * 无效的凭证日期
   */
  public static final String VOUCH_ERROR_INVALID_VOUCH_DATE = "vouch.error.invalid.vouchdate";
  /**
   * 该日期所在的期间已经关闭
   */
  public static final String VOUCH_ERROR_PERIOD_CLOSED = "vouch.error.period.closed";
  /**
   * 重复的凭证编码
   */
  public static final String VOUCH_ERROR_VOUCH_NO_EXISTS = "vouch.error.vouchno.exists";
  /**
   * 需要传递凭证流水号
   */
  public static final String VOUCH_ERROR_VOUCH_SEQ_ID_IS_REQUIED = "vouch.error.vouchseqid.isrequied";
  /**
   * 需要传递凭证流水号
   */
  public static final String VOUCH_ERROR_VOUCH_NO_IS_REQUIED = "vouch.error.vouchno.isrequied";
  
  /**
   * 需要传递会计年度
   */
  public static final String BLNS_ERROR_ACCT_YEAR_IS_REQUIED = "blns.error.acct.year.is.requied";
  /**
   * 需要传递记录流水号
   */
  public static final String BLNS_ERROR_SEQ_ID_IS_REQUIED = "blns.error.seq.id.is.requied";
  /**
   * 需要科目余额不能删除
   */
  public static final String BLNS_ERROR_ACCOUNTBLNS_INIT_ALREADYCLOSED = "blns.error.blnsinit.alreadyclosed";
  /**
   * 不能删除非明细余额记录
   */
  public static final String BLNS_ERROR_CAN_NOT_DELET_NON_DETL_BLNS_RECORD = "blns.error.cannot.delete.nondetl.blnsrecord";
  
  
  /**
   * 数据库备份成功
   */
  public static final String DATABASE_BAKEUP_OK = "database.bakeup.ok";
  
  /**
   * 数据库备份失败
   */
  public static final String DATABASE_BAKEUP_FAILED = "database.bakeup.failed";
  
  /**
   * 软件未注册，且已过试用期
   */
  public static final String REGISTRY_NOT = "registry.not";
  /**
   * 软件未注册，超过最大期间数
   */
  public static final String REGISTRY_EXCEED_MAXACCTEDPERIOD = "registry.exceed.maxacctedperiod";
  /**
   * 转账模版号在本年度已存在
   */
  public static final String TRNSVNO_HAVE_SAME_NO_ERROR = "trnsvno.have_same_no.error";
  /**
   * 没有凭证相关权限
   */
  public static final String COMMON_ERROR_DATAPRIVE_NOVOUCHERPRIVATE = "common.error.dataprive.novouchprive";
  

  
  
}

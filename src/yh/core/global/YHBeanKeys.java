package yh.core.global;

public class YHBeanKeys {
  /**
   * 系统参数对象键名-session
   */
  public static final String SYS_PARAM_KEY = "sysParamBean";
  /**
   * FORMBEAN-request
   */
  public static final String ACTION_FORM_BEAN = "actionFormBean";
  /**
   * 系统活动资源-request
   */
  public static final String ACTION_RESOURCES = "actionResources";
  /**
   * 系统用户信息对象键名-session
   */
  public static final String USER_INFO_KEY = "userInfoBean";
  /**
   * i18n工具对象-request
   */
  public static final String I18N_UTILITY = "i18nUtilityBean";
  /**
   * 辅助核算项目类型集合-session
   */
  public static final String ACCT_ITEM_TYPE_SET = "acctItemTypeSet";
  
  /**
   * 结算方式-session
   */
  public static final String RP_MODE_SET = "rpModeSet";
  /**
   * 业务处理类型-session
   */
  public static final String OPTS_TYPE_SET = "optsTypeSet";
  /**
   * 币种列表-session
   */
  public static final String CURR_LIST = "currList";
  /**
   * 汇率列表-session
   */
  public static final String EXCH_RATE_LIST = "exchRateList";
  /**
   * 活动消息列表对象键名-request
   */
  public static final String ACTION_MSRG_LIST = "actionMsrgList";
  /**
   * 数据库连接管理器-request 
   */
  public static final String REQUEST_DB_CONN_MGR = "requestDbConnMgr";
  /**
   * Request
   */
  public static final String CURR_REQUEST = "currRequest";
  public static final String CURR_REQUEST_FLAG = "currRequest";
  public static final String CURR_REQUEST_ADDRESS = "currRequestAddress";
  /**
   * 嵌套调用其他Action过程中参数传递哈希表
   * 为了处理多层嵌套，请按如下算法设置
   * oldParamMap = request.getAttribute(TDCBeanKeys.PARAM_MAP_INNER);
   * if (oldParamMap != null) {request.removeAttribute(TDCBeanKeys.PARAM_MAP_INNER);}
   * 设置 request.setAttribute(TDCBeanKeys.PARAM_MAP_INNER, newParamMap);
   * 执行新Action，并返回
   * if (oldParamMap != null) {request.setAttribute(TDCBeanKeys.PARAM_MAP_INNER, oldParamMap);}
   */
  public static final String PARAM_MAP_INNER = "paramMapInner";
  /**
   * 系统活动集合对象键名-App
   */
  public static final String ACTION_SET = "actionSet";
  /**
   * 数据库表表列表-App
   */
  public static final String TABLE_LIST = "tableList";
  /**
   * 报表数据列表控件-request
   */
  public static final String REPT_LIST_CNTRL = "reptListCntrl";
  /**
   * 附加信息-session
   */
  public static final String ADD_INF_SET = "addInfSet";
  /**
   * 临时表清理-App
   */
  public static final String RELEASE_THREAD = "releaseThread";
  /**
   * 科目扩展属性列表-session
   */
  public static final String SUB_EXT_SORT_LIST = "subExtSortList";
  /**
   * 数据权限-session
   */
  public static final String DATA_PRIV_LIST = "dataPrivateList";
  /**
   * 固定资产设置-session
   */
  public static final String FIXASST_SETTINGS = "fixAsstSettings";
  /**
   * 报表查询参数列表-session
   */
  public static final String REPT_QUERY_PARAM_LIST = "reptQueryParamList";
  /**
   * 凭证字-session
   */
  public static final String VOUCH_WORD_LIST = "vouchWordList";
  /**
   * 凭证摘要-session
   */
  public static final String VOUCH_SUMMARY_LIST = "vouchSummaryList";
  
  /**
   * 通用数据加载对象名称-request
   */
  public static final String DATA_OBJ_KEY = "dataObjectKey";
  /**
   * 辅助核算项目数据对象-request
   */
  public static final String ACCT_ITEM_DATA_LIST = "acctItemDataList";
  /**
   * 科目数据对象-request
   */
  public static final String SUBJECT_DATA_LIST = "subjectDataList";
  /**
   * 期间定义列表-request
   */
  public static final String PERIOAD_DATA_LIST = "periodDataList";
}

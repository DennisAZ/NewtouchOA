package yh.setup.fis.acset.global;


public class TDAuthConst {
  public static final String USER_KEY = "userInfoBean";
  public static final String LOGON_JSP = "/login.jsp";
  
  /**
   * 完全免费版
   */
  public static final String REGIST_SORT_NOT_REGIST = "00NR";
  /**
   * 免费注册版
   */
  public static final String REGIST_SORT_FREE_REGIST = "00FR";
  /**
   * 合作注册版
   */
  public static final String REGIST_SORT_COOPERATE_REGIST = "00CR";
  /**
   * 服务版
   */
  public static final String REGIST_SORT_BUY_REGIST = "00BR";
  
  /**
   * 属性签名名称-用于注册
   */
  public static final String REGIST_PROPKEY_DIGIST_POSTFIX = ".Didgist";
  
  /**
   * 叶子节点菜单标志-是
   */
  public static final String LEAF_MENU_YES = "1";
  
  /**
   * 权限类别-菜单
   */
  public static final String PRIV_TYPE_MENU = "M";
  /**
   * 权限类别-受控活动集合
   */
  public static final String PRIV_TYPE_CNTRLED_ACTION_SET = "C";
  /**
   * 权限类别-受控活动
   */
  public static final String PRIV_TYPE_CNTRLED_ACTION = "A";
  /**
   * 权限类别-功能集
   */
  public static final String PRIV_TYPE_ACTION_SET = "S";
  
  /**
   * 权限性质-系统管理
   */
  public static final String ACTION_SORT_SYS_ADMIN = "SYS";
  /**
   * 权限性质-财务管理
   */
  public static final String ACTION_SORT_FIS_ADMIN = "ACCT";
  
  // by cly 2006-10-16
  
  /**
   * 权限性质-财务管理
   */
  public static final String ACTION_ACCTSET_CHOICE = "ACCTSET_CHOICE";
  /**
   * 权限性质-财务管理
   */
  public static final String ACTION_ACCTSET_NOCHOICE = "ACCTSET_NOCHOICE";
  /**
   * 注册软件
   */
  public static final String ACTION_REGISTRY = "REGISTRY";
}

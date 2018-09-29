package yh.setup.fis.acset.global;


/**
 * 参数标志定义
 * @author jpt
 * @version 1.0
 * @date 2006-10-17
 */
public class TDAcsetParamFlagConst {
  /** 核算币种定义 **/
  //单币种核算
  public static final int MULTI_CURR_FLAG_SINGLE = 0;
  //辅币核算
  public static final int MULTI_CURR_FLAG_AID = 1;
  //多币核算
  public static final int MULTI_CURR_FLAG_MUL = 2;
  /** 汇率处理方式定义 **/
  //采用固定汇率
  public static final int FIX_EXCH_RATE_YES = 0;
  //非固定汇率
  public static final int FIX_EXCH_RATE_NO = 1;
  /** 制单保存策略 **/
  //不平不能保存
  public static final int VOUCH_SAVE_UNEQUAL_YES = 1;
  //不平可以保存
  public static final int VOUCH_SAVE_UNEQUAL_NO = 0;
  /** 凭证格式 **/
  //单币格式
  public static final int VOUCH_FORMAT_MUL_CURR = 1;
  //多币格式
  public static final int VOUCH_FORMAT_SINGLE_CURR = 2;
  //数量格式
  public static final int VOUCH_FORMAT_QUTY = 3;
  /** 凭证制作人员控制 **/
  //不可以制作其他人的凭证
  public static final int CAN_VOUCH_OTHERS_NO = 0;
  //可以制作其他人凭证
  public static final int CAN_VOUCH_OTHERS_YES = 1;
  /** 送退复核启用标志 **/
  //启用
  public static final int VOUCH_PRINT_CHECK_YES = 0;
  //不启用
  public static final int VOUCH_PRINT_CHECK_NO = 1;
  /** 是否携带摘要 **/
  //携带
  public static final int COPY_SUMMARY_YES = 1;
  //不携带
  public static final int COPY_SUMMARY_NO = 1;
  /** 凭证编码规则 **/
  //自动编码
  public static final int VOUCH_NO_RULE_AUTO = 0;
  //手工编码
  public static final int VOUCH_NO_RULE_MANUAL = 1;
  /** 纸张方向 **/
  //横向
  public static final int PAGE_DIRECT_L = 0;
  //纵向
  public static final int PAGE_DIRECT_P = 1;
  /** 是否使用千分位 **/
  //使用千分位
  public static final int USE_KILO_SPLIT_YES = 0;
  //不使用千分位
  public static final int USE_KILO_SPLIT_N = 1;
  /** 账册编码规则 **/
  //统一编号
  public static final int ACCT_BOOK_NO_RULE_ALL_TOGETHER = 0; 
  //按照一级科目编码
  public static final int ACCT_BOOK_NO_RULE_TOP_SUB = 1; 
  /** 会计期间设置规则 **/
  //自然年月
  public static final int PERIOD_NATURAL = 1;
  //跨年度
  public static final int PERIOD_SPAN_YEAR = 2;
  //完全手工
  public static final int PERIOD_MANUAL = 3;
  /** 汇率定义规则 **/
  //本位币金额 = 辅币金额 / 汇率
  public static final int EXCH_RATE_EXPRESS_DIV = 0;
  //本位币金额 = 辅币金额 * 汇率
  public static final int EXCH_RATE_EXPRESS_MUL = 1;
  /** 存在不记帐凭证，转汇率价差控制方式 **/
  //不可以转汇率价差
  public static final int EXCH_DIFF_NON_ACCTED_VOUCH_NO = 0;
  //可以转汇率价差
  public static final int EXCH_DIFF_NON_ACCTED_VOUCH_YES = 1;
  /** 是否使用中文大写金额 **/
  //使用中文大写金额格式
  public static final int USE_CHINESE_MONEY_YES = 0;
  //不使用中文大写金额格式
  public static final int USE_CHINESE_MONEY_NO = 1;
  //是否启用出纳签字
  public static final int USE_CASHER_ASIGN_YES = 1;
  //允许复核本人凭证
  public static final int PERMIT_CHECK_SELF_VOUCH = 1;
  //是否已经关闭期初录入-是
  public static final int CLOSE_INIT_YES = 1;
}

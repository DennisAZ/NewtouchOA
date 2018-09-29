package yh.core.global;

/**
 * 数据库字段相关常量定义
 * @author jpt
 * @version 1.0
 * @date 2006-8-15
 */
public class YHDsFieldConst {
  /**
   * 字段编码在整个编码中所占的位数
   */
  public static final int FIELD_NO_LEN = 3;
  /**
   * 格式化模式-文本
   */
  public static final String FORMAT_MODE_TEXT = "text";
  /**
   * 格式化模式-数值
   */
  public static final String FORMAT_MODE_NUMBER = "number";
  /**
   * 格式化模式-日期
   */
  public static final String FORMAT_MODE_DATE = "date";
  /**
   * 格式化模式-时间
   */
  public static final String FORMAT_MODE_TIME = "time";
  /**
   * 非必添项目
   */
  public static final String IS_MUST_FILL_YES = "Y";  
  /**
   * 必添项目
   */
  public static final String IS_MUST_FILL_NO = "N";
  /**
   * 是否自增-是
   */
  public static final String IS_IDENTITY_YES = "Y";
  /**
   * 是否自增-否
   */
  public static final String IS_IDENTITY_NO = "N";
  /**
   * 是否主键标志-是
   */
  public static final String PRIM_KEY_YES = "1";
  /**
   * 是否辅助核算项目标记
   */
  public static final String IS_ACCT_ITEM_FLAG_YES = "1";
  
  /**
   * 字段类型分类-数值
   */
  public static final int TYPE_CATA_NUMBER = 0;
  /**
   * 字段类型分类-日期
   */
  public static final int TYPE_CATA_DATE = 5;
  /**
   * 字段类型分类-时间
   */
  public static final int TYPE_CATA_TIME = 10;
  /**
   * 字段类型分类-字符串
   */
  public static final int TYPE_CATA_STRING = 15;
  /**
   * 字段类型分类-布尔
   */
  public static final int TYPE_CATA_BOOL = 20;
  /**
   * 字段类型分类-二进制
   */
  public static final int TYPE_CATA_BINARY = 25;
  /**
   * 字段类型分类-空
   */
  public static final int TYPE_CATA_NULL = 30;
}

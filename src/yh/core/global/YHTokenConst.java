package yh.core.global;

public class YHTokenConst {
  /** 行分隔符 **/
  public final static String LINE_SEP = System.getProperty("line.separator");
  /** 行分隔符长度 **/
  public final static int LINE_SEP_LEN  = LINE_SEP.length();
  /** 文件路径分隔符 **/
  public final static String FILE_SEPERATOR = System.getProperty("file.separator");
  
  /** 最外层分隔符 **/
  public static final String MOSTTOP_SPLIT = ",";
  /** 次外层分隔符 **/
  public static final String SECONDTOP_SPLIT = ";";
  /** 键名/键值分隔符 **/
  public static final String KEYVALUE_SPLIT = ":";
  
  /** 编码分隔符 **/
  public static final String CODE_SPLIT = "-";
  /** 或关系编码分隔符 **/
  public static final String SECOND_CODE_SPLIT = "/";
  /** HTML空格 **/
  public static final String HTML_SPACE = "&nbsp;";
  /** 中文空格 **/
  public static final String CHINA_SPACE = "\u3000";
  /** 行分隔符 **/
  public static final String LINE_SPLIT_WINDOW = "\r\n";
}

/*
 * Created on 2005-11-7
 */
package yh.core.util;

/**
 * @author Administrator
 */
public class YHTokenSplit {
  public static final char TOKEN_SPLIT_CN_SPACE = '　';
  public static final char TOKEN_SPLIT_SPACE = ' ';
  public static final char TOKEN_SPLIT_COMMA = ',';
  public static final char TOKEN_SPLIT_COLON = ':';
  public static final char TOKEN_SPLIT_QUOT = '"';
  public static final char TOKEN_SPLIT_ESC = '\\';
  public static final char TOKEN_SPLIT_DECIMAL_POINT = '.';
  public static final char TOKEN_SPLIT_OPT_ADD = '+';
  public static final char TOKEN_SPLIT_OPT_SUB = '-';
  public static final char TOKEN_SPLIT_OPT_MUL = '*';
  public static final char TOKEN_SPLIT_OPT_DIV = '/';
  public static final char TOKEN_SPLIT_OPT_MOD = '%';
  public static final char TOKEN_SPLIT_OPT_LESSTHAN = '<';
  public static final char TOKEN_SPLIT_OPT_GREATTHAN = '>';
  public static final char TOKEN_SPLIT_OPT_EQUAL = '=';
  public static final char TOKEN_SPLIT_LEFTBRACKET = '(';
  public static final char TOKEN_SPLIT_RIGHTBRACKET = ')';
  public static final char TOKEN_SPLIT_DOLLAR = '$';
  public static final char TOKEN_SPLIT_AND = '&';
  public static final char TOKEN_SPLIT_EQUAL = '=';
  
  /**
   * 判断str是否是特征串的分隔符
   * @param str 分隔符
   * @return
   */
  public static boolean isTokenSplit(char split) {
    if (split == '+'
        || split == '-'
        || split == '*'
        || split == '/'
        || split == '%'
        || split == '<'
        || split == '>'
        || split == '='
        || split == '('
        || split == ')') {
      return true;
    }
    return false;
  }
  
  /**
   * 判断str是否是运算符
   * @param str 分隔符
   * @return
   */
  public static boolean isOperator(char split) {
    if (split == '+'
        || split == '-'
        || split == '*'
        || split == '/'
        || split == '%'
        || split == '<'
        || split == '>'
        || split == '=') {
      return true;
    }
    return false;
  }
  
  /**
   * 判断是否是左括号
   * @param split
   * @return
   */
  public static boolean isLeftBracket(char split) {
    if (split == '(') {
      return true;
    }
    return false;
  }
  
  /**
   * 判断是否是右括号
   * @param split 
   * @return
   */
  public static boolean isRightBracket(char split) {
    if (split == ')') {
      return true;
    }
    return false;
  }
  
  /**
   * 判断是否是减号
   * @param split 
   * @return
   */
  public static boolean isSubtractOperator(char split) {
    if (split == '-') {
      return true;
    }
    return false;
  }
}

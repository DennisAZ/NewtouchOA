package yh.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则表达式工具类
 * @author jpt
 * @date   2007-9-29
 */
public class YHRegexpUtility {

  /**
   * 源字符串
   * @param srcStr
   * @param context
   */
  public static String assignVar(String srcStr,
      Map context) {
    
    if (YHUtility.isNullorEmpty(srcStr)
        || context == null) {
      return srcStr;
    }
    
    StringBuffer rtBuff = new StringBuffer();
    Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
    Matcher matcher = pattern.matcher(srcStr);
    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      String varExpr = srcStr.substring(start, end);
      String varValue = (String)context.get(getVar(varExpr));
      if (varValue == null) {
        continue;
      }
      matcher.appendReplacement(rtBuff, varValue);
    }
    matcher.appendTail(rtBuff);
    
    return rtBuff.toString();
  }
  
  /**
   * 用正则表达式分词
   * @param srcStr
   * @param regExpr
   * @return
   */
  public static List splitWords(String srcStr, String regExpr) {
    ArrayList wordList = new ArrayList();
    if (srcStr == null || regExpr == null) {
      return wordList;
    }
    
    Pattern exprPtn = Pattern.compile(regExpr);
    Matcher matcher = exprPtn.matcher(srcStr);
    
    
    while (matcher.find()) {
      int startIndex = matcher.start();
      int endIndex = matcher.end();
      
      wordList.add(srcStr.substring(startIndex, endIndex));
    }
    return wordList;
  }
  
  /**
   * 源字符串
   * @param srcStr
   * @param valueArray
   */
  public static String assignVar(String srcStr,
      String[] valueArray) {
    
    if (valueArray == null || valueArray.length < 1) {
      return srcStr;
    }
    Map context = new HashMap();
    for (int i = 0; i < valueArray.length; i++) {
      context.put(String.valueOf(i), valueArray[i]);
    }
    return assignVar(srcStr, context);
  }
  
  /**
   * 取得变量字符串
   * @param expStr
   * @return
   */
  private static String getVar(String expStr) {
    if (YHUtility.isNullorEmpty(expStr)
        || expStr.length() < 4) {
      return "";
    }
    return expStr.substring(2, expStr.length() - 1);
  }
  
  /**
   * 取得所有匹配的单词
   * @param srcStr
   * @param reg
   * @return
   */
  public static List getMatchedWords(String srcStr, String reg) {
    List rtList = new ArrayList();
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(srcStr);
    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      String varExpr = srcStr.substring(start, end);
      rtList.add(varExpr);
    }
    
    return rtList;
  }
  
  /**
   * Json串拆串
   * @param srcStr
   * @return
   */
  public static List splitJson(String srcStr) {
    if (srcStr == null || srcStr.length() < 1) {
      return new ArrayList();
    }
    srcStr = srcStr.trim();
    if (srcStr.startsWith("{") && srcStr.endsWith("}")) {
      srcStr = srcStr.substring(1, srcStr.length() - 1);
    }
    srcStr = srcStr.trim().replace("\r\n", "").replace("\n", "").replace("\\\"", "{{quote}}").replace("\\\"", "{{squote}}");
    List rtList = getMatchedWords(srcStr, "(?:(?:\"[^\"]+\")|(?:\'[^\']+\')|(?:[^\":,]+))\\s*:\\s*(?:(?:-?\\d*\\.?\\d+(?:E\\d+)?)|(?:\"[^\"]*\")|(?:\'[^\']*\'))");
    for (int i = 0; i < rtList.size(); i++) {
      String str = (String)rtList.get(i);
      rtList.set(i, str.replace("{{quote}}", "\\\"").replace("{{squote}}", "\\\'"));
    }
    return rtList;
  }
  
  /**
   * 替换title
   * @param srcStr                 需要进行替换的字符串
   * @param title                  需要被替换掉的title
   * @param name                   需要替换成的名称
   */
  public static String replaceTitle(String srcStr, String title, String name) {
    title = title.replaceAll("[*]", "\\\\*");
    title = title.replaceAll("[+]", "\\\\+");
    String rtStr = null;
    Pattern pattern = Pattern.compile("([-*+/(,]+)" + title + "([-*+/),]+)");
    Matcher matcher = pattern.matcher(srcStr);
    rtStr = matcher.replaceAll("$1" + name + "$2");
    
    pattern = Pattern.compile("([-*+/,]+)" + title + "$");
    matcher = pattern.matcher(rtStr);
    rtStr = matcher.replaceAll("$1" + name);
    
    pattern = Pattern.compile("^" + title + "([-*+/,]+)");
    matcher = pattern.matcher(rtStr);
    rtStr = matcher.replaceAll(name + "$1");
    
    return rtStr;
  }
  
  /**
   * 去掉所有html标签
   * @param html
   * @return
   */
  public static String cutHtml(String html){
    String result = "";
    result = html.replaceAll("(<[^/\\s][\\w]*)[\\s]*([^>]*)(>)", "$1$3").replaceAll("<[^>]*>", "");
    return result;
  }
}

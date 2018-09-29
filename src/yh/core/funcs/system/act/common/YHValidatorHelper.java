package yh.core.funcs.system.act.common;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Map;

import yh.core.funcs.system.logic.YHSystemLogic;

public abstract class YHValidatorHelper {
  private Map<String,String> map = null;
  
  /*
   * 判断ip是否在指定ip段中
   * 2010-05-09---pjn
   */
  public static boolean betweenIP(String ip, String start, String end){
    long ipL = ip2Long(ip);
    long startL = ip2Long(start);
    long endL = ip2Long(end);
    
    //三个ip中有不合法的,则无法判断
    if (ipL == 0l || startL == 0l || endL == 0l) {
      return false;
    }
    
    if (ipL >= startL && ipL <= endL){
      return true;
    }
    else{
      return false;
    }
  }
  
  /*
   * ip地址转换为long型数字 127.0.0.1->127 000 000 001
   * 当ip含有除了.和[0-9]之外的字符则返回 0
   * 2010-05-09---pjn
   */
  public static long ip2Long(String ip) {
    DecimalFormat df = new DecimalFormat("000");
    String str = "0";
    for (String s : ip.split("\\.")){
      try {
        str += df.format(Integer.parseInt(s));
      } catch (NumberFormatException e) {
        return 0l;
      }
    }
    return Long.parseLong(str);
  }
}

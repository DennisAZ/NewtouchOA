package yh.core.oaknow.util;

import java.sql.SQLException;

import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;

public class YHStringUtil{
  
  /**
   * 判读字符串为空

   * @param flag
   * @return
   */
  public static boolean isEmpty(String flag){
    //YHOut.println(flag == " "+"*******");
    if(flag == null || flag.length()< 1){
      return true;
    }
    return false;
  }
  
  /**
   * 判读字符串非空

   * @param flag
   * @return
   */
  public static boolean isNotEmpty(String flag){
    return !isEmpty(flag);
  }
  
  /**
   * 高亮显示
   * @param str
   * @param flag  需要高亮显示的串

   * @return
   */
  public static String toBright(String str, String flag, int length){
    String newStr = subString(length,str).replaceAll(flag, "<span class='highlight'>"+ flag +"</span>");
    return newStr;
  }
  
  /**
   * 取0--length的字符串
   * @param length
   * @param str
   * @return
   */
  public static String subString(int length, String str){
    if(isNotEmpty(str)){
      if(str.length() > length){
      String strNew = str.substring(0, length);
      strNew = strNew +"....";
      return strNew;
      }else{
        return str;
     }
    }else{
      return "";
    }    
  } 
  /**
   * 编码","<",">"


   * @param str
   * @return
   */
  public static String toChange(String str){
    if(isNotEmpty(str)){
    String newStr = str.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    //YHOut.println(newStr);
    return newStr;
    }
    return "";
  }
  
  public static String replaceN(String str){
    //YHOut.println(str);
    String reg ="[\n-\r]{1,}";
    if(isNotEmpty(str)){
     str = str.replaceAll(reg, "<br/>");
    }
    return str;
  }
  /**
   * 单引号

   * @param str
   * @return
   */
  public static String replaceSQ(String str){
    return str.replaceAll("'", "''");
  }
  
  /**
   * 把某个字段与'%'连接起来
   * @param fieldName 字段名

   * @return
   * @throws SQLException
   */
  public static String dbLike(String fieldName) throws SQLException {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    if (dbms.equals("sqlserver")) {
        return "quotename("+ fieldName +",'%')" ;
      }else if (dbms.equals("mysql")) {
        return "concat('%',"+ fieldName+",'%')" ;
      }else if (dbms.equals("oracle")) {
        return "concat(concat('%',"+ fieldName +"),'%')" ;
      }else {
        throw new SQLException("not accepted dbms");
      } 
  }
  public static void main(String[] args){
    /* String test = "1啊啊啊啊啊啊啊啊啊\r\n啊啊啊啊啊啊啊 %";
     //System.out.println(test);
     //System.out.println(replaceN(test));*/
     
     
    
     String s = "\r\n   iii \n 44\n\r4";
     String reg ="[\n-\r]{1,}";
     s = s.replaceAll(reg ,"<br/>"); 
     /*Pattern p = Pattern.compile(reg);
     Matcher m = p.matcher(s);
     String beizhu = m.replaceAll("<br>");*/
     //System.out.println(s);
     
  }
}

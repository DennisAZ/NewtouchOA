package yh.core.util.db;

import yh.core.data.YHDBMapUtil;

public final class YHStringFormat {

  private YHStringFormat() {
  }

  /**
   * 
   * @param str
   * @param str2
   * @return
   */
  public static String unformat(String str) {
    String st = null;
    if (str == null) {
      return null;
    }
    StringBuffer words = new StringBuffer();
    // 得到一个String数组
    String[] temps = str.split("_");
    for (String string : temps) {
      String temp = string.trim();
      String s = temp.substring(1, temp.length());
      String ss = temp.substring(0, 1).toUpperCase() + s.toLowerCase();
      words.append(ss);
    }
    st = words.toString().substring(0, 1).toLowerCase()
        + words.toString().substring(1, words.toString().length());
    return st;
  }

  /**
   * 将字符串去除conditionString转换成XXX_YYY格式的字符串
   * 
   * @param originalString
   * @param conditionString
   * @return formatString
   */
  public static String format(String str, String str2) {
	  System.out.println(str);
    String result = "";
    if (str2 != null && str.startsWith(str2)) {
      String s = stringCutOut(str, str2);
      System.out.println(s);
      result = format(s,true);
      System.out.println(result);
    } else {
      result = format(str,true);
    }
    return result;
 
  }
  /**
   * 将字符串str转换成XXX_YYY格式的字符串
   * @param str String
   * @return
   */
  public static String format(String str){
     StringBuffer words = new StringBuffer();
        
        for(int i=0;i<str.length();i++){
          char ch = str.charAt(i);
        
          if(Character.isUpperCase(ch)){
            String s = str.substring(0, i);
            String ss = s.toUpperCase();
            if(!"".equals(ss)){
            words.append(ss+"_");
            }
            str = str.substring(i);
            i=0;
        
          }
        }
        words.append(str.toUpperCase());
        return words.toString();
  }
  /**
   * 将字符串str转换成XXX_YYY格式的字符串
   * 
   * @param str
   *          String
   * @return
   */
  public static String format(String str,boolean isTable) {
    StringBuffer words = new StringBuffer();

    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);

      if (Character.isUpperCase(ch)) {
        String s = str.substring(0, i);
        String ss = s.toUpperCase();
        if (!"".equals(ss)) {
          words.append(ss + "_");
        }
        str = str.substring(i);
        i = 0;

      }
    }
    words.append(str.toUpperCase());
    if(isTable)
    return YHDBMapUtil.getMapDBName(words.toString());
    else
    return words.toString();	
  }

  /**
   * 将originalString字符串去conditionString头

   * 
   * @param originalString
   * @param conditionString
   * @return
   */
  private static String stringCutOut(String str, String str2) {

    String result = "";
    int index = str2.length();
    result = str.substring(index);

    return result;
  }

}

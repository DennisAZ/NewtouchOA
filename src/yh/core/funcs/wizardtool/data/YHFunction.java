package yh.core.funcs.wizardtool.data;

import java.util.HashMap;
import java.util.Map;

public class YHFunction {
  public void loadToHtml(String id , String parameters
      , StringBuffer sb ,Map libMap){
   
    String objStr = parameters.substring(1, parameters.length() - 1);
    objStr = objStr.replaceAll("funName:\"", " ");
    objStr = objStr.replaceAll("\",funPar:\"", ";");
    objStr = objStr.replaceAll("\",funCode:\"", ";");
    objStr = objStr.replaceAll("\"", " ");
    String[] temp = objStr.split(";");
    sb.insert(0,"\nfunction " + temp[0].trim() + "(" + temp[1].trim() + "){\n" + this.unescape(temp[2]).trim() + "\n}");
  }

  public  String unescape(String src) {
    StringBuffer tmp = new StringBuffer();
    tmp.ensureCapacity(src.length());
    int lastPos = 0, pos = 0;
    char ch;
    while (lastPos < src.length()) {
      pos = src.indexOf("%", lastPos);
      if (pos == lastPos) {
        if (src.charAt(pos + 1) == 'u') {
          ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
          tmp.append(ch);
          lastPos = pos + 6;
        } else {
          ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
          tmp.append(ch);
          lastPos = pos + 3;
        }
      } else {
        if (pos == -1) {
          tmp.append(src.substring(lastPos));
          lastPos = src.length();
        } else {
          tmp.append(src.substring(lastPos, pos));
          lastPos = pos;
        }
      }
    }
    return tmp.toString();
  }
}

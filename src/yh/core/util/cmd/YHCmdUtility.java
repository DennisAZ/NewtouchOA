package yh.core.util.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令行工具类
 * @author jpt
 *
 */
public class YHCmdUtility {
  
  /**
   * 解析命令选项参数 格式 -optionName{optionValue}
   * @param args                   命令数组
   * @param options                选项名称数组
   * @return
   */
  public static Map<String, String> parseOptions(String[] args, String[] options) {
    Map rtMap = new HashMap<String, String>();
    if (args == null || options == null) {
      return rtMap;
    }
    for (int i = 0; i < args.length; i++) {
      String argStr = args[i].trim();
      if (argStr.length() < 1) {
        continue;
      }
      for (int j = 0; j < options.length; j++) {
        String optionName = options[j];
        if (argStr.startsWith("-" + optionName)) {
          rtMap.put(optionName, argStr.substring(optionName.length() + 1));
          break;
        }
      }
    }
    return rtMap;
  }
  
  /**
   * 取得用户的输入
   * @param cnt    输入参数的个数
   * @return
   */
  public static String[] getInputFromUser(int cnt) throws Exception {
    ArrayList<String> rtList = new ArrayList<String>();
    
    System.out.println("请您输入 " + cnt + " 个以空格分隔的参数");
    System.out.println("空格请输入&nbps;/空字符串&empty;/空值请输入&null;");
    System.out.print(">>");
    
    String tmpStr = "";
    while (true) {
      int c = System.in.read();
      if (c == '\r') {
        continue;
      }
      if (c == '\n') {
        if (tmpStr.length() > 0) {
          String[] tmpArray = tmpStr.split(" ");
          for (int i = 0; i < tmpArray.length; i++) {
            String rtStr = tmpArray[i];
            if (rtStr.startsWith("&nbsp;")) {
              String[] spaceArray = rtStr.split(";");
              rtStr = "";
              for (int j = 0; j < spaceArray.length; j++) {
                rtStr += " ";
              }
            }else if (rtStr.equalsIgnoreCase("&empty;")) {
              rtStr = "";
            }else if (rtStr.equalsIgnoreCase("&null;")) {
              rtStr = null;
            }
            rtList.add(rtStr);
          }
          tmpStr = "";
        }
        if (rtList.size() >= cnt) {
          break;
        }else {
          System.out.println("您输入的参数不够，请继续输入 "
              + (cnt - rtList.size()) + " 个以空格分隔的参数");
          System.out.println("空格请输入&nbps;/空字符串&empty;/空值请输入&null;");
          System.out.print(">>");
        }
      }else {
        tmpStr += (char)c;
      }
    }
    
    String[] rtArray = new String[rtList.size()];
    rtList.toArray(rtArray);
    return rtArray;
  }
  
  /**
   * 执行命令
   * @param cmd
   */
  public static void exCmd(String cmd) {
    try {
      Runtime  r = Runtime.getRuntime();
      Process process = r.exec(cmd);      
      java.util.List<String> msrgList = new ArrayList<String>();
      YHStreamPumper sp = new YHStreamPumper(process.getInputStream(), msrgList);
      sp.start();
      process.waitFor();
      sp.join();
      process.destroy();
      for (int i = 0; i < msrgList.size(); i++) {
        System.out.println((String)msrgList.get(i));
      }
    }catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}

package yh.core.esb.common.util;

import java.io.PrintStream;

public class YHEsbUtil {
  private static PrintStream out = System.out;
  public static void println(Object o) {
    if (out != null) {
      out.println(o);
    }
  }
  
  public static void print(Object o) {
    if (out != null) {
      out.print(o);
    }
  }
  
  public static void setPrintStream(PrintStream out) {
    YHEsbUtil.out = out;
  }
}

package yh.core.data;

import java.util.Comparator;
import java.util.Map;

import yh.core.util.YHUtility;

public class YHMapComparator implements Comparator<Map> {
  public static final int TYPE_STR = 0;
  public static final int TYPE_INT = 1;
  public static final int TYPE_FLOAT = 2;
  public static final int TYPE_LONG = 3;
  
  //类型 1=整型; 2=浮点型; 3=长整型; 其他=字符串
  private int type = 0;
  //键名
  private String key = null;
  //方向, DESC=升序; ASC=降序
  private String direct = "ACS";
  
  /**
   * 构造函数
   * @param key
   */
  public YHMapComparator(String key) {
    this(0, key, "ACS");
  }
  /**
   * 构造函数
   * @param type
   * @param key
   * @param direct
   */
  public YHMapComparator(int type, String key, String direct) {
    this.type = type;
    this.key = key;
    this.direct = direct;
  }

  /**
   * 比较方法实现
   * @param o1
   * @param o2
   * @return
   */
  public int compare(Map o1, Map o2) {
    String op1 = (String)o1.get(key);
    String op2 = (String)o2.get(key);
    if (this.type == TYPE_INT) {
      int intOp1 = 0;
      int intOp2 = 0;
      if (!YHUtility.isNullorEmpty(op1)) {
        intOp1 = Integer.parseInt(op1);
      }
      if (!YHUtility.isNullorEmpty(op2)) {
        intOp2 = Integer.parseInt(op2);
      }
      if (this.direct.equalsIgnoreCase("DESC")) {
        return (int)intOp2 - intOp1;
      }else {
        return (int)intOp1 - intOp2;
      }
    }else if (this.type == TYPE_FLOAT) {
      double dblOp1 = 0;
      double dblOp2 = 0;
      if (!YHUtility.isNullorEmpty(op1)) {
        dblOp1 = Double.parseDouble(op1);
      }
      if (!YHUtility.isNullorEmpty(op2)) {
        dblOp2 = Double.parseDouble(op2);
      }
      double diff = dblOp1 - dblOp2;
      if (this.direct.equalsIgnoreCase("DESC")) {
        return diff < 0 ? 1 : (diff > 0 ? -1 : 0);
      }else {
        return diff < 0 ? -1 : (diff > 0 ? 1 : 0);
      }
    }else if (this.type == TYPE_LONG) {
      long intOp1 = 0;
      long intOp2 = 0;
      if (!YHUtility.isNullorEmpty(op1)) {
        intOp1 = Long.parseLong(op1);
      }
      if (!YHUtility.isNullorEmpty(op2)) {
        intOp2 = Long.parseLong(op2);
      }
      if (this.direct.equalsIgnoreCase("DESC")) {
        return (int)(intOp2 - intOp1);
      }else {
        return (int)(intOp1 - intOp2);
      }
    }else {
      if (op1 == null) {
        op1 = "";
      }
      if (op2 == null) {
        op2 = "";
      }
      if (this.direct.equalsIgnoreCase("DESC")) {
        return op2.compareTo(op1);
      }else {
        return op1.compareTo(op2);
      }
    }
  }

}

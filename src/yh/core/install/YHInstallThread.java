package yh.core.install;

import java.lang.reflect.Method;

public class YHInstallThread extends Thread {
  //委托对象
  private Object adaptee = null;
  /**
   * 方法名称
   */
  private String methodName = null;
  /**
   * 参数列表
   */
  private Object[] params = null;
  /**
   * 构造方法
   * @param proxyObj
   * @param isNewThread
   */
  public YHInstallThread(Object adaptee, String methodName, Object[] params) {
    this.adaptee = adaptee;
    this.methodName = methodName;
    this.params = params;
  }
  /**
   * 执行活动
   */
  public void run() {
    int paramCnt = 0;
    if (params != null) {
      paramCnt = params.length;
    }
    
    Object[] paramObjArray = new Object[paramCnt];
    Class[] paramClassArray = new Class[paramCnt];
    if (params != null) {
      for (int i = 0; i < params.length; i++) {
        paramObjArray[i] = params[i];
        paramClassArray[i] = params[i].getClass();
      }
    }
    
    Class adapteeClass = adaptee.getClass();
    try {
      Method method = adapteeClass.getMethod(methodName, paramClassArray);
      method.invoke(adaptee, paramObjArray);
    }catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}

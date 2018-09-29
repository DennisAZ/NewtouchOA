package yh.core.util.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class YHSocketUtil {
  /**
   * 取得输入阅读器
   * @param s
   * @return
   * @throws IOException
   */
  public static BufferedReader getReader(Socket s) throws IOException {
    return (new BufferedReader(
        new InputStreamReader(s.getInputStream())));
  }
  
  /**
   * 取得书写器
   * @param s
   * @return
   * @throws IOException
   */
  public static PrintWriter getWriter(Socket s) throws IOException {
    return (new PrintWriter(
        s.getOutputStream(), true));
  }
  
  /**
   * 判断端口是否已经被占用
   * @param port
   * @return
   */
  public static boolean isPortUsed(int port) {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(port);
      return false;
    }catch(Exception ex) {
      return true;
    }finally {
      try {
        if (socket != null) {
          socket.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  
  /**
   * 取得没有使用的端口
   * @param basePort
   * @return
   */
  public static int getNotUsetPort(int basePort) {
    if (!isPortUsed(basePort)) {
      return basePort;
    }
    for (int i = 0; i < 1000; i++) {
      int testPort = basePort + (2 * (i % 2) - 1) * i;
      if (!isPortUsed(testPort)) {
        return testPort;
      }
    }
    return basePort + 1000;
  }
}

package yh.core.esb.client.service;

import java.rmi.RemoteException;

import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

import yh.core.esb.common.util.YHEsbUtil;

public class YHWSCaller {
  private static Logger log = Logger.getLogger(YHWSCaller.class);
  
  private   String WS_PATH  ;
  public String getWS_PATH() {
    return WS_PATH;
  }
  public void setWS_PATH(String ws_path) {
    WS_PATH = ws_path;
  }
  public String config(String host, int port, String username, String password, String webserviceUri, String cacheDir , String token) {
    try {
      String serviceUrl = this.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("config");
      call.addParameter("host", XMLType.XSD_STRING, ParameterMode.IN); 
      call.addParameter("port", XMLType.XSD_INT, ParameterMode.IN);
      call.addParameter("username", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("password", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("webserviceUri", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("cacheDir", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("token",XMLType.XSD_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {host, port, username, password, webserviceUri, cacheDir , token});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("config - 调用web服务异常,异常信息:" + e.getMessage());
      return "config - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  public String login(String token) {
    try {
      String serviceUrl = this.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("login");
      call.setReturnType(XMLType.XSD_STRING); 
      call.addParameter("token",XMLType.XSD_STRING, ParameterMode.IN);
      String ret = (String) call.invoke(new Object[] {token});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("login - 调用web服务异常,异常信息:" + e.getMessage());
      return "login - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  /**
   * 
   * @param filepath
   * @param toId
   * @param token
   * @return
   * {code:'-3',msg:'File does not exist!'} 发送文件不存在
   * {code:'0',msg:'The file is sendding...'} 文件正在发送中...
   * {code:'-1',msg:'Task queue is full, please try again!'} 队列任务已满
   * {code:'-2',msg:'Host address not configured!'} 获取主机配置不正确
   * {code:'-100',msg:'Exception Occurred, please try again!'} 程序出错
   * {code: "-7", msg: "令牌不对！"} 令牌配置不对
   * {msg:'Unknown error!'}
   */
  public String send(String filepath, String toId , String optGuid , String token) {
    try {
      String serviceUrl = this.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("send");
      call.addParameter("filepath", XMLType.XSD_STRING, ParameterMode.IN); 
      call.addParameter("toId", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("token",XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("optGuid",XMLType.XSD_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {filepath, toId , token , optGuid });
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("send - 调用web服务异常,异常信息:" + e.getMessage());
      return "send - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  /**
   * 
   * @param guid 发文的文件
   * @param toId 给那些客户端重发，多个以逗号分割
   * @param token 
   * @return
   */
  public String resend(String guid , String toId , String token) {
    try {
      String serviceUrl = this.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("resend");
      call.addParameter("guid", XMLType.XSD_STRING, ParameterMode.IN); 
      call.addParameter("toId", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("token",XMLType.XSD_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {guid, toId , token});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("send - 调用web服务异常,异常信息:" + e.getMessage());
      return "send - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  public String broadcast(String filepath , String token) {
    try {
      String serviceUrl = this.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("broadcast");
      call.addParameter("filepath", XMLType.XSD_STRING, ParameterMode.IN); 
      call.addParameter("token",XMLType.XSD_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {filepath , token});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("broadcast - 调用web服务异常,异常信息:" + e.getMessage());
      return "broadcast - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  public boolean isOnline(String token) {
    try {
      String serviceUrl = this.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("isOnline");
      call.setReturnType(XMLType.XSD_BOOLEAN); 
      call.addParameter("token",XMLType.XSD_STRING, ParameterMode.IN);
      boolean ret = (Boolean) call.invoke(new Object[] {token});
      return ret;
    } catch (Exception e) {
      log.error("login - 调用web服务异常,异常信息:" + e.getMessage());
      return false;
    }
  }

  public static void main(String[] args) throws RemoteException {
//    long s = System.currentTimeMillis();
//    String md5 = YHDigestUtility.md5File("E:\\TDDOWNLOAD\\database中文.rar");
//    long e = System.currentTimeMillis();
//    System.out.println((e - s) / 1000);
    
    YHWSCaller caller = new YHWSCaller();
    //caller.config("pjn-pc", 80, "fgs1", "", "", "D:\\ESB-CACHE");
   // caller.login();
    //System.out.println("在线状态:" + caller.isOnline());
    //System.out.println(caller.send("d:\\Screenshot.png", "client"));
    
  }
}

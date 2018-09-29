package yh.core.esb.test.ws;

import java.rmi.RemoteException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;
import yh.core.esb.common.util.YHEsbUtil;

import org.apache.axis.encoding.XMLType; 
import javax.xml.rpc.ParameterMode;

public class YHWSCaller {
  private static final String WS_PATH = "http://192.168.0.102:88/yh/services/YHEsbService";
  private static Logger log = Logger.getLogger(YHWSCaller.class);

  public String config(String host, int port, String username, String password, String webserviceUri, String cacheDir) {
    try {
      String serviceUrl = YHWSCaller.WS_PATH;
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
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {host, port, username, password, webserviceUri, cacheDir});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("config - 调用web服务异常,异常信息:" + e.getMessage());
      return "config - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  
  public String login() {
    try {
      String serviceUrl = YHWSCaller.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("login");
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("login - 调用web服务异常,异常信息:" + e.getMessage());
      return "login - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  
  public String send(String filepath, String toId) {
    try {
      String serviceUrl = YHWSCaller.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("send");
      call.addParameter("filepath", XMLType.XSD_STRING, ParameterMode.IN); 
      call.addParameter("toId", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("token", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("optGuid", XMLType.XSD_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {filepath, toId , "eeeee" , ""});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("send - 调用web服务异常,异常信息:" + e.getMessage());
      return "send - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }
  public String down(String guid) {
    try {
      String serviceUrl = YHWSCaller.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("down");
      call.addParameter("guid", XMLType.XSD_STRING, ParameterMode.IN); 
      call.addParameter("token", XMLType.XSD_STRING, ParameterMode.IN);
      call.addParameter("userId", XMLType.XSD_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {guid , "eeeee"  ,"3"});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("send - 调用web服务异常,异常信息:" + e.getMessage());
      return "send - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }

  public String broadcast(String filepath) {
    try {
      String serviceUrl = YHWSCaller.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("broadcast");
      call.addParameter("filepath", XMLType.XSD_STRING, ParameterMode.IN); 
      call.setReturnType(XMLType.XSD_STRING); 
      String ret = (String) call.invoke(new Object[] {filepath});
      YHEsbUtil.println(ret);
      return ret;
    } catch (Exception e) {
      log.error("broadcast - 调用web服务异常,异常信息:" + e.getMessage());
      return "broadcast - 调用web服务异常,异常信息:" + e.getMessage();
    }
  }

  public boolean isOnline() {
    try {
      String serviceUrl = YHWSCaller.WS_PATH;
      Service service = new Service(); 
      Call call = (Call) service.createCall(); 
      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
      call.setOperationName("isOnline");
      call.setReturnType(XMLType.XSD_BOOLEAN); 
      boolean ret = (Boolean) call.invoke(new Object[] {});
      YHEsbUtil.println(ret);
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
    //caller.down("c4cceead-6f43-42f2-a923-027613a3667d");
    caller.send("d:\\test\\big.jpg", "client");
    //caller.resend("d5fa95df-3851-4c65-a29f-68a40a98c5b2", "client", "eeeee");
  //caller.resend("023e0eeb-5b1c-4594-96a2-15d64cd54a66", "client", "eeeee");
    //System.out.println("在线状态:" + caller.isOnline());
    //System.out.println(caller.send("d:\\Screenshot.png", "client"));
  }
  public String resend(String guid , String toId , String token) {
    try {
      String serviceUrl = YHWSCaller.WS_PATH;
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
}

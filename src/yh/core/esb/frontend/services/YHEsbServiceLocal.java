package yh.core.esb.frontend.services;

import java.util.HashMap;
import java.util.Map;

import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.common.util.PropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.frontend.YHEsbFrontend;
import yh.core.esb.frontend.logic.YHEsbPollerLogic;
import yh.core.util.YHUtility;

public class YHEsbServiceLocal {
  /**
   * 配置用户名密码主机地址
   * @param host
   * @param username
   * @param password
   * @param oaWebserviceUri
   * @return
   */
  public String config(String host, int port, String username, String password, String webserviceUri, String cacheDir , String isLocal) {
      ClientPropertiesUtil.updateProp("usercode", username);
      ClientPropertiesUtil.updateProp("port", String.valueOf(port));
      ClientPropertiesUtil.updateProp("password", password);
      ClientPropertiesUtil.updateProp("host", host);
      ClientPropertiesUtil.updateProp("webserviceUri", webserviceUri);
      ClientPropertiesUtil.updateProp("cacheDir", cacheDir);
      ClientPropertiesUtil.updateProp("isLocal", isLocal);
      ClientPropertiesUtil.store();
      
      ClientPropertiesUtil.refresh();
      return "{\"code\": \"0\", \"msg\": \"Configuration has been modified!\"}";
  }
  /**
   * 读配置文件登陆   * @return
   */
  public String login() {
      ClientPropertiesUtil.refresh();
      String username = ClientPropertiesUtil.getProp("usercode");
      String host = ClientPropertiesUtil.getHost();
      int port = ClientPropertiesUtil.getHostPort();
      String password = ClientPropertiesUtil.getProp("password");
      
      if (YHUtility.isNullorEmpty(username) ||
          YHUtility.isNullorEmpty(host)) {
        return "{\"code\": \"-6\", \"msg\": \"Configuration information is not completely!\"}";
      }
      return YHEsbFrontend.login(host, port, username, password);
  }
  
  /**
   * 发送函数
   * @param filepath      待发送文件的绝对路径
   * @param toId          发送到的OA服务器id串，多个用逗号隔开
   * @return              返回发送的状态

   */
  public String send(final String filepath, final String toId , String optGuid , String message) {
      String str = YHEsbFrontend.send(filepath, toId , optGuid ,  message);
      return str;
  }
  public String resend(final String guid, final String toId) {
      String str = YHEsbFrontend.resend(guid, toId);
      return str;
  }
  public String redown(final String guid) {
    String str = YHEsbFrontend.redown(guid);
    YHEsbPollerLogic logic = new YHEsbPollerLogic();
    logic.updateStatus(guid, "1");
    return str;
}
  /**
   * 发送给所有用户
   * @param filepath
   * @return
   */
  public String broadcast(String filepath   , String optGuid , String message) {
      return YHEsbFrontend.broadcast(filepath , optGuid , message);
  }
  public String down(String guid , String userId){
      return YHEsbFrontend.down(guid , userId);
  }
  /**
   * 是否在线
   * @return
   */
  public boolean isOnline() {
    return YHEsbFrontend.isOnline();
  }
}

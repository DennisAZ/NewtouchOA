package yh.core.esb.frontend.services;

import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.common.util.PropertiesUtil;
import yh.core.esb.frontend.YHEsbFrontend;
import yh.core.util.YHUtility;

public class YHEsbService {
  YHEsbServiceLocal local = new YHEsbServiceLocal();
  public static String TOKEN_ERROR = "{\"code\": \"-7\", \"msg\": \"令牌不对！\"}";
  private static String TOKEN = null;
  public static String getToken() {
    if (TOKEN == null) {
      TOKEN = PropertiesUtil.getProp("token");
    }
    return TOKEN;
  }
  /**
   * 配置用户名密码主机地址
   * @param host
   * @param username
   * @param password
   * @param oaWebserviceUri
   * @return
   */
  public String config(String host, int port, String username, String password, String webserviceUri, String cacheDir , String token) {
    if (YHEsbService.getToken().equals(token)) {
      ClientPropertiesUtil.updateProp("usercode", username);
      ClientPropertiesUtil.updateProp("port", String.valueOf(port));
      ClientPropertiesUtil.updateProp("password", password);
      ClientPropertiesUtil.updateProp("host", host);
      ClientPropertiesUtil.updateProp("webserviceUri", webserviceUri);
      ClientPropertiesUtil.updateProp("cacheDir", cacheDir);
      ClientPropertiesUtil.updateProp("isLocal", "0");
      ClientPropertiesUtil.store();
      
      ClientPropertiesUtil.refresh();
      return "{\"code\": \"0\", \"msg\": \"Configuration has been modified!\"}";
    } else {
      return this.TOKEN_ERROR;
    }
    
  }
  /**
   * 读配置文件登陆   * @return
   */
  public String login(String token) {
    String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      return local.login();
    } else {
      if (YHEsbService.getToken().equals(token)) {
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
      } else {
        return this.TOKEN_ERROR;
      }
    }
  }
  
  /**
   * 发送函数
   * @param filepath      待发送文件的绝对路径
   * @param toId          发送到的OA服务器id串，多个用逗号隔开
   * @return              返回发送的状态

   */
  public String send(final String filepath, final String toId  , String token , String optGuid , String message) {
    String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      return local.send(filepath, toId, optGuid , message);
    } else {
      if (YHEsbService.getToken().equals(token)) {
        String str = YHEsbFrontend.send(filepath, toId , optGuid , message);
        return str;
      } else {
        return this.TOKEN_ERROR;
      }
    }
  }
  public String resend(final String guid, final String toId , String token) {
    String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      return local.resend(guid, toId);
    } else {
      if (YHEsbService.getToken().equals(token)) {
        String str = YHEsbFrontend.resend(guid, toId);
        return str;
      } else {
        return this.TOKEN_ERROR;
      }
    }
  }
  /**
   * 需要下载的文件的guid
   * @param guid
   * @param token
   * @return
   */
  public String redown(final String guid, String token) {
    String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      return local.redown(guid);
    } else {
      if (YHEsbService.getToken().equals(token)) {
        String str = YHEsbFrontend.redown(guid);
        return str;
      } else {
        return this.TOKEN_ERROR;
      }
    }
  }
  /**
   * 发送给所有用户
   * @param filepath
   * @return
   */
  public String broadcast(String filepath  , String token , String optGuid , String message) {
    String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      return local.broadcast(filepath, optGuid , message);
    } else {
      if (YHEsbService.getToken().equals(token)) {
        return YHEsbFrontend.broadcast(filepath , optGuid , message);
      } else {
        return this.TOKEN_ERROR;
      }
    }
  }
  public String down(String guid, String token , String userId){
    String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      return local.down(guid, userId);
    } else {
      if (YHEsbService.getToken().equals(token)) {
        return YHEsbFrontend.down(guid , userId);
      } else {
        return this.TOKEN_ERROR;
      }
    }
  }
  /**
   * 是否在线
   * @return
   */
  public boolean isOnline(String token) {
    String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      return local.isOnline();
    } else {
      if (YHEsbService.getToken().equals(token)) {
        return YHEsbFrontend.isOnline();
      } else {
        return false;
      }
    }
  }
}

package yh.core.funcs.system.ispirit.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHMsgPusher {
  public static String PREFIX = "S";
  public static String SEPARATOR = "^";
  public static String MSG_SMS = "a";
  public static String MSG_AVATAR = "b";
  public static String MSG_MYSTATUS = "c";
  public static String MSG_GROUP = "d";
  
  /**
   * 内部短信推送
   * @param idStr
   * @throws IOException
   */
  public static void pushSms(String idStr) throws IOException {
    if (YHUtility.isNullorEmpty(idStr)) {
      return;
    }
    if (!idStr.endsWith(",")) {
      idStr += ",";
    }
    push(PREFIX + SEPARATOR + MSG_SMS + SEPARATOR + idStr);
  }
  
  /**
   * 修改头像消息
   * @param id
   * @param path
   * @throws IOException
   */
  public static void pushAvatar(String id, String path) throws IOException {
    if (YHUtility.isNullorEmpty(id)) {
      return;
    }
    push(PREFIX + SEPARATOR + MSG_AVATAR + SEPARATOR + id + SEPARATOR + path);
  }
  
  /**
   * 修改状态的消息
   * @param id
   * @param content
   * @throws IOException
   */
  public static void pushMyStatus(String id, String content) throws IOException {
    if (YHUtility.isNullorEmpty(id)) {
      return;
    }
    push(PREFIX + SEPARATOR + MSG_MYSTATUS + SEPARATOR + id + SEPARATOR + content);
  }
  
  /**
   * 推送消息的方法
   * @param receiverIp
   * @param content
   * @param port
   * @throws IOException
   */
  public static void push(String receiverIp, int port, String content, String charset) throws IOException {  
    try {
      DatagramSocket socket = null;
      DatagramPacket packet = null;
      byte[] data = content.getBytes(charset);
      socket = new DatagramSocket();
      socket.setBroadcast(true);
      packet = new DatagramPacket(data, data.length, InetAddress.getByName(receiverIp), port);
      socket.send(packet);
    } catch (UnknownHostException e) {
      throw e;
    }
  }
  
  /**
   * 推送消息的方法,从配置中读取im服务器和端口号
   * @param receiverIp
   * @param content
   * @param port
   * @throws IOException
   */
  public static void push(String content) throws IOException {  
    //从配置文件中读取
    String addr = YHSysProps.getString("IM_SERVER_ADDR");
    String portStr = YHSysProps.getString("IM_SERVER_PORT");
//    String addr = "192.168.1.1";
//    String portStr = "";
    int port = 1188;
    if (YHUtility.isNullorEmpty(addr)) {
      addr = "127.0.0.1";
    }
    if (YHUtility.isNumber(portStr)) {
      port = Integer.parseInt(portStr);
    }
    push(addr, port, content, "GBK");
  }
  
  /**
   * 组织机构更新
   * @param conn
   * @param smsIds
   * @return
   * @throws Exception
   */
   public static void updateOrg(Connection dbConn) throws Exception{
     PreparedStatement ps = null;
     Statement st = null;
     ResultSet rs = null;
     try {
       String sql = "select count(1) as AMOUNT from SYS_PARA where PARA_NAME = 'ORG_UPDATE'";
       ps = dbConn.prepareStatement(sql);
       rs = ps.executeQuery();
       int amount = 0;
       if (rs.next()) {
         amount = rs.getInt("AMOUNT");
       }
       
       SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
       String now = df.format(new Date());
       if (amount == 0) {
         sql = "insert into SYS_PARA " +
         		"(PARA_NAME, PARA_VALUE) " +
         		"values ('ORG_UPDATE', " + now + ")";
       }
       else {
         sql = "update SYS_PARA " +
         " set PARA_VALUE = " + now +
         " where PARA_NAME = 'ORG_UPDATE'";
       }
       
       st = dbConn.createStatement();
       st.executeUpdate(sql);
     } catch (Exception e) {
       throw e;
     } finally {
       YHDBUtility.close(ps, null, null);
     }
   }
  
  public static void main(String[] args) throws IOException {
    //pushMyStatus("1", "1111");
    //pushSms("1,");
    pushAvatar("1", "1.gif");
  }
}
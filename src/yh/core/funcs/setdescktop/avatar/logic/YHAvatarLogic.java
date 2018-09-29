package yh.core.funcs.setdescktop.avatar.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.db.YHORM;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.menu.data.YHSysMenu;
import yh.core.util.db.YHDBUtility;

public class YHAvatarLogic {
  private static Logger log = Logger.getLogger(YHAvatarLogic.class);
  
  /**
   * 获取天气和rss是否显示的信息
   * @param conn
   * @return
   * @throws Exception
   */
  public String[] getWeatherRss(Connection conn) throws Exception {
    Statement sm = null;
    ResultSet rs = null;
    
    try {
      String result[] = new String[2];
      String sql = "select SHOW_RSS" +
      		",WEATHER_CITY" +
      		" from oa_inf";
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      if(rs.next()){
        result[0] = rs.getString("SHOW_RSS") == null ? "" :  rs.getString("SHOW_RSS");
        result[1] = rs.getString("WEATHER_CITY") == null ? "" : rs.getString("WEATHER_CITY");
      }
      
      return result;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  /**
   * 初始化表单使用
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public Map getForm(Connection conn,int seqId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "select MENU_TYPE" +
      		",MENU_IMAGE" +
      		",MENU_EXPAND" +
      		",PANEL" +
      		",SMS_ON" +
      		",SHOW_RSS" +
      		",THEME" +
      		",CALL_SOUND" +
      		",NEV_MENU_OPEN" +
      		" from PERSON p" +
      		" where SEQ_ID = ?";
    
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      return this.resultSet2Map(rs);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取默认展开菜单的所有备选项
   * @param conn
   * @return
   * @throws Exception
   */
  public YHSysMenu getMenuList(Connection conn, String menuId) throws Exception {
    YHORM orm = new YHORM();
    try {
      Map<String,String> map = new HashMap<String,String>();
      map.put("MENU_ID", menuId);
      return (YHSysMenu)orm.loadObjSingle(conn, YHSysMenu.class, map);
    } catch(Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 设置桌面的信息保存到数据库
   * @param conn
   * @param map
   * @param userId
   * @throws Exception
   */
  public void setTheme(Connection conn, YHPerson user) throws Exception {
    try{
      YHORM orm = new YHORM();
      orm.updateSingle(conn, user);
    } catch(Exception ex) {
      throw ex;
    } finally {
      
    }
  }
  
  public boolean hasNickName(Connection conn, Map map, int userId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      int result = 0;
      String sql = "select" +
      		" count(1) as COUNT" +
      		" from PERSON" +
      		" where NICK_NAME = ? and SEQ_ID != ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, String.valueOf(map.get("nickName")));
      ps.setInt(2, userId);
      rs = ps.executeQuery();
      if (rs.next()){
        result = rs.getInt("COUNT");
      }
      
      return result > 0;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  
  /**
   * 设置昵称
   * @param conn
   * @param map
   * @param userId
   * @throws Exception
   */
  public boolean setAvatarNickName(Connection conn, Map map, int userId) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update person set" +
      " NICK_NAME = ?" +
      ",BBS_SIGNATURE = ?" +
      " where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      
      ps.setString(1, String.valueOf(map.get("nickName")));
      String bbs = String.valueOf(map.get("bbsSignature"));
      ps.setString(2, bbs);
      ps.setInt(3, userId);
      return ps.executeUpdate() > 0;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public void setPhoto(Connection conn, int userId, String photo) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "update PERSON set PHOTO = ? where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, photo);
      ps.setInt(2, userId);
      ps.executeUpdate();
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public void setAvatar(Connection conn, int userId, String avatar) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "update PERSON set AUATAR = ? where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, avatar);
      ps.setInt(2, userId);
      ps.executeUpdate();
      YHMsgPusher.updateOrg(conn);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取昵称
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public Map getAvatarNickName(Connection conn, int userId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      Map<String,String> map = new HashMap<String,String>();
      String sql = "select NICK_NAME" +
          ",AUATAR" +
          ",PHOTO" +
          ",BBS_SIGNATURE" +
          " from PERSON p" +
          " where SEQ_ID = ?";
    
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userId);
      rs = ps.executeQuery();
      return resultSet2Map(rs);
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 检查昵称是否重复   * @param conn
   * @param nickName
   * @return
   * @throws Exception
   */
  public int checkNickName(Connection conn, String nickName) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      int i = 0;
      String sql = "select count(1) as AMOUNT" +
      		" from PERSON" +
      		" where NICK_NAME = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, nickName);
      rs = ps.executeQuery();
      if(rs.next()){
        i = rs.getInt("AMOUNT");
      }
      return i;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 检查昵称是否重复   * @param conn
   * @param nickName
   * @return
   * @throws Exception
   */
  public Map<String,String> getAvatarConfig(Connection conn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    
    try {
      String sql = "select AVATAR_UPLOAD" +
      		",AVATAR_WIDTH" +
      		",AVATAR_HEIGHT" +
      		" from oa_inf";
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      Map<String,String> map = new HashMap<String,String>();
      
      if(rs.next()){
        map.put("avatar", YHSystemLogic.parseString(rs.getString("AVATAR_UPLOAD"), "0"));
        map.put("width", YHSystemLogic.parseString(rs.getString("AVATAR_WIDTH"), "20"));
        map.put("height", YHSystemLogic.parseString(rs.getString("AVATAR_HEIGHT"), "20"));
      }
      
      return map;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(st, rs, log);
    }
  }
  
  /**
   * 重置指定用户头像
   * @param conn
   * @param nickName
   * @return
   * @throws Exception
   */
  public void resetAvatar(Connection conn, int seqId) throws Exception {
    Statement st = null;
    try {
      String sql = "update person" +
      		" set AUATAR = ''" +
      		" where SEQ_ID = " + seqId;
      st = conn.createStatement();
      st.executeUpdate(sql);
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  /**
   * 重置指定用户头像
   * @param conn
   * @param nickName
   * @return
   * @throws Exception
   */
  public void resetPhoto(Connection conn, int seqId) throws Exception {
    Statement st = null;
    try {
      String sql = "update person" +
      " set PHOTO = ''" +
      " where SEQ_ID = " + seqId;
      st = conn.createStatement();
      st.executeUpdate(sql);
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  private Map resultSet2Map(ResultSet rs) throws SQLException {
    Map<String,String> map = new HashMap<String,String>();
    if(rs.next()){
      ResultSetMetaData rsMeta = rs.getMetaData();
      for(int i = 0; i < rsMeta.getColumnCount(); ++i){   
        String columnName = rsMeta.getColumnName(i+1);   
        map.put(rsMeta.getColumnName(i+1), null == rs.getString(columnName) ? "" : rs.getString(columnName).trim().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "")); 
     }
    }
    return map;
  }
}

package yh.core.funcs.setdescktop.shortcut.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.funcs.system.data.YHMenu;
import yh.core.util.db.YHDBUtility;

public class YHShortcutLogic {
  private static Logger log = Logger.getLogger(YHShortcutLogic.class);
  
  /**
   * 获取用户表的SHORTCUT
   * @param conn
   * @return
   * @throws Exception
   */
  public String getShortcut(Connection conn,int seqId) throws Exception{
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String shortCut = null;
      String sql = "select SHORTCUT" +
          " from PERSON" +
          " where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      if (rs.next()){
        shortCut = rs.getString("SHORTCUT");
      }
      return shortCut;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    
  }
  
  /**
   * 修改SHORTCUT
   * @param conn
   * @return
   * @throws Exception
   */
  public void modifyShortCut(Connection conn,int seqId,String shortCut) throws Exception{
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String sql = "update PERSON" +
      " set SHORTCUT = ?" +
      " where SEQ_ID = ?"; 
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, shortCut);
      ps.setInt(2, seqId);
      ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public Map<String,String> queryMenu(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String sql = "select MENU_ID" +
          ",MENU_NAME" +
          " from SYS_MENU" +
          " where MENU_ID = ?" +
          " order by MENU_ID";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      Map<String,String> menu = null;
      if (rs.next()){
        menu = new HashMap<String,String>();
        menu.put("id", parseString(rs.getString("MENU_ID")));
        menu.put("text", parseString(rs.getString("MENU_NAME")));
      }
      return menu;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 根据菜单id查询菜单信息(二级菜单,三级菜单)
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public Map<String,String> queryFunc(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String sql = "select MENU_ID" +
          ",FUNC_NAME" +
          " from oa_sys_func" +
          " where MENU_ID = ?" +
          " order by MENU_ID";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      Map<String,String> menu = null;
      if (rs.next()){
        menu = new HashMap<String,String>();
        menu.put("value", parseString(rs.getString("MENU_ID")));
        menu.put("text", parseString(rs.getString("FUNC_NAME")));
      }
      return menu;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 根据菜单id查询菜单信息(二级菜单,三级菜单)
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public Map<String,String> queryFunc4Module(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String sql = "select MENU_ID" +
      ",FUNC_NAME" +
      ",ICON" +
      ",OPEN_FLAG" +
      ",FUNC_CODE" +
      " from oa_sys_func" +
      " where MENU_ID = ?" +
      " order by MENU_ID";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      Map<String,String> menu = null;
      if (rs.next()){
        menu = new HashMap<String,String>();
        menu.put("value", parseString(rs.getString("MENU_ID")));
        menu.put("icon", parseString(rs.getString("ICON")));
        menu.put("url", parseString(rs.getString("FUNC_CODE")));
        menu.put("text", parseString(rs.getString("FUNC_NAME")));
      }
      return menu;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public YHMenu queryShortcut(Connection conn, String seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    if (YHUtility.isNullorEmpty(seqId)) {
      return null;
    }
    try{
      String sql = "select SEQ_ID" +
          ",MENU_ID" +
          ",FUNC_NAME" +
          ",FUNC_CODE" +
          ",ICON" +
          ",OPEN_FLAG" +
          " from oa_sys_func" +
          " where MENU_ID = ?" +
          " order by MENU_ID";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, seqId.trim());
      rs = ps.executeQuery();
      
      YHMenu menu = null;
      if (rs.next()){
        menu = new YHMenu();    
        menu.setSeqId(rs.getInt("SEQ_ID"));
        menu.setId(parseString(rs.getString("MENU_ID")));
        menu.setText(parseString(rs.getString("FUNC_NAME")));

        menu.setUrl(parseString(rs.getString("FUNC_CODE")));
        menu.setIcon(parseString(rs.getString("ICON")));
        menu.setOpenFlag(parseString(rs.getString("OPEN_FLAG"), "0"));
      }
      return menu;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  private String parseString(String s){
    if (s == null){
      return "";
    }
    else {
      return s.trim();
    }
  }
  
  private String parseString(String s, String defaultValue){
    if (s == null || s.trim().equals("")){
      return defaultValue;
    }
    else {
      return s.trim();
    }
  }
  
  /**
   * ResultSet转化为List<Map>
   * @param rs
   * @return
   * @throws SQLException
   */
  private List<Map> resultSet2List(ResultSet rs) throws SQLException{
    List<Map> list = new ArrayList<Map>();
    ResultSetMetaData rsMeta = rs.getMetaData();
    while(rs.next()){
      Map<String,String> map = new HashMap<String,String>();
      for(int i = 0; i < rsMeta.getColumnCount(); ++i){   
        String columnName = rsMeta.getColumnName( i + 1 );   
        map.put(rsMeta.getColumnName(i+1), null == rs.getString(columnName) ? "" : rs.getString(columnName)); 
      }
      list.add(map);
    }
    return list;
  }
}

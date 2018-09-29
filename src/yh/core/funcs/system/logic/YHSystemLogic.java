package yh.core.funcs.system.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserOnline;
import yh.core.funcs.system.accesscontrol.data.YHIpRule;
import yh.core.funcs.system.data.YHLoginUsers;
import yh.core.funcs.system.data.YHMenu;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;

public class YHSystemLogic {
  
  private static Logger log = Logger.getLogger(YHSystemLogic.class);
  
  /**
   * 根据菜单id查询菜单信息(一级菜单)
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHMenu queryMenu(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String sql = "select SEQ_ID" +
          ",MENU_ID" +
          ",MENU_NAME" +
          ",IMAGE" +
          " from SYS_MENU" +
          " where MENU_ID = ?" +
          " order by MENU_ID";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      YHMenu menu = null;
      if (rs.next()){
        menu = new YHMenu();
        menu.setId(parseString(rs.getString("MENU_ID")));
        menu.setText(parseString(rs.getString("MENU_NAME")));
        menu.setSeqId(rs.getInt("SEQ_ID"));
        menu.setIcon(parseString(rs.getString("IMAGE")));
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
  public YHMenu queryFunc(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
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
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      YHMenu menu = null;
      if (rs.next()){
        menu = new YHMenu();
        menu.setId(parseString(rs.getString("MENU_ID")));
        menu.setText(parseString(rs.getString("FUNC_NAME")));
        menu.setSeqId(rs.getInt("SEQ_ID"));
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
  
  /**
   * 查询一些登陆时需要的系统参数
   * @param conn
   * @return
   * @throws Exception
   */
  public Map<String,String> getSysPara(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    
    try{
      String sql = "select PARA_VALUE" +
      		",PARA_NAME" +
      		" from SYS_PARA" +
          " where PARA_NAME in" +
          
          //密码过期时间
          " ('SEC_PASS_TIME'" +
          
          //密码是否过期
          ",'SEC_PASS_FLAG'" +
          
          //是否限制错误登陆
          ",'SEC_RETRY_BAN'" +
          
          //重复登陆次数
          ",'SEC_RETRY_TIMES'" +
          
          //多少分钟内禁止再次登陆
          ",'SEC_BAN_TIME'" +
          
          //密码最小长度
          ",'SEC_PASS_MIN'" +
          
          //密码最大长度
          ",'SEC_PASS_MAX'" +
          
          //密码是否需要同时包含字母和数字
          ",'SEC_PASS_SAFE'" +
          
          //登陆时记忆用户名
          ",'SEC_USER_MEM'" +
          
          //是否将用户在线状态记录到PERSON表中
          ",'SEC_ON_STATUS'" +
          
          //Ip规则不限制的用户
          ",'IP_UNLIMITED_USER'" +
          
          //是否启用USBKey
          ",'LOGIN_KEY'" +
          
          //启用USBKey是否需要输入用户名
          ",'SEC_KEY_USER'" +
          
          //是否修改初始的密码          ",'SEC_INIT_PASS'" +
          //是否使用验证码
          ",'VERIFICATION_CODE'" +
          //缺省界面风格(webos/经典界面)
          ",'DEFAULT_INTERFACE_STYLE')";
      
      sm = conn.createStatement();
      System.out.println("sql:"+sql);
      rs = sm.executeQuery(sql);
      
      Map<String,String> map = new HashMap<String,String>();
      while (rs.next()){
        String value = parseString(rs.getString("PARA_VALUE"));
        String name = parseString(rs.getString("PARA_NAME"));
        
        map.put(name, value);
      }
      
      return map;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  /**
   * 查询菜单展开方式的系统参数
   * @param conn
   * @return
   * @throws Exception
   */
  public int queryMenuExpandType(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    
    try{
      String sql = "select PARA_VALUE" +
          ",PARA_NAME" +
          " from SYS_PARA" +
          " where PARA_NAME = 'MENU_EXPAND_SINGLE'";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      
      int type = 0;
      
      if (rs.next()){
        String value = rs.getString("PARA_VALUE");
        if (value == null){
          value = "0";
        }
        type = Integer.parseInt(value.trim());
      }
      return type;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  /**
   * 获取Ip登陆规则
   * @param conn
   * @return
   * @throws Exception
   */
  public List getIpRule(Connection conn) throws Exception{
    
    try{
      YHORM orm = new YHORM();
      
      Map<String,String> map = new HashMap<String,String>();
      
      map.put("TYPE", "0");
      
      return orm.loadListSingle(conn, YHIpRule.class, map);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  /**
   * 登陆成功后将用户信息加入USER_ONLINE表中
   * 其中SESSION_TOKEN代替USER_ID作为标识
   * @param conn
   * @param online
   * @throws Exception
   */
  public void addOnline(Connection conn, YHUserOnline online) throws Exception{
    try{
      YHORM orm = new YHORM();
      orm.saveSingle(conn, online);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  /**
   * 用户退出时,在USER_ONLINE中删除用户信息
   * 通过SESSION_TOKEN索引
   * @param conn
   * @param sessionToken
   * @throws Exception
   */
  public void deleteOnline(Connection conn, String sessionToken) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "delete" +
      		" from oa_online" +
      		" where SESSION_TOKEN = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, sessionToken);
      
      ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 通过USER_ID和BYNAME查询用户(使用别名登陆)
   * @param conn
   * @param userName
   * @return
   * @throws Exception
   */
  public YHPerson queryPerson(Connection conn, String userName) throws Exception{
    
    try{
      
      YHORM orm = new YHORM();
      
      String[] filters = new String[]{"USER_ID = '" + userName + "' or BYNAME = '" + userName + "'"};
      List<YHPerson> list = orm.loadListSingle(conn, YHPerson.class, filters);
      
      YHPerson person = null;
      if (list.size() > 0){
        person = list.get(0);
      }
      
      return person;
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  /**
   * 通过用户id查询绑定的用户id
   * @param conn
   * @param userName
   * @return
   * @throws Exception
   */
  public String queryBindId(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String otherId = null;
      String sql = "select USER_ID_OTHER" +
      		" from oa_bind_persons" +
      		" where USER_SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      
      rs = ps.executeQuery();
      
      if (rs.next()){
        otherId = rs.getString("USER_ID_OTHER");
        
        if (otherId == null || "".equals(otherId.trim())){
          otherId = null;
        }
      }
      
      return otherId;
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  
  /**
   * LOGIN_USERS中添加数据
   * @param conn
   * @param userName
   * @return
   * @throws Exception
   */
  public void addLoginUser(Connection conn, YHLoginUsers loginUsers) throws Exception{
    Statement sm = null;
    try{
      long currTime = System.currentTimeMillis();
      long timeTo = currTime - (2 * YHConst.DT_MINIT);
      Date dtTo = new Date(timeTo);
      String timeStr = YHDBUtility.getDateFilter("LOGIN_TIME", YHUtility.getDateTimeStr(dtTo), "<");
      String sql = "delete from" +
          " oa_login_persons" +
          " where " + timeStr;
      
      sm = conn.createStatement();
      sm.executeUpdate(sql);
      
      YHORM orm = new YHORM();
      orm.saveSingle(conn, loginUsers);
            
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, null, log);
    }
  }
  
  
  /**
   * LOGIN_USERS中添加数据
   * @param conn
   * @param userName
   * @return
   * @throws Exception
   */
  public int queryUserOnline(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String sql = "select distinct USER_ID,USER_STATE from oa_online where user_id = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      String userState = "";
      if (rs.next()){
        userState = rs.getString("USER_STATE");
      }
      
      if (userState == null){
        userState = "";
      }
      
      return Integer.parseInt(userState.trim());
    }catch(NumberFormatException ex){
      return -1;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 通过密码错误日志验证用户错误登陆次数
   * 考虑了xx分钟内,登陆成功过的情况
   * @param conn
   * @param times                     系统设置的错误登陆次数   * @param minutes                   系统设置的xx分钟内不能再次登陆   * @param seqId
   * @param ip
   * @return
   * @throws Exception
   */
  public boolean retryLogin(Connection conn, int times, int minutes, int seqId, String ip) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      long currTime = System.currentTimeMillis();
      long timeTo = currTime - (minutes * YHConst.DT_MINIT);
      Date dtTo = new Date(timeTo);
      String timeStr = YHDBUtility.getDateFilter("TIME", YHUtility.getDateTimeStr(dtTo), ">");
      
      String sql = "select TYPE from" +
          " oa_sys_log" +
          " where USER_ID = ?" +
          " and IP = ?" +
          " and TYPE in ( ?, ?)" +
          " and " + timeStr +
          " order by TIME DESC";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      ps.setString(2, ip);
      ps.setString(3, YHLogConst.LOGIN_PASSWORD_ERROR);
      ps.setString(4, YHLogConst.LOGIN);
      rs = ps.executeQuery();
      
      int errorTimes = 0;
      while (rs.next()){
        String type = YHSystemLogic.parseString(rs.getString("TYPE"));
        if (YHLogConst.LOGIN_PASSWORD_ERROR.equals(type)) {
          errorTimes++;
        }
        if (errorTimes >= times) {
          return false;
        }
        if (YHLogConst.LOGIN.equals(type)) {
          return true;
        }
      }
      
      return true;
    } catch(Exception ex) {
      throw ex;
    } finally {
    	YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 从interface表中查询主题,当interface中设置用户可选主题时返回0
   * @return
   * @throws Exception
   */
  public static int getStyleIndex(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    int index = 0;
    try {
      String sql = "select THEME" +
      		" from oa_inf" +
      		" where THEME_SELECT = '0'";
      
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      
      
      if (rs.next()) {
        //处理THEME字段
        index = Integer.parseInt(parseString(rs.getString("THEME"), "0"));
      }
    } catch (NumberFormatException ex) {
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(st, rs, log);
    }
    return index;
  }
  
  /**
   * 从interface表中查询主题,当interface中设置用户可选主题时返回0
   * @return
   * @throws Exception
   */
  public static int getStyleOA(Connection dbConn, YHPerson person) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    int index = 0;
    try {
      String sql = "select THEME from oa_inf where THEME_SELECT = '0'";
      
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      
      
      if (rs.next()) {
        //处理THEME字段
        index = Integer.parseInt(parseString(rs.getString("THEME"), "0"));
      }
    } catch (NumberFormatException ex) {
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(st, rs, log);
    }
    return index;
  }
  
  /**
   * 从interface表中查询主题,当interface中设置用户可选主题时返回0
   * @return
   * @throws Exception
   */
  public static String queryTemplate(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    
    try {
      String sql = "select TEMPLATE" +
      " from oa_inf";
      
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      
      String template = null;
      if (rs.next()) {
        template = parseString(rs.getString("TEMPLATE"), "default");
      }
      return template;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(st, rs, log);
    }
  }
  
  
  /**
   * 判断指定用户是否已经登录
   * @return
   * @throws Exception
   */
  public boolean isLogin(Connection dbConn, int seqId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "select count(1) as COUNT" +
      " from oa_online" +
      " where USER_ID = ?";
      
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      if (rs.next()) {
        return rs.getInt("COUNT") > 0;
      }
      return false;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取登陆界面背景图片
   * @return
   * @throws Exception
   */
  public String getLoginBg(Connection dbConn) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "select ATTACHMENT_ID1" +
      		",ATTACHMENT_NAME1"+
      		" from oa_inf";
      
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      String path = "";
      
      if (rs.next()) {
        String id = parseString(rs.getString("ATTACHMENT_ID1"));
        String image = parseString(rs.getString("ATTACHMENT_NAME1"));
        path = id + System.getProperty("file.separator") + image;
      }
      
      return path;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取title
   * @return
   * @throws Exception
   */
  public String getIETitle(Connection dbConn) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "select IE_TITLE" +
      " from oa_inf";
      
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      String ieTitle = null;
      
      if (rs.next()) {
        ieTitle = rs.getString("IE_TITLE");
      }
      
      return ieTitle;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取title
   * @return
   * @throws Exception
   */
  public void updateLastVisitInfo(Connection dbConn, int seqId, String ip) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = null;
      if (YHUtility.isNullorEmpty(ip)) {
        sql = "update PERSON" +
        " set LAST_VISIT_TIME = " + YHDBUtility.currDateTime() + 
        " where SEQ_ID = ?";
      }
      else {
        sql = "update PERSON" +
        " set LAST_VISIT_TIME = " + YHDBUtility.currDateTime() + 
        ",LAST_VISIT_IP = '" + ip + "'" +
        " where SEQ_ID = ?";
      }
      
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, seqId);
      ps.executeUpdate();
      
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 加载注册信息需要的：单位名称、软件序列号
   * @param conn
   * @return
   * @throws Exception
   */
  public static List<String> loadRegistRequires(Connection conn) throws Exception{
    List<String> list = new ArrayList<String>();
    list.add(getUnitSN(conn));
    list.add(getUnitName(conn));
    return list;
  }
  
  private static String getUnitName(Connection conn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    try{
      String sql = "select UNIT_NAME from oa_organization";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        String name = rs.getString(1);
        if (!YHUtility.isNullorEmpty(name)) {
          return name;
        }
      }
      return null;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  private static String getUnitSN(Connection conn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    try{
      stmt = conn.createStatement();
      String sql = "select SN from version";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        String serialId = rs.getString(1);
        if (!YHUtility.isNullorEmpty(serialId)) {
          return serialId;
        }
      }
      return "";
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public static String parseString(String s){
    if (s == null){
      return "";
    }
    else {
      return s.trim();
    }
  }
  
  public static String parseString(String s, String defaultValue){
    if (s == null || s.trim().equals("")){
      return defaultValue;
    }
    else {
      return s.trim();
    }
  }
  
}

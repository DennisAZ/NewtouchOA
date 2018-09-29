package yh.core.funcs.setdescktop.setports.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;

public class YHDesktopDefineLogic {
  private static Logger log = Logger.getLogger(YHDesktopDefineLogic.class);

  /**
   * 操作SYS_PARA表,获取DESKTOP_SELF_DEFINE的五种属性值
   * @param conn
   * @return
   * @throws Exception
   */
  public String getDesktopProperties(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    String properties = null;
    String width = null;
    try{
      String sql = "";
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        sql = "select (select cast(PARA_VALUE as nvarchar) from SYS_PARA where PARA_NAME = 'DESKTOP_SELF_DEFINE') as DESKTOP_SELF_DEFINE" +
              ",(select cast(PARA_VALUE as nvarchar) from SYS_PARA where PARA_NAME = 'DESKTOP_LEFT_WIDTH') as DESKTOP_LEFT_WIDTH";
     
      }else if (dbms.equals("mysql")) {
        sql = "select (select PARA_VALUE from SYS_PARA where PARA_NAME = 'DESKTOP_SELF_DEFINE') as DESKTOP_SELF_DEFINE" +
              ",(select PARA_VALUE from SYS_PARA where PARA_NAME = 'DESKTOP_LEFT_WIDTH') as DESKTOP_LEFT_WIDTH";
      
      }else if (dbms.equals("oracle")) {
        sql = "select (select PARA_VALUE from SYS_PARA where PARA_NAME = 'DESKTOP_SELF_DEFINE') as DESKTOP_SELF_DEFINE" +
              ",(select PARA_VALUE from SYS_PARA where PARA_NAME = 'DESKTOP_LEFT_WIDTH') as DESKTOP_LEFT_WIDTH" +
              " from dual";
      
      }else {
        throw new SQLException("not accepted dbms");
      }
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      if(rs.next()){
        properties = rs.getString("DESKTOP_SELF_DEFINE");
        width = rs.getString("DESKTOP_LEFT_WIDTH");
      }
        
      if(properties == null||"".equals(properties)){
        return "{}";
      }
      List<String> list = Arrays.asList(properties.split(","));
        
      StringBuffer sb = new StringBuffer();
        
      sb.append("{POS:\"");
      sb.append(list.contains("POS")?1:0);
      sb.append("\"");
      sb.append(",WIDTH:\"");
      sb.append(list.contains("WIDTH")?1:0);
      sb.append("\"");
      sb.append(",LINES:\"");
      sb.append(list.contains("LINES")?1:0);
      sb.append("\"");
      sb.append(",SCROLL:\"");
      sb.append(list.contains("SCROLL")?1:0);
      sb.append("\"");
      sb.append(",EXPAND:\"");
      sb.append(list.contains("EXPAND")?1:0);
      sb.append("\"");
      sb.append(",LEFT_WIDTH:\"");
      sb.append(width);
      sb.append("\"");
      sb.append("}");
      
      return sb.toString();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }

  /**
   * 操作SYS_PARA表,设置桌面属性
   * @param conn
   * @param map
   * @throws Exception
   */
  public void setDesktopProperties(Connection conn,Map<String,String> map)throws Exception{
    
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    PreparedStatement ps3 = null;
    
    StringBuffer desktopSelfDefine = new StringBuffer();
    desktopSelfDefine.append(map.get("DESKTOP_POS"));
    desktopSelfDefine.append(map.get("DESKTOP_WIDTH"));
    desktopSelfDefine.append(map.get("DESKTOP_LINES"));
    desktopSelfDefine.append(map.get("DESKTOP_SCROLL"));
    desktopSelfDefine.append(map.get("DESKTOP_EXPAND"));
    
    try{
      String updateSelfDefine = "update SYS_PARA set" +
      		" PARA_VALUE = ?" +
      		" where PARA_NAME = 'DESKTOP_SELF_DEFINE'";
      
      String updateLeftWidth = "update SYS_PARA set" +
              " PARA_VALUE = ?" +
              " where PARA_NAME = 'DESKTOP_LEFT_WIDTH'";
      
      String updateMytable = "update oa_mytable set" +
      		" MODULE_SCROLL = ?" +
      		",MODULE_LINES = ?";
      
      //修改DESKTOP_SELF_DEFINE
      ps1 = conn.prepareStatement(updateSelfDefine);
      ps1.setString(1, desktopSelfDefine.toString());
      ps1.execute();
      
      //修改DESKTOP_LEFT_WIDTH
      ps2 = conn.prepareStatement(updateLeftWidth);
      ps2.setString(1, (String)map.get("DESKTOP_LEFT_WIDTH"));
      ps2.execute();
      
      //批量修改mytable属性
      if("on".equals((String)map.get("DESKTOP_MODULE_SET_ALL"))){
        ps3 = conn.prepareStatement(updateMytable);
        //System.out.println((String)map.get("DESKTOP_MODULE_SCROLL"));
        ps3.setString(1, "on".equals((String)map.get("DESKTOP_MODULE_SCROLL"))?"1":"0");
        ps3.setString(2, (String)map.get("DESKTOP_MODULE_LINES"));
        ps3.execute();
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps1, null, log);
      YHDBUtility.close(ps2, null, log);
      YHDBUtility.close(ps3, null, log);
    }
  }

  /**
   * 查询用户的桌面项,添加至登陆用户的session中
   * @param conn
   * @param user
   * @throws Exception
   */
  public void setDesktopConfigToUser(Connection conn,YHPerson user) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
        
      String sql = "select MYTABLE_LEFT" +
      		",MYTABLE_RIGHT" +
      		" from PERSON" +
      		" where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, user.getSeqId());
      rs = ps.executeQuery();
      if(rs.next()){
        user.setMytableLeft(rs.getString("MYTABLE_LEFT"));
        user.setMytableRight(rs.getString("MYTABLE_RIGHT"));
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, log);
  }
}

  /**
   * 设置用户桌面显示项   * @param conn
   * @param user
   * @throws Exception
   */
  public void setUserMytable(Connection conn,YHPerson user) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update PERSON set" +
    		" MYTABLE_LEFT = ?" +
    		",MYTABLE_RIGHT = ?" +
    		" where SEQ_ID = ?";
        
      ps = conn.prepareStatement(sql);
      ps.setString(1, user.getMytableLeft());
      ps.setString(2, user.getMytableRight());
      ps.setInt(3, user.getSeqId());
      ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
  }
  
  
  /**
   * 设置我的应用到其他用户   * @param conn
   * @param user
   * @throws Exception
   */
  public void setMineToOthers(Connection conn,int seqId, YHPerson person) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update PERSON set" +
      " MYTABLE_LEFT = ?" +
      ",MYTABLE_RIGHT = ?" +
      " where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, person.getMytableLeft());
      ps.setString(2, person.getMytableRight());
      ps.setInt(3, seqId);
      ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, null, log);
    }
  }
}

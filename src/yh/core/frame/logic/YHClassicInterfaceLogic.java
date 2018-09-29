package yh.core.frame.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHClassicInterfaceLogic {
  private static Logger log = Logger.getLogger(YHClassicInterfaceLogic.class);
  
  /**
   * 获取title
   * @return
   * @throws Exception
   */
  public Map<String, String> getInterfaceInfo(Connection dbConn) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "select IE_TITLE" +
      		",BANNER_TEXT" +
      		",BANNER_FONT" +
      		",ATTACHMENT_ID" +
      		" from oa_inf";
      
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      if (rs.next()) {
        Map<String, String> map = new HashMap<String, String>();
        String ieTitle = YHUtility.encodeSpecial(rs.getString("IE_TITLE"));
        String bannerText = YHUtility.encodeSpecial(rs.getString("BANNER_TEXT"));
        String bannerFont = YHUtility.encodeSpecial(rs.getString("BANNER_FONT"));
        map.put("title", ieTitle);
        if (!YHUtility.isNullorEmpty(bannerText)) {
          String attachId = rs.getString("ATTACHMENT_ID");
          if (YHUtility.isNullorEmpty(attachId)) {
            map.put("hideLogo", "1");
          }
          map.put("bannerText", bannerText);
          map.put("bannerFont", bannerFont);
        }
        return map;
      }
      return null;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public Map<String, String> queryInfo(Connection dbConn, YHPerson user) throws Exception {
    int userPriv = 0;
    try {
      userPriv = Integer.parseInt(user.getUserPriv()); 
    } catch (NumberFormatException e) {
      
    }
    Map<String,String> map = new HashMap<String,String>();
    
    map.put("deptName", queryDeptName(dbConn, user.getDeptId()));
    map.put("privName", queryPrivName(dbConn, userPriv));
    map.put("userName", user.getUserName());
    map.put("onLine", String.valueOf(user.getOnLine()));
    map.put("onStatus", user.getOnStatus());
    map.put("myStatus", user.getMyStatus());
    map.put("avatar", user.getAuatar());
    map.put("notViewTable", user.getNotViewTable());
    map.put("notViewUser", user.getNotViewUser());
    map.put("panel", user.getPanel());
    map.put("smsOn", user.getSmsOn());
    map.put("sex", user.getSex());
    map.put("menuType", user.getMenuType());
    map.put("callSound", user.getCallSound());
    map.put("seqId", String.valueOf(user.getSeqId()));
    map.put("nevMenuOpen", user.getNevMenuOpen());
    map.put("menuExpand", user.getMenuExpand());
    map.putAll(YHFOM.json2Map(user.getParamSet()));
    return map;
  }
  
  public String queryDeptName(Connection conn, int seqId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select DEPT_NAME" +
          " from oa_department" +
          " where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      String name = null;
      if (rs.next()) {
        name = rs.getString("DEPT_NAME");
      }
      return name;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public String queryPrivName(Connection conn, int seqId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select PRIV_NAME" +
      " from USER_PRIV" +
      " where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      String name = null;
      if (rs.next()) {
        name = rs.getString("PRIV_NAME");
      }
      return name;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
}

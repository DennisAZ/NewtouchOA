package yh.core.frame.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.system.interfaces.logic.YHInterFacesLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHWebosLogic {
  private static Logger log = Logger.getLogger(YHWebosLogic.class);
  
  /**
   * 获取title
   * @return
   * @throws Exception
   */
  public Map<String, String> getBannerInfo(Connection dbConn) throws Exception {
    YHInterFacesLogic logic = new YHInterFacesLogic();
    String logo = logic.queryWebOSLOGO(dbConn);
    String hideLogo = "1";
    if (!YHUtility.isNullorEmpty(logo)) {
      hideLogo = "0";
    }
    Map<String, String> map = getBannerText(dbConn);
    if (map != null) {
      map.put("hideLogo", hideLogo);
    }
    return map;
  }
  
  private  Map<String, String> getBannerText(Connection dbConn) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      String sql = "select " +
          "BANNER_TEXT" +
          ",BANNER_FONT" +
          " from oa_inf";
      
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      if (rs.next()) {
        Map<String, String> map = new HashMap<String, String>();
        String bannerText = YHUtility.encodeSpecial(rs.getString("BANNER_TEXT"));
        String bannerFont = YHUtility.encodeSpecial(rs.getString("BANNER_FONT"));
        if (YHUtility.isNullorEmpty(bannerText)) {
          return null;
        }
        map.put("bannerText", bannerText);
        map.put("bannerFont", bannerFont);
        return map;
      }
      return null;
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
}

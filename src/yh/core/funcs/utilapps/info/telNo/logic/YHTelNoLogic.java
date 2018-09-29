package yh.core.funcs.utilapps.info.telNo.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class YHTelNoLogic {

  public String getProvince(Connection conn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    try{
      String sql = " select province from oa_post_tel_num pt "
                 + " where pt.no<61811 "
                 + " group by province ";
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      List list = new ArrayList();
      while(rs.next()){
        list.add(rs.getString("province"));
      }
      return list.toString();
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  public String getArea(Connection conn) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    try{
      String sql = " select province from oa_post_tel_num pt "
                 + " where pt.no>=61811 "
                 + " group by province ";
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      List list = new ArrayList();
      while(rs.next()){
        list.add(rs.getString("province"));
      }
      return list.toString();
    }catch(Exception ex) {
      throw ex;
    }
  } 
  
  
}

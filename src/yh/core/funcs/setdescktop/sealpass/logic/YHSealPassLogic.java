package yh.core.funcs.setdescktop.sealpass.logic;

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

import yh.core.util.db.YHORM;
import yh.core.funcs.system.sealmanage.data.YHSeal;
import yh.core.util.db.YHDBUtility;

public class YHSealPassLogic {
  private static Logger log = Logger.getLogger(YHSealPassLogic.class);

  /**
   * 获取印章信息
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHSeal> getSealInfo(Connection conn, int seqId) throws Exception{
    
    try{
      YHORM orm = new YHORM();
      
      String[] filter = new String[]{
          YHDBUtility.findInSet(String.valueOf(seqId), "USER_STR")
      };
      
      return orm.loadListSingle(conn, YHSeal.class, filter);
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  /**
   * 获取有权修改印章的所有用户的用户名
   * @param conn
   * @param seqIds
   * @return
   * @throws Exception
   */
  public String getSealUserStr(Connection conn,String seqIds) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String result = "";
      String sql = "select USER_NAME from PERSON where SEQ_ID in " + seqIds;
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      while(rs.next()){
        result += rs.getString("USER_NAME") == null ? "" : rs.getString("USER_NAME") + ",";
      }
      return result;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 修改印章信息
   * @param conn
   * @param data
   * @param seqId
   * @return
   * @throws Exception
   */
  public int updateSealData(Connection conn, String data, String sealId) throws Exception{
    PreparedStatement ps = null;
    
    try{
      String result = "";
      String sql = "update SEAL set SEAL_DATA = ?" +
            " where SEAL_ID = ? ";
      ps = conn.prepareStatement(sql);
      ps.setString(1, data);
      ps.setString(2, sealId);
      return ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 增加印章的Log信息
   * @param conn
   * @param sid
   * @param type
   * @param ip
   * @param seqId
   * @return
   * @throws Exception
   */
  public int updateSealLog(Connection conn, String sid, String type, String ip, int seqId) throws Exception{
    PreparedStatement ps = null;
    
    try{
      String sql = "insert into oa_seal_log" +
      		" (S_ID,LOG_TYPE,LOG_TIME,RESULT,IP_ADD,USER_ID)" +
      		" VALUES" +
          " ( ?, ?, " +
          YHDBUtility.currDateTime() +
          ", '修改印章密码成功', ?, ?)";
      ps = conn.prepareStatement(sql);
      ps.setString(1, sid);
      ps.setString(2, type);
      ps.setString(3, ip);
      ps.setInt(4, seqId);
      return ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 查询有权限修改该印章的用户id的字符串
   * @param conn
   * @param sid
   * @return
   * @throws Exception
   */
  public String getUserList(Connection conn, String sid) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String str = "";
      String sql = "select USER_STR from SEAL where SEAL_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, sid);
      rs = ps.executeQuery();
      if(rs.next()){
        str = rs.getString("USER_STR");
      }
      return str;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  
  private List<Map> resultSet2List(ResultSet rs) throws SQLException{
    List<Map> list = new ArrayList<Map>();
    ResultSetMetaData rsMeta = rs.getMetaData();
    while(rs.next()){
      Map<String,String> map = new HashMap<String,String>();
      for(int i = 0; i < rsMeta.getColumnCount(); ++i){   
        String columnName = rsMeta.getColumnName(i+1);   
        map.put(rsMeta.getColumnName(i+1), null == rs.getString(columnName)?"":rs.getString(columnName)); 
      }
      list.add(map);
    }
    return list;
  }
  
}

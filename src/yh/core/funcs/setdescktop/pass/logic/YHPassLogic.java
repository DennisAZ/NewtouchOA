package yh.core.funcs.setdescktop.pass.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHPassLogic {
  private static Logger log = Logger.getLogger(YHPassLogic.class);

  /**
   * 获取系统参数
   * @param conn
   * @return
   * @throws Exception
   */
  public List<String> getSysPara(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    
    try{
      List<String> list = new ArrayList<String>();
      String sql = "select PARA_VALUE" +
      		",PARA_NAME" +
      		" from SYS_PARA" +
      		" where PARA_NAME in" +
      		" ('SEC_PASS_MAX','SEC_PASS_MIN','SEC_PASS_FLAG','SEC_PASS_SAFE','SEC_PASS_SAFE_SC', 'SEC_PASS_TIME','SEC_PASS_TIME')";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      
      while(rs.next()){
        String name = rs.getString("PARA_NAME");
        String value = rs.getString("PARA_VALUE");
        if (YHUtility.isNullorEmpty(name)){
          name = "";
        }
        
        if (YHUtility.isNullorEmpty(value)){
          value = "";
        }
        
        list.add(name + ":\"" + value + "\"");
      }
      return list;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  /**
   * 获取最后修改密码的时间
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getLastPassTime(Connection conn,int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String time = null;
      String sql = "select LAST_PASS_TIME as LAST_PASS_TIME from PERSON where SEQ_ID = ? ";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      if(rs.next()){
        time = rs.getString("LAST_PASS_TIME");
      }
      return time;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 修改密码
   * @param conn
   * @param seqId
   * @param pw
   * @param formPw
   * @return
   * @throws Exception
   */
  public boolean updatePassWord(Connection conn, int seqId, String pw) throws Exception{
    PreparedStatement ps = null;
    
    try{
      String sql = "update PERSON set" +
      		" LAST_PASS_TIME = " + 
      		YHDBUtility.currDateTime() +
      		",PASSWORD = ?" +
      		" where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, pw);
      ps.setInt(2, seqId);
      return ps.executeUpdate() > 0;
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 判断密码是否正确(当密码为空时认为无论用户输入什么密码都正确)
   * @param conn
   * @param seqId
   * @param pw
   * @return
   * @throws Exception
   */
  public int checkPassWord(Connection conn, int seqId, String pw) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      int result = 0;
      String sql = "select count(1) as amount" +
      		" from PERSON" +
      		" where (PASSWORD = ?" +
      		" or PASSWORD is null)" +
      		" and SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, pw);
      ps.setInt(2, seqId);
      rs = ps.executeQuery();
      if(rs.next()){
        result = rs.getInt("amount");
      }
      return result;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 获取修改密码的记录,最后10条
   * @param conn
   * @param seqId
   * @param type
   * @return
   * @throws Exception
   */
  public List<Map> getSysLog(Connection conn,int seqId , String type) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String time = null;
      
      String sql = "";
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")){
        sql = "SELECT top 10 SEQ_ID" +
        ",IP" +
        ",(select USER_NAME from PERSON where SEQ_ID = s.USER_ID) as USER_ID" +
        ",TIME" +
        ",'修改登录密码' as TYPE" +
        ",REMARK" +
        " from oa_sys_log s" +
        " where TYPE= ?" +
        " and USER_ID= ?" +
        " order by TIME desc";
      }
      else if (dbms.equals("mysql")){
        sql = "SELECT SEQ_ID" +
        ",IP" +
        ",(select USER_NAME from PERSON where SEQ_ID = s.USER_ID) as USER_ID" +
        ",TIME" +
        ",'修改登录密码' as TYPE" +
        ",REMARK" +
        " from oa_sys_log s" +
        " where TYPE= ?" +
        " and USER_ID= ?" +
        " order by TIME desc" +
        " limit 10";
      }else if (dbms.equals("oracle")){
        sql = "SELECT SEQ_ID" +
        ",IP" +
        ",(select USER_NAME from PERSON where SEQ_ID = s.USER_ID) as USER_ID" +
        ",TIME" +
        ",'修改登录密码' as TYPE" +
        ",REMARK" +
        " from oa_sys_log s" +
        " where TYPE= ?" +
        " and USER_ID= ?" +
        " and rownum <= 10" +
        " order by TIME desc";
      }
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, type);
      ps.setInt(2, seqId);
      rs = ps.executeQuery();
      
      return this.resultSet2List(rs);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
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
        String columnName = rsMeta.getColumnName(i+1);   
        map.put(rsMeta.getColumnName(i+1), null == rs.getString(columnName)?"":rs.getString(columnName)); 
      }
      list.add(map);
    }
    return list;
  }
  
}

package yh.subsys.oa.addworkfee.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.addworkfee.data.YHRoleBaseFee;

/**
 * 角色加班费基数表
 * @author Administrator
 *
 */
public class YHRoleBaseFeeLogic{
  
  /**
   * 增加一个加班费基数
   * @param dbConn
   * @param abf
   * @param roleId
   * @return
   * @throws Exception
   */
  public int  addYHRoleBaseFee(Connection dbConn, YHRoleBaseFee abf, int roleId) throws Exception{
    String sql = " insert into oa_add_work_cost(ROLE_ID, NORMAL_ADD, FESTIVAL_ADD, WEEK_ADD, BASE_ADD) values(?, ?, ?, ?, ?)";
    PreparedStatement ps = null;
    int k = 0;
  try {
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, roleId);
      ps.setDouble(2, abf.getNormalAdd());
      ps.setDouble(3, abf.getFestivalAdd());
      ps.setDouble(4, abf.getWeekAdd());
      ps.setDouble(5, abf.getBaseAdd());
      k = ps.executeUpdate();
  } catch (Exception ex) {
    throw ex;
  } finally {
     YHDBUtility.close(ps, null, null);
  }
    return k;
  }
  
  /**
   * 增加一个加班费基数
   * @param dbConn
   * @param abf
   * @throws Exception
   * @throws Throwable
   */
  public void addYHRoleBaseFee(Connection dbConn, YHRoleBaseFee abf) throws Exception, Throwable{
    String roleIds = abf.getRoleIds();
    String[] ids = roleIds.split(",");
    for(int i=0; i<ids.length; i++){
      addYHRoleBaseFee(dbConn, abf, Integer.parseInt(ids[i]));
    }
  }
  
  public int delYHRoleBaseFee(){
    return 0;
  }
  
  public int changeYHRoleBaseFee(){
    return 0;
  }
  
  /**
   * 查找所有的加班费基数
   * @param dbConn
   * @return
   * @throws SQLException
   */
  public List<YHRoleBaseFee> findYHRoleBaseFeeList(Connection dbConn) throws SQLException{
    String sql = "select awf.SEQ_ID, awf.ROLE_ID, awf.NORMAL_ADD, awf.FESTIVAL_ADD, awf.WEEK_ADD,awf.BASE_ADD, up.PRIV_NAME " +
    		"         from oa_add_work_cost awf, USER_PRIV up where awf.ROLE_ID = up.SEQ_ID" ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHRoleBaseFee> dosc = new ArrayList<YHRoleBaseFee>();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHRoleBaseFee fest = new YHRoleBaseFee();
        fest.setSeqId(rs.getInt("SEQ_ID"));
        fest.setRoleId(rs.getInt("ROLE_ID"));
        fest.setNormalAdd(rs.getDouble("NORMAL_ADD"));
        fest.setFestivalAdd(rs.getDouble("FESTIVAL_ADD"));
        fest.setWeekAdd(rs.getDouble("WEEK_ADD"));
        fest.setName(rs.getString("PRIV_NAME"));
        fest.setBaseAdd(rs.getDouble("BASE_ADD"));
        dosc.add(fest);
      }
    } catch (SQLException e){
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return dosc;
  }
  
  
  /**
   * 查找一个加班费基数
   * @param dbConn
   * @return
   * @throws SQLException
   */
  public YHRoleBaseFee findYHRoleBaseFee(Connection dbConn, int seqId) throws SQLException{
    String sql = "select awf.SEQ_ID, awf.ROLE_ID, awf.NORMAL_ADD, awf.FESTIVAL_ADD, awf.WEEK_ADD,awf.BASE_ADD, up.PRIV_NAME " +
        "         from oa_add_work_cost awf, USER_PRIV up where awf.ROLE_ID = up.SEQ_ID and awf.SEQ_ID ="+ seqId ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    YHRoleBaseFee fest = new YHRoleBaseFee();
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        fest.setSeqId(rs.getInt("SEQ_ID"));
        fest.setRoleId(rs.getInt("ROLE_ID"));
        fest.setNormalAdd(rs.getDouble("NORMAL_ADD"));
        fest.setFestivalAdd(rs.getDouble("FESTIVAL_ADD"));
        fest.setWeekAdd(rs.getDouble("WEEK_ADD"));
        fest.setName(rs.getString("PRIV_NAME"));
        fest.setBaseAdd(rs.getDouble("BASE_ADD"));
      }
    } catch (SQLException e){
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return fest;
  }
  
  /**
   * 查找所有的加班费基数
   * @param dbConn
   * @return
   * @throws SQLException
   */
  public void updatYHRoleBaseFee(Connection dbConn, YHRoleBaseFee fee) throws SQLException{
    String sql = "update oa_add_work_cost set role_id=?, NORMAL_ADD=?, FESTIVAL_ADD=?, WEEK_ADD=?, BASE_ADD=? where SEQ_ID=?" ;
    PreparedStatement ps = null;
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, fee.getRoleId());
      ps.setDouble(2, fee.getNormalAdd());
      ps.setDouble(3, fee.getFestivalAdd());
      ps.setDouble(4, fee.getWeekAdd());
      ps.setDouble(5, fee.getBaseAdd());
      ps.setInt(6, fee.getSeqId());
      int k = ps.executeUpdate();
    } catch (SQLException e){
      throw e;
    }finally {
      YHDBUtility.close(ps, null, null);
    }
  }
  
  /**
   * 查找所有的加班费基数
   * @param dbConn
   * @return
   * @throws SQLException
   */
  public void delYHRoleBaseFee(Connection dbConn, int seqId) throws SQLException{
    String sql = "delete from oa_add_work_cost where SEQ_ID=?" ;
    PreparedStatement ps = null;
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, seqId);
      int k = ps.executeUpdate();
    } catch (SQLException e){
      throw e;
    }finally {
      YHDBUtility.close(ps, null, null);
    }
  }
  
  /**
   * 计算加班费 1.平时 2.周末 3.节假日
   * @param dbConn
   * @param flag
   * @param roleId
   * @return
   * @throws SQLException 
   */
  public double getMoney(Connection dbConn, int flag, int roleId) throws SQLException{
    String sql = "select BASE_ADD,";
      if(flag == 1){
        sql += "NORMAL_ADD ";
      }else if(flag == 2){
        sql += "WEEK_ADD  ";
      }else if(flag == 3){
        sql += "FESTIVAL_ADD ";
      }
    sql += " from oa_add_work_cost where ROLE_ID=" + roleId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getDouble("BASE_ADD") * rs.getDouble(2);
      }
    } catch (SQLException e){
      throw e;
    }finally {
      YHDBUtility.close(ps, null, null);
    }
    return 0;
  }
}

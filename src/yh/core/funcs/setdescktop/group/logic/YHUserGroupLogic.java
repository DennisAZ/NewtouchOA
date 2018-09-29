package yh.core.funcs.setdescktop.group.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.funcs.setdescktop.group.data.YHUserGroup;

public class YHUserGroupLogic {
  
  private static Logger log = Logger.getLogger(YHUserGroupLogic.class);
  
  public void add(Connection dbConn, YHUserGroup ug) throws Exception{
    try{
      
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, ug);
      
    }catch(Exception ex) {
      throw ex;
    }finally {
    }
  }
  
  public boolean delete(Connection dbConn, int seqId,int userId) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "delete from oa_person_group" +
          " where SEQ_ID = ?" +
          " and USER_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, seqId);
      ps.setInt(2, userId);
      return ps.executeUpdate() > 0;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public void setUser(Connection dbConn, int seqId,String userId) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update oa_person_group" +
      " set USER_STR = ?" +
      " where SEQ_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, userId);
      ps.setInt(2, seqId);
      ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public String queryUser(Connection dbConn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String sql = "select USER_STR" +
          " from oa_person_group" +
          " where SEQ_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      String userStr = null;
      
      if (rs.next()){
        userStr = rs.getString("USER_STR");
      }
      
      return userStr;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public String queryUserName(Connection dbConn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String sql = "select USER_NAME" +
      " from PERSON" +
      " where SEQ_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      String userStr = null;
      
      if (rs.next()){
        userStr = rs.getString("USER_NAME");
      }
      
      return userStr;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public void update(Connection dbConn, YHUserGroup ug) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update oa_person_group" +
          " set ORDER_NO = ?" +
          ",GROUP_NAME = ?" +
          " where SEQ_ID = ?" +
          " and USER_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, ug.getOrderNo());
      ps.setString(2, ug.getGroupName());
      ps.setInt(3, ug.getSeqId());
      ps.setString(4, ug.getUserId());
      int i = ps.executeUpdate();
      //System.out.println(i);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
}

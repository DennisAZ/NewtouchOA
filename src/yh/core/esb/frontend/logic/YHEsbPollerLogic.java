package yh.core.esb.frontend.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import yh.core.esb.frontend.data.YHEsbDownTask;
import yh.core.esb.frontend.data.YHEsbUploadTask;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.user.api.core.db.YHDbconnWrap;

public class YHEsbPollerLogic {
  public List<YHEsbDownTask> getDownTaskByStatus( String status) throws Exception{
    YHDbconnWrap dbUtil = new YHDbconnWrap();
    
    Connection conn2 = null;
    String query = "select * from ESB_DOWN_TASK WHERE STATUS in (" + status + ")";
    Statement stm2 = null; 
    ResultSet rs2 = null; 
    List<YHEsbDownTask> list = new ArrayList<YHEsbDownTask>();
    try { 
      conn2 = dbUtil.getSysDbConn();
      stm2 = conn2.createStatement(); 
      rs2 = stm2.executeQuery(query); 
      while (rs2.next()){ 
        int seqId = rs2.getInt("SEQ_ID");
        String fileName = rs2.getString("FILE_NAME");
        String guid = rs2.getString("GUID");
        int st = rs2.getInt("STATUS");
        String fromId = rs2.getString("FROM_ID");
        
        YHEsbDownTask task = new YHEsbDownTask();
        task.setSeqId(seqId);
        task.setFileName(fileName);
        task.setGuid(guid);
        task.setStatus(st);
        task.setFromId(fromId);
        list.add(task);
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm2, rs2, null); 
      YHDBUtility.closeDbConn(conn2, null);
    }
    return list;
  }
  public void updateStatus( String guid , String status)  {
    String query = "update ESB_DOWN_TASK set STATUS = " + status + " where guid='" + guid + "'";
    Statement stm2 = null; 
 YHDbconnWrap dbUtil = new YHDbconnWrap();
    
    Connection conn2 = null;
    try { 
      conn2 = dbUtil.getSysDbConn();
      stm2 = conn2.createStatement(); 
      stm2.executeUpdate(query); 
      conn2.commit();
    } catch(Exception ex) { 
      //throw ex; 
      ex.printStackTrace();
    } finally { 
      YHDBUtility.close(stm2, null, null); 
      YHDBUtility.closeDbConn(conn2, null);
    }
  }
  public void addEsbDownTask( String fileName , String guid , int status , String fromId, String optGuid, String message) throws Exception {
    String query = "insert into ESB_DOWN_TASK ( FILE_NAME, GUID, STATUS , FROM_ID , OPT_GUID , MESSAGE) values(?,?,? ,? , ? , ? ) ";
    PreparedStatement stm2 = null; 
 YHDbconnWrap dbUtil = new YHDbconnWrap();
    
    Connection conn2 = null;
    try { 
      conn2 = dbUtil.getSysDbConn();
      stm2 = conn2.prepareStatement(query);
      stm2.setString(1, fileName);
      stm2.setString(2, guid);
      stm2.setInt(3, status);
      stm2.setString(4, fromId);
      stm2.setString(5, optGuid);
      stm2.setString(6, message);
      stm2.executeUpdate(); 
      conn2.commit();
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm2, null, null); 
      YHDBUtility.closeDbConn(conn2, null);
    }
  }
  
  public boolean hasEsbDownTask( String guid) {
    // TODO Auto-generated method stub
    PreparedStatement ps = null;
    ResultSet rs = null;
 YHDbconnWrap dbUtil = new YHDbconnWrap();
    
    Connection conn2 = null;
    try {
      conn2 = dbUtil.getSysDbConn();
      String sql = "select 1 " +
          " from ESB_DOWN_TASK" +
          " where GUID =?";
      ps = conn2.prepareStatement(sql);
      ps.setString(1, guid);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, null);
      YHDBUtility.closeDbConn(conn2, null);
    }
    return false;
  }
  public boolean hasEsbDownTaskField( String guid) {
    // TODO Auto-generated method stub
    PreparedStatement ps = null;
    ResultSet rs = null;
 YHDbconnWrap dbUtil = new YHDbconnWrap();
    
    Connection conn2 = null;
    try {
      conn2 = dbUtil.getSysDbConn();
      String sql = "select 1 " +
          " from ESB_DOWN_TASK" +
          " where GUID =? and STATUS = '-3'";
      ps = conn2.prepareStatement(sql);
      ps.setString(1, guid);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, null);
      YHDBUtility.closeDbConn(conn2, null);
    }
    return false;
  }
}

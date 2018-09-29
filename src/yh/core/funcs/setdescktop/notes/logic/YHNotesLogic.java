package yh.core.funcs.setdescktop.notes.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHNotesLogic {
  
  private static Logger log = Logger.getLogger(YHNotesLogic.class);
  
  public String getNotes(Connection conn,int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String note = "";
      String sql = "select CONTENT" +
      		" from oa_notes" +
      		" where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      if (rs.next()){
        note = rs.getString("CONTENT");
      }
      
      return note;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  public void saveNote(Connection conn, int seqId, String note) throws Exception{
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;
    
    try{
      String sql = "update oa_notes" +
      " set CONTENT = ?" +
      " where SEQ_ID = ?";
      ps1 = conn.prepareStatement(sql);
      ps1.setString(1, note);
      ps1.setInt(2, seqId);
      if (ps1.executeUpdate() == 0){
        sql = "insert into oa_notes (SEQ_ID,CONTENT)" +
        " values (?,?)";
        ps2 = conn.prepareStatement(sql);
        ps2.setInt(1, seqId);
        ps2.setString(2, note);
        ps2.executeUpdate();
      }
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps1, null, log);
      YHDBUtility.close(ps2, null, log);
    }
  }
}

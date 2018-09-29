package yh.core.funcs.system.interfaces.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.log4j.Logger;

import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.system.interfaces.data.YHInterFaceCont;
import yh.core.funcs.system.interfaces.data.YHInterface;
import yh.core.funcs.system.interfaces.data.YHSysPara;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;

public class YHInterFacesLogic {
  private static Logger log = Logger.getLogger(YHInterFacesLogic.class);
  
  
  public void updateTheme(Connection dbConn,String theme)
  {
	  String sql = "update oa_inf set theme ="+theme+" where seq_id=1";
	  Statement stmt = null;
	  try {
		stmt = dbConn.createStatement();
	    stmt.executeUpdate(sql);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	finally {
	      YHDBUtility.closeDbConn(dbConn, log);
	}
  }
  
  public YHSysPara getSysPara(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHSysPara org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='MYTABLE_BKGROUND'";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        org = new YHSysPara();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public YHSysPara getOnlineView(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHSysPara org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='ONLINE_VIEW'";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        org = new YHSysPara();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public void updateOnlineView(Connection conn, String onlineView) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "update SYS_PARA set PARA_VALUE='" + onlineView + "' where PARA_NAME='ONLINE_VIEW'";
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public YHSysPara getMiibeian(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHSysPara org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='MIIBEIAN'";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        org = new YHSysPara();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public void updateMiibeian(Connection conn, String miibeian) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "update SYS_PARA set PARA_VALUE='" + miibeian + "' where PARA_NAME='MIIBEIAN'";
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public void updateBk(Connection conn, int seqId, String myTableBkGround) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "update SYS_PARA set PARA_VALUE='" + myTableBkGround + "' where SEQ_ID=" + seqId;
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public YHSysPara getLogOutText(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHSysPara org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='LOG_OUT_TEXT'";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        org = new YHSysPara();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public void updateLogOutText(Connection conn, String logOutText) throws Exception{
    PreparedStatement stmt = null;
    try {
      String queryStr = "update SYS_PARA set PARA_VALUE=? where PARA_NAME='LOG_OUT_TEXT'";
      stmt = conn.prepareStatement(queryStr);
      stmt.setString(1, logOutText);
      stmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  /**
   * 设置缺省界面风格
   * @param dbConn
   * @param style
   * @throws Exception
   */
  public void changeDefaultStyle(Connection dbConn, String style) throws Exception{
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;
    try {
      String sql = "update SYS_PARA" +
      " set PARA_VALUE = ?" +
      " where PARA_NAME = 'DEFAULT_INTERFACE_STYLE'";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, style);
      if (ps.executeUpdate() <= 0) {
        sql = "insert into" +
        " SYS_PARA(PARA_NAME, PARA_VALUE)" +
        " values('DEFAULT_INTERFACE_STYLE', ?)";
        ps2 = dbConn.prepareStatement(sql);
        ps2.setString(1, style);
        ps2.executeUpdate();
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
      YHDBUtility.close(ps2, null, log);
    }
  }
  
  public void changeWebOSLOGO(Connection dbConn, String guid) throws Exception{
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;
    try {
      String sql = "update SYS_PARA" +
      		" set PARA_VALUE = ?" +
      		" where PARA_NAME = 'WEBOS_LOGO'";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      if (ps.executeUpdate() <= 0) {
        sql = "insert into" +
        		" SYS_PARA(PARA_NAME, PARA_VALUE)" +
        		" values('WEBOS_LOGO', ?)";
        ps2 = dbConn.prepareStatement(sql);
        ps2.setString(1, guid);
        ps2.executeUpdate();
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
      YHDBUtility.close(ps2, null, log);
    }
  }
  
  public String queryWebOSLOGO(Connection dbConn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String sql = "select" +
      " PARA_VALUE" +
      " from SYS_PARA" +
      " where PARA_NAME = 'WEBOS_LOGO'";
      
      sm = dbConn.createStatement();
      rs = sm.executeQuery(sql);
      
      String value = null;
      
      if (rs.next()) {
        value = rs.getString("PARA_VALUE");
      }
      
      return value;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  public String queryDefaultStyle(Connection dbConn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String sql = "select" +
      " PARA_VALUE" +
      " from SYS_PARA" +
      " where PARA_NAME = 'DEFAULT_INTERFACE_STYLE'";
      
      sm = dbConn.createStatement();
      rs = sm.executeQuery(sql);
      
      String value = null;
      
      if (rs.next()) {
        value = rs.getString("PARA_VALUE");
      }
      
      return value;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  public void updateLogic(Connection conn,YHFileUploadForm fileForm,int userId, String filePath, String seqIds,YHInterface wm) throws Exception{
    try {
      YHORM orm = new YHORM();
      String avatarUpload = fileForm.getParameter("avatarUpload");
      if(!YHUtility.isNullorEmpty(avatarUpload)){
        avatarUpload = "1";
      }else{
        avatarUpload = "0";
      }
      String attachmentId = "";
      String attachmentId1 = "";
      String attachmentNameNew = "";
      String attachmentNameNew1 = "";
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        if(fieldName.equals("ATTACHMENT")){
          YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
          String rand = YHGuid.getRawGuid();
          attachmentId = rand;
          attachmentNameNew = fileName;
          fileForm.saveFile(fieldName, filePath  + File.separator+ YHInterFaceCont.MODULE  + File.separator+ rand  + File.separator+ fileName);
        }

      }
      if(!YHUtility.isNullorEmpty(attachmentNameNew)){
        if(!"0".equals(seqIds)){
          int seqId = Integer.parseInt(seqIds);
          wm.setSeqId(seqId);
        }
        wm.setAttachmentId(attachmentId);
        wm.setAttachmentName(attachmentNameNew);
        wm.setAvatarUpload(avatarUpload);
      }
      if("0".equals(seqIds)){
        orm.saveSingle(conn, wm);
      }else{
        orm.updateSingle(conn, wm);
      }
      
    } catch (Exception e) {
      throw e;
    }
  }
  
  public void updateLogic1(Connection conn,YHFileUploadForm fileForm,int userId, String filePath, String seqIds, YHInterface wm) throws Exception{
    try {
      YHORM orm = new YHORM();
      String avatarUpload = fileForm.getParameter("avatarUpload");
      if(!YHUtility.isNullorEmpty(avatarUpload)){
        avatarUpload = "1";
      }else{
        avatarUpload = "0";
      }
      String attachmentId = "";
      String attachmentNameNew = "";
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        if(!fieldName.equals("ATTACHMENT1")){
          continue;
        }
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
        String rand = YHGuid.getRawGuid();
        attachmentId = rand;
        attachmentNameNew = fileName;
        fileForm.saveFile(fieldName, filePath +File.separator+ YHInterFaceCont.MODULE  +File.separator+ rand  +File.separator+ fileName);
      }
      if(attachmentNameNew != null && !"".equals(attachmentNameNew)){
        if(!"0".equals(seqIds)){
          int seqId = Integer.parseInt(seqIds);
          wm.setSeqId(seqId);
        }
        wm.setAttachmentId1(attachmentId);
        wm.setAttachmentName1(attachmentNameNew);
        wm.setAvatarUpload(avatarUpload);
      }
      if("0".equals(seqIds)){
        orm.saveSingle(conn, wm);
      }else{
        orm.updateSingle(conn, wm);
      }
      
    } catch (Exception e) {
      throw e;
    }
  }
  
  public void doDelete(Connection conn, String attaPath, String attachmentIds) throws Exception{
    String path = "";
    File file = new File(path);
    path = attaPath   + File.separator+  YHInterFaceCont.MODULE  + File.separator+ attachmentIds;
    //if(file.exists()){
     // file.delete();
      YHFileUtility.deleteAll(path);
    //}
  }
  
  public void doDelete1(Connection conn, String attaPath, String attachmentIds) throws Exception{
    String path = "";
    File file = new File(path);
    path = attaPath   + File.separator+ YHInterFaceCont.MODULE + File.separator+ attachmentIds;
    //if(file.exists()){
     // file.delete();
      YHFileUtility.deleteAll(path);
    //}
  }
  
  public String getStatusTextLogic(Connection conn) throws Exception{
    String result = "";
    String sql = " select STATUS_TEXT from oa_inf";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}

package yh.core.esb.server.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import yh.core.esb.common.data.YHTaskInfo;
import yh.core.esb.server.act.YHEsbServerAct;
import yh.core.esb.server.data.YHEsbSysMsg;
import yh.core.esb.server.data.YHEsbTransfer;
import yh.core.esb.server.data.YHEsbTransferStatus;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHEsbServerLogic {
  public static final String TRANSFER_STATUS_READY = "0";
  public static final String TRANSFER_STATUS_DOWNLOADING = "1";
  public static final String TRANSFER_STATUS_RECVED = "2";
  public static final String TRANSFER_STATUS_ALLCOMPLETE = "3";
  public static final String TRANSFER_STATUS_FAILED = "4";
  
  private static Logger log = Logger.getLogger(YHEsbServerLogic.class);
  /**
   * 增加传送状态
   * @param dbConn
   * @param transId
   * @param toId
   * @throws Exception
   */
  private void addStatus(Connection dbConn, String transId, int toId) throws Exception {
    YHEsbTransferStatus status = new YHEsbTransferStatus();
    status.setStatus(TRANSFER_STATUS_READY);
    status.setToId(toId);
    status.setTransId(transId);
    
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, status);
  }
  public boolean hasStatus(Connection dbConn, String transId, int toId) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select 1 " +
          " from ESB_TRANSFER_STATUS " +
          " where TRANS_ID =?" ;
      if (toId != 0 ) {
        sql += " AND TO_ID = '" + toId + "' ";
      }
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, transId);
      rs = ps.executeQuery();
      
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return false;
  }
  public void setStatus(Connection dbConn, String guid, String toId, String status) throws Exception {
    PreparedStatement ps = null;
    try {
      if (YHUtility.isNullorEmpty(toId)) {
        return ;
      }
      String[] toIds = toId.split(",");
      toId = "";
      for (String s : toIds) {
        toId += "'" + s + "',";
      }
      if (toId.endsWith(",")) {
        toId = toId.substring(0, toId.length() - 1);
      }
      String sql = "update ESB_TRANSFER_STATUS" +
          " set STATUS = ?" +
          " where TRANS_ID = ?" +
          " and TO_ID in (SELECT TD_USER.SEQ_ID FROM TD_USER WHERE USER_CODE IN (" + toId + ")) and status = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, status);
      ps.setString(2, guid);
      ps.setString(3, TRANSFER_STATUS_FAILED);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 改变发送的状态
   * @param dbConn
   * @param transId
   * @param toId
   * @param status
   * @throws Exception 
   */
  public void setStatus(Connection dbConn, String guid, int toId, String status) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update ESB_TRANSFER_STATUS" +
      		" set STATUS = ?" +
      		" where TRANS_ID = ?" +
      		" and TO_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, status);
      ps.setString(2, guid);
      ps.setInt(3, toId);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 改变发送的状态

   * @param dbConn
   * @param transId
   * @param toId
   * @param status
   * @throws Exception 
   */
  public void setStatus2(Connection dbConn, String guid, int toId, String status) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update ESB_TRANSFER_STATUS" +
          " set STATUS = ?" +
          " where TRANS_ID = ?" +
          " and TO_ID = ? and STATUS = ? ";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, status);
      ps.setString(2, guid);
      ps.setInt(3, toId);
      ps.setString(4, TRANSFER_STATUS_FAILED);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  public boolean hasStatus(Connection dbConn,String guid  , int toId, String status) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select 1 " +
          " from ESB_TRANSFER_STATUS" +
          " where TRANS_ID =? AND TO_ID='" + toId + "' AND STATUS ='" + status + "'"  ;
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      rs = ps.executeQuery();
      
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return false;
  }
  /**
   * 下载完成时间设置
   * @param dbConn
   * @param transId
   * @param toId
   * @param status
   * @throws Exception 
   */
  public void setDownloadCreate(Connection dbConn, String guid, int toId) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update ESB_TRANSFER_STATUS" +
          " set CREATE_TIME = ?" +
          " where TRANS_ID = ?" +
          " and TO_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setTimestamp(1, YHUtility.parseTimeStamp());
      ps.setString(2, guid);
      ps.setInt(3, toId);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 下载完成时间设置

   * @param dbConn
   * @param transId
   * @param toId
   * @param status
   * @throws Exception 
   */
  public void setDownloadComplete(Connection dbConn, String guid, int toId) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update ESB_TRANSFER_STATUS" +
          " set COMPLETE_TIME = ?" +
          " where TRANS_ID = ?" +
          " and TO_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setTimestamp(1, YHUtility.parseTimeStamp());
      ps.setString(2, guid);
      ps.setInt(3, toId);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  
  public void setSysMsgStatus(Connection dbConn, int seqId, String status) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update ESB_SYS_MSG" +
      " set STATUS = ?" +
      " where SEQ_ID = ?";
      
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, status);
      ps.setInt(2, seqId);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 改变发送的状态
   * @param dbConn
   * @param transId
   * @param toId
   * @param status
   * @throws Exception 
   */
  public void setTransStatus(Connection dbConn, String guid, String status) throws Exception {
    PreparedStatement ps = null;
    try {
      String sql = "update ESB_TRANSFER" +
      " set STATUS = ?" +
      " where GUID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, status);
      ps.setString(2, guid);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 接收完毕
   * @param dbConn
   * @param guid
   * @param toId
   * @throws Exception 
   */
  public void recvCompleted(Connection dbConn, String guid, int toId, String fromId, File file) throws Exception {
    setStatus(dbConn, guid, toId, TRANSFER_STATUS_RECVED);
    setDownloadComplete(dbConn, guid, toId);
    Map<String, String> result = new HashMap<String, String>();
    result.put("code", "2");
    result.put("to", seqId2UserCode(dbConn, toId));
    result.put("guid", guid);
    result.put("msg", "The file sent successfully!");
    addSysmsg(dbConn, guid, YHFOM.toJson(result).toString(), fromId);
    if (isTaskCompleted(dbConn, guid)) {
      //整个任务结束
      if (file != null) {
        File dir = file.getParentFile();
        file.delete();
        dir.delete();
      }
      setTransStatus(dbConn, guid, YHEsbServerLogic.TRANSFER_STATUS_ALLCOMPLETE);
    }
  }
  
  private boolean isTaskCompleted(Connection dbConn, String guid) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) as AMOUNT" +
      		" from ESB_TRANSFER_STATUS" +
      		" where TRANS_ID = ?" +
      		" and STATUS != '2'";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      rs = ps.executeQuery();
      
      int amount = -1;
      if (rs.next()) {
        amount = rs.getInt("AMOUNT");
      }
      
      return amount == 0;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 增加传送任务
   * @param dbConn
   * @param fromId
   * @param filePath
   * @param content
   * @throws Exception
   */
  public void addTransferTask(Connection dbConn, String guid, int fromId, String filePath, String content, String type, String toId , String optGuid , String message) throws Exception {
    YHEsbTransfer trans = new YHEsbTransfer();
    trans.setFromId(fromId);
    trans.setGuid(guid);
    trans.setFilePath(filePath);
    trans.setContent(content);
    trans.setStatus("1");//上传中 状态标示    trans.setType(type);
    trans.setCreateTime(YHUtility.parseTimeStamp());
    trans.setToId(toId);
    trans.setOptGuid(optGuid);
    trans.setMessage(message);
    
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, trans);
  }
  
  public void updateTransferTaskStatus(Connection dbConn, String guid, String status) throws Exception{
    PreparedStatement ps = null;
    String sql = " update esb_transfer set status = " + status + ",complete_time = ? where guid ='" + guid + "'"; 
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setTimestamp(1, YHUtility.parseTimeStamp());
      ps.executeUpdate();
    }catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }
  
  public void uploadComplete(Connection dbConn, String guid, int fromId, String toId, String filePath, String content, String type) throws Exception {
    if (YHUtility.isNullorEmpty(toId))
      toId = this.getToId(dbConn, guid);
    if (toId != null && toId.trim().length() > 0) {
      this.updateTransferTaskStatus(dbConn, guid, "2");
      if ("ALL_USERS".equalsIgnoreCase(toId)) {
        if (!this.hasStatus(dbConn, guid, 0)) {
          addStatus4AllUsers(dbConn, guid);
        }
      } else if ("OTHER_USERS".equalsIgnoreCase(toId)) {
        if (!this.hasStatus(dbConn, guid, 0)) {
          addStatus4OtherUsers(dbConn, guid, fromId);
        }
      } else {
        String[] ids = toId.split(",");
        for (String s : ids) {
          try {
            int id = Integer.parseInt(s);
            if (!this.hasStatus(dbConn, guid, id)) {
              this.addStatus(dbConn, guid,id );
            }
          } catch (NumberFormatException e) {
            //什么也不做
          }
        }
      }
    }
  }
  
  
  public String codeStr2IdStr(Connection dbConn, String code) throws Exception {
    String ids = "";
    if (!YHUtility.isNullorEmpty(code)) {
      if ("ALl_USERS".equalsIgnoreCase(code.trim()) || "OTHER_USERS".equalsIgnoreCase(code.trim())) {
        return code;
      }
      else {
        for (String s : code.split(",")) {
          int id = userCode2SeqId(dbConn, s);
          if (id >= 0) {
            ids += id;
            ids += ",";
          }
        }
        return ids;
      }
    }
    return "";
  }
  
  private int userCode2SeqId(Connection dbConn, String code) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select SEQ_ID from TD_USER where USER_CODE = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, code);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("SEQ_ID");
      }
    }catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
    return -1;
  }
  
  
  public String seqId2UserCode(Connection dbConn, int id) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select USER_CODE from TD_USER where SEQ_ID = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, id);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getString("USER_CODE");
      }
    }catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return "";
  }
  
  /**
   * 为所有用户增加下载任务
   * @param dbConn
   * @param guid
   */
  private void addStatus4AllUsers(Connection dbConn, String guid) {
    PreparedStatement ps = null;
    try {
      String sql = "insert into ESB_TRANSFER_STATUS" +
      		" (TRANS_ID, STATUS, TO_ID)" +
      		" select ?, ?, t.SEQ_ID" +
      		" from TD_USER t";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      ps.setString(2, TRANSFER_STATUS_READY);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 为所有用户增加下载任务
   * @param dbConn
   * @param guid
   */
  private void addStatus4OtherUsers(Connection dbConn, String guid, int fromId) {
    PreparedStatement ps = null;
    try {
      String sql = "insert into ESB_TRANSFER_STATUS" +
      " (TRANS_ID, STATUS, TO_ID)" +
      " select ?, ?, t.SEQ_ID" +
      " from TD_USER t" +
      " where SEQ_ID != ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      ps.setString(2, TRANSFER_STATUS_READY);
      ps.setInt(3, fromId);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public void uploadComplete(Connection dbConn, YHTaskInfo info) throws Exception {
    this.uploadComplete(dbConn, info.getGuid(), info.getFromId(), info.getToId(), info.getFile().getAbsolutePath(), info.getContent(), "0");
  }
  
  /**
   * 查询是否有登陆用户的文件
   * @param dbConn
   * @param toId
   * @return
   * @throws Exception
   */
  public List<YHEsbTransfer> queryDownloadTask(Connection dbConn, int toId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHEsbTransfer> list = new ArrayList<YHEsbTransfer>();
    try {
      String sql = "select SEQ_ID" +
      		",FROM_ID" +
      		",FILE_PATH" +
      		",CONTENT" +
      		",STATUS" +
      		",GUID" +
      		",TYPE" +
      		",CREATE_TIME" +
      		",TO_ID" +
      		",COMPLETE_TIME" +
      		",FAILED_MESSAGE" +
      		" from ESB_TRANSFER" +
      		" where GUID in (select TRANS_ID from ESB_TRANSFER_STATUS where TO_ID = ? and STATUS  = ?)" +
      		" order by SEQ_ID desc";
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1, toId);
      ps.setString(2, TRANSFER_STATUS_READY);
      rs = ps.executeQuery();
      
      while (rs.next() && list.size() <= YHEsbServerAct.DOWNLOADS_LIMIT) {
        YHEsbTransfer transfer = new YHEsbTransfer();
        transfer.setSeqId(rs.getInt("SEQ_ID"));
        transfer.setFromId(rs.getInt("FROM_ID"));
        transfer.setFilePath(rs.getString("FILE_PATH"));
        transfer.setContent(rs.getString("CONTENT"));
        transfer.setStatus(rs.getString("STATUS"));
        transfer.setGuid(rs.getString("GUID"));
        transfer.setType(rs.getString("TYPE"));
        transfer.setCreateTime(rs.getDate("CREATE_TIME"));
        transfer.setToId(rs.getString("TO_ID"));
        transfer.setCompleteTime(rs.getDate("COMPLETE_TIME"));
        transfer.setFailedMessage(rs.getString("FAILED_MESSAGE"));
        list.add(transfer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return list;
  }
  
  /**
   * 查询是否有登陆用户的文件
   * @param dbConn
   * @param toId
   * @return
   * @throws Exception
   */
  public List<YHEsbSysMsg> querySysMsg(Connection dbConn, int toId) throws Exception {
    YHORM orm = new YHORM();
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("TO_ID", String.valueOf(toId));
    filters.put("STATUS", "0");
    
    List<YHEsbSysMsg> list = orm.loadListSingle(dbConn, YHEsbSysMsg.class, filters);
    
    return list;
  }
  
  /**
   * 获取下载任务的信息   * @param dbConn
   * @param guid
   * @return
   * @throws Exception
   */
  public YHEsbTransfer queryDownloadInfo(Connection dbConn, String guid) throws Exception {
    YHORM orm = new YHORM();
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("GUID", guid);
    return (YHEsbTransfer) orm.loadObjSingle(dbConn, YHEsbTransfer.class, filters);
  }
  
  /**
   * 将设置广播给所有用户
   * @param dbConn
   * @param configPath
   * @throws Exception
   */
  public void broadcastConfig(Connection dbConn, String configPath, String optGuid ,String message) throws Exception {
    String guid = UUID.randomUUID().toString();
    this.addTransferTask(dbConn, guid, 0, configPath, "", "1", "ALL_USERS" , optGuid , message);
    uploadComplete(dbConn, guid, 0, "ALL_USERS", configPath, "", "1");
  }
  
  public void addSysmsg(Connection dbConn, String guid, String content, String toId) throws Exception {
    YHEsbSysMsg msg = new YHEsbSysMsg();
    msg.setContent(content);
    msg.setTime(new Date(System.currentTimeMillis()));
    msg.setGuid(guid);
    msg.setToId(toId);
    
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, msg);
  }
  
  public static void setUploadFailedMessage(Connection dbConn, String guid, String failedMessage){
    PreparedStatement ps = null;
    String sql = " update esb_transfer set status = " + TRANSFER_STATUS_FAILED + ",failed_message = '" + failedMessage + "' where guid ='" + guid + "'"; 
    try {
      ps = dbConn.prepareStatement(sql);
//      ps.setTimestamp(1, YHUtility.parseTimeStamp());
      ps.executeUpdate();
    }catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }
  
  public static void setDownloadFailedMessage(Connection dbConn, String guid, String failedMessage, String toId , int fromId , boolean checked ) throws Exception{
    PreparedStatement ps = null;
    String sql = " update esb_transfer_status set status = " + TRANSFER_STATUS_FAILED + ",failed_message = '" + failedMessage + "' where trans_id ='" + guid + "'  and to_id ="+toId; 
    try {
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
    }catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, null);
    }
    if (checked) {
      YHEsbServerLogic lo = new YHEsbServerLogic();
      Map<String, String> result = new HashMap<String, String>();
      result.put("code", "-3");
      result.put("to", lo.seqId2UserCode(dbConn, Integer.parseInt(toId)));
      result.put("guid", guid);
      result.put("msg", failedMessage);
      lo.addSysmsg(dbConn, guid, YHFOM.toJson(result).toString(), String.valueOf(fromId));
      /*
      //给下载方一个消息，不让其后台服务循环了
      Map<String, String> result2 = new HashMap<String, String>();
      result2.put("code", "-4");
      result2.put("guid", guid);
      result2.put("msg", "下载任务超时！");
      lo.addSysmsg(dbConn, guid, YHFOM.toJson(result).toString(), toId);
      */
    }
  }
  public boolean hasTransferTask(Connection dbConn, String guid) {
    // TODO Auto-generated method stub
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select 1 " +
          " from ESB_TRANSFER" +
          " where GUID =? ";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      rs = ps.executeQuery();
      
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return false;
  }
  public String  getToId(Connection dbConn, String guid) {
    // TODO Auto-generated method stub
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select TO_ID " +
          " from ESB_TRANSFER" +
          " where GUID =? ";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      rs = ps.executeQuery();
      
      if (rs.next()) {
        return rs.getString("TO_ID");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return "";
  }
  public boolean isTransferTaskField(Connection dbConn, String guid) {
    // TODO Auto-generated method stub
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select 1 " +
          " from ESB_TRANSFER" +
          " where GUID =? and STATUS = '" + TRANSFER_STATUS_FAILED + "'";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, guid);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return false;
  }
}

package yh.core.funcs.workplan.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import yh.core.funcs.workplan.data.YHWorkDetail;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;

public class YHWorkDetailLogic {
  private static Logger log = Logger.getLogger(YHWorkDetailLogic.class);
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public List<YHWorkDetail> selectDetail(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHWorkDetail detail = null;
    List<YHWorkDetail> list = new ArrayList<YHWorkDetail>();                                                                  
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select " +
      "SEQ_ID," +
      "PLAN_ID," +
      "WRITE_TIME," +
      "PROGRESS," +
      "[PERCENT]," +
      "TYPE_FLAG," +
      "WRITER," +
      "ATTACHMENT_ID," +
      "ATTACHMENT_NAME" +
      " from oa_working_detail where PLAN_ID=? and TYPE_FLAG=? order by seq_id";
    }else {
      sql = "select " +
      "SEQ_ID," +
      "PLAN_ID," +
      "WRITE_TIME," +
      "PROGRESS," +
      "PERCENT," +
      "TYPE_FLAG," +
      "WRITER," +
      "ATTACHMENT_ID," +
      "ATTACHMENT_NAME" +
      " from oa_working_detail where PLAN_ID=? and TYPE_FLAG=? order by seq_id";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1, seqId);
      stmt.setString(2, "1");
      rs = stmt.executeQuery();
      while (rs.next()) {
        detail = new YHWorkDetail(); 
        detail.setSeqId(rs.getInt("SEQ_ID"));
        detail.setPlanId(rs.getString("PLAN_ID"));
        detail.setWriteTime(rs.getDate("WRITE_TIME"));
        detail.setProgress(rs.getString("PROGRESS"));
        detail.setPercent(rs.getInt("PERCENT"));
        detail.setTypeFlag(rs.getString("TYPE_FLAG"));
        detail.setWriter(rs.getString("WRITER"));
        detail.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        detail.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
        list.add(detail);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;  
  }
  /***
   * ADD数据
   * @return
   * @throws Exception 
   */
  public void addDetail(Connection dbConn,YHWorkDetail detail) throws Exception {
    PreparedStatement stmt = null ;                                                                 
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "insert into oa_working_detail(" 
        + "PLAN_ID,"
        + "WRITE_TIME,"
        + "PROGRESS,"
        + "[PERCENT],"
        + "TYPE_FLAG,"
        + "WRITER,"
        + "ATTACHMENT_ID,"
        + "ATTACHMENT_NAME) values(?,?,?,?,?,?,?,?)";
    }else {
      sql = "insert into oa_working_detail(" 
        + "PLAN_ID,"
        + "WRITE_TIME,"
        + "PROGRESS,"
        + "PERCENT,"
        + "TYPE_FLAG,"
        + "WRITER,"
        + "ATTACHMENT_ID,"
        + "ATTACHMENT_NAME) values(?,?,?,?,?,?,?,?)";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1, detail.getPlanId());
      stmt.setDate(2,detail.getWriteTime());
      stmt.setString(3, detail.getProgress());
      stmt.setInt(4,detail.getPercent());
      stmt.setString(5, detail.getTypeFlag());
      stmt.setString(6, detail.getWriter());
      stmt.setString(7, detail.getAttachmentId());
      stmt.setString(8, detail.getAttachmentName());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    } 
  }
  /***
   * detele数据
   * @return
   * @throws Exception 
   */
  public void deteleDetail(Connection dbConn,int seqId) throws Exception {
    PreparedStatement stmt = null ;                                                                 
    String sql = "delete from oa_working_detail where SEQ_ID=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1,seqId);
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    } 
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public YHWorkDetail selectId(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHWorkDetail detail = null;                                                                
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select " +
      "SEQ_ID," +
      "PLAN_ID," +
      "WRITE_TIME," +
      "PROGRESS," +
      "[PERCENT]," +
      "TYPE_FLAG," +
      "WRITER," +
      "ATTACHMENT_ID," +
      "ATTACHMENT_NAME" +
      " from oa_working_detail where SEQ_ID=?";
    }else {
      sql = "select " +
      "SEQ_ID," +
      "PLAN_ID," +
      "WRITE_TIME," +
      "PROGRESS," +
      "PERCENT," +
      "TYPE_FLAG," +
      "WRITER," +
      "ATTACHMENT_ID," +
      "ATTACHMENT_NAME" +
      " from oa_working_detail where SEQ_ID=?";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1, seqId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        detail = new YHWorkDetail(); 
        detail.setSeqId(rs.getInt("SEQ_ID"));
        detail.setPlanId(rs.getString("PLAN_ID"));
        detail.setWriteTime(rs.getDate("WRITE_TIME"));
        detail.setProgress(rs.getString("PROGRESS"));
        detail.setPercent(rs.getInt("PERCENT"));
        detail.setTypeFlag(rs.getString("TYPE_FLAG"));
        detail.setWriter(rs.getString("WRITER"));
        detail.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        detail.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return detail;  
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public void updateDetail(Connection dbConn,YHWorkDetail detail) throws Exception {
    PreparedStatement stmt = null ;                                                                
    String sql = "update oa_working_detail set "
      + "WRITE_TIME=?,"
      + "PROGRESS=?,"
      + "WRITER=?,"
      + "ATTACHMENT_ID=?,"
      + "ATTACHMENT_NAME=?" 
      + " where SEQ_ID=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setDate(1, detail.getWriteTime());
      stmt.setString(2,detail.getProgress());
      stmt.setString(3,detail.getWriter());
      stmt.setString(4, detail.getAttachmentId());
      stmt.setString(5, detail.getAttachmentName());
      stmt.setInt(6,detail.getSeqId());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }  
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public void updateDetailId(Connection dbConn,YHWorkDetail detail) throws Exception {
    PreparedStatement stmt = null ;                                                                
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "update oa_working_detail set "
        + "WRITE_TIME=?,"
        + "PROGRESS=?,"
        + "WRITER=?,"
        + "ATTACHMENT_ID=?,"
        + "ATTACHMENT_NAME=?,"
        + "[PERCENT]=?"
        + " where SEQ_ID=?";
    }else {
      sql = "update oa_working_detail set "
        + "WRITE_TIME=?,"
        + "PROGRESS=?,"
        + "WRITER=?,"
        + "ATTACHMENT_ID=?,"
        + "ATTACHMENT_NAME=?,"
        + "PERCENT=?"
        + " where SEQ_ID=?";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setDate(1, detail.getWriteTime());
      stmt.setString(2,detail.getProgress());
      stmt.setString(3,detail.getWriter());
      stmt.setString(4, detail.getAttachmentId());
      stmt.setString(5, detail.getAttachmentName());
      stmt.setInt(6, detail.getPercent());
      stmt.setInt(7,detail.getSeqId());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }  
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public List<YHWorkDetail> selectDetailId(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHWorkDetail detail = null;
    List<YHWorkDetail> list = new ArrayList<YHWorkDetail>();                                                                  
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select " +
      "SEQ_ID," +
      "PLAN_ID," +
      "WRITE_TIME," +
      "PROGRESS," +
      "[PERCENT]," +
      "TYPE_FLAG," +
      "WRITER," +
      "ATTACHMENT_ID," +
      "ATTACHMENT_NAME" +
      " from oa_working_detail where PLAN_ID=? and TYPE_FLAG=? order by seq_id asc";
    }else {
      sql = "select " +
      "SEQ_ID," +
      "PLAN_ID," +
      "WRITE_TIME," +
      "PROGRESS," +
      "PERCENT," +
      "TYPE_FLAG," +
      "WRITER," +
      "ATTACHMENT_ID," +
      "ATTACHMENT_NAME" +
      " from oa_working_detail where PLAN_ID=? and TYPE_FLAG=? order by seq_id asc";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1, seqId);
      stmt.setString(2, "0");
      rs = stmt.executeQuery();
      while (rs.next()) {
        detail = new YHWorkDetail(); 
        detail.setSeqId(rs.getInt("SEQ_ID"));
        detail.setPlanId(rs.getString("PLAN_ID"));
        detail.setWriteTime(rs.getDate("WRITE_TIME"));
        detail.setProgress(rs.getString("PROGRESS"));
        detail.setPercent(rs.getInt("PERCENT"));
        detail.setTypeFlag(rs.getString("TYPE_FLAG"));
        detail.setWriter(rs.getString("WRITER"));
        detail.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        detail.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
        list.add(detail);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;  
  }
  
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public int sunNum(Connection dbConn,int percnt,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    int sun =0;
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select [PERCENT] from oa_working_detail where WRITER='" + percnt + "' and plan_id=" + seqId;
    }else {
      sql = "select PERCENT from oa_working_detail where WRITER='" + percnt + "' and plan_id=" + seqId;
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        sun += rs.getInt("PERCENT");
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sun;  
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public int sunNum2(Connection dbConn,int percnt,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    int sun =0;
    String sql = "select PERCENT from oa_working_detail where WRITER='" + percnt + "' and plan_id=" + seqId + " order by seq_id asc";
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select [PERCENT] from oa_working_detail where WRITER='" + percnt + "' and plan_id=" + seqId + " order by seq_id asc";
    }else {
      sql = "select PERCENT from oa_working_detail where WRITER='" + percnt + "' and plan_id=" + seqId + " order by seq_id asc";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        sun = rs.getInt("PERCENT");
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sun;  
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public int maxSunNum(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    int sun =0;
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select max([PERCENT]),writer from oa_working_detail where plan_id='" + seqId +"' GROUP BY writer";
    }else {
      sql = "select max(PERCENT),writer from oa_working_detail where plan_id='" + seqId +"' GROUP BY writer";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        sun += rs.getInt(1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sun;  
  }
}

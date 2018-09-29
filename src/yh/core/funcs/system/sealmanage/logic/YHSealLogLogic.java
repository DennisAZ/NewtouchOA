package yh.core.funcs.system.sealmanage.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;

import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHSealLogLogic {
  private static Logger log = Logger.getLogger(YHSealLogLogic.class);

  public String getSealList(Connection conn,Map request) throws Exception{
    String sql =  "select SEQ_ID" +
                  ",S_ID" +
                  ",LOG_TYPE" +
                  ",USER_ID" +
                  ",CLIENT_TYPE" +
                  ",LOG_TIME" +
                  ",RESULT" +
                  ",IP_ADD from oa_seal_log";
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }

  
  public String getSearchList(Connection conn,Map request, String logType, String sealName, String beginTime, String endTime, String userId) throws Exception{
    String whereStr = "";
    if(!YHUtility.isNullorEmpty(userId)){
      whereStr = " and oa_seal_log.USER_ID = "+userId;
    }
    String sql = "select oa_seal_log.SEQ_ID" +
                 ",oa_seal_log.S_ID" +
                 ",oa_seal_log.LOG_TYPE" +
                 ",oa_seal_log.USER_ID" +
                 ",oa_seal_log.CLIENT_TYPE" +
                 ",oa_seal_log.LOG_TIME" +
                 ",oa_seal_log.RESULT" +
                 ",oa_seal_log.IP_ADD from SEAL,oa_seal_log where SEAL.SEAL_ID = oa_seal_log.S_ID"+whereStr;
    if(!YHUtility.isNullorEmpty(logType)){ 
      sql = sql + " and oa_seal_log.LOG_TYPE like '%" + logType + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(sealName)){ 
      sql = sql + " and SEAL.SEAL_NAME like '%" + sealName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(beginTime)){
      beginTime = YHDBUtility.getDateFilter("CREATE_TIME", beginTime, ">=");
      sql = sql + " and " + beginTime; 
    }else if(!YHUtility.isNullorEmpty(endTime)){
      endTime = YHDBUtility.getDateFilter("CREATE_TIME", endTime, "<=");
      sql = sql + " and " + endTime; 
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
  
  public String getSealNameLogic(Connection dbConn , String sId) throws Exception{
    String result = "";
    String sql = " select SEAL_NAME from SEAL where SEAL_ID = '" + sId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
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
  
  /**
   * 取得用户名称(单个)
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getUserNameLogic(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId ;
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
  
  /**
   * 删除印章日志
   * @param conn
   * @param seqIds   PERSON表中的SEQ_ID串（以逗号为分隔）
   * @throws Exception
   */
  
  public void deleteSealLog(Connection conn, String seqIds) throws Exception {
    String sql = "DELETE FROM oa_seal_log WHERE SEQ_ID IN(" + seqIds + ")";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
}

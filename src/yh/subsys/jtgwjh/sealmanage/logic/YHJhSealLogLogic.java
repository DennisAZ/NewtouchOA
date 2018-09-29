package yh.subsys.jtgwjh.sealmanage.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSeal;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSealLog;

public class YHJhSealLogLogic {
  private static Logger log = Logger.getLogger(YHJhSealLogLogic.class);

  public String getSealList(Connection conn,Map request) throws Exception{
    String sql =  "select SEQ_ID" +
                  ",S_ID" +
                  ",SEAL_NAME"+
                  ",LOG_TYPE" +
                  ",USER_ID" +
                  ",USER_NAME" +
                  ",CLIENT_TYPE" +
                  ",LOG_TIME" +
                  ",RESULT" +
                  ",IP_ADD from JH_SEAL_LOG order by LOG_TIME desc";
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }

  
  public String getSearchList(Connection conn,Map request, String logType, String sealName, String beginTime, String endTime, String userId,String userName) throws Exception{
    String whereStr = "";
    if(!YHUtility.isNullorEmpty(userId)){
      whereStr = " and JH_SEAL_LOG.USER_ID = "+userId;
    }
    String sql = "select JH_SEAL_LOG.SEQ_ID" +
                 ",JH_SEAL_LOG.S_ID" +
                 ",JH_SEAL_LOG.SEAL_NAME" +
                 ",JH_SEAL_LOG.LOG_TYPE" +
                 ",JH_SEAL_LOG.USER_ID" +
                 ",JH_SEAL_LOG.USER_NAME" +
                 ",JH_SEAL_LOG.CLIENT_TYPE" +
                 ",JH_SEAL_LOG.LOG_TIME" +
                 ",JH_SEAL_LOG.RESULT" +
                 ",JH_SEAL_LOG.IP_ADD from JH_SEAL_LOG where 1=1";
    if(!YHUtility.isNullorEmpty(logType)){ 
      sql = sql + " and JH_SEAL_LOG.LOG_TYPE like '%" + logType + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(sealName)){ 
      sealName = sealName.replace("\\", "");
      sealName = sealName.replace("'", "''");
      sql = sql + " and JH_SEAL_LOG.SEAL_NAME like '%" + sealName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(userName)){ 
      userName = userName.replace("\\", "");
      userName = userName.replace("'", "''");
      sql = sql + " and JH_SEAL_LOG.USER_NAME like '%" + userName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(beginTime)){
      beginTime = YHDBUtility.getDateFilter("LOG_TIME", beginTime, ">=");
      sql = sql + " and " + beginTime; 
    }else if(!YHUtility.isNullorEmpty(endTime)){
      endTime = YHDBUtility.getDateFilter("LOG_TIME", endTime, "<=");
      sql = sql + " and " + endTime; 
    }
    
    sql = sql + " order by LOG_TIME desc";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
  
  public String getSealNameLogic(Connection dbConn , String sId) throws Exception{
    String result = "";
    String sql = " select SEAL_NAME from JH_SEAL where SEAL_ID = '" + sId + "'";
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
    String sql = "DELETE FROM JH_SEAL_LOG WHERE SEQ_ID IN(" + seqIds + ")";
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
  
  public void addSealLog(Connection dbConn,YHJhSealLog log){
    YHORM orm = new YHORM();
    try {
      orm.saveSingle(dbConn, log);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /*
   * 添加印章使用情况日志
   * person : 当前登录用户
   * seal：被操作印章对象
   * logType：操作类型
   * ip : 操作IP
   * desc : 描述
   */
  public static void addSealInfoLog(Connection dbConn, YHPerson person,YHJhSeal seal, String logType,String ip,String desc ){
	  YHORM orm = new YHORM();
	  try{
		  YHJhSealLog sealLog=new YHJhSealLog();
	      Calendar cal = Calendar.getInstance();        
	      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
	      String logTime = sdf.format(cal.getTime());    
		  sealLog.setsId(seal.getSealId());
		  sealLog.setLogType(logType);
		  sealLog.setSealName(seal.getSealName());
		  String userId=String.valueOf(person.getSeqId());
		  sealLog.setUserId(userId);
		  sealLog.setUserName(person.getUserName());
		  sealLog.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTime));
		  sealLog.setIpAdd(ip);
	      String dept = "";
	      String deptName = "";
		  if(!YHUtility.isNullorEmpty(seal.getDeptStr())){
		        String[] esbDept = YHDeptTreeLogic.getEsbUsers2(dbConn, seal.getDeptStr());
		        if(esbDept.length == 2 && !YHUtility.isNullorEmpty(esbDept[0]) && !YHUtility.isNullorEmpty(esbDept[1])){
		          dept = esbDept[0];
		          deptName = esbDept[1];
		        }
		      }
		  sealLog.setResult(desc);
      
		  orm.saveSingle(dbConn, sealLog);
	  }catch(Exception e){
		  e.printStackTrace();
	  }
  }
}

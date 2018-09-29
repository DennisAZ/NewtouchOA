package yh.core.funcs.calendar.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.calendar.data.YHAffair;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHAffairLogic {
  private static Logger log = Logger.getLogger(YHAffairLogic.class);
  public int addAffair(Connection dbConn,YHAffair affair) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, affair);
    return YHCalendarLogic.getMaSeqId(dbConn, "AFFAIR");
  }
  public void updateAffair(Connection dbConn,YHAffair affair) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, affair);
  }
  public List<YHAffair> selectAffair(Connection dbConn,String str[]) throws Exception{
    List<YHAffair> affairList = new ArrayList<YHAffair>();
    YHORM orm = new YHORM();
    affairList = orm.loadListSingle(dbConn, YHAffair.class, str);
    return affairList;
  }
  public String toSearchData(Connection conn,Map request,int userId) throws Exception{
    String sql = "select SEQ_ID,BEGIN_TIME,END_TIME,TYPE,REMIND_DATE,REMIND_TIME,CONTENT,MANAGER_ID,USER_ID from oa_affairs where USER_ID  ='" + userId+"'  order by BEGIN_TIME desc";
    //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  public YHAffair selectAffairById(Connection dbConn,int seqId) throws Exception{
    YHAffair affair = new YHAffair();
    YHORM orm = new YHORM();
    affair = (YHAffair) orm.loadObjSingle(dbConn, YHAffair.class, seqId);
    return affair;
  }
  public void deleteAffairById(Connection dbConn,int seqId) throws Exception{
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHAffair.class,seqId);
  }
  public List<Map<String,String>> selectAffairByTerm(Connection dbConn,int userId,String minTime,String maxTime,String content)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Map<String,String>> affairList = new ArrayList<Map<String,String>>();
    String sql = "select *from oa_affairs where USER_ID='" + userId + "'" ;
    if(!content.equals("")){
      content = YHDBUtility.escapeLike(content);
      sql = sql + " and CONTENT like '%" + content + "%' " + YHDBUtility.escapeLike(); 
    }
    if(!minTime.equals("")){
      String temp = YHDBUtility.getDateFilter("BEGIN_TIME", minTime, ">=");
      sql = sql + " and " + temp;
    }
    if(!maxTime.equals("")){
      maxTime = maxTime+" 23:59:59";
      String temp = YHDBUtility.getDateFilter("END_TIME", maxTime, "<=");
      sql = sql + " and " + temp;
    }
    sql = sql + " order by BEGIN_TIME";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map = new HashMap<String,String>();
        map.put("seqId",rs.getString("SEQ_ID"));
        map.put("userId",rs.getString("USER_ID"));
        map.put("content",rs.getString("CONTENT"));
        map.put("remindDate",rs.getString("REMIND_DATE"));
        map.put("remindTime",rs.getString("REMIND_TIME"));
        map.put("managerId",rs.getString("MANAGER_ID"));
        map.put("type",rs.getString("TYPE"));
        map.put("sms2Remind",rs.getString("SMS2_REMIND"));
        map.put("beginTime", rs.getString("BEGIN_TIME"));
        map.put("endTime",rs.getString("END_TIME"));
        map.put("lastRemind", rs.getString("LAST_REMIND"));
        map.put("lastSms2Remind", rs.getString("LAST_SMS2_REMIND"));
        affairList.add(map);
      }  
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return affairList;
  }
  public void deleteAffair(Connection dbConn,String seqIds)throws Exception{ 
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_affairs where SEQ_ID in(" + seqIds+")";
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
     YHDBUtility.close(stmt, rs, log);
    }
  }
  /*
   * 判断结束日期是否为空
   */
  public boolean checkEndTime(Connection dbConn,String userId,String[] str ) throws Exception{
     Statement stmt = null;
     ResultSet rs = null;
     String sql="";
     try {
       stmt = dbConn.createStatement();
       stmt.executeUpdate(sql);
     }catch(Exception ex) {
       throw ex;
     }finally {
      YHDBUtility.close(stmt, rs, log);
     }
      return true;
   }

}

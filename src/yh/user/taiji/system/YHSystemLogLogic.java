package yh.user.taiji.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.util.db.YHDBUtility;

public class YHSystemLogLogic {
  public List<Map>  getSystemLogByDate(Connection conn , String beginDate,String endDate) throws Exception {
    String query = "select * from oa_sys_log where TYPE='1' ";
    query += this.getQueryDate(beginDate, endDate);
    Statement stm = null;
    ResultSet rs = null;
    List<Map> list = new ArrayList();
    try {
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stm.executeQuery(query);
      while (rs.next()) {
        //String content = rs.getString("REMARK");
        int userId = rs.getInt("USER_ID");
        Timestamp time = rs.getTimestamp("TIME");
        String type = rs.getString("TYPE");
        String query2 = "select USER_NAME from PERSON WHERE SEQ_ID=" + userId;
        Statement stm2 = null;
        ResultSet rs2 = null;
        String userName = "";
        try {
          stm2 = conn.createStatement();
          rs2 = stm2.executeQuery(query2);
          if (rs2.next()) {
            userName = rs2.getString("USER_NAME");
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null);
        }
        String content = userName + "登陆了系统";
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("time", time);
        map.put("content", content);
        map.put("mod", "system");
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return list; 
  }
  public String[][] getLogByDate(Connection conn , String beginDate,String endDate , int start , int length) throws Exception {
    List<Map> sys = this.getSystemLogByDate(conn, beginDate, endDate);
    List<Map> workflow = this.getWorkflowLogByDate(conn, beginDate, endDate);
    sys.addAll(workflow);
    Collections.sort(sys, new YHSysLogComparaor());
    List<Map> list = new ArrayList();
    if (start < 0) {
      start = 0;
    }
    if (sys.size() > start) {
      int to = sys.size();
      if (length < to && length != 0) {
        to = length; 
      } 
      list = sys.subList(start, to);
    }
    String[][] result =  this.listToArray(list);
    return result;
  }
  
  public List<Map> getWorkflowLogByDate(Connection conn , String beginDate,String endDate ) throws Exception {
    String query = "select * from oa_fl_run_log where 1=1 ";
    query += this.getQueryDate(beginDate, endDate);
    Statement stm = null;
    ResultSet rs = null;
    List<Map> list = new ArrayList();
    try {
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stm.executeQuery(query);
      
      while (rs.next()) {
        String content = rs.getString("CONTENT");
        int userId = rs.getInt("USER_ID");
        Timestamp time = rs.getTimestamp("TIME");
        //SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       // String timeStr = sd.format(time);
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("time", time);
        map.put("content", content);
        map.put("mod", "workflow");
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return list;
  }
  public String getQueryDate(String beginDate,String endDate) throws Exception {
    String query = "";
    if(beginDate != null && !"".equals(beginDate)){
      String dbDateF = YHDBUtility.getDateFilter("TIME", beginDate, " >= ");
      query +=  " and " + dbDateF;
    }
    if(endDate != null && !"".equals(endDate)){
      String dbDateF = YHDBUtility.getDateFilter("TIME", endDate, " <= ");
      query += " and " + dbDateF;
    }
    return query;
  }
  public  int getLogCountByDate(Connection conn , String beginDate,String endDate) throws Exception {
    int workflow = this.getWorkflowLogCountByDate(conn, beginDate, endDate);
    int sys = this.getSystemLogCountDate(conn, beginDate, endDate);
    return sys + workflow;
  }
  public  int getWorkflowLogCountByDate(Connection conn , String beginDate,String endDate) throws Exception {
    String query = "select * from oa_fl_run_log where 1=1 ";
    query += this.getQueryDate(beginDate, endDate);
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stm.executeQuery(query);
      rs.last(); 
      int len = rs.getRow();
      return len;
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
  }
  public int getSystemLogCountDate(Connection conn , String beginDate,String endDate) throws Exception {
    String query = "select * from oa_sys_log where TYPE='1' ";
    query += this.getQueryDate(beginDate, endDate);
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stm.executeQuery(query);
      rs.last(); 
      int len = rs.getRow();
      return len;
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
  }
  private String[][] listToArray(List<Map> list) {
    int widCount =  list.size();
    int count = 4;
    String[][] arrayList = new String[widCount][count];
    for (int i = 0 ;i < list.size() ;i++) {
      Map map = list.get(i);
      String content = (String)map.get("content");
      arrayList[i][0] = content;
      String mod = (String)map.get("mod");
      arrayList[i][1] = mod;
      Timestamp time = (Timestamp)map.get("time") ;
      SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      String timeStr = sd.format(time);
      arrayList[i][2] = timeStr;
      String userId = (Integer)map.get("userId")  + "";
      arrayList[i][3] = userId;
    }
    return arrayList;
  }
}

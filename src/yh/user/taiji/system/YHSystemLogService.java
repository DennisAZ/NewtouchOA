package yh.user.taiji.system;

import java.sql.Connection;
import java.sql.SQLException;

import yh.user.api.core.db.YHDbconnWrap;

public class YHSystemLogService {
//  public String[][] getLogByDate(String beginDate,String endDate){
//    Connection dbConn = null;
//    try {
//      dbConn = YHDbHelp.getSysDbConn();
//      YHFlowRunLogLogic logic = new YHFlowRunLogLogic();
//      String[][] list = logic.getLogByDate(dbConn, beginDate, endDate, 0, 0);
//      return list;
//    } catch (Exception e) {
//      e.printStackTrace();
//    } finally {
//      if (dbConn != null) {
//        try {
//          dbConn.close();
//        } catch (SQLException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//    return null;
//  }
  
  public String[][] getLogByDate(int start,int length, String beginDate,String endDate){
    YHDbconnWrap dbUtil = new YHDbconnWrap();
    Connection dbConn = null;
    try {      
      dbConn = dbUtil.getSysDbConn();
      YHSystemLogLogic logic = new YHSystemLogLogic();
      String[][] list = logic.getLogByDate(dbConn, beginDate, endDate, start, length);
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      dbUtil.closeAllDbConns();
    }
    return null;
  }
  public int getLogCountByDate(String beginDate,String endDate){
    YHDbconnWrap dbUtil = new YHDbconnWrap();
    Connection dbConn = null;
    int result = 0 ;
    try {
      dbConn = dbUtil.getSysDbConn();
      YHSystemLogLogic logic = new YHSystemLogLogic();
      result = logic.getLogCountByDate(dbConn, beginDate, endDate);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      dbUtil.closeAllDbConns();
    }
    return result;
  }
}

package yh.core.funcs.system.attendance.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHManageDataLogic {
  private static Logger log = Logger.getLogger(YHManageDataLogic.class);
  public void deleteDutyDate(Connection dbConn,String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_attendance_duty where ";
    if(minTime.equals("") && maxTime.equals("")){
      sql = "delete from oa_attendance_duty" ;
    }else if(!minTime.equals("") && !maxTime.equals("")){
      String temp1 = YHDBUtility.getDateFilter("REGISTER_TIME", minTime, ">=");
      String temp2 = YHDBUtility.getDateFilter("REGISTER_TIME", maxTime, "<=");
      sql = sql + temp1 + " and " + temp2;
    }else{
      if(!minTime.equals("")){
        String temp = YHDBUtility.getDateFilter("REGISTER_TIME", minTime, ">=");
        sql = sql  + temp;
      }
      if(!maxTime.equals("")){
        String temp = YHDBUtility.getDateFilter("REGISTER_TIME", maxTime, "<=");
        sql = sql + temp;
      }
    }
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
  public void deleteDutyDate(Connection dbConn,String userIds, String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String newUserId = "";
    String[] userIdArray = userIds.split(",");
    for (int i = 0; i < userIdArray.length; i++) {
      newUserId = newUserId + "'" + userIdArray[i] + "'," ; 
    }
    if(userIdArray.length>0){
      newUserId = newUserId.substring(0, newUserId.length()-1);
    }
    String sql = "delete from oa_attendance_duty where USER_ID in(" +newUserId + ")" ;
    if(!minTime.equals("")){
      String temp = YHDBUtility.getDateFilter("REGISTER_TIME", minTime, ">=");
      sql = sql + " and " + temp;
    }
    if(!maxTime.equals("")){
      String temp = YHDBUtility.getDateFilter("REGISTER_TIME", maxTime, "<=");
      sql = sql + " and " + temp;
    }
    try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public void deleteOutDate(Connection dbConn,String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_attendance_out where ";
    if(minTime.equals("") && maxTime.equals("")){
      sql = "delete from oa_attendance_out" ;
    }else if(!minTime.equals("") && !maxTime.equals("")){
      String temp1 = YHDBUtility.getDateFilter("SUBMIT_TIME", minTime, ">=");
      String temp2 = YHDBUtility.getDateFilter("SUBMIT_TIME", maxTime, "<=");
      sql = sql + temp1 + " and " + temp2;
    }else{
      if(!minTime.equals("")){
        String temp = YHDBUtility.getDateFilter("SUBMIT_TIME", minTime, ">=");
        sql = sql + temp;
      }
      if(!maxTime.equals("")){
        String temp = YHDBUtility.getDateFilter("SUBMIT_TIME", maxTime, "<=");
        sql = sql + temp;
      }
    }
    try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public void deleteOutDate(Connection dbConn,String userIds, String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String newUserId = "";
    String[] userIdArray = userIds.split(",");
    for (int i = 0; i < userIdArray.length; i++) {
      newUserId = newUserId + "'" + userIdArray[i] + "'," ; 
    }
    if(userIdArray.length>0){
      newUserId = newUserId.substring(0, newUserId.length()-1);
    }
    String sql = "delete from oa_attendance_out where USER_ID in(" +newUserId + ")" ;
    if(!minTime.equals("")){
      String temp = YHDBUtility.getDateFilter("SUBMIT_TIME", minTime, ">=");
      sql = sql + " and " + temp;
    }
    if(!maxTime.equals("")){
      String temp = YHDBUtility.getDateFilter("SUBMIT_TIME", maxTime, "<=");
      sql = sql + " and " + temp;
    }
    try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public void deleteLeaveDate(Connection dbConn,String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_attendance_off where ";
    if(minTime.equals("") && maxTime.equals("")){
      sql = "delete from oa_attendance_off" ;
    }else if(!minTime.equals("") && !maxTime.equals("")){
      String temp1 = YHDBUtility.getDateFilter("LEAVE_DATE1", minTime, ">=");
      String temp2 = YHDBUtility.getDateFilter("LEAVE_DATE2", maxTime, "<=");
      sql = sql + temp1 + " and " + temp2;
    }else{
      if(!minTime.equals("")){
        String temp = YHDBUtility.getDateFilter("LEAVE_DATE1", minTime, ">=");
        sql = sql + temp;
      }
      if(!maxTime.equals("")){
        String temp = YHDBUtility.getDateFilter("LEAVE_DATE2", maxTime, "<=");
        sql = sql + temp;
      }
    }   
    try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public void deleteLeaveDate(Connection dbConn,String userIds, String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String newUserId = "";
    String[] userIdArray = userIds.split(",");
    for (int i = 0; i < userIdArray.length; i++) {
      newUserId = newUserId + "'" + userIdArray[i] + "'," ; 
    }
    if(userIdArray.length>0){
      newUserId = newUserId.substring(0, newUserId.length()-1);
    }
    String sql = "delete from oa_attendance_off where USER_ID in(" +newUserId + ")" ;
    if(!minTime.equals("")){
      String temp = YHDBUtility.getDateFilter("LEAVE_DATE1", minTime, ">=");
      sql = sql + " and " + temp;
    }
    if(!maxTime.equals("")){
      String temp = YHDBUtility.getDateFilter("LEAVE_DATE2", maxTime, "<=");
      sql = sql + " and " + temp;
    }
    try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public void deleteEvectionDate(Connection dbConn,String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_attendance_trip where ";
    if(minTime.equals("") && maxTime.equals("")){
      sql = "delete from oa_attendance_trip" ;
    }else if(!minTime.equals("") && !maxTime.equals("")){
      String temp1 = YHDBUtility.getDateFilter("EVECTION_DATE1", minTime, ">=");
      String temp2 = YHDBUtility.getDateFilter("EVECTION_DATE2", maxTime, "<=");
      sql = sql + temp1 + " and " + temp2;
    }else{
      if(!minTime.equals("")){
        String temp = YHDBUtility.getDateFilter("EVECTION_DATE1", minTime, ">=");
        sql = sql + temp;
      }
      if(!maxTime.equals("")){
        String temp = YHDBUtility.getDateFilter("EVECTION_DATE2", maxTime, "<=");
        sql = sql + temp;
      }
    }
    try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public void deleteEvectionDate(Connection dbConn,String userIds, String minTime,String maxTime)throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String newUserId = "";
    String[] userIdArray = userIds.split(",");
    for (int i = 0; i < userIdArray.length; i++) {
      newUserId = newUserId + "'" + userIdArray[i] + "'," ; 
    }
    if(userIdArray.length>0){
      newUserId = newUserId.substring(0, newUserId.length()-1);
    }
    String sql = "delete from oa_attendance_trip where USER_ID in(" +newUserId + ")" ;
    if(!minTime.equals("")){
      String temp = YHDBUtility.getDateFilter("EVECTION_DATE1", minTime, ">=");
      sql = sql + " and " + temp;
    }
    if(!maxTime.equals("")){
      String temp = YHDBUtility.getDateFilter("EVECTION_DATE2", maxTime, "<=");
      sql = sql + " and " + temp;
    }
    try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
}

package yh.core.funcs.attendance.manage.logic;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.personal.data.YHAttendDuty;
import yh.core.funcs.attendance.personal.logic.YHAttendDutyLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHManageQueryLogic {
  public static void main(String args[]) {
    Date da = new Date();
    System.out.println(da.getTime());
  }
  public String getDutyType(Connection conn)throws Exception{
      Statement stmt=null;
      ResultSet rs=null;
      String data="";
      try{
        
       String sql = "SELECT * from oa_attendance_conf order by seq_id";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       while(rs.next()){
         int type=rs.getInt("SEQ_ID");
         String name=rs.getString("DUTY_NAME");
         data+="{type:'"+type+"',name:'"+name+"'},";  
       }
        
        
      }catch(Exception ex){
        ex.printStackTrace();
      }finally{
        YHDBUtility.close(stmt, rs, null);
      }
     if(data.endsWith(",")){
       data=data.substring(0,data.length()-1);
     }
   return data;
  }
  

  public String getDeptDuty(Connection conn,Map request) throws Exception{
    String query = "SELECT PARA_VALUE from SYS_PARA where PARA_NAME='NO_DUTY_USER'";
    Statement stmt2=null;
    ResultSet rs2=null;
    String paraValue = "";
    try {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery(query);
      if (rs2.next()) {
        paraValue = rs2.getString("PARA_VALUE");
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt2, rs2, null);
    } 
    
    
    StringBuffer sb = new StringBuffer("[");
    String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
    String dutyType = request.get("dutyType") == null ? null : ((String[]) request.get("dutyType"))[0];
    String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
    String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
    Date start = null;
    Date end = null;
    if (startDate != null) {
      start = YHUtility.parseDate(startDate);
    }
    if (endDate != null) {
      end = YHUtility.parseDate(endDate);
    }
    //---- 取规定上下班时间 -----
     String query2  = "SELECT * from oa_attendance_conf";
     Statement stmt3=null;
     ResultSet rs3=null;
     Map attendConfig = new HashMap();
     Map regCount = new HashMap();
     try {
       stmt3 = conn.createStatement();
       rs3 = stmt3.executeQuery(query2);
       while (rs3.next()) {
         int seqId = rs3.getInt("SEQ_ID");
         String dutyName = rs3.getString("DUTY_NAME");
         String general = rs3.getString("GENERAL");
         
         Map map =new HashMap();
         map.put("SEQ_ID", seqId);
         map.put("DUTY_NAME", dutyName);
         map.put("GENERAL", general);
         map.put("DUTY_TIME1",rs3.getString("DUTY_TIME1"));
         map.put("DUTY_TIME2",rs3.getString("DUTY_TIME2"));
         map.put("DUTY_TIME3",rs3.getString("DUTY_TIME3"));
         map.put("DUTY_TIME4",rs3.getString("DUTY_TIME4"));
         map.put("DUTY_TIME5",rs3.getString("DUTY_TIME5"));
         map.put("DUTY_TIME6",rs3.getString("DUTY_TIME6"));

         map.put("DUTY_TYPE1",rs3.getString("DUTY_TYPE1"));
         map.put("DUTY_TYPE2",rs3.getString("DUTY_TYPE2"));
         map.put("DUTY_TYPE3",rs3.getString("DUTY_TYPE3"));
         map.put("DUTY_TYPE4",rs3.getString("DUTY_TYPE4"));
         map.put("DUTY_TYPE5",rs3.getString("DUTY_TYPE5"));
         map.put("DUTY_TYPE6",rs3.getString("DUTY_TYPE6"));
         
         int c = 0;
         int dutyOnTimes = 0;
         int dutyOffTimes = 0;
         for (int i = 1 ;i <= 6 ;i++) {
           String dutyTimeI = rs3.getString("DUTY_TIME" + i);
           String dutyTypeI = rs3.getString("DUTY_TYPE" + i);
           if (YHUtility.isNullorEmpty(dutyTimeI)) {
             continue;
           }
           c++;
           if ("1".equals(dutyTypeI)) {
             dutyOnTimes++;
           } else {
             dutyOffTimes++;
           }
         }
         regCount.put(seqId + "", c);
         map.put("DUTY_ON_TIMES", dutyOnTimes);
         map.put("DUTY_OFF_TIMES", dutyOffTimes);
         attendConfig.put(seqId + "", map);
       }
     }catch(Exception ex){
       throw ex;
     }finally{
       YHDBUtility.close(stmt3, rs3, null);
     } 
     
     String query3 = "SELECT * from PERSON,USER_PRIV,oa_department as oa_department  where PERSON.NOT_LOGIN='0' and "+ YHDBUtility.findNoInSet(paraValue, "PERSON.SEQ_ID") +" and oa_department.SEQ_ID=PERSON.DEPT_ID ";
     String deptChildId= "";
     if(!"ALL_DEPT".equals(dept) 
         && !YHUtility.isNullorEmpty(dept)){
       deptChildId=this.getDeptChildId(conn, dept);
       if (!"".equals(deptChildId)) {
         if (deptChildId.endsWith(",")) {
           deptChildId += dept + ""; 
         } else {
           deptChildId = deptChildId + "," + dept ;
         }
       } else {
         deptChildId = dept ;
       }
       query3 += " and oa_department.SEQ_ID in ("+ deptChildId +") ";
     }
     if (!"ALL_TYPE".equals(dutyType)) {
       query3 += " AND DUTY_TYPE = '" + dutyType + "'";
     }
     query3 += " and PERSON.USER_PRIV=USER_PRIV.SEQ_ID order by DEPT_NO,PRIV_NO,USER_NO,USER_NAME";
     int lineCount = 0;
     
     Statement stmt5=null;
     ResultSet rs5=null;
     int count = 0 ;
     try {
       stmt5 = conn.createStatement();
       rs5 = stmt5.executeQuery(query3);
       Map dutyMap = new HashMap();
       
       while (rs5.next() ) {
         int allHours = 0;
         int allMinites = 0;
         
         
         int seqId = rs5.getInt("SEQ_ID");
         String userName = rs5.getString("USER_NAME");
         String dutyTypeTmp = dutyType = rs5.getString("DUTY_TYPE");
         String userDeptName = rs5.getString("DEPT_NAME");
         
         Map duty = (Map)attendConfig.get(dutyTypeTmp);
         if (duty == null) {
           continue;
         }
         String general = (String)duty.get("GENERAL");
         
         int dutyOnTimes = (Integer)duty.get("DUTY_ON_TIMES");
         int dutyOffTimes =  (Integer)duty.get("DUTY_OFF_TIMES");
         
         lineCount++;
         
         int prefectCount = 0;
         int earlyCount = 0;
         int lateCount = 0 ;
         int dutyOnCount = 0 ;
         int dutyOffCount = 0;
         int dutyOnTotal = 0 ;
         int dutyOffTotal = 0 ;
         int overOnCount = 0 ;
         int overOffCount = 0 ;
         String allHoursMinites = "";
         
         for (Date j = start ;  end.compareTo(j) >= 0 ;  j = new Date(j.getTime() + 24 * 3600 * 1000)) {
           String jj = YHUtility.getDateTimeStr(j);
           int week = j.getDay();
           int holiday = 0 ;
           int holiday1 = 0 ;
           
           if (YHWorkFlowUtility.findId(general, week + "")) {
             holiday = 1;
           }
           if (holiday == 0) {
             String query5 = "select count(*)  from oa_attendance_festival where  "+YHDBUtility.getDateFilter("BEGIN_DATE", jj,"<=") +" and " + YHDBUtility.getDateFilter("END_DATE", jj,">=");
             holiday = getCount( conn , query5);
             if (holiday > 0 ) {
               holiday1 = 1;
             }
           }
           if (holiday == 0) {
             String query5 = "select count(*) from oa_attendance_trip where   USER_ID='"
               +seqId+"' and ALLOW='1' and "
               +  YHDBUtility.getDateFilter("EVECTION_DATE1", jj,"<=") +" and " + YHDBUtility.getDateFilter("EVECTION_DATE2", jj,">=");
             
             query5 = "select count(*) from oa_attendance_trip where USER_ID='"
               +seqId+"' and ALLOW='1' and "+YHDBUtility.getDateFilter("EVECTION_DATE1", jj,"<=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE2", jj,">=");
             holiday = getCount( conn , query5);
             if (holiday > 0 ) {
               holiday1 = 1;
             }
           }
           
           if (holiday == 0) {
             String query5 = "select count(*) from oa_attendance_off where   USER_ID='"
               +seqId+"' and ALLOW='1' and "
               +  YHDBUtility.getDateFilter("LEAVE_DATE1", jj,"<=") +" and " + YHDBUtility.getDateFilter("LEAVE_DATE2", jj,">=");
             query5 = "select count(*) from oa_attendance_off where USER_ID='"
               +seqId+"' and ALLOW='1' and "+YHDBUtility.getSqlSubstr("LEAVE_DATE1", jj,0, 10,"<=")+" and "+YHDBUtility.getSqlSubstr("LEAVE_DATE2", jj,0, 10,">=");
             holiday = getCount( conn , query5);
             if (holiday > 0 ) {
               holiday1 = 1;
             }
           }
           if (holiday == 0) {
             dutyOnTotal += dutyOnTimes;
             dutyOffTotal += dutyOffTimes;
           }
           int perfectFlag = 0 ;
           String query6 = "SELECT * from oa_attendance_duty where USER_ID='"+seqId+"' and "+YHDBUtility.getDateFilter("REGISTER_TIME", jj,"=")+" order by REGISTER_TIME";
           //query6 = "SELECT * from oa_attendance_duty where USER_ID='"+seqId+"' and to_days(REGISTER_TIME)=to_days('"+jj+"') order by REGISTER_TIME";
           
           int oneDayCount = 0 ;
           Statement stmyh=null;
           ResultSet rs9=null;
           Map oneDayReg = new HashMap();
           try {
             stmyh = conn.createStatement();
             rs9 = stmyh.executeQuery(query6);
             while (rs9.next()) {
               oneDayCount++;
               Date d = rs9.getTimestamp("REGISTER_TIME");
               oneDayReg.put(oneDayCount, d);
               int dutyTypeCount =(Integer) regCount.get(dutyTypeTmp);
               if (oneDayCount == dutyTypeCount 
                   && dutyTypeCount %2 == 0 
                   && oneDayCount % 2 == 0
                   && dutyTypeCount > 1
                   && oneDayCount > 1
                 ) {
                 int cc = (Integer)regCount.get(dutyTypeTmp);
                 Date dd = (Date)oneDayReg.get(cc);
                 Date dd2 = (Date)oneDayReg.get(cc - 1);
                 long cha5 = dd.getTime();
                 long cha4 = dd2.getTime();
                 long cha1 = cha5 - cha4;
                 long cha2 = 0 ;
                 long cha3 = 0 ;
                
                 if ( (Integer)regCount.get(dutyTypeTmp) - 2 > 1) {
                   dd = (Date)oneDayReg.get(cc - 2);
                   dd2 = (Date)oneDayReg.get(cc - 3 );
                   long cha7 = dd.getTime();
                   long cha8 = dd2.getTime();
                   cha2 = cha7 - cha8;
                 } 
                 if ( (Integer)regCount.get(dutyTypeTmp) - 4 > 1) {
                   dd = (Date)oneDayReg.get(cc - 4);
                   dd2 = (Date)oneDayReg.get(cc - 5 );
                   long cha7 = dd.getTime();
                   long cha8 = dd2.getTime();
                   cha3 = cha7 - cha8;
                 } 
                 allMinites += cha1 + cha2 + cha3;
               }
               String registerType = rs9.getString("REGISTER_TYPE");
               String dutyTime = (String)duty.get("DUTY_TIME" + registerType);
               String dutyType11 = (String)duty.get("DUTY_TYPE" + registerType);
               if ("".equals(dutyTime)) {
                 continue;
               }
               if ("1".equals(dutyType11)) {
                 if (this.compareTime(d, dutyTime) < 1) {
                   perfectFlag++;
                 }
                 
                 if (holiday > 0 && holiday1 != 1) {
                   overOnCount++;
                   continue;
                 }
                 
                 dutyOnCount++;
                 if  (this.compareTime(d, dutyTime) == 1) {
                   lateCount++;
                 }
               }
               if ("2".equals(dutyType11)) {
                 if (this.compareTime(d, dutyTime) > -1) {
                   perfectFlag++;
                 }
                 if (holiday > 0 && holiday != 1) {
                   overOffCount++;
                   continue;
                 }
                 dutyOffCount++;
                 if (this.compareTime(d, dutyTime) == -1) {
                   earlyCount++;
                 }
               }
             }
           }catch(Exception ex){
             throw ex;
           }finally{
             YHDBUtility.close(stmyh, rs9, null);
           } 
           if (perfectFlag >= dutyOnTimes + dutyOffTimes) {
             prefectCount++;
           }
         }
         
         allMinites = allMinites / 1000;
         allHours = allMinites / 3600 ;
         int hour1 = allMinites % 3600;
         int minite = hour1 / 60;
         if (allHours != 0 || minite != 0) {
           allHoursMinites = allHours + "时" + minite + "分";
         } else {
           allHoursMinites = "0";
         }
         sb.append("{");
         sb.append("userName:\"" + YHUtility.encodeSpecial(userName) + "\"");
         sb.append(",deptName:\"" + YHUtility.encodeSpecial(userDeptName) + "\"");
         sb.append(",perfectCount:\"" + prefectCount+ "\"");
         sb.append(",allHoursMinites:\"" + allHoursMinites  + "\"");
         sb.append(",lateCount:\"" + lateCount  + "\"");
         int tmp = dutyOnTotal - dutyOnCount ;
         sb.append(",on:" + ((tmp < 0 ) ? "0" : tmp) );
         sb.append(",earlyCount:" + earlyCount );
         tmp = dutyOffTotal - dutyOffCount;
         sb.append(",off:"  + ((tmp < 0 ) ? "0" : tmp) );
         sb.append(",overOnCount:"  + overOnCount);
         sb.append(",overOffCount:"  + overOffCount);
         sb.append(",userId:"  + seqId);
         sb.append(",date1:\"" + startDate + "\"");
         sb.append(",date2:\"" + startDate + "\"");
         sb.append("},");
         count++;
       }
     }catch(Exception ex){
       throw ex;
     }finally{
       YHDBUtility.close(stmt5, rs5, null);
     } 
     if (count > 0 ) {
       sb.deleteCharAt(sb.length() - 1);
     }
     sb.append("]");
     return sb.toString();
  }
  public List<Map<String, String>>  expDeptDuty(Connection conn,Map request) throws Exception{
    String query = "SELECT PARA_VALUE from SYS_PARA where PARA_NAME='NO_DUTY_USER'";
    Statement stmt2=null;
    ResultSet rs2=null;
    String paraValue = "";
    try {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery(query);
      if (rs2.next()) {
        paraValue = rs2.getString("PARA_VALUE");
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt2, rs2, null);
    } 
    
    
    StringBuffer sb = new StringBuffer("[");
    String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
    String dutyType = request.get("dutyType") == null ? null : ((String[]) request.get("dutyType"))[0];
    String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
    String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
    Date start = null;
    Date end = null;
    if (startDate != null) {
      start = YHUtility.parseDate(startDate);
    }
    if (endDate != null) {
      end = YHUtility.parseDate(endDate);
    }
    //---- 取规定上下班时间 -----
     String query2  = "SELECT * from oa_attendance_conf";
     Statement stmt3=null;
     ResultSet rs3=null;
     Map attendConfig = new HashMap();
     Map regCount = new HashMap();
     try {
       stmt3 = conn.createStatement();
       rs3 = stmt3.executeQuery(query2);
       while (rs3.next()) {
         int seqId = rs3.getInt("SEQ_ID");
         String dutyName = rs3.getString("DUTY_NAME");
         String general = rs3.getString("GENERAL");
         
         Map map =new HashMap();
         map.put("SEQ_ID", seqId);
         map.put("DUTY_NAME", dutyName);
         map.put("GENERAL", general);
         map.put("DUTY_TIME1",rs3.getString("DUTY_TIME1"));
         map.put("DUTY_TIME2",rs3.getString("DUTY_TIME2"));
         map.put("DUTY_TIME3",rs3.getString("DUTY_TIME3"));
         map.put("DUTY_TIME4",rs3.getString("DUTY_TIME4"));
         map.put("DUTY_TIME5",rs3.getString("DUTY_TIME5"));
         map.put("DUTY_TIME6",rs3.getString("DUTY_TIME6"));

         map.put("DUTY_TYPE1",rs3.getString("DUTY_TYPE1"));
         map.put("DUTY_TYPE2",rs3.getString("DUTY_TYPE2"));
         map.put("DUTY_TYPE3",rs3.getString("DUTY_TYPE3"));
         map.put("DUTY_TYPE4",rs3.getString("DUTY_TYPE4"));
         map.put("DUTY_TYPE5",rs3.getString("DUTY_TYPE5"));
         map.put("DUTY_TYPE6",rs3.getString("DUTY_TYPE6"));
         
         int c = 0;
         int dutyOnTimes = 0;
         int dutyOffTimes = 0;
         for (int i = 1 ;i <= 6 ;i++) {
           String dutyTimeI = rs3.getString("DUTY_TIME" + i);
           String dutyTypeI = rs3.getString("DUTY_TYPE" + i);
           if (YHUtility.isNullorEmpty(dutyTimeI)) {
             continue;
           }
           c++;
           if ("1".equals(dutyTypeI)) {
             dutyOnTimes++;
           } else {
             dutyOffTimes++;
           }
         }
         regCount.put(seqId + "", c);
         map.put("DUTY_ON_TIMES", dutyOnTimes);
         map.put("DUTY_OFF_TIMES", dutyOffTimes);
         attendConfig.put(seqId + "", map);
       }
     }catch(Exception ex){
       throw ex;
     }finally{
       YHDBUtility.close(stmt3, rs3, null);
     } 
     List<Map<String, String>> list = new ArrayList();
     String query3 = "SELECT * from PERSON,USER_PRIV,oa_department as oa_department where PERSON.NOT_LOGIN='0' and "+ YHDBUtility.findNoInSet(paraValue, "PERSON.SEQ_ID") +" and DEPARTMENT.SEQ_ID=PERSON.DEPT_ID ";
     String deptChildId="";
     if(!"ALL_DEPT".equals(dept)){
       deptChildId=this.getDeptChildId(conn, dept);
       if (!"".equals(deptChildId)) {
         if (deptChildId.endsWith(",")) {
           deptChildId += dept + ""; 
         } else {
           deptChildId = deptChildId + "," + dept ;
         }
       }else {
         deptChildId = dept;
       }
       query3 += " and oa_department.SEQ_ID in ("+ deptChildId +") ";
     }
     if (!"ALL_TYPE".equals(dutyType)) {
       query3 += " AND DUTY_TYPE = '" + dutyType + "'";
     }
     query3 += " and PERSON.USER_PRIV=USER_PRIV.SEQ_ID order by DEPT_NO,PRIV_NO,USER_NO,USER_NAME";
     int lineCount = 0;
     
     Statement stmt5=null;
     ResultSet rs5=null;
     int count = 0 ;
     try {
       stmt5 = conn.createStatement();
       rs5 = stmt5.executeQuery(query3);
       Map dutyMap = new HashMap();
       
       while (rs5.next() ) {
         int allHours = 0;
         int allMinites = 0;
         
         
         int seqId = rs5.getInt("SEQ_ID");
         String userName = rs5.getString("USER_NAME");
         String dutyTypeTmp = dutyType = rs5.getString("DUTY_TYPE");
         String userDeptName = rs5.getString("DEPT_NAME");
         
         Map duty = (Map)attendConfig.get(dutyTypeTmp);
         if (duty == null) {
           continue;
         }
         String general = (String)duty.get("GENERAL");
         
         int dutyOnTimes = (Integer)duty.get("DUTY_ON_TIMES");
         int dutyOffTimes =  (Integer)duty.get("DUTY_OFF_TIMES");
         
         lineCount++;
         
         int prefectCount = 0;
         int earlyCount = 0;
         int lateCount = 0 ;
         int dutyOnCount = 0 ;
         int dutyOffCount = 0;
         int dutyOnTotal = 0 ;
         int dutyOffTotal = 0 ;
         int overOnCount = 0 ;
         int overOffCount = 0 ;
         String allHoursMinites = "";
         
         for (Date j = start ;  end.compareTo(j) >= 0 ;  j = new Date(j.getTime() + 24 * 3600 * 1000)) {
           String jj = YHUtility.getDateTimeStr(j);
           int week = j.getDay();
           int holiday = 0 ;
           int holiday1 = 0 ;
           
           if (YHWorkFlowUtility.findId(general, week + "")) {
             holiday = 1;
           }
           if (holiday == 0) {
             String query5 = "select count(*)  from oa_attendance_festival where  "+YHDBUtility.getDateFilter("BEGIN_DATE", jj,"<=") +" and " + YHDBUtility.getDateFilter("END_DATE", jj,">=");
             holiday = getCount( conn , query5);
             if (holiday > 0 ) {
               holiday1 = 1;
             }
           }
           if (holiday == 0) {
             String query5 = "select count(*) from oa_attendance_trip where   USER_ID='"
               +seqId+"' and ALLOW='1' and "
               +  YHDBUtility.getDateFilter("EVECTION_DATE1", jj,"<=") +" and " + YHDBUtility.getDateFilter("EVECTION_DATE2", jj,">=");
             
//             query5 = "select count(*) from oa_attendance_trip where USER_ID='"
//               +seqId+"' and ALLOW='1' and to_days(EVECTION_DATE1)<=to_days('"+jj+"') and to_days(EVECTION_DATE2)>=to_days('"+jj+"')";
             holiday = getCount( conn , query5);
             if (holiday > 0 ) {
               holiday1 = 1;
             }
           }
           
           if (holiday == 0) {
             String query5 = "select count(*) from oa_attendance_off where   USER_ID='"
               +seqId+"' and ALLOW='1' and "
               +  YHDBUtility.getDateFilter("LEAVE_DATE1", jj,"<=") +" and " + YHDBUtility.getDateFilter("LEAVE_DATE2", jj,">=");
//             query5 = "select count(*) from oa_attendance_off where USER_ID='"
//               +seqId+"' and ALLOW='1' and left(LEAVE_DATE1,10)<='"+jj+"' and left(LEAVE_DATE2,10)>='"+jj+"'";
             holiday = getCount( conn , query5);
             if (holiday > 0 ) {
               holiday1 = 1;
             }
           }
           if (holiday == 0) {
             dutyOnTotal += dutyOnTimes;
             dutyOffTotal += dutyOffTimes;
           }
           int perfectFlag = 0 ;
           String query6 = "SELECT * from oa_attendance_duty where USER_ID='"+seqId+"' and "+YHDBUtility.getDateFilter("REGISTER_TIME", jj,"=")+" order by REGISTER_TIME";
//           query6 = "SELECT * from oa_attendance_duty where USER_ID='"+seqId+"' and to_days(REGISTER_TIME)=to_days('"+jj+"') order by REGISTER_TIME";
           int oneDayCount = 0 ;
           Statement stmyh=null;
           ResultSet rs9=null;
           Map oneDayReg = new HashMap();
           try {
             stmyh = conn.createStatement();
             rs9 = stmyh.executeQuery(query6);
             while (rs9.next()) {
               oneDayCount++;
               Date d = rs9.getTimestamp("REGISTER_TIME");
               oneDayReg.put(oneDayCount, d);
               int dutyTypeCount =(Integer) regCount.get(dutyTypeTmp);
               if (oneDayCount == dutyTypeCount 
                   && dutyTypeCount %2 == 0 
                   && oneDayCount % 2 == 0
                   && dutyTypeCount > 1
                   && oneDayCount > 1
                 ) {
                 int cc = (Integer)regCount.get(dutyTypeTmp);
                 Date dd = (Date)oneDayReg.get(cc);
                 Date dd2 = (Date)oneDayReg.get(cc - 1);
                 long cha5 = dd.getTime();
                 long cha4 = dd2.getTime();
                 long cha1 = cha5 - cha4;
                 long cha2 = 0 ;
                 long cha3 = 0 ;
                
                 if ( (Integer)regCount.get(dutyTypeTmp) - 2 > 1) {
                   dd = (Date)oneDayReg.get(cc - 2);
                   dd2 = (Date)oneDayReg.get(cc - 3 );
                   long cha7 = dd.getTime();
                   long cha8 = dd2.getTime();
                   cha2 = cha7 - cha8;
                 } 
                 if ( (Integer)regCount.get(dutyTypeTmp) - 4 > 1) {
                   dd = (Date)oneDayReg.get(cc - 4);
                   dd2 = (Date)oneDayReg.get(cc - 5 );
                   long cha7 = dd.getTime();
                   long cha8 = dd2.getTime();
                   cha3 = cha7 - cha8;
                 } 
                 allMinites += cha1 + cha2 + cha3;
               }
               String registerType = rs9.getString("REGISTER_TYPE");
               String dutyTime = (String)duty.get("DUTY_TIME" + registerType);
               String dutyType11 = (String)duty.get("DUTY_TYPE" + registerType);
               if ("".equals(dutyTime)) {
                 continue;
               }
               if ("1".equals(dutyType11)) {
                 if (this.compareTime(d, dutyTime) < 1) {
                   perfectFlag++;
                 }
                 
                 if (holiday > 0 && holiday1 != 1) {
                   overOnCount++;
                   continue;
                 }
                 
                 dutyOnCount++;
                 if  (this.compareTime(d, dutyTime) == 1) {
                   lateCount++;
                 }
               }
               if ("2".equals(dutyType11)) {
                 if (this.compareTime(d, dutyTime) > -1) {
                   perfectFlag++;
                 }
                 if (holiday > 0 && holiday != 1) {
                   overOffCount++;
                   continue;
                 }
                 dutyOffCount++;
                 if (this.compareTime(d, dutyTime) == -1) {
                   earlyCount++;
                 }
               }
             }
           }catch(Exception ex){
             throw ex;
           }finally{
             YHDBUtility.close(stmyh, rs9, null);
           } 
           if (perfectFlag >= dutyOnTimes + dutyOffTimes) {
             prefectCount++;
           }
         }
         
         allMinites = allMinites / 1000;
         allHours = allMinites / 3600 ;
         int hour1 = allMinites % 3600;
         int minite = hour1 / 60;
         if (allHours != 0 || minite != 0) {
           allHoursMinites = allHours + "时" + minite + "分";
         } else {
           allHoursMinites = "0";
         }
         Map<String, String> m = new HashMap();
         m.put("userName" , userName);
         m.put("deptName" , userDeptName);
         m.put("perfectCount" , prefectCount + "");
         m.put("allHoursMinites" , allHoursMinites);
         m.put("lateCount" , lateCount + "");
         int tmp = dutyOnTotal - dutyOnCount ;
         m.put("on" , tmp + "");
         m.put("earlyCount" , earlyCount + "");
         tmp = dutyOffTotal - dutyOffCount;
         m.put("off" , tmp + "");
//         m.put("overOnCount" , overOnCount + "");
//         m.put("overOffCount" , overOffCount + "");
         list.add(m);
       }
     }catch(Exception ex){
       throw ex;
     }finally{
       YHDBUtility.close(stmt5, rs5, null);
     } 
     return list;
  }
  public int compareTime(Date date , String time) {
    String[] times = time.split(":");
    int hours = date.getHours();
    int seconds = date.getSeconds();
    int minutes = date.getMinutes();
    
    if (Integer.parseInt(times[0]) > hours) {
      return -1;
    } else if (Integer.parseInt(times[0]) < hours) {
      return 1;
    } else {
      if (Integer.parseInt(times[1]) > minutes) {
        return -1;
      } else if (Integer.parseInt(times[1]) < minutes) {
        return 1;
      } else {
        if (Integer.parseInt(times[2]) > seconds) {
          return -1;
        } else if (Integer.parseInt(times[2]) < seconds) {
          return 1;
        } else {
          return 0;
        }
      }
    }
    
  }
public String getEvection(Connection conn,Map request){
  Statement stmt=null;
  ResultSet rs=null;
  String data="";
  String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
  String dutyType = request.get("dutyType") == null ? null : ((String[]) request.get("dutyType"))[0];
  String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
  String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
  try{
    //获取子部门
     String deptChildId="";
      if(!"ALL_DEPT".equals(dept)){
        deptChildId=this.getDeptChildId(conn, dept);
      }
      if(YHUtility.isNullorEmpty(deptChildId)){
        dept+=",";
        dept+=deptChildId;
      }    
      String userIds="";
      userIds=this.getUserIdsByDept(conn,dept);
      if(!"".equals(userIds)){
        String  sql="select a.seq_id ,d.dept_name,p.user_name,a.EVECTION_DEST,a.REGISTER_IP,a.evection_date1,a.evection_date2,a.LEADER_ID,a.status from person p,oa_attendance_trip a,oa_department d where "
          +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") and a.allow='1' "
          +" and ( ("+YHDBUtility.getDateFilter("evection_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("evection_date1", endDate+" 23:59:59", "<=")+") "
          +" or  ("+YHDBUtility.getDateFilter("evection_date2", startDate, ">=")+" and "+YHDBUtility.getDateFilter("evection_date2", endDate+" 23:59:59", "<=")+") "
          +" or  ("+YHDBUtility.getDateFilter("evection_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("evection_date2", endDate+" 23:59:59", "<=")+") "
          +") order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);

        while(rs.next()){
         int seqId=rs.getInt("seq_id");
         String deptName=rs.getString("dept_name");
         String userName=rs.getString("user_name");
         String evectionDest=rs.getString("evection_dest");
         String registerIp=rs.getString("register_ip");
         String evectionDate1=rs.getString("evection_date1");
         String evectionDate2=rs.getString("evection_date2");
         String leaderId=rs.getString("leader_id");
         String status=rs.getString("status");
         leaderId=this.getUserNameById(conn,leaderId);
         data+="{seqId:'"+seqId+"',deptName:'"+deptName+"',userName:'"
               +userName+"',evectionDest:'"+evectionDest+"',registerIp:'"+registerIp+"',evectionDate1:'"
               +evectionDate1+"',evectionDate2:'"+evectionDate2+"',leaderId:'"+leaderId+"',status:'"+status+"'}";
         data+=",";
        }
      }

    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
   if(data.endsWith(",")){
     data=data.substring(0,data.length()-1);
   }
 return data;
}


public String getOut(Connection conn,Map request){
  Statement stmt=null;
  ResultSet rs=null;
  String data="";
  String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
  String dutyType = request.get("dutyType") == null ? null : ((String[]) request.get("dutyType"))[0];
  String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
  String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
  try{
    //获取子部门
     String deptChildId="";
      if(!"ALL_DEPT".equals(dept)){
        deptChildId=this.getDeptChildId(conn, dept);
      }
      if(YHUtility.isNullorEmpty(deptChildId)){
        dept+=",";
        dept+=deptChildId;
      }    
      String userIds="";
      userIds=this.getUserIdsByDept(conn,dept);
      if(!"".equals(userIds)){
        String  sql="select a.seq_id ,d.dept_name,p.user_name,a.CREATE_DATE,a.out_type,a.REGISTER_IP,a.SUBMIT_TIME,a.OUT_TIME1,a.OUT_TIME2,a.LEADER_ID,a.status ,a.allow from person p,oa_attendance_out a,oa_department d where "
          +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") "
          +" and "+YHDBUtility.getDateFilter("submit_time", startDate, " >= ")+" and "+YHDBUtility.getDateFilter("submit_time", endDate+" 23:59:59", "<=")
          +" order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);

        while(rs.next()){
         int seqId=rs.getInt("seq_id");
         String deptName=rs.getString("dept_name");
         String userName=rs.getString("user_name");
         String createDate=rs.getString("CREATE_DATE");
         String outType=rs.getString("out_type");
         String registerIp=rs.getString("REGISTER_IP");
         String submitTime=rs.getString("SUBMIT_TIME");
         String outTime1=rs.getString("OUT_TIME1");
         String outTime2=rs.getString("OUT_TIME2");
         String leaderId=rs.getString("LEADER_ID");
         String status=rs.getString("status");
         String allow=rs.getString("allow");
         leaderId=this.getUserNameById(conn,leaderId);
         data+="{seqId:'"+seqId+"',deptName:'"+deptName+"',userName:'"
               +userName+"',createDate:'"+createDate+"',outType:'"+outType+"',registerIp:'"
               +registerIp+"',leaderId:'"+leaderId+"',allow:'"+allow+"',status:'"+status+"',submitTime:'"+submitTime+"',outTime1:'"+outTime1+"',outTime2:'"+outTime2+"'}";
         data+=",";
        }
      }

    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
   if(data.endsWith(",")){
     data=data.substring(0,data.length()-1);
   }
 return data;
}

public String getLeave(Connection conn,Map request){
  Statement stmt=null;
  ResultSet rs=null;
  String data="";
  String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
  String dutyType = request.get("dutyType") == null ? null : ((String[]) request.get("dutyType"))[0];
  String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
  String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
  try{
    //获取子部门
     String deptChildId="";
      if(!"ALL_DEPT".equals(dept)){
        deptChildId=this.getDeptChildId(conn, dept);
      }
      if(YHUtility.isNullorEmpty(deptChildId)){
        dept+=",";
        dept+=deptChildId;
      }    
      String userIds="";
      userIds=this.getUserIdsByDept(conn,dept);
      if(!"".equals(userIds)){
        String  sql="select a.seq_id ,d.dept_name,p.user_name,a.leave_type,a.ANNUAL_LEAVE,a.REGISTER_IP,a.LEAVE_DATE1,a.LEAVE_DATE2,a.LEADER_ID,a.STATUS from person p,oa_attendance_off a,oa_department d where "
          +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") and a.allow='1' "
          +" and ( ("+YHDBUtility.getDateFilter("leave_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("leave_date1", endDate+" 23:59:59", "<=")+") "
          +" or  ("+YHDBUtility.getDateFilter("leave_date2", startDate, ">=")+" and "+YHDBUtility.getDateFilter("leave_date2", endDate+" 23:59:59", "<=")+") "
          +" or  ("+YHDBUtility.getDateFilter("leave_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("leave_date2", endDate+" 23:59:59", "<=")+") "
          +" ) order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);

        while(rs.next()){
         int seqId=rs.getInt("seq_id");
         String deptName=rs.getString("dept_name");
         String userName=rs.getString("user_name");
         String leaveType=rs.getString("leave_type");
         String annualLeave=rs.getString("annual_leave");
         String registerIp=rs.getString("register_ip");
         String leaveDate1=rs.getString("leave_date1");
         String leaveDate2=rs.getString("leave_date2");
         String leaderId=rs.getString("leader_id");
         String status=rs.getString("status");
         leaderId=this.getUserNameById(conn,leaderId);
         data+="{seqId:'"+seqId+"',deptName:'"+deptName+"',userName:'"
               +userName+"',leaveType:'"+leaveType+"',annualLeave:'"+annualLeave+"',registerIp:'"+registerIp+"',leaveDate1:'"
               +leaveDate1+"',leaveDate2:'"+leaveDate2+"',leaderId:'"+leaderId+"',status:'"+status+"'}";
         data+=",";
        }
      }

    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
   if(data.endsWith(",")){
     data=data.substring(0,data.length()-1);
   }
 return data;
}


public String getOvertime(Connection conn,Map request){
  Statement stmt=null;
  ResultSet rs=null;
  String data="";
  String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
  String dutyType = request.get("dutyType") == null ? null : ((String[]) request.get("dutyType"))[0];
  String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
  String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
  try{
    //获取子部门
     String deptChildId="";
      if(!"ALL_DEPT".equals(dept)){
        deptChildId=this.getDeptChildId(conn, dept);
      }
      if(YHUtility.isNullorEmpty(deptChildId)){
        dept+=",";
        dept+=deptChildId;
      }    
      String userIds="";
      userIds=this.getUserIdsByDept(conn,dept);
      if(!"".equals(userIds)){
        String  sql="select a.seq_id ,d.dept_name,p.user_name,a.OVERTIME_TIME,a.OVERTIME_DESC,a.BEGIN_TIME,a.END_DATE,a.HOUR,a.LEADER_ID,a.STATUS from person p,oa_timeout_record a,oa_department d where "
          +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") and a.status in ('1','3') "
          +" and "+YHDBUtility.getDateFilter("begin_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("begin_time", endDate+" 23:59:59", "<=") 
          +" order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);

        while(rs.next()){
         int seqId=rs.getInt("seq_id");
         String deptName=rs.getString("dept_name");
         String userName=rs.getString("user_name");
         String overtimeTime=rs.getString("OVERTIME_TIME");
         String overtimeDesc=rs.getString("OVERTIME_DESC");
         String beginTime=rs.getString("BEGIN_TIME");
         String endTime=rs.getString("END_DATE");
         String hour=rs.getString("HOUR");
         String leaderId=rs.getString("leader_id");
         String status=rs.getString("status");
         leaderId=this.getUserNameById(conn,leaderId);
         data+="{seqId:'"+seqId+"',deptName:'"+deptName+"',userName:'"
               +userName+"',overtimeTime:'"+overtimeTime+"',overtimeDesc:'"+overtimeDesc+"',beginTime:'"+beginTime+"',endTime:'"
               +endTime+"',hour:'"+hour+"',leaderId:'"+leaderId+"',status:'"+status+"'}";
         data+=",";
        }
      }

    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
   if(data.endsWith(",")){
     data=data.substring(0,data.length()-1);
   }
 return data;
}


public String getUserNameById(Connection conn,String seqId)throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  String name="";
  try{
    String sql="select user_name from person where seq_id='"+seqId+"'";
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      name=rs.getString("user_name");
    }
    
  }catch(Exception ex){
    ex.printStackTrace();
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
  return name;
}


public void delEvection(Connection conn,String seqId)throws Exception {
  Statement stmt=null;
  try{
      String sql="delete from  oa_attendance_trip where seq_id='"+seqId+"'";
      stmt=conn.createStatement();
      stmt.execute(sql);
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, null, null);
    }
  
}

public void delOvertime(Connection conn,String seqId)throws Exception {
  Statement stmt=null;
  try{
      String sql="delete from  oa_timeout_record where seq_id='"+seqId+"'";
      stmt=conn.createStatement();
      stmt.execute(sql);
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, null, null);
    }
  
}



public void delLeave(Connection conn,String seqId)throws Exception {
  Statement stmt=null;
  try{
      String sql="delete from  oa_attendance_off where seq_id='"+seqId+"'";
      stmt=conn.createStatement();
      stmt.execute(sql);
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, null, null);
    }
  
}

public void delOut(Connection conn,String seqId)throws Exception {
  Statement stmt=null;
  try{
      String sql="delete  from oa_attendance_out where seq_id='"+seqId+"'";
      stmt=conn.createStatement();
      stmt.execute(sql);
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, null, null);
    }
  
}

public String getDutyJsonByUserIds(Connection conn,String userIds,String startDate,String endDate)throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  try{
    String userId[]=userIds.split(",");
    for(int i=0;i<userId.length;i++){
      Map<String,String> map=new HashMap();
      map=this.getDutyJson(conn,userId[i],startDate,endDate);
    }
    
  }catch(Exception ex){
    throw ex;
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
  return "";
}


public Map<String,String> getDutyJson(Connection conn,String userId,String startDate,String endDate)throws Exception{
  Statement stmt =null;
  ResultSet rs=null;
  Map<String,String> map=new HashMap();
  String name="";
  String deptId="";
  String dutyType="";
  String lateAndEarly="";
  try{
    String sql="select * from person where seq_id='"+userId+"'";
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);

    while(rs.next()){
      name=rs.getString("user_name");
      deptId=rs.getString("dept_id");
      dutyType=rs.getString("duty_type");
    }
    String deptName=this.getDeptName(conn,deptId);
    lateAndEarly=this.getLateCount(conn,userId,dutyType,startDate,endDate);
    String noRegisster=this.getNoRegister(conn,userId,dutyType,startDate,endDate);
    String LE[]=lateAndEarly.split(",");
    String NR[]=noRegisster.split(",");
    map.put("userName", name);
    map.put("deptName", deptName);
    map.put("late", LE[0]);
    map.put("early",LE[1]);
    map.put("WorkOn", NR[0]);
    map.put("WorkOff", NR[1]);
    
    
  }catch(Exception ex){
    throw ex;
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
  return map;
}
public String getLateCount(Connection conn,String userId,String Type,String startDate,String endDate)throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  Map<String ,String> map=new HashMap();
  int late=0,early=0;

  try{
    String sql="select * from oa_attendance_conf where seq_id='"+Type+"'";
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);
    while(rs.next()){
     map.put("dutyTime1", rs.getString("duty_time1")) ;
     map.put("dutyType1", rs.getString("duty_type1")) ;
     map.put("dutyTime2", rs.getString("duty_time2")) ;
     map.put("dutyType2", rs.getString("duty_type2")) ;
     map.put("dutyTime3", rs.getString("duty_time3")) ;
     map.put("dutyType3", rs.getString("duty_type3")) ;
     map.put("dutyTime4", rs.getString("duty_time4")) ;
     map.put("dutyType4", rs.getString("duty_type4")) ;
     map.put("dutyTime5", rs.getString("duty_time5")) ;
     map.put("dutyType5", rs.getString("duty_type5")) ;
     map.put("dutyTime6", rs.getString("duty_time6")) ;
     map.put("dutyType6", rs.getString("duty_type6")) ;
    }
    sql="select * from oa_attendance_duty where user_id='"+userId+"' and "+YHDBUtility.getDateFilter("register_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("register_time", endDate, "<=");
    rs=stmt.executeQuery(sql);
    while(rs.next()){
      String time=rs.getString("register_time");
      String type=rs.getString("register_type");
      String dutyTime="dutyTime"+type;
      String dutyType="dutyType"+type;
      dutyTime=map.get(dutyTime);
      dutyType=map.get(dutyType);
      if("1".equals(dutyType)){
         if(dutyTime.compareTo(time)<0){
           late++;
         }
      }
      if("2".equals(dutyType)){
        if(dutyTime.compareTo(time)>0){
          early++;
        }
     }
      
    }
    
    
    
  }catch(Exception ex){
    throw ex;
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
  
  return late+","+early;
}

public String getNoRegister(Connection conn,String userId,String Type,String startDate,String endDate)throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  Map<String ,String> map=new HashMap();
  int on=0,off=0;
  int num=0;
  int total=0;
  String holday="";
  try{
    String sql="select * from oa_attendance_conf where seq_id='"+Type+"'";
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);
    while(rs.next()){
     map.put("dutyTime1", rs.getString("duty_time1")) ;
     map.put("dutyType1", rs.getString("duty_type1")) ;
     map.put("dutyTime2", rs.getString("duty_time2")) ;
     map.put("dutyType2", rs.getString("duty_type2")) ;
     map.put("dutyTime3", rs.getString("duty_time3")) ;
     map.put("dutyType3", rs.getString("duty_type3")) ;
     map.put("dutyTime4", rs.getString("duty_time4")) ;
     map.put("dutyType4", rs.getString("duty_type4")) ;
     map.put("dutyTime5", rs.getString("duty_time5")) ;
     map.put("dutyType5", rs.getString("duty_type5")) ;
     map.put("dutyTime6", rs.getString("duty_time6")) ;
     map.put("dutyType6", rs.getString("duty_type6")) ;
     holday=rs.getString("general");
    }
    for(int i=1;i<=6;i++){
      String dutytime="dutyTime"+i;
      if(YHUtility.isNullorEmpty(map.get(dutytime))){
        num=i;
        break;
      }
    }
    
    
    
    sql="select * from oa_attendance_duty where  and user_id='"+userId+"' and "+YHDBUtility.getDateFilter("register_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("register_time", endDate, "<=");
    rs=stmt.executeQuery(sql);
    while(rs.next()){
      String type=rs.getString("register_type");
      int intType=Integer.parseInt(type)%2;
      if(intType==0){
        off++;
      }else if(intType==1){
        on++;
      }
    }
  
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
 
     Date date1 = ft.parse( startDate );
     Date date2 = ft.parse( endDate );
     long quot = date2.getTime() - date1.getTime();
     quot = quot/1000/60/60/24;
     total=(int) (num/2*quot);
   //处理假日
     int time=0;
     while(startDate.compareTo(endDate)<=0){
        date1 = ft.parse( startDate );
        date2 = ft.parse( endDate );
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date1);          
        int w=cal.get(java.util.Calendar.DAY_OF_WEEK)-1;
        if(w==0)w=7;
        if(holday.indexOf(w+"")>0){
          time++;
        }
     }
     
     on=total-on;
     off=total-off;
      on=on-time*num/2;
      off=off-time*num/2;
  }catch(Exception ex){
    ex.printStackTrace();
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
  return on+","+off;
}
public String getDeptName(Connection conn,String deptId)throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String name="";
   try{
     String sql="select dept_name from oa_department where seq_id='"+deptId+"'";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     while(rs.next()){
       name=rs.getString("dept_name");
     }
   }catch(Exception ex){
     ex.printStackTrace();
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
   return name;
}
public String getUserIdsByDeptAndDuty(Connection conn,String dept,String dutyType)throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  String ids="";
  try{
    String duty=" and duty_type='"+dutyType+"' ";
    if("ALL_TYPE".equals(dutyType)){
      duty="";
    }
    if(dept.endsWith(",")){
      dept=dept.substring(0,dept.length()-1);
    }
    String deptStr=" and dept_id in ("+dept+") ";
    if("ALL_DEPT".equals(dept)){
      deptStr="";
    }
    String sql="select seq_id from person where 1=1 "+deptStr+duty;
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);
    while(rs.next()){
      ids+=rs.getInt("seq_id");
      ids+=",";
     }
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(ids.endsWith(",")){
      ids=ids.substring(0,ids.length()-1);
    }
  return ids;
}


public String getUserIdsByDept(Connection conn,String dept)throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  String ids="";
  try{
  
    if(dept.endsWith(",")){
      dept=dept.substring(0,dept.length()-1);
    }
    String deptStr=" and dept_id in ("+dept+") ";
    if("ALL_DEPT".equals(dept)){
      deptStr="";
    }
    String sql="select seq_id from person where 1=1 "+deptStr;
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);
    while(rs.next()){
      ids+=rs.getInt("seq_id");
      ids+=",";
     }
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(ids.endsWith(",")){
      ids=ids.substring(0,ids.length()-1);
    }
  return ids;
}

/**
 * 获取deptId的子部门
 * 
 * @param conn
 * @param deptId
 * 
 * */
public String getDeptChildId(Connection conn, String deptId) throws Exception {
  Statement stmt = null;
  ResultSet rs = null;
  stmt = conn.createStatement();
  String sql = "";
  String deptIdStr = "";
  sql = "select SEQ_ID from oa_department where dept_parent=" + deptId;
  rs = stmt.executeQuery(sql);
  while (rs.next()) {
    deptIdStr += rs.getInt(1);
    deptIdStr += ",";
  }
  if (!"".equals(deptIdStr)) {

    String deptOrg[] = deptIdStr.split(",");
    for (int i = 0; i < deptOrg.length; i++) {
      deptIdStr += this.getDeptChildId(conn, deptOrg[i]);
    }
  }
  return deptIdStr;
}


public List<Map<String, String>> getExeclEvectionLogic(Connection  conn,String dept,String startDate,String endDate)throws Exception{
  Statement stmt = null;
  ResultSet rs = null;   
  List<Map<String, String>> list = new ArrayList();
  
  try{
    //获取子部门
    String deptChildId="";
    if(!"ALL_DEPT".equals(dept)){
      deptChildId=this.getDeptChildId(conn, dept);
    }
    if(YHUtility.isNullorEmpty(deptChildId)){
      dept+=",";
      dept+=deptChildId;
    }    
    String userIds="";
    userIds=this.getUserIdsByDept(conn,dept);
    if(!"".equals(userIds)){
      String  sql="select a.seq_id ,d.dept_name,p.user_name,a.EVECTION_DEST,a.REGISTER_IP,a.evection_date1,a.evection_date2,a.LEADER_ID,a.status from person p,oa_attendance_trip a,oa_department d where "
        +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") and a.allow='1' "
        +" and ( ("+YHDBUtility.getDateFilter("evection_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("evection_date1", endDate+" 23:59:59", "<=")+") "
        +" or  ("+YHDBUtility.getDateFilter("evection_date2", startDate, ">=")+" and "+YHDBUtility.getDateFilter("evection_date2", endDate+" 23:59:59", "<=")+") "
        +" or  ("+YHDBUtility.getDateFilter("evection_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("evection_date2", endDate+" 23:59:59", "<=")+") "
        +") order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);

      while(rs.next()){
        Map<String, String> map = new HashMap<String, String>(); 
       int seqId=rs.getInt("seq_id");
       String deptName=rs.getString("dept_name");
       String userName=rs.getString("user_name");
       String evectionDest=rs.getString("evection_dest");
       String registerIp=rs.getString("register_ip");
       String evectionDate1=rs.getString("evection_date1");
       String evectionDate2=rs.getString("evection_date2");
       String leaderId=rs.getString("leader_id");
       String status=rs.getString("status");
       leaderId=this.getUserNameById(conn,leaderId);
       
     
       if("1".equals(status)){
          status="在外";
         }else{
           status="归来";
         }
       
       map.put("deptName", deptName);
       map.put("userName", userName);
       map.put("evectionDest", evectionDest);
       map.put("registerIp", registerIp);
       map.put("evectionDate1", evectionDate1);
       map.put("evectionDate2", evectionDate2);
       map.put("leaderId", leaderId);
       map.put("status", status);
       
       list.add(map);
      }
    }
   
  } catch (Exception e) {
    throw e;
  }finally {
    YHDBUtility.close(stmt, rs, null);
  }
 return list;
}


public List<Map<String, String>> getExeclLeaveLogic(Connection  conn,String dept,String startDate,String endDate)throws Exception{
  Statement stmt = null;
  ResultSet rs = null;   
  List<Map<String, String>> list = new ArrayList();
  
  try{
    //获取子部门
    String deptChildId="";
    if(!"ALL_DEPT".equals(dept)){
      deptChildId=this.getDeptChildId(conn, dept);
    }
    if(YHUtility.isNullorEmpty(deptChildId)){
      dept+=",";
      dept+=deptChildId;
    }    
    String userIds="";
    userIds=this.getUserIdsByDept(conn,dept);
    if(!"".equals(userIds)){
      String  sql="select a.seq_id ,d.dept_name,p.user_name,a.leave_type,a.ANNUAL_LEAVE,a.REGISTER_IP,a.LEAVE_DATE1,a.LEAVE_DATE2,a.LEADER_ID,a.STATUS from person p,oa_attendance_off a,oa_department d where "
        +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") and a.allow='1' "
        +" and ( ("+YHDBUtility.getDateFilter("leave_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("leave_date1", endDate+" 23:59:59", "<=")+") "
        +" or  ("+YHDBUtility.getDateFilter("leave_date2", startDate, ">=")+" and "+YHDBUtility.getDateFilter("leave_date2", endDate+" 23:59:59", "<=")+") "
        +" or  ("+YHDBUtility.getDateFilter("leave_date1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("leave_date2", endDate+" 23:59:59", "<=")+") "
        +" ) order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);

      while(rs.next()){
        Map<String, String> map = new HashMap<String, String>(); 
        int seqId=rs.getInt("seq_id");
        String deptName=rs.getString("dept_name");
        String userName=rs.getString("user_name");
        String leaveType=rs.getString("leave_type");
        String annualLeave=rs.getString("annual_leave");
        String registerIp=rs.getString("register_ip");
        String leaveDate1=rs.getString("leave_date1");
        String leaveDate2=rs.getString("leave_date2");
        String leaderId=rs.getString("leader_id");
        String status=rs.getString("status");
        leaderId=this.getUserNameById(conn,leaderId);
       
     
        if("1".equals(status))
          status="现行";
         else
            status="已销假";
       
       map.put("deptName", deptName);
       map.put("userName", userName);
       map.put("leaveType", leaveType);
       map.put("registerIp", registerIp);
       map.put("annualLeave", annualLeave);
       map.put("leaveDate1", leaveDate1);
       map.put("leaveDate2", leaveDate2);
       map.put("leaderId", leaderId);
       map.put("status", status);
       
       list.add(map);
      }
    }
   
  } catch (Exception e) {
    throw e;
  }finally {
    YHDBUtility.close(stmt, rs, null);
  }
 return list;
}

public List<Map<String, String>> getExeclOvertimeLogic(Connection  conn,String dept,String startDate,String endDate)throws Exception{
  Statement stmt = null;
  ResultSet rs = null;   
  List<Map<String, String>> list = new ArrayList();
  
  try{
    //获取子部门
    String deptChildId="";
    if(!"ALL_DEPT".equals(dept)){
      deptChildId=this.getDeptChildId(conn, dept);
    }
    if(YHUtility.isNullorEmpty(deptChildId)){
      dept+=",";
      dept+=deptChildId;
    }    
    String userIds="";
    userIds=this.getUserIdsByDept(conn,dept);
    if(!"".equals(userIds)){
      String  sql="select a.seq_id ,d.dept_name,p.user_name,a.OVERTIME_TIME,a.OVERTIME_DESC,a.BEGIN_TIME,a.END_DATE,a.HOUR,a.LEADER_ID,a.STATUS from person p,oa_timeout_record a,department d where "
        +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") and a.status in ('1','3') "
        +" and "+YHDBUtility.getDateFilter("begin_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("begin_time", endDate+" 23:59:59", "<=") 
        +" order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);

      while(rs.next()){
        Map<String, String> map = new HashMap<String, String>(); 
        int seqId=rs.getInt("seq_id");
        String deptName=rs.getString("dept_name");
        String userName=rs.getString("user_name");
        String overtimeTime=rs.getString("OVERTIME_TIME");
        String overtimeDesc=rs.getString("OVERTIME_DESC");
        String beginTime=rs.getString("BEGIN_TIME");
        String endTime=rs.getString("END_DATE");
        String hour=rs.getString("HOUR");
        String leaderId=rs.getString("leader_id");
        String status=rs.getString("status");
        leaderId=this.getUserNameById(conn,leaderId);
       
     
        if("0".equals(status))
          status="未确认";
         else if("1".equals(status))
           status="已确认";
       
       map.put("deptName", deptName);
       map.put("userName", userName);
       map.put("overtimeTime", overtimeTime);
       map.put("overtimeDesc", overtimeDesc);
       map.put("beginTime", beginTime);
       map.put("endTime", endTime);
       map.put("hour", hour);
       map.put("leaderId", leaderId);
       map.put("status", status);
       
       list.add(map);
      }
    }
   
  } catch (Exception e) {
    throw e;
  }finally {
    YHDBUtility.close(stmt, rs, null);
  }
 return list;
}


public List<Map<String, String>> getExeclOutLogic(Connection  conn,String dept,String startDate,String endDate)throws Exception{
  Statement stmt = null;
  ResultSet rs = null;   
  List<Map<String, String>> list = new ArrayList();
  
  try{
    //获取子部门
    String deptChildId="";
    if(!"ALL_DEPT".equals(dept)){
      deptChildId=this.getDeptChildId(conn, dept);
    }
    if(YHUtility.isNullorEmpty(deptChildId)){
      dept+=",";
      dept+=deptChildId;
    }    
    String userIds="";
    userIds=this.getUserIdsByDept(conn,dept);
    if(!"".equals(userIds)){
      String  sql="select a.seq_id ,d.dept_name,p.user_name,a.CREATE_DATE,a.out_type,a.REGISTER_IP,a.SUBMIT_TIME,a.OUT_TIME1,a.OUT_TIME2,a.LEADER_ID,a.status ,a.allow from person p,oa_attendance_out a,oa_department d where "
        +" p.seq_id=a.user_id and p.dept_id=d.seq_id and p.seq_id in ("+userIds+") "
        +" and "+YHDBUtility.getDateFilter("submit_time", startDate, " >= ")+" and "+YHDBUtility.getDateFilter("submit_time", endDate+" 23:59:59", "<=")
        +" order by d.seq_id,p.SEQ_ID,p.USER_NAME ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);

      while(rs.next()){
        Map<String, String> map = new HashMap<String, String>(); 
        int seqId=rs.getInt("seq_id");
        String deptName=rs.getString("dept_name");
        String userName=rs.getString("user_name");
        String createDate=rs.getString("CREATE_DATE");
        String outType=rs.getString("out_type");
        String registerIp=rs.getString("REGISTER_IP");
        String submitTime=rs.getString("SUBMIT_TIME");
        String outTime1=rs.getString("OUT_TIME1");
        String outTime2=rs.getString("OUT_TIME2");
        String leaderId=rs.getString("LEADER_ID");
        String status=rs.getString("status");
        String allow=rs.getString("allow");
        leaderId=this.getUserNameById(conn,leaderId);
       
        String statusDesc="";
        if("0".equals(status))
          statusDesc="外出";
         else if("1".equals(status))
           statusDesc="已归来";
         if("0".equals(allow))
           statusDesc="待批";
         else if("2".equals(allow))
           statusDesc="不批准";
       
       map.put("deptName", deptName);
       map.put("userName", userName);
       map.put("createDate", createDate);
       map.put("outType", outType);
       map.put("registerIp", registerIp);
       map.put("submitTime", submitTime);
       map.put("outTime1", outTime1);
       map.put("outTime2", outTime2);
       map.put("leaderId", leaderId);
       map.put("status", statusDesc);
       
       list.add(map);
      }
    }
   
  } catch (Exception e) {
    throw e;
  }finally {
    YHDBUtility.close(stmt, rs, null);
  }
 return list;
}


public ArrayList<YHDbRecord> convertOutList(List<Map<String, String>> list){
  ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
  if(list != null && list.size() >0){
    for (int i = 0; i < list.size(); i++) {
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("部门", list.get(i).get("deptName"));
      dbrec.addField("姓名", list.get(i).get("userName"));
      dbrec.addField("申请时间", list.get(i).get("createDate"));
      dbrec.addField("外出原因", list.get(i).get("outType"));
      dbrec.addField("登记IP", list.get(i).get("registerIp"));       
      dbrec.addField("外出日期", list.get(i).get("submitTime"));
      dbrec.addField("外出时间", list.get(i).get("outTime1"));
      dbrec.addField("归来时间", list.get(i).get("outTime2"));
      dbrec.addField("审批人", list.get(i).get("leaderId"));
      dbrec.addField("状态", list.get(i).get("status"));
      dbL.add(dbrec);
    }
  }     
  return dbL;    
}

public ArrayList<YHDbRecord> convertList(List<Map<String, String>> list){
  ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
  if(list != null && list.size() >0){
    for (int i = 0; i < list.size(); i++) {
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("部门", list.get(i).get("deptName"));
      dbrec.addField("姓名", list.get(i).get("userName"));
      dbrec.addField("全勤(天)", list.get(i).get("perfectCount"));
      dbrec.addField("时长", list.get(i).get("allHoursMinites"));
      dbrec.addField("迟到", list.get(i).get("lateCount"));  
      dbrec.addField("上班未登记", list.get(i).get("on"));
      dbrec.addField("早退", list.get(i).get("earlyCount"));
      dbrec.addField("下班未登记", list.get(i).get("off"));
      dbL.add(dbrec);
    }
  }     
  return dbL;    
}
public ArrayList<YHDbRecord> convertEvectionList(List<Map<String, String>> list){
  ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
  if(list != null && list.size() >0){
    for (int i = 0; i < list.size(); i++) {
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("部门", list.get(i).get("deptName"));
      dbrec.addField("姓名", list.get(i).get("userName"));
      dbrec.addField("出差地点", list.get(i).get("evectionDest"));
      dbrec.addField("登记IP", list.get(i).get("registerIp"));
      dbrec.addField("开始日期", list.get(i).get("evectionDate1"));       
      dbrec.addField("结束日期", list.get(i).get("evectionDate2"));
      dbrec.addField("审批人", list.get(i).get("leaderId"));
      dbrec.addField("状态", list.get(i).get("status"));
      dbL.add(dbrec);
    }
  }     
  return dbL;    
}


public ArrayList<YHDbRecord> convertLeaveList(List<Map<String, String>> list){
  ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
  if(list != null && list.size() >0){
    for (int i = 0; i < list.size(); i++) {
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("部门", list.get(i).get("deptName"));
      dbrec.addField("姓名", list.get(i).get("userName"));
      dbrec.addField("请假原因", list.get(i).get("leaveType"));
      dbrec.addField("占年休假", list.get(i).get("annualLeave"));
      dbrec.addField("登记IP", list.get(i).get("registerIp"));       
      dbrec.addField("开始日期", list.get(i).get("leaveDate1"));
      dbrec.addField("结束日期", list.get(i).get("leaveDate2"));
      dbrec.addField("审批人", list.get(i).get("leaderId"));
      dbrec.addField("状态", list.get(i).get("status"));
      dbL.add(dbrec);
    }
  }     
  return dbL;    
}


public ArrayList<YHDbRecord> convertOvertimeList(List<Map<String, String>> list){
  ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
  if(list != null && list.size() >0){
    for (int i = 0; i < list.size(); i++) {
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("部门", list.get(i).get("deptName"));
      dbrec.addField("姓名", list.get(i).get("userName"));
      dbrec.addField("申请时间", list.get(i).get("overtimeTime"));
      dbrec.addField("加班内容", list.get(i).get("overtimeDesc"));
      dbrec.addField("开始日期", list.get(i).get("beginTime"));       
      dbrec.addField("结束时间", list.get(i).get("endTime"));
      dbrec.addField("时长", list.get(i).get("hour"));
      dbrec.addField("审批人", list.get(i).get("leaderId"));
      dbrec.addField("状态", list.get(i).get("status"));
      dbL.add(dbrec);
    }
  }     
  return dbL;    
}

public ArrayList<YHDbRecord> convertExelDutyList( List<LinkedList<String>> list){
  ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
  if(list != null && list.size() >0){
    LinkedList<String> namelist=list.get(0);
    int length=namelist.size();
    for(int i=1;i<list.size();i++){
      LinkedList<String> datalist=list.get(i);
      YHDbRecord dbrec=new YHDbRecord();
      for(int j=0;j<length;j++){       
        dbrec.addField(namelist.get(j),datalist.get(j));        
      }
       dbL.add(dbrec);
    }

  }     
  return dbL;    
}

public int getCount(Connection conn , String sql) throws Exception {
  Statement stmt7=null;
  ResultSet rs7=null;
  int result = 0 ;
  try {
    stmt7 = conn.createStatement();
    rs7 = stmt7.executeQuery(sql);
    if (rs7.next()) {
      result = rs7.getInt(1);
    }
  }catch(Exception ex){
    throw ex;
  }finally{
    YHDBUtility.close(stmt7, rs7, null);
  }
  return result;
}


/**
 * 查询在一段时间内得到考勤登记信息
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String getUserDutyInfoLogic(Connection dbConn,HttpServletRequest request,
    HttpServletResponse response,String userId,String days)throws Exception {
  
  String[] dayArray = days.split(",");
  String data = "{trTemp:\"";
  YHManageOutLogic yhaol = new YHManageOutLogic();
  //根据用户得到相应的排班类型


  SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
  //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
  YHPersonLogic personLogic  = new YHPersonLogic();
  YHPerson person = personLogic.getPerson(dbConn, userId);
  int configSeqId = person.getDutyType();
  YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
  YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
  String dutyName = config.getDutyName();
  String dutyTime1 = config.getDutyTime1();
  String dutyTime2 = config.getDutyTime2();
  String dutyTime3 = config.getDutyTime3();
  String dutyTime4 = config.getDutyTime4();
  String dutyTime5 = config.getDutyTime5();
  String dutyTime6 = config.getDutyTime6();
  String dutyType1 = config.getDutyType1();
  String dutyType2 = config.getDutyType2();
  String dutyType3 = config.getDutyType3();
  String dutyType4 = config.getDutyType4();
  String dutyType5 = config.getDutyType5();
  String dutyType6 = config.getDutyType6();
  String general = "";

  if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
    general = config.getGeneral();
  }
  String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
  //对日期循环



  for(int i = 0; i < dayArray.length; i++){
    Date dateTemp = YHUtility.parseDate(dayArray[i]); 
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateTemp);
    int week = calendar.get(Calendar.DAY_OF_WEEK);
    String weekStr = weeks[week-1];
    String trClass = "TableData";
    //判断当天是否是节假日.公休日。



  

    data = data + "<tr align='center' class='"+ trClass+"'>" ;
    data = data + "<td align='center' nowrap>" + dayArray[i]+ " (" + weekStr + ")</td>";
    //对排版类型的6循环
    for(int j = 1;j<=6;j++){
      
      String holidayStr = "未登记";
      
      String registerTimeStr="";
      String registerIp="";
      String dutyTime = "";
      String dutyType = "";
      if(j==1){
        dutyTime = dutyTime1;
        dutyType = dutyType1;
      }
      if(j==2){
        dutyTime = dutyTime2;
        dutyType = dutyType2;
      }
      if(j==3){
        dutyTime = dutyTime3;
        dutyType = dutyType3;
      }
      if(j==4){
        dutyTime = dutyTime4;
        dutyType = dutyType4;
      }
      if(j==5){
        dutyTime = dutyTime5;
        dutyType = dutyType5;
      }
      if(j==6){
        dutyTime = dutyTime6;
        dutyType = dutyType6;
      }

      if(dutyTime!=null&&!dutyTime.trim().equals("")){       
  
        //查出当天有没有登记 记录
        List<YHAttendDuty> dutyList =getAttendDuty(request,response,dayArray[i],String.valueOf(j),userId,config);
        String td = "" ;
        if(dutyList.size()>0){
          YHAttendDuty duty = dutyList.get(0);
          Date registerTime = duty.getRegisterTime();
           registerTimeStr = dateFormat.format(registerTime);          
           registerIp = duty.getRegisterIp();
           registerIp="("+registerIp+")";
          int seqId = duty.getSeqId();
          String remark = duty.getRemark();
          
       
          long dutyTimeInt = getLongByDutyTime(dutyTime);
          long registerTimeInt = getLongByDutyTime(registerTimeStr);
          if(dutyType.equals("1")){
            if(dutyTimeInt<registerTimeInt){
              holidayStr = "<span class=big4>迟到</span><br>";
            }
          }else{
            if(dutyTimeInt>registerTimeInt){
              holidayStr = "<span class=big4>早退</span>";
            }
          }

        }
          //判断是否外出
          if(isOutTemp(dbConn,request, response, dayArray[i]+" "+dutyTime, userId).equals("1")){
               holidayStr = "<font color='#00CC33'>外出</font>";
                        
           }
          //判断是否请假
          if(isLeaveTemp(dbConn,request, response, dayArray[i] + " " + dutyTime, userId).equals("1")){
            holidayStr = "<font color='#00CC33'>请假</font>";
          
          }
          //判断是否出差
          if(isEvectionTemp(dbConn,request, response, dayArray[i] + " " + dutyTime, userId).equals("1")){
            holidayStr = "<font color='#00CC33'>出差</font>";
                  
          }
          if(holidayStr.equals("未登记")){
            holidayStr= "<font color='#CCCC66'>未登录</font>";
            if(!"".equals(registerIp)){    
              holidayStr="";
            }
          }
          data = data + "<td align='center' >"+registerTimeStr +registerIp+ holidayStr + "</td>";
        
      }
    }
    data = data + "</tr>";
  } 
  data = data + "\"}";
  
 return data;
}

public  List<LinkedList<String>> getExeclDutyInfo(Connection dbConn,HttpServletRequest request,
    HttpServletResponse response,String userId,String days)throws Exception {
  
  List<LinkedList<String>> dataList=new ArrayList();
  LinkedList<String> titleList=this.getExeclTitleByUserId(dbConn,userId);
  dataList.add(titleList);
  String[] dayArray = days.split(",");
  String data = "{trTemp:\"";
  YHManageOutLogic yhaol = new YHManageOutLogic();
  //根据用户得到相应的排班类型

  SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
  //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
  YHPersonLogic personLogic  = new YHPersonLogic();
  YHPerson person = personLogic.getPerson(dbConn, userId);
  int configSeqId = person.getDutyType();
  YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
  YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
  String dutyName = config.getDutyName();
  String dutyTime1 = config.getDutyTime1();
  String dutyTime2 = config.getDutyTime2();
  String dutyTime3 = config.getDutyTime3();
  String dutyTime4 = config.getDutyTime4();
  String dutyTime5 = config.getDutyTime5();
  String dutyTime6 = config.getDutyTime6();
  String dutyType1 = config.getDutyType1();
  String dutyType2 = config.getDutyType2();
  String dutyType3 = config.getDutyType3();
  String dutyType4 = config.getDutyType4();
  String dutyType5 = config.getDutyType5();
  String dutyType6 = config.getDutyType6();
  String general = "";

  if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
    general = config.getGeneral();
  }
  String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
  //对日期循环



  for(int i = 0; i < dayArray.length; i++){
   LinkedList<String> list=new LinkedList<String>();
    
    Date dateTemp = YHUtility.parseDate(dayArray[i]); 
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateTemp);
    int week = calendar.get(Calendar.DAY_OF_WEEK);
    String weekStr = weeks[week-1];
    String trClass = "TableData";
    //判断当天是否是节假日.公休日。



  

    data = data + "<tr align='center' class='"+ trClass+"'>" ;
    data = data + "<td align='center' nowrap>" + dayArray[i]+ " (" + weekStr + ")</td>";
    list.add(dayArray[i]+ " (" + weekStr + ")");
    //对排版类型的6循环
    for(int j = 1;j<=6;j++){
      
      String holidayStr = "未登记";
      String holidayStr1="未登记";
      
      String registerTimeStr="";
      String registerIp="";
      String dutyTime = "";
      String dutyType = "";
      if(j==1){
        dutyTime = dutyTime1;
        dutyType = dutyType1;
      }
      if(j==2){
        dutyTime = dutyTime2;
        dutyType = dutyType2;
      }
      if(j==3){
        dutyTime = dutyTime3;
        dutyType = dutyType3;
      }
      if(j==4){
        dutyTime = dutyTime4;
        dutyType = dutyType4;
      }
      if(j==5){
        dutyTime = dutyTime5;
        dutyType = dutyType5;
      }
      if(j==6){
        dutyTime = dutyTime6;
        dutyType = dutyType6;
      }

      if(dutyTime!=null&&!dutyTime.trim().equals("")){       
  
        //查出当天有没有登记 记录
        List<YHAttendDuty> dutyList =getAttendDuty(request,response,dayArray[i],String.valueOf(j),userId,config);
        String td = "" ;
        if(dutyList.size()>0){
          YHAttendDuty duty = dutyList.get(0);
          Date registerTime = duty.getRegisterTime();
           registerTimeStr = dateFormat.format(registerTime);          
           registerIp = duty.getRegisterIp();
           registerIp="("+registerIp+")";
          int seqId = duty.getSeqId();
          String remark = duty.getRemark();
          
       
          long dutyTimeInt = getLongByDutyTime(dutyTime);
          long registerTimeInt = getLongByDutyTime(registerTimeStr);
          if(dutyType.equals("1")){
            if(dutyTimeInt<registerTimeInt){
              holidayStr = "<span class=big4>迟到</span><br>";
              holidayStr1="迟到";
            }
          }else{
            if(dutyTimeInt>registerTimeInt){
              holidayStr = "<span class=big4>早退</span>";
              holidayStr1="早退";
            }
          }

        }
          //判断是否外出
          if(isOutTemp(dbConn,request, response, dayArray[i]+" "+dutyTime, userId).equals("1")){
               holidayStr = "<font color='#00CC33'>外出</font>";
               holidayStr1 = "外出";  
           }
          //判断是否请假
          if(isLeaveTemp(dbConn,request, response, dayArray[i] + " " + dutyTime, userId).equals("1")){
            holidayStr = "<font color='#00CC33'>请假</font>";
            holidayStr1 = "请假";
          }
          //判断是否出差
          if(isEvectionTemp(dbConn,request, response, dayArray[i] + " " + dutyTime, userId).equals("1")){
            holidayStr = "<font color='#00CC33'>出差</font>";
            holidayStr1 = "出差";
          }
          if(holidayStr.equals("未登记")){
            holidayStr= "<font color='#CCCC66'>未登录</font>";
            if(!"".equals(registerIp)){    
              holidayStr="";
              holidayStr1="";
            }
          }
          data = data + "<td align='center' >"+registerTimeStr +registerIp+ holidayStr + "</td>";
          list.add(registerTimeStr +registerIp+ holidayStr1);
      }
    }
    data = data + "</tr>";
    dataList.add(list);
  } 
  
  data = data + "\"}";
  
 return dataList;
}

public  LinkedList<String> getExeclTitleByUserId(Connection conn,String userId)throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  LinkedList<String> list=new LinkedList<String>();
  list.add("日期");
  try{
    String sql="select a.* from oa_attendance_conf a,person p where p.duty_type=a.seq_id and p.seq_id='"+userId+"'";
    stmt=conn.createStatement();
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      for(int i=1;i<=6;i++){
         String dutyTime="duty_time";
         String dutyType="duty_type";
         dutyTime+=i;
         dutyType+=i;
         String time="";
         time=rs.getString(dutyTime);
         dutyType=rs.getString(dutyType);
         if(!YHUtility.isNullorEmpty(time)){
            String duty="";
            if("1".equals(dutyType)){
              duty="上班时间("+time+")";
            }else if("2".equals(dutyType)){
              duty="下班时间("+time+")";
            }
           list.add(duty);
         }
      }
      
    }
  }catch(Exception ex) {  
     ex.printStackTrace();
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
  return list;
}

public String isLeaveTemp(Connection dbConn,HttpServletRequest request,
    HttpServletResponse response,String date,String userId) throws Exception {
 
  Statement stmt;
  ResultSet rs;
  String isLeave = "0";
  try {
  
    stmt=dbConn.createStatement();
    String sql="select * from oa_attendance_off where USER_ID='"+userId+"' and ALLOW in ('1','3') and "+YHDBUtility.getDateFilter("LEAVE_DATE1",date, "<=")+" and "+YHDBUtility.getDateFilter("LEAVE_DATE2",date, ">=");
  
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      isLeave="1";
    }
    rs.close();
    stmt.close();
  }catch(Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
  return isLeave;
}

public String isOutTemp(Connection dbConn,HttpServletRequest request,
    HttpServletResponse response,String date,String userId) throws Exception {
 
  String isOut = "0";
  try {
    Statement stmt=dbConn.createStatement();
    ResultSet rs;
    
    //是否为外出    
    String  sql="select * from oa_attendance_out where USER_ID='"+userId+"' and ALLOW in ('1') and "+YHDBUtility.getDateFilter("SUBMIT_TIME",date.substring(0,10)+" 00:00:00", ">=") + " and  "+YHDBUtility.getDateFilter("SUBMIT_TIME",date.substring(0,10)+" 23:59:59", "<=") + " and OUT_TIME1<='"+date.substring(11,16)+"' and OUT_TIME2>='"+date.substring(11,16)+"'";
    rs=stmt.executeQuery(sql);   
  
    if(rs.next()){
      isOut="1";
     
    }
    rs.close();
    stmt.close();
  }catch(Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
  return isOut;
}

/**
 * 
 * @param request
 * @param response
 * @param date 时间 String 类型
 * @param userId 
 * @return
 * @throws Exception
 */
  public String isEvectionTemp(Connection dbConn,HttpServletRequest request,
      HttpServletResponse response,String date,String userId) throws Exception {
   
    Statement stmt;
    ResultSet rs;
    String isEvection = "0";
    try {
     
      stmt=dbConn.createStatement();
      //是否为出差


     String sql="select * from oa_attendance_trip where USER_ID='"+userId+"' and ALLOW='1' and "+YHDBUtility.getDateFilter("EVECTION_DATE1",date, "<=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE2",date, ">=");
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        isEvection="1";
      }
      rs.close();
      stmt.close();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isEvection;
  }

public long getLongByDutyTime(String dutyTime){
  long time = 0;
  String times[] = dutyTime.split(":");
  int length = times.length;
  for (int i = 0; i < times.length; i++) {
    time = time + Long.parseLong(times[i])* (long)(Math.pow(60, length-1-i)) ;
  }
  return time;
}

public List<YHAttendDuty> getAttendDuty(HttpServletRequest request,
    HttpServletResponse response,String date,String registerType,String userId,YHAttendConfig config) throws Exception {
  Connection dbConn = null;
  List<YHAttendDuty> dutyList = new ArrayList<YHAttendDuty>();
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    //得到指定当天登记的记录



    YHDBUtility yhdbu = new YHDBUtility();
    YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
    String date1 = date + " 00:00:00";
    String date2 = date + " 23:59:59";
    String DBStr =  yhdbu.curDayFilter("REGISTER_TIME");
    date1 = yhdbu.getDateFilter("REGISTER_TIME", date1, ">=");
    date2 = yhdbu.getDateFilter("REGISTER_TIME", date2, "<=");
    //System.out.println(userId);
    dutyList = yhadl.selectDuty(dbConn, String.valueOf(userId), date1,date2,registerType);
    YHManageAttendLogic attendLogic = new YHManageAttendLogic();
    String dutyName = config.getDutyName();
    String dutyTime1 = config.getDutyTime1();
    String dutyTime2 = config.getDutyTime2();
    String dutyTime3 = config.getDutyTime3();
    String dutyTime4 = config.getDutyTime4();
    String dutyTime5 = config.getDutyTime5();
    String dutyTime6 = config.getDutyTime6();
    String dutyType1 = config.getDutyType1();
    String dutyType2 = config.getDutyType2();
    String dutyType3 = config.getDutyType3();
    String dutyType4 = config.getDutyType4();
    String dutyType5 = config.getDutyType5();
  }catch(Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
  return dutyList;
}

}

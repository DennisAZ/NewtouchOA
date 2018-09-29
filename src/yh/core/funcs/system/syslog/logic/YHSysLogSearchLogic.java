package yh.core.funcs.system.syslog.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.*;
import yh.core.funcs.attendance.personal.data.YHAttendDuty;
import yh.core.funcs.system.syslog.data.YHSysLog;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHSysLogSearchLogic {
  /**
   * 统计总天数
   * 保存系统日志
   * @param conn
   * @param type 类型[1登录日志|2登录密码错误|3添加部门|4编辑部门|5删除部门|6添加用户|7编辑用户|8删除用户
   * |9 非法IP登录|10错误用户名|11admin密码清空|12系统资源回收|13考勤数据管理|14修改登录密码|15公告通知管理
   * |16 公共文件柜|17网络硬盘|18软件注册|19用户批量设置|20培训课程管理|21用户KEY验证失败|22退出系统|23员工离职]
   * @param remark 日志说明
   * @param userId 用户Id
   * @param ip IP地址
   * @throws Exception
   */
  public static String getMySysLog(Connection conn,int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    Statement stmt1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    String time = "";
    String time1 = ""; 
    long countDay = 0;
    long countDay1 = 0;
    String count = "0";
    try{
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        String SysLogSqldesc = "select TIME from oa_sys_log order by TIME desc";
        String SysLogSqlasc = "select TIME from oa_sys_log order by TIME asc";
        //此语句也可以
        //String sql = "select (select max(TIME) from sys_log) - (select min(time) from sys_log) from dual";
        rs = stmt.executeQuery(SysLogSqldesc);
        rs1 = stmt1.executeQuery(SysLogSqlasc);
        if(rs.next()){
           time = rs.getString("TIME");
        }else{
          return  count;
        }
        if(rs1.next()){
            time1 = rs1.getString("TIME");
        } 
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        Date Timedesc = sdf.parse(time);
        Date Timeasc = sdf.parse(time1);
        countDay = Timedesc.getTime();
        countDay1 = Timeasc.getTime();
        long timeUsed = countDay - countDay1;
        long day = timeUsed/(24*60*60*1000); 
        count = String.valueOf(day+1);
    }catch(Exception ex){
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return count;
  }
  /**
   * 总访问量
   * @param request
   * @return
   */
  public static String getMySysLogAll(Connection conn,int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    String countAll = "";
    try{
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        String SysLogSqlAll = "SELECT count(*) from oa_sys_log where TYPE='1'";
        rs = stmt.executeQuery(SysLogSqlAll);
        if(rs.next()){
           countAll = rs.getString(1);
        }
    }catch(Exception ex){
       throw ex;
    } finally {
       YHDBUtility.close(stmt, rs, null);
    }
    return countAll;
  }
  /**
   * 今年访问量
   * @param request
   * @return
   */
  public static String getMySysYearLog(Connection conn,int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    String YearAll = "";
    try{
        stmt =  conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
       // String SysLogYear = "select count(*) from SYS_LOG where TYPE='1' and TIME >= to_date('2010-01-01 00:00:00','yyyy-mm-dd HH24:MI:SS') and TIME <= to_date('2010-12-31 23:23:59','yyyy-mm-dd HH24:MI:SS')";
        String SysLogYear = "SELECT count(*) from oa_sys_log where TYPE=1 and " + YHDBUtility.curYearFilter("TIME");
        rs = stmt.executeQuery(SysLogYear);
        if(rs.next()){
           YearAll = rs.getString(1);
        }
    }catch(Exception ex){
        throw ex;
    }finally{
        YHDBUtility.close(stmt, rs, null);
    }
    return YearAll;
  }
  /**
   * 本月访问量
   * @param request
   * @return
   */
  public static String getMySysMonthLog(Connection conn,int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    String monthAll = "";
    try{
        stmt =  conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        //String month = "SELECT count(*) from SYS_LOG where TYPE=1 and TIME >= to_timestamp(to_char(trunc(add_months(last_day(sysdate), -1) + 1), 'yyyy-mm-dd HH24:MI:SS'),'yyyy-mm-dd HH24:MI:SS')"
        //+ "and  TIME<=to_timestamp(to_char(last_day(sysdate), 'yyyy-mm-dd HH24:MI:SS'),'yyyy-mm-dd HH24:MI:SS')";
        String month = "SELECT count(*) from oa_sys_log where TYPE=1 and " + YHDBUtility.curMonthFilter("TIME");
        rs = stmt.executeQuery(month);
       if(rs.next()){
           monthAll =  rs.getString(1);
       }
    }catch(Exception ex){
      throw ex;
    }
     return monthAll;
  }  
  /**
   * 今日访问量
   * @param request
   * @return
   */
  public static String getMySysDayLog(Connection conn,int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    String dayAll = "";
    String times;
    long dayLog;
    try{
        Date date = new Date();
        Date dates = new Date();
        String date1= YHUtility.getCurDateTimeStr();
        String datetime= YHUtility.getCurDateTimeStr();
        //String datetime = dates.toLocaleString();
        //String date1 = date.toLocaleString();
        date1 = date1.substring(0,10);
        datetime = datetime.substring(0,10);
        date1 = date1 + " 00:00:00";
        datetime = datetime + " 23:59:59";
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        Date todate = sdf.parse(date1);
        Date todatemax = sdf.parse(datetime);
        stmt =  conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
       //String day = "SELECT count(*) from SYS_LOG where TYPE='1' and TIME>= to_timestamp('" + date1 + "', 'yyyy-mm-dd HH24:MI:SS') and TIME<=to_timestamp('" + datetime + "', 'yyyy-mm-dd HH24:MI:SS')";
        String day = "SELECT count(*) from oa_sys_log where TYPE=1 and " + YHDBUtility.curDayFilter("TIME");
        rs = stmt.executeQuery(day);
        if(rs.next()){
           dayAll = rs.getString(1);
       }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
     return dayAll;
  }
  /**
   * 最近10条日志 
   * @param request
   * @return
   */
  public static String getMySysTenLog(Connection conn,int userId) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    int SysTen = 0 ;
    try{
        sb.append("{");
        sb.append("listData:[");
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        String dbms = YHSysProps.getProp("db.jdbc.dbms");// 通过配置文件配置（sysconfig.properties）
                                                         // 区分是用的那种数据库
        // String SysTenLog =
        // " select * from (SELECT SEQ_ID,USER_ID,TIME,IP,TYPE,REMARK  from SYS_LOG order by TIME desc) where rownum <= 10";
        String SysTenLog = " SELECT SEQ_ID,USER_ID,TIME,IP,TYPE,REMARK,USER_NAME from oa_sys_log order by TIME desc";
        rs = stmt.executeQuery(SysTenLog);
        List<Map> list = new ArrayList();
        int rowCnt = 0;
        while (rs.next()) {
            String seqId = rs.getString("SEQ_ID");
            String useId = rs.getString("USER_ID");
            Timestamp tt= rs.getTimestamp("TIME");
            String time = YHUtility.getDateTimeStr(tt);
            String ip = rs.getString("IP");
            String type = rs.getString("TYPE");
            String remark = rs.getString("REMARK");
            String userName = rs.getString("USER_NAME");
            Map mapTmp = new HashMap();
            mapTmp.put("useId", useId);
            mapTmp.put("time", time);
            mapTmp.put("ip", ip);
            mapTmp.put("type", type);
            mapTmp.put("seqId", seqId);
            mapTmp.put("remark", remark);
            mapTmp.put("userName", YHUtility.empty2Default(userName, ""));
            list.add(mapTmp);
            if (++rowCnt > 9) {
               break;
            }
        }
       for(int j = 0; j < list.size();  j++){
          Map tmpMap = list.get(j);
          sb.append("{");
          sb.append("useId:" + tmpMap.get("useId")); 
          sb.append(",time:\"" + tmpMap.get("time") + "\"");
          sb.append(",ip:\"" + tmpMap.get("ip")+ "\"");
          sb.append(",type:\"" + tmpMap.get("type")+ "\"");
          sb.append(",remark:\"" + tmpMap.get("remark")+ "\"");
          sb.append(",userName:\"" + tmpMap.get("userName")+ "\"");
          sb.append("},");       
       }
       if(list.size() > 0)
          sb.deleteCharAt(sb.length() - 1);
          sb.append("]");
          sb.append("}");
     }catch(Exception ex){
         throw ex;
    }finally{
         YHDBUtility.close(stmt, rs, null);
    }
     return sb.toString();
  }
  /**
   * 平均每日访问量
   * @param request
   * @return
   */
  public static String getMySysAveLog(Connection conn,int userId) throws Exception{
    String count = YHSysLogSearchLogic.getMySysLog(conn, userId);
    String countAll =  YHSysLogSearchLogic.getMySysLogAll(conn, userId);
    if(YHUtility.isNullorEmpty(count) || count.equals("0")){
      return "0";
    }
    if(count.length() > 4){
       count=count.substring(0,2);
    }else{
       count=count.substring(0,1);
    }
    Double dayAve=0.0;
    Double aveAll = Double.valueOf(countAll);
    Double aveDay = Double.valueOf(count);
   
    if(aveAll%aveDay ==0){
       dayAve = aveAll/aveDay;
    }else{
       dayAve = aveAll/aveDay;
       dayAve = dayAve+1;
   }
    String aveCount =   String.valueOf(dayAve);
  
    if(aveCount.contains(".")){
      aveCount = aveCount.substring(0,aveCount.lastIndexOf("."));
   }  
   //System.out.println(aveCount+"aveCount");
    return aveCount;
  }
}

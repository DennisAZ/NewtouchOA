package yh.core.funcs.workflow.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHFlowTimerLogic {
  public void saveTimer(Connection conn , String seqId ,  String flowId , String type , String privUser, String remindDate , String remindTime) throws Exception {
    String query = "";
    Date now = new Date();
    String nowStr = YHUtility.getDateTimeStr(now);
    if (YHUtility.isNullorEmpty(remindDate)) {
      remindDate = nowStr.split(" ")[0];
    }
    if (YHUtility.isNullorEmpty(remindTime)) {
      remindTime = nowStr.split(" ")[0];
    }
    if( !"".equals(seqId) ) {
       query ="update oa_fl_timer set TYPE='"+type+"', USER_STR='"+privUser+"', REMIND_DATE='"+remindDate+"', REMIND_TIME='"+remindTime+"' where SEQ_ID='"+seqId+"'";
    } else {
      query ="insert into oa_fl_timer (FLOW_ID,TYPE,USER_STR,REMIND_DATE,REMIND_TIME) values ('"+flowId+"','"+type+"','"+privUser+"','"+remindDate+"','"+remindTime+"')";
    }
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(query);
      ps.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally{
      YHDBUtility.close(ps, null, null);
    }
  }

  public void delTimer(Connection dbConn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    String del = "delete from oa_fl_timer where SEQ_ID =" + seqId;
    YHWorkFlowUtility.updateTableBySql(del, dbConn);
  }

  public String getTimers(Connection dbConn, String flowId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    String sql = "select * from oa_fl_timer where flow_ID =" + flowId;
    
    sb.append("[");
    Statement stm = null;
    ResultSet rs = null;
    int count = 0 ;
    YHPersonLogic logic = new YHPersonLogic();
    try{
      stm = dbConn.createStatement();
      rs = stm.executeQuery(sql);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        
        String remindDate =YHUtility.null2Empty( rs.getString("REMIND_DATE"));
        String remindTime = YHUtility.null2Empty(rs.getString("REMIND_TIME"));
        
        String type = rs.getString("TYPE");
        if ("1".equals(type)) {
          type = "只运行一次";
        } else if ("2".equals(type)) {
          type = "按日";
          remindDate = "";
        }else if ("3".equals(type)) {
          type = "按周";
          remindDate = "周" + remindDate;
        }else if ("4".equals(type)) {
          type = "按月";
          remindDate =  remindDate + "日";
        }else if ("5".equals(type)) {
          type = "按年";
          remindDate = remindDate.replace("-","月");
          remindDate =  remindDate + "日";
        }
        
        String userStr = YHUtility.null2Empty(rs.getString("USER_STR"));
        userStr = logic.getNameBySeqIdStr(userStr, dbConn);
        sb.append("{"); 
        sb.append("seqId:" + seqId);
        sb.append(",type:\"" + type + "\"");
        sb.append(",privUser:\"" + YHUtility.encodeSpecial(userStr)+ "\"");
        sb.append(",remindDate:\"" +remindDate + "\"");
        sb.append(",remindTime:\"" + remindTime + "\"");
        sb.append("},"); 
        count++;
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1 );
    }
    sb.append("]");
    return sb.toString();
  }

  public String getTimer(Connection dbConn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    String sql = "select * from oa_fl_timer where seq_ID =" + seqId;
    
    
    Statement stm = null;
    ResultSet rs = null;
    YHPersonLogic logic = new YHPersonLogic();
    try{
      stm = dbConn.createStatement();
      rs = stm.executeQuery(sql);
      if (rs.next()) {
        
        String remindDate = rs.getString("REMIND_DATE");
        String remindTime = rs.getString("REMIND_TIME");
        
        
        String type = rs.getString("TYPE");
        String userStr = YHUtility.null2Empty(rs.getString("USER_STR"));
        String privUserName = logic.getNameBySeqIdStr(userStr, dbConn);
        sb.append("{"); 
        sb.append("type:\"" + type + "\"");
        sb.append(",privUser:\"" + userStr+ "\"");
        sb.append(",privUserName:\"" + YHUtility.encodeSpecial(privUserName)+ "\"");
        sb.append(",remindDate:\"" +remindDate + "\"");
        sb.append(",remindTime:\"" + remindTime + "\"");
        sb.append("}"); 
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    return sb.toString();
  }
  public String getTime(int d) {
    if (d > 10) {
      return d+"";
    } else {
      return "0" + d;
    }
  }
  public final static byte[] loc = new byte[1];
  public void timeRun(Connection conn ) throws Exception {
    String sql = "select * from oa_fl_timer " ;
    synchronized(loc) {
      Statement stm = null;
      ResultSet rs = null;
      int count = 0 ;
      YHPersonLogic logic = new YHPersonLogic();
      Date now = new Date();
      long s5 = 5 * 60 * 1000;
      long d1 = 1000 * 60 * 60 * 24;
      long d7 = 7 * 1000 * 60 * 60 * 24;
      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
      try{
        stm = conn.createStatement();
        rs = stm.executeQuery(sql);
        while (rs.next()) {
          boolean isNew = false;
          int flowId = rs.getInt("FLOW_ID");
          int seqId = rs.getInt("SEQ_ID");
          
          Date lastTime = rs.getTimestamp("LAST_TIME");
          String remindDate =YHUtility.null2Empty(rs.getString("REMIND_DATE"));
          String userStr =YHUtility.null2Empty(rs.getString("USER_STR"));
          String remindTime = YHUtility.null2Empty(rs.getString("REMIND_TIME"));
          
          String type = rs.getString("TYPE");
          if ("1".equals(type)) {
            String time = remindDate + " " + remindTime;
            Date date = sdf.parse(time);
            
            long c = Math.abs(now.getTime() - date.getTime()) ;
            if (lastTime == null && c <= s5) {
              isNew = true;
            }
          } else if ("2".equals(type)) {
              String da = sdf2.format(now) + " " + remindTime.trim();
              Date date2 = sdf.parse(da);
              long c = Math.abs(now.getTime() - date2.getTime()) ;
              if (c <= s5) {
                isNew = true;
              }
          }else if ("3".equals(type)) {
            int today = now.getDay();
            if (today == Integer.parseInt(remindDate)) {
              String da = sdf2.format(now) + " " + remindTime.trim();
              Date date2 = sdf.parse(da);
              long c = Math.abs(now.getTime() - date2.getTime()) ;
              if (c <= s5) {
                isNew = true;
              }
            }
          }else if ("4".equals(type)) {
            int today = now.getDate();
            if (today == Integer.parseInt(remindDate)) {
              String da = sdf2.format(now) + " " + remindTime.trim();
              Date date2 = sdf.parse(da);
              long c = Math.abs(now.getTime() - date2.getTime()) ;
              if (c <= s5) {
                isNew = true;
              }
            }
          }else if ("5".equals(type)) {
            String mon = remindDate.split("-")[0];
            String day = remindDate.split("-")[1];
            int today = now.getDate();
            int nowMon = now.getMonth();
            if (today == Integer.parseInt(day) && nowMon == Integer.parseInt(mon)) {
              String da = sdf2.format(now) + " " + remindTime.trim();
              Date date2 = sdf.parse(da);
              long c = Math.abs(now.getTime() - date2.getTime()) ;
              if (c <= s5) {
                isNew = true;
              }
            }
          }
          if (isNew) {
            this.createFlow(conn, flowId, userStr);
            this.updateLastTime(conn, seqId, now);
          }
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(stm, rs, null);
      }
      conn.commit();
    }
  }
  public void createFlow(Connection conn , int flowId , String userStr) throws Exception {
    String[] userIds = userStr.split(",");
    YHFlowProcessLogic fpl = new YHFlowProcessLogic();
    YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
    List<YHFlowProcess> list = fpl.getFlowProcessByFlowId(flowId , conn);
    YHFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , conn);
    YHFlowRunLogic frl = new YHFlowRunLogic();
    YHPersonLogic  perLogic  = new YHPersonLogic();
    for (String user : userIds) {
      YHPerson p = perLogic.getPerson(conn, user);
      boolean flag = YHWorkFlowUtility.checkPriv(flowType, list, p , conn);
      if (!flag) {
        String runName = frl.getRunName(flowType, p , conn , false) ;
        frl.createNewWork(p, flowType, runName , conn);
      }
    }
  }
  public void updateLastTime(Connection conn , int seqId , Date date) throws Exception {
   String   query ="update oa_fl_timer set  LAST_TIME=? where SEQ_ID='"+seqId+"'";
   PreparedStatement ps = null;
   try {
     ps = conn.prepareStatement(query);
     Timestamp ts = new Timestamp(date.getTime());
     ps.setTimestamp(1, ts);
     ps.executeUpdate();
   } catch (Exception ex) {
     throw ex;
   } finally{
     YHDBUtility.close(ps, null, null);
   }
  }
}

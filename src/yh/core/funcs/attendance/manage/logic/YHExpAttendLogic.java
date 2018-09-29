package yh.core.funcs.attendance.manage.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.funcs.attendance.personal.data.YHAttendEvection;
import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.funcs.person.logic.YHPersonLogic;

public class YHExpAttendLogic {
  private static Logger log = Logger.getLogger(YHExpAttendLogic.class);
  public  ArrayList<YHDbRecord>  getOutCVS(Connection dbConn,List<YHAttendOut> outList) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ArrayList<YHDbRecord>  cvs = new   ArrayList<YHDbRecord> ();
    YHPersonLogic tpl = new YHPersonLogic();
    YHManageOutLogic yhaol = new YHManageOutLogic();
    for (int i = 0; i < outList.size(); i++) {
      YHAttendOut out = new YHAttendOut();
      out = outList.get(i);
      YHDbRecord rc = new YHDbRecord();
      String userName = tpl.getNameBySeqIdStr(out.getUserId(), dbConn);
      String leaderName = tpl.getNameBySeqIdStr(out.getLeaderId() , dbConn);
      String deptName = yhaol.selectByUserIdDept(dbConn, out.getUserId());
      String allow = out.getAllow();
      String status = out.getStatus();
      rc.addField("部门",deptName);
      rc.addField("姓名",userName);
      
      
      rc.addField("外出原因",out.getOutType());
      rc.addField("登记IP",out.getRegisterIp());
      
      rc.addField("外出日期",out.getSubmitTime());  
      rc.addField("外出时间",out.getOutTime1());
      rc.addField("归来时间",out.getOutTime2());
      
      rc.addField("审批人员",leaderName);  
      String outStatus = "待批";
      if(allow.equals("1")&&status.equals("0")){
        outStatus = "审批";
      }
      if(allow.equals("2")&&status.equals("0")){
        outStatus = "未批";
      }
      if(allow.equals("1")&&status.equals("1")){
        outStatus = "已归来";
      }
      rc.addField("状态",outStatus);
      cvs.add(rc);
      
    }
    return cvs;
  }
  public  ArrayList<YHDbRecord>  getLeaveCVS(Connection dbConn,List<YHAttendLeave> leaveList) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ArrayList<YHDbRecord>  cvs = new   ArrayList<YHDbRecord> ();
    YHPersonLogic tpl = new YHPersonLogic();
    YHManageOutLogic yhaol = new YHManageOutLogic();
    for (int i = 0; i < leaveList.size(); i++) {
      YHAttendLeave leave = new YHAttendLeave();
      leave = leaveList.get(i);
      YHDbRecord rc = new YHDbRecord();
      String userName = tpl.getNameBySeqIdStr(leave.getUserId(), dbConn);
      String leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
      String deptName = yhaol.selectByUserIdDept(dbConn, leave.getUserId());
      String allow = leave.getAllow();
      String status = leave.getStatus();
      rc.addField("部门",deptName);
      rc.addField("姓名",userName);
      
      
      rc.addField("请假原因",leave.getLeaveType());
      rc.addField("占休年假",leave.getAnnualLeave());
      rc.addField("登记IP",leave.getRegisterIp());
      
      rc.addField("开始日期",leave.getLeaveDate1());  
      rc.addField("结束日期",leave.getLeaveDate2());
      
      rc.addField("审批人员",leaderName);  
      String leaveStatus = "待批";
      if(status.equals("1")&&allow.equals("1")){
        leaveStatus = "现行";
      }
      if(status.equals("1")&&allow.equals("2")){
        leaveStatus = "未批";
      }
      if(status.equals("1")&&allow.equals("3")){
        leaveStatus = "现行";
      }
      if(status.equals("2")&&allow.equals("3")){
        leaveStatus = "已销毁";
      }
      rc.addField("状态",leaveStatus);
      cvs.add(rc);
    }
    return cvs;
  }
  public  ArrayList<YHDbRecord>  getEvectionCVS(Connection dbConn,List<YHAttendEvection> evectionList) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ArrayList<YHDbRecord>  cvs = new   ArrayList<YHDbRecord> ();
    YHPersonLogic tpl = new YHPersonLogic();
    YHManageOutLogic yhaol = new YHManageOutLogic();
    for (int i = 0; i < evectionList.size(); i++) {
      YHAttendEvection evection = new YHAttendEvection();
      evection = evectionList.get(i);
      YHDbRecord rc = new YHDbRecord();
      String userName = tpl.getNameBySeqIdStr(evection.getUserId(), dbConn);
      String leaderName = tpl.getNameBySeqIdStr(evection.getLeaderId() , dbConn);
      String deptName = yhaol.selectByUserIdDept(dbConn, evection.getUserId());
      String allow = evection.getAllow();
      String status = evection.getStatus();
      rc.addField("部门",deptName);
      rc.addField("姓名",userName);
      
      
      rc.addField("出差地点",evection.getEvectionDest());
      rc.addField("登记IP",evection.getRegisterIp());
      
      rc.addField("开始日期",evection.getEvectionDate1());  
      rc.addField("结束日期",evection.getEvectionDate2());
      
      rc.addField("审批人员",leaderName);  
      String evectionStatus = "在外";
      if(status.equals("1")&&allow.equals("0")){
        evectionStatus = "待批";
      }
      if(status.equals("1")&&allow.equals("1")){
        evectionStatus = "现行";
      }
      if(status.equals("2")&&allow.equals("1")){
        evectionStatus = "归来";
      }
      rc.addField("状态",evectionStatus);
      cvs.add(rc);
    }
    return cvs;
  }
}

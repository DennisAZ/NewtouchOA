package yh.core.funcs.workplan.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.core.data.YHDbRecord;
import yh.core.funcs.workplan.data.YHWorkPlan;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHExportLogic {

  public ArrayList<YHDbRecord> getDbRecord(List<YHWorkPlan> worklist,Connection dbConn) throws Exception{
    //System.out.println(worklist.size());
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHWorkPlan plan = new YHWorkPlan();
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    String time = sf.format(new Date());//系统当前时间
    int nian = Integer.parseInt(time.toString().substring(0,4));
    int yue = Integer.parseInt(time.toString().substring(5,7));
    int day = Integer.parseInt(time.toString().substring(8,10));
    
    int planNian = 0;
    int planYue = 0;
    int planDay = 0;
    for (int i = 0; i < worklist.size(); i++) {
      plan = worklist.get(i);
      if (plan.getEndTime() != null) {
        planNian = Integer.parseInt(plan.getEndTime().toString().substring(0,4));
        planYue = Integer.parseInt(plan.getEndTime().toString().substring(5,7));
        planDay = Integer.parseInt(plan.getEndTime().toString().substring(8,10));
      }
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("计划名称",plan.getName());
      dbrec.addField("计划内容",plan.getContent());
      dbrec.addField("开始时间",plan.getStatrTime());
      dbrec.addField("结束时间  ",plan.getEndTime());
      if (!YHUtility.isNullorEmpty(plan.getType())) {
        dbrec.addField("计划类别 ",getType(dbConn,plan.getType()));
      }
      if (YHUtility.isNullorEmpty(plan.getType())) {
        dbrec.addField("计划类别 ",plan.getType());
      }
      if (plan.getDeptParentDesc().equals("0") || plan.getDeptParentDesc().equals("ALL_DEPT")) {
        dbrec.addField("开放部门  ","全体部门");
      }
      if (!plan.getDeptParentDesc().equals("0") && !plan.getDeptParentDesc().equals("ALL_DEPT") && !YHUtility.isNullorEmpty(plan.getDeptParentDesc())) {
        dbrec.addField("开放部门  ", getDept(dbConn,plan.getDeptParentDesc()));
      }
      if (YHUtility.isNullorEmpty(plan.getDeptParentDesc())) {
        dbrec.addField("开放部门  ",plan.getDeptParentDesc());
      }
      if (!YHUtility.isNullorEmpty(plan.getManagerDesc())) { 
        dbrec.addField("开放人员",getManagerDesc(dbConn,plan.getManagerDesc()));
      }
      if (YHUtility.isNullorEmpty(plan.getManagerDesc())) { 
        dbrec.addField("开放人员",plan.getManagerDesc());
      }
      if (!YHUtility.isNullorEmpty(plan.getLeader2Desc())) {
        dbrec.addField("负责人",getManagerDesc(dbConn,plan.getLeader2Desc()));
      }
      if (YHUtility.isNullorEmpty(plan.getLeader2Desc())) {
        dbrec.addField("负责人",plan.getLeader2Desc());
      }
      if (!YHUtility.isNullorEmpty(plan.getLeader1Desc())) {
        dbrec.addField("参与人",getManagerDesc(dbConn,plan.getLeader1Desc()));
      }
      if (YHUtility.isNullorEmpty(plan.getLeader1Desc())) {
        dbrec.addField("参与人",plan.getLeader1Desc());
      }
      dbrec.addField("创建人",getManagerDesc(dbConn,plan.getCreator()));
      dbrec.addField("创建日期",plan.getCreatedate());
      if (plan.getPublish().equals("0")) {
        dbrec.addField("状态","未发布");
      }
      if (plan.getPublish().equals("2")) {
        dbrec.addField("状态","暂停");
      }
      if (plan.getPublish().equals("1") && plan.getEndTime() == null) {
        dbrec.addField("状态","进行中");
      }
      if (plan.getPublish().equals("1") && plan.getEndTime() != null && (planNian > nian || (planNian == nian && planYue > yue) || (planNian == nian && planYue == yue && planDay >= day))) {
        dbrec.addField("状态","进行中");
      }
      if (plan.getPublish().equals("1") && plan.getEndTime() != null && (planNian < nian || (planNian == nian && planYue < yue) || (planNian == nian && planYue == yue && planDay < day))) {
        dbrec.addField("状态","已结束");
      }
      if (plan.getPublish().equals("3")) {
        dbrec.addField("状态","已结束");
      }
      dbrec.addField("备注",plan.getRemark());
      dbL.add(dbrec);
    }
    return dbL;
  }
  //类型
  public String getType(Connection dbConn,String typeId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    String nameType = "";
    String sql = "select TYPE_NAME from oa_plan_kind where SEQ_ID=" + Integer.parseInt(typeId);
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        nameType = rs.getString("TYPE_NAME");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return nameType;
  }
  //部门
  public String getDept(Connection dbConn,String deptId) {
    // bindDesc([{cntrlId:"dept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    ResultSet rs = null;
    PreparedStatement ps = null;
    String deptName = null;
    String deptString = "";
    try {
      for (int i = 0; i < deptId.split(",").length;i++) {
        String sql = "select DEPT_NAME from oa_department where SEQ_ID=" + deptId.split(",")[i];
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        if (rs.next()) {
          deptName = rs.getString("DEPT_NAME");
        }
        deptString += deptName + ",";
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return deptString;
  }

  //人员
  public String getManagerDesc(Connection dbConn,String managerDesc) {
    // bindDesc([{cntrlId:"manager", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    ResultSet rs = null;
    PreparedStatement ps = null;
    String managerName = null;
    String managerString = "";
    try {
      for (int i = 0; i < managerDesc.split(",").length;i++) {
        String sql = "select USER_NAME from PERSON where SEQ_ID=" + managerDesc.split(",")[i];
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        if (rs.next()) {
          managerName = rs.getString("USER_NAME");
        }
        managerString += managerName + ",";
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return managerString;
  }
}

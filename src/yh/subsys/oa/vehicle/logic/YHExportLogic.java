package yh.subsys.oa.vehicle.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import yh.core.data.YHDbRecord;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.vehicle.data.YHVehicleUsage;

public class YHExportLogic {
  /**
   * lz
   * 
   * */
  public static ArrayList<YHDbRecord> getDbRecord(List<YHVehicleUsage> usageList,Connection dbConn) throws Exception{
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHVehicleUsage usage = new YHVehicleUsage();
    for (int i = 0; i < usageList.size(); i++) {
      usage = usageList.get(i);

      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("车牌号",usage.getVId());
      if (!YHUtility.isNullorEmpty(usage.getVuDriver())) {
        dbrec.addField("司机",getUser(dbConn,usage.getVuDriver()));
      }
      if (YHUtility.isNullorEmpty(usage.getVuDriver())) {
        dbrec.addField("司机","");
      }
      if (!YHUtility.isNullorEmpty(usage.getVuProposer())) {
        dbrec.addField("申请人",getUser(dbConn,usage.getVuProposer()));
      }
      if (YHUtility.isNullorEmpty(usage.getVuProposer())) {
        dbrec.addField("申请人","");
      }
      dbrec.addField("申请时间 ",usage.getVuRequestDate());
      dbrec.addField("用车人 ",usage.getVuUser());
      if (!YHUtility.isNullorEmpty(usage.getVuDept())) {
        dbrec.addField("用车部门",getDept(dbConn,usage.getVuDept()));
      }
      if (YHUtility.isNullorEmpty(usage.getVuDept())) {
        dbrec.addField("用车部门","");
      }
      dbrec.addField("事由",usage.getVuReason());
      dbrec.addField("开始时间",usage.getVuStart());
      dbrec.addField("结束时间",usage.getVuEnd());
      dbrec.addField("目的地 里程(公里)",usage.getVuDestination());
      dbrec.addField("里程(公里)",usage.getVuMileage());
      if (!YHUtility.isNullorEmpty(usage.getVuOperator())) {
        dbrec.addField("调度员",getUser(dbConn,usage.getVuOperator()));
      }
      if (YHUtility.isNullorEmpty(usage.getVuOperator())) {
        dbrec.addField("调度员",getUser(dbConn,usage.getVuOperator()));
      }
      if (usage.getVuStatus().equals("0")) {
        dbrec.addField("当前状态","待批");
      }
      if (usage.getVuStatus().equals("1")) {
        dbrec.addField("当前状态","已准");
      }
      if (usage.getVuStatus().equals("2")) {
        dbrec.addField("当前状态","使用中");
      }
      if (usage.getVuStatus().equals("3")) {
        dbrec.addField("当前状态","未准");
      }
      if (usage.getVuStatus().equals("4")) {
        dbrec.addField("当前状态","结束");
      }
      dbrec.addField("备注",usage.getVuRemark());
      dbL.add(dbrec);
    }
    return dbL;
  }
  /***
   * 根据条件查询数据,导出数据
   * @return
   * @throws Exception 
   */
  public static List<YHVehicleUsage> vehicleAll(Connection dbConn,YHVehicleUsage usage,String vuRequestDateMax,String vuStartMax,String vuEndMax) throws Exception {
    String sql = "select veus.SEQ_ID,veus.V_ID,ve.V_NUM as Vnum,pe.USER_NAME as userName,veus.VU_REASON,veus.VU_DESTINATION,veus.VU_START,veus.VU_END,veus.VU_REMARK"
      + ",veus.VU_STATUS,veus.dmer_status,veus.VU_REQUEST_DATE,veus.VU_PROPOSER,veus.VU_MILEAGE"
      + ",veus.VU_DEPT,veus.VU_OPERATOR,veus.VU_DRIVER,veus.SMS_REMIND"
      + ",veus.SMS2_REMIND,veus.DEPT_MANAGER,veus.SHOW_FLAG,veus.DEPT_REASON,veus.OPERATOR_REASON,veus.VU_MILEAGE_TRUE,veus.VU_PARKING_FEES "
      + " from oa_vehicle_usage veus left outer join vehicle ve on ve.seq_id=veus.V_ID " 
      + " left outer join person pe on pe.seq_id=veus.VU_USER where 1=1 ";
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    List<YHVehicleUsage> usageList = new ArrayList<YHVehicleUsage>();
    YHVehicleUsage vehicle = null;
    try {
      if (!YHUtility.isNullorEmpty(usage.getVuStatus())) {
        sql += " and veus.VU_STATUS=" + usage.getVuStatus();
      }
      if (!YHUtility.isNullorEmpty(usage.getVId())) {
        sql += " and veus.V_ID=" + usage.getVId();
      }
      if (!YHUtility.isNullorEmpty(usage.getVuDriver())) {
        sql += " and veus.VU_DRIVER=" + usage.getVuDriver();
      }
      if (!YHUtility.isNullorEmpty(usage.getVuUser())) {
        sql += " and veus.vu_user=" + usage.getVuUser();
      }
      if (!YHUtility.isNullorEmpty(usage.getVuDept())) {
        sql += " and veus.VU_DEPT=" + usage.getVuDept();
      }
      if (!YHUtility.isNullorEmpty(usage.getVuProposer())) {
        sql += " and veus.VU_PROPOSER=" + usage.getVuProposer();
      }
      if (!YHUtility.isNullorEmpty(usage.getVuReason())) {
        //System.out.println(usage.getVuReason());
        sql += " and veus.vu_reason like '%" + YHDBUtility.escapeLike(usage.getVuReason()) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(usage.getVuRemark())) {
        sql += " and veus.VU_REMARK like '%" + YHDBUtility.escapeLike(usage.getVuRemark()) + "%' " + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(usage.getVuOperator())) {
        sql += " and veus.VU_OPERATOR=" + usage.getVuOperator();
      }
      if (usage.getVuRequestDate() != null) {
        String str =  YHDBUtility.getDateFilter("veus.vu_request_date", YHUtility.getDateTimeStr(usage.getVuRequestDate()), ">=");
        sql += " and " + str;
      }
      if (!YHUtility.isNullorEmpty(vuRequestDateMax)) {
        String str =  YHDBUtility.getDateFilter("veus.vu_request_date", YHUtility.getDateTimeStr(YHUtility.parseDate(vuRequestDateMax)), "<=");
        sql += " and " + str;
      }
      if (usage.getVuStart() != null) {
        String str =  YHDBUtility.getDateFilter("veus.VU_START", YHUtility.getDateTimeStr(usage.getVuStart()), ">=");
        sql += " and " + str;
      }
      if (!YHUtility.isNullorEmpty(vuStartMax)) {
        String str =  YHDBUtility.getDateFilter("veus.VU_START", YHUtility.getDateTimeStr(YHUtility.parseDate(vuStartMax)), "<=");
        sql += " and " + str;
      }
      if (usage.getVuEnd() != null) {
        String str =  YHDBUtility.getDateFilter("veus.VU_END", YHUtility.getDateTimeStr(usage.getVuEnd()), ">=");
        sql += " and " + str;
      }
      if (!YHUtility.isNullorEmpty(vuEndMax)) {
        String str =  YHDBUtility.getDateFilter("veus.VU_END", YHUtility.getDateTimeStr(YHUtility.parseDate(vuEndMax)), "<=");
        sql += " and " + str;
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        vehicle = new YHVehicleUsage(); 
        vehicle.setVId(rs.getString("Vnum"));
        vehicle.setVuDriver(rs.getString("vu_driver"));
        vehicle.setVuProposer(rs.getString("vu_proposer"));
        vehicle.setVuRequestDate(rs.getDate("vu_request_date"));
        vehicle.setVuUser(rs.getString("userName"));
        vehicle.setVuDept(rs.getString("vu_dept"));
        vehicle.setVuReason(rs.getString("vu_reason"));
        vehicle.setVuStart(rs.getDate("vu_start"));
        vehicle.setVuEnd(rs.getDate("vu_end"));
        vehicle.setVuDestination(rs.getString("vu_destination"));
        vehicle.setVuOperator(rs.getString("vu_operator"));
        vehicle.setVuStatus(rs.getString("vu_status"));
        vehicle.setVuRemark(rs.getString("vu_remark"));
        vehicle.setVuMileage(rs.getInt("vu_mileage"));
        usageList.add(vehicle);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,rs,null);
    }
    return usageList;
  }


  //部门
  public static String getDept(Connection dbConn,String deptId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    String deptName = null;
    try {
      String sql = "select DEPT_NAME from oa_department where SEQ_ID=" + deptId;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        deptName = rs.getString("DEPT_NAME");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return deptName;
  }

  //人员
  public static String getUser(Connection dbConn,String managerDesc) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    String managerName = null;
    try {
      String sql = "select USER_NAME from PERSON where SEQ_ID=" + managerDesc;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        managerName = rs.getString("USER_NAME");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return managerName;
  }
}

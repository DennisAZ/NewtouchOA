package yh.subsys.oa.vehicle.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.vehicle.data.YHVehicleMaintenance;

public class YHVehicleMaintenanceLogic {
  private static Logger log = Logger.getLogger(YHVehicleMaintenanceLogic.class);
  public void addMaintenance(Connection dbConn, YHVehicleMaintenance vcMaintenance) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, vcMaintenance);  
  }
  public void updateMaintenance(Connection dbConn, YHVehicleMaintenance vcMaintenance) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, vcMaintenance);  
  }
  /**
   * 按条件查询
   * @param dbConn
   * @param vId
   * @param vmBeginDate
   * @param vmEndDate
   * @param vmReason
   * @param vmType
   * @param vmPerson
   * @param vmFeeMin
   * @param vmFeeMax
   * @param vmRemark
   * @param asc
   * @return
   * @throws Exception
   */
  public ArrayList<Map<String,String> > selectMaintenance(Connection dbConn ,String vId,String vmBeginDate,String vmEndDate,String vmReason,String vmType,String vmPerson,String vmFeeMin,String vmFeeMax,String vmRemark,String asc) throws Exception{
    Statement stmt2 = null;
    ResultSet rs2 = null;
    Map typeMap = new HashMap();
    String sql2 = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_CODE from OA_KIND_DICT_ITEM where CLASS_NO = (select CLASS_NO from oa_kind_dict where CLASS_NO = 'VEHICLE_REPAIR_TYPE') order by SORT_NO";
    try {
      stmt2 = dbConn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      while (rs2.next()) {
        String classCode = rs2.getString("CLASS_CODE");
        String classDesc = rs2.getString("CLASS_DESC");
        typeMap.put(classCode, classDesc);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt2, rs2, log);
    }
    
    
    Statement stmt = null;
    ResultSet rs = null;
    Map maintenancemap = null;
    ArrayList<Map<String,String> > maintenanceList = new ArrayList<Map<String,String> >();
    String sql = "select vm.SEQ_ID as SEQ_ID,vc.V_NUM as V_NUM,vm.V_ID as V_ID,vm.VM_REQUEST_DATE as VM_REQUEST_DATE,vm.VM_TYPE as VM_TYPE,vm.VM_REASON as VM_REASON,vm.VM_PERSON as VM_PERSON,vm.VM_FEE as VM_FEE,vm.VM_REMARK as VM_REMARK " +
    		"  from oa_vehicle_upkeep vm left outer join  VEHICLE vc on  vm.V_ID = vc.SEQ_ID where 1 = 1 ";
    if(!YHUtility.isNullorEmpty(vId)){
      sql = sql + " and vm.V_ID=" + vId;
    }
    //System.out.println(map.get("vmBeginDate"));
    if(!YHUtility.isNullorEmpty(vmBeginDate)){
      sql = sql + " and "  + YHDBUtility.getDateFilter("vm.VM_REQUEST_DATE", vmBeginDate, ">=");
    }
    if(!YHUtility.isNullorEmpty(vmEndDate)){
      sql = sql + " and "  + YHDBUtility.getDateFilter("vm.VM_REQUEST_DATE", vmEndDate + " 23:59:59", "<=");
    }
    if(!YHUtility.isNullorEmpty(vmReason)){
      sql = sql + " and vm.VM_REASON like '" + YHDBUtility.escapeLike(vmReason) + "' " + YHDBUtility.escapeLike();
    }
    if(!YHUtility.isNullorEmpty(vmType)){
      sql = sql + " and vm.VM_TYPE=" + vmType;
    }
    if(!YHUtility.isNullorEmpty(vmPerson)){
      sql = sql + " and vm.VM_PERSON='" + vmPerson.replace("'", "''") + "'";
    }
    if(!YHUtility.isNullorEmpty(vmRemark)){
      sql = sql + " and vm.VM_REMARK like '" + YHDBUtility.escapeLike(vmRemark) + "' " + YHDBUtility.escapeLike();
    }

    if(!YHUtility.isNullorEmpty(vmFeeMin)){
      sql = sql + " and vm.VM_FEE >= " +vmFeeMin;
    }
    if(!YHUtility.isNullorEmpty(vmFeeMax)){
      sql = sql + " and vm.VM_FEE <= " + vmFeeMax;
    }
    sql = sql + " order by  vm.VM_REQUEST_DATE " + asc;
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      Map<String,String> mapTotal= new HashMap<String,String>();
      double total = 0;
      while(rs.next()){
        Map<String,String> mapmaintenance = new HashMap<String,String>();
        String seqId = rs.getString("SEQ_ID");
        String vid  = rs.getString("V_ID");
        String vNum  = rs.getString("V_NUM");
        String vmFee = rs.getString("VM_FEE");
        String vmperson = rs.getString("VM_PERSON");
        String vmreason = rs.getString("VM_REASON");
        String vmremark = rs.getString("VM_REMARK");
        Date vmRequestDate = rs.getDate("VM_REQUEST_DATE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String vmRequestDateStr = "";
        if(vmRequestDate != null){
          vmRequestDateStr = dateFormat.format(vmRequestDate);
        }
     
        //System.out.println(vmRequestDate);
        String vmtype = rs.getString("VM_TYPE");
        mapmaintenance.put("seqId", seqId);
        mapmaintenance.put("vId", vid);  
        mapmaintenance.put("vNum", vNum);
        mapmaintenance.put("vmFee", vmFee);
        if(!YHUtility.isNullorEmpty(vmFee)){
          total = total +  Double.parseDouble(vmFee);
        }
        mapmaintenance.put("vmperson", vmperson);
        mapmaintenance.put("vmReason", vmreason);
        mapmaintenance.put("vmRemark", vmremark);
        mapmaintenance.put("vmRequestDate", vmRequestDateStr);
        vmtype  = (String)typeMap.get(vmtype);
        mapmaintenance.put("vmType", vmtype);
        //mapmaintenance.put("vmFeeTotal", vmFeeTotal);
        
        maintenanceList.add(mapmaintenance);
      }
      mapTotal.put("total", total+"");
      maintenanceList.add(mapTotal);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maintenanceList;
  }
  /**
   * 按条件查询
   * @param dbConn
   * @param vId
   * @param vmBeginDate
   * @param vmEndDate
   * @param vmReason
   * @param vmType
   * @param vmPerson
   * @param vmFeeMin
   * @param vmFeeMax
   * @param vmRemark
   * @param asc
   * @return
   * @throws Exception
   */
  public ArrayList<YHDbRecord > selectMaintenanceCvs(Connection dbConn ,String vId,String vmBeginDate,String vmEndDate,String vmReason,String vmType,String vmPerson,String vmFeeMin,String vmFeeMax,String vmRemark,String asc) throws Exception{
    Statement stmt2 = null;
    ResultSet rs2 = null;
    Map typeMap = new HashMap();
    String sql2 = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_CODE from OA_KIND_DICT_ITEM where CLASS_NO = (select CLASS_NO from oa_kind_dict where CLASS_NO = 'VEHICLE_REPAIR_TYPE') order by SORT_NO";
    try {
      stmt2 = dbConn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      while (rs2.next()) {
        String classCode = rs2.getString("CLASS_CODE");
        String classDesc = rs2.getString("CLASS_DESC");
        typeMap.put(classCode, classDesc);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt2, rs2, log);
    }
    
    Statement stmt = null;
    ResultSet rs = null;
    Map maintenancemap = null;
    ArrayList<YHDbRecord > maintenanceList = new ArrayList<YHDbRecord>();
    String sql = "select vm.SEQ_ID as SEQ_ID,vc.V_NUM as V_NUM,vm.V_ID as V_ID,vm.VM_REQUEST_DATE as VM_REQUEST_DATE,vm.VM_TYPE as VM_TYPE,vm.VM_REASON as VM_REASON,vm.VM_PERSON as VM_PERSON,vm.VM_FEE as VM_FEE,vm.VM_REMARK as VM_REMARK " +
        "  from oa_vehicle_upkeep vm left outer join VEHICLE vc on  vm.V_ID = vc.SEQ_ID where 1 = 1";
    if(!YHUtility.isNullorEmpty(vId)){
      sql = sql + " and vm.V_ID=" + vId;
    }
    //System.out.println(map.get("vmBeginDate"));
    if(!YHUtility.isNullorEmpty(vmBeginDate)){
      sql = sql + " and "  + YHDBUtility.getDateFilter("VM_REQUEST_DATE", vmBeginDate, ">=");
    }
    if(!YHUtility.isNullorEmpty(vmEndDate)){
      sql = sql + " and "  + YHDBUtility.getDateFilter("VM_REQUEST_DATE", vmEndDate + " 23:59:59", "<=");
    }
    if(!YHUtility.isNullorEmpty(vmReason)){
      sql = sql + " and vm.VM_REASON like '" + YHDBUtility.escapeLike(vmReason) + "' " + YHDBUtility.escapeLike();
    }
    if(!YHUtility.isNullorEmpty(vmType)){
      sql = sql + " and vm.VM_TYPE=" + vmType;
    }
    if(!YHUtility.isNullorEmpty(vmPerson)){
      sql = sql + " and vm.VM_PERSON='" + vmPerson.replace("'", "''") + "'";
    }
    if(!YHUtility.isNullorEmpty(vmRemark)){
      sql = sql + " and vm.VM_REMARK like '" + YHDBUtility.escapeLike(vmRemark) + "' " + YHDBUtility.escapeLike();
    }

    if(!YHUtility.isNullorEmpty(vmFeeMin)){
      sql = sql + " and vm.VM_FEE >= " +vmFeeMin;
    }
    if(!YHUtility.isNullorEmpty(vmFeeMax)){
      sql = sql + " and vm.VM_FEE <= " + vmFeeMax;
    }
    sql = sql + " order by  vm.VM_REQUEST_DATE " + asc;
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      Map<String,String> mapTotal= new HashMap<String,String>();
      double total = 0;
      while(rs.next()){
        YHDbRecord rc = new YHDbRecord();
        String seqId = rs.getString("SEQ_ID");
        String vid  = rs.getString("V_ID");
        String vNum  = rs.getString("V_NUM");
        String vmFee = rs.getString("VM_FEE");
        String vmperson = rs.getString("VM_PERSON");
        String vmreason = rs.getString("VM_REASON");
        String vmremark = rs.getString("VM_REMARK");
        Date vmRequestDate = rs.getDate("VM_REQUEST_DATE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String vmRequestDateStr = "";
        if(vmRequestDate != null){
          vmRequestDateStr = dateFormat.format(vmRequestDate);
        }
 
        String vmtype = rs.getString("VM_TYPE");
        String vmTypeDesc = "";
        if(!YHUtility.isNullorEmpty(vmtype)){
          vmTypeDesc = (String)typeMap.get(vmtype);
        }
        rc.addField("车牌号", vNum);
        rc.addField("维护类型", vmTypeDesc);
        rc.addField("维护原因", vmreason);
        rc.addField("维护日期", vmRequestDateStr);
        rc.addField("经办人", vmperson);
        
        rc.addField("维护费用", vmFee);   
        rc.addField("备注", vmremark);
        maintenanceList.add(rc);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maintenanceList;
  }
  /**
   * 按条件查询
   * @param dbConn
   * @param vId
   * @return
   * @throws Exception
   */
  public ArrayList<YHDbRecord > selectMaintenanceCvsByVId(Connection dbConn ,String vId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    Map maintenancemap = null;
    ArrayList<YHDbRecord > maintenanceList = new ArrayList<YHDbRecord>();
    String sql = "select vm.SEQ_ID as SEQ_ID,vc.V_NUM as V_NUM,vm.V_ID as V_ID,vm.VM_REQUEST_DATE as VM_REQUEST_DATE,vm.VM_TYPE as VM_TYPE,vm.VM_REASON as VM_REASON,vm.VM_PERSON as VM_PERSON,vm.VM_FEE as VM_FEE,vm.VM_REMARK as VM_REMARK " +
        "  from oa_vehicle_upkeep vm left outer join VEHICLE vc on  vm.V_ID = vc.SEQ_ID where 1 = 1";
    if(!YHUtility.isNullorEmpty(vId)){
      sql = sql + " and vm.V_ID=" + vId;
    }
    sql = sql + " order by  vm.VM_REQUEST_DATE ";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      Map<String,String> mapTotal= new HashMap<String,String>();
      double total = 0;
      while(rs.next()){
        YHDbRecord rc = new YHDbRecord();
        String seqId = rs.getString("SEQ_ID");
        String vid  = rs.getString("V_ID");
        String vNum  = rs.getString("V_NUM");
        String vmFee = rs.getString("VM_FEE");
        String vmperson = rs.getString("VM_PERSON");
        String vmreason = rs.getString("VM_REASON");
        String vmremark = rs.getString("VM_REMARK");
        Date vmRequestDate = rs.getDate("VM_REQUEST_DATE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String vmRequestDateStr = "";
        if(vmRequestDate != null){
          vmRequestDateStr = dateFormat.format(vmRequestDate);
        }
        String vmtype = rs.getString("VM_TYPE");
        String vmTypeDesc = "";
        if(!YHUtility.isNullorEmpty(vmtype)){
          if(vmtype.equals("0")){
            vmTypeDesc = "维修";
          }
          if(vmtype.equals("1")){
            vmTypeDesc = "加油";
          }
          if(vmtype.equals("2")){
            vmTypeDesc = "洗车";
          }
          if(vmtype.equals("3")){
            vmTypeDesc = "年检";
          }
          if(vmtype.equals("4")){
            vmTypeDesc = "其他";
          }
        }
        rc.addField("车牌号", vNum);
        rc.addField("维护类型", vmTypeDesc);
        rc.addField("维护原因", vmreason);
        rc.addField("维护日期", vmRequestDateStr);
        rc.addField("经办人", vmperson);
        
        rc.addField("维护费用", vmFee);   
        rc.addField("备注", vmremark);
        maintenanceList.add(rc);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maintenanceList;
  }
  /**
   * 按车辆ID 来查询车辆维护记录
   * @param dbConn
   * @param vId
   * @param asc
   * @return
   * @throws Exception
   */
  public ArrayList<Map<String,String> > selectMaintenanceByVId(Connection dbConn ,String vId,String asc) throws Exception{
    Statement stmt2 = null;
    ResultSet rs2 = null;
    Map typeMap = new HashMap();
    String sql2 = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_CODE from OA_KIND_DICT_ITEM where CLASS_NO = (select CLASS_NO from oa_kind_dict where CLASS_NO = 'VEHICLE_REPAIR_TYPE') order by SORT_NO";
    try {
      stmt2 = dbConn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      while (rs2.next()) {
        String classCode = rs2.getString("CLASS_CODE");
        String classDesc = rs2.getString("CLASS_DESC");
        typeMap.put(classCode, classDesc);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt2, rs2, log);
    }
    Statement stmt = null;
    ResultSet rs = null;
    Map maintenancemap = null;
    ArrayList<Map<String,String> > maintenanceList = new ArrayList<Map<String,String> >();
    String sql = "select vm.SEQ_ID as SEQ_ID,vc.V_NUM as V_NUM,vm.V_ID as V_ID,vm.VM_REQUEST_DATE as VM_REQUEST_DATE,vm.VM_TYPE as VM_TYPE,vm.VM_REASON as VM_REASON,vm.VM_PERSON as VM_PERSON,vm.VM_FEE as VM_FEE,vm.VM_REMARK as VM_REMARK " +
        "  from VEHICLE vc left outer join oa_vehicle_upkeep vm on  vm.V_ID = vc.SEQ_ID where 1 = 1";
    if(!YHUtility.isNullorEmpty(vId)){
      sql = sql + " and vm.V_ID=" + vId;
    }
    sql = sql + " order by  vm.VM_REQUEST_DATE " + asc;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      Map<String,String> mapTotal= new HashMap<String,String>();
      double total = 0;
      while(rs.next()){
        Map<String,String> mapmaintenance = new HashMap<String,String>();
        String seqId = rs.getString("SEQ_ID");
        String vid  = rs.getString("V_ID");
        String vNum  = rs.getString("V_NUM");
        String vmFee = rs.getString("VM_FEE");
        String vmperson = rs.getString("VM_PERSON");
        String vmreason = rs.getString("VM_REASON");
        String vmremark = rs.getString("VM_REMARK");
        Date vmRequestDate = rs.getDate("VM_REQUEST_DATE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String vmRequestDateStr = "";
        if(vmRequestDate != null){
          vmRequestDateStr = dateFormat.format(vmRequestDate);
        }
        String vmtype = rs.getString("VM_TYPE");
        mapmaintenance.put("seqId", seqId);
        mapmaintenance.put("vId", vid);  
        mapmaintenance.put("vNum", vNum);
        mapmaintenance.put("vmFee", vmFee);
        if(!YHUtility.isNullorEmpty(vmFee)){
          total = total +  Double.parseDouble(vmFee);
        }
        mapmaintenance.put("vmperson", vmperson);
        mapmaintenance.put("vmReason", vmreason);
        mapmaintenance.put("vmRemark", vmremark);
        mapmaintenance.put("vmRequestDate", vmRequestDateStr);
        vmtype  = (String)typeMap.get(vmtype);
        mapmaintenance.put("vmType", vmtype);
        maintenanceList.add(mapmaintenance);
      }
      mapTotal.put("total", total+"");
      maintenanceList.add(mapTotal);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maintenanceList;
  }
  public Map<String,String> selectMaintenanceById(Connection dbConn ,String seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    Map<String,String> maintenance = null;
    String sql = "select vm.SEQ_ID as SEQ_ID,vc.VC_NUM as VC_NUM,vm.V_ID as VC_ID,vm.VM_REQUEST_DATE as VM_REQUEST_DATE,vm.VM_TYPE as VM_TYPE,vm.VM_REASON as VM_REASON,vm.VM_PERSON as VM_PERSON,vm.VM_FEE as VM_FEE,vm.VM_REMARK as VM_REMARK " +
        "  from VEHICLE vc ,oa_vehicle_upkeep vm where vm.V_ID = vc.SEQ_ID and vm.SEQ_ID = " + seqId;
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        maintenance = new HashMap<String,String>();
        String seqIds = rs.getString("SEQ_ID");
        String vcId  = rs.getString("VC_ID");
        String vcNum  = rs.getString("VC_NUM");
        String vmFee = rs.getString("VM_FEE");
        String vmperson = rs.getString("VM_PERSON");
        String vmReason = rs.getString("VM_REASON");
        String vmRemark = rs.getString("VM_REMARK");
        String vmRequestDate = rs.getString("VM_REQUEST_DATE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        vmRequestDate = dateFormat.format(YHUtility.parseDate(vmRequestDate));
        //System.out.println(vmRequestDate+"-------->");
        String vmType = rs.getString("VM_TYPE");
        maintenance.put("seqId", seqId);
        maintenance.put("vcId", vcId);  
        maintenance.put("vcNum", vcNum);
        maintenance.put("vmFee", vmFee);
        maintenance.put("vmPerson", vmperson);
        maintenance.put("vmReason", vmReason);
        maintenance.put("vmRemark", vmRemark);
        maintenance.put("vmRequestDate", vmRequestDate);
        maintenance.put("vmType", vmType);     
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maintenance;
  }
  public YHVehicleMaintenance selectMaintenanceById(Connection dbConn ,int seqId) throws Exception{
    YHORM orm = new YHORM();
    YHVehicleMaintenance vehicleMain =  (YHVehicleMaintenance) orm.loadObjSingle(dbConn, YHVehicleMaintenance.class, seqId); 
    return vehicleMain;
  }
  public static void deleteMaintenanceByVId(Connection dbConn ,String vId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_vehicle_upkeep  where V_ID =  " + vId;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public static void deleteMaintenanceByVIds(Connection dbConn ,String vIds) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_vehicle_upkeep  where V_ID in(" + vIds + ")";
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

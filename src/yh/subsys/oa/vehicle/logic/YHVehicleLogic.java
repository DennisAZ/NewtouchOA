package yh.subsys.oa.vehicle.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vehicle.data.YHVehicle;

public class YHVehicleLogic {
  private static Logger log = Logger.getLogger(YHVehicleLogic.class);
  public void addVehicle(Connection dbConn, YHVehicle vehicle) throws Exception {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, vehicle);  
  }
  public void updateVehicle(Connection dbConn, YHVehicle vehicle) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, vehicle);  
  }
  public void deleteVehicle(Connection dbConn, String seqId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from VEHICLE WHERE SEQ_ID in (" + seqId + ")";
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
    }catch(Exception ex){
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public ArrayList<YHVehicle> selectVehicle(Connection dbConn, String[] str) throws Exception{
    YHORM orm = new YHORM();
    ArrayList<YHVehicle> vehicleList = (ArrayList<YHVehicle>)orm.loadListSingle(dbConn, YHVehicle.class, str);
    return vehicleList;
  }
  public String toSearchData(Connection conn,Map request,String seqId) throws Exception{
    String sql = "select v.SEQ_ID,v.V_MODEL,v.V_NUM,p.USER_NAME,ci.CLASS_DESC,v.V_DATE,v.V_STATUS,v.insurance_flag"
      +" from VEHICLE v left outer join OA_KIND_DICT_ITEM ci on v.V_TYPE = ci.SEQ_ID left outer join oa_kind_dict cc on "
      +" ci.CLASS_NO = cc.CLASS_NO  left outer join PERSON p on v.V_DRIVER = p.SEQ_ID";
    if(!YHUtility.isNullorEmpty(seqId)){
      sql = sql + " where v.SEQ_ID IN (" + seqId + ") ";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  public YHVehicle selectVehicleById(Connection dbConn, String seqId) throws Exception{
    YHORM orm = new YHORM();
    YHVehicle vehicle = (YHVehicle)orm.loadObjSingle(dbConn, YHVehicle.class,Integer.parseInt(seqId));
    return vehicle;
  }
  public Map<String,String> selectVehicleById(Connection dbConn, int seqId) throws Exception{
    YHORM orm = new YHORM();
    Map<String,String> map = new HashMap<String,String>();
    Statement stmt = null;
    ResultSet rs = null;
    ArrayList<Map<String,String>> vehicleList = new ArrayList<Map<String,String>>();
    try{
      String sql = "select v.SEQ_ID,v.V_DATE,v.V_MODEL,v.V_DRIVER,v.V_NUM,v.V_STATUS,v.V_REMARK,v.V_ENGINE_NUM,ci.CLASS_DESC,v.V_PRICE,v.ATTACHMENT_ID,v.ATTACHMENT_NAME,p.USER_NAME,v.insurance_date"

                   +" from VEHICLE v left outer join OA_KIND_DICT_ITEM ci on v.V_TYPE = ci.SEQ_ID left outer join oa_kind_dict cc on "

                   +" ci.CLASS_NO = cc.CLASS_NO  left outer join PERSON p on v.V_DRIVER = p.SEQ_ID where v.SEQ_ID = " + seqId;
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        map.put("seqId", rs.getString(1));
        map.put("vDate", rs.getString(2));
        map.put("vModel", rs.getString(3));
        map.put("vDriver", rs.getString(4)); 
        map.put("vNum", rs.getString(5));
        map.put("vStatus", rs.getString(6));
        map.put("vRemark", rs.getString(7));
        map.put("vEngineNum", rs.getString(8));
        map.put("classDesc", rs.getString(9));
        map.put("vPrice", rs.getString(10));
        map.put("attachmentId", rs.getString(11));
        map.put("attachmentName", rs.getString(12));
        map.put("vDriverName", rs.getString(13));
        map.put("insuranceDate", rs.getString(14));
      }
      
    }catch(Exception ex){
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return map;
  }
  public ArrayList<Map<String,String>> selectVehicle(Connection dbConn,String useingFlag) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    ArrayList<Map<String,String>> vehicleList = new ArrayList<Map<String,String>>();
    try{
      String sql = "select v.SEQ_ID,v.V_DATE,v.V_MODEL,v.V_DRIVER,v.V_NUM,v.V_STATUS,v.V_REMARK,v.V_ENGINE_NUM,ci.CLASS_DESC,p.USER_NAME"
                   +" from VEHICLE v left outer join OA_KIND_DICT_ITEM ci on v.V_TYPE = ci.SEQ_ID left outer join oa_kind_dict cc on "
                   +" ci.CLASS_NO = cc.CLASS_NO left outer join PERSON p on v.V_DRIVER = p.SEQ_ID where 1=1 ";
      if(!YHUtility.isNullorEmpty(useingFlag)){
        sql = sql + " and USEING_FLAG = '" + useingFlag + "'";
      }
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map = new HashMap<String,String>();
        map.put("seqId", rs.getString(1));
        map.put("vDate", rs.getString(2));
        map.put("vModel", rs.getString(3));
        map.put("vDriver", rs.getString(4)); 
        map.put("vNum", rs.getString(5));
        map.put("vStatus", rs.getString(6));
        map.put("vRemark", rs.getString(7));
        map.put("vEngineNum", rs.getString(8));
        map.put("classDesc", rs.getString(9));
        map.put("vDriverName", rs.getString(10)); 
        vehicleList.add(map);
      }
      
    }catch(Exception ex){
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return vehicleList;
  }
  /**自动使用和自动回收
   * 修改VU_STATUS-syl
   * @param conn
   * @param tableName
   * @return
   * @throws Exception
   */
  public static void updateVStatus(Connection conn,int seqId,String vStatus) throws Exception{
    String sql = "update vehicle set USEING_FLAG=? where seq_id=?";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, vStatus);
      ps.setInt(2,seqId);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, null, log);
    }
  }
  /**
   * 更新数据库中的文件

   * @param dbConn
   * @param attachmentId
   * @param attachmentName
   * @param seqId
   * @throws Exception
   */
  public void updateFile(Connection dbConn,String attachmentId,String attachmentName,String seqId) throws Exception {
    YHORM orm = new YHORM();
    PreparedStatement pstmt = null;
    ResultSet rs = null; 
    String budgetMoneyTotal = "0";
    try {
      String sql = "update VEHICLE set ATTACH_ID = ? ,ATTACH_NAME = ? where SEQ_ID=?"   ;
      pstmt = dbConn.prepareStatement(sql);
      pstmt.setString(1, attachmentId);
      pstmt.setString(2,attachmentName);
      pstmt.setString(3, seqId);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
  /**
   * 更新数据库中的文件

   * @param dbConn
   * @param attachmentId
   * @param attachmentName
   * @param seqId
   * @throws Exception
   */
  public void updateFile(Connection dbConn,String attachmentId,String attachmentName,String carUser,String history,String seqId) throws Exception {
    YHORM orm = new YHORM();
    PreparedStatement pstmt = null;
    ResultSet rs = null; 
    String budgetMoneyTotal = "0";
    try {
      String sql = "update VEHICLE set ATTACH_ID = ? ,ATTACH_NAME = ? ,CAR_USER = ?, HISTORY =? where SEQ_ID=?"   ;
      pstmt = dbConn.prepareStatement(sql);
      pstmt.setString(1, attachmentId);
      pstmt.setString(2,attachmentName);
      pstmt.setString(3, carUser);
      pstmt.setString(4, history);
      pstmt.setString(5, seqId);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
  
  public void stopInsurance(Connection dbConn, String seqId){
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String sql = " update vehicle set insurance_flag = 1 where seq_id = "+ seqId;
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
  }
}

package yh.subsys.oa.vehicle.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.vehicle.data.YHVehicleOperator;

public class YHVehicleOperatorLogic {
  private static Logger log = Logger.getLogger(YHVehicleOperatorLogic.class);
  public void addOperator(Connection dbConn, YHVehicleOperator vcOperator) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, vcOperator);  
  }
  public void updateOperator(Connection dbConn, YHVehicleOperator vcOperator) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, vcOperator) ;
  }
  public ArrayList<YHVehicleOperator> selectOperator(Connection dbConn, Map map) throws Exception{
    YHORM orm = new YHORM();
    ArrayList<YHVehicleOperator> operatorList = (ArrayList<YHVehicleOperator>)orm.loadListSingle(dbConn, YHVehicleOperator.class, map);
    return operatorList;
  }

  public void updateOperator(Connection dbConn ,String operatorId,String opertorName) throws Exception{
    Statement stmt = null;
    String sql = "update oa_vehicle_driver set OPERATOR_ID = '" + operatorId + "'";
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /*
   * 根据id字符串得到name字符串


   */
  public  List<YHPerson>  getPersonByIds(Connection dbConn,Map map)throws Exception{
    YHPersonLogic tpl = new YHPersonLogic();
    String ids = getOperatorIds(dbConn);
    List<YHPerson> personList = new ArrayList<YHPerson>();
    YHORM orm = new YHORM();
    if(!ids.equals("")){
      String[] str = {"SEQ_ID in (" + ids + ")"};
      personList =orm.loadListSingle(dbConn, YHPerson.class, str );
    }
    return personList;
  }

  public String getOperatorIds(Connection dbConn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String ids = "";
    String sql = "select OPERATOR_ID from oa_vehicle_driver ";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        if(!YHUtility.isNullorEmpty(rs.getString(1))){
          ids =rs.getString(1);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
    return ids;
  }
  /**
  * 调度人员ID串-lz
  * 
  * */
  public static String selectPerson(Connection dbConn) throws Exception{
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String seqIdStr = "";//符合条件的ID串
    String nameStr = "";//名字串
    String seqId = selectId(dbConn);
//    and us.user_state=1
    if(YHUtility.isNullorEmpty(seqId)){
      seqId = "0";
    }
    String sql = "select us.user_id as userId from oa_online us where us.user_id in (" + seqId + ") GROUP by us.user_id ";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()){
        seqIdStr += rs.getString("userId") + ",";
      }
      if (!YHUtility.isNullorEmpty(seqIdStr)) {
        seqIdStr = seqIdStr.substring(0,seqIdStr.length() - 1);
      }
      nameStr = getName(dbConn,seqIdStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
    return nameStr;
  } 
  /**
   * 调度人员ID串-lz
   * 
   * */
  public static String getName(Connection dbConn,String seqId) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String sql = "select user_name from  person where seq_id in (" + seqId + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String name = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        name += rs.getString("user_name") + ",";
      }
      if (name.length() > 0) {
        name = name.substring(0,name.length()-1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return name;
  }

  /**
   * 调度人员ID串-lz
   * 
   * */
  public static String selectId(Connection dbConn) throws Exception{
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String seqIdStr = "";
    String sql = "select operator_id from oa_vehicle_driver";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if (rs.next()){
        seqIdStr = rs.getString("operator_id");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
    return seqIdStr;
  }
}

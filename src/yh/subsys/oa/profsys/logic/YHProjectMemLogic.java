package yh.subsys.oa.profsys.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.profsys.data.YHProjectMem;

public class YHProjectMemLogic {
  private static Logger log = Logger.getLogger(YHProjectMemLogic.class);
  public int addMem(Connection dbConn, YHProjectMem mem) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, mem);
    return YHCalendarLogic.getMaSeqId(dbConn, "oa_project_member");
  }
  public void updateMem(Connection dbConn,  YHProjectMem mem) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, mem);
    //return YHCalendarLogic.getMaSeqId(dbConn, "oa_project");
  }
  public YHProjectMem getMemById(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHProjectMem mem = (YHProjectMem) orm.loadObjSingle(dbConn, YHProjectMem.class, Integer.parseInt(seqId));
    return mem;
  }
  public void deleteMemById(Connection dbConn,  String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHProjectMem.class, Integer.parseInt(seqId));
  }
  /***
   * 根据条件查询数据的projId
   * @return
   * @throws Exception 
   */
  public static String queryMemToProjId(Connection dbConn,String memNum,String memPosition,String memName,String memSex,String memBirth,String memIdNum,String projMemType) throws Exception {
    String sql = "select PROJ_ID from oa_project_member where PROJ_MEM_TYPE ='" + projMemType + "'";
    if (!YHUtility.isNullorEmpty(memNum)) {
      sql += " and MEM_NUM like '%" + YHUtility.encodeLike(memNum) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(memPosition)) {
      sql += " and " + YHDBUtility.findInSet(memPosition, "MEM_POSITION");
    }
    if (!YHUtility.isNullorEmpty(memName)) {
      sql += " and MEM_NAME like '%" + YHUtility.encodeLike(memName) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(memIdNum)) {
      sql += " and MEM_ID_NUM like '%" + YHUtility.encodeLike(memIdNum) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(memSex)) {
      sql += " and MEM_SEX='" + memSex + "'";
    }
    if (!YHUtility.isNullorEmpty(memBirth)) {
      String str =  YHDBUtility.getDateFilter("MEM_BIRTH", memBirth, "=");
      sql += " and " + str;
    }
    sql = sql + " group by PROJ_ID";
    PreparedStatement ps = null;
    ResultSet rs = null;
    ps = dbConn.prepareStatement(sql);
    rs = ps.executeQuery();
    String projId = "";
    while (rs.next()) {
      if(!YHUtility.isNullorEmpty(rs.getString("PROJ_ID"))){
        projId += rs.getString("PROJ_ID") + ",";
      }
      
    }
    if (!YHUtility.isNullorEmpty(projId)) {
      projId = projId.substring(0,projId.length() - 1);
    }
    return projId;
  }

}

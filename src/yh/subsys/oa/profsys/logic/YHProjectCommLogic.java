package yh.subsys.oa.profsys.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProjectComm;

public class YHProjectCommLogic {
  /**
   * 分页列表
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public static String toSearchData(Connection dbConn,Map request,String projId,String projCommType) throws Exception{
    String sql = "select SEQ_ID,COMM_NUM,COMM_NAME" 
      + ",COMM_MEM_CN,COMM_MEM_FN,COMM_TIME,COMM_PLACE,ATTACHMENT_ID,ATTACHMENT_NAME"
      + ",COMM_CONTENT,COMM_NOTE,PROJ_COMM_TYPE,PROJ_DATE,PROJ_ID from oa_project_comm "
      + " where PROJ_ID = " + projId  + " and PROJ_COMM_TYPE = '"+projCommType+"'" ;
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   * 查询会议纪要
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public static YHProjectComm getCommById(Connection dbConn,String seqId) throws Exception{
    YHORM orm = new YHORM();
    YHProjectComm comm = (YHProjectComm)orm.loadObjSingle(dbConn,YHProjectComm.class,Integer.parseInt(seqId));
    return comm;
  }
  /***
   * 根据条件查询数据的projId
   * @return
   * @throws Exception 
   */
  public static String queryCommToProjId(Connection dbConn,String commNum,String commMemCn,String commMemFn,String commName,String commTime,String commPlace,String projCommType) throws Exception {
    String sql = "select PROJ_ID FROM oa_project_comm WHERE PROJ_COMM_TYPE='" + projCommType + "' ";

    if (!YHUtility.isNullorEmpty(commNum)) {
      sql += " and COMM_NUM like '%" +  YHUtility.encodeLike(commNum) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(commMemCn)) {
      sql += " and COMM_MEM_CN like '%" +  YHUtility.encodeLike(commMemCn) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(commMemFn)) {
      sql += " and COMM_MEM_FN like '%" +  YHUtility.encodeLike(commMemFn) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(commName)) {
      sql += " and COMM_NAME like '%" +  YHUtility.encodeLike(commName) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(commPlace)) {
      sql += " and COMM_PLACE like '%" +  YHUtility.encodeLike(commPlace) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(commTime)) {
      String str =  YHDBUtility.getDateFilter("COMM_TIME", commTime, "=");
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

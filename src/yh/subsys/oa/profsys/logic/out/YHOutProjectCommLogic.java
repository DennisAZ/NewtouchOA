package yh.subsys.oa.profsys.logic.out;
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
public class YHOutProjectCommLogic {
  /**
   *添加数据 
   * @param dbConn
   * @throws Exception 
   * @throws Exception
   */
  public static void addProjectComm(Connection dbConn,YHProjectComm comm) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn,comm);
  }
  /**
   *修改数据 
   * @param dbConn
   * @throws Exception 
   * @throws Exception
   */
  public static void updateProjectComm(Connection dbConn,YHProjectComm comm) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn,comm);
  }
  /**
   * 分页列表
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public static String toSearchData(Connection dbConn,Map request,String projId) throws Exception{
    String sql = "select SEQ_ID,COMM_NUM,COMM_NAME" 
      + ",COMM_MEM_CN,COMM_MEM_FN,COMM_TIME,COMM_PLACE,ATTACHMENT_ID,ATTACHMENT_NAME"
      + ",COMM_CONTENT,COMM_NOTE,PROJ_COMM_TYPE,PROJ_DATE,PROJ_ID from oa_project_comm "
      + " where PROJ_ID = " + projId  + " and PROJ_COMM_TYPE = '1'" ;
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   * 删除会议纪要
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public static void deleteCommById(Connection dbConn,String seqId) throws Exception{
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn,YHProjectComm.class,Integer.parseInt(seqId));
  }
  /**
   *删除
   * @return
   * @throws Exception
   */
  public static void delComm(Connection dbConn,int projId) throws Exception {
    String sql = "delete from oa_project_comm where proj_id=? ";
    PreparedStatement ps = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1,projId);
      ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps,null,null);
    }
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
  public static String profsysSelectComm(Connection dbConn,YHProjectComm comm) throws Exception {
    String sql = "select PROJ_ID "
      + " FROM oa_project_comm WHERE PROJ_COMM_TYPE='" + comm.getProjCommType() + "' ";

    if (!YHUtility.isNullorEmpty(comm.getCommNum())) {
      sql += " and COMM_NUM like '%" +  YHUtility.encodeLike(comm.getCommNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommMemCn())) {
      sql += " and COMM_MEM_CN like '%" +  YHUtility.encodeLike(comm.getCommMemCn()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommMemFn())) {
      sql += " and COMM_MEM_FN like '%" +  YHUtility.encodeLike(comm.getCommMemFn()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommName())) {
      sql += " and COMM_NAME like '%" +  YHUtility.encodeLike(comm.getCommName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommPlace())) {
      sql += " and COMM_PLACE like '%" +  YHUtility.encodeLike(comm.getCommPlace()) + "%' " + YHDBUtility.escapeLike();
    }
    if (comm.getCommTime() != null) {
      String str =  YHDBUtility.getDateFilter("COMM_TIME", YHUtility.getDateTimeStr(comm.getCommTime()), "=");
      sql += " and " + str;
    }
    sql += " group by PROJ_ID ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String projId = "";
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();  
      while (rs.next()) {
        projId += rs.getString("PROJ_ID") + ",";
      }
      if (!YHUtility.isNullorEmpty(projId)) {
        projId = projId.substring(0,projId.length() - 1);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps,rs,null);
    }
    return projId;
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String profsysCommList(Connection dbConn,Map request,String projType,YHProjectComm comm) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,b.BUDGET_ITEM,de.DEPT_NAME,code.class_desc,son.user_name,code2.class_desc" 
      + ",p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS,p.PROJ_TYPE"
      + ",p.PROJ_STATUS,p.PROJ_VISIT_TYPE,p.PROJ_LEADER,p.PROJ_ACTIVE_TYPE FROM project p"
      + " left outer join oa_ration_apply b on p.BUDGET_ID=b.SEQ_ID"
      + " left outer join oa_department de on de.seq_id =p.DEPT_ID"
      + " left outer join oa_kind_dict_item code on code.seq_id =p.PROJ_VISIT_TYPE"
      + " left outer join person son on son.seq_id =p.PROJ_LEADER"
      + " left outer join oa_kind_dict_item code2 on code2.seq_id=p.PROJ_ACTIVE_TYPE,oa_project_comm pcom "
      + " where p.PROJ_TYPE='" + projType + "' and pcom.PROJ_ID = p.SEQ_ID"
      + " and pcom.PROJ_COMM_TYPE='" + comm.getProjCommType() + "' ";

    if (!YHUtility.isNullorEmpty(comm.getCommNum())) {
      sql += " and pcom.COMM_NUM like '%" +  YHUtility.encodeLike(comm.getCommNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommMemCn())) {
      sql += " and pcom.COMM_MEM_CN like '%" +  YHUtility.encodeLike(comm.getCommMemCn()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommMemFn())) {
      sql += " and pcom.COMM_MEM_FN like '%" +  YHUtility.encodeLike(comm.getCommMemFn()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommName())) {
      sql += " and pcom.COMM_NAME like '%" +  YHUtility.encodeLike(comm.getCommName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(comm.getCommPlace())) {
      sql += " and pcom.COMM_PLACE like '%" +  YHUtility.encodeLike(comm.getCommPlace()) + "%' " + YHDBUtility.escapeLike();
    }
    if (comm.getCommTime() != null) {
      String str =  YHDBUtility.getDateFilter("pcom.COMM_TIME", YHUtility.getDateTimeStr(comm.getCommTime()), "=");
      sql += " and " + str;
    }
    //p.PROJ_STATUS <> '1' and  
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
}

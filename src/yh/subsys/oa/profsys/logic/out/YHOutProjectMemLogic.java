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
import yh.subsys.oa.profsys.data.YHProjectMem;

public class YHOutProjectMemLogic {
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public static String toSearchData(Connection conn,Map request,String projId) throws Exception{
    String sql = "select pm.SEQ_ID,pm.MEM_NUM,pm.MEM_POSITION,pm.MEM_NAME,pm.MEM_SEX,pm.MEM_BIRTH,pm.MEM_ID_NUM,"
      + "pm.MEM_PHONE,pm.MEM_MAIL,pm.MEM_FAX,pm.MEM_ADDRESS,pm.ATTACHMENT_ID,pm.ATTACHMENT_NAME"
      +" from oa_project_member pm "
      +" where pm.PROJ_ID = " + projId  + " and pm.PROJ_MEM_TYPE = '1'" ;
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }

  /***
   * 根据条件查询数据的projId
   * @return
   * @throws Exception 
   */
  public static String profsysSelectMem(Connection dbConn,YHProjectMem mem) throws Exception {
    String sql = "select PROJ_ID "
      + " from oa_project_member where PROJ_MEM_TYPE ='" + mem.getProjMemType() + "'";
    if (!YHUtility.isNullorEmpty(mem.getMemNum())) {
      sql += " and MEM_NUM like '%" + YHUtility.encodeLike(mem.getMemNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(mem.getMemPosition())) {
      sql += " and " + YHDBUtility.findInSet(mem.getMemPosition(), "MEM_POSITION");
      //sql += " and MEM_POSITION in (" + mem.getMemPosition()+ ") ";
    }
    if (!YHUtility.isNullorEmpty(mem.getMemName())) {
      sql += " and MEM_NAME like '%" + YHUtility.encodeLike(mem.getMemName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(mem.getMemIdNum())) {
      sql += " and MEM_ID_NUM like '%" + YHUtility.encodeLike(mem.getMemIdNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(mem.getMemSex())) {
      sql += " and MEM_SEX='" + mem.getMemSex() + "'";
    }
    if (mem.getMemBirth() != null) {
      String str =  YHDBUtility.getDateFilter("MEM_BIRTH", YHUtility.getDateTimeStr(mem.getMemBirth()), "=");
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
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return projId;
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String profsysMemList(Connection dbConn,Map request,String projType,YHProjectMem mem) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,b.BUDGET_ITEM,de.DEPT_NAME,code.class_desc,son.user_name,code2.class_desc" 
      + ",p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS,p.PROJ_TYPE"
      + ",p.PROJ_STATUS,p.PROJ_VISIT_TYPE,p.PROJ_LEADER,p.PROJ_ACTIVE_TYPE FROM project p"
      + " left outer join oa_ration_apply b on p.BUDGET_ID=b.SEQ_ID"
      + " left outer join oa_department de on de.seq_id =p.DEPT_ID"
      + " left outer join oa_kind_dict_item code on code.seq_id =p.PROJ_VISIT_TYPE"
      + " left outer join person son on son.seq_id =p.PROJ_LEADER"
      + " left outer join oa_kind_dict_item code2 on code2.seq_id=p.PROJ_ACTIVE_TYPE,oa_project_member pmem "
      + " where  p.PROJ_TYPE='" + projType + "' and pmem.PROJ_ID=p.SEQ_ID "
      + " and pmem.PROJ_MEM_TYPE ='" + mem.getProjMemType() + "'";
    if (!YHUtility.isNullorEmpty(mem.getMemNum())) {
      sql += " and pmem.MEM_NUM like '%" + YHUtility.encodeLike(mem.getMemNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(mem.getMemPosition())) {
      sql += " and " + YHDBUtility.findInSet(mem.getMemPosition(), "pmem.MEM_POSITION");
      //sql += " and MEM_POSITION in (" + mem.getMemPosition()+ ") ";
    }
    if (!YHUtility.isNullorEmpty(mem.getMemName())) {
      sql += " and pmem.MEM_NAME like '%" + YHUtility.encodeLike(mem.getMemName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(mem.getMemIdNum())) {
      sql += " and pmem.MEM_ID_NUM like '%" + YHUtility.encodeLike(mem.getMemIdNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(mem.getMemSex())) {
      sql += " and pmem.MEM_SEX='" + mem.getMemSex() + "'";
    }
    if (mem.getMemBirth() != null) {
      String str =  YHDBUtility.getDateFilter("pmem.MEM_BIRTH", YHUtility.getDateTimeStr(mem.getMemBirth()), "=");
      sql += " and " + str;
    }
    //p.PROJ_STATUS <> '1' and 
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /**
   *添加数据 
   * @param dbConn
   * @throws Exception 
   * @throws Exception
   */
  public static void addProjectMem(Connection dbConn,YHProjectMem mem) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, mem);
  }

  /**
   * 修改项目
   * 
   * @return
   * @throws Exception
   */
  public static void updateProjectMem(Connection dbConn,YHProjectMem mem) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn,mem);
  }

  //seqId串转换成NAME串
  public static String userName(Connection dbConn,String seqId) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String sql = "select priv_name from user_priv where seq_id in (" + seqId + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String name = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        name += rs.getString("priv_name") + ",";
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
   *删除
   * @return
   * @throws Exception
   */
  public static void delMem(Connection dbConn,int projId) throws Exception {
    String sql = "delete from oa_project_member where proj_id=? ";
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
}

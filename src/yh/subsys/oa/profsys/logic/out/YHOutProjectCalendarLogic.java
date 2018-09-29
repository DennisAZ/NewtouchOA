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
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProjectCalendar;

public class YHOutProjectCalendarLogic {
  /***
   * 根据条件查询数据的projId
   * @return
   * @throws Exception 
   */
  public static String profsysSelectCalendar(Connection dbConn,YHProjectCalendar calendar,String start,String start1,String end,String end1) throws Exception {
    String sql = "select PROJ_ID"
      + " from oa_project_calendar"
      + " where PROJ_CALENDAR_TYPE='" + calendar.getProjCalendarType() + "'";
    if (!YHUtility.isNullorEmpty(calendar.getActiveType())) {
      sql += " and ACTIVE_TYPE='" + calendar.getActiveType() + "'";
    }
    if (!YHUtility.isNullorEmpty(calendar.getActiveContent())) {
      sql += " and ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(calendar.getActiveContent()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(calendar.getActiveLeader())) {
      sql += " and ACTIVE_LEADER ='" +  calendar.getActiveLeader() + "'";
    }
    if (!YHUtility.isNullorEmpty(calendar.getActivePartner())) {
      sql += " and ACTIVE_PARTNER like '%" +  YHUtility.encodeLike(calendar.getActivePartner()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(start)) {
      String str =  YHDBUtility.getDateFilter("START_TIME",start, ">=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(start1)) {
      String str =  YHDBUtility.getDateFilter("START_TIME", start1, "<=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(end)) {
      String str =  YHDBUtility.getDateFilter("END_TIME",end, ">=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(end1)) {
      String str =  YHDBUtility.getDateFilter("END_TIME",end1, "<=");
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
  public static String profsysCalendarList(Connection dbConn,Map request,String projType,YHProjectCalendar calendar,String start,String start1,String end,String end1) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,b.BUDGET_ITEM,de.DEPT_NAME,code.class_desc,son.user_name,code2.class_desc" 
      + ",p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS,p.PROJ_TYPE"
      + ",p.PROJ_STATUS,p.PROJ_VISIT_TYPE,p.PROJ_LEADER,p.PROJ_ACTIVE_TYPE FROM project p"
      + " left outer join oa_ration_apply b on p.BUDGET_ID = b.SEQ_ID"
      + " left outer join oa_department de on de.seq_id = p.DEPT_ID"
      + " left outer join oa_kind_dict_item code on code.seq_id = p.PROJ_VISIT_TYPE"
      + " left outer join person son on son.seq_id = p.PROJ_LEADER"
      + " left outer join oa_kind_dict_item code2 on code2.seq_id = p.PROJ_ACTIVE_TYPE "
      + " ,oa_project_calendar pca "
      + " where p.PROJ_TYPE='" + projType + "' and pca.PROJ_ID = p.SEQ_ID and "
      + " pca.PROJ_CALENDAR_TYPE='" + calendar.getProjCalendarType() + "'";
    if (!YHUtility.isNullorEmpty(calendar.getActiveType())) {
      sql += " and pca.ACTIVE_TYPE='" + calendar.getActiveType() + "'";
    }
    if (!YHUtility.isNullorEmpty(calendar.getActiveContent())) {
      sql += " and pca.ACTIVE_CONTENT like '%" +  YHUtility.encodeLike(calendar.getActiveContent()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(calendar.getActiveLeader())) {
      sql += " and pca.ACTIVE_LEADER ='" +  calendar.getActiveLeader() + "'";
    }
    if (!YHUtility.isNullorEmpty(calendar.getActivePartner())) {
      sql += " and pca.ACTIVE_PARTNER like '%" +  YHUtility.encodeLike(calendar.getActivePartner()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(start)) {
      String str =  YHDBUtility.getDateFilter("pca.START_TIME",start, ">=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(start1)) {
      String str =  YHDBUtility.getDateFilter("pca.START_TIME", start1, "<=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(end)) {
      String str =  YHDBUtility.getDateFilter("pca.END_TIME",end, ">=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(end1)) {
      String str =  YHDBUtility.getDateFilter("pca.END_TIME",end1, "<=");
      sql += " and " + str;
    }
    //System.out.println(sql);
    // p.PROJ_STATUS <> '1' and 
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   *删除
   * @return
   * @throws Exception
   */
  public static void delCalendar(Connection dbConn,int projId) throws Exception {
    String sql = "delete from oa_project_calendar where proj_id=? ";
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

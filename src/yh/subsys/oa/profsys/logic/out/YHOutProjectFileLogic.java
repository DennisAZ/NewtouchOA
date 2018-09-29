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
import yh.subsys.oa.profsys.data.YHProjectFile;

public class YHOutProjectFileLogic {
  /**
   *添加数据 
   * @param dbConn
   * @throws Exception 
   * @throws Exception
   */
  public static void addProjectFile(Connection dbConn,YHProjectFile file) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn,file);
  }
  /**
   *查询数据 
   * @param dbConn
   * @throws Exception 
   * @throws Exception
   */
  public static YHProjectFile getFileById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHProjectFile file = (YHProjectFile)orm.loadObjSingle(dbConn, YHProjectFile.class,Integer.parseInt(seqId));
    return file;
  }

  /**
   *查询数据 
   * @param dbConn
   * @throws Exception 
   * @throws Exception
   */
  public static void updateProjectFile(Connection dbConn,YHProjectFile file) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, file);
  }
  /**
   * 分页列表
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public static String toSearchData(Connection dbConn,Map request,String projId) throws Exception{
    String sql = "select p.SEQ_ID,p.FILE_NUM,p.FILE_NAME"
      + ",p.FILE_TYPE,son.USER_NAME,p.FILE_TITLE,p.ATTACHMENT_ID,p.ATTACHMENT_NAME,p.FILE_CONTENT"
      + ",p.FILE_NOTE,p.PROJ_FILE_TYPE,p.PROJ_CREATOR,p.PROJ_DATE,p.PROJ_ID from oa_project_attach p "
      + " left outer join person son on son.seq_id=p.PROJ_CREATOR"
      + " where PROJ_ID = " + projId  + " and PROJ_FILE_TYPE = '1'" ;
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /**
   *删除数据 
   * @param dbConn
   * @throws Exception 
   * @throws Exception
   */
  public static void deleteFileById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHProjectFile.class,Integer.parseInt(seqId));
  }
  /**
   *删除
   * @return
   * @throws Exception
   */
  public static void delFile(Connection dbConn,int projId) throws Exception {
    String sql = "delete from oa_project_attach where proj_id=? ";
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
  /***
   * 根据条件查询数据的projId
   * @return
   * @throws Exception 
   */
  public static String profsysSelectFile(Connection dbConn,YHProjectFile file) throws Exception {
    String sql = "select PROJ_ID "
      + " FROM oa_project_attach WHERE PROJ_FILE_TYPE='" + file.getProjFileType()+ "' ";

    if (!YHUtility.isNullorEmpty(file.getFileNum())) {
      sql += " and FILE_NUM like '%" +  YHUtility.encodeLike(file.getFileNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(file.getFileName())) {
      sql += " and FILE_NAME like '%" +  YHUtility.encodeLike(file.getFileName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(file.getFileType())) {
      sql += " and FILE_TYPE like '%" +  YHUtility.encodeLike(file.getFileType()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(file.getProjCreator())) {
      sql += " and PROJ_CREATOR ='" + file.getProjCreator() + "'";
    }
    if (!YHUtility.isNullorEmpty(file.getFileTitle())) {
      sql += " and FILE_TITLE like '%" +  YHUtility.encodeLike(file.getFileTitle()) + "%' " + YHDBUtility.escapeLike();
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
  public static String profsysFileList(Connection dbConn,Map request,String projType,YHProjectFile file) throws Exception {
    String sql = "select p.SEQ_ID,p.PROJ_NUM,b.BUDGET_ITEM,de.DEPT_NAME,code.class_desc,son.user_name,code2.class_desc" 
      + ",p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS,p.PROJ_TYPE"
      + ",p.PROJ_STATUS,p.PROJ_VISIT_TYPE,p.PROJ_LEADER,p.PROJ_ACTIVE_TYPE FROM project p"
      + " left outer join oa_ration_apply b on p.BUDGET_ID=b.SEQ_ID"
      + " left outer join oa_department de on de.seq_id =p.DEPT_ID"
      + " left outer join oa_kind_dict_item code on code.seq_id =p.PROJ_VISIT_TYPE"
      + " left outer join person son on son.seq_id =p.PROJ_LEADER"
      + " left outer join oa_kind_dict_item code2 on code2.seq_id=p.PROJ_ACTIVE_TYPE,oa_project_attach pfi"
      + " where  p.PROJ_TYPE='" + projType + "' and pfi.PROJ_ID = p.SEQ_ID "
      + " and pfi.PROJ_FILE_TYPE='" + file.getProjFileType()+ "' ";
    if (!YHUtility.isNullorEmpty(file.getFileNum())) {
      sql += " and pfi.FILE_NUM like '%" +  YHUtility.encodeLike(file.getFileNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(file.getFileName())) {
      sql += " and pfi.FILE_NAME like '%" +  YHUtility.encodeLike(file.getFileName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(file.getFileType())) {
      sql += " and pfi.FILE_TYPE like '%" +  YHUtility.encodeLike(file.getFileType()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(file.getProjCreator())) {
      sql += " and pfi.PROJ_CREATOR ='" + file.getProjCreator() + "'";
    }
    if (!YHUtility.isNullorEmpty(file.getFileTitle())) {
      sql += " and pfi.FILE_TITLE like '%" +  YHUtility.encodeLike(file.getFileTitle()) + "%' " + YHDBUtility.escapeLike();
    }
    //p.PROJ_STATUS <> '1' and 
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
}

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
import yh.core.util.form.YHFOM;

public class YHProjectFileLogic {
 
  /**
   * 分页列表
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public static String toSearchData(Connection dbConn,Map request,String projId,String projFileType) throws Exception{
    String sql = "select SEQ_ID,FILE_NUM,FILE_NAME"
      + ",FILE_TYPE,FILE_CREATOR,FILE_TITLE,ATTACHMENT_ID,ATTACHMENT_NAME,FILE_CONTENT"
      + ",FILE_NOTE,PROJ_FILE_TYPE,PROJ_CREATOR,PROJ_DATE,PROJ_ID from oa_project_attach "
      + " where PROJ_ID = " + projId ;
    if(projFileType.equals("0")){
      sql = sql + " and (PROJ_FILE_TYPE = '0' or PROJ_FILE_TYPE is null)";
    }else{
      sql = sql + " and PROJ_FILE_TYPE = '" + projFileType + "'";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据的projId
   * @return
   * @throws Exception 
   */
  public static String queryCommToProjId(Connection dbConn,String fileNum,String fileName,String fileType,String projCreator,String fileTitle,String projFileType) throws Exception {
    String sql = "select PROJ_ID from oa_project_attach WHERE PROJ_FILE_TYPE='" + projFileType+ "' ";

    if (!YHUtility.isNullorEmpty(fileNum)) {
      sql += " and FILE_NUM like '%" +  YHUtility.encodeLike(fileNum) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(fileName)) {
      sql += " and FILE_NAME like '%" +  YHUtility.encodeLike(fileName) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(fileType)) {
      sql += " and FILE_TYPE like '%" +  YHUtility.encodeLike(fileType) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(projCreator)) {
      sql += " and PROJ_CREATOR = '" + projCreator + "'";
    }
    if (!YHUtility.isNullorEmpty(fileTitle)) {
      sql += " and FILE_TITLE like '%" +  YHUtility.encodeLike(fileTitle) + "%' " + YHDBUtility.escapeLike();
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

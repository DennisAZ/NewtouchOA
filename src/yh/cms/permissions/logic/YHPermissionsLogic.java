package yh.cms.permissions.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import yh.cms.column.data.YHCmsColumn;
import yh.cms.station.data.YHCmsStation;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHPermissionsLogic {

  /**
   * 获取站点权限
   * 
   * @param dbConn
   * @param userType
   * @param seqId
   * @return json
   */
  public String getPermissions(Connection dbConn, String userType, String seqId) throws Exception {
    
    StringBuffer sb = new StringBuffer();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "select " + userType + " from oa_cms_sites where seq_id="+seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if (rs.next()) {
        String userTypeTemp = rs.getString(userType);
        if(YHUtility.isNullorEmpty(userTypeTemp)) {
          sb.append("{\"deptPer\":\"\",\"deptPerDesc\":\"\",\"privPer\":\"\",\"privPerDesc\":\"\",\"userPer\":\"\",\"userPerDesc\":\"\"}");
          return sb.toString();
        }
        String[] permissions = {"","",""};
        String permissionsTemp[] = userTypeTemp.split("\\|");
        for(int i = 0; i<permissionsTemp.length; i++){
          permissions[i] = permissionsTemp[i];
        }
        sb.append("{\"deptPer\":\""+ permissions[0] +"\""
                + ",\"deptPerDesc\":\""+ queryDeptname(dbConn, permissions[0]) +"\""
        		    + ",\"privPer\":\""+ permissions[1] +"\""
        		    + ",\"privPerDesc\":\""+ queryPrivname(dbConn, permissions[1]) +"\""
        		    +	",\"userPer\":\""+ permissions[2] +"\""
        		    + ",\"userPerDesc\":\""+ queryUsername(dbConn, permissions[2]) +"\"}");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if(YHUtility.isNullorEmpty(sb.toString())){
      sb.append("{\"deptPer\":\"\",\"deptPerDesc\":\"\",\"privPer\":\"\",\"privPerDesc\":\"\",\"userPer\":\"\",\"userPerDesc\":\"\"}");
    }
    return sb.toString();
  }
  
  /**
   * 获取部门名称
   * 
   * @param dbConn
   * @param seqIds
   * @return String
   */
  public String queryDeptname(Connection dbConn, String seqIds) throws Exception {
    if(YHUtility.isNullorEmpty(seqIds)){
      return "";
    }
    StringBuffer sb = new StringBuffer("");
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "select dept_name from oa_department where seq_id in ("+ seqIds +")";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        String deptName = rs.getString("dept_name");
        sb.append(deptName+",");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if(sb.length() > 1){
      sb = sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  /**
   * 获取角色名称
   * 
   * @param dbConn
   * @param seqIds
   * @return String
   */
  public String queryPrivname(Connection dbConn, String seqIds) throws Exception {
    if(YHUtility.isNullorEmpty(seqIds)){
      return "";
    }
    StringBuffer sb = new StringBuffer("");
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "select priv_name from user_priv where seq_id in ("+ seqIds +")";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        String privName = rs.getString("priv_name");
        sb.append(privName+",");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if(sb.length() > 1){
      sb = sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  /**
   * 获取人员名称
   * 
   * @param dbConn
   * @param seqIds
   * @return String
   */
  public String queryUsername(Connection dbConn, String seqIds) throws Exception {
    if(YHUtility.isNullorEmpty(seqIds)){
      return "";
    }
    StringBuffer sb = new StringBuffer("");
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "select user_name from person where seq_id in ("+ seqIds +")";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        String userName = rs.getString("user_name");
        sb.append(userName+",");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if(sb.length() > 1){
      sb = sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  
  /**
   * 设置站点权限
   * 
   * @param dbConn
   * @param userType
   * @param seqId
   * @param stringInfo
   * @return 
   */
  public void setPermissions(Connection dbConn, String userType, String seqId, String stringInfo) throws Exception {
    
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = " update oa_cms_sites set "+ userType +"='"+stringInfo+"' where seq_id="+seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  
  /**
   * 设置栏目权限--批量设置某一站点下所有栏目
   * 
   * @param dbConn
   * @param userType
   * @param seqId
   * @param stringInfo
   * @return 
   */
  public void setPermissionsChild(Connection dbConn, String userType, String seqId, String stringInfo) throws Exception {
    
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = " update oa_cms_col set "+ userType +"='"+stringInfo+"' where station_id="+seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  
  /**
   * 获取栏目权限
   * 
   * @param dbConn
   * @param userType
   * @param seqId
   * @return String
   */
  public String getPermissionsColumn(Connection dbConn, String userType, String seqId) throws Exception {
	    
    StringBuffer sb = new StringBuffer();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "select " + userType + " from oa_cms_col where seq_id="+seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if (rs.next()) {
        String userTypeTemp = rs.getString(userType);
        if(YHUtility.isNullorEmpty(userTypeTemp)) {
          sb.append("{\"deptPer\":\"\",\"deptPerDesc\":\"\",\"privPer\":\"\",\"privPerDesc\":\"\",\"userPer\":\"\",\"userPerDesc\":\"\"}");
          return sb.toString();
        }
        String[] permissions = {"","",""};
        String permissionsTemp[] = userTypeTemp.split("\\|");
        for(int i = 0; i<permissionsTemp.length; i++){
          permissions[i] = permissionsTemp[i];
        }
        sb.append("{\"deptPer\":\""+ permissions[0] +"\""
                + ",\"deptPerDesc\":\""+ queryDeptname(dbConn, permissions[0]) +"\""
        		    + ",\"privPer\":\""+ permissions[1] +"\""
        		    + ",\"privPerDesc\":\""+ queryPrivname(dbConn, permissions[1]) +"\""
        		    +	",\"userPer\":\""+ permissions[2] +"\""
        		    + ",\"userPerDesc\":\""+ queryUsername(dbConn, permissions[2]) +"\"}");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if(YHUtility.isNullorEmpty(sb.toString())){
      sb.append("{\"deptPer\":\"\",\"deptPerDesc\":\"\",\"privPer\":\"\",\"privPerDesc\":\"\",\"userPer\":\"\",\"userPerDesc\":\"\"}");
    }
    return sb.toString();
  }
  
  /**
   * 设置栏目权限
   * 
   * @param dbConn
   * @param userType
   * @param seqId
   * @param stringInfo
   * @return 
   */
  public void setPermissionsColumn(Connection dbConn, String userType, String seqId, String stringInfo) throws Exception {
	    
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = " update oa_cms_col set "+ userType +"='"+stringInfo+"' where seq_id="+seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  
  /**
   * 设置栏目权限--批量设置某栏目下所有栏目权限
   * 
   * @param dbConn
   * @param userType
   * @param seqId
   * @param stringInfo
   * @return 
   */
  public void setPermissionsChildColumn(Connection dbConn, String userType, String seqId, String stringInfo) throws Exception {
    String seqIds = getChild(dbConn , seqId);
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = " update oa_cms_col set "+ userType +"='"+stringInfo+"' where seq_id in (0"+seqIds+")";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  
  /**
   * 获取某栏目下所有子栏目
   * 
   * @param dbConn
   * @param seqId
   * @return String
   */
  public String getChild(Connection dbConn, String seqId) throws Exception{
	
  	StringBuffer sb = new StringBuffer();
  	YHORM orm = new YHORM();
  	String filters[] = {" PARENT_ID =" + seqId};
  	List<YHCmsColumn> columnList = orm.loadListSingle(dbConn, YHCmsColumn.class, filters);
  	if(columnList == null){
  	  return sb.toString();
  	}
  	else{
  	  for(YHCmsColumn column : columnList){
    		sb.append("," + column.getSeqId());
    		sb.append(getChild(dbConn , column.getSeqId()+""));
  	  }
  	}
	  return sb.toString();
  }
  
  /**
   * CMS 获取站点树
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String getStationTree(Connection dbConn, YHPerson person) throws Exception {
    
    YHORM orm = new YHORM();
    StringBuffer sb = new StringBuffer("[");
    boolean isHave = false;
    String filters[] = {" 1=1 order by SEQ_ID asc "};
    List<YHCmsStation> stations = orm.loadListSingle(dbConn, YHCmsStation.class, filters);
    if (stations != null && stations.size() > 0) {
      for (YHCmsStation station : stations) {
        
        int dbSeqId = station.getSeqId();
        String stationName = YHUtility.null2Empty(station.getStationName());
        boolean flag = this.isHaveChild(dbConn, dbSeqId);
        if (flag) {
          sb.append("{");
          sb.append("nodeId:\"" + dbSeqId + ",station\"");
          sb.append(",name:\"" + YHUtility.encodeSpecial(stationName) + "\"");
          sb.append(",isHaveChild:" + 1 + "");
          sb.append(",extData:\"" + "" + "\"");
          sb.append("},");
          isHave = true;
        } else {
          sb.append("{");
          sb.append("nodeId:\"" + dbSeqId + ",station\"");
          sb.append(",name:\"" + YHUtility.encodeSpecial(stationName) + "\"");
          sb.append(",isHaveChild:" + 0 + "");
          sb.append(",extData:\"" + "" + "\"");
          sb.append("},");
          isHave = true;
        }
      }
      if (isHave) {
        sb = sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
    } else {
      sb.append("]");
    }
    return sb.toString();
  }
  
  /**
   * 站点是否存在子节点
   * 
   */
  public boolean isHaveChild(Connection dbConn, int dbSeqId){
    
    String sql = " select 1 from oa_cms_col where STATION_ID ="+dbSeqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return false;
  }
  
  /**
   * 获取栏目树

   * 
   * 
   * @param dbConn
   * @param id
   * @return
   * @throws Exception
   */
  public String getColumnTree(Connection dbConn, String id, String type, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    StringBuffer sb = new StringBuffer("[");
    boolean isHave = false;
    try {
      int seqId = 0;
      if (YHUtility.isNumber(id)) {
        seqId = Integer.parseInt(id);
      }
      String filters[];
      if("station".equals(type)){
        String filtersTemp[] = {" STATION_ID =" + seqId + " and PARENT_ID =" + 0 + " order by COLUMN_INDEX desc "};
        filters = filtersTemp;
      }
      else{
        String filtersTemp[] = {" PARENT_ID =" + seqId + " order by COLUMN_INDEX desc "};
        filters = filtersTemp;
      }
      List<YHCmsColumn> columns = orm.loadListSingle(dbConn, YHCmsColumn.class, filters);
      if (columns != null && columns.size() > 0) {
        for (YHCmsColumn column : columns) {
          int dbSeqId = column.getSeqId();
          String columnName = YHUtility.null2Empty(column.getColumnName());
          boolean counter = isHaveChildColumn(dbConn, dbSeqId);
          if (counter) {
            sb.append("{");
            sb.append("nodeId:\"" + dbSeqId + ",column\"");
            sb.append(",name:\"" + YHUtility.encodeSpecial(columnName) + "\"");
            sb.append(",isHaveChild:" + 1 + "");
            sb.append(",extData:\"" + "" + "\"");
            sb.append("},");
            isHave = true;
          } else {
            sb.append("{");
            sb.append("nodeId:\"" + dbSeqId + ",column\"");
            sb.append(",name:\"" + YHUtility.encodeSpecial(columnName) + "\"");
            sb.append(",isHaveChild:" + 0 + "");
            sb.append(",extData:\"" + "" + "\"");
            sb.append("},");
            isHave = true;
          }
        }
        if (isHave) {
          sb = sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
      } else {
        sb.append("]");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return sb.toString();
  }
  
  /**
   * 栏目是否存在子节点
   * 
   */
  public boolean isHaveChildColumn(Connection dbConn, int dbSeqId){
    
    String sql = " select 1 from oa_cms_col where PARENT_ID ="+dbSeqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return false;
  }
  
  /**
   * 判断列表访问权限
   * 
   * @param YHPageDataList
   * @param person
   * @return YHPageDataList
   */
  public YHPageDataList visitControl(YHPageDataList pageDataList, YHPerson person) {
    YHPageDataList pageDataListRe = new YHPageDataList();
    int total = pageDataList.getTotalRecord();
    int newTotal = total;
    for(int i = 0; i < pageDataList.getRecordCnt() ;i++){
      YHDbRecord record = pageDataList.getRecord(i);
      String visitUser = (String)record.getValueByName("visitUser");
      if(YHUtility.isNullorEmpty(visitUser)){
        newTotal--;
        continue;
      }
      else{
        String[] permissions = {"","",""};
        String[] permissionsTemp = visitUser.split("\\|");
        for(int j = 0; j<permissionsTemp.length; j++){
          permissions[j] = permissionsTemp[j];
        }
        String visitDept = permissions[0] == null ? "" : permissions[0];
        visitDept = "," + visitDept + ",";
        String visitPriv = permissions[1] == null ? "" : permissions[1];
        visitPriv = "," + visitPriv + ",";
        String visitPerson = permissions[2] == null ? "" : permissions[2];
        visitPerson = "," + visitPerson + ",";
        
        if(visitDept.contains((","+person.getDeptId()+",")) || ",0,".equals(visitDept)){
          pageDataListRe.addRecord(record);
        }
        else if(visitPriv.contains((","+person.getUserPriv()+","))){
          pageDataListRe.addRecord(record);
        }
        else if(visitPerson.contains((","+person.getSeqId()+","))){
          pageDataListRe.addRecord(record);
        }
        else{
          newTotal--;
          continue;
        }
      }
    }
    pageDataListRe.setTotalRecord(newTotal);
    return pageDataListRe;
  }
  
  /**
   * 判断访问权限
   * 
   * @param YHPageDataList
   * @param person
   * @return YHPageDataList
   */
  public boolean isPermissions(Connection dbConn, String userPermissions, YHPerson person){
    
    String[] permissions = {"","",""};
    String[] permissionsTemp = userPermissions.split("\\|");
    for(int j = 0; j < permissionsTemp.length; j++){
      permissions[j] = permissionsTemp[j];
    }
    String permissionsDept = permissions[0] == null ? "" : permissions[0];
    permissionsDept = "," + permissionsDept + ",";
    String permissionsPriv = permissions[1] == null ? "" : permissions[1];
    permissionsPriv = "," + permissionsPriv + ",";
    String permissionsPerson = permissions[2] == null ? "" : permissions[2];
    permissionsPerson = "," + permissionsPerson + ",";
    
    if(permissionsDept.contains((","+person.getDeptId()+",")) || ",0,".equals(permissionsDept)){
      return true;
    }
    else if(permissionsPriv.contains((","+person.getUserPriv()+","))){
      return true;
    }
    else if(permissionsPerson.contains((","+person.getSeqId()+","))){
      return true;
    }
    return false;
  }
  
  
}

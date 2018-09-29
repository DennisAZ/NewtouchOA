package yh.cms.column.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.cms.column.data.YHCmsColumn;
import yh.cms.common.logic.YHCmsCommonLogic;
import yh.cms.content.data.YHCmsContent;
import yh.cms.content.logic.YHContentLogic;
import yh.cms.permissions.logic.YHPermissionsLogic;
import yh.cms.station.data.YHCmsStation;
import yh.cms.station.logic.YHStationLogic;
import yh.cms.template.data.YHCmsTemplate;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.cms.velocity.YHvelocityUtil;

public class YHColumnLogic {
  public static final String attachmentFolder = "cms";

  /**
   * CMS 文章模板获取
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String getTemplateArticle(Connection dbConn, String stationId) throws Exception {
    try {
      StringBuffer data = new StringBuffer("[");
      String sql = " select c1.SEQ_ID, c1.TEMPLATE_NAME "
                 + " from oa_cms_template c1 "
                 + " where c1.template_type = 2 "
                 + " and c1.station_id =" + stationId
                 + " ORDER BY c1.SEQ_ID asc ";
      PreparedStatement ps = null;
      ResultSet rs = null;
      boolean flag = false;
      try {
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
          data.append("{seqId:"+rs.getInt("SEQ_ID")+","+"templateName:\""+rs.getString("TEMPLATE_NAME")+"\"},");
          flag = true;
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        YHDBUtility.close(ps, rs, null);
      }
      if(flag){
        data = data.deleteCharAt(data.length() - 1);
      }
      data.append("]");
      return data.toString();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public StringBuffer getInfomation(Connection conn, int stationId, int parentId) throws Exception {
    try {
      YHORM orm = new YHORM();
      StringBuffer data = new StringBuffer();
      if(parentId == 0){
        YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, stationId);
        data = YHFOM.toJson(station);
      }
      else{
        YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, parentId);
        YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, column.getStationId());
        data = YHFOM.toJson(column);
        data = data.deleteCharAt(data.length()-1);
        data.append(",stationName:\""+station.getStationName()+"\"}");
      }
      return data;
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * CMS栏目 添加
   * 
   */
  public int addColumn(Connection dbConn, YHCmsColumn column, YHPerson person) throws Exception{
    
    int columnIndex = 1;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "  SELECT max(COLUMN_INDEX) COLUMN_INDEX FROM oa_cms_col c WHERE c.STATION_ID ="+column.getStationId()+" and c.PARENT_ID ="+column.getParentId();
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        columnIndex = rs.getInt("COLUMN_INDEX") + 1;
      }
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    
    YHORM orm = new YHORM();
    column.setCreateId(person.getSeqId());
    column.setCreateTime(YHUtility.parseTimeStamp());
    column.setColumnIndex(columnIndex);
    
    //初始化权限
    column.setVisitUser("0||");
    column.setEditUser("0||");
    column.setNewUser("0||");
    column.setDelUser("0||");
    column.setRelUser("0||");
    column.setEditUserContent("0||");
    column.setApprovalUserContent("0||");
    column.setReleaseUserContent("0||");
    column.setRecevieUserContent("0||");
    column.setOrderContent("0||");
    
    orm.saveSingle(dbConn,column);
    
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(dbConn, YHCmsStation.class, column.getStationId());
    String parent = getParentPath(dbConn, column);
    //System.out.println(parent);
    File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath());
    file.mkdir();
    
    int seqId = this.getMaSeqId(dbConn, "oa_cms_col");
    return seqId;
  }
  
  /**
   * 递归获取目录结构
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String getParentPath(Connection dbConn, YHCmsColumn column) throws Exception{
    String parentPath = "";
    if(column.getParentId() > 0){
      YHORM orm = new YHORM();
      YHCmsColumn parentColumn = (YHCmsColumn) orm.loadObjSingle(dbConn, YHCmsColumn.class, column.getParentId());
      if(parentColumn == null){
        return "";
      }
      parentPath = parentColumn.getColumnPath() + File.separator;
      parentPath = getParentPath(dbConn, parentColumn) + parentPath;
    }
    return parentPath;
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
        
        //访问权限
        YHPermissionsLogic logic = new YHPermissionsLogic();
        if(logic.isPermissions(dbConn, station.getVisitUser(), person)){
          int dbSeqId = station.getSeqId();
          String stationName = YHUtility.null2Empty(station.getStationName());
          boolean flag = this.isHaveChild(dbConn, dbSeqId);
          if (flag) {
            sb.append("{");
            sb.append("nodeId:\"" + dbSeqId + ",station\"");
            sb.append(",name:\"" + YHUtility.encodeSpecial(stationName) + "\"");
            sb.append(",isHaveChild:" + 1 + "");
            sb.append(",extData:{visitUser:\""+station.getVisitUser()+"\",editUser:\""+station.getEditUser()+"\",newUser:\""+station.getNewUser()+"\",delUser:\""+station.getDelUser()+"\",relUser:\""+station.getRelUser()+"\"}");
            sb.append("},");
            isHave = true;
          } else {
            sb.append("{");
            sb.append("nodeId:\"" + dbSeqId + ",station\"");
            sb.append(",name:\"" + YHUtility.encodeSpecial(stationName) + "\"");
            sb.append(",isHaveChild:" + 0 + "");
            sb.append(",extData:{visitUser:\""+station.getVisitUser()+"\",editUser:\""+station.getEditUser()+"\",newUser:\""+station.getNewUser()+"\",delUser:\""+station.getDelUser()+"\",relUser:\""+station.getRelUser()+"\"}");
            sb.append("},");
            isHave = true;
          }
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
          
          //访问权限
          YHPermissionsLogic logic = new YHPermissionsLogic();
          if(logic.isPermissions(dbConn, column.getVisitUser(), person)){
            int dbSeqId = column.getSeqId();
            String columnName = YHUtility.null2Empty(column.getColumnName());
            boolean counter = isHaveChildColumn(dbConn, dbSeqId);
            if (counter) {
              sb.append("{");
              sb.append("nodeId:\"" + dbSeqId + ",column\"");
              sb.append(",name:\"" + YHUtility.encodeSpecial(columnName) + "\"");
              sb.append(",isHaveChild:" + 1 + "");
              sb.append(",extData:{" +
              		"visitUser:\""+column.getVisitUser()+"\""
              	+ ",editUser:\""+column.getEditUser()+"\""
              	+	",newUser:\""+column.getNewUser()+"\""
              	+ ",delUser:\""+column.getDelUser()+"\""
              	+ ",relUser:\""+column.getRelUser()+"\""
              	+ ",editUserContent:\""+column.getEditUserContent()+"\""
              	+ ",approvalUserContent:\""+column.getApprovalUserContent()+"\""
              	+ ",releaseUserContent:\""+column.getReleaseUserContent()+"\""
              	+ ",recevieUserContent:\""+column.getRecevieUserContent()+"\""
              	+ ",orderContent:\""+column.getOrderContent()+"\""
              	+ "}");
              sb.append("},");
              isHave = true;
            } else {
              sb.append("{");
              sb.append("nodeId:\"" + dbSeqId + ",column\"");
              sb.append(",name:\"" + YHUtility.encodeSpecial(columnName) + "\"");
              sb.append(",isHaveChild:" + 0 + "");
              sb.append(",extData:{" +
                  "visitUser:\""+column.getVisitUser()+"\""
                + ",editUser:\""+column.getEditUser()+"\""
                + ",newUser:\""+column.getNewUser()+"\""
                + ",delUser:\""+column.getDelUser()+"\""
                + ",relUser:\""+column.getRelUser()+"\""
                + ",editUserContent:\""+column.getEditUserContent()+"\""
                + ",approvalUserContent:\""+column.getApprovalUserContent()+"\""
                + ",releaseUserContent:\""+column.getReleaseUserContent()+"\""
                + ",recevieUserContent:\""+column.getRecevieUserContent()+"\""
                + ",orderContent:\""+column.getOrderContent()+"\""
                + "}");
              sb.append("},");
              isHave = true;
            }
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
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public StringBuffer getColumnDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, seqId);
      StringBuffer data = YHFOM.toJson(column);
      return data;
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * CMS站点 修改
   * 
   */
  public void updateColumn(Connection dbConn, YHCmsColumn column) throws Exception{
    
    YHORM orm = new YHORM();
    YHCmsColumn columnOld = (YHCmsColumn) orm.loadObjSingle(dbConn, YHCmsColumn.class, column.getSeqId());
    orm.updateSingle(dbConn, column);
    
    String parent = getParentPath(dbConn, column);
    
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(dbConn, YHCmsStation.class, column.getStationId());
    File fileOld = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + columnOld.getColumnPath());
    File fileNew = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath());
    fileOld.renameTo(fileNew);
  }
  
  
  
  
  /**
   * 返回最大的SEQ_Id
   * 
   * @param dbConn
   * @param tableName
   * @return
   * @throws Exception
   */
  public int getMaSeqId(Connection dbConn, String tableName) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int maxSeqId = 0;
    String sql = "select max(SEQ_ID) as SEQ_ID from " + tableName;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        maxSeqId = rs.getInt("SEQ_ID");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return maxSeqId;
  }
  
  /**
   * CMS下级栏目 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getColumnList(Connection dbConn, Map request, YHPerson person, int seqId, String flag) throws Exception {
    
    String sql = "";
    if("1".equals(flag)){
      sql = " SELECT c.SEQ_ID, c.COLUMN_NAME, c.CREATE_TIME, c.COLUMN_INDEX, c.VISIT_USER, c.EDIT_USER, c.NEW_USER, c.DEL_USER, c.REL_USER FROM oa_cms_col c where c.STATION_ID =" + seqId + " and c.PARENT_ID = 0 ";
    }
    else{
      sql = " SELECT c.SEQ_ID, c.COLUMN_NAME, c.CREATE_TIME, c.COLUMN_INDEX, c.VISIT_USER, c.EDIT_USER, c.NEW_USER, c.DEL_USER, c.REL_USER FROM oa_cms_col c where c.PARENT_ID =" + seqId;
    }
    sql = sql + " ORDER BY c.COLUMN_INDEX desc ";
    try {
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      
      //判断列表访问权限
      YHPermissionsLogic pLogic = new YHPermissionsLogic();
      pageDataList = pLogic.visitControl(pageDataList, person);
      
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 删除栏目
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public int deleteColumn(Connection dbConn, String seqIdStr) throws Exception {
    int data = 0;
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(dbConn, YHCmsColumn.class, Integer.parseInt(seqId));
          
          //判断是否存在子栏目
          Map map = new HashMap();
          map.put("PARENT_ID", column.getSeqId());
          List<YHCmsColumn> list = orm.loadListSingle(dbConn, YHCmsColumn.class, map);
          if(list != null && list.size() > 0){
            data = 1;
            continue;
          }
          String parent = getParentPath(dbConn, column);
          YHCmsStation station = (YHCmsStation) orm.loadObjSingle(dbConn, YHCmsStation.class, column.getStationId());
          File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath());
          if(file.list() != null){
            deleteFiles(file, file.list());
          }
          file.delete();
          
          orm.deleteSingle(dbConn, column);
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return data;
  }
  
  /**
   * 删除该文件夹下所有文件
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteFiles(File file, String[] files){
    int len = files.length;
    for(int i = 0 ; i < len ; i ++){
      File fileDir = new File(file, files[i]);
//      System.out.println(fileDir.getAbsolutePath());
      if(fileDir.isFile() || fileDir.list() != null){
        fileDir.delete();
      }
      else{
        deleteFiles(fileDir, fileDir.list());
      }
    }
  }
  
  /**
   * 发布
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int toRelease(Connection conn, int seqId, boolean fullRelease) throws Exception {
    YHORM orm = new YHORM();
    YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, seqId);
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, column.getStationId());
    YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, column.getTemplateIndexId());
    try {
      if(template != null){
//        File templateFile = new File(YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + template.getAttachmentId() + "_" + template.getAttachmentName());
//        StringBuffer sb = new StringBuffer("");
        
        //获取出来的list 是有排序的
        String filters[] = {" COLUMN_ID =" + seqId + " and CONTENT_STATUS = 5 order by CONTENT_TOP desc, CONTENT_INDEX desc "};
        List<YHCmsContent> contentListTotal = orm.loadListSingle(conn, YHCmsContent.class, filters);
        
        //读取模板文件
        YHCmsCommonLogic commonLogic = new YHCmsCommonLogic();
//        sb = commonLogic.readFile(conn, templateFile, station, column, contentList);
        
        //获取栏目树形结构目录
        YHColumnLogic logic = new YHColumnLogic();
        String parent = logic.getParentPath(conn, column);
        
        //是否需要分页
        if(column.getPaging() == 1){
          int maxIndexPage = column.getMaxIndexPage();
          int pagingNumber = column.getPagingNumber(); 
          int total = contentListTotal.size();
          
          List<YHCmsContent> contentListTemp = new ArrayList<YHCmsContent>();
          for(int i = 0; i < maxIndexPage && total > 0; i++){
            contentListTemp.clear();
            for(int j = 0; j < pagingNumber && total > 0; j++){
              total--;
              contentListTemp.add(contentListTotal.get(i*pagingNumber + j));
            }
            
            //获取生成索引的文件名&扩展名，如个为空则用seqId代替
            String fileName = template.getTemplateFileName();
            if(i != 0){
              fileName = fileName + i;
            }
            
            //velocity拼map
            Map<String,Object> request = new HashMap<String,Object>();
            //文件输出路径的文件名
            request.put("fileName", fileName + "." + station.getExtendName());
            //当前位置
            request.put("location", commonLogic.getLocation(conn, column, "../") + " >" + column.getColumnName());
            //栏目及栏目url
            request.put("column", column);
            
            //获取站点所有信息
            station = commonLogic.getStationInfo(conn, station.getSeqId());
            request.put("station", station);
            
            //当前栏目下的文章，并设置文章url
            //获取出来的list 是有排序的
            for(YHCmsContent content : contentListTemp){
              String contentFileName = content.getContentFileName();
              if(YHUtility.isNullorEmpty(content.getContentFileName())){
                contentFileName = content.getSeqId()+"";
              }
              String path = commonLogic.getColumnPath(conn, content.getColumnId());
              contentFileName = "/yh/" + station.getStationPath() + "/" + path + "/" + contentFileName + "." + station.getExtendName();
              content.setUrl(contentFileName);
            }
            request.put("contentList", contentListTemp);
            
            //当前站点下的最新文章
            //获取出来的list 是有排序的
            YHCmsColumn columnNew = new YHCmsColumn();
            columnNew.setColumnName("最新更新");
            String filtersContentNew[] = {" STATION_ID =" + station.getSeqId() + " and CONTENT_STATUS = 5 order by CONTENT_DATE desc "};
            List<YHCmsContent> contentListNew = orm.loadListSingle(conn, YHCmsContent.class, filtersContentNew);
            for(YHCmsContent content : contentListNew){
              String contentFileName = content.getContentFileName();
              if(YHUtility.isNullorEmpty(content.getContentFileName())){
                contentFileName = content.getSeqId()+"";
              }
              String path = commonLogic.getColumnPath(conn, content.getColumnId());
              contentFileName = "/yh/" + station.getStationPath() + "/" + path + "/" + contentFileName + "." + station.getExtendName();
              content.setUrl(contentFileName);
            }
            columnNew.setContentList(contentListNew);   
            request.put("columnNew", columnNew);
            
            //当前栏目模板
            request.put("template", template);
            
            //当前栏目模板
            request.put("contentSize", contentListTotal.size());
            request.put("pageNow", i);
            
            //文件输出路径、模板名、模板路径
            String pageOutPath = YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() ;
            String indexTemplateName = template.getAttachmentId() + "_" + template.getAttachmentName();
            String pageTemlateUrl = YHSysProps.getAttachPath() + File.separator + attachmentFolder;
            
            YHvelocityUtil.velocity(request, pageOutPath, indexTemplateName, pageTemlateUrl);
            
            
            
            
//            sb = commonLogic.readFile(conn, templateFile, station, column, contentListTemp, i);
//            
//            //获取栏目树形结构目录
//            YHColumnLogic logic = new YHColumnLogic();
//            String parent = logic.getParentPath(conn, column);
//            
//
//            File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() + File.separator + fileName + "." + station.getExtendName());
//            if(!file.exists()){
//              file.createNewFile();
//            }
//            else{
//              file.delete();
//              file.createNewFile(); 
//            }
//            FileOutputStream out = new FileOutputStream(file);
//            out.write(sb.toString().getBytes("utf-8"));
//            out.close();
            
          }
        }
        else{
        
          //获取生成索引的文件名&扩展名，如个为空则用seqId代替
          String fileName = template.getTemplateFileName();
//          File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() + File.separator + fileName + "." + station.getExtendName());
//          if(!file.exists()){
//            file.createNewFile();
//          }
//          else{
//            file.delete();
//            file.createNewFile(); 
//          }
//          FileOutputStream out = new FileOutputStream(file);
//          out.write(sb.toString().getBytes("utf-8"));
//          out.close();
//        }
        
          
          //velocity拼map
          Map<String,Object> request = new HashMap<String,Object>();
          //文件输出路径的文件名
          request.put("fileName", fileName + "." + station.getExtendName());
          //当前位置
          request.put("location", commonLogic.getLocation(conn, column, "../") + " >" + column.getColumnName());
          //栏目及栏目url
          request.put("column", column);
          
          //获取站点所有信息
          station = commonLogic.getStationInfo(conn, station.getSeqId());
          request.put("station", station);
          
          //当前栏目下的文章，并设置文章url
          //获取出来的list 是有排序的
          String filtersContent[] = {" COLUMN_ID =" + seqId + " and CONTENT_STATUS = 5 order by CONTENT_TOP desc, CONTENT_INDEX desc "};
          List<YHCmsContent> contentList = orm.loadListSingle(conn, YHCmsContent.class, filtersContent);
          for(YHCmsContent content : contentList){
            String contentFileName = content.getContentFileName();
            if(YHUtility.isNullorEmpty(content.getContentFileName())){
              contentFileName = content.getSeqId()+"";
            }
            String path = commonLogic.getColumnPath(conn, content.getColumnId());
            contentFileName = "/yh/" + station.getStationPath() + "/" + path + "/" + contentFileName + "." + station.getExtendName();
            content.setUrl(contentFileName);
          }
          request.put("contentList", contentList);
          
          //当前站点下的最新文章
          //获取出来的list 是有排序的
          YHCmsColumn columnNew = new YHCmsColumn();
          columnNew.setColumnName("最新更新");
          String filtersContentNew[] = {" STATION_ID =" + station.getSeqId() + " and CONTENT_STATUS = 5 order by CONTENT_DATE desc "};
          List<YHCmsContent> contentListNew = orm.loadListSingle(conn, YHCmsContent.class, filtersContentNew);
          for(YHCmsContent content : contentListNew){
            String contentFileName = content.getContentFileName();
            if(YHUtility.isNullorEmpty(content.getContentFileName())){
              contentFileName = content.getSeqId()+"";
            }
            String path = commonLogic.getColumnPath(conn, content.getColumnId());
            contentFileName = "/yh/" + station.getStationPath() + "/" + path + "/" + contentFileName + "." + station.getExtendName();
            content.setUrl(contentFileName);
          }
          columnNew.setContentList(contentListNew);   
          request.put("columnNew", columnNew);
          
          //文件输出路径、模板名、模板路径
          String pageOutPath = YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() ;
          String indexTemplateName = template.getAttachmentId() + "_" + template.getAttachmentName();
          String pageTemlateUrl = YHSysProps.getAttachPath() + File.separator + attachmentFolder;
          
          YHvelocityUtil.velocity(request, pageOutPath, indexTemplateName, pageTemlateUrl);
        }
          
          
          
        //发布栏目所在的站点
        YHStationLogic logicStation = new YHStationLogic();
        logicStation.toRelease(conn, column.getStationId(), false);
        
        //发布栏目下的所有文章
        if(fullRelease){
          YHContentLogic logicContent = new YHContentLogic();
          for(YHCmsContent content : contentListTotal){
            logicContent.toRelease(conn, content.getSeqId(), false);
          }
        }
        
        return 1;
      }
    } catch (Exception ex) {
      throw ex;
    }
    return 0;
  }
  
  /**
   * 调序、排序
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void toSort(Connection conn, int seqId, int toSeqId, int flag) throws Exception {
    if((flag == 1 || flag == 2) && toSeqId != 0){
      int toColumnIndex = getColumnIndex(conn,toSeqId);
      int columnIndex = getColumnIndex(conn,seqId);
      updateColumnIndex(conn, seqId,toColumnIndex);
      updateColumnIndex(conn, toSeqId,columnIndex);
    }
    if(flag == 3 || flag == 4){
      PreparedStatement ps = null;
      String sql1 = "";
      YHORM orm = new YHORM();
      YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, seqId);
      int columnIndex = getMaxOrMinColumnIndex(conn, column.getStationId(), column.getParentId(), flag);
      if(flag == 3){
        columnIndex = columnIndex + 1;
        sql1 = " update oa_cms_col set COLUMN_INDEX ="+columnIndex+" where SEQ_ID ="+seqId;
      }
      else if(flag == 4){
        columnIndex = columnIndex - 1;
        sql1 = " update oa_cms_col set COLUMN_INDEX ="+columnIndex+" where SEQ_ID ="+seqId;
      }
      try {
        ps = conn.prepareStatement(sql1);
        ps.executeUpdate();
      } catch (Exception ex) {
        throw ex;
      }
      finally{
        YHDBUtility.close(ps, null, null);
      }
    }
  }
  
  /**
   * 获取排序索引 contentIndex
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int getColumnIndex(Connection conn,int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql1 = " select COLUMN_INDEX from oa_cms_col where SEQ_ID ="+seqId;
    try {
      ps = conn.prepareStatement(sql1);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getInt("COLUMN_INDEX");
      }
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    return 0;
  }
  
  /**
   * 更新排序索引
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int updateColumnIndex(Connection conn,int seqId,int columnIndex) throws Exception{
    PreparedStatement ps = null;
    String sql1 = " update oa_cms_col set COLUMN_INDEX ="+columnIndex+" where SEQ_ID ="+seqId;
    try {
      ps = conn.prepareStatement(sql1);
      ps.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    return 0;
  }
  
  /**
   * 更新排序索引
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int getMaxOrMinColumnIndex(Connection conn,int stationId, int parentId, int flag) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "";
    if(flag ==3){
      sql = " select MAX(COLUMN_INDEX) COLUMN_INDEX from oa_cms_col where STATION_ID ="+stationId+" and PARENT_ID ="+parentId;
    }
    else if(flag == 4){
      sql = " select MIN(COLUMN_INDEX) COLUMN_INDEX from oa_cms_col where STATION_ID ="+stationId+" and PARENT_ID ="+parentId;
    }
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getInt("COLUMN_INDEX");
      }
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    return 0;
  }
  
  /**
   * 验证路径是否存在
   * 
   * @param conn
   * @param stationId
   * @param parentId
   * @param seqId
   * @param columnPath
   * @return
   * @throws Exception
   */
  public int checkPath(Connection conn, int stationId, int parentId, int seqId, String columnPath) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = " SELECT 1 FROM oa_cms_col c where c.STATION_ID = " + stationId + " and c.PARENT_ID = " + parentId + " and COLUMN_PATH = '" + columnPath + "' and SEQ_ID != " + seqId;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return 1;
      }
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    return 0;
  }
  
  public String getPath(Connection conn, int seqId) throws Exception{
    YHORM orm = new YHORM();
    YHCmsContent content = (YHCmsContent) orm.loadObjSingle(conn, YHCmsContent.class, seqId);
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, content.getStationId());
    String contentFileName = content.getContentFileName();
    if(YHUtility.isNullorEmpty(content.getContentFileName())){
      contentFileName = content.getSeqId()+"";
    }
    YHCmsCommonLogic commonLogic = new YHCmsCommonLogic();
    String pathContent = commonLogic.getColumnPath(conn, content.getColumnId());
    
    return YHUtility.encodeSpecial("/yh/" + station.getStationPath() + "/" + pathContent + "/" + contentFileName + "." + station.getExtendName());
  }
}

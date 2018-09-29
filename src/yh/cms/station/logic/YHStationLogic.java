package yh.cms.station.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.cms.column.data.YHCmsColumn;
import yh.cms.column.logic.YHColumnLogic;
import yh.cms.common.logic.YHCmsCommonLogic;
import yh.cms.content.data.YHCmsContent;
import yh.cms.permissions.logic.YHPermissionsLogic;
import yh.cms.station.data.YHCmsStation;
import yh.cms.template.data.YHCmsTemplate;
import yh.cms.velocity.YHvelocityUtil;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHStationLogic {
  public static final String attachmentFolder = "cms";

  /**
   * CMS索引模板获取
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getTemplate(Connection dbConn, String stationId) throws Exception {
    try {
      StringBuffer data = new StringBuffer("[");
      String sql = " select c1.SEQ_ID, c1.TEMPLATE_NAME "
                 + " from oa_cms_template c1 "
                 + " where c1.template_type = 1 "
                 + " and station_id ="+ stationId
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
   * CMS站点 添加
   * 
   */
  public void addStation(Connection dbConn, YHCmsStation station, YHPerson person) throws Exception{
    
    YHORM orm = new YHORM();
    station.setCreateId(person.getSeqId());
    station.setCreateTime(YHUtility.parseTimeStamp());
    
    //初始化权限
    station.setVisitUser("0||");
    station.setEditUser("0||");
    station.setNewUser("0||");
    station.setDelUser("0||");
    station.setRelUser("0||");
    
    orm.saveSingle(dbConn,station);
    
    File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath());
    file.mkdir();
  }
  
  /**
   * CMS站点 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getStationList(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = " SELECT s.SEQ_ID, s.STATION_NAME, s.STATION_DOMAIN_NAME, s.TEMPLATE_ID, t.TEMPLATE_NAME, s.STATION_PATH, s.EXTEND_NAME, s.ARTICLE_EXTEND_NAME "
                 + " , s.VISIT_USER, s.EDIT_USER, s.NEW_USER, s.DEL_USER, s.REL_USER "
                 + " FROM oa_cms_sites s "
                 + " LEFT JOIN oa_cms_template t on s.TEMPLATE_ID = t.SEQ_ID "
                 + " ORDER BY s.SEQ_ID asc ";
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
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public StringBuffer getStationDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, seqId);
      StringBuffer data = YHFOM.toJson(station);
      return data;
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * CMS站点 修改
   * 
   */
  public void updateStation(Connection dbConn, YHCmsStation station) throws Exception{
    
    YHORM orm = new YHORM();
    YHCmsStation stationOld = (YHCmsStation) orm.loadObjSingle(dbConn, YHCmsStation.class, station.getSeqId());
    orm.updateSingle(dbConn, station);
    
    File fileOld = new File(YHSysProps.getWebPath() + File.separator + stationOld.getStationPath());
    File fileNew = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath());
    fileOld.renameTo(fileNew);
  }
  
  /**
   * 删除模板信息
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteStation(Connection dbConn, String seqIdStr) throws Exception {
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          YHCmsStation station = (YHCmsStation) orm.loadObjSingle(dbConn, YHCmsStation.class, Integer.parseInt(seqId));
          orm.deleteSingle(dbConn, station);
        }
      }
    } catch (Exception e) {
      throw e;
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
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, seqId);
    YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, station.getTemplateId());
    try {
      if(template != null){
//        File templateFile = new File(YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + template.getAttachmentId() + "_" + template.getAttachmentName());
//        StringBuffer sb = new StringBuffer("");
        
        //读取模板文件
        YHCmsCommonLogic commonLogic = new YHCmsCommonLogic();
//        sb = commonLogic.readFile(conn, templateFile, station);
        
        //获取生成索引的文件名&扩展名，站点文件名为模板文件名，此字段不能为空
        String fileName = template.getTemplateFileName();
//        File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + fileName + "." + station.getExtendName());
//        if(!file.exists()){
//          file.createNewFile();
//        }
//        else{
//          file.delete();
//          file.createNewFile(); 
//        }
//        FileOutputStream out = new FileOutputStream(file, true);
//        out.write(sb.toString().getBytes("UTF-8"));
//        out.close();
        
        
        
        //velocity拼map
        Map<String,Object> request = new HashMap<String,Object>();
        //文件输出路径的文件名
        request.put("fileName", fileName + "." + station.getExtendName());
        
        //获取站点所有信息
        station = commonLogic.getStationInfo(conn, seqId);
        request.put("station", station);
        
        //获取图片新闻
        String filtersContentImg[] = {" STATION_ID = "+ seqId +" and CONTENT_STATUS = 5 and  (ATTACHMENT_ID is not null and ATTACHMENT_ID != '') order by CONTENT_DATE desc "};
        List<YHCmsContent> contentImgList = orm.loadListSingle(conn, YHCmsContent.class, filtersContentImg);
        List<YHCmsContent> contentImgListReturn = new ArrayList<YHCmsContent>();
        int countImg = 0;
        for(YHCmsContent content : contentImgList){
          if(countImg >= 5){
            break;
          }
          String contentFileName = content.getContentFileName();
          if(YHUtility.isNullorEmpty(content.getContentFileName())){
            contentFileName = content.getSeqId()+"";
          }
          String pathContent = commonLogic.getColumnPath(conn, content.getColumnId());
          contentFileName = "/yh/" + station.getStationPath() + "/" + pathContent + "/" + contentFileName + "." + station.getExtendName();
          content.setUrl(contentFileName);
          
          String str[] = content.getAttachmentId().split(",")[0].split("_");
          String srcFile = YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + str[0] + File.separator + str[1] + "_" + content.getAttachmentName().split("\\*")[0];
          String destFile = YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + "images" + File.separator + "contentImages" + File.separator + str[1] + "." + content.getAttachmentName().split("\\*")[0].split("\\.")[1];
          String fileNameEnd = content.getAttachmentName().split("\\*")[0];
          if(fileNameEnd.endsWith(".jpg") || fileNameEnd.endsWith(".JPG")){
            YHFileUtility fileUtility = new YHFileUtility();
            fileUtility.deleteAll(destFile);
            fileUtility.copyFile(srcFile, destFile);
            content.setImageUrl(File.separator + "yh" + File.separator + station.getStationPath() + File.separator + "images" + File.separator + "contentImages" + File.separator + str[1] + "." + content.getAttachmentName().split("\\*")[0].split("\\.")[1]);
            countImg++;
            contentImgListReturn.add(content);
          }
        }
        request.put("contentImgList", contentImgListReturn);
        
        //文件输出路径、模板名、模板路径
        String pageOutPath = YHSysProps.getWebPath() + File.separator + station.getStationPath();
        String indexTemplateName = template.getAttachmentId() + "_" + template.getAttachmentName();
        String pageTemlateUrl = YHSysProps.getAttachPath() + File.separator + attachmentFolder;
        
        YHvelocityUtil.velocity(request, pageOutPath, indexTemplateName, pageTemlateUrl);
        
        //发布站点下的所有栏目
        if(fullRelease){
          String filtersAll[] = {" STATION_ID =" + seqId + " order by COLUMN_INDEX asc "};
          List<YHCmsColumn> columnListAll = orm.loadListSingle(conn, YHCmsColumn.class, filtersAll);
          YHColumnLogic logicColumn = new YHColumnLogic();
          for(YHCmsColumn column : columnListAll){
            logicColumn.toRelease(conn, column.getSeqId(), true);
          }
        }
        
        return 1;
      } 
    } catch (Exception ex) {
      throw ex;
    }
    return 0;
  }
  
  public int checkPath(Connection conn, int seqId, String stationPath) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = " SELECT 1 FROM oa_cms_sites c where c.station_path = '" + stationPath + "' and seq_id !="+seqId;
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
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, seqId);
    YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, station.getTemplateId());
    if(template == null){
      return "1";
    }
    return YHUtility.encodeSpecial("/yh/" + station.getStationPath() + "/" + template.getTemplateFileName() + "." + station.getExtendName());
  }
}

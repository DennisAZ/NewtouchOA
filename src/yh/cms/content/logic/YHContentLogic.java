package yh.cms.content.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.news.data.YHNews;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHContentLogic {
  public static final String attachmentFolder = "cms";

  /**
   * CMS栏目 添加
   * 
   */
  public void addContent(Connection dbConn, YHCmsContent content, YHPerson person) throws Exception{
    
    int contentIndex = 1;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "  SELECT max(CONTENT_INDEX) CONTENT_INDEX FROM oa_cms_contents c WHERE c.COLUMN_ID ="+content.getColumnId();
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        contentIndex = rs.getInt("CONTENT_INDEX") + 1;
      }
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    
    YHORM orm = new YHORM();
    content.setContentTop(0);
    content.setContentType(1);
    content.setContentStatus(0);//新建
    content.setCreateId(person.getSeqId());
    content.setCreateTime(YHUtility.parseTimeStamp());
    content.setContentIndex(contentIndex);
    orm.saveSingle(dbConn,content);
  }
  
  /**
   * CMS文章 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getContentList(Connection dbConn, Map request, YHPerson person, int stationId, int columnId) throws Exception {
    String whereStr = "";
    if(columnId != 0 && stationId!=0){
      whereStr = "c.STATION_ID ="+stationId+" and c.COLUMN_ID ="+columnId;
    }
    else 
    {
    	 whereStr = " c.COLUMN_ID ="+columnId;
	
    }
    try {
      String sql = " SELECT c.SEQ_ID, c.CONTENT_NAME, c.CREATE_ID, p.USER_NAME, c2.COLUMN_NAME, c.CONTENT_DATE, c.CONTENT_STATUS, c.CONTENT_INDEX, c.CONTENT_TOP "
                 + " , c2.VISIT_USER, c2.EDIT_USER_CONTENT, c2.APPROVAL_USER_CONTENT, c2.RELEASE_USER_CONTENT, c2.RECEVIE_USER_CONTENT, c2.ORDER_CONTENT "
                 + " FROM oa_cms_contents c "
                 + " left join person p on c.CREATE_ID = p.SEQ_ID "
                 + " left join oa_cms_col c2 on c.COLUMN_ID = c2.SEQ_ID where "
                 + whereStr
                 + " ORDER BY c.CONTENT_TOP desc, c.CONTENT_INDEX desc ";
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
  public StringBuffer getContentDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      YHCmsContent content = (YHCmsContent) orm.loadObjSingle(conn, YHCmsContent.class, seqId);
      YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, content.getColumnId());
      StringBuffer data = YHFOM.toJson(content);
      data = data.deleteCharAt(data.length()-1);
      data.append(",columnName:\""+column.getColumnName()+"\"}");
      return data;
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * CMS站点 修改
   * 
   */
  public void updateContent(Connection dbConn, YHCmsContent content) throws Exception{
    
    YHORM orm = new YHORM();
    content.setContentType(1);
    content.setContentStatus(1);//已编
    orm.updateSingle(dbConn, content);
  }
  
  /**
   * 删除内容
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteContent(Connection dbConn, String seqIdStr) throws Exception {
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          YHCmsContent content = (YHCmsContent) orm.loadObjSingle(dbConn, YHCmsContent.class, Integer.parseInt(seqId));
          orm.deleteSingle(dbConn, content);
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 签发
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void toIssued(Connection conn, int seqId) throws Exception {
    PreparedStatement ps = null;
    String sql = " update oa_cms_contents set CONTENT_STATUS = 3 where SEQ_ID ="+seqId;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  
  /**
   * 否定
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void toNo(Connection conn, int seqId) throws Exception {
    PreparedStatement ps = null;
    String sql = " update oa_cms_contents set CONTENT_STATUS = 2 where SEQ_ID ="+seqId;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, null, null);
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
    YHCmsContent content = (YHCmsContent) orm.loadObjSingle(conn, YHCmsContent.class, seqId);
    YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, content.getColumnId());
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, content.getStationId());
    YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, column.getTemplateArticleId());
    if(template != null){
//      File templateFile = new File(YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + template.getAttachmentId() + "_" + template.getAttachmentName());
      PreparedStatement ps = null;
//      StringBuffer sb = new StringBuffer("");
      try {
        
        //读取模板文件
        YHCmsCommonLogic commonLogic = new YHCmsCommonLogic();
//        sb = commonLogic.readFile(conn, templateFile, content);
        
        //获取栏目树形结构目录
        YHColumnLogic logic = new YHColumnLogic();
        String parent = logic.getParentPath(conn, column);
        
        //获取生成文章的文件名&扩展名，如果为空则用seqId代替
        String fileName = "";
        if(YHUtility.isNullorEmpty(content.getContentFileName().trim())){
          fileName = content.getSeqId()+"";
        }
        else{
          fileName = content.getContentFileName().trim();
        }
        
//        File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() + File.separator + fileName + "." + station.getArticleExtendName());
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
        request.put("fileName", fileName + "." + station.getArticleExtendName());
        //当前位置
        request.put("location", commonLogic.getLocation(conn, column, "../") + " >" + column.getColumnName());
        
        //获取站点所有信息
        station = commonLogic.getStationInfo(conn, station.getSeqId());
        request.put("station", station);
        
        //当前文章内容
        request.put("content", content);
        
        
        //文件输出路径、模板名、模板路径
        String pageOutPath = YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() ;
        String indexTemplateName = template.getAttachmentId() + "_" + template.getAttachmentName();
        String pageTemlateUrl = YHSysProps.getAttachPath() + File.separator + attachmentFolder;
        
        YHvelocityUtil.velocity(request, pageOutPath, indexTemplateName, pageTemlateUrl);
        
        //修改发布文章状态
        String sql = " update oa_cms_contents set CONTENT_STATUS = 5 where SEQ_ID ="+seqId;
        ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        
        //发布内容所在的栏目
        if(fullRelease){
          YHColumnLogic logicColumn = new YHColumnLogic();
          logicColumn.toRelease(conn, content.getColumnId(), false);
        }
        
        return 1;
      } catch (Exception ex) {
        throw ex;
      }
      finally{
        YHDBUtility.close(ps, null, null);
      }
    }
    return 0;
  }
  
  /**
   * 撤回
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void toReceive(Connection conn, int seqId) throws Exception {
    
    YHORM orm = new YHORM();
    YHCmsContent content = (YHCmsContent) orm.loadObjSingle(conn, YHCmsContent.class, seqId);
    YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, content.getColumnId());
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, content.getStationId());
    
    //获取栏目树形结构目录
    YHColumnLogic logic = new YHColumnLogic();
    String parent = logic.getParentPath(conn, column);
    
    //获取生成文章的文件名&扩展名，如个为空则用seqId代替
    String fileName = "";
    if(YHUtility.isNullorEmpty(content.getContentFileName().trim())){
      fileName = content.getSeqId()+"";
    }
    else{
      fileName = content.getContentFileName().trim();
    }
    File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() + File.separator + fileName + "." + station.getArticleExtendName());
    if(file.exists()){
      file.delete();
    }
    
    
    PreparedStatement ps = null;
    String sql = " update oa_cms_contents set CONTENT_STATUS = 4 where SEQ_ID ="+seqId;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
      
      //重新发布内容所在的栏目
      YHColumnLogic logicColumn = new YHColumnLogic();
      logicColumn.toRelease(conn, content.getColumnId(), false);
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  
  /**
   * 置顶 取消置顶
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void toTop(Connection conn, int seqId, int contentTop) throws Exception {
    String flag = "";
    if(contentTop == 0){
      flag = "1";
    }
    else{
      flag = "0";
    }
    PreparedStatement ps = null;
    String sql = " update oa_cms_contents set CONTENT_TOP = " + flag + " where SEQ_ID ="+seqId;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  
  /**
   * 调序、排序
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public void toSort(Connection conn, int seqId, int toSeqId, int flag, int columnId) throws Exception {
    if((flag == 1 || flag == 2) && toSeqId != 0){
      int toContentIndex = getContentIndex(conn,toSeqId);
      int contentIndex = getContentIndex(conn,seqId);
      updateContentIndex(conn, seqId,toContentIndex);
      updateContentIndex(conn, toSeqId,contentIndex);
    }
    if(flag == 3 || flag == 4){
      PreparedStatement ps = null;
      String sql1 = "";
      int contentIndex = getMaxOrMinContentIndex(conn, columnId, flag);
      if(flag == 3){
        contentIndex = contentIndex + 1;
        sql1 = " update oa_cms_contents set CONTENT_INDEX ="+contentIndex+" where SEQ_ID ="+seqId;
      }
      else if(flag == 4){
        contentIndex = contentIndex - 1;
        sql1 = " update oa_cms_contents set CONTENT_INDEX ="+contentIndex+" where SEQ_ID ="+seqId;
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
  public int getContentIndex(Connection conn,int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql1 = " select CONTENT_INDEX from oa_cms_contents where SEQ_ID ="+seqId;
    try {
      ps = conn.prepareStatement(sql1);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getInt("CONTENT_INDEX");
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
  public int updateContentIndex(Connection conn,int seqId,int contentIndex) throws Exception{
    PreparedStatement ps = null;
    String sql1 = " update oa_cms_contents set CONTENT_INDEX ="+contentIndex+" where SEQ_ID ="+seqId;
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
  public int getMaxOrMinContentIndex(Connection conn,int columnId,int flag) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "";
    if(flag ==3){
      sql = " select MAX(CONTENT_INDEX) CONTENT_INDEX from oa_cms_contents where COLUMN_ID ="+columnId;
    }
    else if(flag == 4){
      sql = " select MIN(CONTENT_INDEX) CONTENT_INDEX from oa_cms_contents where COLUMN_ID ="+columnId;
    }
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getInt("CONTENT_INDEX");
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
   * 根据YHCmsContent id 获取文章路径
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getUrls(Connection conn, int seqId) throws Exception {
    String url="error.jsp";
    YHORM orm = new YHORM();
    YHCmsContent content = (YHCmsContent) orm.loadObjSingle(conn, YHCmsContent.class, seqId);
    YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, content.getColumnId());
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, content.getStationId());
    //获取栏目树形结构目录
    YHColumnLogic logic = new YHColumnLogic();
    String parent = logic.getParentPath(conn, column);
    parent=parent.replace("\\", "/");
    //获取生成文章的文件名&扩展名，如个为空则用seqId代替
    String fileName = "";
    if(YHUtility.isNullorEmpty(content.getContentFileName().trim())){
      fileName = content.getSeqId()+"";
    }
    else{
      fileName = content.getContentFileName().trim();
    }
    File file = new File(YHSysProps.getWebPath() + File.separator + station.getStationPath() + File.separator + parent + column.getColumnPath() + File.separator + fileName + "." + station.getArticleExtendName());
    if(file.exists()){
      url="/yh/" + station.getStationPath() + "/" + parent + column.getColumnPath() + "/" + fileName + "." + station.getArticleExtendName();
    }
    return url;
  }
  
  
  //点击单文件上传时调用的方法
  /**
   * 处理上传附件，返回附件id，附件名称
  * @param request  HttpServletRequest
  * @param 
  * @return Map<String, String> ==> {id = 文件名}
  * @throws Exception 
  */
 public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm, String pathPx) throws Exception {
   Map<String, String> result = new HashMap<String, String>();
   String filePath = pathPx;
   try {
     Calendar cld = Calendar.getInstance();
     int year = cld.get(Calendar.YEAR) % 100;
     int month = cld.get(Calendar.MONTH) + 1;
     String mon = month >= 10 ? month + "" : "0" + month;
     String hard = year + mon;
     Iterator<String> iKeys = fileForm.iterateFileFields();
     while (iKeys.hasNext()) {
       String fieldName = iKeys.next();
       String fileName = fileForm.getFileName(fieldName).replaceAll("\\'", "");
       String fileNameV = fileName;
       //YHOut.println(fileName+"*************"+fileNameV);
       if (YHUtility.isNullorEmpty(fileName)) {
         continue;
       }
       String rand = YHDiaryUtil.getRondom();
       fileName = rand + "_" + fileName;
       
       while (YHDiaryUtil.getExist(filePath + File.separator + hard, fileName)) {
         rand = YHDiaryUtil.getRondom();
         fileName = rand + "_" + fileName;
       }
       result.put(hard + "_" + rand, fileNameV);
       fileForm.saveFile(fieldName, filePath + File.separator + attachmentFolder + File.separator + hard + File.separator + fileName);
     }
   } catch (Exception e) {
     throw e;
   }
   return result;
 }
 
 /**
  * 浮动菜单文件删除
  * 
  * @param dbConn
  * @param attId
  * @param attName
  * @param contentId
  * @throws Exception
  */
 public boolean delFloatFile(Connection dbConn, String attId, String attName, int seqId) throws Exception {
   boolean updateFlag = false;
   if (seqId != 0) {
     YHORM orm = new YHORM();
     YHNews news = (YHNews)orm.loadObjSingle(dbConn, YHNews.class, seqId);
     String[] attIdArray = {};
     String[] attNameArray = {};
     String attachmentId = news.getAttachmentId();
     String attachmentName = news.getAttachmentName();
     //YHOut.println("attachmentId"+attachmentId+"--------attachmentName"+attachmentName);
     if (!"".equals(attachmentId.trim()) && attachmentId != null && attachmentName != null) {
       attIdArray = attachmentId.trim().split(",");
       attNameArray = attachmentName.trim().split("\\*");
     }
     String attaId = "";
     String attaName = "";
 
     for (int i = 0; i < attIdArray.length; i++) {
       if (attId.equals(attIdArray[i])) {
         continue;
       }
       attaId += attIdArray[i] + ",";
       attaName += attNameArray[i] + "*";
     }
     //YHOut.println("attaId=="+attaId+"--------attaName=="+attaName);
     news.setAttachmentId(attaId.trim());
     news.setAttachmentName(attaName.trim());
     orm.updateSingle(dbConn, news);
   }
 //处理文件
   String[] tmp = attId.split("_");
   String path = YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator  + tmp[0] + File.separator + tmp[1] + "_" + attName;
   File file = new File(path);
   if(file.exists()){
     file.delete();
   } else {
     //兼容老的数据
     String path2 = YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator  + tmp[0] + File.separator + tmp[1] + "." + attName;
     File file2 = new File(path2);
     if(file2.exists()){
       file2.delete();
     }
   }
   updateFlag=true;
   return updateFlag;
 }
 
 /**暂时没用处理多文件上传

  * 附件批量上传页面处理
  * @return
 * @throws Exception 
  */
 public StringBuffer uploadMsrg2Json( YHFileUploadForm fileForm,String pathP) throws Exception{
   StringBuffer sb = new StringBuffer();
   Map<String, String> attr = null;
   String attachmentId = "";
   String attachmentName = "";
   try{    
     attr = fileUploadLogic(fileForm, pathP);
     Set<String> attrKeys = attr.keySet();
     for (String key : attrKeys){
       String fileName = attr.get(key);
       attachmentId += key + ",";
       attachmentName += fileName + "*";
     }
     long size = getSize(fileForm);
     sb.append("{");
     sb.append("'attachmentId':").append("\"").append(attachmentId).append("\",");
     sb.append("'attachmentName':").append("\"").append(attachmentName).append("\",");
     sb.append("'size':").append("").append(size);
     sb.append("}");
  } catch (Exception e){
    e.printStackTrace();
    throw e;
  }
   return sb;
 }
 
 public long getSize( YHFileUploadForm fileForm) throws Exception{
   long result = 0l;
   Iterator<String> iKeys = fileForm.iterateFileFields();
   while (iKeys.hasNext()) {
     String fieldName = iKeys.next();
     String fileName = fileForm.getFileName(fieldName);
     if (YHUtility.isNullorEmpty(fileName)) {
       continue;
     }
     result += fileForm.getFileSize(fieldName);
   }
   return result;
 }
}

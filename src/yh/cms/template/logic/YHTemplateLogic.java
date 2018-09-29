package yh.cms.template.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yh.cms.template.data.YHCmsTemplate;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHTemplateLogic {
  public static final String attachmentFolder = "cms";

  
  public Map<Object, Object> fileUploadLogic(YHFileUploadForm fileForm) throws Exception {
    
    Map<Object, Object> result = new HashMap<Object, Object>();
    Iterator<String> iKeys = fileForm.iterateFileFields();
    String attachmentId = "";
    String attachmentName = "";
    boolean attachFlag = false;
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      attachmentName = fileName;
      YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
      String rand = emul.getRandom();
      attachmentId = rand;
      fileForm.saveFile(fieldName, YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + rand + "_" + fileName);
      attachFlag = true;
    }
    result.put("attachFlag", attachFlag);
    result.put("attachmentId", attachmentId);
    result.put("attachmentName", attachmentName);
    return result;
  }
  
  /**
   * CMS模板 添加
   * 
   */
  public void addTemplate(Connection dbConn, YHCmsTemplate template, YHPerson person, YHFileUploadForm fileForm) throws Exception{
    
    Map<Object, Object> map = fileUploadLogic(fileForm);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentId = (String) map.get("attachmentId");
    String attachmentName = (String) map.get("attachmentName");
    
    YHORM orm = new YHORM();
    template.setCreateId(person.getSeqId());
    template.setCreateTime(YHUtility.parseTimeStamp());
    if (attachFlag) {
      template.setAttachmentId(attachmentId);
      template.setAttachmentName(attachmentName);
    }
    orm.saveSingle(dbConn,template);
  }
  
  /**
   * CMS模板 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getTemplateList(Connection dbConn, Map request, YHPerson person, String stationId) throws Exception {
    try {
      String sql = " select c1.SEQ_ID, c1.template_name, c1.template_file_name, c1.template_type, c1.template_type "
                 + " from oa_cms_template c1 "
                 + " where c1.station_id ="+stationId
                 + " ORDER BY c1.SEQ_ID asc ";
      if("0".equals(stationId)){
        sql = " select c1.SEQ_ID, c1.template_name, c1.template_file_name, c1.template_type, c1.template_type "
            + " from oa_cms_template c1 "
            + " ORDER BY c1.SEQ_ID asc ";
      }
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
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
  public StringBuffer getTemplateDetailLogic(Connection conn, int seqId, int flag) throws Exception {
    try {
      YHORM orm = new YHORM();
      YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, seqId);
      StringBuffer data = YHFOM.toJson(template);
      
      File templateFile = new File(YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + template.getAttachmentId() + "_" + template.getAttachmentName());
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile),"utf-8"));
      StringBuffer sb = new StringBuffer();
      String tempStr = "";
      while((tempStr = reader.readLine()) != null){
        sb.append(tempStr+"\n");
      }
      if(flag == 1){
        data.deleteCharAt(data.length()-1);
        data.append(",templateContent:\""+sb.toString().replace(" ", "&nbsp;&nbsp;").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;").replace("\\", "&#92;").replace("\"", "&#34;").replace("\'", "&#39;").replaceAll( "<", "&lt").replaceAll( ">", "&gt").replace("\n", "<br>")+"\"}");
      }
      else{
        data.deleteCharAt(data.length()-1);
        data.append(",templateContent:\""+sb.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\\\'").replace("\n", "\\n")+"\"}");
      }
      return data;
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * CMS模板 修改
   * 
   */
  public void updateTemplate(Connection dbConn, YHCmsTemplate template, YHFileUploadForm fileForm) throws Exception{
    
    Map<Object, Object> map = fileUploadLogic(fileForm);
    boolean attachFlag = (Boolean) map.get("attachFlag");
    String attachmentId = (String) map.get("attachmentId");
    String attachmentName = (String) map.get("attachmentName");
    
    YHORM orm = new YHORM();
    if (attachFlag) {
      template.setAttachmentId(attachmentId);
      template.setAttachmentName(attachmentName);
      deleteFile(dbConn, template.getSeqId());
    }
    else{
      YHCmsTemplate templateTemp = (YHCmsTemplate) orm.loadObjSingle(dbConn, YHCmsTemplate.class, template.getSeqId());
      File templateFile = new File(YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + templateTemp.getAttachmentId() + "_" + templateTemp.getAttachmentName());
      FileOutputStream out = new FileOutputStream(templateFile, false);
      out.write(fileForm.getParameter("templateContent").getBytes("UTF-8"));
      out.close();
    }
    orm.updateSingle(dbConn, template);
  }
  
  /**
   * 删除模板文件
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public boolean deleteFile(Connection dbConn, int seqId) throws Exception{
    
    YHORM orm = new YHORM();
    YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(dbConn, YHCmsTemplate.class, seqId);
    template.getAttachmentId();
    template.getAttachmentName();
    File file = new File(YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator +  template.getAttachmentId() + "_" + template.getAttachmentName());
    return file.delete();
  }
  
  /**
   * 删除模板信息
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteTemplateLogic(Connection dbConn, String seqIdStr) throws Exception {
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(dbConn, YHCmsTemplate.class, Integer.parseInt(seqId));
          deleteFile(dbConn, Integer.parseInt(seqId));
          orm.deleteSingle(dbConn, template);
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 员工关怀查询
   * 
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
  public String queryTemplateLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
    String templateName = (String) map.get("templateName");
    String templateFileName = (String) map.get("templateFileName");
    String templateType = (String) map.get("templateType");
    String conditionStr = "";
    String sql = "";
    try {
      if (!YHUtility.isNullorEmpty(templateType)) {
        conditionStr = " and c1.TEMPLATE_TYPE ='" + YHDBUtility.escapeLike(templateType) + "'";
      }
      if (!YHUtility.isNullorEmpty(templateName)) {
        conditionStr += " and c1.TEMPLATE_NAME like '%" + YHDBUtility.escapeLike(templateName) + "%'";
      }
      if (!YHUtility.isNullorEmpty(templateFileName)) {
        conditionStr += " and c1.TEMPLATE_FILE_NAME like '%" + YHDBUtility.escapeLike(templateFileName) + "%'";
      }
      sql = " select c1.SEQ_ID, c1.template_name, c1.template_file_name, c2.CLASS_DESC, c1.template_type "
          + " from oa_cms_template c1 "
          + " join oa_kind_dict_item c2 on c1.template_type = c2.seq_id "
          + " where 1=1" + conditionStr
          + " ORDER BY c1.SEQ_ID desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 查询下一级
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String getStationName(Connection dbConn) throws Exception {
    StringBuffer sb = new StringBuffer("[");
    Statement stmt = null;
    ResultSet rs = null;
    String sql = " SELECT SEQ_ID, STATION_NAME FROM oa_cms_sites order by SEQ_ID asc";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        sb.append("{\"seqId\":" + rs.getInt("SEQ_ID"));
        sb.append(",\"stationName\":\"" + rs.getString("STATION_NAME") + "\"},");
      }
      if(sb.length() > 3){
        sb = sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return sb.toString();
  }

}

package yh.subsys.jtgwjh.docReceive.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.esb.frontend.services.YHEsbService;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic;
import yh.subsys.jtgwjh.util.YHDocUtil;

public class YHJhDocrecvInfoLogic {
  private static Logger log = Logger
      .getLogger("yh.subsys.jtgwjh.docReceive.logic.YHJhDocrecvInfoLogic");

  /**
   * 新建
   * 
   * @param dbConn
   * @param code
   * @return
   * @throws Exception
   */
  public static int add(Connection dbConn, YHJhDocrecvInfo code)
      throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, code);
    dbConn.commit();
    return YHCalendarLogic.getMaSeqId(dbConn, "JH_DOCRECV_INFO");
  }

  /**
   * 更新
   * 
   * @param dbConn
   * @param code
   * @return
   * @throws Exception
   */
  public static void updateNation(Connection dbConn, YHJhDocrecvInfo nation)
      throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, nation);
  }

  /**
   * 查询ById
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static YHJhDocrecvInfo getById(Connection dbConn, String seqId)
      throws Exception {
    YHORM orm = new YHORM();
    YHJhDocrecvInfo code = (YHJhDocrecvInfo) orm.loadObjSingle(dbConn,
        YHJhDocrecvInfo.class, Integer.parseInt(seqId));
    return code;
  }

  /**
   * 条件查询
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static List<YHJhDocrecvInfo> select(Connection dbConn, String[] str)
      throws Exception {
    YHORM orm = new YHORM();
    List<YHJhDocrecvInfo> codeList = new ArrayList<YHJhDocrecvInfo>();
    codeList = orm.loadListSingle(dbConn, YHJhDocrecvInfo.class, str);
    return codeList;
  }

  /**
   * 删除ById
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delById(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHJhDocrecvInfo.class, Integer.parseInt(seqId));
  }

  /**
   * 根据用户得到分页列表---管理
   * 
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public static String toSearchData(Connection conn, Map request,
      HttpServletRequest request1) throws Exception {
    String status = request1.getParameter("status") == null ? "" : request1
        .getParameter("status");

    String sql = "select seq_id,doc_title,doc_no,doc_type,send_dept_name,send_datetime,return_reason,main_doc_id,main_doc_name from jh_docrecv_info ";
    sql = sql + " where status ='" + status + "' ";
    String docTitle = request1.getParameter("docTitle");
    String docNo = request1.getParameter("docNo");
    String sendDate = request1.getParameter("sendDate");
    String sendDate2 = request1.getParameter("sendDate2");
    if (!YHUtility.isNullorEmpty(docTitle)) {
      docTitle = docTitle.replace("'", "''");
      sql = sql + " and DOC_TITLE like '%" + YHDBUtility.escapeLike(docTitle)
          + "%' " + YHDBUtility.escapeLike();
    }
    

    if (!YHUtility.isNullorEmpty(docNo)) {
      docNo = docNo.replace("'", "''");
      sql = sql + " and DOC_NO like '%" + YHDBUtility.escapeLike(docNo) + "%' "
          + YHDBUtility.escapeLike();
    }

    if (!YHUtility.isNullorEmpty(sendDate)) {
      sql = sql + " and "
          + YHDBUtility.getDateFilter("SEND_DATETIME", sendDate, ">=");
    }
    if (!YHUtility.isNullorEmpty(sendDate2)) {
      sql = sql
          + " and "
          + YHDBUtility.getDateFilter("SEND_DATETIME", sendDate2 + " 23:59:59",
              "<=");
    }
    
    sql = sql + " and hand_status <> '1'";//已归档
    sql = sql + " order by send_datetime desc";

    // 下面是判断是否登录人部门是计划的接收部门，且团组号也要在这条计划中

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    return pageDataList.toJson();
  }

  /**
   * 
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static boolean checkCodeNo(Connection dbConn, String codeNo,
      String seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(codeNo)) {
      codeNo = "";
    }
    codeNo = codeNo.replaceAll("'", "''");
    String sql = "SELECT * from H_PRINT_NATION where NATION_NO='" + codeNo
        + "'";
    if (YHUtility.isInteger(seqId)) {
      sql = sql + " and SEQ_ID <> " + seqId;
    }
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return true;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return false;
  }

  /**
   * 
   * 更新
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public void updateStatus(Connection dbConn, String seqId, String status)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;

    if (seqId.endsWith(",")) {
      seqId = seqId.substring(0, seqId.length() - 1);
    }
    String sql = "update JH_DOCRECV_INFO set STATUS = '" + status
        + "'  where seq_id in(" + seqId + ")";
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  /**
   * 
   * 更新
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public void updateStatusReturn(Connection dbConn, String seqId,
      String returnReason) throws Exception {
    PreparedStatement stmt = null;
    ResultSet rs = null;

    if (seqId.endsWith(",")) {
      seqId = seqId.substring(0, seqId.length() - 1);
    }
    String sql = "update JH_DOCRECV_INFO set STATUS =4,return_reason=?  where seq_id in("
        + seqId + ")";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1, returnReason);
      stmt.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  /**
   * 
   * 更新
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public void delete(Connection dbConn, String seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;

    if (seqId.endsWith(",")) {
      seqId = seqId.substring(0, seqId.length() - 1);
    }
    String sql = "delete  from  JH_DOCRECV_INFO   where seq_id in(" + seqId
        + ")";
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  /**
   * 处理上传附件，返回附件id，附件名称
   * 
   * 
   * 
   * 
   * 
   * 
   * @param request
   *          HttpServletRequest
   * @param
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm)
      throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      System.out.println(fileForm.getParameter("moduel"));
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }

        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath() + File.separator
            + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath()
            + File.separator + fileForm.getParameter("moduel") + File.separator
            + hard + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }

  

  /*
   * 接收成功，生成XML文件
   */
  public static String toXML(String guid, int docsendId, String taskId,
      String status,String sendDept,String sendDeptName) {

    String str = "<?xml version='1.0' encoding='UTF-8'?>" + "<body>" + "<guid>"
        + guid + "</guid>" + "<docsendId>" + docsendId + "</docsendId>"
        + "<taskId>" + taskId + "</taskId>" + "<status>" + status + "</status>"
        + "<sendDept>" + sendDept + "</sendDept>"
            + "<sendDeptName>" + sendDeptName + "</sendDeptName>"
        + "</body>";
    return str;
  }

  /**
   * 生成XML附件
   * 
   * @param guid :公文发送时的唯一标识
   * @param docsendId ： 公文发文主表的seqId
   * @param taskId ：从表 任务表的seqId
   * @param status：状态
   * @return toUnit：发送单位
   * @throws Exception 
   */
  public static void createXML(YHJhDocrecvInfo recvInfo,String webroot , Connection dbConn) throws Exception {
    if(!YHUtility.isNullorEmpty(recvInfo.getSendDept())){
      YHDocSendLogic dsl = new YHDocSendLogic();
      String reciveDeptDescClient = dsl.getEsbUser(dbConn, recvInfo.getSendDept());
      if(!YHUtility.isNullorEmpty(reciveDeptDescClient)){
        String xml = toXML(recvInfo.getGuid(), recvInfo.getDocsendId(), recvInfo.getSeqId()+"", "1",recvInfo.getSendDept(),recvInfo.getSendDeptName());
        //此次打包任务名称
        String taskName = YHGuid.getRawGuid();
        String path = YHSysProps.getAttachPath() + File.separator + "uploadJTGW"
            + File.separator + taskName ;
        String FileName = "JHMESSAGEDATA_" + YHGuid.getRawGuid() + ".xml";
        while (YHDocUtil.getExist(path, FileName)) {
          FileName = "JHMESSAGEDATA_" + YHGuid.getRawGuid() + ".xml";
        }
        
        //利用webservice
        MessageContext mc = MessageContext.getCurrentMessageContext();
        if (mc == null){
          System.out.println("无法获取到MessageContext");
         // return;
        }
       // HttpServletRequest request = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
       // webroot = request.getRealPath("/");
       // webroot = "D:\\project\\yhcoreGW\\tomcat\\..\\webroot\\yh\\";
        YHEsbClientConfig config = YHEsbClientConfig.builder(webroot + YHEsbConst.CONFIG_PATH) ;
        YHEsbService esbService = new YHEsbService();
        String fileName  =path + File.separator + FileName;
        YHFileUtility.storeString2File(fileName, xml);

     
        String ret = esbService.send(fileName, reciveDeptDescClient,config.getToken(), "JHdoc", "");
        System.out.println(ret);
        
        //生成zip压缩包
        //YHZipFileTool.doZip(path, path + ".zip");
      }
    }
  
  }
  
  /**
   * 更新模版的sessionToken  byId
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static void updateById(Connection dbConn, String sessionToken ,String seqId) throws Exception {
    String sql = "update jh_docrecv_info set SESSIONTOKEN = ? where SEQ_ID = " + seqId ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, sessionToken);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  
  /**
   * 判断sessionToken 是否过期
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static boolean getSessionToken(Connection dbConn, String sessionToken ) throws Exception {
    String sql = "select SEQ_ID from oa_online  where SESSION_TOKEN = ? and USER_STATE = '1'" ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, sessionToken);
      rs = ps.executeQuery();
      if(rs.next()){
        return true;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return false;
  }
  
  /**
   * 更新办理状态
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static void updateHandStatus(Connection dbConn, String handStatus ,String seqId) throws Exception {
    String sql = "update jh_docrecv_info set HAND_STATUS = ? where SEQ_ID = " + seqId ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, handStatus);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 正在接收文件
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getReceiving(Connection dbConn, Map request1, YHPerson person, HttpServletRequest request) throws Exception {
    
    try {
      String sql = " SELECT  SEQ_ID, MESSAGE, FROM_ID, STATUS, GUID FROM esb_down_task WHERE STATUS != 0 ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request1);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
}

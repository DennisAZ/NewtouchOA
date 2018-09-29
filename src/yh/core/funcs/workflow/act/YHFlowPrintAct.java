package yh.core.funcs.workflow.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.exps.YHInvalidParamException;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHFlowPrintLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;

public class YHFlowPrintAct {
  private static Logger log = Logger.getLogger(YHFlowPrintAct.class);
  private YHFlowPrintLogic logic=new YHFlowPrintLogic();
  /**
   * 上传AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadAip(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String tName = request.getParameter("T_NAME");
      String content = request.getParameter("CONTENT");
      String tType= request.getParameter("t_type");
      String flowId= request.getParameter("flow_id");
      
      if (YHUtility.isNullorEmpty(tName)) {
        throw new YHInvalidParamException("没有传递模板名称");
      }
      if (YHUtility.isNullorEmpty(content)) {
        throw new YHInvalidParamException("没有传递模板内容");
      }
      String data= this.logic.savePrintTpl(dbConn, tName, content, tType, flowId);
      data="{id:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
      request.setAttribute(YHActionKeys.RET_DATA, data );
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 上传AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAipFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      
      String module = request.getParameter("module");
      String attachmentId = request.getParameter("attachmentId");

      int index = attachmentId.indexOf("_");
      String ym = attachmentId.substring(0 , index);
      attachmentId = attachmentId.substring(index + 1);
      String pathPx = YHSysProps.getAttachPath();
      String filePath = pathPx + File.separator + module;
      String filePath2  = filePath + File.separator +  ym + File.separator +  attachmentId + ".aip";
      
      File file = new File(filePath2);
      FileInputStream fi = new FileInputStream(file);
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html");
      response.setHeader("Cache-Control", "no-cache");  
      OutputStream os = response.getOutputStream();
      byte[] buff = new byte[1024];
      int readLength = 0;
      while ((readLength = fi.read(buff)) > 0) {        
        os.write(buff, 0, readLength);
      }
      os.flush();
      os.close();
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return null;
  }
  
  /**
   * 上传AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadAipFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      
      YHFileUploadForm fileForm = new YHFileUploadForm();
      //注意这里的
      fileForm.parseUploadRequest(request);
      String runId = fileForm.getParameter("runId");
      String seqId = fileForm.getParameter("seqId");
      String attachmentId= fileForm.getParameter("ATTACHMENT_ID");
      String module= fileForm.getParameter("MODULE");
      String trusPath  = "";
      String pathPx = YHSysProps.getAttachPath();
      String filePath = pathPx + File.separator +  module;
      if (YHUtility.isNullorEmpty(attachmentId)) {
        Calendar cld = Calendar.getInstance();
        int year = cld.get(Calendar.YEAR) % 100;
        int month = cld.get(Calendar.MONTH) + 1;
        String mon = month >= 10 ? month + "" : "0" + month;
        String hard = year + mon;
        attachmentId  = YHGuid.getRawGuid();
        trusPath = filePath + File.separator +  hard + File.separator +  attachmentId + ".aip";
        File storeDir = new File(filePath + File.separator +  hard);
        if (!storeDir.exists()) {
          storeDir.mkdirs();
        }
        attachmentId = hard + "_" + attachmentId;
        YHFlowPrintLogic logic = new YHFlowPrintLogic();
        logic.updateAip(dbConn, seqId, runId, attachmentId);
      } else {  
        int index = attachmentId.indexOf("_");
        String ym = attachmentId.substring(0 , index);
        attachmentId = attachmentId.substring(index + 1);
        trusPath  = filePath + File.separator + ym + File.separator + attachmentId + ".aip";
      }
      fileForm.saveFile(trusPath);
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html");
      response.setHeader("Cache-Control", "no-cache");  
      PrintWriter pw = response.getWriter();
      pw.print("ok");
      pw.flush();
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * 加载AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadAip(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String seq_id = request.getParameter("seq_id");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      
      String data =logic.loadAip(dbConn, seq_id);
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
      request.setAttribute(YHActionKeys.RET_DATA,  data );
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 加载AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadAutoFlag(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      int flowId = 0 ;
      String fl = request.getParameter("flowId");
      if ( YHUtility.isInteger(fl)) {
        flowId = Integer.parseInt(fl);
      }
      int runId = 0;
      String fl2 = request.getParameter("runId");
      if ( YHUtility.isInteger(fl2)) {
        runId = Integer.parseInt(fl2);
      }
      String modelShort = request.getParameter("nodes");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String data = YHUtility.encodeSpecial(logic.analysisAutoFlag(runId, flowId, modelShort, dbConn));
       data = data.replace("<br>", "\n");
      data = YHDiaryUtil.cutHtml(data);
      data = YHUtility.encodeSpecial(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,  "\"" + data + "\"" );
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更新AIP
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateAipAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String tName = request.getParameter("tName");
      String content = request.getParameter("content");
      String tType= request.getParameter("tType");
      String flowId= request.getParameter("flowId");
      String seqId = request.getParameter("seqId"); 
      String prcsStr = request.getParameter("prcsStr"); 
      this.logic.updatePrintTpl(dbConn,seqId,prcsStr, tName, content, tType, flowId);
   
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
     
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 删除AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delTplAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String seq_id = request.getParameter("seq_id");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      
      logic.delTplLogic(dbConn, seq_id);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP模板删除成功");

    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  
  /**
   * 按照flow_id加载AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTplListAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String flow_id = request.getParameter("flow_id");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      
      String data =logic.getTplList(dbConn, flow_id);
     data="{list:["+data+"]}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
      request.setAttribute(YHActionKeys.RET_DATA,  data );
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 按照flow_id加载AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDisSelectByFlowIdAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String flow_id = request.getParameter("flow_id");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      
      String data =logic.getDisSelectByFlowIdLogic(dbConn, flow_id);
     data="["+data+"]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
      request.setAttribute(YHActionKeys.RET_DATA,  data );
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 按照flow_id加载AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSelectByFlowIdAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String seq_id = request.getParameter("seq_id");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      
      String data =logic.getSelectByFlowIdLogic(dbConn, seq_id);
     data="["+data+"]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
      request.setAttribute(YHActionKeys.RET_DATA,  data );
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 按照flow_id加载AIP文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTempOptionAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String flowId = request.getParameter("flowId");
      String runId = request.getParameter("runId");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);

      String data =logic.getTempOptionLogic(dbConn,user,flowId,runId);
      data="["+data+"]";
      log.debug(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
      request.setAttribute(YHActionKeys.RET_DATA,  data );
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 根据flow——id获取item，data
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowItemData(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {

      String runId = request.getParameter("runId");
      String flowId = request.getParameter("flowId");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);      
      dbConn = requestDbConn.getSysDbConn();
      String data =logic.getFlowItemData(dbConn,flowId,runId);
     data="["+data+"]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存成功");
      request.setAttribute(YHActionKeys.RET_DATA,  data );
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "AIP 文件保存失败");
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
}

package yh.core.funcs.doc.act;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormType;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.logic.YHMyWorkLogic;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHFlowExportAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.act.YHFlowExportAct");
  public String exportForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String formId = request.getParameter("formId");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      YHDocFlowFormType  form = (YHDocFlowFormType) orm.loadObjSingle(dbConn, YHDocFlowFormType.class, Integer.parseInt(formId));
      request.setAttribute("formName", form.getFormName());
      String printModel = form.getPrintModel();
      String css = form.getCss();
      if (css == null) {
        css = "";
      }
      String script = form.getScript();
      if (script == null) {
        script = "";
      }
      String tmp = "<style>"+ css +"</style><script>" + script + "</script>";
      printModel = tmp + printModel ;
      printModel = printModel.replaceAll("\\\\\"", "\"");
      request.setAttribute("printModel", printModel);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowform/export.jsp";
  }
  public String exportFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    InputStream in = null;
    OutputStream out = null;
    try{
      String flowId = request.getParameter("flowId");
      int seqId = Integer.parseInt(flowId);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic logic = new YHFlowTypeLogic();
      String flowName = logic.getFlowTypeName(seqId, dbConn);
      
      StringBuffer sb = new StringBuffer();
      sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
      sb.append("<WorkFlow>\r\n");
      sb.append(logic.getFlowTypeMsg(seqId, dbConn));
      sb.append(logic.getFlowProcMsg(seqId, dbConn));
      sb.append("</WorkFlow>\r\n");
      response.setContentType("application/octet-stream");
      flowName = URLEncoder.encode(flowName, "UTF-8");
      response.setHeader("Cache-control","private");
      response.setHeader("Accept-Ranges","bytes");
      //response.setHeader("Accept-Length",printModel.length() + "");
      response.setHeader("Content-Disposition", "attachment; filename=" + flowName +".xml");
      in = new ByteArrayInputStream(sb.toString().getBytes("UTF-8")) ;
      out = response.getOutputStream();
      byte[] buff = new byte[1024];
      int readLength = 0;
      while ((readLength = in.read(buff)) > 0) {        
         out.write(buff, 0, readLength);
      }
      out.flush();
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
    }
    return null;
  }
  public String exportFlowZip(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    OutputStream out = null;
    try{
      String runIdStr  = request.getParameter("runIdStr");
      if (runIdStr == null) {
        String runId = request.getParameter("runId");
        if (runId != null) {
          runIdStr =  runId;
        } else {
          runIdStr = "";
        }
      }
      if (runIdStr.endsWith( ",")) {
        runIdStr = runIdStr.substring(0, runIdStr.length() - 1);
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMyWorkLogic zip = new YHMyWorkLogic();
      response.setContentType("application/octet-stream");
      response.setHeader("Cache-control","private");
      response.setHeader("Accept-Ranges","bytes");
      String fileName = URLEncoder.encode("OA工作流(" + YHUtility.getCurDateTimeStr() + ").zip", "UTF-8");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileName );
      out = response.getOutputStream();
      String imgPath = YHWorkFlowUtility.getImgPath(request);
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String , InputStream> map = zip.getExportZip(runIdStr, dbConn , loginUser , imgPath);
      Set<String> key = map.keySet();
      org.apache.tools.zip.ZipOutputStream zipout = new org.apache.tools.zip.ZipOutputStream(out);
      zipout.setEncoding("GBK");
      for (String tmp : key) {
        InputStream in = map.get(tmp);
        YHMyWorkLogic.output(in, zipout, tmp);
      }
      zipout.flush();
      //out.flush();
      zipout.close();
     // out.close();
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    } 
    return null;
  }
}

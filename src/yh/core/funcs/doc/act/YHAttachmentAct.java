package yh.core.funcs.doc.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.doc.logic.YHAttachmentLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;

public class YHAttachmentAct {
  
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.act.YHAttachmentAct");
  /**
   * 上传附件处理
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("UTF-8");
    Connection dbConn = null;
    boolean isFeedAttach = false;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       
      YHFileUploadForm fileForm = new YHFileUploadForm();
      //注意这里的      fileForm.parseUploadRequest(request);
      String sIsFeedAttach = fileForm.getParameter("isFeedAttach");
      if (sIsFeedAttach != null && !"".equals(sIsFeedAttach)) {
        isFeedAttach = Boolean.valueOf(sIsFeedAttach);
      }
      int runId = Integer.parseInt(fileForm.getParameter("runId"));
      int flowId = Integer.parseInt(fileForm.getParameter("flowId"));
      
      if (isFeedAttach) {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        Map attach = logic.uploadAttachment(fileForm, dbConn);
        String data = "obj = {id:'"+(String)attach.get("id") +"' , name:'"+(String)attach.get("name")+"'};";
        request.setAttribute("fileForm", "ATTACHMENT1_fileForm");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "取得成功 ");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      } else {
        boolean isEdit = false;
        String sIsEdit = fileForm.getParameter("isEdit");
        if (sIsEdit != null && !"".equals(sIsEdit)) {
          isEdit = Boolean.valueOf(sIsEdit);
        }
        if (!isEdit) {
          String sPrcsId = fileForm.getParameter("prcsId");
          int prcsId = Integer.parseInt(sPrcsId);
          YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
          //验证是否有权限,并取出权限字符串
          String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
          if ( "".equals(roleStr) && !isEdit) {//没有权限
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
            request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程办理权限，请与OA管理员联系");
            return "/core/inc/rtjson.jsp";
          }
        } 
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        logic.addAttachment(runId , fileForm, dbConn);
        request.setAttribute("fileForm", "fileForm");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "取得成功 ");
      }
    } catch (SizeLimitExceededException ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "上传文件的大小超出了系统允许上传文件的大小! ");
      return "/core/inc/rtuploadfile.jsp";
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtuploadfile.jsp";
  }
  public String getAttachments(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("UTF-8");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser ,dbConn);
      if ( "".equals(roleStr) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,"没有该流程办理权限，请与OA管理员联系");
      } else {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        String data = "[" + logic.getAttachments(loginUser, runId , flowId , dbConn) + "]";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "取得成功 ");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      boolean isEdit = false;
      String sIsEdit = request.getParameter("isEdit");
      if (sIsEdit != null && !"".equals(sIsEdit)) {
        isEdit = Boolean.valueOf(sIsEdit);
      }
      if (!isEdit) {
        int prcsId = Integer.parseInt(request.getParameter("prcsId"));
        YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
        //验证是否有权限,并取出权限字符串
        String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
        if ( "".equals(roleStr) && !isEdit) {//没有权限
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程办理权限，请与OA管理员联系");
          return "/core/inc/rtjson.jsp";
        }
      } 
      YHAttachmentLogic logic = new  YHAttachmentLogic();
      logic.delAttachment(runId, attachmentId, attachmentName , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功 ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delFeedbackAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int feedId = Integer.parseInt(request.getParameter("feedId"));
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      YHAttachmentLogic logic = new  YHAttachmentLogic();
      logic.delFeedAttachment(feedId, attachmentId, attachmentName , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功 ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String createAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      String newType = request.getParameter("newType");
      String newName = request.getParameter("newName");
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if ( "".equals(roleStr) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程办理权限，请与OA管理员联系");
      } else {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        String realPath = request.getRealPath("/");
        String attachment = logic.createAttachment(runId, newType, newName , dbConn, realPath);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功 ");
        request.setAttribute(YHActionKeys.RET_DATA, attachment);
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String downAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String filePath = request.getParameter("filePath");
      String fileName = request.getParameter("fileName");
     // filePath = URLEncoder.encode(filePath, "UTF-8");
      filePath =  YHAttachmentLogic.filePath + File.separator +  filePath;
      fileName = URLEncoder.encode(fileName, "UTF-8");
      
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
      InputStream in = null;
      OutputStream out = null;
      try {      
        in = new FileInputStream(filePath);
        out = response.getOutputStream();
        byte[] buff = new byte[1024];
        int readLength = 0;
        while ((readLength = in.read(buff)) > 0) {        
           out.write(buff, 0, readLength);
        }
        out.flush();
      }catch(Exception ex) {
        ex.printStackTrace();
      }finally {
        try {
          if (in != null) {
            in.close();
          }
        }catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String restoreFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String attachName = request.getParameter("attachmentName");
      String attachDir = request.getParameter("attachmentDir");
      String diskId = request.getParameter("diskId");
      
      Map map = new HashMap();
      map.put(YHSelAttachUtil.ATTACH_DIR, attachDir);
      map.put(YHSelAttachUtil.ATTACH_NAME, attachName);
      map.put(YHSelAttachUtil.DISK_ID, diskId);
      boolean isFeedAttach = false;
      String sIsFeedAttach = request.getParameter("isFeedAttach");
      if (sIsFeedAttach != null && !"".equals(sIsFeedAttach)) {
        isFeedAttach = Boolean.valueOf(sIsFeedAttach);
      }
      if (isFeedAttach) {
        YHSelAttachUtil su = new YHSelAttachUtil(map , "workflow");
        String ids = su.getAttachIdToString("");
        String names = su.getAttachNameToString("");
        if (!ids.endsWith(","))  {
          ids += ",";
        }
        if (!names.endsWith("*")) {
          names += "*";
        }
        String data = "{id:'"+ ids +"' , name:'"+ YHUtility.encodeSpecial(names) +"'}";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "取得成功 ");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      } else {
        int runId = Integer.parseInt(request.getParameter("runId"));
        int flowId = Integer.parseInt(request.getParameter("flowId"));
        boolean isEdit = false;
        String sIsEdit = request.getParameter("isEdit");
        if (sIsEdit != null && !"".equals(sIsEdit)) {
          isEdit = Boolean.valueOf(sIsEdit);
        }
        if (!isEdit) {
          int prcsId = Integer.parseInt(request.getParameter("prcsId"));
          YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
          //验证是否有权限,并取出权限字符串
          String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
          if ( "".equals(roleStr) && !isEdit) {//没有权限
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
            request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程办理权限，请与OA管理员联系");
            return "/core/inc/rtjson.jsp";
          }
        } 
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        
        YHSelAttachUtil su = new YHSelAttachUtil(map , "workflow");
        String ids = su.getAttachIdToString("");
        String names = su.getAttachNameToString("");
        logic.restoreFile(names, ids , runId, dbConn);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "取得成功 ");
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

package yh.subsys.oa.profsys.act.out;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProjectFile;
import yh.subsys.oa.profsys.logic.out.YHOutProjectFileLogic;

public class YHOutProjectFileAct {
  /**
   * 新建会议纪要
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addProjectFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectFile file = (YHProjectFile)YHFOM.build(request.getParameterMap());
      
      //文件柜中上传附件
      String attachmentName = request.getParameter("attachmentName");
      String attachmentId = request.getParameter("attachmentId");
      YHSelAttachUtil sel = new YHSelAttachUtil(request,"profsys");
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !"".equals(attachmentId) &&  !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !"".equals(attachmentName)  && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;

      file.setAttachmentId(attachmentId);
      file.setAttachmentName(attachmentName);
      
      YHOutProjectFileLogic.addProjectFile(dbConn,file);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询相关文档
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryOutFileByProjId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String projId = request.getParameter("projId");
      if(YHUtility.isNullorEmpty(projId)){
        projId = "0";
      }
      String data = YHOutProjectFileLogic.toSearchData(dbConn,request.getParameterMap(),projId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }

  /**
   * 删除相关文档
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteFileById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      YHOutProjectFileLogic.deleteFileById(dbConn, seqId);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询相关文档
   * @param request
   * @param response
   * @return
   * @throws Exception updateProjectFile
   */
  public String getFileById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      String data = "";
      if (YHUtility.isInteger(seqId)) {
        YHProjectFile file = YHOutProjectFileLogic.getFileById(dbConn, seqId);
        if(file != null){
          data = YHFOM.toJson(file).toString();
        }
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 修改相关文档
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String updateProjectFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectFile file = (YHProjectFile)YHFOM.build(request.getParameterMap());
      
      //文件柜中上传附件
      String attachmentName = request.getParameter("attachmentName");
      String attachmentId = request.getParameter("attachmentId");
      YHSelAttachUtil sel = new YHSelAttachUtil(request,"profsys");
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !"".equals(attachmentId) &&  !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !"".equals(attachmentName)  && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;

      file.setAttachmentId(attachmentId);
      file.setAttachmentName(attachmentName);
      
      YHOutProjectFileLogic.updateProjectFile(dbConn,file);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 文档查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String profsysSelectFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectFile file = new YHProjectFile();
      String fileNum = request.getParameter("fileNum");
      String fileName = request.getParameter("fileName");
      String fileType = request.getParameter("fileType");
      String projCreator = request.getParameter("projCreator");
      String fileTitle = request.getParameter("fileTitle");
      String projFileType = request.getParameter("projFileType");
      file.setFileNum(fileNum);
      file.setFileName(fileName);
      file.setFileType(fileType);
      file.setProjCreator(projCreator);
      file.setFileTitle(fileTitle);
      file.setProjFileType(projFileType);

      //String projId = YHOutProjectFileLogic.profsysSelectFile(dbConn,file);
      //通用查询数据
      String data = YHOutProjectFileLogic.profsysFileList(dbConn,request.getParameterMap(),projFileType, file);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}

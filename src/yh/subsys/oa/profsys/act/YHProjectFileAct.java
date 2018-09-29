package yh.subsys.oa.profsys.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProjectFile;
import yh.subsys.oa.profsys.logic.YHProjectFileLogic;
import yh.subsys.oa.profsys.logic.YHProjectLogic;
import yh.subsys.oa.profsys.logic.out.YHOutProjectFileLogic;

public class YHProjectFileAct {
  /**
   * 新建更新 会议纪要
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUpdateProjectFile(HttpServletRequest request,
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
      if(file!=null){
        if(file.getSeqId()>0){
          YHOutProjectFileLogic.updateProjectFile(dbConn,file);
        }else{
          YHOutProjectFileLogic.addProjectFile(dbConn,file);
        }
      }
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
      String projFileType = request.getParameter("projFileType");
      if(YHUtility.isNullorEmpty(projFileType)){
        projFileType = "0";
      }

      if(YHUtility.isNullorEmpty(projId)){
        projId = "0";
      }
      String data = YHProjectFileLogic.toSearchData(dbConn,request.getParameterMap(),projId,projFileType);
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
          YHPersonLogic personLogic = new YHPersonLogic();
          String projCreatorName = "";
          if(!YHUtility.isNullorEmpty(file.getProjCreator())){
            projCreatorName = YHUtility.encodeSpecial(personLogic.getNameBySeqIdStr(file.getProjCreator(), dbConn));
          }
          data = YHFOM.toJson(file).toString().substring(0, YHFOM.toJson(file).toString().length()-1) + ",projCreatorName:\"" + projCreatorName + "\"}";
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
  
  /***
   * 更新的附件ById
     删除一个附件
   * @return
   * @throws Exception 
   */
  public String deleleFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String attachId = request.getParameter("attachId");
      String attachName = request.getParameter("attachName");
      if(seqId==null){
        seqId = "";
      }
      if(attachId==null){
        attachId = "";
      }
      if(attachName==null){
        attachName = "";
      }
      YHOutProjectFileLogic fileLogic = new YHOutProjectFileLogic();
      YHProjectFile file  = null ;
      String updateFlag = "0";
      if(seqId!=null&&!seqId.equals("")){
        file = fileLogic.getFileById(dbConn, seqId) ;
       if(file!=null){
         String attachmentId = file.getAttachmentId();
         String attachmentName = file.getAttachmentName();
         if(attachmentId==null){
           attachmentId = "";
         }
         if(attachmentName==null){
           attachmentName = "";
         }
         String[] attachmentIdArray = attachmentId.split(",");
         String[] attachmentNameArray = attachmentName.split("\\*");
         String newAttachmentId = "";
         String newAttachmentName = "";
         for (int i = 0; i < attachmentIdArray.length; i++) {
           if(!attachmentIdArray[i].equals(attachId)){
             newAttachmentId = newAttachmentId +attachmentIdArray[i] + ",";
           }
         }
         if(!newAttachmentId.equals("")&&newAttachmentId.endsWith(",")){
           newAttachmentId = newAttachmentId.substring(0, newAttachmentId.length()-1);
         }
         for (int i = 0; i < attachmentNameArray.length; i++) {
           if(!attachmentNameArray[i].equals(attachName)){
             newAttachmentName = newAttachmentName +attachmentNameArray[i] + "*";
           }
         }
         if(!newAttachmentName.equals("")&& newAttachmentName.endsWith("*")){
           newAttachmentName = newAttachmentName.substring(0, newAttachmentName.length()-1);
         }
         YHProjectLogic pl = new YHProjectLogic();  
         pl.updateFile(dbConn,"oa_project_attach" ,newAttachmentId, newAttachmentName, seqId);
         updateFlag = "1";
       }
      }
      String data = "{updateFlag:"+updateFlag+"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

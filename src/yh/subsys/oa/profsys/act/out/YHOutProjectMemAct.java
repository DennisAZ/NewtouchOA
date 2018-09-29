package yh.subsys.oa.profsys.act.out;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProjectMem;
import yh.subsys.oa.profsys.logic.out.YHOutProjectMemLogic;

public class YHOutProjectMemAct {
  /**
   * 新建项目——人员
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addProjectMem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectMem mem = (YHProjectMem)YHFOM.build(request.getParameterMap());
      
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

      mem.setAttachmentId(attachmentId);
      mem.setAttachmentName(attachmentName);
      
      YHOutProjectMemLogic.addProjectMem(dbConn, mem);
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
   * 来访项目人员By ProjId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryOutMemByProjId(HttpServletRequest request,
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
      String data = YHOutProjectMemLogic.toSearchData(dbConn,request.getParameterMap(),projId);
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
   * 修改项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateProjectMem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectMem mem = (YHProjectMem)YHFOM.build(request.getParameterMap());
      
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

      mem.setAttachmentId(attachmentId);
      mem.setAttachmentName(attachmentName);
      
      YHOutProjectMemLogic.updateProjectMem(dbConn, mem);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *查询所有ID串名字
   */
  public String userName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");

      String data = YHOutProjectMemLogic.userName(dbConn,seqId);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 来访项目人员查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String profsysSelectMem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectMem mem = new YHProjectMem();
      String memNum = request.getParameter("memNum");
      String memPosition = request.getParameter("memPosition");
      String memName = request.getParameter("memName");
      String memSex = request.getParameter("memSex");
      String memBirth = request.getParameter("memBirth");
      String memIdNum = request.getParameter("memIdNum");
      String projMemType = request.getParameter("projMemType");
      if (!YHUtility.isNullorEmpty(memBirth)) {
        mem.setMemBirth(Date.valueOf(memBirth));
      }
      mem.setMemNum(memNum);
      mem.setMemPosition(memPosition);
      mem.setMemName(memName);
      mem.setMemSex(memSex);
      mem.setMemIdNum(memIdNum);
      mem.setProjMemType(projMemType);
      //String projId = YHOutProjectMemLogic.profsysSelectMem(dbConn,mem);
      //通用查询数据
      String data = YHOutProjectMemLogic.profsysMemList(dbConn,request.getParameterMap(),projMemType,mem);
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

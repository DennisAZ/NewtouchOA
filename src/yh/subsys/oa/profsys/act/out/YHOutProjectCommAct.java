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
import yh.subsys.oa.profsys.data.YHProjectComm;
import yh.subsys.oa.profsys.logic.out.YHOutProjectCommLogic;


public class YHOutProjectCommAct {
  /**
   * 新建会议纪要
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addProjectComm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectComm comm = (YHProjectComm)YHFOM.build(request.getParameterMap());

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

      comm.setAttachmentId(attachmentId);
      comm.setAttachmentName(attachmentName);

      YHOutProjectCommLogic.addProjectComm(dbConn,comm);
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
   * 查询会议纪要
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String queryOutCommByProjId(HttpServletRequest request,
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
      String data = YHOutProjectCommLogic.toSearchData(dbConn,request.getParameterMap(),projId);
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
   * 删除会议纪要
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteCommById(HttpServletRequest request,
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
      YHOutProjectCommLogic.deleteCommById(dbConn,seqId);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询修改会议纪要
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCommById(HttpServletRequest request,
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
        YHProjectComm comm = YHOutProjectCommLogic.getCommById(dbConn,seqId);
        if(comm != null){
          data = YHFOM.toJson(comm).toString();
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
   * 修改会议纪要
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateProjectComm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectComm comm = (YHProjectComm)YHFOM.build(request.getParameterMap());

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

      comm.setAttachmentId(attachmentId);
      comm.setAttachmentName(attachmentName);

      YHOutProjectCommLogic.updateProjectComm(dbConn,comm);
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
   * 会议纪要查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String profsysSelectComm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectComm comm = new YHProjectComm();
      String commNum = request.getParameter("commNum");
      String commMemCn = request.getParameter("commMemCn");
      String commMemFn = request.getParameter("commMemFn");
      String commName = request.getParameter("commName");
      String commTime = request.getParameter("commTime");
      String commPlace = request.getParameter("commPlace");
      String projCommType = request.getParameter("projCommType");

      comm.setCommNum(commNum);
      comm.setCommMemCn(commMemCn);
      comm.setCommMemFn(commMemFn);
      comm.setCommName(commName);
      if (!YHUtility.isNullorEmpty(commTime)) {
        comm.setCommTime(Date.valueOf(commTime));
      }
      comm.setCommPlace(commPlace);
      comm.setProjCommType(projCommType);
      //String projId = YHOutProjectCommLogic.profsysSelectComm(dbConn,comm);
      //通用查询数据
      String data = YHOutProjectCommLogic.profsysCommList(dbConn,request.getParameterMap(),projCommType,comm);
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

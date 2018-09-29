package yh.core.funcs.doc.receive.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import srvSeal.SrvSealUtil;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.logic.YHFlowManageLogic;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.receive.logic.YHDocReceiveLogic;
import yh.core.funcs.doc.receive.logic.YHDocReceiveRegLogic;
import yh.core.funcs.doc.receive.logic.YHDocRegisterLogic;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
public class YHDocRegisterAct{
  public final static byte[] loc = new byte[1];
  /**
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRegList2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      PrintWriter pw = response.getWriter();
      YHDocRegisterLogic doc = new YHDocRegisterLogic();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String data = doc.getSendMesage2(user, dbConn, request.getParameterMap(), request.getRealPath("/") , "1");
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
   * 取得已登记的收文
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRegList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      PrintWriter pw = response.getWriter();
      YHDocRegisterLogic doc = new YHDocRegisterLogic();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String data = doc.getRegList(user, dbConn, request.getParameterMap(), request.getRealPath("/"));
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String getFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      String webroot = request.getRealPath("/");
      YHDocUtility util =  new YHDocUtility();
      String str = YHDocConst.getProp(webroot  , YHDocConst.DOC_RECEIVE_FLOW_SORT) ;
      String sortId = util.getSortIds(str, dbConn);
      Map map = util.getFlowBySortIds(sortId, dbConn  , user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, sortId);
      request.setAttribute(YHActionKeys.RET_DATA, this.mapTojson(map));
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String mapTojson(Map map) {
    StringBuffer sb = new StringBuffer().append("[");
    int count=0;
    Set<String> set = map.keySet();
    for (String tmp : set) {
      String name = (String)map.get(tmp);
      sb.append("{id:\"").append(tmp).append("\"").append(",").append("name:\"").append(name).append("\"},");
      count++;
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  /**
   * 根据seq_id取得未登记的记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRecReg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String rec_seqId = request.getParameter("rec_seqId");
      String webroot = request.getRealPath("/");
      YHDocRegisterLogic logic = new YHDocRegisterLogic();
      String str = logic.getRecReg(dbConn, rec_seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getRecRegBySeqId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String webroot = request.getRealPath("/");
      YHDocRegisterLogic logic = new YHDocRegisterLogic();
      String str = logic.getRecRegBySeqId(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 获得最大的收文编号
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMaxOrderNo(HttpServletRequest request, HttpServletResponse response)throws Exception{
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String type = request.getParameter("type");
      YHDocRegisterLogic logic = new YHDocRegisterLogic();
      synchronized(loc) {
        int max = logic.getMaxOrderNo(dbConn, type);
        dbConn.commit();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功");
        request.setAttribute(YHActionKeys.RET_DATA, String.valueOf(max));
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String register(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection conn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHDocRegisterLogic logic = new YHDocRegisterLogic();
      String seqId = request.getParameter("seqId");
      String recId = request.getParameter("recId");
      String recType = request.getParameter("recType");
      String recNo = request.getParameter("recNo");
      String fromDeptName = request.getParameter("fromDeptName");
      String fromDeptId = request.getParameter("fromDeptId");
      String secretsLevel = request.getParameter("secretsLevel");
      String sendDocNo = request.getParameter("sendDocNo");
      String title = request.getParameter("title");
      String copies = request.getParameter("copies");
      String recDocId = request.getParameter("recDocId");
      String recDocName = request.getParameter("recDocName");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      String flag = request.getParameter("flag");
      synchronized(loc) {
        if (YHUtility.isNullorEmpty(seqId)) {
          /*
          YHWorkFlowUtility util = new YHWorkFlowUtility();
          String path = util.getAttachPath(recDocId, recDocName, "doc");
          
          //path = path.toLowerCase();
          String newPath = "";
          if (path.endsWith(".doc") || path.endsWith("docx")) {
            SrvSealUtil ssu = new SrvSealUtil();
            int nObjID = ssu.openObj("", 0, 0);
            ssu.login(nObjID, 4, "HWSEALDEMOXX", "");
            ssu.addPage(nObjID, path, "");
            
            String [] p = util.getNewAttachPath(recDocName, "doc");
            newPath = p[1];
            recDocId = p[0];
            ssu.saveFile(nObjID, newPath, "doc");
          }*/
          logic.register(conn ,recId, recType ,recNo,fromDeptName,fromDeptId,secretsLevel,sendDocNo,title,copies,recDocId,recDocName,attachmentId,attachmentName,user.getSeqId());
          if (!YHUtility.isNullorEmpty(recId)) {
            logic.updateStatus(conn, recId);
          }
        } else {
          logic.update(conn , seqId,recId, recType ,recNo,fromDeptName,fromDeptId,secretsLevel,sendDocNo,title,copies,recDocId,recDocName,attachmentId,attachmentName);
        } 
        conn.commit();
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  public static void main(String args[]) {
    String path = "D:\\sss.docx";
    
    String newPath = "";
    if (path.endsWith(".doc") || path.endsWith("docx")) {
      SrvSealUtil ssu = new SrvSealUtil();
      int nObjID = ssu.openObj("", 0, 0);
      ssu.login(nObjID, 4, "HWSEALDEMOXX", "");
      ssu.addPage(nObjID, path, "");
      
      newPath = "D:\\333.doc";
      ssu.saveFile(nObjID, newPath, "doc");
    }
  }
  public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> attr = null;
    String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
    String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
    String data = "";
    try{
      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
      attr = docLogic.fileUploadLogic(fileForm, YHSysProps.getAttachPath());
      Set<String> keys = attr.keySet();
      for (String key : keys){
        String value = attr.get(key);
        if(attrId != null && !"".equals(attrId)){
          if(!(attrId.trim()).endsWith(",")){
            attrId += ",";
          }
          if(!(attrName.trim()).endsWith("*")){
            attrName += "*";
          }
        }
        attrId += key + ",";
        attrName += value + "*";
      }
      data = "{attrId:\"" + YHUtility.encodeSpecial(attrId) + "\",attrName:\"" + YHUtility.encodeSpecial(attrName) + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtuploadfile.jsp";
  }

  /**
   * 强制结束工作流

   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String endWorkFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String runIdStr = request.getParameter("runIdStr");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String s = "";
      boolean isManage = true;

      if (!"".equals(runIdStr)) {
        int runId = Integer.parseInt(runIdStr);
        YHFlowManageLogic manage = new YHFlowManageLogic();
        manage.endWorkFlow(runId, loginUser, dbConn);
      }
      this.setRequestSuccess(request, "结束流水号为[" + runIdStr + "]的工作,操作成功！");
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 设置错误信息
   * 
   * @param request
   * @param message
   */
  public void setRequestError(HttpServletRequest request, String message) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }

  /**
   * 设置成功信息
   * 
   * @param request
   * @param message
   */
  public void setRequestSuccess(HttpServletRequest request, String message) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }

  /**
   * 设置成功信息
   * 
   * @param request
   * @param message
   * @param data
   */
  public void setRequestSuccess(HttpServletRequest request, String message,
      String data) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
    request.setAttribute(YHActionKeys.RET_DATA, data);
  }
  /**
   * 恢复执行
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String restore(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String runId = request.getParameter("runId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHFlowManageLogic manage = new YHFlowManageLogic();
      boolean reslut = manage.restore(Integer.parseInt(runId), loginUser
          .getSeqId(), dbConn);
      if (!reslut) {
        this.setRequestSuccess(request, "您的恢复执行操作没有成功!");
      } else {
        this.setRequestSuccess(request, "流水号为[" + runId + "]的工作已经恢复到执行状态!");
      }
    } catch (Exception ex) {
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

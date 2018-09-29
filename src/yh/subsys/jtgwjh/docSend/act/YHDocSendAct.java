package yh.subsys.jtgwjh.docSend.act;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo;
import yh.subsys.jtgwjh.docReceive.logic.YHAipToJNI;
import yh.subsys.jtgwjh.docReceive.logic.YHJhDocrecvInfoLogic;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendFiles;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendStamps;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendTasks;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendStampLogic;

public class YHDocSendAct {
  private static Logger log = Logger.getLogger(YHDocSendAct.class);
  final String attachmentFolder = "jtgw";
  
  /**
   * 新建一个发文登记

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addDocInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    String sendFlag = request.getParameter("sendFlag");
    String forward = request.getParameter("forward");
    
    //转发控制打印份数---syl
    String mainFilePath = fileForm.getParameter("mainFilePath");//正文附件路径
    String SYPrintCount = fileForm.getParameter("SYPrintCount");//剩余打印份数
    String beginNo = fileForm.getParameter("beginNo");//开始编号
    String endNo = fileForm.getParameter("endNo");//结束编号
    String forwordId = fileForm.getParameter("forwordId") ;
    
    Connection dbConn = null;
    int docsendInfoId = 0;
    String msg = "";
    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHDocSendLogic logic = new YHDocSendLogic();
      YHJhDocsendInfo docsendInfo = logic.addDocInfo(dbConn, fileForm, person);
      docsendInfoId = docsendInfo.getSeqId();
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "新建发文登记：" + docsendInfo.toString() + "。" ,person.getSeqId(), request.getRemoteAddr());
      
      //直接发送公文
      if("1".equals(sendFlag)){
        msg = logic.sendDoc(dbConn, docsendInfoId, request, forward);
      }

      //修改打印份数--syl  转发
      if(!YHUtility.isNullorEmpty(mainFilePath) && !"0".equals(forwordId)){
        YHAipToJNI.UpdateAipToJNI(mainFilePath, SYPrintCount, beginNo, endNo);
      }
      //修改转发状态--syl  转发
      if(!"0".equals(forwordId)){
        YHJhDocrecvInfoLogic jdil = new YHJhDocrecvInfoLogic ();
        YHJhDocrecvInfo recv = jdil.getById(dbConn, forwordId);
        recv.setHandStatus("2");
        jdil.updateNation(dbConn, recv);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    if("2".equals(sendFlag)){
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      logic.startStamp(dbConn, docsendInfoId+"", person, request, false);
      response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/stamp/stamp.jsp?seqId="+docsendInfoId+"&flag=1&flagClose=1");
    }
    else if("1".equals(sendFlag)){
      if(msg == null){
        msg = "";
      }
      response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/newRemind.jsp?sendFlag="+sendFlag+"&msg="+URLEncoder.encode(msg,"UTF-8")+"&seqId="+docsendInfoId);
    }
    else{
      if("1".equals(forward)){
        response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/modify.jsp?seqId="+docsendInfoId);
      }
      else{
        response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/modify.jsp?seqId="+docsendInfoId);
      }
    }
    return null;
  }
  
  /**
   * 发送发文登记

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendDocInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    String docsendInfoId = request.getParameter("docsendInfoId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHDocSendLogic logic = new YHDocSendLogic();
      
      //直接发送公文
      logic.sendDoc(dbConn, Integer.parseInt(docsendInfoId), request, null);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 发送发文登记

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendDocInfoTasks(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    String guid = request.getParameter("guid");
    String toId = request.getParameter("toId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHDocSendLogic logic = new YHDocSendLogic();
      
      //直接发送公文
      logic.sendDocTasks(dbConn, guid, toId, request);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String fileLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{
    PrintWriter pw = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    try {
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHDocSendLogic logic = new YHDocSendLogic();
      StringBuffer sb = logic.uploadMsrg2Json(fileForm);
      String data = "{'state':'0','data':" + sb.toString() + "}";
      pw = response.getWriter();
      pw.println(data.trim());
      pw.flush();
    }catch(Exception e){
      pw = response.getWriter();
      pw.println("{'state':'1'}".trim());
      pw.flush();
    } finally {
      pw.close();
    }
    return null;
  }
  
  /**
   * 单文件附件上传

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqIdStr = request.getParameter("seqId");
      
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      Map<String, String> attr = null;
      String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
      String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
      String data = "";
      YHDocSendLogic logic = new YHDocSendLogic();
      attr = logic.fileUploadLogic(fileForm);
      Set<String> keys = attr.keySet();
      for (String key : keys) {
        String value = attr.get(key);
        if(attrId != null && !"".equals(attrId)){
          if(!(attrId.trim()).endsWith(",")){
            attrId += ",";
          }
          if(!(attrName.trim()).endsWith("*")){
            attrName += "*";
          }
        }
        attrId = key;
        attrName = value;
      }
      int seqId = logic.setDocMainInfo(dbConn, attrId, attrName, loginUser, seqIdStr);
      data = "{seqId:\""+seqId+"\",attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
    }
    return "/core/inc/rtuploadfile.jsp";
  }
  
  /**
   * 发文登记列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendLogic logic = new YHDocSendLogic();
      String data = logic.getJsonLogic(dbConn, request.getParameterMap(), person, request);
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
   * 已发发文列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getListJsonComplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendLogic logic = new YHDocSendLogic();
      String data = logic.getJsonCompleteLogic(dbConn, request.getParameterMap(), person, request);
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
   * 删除发文登记
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

      YHDocSendLogic logic = new YHDocSendLogic();
      String filePath = YHSysProps.getAttachPath() + File.separator + this.attachmentFolder + File.separator;
      String temp = logic.deleteFileLogic(dbConn, seqIdStr, filePath);
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "删除发文标题为" + temp + "的公文登记信息。"  ,person.getSeqId(), request.getRemoteAddr());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDocSendLogic logic = new YHDocSendLogic();
      
      //主表json串
      YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo) logic.getDetailLogic(dbConn, Integer.parseInt(seqId));
      if (docsendInfo == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data1 = YHFOM.toJson(docsendInfo);
      data1.deleteCharAt(0);
      data1.deleteCharAt(data1.length() - 1);
      
      //附件从表json串
      List<YHJhDocsendFiles> docsendFiles = logic.getFilesDetailLogic(dbConn, Integer.parseInt(seqId));
      StringBuffer data2 = new StringBuffer("[");
      for(YHJhDocsendFiles files : docsendFiles){
        StringBuffer dataTemp = YHFOM.toJson(files);
        data2.append(dataTemp);
        data2.append(",");
      }
      if(data2.length() > 3){
        data2.deleteCharAt(data2.length() - 1);
      }
      data2.append("]");
      
      //签发/任务从表json串
      List<YHJhDocsendTasks> docsendTasks = logic.getFilesTasksLogic(dbConn, Integer.parseInt(seqId));
      StringBuffer data3 = new StringBuffer("[");
      for(YHJhDocsendTasks tasks : docsendTasks){
        StringBuffer dataTemp = YHFOM.toJson(tasks);
        data3.append(dataTemp);
        data3.append(",");
      }
      if(data3.length() > 3){
        data3.deleteCharAt(data3.length() - 1);
      }
      data3.append("]");
      
      //盖章从表json串
      StringBuffer data4 = new StringBuffer("[");
      if("1".equals(docsendInfo.getStampComplete())){
        
        List<YHJhDocsendStamps> docsendStamps = logic.getStampsLogic(dbConn, Integer.parseInt(seqId));
        for(YHJhDocsendStamps stamps : docsendStamps){
          StringBuffer dataTemp = YHFOM.toJson(stamps);
          data4.append(dataTemp);
          data4.append(",");
        }
        if(data4.length() > 3){
          data4.deleteCharAt(data4.length() - 1);
        }
      }
      data4.append("]");
      
      
      String data = "{"+data1+",\"data2\":"+data2+",\"data3\":"+data3+",\"data4\":"+data4+"}";
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 修改发文登记

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateDocInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    String sendFlag = request.getParameter("sendFlag");
    String seqId = request.getParameter("seqId");
    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    
    Connection dbConn = null;
    String msg = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHDocSendLogic logic = new YHDocSendLogic();
      YHJhDocsendInfo docsendInfo = logic.updateDocInfo(dbConn, fileForm, person, Integer.parseInt(seqId));
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "修改发文登记：" + docsendInfo.toString() ,person.getSeqId(), request.getRemoteAddr());
      
      //直接发送公文
      if("1".equals(sendFlag)){
        msg = logic.sendDoc(dbConn, Integer.parseInt(seqId), request, null);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    if("2".equals(sendFlag)){
      YHDocSendStampLogic logic = new YHDocSendStampLogic();
      logic.startStamp(dbConn, seqId, person, request, false);
      response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/stamp/stamp.jsp?seqId="+seqId+"&flag=1&flagClose=2");
    }
    else if("1".equals(sendFlag)){
      if(msg == null){
        msg = "";
      }
      response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/newRemind.jsp?sendFlag="+sendFlag+"&msg="+URLEncoder.encode(msg,"UTF-8")+"&seqId="+seqId);
    }
    else{
      response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/modify.jsp?seqId="+seqId);
    }
    return null;
  }
  
  /**
   * 修改发文登记--签发--暂未使用

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateDocInfoSign(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String contexPath = request.getContextPath();
    String sendFlag = request.getParameter("sendFlag");
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      dbConn = requestDbConn.getSysDbConn();
      
      YHDocSendLogic logic = new YHDocSendLogic();
      YHJhDocsendInfo docsendInfo = logic.updateDocInfoSign(dbConn, fileForm, person, Integer.parseInt(seqId));
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "签发发文登记：" + docsendInfo.toString() ,person.getSeqId(), request.getRemoteAddr());
      
      //直接发送公文
      if("1".equals(sendFlag)){
        logic.sendDoc(dbConn, Integer.parseInt(seqId), request, null);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    response.sendRedirect(contexPath + "/subsys/jtgwjh/sendDoc/sign/signRemind.jsp?sendFlag="+sendFlag);
    return null;
  }
  
  public String createAttachment(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String newName = request.getParameter("newName");
      String seqId = request.getParameter("seqId");
      
      YHDocSendLogic logic = new  YHDocSendLogic();
      String realPath = request.getRealPath("/");
      String attachment = logic.createAttachment(newName , dbConn, realPath, loginUser, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功 ");
      request.setAttribute(YHActionKeys.RET_DATA, attachment);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}

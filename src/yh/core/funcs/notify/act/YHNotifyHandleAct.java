package yh.core.funcs.notify.act;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.news.data.YHNews;
import yh.core.funcs.notify.data.YHNotify;
import yh.core.funcs.notify.logic.YHNotifyManageLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHNotifyHandleAct {
  private static Logger log = Logger.getLogger(YHNotifyHandleAct.class);
  private YHSysLogLogic logLogic = new YHSysLogLogic();
  private YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
  public String beforeAddNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      String data = notifyManageLogic.beforeAddnotify(dbConn,person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  /**
 * add by zyy 张银友
 * @param request
 * @param response
 * @return
 * @throws Exception
 * 从工作流中添加通知公告
 */
public String addFlowNotify(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    String urlAdd = request.getParameter("urlAdd"); 
	   
	    String subjectFont = request.getParameter("subjectFont");
	    String subject = request.getParameter("subject"); 
	    String publish = request.getParameter("publish");    
	
	 
	    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
	      Map<String,String[]> requestMap=request.getParameterMap();
	      String imgPath = YHWorkFlowUtility.getImgPath(request);
	      notifyManageLogic.saveFlowNotify(dbConn, requestMap,person,imgPath);
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      if("0".equals(publish)){
	        request.setAttribute(YHActionKeys.RET_MSRG, "公告通知保存成功");
	      }
	      if("1".equals(publish)){
	        request.setAttribute(YHActionKeys.RET_MSRG, "公告通知发布成功");
	      }
	      if("2".equals(publish)){
	        request.setAttribute(YHActionKeys.RET_MSRG, "公告通知提交审批成功");
	      }
	    } catch (Exception ex) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
	      throw ex;
	    }
	    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
	    return "/core/funcs/notify/manage/notifySaveOk.jsp?publish="+publish;
	  }
  
  
  public String addNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String urlAdd = request.getParameter("urlAdd"); 
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map paramMap = fileForm.getParamMap(); 
    String subjectFont = fileForm.getParameter("subjectFont");
    String subject = fileForm.getParameter("subject"); 
    String flag = (String)paramMap.get("publish");    
    if("2".equalsIgnoreCase(flag)){
      paramMap.put("subject", URLDecoder.decode(subject, YHConst.DEFAULT_CODE));
      paramMap.put("content", URLDecoder.decode((String)paramMap.get("content"), YHConst.DEFAULT_CODE));
    }   
    
    if(paramMap.get("print")==null){
      paramMap.put("print", "0");
    }
    if(paramMap.get("download") == null){
      paramMap.put("download", "0");
    }
    String publish = null;
    try{
      publish = ((String[]) paramMap.get("publish"))[0];
    }catch(Exception e){
      publish = (String) paramMap.get("publish");
    }
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      notifyManageLogic.saveMailLogic(dbConn, fileForm,person,YHSysProps.getAttachPath(), publish, subjectFont);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      if("0".equals(publish)){
        request.setAttribute(YHActionKeys.RET_MSRG, "公告通知保存成功");
      }
      if("1".equals(publish)){
        request.setAttribute(YHActionKeys.RET_MSRG, "公告通知发布成功");
      }
      if("2".equals(publish)){
        request.setAttribute(YHActionKeys.RET_MSRG, "公告通知提交审批成功");
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
    }
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/core/funcs/notify/manage/notifySaveOk.jsp?publish="+publish;
  }
  
  /**
   * 附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String fileLoad(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PrintWriter pw = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      StringBuffer sb = notifyManageLogic.uploadMsrg2Json(fileForm);
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
    try{
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      Map<String, String> attr = null;
      String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
      String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
      String data = "";
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      attr = notifyManageLogic.fileUploadLogic(fileForm);
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
        attrId += key + ",";
        attrName += value + "*";
      }
      data = "{attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch (SizeLimitExceededException ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败：文件需要小于" + YHSysProps.getInt(YHSysPropKeys.MAX_UPLOAD_FILE_SIZE) + "兆");
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
    }
    return "/core/inc/rtuploadfile.jsp";
  }
  /**
   * 存草稿
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveNotifyByUp(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String urlAdd = request.getParameter("urlAdd");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    int bId = -1 ;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      bId = notifyManageLogic.savettachMailLogic(conn, fileForm,person.getSeqId(),YHSysProps.getAttachPath());
      request.setAttribute("bId", bId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "公告通知保存成功！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "公告通知保存失败！" + e.getMessage());
      e.printStackTrace();
    }
    return "/core/funcs/notify/manage/notifyAdd.jsp?seqId="+ bId;
  }
  
  //分页取出公告列表数据
  public String getnotifyManagerList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
    Connection dbConn = null;
    String data = "";
    String type = request.getParameter("type");//下拉框中类型
    String ascDesc = request.getParameter("ascDesc");//升序还是降序
    String field = request.getParameter("field");//排序的字段

    String showLenStr = request.getParameter("showLength");//每页显示长度
    String pageIndexStr = request.getParameter("pageIndex");//页码数    if(pageIndexStr == null || pageIndexStr==""){
      pageIndexStr ="1";
    }

    if("".equals(ascDesc)) {
      ascDesc = "1";
    }
    YHPerson loginUser = null;
    loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      data = notifyManageLogic.getnotifyManagerList(dbConn, loginUser, type,
             ascDesc, field, Integer.parseInt(showLenStr), Integer.parseInt(pageIndexStr));
      //YHOut.println(data+"****************");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出公告数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteAllNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String loginUserPriv = person.getUserPriv();
    String postPriv = person.getPostPriv();
    int loginUserId = person.getSeqId();
    String ip = request.getRemoteAddr();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      boolean success =notifyManageLogic.deleteAllNotify(dbConn, Integer.toString(loginUserId), loginUserPriv, postPriv,ip);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  public String deleteCheckNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String pageIndex = request.getParameter("pageIndex");
    String showLength = request.getParameter("showLength");
    String deleteStr = request.getParameter("delete_str");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String loginUserPriv = person.getUserPriv();
    String postPriv = person.getPostPriv();
    int loginUserId = person.getSeqId();
    String ip = request.getRemoteAddr();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      boolean success =notifyManageLogic.deleteCheckNotify(dbConn, Integer.toString(loginUserId), loginUserPriv, postPriv, deleteStr,ip);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String data = "";
//    int pageIndex = 1;
//    int showLength = 10;
    String beginDate = request.getParameter("beginDate");
    String endDate = request.getParameter("endDate");
    String stat = request.getParameter("stat");
    if(stat == null || stat.length() <1){
      stat = "";
    }
    YHNotify notify = (YHNotify)YHFOM.build(request.getParameterMap());
//    String pageIndexStr = request.getParameter("pageIndex");
//    String showLengthStr = request.getParameter("showLength");
//    if(!"".equals(pageIndexStr)&&pageIndexStr!=null){
//      pageIndex = Integer.parseInt(pageIndexStr);
//    }
//    if(!"".equals(showLengthStr)&&showLengthStr!=null){
//      showLength = Integer.parseInt(showLengthStr);
//    }
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyMangerLogic = new YHNotifyManageLogic();
      data = notifyMangerLogic.queryNotify(dbConn,notify,person,beginDate,endDate,stat);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage().substring(ex.getMessage().lastIndexOf(":")+1,ex.getMessage().length()),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String editNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHNotify notify = null;
    String data = "";
    String seqId = request.getParameter("seqId");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Statement st = null;
      ResultSet rs = null;
      YHORM orm = new YHORM();
      notify = (YHNotify)orm.loadObjSingle(dbConn, YHNotify.class, Integer.parseInt(seqId));
      
      if (notify == null) {
        notify = new YHNotify();
      }else if (!YHUtility.null2Empty(notify.getFormat()).equals("2")) {
        byte[] byteContent = notify.getCompressContent();
        if (byteContent == null) {
          notify.setContent("");
        }else {
          notify.setContent(new String(byteContent, "UTF-8"));
        }
      }
      
      data = notify.toJSON();
//      StringBuffer sb = new StringBuffer(data);
//      sb.deleteCharAt(data.length()-1);
//      sb.append(",attachList:[");
//      String attachmentId = notify.getAttachmentId();
//      String attachmentName = notify.getAttachmentName();
//      List attachmentList = new ArrayList();
//      if(!"".equals(attachmentId)&&attachmentId!=null) {
//        String[] attachmentIds = attachmentId.split(",");
//        for(int i=0;i<attachmentIds.length;i++) {
//          attachmentList.add(attachmentIds[i]);
//        }
//      }
//      String[] attachmentNames = null;
//      if(!"".equals(attachmentName)&&attachmentName!=null) {
//        attachmentNames = attachmentId.split(",");
//        for(int i=0;i<attachmentNames.length;i++) {
//          sb.append("{");
//          sb.append("attachmentId:\"" + attachmentList.get(i) + "\"");
//          sb.append(",fattachmentName:\"" + attachmentNames[i] + "\"");
//          sb.append("},");
//        }
//      }
//      if(attachmentNames.length>0) {  
//        sb.deleteCharAt(sb.length() - 1); 
//        }
//       sb.append("]");
//      sb.append("}");
//      data = sb.toString();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String downPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      InputStream in = null;
      OutputStream out = null;
      try{
        String path = request.getParameter("path");
        response.setContentType("application/octet-stream");
        response.setHeader("Cache-Control","maxage=3600");
        response.setHeader("Pragma","public");
        response.setHeader("Content-Disposition", "attachment;filename=" + YHFileUtility.getFileName(path));
        in = new FileInputStream(path);
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
      return null;
  }
  
/**
 * 改变公告状态，终止，生效等
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String changeState(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
    String seqId = request.getParameter("seqId");//seqId
    String operation = request.getParameter("operation");//操作
    String showLenStr = request.getParameter("showLength");//每页显示长度
    String pageIndexStr = request.getParameter("pageIndex");//页码数    if(pageIndexStr == null || pageIndexStr==""){
      pageIndexStr ="1";
    }

//    String loginUserId = request.getParameter("loginUserId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    boolean success = false;
     loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request
           .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       success = notifyManageLogic.changeState(dbConn, loginUser,seqId,operation);
    
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG,"终止生效状态已修改");
  } catch (Exception ex) {
    String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
    throw ex;
  }
  String forward = YHUtility.null2Empty(request.getParameter(YHActionKeys.RET_METHOD_FORWARD));
  if (forward.equals("rtJson")) {
    return "/core/inc/rtjson.jsp";
  }
  return "/core/funcs/notify/manage/notifyList.jsp";
}
  
  public String changeStateGroup(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
    String delete_str = request.getParameter("delete_str");//seqId
    String operation = request.getParameter("operation");//操作

//    String loginUserId = request.getParameter("loginUserId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    boolean success = false;
     loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn) request
           .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       success = notifyManageLogic.changeStateGroup(dbConn, loginUser,delete_str,operation);
    
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG,"终止生效状态已修改");
  } catch (Exception ex) {
    String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
    throw ex;
  }
  //return "/core/funcs/dept/deptinput.jsp";
  //?deptParentDesc=+deptParentDesc
  return "/core/inc/rtjson.jsp";
  }
  
  public String cancelTop(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String pageIndex = request.getParameter("pageIndex");
    String showLength = request.getParameter("showLength");
    String deleteStr = request.getParameter("delete_str");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String loginUserPriv = person.getUserPriv();
    String postPriv = person.getPostPriv();
    int loginUserId = person.getSeqId();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      notifyManageLogic.cancelTop(dbConn, Integer.toString(loginUserId), loginUserPriv, postPriv, deleteStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取消置顶");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String getNoteById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      String data = notifyManageLogic.getNoteById(dbConn,person,Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 点击标题，查看
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showObject(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      String data = notifyManageLogic.showObject(dbConn,person,Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String getnotifyType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHNews news = null;
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int typeNum = 0;
      String getTypeSql = "select SEQ_ID,CLASS_DESC from oa_kind_dict_item where CLASS_NO='NOTIFY'";
      Statement typeSt = dbConn.createStatement();
      ResultSet typeRs = typeSt.executeQuery(getTypeSql);
      sb.append("typeData:[");
      while(typeRs.next()){
        typeNum ++;
        sb.append("{");
        sb.append("typeId:\"" + typeRs.getInt("SEQ_ID") + "\"");
        sb.append(",typeDesc:\"" + typeRs.getString("CLASS_DESC") + "\"");
        sb.append("},");
      }
      if(typeNum >0) {
        sb.deleteCharAt(sb.length() - 1); 
        }
      sb.append("]");
      sb.append("}");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 浮动菜单文件删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String attachId = request.getParameter("attachId");
    String attachName = request.getParameter("attachName");
    String sSeqId = request.getParameter("seqId");
    //YHOut.println(sSeqId);
    if (attachId == null) {
      attachId = "";
    }
    if (attachName == null) {
      attachName = "";
    }
    int seqId = 0 ;
    if (sSeqId != null && !"".equals(sSeqId)) {
      seqId = Integer.parseInt(sSeqId);
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requesttDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();

      boolean updateFlag = notifyManageLogic.delFloatFile(dbConn, attachId, attachName , seqId);
      if(updateFlag){
        YHSysLogLogic.addSysLog(dbConn, "15", "删除附件，附件名称:"+attachName, person.getSeqId(), logLogic.getIpAddr(request));
      }
      String isDel="";
      if (updateFlag) {
        isDel ="isDel"; 

      }
      String data = "{updateFlag:\"" + isDel + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }

    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 导出到excel
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toExcel(HttpServletRequest request, HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    OutputStream ops = null;
    try{
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String stat = request.getParameter("stat");
      if(stat == null || stat.length() <1){
        stat = "";
      }
      YHNotify notify = (YHNotify)YHFOM.build(request.getParameterMap());    
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      //YHOut.println(person.getSeqId());
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyMangerLogic = new YHNotifyManageLogic();
      List<Map<String, String>> notifys = notifyMangerLogic.toExcel(dbConn,notify,person,beginDate,endDate,stat);
      String fileName = URLEncoder.encode("公告.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream(); 
      ArrayList<YHDbRecord > dbL = notifyMangerLogic.convertList(notifys);
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      ops.close();
    }
    return null;
  }
  
  /**
   * 删除选择的公告
   * @param request
   * @param response
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public String deleteSelNotify(HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      try{
        dbConn = requestDbConn.getSysDbConn();    
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String stat = request.getParameter("stat");
        if(stat == null || stat.length() <1){
          stat = "";
        }
        YHNotify notify = (YHNotify)YHFOM.build(request.getParameterMap());
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        notifyManageLogic.deleteSelNotify(dbConn, notify, person, beginDate, endDate, stat);    
        int count = notifyManageLogic.getCount();
        request.setAttribute("count",count);
      } catch (Exception ex){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());  
        request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
        throw ex;
      }      
      return "/core/funcs/notify/manage/msg.jsp";
  }
  
  /**
   * 查看最大的系统设置的置顶时间
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getManageTopDays(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHNotifyManageLogic notifyManageLogic = new YHNotifyManageLogic();
      int topDays = notifyManageLogic.getNotifyTopDay(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, topDays);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}

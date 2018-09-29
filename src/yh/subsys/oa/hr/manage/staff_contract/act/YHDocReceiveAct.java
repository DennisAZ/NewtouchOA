package yh.subsys.oa.hr.manage.staff_contract.act;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.hr.manage.staff_contract.data.YHDocConst;
import yh.subsys.oa.hr.manage.staff_contract.data.YHDocReceive;
import  yh.subsys.oa.hr.manage.staff_contract.logic.YHDocSmsLogic;
import  yh.subsys.oa.hr.manage.staff_contract.logic.YHPublicFileUploadLogic;

/**
 * 收文管理
 * @author Administrator
 *
 */
public class YHDocReceiveAct{
  
  public String gotoIndex(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    request.setAttribute("user", user);
    return "/subsys/inforesource/docmgr/docreceve/newdoc.jsp";
  }
  
  public String gotoReadIndex(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    request.setAttribute("user", user);
    return "/subsys/inforesource/docmgr/docreceve/readdocindex.jsp";
  }
  
  /**
   * 新建收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String addYHDocReceive(HttpServletRequest request, HttpServletResponse response) throws Exception{
    
	  YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHDocReceive doc = new YHDocReceive();
      String ftype = request.getParameter("ftype");
      String docNo = request.getParameter("docNo");
      String resDate = request.getParameter("startTime");
      String fromUnits = request.getParameter("fromUnits");
      String oppDocNo = request.getParameter("oppDocNo");
      String title = request.getParameter("title");
      String copies = request.getParameter("copies");
      String confLevel = request.getParameter("confLevel");
      String instruct = request.getParameter("instruct");
      String recipient = request.getParameter("recipient");
      String docType = request.getParameter("docType");
      String sponsor = request.getParameter("sponsor");
      String personId = request.getParameter("user");
      String alarm = request.getParameter("alarm");
      String attachName = request.getParameter("attachmentName");
      String attachId = request.getParameter("attachmentId");

      int userId = Integer.parseInt(personId);
      doc.setDocNo(docNo);
      doc.setResDate(YHUtility.parseDate(resDate));
      doc.setFromUnits(fromUnits);
      doc.setOppDocNo(oppDocNo);
      doc.setTitle(title);
      doc.setCopies(Integer.parseInt(copies));
      doc.setConfLevel(Integer.parseInt(confLevel));
      doc.setInstruct(instruct);
      doc.setRecipient(recipient);
      doc.setDocType(Integer.parseInt(docType));
      doc.setSponsor(sponsor);
      doc.setUserId(userId);
      doc.setAttachNames(attachName);
      doc.setAttachIds(attachId);
      if("1".equalsIgnoreCase(ftype)){
        doc.setSendStauts(0);
      }else{
        doc.setSendStauts(1);
      }
      docLogic.insertBeanChYHDocReceive(dbConn, doc);
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String content = user.getUserName() + "提醒：请签收您的收文!收文文号:" + docNo;  
      String url =  gotoReadIndex(request, response);
      if(!YHUtility.isNullorEmpty(alarm)){
        YHDocSmsLogic.sendSms(user, dbConn, content, url, recipient, null);
      }
      request.setAttribute("msg", "新建成功！");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
  }
  
  /**
   * (已)未发收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String faDocReceive(HttpServletRequest request, HttpServletResponse response) throws Exception{
	  YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      dbConn = requestDbConn.getSysDbConn();
      String ftype  = request.getParameter("ftype");
      int typeId = 0;
      if(YHUtility.isNullorEmpty(ftype)){
        typeId = 0;    //未发收文
      }else{
        typeId = Integer.parseInt(ftype);  //已发收文
      }
      List<YHDocReceive>docs =  docLogic.faDocReceive(dbConn,  user, typeId);
      if(docs.size() == 0){
        request.setAttribute("msg", "没有符合的记录!");
        return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
      }
      request.setAttribute("docs", docs);
      request.setAttribute("ftype", ftype);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    } 
    return "/subsys/inforesource/docmgr/docreceve/havedoc.jsp";
  }
  
  /**
   * 未分法的更新为分发的
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateDocReceive(HttpServletRequest request, HttpServletResponse response)throws Exception{
	  YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      int keyId = Integer.parseInt(seqId);
      int ok = docLogic.updateDocReceive(dbConn, keyId);
      if(ok == 0){
        request.setAttribute("msg", "确认签收失败");
        return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    request.setAttribute("msg", "确认签收成功");
    return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
  }
  
  /**
   * (已)未阅读收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String readDocReceive(HttpServletRequest request, HttpServletResponse response) throws Exception{
	  YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      dbConn = requestDbConn.getSysDbConn();
      String ftype  = request.getParameter("ftype");
      int typeId = 0;
      if(YHUtility.isNullorEmpty(ftype)){
        typeId = 0;    //未发收文
      }else{
        typeId = Integer.parseInt(ftype);  //已发收文
      }
      List<YHDocReceive>docs =  docLogic.myReadDocReceive(dbConn, user, typeId);
      if(docs.size() == 0){
        request.setAttribute("msg", "没有符合的记录!");
        return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
      }
      request.setAttribute("docs", docs);
      request.setAttribute("ftype", ftype);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    } 
    return "/subsys/inforesource/docmgr/docreceve/writedoc.jsp";
  }
  
  
  /**
   * 未分法的更新为分发的(确认签收)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateReadDocReceive(HttpServletRequest request, HttpServletResponse response)throws Exception{
	  YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      int keyId = Integer.parseInt(seqId);
      int ok = docLogic.updateReadDocReceive(dbConn, keyId);
      if(ok == 0){
        request.setAttribute("msg", "签阅失败");
        return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    request.setAttribute("msg", "签阅成功");
    return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
  }
  
  /**
   * 提醒签收
   * @param request
   * @param response
   * @return
   */
  public String alarmToRead(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String toId = request.getParameter("toId");
      String docNo = request.getParameter("docNo");
      docNo = URLDecoder.decode(docNo, YHConst.DEFAULT_CODE);
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String content = user.getUserName() + "提醒：请签收您的收文!收文文号:" + docNo;  
      String url =  gotoReadIndex(request, response);
      YHDocSmsLogic.sendSms(user, dbConn, content, url, toId, null);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 批量确认签收
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String beanChConfirm(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
    try{
      dbConn = requestDbConn.getSysDbConn();
      String[] seqId = request.getParameterValues("selAll");
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      docLogic.beanChConfirm(dbConn, user, seqId);
      request.setAttribute("msg", "批量确认签收成功");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
  }
  
  /**
   * 批量提醒
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String beanchAlarm(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      String[] seqId = request.getParameterValues("selAll");
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      List<YHDocReceive> docs = docLogic.beanChAlarm(dbConn,  seqId);
      if(docs != null){
        for(int i=0; i<docs.size(); i++){
          String content = user.getUserName() + "提醒：请签收您的收文!收文文号:" + docs.get(i).getDocNo();  
          String url =  gotoReadIndex(request, response);
          YHDocSmsLogic.sendSms(user, dbConn, content, url, docs.get(i).getRecipient(), null);
        }
      }
      if(docs == null || docs.size() == 0){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute("msg", "没有需要提醒的人");
      }
      request.setAttribute("msg","批量提醒签收人成功");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/subsys/inforesource/docmgr/docreceve/msgBox.jsp";
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

      YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();

      boolean updateFlag = docLogic.delFloatFile(dbConn, attachId, attachName , seqId);
     
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
   * 单文件附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> attr = null;
    String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
    String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
    String data = "";
    try{
    	YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
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
      data = "{attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      //YHOut.println(data);
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
  
}

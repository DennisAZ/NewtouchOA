package yh.core.funcs.doc.receive.act;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.receive.data.YHDocReceive;
import yh.core.funcs.doc.receive.logic.YHDocReceiveLogic;
import yh.core.funcs.doc.receive.logic.YHDocSmsLogic;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;

/**
 * 收文管理
 * @author Administrator
 *
 */
public class YHDocReceiveAct{
  
  public String gotoIndex(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    request.setAttribute("user", user);
    return "/core/funcs/doc/receive/newdoc.jsp";
  }
  
  public String gotoReadIndex(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    request.setAttribute("user", user);
    return "/core/funcs/doc/receive/readdocindex.jsp";
  }
  
  /**
   * 新建收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String addYHDocReceive(HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
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
      String sponsor = request.getParameter("deptId");
      String personId = request.getParameter("user");
      String alarm = request.getParameter("alarm");
      String attachName = request.getParameter("attachmentName");
      String attachId = request.getParameter("attachmentId");

      int userId = Integer.parseInt(personId);
      doc.setDocNo(docNo);
      doc.setResDate(YHUtility.parseDate(resDate));
      doc.setFromUnits(fromUnits);
      doc.setOppdocNo(oppDocNo);
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
    return "/core/funcs/doc/receive/msgBox.jsp";
  }
  
  /**
   * (已)未发收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String faDocReceive(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      dbConn = requestDbConn.getSysDbConn();
      String ftype  = request.getParameter("ftype");
      String column = request.getParameter("colum");  //需要排序的列
      String asc = request.getParameter("asc");      //升序还是降序
      int typeId = 0;
      if(YHUtility.isNullorEmpty(ftype)){
        typeId = 0;    //未发收文
      }else{
        typeId = Integer.parseInt(ftype);  //已发收文
      }
      List<YHDocReceive> docs =  docLogic.faDocReceive(dbConn,  user, typeId, column, asc);
      if(docs.size() == 0){
        request.setAttribute("msg", "没有符合的记录!");
        return "/core/funcs/doc/receive/msgBox.jsp";
      }
      request.setAttribute("docs", docs);
      request.setAttribute("ftype", ftype);
      request.setAttribute("column", column);
      request.setAttribute("asc", asc);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e; 
    } 
    return "/core/funcs/doc/receive/havedoc.jsp";
  }
  /**
   * (已)未发收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String faDocReceive1(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      dbConn = requestDbConn.getSysDbConn();
      String ftype  = request.getParameter("ftype");
      String column = request.getParameter("colum");  //需要排序的列

      String asc = request.getParameter("asc");      //升序还是降序
      int typeId = 0;
      if(YHUtility.isNullorEmpty(ftype)){
        typeId = 0;    //未发收文
      }else{
        typeId = Integer.parseInt(ftype);  //已发收文
      }
      List<YHDocReceive> docs =  docLogic.faDocReceive1(dbConn,  user, typeId, column, asc);
      if(docs.size() == 0){
        request.setAttribute("msg", "没有符合的记录!");
        return "/core/funcs/doc/receive/msgBox.jsp";
      }
      request.setAttribute("docs", docs);
      request.setAttribute("ftype", ftype);
      request.setAttribute("column", column);
      request.setAttribute("asc", asc);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e; 
    } 
    return "/core/funcs/doc/receive/havedoc.jsp";
  }
  
  /**
   * 未分法的更新为分发的
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateDocReceive(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String userId = request.getParameter("userId");//签收人id
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      if(YHUtility.isNullorEmpty(userId)){
        userId = "0";
      }
      int keyId = Integer.parseInt(seqId);
      int wUserId = Integer.parseInt(userId);
      int ok = docLogic.updateDocReceive(dbConn, keyId, wUserId);
      if(ok == 0){
        request.setAttribute("msg", "确认签收失败");
        return "/core/funcs/doc/receive/msgBox.jsp";
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    request.setAttribute("msg", "确认签收成功");
    return "/core/funcs/doc/receive/msgBox.jsp";
  }
  
  /**
   * (已)未阅读收文
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String readDocReceive(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
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
        return "/core/funcs/doc/receive/msgBox.jsp";
      }
      request.setAttribute("docs", docs);
      request.setAttribute("ftype", ftype);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    } 
    return "/core/funcs/doc/receive/writedoc.jsp";
  }
  public String readDocReceive1(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      dbConn = requestDbConn.getSysDbConn();
      String ftype  = request.getParameter("ftype");
      List<YHDocReceive>docs =  docLogic.myReadDocReceive1(dbConn, user, ftype);
      if(docs.size() == 0){
        request.setAttribute("msg", "没有符合的记录!");
        return "/core/funcs/doc/receive/msgBox.jsp";
      }
      String webroot = request.getRealPath("/");
      YHDocUtility util =  new YHDocUtility();
      String str = YHDocConst.getProp(webroot  , YHDocConst.DOC_RECEIVE_FLOW_SORT) ;
      String sortId = util.getSortIds(str, dbConn);
      Map map = util.getFlowBySortIds(sortId, dbConn  , user);
      request.setAttribute("sortId", sortId);
      request.setAttribute("flowType", map);
      request.setAttribute("docs", docs);
      request.setAttribute("ftype", ftype);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    } 
    return "/core/funcs/doc/receive/writedoc.jsp";
  }
  
  /**
   * 未分法的更新为分发的(确认签收)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateReadDocReceive(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
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
        return "/core/funcs/doc/receive/msgBox.jsp";
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    request.setAttribute("msg", "签阅成功");
    return "/core/funcs/doc/receive/msgBox.jsp";
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
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
    try{
      dbConn = requestDbConn.getSysDbConn();
     
      String seqIds = request.getParameter("seqIds");
      String userIds = request.getParameter("userIds");
      String[] seqId = seqIds.split(",");
      String[] userId = userIds.split(",");
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      docLogic.beanChConfirm(dbConn, seqId, userId);
      request.setAttribute("msg", "批量确认签收成功");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/funcs/doc/receive/msgBox.jsp";
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
    YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
    
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
    return "/core/funcs/doc/receive/msgBox.jsp";
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

      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();

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
  
  /**
   * 查找一个批办单
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findDocFileAjax(HttpServletRequest request, HttpServletResponse response)throws Exception{
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      int id = Integer.parseInt(seqId);
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String webroot = request.getRealPath("/");
      String docJson = docLogic.myDocReceiveJson(dbConn, user, id , webroot);
      String deptName = docLogic.getDeptName(dbConn, user.getDeptId());
      StringBuffer sb = new StringBuffer();
      sb.append("[");
        sb.append(docJson).append(",");
        sb.append("{deptName:").append("'").append(YHUtility.encodeSpecial(deptName)).append("'}");
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
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
      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
      dbConn = requestDbConn.getSysDbConn();
      String typeId = request.getParameter("typeId");
      int id = Integer.parseInt(typeId);
      List<Integer> ints = docLogic.getMaxOrderNo(dbConn, id);
      int max = docLogic.getMaxOrderNo(ints);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, String.valueOf(max));
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 修改收文
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String editDoc(HttpServletRequest request, HttpServletResponse response)throws Exception{
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      int id = Integer.parseInt(seqId);
      YHDocReceive doc = docLogic.getAdocById(dbConn, id);
      request.setAttribute("doc", doc);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/funcs/doc/receive/edit.jsp";
  }
  
  /**
   * 更新
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String update(HttpServletRequest request, HttpServletResponse response)throws Exception{
    
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
      Connection dbConn = null;
      dbConn = requestDbConn.getSysDbConn();
      YHDocReceive doc = new YHDocReceive();
      String docNo = request.getParameter("docNo");
      String seqId = request.getParameter("docId");
      //String resDate = request.getParameter("startTime");
      String fromUnits = request.getParameter("fromUnits");
      String oppDocNo = request.getParameter("oppDocNo");
      String title = request.getParameter("title");
      String copies = request.getParameter("copies");
      String confLevel = request.getParameter("confLevel");
      String instruct = request.getParameter("instruct");
     // String recipient = request.getParameter("recipient");
      String docType = request.getParameter("docType");
      String sponsor = request.getParameter("deptId");
      String personId = request.getParameter("user");
      //String alarm = request.getParameter("alarm");
      String attachName = request.getParameter("attachmentName");
      String attachId = request.getParameter("attachmentId");

      int userId = Integer.parseInt(personId);
      doc.setSeq_id(Integer.parseInt(seqId));
      doc.setDocNo(docNo);
      //doc.setResDate(YHUtility.parseDate(resDate));
      doc.setFromUnits(fromUnits);
      doc.setOppdocNo(oppDocNo);
      doc.setTitle(title);
      doc.setCopies(Integer.parseInt(copies));
      doc.setConfLevel(Integer.parseInt(confLevel));
      doc.setInstruct(instruct);
      //doc.setRecipient(recipient);
      doc.setDocType(Integer.parseInt(docType));
      doc.setSponsor(sponsor);
      doc.setUserId(userId);
      doc.setAttachNames(attachName);
      doc.setAttachIds(attachId);
      
      //docLogic.insertBeanChYHDocReceive(dbConn, doc);
      docLogic.update(dbConn, doc);
      request.setAttribute("msg", "修改成功！");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    return "/core/funcs/doc/receive/openMsg.jsp";
  }
  
  /**
   * 撤销（清空签收人和承办处，签收状态变为未签收状态）
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String cancel(HttpServletRequest request, HttpServletResponse response)throws Exception{
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
      Connection dbConn = null;
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      int id = Integer.parseInt(seqId);
      docLogic.updateStatus(dbConn, id);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "退回成功");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 打印
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String print(HttpServletRequest request, HttpServletResponse response)throws Exception{
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHDocReceiveLogic  docLogic = new YHDocReceiveLogic();
      Connection dbConn = null;
      dbConn = requestDbConn.getSysDbConn();
      String printIds = request.getParameter("printIds");
      List<YHDocReceive>docs = null;
      if(YHUtility.isNullorEmpty(printIds)){
        printIds = "0";
      }else{
       docs =  docLogic.printDocs(dbConn, printIds);
      }
      request.setAttribute("docs", docs);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e; 
    }
    return "/core/funcs/doc/receive/print.jsp";
  }
  public String getUserByDept(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptStr = request.getParameter("deptId");
      int deptId = Integer.parseInt(deptStr);
      StringBuffer data = this.deptUser2Json(dbConn, deptId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 组装成json数据（包含当前部门）
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  public StringBuffer deptUser2Json(Connection conn, int deptId ) throws Exception{
    String deptName = "";
    if(deptId == 0){
      deptName = "全体部门";
    }else{
      deptName =  getDeptNameById(conn, deptId);
    }
    StringBuffer result = new StringBuffer();
    result.append("[").append(deptUser2Json(conn, deptId,deptName,0)).append("]");
    return result;
  }
  public String getDeptNameById(Connection conn, int deptId ) throws Exception{
    String query = "select DEPT_NAME from oa_department where SEQ_ID=" + deptId;
    Statement stm4 = null;
    ResultSet rs4 = null;
    String deptName = "";
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if (rs4.next()) {
        deptName = rs4.getString("DEPT_NAME");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return deptName;
  }
  /**
   * 组装成json数据（包含当前部门）
   * @param conn
   * @param deptId
   * @param deptName
   * @return
   * @throws Exception
   */
  public StringBuffer deptUser2Json(Connection conn, int deptId , String deptName,int childNum) throws Exception{
    StringBuffer sb = new StringBuffer();
    YHOrgSelect2Logic logic =new YHOrgSelect2Logic();
    StringBuffer users = new StringBuffer();
    ArrayList<YHPerson> persons = logic.getDeptUser(conn, deptId , false);
    sb.append("{")
    .append("deptName:\"").append(logic.deptNameRender(deptName, childNum)).append("\"").append(",user:[");
    for (int i = 0; i < persons.size(); i++) {
      YHPerson person = persons.get(i);
      int isOnline = logic.isUserOnline(conn, person.getSeqId());
      String userNameRender = YHUtility.encodeSpecial(person.getUserName()) ;
      if(!"".equals(users.toString())){
        users.append(",");
      }
      users.append("{userId:\"").append(person.getSeqId()).append("\"")
        .append(",userName:\"").append(userNameRender).append("\"")
        .append(",isOnline:\"").append(isOnline).append("\"")
        .append("}");
    }
    sb.append(users).append("]}");
    return sb;
  }
  
}

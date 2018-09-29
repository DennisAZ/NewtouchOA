package yh.core.funcs.message.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.message.data.YHMessage;
import yh.core.funcs.message.data.YHMessageBody;
import yh.core.funcs.message.logic.YHMessageLogic;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.ispirit.n12.org.logic.YHIsPiritLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHMessageAct {
  private static Logger log = Logger.getLogger(YHMessageAct.class);
  YHMessageLogic messageLogic = null;
  
  public String addMessageBody(HttpServletRequest request,
      HttpServletResponse response) throws Exception  {
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int userId = person.getSeqId();
    String toId = request.getParameter("user");
    String content = request.getParameter("content");
    String sendTime = request.getParameter("sendTime");
    String MessageType = request.getParameter("messageType");
    String remindUrl = request.getParameter("remindUrl");

    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      int fromId = userId;//"liuhan";
      YHMessageBody messageBody = new YHMessageBody();
      messageBody.setFromId(fromId);
      messageBody.setContent(content);
      Date time = null;
      if(sendTime == null || "".equals(sendTime)){
        time = new Date();
      }else {
        try{
          time = YHUtility.parseDate(sendTime);
        } catch (Exception e){
          time = new Date();
        }
      }
      if(remindUrl != null && !"".equals(remindUrl)){
        messageBody.setRemindUrl(remindUrl);
      }else{
        messageBody.setRemindUrl("");
      }
      messageBody.setSendTime(time);
      ArrayList<YHMessage> messageList = new ArrayList<YHMessage>();
      YHMessage message = null;
      
      String flag = "1";  //标记为2  表示没有阅读的
      String delFlag = "0";
      if("0".equals(toId) || "ALL_DEPT".equals(toId)){
        toId = YHOrgSelectLogic.getAlldept(dbConn);
      }
      String[] userIds = toId.split(",");
      String extendFlagStr = YHSysProps.getProp("$Message_DELAY_PER_ROWS");
      String extendTimeStr = YHSysProps.getProp("$Message_DELAY_SECONDS");
      long curTimeL = time.getTime();
      int extendTime = 0;
      int extendFlag = 0;
      Date remindDate = time;
      if (YHUtility.isInteger(extendTimeStr)) {
        extendTime = Integer.valueOf(extendTimeStr);
      }
      if (YHUtility.isInteger(extendFlagStr)) {
        extendFlag = Integer.valueOf(extendFlagStr);
      }
      for(int i = 0; i < userIds.length; i++) {
        message = new YHMessage();
        message.setToId(Integer.parseInt(userIds[i]));
        message.setRemindFlag(flag);
        message.setDeleteFlag(delFlag);
        if(i>0 && extendFlag != 0 && extendTime != 0 && (i % extendFlag) ==0 ){
          long remindTime = curTimeL + (i / extendFlag) * extendTime*1000;
          remindDate = new Date(remindTime);
        }
        message.setRemindTime(remindDate);
        messageList.add(message);
        
        //设置提醒
        if(YHUtility.isNullorEmpty(sendTime))
        YHIsPiritLogic.setUserMessageRemind(userIds[i]);
        
      }
      messageBody.setMessagelist(messageList);
//      if(){
//        
//      }
      messageBody.setMessageType(MessageType);
    
      orm.saveComplex(dbConn, messageBody);
    
      YHMsgPusher.pushSms(toId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加微讯");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      ex.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String addMessageBody1(HttpServletRequest request,
      HttpServletResponse response) throws Exception  {
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int userId = person.getSeqId();
    String toId = request.getParameter("user");
    String content = request.getParameter("content");
    String sendTime = request.getParameter("sendTime");
    String MessageType = request.getParameter("messageType");
    String remindUrl = request.getParameter("remindUrl");

    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      int fromId = userId;//"liuhan";
      YHMessageBody messageBody = new YHMessageBody();
      messageBody.setFromId(fromId);
      messageBody.setContent(content);
      Date time = null;
      if(sendTime == null || "".equals(sendTime)){
        time = new Date();
      }else {
        try{
          time = YHUtility.parseDate(sendTime);
        } catch (Exception e){
          time = new Date();
        }
      }
      if(remindUrl != null && !"".equals(remindUrl)){
        messageBody.setRemindUrl(remindUrl);
      }else{
        messageBody.setRemindUrl("");
      }
      messageBody.setSendTime(time);
      ArrayList<YHMessage> messageList = new ArrayList<YHMessage>();
      YHMessage message = null;
      
      String flag = "0";  //标记为2  表示没有阅读的

      String delFlag = "0";
      if("0".equals(toId) || "ALL_DEPT".equals(toId)){
        toId = YHOrgSelectLogic.getAlldept(dbConn);
      }
      String[] userIds = toId.split(",");
      String extendFlagStr = YHSysProps.getProp("$Message_DELAY_PER_ROWS");
      String extendTimeStr = YHSysProps.getProp("$Message_DELAY_SECONDS");
      long curTimeL = time.getTime();
      int extendTime = 0;
      int extendFlag = 0;
      Date remindDate = time;
      if (YHUtility.isInteger(extendTimeStr)) {
        extendTime = Integer.valueOf(extendTimeStr);
      }
      if (YHUtility.isInteger(extendFlagStr)) {
        extendFlag = Integer.valueOf(extendFlagStr);
      }
      for(int i = 0; i < userIds.length; i++) {
        message = new YHMessage();
        message.setToId(Integer.parseInt(userIds[i]));
        message.setRemindFlag(flag);
        message.setDeleteFlag(delFlag);
        if(i>0 && extendFlag != 0 && extendTime != 0 && (i % extendFlag) ==0 ){
          long remindTime = curTimeL + (i / extendFlag) * extendTime*1000;
          remindDate = new Date(remindTime);
        }
        message.setRemindTime(remindDate);
        messageList.add(message);
        
        //设置提醒
        if(YHUtility.isNullorEmpty(sendTime))
        YHIsPiritLogic.setUserMessageRemind(userIds[i]);
        
      }
      messageBody.setMessagelist(messageList);
//      if(){
//        
//      }
      messageBody.setMessageType(MessageType);
    
      orm.saveComplex(dbConn, messageBody);
    
      YHMsgPusher.pushSms(toId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加微讯");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      ex.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  
  public String showMessageBody(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      messageLogic = new YHMessageLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHMessageBody messageBody = null;
      YHORM orm = new YHORM();
      messageBody = (YHMessageBody)orm.loadObjSingle(dbConn, YHMessageBody.class, Integer.parseInt(seqId));
      messageLogic.updateFalg(dbConn, person.getSeqId(), Integer.parseInt(seqId));
      StringBuffer data = YHFOM.toJson(messageBody);
      //System.out.println(data.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出微讯内容！");
      request.setAttribute(YHActionKeys.RET_DATA,data.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteMessage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");  
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
     // Map map = new HashMap();
     // map.put("seqId", seqId);
      YHORM orm = new YHORM();
      YHMessage message = (YHMessage)orm.loadObjSingle(dbConn, YHMessage.class, Integer.parseInt(seqId));
      String bodyId=message.getBodySeqId()+"";
      YHMessageBody mb = (YHMessageBody)orm.loadObjSingle(dbConn, YHMessageBody.class, Integer.parseInt(bodyId));
      
      orm.deleteSingle(dbConn, message);
     // orm.deleteSingle(dbConn, mb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除内部短信！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 短信删除逻辑
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delMessage(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deType = request.getParameter("deType");
      String bodyId = request.getParameter("bodyId");
      YHMessageLogic Logic = new YHMessageLogic();
      if(bodyId != null){
        String[] bIds = bodyId.split(",");
        for (String bIdstr : bIds){
          if("".equals(bIdstr.trim())){
            continue;
          }
          int bId = Integer.parseInt(bIdstr.trim());
          Logic.doDelSms(dbConn, bId, deType, person.getSeqId());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除失败！可能原因：" + e.getMessage());
       e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getMessageTypeDesc(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    String smsType = request.getParameter("messageType");
    String code = request.getParameter("code");
    Connection dbConn = null;
    try {
      
        
  

      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHMessageLogic sl = new YHMessageLogic();
      String data="";
      if(YHUtility.isNullorEmpty(smsType) || "null".equals(smsType)){    
        data="微讯";
      }else{
        
        if(YHUtility.isNullorEmpty(smsType) || "null".equals(smsType)){    
          smsType="0";
        }
        
        data= sl.getMessageTypeDesc(dbConn, Integer.parseInt(smsType.trim()), code);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 标记为读
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String resetFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      String bodySeqIds = request.getParameter("seqId");
      YHORM orm = new YHORM();
 
      YHMessageLogic sl = new YHMessageLogic();
    
      String[] bodyIds = bodySeqIds.split(",");
      for (String bodyId : bodyIds) {
        if("".equals(bodyId.trim())){
          continue;
        }
        
        //解决sms表 body_seq_id和to_id索引出数据不唯一的情况

        List<Integer> list = sl.getMessageSeqIds(dbConn, toId, Integer.parseInt(bodyId.trim()));
        for (int id : list) {
          YHMessage Message = (YHMessage) orm.loadObjSingle(dbConn, YHMessage.class, id);
          Message.setSeqId(id);
          Message.setToId(toId);
          Message.setRemindFlag("0");
          orm.updateSingle(dbConn, Message);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 已收微讯
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String messageInbox(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    int toId = person.getSeqId();  //用户seqId
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try {
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
      dbConn = requestDbConn.getSysDbConn();
      messageLogic = new YHMessageLogic();
      String data = messageLogic.getPanelInBox1(dbConn,request.getParameterMap(), toId, pageNo, pageSize);
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 标记为读
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String resetFlagAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    ArrayList<YHMessage> messagelist = null;
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHORM orm = new YHORM();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{" REMIND_FLAG IN(1,2) AND TO_ID=" + toId };
      List funcList = new ArrayList();
      YHMessageLogic sl = new YHMessageLogic();
      funcList.add("message");
      
      messagelist = (ArrayList<YHMessage>) orm.loadListSingle(dbConn,  YHMessage.class, filters);
      for (YHMessage message : messagelist){
        message.setRemindFlag("0");
        orm.updateSingle(dbConn, message);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
 
  public String queryMessageList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHPageDataList data = null;
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    String queryType = request.getParameter("queryType");
    boolean isQuery = false;
    String url = "";
      isQuery = true;
      url =  "/core/funcs/message/searchForOut.jsp?";
   
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHMessageLogic messageLogic = new YHMessageLogic();
      data = messageLogic.toSendBoxJson2(dbConn, request.getParameterMap(), toId,pageNo,pageSize,isQuery);
      sizeNo = data.getTotalRecord();
      request.setAttribute("contentList", data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return url + "sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  
  public String queryMessageList1(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHPageDataList data = null;
    String pageNoStr = request.getParameter("pageNo");
    String pageSizeStr = request.getParameter("pageSize");
    String queryType = request.getParameter("queryType");
    boolean isQuery = false;
    String url = "";
      isQuery = true;
      url =  "/core/funcs/message/searchForOut.jsp?";
   
    int sizeNo = 0;
    int pageNo = 0;
    int pageSize = 0;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      pageNo = Integer.parseInt(pageNoStr);
      pageSize = Integer.parseInt(pageSizeStr);
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHMessageLogic messageLogic = new YHMessageLogic();
      data = messageLogic.toSendBoxJson1(dbConn, request.getParameterMap(), toId,pageNo,pageSize,isQuery);
      sizeNo = data.getTotalRecord();
      request.setAttribute("contentList", data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return url + "sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
  }
  
  
  
  public String getMessageToId(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    String bodyId = request.getParameter("bodyId");
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHMessageLogic sl = new YHMessageLogic();
      if(bodyId.indexOf(".")!=-1)
        bodyId=bodyId.substring(0, bodyId.indexOf("."));
      String data = sl.getToIdByBodyId(dbConn, Integer.parseInt(bodyId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getMessageBodyContent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    YHMessageLogic messageLogic = null;
    String contentDetail = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      StringBuffer sb = new StringBuffer();
      messageLogic = new YHMessageLogic();
      contentDetail = messageLogic.getContent(dbConn, seqId);
      contentDetail = YHUtility.encodeSpecial(contentDetail);
      sb.append("{");
      sb.append("content:\"" + YHUtility.encodeSpecial(contentDetail) + "\"");
      sb.append("}");
      data = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出短信内容！");
      request.setAttribute(YHActionKeys.RET_DATA, data);   
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getStauts(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    String bodyId = request.getParameter("bodyId");
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHMessageLogic sl = new YHMessageLogic();
      if(bodyId.indexOf(".")!=-1)
      bodyId=bodyId.substring(0, bodyId.indexOf("."));
      String data = sl.getStatusByBodyId(dbConn, Integer.parseInt(bodyId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getStatusByBodyId(HttpServletRequest request,
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    String bodyIdStr = request.getParameter("bodyId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      ArrayList<YHMessage> contentList = (ArrayList<YHMessage>) orm.loadListSingle(dbConn, YHMessage.class, new String[]{" BODY_SEQ_ID=" + bodyIdStr + " and (DELETE_FLAG = '0' or DELETE_FLAG = '1') "});
      request.setAttribute("contentList", contentList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/message/test.jsp";
  }
  
  
  /**
   * 数据结构：Map<YHSmsBody,List<YHsms>>
   * @param request
   * @param response
   * @return
   * @throws Exception
  */
   public String messageSentbox(HttpServletRequest request,
       HttpServletResponse response) throws Exception { 
     String pageNoStr = request.getParameter("pageNo");
     String pageSizeStr = request.getParameter("pageSize");
     int sizeNo = 0;
     int pageNo = 0;
     int pageSize = 0;
     Connection dbConn = null;
     try {
       pageNo = Integer.parseInt(pageNoStr);
       pageSize = Integer.parseInt(pageSizeStr);
       YHORM orm = new YHORM();
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
       dbConn = requestDbConn.getSysDbConn();
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       int userId = person.getSeqId();
       YHMessageLogic sl = new YHMessageLogic();
       StringBuffer data = sl.getPanelSentBox(dbConn, request.getParameterMap(),userId, pageNo, pageSize);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_DATA,data.toString());
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       ex.printStackTrace();
     }
     return "/core/inc/rtjson.jsp";
   }
   
   /**
    * 
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public String viewDetails(HttpServletRequest request,
       HttpServletResponse response) throws Exception{
     Connection dbConn = null;
     String smsIds = request.getParameter("smsIds");
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       int toId = person.getSeqId();
       YHMessageLogic sl = new YHMessageLogic();
       List<Map<String, String>> l = sl.viewDetailsLogic(dbConn, smsIds,toId);
       request.setAttribute("pageData", l);
     } catch (Exception e) {
       throw e;
     }
     return "/core/frame/ispirit/nav.jsp";
   }
   
   public String acceptedSms(HttpServletRequest request,
       HttpServletResponse response) throws Exception{
     Connection dbConn = null;
     YHPageDataList data = null;
     String pageNoStr = request.getParameter("pageNo");
     String pageSizeStr = request.getParameter("pageSize");
     String queryType = request.getParameter("queryType");
     boolean isQuery = false;
     String url = "";
     int sizeNo = 0;
     int pageNo = 0;
     int pageSize = 0;
     if("1".equals(queryType)){
       isQuery = true;
       url =  "/core/funcs/message/searchForIn.jsp?";
     }else{
       url =  "/core/funcs/message/accepte.jsp?";
     }
     try{
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       pageNo = Integer.parseInt(pageNoStr);
       pageSize = Integer.parseInt(pageSizeStr);
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       int toId = person.getSeqId();
       YHMessageLogic smsLogic = new YHMessageLogic();
       data = smsLogic.toInBoxJson(dbConn, request.getParameterMap(), toId,pageNo,pageSize,isQuery);
       sizeNo = data.getTotalRecord();
       request.setAttribute("contentList", data);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
     }catch(Exception ex) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
       throw ex;
     }
     return url + "sizeNo="+sizeNo + "&pageNo=" + pageNo + "&pageSize=" + pageSize ;
   }
   
   /**
    * 精灵检查是否有新微讯
    **/
   public String get_msg(HttpServletRequest request,
       HttpServletResponse response) throws Exception{
      Connection dbConn = null;
      Statement stmt=null;
      try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String IM_FLAG=request.getParameter("IM_FLAG");
      String CUR_TIME=YHUtility.getCurDateTimeStr("yyyy-MM-dd");
      String MSG_ID_STR ="";
      String FROM_UID_STR = "";
      Map<String,String> output =new HashMap();
      List<Map<String,String>> list = new ArrayList(); 
      YHMessageLogic  logic= new YHMessageLogic();
      if("1".equals(IM_FLAG))
      {
        String dateFiler = YHDBUtility.getDateFilter("T0.SEND_TIME",YHUtility.getCurDateTimeStr(), "<=");
        String dbDateFremind = YHDBUtility.getDateFilter("T1.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
        String sql = "SELECT * FROM oa_message T1 ,oa_message_body T0 WHERE TO_ID='"+person.getSeqId()+"' AND REMIND_FLAG = '1' AND  T1.BODY_SEQ_ID= T0.SEQ_ID  " +
            "and DELETE_FLAG in (0, 2) " +            "AND " + dateFiler +
            " AND (T1.REMIND_TIME IS NULL OR " + dbDateFremind + ")";
         stmt=dbConn.createStatement();
         ResultSet rs=stmt.executeQuery(sql);
         while(rs.next())
         {
            String MSG_ID=rs.getString("SEQ_ID");
            String FROM_UID=rs.getString("FROM_ID");
            String MSG_TYPE=rs.getString("MESSAGE_TYPE");
            String SEND_TIME=rs.getString("SEND_TIME");
            String CONTENT=rs.getString("CONTENT");
            output =new HashMap();
            MSG_ID_STR += MSG_ID+",";
            FROM_UID_STR += FROM_UID+",";
            output.put("from_uid", FROM_UID);
            SEND_TIME=SEND_TIME.substring(0, 19);
            output.put("time", SEND_TIME);
            output.put("type", SEND_TIME);
            output.put("time", SEND_TIME);
            if(YHUtility.isNullorEmpty(MSG_TYPE)){
              MSG_TYPE="0";
            }
           
          
         //   String type = logic.getMessageTypeDesc(dbConn, Integer.parseInt(MSG_TYPE), "SMS_REMIND");
            output.put("type",  "发自 网页微讯") ;
            output.put("content", YHUtility.encodeSpecial(CONTENT));
            output.put("from_name", logic.getUserName(dbConn, FROM_UID));
            list.add(output);
            
         }
       }/*else{
         
        String sql=" SELECT SEND_TIME from MESSAGE where TO_ID='"+person.getSeqId()+"' and REMIND_FLAG!='0' and DELETE_FLAG!='1' and "+YHDBUtility.getDateFilter("send_time", CUR_TIME, "<=")+" order by SEND_TIME ";
         Statement stmt=dbConn.createStatement();
         ResultSet rs=stmt.executeQuery(sql);
         if(rs.next())
         {
            String DEADLINE=rs.getString("SEND_TIME");
         
            sql=" SELECT SEQ_ID,FROM_ID,TO_ID,SEND_TIME as SEND_TIME,REMIND_FLAG,DELETE_FLAG,MESSAGE_TYPE,CONTENT,USER_NAME,AVATAR from MESSAGE,PERSON USER where USER.SEQ_ID=MESSAGE.FROM_ID AND (TO_ID='"+person.getSeqId()+"' and REMIND_FLAG!='0' and DELETE_FLAG!='1' or FROM_ID='"+person.getSeqId()+"') and "+YHDBUtility.getDateFilter("SEND_TIME", CUR_TIME, ">=")+" and "+YHDBUtility.getDateFilter("SEND_TIME", DEADLINE, "<=")+" order by SEND_TIME asc    ";
            rs=stmt.executeQuery(sql);
            while(rs.next())
            {
               String MSG_ID=rs.getString("SEQ_ID");
               String FROM_ID=rs.getString("FROM_ID");
               String TO_ID=rs.getString("TO_ID");
               String DELETE_FLAG=rs.getString("DELETE_FLAG");
               String MSG_TYPE=rs.getString("MESSAGE_TYPE");
               String SEND_TIME=rs.getString("SEND_TIME");
               String CONTENT=rs.getString("CONTENT");
               String FROM_NAME=rs.getString("USER_NAME");
               String AVATAR = rs.getString("AVATAR");
              
               FROM_NAME = FROM_NAME.length() > 18 ? FROM_NAME.substring(0, 16)+"..." : FROM_NAME;
            //   SEND_TIME = substr(SEND_TIME, 0, 10) == date('Y-m-d') ? substr($SEND_TIME, 11, 5) : substr($SEND_TIME, 5, 11);
               
               String  RECEIVE = "0";
               if(TO_ID.equals(person.getSeqId()+""))
               {
                  MSG_ID_STR += MSG_ID+",";
                  RECEIVE = DELETE_FLAG.equals("1") ? "0" : "1";
               }
            
               output =new HashMap();
               output.put("sms_id", MSG_ID);
               output.put("to_id", TO_ID);
               output.put("from_id",FROM_ID);
               output.put("from_name",logic.getUserName(dbConn,FROM_ID) );
               output.put("type_id", MSG_TYPE);
               output.put("from_type_name", "网页版微讯");
               output.put("type_name", "网页版微讯");
               output.put("send_time",SEND_TIME );
               output.put("unread", "1");
               output.put("content", CONTENT);
               output.put("avatar", AVATAR);
               output.put("receive", RECEIVE);
               list.add(output);
          
            }
         }
         
        // 提醒  new_sms_remind($LOGIN_UID, 0, 1);
         
         */
         //已读
         MSG_ID_STR=MSG_ID_STR.trim();
         if(MSG_ID_STR.endsWith(",")){
           MSG_ID_STR=MSG_ID_STR.substring(0, MSG_ID_STR.length()-1);
         }
          String REMIND_FLAG ="0";
         if(!YHUtility.isNullorEmpty(MSG_ID_STR)){
           String update = "update oa_message set REMIND_FLAG='"+REMIND_FLAG+"' where SEQ_ID in ("+MSG_ID_STR+")"; 
           stmt=dbConn.createStatement();
           stmt.executeUpdate(update);
         /*  update = "update message set READ_FLAG='1' where SEQ_ID in ("+MSG_ID_STR+")";
           
           stmt.executeUpdate(update);*/
         }
    //   }

      String returnStr="";
      for(int i=0;i<list.size();i++){
        Map<String,String> map = list.get(i);
        returnStr+=YHFOM.toJson(map).toString();
        returnStr+=",";
      }
      
      if(returnStr.endsWith(",")){
        returnStr=returnStr.substring(0, returnStr.length()-1);  
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, "{data:["+returnStr+"]}");
     // System.out.println( "{data:["+returnStr+"]}");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
    
   
   }
   
   /**
    * 
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
   public String remindCheck(HttpServletRequest request,
       HttpServletResponse response) throws Exception{
     Connection conn = null;
     int data = 0;
     try{
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       conn = requestDbConn.getSysDbConn();
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       if(person != null){
         int personId = person.getSeqId();
         messageLogic = new YHMessageLogic();
         data = messageLogic.isRemind(conn, personId);
       }
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
       request.setAttribute(YHActionKeys.RET_DATA, data+"");
     } catch (Exception e){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
       return "/core/inc/rtjson.jsp";
     }
     return "/core/inc/rtjson.jsp";
   }
   
}

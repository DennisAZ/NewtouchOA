<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.YHSmsBody, yh.core.funcs.sms.logic.YHSmsLogic" %>
<%@ page import="yh.core.funcs.message.data.YHMessage" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%@ page import="java.text.*" %>
<%@ include file="/core/inc/header.jsp" %>

<%
  String pageNoString = request.getParameter("pageNo");
  ArrayList<YHMessage> contentList = (ArrayList<YHMessage>)request.getAttribute("contentList");
  //System.out.println(contentList.size()+"PPPPPp");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已接收短信</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/smsutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/sms.js"></script>
<script type="text/javascript">

  function doInit() {
  <%--   var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getTotalSmsContentNum.act";
    var rtJson = getJsonRs(url, null); 

    if (rtJson.rtState == "0") {
      document.getElementById("code").innerHTML = rtJson.rtData.num;
    }else {
      alert(rtJson.rtMsrg); 
    } --%>
  }

  function deleteAllCheckBoxs() {
    var idStrs = checkMags('deleteFlag');
    if(!idStrs) {
      alert("请选择需要删除的内部短信！");
      return;
    }
    if(!confirmDel()) {
    	return ;
    } 
    var idStrsArray = idStrs.split(",");
    var bodyIds = "";
    var userIds = "";
    
    for (var i = 0 ; i < idStrsArray.length ; i++){
      if(idStrsArray[i]){
        bodyIds += bodyIds == "" ? idStrsArray[i].split("|")[0] : "," + idStrsArray[i].split("|")[0];
        userIds += userIds == "" ? idStrsArray[i].split("|")[1] : "," + idStrsArray[i].split("|")[1];
      }
    }
    var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/delSmsByUser.act";
    var rtJson = getJsonRs(url, "bodyId=" + bodyIds + "&deType=2" + "&userId=" + userIds );
    if (rtJson.rtState == "0") {
      window.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    } 
  }

  function resendSms(seqId,formId) {
    var seqID = null;
    var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/bodySeqIdTest.act";
    var rtJson = getJsonRs(url, "formId=" + formId);
    if (rtJson.rtState == "0") {
      bindJson2Cntrl(rtJson.rtData);
      for(var i = 0; i < rtJson.rtData.length; i++){
        seqID = rtJson.rtData[i].seqId; 
      }
    }else {
      alert(rtJson.rtMsrg); 
    }
    window.location.href = "<%=contextPath %>/core/funcs/message/sendmessage.jsp?seqId="+seqID+"&formId=" + formId;
  }

  function checkAll(field) {
    var deleteFlags = document.getElementsByName("deleteFlag");
    for(var i = 0; i < deleteFlags.length; i++) {
      deleteFlags[i].checked = field.checked;
    }
  }

  function dispatchSms(seqId) {
    window.location.href = "<%=contextPath %>/core/funcs/message/sendmessage.jsp?seqId=" + seqId;
  }
    
</script>
</head>
<body topmargin="5" onload="doInit()">

<table cellscpacing="1" cellpadding="3" width="100%">
  <tr class="TableHeader">
  
    <td nowrap align="center">状态</td>
    <td nowrap align="center">收件人</td>
  
  </tr>
   <%
     int k = contentList.size();
     for(int i = 0; i < contentList.size(); i++) {
       boolean isRead = false;
       boolean isDel = false;
       YHMessage smsContentWant = contentList.get(i);
       if(smsContentWant.getRemindFlag() == null){
         smsContentWant.setRemindFlag("2");
       }
       if(smsContentWant.getDeleteFlag() == null){
         smsContentWant.setDeleteFlag("0");
       }
       if(smsContentWant.getRemindFlag().equals("0")){
         isRead = true;
       }else {
         isRead = false;
       }
       if(smsContentWant.getDeleteFlag().equals("1")){
         isDel = true;
       }else {
         isDel = false;
       }
   %>
   <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">

     <td>
      <%if(isDel){ %>
     <img id="delete"  src="<%=imgPath%>/email_delete.gif" title="收信人已删除">
     <%}else {
       if(isRead){%>
        <img id="open"  src="<%=imgPath%>/email_open.gif" title="收信人已读">
     <%}else{%>
        <img id="close" src="<%=imgPath%>/email_close.gif" title="收信人未读">
     <%}
       }%>
     </td>
     <td nowrap align="center" >
       <input id="toId_<%=i %>" type="hidden" value="<%=smsContentWant.getToId()%>">
       <span id="toId_<%=i %>Desc"></span>
     </td>
   
   </tr>  
   <%
     }
   %>
</table>
 
</body>
<script>
function doInit(){
  for(var i = 0 ; i < <%=k%> ; i++){
    bindDesc([{cntrlId:"toId_" + i, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
  }
}
</script>
</html>
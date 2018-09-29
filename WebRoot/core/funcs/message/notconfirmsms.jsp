<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.*, yh.core.funcs.sms.logic.YHSmsLogic" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%@ page import="java.text.*" %>
<%@ include file="/core/inc/header.jsp" %>
<%
String totalRecord = request.getParameter("sizeNo");
if(totalRecord == null){
  totalRecord = "0";
}
String pageIndex = request.getParameter("pageNo");
if(pageIndex == null){
  pageIndex = "0";
}
String pageSize = request.getParameter("pageSize");
if(pageSize == null){
  pageSize = "20";
}
  ArrayList<YHSms> contentList = (ArrayList<YHSms>)request.getAttribute("contentList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未确认短信</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/sms.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/smsutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/sms.js"></script>
<script type="text/javascript">
var pageSize = "<%=pageSize%>";
var totalRecord = "<%=totalRecord%>";
var pageIndex = "<%=pageIndex%>";
function deleteSms(seqId) {
  if(!confirmDel()) {
	  return ;
  }
      
  var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/deleteSms.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg); 
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function resendSms(seqId,formId) {
  window.location.href = "<%=contextPath %>/core/funcs/sms/sendsms.jsp?toId=" + formId;
}

function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
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
  var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/delSms.act";
  var rtJson = getJsonRs(url, "bodyId=" + idStrs + "&deType=1");
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}
function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  window.location.href = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/acceptedSms.act?pageNo=" + pageNo + "&pageSize=" + pageSize ;
}
</script> 
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/email_close.gif" WIDTH="20" HEIGHT="20" align="absmiddle"><span class="big3"> 未确认短信息</span><br>
    </td>
     <td id="numDiv" style="display:none;" align="center">共有<span style="color:red" id="code"></span>条短消息</td>
    <td  id="pageDiv" align="right" style="display:none;"><div id="pageInfo" style="float:right;"></div></td>
  </tr>
</table>
<div id="acceptDiv" style="display:none;">
<table cellscpacing="1" cellpadding="3" width="100%" class="TableList">
  <tr class="TableHeader">
    <td nowrap align="center">选择</td>
    <td nowrap align="center">发送人</td>
    <td nowrap align="center">内容</td>
    <td nowrap align="center">发送时间</td>
    <td nowrap align="center">操作</td>
  </tr>
   <%
     int k = 0;
     for(int i = 0; i < contentList.size(); i++) {
       YHSms sms = contentList.get(i);
       YHSmsBody smsContentWant = sms.getSmsBodyList().get(0);
   %>
   <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
     <td>
       <input type="checkbox" name="deleteFlag" value="<%=smsContentWant.getSeqId()%>"/>
     </td>
     <td nowrap align="center" >
      <input id="fromId_<%=k%>" type="hidden" value="<%=smsContentWant.getFromId()%>">
      <span id="fromId_<%=k%>Desc"></span>
     </td>
     <td>
       <%if("0".equals(smsContentWant.getSmsType())){ %>
     <img src="<%=imgPath%>/collapsed.gif" align="absMiddle" title="点击展开消息记录" style="cursor:pointer;" id="img4" onclick="redHistory('history_<%=smsContentWant.getSeqId()%>',history_con_<%=smsContentWant.getSeqId()%>,<%=smsContentWant.getFromId()%>,'<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsContentWant.getSendTime())%>',1,'<%=smsContentWant.getSeqId()%>');">
      <%} %>
      <img src="<%=imgPath%>/sms/sms_type<%=smsContentWant.getSmsType() %>.gif" align="absMiddle" width="15" height="15">
       <a href="javascript:showContent('<%=smsContentWant.getSeqId() %>');"><%=(smsContentWant.getContent().replaceAll("<br>","\n\r").length() < 15) ? smsContentWant.getContent().replaceAll("<br>","\n\r") : smsContentWant.getContent().replaceAll("<br>","\n\r").substring(0, 15)%></a>
       <%if("2".equals(sms.getRemindFlag())){ %>
       <img src="<%=imgPath%>/email_close.gif" title="未读短信"> 
       <%}else{ %>
       <img src="<%=imgPath%>/email_new.gif" title="新信息"> 
       <%} %>
     </td>
     <td nowrap align="center">
       <%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsContentWant.getSendTime())%>
     </td>
      <td nowrap align="center">
    <%if("0".equals(smsContentWant.getSmsType())){ %>
       <a href="javascript:resendSms('<%=smsContentWant.getSeqId()%>','<%=smsContentWant.getFromId()%>');">回复</a>
       <%}else{%>
         <a href="javascript:detailsForSms('<%=smsContentWant.getRemindUrl()%>','<%=smsContentWant.getSeqId()%>','<%=smsContentWant.getSmsType()%>');">查看详情</a>
      <% } %>
     </td>
   </tr>
   <tr id='history_<%=smsContentWant.getSeqId()%>' style="display:none" class="<%= (i % 2 == 0) ? "TableLine2" : "TableLine1" %>">
   <td colspan='5'>
      <div id = "history_con_<%=smsContentWant.getSeqId()%>"></div>
   </td>
   </tr>  
   <%
     k++;}
   %>
</table>
  <div>
     <input type="checkbox" id="checkAll" name="checkAll" onclick="checkAll(this)"/><label for="checkAll">全选</label>&nbsp;&nbsp;&nbsp;&nbsp;
      <IMG src="<%=imgPath%>/sms/sms_delete.gif"> <A title="" href="javascript:deleteAllCheckBoxs();">删除</A>
     <IMG src="<%=imgPath%>/sms/sms_open.gif"> <A title=标记为已读 href="javascript:doMarkRead();">已读</A>
  </div>
<br>
<BR>
<TABLE class=TableBlock width="100%" align=center>
<TBODY>
<TR>
<TD class=TableContent width=80 noWrap align=middle><B>快捷操作：</B></TD>
<TD class=TableControl noWrap>&nbsp; <IMG src="<%=imgPath%>/sms/sms_open.gif"> <A 
title=标记所有短信息为已读 
href="javascript:readAll();">全部标记为已读</A>&nbsp;&nbsp; 
</TD></TR></TBODY></TABLE>
</div>

<div id="acceptSms" style="display: none;">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div  style="font-size:12pt;" class="content">无未确认的短信息</div>
    </td>
  </tr>
</table>
</div>
<script type="text/javascript">
var cfgs = {
    dataAction: "",
    container: "pageInfo",
    pageSize:pageSize,
    loadData:loadDataAction,
    totalRecord:totalRecord,
    pageIndex:pageIndex
  };
var pageInfoS = null;
function doInit(){
  pageInfoS = new YHJsPagePilot(cfgs);
  pageInfoS.show();
  if(pageInfoS.pageInfo.totalRecord){
    $('code').innerHTML = pageInfoS.pageInfo.totalRecord;
    for(var i = 0 ; i < <%=k%> ; i++){
      bindDesc([{cntrlId:"fromId_" + i, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
    }
    $('numDiv').style.display = "";
    $('pageDiv').style.display = "";
    $('acceptDiv').style.display = "";
  }else{
    $('acceptSms').style.display = "";
  }
}
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.*" %>
<%@ page import="yh.core.global.YHBeanKeys" %>
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
  List<YHSmsBody> smsList = (List<YHSmsBody>)request.getAttribute("sbList");
  Map<YHSmsBody,List<YHSms>> smsMap = (Map<YHSmsBody,List<YHSms>>)request.getAttribute("DataMap");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已发送短信</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/sms.css" type="text/css" />
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

function dispatchSms(seqId) {
  window.location.href = "<%=contextPath %>/core/funcs/sms/sendsms.jsp?seqId=" + seqId;
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
  var rtJson = getJsonRs(url, "bodyId=" + idStrs + "&deType=2");
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}

function resetSms(smsSeqId,toId,content,sendTime,smsType){
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/addSmsBody.act";
  var rtJson = getJsonRs(url, "smsSeqId="+smsSeqId+"&user="+toId+"&content="+content+"&sendTime="+sendTime+"&smsType="+smsType);
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
    window.location.reload();
    //$("smsBodyInfoForm").reset();
  }
}

function showMsg(seqId){
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getDialogDetail1.act?seqId="+seqId;
  var rtJson = getJsonRs(url);
  var dialogDetail = document.getElementById("dialogDetail");
  dialogDetail.style.display = "";
}
function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  window.location.href = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/listAllSms.act?pageNo=" + pageNo + "&pageSize=" + pageSize ;
}
function showStatus(smsBodyId){
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getStatusByBodyId.act?bodyId="+smsBodyId;
  openWindow(url,"查看详情",700,600) ;
}
</script>
</head>
<body topmargin="5" onload="doInit()">

<table cellscpacing="1" cellpadding="3" width="100%">
  <tr>
    <td class="Big">
      <img src="<%=imgPath%>/msg_fwd.gif"></img><span class="big3"> 已发送的短信息</span>
    </td>
    <td id="numDiv" style="display:none;" align="center">共有<span style="color:red" id="code"></span>条短消息</td>
    <td  id="pageDiv" align="right" style="display:none;"><div id="pageInfo" style="float:right;"></div></td>
  </tr>
</table>
<div id="acceptDiv" style="display:none;">
<table cellscpacing="1" cellpadding="3" width="100%" id="sentSmscon">
  <tr class="TableHeader">
    <td nowrap align="center">选择</td>
    <td nowrap align="center">收信人</td>
    <td nowrap align="center">内容</td>
    <td nowrap align="center">发送时间</td>
    <td nowrap align="center">状态</td>
    <td nowrap align="center" width="100px">操作</td>
  </tr>
   <%
   int i = 0;
   int k = 0;
     for(YHSmsBody smsBody: smsList) {
       List<YHSms> smslist = smsMap.get(smsBody);
       int smsSeqId = 0;
       boolean isRead = false;
       boolean isDel = false;
       if(smslist == null ||smslist.size() == 0){
         continue;
       }
       if(smslist.size() == 1){
         k ++;
       }
       for(YHSms sms: smslist){
	       if(sms.getRemindFlag() == null){
	         sms.setRemindFlag("2");
	       }
	       if(sms.getDeleteFlag() == null){
	         sms.setDeleteFlag("0");
	       }
	       if(sms.getRemindFlag().equals("0")){
	         isRead = true;
	       }else {
	         isRead = false;
	       }
	       if(sms.getDeleteFlag().equals("1")){
	         isDel = true;
	       }else {
	         isDel = false;
	       }
       }
   %>
   <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
     <td>
       <input type="checkbox" name="deleteFlag" value="<%=smsBody.getSeqId() %>"/>
     </td>
     <td nowrap align="center" id="userId">
       <%if(smslist.size() != 1) {%>
       <a href="javascript:showStatus(<%=smsBody.getSeqId() %>);">查看</a>
       <%}else{ %>
       <input id="toId_<%=k %>" type="hidden" value="<%=smslist.get(0).getToId()%>">
       <span id="toId_<%=k %>Desc"></span>
       <%} %>
     </td>
     <td>
     <%if("0".equals(smsBody.getSmsType())){ %>
     <img src="<%=imgPath%>/collapsed.gif" align="absMiddle" title="点击展开消息记录" style="cursor:pointer;" id="img4" onclick="redHistory('history_<%=smsBody.getSeqId()%>',history_con_<%=smsBody.getSeqId()%>,<%=smsBody.getFromId()%>,'<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsBody.getSendTime())%>',0,'<%=smsBody.getSeqId()%>');">
     <%} %>
       <img src="<%=imgPath%>/sms/sms_type<%=smsBody.getSmsType() %>.gif" align="absMiddle" width="15" height="15" alt="个人短信">
       <a href="javascript:showContent('<%=smsBody.getSeqId() %>');"><%=(smsBody.getContent().replaceAll("<br>","\n\r").length() < 15) ? smsBody.getContent().replaceAll("<br>","\n\r") : smsBody.getContent().replaceAll("<br>","\n\r").substring(0, 15)%></a>
     </td>
     <td nowrap align="center">
       <%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsBody.getSendTime())%>
     </td>
     <td nowrap align="center">
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
     <td>
       <%   
         smsSeqId = smslist.get(0).getSeqId();
       %>
       <a href="javascript:dispatchSms('<%=smslist.get(0).getBodySeqId() %>');">转发</a>&nbsp;&nbsp;&nbsp;&nbsp;
       <a href="javascript:resetSms('<%=smsSeqId %>','<%=smslist.get(0).getToId()%>','<%=(smsBody.getContent().length() < 15) ? smsBody.getContent() : smsBody.getContent().substring(0, 15)%>','<%=new SimpleDateFormat("yyyy-MM-dd").format(smsBody.getSendTime())%>','<%=smsBody.getSmsType() %>');">重发</a>
     </td>
   </tr> 
    <tr id='history_<%=smsBody.getSeqId()%>' style="display:none" class="<%= (i % 2 == 0) ? "TableLine2" : "TableLine1" %>">
    <td colspan='6'>
      <div id = "history_con_<%=smsBody.getSeqId()%>"></div>
    </td>
    </tr>  
   <%
   i++;
     }
   %>
</table>
  <div>
     <input type="checkbox" id="checkAll" name="checkAll" onclick="checkAll(this)"/><label for="checkAll">全选</label>&nbsp;&nbsp;&nbsp;&nbsp;
     <IMG src="<%=imgPath%>/sms/sms_delete.gif"> <A title="" href="javascript:deleteAllCheckBoxs();">删除</A>
  </div>
  <BR>
	<TABLE class=TableBlock width="100%" align=center>
	<TBODY>
	<TR>
	<TD class=TableContent width=80 noWrap align=middle><B>快捷操作：</B></TD>
	<TD class=TableControl noWrap>&nbsp; <A title="" 
	href="javascript:delShortCut(2,2,'确认删除已提醒收信人短信？');"><IMG 
	style="PADDING-BOTTOM: 4px; PADDING-LEFT: 3px; PADDING-RIGHT: 3px; PADDING-TOP: 4px" 
	align=absMiddle src="<%=imgPath%>/sms/sms_delete.gif">删除已提醒收信人短信</A>&nbsp;&nbsp; <A title="" 
	href="javascript:delShortCut(3,2,'确认删除收信人已删除短信？');"><IMG 
	style="PADDING-BOTTOM: 4px; PADDING-LEFT: 3px; PADDING-RIGHT: 3px; PADDING-TOP: 4px" 
	align=absMiddle src="<%=imgPath%>/sms/sms_delete.gif">删除收信人已删除短信</A>&nbsp;&nbsp; <A 
	title=删除所有已发送的短信息 href="javascript:delShortCut(1,2,'确认删除所有短信？');"><IMG 
	style="PADDING-BOTTOM: 4px; PADDING-LEFT: 3px; PADDING-RIGHT: 3px; PADDING-TOP: 4px" 
	align=absMiddle src="<%=imgPath%>/sms/sms_delete.gif">全部删除</A>&nbsp;&nbsp; 
	</TD></TR></TBODY></TABLE>
</div>
<div id="acceptSms" style="display: none;">
  <table class="MessageBox" align="center" width="300" >
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无已发送的短信息</div>
    </td>
  </tr>
</table>	
</div>

<div id="dialogDetail" style="display: none;height:200;position:absolute;border-top:1 solid #b8d1e2;width:100%;overflow:auto;" class="classColor">
 
 
 <div class="module_sms listColor" id="module_52">
    <div class="time">
      <u title="部门：中国兵器/系统开发部/PHP开发组" style="cursor:pointer">ddd</u>&nbsp;&nbsp;01-15 11:49    </div>
    <div class="content" style="height:30px;">内容</div>
  </div>
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
function doInit() {
  pageInfoS = new YHJsPagePilot(cfgs);
  pageInfoS.show();
  if(pageInfoS.pageInfo.totalRecord){
    $('code').innerHTML = pageInfoS.pageInfo.totalRecord;
    for(var i = 1 ; i <= <%=k%> ; i++){
      bindDesc([{cntrlId:"toId_" + i, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
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
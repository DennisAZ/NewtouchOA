<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.*" %>
<%@ page import="yh.core.data.*"%>
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
  YHPageDataList data = (YHPageDataList)request.getAttribute("contentList");
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

function resetSms(smsBodyId,content,smsType,remindUrl){
  var toId = "";
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getSmsToId.act";
  var rtJson = getJsonRs(url, "bodyId=" + smsBodyId);
  if (rtJson.rtState == "0") {
    toId = rtJson.rtData;
  }else{
     return;
   }
  var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/getSmsBodyContent.act";
  var rtJson = getJsonRs(url, "seqId=" + smsBodyId);
  if (rtJson.rtState == "0") {
     content =  rtJson.rtData.content;
   }
  var contentValue =  content.replace(/\"/g,"\\\"").replace(/\'/g,"\\\'").replace(/[\n\r\f]/g,"<br>");
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/addSmsBody.act";
  var rtJson = getJsonRs(url, "user="+toId+"&content="+encodeURIComponent(content)+"&smsType="+smsType+"&remindUrl="+encodeURIComponent(remindUrl));
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
    window.location.reload();
  }
}
function getToIdByBodyId(smsBodyId){
  var toId = "";
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getSmsToId.act";
  var rtJson = getJsonRs(url, "bodyId=" + smsBodyId);
  if (rtJson.rtState == "0") {
    toId = rtJson.rtData;
  }
  return toId;
}

function bindStauts(smsBodyId){
  var obj;
  var html = "";
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getStauts.act";
  var rtJson = getJsonRs(url, "bodyId=" + smsBodyId);
  if (rtJson.rtState == "0") {
    obj = rtJson.rtData;
  }
  if(obj){
    var isDel = obj.isDel;
    var isRead = obj.isRead;
    if(isDel == "1"){ 
      html = "<img id=\"delete\"  src=\"" + "<%=imgPath%>" + "/email_delete.gif\" title=\"收信人已删除\">";
    }else {
      if(isRead == "1"){
        html = "<img id=\"open\"  src=\"" + "<%=imgPath%>" + "/email_open.gif\" title=\"收信人已读\">";
      }else{
        html = "<img id=\"close\" src=\"" + "<%=imgPath%>" + "/email_close.gif\" title=\"收信人未读\">";
      }
    }
   }
  return html;
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
  window.location.href = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsTestAct/sentSmsList.act?pageNo=" + pageNo + "&pageSize=" + pageSize + "&queryType=1";
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
      <img src="<%=imgPath%>/msg_fwd.gif"></img><span class="big3"> 已发送短信查询结果 ，最多显示200条结果</span>
    </td>
    <td id="numDiv" style="display:none;" align="center">共有<span style="color:red" id="code"></span>条短消息</td>
      <td class="Big">
      <input type="button"  class="BigButton"value="返回" onclick="location='<%=contextPath %>/core/funcs/sms/querysms.jsp'"></input>
    </td>
    <td  id="pageDiv" align="right" style="display:none;"><div id="pageInfo" style="float:right;"></div></td>
  </tr>
</table>
<div id="acceptDiv" style="display:none;">
<table cellscpacing="1" cellpadding="3" width="100%" id="sentSmscon" class="TableList">
  <tr class="TableHeader">
    <td nowrap align="center">选择</td>
    <td nowrap align="center">收信人</td>
    <td nowrap align="center">内容</td>
    <td nowrap align="center">发送时间</td>
    <td nowrap align="center">状态</td>
    <td nowrap align="center">操作</td>
  </tr>
   <%
   int k = 0;
   for(int i = 0; i < data.getRecordCnt(); i++) {
     YHDbRecord record = data.getRecord(i);
     String smsBodyStr = record.getValueByName("smsBodyId").toString();
     String smsBodyId = smsBodyStr.indexOf(".") > 0 ? smsBodyStr.substring(0,smsBodyStr.indexOf(".")) : smsBodyStr;
     String fromId = record.getValueByName("fromId").toString();
     String smsType = (String)record.getValueByName("smsType");
     Date sendTime = (Date)record.getValueByName("sendTime");
     String content = (String)record.getValueByName("content");
     content = YHUtility.cutHtml(content);
     String remindUrl = (String)record.getValueByName("remindUrl") == null ? "" : (String)record.getValueByName("remindUrl");
   %>
   <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
     <td>
       <input type="checkbox" name="deleteFlag" value="<%=smsBodyId %>"/>
     </td>
     <td nowrap align="center" id="userId">
     <script>
       var toId = getToIdByBodyId('<%=smsBodyId%>');
       if(toId){
         var toIds = toId.split(",");
         if(toIds.length > 1){
           document.write("<a href=\"javascript:showStatus(<%=smsBodyId %>);\">查看</a>");
         }else{
           document.write("<input id=\"toId_<%=k %>\" type=\"hidden\" value=\"" + toId + "\">");
           document.write("<span id=\"toId_<%=k %>Desc\"></span>");
           <% k++; %>
         }
       }
     </script>
     </td>
     <td>
     <%if("0".equals(smsType)){ %>
     <img src="<%=imgPath%>/collapsed.gif" align="absMiddle" title="点击展开消息记录" style="cursor:pointer;" id="img4_history_<%=smsBodyId%>" onclick="redHistory('history_<%=smsBodyId%>',history_con_<%=smsBodyId%>,<%=fromId%>,'<%=YHUtility.getDateTimeStr(sendTime)%>',0,'<%=smsBodyId%>');">
     <%} %>
       <img src="<%=imgPath%>/sms/sms_type<%=smsType %>.gif" align="absMiddle" width="15" height="15" alt="个人短信">
       <a href="javascript:showContent('<%=smsBodyId %>');"><%=content%></a>
     </td>
     <td nowrap align="center">
       <%=YHUtility.getDateTimeStr(sendTime)%>
     </td>
     <td nowrap align="center">
      <script>
        var htmlstr = bindStauts('<%=smsBodyId%>');
        document.write(htmlstr);
     </script>
     </td>
     <td>
       <a href="javascript:dispatchSms('<%=smsBodyId %>');">转发</a>&nbsp;&nbsp;
       <a href='javascript:resetSms("<%=smsBodyId%>","","<%=smsType %>","<%=remindUrl %>");'>重发</a>
     </td>
   </tr> 
    <tr id='history_<%=smsBodyId%>' style="display:none" class="<%= (i % 2 == 0) ? "TableLine2" : "TableLine1" %>">
    <td colspan='6'>
      <div id = "history_con_<%=smsBodyId%>"></div>
    </td>
    </tr>  
   <%
     }
   %>
</table>
   <TABLE class=TableBlock width="100%" >
  <tr>
    <td>
     <input type="checkbox" id="checkAll" name="checkAll" onclick="checkAll(this)"/><label for="checkAll">全选</label>&nbsp;&nbsp;&nbsp;&nbsp;
     <IMG src="<%=imgPath%>/sms/sms_delete.gif"> <A title="" href="javascript:deleteAllCheckBoxs();">删除</A>
  </td>
  </tr>
  </TABLE>
</div>
<div id="acceptSms" style="display: none;">
  <table class="MessageBox" align="center" width="300" >
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无匹配的查询结果</div>
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
function doInit() {
  pageInfoS = new YHJsPagePilot(cfgs);
  pageInfoS.show();
  if(pageInfoS.pageInfo.totalRecord){
    $('code').innerHTML = pageInfoS.pageInfo.totalRecord;
    for(var i = 0 ; i <= <%=k%> ; i++){
      bindDesc([{cntrlId:"toId_" + i, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
    }
    $('numDiv').style.display = "";
    //$('pageDiv').style.display = "";
    $('acceptDiv').style.display = "";
  }else{
    $('acceptSms').style.display = "";
  }
}
</script>
</body>
</html>
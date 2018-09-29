<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.*" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="noscrollX">

<%
  String totalRecord = request.getParameter("sizeNo");
  if(totalRecord == null){
    totalRecord = "0";
  }
  String pageNo = request.getParameter("pageNo");
  if(pageNo == null){
    pageNo = "0";
  }
  String pageSize = request.getParameter("pageSize");
  if(pageSize == null){
    pageSize = "5";
  }
  String im = request.getParameter("im");
  if(im == null){
    im = "";
  }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/smsbox.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/js/messageutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/js/message.js"></script>
<script type="text/javascript">
function deleteSms(seqId) {
  if(!confirmDel()) {
    return ;
  }
  var url = "<%=contextPath %>/yh/core/funcs/message/act/YHMessageAct/delMessage.act";
  var rtJson = getJsonRs(url, "bodyId=" + seqId);
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg); 
    window.location.href="sentboxMessage.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  }
}
function resendSms(seqId,formId) {
  var seqID = null;
  var url = "<%=contextPath %>/yh/core/funcs/message/act/YHMessageAct/bodySeqIdTest.act";
  var rtJson = getJsonRs(url, "formId=" + formId);
  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    for(var i = 0; i < rtJson.rtData.length; i++){
      seqID = rtJson.rtData[i].seqId; 
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
  //alert(seqID);
  window.location.href = "<%=contextPath %>/core/funcs/message/sendmessage.jsp?seqId="+seqID+"&formId=" + formId;
}
function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function deleteAllCheckBoxs() {
  var idStrs = getAllIds('deleteFlag');
  if(!idStrs) {
    alert("请选择需要删除的内部微讯！");
    return;
  }
  if(!confirmDel()) {
    return ;
  } 
  var url = "<%=contextPath %>/yh/core/funcs/message/act/YHMessageAct/delMessages.act";
  var rtJson = getJsonRs(url, "bodyId=" + idStrs + "&deType=2");
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}
function showStatus(smsBodyId){
  var url = "<%=contextPath%>/yh/core/funcs/message/act/YHMessageAct/getStatusByBodyId.act?bodyId="+smsBodyId;
  openWindow(url,"查看详情",700,600) ;
}
/**
 * 
 */
function dispatchSms(seqId) {
  window.location.href = "<%=contextPath %>/core/funcs/message/sendmessage.jsp?seqId=" + seqId;
}
function deleteSmsById(bodyId) {
  if(!confirmDel()) {
    return ;
  } 
  var url = "<%=contextPath %>/yh/core/funcs/message/act/YHMessageAct/delMessage.act";
  var rtJson = getJsonRs(url, "bodyId=" + bodyId + "&deType=2");
  if (rtJson.rtState == "0") {
	    window.location.href="sentboxMessage.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  } 
}
function resetSms(smsBodyId,content,smsType,remindUrl){
  var toId = "";
  var url = "<%=contextPath%>/yh/core/funcs/message/act/YHMessageAct/getMessageToId.act";
  var rtJson = getJsonRs(url, "bodyId=" + smsBodyId);
  if (rtJson.rtState == "0") {
    toId = rtJson.rtData;
  }else{
     return;
   }
  var contentValue =  content.replace(/\"/g,"\\\"").replace(/\'/g,"\\\'").replace(/[\n\r\f]/g,"<br>");
  var url = "<%=contextPath%>/yh/core/funcs/message/act/YHMessageAct/addMessageBody.act";
  var rtJson = getJsonRs(url, "user="+toId+"&content="+encodeURIComponent(contentValue)+"&smsType="+smsType+"&remindUrl="+encodeURIComponent(remindUrl));
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
    window.location.reload();
  }
}
function smsSend(fromId){
  var URL = "<%=contextPath %>/core/funcs/message/smsback.jsp?fromId=" + fromId;
  if(typeof(window.external.OA_SMS) != 'undefined' ){  //在精灵中打开
      window.external.OA_SMS(URL,"","OPEN_URL");
  } else{
     openDialog(URL,'700', '300');
  }
}
/**
 * 查看详情
 */
function Details(url,id,type){
  gotoURLSms(url,id,type);
}
var pageNo = "<%=pageNo%>";
var pageSize = "<%=pageSize%>";
var totlePage = "";
var im = "<%=im%>";

function doInit(){

  //如果是在im中打开,则设置宽度为100%
  if (im) {
    $$('table')[0].setStyle({width: '100%'});
  }
  
  var url = "<%=contextPath%>/yh/core/funcs/message/act/YHMessageAct/messageSentbox.act?pageNo=" + pageNo  + "&pageSize=" + pageSize;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    var promStr = "";
    var total=rtJson.rtData.sizeNo;
   
    if(rtJson.rtData.data.length<=0){
      $("non_recive").style.display="block";
    }
    if(rtJson.rtData.data.length < 1){
      return;
    }
    pageNo = rtJson.rtData.pageNo;
    pageSize = rtJson.rtData.pageSize;
 
    totlePage = (rtJson.rtData.sizeNo%pageSize == 0)?Math.floor(rtJson.rtData.sizeNo/pageSize) - 1 : Math.floor(rtJson.rtData.sizeNo/pageSize);
    for(var i = 0; i < rtJson.rtData.data.length; i++){
      var obj = rtJson.rtData.data[i];
      promStr += toPanelSentBoxStr(obj);
     // ids.push(obj.bodyIds);
    }
    $('sms_body').insert(promStr,'content');
    
 
     
    
    //绑定所有的
  /*   for(var i = 0; i < rtJson.rtData.data.length; i++){
      var obj = rtJson.rtData.data[i];
     // bindDesc([{cntrlId:"type_" + obj.bodyId, dsDef:"CODE_ITEM,CLASS_CODE,CLASS_DESC,SMS_REMIND"}]); 

      bindDesc([{cntrlId:"fromId_show" + obj.bodyId, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
    } */
    if(pageNo > 0){
       $('prec').style.display = "";
    }else{
      $('next').style.display = "none";
    }
    if(pageNo < totlePage){
      $('next').style.display = "";
    }else{
      $('next').style.display = "none";
    }
  }
}
function doPrec(){
  var precNo = 0;
  if(pageNo - 1 < 0){
    precNo = 0;
  }else{
    precNo = pageNo - 1;
   }
  location = "<%=contextPath %>/core/funcs/message/jpanel/sentboxMessage.jsp?pageNo=" + precNo + "&pageSize=" + pageSize;
}
function doNext(){
  var nextNo = 0;
  if(pageNo + 1 > totlePage){
    nextNo = totlePage;
  }else{
    nextNo = pageNo + 1;
   }
  var url  = "<%=contextPath %>/core/funcs/message/jpanel/sentboxMessage.jsp?pageNo=" + nextNo + "&pageSize=" + pageSize;
  location = url;
}
</script>
<title>微讯箱</title>
</head>
<body onload="doInit()" style="background-color:transparent;">
<table  class="sms-box">
  <tr> 
    <td><div id="sms_body"></div></td>
  </tr>
  <tr>
    <td align="center"><div id="sms_op">
    <div align="center">
    <input id= "prec" type="button"  value="上一页" class="BigButton" onClick="doPrec();" style="display:none"/>
    <input id= "next" type="button"  value="下一页" class="BigButton" onClick="doNext();" style="display:none"/>
    <br><br>
   <!--  <input type="button"  value="删除以上微讯" class="BigButtonC" onClick="deleteAllCheckBoxs()" title="删除以上列出的微讯"/> -->
</div>
    </div></td>
  </tr>
</table>
<div style="display:none" align="center" id="non_recive">暂无收讯</div>
<div id="bottom"></div>
 
<script> 
function delete_all()
{
 msg="确认要删除以上列出的微讯吗？";
 if(window.confirm(msg))
 {
  URL="delete_all.php?SMS_ID_STR=156,151,152,150,148,&DEL_TYPE=1&start=0";
  window.location=URL;
 }
}
</script>
 
</body>
</html>
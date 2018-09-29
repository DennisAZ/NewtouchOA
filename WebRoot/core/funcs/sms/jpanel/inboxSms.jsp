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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/smsutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/sms.js"></script>
<script type="text/javascript">
function deleteSms(bodyId) {
  if(!confirmDel()) {
    return ;
  } 
  var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/delSms.act";
  var rtJson = getJsonRs(url, "bodyId=" + bodyId + "&deType=1");
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
  //alert(seqID);
  window.location.href = "<%=contextPath %>/core/funcs/sms/sendsms.jsp?seqId="+seqID+"&formId=" + formId;
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

/**
 * 回复
 */
function SmsBack(seqId,fromId){
  var from = fromId;
  var URL = "<%=contextPath %>/core/funcs/sms/smsback.jsp?seqId=" + seqId + "&fromId=" + from;
  openDialog(URL,'700', '300');
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

  
  var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/smsInbox.act?pageNo=" + pageNo  + "&pageSize=" + pageSize;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    var promStr = "";
    if(rtJson.rtData.pageData.length < 1){
      return;
    }
    totlePage = (rtJson.rtData.totalRecord%pageSize == 0)?Math.floor(rtJson.rtData.totalRecord/pageSize) - 1 : Math.floor(rtJson.rtData.totalRecord/pageSize);
    for(var i = 0; i < rtJson.rtData.pageData.length; i++){
      var obj = rtJson.rtData.pageData[i];
      promStr += toPanelInBoxStr(obj);
     // ids.push(obj.bodyIds);
    }
    $('sms_body').insert(promStr,'content');
    //绑定所有的
    for(var i = 0; i < rtJson.rtData.pageData.length; i++){
      var obj = rtJson.rtData.pageData[i];
      bindDesc([{cntrlId:"type_" + obj.smsBodyId, dsDef:"CODE_ITEM,CLASS_CODE,CLASS_DESC,SMS_REMIND"}]); 
      bindDesc([{cntrlId:"fromId_" + obj.smsBodyId, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
    }
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
  location = "<%=contextPath %>/core/funcs/sms/jpanel/inboxSms.jsp?pageNo=" + precNo + "&pageSize=" + pageSize;
}
function doNext(){
  var nextNo = 0;
  if(pageNo + 1 > totlePage){
    nextNo = totlePage;
  }else{
    nextNo = pageNo + 1;
   }
  var url  = "<%=contextPath %>/core/funcs/sms/jpanel/inboxSms.jsp?pageNo=" + nextNo + "&pageSize=" + pageSize;
  location = url;
}
</script>
<title>短信箱</title>
</head>
<body onload="doInit()" style="background-color:transparent;">
<table class="sms-box">
  <tr> 
    <td><div id="sms_body"></div></td>
  </tr>
  <tr>
    <td align="center"><div id="sms_op">
    <div align="center">
    <input id= "prec" type="button"  value="上一页" class="BigButton" onClick="doPrec();" style="display:none"/>
    <input id= "next" type="button"  value="下一页" class="BigButton" onClick="doNext();" style="display:none"/>
    <br><br>
   <!--  <input type="button"  value="删除以上短信" class="BigButtonC" onClick="deleteAllCheckBoxs();" title="删除以上列出的短信"/> -->
</div>
    </div></td>
  </tr>
</table>
<div id="bottom"></div>
 
<script> 
function delete_all()
{
 msg="确认要删除以上列出的短信吗？";
 if(window.confirm(msg))
 {
  URL="delete_all.php?SMS_ID_STR=156,151,152,150,148,&DEL_TYPE=1&start=0";
  window.location=URL;
 }
}
</script>
 
</body>
</html>
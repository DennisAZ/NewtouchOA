<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公告通知设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var day = "";
var isAuditing = "";
var RangeAll ="";
var ExceptionAll = "";
var seqIdDay = "";
var isIdAuditing = "";
var seqIdRangAll = "";
var seqIdExceptionAll = "";
function doInit(){
   var url = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/getNotify.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     document.getElementById("topDays").value = document.getElementById("paraValue").value;
     day = document.getElementById("paraName").value;
     seqIdDay = document.getElementById("seqId").value;
   }else {
     alert(rtJson.rtMsrg); 
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/getNotifySingle.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     //document.getElementById("isAuditing").value = document.getElementById("paraValue").value;
     isAuditing = document.getElementById("paraName").value;
     if(document.getElementById("paraValue").value == "1"){
       document.getElementById("isAuditing").checked = true;
     }
     isIdAuditing = document.getElementById("seqId").value;
   }else {
     alert(rtJson.rtMsrg); 
   }
   
   var url = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/getNotifyRangeAll.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     document.getElementById("rangeAllId").value = document.getElementById("paraValue").value.trim();
     seqIdRangAll = document.getElementById("seqId").value;
     RangeAll = document.getElementById("paraName").value;
     if(document.getElementById("rangeAllId").value){
       bindDesc([{cntrlId:"rangeAllId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
     }

   }else {
     alert(rtJson.rtMsrg); 
   }

   var url = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/getNotifyExceptionId.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     bindJson2Cntrl(rtJson.rtData);
     document.getElementById("rangeExceptionId").value = document.getElementById("paraValue").value;
     ExceptionAll = document.getElementById("paraName").value;
     seqIdExceptionAll = document.getElementById("seqId").value;
     if(document.getElementById("rangeExceptionId").value!=""){
       bindDesc([{cntrlId:"rangeExceptionId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
     	        ]);
     }
   }else {
     alert(rtJson.rtMsrg); 
   }
}

function commit(){
  if (document.getElementById("isAuditing").checked == true) {
    if(document.getElementById("rangeAllIdDesc").value == ""){
      alert("指定审批人员！");
      return false;
    }
  }
  /*if ($("topDays").value != "" && ($("topDays").value < 0 || $("topDays").value != parseInt($("topDays").value))){
    alert("最大置顶时间应为正整数！");
    $("topDays").focus();
    $("topDays").select();
    return false;
  }*/
  var num  = $("topDays").value;
  if ((num < 0 || !isInteger(num)) && num !="" && num != 0 || num.indexOf(".")>-1){
    alert("最大置顶时间应为正整数！");
    $("topDays").focus();
    $("topDays").select();
    return false;
  }
  var url = null;
  var rtJson = null;
  //var paraName = document.getElementById("paraName").value;
  var seqId = document.getElementById("seqId").value;
  var checkAuding = "";
  if(document.getElementById("isAuditing").checked == true){
    checkAuding = "1";
  }else{
    checkAuding = "0";
  }
  var topDays = document.getElementById("topDays").value;
  var rangeAllId = document.getElementById("rangeAllId").value;
  var rangeExceptionId = document.getElementById("rangeExceptionId").value;
  url = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/";
  if (day == "NOTIFY_TOP_DAYS") {
    url += "updateNotifyDaysd.act?topDays="+topDays+"&seqId="+seqIdDay;
  } else {
    url += "addNotifyDays.act?topDays="+topDays+"&seqId="+seqIdDay;
  }
  rtJson = getJsonRs(url, mergeQueryString($("form1")));

  var urls = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/";
  if (isAuditing == "NOTIFY_AUDITING_SINGLE") {
    urls += "updateNotifyCheck.act?checkAuding="+checkAuding+"&seqId="+isIdAuditing;
  } else {
    urls += "addNotifyCheck.act?checkAuding="+checkAuding+"&seqId="+isIdAuditing;
  }
  rtJson = getJsonRs(urls, mergeQueryString($("form1")));

  var urls = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/";
  if (RangeAll == "NOTIFY_AUDITING_ALL") {
    urls += "updateNotifyRangeAll.act?rangeAllId="+rangeAllId+"&seqId="+seqIdRangAll;
  } else {
    urls += "addNotifyRangeAll.act?rangeAllId="+rangeAllId+"&seqId="+seqIdRangAll;
  }
  rtJson = getJsonRs(urls, mergeQueryString($("form1")));

  var urlsd = "<%=contextPath%>/yh/core/funcs/system/notify/act/YHNotifyAct/";
  if (ExceptionAll == "NOTIFY_AUDITING_EXCEPTION") {
    urlsd += "updateNotifyExceptionId.act?rangeExceptionId="+rangeExceptionId+"&seqId="+seqIdExceptionAll;
  } else  {
    urlsd += "addNotifyExceptionId.act?rangeExceptionId="+rangeExceptionId+"&seqId="+seqIdExceptionAll;
  }
  rtJson = getJsonRs(urlsd, mergeQueryString($("form1")));
  //alert(rtJson.rtMsrg);
  location = "<%=contextPath %>/core/funcs/system/notify/submit.jsp";
}

function SelectUser(userId,domId){ 
  URL = contextPath + "/core/funcs/dept/userselect.jsp?TO_ID=" + userId + "&TO_NAME=" + domId; 
  openDialog(URL,'400', '350'); 
}

function ClearUser(){ 
  var args = $A(arguments); 
  for(var i = 0; i < args.length; i++ ){ 
    var cntrl = $(args[i]); 
    if(cntrl){ 
      if (cntrl.tagName.toLowerCase() == "td" 
        || cntrl.tagName.toLowerCase() == "div" 
        || cntrl.tagName.toLowerCase() == "span") { 
        cntrl.innerHTML = ''; 
      } else{ 
        cntrl.value =''; 
      } 
    } 
  } 
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 公告通知设置</span><br>
    </td>
  </tr>
</table>
<form name="form1" id="form1"  enctype="multipart/form-data" method="post">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.notify.data.YHNotify.java"/>
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" name="paraName" id="paraName" value="">
<input type="hidden" name="paraValue" id="paraValue" value="">
<table class="TableBlock" width="60%" align="center">
<tr>
   <td nowrap class="TableContent">公告最大置顶时间</td>
   <td class="TableData"><input type="text" name="topDays" id="topDays" size="3" maxlength="4" class="BigInput" value="">&nbsp;天 &nbsp;&nbsp;0或空表示不限制置顶时间
   </td>
</tr>
<tr>
   <td nowrap class="TableContent">发布公告是否需要审批</td>
   <td class="TableData"><input type="checkbox" id="isAuditing" name="isAuditing">需要审批
   </td>
</tr>
<tr>
   <td nowrap class="TableContent">指定可审批公告人员</td>
   <td class="TableData">
   <input type="hidden" name="rangeAllId" id="rangeAllId" value="">
   <textarea cols=40 name="rangeAllIdDesc" id="rangeAllIdDesc" rows="6" class="BigStatic" wrap="yes" readonly></textarea><a href="javascript:;" class="orgAdd" onClick="selectUser(['rangeAllId', 'rangeAllIdDesc'], 5)">添加</a>
   <a href="javascript:;" class="orgClear" onClick="ClearUser('rangeAllId', 'rangeAllIdDesc')">清空</a></td>
</tr>
<tr>
   <td nowrap class="TableContent">指定无需审批人员</td>
   <td class="TableData">
   <input type="hidden" name="rangeExceptionId" id="rangeExceptionId" value="">
   <textarea cols=40 name="rangeExceptionIdDesc" id="rangeExceptionIdDesc" rows="6" class="BigStatic" wrap="yes" readonly></textarea><a href="javascript:;" class="orgAdd" onClick="selectUser(['rangeExceptionId', 'rangeExceptionIdDesc'], 5)">添加</a>
   <a href="javascript:;" class="orgClear" onClick="ClearUser('rangeExceptionId', 'rangeExceptionIdDesc')">清空</a></td>
</tr>
<tr>
   <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button" value="确定" class="BigButton" onclick="commit()">&nbsp;&nbsp;
   </td>
  </tr>
</table>
</form>

</body>
</html>
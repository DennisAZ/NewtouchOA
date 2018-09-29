<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkDetail"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.core.funcs.person.data.YHPerson" %>
<%  int sunNum = Integer.parseInt(request.getAttribute("sunWork").toString()); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>进度图日志</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">

<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var upload_limit = 1,limit_type = limitUploadFiles;
var oa_upload_limit = limitUploadFiles;
var swfupload;
function doInit() {
  work();
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    if($('attachmentName_' + i)) {
      attachMenuUtil("showAtt_" + i ,"work_plan",null,$('attachmentName_' + i).value ,$('attachmentId_' + i).value,true, i);
    }
  }
  doInit3();
  moblieSmsRemind('sms2Remind','sms2Check');
}
function uploadSuccessOver(file, serverData) {
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
//  progress.setComplete();
//  progress.setStatus("Complete.");
  progress.toggleCancel(false);
  var json = null;
  json = serverData.evalJSON();
  if (json.state == "1") {
     progress.setError();
     progress.setStatus("上传失败：" + serverData.substr(5));
     
     var stats = this.getStats();
     stats.successful_uploads--;
     stats.upload_errors++;
     this.setStats(stats);
  } else {
  $('attachmentId').value += json.data.attachmentId;
    $('attachmentName').value += json.data.attachmentName;
    var attachmentIds = $("attachmentId").value;
    var attachmentName = $("attachmentName").value;
    var ensize =  $('ensize').value;
    if (ensize) {
    $('ensize').value =(json.data.size + parseInt(ensize));
  }else {
  $('ensize').value =json.data.size ;
    }//附件大小
    showAttach(attachmentIds,attachmentName,"showAtt");
  }
} catch (ex) {
  this.debug(ex);
}
}
/**
* 处理附件的显示

* @param cntrlId
* @return
*/
function showAttach(attrIds,attrNames,cntrId) {
  var reStr = "<div id='attrDiv'>";
  var ym = "";
  var attrId = ""
  var attrIdArrays = attrIds.split(",");
  var attrNameArrays = attrNames.split("*");
  for (var i = 0 ; i <= attrIdArrays.length; i++) {
    if (!attrIdArrays[i]) {
      continue;
    }
    var key = attrIdArrays[i];
    var attrName = attrNameArrays[i];
    var value = attrName.substring( attrName.indexOf("_")+1, attrName.length);
    reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\">" + value + "</a><br>";
    //MODULE=email&amp;YM=1001&amp;ATTACHMENT_ID=216664316&amp;ATTACHMENT_NAME=SoftMgrUninst.exe
  }
  reStr += "</div>";
  if (cntrId) {
    $(cntrId).innerHTML = reStr;
  }else {
    document.write(reStr);
  }
}
function upload_attach()
{
    $("btnFormFile").click();
}
function handleSingleUpload(rtState, rtMsrg, rtData) {
//var data = rtData.evalJSON(); 
  //$('attachmentId').value += data.attrId;
  //$('attachmentName').value += data.attrName;
  //showAttach($('attachmentId').value,$('attachmentName').value ,"showAtt");
  //$('attr_tr').style.display = ''; 
  //removeAllFile();

  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  //showAttach($('returnAttId').value,$('returnAttName').value ,"attr",false);
  attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,false,'pre');
  //$('attr_tr').style.display = ''; 
  removeAllFile();
  if (isUploadBackFun) {
    checkForm2(isPlanId);
    isUploadBackFun = false;
  }
}

function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var attachNameOld = $('attachmentName').value; 
  var attachIdOld = $('attachmentId').value; 
  var attachNameArrays = attachNameOld.split("*"); 
  var attachIdArrays = attachIdOld.split(","); 
  var attaName = ""; 
  var attaId = ""; 
  for(var i = 0 ; i < attachNameArrays.length ; i++) { 
    if(!attachIdArrays[i] || attachIdArrays[i] == attachId) { 
      continue; 
    }
    attaName += attachNameArrays[i] + "*"; 
    attaId += attachIdArrays[i] + ","; 
  } 
    $('attachmentId').value = attaId; 
    $('attachmentName').value = attaName; 
    return true;
}


function work() {
  var now = new Date(); 
  var year = now.getFullYear();
  var month = now.getMonth()+1;
  var da = now.getDate();
  var hour = now.getHours(); 
  var minute = now.getMinutes();
  var second = now.getSeconds();
  if (eval(hour) < 10) { 
    hour = "0" + hour;
  }
  if (eval(month) < 10) {
    month = "0" + month;
  }
  if (eval(da) < 10) {
    da= "0" + da;
  }
  if (eval(minute) < 10) {
    minute = "0" + minute;
  }
  if (eval(second) < 10) {
    second= "0" + second;
  }
  if (hour >= 12) {
    document.getElementById("WRITE_TIME").value = year + "-" + month + "-" + da + "  " + hour + ":" + minute + ":" + second;
  }else {
    document.getElementById("WRITE_TIME").value = year + "-" + month + "-" + da + "  " + hour + ":" + minute + ":" + second;
  }
}
function checkForm() { 
  var num = /^[0-9]*$/;
  if (!$("PERCENT").value) { 
    alert("估计完成量与总量的百分比不能为空！");
    selectLast($("PERCENT")); 
    return false;
  }
  if (!num.exec(document.getElementById("PERCENT").value)) { 
    alert("估计完成量与总量的百分比只能为数字！");
    selectLast($("PERCENT")); 
    return false;
  }
  if (parseInt(document.getElementById("PERCENT").value) > 100) { 
    alert("估计完成量与总量的百分比只能为0~100！");
    selectLast($("PERCENT")); 
    return false;
  }
  if (parseInt(document.getElementById("PERCENT").value) <= parseInt(document.getElementById("sunNum").value)) { 
    alert("估计完成量不能比上次少！");
    selectLast($("PERCENT")); 
    return false;
  }
  return true;
}
var isPlanId;
var isUploadBackFun = false;
function checkForm2(planId) {
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      isPlanId= planId;
      return ;
    }
    var pars = $('form1').serialize() ;
    var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/addDetaiId.act";
    var json = getJsonRs(url,pars);
    if (json.rtState == "1") {
      alert(json.rtMsrg);
    }else {
      window.location = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId.act?seqId=" + planId;
    }
  }
}
function jugeFile(){
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for(var i=0; i<inputDoms.length; i++){
    var idval = inputDoms[i].id;
    if(idval.indexOf("ATTACHMENT")!=-1){
      return true;
    }
  } 
  return false; 
}

function deleteId2(seqId,planId) {
  var msg = "确认要删除该进度日志吗?";
  if (window.confirm(msg)) {
  window.location = "<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkDetailAct/deleteDetaiId.act?seqId=" + seqId + "&planId=" + planId;
  } 
}
function updateId(seqId,sqlName) {
  window.location = "<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectId2.act?seqId=" + seqId + "&sqlName=" + sqlName;
}
function checkBox2() {
  if (document.getElementById("smsfla").checked) {
    document.getElementById("smsflag").value = "true";
  }else {
    document.getElementById("smsflag").value = "false";
  }
}
function checkTrue() {
  if (document.getElementById("WRITE_IN_WORK").checked) {
    document.getElementById("chi").value = "true";
  }else {
    document.getElementById("chi").value = "false";
  }
}
function doInit3() {
  var lenght = parseInt($('count').value);
  for(var i = 0; i < lenght ; i++) {
    var writerCoun = "writerName" + i; 
    var writerCounVlue = document.getElementById + "(writerName" + i + ")";
    if(writerCounVlue.value != ""){
      bindDesc([{cntrlId:writerCoun, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }
}
function InsertImage(src) { 
return;
}

function checkBox3() {
  if (document.getElementById("sms2Check").checked) {
     document.getElementById("sms2flag").value = "true";
  }else {
   document.getElementById("sms2flag").value = "false";
  }
}
/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
   var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=12";
   var rtJson = getJsonRs(requestUrl); 
   if (rtJson.rtState == "1"){ 
     alert(rtJson.rtMsrg); 
     return ; 
   }
   var prc = rtJson.rtData;
   var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
   if (moblieRemindFlag == '2') {
     $(remidDiv).style.display = ''; 
     $(remind).checked = true;
     document.getElementById("sms2flag").value = "true";
   } else if(moblieRemindFlag == '1') { 
     $(remidDiv).style.display = ''; 
     $(remind).checked = false; 
   }else{
     $(remidDiv).style.display = 'none'; 
   }
 }
</script>
</head>
<body topmargin="5" onload="doInit()">
<form name="form1" id="form1">
<%
  YHWorkDetail detail = new YHWorkDetail();
  List<YHWorkDetail> list = (List<YHWorkDetail>)request.getAttribute("listId");
  YHWorkPlan plan = (YHWorkPlan)request.getAttribute("plan");
  
  Date date = new Date();
  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
  String time = sf.format(date);
  String year = sf.format(date).substring(0,4);
  String mod = sf.format(date).substring(5,7);
  String day = sf.format(date).substring(8,10);
  
  int endTime1 = Integer.parseInt(year);//当前时间
  int endTime2 = Integer.parseInt(mod);
  int endTime3 = Integer.parseInt(day);
  
  String eTime = null;
  String eTime2 = null;
  String eTime3 = null;
  
  String begin1 = null;
  String begin2 = null;
  String begin3 = null;
  
  int time1 = 0;//结束时间
  int time2 = 0;
  int time3 = 0;
  
  int beginTime1 = 0;
  int beginTime2 = 0;
  int beginTime3 = 0;
  
  boolean flag = false;
  boolean personFlage = false;
  boolean personFlage2 = false;
  int len = 0;
  int len2 = 0;
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String sqlName = plan.getCreator() + ",";
  if (!YHUtility.isNullorEmpty(plan.getLeader1Desc())) {
    sqlName += plan.getLeader1Desc() + ",";
    len = plan.getLeader1Desc().split(",").length;
    for (int i = 0 ; i < len; i++) {
      if (plan.getLeader1Desc().split(",")[i].equals(String.valueOf(person.getSeqId()))) {
        flag = true;
      }
    }
  }
  if (!YHUtility.isNullorEmpty(plan.getLeader2Desc())) {
    sqlName += plan.getLeader2Desc() + ",";
    len2 = plan.getLeader2Desc().split(",").length;
    for (int j = 0 ; j < len2; j++) {
      if (plan.getLeader2Desc().split(",")[j].equals(String.valueOf(person.getSeqId()))) {
        flag = true;
      }
    }
  }
  if (person.getSeqId()==1) {
    flag = true;
  }
  if (plan.getEndTime() != null) {
    eTime = String.valueOf(plan.getEndTime()).substring(0,4);
    eTime2 = String.valueOf(plan.getEndTime()).substring(5,7);
    eTime3 = String.valueOf(plan.getEndTime()).substring(8,10);
    time1 = Integer.parseInt(eTime);
    time2 = Integer.parseInt(eTime2);
    time3 = Integer.parseInt(eTime3);
  }
  if (plan.getStatrTime() != null) {
    begin1 = String.valueOf(plan.getStatrTime()).substring(0,4);
    begin2 = String.valueOf(plan.getStatrTime()).substring(5,7);
    begin3 = String.valueOf(plan.getStatrTime()).substring(8,10);
    beginTime1 = Integer.parseInt(begin1);
    beginTime2 = Integer.parseInt(begin2);
    beginTime3 = Integer.parseInt(begin3);
  }
  if (plan.getEndTime() != null) {
    if (plan.getPublish().equals("1") && (time1 > endTime1 || (time1 == endTime1 && time2 > endTime2) || (time1 == endTime1 && time2 == endTime2 && time3 >= endTime3)) && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
      personFlage2 = true;
    }
  }
  if (plan.getEndTime() == null) {
    if (plan.getPublish().equals("1") && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
      personFlage2 = true;
    }
  }
  if (list.size() > 0 && personFlage2) {
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" width="22"
   height="18">&nbsp;<span class="big3"> 进度日志详情(<%=plan.getName() %>&nbsp;&nbsp;<%if(plan.getStatrTime() != null){%><%=plan.getStatrTime() %><%} %>&nbsp;至&nbsp;<%if(plan.getEndTime() != null){%><%=plan.getEndTime() %><%} %>)</span></td>
 </tr>
</table>
<br>
<table class="TableList" width="95%" align="center">
 <tr class="TableHeader">
  <td nowrap align="center">作者</td>
  <td nowrap align="center">内容</td>
  <td nowrap align="center">附件</td>
  <td nowrap align="center">日志时间</td>
  <td nowrap align="center">进度百分比</td>
  <td nowrap align="center">操作</td>
 </tr>
 <%for (int i = 0; i < list.size() ; i++) {
   detail = list.get(i);
   %>
 <tr class='TableLine1'>
  <td align="center"><%if(detail.getWriter()!=null) {%>
    <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="<%=detail.getWriter()%>">
    <%
   }else {
 %>
 <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="">
 <%} %>
  </td>
  <td align="center" width="20%"><%if(detail.getProgress()!=null) {%><%=detail.getProgress() %><%} %></td>
  <td align="center"><%if(detail.getAttachmentName()!=null) {%> 
  <input type="hidden" id="attachmentId_<%=i %>" name="attachmentId_<%=i %>" value="<%=detail.getAttachmentId()%>">
        <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName_<%=i %>" value="<%=detail.getAttachmentName() %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt_<%=i %>"></span>
  <%}else {%>&nbsp;<%} %>
  </td>
  <td align="center"><%if(detail.getWriteTime()!=null) {%><%=detail.getWriteTime() %><%} %></td>
  <td align="center"><%=detail.getPercent() %>%</td>
  <td align="center"><%if(detail.getWriter().equals(String.valueOf(person.getSeqId())) || (person.getSeqId()==1)){%><a href="javascript:updateId('<%=detail.getSeqId()%>','<%=sqlName%>')"> 修改</a> <a href="javascript:deleteId2('<%=detail.getSeqId()%>','<%=plan.getSeqId()%>');"> 删除</a><%} %></td>
 </tr>
 <%} %>
</table>
<%}else {%>
<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>没有进度日志
</div>
</td></tr>
</table>
<% } if (plan.getEndTime() != null) {
  if (flag && plan.getPublish().equals("1") && (time1 > endTime1 || (time1 == endTime1 && time2 > endTime2) || (time1 == endTime1 && time2 == endTime2 && time3 >= endTime3)) && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
    personFlage = true;
  %>
  <br>
<table border="0" width="100%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22"
   HEIGHT="20"><span class="big3"> 添加进度日志</span></td>
 </tr>
</table>
<br>
<table class="TableBlock" align="center">
 <tr>
  <td nowrap class="TableContent" width="90">&nbsp;当前时间：</td>
  <td class="TableData"><input type="text" name="WRITE_TIME" id="WRITE_TIME"
   size="19" readonly maxlength="100" class="BigStatic" value="">
   <input type="hidden" name="sqlName" id="sqlName" value="<%=sqlName%>">
  </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">&nbsp;完成百分比：</td>
  <td class="TableData" colspan="1"><input type="text"
   name="PERCENT" id="PERCENT" size="2" class="BigInput" value="" maxlength="3"> <font
   size="3"></font>上次进度&nbsp;&nbsp;<%=sunNum%>%&nbsp;&nbsp;
   (注:估计完成量与总量的百分比)不能有小数点 
   <input type="hidden" value="<%=sunNum%>" id="sunNum" name="sunNum">
   </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">&nbsp;进度详情：</td>
  <td class="TableData" colspan="1"><textarea name="PROGRESS" id="PROGRESS"
   class="BigInput" cols="55" rows="5"></textarea></td>
 </tr>
<tr id="attr_tr">
      <td nowrap class="TableContent">&nbsp;附件: </td>
      <td class="TableData">
      <input type="hidden" name="PLAN_ID"  id="PLAN_ID" value="<%=plan.getSeqId()%>">
        <input type="hidden" id="attachmentId" name="attachmentId" value="">
        <input type="hidden" id="attachmentName" name="attachmentName" value="">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="writer" name="writer" value="系统管理员">
        <span id="showAtt"></span>
        </td>
    </tr>
      <tr>
      <td nowrap class="TableContent">&nbsp;附件选择：</td>
      <td class="TableData" id="fsUploadRow">
           <div id="fsUploadArea" class="flash" style="width:380px;">
             <div id="fsUploadProgress"></div>
             <div id="totalStatics" class="totalStatics"></div>
             <div id="Divs">
               <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onClick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
               <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onClick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
            <input type="button" class="SmallButtonW" value="刷新页面" onClick="location.reload();">
            </div>
            </div>
            <div id="attachment1">
      <script>ShowAddFile();</script>
       <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
          <input type="hidden" name="attachmentId" value="">
          <input type="hidden" name="attachmentName" value=""> 
          <div style="display:none"><span id="spanButtonUpload" title="批量上传附件"> </span></div></div>
            </td>
    </tr> 
 <tr>
   <td nowrap class="TableContent">提醒：</td>
  <td class="TableData"><div>&nbsp;<input type='checkbox'name='smsfla' id='smsfla' onClick='checkBox2();' checked>
<label for='SMS_REMIND'>使用内部短信提醒</label>
 <input type='hidden'  name="smsflag" id="smsflag" value="true"></div>
  <div id="sms2Remind">&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" value="1"  onClick="checkBox3();">
<label for="SMS_REMIND">使用手机短信提醒</label>&nbsp;&nbsp;
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
 </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">是否写入工作日志：</td>
  <td class="TableData"><input type="checkbox" name="WRITE_IN_WORK"
   id="WRITE_IN_WORK" OnClick="checkTrue()"> (注意：勾选会将进度详情写入工作日志中)
   <input type="hidden" name="chi" id="chi" value='false'>
   </td>
 </tr>
</table>
<table align="center">
 <tr>
  <td colspan="2" align="center"><input
   type="button" value="确定" class="BigButton" onClick="checkForm2('<%=plan.getSeqId()%>')">
  &nbsp;&nbsp;<input type="button"
   class="BigButton" value="关闭" onClick="window.close();" title="关闭窗口">&nbsp;&nbsp;</td>
  </tr>
</table>
<%}}if (plan.getEndTime() == null) {
  if (flag && plan.getPublish().equals("1") && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))) {
    personFlage = true;
  %>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22"
   HEIGHT="20"><span class="big3">添加进度日志</span></td>
 </tr>
</table>
<br>
<table class="TableBlock" align="center">
 <tr>
  <td nowrap class="TableContent" width="90">&nbsp;当前时间：</td>
  <td class="TableData"><input type="text" name="WRITE_TIME" id="WRITE_TIME"
   size="19" readonly maxlength="100" class="BigStatic" value="">
   <input type="hidden" name="sqlName" id="sqlName" value="<%=sqlName%>">
  </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">&nbsp;完成百分比：</td>
  <td class="TableData" colspan="1"><input type="text"
   name="PERCENT" id="PERCENT" size="2" class="BigInput" value="" maxlength="3"> <font
   size="3"></font>上次进度&nbsp;&nbsp;<%=sunNum%>%&nbsp;&nbsp;
   (注:估计完成量与总量的百分比)不能有小数点 
   <input type="hidden" value="<%=sunNum%>" id="sunNum" name="sunNum">
   </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">&nbsp;进度详情：</td>
  <td class="TableData" colspan="1"><textarea name="PROGRESS" id="PROGRESS"
   class="BigInput" cols="55" rows="5"></textarea></td>
 </tr>
<tr id="attr_tr">
      <td nowrap class="TableContent">&nbsp;附件: </td>
      <td class="TableData">
      <input type="hidden" name="PLAN_ID"  id="PLAN_ID" value="<%=plan.getSeqId()%>">
        <input type="hidden" id="attachmentId" name="attachmentId" value="">
        <input type="hidden" id="attachmentName" name="attachmentName" value="">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="writer" name="writer" value="系统管理员">
        <span id="showAtt"></span>
        </td>
    </tr>
      <tr>
      <td nowrap class="TableContent">&nbsp;附件选择：</td>
      <td class="TableData" id="fsUploadRow">
           <div id="fsUploadArea" class="flash" style="width:380px;">
             <div id="fsUploadProgress"></div>
             <div id="totalStatics" class="totalStatics"></div>
             <div id="Divs">
               <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onClick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
               <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onClick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
            <input type="button" class="SmallButtonW" value="刷新页面" onClick="location.reload();">
            </div>
            </div>
             <div id="attachment1">
      <script>ShowAddFile();</script>
       <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
          <input type="hidden" name="attachmentId" value="">
          <input type="hidden" name="attachmentName" value=""> 
          <div style="display:none"><span id="spanButtonUpload" title="批量上传附件"> </span></div></div>
            </td>
    </tr> 
 <tr>
  <td nowrap class="TableContent">&nbsp;提醒：</td>
  <td class="TableData"><div><input type='checkbox' name='smsfla' id='smsfla' onClick='checkBox2();' checked>
<label for='SMS_REMIND'>使用内部短信提醒</label>
 <input type='hidden'  name="smsflag" id="smsflag" value="true"></div>
 <div id="sms2Remind"><input type="checkbox" name="sms2Check" id="sms2Check" value="1"  onClick="checkBox3();">
<label for="SMS_REMIND">使用手机短信提醒</label>&nbsp;&nbsp;
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
 </td>
 </tr>
 <tr>
  <td nowrap class="TableContent">&nbsp;是否写入工作日志：</td>
  <td class="TableData"><input type="checkbox" name="WRITE_IN_WORK"
   id="WRITE_IN_WORK" OnClick="checkTrue()"> (注意：勾选会将进度详情写入工作日志中)
   <input type="hidden" name="chi" id="chi" value="false">
   </td>
 </tr>
</table>
<table align="center">
 <tr>
  <td colspan="2" align="center"><input
   type="button" value="确定" class="BigButton" onClick="checkForm2('<%=plan.getSeqId()%>')">
  &nbsp;&nbsp;<input type="button"
   class="BigButton" value="关闭" onClick="window.close();" title="关闭窗口">&nbsp;&nbsp;</td>
  </tr>
</table>
<%}}if (!personFlage) {%>
<table align="center">
 <tr>
  <td colspan="2" align="center"><input type="button"
   class="BigButton" value="关闭" onClick="window.close();" title="关闭窗口">&nbsp;&nbsp;</td>
  </tr>
</table>
<div id="sms2Remind" style="display:none">&nbsp;<input style="display:none" type="checkbox" name="sms2Check" id="sms2Check" value="1">
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
<%}%>
</form>
<form id="formFile" action="<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
  <input type='hidden' id=count name=count value="<%=list.size() %>"/>
  <input type="hidden" name="WRITE_TIME" id="WRITE_TIME"
   size="19" readonly maxlength="100" class="BigStatic" value="">
   <input type="hidden" id="moduel" name="moduel" value="work_plan">
</body>
</html>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.core.funcs.workplan.data.YHWorkDetail"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<%@page import="yh.core.funcs.person.data.YHPerson" %>
<%
  YHWorkDetail detail = new YHWorkDetail();
  List<YHWorkDetail> list = (List<YHWorkDetail>)request.getAttribute("list");
  YHWorkPlan plan = (YHWorkPlan)request.getAttribute("plan");
  
  boolean flag = false;
  boolean flag2 = true;
  int len = 0;
  int len2 = 0;
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  
  if("0".equals(plan.getDeptParentDesc())){
    flag = true;
    flag2 = false;
  }
  
  if((","+plan.getManagerDesc()+",").contains((","+person.getSeqId()+",")) || (","+plan.getDeptParentDesc()+",").contains((","+person.getDeptId()+","))){
    flag = true;
    flag2 = false;
  }
  //plan.getDeptParentDesc();
  
//参与人
  if (!YHUtility.isNullorEmpty(plan.getLeader1Desc())) {
    len = plan.getLeader1Desc().split(",").length;
    for (int i = 0 ; i < len; i++) {
      if (plan.getLeader1Desc().split(",")[i].equals(String.valueOf(person.getSeqId()))) {
        flag = true;
        flag2 = false;
      }
    }
  }
//批注领导
  if (!YHUtility.isNullorEmpty(plan.getLeader3Desc())) {
    len = plan.getLeader3Desc().split(",").length;
    for (int i = 0 ; i < len; i++) {
      if (plan.getLeader3Desc().split(",")[i].equals(String.valueOf(person.getSeqId()))) {
        flag = true;
        flag2 = true;
      }
    }
  }
//负责人
  if (!YHUtility.isNullorEmpty(plan.getLeader2Desc())) {
    len2 = plan.getLeader2Desc().split(",").length;
    for (int j = 0 ; j < len2; j++) {
      if (plan.getLeader2Desc().split(",")[j].equals(String.valueOf(person.getSeqId()))) {
        flag = true;
        flag2 = true;
      }
    }
  }
  if (!YHUtility.isNullorEmpty(plan.getCreator())) {
      if (plan.getCreator().equals(String.valueOf(person.getSeqId()))) {
        flag = true;
        flag2 = true;
      }
  }
%>
<html>
<head>
<title>添加批注</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
  function doInit() {
    if(<%=flag2 %>){
	    doInit2();   
	    work();
	    moblieSmsRemind('sms2Remind','sms2Check');
    }
    
    var lenght = parseInt($('count').value);
    for(var i = 0; i < lenght ; i++) {
      if($('attachmentName_' + i)) {
        attachMenuUtil("showAtt_" + i ,"work_plan",null,$('attachmentName_' + i).value ,$('attachmentId_' + i).value,true, i);
      }
    }
    doInit3();
    
  }
</script>
<script type="text/javascript">
  function checkBox2() {
    if (document.getElementById("smsfla").checked) {
      document.getElementById("smsflag").value = "true";
    }else {
      document.getElementById("smsflag").value = "false";
    }
  }
  function work() {
    var now = new Date(); 
    var year = now.getYear();
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
  function updateFugle(seqId) {
    window.location = "<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectId.act?seqId=" + seqId;
  }
  function deleteDetail(seqId,planId) {
    var msg = "确认要删除该批注吗?";
    if (window.confirm(msg)) {
    window.location = "<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/deleteDetai.act?seqId=" + seqId + "&planId=" + planId;
    }   
  }
  function checkForm() {   
    if (!$("PROGRESS").value) {
      alert("内容不能为空！");
      selectLast($("PROGRESS")); 
      return false;
    }
    return true;
  }
  var isPlanId;
  var isUploadBackFun = false;
  function checkForm2(planId) {  
    if (checkForm()) {
      if(jugeFile()){//如果有没有上传的文件，则进行上传
        $("formFile").submit();
        isUploadBackFun = true;
        isPlanId = planId;
        return ;
      }
      var pars = $('form1').serialize() ;
      var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/addDetai.act";
      var json = getJsonRs(url,pars);
      if(json.rtState == "1"){
        alert(json.rtMsrg);
      }else {
        window.location = contextPath + "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + planId
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
  var upload_limit = 1,limit_type = limitUploadFiles;
  var oa_upload_limit =limitUploadFiles;
  var swfupload;
  function doInit2() {
    var linkColor = document.linkColor;
    var settings = {
      flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
      upload_url: "<%=contextPath %>/yh/core/funcs/notify/act/YHNotifyHandleAct/fileLoad.act",
      post_params: {"PHPSESSID" : "<%=session.getId()%>"},
      file_size_limit : (maxUploadSize + " MB"),
      file_types : "*.*",
      file_types_description : "All Files",
      file_upload_limit : 100,
      file_queue_limit : 0,
      custom_settings : {
        uploadRow : "fsUploadRow",
        uploadArea : "fsUploadArea",
        progressTarget : "fsUploadProgress",
        startButtonId : "btnStart",
        cancelButtonId : "btnCancel"
      },
      debug: false,
      button_image_url: "<%=imgPath %>/uploadx4.gif",
      button_width: "65",
      button_height: "29",
      button_placeholder_id: "spanButtonPlaceHolder",
      button_text: '<span class=\"textUpload\">批量上传附件</span>',
      button_text_style: ".textUpload{color:" + linkColor + ";}",
      button_text_top_padding : 1,
      button_text_left_padding : 18,
      button_placeholder_id : "spanButtonUpload",
      button_width: 160,
      button_height: 18,
      button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
      button_cursor: SWFUpload.CURSOR.HAND,
      
      file_queued_handler : fileQueued,
      file_queue_error_handler : fileQueueError,
      file_dialog_complete_handler : fileDialogComplete,
      upload_start_handler : uploadStart,
      upload_progress_handler : uploadProgress,
      upload_error_handler : uploadError,
      upload_success_handler : uploadSuccessOver,
      upload_complete_handler : uploadComplete,
      queue_complete_handler : queueComplete
    };
    swfupload = new SWFUpload(settings);
    var attachmentIds = $("attachmentId").value;
    var attachmentName = $("attachmentName").value;
    showAttach(attachmentIds,attachmentName,"showAtt");
  }
  function uploadSuccessOver(file, serverData) {
    try {
      var progress = new FileProgress(file, this.customSettings.progressTarget);
//    progress.setComplete();
//    progress.setStatus("Complete.");
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
  function InsertImage(src) { 
    return;
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
 function checkBox3() {
   if (document.getElementById("sms2Check").checked) {
      document.getElementById("sms2flag").value = "true";
   }else {
    document.getElementById("sms2flag").value = "false";
   }
 }
</script>
</head>
<body topmargin="5" onload="doInit()">
<table class="small" width="100%" cellspacing="0"  cellpadding="3">
<tr align='center' class='TableHeader'><tr>
<td class='Big'><img src='<%=imgPath%>/form.gif' width='22' height='18'>&nbsp;
<span class='big3'> 领导批注记录 (<%=plan.getName() %>&nbsp;&nbsp;<%if(plan.getStatrTime() != null){%><%=plan.getStatrTime() %><%} %>&nbsp;至&nbsp;<%if(plan.getEndTime() != null){%><%=plan.getEndTime() %><%} %>)</span></td>
</tr></table><br>
<%
  if (list.size() > 0 && flag) {
%>

<table class="TableList"  align="center" width="100%">
<tr align='center' class='TableHeader'>
<td>批注领导</td>
<td width='120'>批注内容</td>
<td>附件</td>
<td width='120'>批注时间</td>
<% if(flag2){ %>
 <td width='120'>操作</td>
 <%} %>
 </tr>
 <% for (int i = 0; i < list.size(); i++) {
    detail = list.get(i);
 %>
<tr align="center" class="TableLine1" title="批注">
<td><%if(detail.getWriter() != null){%>
    <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="<%=detail.getWriter()%>">
    <%
   }else {
 %>
 <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="">
 <%} %>
</td>
<td><%if(detail.getProgress() != null){%><%=detail.getProgress() %><%} %></td>
<td><%if(detail.getAttachmentName() != null){%>
<input type="hidden" id="attachmentId_<%=i %>" name="attachmentId_<%=i %>" value="<%=detail.getAttachmentId() %>">
        <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName_<%=i %>" value="<%=detail.getAttachmentName()  %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt_<%=i %>"></span>
<%} %></td>
<td align='center'><%if(detail.getWriteTime() != null){%><%=detail.getWriteTime() %><%} %></td>
<% if(flag2){ %>
<td>
<a href="javascript:updateFugle('<%=detail.getSeqId() %>')">修改</a>&nbsp;
<a href="javascript:deleteDetail('<%=detail.getSeqId() %>','<%=plan.getSeqId() %>')">删除</a>
 </td>
  <%} %>
 </tr>
<%} %>
 </table>
 <div id="writerName<%=0%>Desc" style="display:none"></div>
<input type="hidden" name="writerName<%=0%>" id="writerName<%=0%>" value="<%=detail.getWriter() %>">
 <%} 
if (list.size() > 0 && !flag) {%>
 <% for (int i = 0; i < list.size(); i++) {
    detail = list.get(i);
 %>
    <div id="writerName<%=i%>Desc" style="display:none"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="<%=detail.getWriter()%>">
 <%} %>

<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>没有批注内容</div>
</td></tr></table>
<div align="center"><input type='button' class='BigButton' value='关闭' onClick='window.close();' title='关闭窗口'></div>
<%}if (list.size() <= 0) {%>
<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>没有批注内容</div>
</td></tr></table>
<%if (!flag) { %>
<div align="center"><input type='button' class='BigButton' value='关闭' onClick='window.close();' title='关闭窗口'></div>
<%} %>
<%} if(flag2){
  if (flag){%>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3"
  class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sign.gif"
      WIDTH="22" HEIGHT="20">&nbsp;<span class="big3"> 领导批注</span></td>
  </tr>
</table>
<br>
<form name="form1" id="form1">
<table class="TableBlock" width="100%" ><tr>
<td nowrap class="TableContent" width='90'>&nbsp;批注时间：</td>
 <td class='TableData' align='left'>
 <input type='text' name='WRITE_TIME' id='WRITE_TIME' size='19' readonly maxlength='100' class='BigStatic' value=''>
 <input type='hidden' name='PLAN_ID' id='PLAN_ID'  readonly maxlength='100' class='BigStatic' value='<%=plan.getSeqId()%>'>  
 <input type="hidden" name="leader1" id="leader1" value="<%=plan.getLeader1Desc()%>"> 
 </td> 
</tr> 
<tr> 
<td class="TableContent">&nbsp;批注内容：</td>
<td class='TableData' colspan='1'><textarea name='PROGRESS' id='PROGRESS' class='BigInput' cols='80' rows='8'></textarea></td>
</tr>   
<tr id="attr_tr">
      <td nowrap class="TableContent">&nbsp;附件: </td>
      <td class="TableData">
        <input type="hidden" id="attachmentId" name="attachmentId" value="">
        <input type="hidden" id="attachmentName" name="attachmentName" value="">
        <input type="hidden" id="ensize" name="ensize">
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
<td class='TableContent'>&nbsp;提醒：</td>
<td class='TableData' align='left'><div>&nbsp;<input type='checkbox'name='smsfla' id='smsfla' onClick='checkBox2();' checked>
<label for='SMS_REMIND'>使用内部短信提醒</label>
 <input type='hidden'  name='smsflag' id='smsflag' value="true"></div>
  <div id="sms2Remind">&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" value="1"  onClick="checkBox3();">
<label for="SMS_REMIND">使用手机短信提醒</label>&nbsp;&nbsp;
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
 </td>
</tr>        
<tr> 
<td nowrap  class='TableControl' colspan='2' align='center'>
<input type='button' value='增加批注' class='BigButton' onClick="javascript:checkForm2('<%=plan.getSeqId()%>')">&nbsp;&nbsp;
<input type='button' class='BigButton' value='关闭' onClick='window.close();' title='关闭窗口'>
</td>
</tr>
</table>
</form>
<form id="formFile" action="/yh/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
<%} else {%>
<div style="display:none">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
  class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sign.gif"
      WIDTH="22" HEIGHT="20"><span class="big3"> 领导批注</span></td>
  </tr>
</table>
<form name="form1" id="form1">
<table class="TableBlock" width="100%" ><tr>
<td nowrap class="TableContent" width='90'>&nbsp;批注时间：</td>
 <td class='TableData' align='left'>
 <input type='text' name='WRITE_TIME' id='WRITE_TIME' size='19' readonly maxlength='100' class='BigStatic' value=''>
 <input type='hidden' name='PLAN_ID' id='PLAN_ID'  readonly maxlength='100' class='BigStatic' value='<%=plan.getSeqId()%>'> 
 <input type="hidden" name="leader1" id="leader1" value="<%=plan.getLeader1Desc()%>">   
 </td> 
</tr> 
<tr> 
<td class="TableContent">&nbsp;批注内容：</td>
<td class='TableData' colspan='1'><textarea name='PROGRESS' id='PROGRESS' class='BigInput' cols='80' rows='8'></textarea></td>
</tr>   
<tr id="attr_tr">
      <td nowrap class="TableContent">&nbsp;附件: </td>
      <td class="TableData">
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
<td class='TableContent'>&nbsp;提醒：</td>
<td class='TableData' align='left'><div>&nbsp;<input type='checkbox'name='smsfla' id='smsfla' onClick='checkBox2();' checked>
<label for='SMS_REMIND'>使用内部短信提醒</label>
 <input type='hidden'  name='smsflag' id='smsflag' value="true"></div>
 <div id="sms2Remind">&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" value="1"  onClick="checkBox3();">
<label for="SMS_REMIND">使用手机短信提醒</label>&nbsp;&nbsp;
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
 </td>
</tr>        
<tr> 
<td nowrap  class='TableControl' colspan='2' align='center'>
<input type='button' value='增加批注' class='BigButton' onClick="javascript:checkForm2('<%=plan.getSeqId()%>')">&nbsp;&nbsp;
<input type='button' class='BigButton' value='关闭' onClick='window.close();' title='关闭窗口'>
</td>
</tr>
</table>
</form>
<form id="formFile" action="/yh/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</div>
<%}
}%>
<input type='hidden' id=count name=count value="<%=list.size()%>"/>
<input type="hidden" id="moduel" name="moduel" value="work_plan">
</body>
</html>
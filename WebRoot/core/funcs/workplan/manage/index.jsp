<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
   seqId = "";
  }
  String treeId = request.getParameter("treeId");
  if (treeId == null) {
    treeId = "";
  }
  
  String deptParent = request.getParameter("deptParent");
  if (deptParent == null) {
    deptParent = "";
  }
  String TO_ID = request.getParameter("TO_ID");
  String deptLocal = request.getParameter("deptLocal");
  if (deptLocal == null){
    deptLocal = "";
  }
  String TO_NAME = request.getParameter("TO_NAME");
  String deptParentDesc = request.getParameter("deptParentDesc");
  if (deptParentDesc == null) {
    deptParentDesc = "";
  }
  String parentdesc = (String)request.getAttribute("desc");
  if (parentdesc == null) {
    parentdesc = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作计划</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>

<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>

<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
  var TO_ID = "<%=TO_ID%>";
  var TO_NAME = "<%=TO_NAME%>";
  var treeId = "<%=treeId%>";
  var deptLocal = "<%=deptLocal%>";
  var deptParent = "<%=deptParent%>";
  var deptParentDescValue = "<%=deptParentDesc%>";
  var parentdesc = "<%=parentdesc%>";
  var oFCKeditor = new FCKeditor('WORK_CONTENT');
  
  function doInit() {
    getSysRemind();
    cratetime();
    planType();
    doInit2();
    Init();
    attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,false);
    moblieSmsRemind('sms2Remind','sms2Check');
  }
  function clearUser(TO_ID, TO_NAME) {
    if (!TO_ID) {
      TO_ID = "TO_ID";
      TO_NAME = "TO_NAME";
    }
    document.getElementsByName(TO_ID)[0].value = "";
    document.getElementsByName(TO_NAME)[0].value = "";
  }
  function Init() { 
    showCalendar('statrTime',false,'beginDateImg');
    showCalendar('endTime',false,'endDateImg');
  }
  function checkForm() {   
    if (!$("name").value.trim()) {
      alert("名称不能为空！");
      $("name").focus();
      //selectLast($("name")); 
      return false;
    }
    if (document.getElementById("deptDesc").value == "" && document.getElementById("managerDesc").value == "" &&  document.getElementById("leader2Desc").value == "") {
       alert("发布范围(部门或人员)与参与人不能同时为空！");
       //selectLast($("deptParentDesc"));
       document.getElementById("deptDesc").focus();
       return false;
    }
    if (document.getElementById("leader1Desc").value == "") {
      alert("参与人不能为空！");
      document.getElementById("leader1Desc").focus();
      return false;
    }
    if (document.getElementById("statrTime").value == "" || document.getElementById("endTime").value == "") { 
      alert("开始时间与结束时间不能为空！");
      document.getElementById("statrTime").focus();
      return false;
    }
    if (document.getElementById("statrTime").value > document.getElementById("endTime").value ) {
      alert("开始时间不能大于结束时间！");
      document.getElementById("endTime").focus();
      return false;
    }
    return true;
  }
function checkBox2() {
  if (document.getElementById("smsflag2").checked) {
     document.getElementById("smsflag").value = "true";
     document.getElementById("sms").value = "1";
  }else {
   document.getElementById("smsflag").value = "false";
   document.getElementById("sms").value = "0";
  }
}
function checkBox3() {
  if (document.getElementById("sms2Check").checked) {
     document.getElementById("sms2flag").value = "true";
  }else {
   document.getElementById("sms2flag").value = "false";
  }
}
function cratetime() {
  var now = new Date(); 
  var year = now.getYear();
  var month = now.getMonth()+1;
  var da = now.getDate();
  if(month<10){
	  month="0"+month;
  }
  document.getElementById("CREATE_DATE").value = year + "-" + month + "-" + da;
}
  function planType() {
    var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/planType.act";
    var json = getJsonRs(url);
    var rtData = json.rtData;   
    for (var i = 0;i < rtData.length; i++) {      
      var opt=document.createElement("option");      
      opt.value = rtData[i].seqId;
      opt.text = rtData[i].typeName;      
      var selectObj = $('WORK_TYPE');
      selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0);
    }    
  }
  var upload_limit = 1,limit_type = limitUploadFiles;
  var oa_upload_limit = limitUploadFiles;
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
    attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,false);
    //$('attr_tr').style.display = ''; 
    removeAllFile();
    if (isUploadBackFun) {
      checkForm2(isPublish);
      isUploadBackFun = false;
     }
  }
  
  function checkTwo(chi) {
    if (chi == 1) {
      document.getElementById("PUBLISH").value = "1";
    }else {
      document.getElementById("PUBLISH").value = "0";
    }
  }

  var isPublish;
  var isUploadBackFun = false;
  function checkForm2(chi) {
    var FCK = FCKeditorAPI.GetInstance('WORK_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

    var FORM_MODE = FCK.EditingArea.Mode;
    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('WORK_CONTENT___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst) {
      FCK.Commands.GetCommand('Source').Execute();
    } 
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    document.getElementById("DIARY_CONTENT").value = FORM_HTML;
    
    checkTwo(chi);
    if (checkForm()) {
      if(jugeFile()){//如果有没有上传的文件，则进行上传
        $("formFile").submit();
        isUploadBackFun = true;
        isPublish= chi;
        return ;
    }
      var pars = $('form1').serialize() ;
      var url = contextPath + "/yh/core/funcs/workplan/act/YHPlanWorkAct/add.act";
      var json = getJsonRs(url,pars);
      if(json.rtState == "1") {
        alert(json.rtMsrg);
      }else {
        window.location = contextPath + "/core/funcs/workplan/manage/manageWork.jsp";
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
  
  function addDept() {
    var URL="<%=contextPath %>/core/funcs/orgselect/MultiDeptSelect.jsp";
      openDialog(URL,'500', '500');
  }

//判断是否要显示短信提醒 
  function getSysRemind(){ 
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=12"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
  alert(rtJson.rtMsrg); 
  return ; 
  } 
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind; 
  var mobileRemind = prc.mobileRemind; 
  if(allowRemind=='2'){ 
  $("smsRemindDiv").style.display = 'none'; 
  }else{ 
  if(defaultRemind=='1'){
  document.getElementById("smsflag").value = "true";
  $("smsflag2").checked = true; 
  } 
  } 
  //return prc; 
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
    var oEditor = FCKeditorAPI.GetInstance('WORK_CONTENT') ; //FCK实例 
    if (oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oEditor.InsertHtml( "<img src = '"+ src + "'/>") ; 
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
<body topmargin="5" onLoad="doInit()">
<div><img src="<%=imgPath%>/edit.gif" width="18"
height="18"><span class="big3"> 新建工作计划</span></div>
<input type="hidden" id="moduel" name="moduel" value="work_plan">
<form name="form1" id="form1">
<table class="TableBlock"  align="center" width="60%">
<tr>
<td nowrap class="TableContent">&nbsp;计划名称：</td>
<td  class="TableData"><input type="text" name="name"
id="name" size="36" maxlength="200" class="BigInput"></td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;发布范围(部门)：</td>
<td class="TableData"><input type="hidden" name="deptParent"
id="dept" class="BigInput" size="25" maxlength="25" value="">
<textarea cols="45" name="deptParentDesc" id="deptDesc" rows="2"
style="overflow-y: auto;" class="SmallStatic" wrap="yes" readonly></textarea>

<a href="javascript:;" class="orgAdd" onClick="javascript:addDept();">添加</a>

<!-- <a href="javascript:;" class="orgAdd" style="vertical-align:bottom"
onClick="openDialog('/yh/core/funcs/dept/deptedittree.jsp?deptId=1','300', '300')">添加</a> -->
<a href="javascript:;" class="orgClear"
onClick="clearUser('deptParent', 'deptParentDesc')">清空</a></td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;发布范围(人员)：</td>
<td nowrap class="TableData"><input type="hidden" name="manager"
id="manager" value=""> <textarea cols="45" name="managerDesc"
id="managerDesc" rows="2" style="overflow-y: auto;"
class="SmallStatic" wrap="yes" readonly></textarea> <a
href="javascript:;" class="orgAdd"
onClick="selectUser(['manager', 'managerDesc'])">添加</a> <a
href="javascript:;" class="orgClear"
onClick="clearUser('manager', 'managerDesc')">清空</a></td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;参与人：</td>
<td nowrap class="TableData"><input type="hidden" name="leader1"
id="leader1" value=""> <textarea cols="45" name="leader1Desc"
id="leader1Desc" rows="2" style="overflow-y: auto;"
class="SmallStatic" wrap="yes" readonly></textarea> <a
href="javascript:;" class="orgAdd"
onClick="selectUser(['leader1', 'leader1Desc'])">添加</a> <a
href="javascript:;" class="orgClear"
onClick="clearUser('leader1', 'leader1Desc')">清空</a></td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;负责人：</td>
<td nowrap class="TableData"><input type="hidden" name="leader2"
id="leader2" value=""> <textarea cols="45" name="leader2Desc"
id="leader2Desc" rows="2" style="overflow-y: auto;"
class="SmallStatic" wrap="yes" readonly></textarea> <a
href="javascript:;" class="orgAdd"
onClick="selectUser(['leader2', 'leader2Desc'])">添加</a> <a
href="javascript:;" class="orgClear"
onClick="clearUser('leader2', 'leader2Desc')">清空</a></td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;批注领导：</td>
<td nowrap class="TableData"><input type="hidden" name="leader3"
id="leader3" value=""> <textarea cols="45" name="leader3Desc"
id="leader3Desc" rows="2" style="overflow-y: auto;"
class="SmallStatic" wrap="yes" readonly></textarea> <a
href="javascript:;" class="orgAdd"
onClick="selectUser(['leader3', 'leader3Desc'])">添加</a> <a
href="javascript:;" class="orgClear"
onClick="clearUser('leader3', 'leader3Desc')">清空</a></td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;说明：</td>
<td class="TableData">&nbsp;发布范围：指在“工作计划查询”中，可以查询到该工作计划的人员。<br>
&nbsp;参与人：指该工作计划的执行人员，可以写进度日志。<br>
&nbsp;负责人：指执行、管理该工作计划的人员，可以写进度日志。<br>
&nbsp;批注领导：默认包括负责人、创建人。批注领导对该工作计划有批注权。</td>
</tr>
<tr id="attr_tr">
      <td nowrap class="TableContent">&nbsp;附件: </td>
      <td class="TableData">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
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
      <script>ShowAddFile(); ShowAddImage();</script>
       <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
          <input type="hidden" name="attachmentId" value="">
          <input type="hidden" name="attachmentName" value=""> 
          <div style="display:none"><span id="spanButtonUpload" title="批量上传附件"> </span></div></div>
            </td>
    </tr>
<tr>
<td nowrap class="TableContent">&nbsp;附件说明：</td>
<td class="TableData"><textarea cols=45 id="ATTACHMENT_COMMENT" name="ATTACHMENT_COMMENT" rows=3
class="BigINPUT" wrap="yes"></textarea></td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;备注：</td>
<td class="TableData"><textarea cols=45 name="remark" rows=3 id="remark" class="BigINPUT"></textarea></td>
</tr>

<tr>
<td nowrap class="TableContent">&nbsp;有效期：</td>
<td class="TableData">&nbsp;开始日期： <input type="text" id="statrTime" name="statrTime" size="10"
maxlength="19" class="BigInput" value="" readonly> <img
id="beginDateImg" src="<%=imgPath%>/calendar.gif"
align="absMiddle" border="0" style="cursor: hand"> 开始时间不能为空<br>&nbsp;结束日期： <input
type="text" id="endTime" name="endTime" size="10" maxlength="19"
class="BigInput" value="" readonly> <img id="endDateImg"
src="<%=imgPath%>/calendar.gif" align="absMiddle"
border="0" style="cursor: hand">&nbsp;结束时间不能为空</td>
</tr>
<tr>
<td nowrap class="TableContent">&nbsp;计划类型：</td>
<td class="TableData">&nbsp;<select name="WORK_TYPE" id="WORK_TYPE"></select></td>
</tr>
<tr>

<td nowrap class="TableContent">&nbsp;提醒：</td>
<td class="TableData"><div id="smsRemindDiv">&nbsp;<input type="checkbox" name="smsflag2" id="smsflag2" onClick="checkBox2();">
<label for="SMS_REMIND">使用内部短信提醒</label>&nbsp;&nbsp;</div>
 <input type="hidden" name="smsflag" id="smsflag" value="false">
 <input type="hidden" name="sms" id="sms" value="1">
 
 <div id="sms2Remind">&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" value="1"  onClick="checkBox3();">
<label for="SMS_REMIND">使用手机短信提醒</label>&nbsp;&nbsp;
<input type="hidden" name="sms2flag" id="sms2flag" value="false">
</div>
</td>
</tr>
<tr>
<td class="TableContent" colspan="2">&nbsp;计划内容：</td>
</tr>
<tr id="EDITOR">
<td class="TableData" colspan="3">
<div><script language=JavaScript>    
         oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKeditor.Height = "300px";
         oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
         oFCKeditor.ToolbarSet="DiaryBar";
         oFCKeditor.Create();</script>
         <input type="hidden" id="DIARY_CONTENT" name="DIARY_CONTENT" value="">
</div>
</td>
</tr>
<tr align="center" class="TableControl">
<td colspan="2" nowrap><input type="hidden" name="OP">
<input type="hidden" name="PUBLISH" id="PUBLISH"> <input
type="hidden" name="PLAN_ID"> <input type="hidden"
name="CREATOR" value="admin"> <input type="hidden"
name="CREATE_DATE" id="CREATE_DATE" value=""> <input type="hidden"
name="ATTACHMENT_ID_OLD" value=""> <input type="hidden"
name="ATTACHMENT_NAME_OLD" value=""> <input type="hidden"
name="" value=""><input type="button" id="tijiao" name="tijiao"
value="提交" class="BigButton" onClick="checkForm2('1')">&nbsp;&nbsp;<input
type="button" value="保存 " class="BigButton" id="baoc" name="baoc" onClick="checkForm2('0')">
</td>
</tr>
</table>
</form>
<form id="formFile" action="<%=contextPath %>/yh/core/funcs/workplan/act/YHWorkPlanHandleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>
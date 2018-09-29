<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic"%>
<%@ include file="/core/inc/header.jsp" %>

<%
	String serviceName = request.getServerName();
	int port = request.getLocalPort();
	String filePath = YHSysProps.getAttachPath() + "/jtgw/" ;
	filePath = filePath.replace("\\" ,"\\\\");
	
	boolean isStamp = YHDocSendLogic.isStamp(request);
	boolean isSend = YHDocSendLogic.isSend(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发文登记</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/src/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/src/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/sendDoc/js/logic.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/sendDoc/js/util.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/designer.js"></script>
<link rel="stylesheet" href="/yh/subsys/jtgwjh/css/style.css" type="text/css" />
<link rel="stylesheet" href="/yh/subsys/jtgwjh/css/gwjh.css" type="text/css" />
<script type="text/javascript">
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var uploadSize = 5120

window.onresize=function(){
  setIframeHeight();
}

/*
 * 改变窗口大小的时候更改控件的大小
 */
function setIframeHeight(){
  var height = ((document.documentElement || document.body).offsetHeight - 58) + 'px';
  $('wordDocIframe').style.height = height ;
}
function doInit(){
  controlOver();
  setIframeHeight();
  
  var myDate = new Date();
  $('yearShow').value = myDate.getFullYear();    //获取完整的年份(4位,1970-????)
  $('mouthShow').value = (myDate.getMonth() + 1) < 10 ? '0' + (myDate.getMonth() + 1) : (myDate.getMonth() + 1);       //获取当前月份(0-11,0代表1月)
  $('dayShow').value = myDate.getDate() < 10 ? '0' + myDate.getDate() : myDate.getDate();        //获取当前日(1-31)
  
  //初始化批量上传附件功能
  var linkColor = document.linkColor;
  var file_size_limit_seq = 1000;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/fileLoad.act",
    post_params: {"PHPSESSID" : "<%=session.getId()%>"},
    file_size_limit : file_size_limit_seq +  " MB",
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
    button_text: '<span class=\"textUpload\">批量上传</span>',
    button_text_style: ".textUpload{color:" + linkColor + ";}",
    button_text_top_padding : 1,
    button_text_left_padding : 18,
    button_placeholder_id : "spanButtonUpload",
    button_width: 70,
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
  var selfdefMenu = {
      office:["downFile","read","edit"], 
      img:["downFile","play"],  
      music:["downFile","play"],  
      video:["downFile","play"], 
      others:["downFile"]
  }
  var attachmentIds = $("attachmentId").value;
  var attachmentNames = $("attachmentName").value;
  if(attachmentIds){
    var attachmentIdArray =  attachmentIds.split(",");
    var attachmentNameArray =  attachmentNames.split("*");
    for(var i = 0 ; i < attachmentIdArray.length; i++){
      attachMenuSelfUtilTable("showAttTbody","jtgw",attachmentNameArray[i] ,attachmentIdArray[i], 0, '','','',selfdefMenu,i);
    }
    $('showAttTable').style.display = "";
    $('attachTotalDiv').style.display = "";
  }
  $('docTitle').focus();
  isOnline();
  setInterval(isOnline, 10*1000);
}

//第一界面确定事件，验证成功后跳转到确认页面
function doSubmit(){
  if(checkForm()){
    if($('attachTotalDiv').style.color == 'green'){
	    attachmentTrans();
	    
	    $('docTitleConfirm').innerHTML = $('docTitle').value;
	    $('reciveDeptConfirm').innerHTML = $('reciveDeptDesc').value;
	    $('handReciveDeptConfirm').innerHTML = $('handReciveDept').value;
	    $('remarkConfirm').innerHTML = $('remark').value;
	    
	    $('docTypeConfirm').innerHTML = getSelectedInner($('docType'));
	    $('urgentTypeConfirm').innerHTML = getSelectedInner($('urgentType'));
	    $('securityLevelConfirm').innerHTML = getSelectedInner($('securityLevel'));
	    $('printCountConfirm').innerHTML = $('totalPrintCount').value;
	    $("form1").style.display = "none";
	    $("tableWord").style.display = "none";
	    $("navTabDiv").style.display = "none";
	    $("confirmDiv").style.display = "";
    }
    else{
      alert("附件大小超过限制！");
      return;
    }
  }
}

//附件列表参数转换
function attachmentTrans(){
  var attachmentName = $('attachmentName').value;
  while( attachmentName.indexOf("*") != -1 ) {
    attachmentName = attachmentName.replace("*","<br>"); 
  }
  $('attachmentConfirm').innerHTML = attachmentName;
  
  for(var i = 0; i < uploadNum ;i++){
    if($('reciveDept_'+i+'_Attach')){
      $('attachmentReciveDeptId').value += $('reciveDept_'+i+'_Attach').value + '*';
      $('attachmentReciveDeptDesc').value += $('reciveDept_'+i+'_AttachDesc').value + '*';
      $('attachmentUploadNum').value += i + ',';
    }
  }
}

//获取select选中的值
function getSelectedInner(sel){
  return sel.options[sel.options.selectedIndex].innerHTML;
}

//保存事件
function doSubmit2(){
  if(checkForm()){
    attachmentTrans();
	  if(document.frames["wordDocIframe"].TANGER_OCX_SaveDoc){
	    document.frames["wordDocIframe"].TANGER_OCX_SaveDoc(0);
	  }
	  $("form1").submit();
  }
}

//确认页面发送事件
function doSubmit3(){
  if(checkForm()){
	  // 关闭NTKO控件
	  if(document.frames["wordDocIframe"].TANGER_OCX_SaveDoc){
	    document.frames["wordDocIframe"].TANGER_OCX_SaveDoc(0);
	    document.frames["wordDocIframe"].closeNTKO();
	  }
	  addAPINote();
	  $("form1").action = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/addDocInfo.act?sendFlag=1";
	  $("form1").submit();
  }
}

/**
 * 如果有正文，加载AIP---syl
 */
function addAPINote(){
  var attachId = $("mainDocId").value;
  var attachName = $("mainDocName").value;
  if(attachId && attachName){
    var serviceName = '<%=serviceName%>';
    var port = <%=port%>;
    var attachPath = "<%=filePath%>";
    var fileRePath = loadWordToAIP(attachId,attachName,attachPath,serviceName,port,$("AIPDIV"));
    addAipNote();
    updateAipToService(fileRePath);   
  }
}

/*
 * 把aip文件上传到服务器去
 */
function updateAipToService(filePath) {
  var  attachmentName = "";
  try{   
    var webObj=document.getElementById("HWPostil1");
        webObj.HttpInit();
        webObj.HttpAddPostCurrFile("FileBlody");  
        var port = '<%=port%>';
        returnValue = webObj.HttpPost("http://<%=serviceName%>:" +port+ "/yh/subsys/jtgwjh/receiveDoc/saveFile/saveAip.jsp?filePath=" + encodeURIComponent(filePath) + "&fileName=" + encodeURIComponent(attachmentName));
        if("ok" == returnValue){
          //alert("上传成功！"); 
        }else if("failed" == returnValue){
          alert("上传失败！");
        }
        //window.location.href  = "index.jsp" ;
  }catch(e){
    alert(e);
  }
}


//确认页面盖章事件
function doSubmit4(){
  if(document.frames["wordDocIframe"].TANGER_OCX_SaveDoc){
    document.frames["wordDocIframe"].TANGER_OCX_SaveDoc(0);
  }
  $("form1").action = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/addDocInfo.act?sendFlag=2";
  $("form1").submit();
}

//验证
function checkForm(){
  if($("docTitle").value == ""){
    alert("文件标题不能为空！");
    $("docTitle").focus();
    return (false);
  }

  if($("reciveDept").value == ""){
    alert("接收单位不能为空！");
    return (false);
  }
  
  if($("docType").value == 1 && $("mainDocId").value == "" ){
    alert("公文类型发文需要设置正文！");
    return (false);
  }
  
  if($("totalPrintCount").value != "" && !checkRate($("totalPrintCount").value)){
    alert("发送总份数必须为数字！");
    $("totalPrintCount").focus();
    return (false);
  }
  
  if($("printCount").value != "" && !checkRate($("printCount").value)){
    alert("网发份数必须为数字！");
    $("printCount").focus();
    return (false);
  }
  
  if($("paperPrintCount").value != "" && !checkRate($("paperPrintCount").value)){
    alert("纸制发份数必须为数字！");
    $("paperPrintCount").focus();
    return (false);
  }
  
  var reciveDeptLength = ($('reciveDept').value).split(",").length;
  for(var i = 0; i < reciveDeptLength ; i++){
    if(!checkRate($('print_count_'+i).value)){
      alert("请输入正确的打印份数！");
      $('print_count_'+i).focus();
      return false;
    }
  }
	return true;
}

//确认页面返回事件
function returnInfo(){
  $("confirmDiv").style.display = "none";
  $("navTabDiv").style.display = "";
  $("form1").style.display = "";
  $('tab_1').className = 'tab_on_1';
  $('tab_2').className = 'tab_2';
  $('tab_3').className = 'tab_3';
  $('tab_4').className = 'tab_4';
  $('tableWord').style.display = 'none';
  $('tablePrint').style.display = 'none';
  $('tableAttachment').style.display = 'none';
  $('tableForm').style.display = '';
}

//打印控制按钮事件
function doPrintControl(flag){
  if(flag){
    if(isHaveMainDoc()){
		  $('showPrintTableMain').style.display = '';
		  $('printButton').style.display = '';
		  if($('reciveDept').value){
		    consider(($('reciveDept').value).split(",").length);
		  }
    }
  }
  else{
    $('showPrintTableMain').style.display = 'none';
    $('printButton').style.display = 'none';
  }
}

function isHaveMainDoc(){
	if($("mainDocId").value == "" ){
	  $('isSignNO').checked = true;
	  alert("请先上传一个正文！");
	  return false;
	}
	return true;
}

//打印控制列表实现
function printTable(){
  
  //判断是否存在拨号单位
  if(($('reciveDeptFlag').value).indexOf('*') > -1){
    uploadSize = 1024;
  }
  else{
    uploadSize = 5120;
  }
  
  //计算附件大小
  var attachTotalCount = 0;
  for(var i = 0; i < uploadNum; i++){
    if($('attach_Size_'+i)){
      attachTotalCount = parseInt(attachTotalCount) + parseInt($('attach_Size_'+i).value);
    }
  }
  
  //改变附件总数统计颜色
  $('ensize').value = attachTotalCount;
  attachTotalCount = (attachTotalCount/1024).toFixed(0);
  if(attachTotalCount > uploadSize){
    $('attachTotalDiv').style.color = 'red';
  }
  else{
    $('attachTotalDiv').style.color = 'green';
  }
  
  var reciveDept = $('reciveDept').value;
  var reciveDeptDesc = $('reciveDeptDesc').value;
  
  var table = document.getElementById('showPrintTable');
  var tbodys = table.getElementsByTagName("tbody");
  for(i = 0; i < tbodys.length; i++){
    table.removeChild(tbodys[i]);
  }
  if(reciveDept == ''){
    $('companyCount').innerHTML = 0;
    $('docAllCount').innerHTML = 0;
    setPrintNo();
    return;
  }
  else if($('isSignYes').checked){
    $('showPrintTableMain').style.display = '';
  }
  
  var tbody = $C('tbody');
  tbody.id = 'showPrintTbody';
  var reciveDeptArray = reciveDept.split(',');
  var reciveDeptDescArray = reciveDeptDesc.split(',');
  for(var i = 0; i < reciveDeptArray.length ; i++){
    
	  var td1 = $C('td');
	  td1.align = 'center';
	  td1.innerHTML = reciveDeptDescArray[i];
	  
	  var td2 = $C('td');
	  td2.align = 'right';
	  td2.innerHTML = '<input type="text" id="print_count_'+i+'" name="print_count_'+i+'" size="5" maxlength="5" style="text-align:right;" value="1" onchange="consider('+reciveDeptArray.length+')">';
	  
	  var td3 = $C('td');
	  td3.align = 'right';
	  td3.innerHTML = '<input type="text" id="print_no_start_'+i+'" name="print_no_start_'+i+'" size="5" style="text-align:right;" value="-" readonly>';
	  
	  var td4 = $C('td');
	  td4.align = 'right';
	  td4.innerHTML = '<input type="text" id="print_no_end_'+i+'" name="print_no_end_'+i+'" size="5" style="text-align:right;" value="-" readonly>';
	  
	  var tr = $C('tr');
	  tr.id = "print_tr_" + i;
	  tr.name = "print_tr_" + i;
	  
	  tr.appendChild(td1);
	  tr.appendChild(td2);
	  tr.appendChild(td3);
	  tr.appendChild(td4);
	  tbody.appendChild(tr);
  }
  
  $('showPrintTable').appendChild(tbody);
  $('companyCount').innerHTML = reciveDeptArray.length;
  $('docAllCount').innerHTML = reciveDeptArray.length;
  setPrintNo();
}

//计算总打印份数
function consider(length){
  var count = 0;
  for(var i = 0; i < length ; i++){
    if(!checkRate($('print_count_'+i).value)){
      alert("请输入正确的打印份数！");
      $('print_count_'+i).focus();
      return;
    }
    count = count + parseInt($('print_count_'+i).value);
  }
  $('docAllCount').innerHTML = count;
  $('printCount').value = count;
  $('totalPrintCount').value = parseInt(count) + parseInt($('paperPrintCount').value);
  
  var printCount = $('printCount').value;
  autoCompletionStr(printCount,'','',$("reciveDept").value);
}

/**
 * 统一设置打印编号
 */
function setPrintNo(){
  var reciveDeptNum = 0;
  if($('reciveDept').value.trim() != ""){
    reciveDeptNum = $('reciveDept').value.split(",").length;
  }
  consider(reciveDeptNum);
  var printCount = $('printCount').value;
  autoCompletionStr(printCount,'','',$("reciveDept").value);
}

function controlOver(){
  $('saveControl').onmouseover = function(){
    this.className = "newButton newButtonSaveHover";
  }
  $('saveControl').onmouseout = function(){
    this.className = "newButton newButtonSave";
  }
  $('sendControl').onmouseover = function(){
    this.className = "newButton newButtonSendHover";
  }
  $('sendControl').onmouseout = function(){
    this.className = "newButton newButtonSend";
  }
  $('getControl').onmouseover = function(){
    this.className = "newButtonB newButtonGetHover";
  }
  $('getControl').onmouseout = function(){
    this.className = "newButtonB newButtonGet";
  }
  $('confirmControl').onmouseover = function(){
    this.className = "newButtonB newButtonConfirmHover";
  }
  $('confirmControl').onmouseout = function(){
    this.className = "newButtonB newButtonConfirm";
  }
}

function navTabChange(flag){
  
  switch(flag){
    case 1 : $('tab_1').className = 'tab_on_1';
             $('tab_2').className = 'tab_2';
             $('tab_3').className = 'tab_3';
             $('tab_4').className = 'tab_4';
             $('tableWord').style.display = 'none';
             $('tablePrint').style.display = 'none';
             $('tableAttachment').style.display = 'none';
             $('stampButton1').style.display = 'none';
             $('tableForm').style.display = '';
             break;
    case 2 : $('tab_1').className = 'tab_1';
				     $('tab_2').className = 'tab_on_2';
				     $('tab_3').className = 'tab_3';
				     $('tab_4').className = 'tab_4';
				     $('tablePrint').style.display = 'none';
				     $('tableForm').style.display = 'none';
				     $('tableAttachment').style.display = 'none';
				     $('stampButton1').style.display = '';
				     if($('confirmDiv').style.display == 'none'){
				       $('tableWord').style.display = '';
				     }
             break;
    case 3 : if($('mainDocId').value == ""){
				       alert("请先上传一个正文！");
				       return;
				     }
             $('tab_1').className = 'tab_1';
				     $('tab_2').className = 'tab_2';
				     $('tab_3').className = 'tab_on_3';
				     $('tab_4').className = 'tab_4';
				     $('tableForm').style.display = 'none';
				     $('tableWord').style.display = 'none';
				     $('tableAttachment').style.display = 'none';
				     $('stampButton1').style.display = 'none';
				     $('tablePrint').style.display = '';
				     break;
    case 4 : $('tab_1').className = 'tab_1';
             $('tab_2').className = 'tab_2';
             $('tab_3').className = 'tab_3';
             $('tab_4').className = 'tab_on_4';
             $('tableForm').style.display = 'none';
             $('tablePrint').style.display = 'none';
             $('tableWord').style.display = 'none';
             $('stampButton1').style.display = 'none';
             $('tableAttachment').style.display = '';
             break;
  }
}

function wordTypeSelect(flag){
  if(flag){
	  $('wordLocalTr').style.display = '';
  }
  else{
    $('wordLocalTr').style.display = 'none';
  }
}

function createWord(){
  var title = '正文';
  
  if($('createTypeLocal').checked){
    if($('fileWord').value){
      if(endWith($('fileWord').value,'.doc') || endWith($('fileWord').value,'.DOC') || endWith($('fileWord').value,'.docx') || endWith($('fileWord').value,'.DOCX')){
	      $('tableWordTable').style.display = 'none';
	      $('fileWord2').click();
      }
      else{
        alert("请选择Word格式文件做为正文！")
      }
    }
    else{
      alert("请选择正文！")
    }
  }
  else{
    $('tableWordTable').style.display = 'none';
    var url = contextPath + "/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/createAttachment.act";
    var json = getJsonRs(url , 'newName='+title);
    if (json.rtState == '0') {
      var attach = json.rtData;
      if (attach) {
        $('seqId').value = attach.seqId;
        $('mainDocId').value = attach.attachmentId;
        $('mainDocName').value = attach.attachmentName;
        officeIframe(attach.attachmentName,attach.attachmentId,"jtgw",4);
      }
    } else {
      //alert(json.rtMsrg);
    }
  }
}

/**
 * NTKOword操作
 * @param attachmentName 附件名称
 * @param attachmentId  附件ID
 * @param moudle 附件模块名称
 * @param op  操作类型
 * @param signKey 验证
 * @param print 打印类型
 * @return
 */
function officeIframe(attachmentName,attachmentId,moudle,op,signKey,print){
  if($('isStamp').value == '0'){
	  var param = "attachmentName=" + encodeURIComponent(attachmentName)
	     + "&attachmentId=" + attachmentId 
	     + "&moudle=" + moudle 
	     + "&op=" + op 
	     + "&signKey=" + signKey 
	     + "&print=" + print 
	     + "&seqId=" + $('seqId').value; 
	  var url = contextPath + "/subsys/jtgwjh/ntko/indexNtko.jsp?" + param;
  }
  else{
    var url = contextPath + "/subsys/jtgwjh/sendDoc/stamp/stampWord.jsp?seqId="+$('seqId').value+"&flag=1&flagClose=1";
  }
  $('wordDocIframe').src = url;
  $('wordDocIframe').style.display = '';
}

function endWith(s1,s2){  
  if(s1.length<s2.length)  
    return   false;  
  if(s1==s2)  
    return   true;  
  if(s1.substring(s1.length-s2.length)==s2)  
      return   true;  
  return   false;  
}

function handleSingleUpload(returnState, rtMsrg , retData){
  var data = retData.evalJSON();
  $('seqId').value = data.seqId;
  $('mainDocId').value = data.attrId;
  $('mainDocName').value = data.attrName;
  officeIframe(data.attrName,data.attrId,"jtgw",4);
}

/**
 * 选择部门
 * @return
 */
function selectOutDept3(retArray) {
  deptRetNameArray = retArray;
  var url = contextPath + "/core/esb/client/org/MultiDeptSelect3.jsp?reciveDept="+$('reciveDept').value;
  openDialogResize(url, 750, 500);
}

function destroySwfupload() {
   swfupload.destroy();
}

function getStamp(){
	document.frames["wordDocIframe"].isStamp();
}

function confirmSave(){
  document.frames["wordDocIframe"].save();
}

function textareaBig(){
  $('reciveDeptDesc').rows = $('reciveDeptDesc').rows + 4;
}

function textareaRe(){
  $('reciveDeptDesc').rows = 5;
}

function clickCheckgroup(el){
  var url = contextPath + "/yh/core/esb/client/act/YHDeptTreeAct/getGroupDept.act";
  var json = getJsonRs(url , 'flag='+el.value);
  if (json.rtState == '0') {
    var data = json.rtData;
    if(el.checked){
	    var reciveDeptDesc = data.deptName;
	    var reciveDept = data.deptId;
	    var reciveDeptFlag = data.telFlag;
	    
	    var reciveDeptDescArray = ($('reciveDeptDesc').value).split(',');
	    var reciveDeptArray = ($('reciveDept').value).split(',');
	    var reciveDeptFlagArray = ($('reciveDeptFlag').value).split(',');
	    for(var i = 0 ; i< reciveDeptDescArray.length ;i++){
	      reciveDeptDesc = getOutofStr(reciveDeptDesc, reciveDeptDescArray[i]);
	      reciveDept = getOutofStr(reciveDept, reciveDeptArray[i]);
	      reciveDeptFlag = getOutofStr(reciveDeptFlag, reciveDeptFlagArray[i]);
	    }
	    if(reciveDeptDesc != ""){
		    $('reciveDeptDesc').value += $('reciveDeptDesc').value.length == 0 ? reciveDeptDesc : ','+reciveDeptDesc;
		    $('reciveDept').value += $('reciveDept').value.length == 0 ? reciveDept : ','+reciveDept;;
		    $('reciveDeptFlag').value += $('reciveDeptFlag').value.length == 0 ? reciveDeptFlag : ','+reciveDeptFlag;
	    }
    }
    else{
      var deptNameArray = (data.deptName).split(',');
      var reciveDeptArray = (data.deptId).split(',');
      var reciveDeptFlagArray = (data.telFlag).split(',');
      for(var i = 0 ; i< deptNameArray.length ;i++){
        $('reciveDeptDesc').value = getOutofStr($('reciveDeptDesc').value, deptNameArray[i]);
        $('reciveDept').value = getOutofStr($('reciveDept').value, reciveDeptArray[i]);
        $('reciveDeptFlag').value = getOutofStr($('reciveDeptFlag').value, reciveDeptFlagArray[i]);
      }
    }
    printTable();
  }
}

function getOutofStr(str, s){
  var aStr = str.split(',');
  var strTmp = "";
  var j = 0 ;//控制重名
  for(var i = 0 ;i < aStr.length ; i++){
    if(aStr[i] && (aStr[i] != s || j != 0)){
        strTmp += aStr[i] + ',';
    }else{
      if(aStr[i] == s){
        j = 1;
      }  
    }
  }
  if (strTmp && strTmp.lastIndexOf(",") == strTmp.length - 1) {
    strTmp = strTmp.substring(0, strTmp.length - 1);
  }
  return strTmp;
}

function countPrintTotal(){
  $('totalPrintCount').value = parseInt($('printCount').value) + parseInt($('paperPrintCount').value);
}
</script>
</head>
<body onload="doInit();">

<div id="navTabDiv" style="position: fixed;height:40px;background-color: #E5E5E5;top: 5px;width: 100%;">
  <ul class="navTab" >
    <li title="表单" class="tab_on_1" id="tab_1" onclick="navTabChange(1)"/>
    <li title="正文" class="tab_2" id="tab_2" onclick="navTabChange(2)"/>
    <li title="打印控制" class="tab_3" id="tab_3" onclick="navTabChange(3)"/>
    <li title="附件" class="tab_4" id="tab_4" onclick="navTabChange(4)"/>
  </ul>
  <div id="formControl">
    <span id="stampButton1" style="display:none;"><span id="getStampDiv" style="display:none;"><input type="button" title="获取印章" onclick="getStamp();" class="newButtonB newButtonGet" id="getControl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <span id="saveStampDiv" style="display:none;"><input type="button" title="盖章确认" onclick="confirmSave();" class="newButtonB newButtonConfirm" id="confirmControl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span>
    <input type="button" title="保存" onclick="doSubmit2();" class="newButton newButtonSave" id="saveControl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" title="发送" onclick="doSubmit();" class="newButton newButtonSend" id="sendControl">
  </div>
</div>

<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/addDocInfo.act"  method="post" name="form1" id="form1" onsubmit="">
<input type="hidden" id="seqId" name="seqId">
<input type="hidden" id="isStamp" name="isStamp" value="0">
<input type="hidden" id="forwordId" name="forwordId" value="0"> 
  
<div style='layout-grid:15.6pt' id="tableForm">
  <div class=MsoNormal align=center style='width:750px;text-align:center;margin-left: auto;margin-right: auto;'>
	<span style='font-size:22.0pt;font-family:方正大标宋简体;color:red'>中国兵器工业集团公司发文登记单</span>
  </div><br>
	<div align="center"> 
    <table class=MsoNormalTable border=1 cellspacing=0 cellpadding=0 style='width:750px;margin-left:14.4pt;border-collapse:collapse;border:none'>
 		  <tr style='height:85.3pt'>
 		    <td width=549 colspan=10 valign=top class="tabtd1" cols="70">
				  <p class=MsoNormal><span class="label">标题:</span></p>
				  <p class=MsoNormal><span  style='color:red'><textarea name="docTitle" id="docTitle" rows="5" cols="70"></textarea></span></p>
		   </td>
		 </tr>
     <tr style='height:45.9pt'>
       <td colspan=10 class="tabtd2">
         <p class=MsoNormal><span class="label">文号: <input type="text" name="docNo" id="docNo" maxlength="50" size="80"></span></p>
       </td>
     </tr>
	  <tr style='height:45.9pt'>
      <td width=216 colspan=3 class="tabtd3">
        <p class=MsoNormal><span class="label">文件类型:</span>
	        <select id="docType" name="docType" style="width:71px;">
	          <option value='0'>普通</option>
	          <option value='1'>公文</option>
	        </select>
        </p>
      </td>
      <td width=60 colspan=2 class="tabtd4">
        <p class=MsoNormal style='margin-left:2.85pt'><span class="label">密级:</span></p>
      </td>
      <td width=72 colspan=2 style='width:53.65pt;border-top:none;border-left:none;border-bottom:solid red 1.0pt;border-right:solid red 1.0pt;padding:0cm 5.4pt 0cm 5.4pt;height:45.9pt'>
        <p class=MsoNormal><span  style='color:red'>       
          <select id="securityLevel" name="securityLevel" style="width:71px;">
	          <option value='0'>普通</option>
	          <option value='1'>内部</option>
	          <option value='2'>秘密</option>
	          <option value='3'>机密</option>
          </select></span></p>
      </td>
		  <td width=95 class="tabtd5">
		    <p class=MsoNormal><span class="label">紧急程度:</span></p>
		  </td>
		  <td width=107 colspan=2 style='width:80.45pt;border:none;border-bottom:solid red 1.0pt;padding:0cm 5.4pt 0cm 5.4pt;height:45.9pt'>
		    <p class=MsoNormal><span  style='color:red'>        
		      <select id="urgentType" name="urgentType" style="width:71px;">
	          <option value='0'>一般</option>
	          <option value='1'>紧急</option>
	          <option value='2'>特急</option>
          </select></span></p>
		  </td>
    </tr>
    <tr style='height:85.3pt'>
      <td width=549 colspan=10 valign=top class="tabtd6"  align="left">
		    <p class=MsoNormal><span class="label">主送单位:</span></p>
        <p class=MsoNormal><span  style='color:red'><textarea name="oaMainSend" id="oaMainSend" rows="5" cols="70"></textarea></span></p>
		  </td>
    </tr>
    <tr style='height:85.3pt'>
      <td width=549 colspan=10 valign=top  class="tabtd6"  align="left">
		    <p class=MsoNormal><span class="label">抄送单位:</span></p>
        <p class=MsoNormal><span  style='color:red'><textarea name="oaCopySend" id="oaCopySend" rows="5" cols="70"></textarea></span></p>
		  </td>
    </tr>
    <tr style='height:69.45pt'>
      <td width=549 colspan=10 valign=top  class="tabtd6">
		    <p class=MsoNormal><span class="label">附件:</span></p>
		    <p class=MsoNormal style="margin-bottom:5px;"><span id="attachMainShow" style="font-size: 18px;margin-left:20px;"></span></p>
		  </td>
    </tr>
    <tr id="selectTr" style='height:63.25pt'>
      <td  colspan=10 valign=top  class="tabtd6" nowrap  align="left">
        <p class=MsoNormal ><span class="label">网络发送单位:</span></p>
        
        <div style="position:relative;">
          <div style="position:relative;">
            <input type="hidden" id="reciveDept" name="reciveDept" value="">
            <input type="hidden" id="reciveDeptFlag" name="reciveDeptFlag" value="">
            <textarea id="reciveDeptDesc" name="reciveDeptDesc" rows="5" style="" cols="70"   class="BigStatic" readonly></textarea>
            <a href="javascript:void(0);" class="orgAdd" onClick="selectOutDept(['reciveDept', 'reciveDeptDesc', 'reciveDeptFlag']);printTable()">修改</a>
            <a href="javascript:void(0);" class="orgClear" onClick="clearDept();">清空</a>
          </div>
          <div style="position:absolute;bottom:5px;margin-left:605px;">
            <a href="javascript:void(0);" onClick="textareaBig();">放大</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:void(0);" onClick="textareaRe();">还原</a>
         </div>
        </div>
        <div style="clear:both;margin-top:10px;margin-bottom:5px;font-size: 14px;">
          <label><input type="checkbox" style="margin-left:5px;"  onclick="clickCheckgroup(this)" id="check1" name="check1" value="1">集团公司各部门</label>
          <label><input type="checkbox" style="margin-left:15px;" onclick="clickCheckgroup(this)" id="check2" name="check2" value="2">子集团和直管单位</label>
          <label><input type="checkbox" style="margin-left:15px;" onclick="clickCheckgroup(this)" id="check3" name="check3" value="3">兵科院联系单位</label>
          <label><input type="checkbox" style="margin-left:15px;" onclick="clickCheckgroup(this)" id="check4" name="check4" value="4">北化集团联系单位</label>
          <label><input type="checkbox" style="margin-left:15px;" onclick="clickCheckgroup(this)" id="check5" name="check5" value="5">其它有网单位</label>
        </div>
      </td>
    </tr>
    <tr style='height:67.4pt'>
		  <td width=549 colspan=10 valign=top  class="tabtd6" align="left">
			  <p class=MsoNormal><span class="label">纸制发送单位:</span></p>
			  <textarea id="handReciveDept" name="handReciveDept" rows="3" cols="70" ></textarea>
	    </td>
    </tr>
    <tr style='page-break-inside:avoid;height:5.0pt'  >
      <td width=120 valign=top class="tabtd7">
      <p class=MsoNormal><span class="label">发送总份数:</span></p>
	    </td>
	    <td width=40 valign=top class="tabtd8">
	      <p class=MsoNormal><span  class="inputcls"> <input type="text" size="5" id="totalPrintCount" name="totalPrintCount" value="0" maxlength="5" style="text-align:right"></span></p>
	    </td>
	    <td width=150 colspan=3 valign=top class="tabtd9" nowrap>
	      <p class=MsoNormal><span class="label">网发份数:</span></p>
	    </td>
	      <td width=40 valign=top class="tabtd10">
	      <p class=MsoNormal><span  class="inputcls"><input type="text" size="5" id="printCount" name="printCount" value="0" onchange="countPrintTotal();" maxlength="5" style="text-align:right"></span></p>
	    </td>
	    <td width=143 colspan=3 valign=top class="tabtd11" style="padding-right:0px;">
	      <p class=MsoNormal><span class="label">纸制发份数:</span></p>
	    </td>
	    <td width=40 valign=top class="tabtd12">
	      <p class=MsoNormal><span  class="inputcls"><input type="text" size="5" id="paperPrintCount" name="paperPrintCount" value="0" onchange="countPrintTotal();" maxlength="5" style="text-align:right"></span></p>
	    </td>
    </tr>
    <tr style='height:67.4pt'>
		  <td width=549 colspan=10 class="tabtd13" align="left">
			  <p class=MsoNormal><span class="label">备注:</span></p>
			  <textarea id="remark" name="remark" rows="3" cols="70"></textarea>
      </td>
    </tr>
  </table>
</div>
<div class=MsoNormal style="width: 750px;text-align: center;margin-left: auto;margin-right: auto; ">
<table align="center">
<tr>
  <td nowrap>
		<span class="label">发送日期：</span>
		<input type="text" size="4" id="yearShow" name="yearShow" maxlength="4" style="text-align:right"><span class="label">年</span>
		<input type="text" size="2" id="mouthShow" name="mouthShow" maxlength="2" style="text-align:right"><span class="label">月</span>
		<input type="text" size="2" id="dayShow" name="dayShow" maxlength="2" style="text-align:right"><span class="label">日</span>
  </td>
</tr>
</table>
</div>
</div>


  <div id="tablePrint" style="display:none;">
  <br>
  <table class="TableBlock" width="80%" align="center" >
    <tr>
      <td nowrap class="TableData" width="152px;" colspan="6" style="background: #C4DE83;"><b style="font-size: 14px;">设置打印信息 </b></td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData" width="152px;">打印控制：</td>
      <td class="TableData" colspan="5" >
        <label><input type="radio" id="isSignYes" name="isSign" value="1" onclick="doPrintControl(1);" checked>是</label>&nbsp;
        <label><input type="radio" id="isSignNO" name="isSign" value="0" onclick="doPrintControl(0);" >否</label>
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="printButton" value="统一设置打印编号" onclick="setPrintNo()" class="BigButtonC" />
        <div id="showPrintTableMain" style="display:none;">
          <table class="TableList" id="showPrintTable">
            <thead>
              <tr class="TableHeader">
                <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:250px;">接收单位</td>
                <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:70px;">打印份数</td>
                <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:70px;">开始编号</td>
                <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:70px;">结束编号</td>
              </tr>
            </thead>
            <tbody id="showPrintTbody"></tbody>
          </table>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单位数：<span id="companyCount"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;打印总份数：<span id="docAllCount"></span>
        </div>
      </td>
    </tr>
  </table>
  </div>
  
  <div id="tableAttachment" style="display:none;">
  <br>
  <table class="TableBlock" width="80%" align="center" >
    <tr>
      <td nowrap class="TableData" colspan="2" style="background: #C4DE83;"><b style="font-size: 14px;">上传附件 </b><font color="red" style="font-size: 14px;">(拨号单位附件总大小不能超过1M，专网单位附件总大小不能超过5M)</font></td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="152px;">文件: </td>
      <td class="TableData">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="attachmentReciveDeptId" name="attachmentReciveDeptId">
        <input type="hidden" id="attachmentReciveDeptDesc" name="attachmentReciveDeptDesc">
        <input type="hidden" id="attachmentUploadNum" name="attachmentUploadNum">
        <input type="hidden" id="mainDocId" name="mainDocId">
        <input type="hidden" id="mainDocName" name="mainDocName">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt"> 无附件</span>
        <table class="TableList" id="showAttTable" style="display:none;">
          <thead>
          <tr class="TableHeader" >
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:250px;">文件名</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:70px;">大小</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:230px;">发送范围</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:40px;">操作</td>
          </tr>
          </thead>
          <tbody id="showAttTbody"></tbody>
        </table>
        <div id="attachTotalDiv" style="font-size: 14px;display:none;color: green;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;附件总大小：<span id="attachTotal"></span></div>
      </td>
    </tr>
    <tr height="25" id="attachment1">
      <td nowrap class="TableData"><span id="ATTACH_LABEL">文件上传：</span></td>
      <td class="TableData" id="fsUploadRow">
         <div id="fsUploadArea" class="flash" style="width:380px;">
           <div id="fsUploadProgress"></div>
           <div id="totalStatics" class="totalStatics"></div>
           <div>
             <input type="button" id="btnStart" class="SmallButtonW" value="开始上传" onclick="swfupload.startUpload();" disabled="disabled">&nbsp;&nbsp;
             <input type="button" id="btnCancel" class="SmallButtonW" value="全部取消" onclick="swfupload.cancelQueue();" disabled="disabled">&nbsp;&nbsp;
          </div>
        </div>
        <div id="attachment1">
          <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
          <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
          <span id="spanButtonUpload" title="批量上传附件"> </span>
        </div>
      </td>
    </tr>
  </table>
  </div>
</form>

<form id="form2" name="form2" action="<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <div id="tableWord" style="display:none;">
  <br>
  <table class="TableBlock" width="80%" align="center" id="tableWordTable">
    <tr>
      <td nowrap class="TableData" colspan="6" style="background: #C4DE83;"><b style="font-size: 14px;">创建正文文件 </b></td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData" width="152px;">创建方式</td>
      <td class="TableData" colspan="5">
        <label><input type="radio" id="createTypeLocal" name="createType" onclick="wordTypeSelect(1)" > 上传本地文件</label> &nbsp;&nbsp;
        <label><input type="radio" id="createTypeNew" name="createType"  onclick="wordTypeSelect(0)" checked> 新建空文件</label> &nbsp;&nbsp;
        <input type="button" class="BigButton" value="确定" onclick="createWord();">
      </td>
    </tr>
    <tr height="25" id="wordLocalTr" style="display : none;">
      <td nowrap class="TableData">选择本地文件：</td>
      <td class="TableData" colspan="5">
        <input type="file" id="fileWord" name="fileWord">
      </td>
    </tr>
  </table>
  <iframe src="" id="wordDocIframe" width="100%" height="420px;" frameBorder="0" style="display: none; margin-top: -20px;"></iframe>
  </div>
  <input type="submit" id="fileWord2" name="fileWord2" style="display:none;">
</form>
<iframe width="0" height="0" id="commintFrame" name="commintFrame" style="display: none;"></iframe>

<div id="confirmDiv" style="display:none;">
	<table class="TableBlock" width="350" align="center">
	  <tr class="TableData">
	    <td nowrap class="TableData" align="center" style="width:200px;">数据交换平台连接状态：</td>
	    <td align="center">
	       <span id="state"><img  src="../images/a0.gif" align="absmiddle">未连接</span>
	    </td>
	  </tr>
	</table>
	<br>
  <table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData" colspan="4" style="background: #C4DE83;"><b style="font-size: 14px;">确认发文登记信息 </b></td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 150px;">文件标题： </td>
      <td class="TableData" colspan="3">
        <div id="docTitleConfirm"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">文件类型： </td>
      <td class="TableData">
        <div id="docTypeConfirm"></div>
      </td>
      <td nowrap class="TableData" style="width: 120px;">紧急程度： </td>
      <td class="TableData">
        <div id="urgentTypeConfirm"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">密级： </td>
      <td class="TableData">
        <div id="securityLevelConfirm"></div>
      </td>
      <td nowrap class="TableData" style="width: 120px;">发送总份数： </td>
      <td class="TableData">
        <div id="printCountConfirm"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">网络接收单位： </td>
      <td class="TableData" colspan="3">
        <div id="reciveDeptConfirm"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">纸质接收单位： </td>
      <td class="TableData" colspan="3">
        <div id="handReciveDeptConfirm"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">备注： </td>
      <td class="TableData" colspan="3">
        <div id="remarkConfirm"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">发送文件： </td>
      <td class="TableData" colspan="3">
        <div id="attachmentConfirm"></div>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan='6' nowrap>
        <input type="button" value="返回" onclick="returnInfo()" class="BigButton">
        <%if(isSend){ %>
        <input type="button" id="sendButton" value="发送" onclick="doSubmit3()" class="BigButton">
        <%} %>
      </td>
    </tr>
  </table>
  <div id="AIPDIV"></div>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  String seqId = request.getParameter("seqId");
String hostIp = request.getLocalAddr();
String serviceName = request.getServerName();
int port = request.getLocalPort();

String filePath = YHSysProps.getAttachPath() + "/docReceive/" ;
filePath = filePath.replace("\\" ,"\\\\");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收文转发文</title>
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
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/jhDocReceive.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/designer.js"></script>
<link rel="stylesheet" href="/yh/subsys/jtgwjh/css/style.css" type="text/css" />
<link rel="stylesheet" href="/yh/subsys/jtgwjh/css/gwjh.css" type="text/css" />
<script type="text/javascript">
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var isClickDocMian = false;
var uploadSize = 5120

function doInit(){
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
  
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/selectById.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    if(data.attachmentId){
      data.attachmentId += ',';
      data.attachmentName += '*';
      data.attachmentSize += ',';
    }
    bindJson2Cntrl(rtJson.rtData);
    $('isSignNO').checked = true;
    //加载AIP控件，获取打印份数和编号--syl
    loadAip(rtJson.rtData.mainDocId,rtJson.rtData.mainDocName,0);
    var attachmentIds = $("attachmentId").value;
    var attachmentNames = $("attachmentName").value;
    var attachmentSize = data.attachmentSize;
    if(attachmentIds){
      if(data.mainDocId){
        $('isSignYes').checked = true;
        
        var attaId = "";
        var attaName = "";
        var attaSize = "";
        var attachIdArrays = attachmentIds.split(",");
        var attachNameArrays = attachmentNames.split("*");
        var attaSizeArrays = attachmentSize.split(",");
        
        for(var i = 0 ; i < attachNameArrays.length ; i++){
          if(!attachIdArrays[i] || attachIdArrays[i] == data.mainDocId){
            continue;
          }
          attaId += attachIdArrays[i] + ",";
          attaName += attachNameArrays[i] + "*";
          attaSize += attaSizeArrays[i] + ",";
        }
        $("attachmentId").value = attaId;
        $("attachmentName").value = attaName;
        attachmentIds = attaId;
        attachmentNames = attaName;
        attachmentSize = attaSize;
      }
      if(attachmentIds){
        $('showAtt').innerHTML = '';
	      var selfdefMenu = {
	          office:["downFile","read"], 
	          img:["downFile","play"],  
	          music:["downFile","play"],  
	          video:["downFile","play"], 
	          others:["downFile"]
	      }
	      var attachmentIdArray =  attachmentIds.split(",");
	      var attachmentNameArray =  attachmentNames.split("*");
	      var attachmentSizeArray = attachmentSize.split(",");
	      uploadNum = attachmentIdArray.length;

	      for(var i = 0 ; i < attachmentIdArray.length; i++){
	        if(attachmentIdArray[i]){
		        var checked = false;
		        if(attachmentIdArray[i] == data.mainDocId){
		          checked = true;
		        }
		        attachMenuSelfUtilTable("showAttTbody","docReceive",attachmentNameArray[i] ,attachmentIdArray[i], attachmentSizeArray[i], '','','',selfdefMenu,i,'','',checked);
		        $('attachMainShow').innerHTML += attachmentNameArray[i] + ',';
	        }
	      }
	      clearCheckbox();
	      
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
        
        //改变附件总数统计颜色及显示
        $('ensize').value = attachTotalCount;
        attachTotalCount = (attachTotalCount/1024).toFixed(0);
        if(attachTotalCount > uploadSize){
          $('attachTotalDiv').style.color = 'red';
        }
        else{
          $('attachTotalDiv').style.color = 'green';
        }
        var attachTotalCountShow = '';
        if(attachTotalCount > 1024){
          attachTotalCountShow = (attachTotalCount/1024).toFixed(1) + 'M';
        }
        else{
          attachTotalCountShow = attachTotalCount + 'K';
        }
        $('attachTotal').innerHTML = attachTotalCountShow;
        
	      $('showAttTable').style.display = "";
        $('attachTotalDiv').style.display = "";
	    }
      else{
        $('showAtt').innerHTML = " 无文件"; 
      }
    }
    else{
      $('showAtt').innerHTML = " 无文件"; 
    }
  }
  $('docTitle').focus();
  isOnline1();
  setInterval(isOnline1, 10*1000);
}

//第一界面确定事件，验证成功后跳转到确认页面
function doSubmit(){
  if(checkForm()){
    var isMainDoc = $('mainDocId').value;
    if(isMainDoc != ""){
      var printCount = $("docAllCount").innerHTML;
      if(parseInt(printCountAll) < parseInt(printCount)){
        alert("打印份数不能超过剩余打印份数" + printCountAll);
        return;
      }
    }
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

//确认页面确定事件
function doSubmit2(){
  if(checkForm()){
    attachmentTrans();
    $("form1").submit();
  }
}

//确认页面发送事件
function doSubmit3(){
  if(checkForm()){
	  $("form1").action = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/addDocInfo.act?seqId=<%=seqId %>&sendFlag=1&forward=1";
	  $("form1").submit();
  }
}

//确认页面盖章事件
function doSubmit4(){
  $("form1").action = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/addDocInfo.act?seqId=<%=seqId %>&sendFlag=2&forward=1";
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
    $('showPrintTableMain').style.display = '';
    $('printButton').style.display = '';
    if($('reciveDept').value){
	    consider(($('reciveDept').value).split(",").length);
    }
  }
  else{
    $('showPrintTableMain').style.display = 'none';
    $('printButton').style.display = 'none';
  }
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
  
  if($('mainDocId').value){
	  setPrintNo();
  }
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
  setPrintNo();
}

//跳转到列表页面
function returnList(){
  location = '<%=contextPath %>/subsys/jtgwjh/receiveDoc/manage/waitHandle.jsp';
}

/**
 * 统一设置打印编号 --syl
 */
function setPrintNo(){
  //var printCount = $('printCount').value;
//  if(printCount == "" || isNaN(printCount)){
 //  alert("请正确设置打印份数！");
  ///  $("printCount").focus();
   // return;
 // }
// alert(printCountAll);//转发真实从API文件中获取
  var realPrintCount = $("docAllCount").innerHTML;//实际转发打印份数
  var nextNo = autoCompletionStr(printCountAll,printStratNo,printEndNo,$("reciveDept").value);
  var obj = $("HWPostil1");
  var SYPrintCount = parseInt(printCountAll,10) - parseInt(realPrintCount,10);
  $("SYPrintCount").value = SYPrintCount;
  $("beginNo").value = nextNo;
  $('printCount').value = realPrintCount;
  $('totalPrintCount').value = parseInt(realPrintCount) + parseInt($('paperPrintCount').value);
}


var printCountName = "printCount";
var printStratName = "printStratNo";
var printEndNoName = "printEndNo";
var printStratNo = 0;
var printEndNo = 0;
var printCountAll = 0;//剩余打印总份数
var filePath = "<%=filePath%>";
/**
 * 加载AIP控件,获取打印份数的编号  --syl
 type---->0-第一加载；1-点击正文
 
 */
 function loadAip(attachmentId,attachmentName,type){
   //加载AIP
   if(attachmentId && attachmentName){
     
     //loadAIPObject($("wordDocIframe"),500,500);\
     setObjHeight();
     var obj = $("HWPostil1");
     obj.ShowDefMenu = false; //隐藏菜单
     obj.ShowToolBar = false; //隐藏工具条
   
     //obj.SilentMode =1 ;//1：安静模式。安静模式下不显示下载提示对话框、文档转化提示对话框和部份提示信息

     var attachmentIdArra = attachmentId.split("_");
     var attachmentIdDate = attachmentIdArra[0];
     var fileName =attachmentName;
     $("tableWord").style.display = "";
     var attachmentIdDate = attachmentId.substr(0,4);
     var fileName = attachmentIdArra[1] + "_" + attachmentName;
     var filePath1 = filePath +attachmentIdDate + "/" + fileName;
     obj.LoadFile("http://<%=serviceName%>:<%=port%><%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePath1));
     $("mainFilePath").value = filePath1;
     window.setTimeout("getPrintInfo(" + type+ ")",1000);
   }
}

/**
 * 获取打印控制信息
 */
 function getPrintInfo(type){
   var obj = $("HWPostil1");
 
   var printStratNoTemp =  obj.GetValueEx(printStratName,2,"",0,"");
   var printEndNoTemp =   obj.GetValueEx(printEndNoName,2,"",0,"");
   var printCountTemp =   obj.GetValueEx(printCountName,2,"",0,"");

   if(printStratNoTemp != ""){
     printStratNo = printStratNoTemp;
   }
   if(printEndNoTemp != "" ){
     printEndNo = printEndNoTemp;
   }
   if(printCountTemp != ""){
     printCountAll = printCountTemp;
     $("printCount").value = printCountAll;
   }
   if(type != "1"){//如果不是点击打印控制则隐藏
     $("tableWord").style.display = "none";
   }

 }
 /*
  * 改变窗口大小的时候更改控件的大小
  */
 function setObjHeight(){
   var height = ((document.documentElement || document.body).offsetHeight - 85) + 'px';
   $('HWPostil1').style.height = height ;
   //$("fieldList").style.height =((document.documentElement || document.body).scrollHeight - 63) + 'px';
 }
 
 window.onresize=function(){
   setObjHeight();
 }

function clearCheckbox(){
  var inputs = $('showAttTbody').getElementsByTagName('input');
  for(i = 0; i < inputs.length; i++){
    if(inputs[i].type == 'checkbox'){
      if(inputs[i].checked){
        var td = inputs[i].parentNode;
        td.innerHTML = '√';
        td.nextSibling.innerHTML = '';
      }
      else{
        inputs[i].style.display = 'none';
      }
    }
  }
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
             $('tableForm').style.display = '';
             break;
    case 2 : $('tab_1').className = 'tab_1';
             $('tab_2').className = 'tab_on_2';
             $('tab_3').className = 'tab_3';
             $('tab_4').className = 'tab_4';
             $('tablePrint').style.display = 'none';
             $('tableForm').style.display = 'none';
             $('tableAttachment').style.display = 'none';
             if($('confirmDiv').style.display == 'none'){
               $('tableWord').style.display = '';
               if(!isClickDocMian){
                 if($('mainDocId').value){
	                 loadAip($('mainDocId').value,$('mainDocName').value,1);
	                 $('wordDocIframe').style.display = '';
	                 isClickDocMian = true;
                 }
                 else{
                   $('wordDocIframe').style.display = 'none';
                   $('returnNull').style.display = ''
                 }
               }
             }
             break;
    case 3 : if($('mainDocId').value == ""){
               alert("此公文没有正文，不能进行打印控制！");
               return;
             }
             $('tab_1').className = 'tab_1';
             $('tab_2').className = 'tab_2';
             $('tab_3').className = 'tab_on_3';
             $('tab_4').className = 'tab_4';
             $('tableForm').style.display = 'none';
             $('tableWord').style.display = 'none';
             $('tableAttachment').style.display = 'none';
             $('tablePrint').style.display = '';
             break;
    case 4 : $('tab_1').className = 'tab_1';
             $('tab_2').className = 'tab_2';
             $('tab_3').className = 'tab_3';
             $('tab_4').className = 'tab_on_4';
             $('tableForm').style.display = 'none';
             $('tablePrint').style.display = 'none';
             $('tableWord').style.display = 'none';
             $('tableAttachment').style.display = '';
             break;
  }
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
    <input type="button" title="发送" onclick="doSubmit();" class="newButton newButtonSend" id="sendControl">
  </div>
</div>

<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/addDocInfo.act?seqId=<%=seqId %>&forward=1"  method="post" name="form1" id="form1" onsubmit="">
<input type="hidden" id="forwordId" name="forwordId" value="<%=seqId%>"> 

<div style='layout-grid:15.6pt' id="tableForm">
  <div class=MsoNormal align=center style='width:750px;text-align:center;margin-left: auto;margin-right: auto;'>
  <span style='font-size:22.0pt;font-family:方正大标宋简体;color:red'>中国兵器工业集团公司发文登记单</span>
  </div><br>
  <div align="center"> 
    <table class=MsoNormalTable border=1 cellspacing=0 cellpadding=0 style='width:750px;margin-left:14.4pt;border-collapse:collapse;border:none'>
      <tr style='height:85.3pt'>
        <td width=549 colspan=10 valign=top class="tabtd1" align="left">
          <p class=MsoNormal><span class="label">标题:</span></p>
          <p class=MsoNormal><span  style='color:red'><textarea name="docTitle" id="docTitle" rows="5" cols="70"></textarea></span></p>
       </td>
     </tr>
     <tr style='height:45.9pt'>
       <td width=549 colspan=10 class="tabtd2">
         <p class=MsoNormal><span class="label">文号: <input type="text" name="docNo" id="docNo" size="80" maxlength="50"></span></p>
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
      <td width=549 colspan=10 valign=top class="tabtd6" align="left">
        <p class=MsoNormal><span class="label">主送单位:</span></p>
        <p class=MsoNormal><span  style='color:red'><textarea name="oaMainSend" id="oaMainSend" rows="5" cols="70"></textarea></span></p>
      </td>
    </tr>
    <tr style='height:85.3pt'>
      <td width=549 colspan=10 valign=top  class="tabtd6" align="left">
        <p class=MsoNormal><span class="label">抄送单位:</span></p>
        <p class=MsoNormal><span  style='color:red'><textarea name="oaCopySend" id="oaCopySend" rows="5" cols="70"></textarea></span></p>
      </td>
    </tr>
    <tr style='height:69.45pt'>
      <td width=549 colspan=10 valign=top  class="tabtd6" align="left">
        <p class=MsoNormal><span class="label">附件:</span></p>
        <p class=MsoNormal style="margin-bottom:5px;"><span id="attachMainShow" style="font-size: 18px;margin-left:20px;"></span></p>
      </td>
    </tr>
    <tr id="selectTr" style='height:63.25pt'>
      <td width=549 colspan=10 valign=top  class="tabtd6" align="left">
        <p class=MsoNormal><span class="label">网络发送单位:</span></p>
        
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
        <div style="clear:both;margin-top:10px;margin-bottom:5px;">
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
        <textarea id="handReciveDept" name="handReciveDept" rows="3" cols="70"  ></textarea>
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
      <td nowrap class="TableData" colspan="6" style="background: #C4DE83;"><b style="font-size: 14px;">设置打印信息 </b></td>
    </tr>
      <tr height="25">
      <td nowrap class="TableData" width="152px;">打印控制：</td>
      <td class="TableData" colspan="5" >
        <label><input type="radio" id="isSignYes" name="isSign" value="1" onclick="doPrintControl(1);" checked>是</label>&nbsp;
        <label><input type="radio" id="isSignNO" name="isSign" value="0" onclick="doPrintControl(0);">否</label>
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="printButton" value="统一设置打印编号" onclick="setPrintNo()" class="BigButtonC"/>
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
      <td nowrap class="TableData" colspan="6" style="background: #C4DE83;"><b style="font-size: 14px;">上传附件 </b><font color="red" style="font-size: 14px;">(拨号单位附件总大小不能超过1M，专网单位附件总大小不能超过5M)</font></td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="152px;">文件: </td>
      <td class="TableData" colspan="5">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="attachmentReciveDeptId" name="attachmentReciveDeptId">
        <input type="hidden" id="attachmentReciveDeptDesc" name="attachmentReciveDeptDesc">
        <input type="hidden" id="attachmentUploadNum" name="attachmentUploadNum">
        <input type="hidden" id="mainDocId" name="mainDocId">
        <input type="hidden" id="mainDocName" name="mainDocName">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="mainFilePath" name="mainFilePath"/>
        <input type="hidden" id="SYPrintCount" name="SYPrintCount"/>
        <input type="hidden" id="beginNo" name="beginNo"/>
        <input type="hidden" id="endNo" name="endNo"/>
        <span id="showAtt"> 无文件</span>
        <table class="TableList" id="showAttTable" style="display:none;">
          <tr class="TableHeader" >
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:250px;">文件名</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:50px;">大小</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:250px;">发送范围</td>
            <td align="center" nowrap style="background-color: rgb(247, 248, 189);padding-left: 0px;width:40px;">操作</td>
          </tr>
          <tbody id="showAttTbody"></tbody>
        </table>
        <div id="attachTotalDiv" style="font-size: 14px;display:none;color:green;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;附件总大小：<span id="attachTotal"></span></div>
      </td>
    </tr>
    <tr height="25" id="attachment1">
      <td nowrap class="TableData"><span id="ATTACH_LABEL">文件上传：</span></td>
      <td class="TableData" colspan="5" id="fsUploadRow">
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

  <div id="tableWord" style="display:none;">
  <br>
  <div style="margin-top: 50px;display: none;" id="returnNull">
    <table class="MessageBox" width="340" align="center">
      <tbody>
        <tr>
          <td class="msg info">
            <div style="font-size: 12pt;" class="content">此公文没有正文!</div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  
  <div id="wordDocIframe" style="width:100%; margin-top: 5px;">
    <OBJECT id=HWPostil1 style="WIDTH:100%;" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='/yh/subsys/jtgwjh/setting/aip/HWPostil.cab#version=3.0.9.4' ></OBJECT>
  </div>
  </div>
<iframe width="0" height="0" id="commintFrame" name="commintFrame" style="display: none;"></iframe>

<div id="confirmDiv" style="display:none;">
	<table class="TableBlock" width="350" align="center">
	  <tr class="TableData">
	    <td nowrap class="TableData" align="center" style="width:200px;">数据交换平台连接状态：</td>
	    <td align="center">
	       <span id="state"><img  src="../../images/a0.gif" align="absmiddle">未连接</span>
	    </td>
	  </tr>
	</table>
	<br>
  <table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData" colspan="4" style="background: #C4DE83;"><b style="font-size: 14px;">确认收文转发文信息 </b></td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="width: 120px;">文件标题： </td>
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
        <div id="attachmentConfirm" name="attachmentConfirm"></div>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan='6' nowrap>
        <input type="button" value="返回" onclick="returnInfo()" class="BigButton">
        <input type="button" id="sendButton" value="发送" onclick="doSubmit3()" class="BigButton">
      </td>
    </tr>
  </table>
</div>
</body>
</html>
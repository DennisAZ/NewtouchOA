<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会议申请</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingmanagelogic.js"></script>
<script type="text/javascript">
var requestUrl = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct";
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
  autoBeginEnd();
	$("mName").focus();
	getMRoomName();
	my_query();
	getMManagerName();
	getOnlineUsers();
	setDate();
	moblieSmsRemind('smsReminde2Div','smsReminde2');
	moblieSmsRemind('sms2RemindDiv','sms2Remind');
	getSysRemind();
}

/**
 * 获取会议室管理员名称
 */
function getMManagerName(){
	var url = requestUrl + "/getMManagerName.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
  var prcs = rtJson.rtData;
	var selects = document.getElementById("mManager");
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.value; 
	  option.innerHTML = prc.text; 
	  selects.appendChild(option);	  
	}
}
/**
 * 获取在线调度员名称 */
function getOnlineUsers(){
	var url = requestUrl + "/getOnlineUsers.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
  var prcs = rtJson.rtData;
	var userName = prcs.userName;
	$("onlineUser").innerHTML = userName;
}

/**
 * 获取有权限的会议室名称 */
function getMRoomName(){
	var url = requestUrl + "/getMRoomName.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
  var prcs = rtJson.rtData;
	var selects = document.getElementById("mRoom");
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.value; 
	  option.innerHTML = prc.text; 
	  option.title = prc.mrDesc; 
	  selects.appendChild(option);	  
	}
}


/**
 * 获取会议室设备下拉列表  checkSelectStr
 */
function my_query(){
	var selectFlag = 0;
	var div=$("equipment");
	div.innerHTML = "数据加载中...";
	var mRoom = $("mRoom").value;
	//var checkBoxFlag = getEquipmentCheckBox();
	var url = "<%=contextPath %>/yh/subsys/oa/meeting/act/YHMeetingEquipmentAct/getEquipmentById.act?mRoom=" + mRoom;
	var json = getJsonRs(url)
	if(json.rtState == "1"){
		alert(json.rtMsrg);
		return ;
	}
	var prcs = json.rtData;	
	var selectStr = prcs.selectDiv;
	//alert("selectStr>>"+selectStr);
	if(selectStr != '0'){
		//selectFlag = 1;
		div.innerHTML = prcs.selectDiv;
	}else{
		div.innerHTML = "无记录";
	}
	
}

//获取会议室设备复选框选中项
function get_checked(){
	var checked_str="";
	for(var i=0;i<document.getElementsByName("checkStr").length;i++){
		var e1=document.getElementsByName("checkStr").item(i);
		if(e1.checked){
			val=e1.value;
			checked_str += val + ",";
		}
	}
	if(i==0){
		var e1=document.getElementsByName("checkStr");
		if(e1.checked){
			val=e1.value;
			checked_str+=val + ",";
		}
	}
	var selectStrs = document.getElementsByName("checkSelectStr");
	var selectLength = selectStrs.length;
	for(var j=0;j<selectLength;j++){
		if(selectStrs[j].value){
			checked_str += selectStrs[j].value + ",";
		}
	}
	return checked_str;
}




//日期
function setDate(){
var date1Parameters = {
   inputId:'mStart',
   property:{isHaveTime:true}
   ,bindToBtn:'date1'
};
new Calendar(date1Parameters);
var date2Parameters = {
   inputId:'mEnd',
   property:{isHaveTime:true}
   ,bindToBtn:'date2'
};
new Calendar(date2Parameters);

var date3Parameters = {
   inputId:'M_START_DATE',
   property:{isHaveTime:false}
   ,bindToBtn:'date3'
   ,callbackFun:week_select
};
new Calendar(date3Parameters);

var date4Parameters = {
   inputId:'M_END_DATE',
   property:{isHaveTime:false}
   ,bindToBtn:'date4'
   ,callbackFun:week_select
     
};
new Calendar(date4Parameters);

}

/**
 * 显示周期性会议
 */
function time_status(str){
	if(str=='1'){
	  document.getElementById("time_status").style.display='';
	  document.getElementById("time_status2").style.display='';
	  document.getElementById("time_status0").style.display='none';      
	}
	if(str=='0'){
	  document.getElementById("time_status").style.display='none';
	  document.getElementById("time_status2").style.display='none';
	  document.getElementById("time_status0").style.display='';
	} 
}

function week_select()
{        
   document.all.WEEKEND1.style.display="none";
   document.all.WEEKEND2.style.display="none";
   document.all.WEEKEND3.style.display="none";
   document.all.WEEKEND4.style.display="none";
   document.all.WEEKEND5.style.display="none";
   document.all.WEEKEND6.style.display="none";
   document.all.WEEKEND0.style.display="none"; 
      
   var x = document.form1.M_START_DATE.value;
   y = x.replace(/-/, ",");    
   y = y.replace(/-/, "/"); 
   y = new Date(y);
   
   var z = document.form1.M_END_DATE.value;
   r = z.replace(/-/, ",");    
   r = r.replace(/-/, "/"); 
   r = new Date(r);
   a = r - y;
   a= a/24/60/60/1000+1;
   if(a>6){
	   document.all.WEEKEND1.style.display="";
	   document.all.WEEKEND2.style.display="";
	   document.all.WEEKEND3.style.display="";
	   document.all.WEEKEND4.style.display="";
	   document.all.WEEKEND5.style.display="";
	   document.all.WEEKEND6.style.display="";
	   document.all.WEEKEND0.style.display="";   
   }else{
     for(var i=x;i<=z;i=DateNextDay(i)){          
       i = i.replace(/-/, ",");    
       i = i.replace(/-/, "/");  
       v = new Date(i);
       s = v.getDay(); 
       if(/^[-]?\d+$/.test(s)){
        w = "WEEKEND"+s;
        document.getElementById(w).style.display="";
       }
     }
  }
}

function  DateNextDay(d2){ 
//slice返回一个数组 
  var   str = d2.slice(5) + "-" + d2.slice(0,4);   
  var d = new Date(str);     
  var d3 = new Date(d.getFullYear(),d.getMonth(),d.getDate()+1);   
  var month=returnMonth(d3.getMonth());       
  var day=d3.getDate(); 
  day=day < 10? "0"+day:day;       
  var str2=d3.getFullYear()+"-"+month+"-"+day; 
  return str2; 
}

function returnMonth(num){       
  var str= "";       
  switch(num)  {       
     case 0: str= "01"; break;       
     case 1: str= "02"; break;       
     case 2: str= "03"; break;       
     case 3: str= "04"; break;       
     case 4: str= "05"; break;       
     case 5: str= "06"; break;       
     case 6: str= "07"; break;       
     case 7: str= "08"; break;       
     case 8: str= "09"; break;       
     case 9: str= "10"; break;       
     case 10: str= "11"; break;       
     case 11: str= "12"; break;       
  }       
  return str;       
}

/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
   var requestUrl = "<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=8";
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
     document.getElementById("smsReminde2").value = "1";
     document.getElementById("sms2Remind").value = "1";
   } else if(moblieRemindFlag == '1') { 
     $(remidDiv).style.display = ''; 
     $(remind).checked = false; 
   }else{
     $(remidDiv).style.display = 'none'; 
   }
 }

//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=8"; 
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1") { 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind; 
  var mobileRemind = prc.mobileRemind;
  if (allowRemind == '2') {
    $("smsReminde1Div").style.display = '';
    $("smsRemindDiv").style.display = 'none';
  }else{ 
    if (defaultRemind == '1') { 
      $("smsReminde1").checked = true;
      $("smsRemind").checked = true;
      document.getElementById("smsReminde1").value = "1";
      document.getElementById("smsRemind").value = "1";
    }
  }
}

/**
 * 内部短信提醒选择
 */
function checkBox2() {
  if (document.getElementById("smsReminde1").checked) {
     document.getElementById("smsReminde1").value = "1";
  }else {
   document.getElementById("smsReminde1").value = "0";
  }
}

/**
 * 手机短信提醒选择
 */
function checkBox3() {
  if (document.getElementById("smsReminde2").checked) {
     document.getElementById("smsReminde2").value = "1";
  }else {
   document.getElementById("smsReminde2").value = "0";
  }
}

//提醒申请人function checkBox(ramCheck,sms) {
  if (document.getElementById(ramCheck).checked) {
     document.getElementById(ramCheck).value = "1";
  }else {
   document.getElementById(ramCheck).value = "0";
  }
}

function checkForm(){
	if($("mName").value.trim() == ""){
		alert("请指定会议名称！");
		$("mName").focus();
		$("mName").select();
    return (false);
	}
	if($("mTopic").value.trim() == ""){
		alert("请指定会议主题！");
		$("mTopic").focus();
		$("mTopic").select();
    return (false);
	}
  if(document.form1.RD.item(1).checked){
  	var leaveDate1 = document.getElementById("mStart"); 
  	var leaveDate2 = document.getElementById("mEnd"); 
  	var leaveDate1Array = leaveDate1.value.trim().split(" "); 
  	var leaveDate2Array = leaveDate2.value.trim().split(" "); 
  	var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  	var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  	var re1 = new RegExp(type1); 
  	var re2 = new RegExp(type2); 
  	
    if(leaveDate1.value == ""){  
       alert("开始时间不能为空！");
     	 $("mStart").focus(); 
       return (false);
     }else{
    	 if(leaveDate1Array.length!=2){
    		 alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
   			 leaveDate1.focus(); 
   			 leaveDate1.select(); 
   			 return false; 
  		 }else{
 				 if(!isValidDateStr(leaveDate1Array[0])||leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){
 				 	 alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
 					 leaveDate1.focus(); 
 					 leaveDate1.select(); 
 					 return false; 
 				 } 
  		 }
     }
     if($("mEnd").value == ""){  
       alert("结束时间不能为空！");
       $("mEnd").focus(); 
       return (false);
     }else{
    	 if(leaveDate2Array.length!=2){ 
   			alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
   			leaveDate2.focus(); 
   			leaveDate2.select(); 
   			return false; 
   		}else{ 
   			if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
   				alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
   				leaveDate2.focus(); 
   				leaveDate2.select(); 
   				return false; 
   			} 
   		}
				
     }
     if($("mStart").value == $("mEnd").value){  
        alert("开始时间与结束时间不能相等！");
        $("mEnd").focus(); 
        $("mEnd").select(); 
        return (false);
     }
     if($("mStart").value!="" && $("mEnd").value!="") {       
        var M_START = $("mStart").value.replace("-","/");
        var M_END = $("mEnd").value.replace("-","/");  
        M_START = new Date(Date.parse(M_START));   
        M_END = new Date(Date.parse(M_END));  
        if(M_START > M_END){   
          alert("开始时间不能大于结束时间");   
          return false;
        }  
     }
     if($("resendLong").value){
    	 if(isNaN($("resendLong").value)){
    			alert("提前提醒应为数字！");
    			$("resendLong").focus();
    			$('resendLong').select();		
    			return false;
    		}else{
					var resendLong = parseInt($("resendLong").value);
					$("resendLong").value = resendLong;
      	}
     }
     if($("resendSeveral").value){
    	 if(isNaN($("resendSeveral").value)){
    			alert("提醒次数应为数字！");
    			$("resendSeveral").focus();
    			$('resendSeveral').select();		
    			return false;
    		}else{
					var resendSeveral = parseInt($("resendSeveral").value);
					$("resendSeveral").value = resendSeveral;
      	}
     }
     //document.form1.action="add.php";
     document.form1.action="<%=contextPath %>/yh/subsys/oa/meeting/act/YHMeetingAct/addMeetingInfo.act";
  }

  //周期性会议  if(document.form1.RD.item(0).checked){
     if(document.form1.M_START_DATE.value == ""){  
       alert("开始日期不能为空！");
       $("M_START_DATE").focus(); 
       return (false);
     }
     if(document.form1.M_END_DATE.value == ""){  
       alert("结束日期不能为空！");
       $("M_END_DATE").focus(); 
       return (false);
     }
     if(document.form1.M_START_TIME.value == ""){  
       alert("会议开始时间不能为空！");
       $("M_START_TIME").focus(); 
       return (false);
     }
     if(document.form1.M_END_TIME.value == ""){  
       alert("会议结束时间不能为空！");
       $("M_END_TIME").focus(); 
       return (false);
     }
     if(document.form1.M_START_DATE.value != "" && document.form1.M_END_DATE.value != "") {  
       checkDate("M_START_DATE");
        var arr = document.form1.M_START_DATE.value.split("-");
        var starttime = new Date(arr[0],arr[1],arr[2]);
        var starttimes = starttime.getTime();
        var arrs = document.form1.M_END_DATE.value.split("-");
        var lktime = new Date(arrs[0],arrs[1],arrs[2]);
        var lktimes = lktime.getTime();
        if(starttimes > lktimes){           
           alert('结束日期不能小于开始日期！');
           return false;
        }
     }
    if(document.form1.M_START_TIME.value != "" && document.form1.M_END_TIME.value != ""){
       var endTime = document.form1.M_END_TIME.value;
       var beginTime = document.form1.M_START_TIME.value;
       endTime = "2000/01/01 " + endTime;
       beginTime = "2000/01/01 " + beginTime;
       if(beginTime > endTime){
          alert('会议结束时间不能小于开始时间');
          $("M_END_TIME").focus(); 
          return false;
       }
       if(beginTime == endTime){
          alert('会议结束时间不能等于开始时间');
          $("M_END_TIME").focus(); 
          return false;
       }
    }         
    document.form1.action = "<%=contextPath %>/yh/subsys/oa/meeting/act/YHMeetingAct/addCycleMeetingInfo.act";
  }
	return true;
}

//判断时间(没有时间)
function checkDate(dateValue){
	var leaveDate1 = document.getElementById(dateValue); 
	var leaveDate1Array = leaveDate1.value.trim().split(" "); 
	if(!leaveDate1.value){
		return true;
	}
	if(!isValidDateStr(leaveDate1Array[0])){
		alert("日期格式不对，应形如 1999-01-01"); 
		leaveDate1.focus(); 
		leaveDate1.select(); 
		return false; 
	}
	return true;
}

function doSubmit(){
	if(checkForm()){
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		$("mDesc").value= oEditor.GetXHTML();
		if($("calendar").checked == true){
			$("calendar").value =1;
		}else{
			$("calendar").value = 0;
		}
		var checkEquipmentes = get_checked();
		$("checkEquipmentes").value = checkEquipmentes
		
		if (jugeFile()) {//如果有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
		$("form1").submit();
		//alert("通过。。。。");
		//var kkkcc = $("checkSelectStr").value;
		//alert(kkkcc);
	}
}

var timeField = null;
function getTime(M_START_TIME){
  timeField = M_START_TIME;
  var URL= contextPath + "/subsys/oa/meeting/apply/clock.jsp";
  openDialogResize(URL , 350, 150);
  //window.open(URL,"aaee","height=50px,width=50x,directories=no,menubar=no,toolbar=no,status=no,scrollbars=yes,location=no");
}

//预约情况
function openPrearrange() {
  window.open( contextPath + '/subsys/oa/meeting/apply/prearrange.jsp','','height=500,width=820,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=100,resizable=yes');
}

//插入图片
function InsertImage(src){
 	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
 	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )  	{
 		oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
 	}
}
//判断是否有附件
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

//附件上传
var isUploadBackFun = false;
function upload_attach(){
  if(checkForm()){
    $("btnFormFile").click();
  }  
}
function handleSingleUpload(rtState,rtMsrg,rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += data.attrId;
  $('attachmentName').value += data.attrName;
  attachMenuUtil("showAtt","meeting",null,$('attachmentName').value ,$('attachmentId').value,false);
	//attachMenuSelfUtil("showAtt","meeting",$('attachmentName1').value ,$('attachmentId1').value, 'jj','','',selfdefMenu);
  removeAllFile();
  if (isUploadBackFun) {
  	doSubmit();
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
  for (var i = 0 ; i < attachNameArrays.length ; i++) {
    if (!attachIdArrays[i] || attachIdArrays[i] == attachId) { 
    	continue; 
    }
    attaName += attachNameArrays[i] + "*"; 
    attaId += attachIdArrays[i] + ","; 
  }
  $('attachmentId').value = attaId; 
  $('attachmentName').value = attaName;
  return true;
}

</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" HEIGHT="20"><span class="big3"> 会议申请(<font size="2">申请之前,请查询“待批会议”,“已准会议”,“进行中会议”三种状态的会议,以避免时间冲突</font>)</span>
    </td>
  </tr>
</table>
<br>
 
<form action="" enctype="multipart/form-data" method="post" name="form1" id="form1">
<table align="center" width="90%" class="TableBlock">
	<tr>
    <td nowrap class="TableData"> 出席人员（外部）：</td>
    <td class="TableData" colspan="3">
      <textarea name="mAttendeeOut" id="mAttendeeOut" class="BigInput"  cols="45" rows="2"></textarea>
      &nbsp;&nbsp;&nbsp;<input type="button" value="会议室管理制度" class="BigButtonC" onClick="window.open('meetingrule.jsp','','height=300,width=492,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left=200,resizable=yes');">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 出席人员（内部）：</td>
    <td class="TableData" colspan="3">
      <input type="hidden" name="mAttendee" id="mAttendee" value="">
      <textarea cols=50 name="mAttendeeDesc" id="mAttendeeDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['mAttendee', 'mAttendeeDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('mAttendee').value='';$('mAttendeeDesc').value='';">清空</a>

    </td>
  </tr>
    <tr>
      <td nowrap class="TableData">查看范围（部门）：</td>
      <td class="TableData" colspan="3">
        <input type="hidden" name="toId" id="toId" value="">
	      <textarea cols=50 name="toIdDesc" id="toIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
	      <a href="javascript:;" class="orgAdd" onClick="selectDept(['toId', 'toIdDesc']);">添加</a>
	      <a href="javascript:;" class="orgClear" onClick="$('toId').value='';$('toIdDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">查看范围（角色）：</td>
      <td class="TableData" colspan="3">
        <input type="hidden" name="privId" id="privId" value="">
	      <textarea cols=50 name="privIdDesc" id="privIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
	      <a href="javascript:;" class="orgAdd" onClick="selectRole(['privId', 'privIdDesc']);">添加</a>
	      <a href="javascript:;" class="orgClear" onClick="$('privId').value='';$('privIdDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData">查看范围（人员）：</td>
      <td class="TableData" colspan="3">
        <%-- <input type="hidden" name="SECRET_TO_ID" value="">--%>
        <input type="hidden" name="secretToId" id="secretToId" value="">
	      <textarea cols=50 name="secretToIdDesc" id="secretToIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
	      <a href="javascript:;" class="orgAdd" onClick="selectUser(['secretToId', 'secretToIdDesc']);">添加</a>
	      <a href="javascript:;" class="orgClear" onClick="$('secretToId').value='';$('secretToIdDesc').value='';">清空</a>
      </td>
   </tr>
  <tr>
    <td nowrap class="TableData" >名    称：<font style='color:red'>*</font></td>
    <td class="TableData" colspan="3">
      <input type="text" name="mName" id="mName" size="60" maxlength="100" class="BigInput" value="">
    </td>
  </tr> 
  <tr>
    <td nowrap class="TableData"> 主    题：<font style='color:red'>*</font></td>
    <td class="TableData" colspan="3">
      <input type="text" name="mTopic" id="mTopic" size="60" maxlength="100" class="BigInput" value="">
    </td>
  	</tr>
  <tr>
    <td nowrap class="TableData" width="50">会  议  室：</td>
    <td class="TableData">
    	<select id="mRoom" name="mRoom" onchange="my_query();">
      </select>
      &nbsp;<a href="javascript:;" onClick="window.open('<%=contextPath %>/subsys/oa/meeting/apply/roomDetail.jsp','','height=500,width=820,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left=200,resizable=yes');">详情</a>
      &nbsp;<input type="button" value="预约情况" class="BigButton" onClick="openPrearrange();">
   </td>
  <td nowrap class="TableData" width="85"> 会议室管理员：</td>
    <td class="TableData" id="select_manager">
    	<select name="mManager" id="mManager" >
      </select>
  </td>
  </tr>
  <tr>
    <td nowrap class="TableData">周期性会议申请:</td>
    <td nowrap class="TableData">
    	<input type="radio" name="RD" value="1" onClick="time_status('1')">是 
    	<input type="radio" name="RD" value="0" onClick="time_status('0')" checked="true">否 
    </td>
      <td nowrap class="TableData"> 在线调度人员：</td>
      <td class="TableData" colspan="3" id="online_user">
      	<span id="onlineUser"></span>
		</td>
    </tr>
    <tr id="time_status0">
    <td nowrap class="TableData" width="70"> 开始时间：</td>
    <td class="TableData">
      <input type="text" name="mStart" id="mStart" size="19" maxlength="19" class="BigInput" value="<%=curTime.format(new Date()) %>">
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
    <td nowrap class="TableData" width="70"> 结束时间：</td>
    <td class="TableData">
      <input type="text" name="mEnd" id="mEnd" size="20" maxlength="19" class="BigInput" value="<%=curTime.format(new Date()) %>">
      <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    
    </td>
  </tr>
  <tr id="time_status" style="display:none;">
    <td nowrap class="TableData" width="70"> 会议日期：</td>
    <td class="TableData">
      <input type="text" name="M_START_DATE" id="M_START_DATE" size="10" maxlength="10" class="BigInput" value="" onClick="">
      <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
至      <input type="text" name="M_END_DATE" id="M_END_DATE" size="10" maxlength="10" class="BigInput" value="" onClick="">
    	<img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    <td nowrap class="TableData" width="70"> 会议时间：</td>
    <td class="TableData">
 			 <input type="text" name="M_START_TIME" id="M_START_TIME" size="10" maxlength="8" class="BigInput" value="">
      <img src="<%=imgPath %>/clock.gif" align="middle" border="0" align="middle" style="cursor:pointer" onclick="getTime('M_START_TIME')">&nbsp;至      <input type="text" name="M_END_TIME" id="M_END_TIME" size="10" maxlength="8" class="BigInput" value="">
      <img src="<%=imgPath %>/clock.gif" align="middle" border="0" align="middle" style="cursor:pointer" onclick="getTime('M_END_TIME');">
    </td>
  </tr>
  <tr id="time_status2" style="display:none;">
    <td nowrap class="TableData" width="100"> 申请星期：</td>
    <td class="TableData" colspan="3">
     <span id="WEEKEND1" style="display:none"><input type="checkbox" name="W1" value="1">星期一</span>
     <span id="WEEKEND2" style="display:none"><input type="checkbox" name="W2" value="1">星期二</span>
     <span id="WEEKEND3" style="display:none"><input type="checkbox" name="W3" value="1">星期三</span>
     <span id="WEEKEND4" style="display:none"><input type="checkbox" name="W4" value="1">星期四</span>
     <span id="WEEKEND5" style="display:none"><input type="checkbox" name="W5" value="1">星期五</span>
     <span id="WEEKEND6" style="display:none"><input type="checkbox" name="W6" value="1">星期六</span>
     <span id="WEEKEND0" style="display:none"><input type="checkbox" name="W7" value="1">星期日</span>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 提醒设置：</td>
    <td class="TableData">
    	 提前 <input type="text" name="resendLong" id="resendLong" size="4" maxlength="3" class="BigInput" style="text-align:right" value=""> 小时提醒，提醒 <input type="text" name="resendSeveral" id="resendSeveral" class="BigInput" size="4" maxlength="3" style="text-align:right" value=""> 次
  </td>
   <td nowrap class="TableData"> 写入日程安排：</td>
    <td class="TableData">
    	  <input type="checkbox" name="calendar" id="calendar" value="" checked><label for="calendar">是</label>
  </td>
  </tr>
  <tr>
    <td nowrap class="TableData">会议室设备：</td>
    <td nowrap class="TableData" colspan="3">
    
    	<span id="checkBoxSpan"></span> <div id="equipment" ></div>
    </td>
  </tr>
    <tr>
      <td nowrap class="TableData">会议纪要员：</td>
      <td  class="TableData" colspan="3">
        <input type="hidden" name="recorder" id="recorder" value="">
        <input type="text" name="recorderDesc" id="recorderDesc" size="20" readonly class="BigInput" maxlength="20"  value="">
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['recorder', 'recorderDesc']);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('recorder').value='';$('recorderDesc').value='';">清空</a>
      </td>
    </tr>
  <tr>
    <td nowrap class="TableData" width="80"> 提醒会议室管理员：</td>
    <td class="TableData" colspan="3">
    	<span id="smsReminde1Div"> <input type="checkbox" name="smsReminde1" id="smsReminde1" onClick="checkBox2();"><label for="smsReminde1">使用内部短信提醒</label>&nbsp; </span>
    	<span id="smsReminde2Div"> <input type="checkbox" name="smsReminde2" id="smsReminde2" onClick="checkBox3();"><label for="smsReminde2">使用手机短信提醒</label> </span>
  </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="80">通知出席人员：</td>
    <td class="TableData" colspan="3">
    	<span id="smsRemindDiv"><input type="checkbox" name="smsRemind" id="smsRemind" onClick="checkBox('smsRemind','smsRemindDiv')"><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>
    	<span id="sms2RemindDiv"><input type="checkbox" name="sms2Remind" id="sms2Remind" onClick="checkBox('sms2Remind','sms2RemindDiv')"><label for="sms2Remind">使用手机短信提醒</label>    </span>
		</td>
  </tr>
  <tr>
    <td nowrap class="TableData">附件文档：</td>
    <td nowrap class="TableData" colspan="3">
    	<input type="hidden" id="attachmentName" name="attachmentName"></input>
      <input type="hidden" id="attachmentId" name="attachmentId"></input>
      <span id="showAtt"></span>
		</td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">附件选择：</td>
    <td class="TableData" colspan="3">
	    <script>ShowAddFile();</script>
	    <script>ShowAddImage('','img');</script>
	    <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script> 
			<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
			<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			<%--插入图片 --%>
			<input type="hidden" id="moduel" name="moduel" value="meeting">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
			<input type="hidden" id="imgattachmentName" name="imgattachmentName">
     </td>
  </tr>  
  <tr>
    <td nowrap class="TableData" colspan="4"><span id="ATTACH_LABEL">会议描述：</span></td>
  </tr>
  <tr id="EDITOR">
    <td class="TableData" colspan="4">
		   <div>
		     <script language=JavaScript>    
		      var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
		      var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
		      oFCKeditor.BasePath = sBasePath ;
		      oFCKeditor.Height = 200;
		      var sSkinPath = sBasePath + 'editor/skins/office2003/';
		      oFCKeditor.Config['SkinPath'] = sSkinPath ;
		      oFCKeditor.Config['PreloadImages'] =
		                      sSkinPath + 'images/toolbar.start.gif' + ';' +
		                      sSkinPath + 'images/toolbar.end.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonarrow.gif' ;
		      //oFCKeditor.Config['FullPage'] = true ;
		       oFCKeditor.ToolbarSet = "fileFolder";
		      oFCKeditor.Value = '' ;
		      oFCKeditor.Create();
		     </script>
  </div>
 		</td>
  </tr>
  <tr class="TableControl">
    <td nowrap colspan="6" align="center">
      <input type="hidden" name="mDesc" id="mDesc" value="">
      <input type="hidden" name="checkEquipmentes" id="checkEquipmentes" value="">
      <input type="button" value="确定" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;&nbsp;
    </td>
  </tr>
  </table>
</form>
<form id="formFile" action="<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
</body>
</html>
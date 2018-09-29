<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId= request.getParameter("userId");
String treeFlag= request.getParameter("treeFlag");
if(seqId == null){
	seqId = "";
}
if(treeFlag == null){
	treeFlag = "";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.subsys.oa.hr.manage.staffInfo.act.YHHrStaffInfoAct"%>
<%@page import="java.io.File"%><html>
<head>
<title>编辑人事档案</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffInfo/js/staffInfoLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
var dbUserId = "<%=seqId%>";
function doInit(){
	//alert( " userId>>"+cckk);
//初始化日期	setDate();
//是否允许登录
	getHrSetUserLogin2();
//获取角色名称
	getUserPrivName("userPriv");
//籍贯	
	getSelectedCode("AREA","staffNativePlace");
//政治面貌
	getSelectedCode("STAFF_POLITICAL_STATUS","staffPoliticalStatus");
//户口类别
	getSelectedCode("HR_STAFF_TYPE","staffType");
//员工类型
	getSelectedCode("STAFF_OCCUPATION","staffOccupation");
//职称
	getSelectedCode("PRESENT_POSITION","presentPosition");
//在职状态

	getSelectedCode("WORK_STATUS","workStatus");
//学历
	getSelectedCode("STAFF_HIGHEST_SCHOOL","staffHighestSchool");
//学位
	getSelectedCode("EMPLOYEE_HIGHEST_DEGREE","staffHighestDegree");
//给信息域值
	getHrStaffInfoById();
//禁止登录提示
	getNotLoginInfo();
//获取用户信息
  getPersonInfo();
}
//日期
function setDate(){
	var date1Parameters = {
		inputId:'staffBirth',
		property:{isHaveTime:false},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
		inputId:'joinPartyTime',
		property:{isHaveTime:false},
		bindToBtn:'date2'
	};
	new Calendar(date2Parameters);
	
	var date3Parameters = {
		inputId:'datesEmployed',
		property:{isHaveTime:false},
		bindToBtn:'date3'
	};
	new Calendar(date3Parameters);
	
	var date4Parameters = {
		inputId:'beginSalsryTime',
		property:{isHaveTime:false},
		bindToBtn:'date4'
	};
	new Calendar(date4Parameters);
	
	var date5Parameters = {
		inputId:'jobBeginning',
		property:{isHaveTime:false},
		bindToBtn:'date5'
	};
	new Calendar(date5Parameters);
	
	var date6Parameters = {
		inputId:'graduationDate',
		property:{isHaveTime:false},
		bindToBtn:'date6'
	};
	new Calendar(date6Parameters);
	
}
function checkUser(id){
	if(id.trim() == ""){
		leavMouth(id);
		return ;
	}
	$("user_id_msg").innerHTML="<img src='<%=imgPath%>/loading_16.gif' align='absMiddle'> 检查中，请稍候……";
	var url = contextPath + "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/checkUser.act";
	var rtJson = getJsonRs(url,"userId=" + id);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	if(prcs.isHave == "0"){
		$("user_id_msg").innerHTML="<img src='<%=imgPath%>/correct.gif' align='absMiddle'>";
	}else{
		$("user_id_msg").innerHTML="<img src='<%=imgPath%>/error.gif' align='absMiddle'> 该用户名已存在";
		$("userId").focus();
		$("userId").select();
	}
	
}
/**
 * 是否允许登录(编辑)
 * 
 * @return
 */
function getHrSetUserLogin2() {
	var url = contextPath
	+ "/yh/subsys/oa/hr/setting/act/YHHrSetOtherAct/getHrSetUserLogin2.act";
	var rtJson = getJsonRs(url,"seqId=" + encodeURIComponent(dbUserId) + "&treeFlag=<%=treeFlag%>");
	//alert(rsText);
	if (rtJson.rtState == "0") {
		if (rtJson.rtData.isLogin == 1 && rtJson.rtData.counter ==0) {
			document.getElementById("yesOrNotStr").checked = true;
			document.getElementById("yesOrNot").value = "on";
		} else {
			document.getElementById("yesOrNotStr").disabled = true;
		}
	} else {
		alert(rtJson.rtMsrg);
	}
}

function doSubmit(){
	if(checkForm()){
		var yesOrNotStr = $("yesOrNotStr");
		if(yesOrNotStr.checked){
			//alert("yes");
			//document.getElementById("yesOrNot").value = "on";
		}else{
			//alert("no");
			//document.getElementById("yesOrNot").value = "";
		}
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		$("resume").value = oEditor.GetXHTML();
		var operation = $("operation").value ;
		if(operation == "1"){
			document.form1.action="<%=contextPath %>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/addHrStaffInfo.act";
		}else{
			document.form1.action="<%=contextPath %>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/updateHrStaffInfo.act";
		}
		$("form1").submit();
	}
}
function checkForm(){
	if($("userId").value.trim() ==""){
		alert("OA用户名不能为空!");
		$("userId").focus();
		return false;
	}
	if($("staffName").value.trim() ==""){
		alert("姓名不能为空!");
		$("staffName").focus();
		return false;
	}
	if(!$("deptId").value.trim()){
		//alert("部门不能为空!");
		//$("deptIdDesc").focus();
		//return false;
	}
	return true;
}
function leavMouth(id){
	if(id.trim() == ""){
		$("user_id_msg").innerHTML="";
	}
}
var deleFlag = 0;
function getPersonInfo(){
	var url = "<%=contextPath %>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/getPersonInfo.act";
	var rtJson = getJsonRs(url,"seqId=" + encodeURIComponent(dbUserId) + "&treeFlag=<%=treeFlag%>");
	//获取角色名称
	//getUserPrivName("userPriv");
	if (rtJson.rtState == "0") {
	  var treeFlag = "<%=treeFlag%>";
	  if(treeFlag == "1"){
	    $("userId").value = "<%=seqId%>";
	  }
	  var prcs = rtJson.rtData;
	  if(prcs.userName){
	    //alert(prcs.userName);
	    //$("staffName").value = prcs.userName;
	    if(prcs.deptId!="0"){
	      $("staffNameTitle").innerHTML= "("+ prcs.userName + ")";
	    }else{
	      $("staffNameTitle").innerHTML= "(用户已删)";
	      deleFlag = 1;
	    }
	  }
	  if(prcs.deptId &&prcs.deptId!="0"){
	    $("deptId").value = prcs.deptId;
	    $("deptIdDesc").value = prcs.deptName;
	  }else{
	    $("deptId").value = "";
	  }
	  if(prcs.userPriv){
	    setSelectValue("userPriv" , prcs.userPriv);
	  }
	  if(prcs.deptId != "0"){
	    $("staffLeaveSpan").show();
	  }
	} else {
		alert(rtJson.rtMsrg);
		return;
	}
}
function setSelectValue(optionType , extValue) {
  var selects = document.getElementById(optionType);
	for ( var i = 0; i < selects.length; i++) {
		var prc = selects[i];
		if (extValue && (extValue == prc.value)) {
		  prc.selected = true;
		}
	}
}
function getHrStaffInfoById(){
	var url = "<%=contextPath %>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/getStaffInfoById.act";
	var rtJson = getJsonRs(url,"seqId=" + encodeURIComponent(dbUserId) + "&treeFlag=<%=treeFlag%>");
	if (rtJson.rtState == "0") {
		var prcs = rtJson.rtData;
		var staffSeqId = prcs.seqId;
		bindJson2Cntrl(rtJson.rtData);
		
    if(prcs.joinPartyTime){
      $("joinPartyTime").value = prcs.joinPartyTime.substr(0,10);
    }
    if(prcs.datesEmployed){
      $("datesEmployed").value = prcs.datesEmployed.substr(0,10);
    }
    if(prcs.beginSalsryTime){
      $("beginSalsryTime").value = prcs.beginSalsryTime.substr(0,10);
    }
    if(prcs.jobBeginning){
      $("jobBeginning").value = prcs.jobBeginning.substr(0,10);
    }   
    if(prcs.graduationDate){
      $("graduationDate").value = prcs.graduationDate.substr(0,10);
    } 
    
		
		if(deleFlag =="1" ){
			$("deptId").value= "";
		}
		if(prcs.resume){
			fckContentStr = prcs.resume; 
		}
		if(rtJson.rtMsrg == "1"){
			$("buildDiv").innerHTML = "- 已建档"; 
			$("operation").value = "2";
		}else{
			$("buildDiv").innerHTML = "- 未建档" ;
			$("operation").value = "1";
		}
		if(prcs.staffSex == "0"){
			$("staffSex").value = "0";
		}else{
			$("staffSex").value = "1";
		}
		if(prcs.staffBirth){
			$("staffBirth").value = prcs.staffBirth.substr(0,10);
		}
		//alert(prcs.photoName);
		if(prcs.photoName){
			var photoName = prcs.photoName;
			var filePathStr = "<%=YHSysProps.getAttachPath().replace('\\','/') + "/" + YHHrStaffInfoAct.headPicFolder%>" ;
			var imgStr = "<img src=<%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePathStr) + "/" +  encodeURIComponent(photoName) + " border='0'  width='150'  ></img>";
			//alert(imgStr);
			$("showImageSpan").innerHTML = imgStr;
			$("showPicOpt").innerHTML = "照片更改：";

			var delPicStr = "<a href=javascript:deletePhoto('" + staffSeqId +"')> 删除照片</a>";
			
			$("delPicSpan").innerHTML = delPicStr;
		}else{
			$("showImageSpan").innerHTML = "暂无照片";
			$("showPicOpt").innerHTML = "照片上传：";
		}
		

		
		if(prcs.attachmentId){
			$("returnAttId").value = prcs.attachmentId;
			$("returnAttName").value = prcs.attachmentName;
			var selfdefMenu = {
          office:["downFile","dump","read","edit","deleteFile"], 
          img:["downFile","dump","play","deleteFile"],  
          music:["downFile","play","deleteFile"],  
          video:["downFile","play","deleteFile"], 
          others:["downFile","dump","deleteFile"]
      }
      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','',staffSeqId,selfdefMenu);
		}else{
			$('attr').innerHTML = "无附件";
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}

function deletePhoto(seqId){
	var msg="确定要删除上传的照片吗?";
	if(window.confirm(msg)){
		var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct";
		var url = requestURLStr + "/deletePhoto.act";
		var rtJson = getJsonRs(url, "seqId=" + seqId);
		if (rtJson.rtState == "0") {
			window.location.reload();
		} else {
			alert(rtJson.rtMsrg);
		}
	}
}

function getNotLoginInfo(){
	var url = "<%=contextPath %>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/getNotLoginInfo.act";
	var rtJson = getJsonRs(url,"seqId=" + encodeURIComponent(dbUserId) + "&treeFlag=<%=treeFlag%>");
	if (rtJson.rtState == "0") {
		var prcs = rtJson.rtData;
		var notLogin = prcs.notLogin;
		if(notLogin =="1"){
			$("notLoginStatusDiv").innerHTML = "&nbsp;<font color=red>禁止登录</font>";
		}else{
			$("notLoginStatusDiv").innerHTML = "";
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}
//插入图片
function InsertImage(src){
 	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
 	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )  	{
 		oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
 	}
}
function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}

//删除附件
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/delFloatFile.act?delAttachId=" + attachId + "&seqId="+encodeURIComponent(dbUserId);
  var json=getJsonRs(url);
  if(json.rtState =='1'){
    alert(json.rtMsrg);
    return false;
  }else{
    prcsJson=json.rtData;
    var updateFlag=prcsJson.updateFlag;
    if(updateFlag == "1"){
      return true;
    }else{
      return false;
    }
  }
}

</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="770" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td>
    	<img src="<%=imgPath%>/notify_new.gif" align="middle"><span class="big3"> 人员档案<span id="staffNameTitle"></span>

    	<span id="buildDiv"></span> <span id="notLoginStatusDiv"></span> </span>&nbsp;&nbsp;
    
    </td>
  </tr>
</table>

<form enctype="multipart/form-data" action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="770" align="center">
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;基本信息</b></td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">OA用户名：<font color="red">*</font> </td>
    <td class="TableData" width="180">
      <input type="text" name="userId" id="userId" value="" class="BigStatic"  size="10" maxlength="20" readonly="readonly"  >&nbsp;  	
    </td>
    <td nowrap class="TableData" width="100">是否允许登录：</td>
    <td class="TableData"  width="200" colspan="2">
      <input type="checkbox" name="yesOrNotStr" id="yesOrNotStr"  value=""> 是
      <span><font color="blue">（注：此权限由管理员开放）</font></span>
    </td>
    <td class="TableData" align="center" rowspan="6" colspan="2">
    	<span id="showImageSpan"></span>
    </td> 
  </tr>
   <tr>
    <td nowrap class="TableData" width="100">部门：</td>
    <td class="TableData" width="180">
    	<input type="hidden" name="deptId" id="deptId" class="BigStatic" value="">
    	<input type="text" name="deptIdDesc" id="deptIdDesc" class="BigStatic" value="" readonly="readonly">
    </td>
    <td nowrap class="TableData"> 角色：</td>
    <td class="TableData" colspan=2 width=80%>
       <select name="userPriv" id="userPriv" >
        </select>
      </td>      
  </tr>
  <tr>
     <td nowrap class="TableData" width="100">编号：</td>
    <td class="TableData" width="180"><input type="text" name="staffNo" id="staffNo" class="BigInput"></td>
    <td nowrap class="TableData">工号：</td>
    <td class="TableData" colspan="2"><input type="text" name="workNo" id="workNo" class="BigInput"></td>
  </tr>
  <tr>
  	<td nowrap class="TableData">姓名：<font color="red">*</font> </td>
    <td class="TableData"><input type="text" name="staffName" id="staffName" class="BigInput"></td>
    <td nowrap class="TableData">英文名：</td>
    <td class="TableData" colspan="2"><input type="text" name="staffEName" id="staffEName" class="BigInput"></td>          
  </tr>
  <tr>
  	<td nowrap class="TableData">身份证号：</td>
    <td class="TableData" ><input type="text" name="staffCardNo" id="staffCardNo" class="BigInput" onBlur="checkIdcard('staffCardNo','staffBirth','staffAge','staffSex')"></td>
    <td nowrap class="TableData">出生日期：</td>
    <td class="TableData" colspan="2" title="填写完身份证号码后可直接生成">
      <input type="text" name="staffBirth" id="staffBirth" size="10" maxlength="10" class="BigInput" value="" />
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>             
  </tr>
  <tr>
    <td nowrap class="TableData">年龄：</td>
    <td class="TableData" title="填写完身份证号码后可直接生成"><input type="text" name="staffAge" id="staffAge" size="4" class="BigInput">岁</td>
    <td nowrap class="TableData">性别：</td>
    <td class="TableData" colspan="2" title="填写完身份证号码后可直接生成">
      <select name="staffSex" id="staffSex" >
          <option value="0">男</option>
          <option value="1">女</option>
      </select>
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">籍贯：</td>
    <td class="TableData" >
      <select name="staffNativePlace" id="staffNativePlace" >
        <option value=""></option>
      </select>
    </td>         
    <td nowrap class="TableData" >民族：</td>
    <td class="TableData" colspan="3"><input type="text" name="staffNationality" id="staffNationality" class="BigInput"></td>          
  </tr>
  <tr>
    <td nowrap class="TableData">婚姻状况：</td>
    <td class="TableData">
      <select name="staffMaritalStatus" id="staffMaritalStatus" >
        <option value=""></option>
        <option value="0">未婚</option>
        <option value="1">已婚</option>
        <option value="2">离异</option>
        <option value="3">丧偶</option>
      </select>     
    </td>
    <td nowrap class="TableData" width="100">健康状况：</td>
    <td class="TableData"  colspan="3"><input type="text" name="staffHealth" id="staffHealth" class="BigInput"></td>
  </tr>
  <tr>
    <td nowrap class="TableData">政治面貌：</td>
    <td class="TableData" width="180">
      <select name="staffPoliticalStatus" id="staffPoliticalStatus" >
        <option value=""></option>
      </select>
     </td>
     <td nowrap class="TableData" width="100">入党时间：</td>
    <td class="TableData"  colspan="3">
       <input type="text" name="joinPartyTime" id="joinPartyTime" size="10" maxlength="10" class="BigInput" value="" />
       <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>     
  </tr>   
  <tr>
    <td nowrap class="TableData">户口类别：</td>
     <td class="TableData">
      <select name="staffType" id="staffType" >
        <option value=""></option>
      </select>
     </td>
     <td nowrap class="TableData" width="100">户口所在地：</td>
    <td class="TableData" width="180" colspan="3"><input type="text" name="staffDomicilePlace" id="staffDomicilePlace" size="40" class="BigInput"></td>    
  </tr>  
  <tr>
    <td nowrap class="TableData" width="100" style="display:none;">年休假：</td>
    <td class="TableData" style="display:none;"><input type="text" name="leaveType" id="leaveType" size="3" maxlength="3" class="BigInput" value="">天</td>  
    <td nowrap class="TableData" width="100"><span id="showPicOpt"></span></td>
    <td class="TableData"  width="180" colspan="5">
       <input type="file" name="headPic" id="headPic" size="40"  class="BigInput" title="选择附件文件" >
       <br><span id="delPicSpan"></span>

    </td>
  </tr>
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;职位情况及联系方式：</b></td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">工种：</td>
    <td class="TableData"  width="180"><input type="text" name="workType" id="workType" class="BigInput"></td>
    <td nowrap class="TableData" width="100">行政级别：</td>
    <td class="TableData"  width="180"><input type="text" name="administrationLevel" id="administrationLevel" class="BigInput"></td>                 
    <td nowrap class="TableData" width="100">员工类型：</td>
    <td class="TableData">
        <select name="staffOccupation" id="staffOccupation">
          <option value=""></option>  
        </select>     
    </td>     
  </tr>       
  <tr>
    <td nowrap class="TableData" width="100">职务：</td>
    <td class="TableData"  width="180"><input type="text" name="jobPosition" id="jobPosition" class="BigInput"></td>
    <td nowrap class="TableData" width="100">职称：</td>
    <td class="TableData"  width="180">
        <select name="presentPosition" id="presentPosition" >
          <option value=""></option> 
        </select>
    </td>
    <td nowrap class="TableData" width="100">入职时间：</td>
    <td class="TableData"  width="180">
      <input type="text" name="datesEmployed" id="datesEmployed" size="10" maxlength="10" class="BigInput" value=""/>
      <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >   
    </td>                 
  </tr>   
  <tr>
    <td nowrap class="TableData" width="100">本单位工龄：</td>
    <td class="TableData"  width="180"><input type="text" name="jobAge" id="jobAge" class="BigInput"></td>
    <td nowrap class="TableData" width="100">起薪时间：</td>
    <td class="TableData"  width="180">
      <input type="text" name="beginSalsryTime" id="beginSalsryTime" size="10" maxlength="10" class="BigInput" value="" />
      <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
     <td nowrap class="TableData" width="100">在职状态：</td>
    <td class="TableData"  width="180">
      <select name="workStatus" id="workStatus" >
          <option value=""></option>
      </select>     
    </td>          
  </tr> 
  <tr>
    <td nowrap class="TableData" width="100">总工龄：</td>
    <td class="TableData"  width="180"><input type="text" name="workAge" id="workAge" class="BigInput"></td>
    <td nowrap class="TableData" width="100">参加工作时间：</td>
    <td class="TableData"  width="180">
      <input type="text" name="jobBeginning" id="jobBeginning" size="10" maxlength="10" class="BigInput" value="" />
      <img id="date5" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >     
    </td>
    <td nowrap class="TableData" width="100">联系电话：</td>
    <td class="TableData"  width="180"><input type="text" name="staffPhone" id="staffPhone" class="BigInput"></td>          
  </tr>   
  <tr>
    <td nowrap class="TableData" width="100">手机号码：</td>
    <td class="TableData"  width="180"><input type="text" name="staffMobile" id="staffMobile" class="BigInput"></td>      
    <!-- td nowrap class="TableData" width="100">小灵通：</td>
    <td class="TableData"  width="180"></td> -->
    <td nowrap class="TableData" width="100">MSN：</td>
    <td class="TableData"  width="180" colspan="3">
    <input type="hidden" name="staffLittleSmart" id="staffLittleSmart" class="BigInput">
    <input type="text" name="staffMsn" id="staffMsn" class="BigInput" size="50"></td>          
  </tr>   
  
  <tr>
    <td nowrap class="TableData" width="100">电子邮件：</td>
    <td class="TableData"  width="180"><input type="text" name="staffEmail" id="staffEmail" class="BigInput"></td>
    <td nowrap class="TableData" width="100">家庭地址：</td>
    <td class="TableData"  width="180" colspan="3"><input type="text" name="homeAddress" id="homeAddress" size="50" id="HOME_ADDRESS" class="BigInput"></td>                
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">QQ：</td>
    <td class="TableData"  width="180"><input type="text" name="staffQq" id="staffQq" class="BigInput"></td>      
    <td nowrap class="TableData" width="100">其他联系方式：</td>
    <td class="TableData"  width="180" colspan="3"><input type="text" name="otherContact" id="otherContact" size="50"  id="OTHER_CONTACT" class="BigInput"></td>                
  </tr>   
    <tr>
    <td nowrap class="TableData" width="100">社保号：</td>
    <td class="TableData"  width="180" colspan="5"><input type="text" name="insureNum" id="insureNum" class="BigInput"></td>      
  </tr>       
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;教育背景：</b></td>
  </tr>       
  <tr>
    <td nowrap class="TableData" width="100">学历：</td>
    <td class="TableData"  width="180">
        <select name="staffHighestSchool" id="staffHighestSchool" >
          <option value="">请选择</option>
        </select>
    </td>
    <td nowrap class="TableData" width="100">学位：</td>
    <td class="TableData"  width="180">
      <select name="staffHighestDegree" id="staffHighestDegree" >
        <option value="">请选择</option>
      </select>
    </td>     
    <td nowrap class="TableData" width="100">毕业时间：</td>
    <td class="TableData"  width="180">
      <input type="text" name="graduationDate" id="graduationDate" size="10" maxlength="10" class="BigInput" value="" />
      <img id="date6" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td> 
  </tr> 
  <tr>
    <td nowrap class="TableData" width="100">毕业学校：</td>
    <td class="TableData"  width="180"><input type="text" name="graduationSchool" id="graduationSchool" class="BigInput"></td>
    <td nowrap class="TableData" width="100">专业：</td>
    <td class="TableData"  width="180"><input type="text" name="staffMajor" id="staffMajor" class="BigInput"></td>
    <td nowrap class="TableData" width="100">计算机水平：</td>
    <td class="TableData"  width="180"><input type="text" name="computerLevel" id="computerLevel" class="BigInput"></td>                 
  </tr>       
  <tr>
    <td nowrap class="TableData" width="100">外语语种1：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage1" id="foreignLanguage1" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种2：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage2" id="foreignLanguage2" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种3：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage3" id="foreignLanguage3" class="BigInput"></td>                 
  </tr>       
  <tr>
    <td nowrap class="TableData" width="100">外语水平1：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLevel1" id="foreignLevel1" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语水平2：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLevel2" id="foreignLevel2" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语水平3：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLevel3" id="foreignLevel3" class="BigInput"></td>                 
  </tr>  
  <tr>
    <td nowrap class="TableData" width="100">特长：</td>
    <td class="TableData"  width="180" colspan="5"><input type="text" name="staffSkills" id="staffSkills" size="80" id="STAFF_SKILLS" class="BigInput"></td>             
  </tr>         
  <tr>
    <td nowrap class="TableHeader" colspan="3"><b>&nbsp;职务情况：</b></td>
    <td nowrap class="TableHeader" colspan="3"><b>&nbsp;担保记录：</b></td>
  </tr>
  <tr>
    <td class="TableData" colspan="3"><textarea cols="45" name="certificate" id="certificate" rows="3" class="BigInput" wrap="on"></textarea></td>
    <td class="TableData" colspan="3"><textarea cols="45" name="surety" id="surety" rows="3" class="BigInput" wrap="on"></textarea></td>
  </tr>
  <tr>
    <td nowrap class="TableHeader" colspan="3"><b>&nbsp;社保缴纳情况：</b></td>
    <td nowrap class="TableHeader" colspan="3"><b>&nbsp;体检记录：</b></td>
  </tr>
  <tr>
    <td class="TableData" colspan="3"><textarea cols="45" name="insure" id="insure" rows="3" class="BigInput" wrap="on"></textarea></td>
    <td class="TableData" colspan="3"><textarea cols="45" name="bodyExamim" id="bodyExamim" rows="3" class="BigInput" wrap="on"></textarea></td>
  </tr>
  <tr>
    <td class="TableHeader" colspan="6" >
     <?=get_field_table(get_field_html("HR_STAFF_INFO","$USER_ID"))?>
    </td>
  </tr>
  <tr>
    <td nowrap align="left" colspan="6" class="TableHeader">备注：</td>
  </tr> 
  <tr>
    <td nowrap class="TableData" colspan="6"><textarea name="remark" id="remark" cols="95" rows="3" class="BigInput" value=""></textarea></td>               
  </tr>
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;附件文档：</b></td>
  </tr>
  <tr>
    <td class="TableData" colspan="6">
      <input type = "hidden" id="returnAttId" name="returnAttId"></input>
      <input type = "hidden" id="returnAttName" name="returnAttName"></input>
      <span id="attr"></span> 
    </td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">附件选择：</td>
    <td class="TableData" colspan="6">
      <script>ShowAddFile();</script>
      <script></script>
      <script></script> 
      <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
      <%--插入图片 --%>
    </td>
  </tr>     
  <tr>   
    <td nowrap class="TableData" colspan="6">&nbsp;简历：
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
  <tr>
    <td nowrap class="TableData" colspan="6">
    </td>
  </tr>  
  <tr align="center" class="TableControl">
    <td colspan=6 nowrap>
     <input type="hidden" name="DEPT_ID" value="<?=$DEPT_ID?>">
     <input type="hidden" name="yesOrNot" id="yesOrNot" value="">
     <input type="hidden" name="resume" id="resume" value="">
     <input type="hidden" name="operation" id="operation" value="">
     <input type="button" value="保存" class="BigButton" onClick="doSubmit();">
     <span id="staffLeaveSpan" style="display: none">
        <input type="button" value="离职" class="BigButton" onClick="location='<%=contextPath %>/subsys/oa/hr/manage/staffLeave/new.jsp?hrSeqId=<%=seqId %>&treeFlag=<%=treeFlag %>'">
     </span>
    </td>
  </tr>             
</table>
<form>
</body>
</html>
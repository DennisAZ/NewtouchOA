<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.manage.staff_contract.data.*"%> 
<%@ page  import="java.util.List"%>
<html>
<head>
<%
List<YHHrStaffContract> contractList = (List<YHHrStaffContract>)request.getAttribute("contractXXInfoList");
YHHrStaffContract contract = contractList.get(0);
String userName = (String)request.getAttribute("userName");
String contractType = "";//合同类型
String contractState = "";//合同状态
String contractSpecialization = "";//合同属性
String removeOrnot = "";
String passOrnot = "";
if(!YHUtility.isNullorEmpty(contract.getRemoveOrNot())){
	removeOrnot = contract.getRemoveOrNot();
}
if(!YHUtility.isNullorEmpty(contract.getPassOrNot())){
	passOrnot = contract.getPassOrNot();
}
if(!YHUtility.isNullorEmpty(contract.getContractType())){//合同类型
	contractType = contract.getContractType();
}
if(!YHUtility.isNullorEmpty(contract.getStatus())){//合同状态
	contractState = contract.getStatus();
}
if(!YHUtility.isNullorEmpty(contract.getContractSpecialization())){//合同属性
	contractSpecialization = contract.getContractSpecialization();
}
String makeSub="";//合同签订日期
String trailSub="";//试用生效日期
String trailOver="";//试用到期日期
String probation="";  //合同转正日期
String effective=""; //合同生效日期
String contractEt="";//合同到期日期
String contractRt=""; //合同解除日期
String makeContract = String.valueOf(contract.getMakeContract());
if(!YHUtility.isNullorEmpty(makeContract) && makeContract!="null"){
  makeSub =	makeContract.substring(0,10);
}
System.out.print("contract.getTrailEffectiveTime()>>>>>>>>>>>>"+contract.getTrailEffectiveTime());
 String trailEffective = String.valueOf(contract.getTrailEffectiveTime());
 if(!YHUtility.isNullorEmpty(trailEffective) && trailEffective!="null"){
	 trailSub =	trailEffective.substring(0,10);
	}
String trailOverTime =  String.valueOf(contract.getTrailOverTime());
if(!YHUtility.isNullorEmpty(trailOverTime) && trailOverTime!="null"){
	trailOver =	trailOverTime.substring(0,10);
	}
 String probationEnd = String.valueOf(contract.getProbationEndDate()); 
 if(!YHUtility.isNullorEmpty(probationEnd) && probationEnd!="null"){
	 probation =	probationEnd.substring(0,10);
 }
 String probationEffective = String.valueOf(contract.getProbationEffectiveDate());
 if(!YHUtility.isNullorEmpty(probationEffective) && probationEffective!="null"){
	 effective =	probationEffective.substring(0,10);
 }
String contractEndTime = String.valueOf(contract.getContractEndTime());
if(!YHUtility.isNullorEmpty(contractEndTime) && contractEndTime!="null"){
	 contractEt =	contractEndTime.substring(0,10);
}
String contractRemoveTime = String.valueOf(contract.getContractRemoveTime());
if(!YHUtility.isNullorEmpty(contractRemoveTime) && contractRemoveTime!="null"){
	contractRt =	contractRemoveTime.substring(0,10);
}
%>
<title>修改合同信息</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/setting/codeJs/hrCodeJs.js"></script>
<script type="text/Javascript">
var removeOrnot = "<%=removeOrnot%>";
var passOrnot = "<%=passOrnot%>";
var contractType1 = "<%=contractType%>";
var contractState1 ="<%=contractState%>";
var contractSpecialization1 ="<%=contractSpecialization%>";
/*****************附件上传开始*****************************/
var upload_limit=1,limit_type=limitUploadFiles;
var isUploadBackFun = false;
var oa_upload_limit = limitUploadFiles;
var swfupload;
function initSwfUpload() {
  var linkColor = document.linkColor;
  var file_size_limit_seq = 1000;
  var settings = {
    flash_url : "<%=contextPath %>/core/cntrls/swfupload.swf",
    upload_url: "<%=contextPath %>/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/fileLoad.act",
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
}
function uploadSuccessOver(file, serverData){
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if(json.state=="1") {
       progress.setError();
       progress.setStatus("上传失败：" + serverData.substr(5));
       
       var stats=this.getStats();
       stats.successful_uploads--;
       stats.upload_errors++;
       this.setStats(stats);
    } else {
       $('attachmentId').value += json.data.attachmentId;
       $('attachmentName').value += json.data.attachmentName;
       var attachmentIds = $("attachmentId").value;
       var attachmentNames = $("attachmentName").value;
       attachMenuUtil("showAtt","hr",null,attachmentNames,attachmentIds,false);
    }
  } catch (ex) {
    this.debug(ex);
  }
  
}
/*****************附件上传结束*****************************/
function doOnload(){
	//alert("ddd");
	contractType();//查询码表相关信息(合同类型)
	contractState();//合同状态
	  //时间(判断的事合同签订日期和合同生效日期)
	  var parameters = {
	      inputId:'MAKE_CONTRACT',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date1'
	  };
	  new Calendar(parameters);
	  var parameters = {
	      inputId:'TRAIL_EFFECTIVE_TIME',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date2'
	  };
	  new Calendar(parameters);
	  var parameters = {
		      inputId:'TRAIL_OVER_TIME',
		      property:{isHaveTime:false}
		      ,bindToBtn:'date3'
		  };
		  new Calendar(parameters);
		  var parameters = {
			      inputId:'PROBATION_END_DATE',
			      property:{isHaveTime:false}
			      ,bindToBtn:'date4'
			  };
			  new Calendar(parameters);
			  var parameters = {
			      inputId:'PROBATION_EFFECTIVE_DATE',
			      property:{isHaveTime:false}
			      ,bindToBtn:'date5'
			  };
			  new Calendar(parameters);
			  var parameters = {
				      inputId:'CONTRACT_END_TIME',
				      property:{isHaveTime:false}
				      ,bindToBtn:'date6'
				  };
				  new Calendar(parameters);
				  var parameters = {
				      inputId:'CONTRACT_REMOVE_TIME',
				      property:{isHaveTime:false}
				      ,bindToBtn:'date7'
				  };
				  new Calendar(parameters);
				  var parameters = {
					      inputId:'REMIND_TIME',
					      property:{isHaveTime:true}
					      ,bindToBtn:'date8'
					  };
					  new Calendar(parameters);

		if(removeOrnot){ //判断是否解除合同
		  if(removeOrnot == '1'){
			  var whichE4 =document.getElementById("id_msg4");
			  var whichE5 =document.getElementById("id_msg5");
				 document.form1.REMOVE_OR_NOT[0].checked = true;
			   whichE4.style.display = '';
			   whichE5.style.display = '';
			  }		
		}
		if(passOrnot){//判断是否转正
        if(passOrnot =='1'){
        	  whichE1 =document.getElementById("menu");
        	  whichE2 =document.getElementById("id_msg1");
        	  whichE3 =document.getElementById("id_msg2");
        	  document.form1.PASS_OR_NOT[0].checked = true 
        	   whichE1.style.display = '';
        	   whichE2.style.display = '';
        	   whichE3.style.display = '';  
       }
		}
		 showAttachment();
		$("CONTRACT_TYPE").value = contractType1;//给合同类型赋值
		$("STATUS").value = contractState1;//给合同状态赋值
		$("CONTRACT_SPECIALIZATION").value =contractSpecialization1 //给合同属性赋值

	//	var contractType = $("CONTRACT_TYPE").value;
	//	$("contractValue").value = contractType;
		
	//	var contractState1 = $("STATUS").value;
	//	$("contractState").value = contractState1;
		
	//	var contractSX1 = $("CONTRACT_SPECIALIZATION").value;
	//	$("contractSX").value = contractSX1;
		
		
}

function contractType(){
	var codeObject = getChildCode("HR_STAFF_CONTRACT1");
	var selectObj = $("CONTRACT_TYPE");
  for(var i=0; i<codeObject.length; i++){
    var code = codeObject[i];
    var codeId = code.seqId;
    //alert(codeId);
    var codeNo = code.codeNo;
    var codeDesc = code.codeName;
    var myOption = document.createElement("option");
    
    myOption.value = codeId; //codeId是编号
   
    myOption.text = codeDesc;
   
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
    //selectObj.options.add(new Option(codeObject[i], codeObject[i]));
    //$("CONTRACT_TYPE").value = myOption.text;
	}
	
}

function contractState(){ 
  var codeObject = getChildCode("HR_STAFF_CONTRACT2");
	var selectObj = $("STATUS");
	
	for(var i=0; i<codeObject.length; i++){
    var code = codeObject[i];
    var codeId = code.seqId;
    var codeNo = code.codeNo;
    var codeDesc = code.codeName;
    
    var myOption = document.createElement("option");
    myOption.value = codeId;
    myOption.text = codeDesc;
  
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
	}
}
//判断合同是否转正
 function expandIt(){
	  whichE1 =document.getElementById("menu");
	  whichE2 =document.getElementById("id_msg1");
	  whichE3 =document.getElementById("id_msg2");
	  if (document.form1.PASS_OR_NOT[0].checked == true) 
	  {
	   whichE1.style.display = '';
	   whichE2.style.display = '';
	   whichE3.style.display = '';  
	  }
	  if (document.form1.PASS_OR_NOT[1].checked == true)
	  { 
	  	whichE1.style.display = 'none';
	    whichE2.style.display = 'none';  
	    whichE3.style.display = 'none'; 
	  }
 }
//判断合同是否解除
function expandIt2(){
	  whichE4 =document.getElementById("id_msg4");
	  whichE5 =document.getElementById("id_msg5");
	  if (document.form1.REMOVE_OR_NOT[0].checked == true) 
	  {
	   whichE4.style.display = '';
	   whichE5.style.display = '';
	  }
	  if (document.form1.REMOVE_OR_NOT[1].checked == true)
	  { 
	  	whichE4.style.display = 'none';
	  	whichE5.style.display = 'none';
	  }
}
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function CheckForm(){
	var seqId =  $("seqid").value;
	var userName =  $("userName").value;
	var contractNo = $("STAFF_CONTRACT_NO").value;
	var contractType = $("CONTRACT_TYPE").value;
	var CONTRACT_SPECIALIZATION =  $("CONTRACT_SPECIALIZATION").value;
	var STATUS = $("STATUS").value;
	var makeContract = $("MAKE_CONTRACT").value;
	var TRAIL_EFFECTIVE_TIME = $("TRAIL_EFFECTIVE_TIME").value;

	var PROBATIONARY_PERIOD = $("PROBATIONARY_PERIOD").value;
	var TRAIL_OVER_TIME = $("TRAIL_OVER_TIME").value;
	var PROBATION_END_DATE = $("PROBATION_END_DATE").value;
	var PROBATION_EFFECTIVE_DATE = $("PROBATION_EFFECTIVE_DATE").value;
	var CONTRACT_END_TIME = $("CONTRACT_END_TIME").value;
	var CONTRACT_REMOVE_TIME = $("CONTRACT_REMOVE_TIME").value;

	var CONTRACT_END_TIME = $("CONTRACT_END_TIME").value;
	var CONTRACT_REMOVE_TIME = $("CONTRACT_REMOVE_TIME").value;
	if(userName.replaceAll(" ","") == "" || userName == "null"){
	      alert("单位员工不能为空");
	      return false;
  }
	if(contractNo.replaceAll(" ","") == "" || contractNo == "null"){
	      alert("合同编号不能为空");
	      return false;
  }  
	if(contractType.replaceAll(" ","") == "" || contractType == "null"){
	      alert("合同类型不能为空"); 
	      return false;
  }
	if(makeContract.replaceAll(" ","") == "" || makeContract == "null"){
	      alert("合同签订日期不能为空");
	      return false;
  }
	if(document.form1.TRAIL_EFFECTIVE_TIME.value!="" && document.form1.MAKE_CONTRACT.value > document.form1.TRAIL_EFFECTIVE_TIME.value)
	   { 
	      alert("试用生效日期不能小于合同签订日期！");
	      return (false);
	   }
	if(document.form1.TRAIL_EFFECTIVE_TIME.value!="" && document.form1.TRAIL_OVER_TIME.value!="" && document.form1.TRAIL_EFFECTIVE_TIME.value > document.form1.TRAIL_OVER_TIME.value)
	   { 
	      alert("试用到期日期不能小于试用生效日期！");
	      return (false);
	   }
	if($('PASS_OR_NOT1').checked && document.form1.PROBATION_END_DATE.value!="" && document.form1.TRAIL_OVER_TIME.value!="" && document.form1.TRAIL_OVER_TIME.value > document.form1.PROBATION_END_DATE.value)
	   { 
	      alert("合同转正日期不能小于试用到期日期！");
	      return (false);
	   } 
	 if($('PASS_OR_NOT1').checked && document.form1.PROBATION_END_DATE.value!="" && document.form1.PROBATION_EFFECTIVE_DATE.value!="" && document.form1.PROBATION_END_DATE.value > document.form1.PROBATION_EFFECTIVE_DATE.value)
	   { 
	      alert("合同生效日期不能小于合同转正日期！");
	      return (false);
	   }
	   if($('PASS_OR_NOT1').checked && document.form1.CONTRACT_END_TIME.value!="" && document.form1.PROBATION_EFFECTIVE_DATE.value!="" && document.form1.PROBATION_EFFECTIVE_DATE.value > document.form1.CONTRACT_END_TIME.value)
	   { 
	      alert("合同到期日期不能小于合同生效日期！");
	      return (false);
	   }
	   if($('REMOVE_OR_NOT1').checked && document.form1.CONTRACT_END_TIME.value!="" && document.form1.CONTRACT_REMOVE_TIME.value!="" && document.form1.CONTRACT_END_TIME.value > document.form1.CONTRACT_REMOVE_TIME.value)
	   { 
	      alert("合同解除日期不能小于合同到期日期！");
	      return (false);
	   }
	   document.form1.action = contextPath +"/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/updateContractInfo.act?seqid="+seqId;
	    document.form1.submit();
	   
		return true;
}
function getvalue(){
	var contractType = $("CONTRACT_TYPE").value;
	//alert(contractType);
	$("contractValue").value = contractType;
}
function getState(){
	var contractState1 = $("STATUS").value;
	$("contractState").value = contractState1;
}
function getSx(){
	var contractSX1 = $("CONTRACT_SPECIALIZATION").value;
	$("contractSX").value = contractSX1;
}
function gotos(){
	window.location.href= contextPath + "/subsys/oa/hr/manage/staff_contract/query.jsp";
}

function showAttachment(){
	var attachmentId = "<%=YHUtility.null2Empty(contract.getAttachmentId())%>";
	var attachmentName = "<%= YHUtility.encodeSpecial(YHUtility.null2Empty(contract.getAttachmentName()))%>";
	
	if(attachmentId){
		$("returnAttId").value = attachmentId;
		$("returnAttName").value = attachmentName;
		var selfdefMenu = {
		          office:["downFile","dump","read","edit","deleteFile"], 
		          img:["downFile","dump","play","deleteFile"],  
		          music:["downFile","play","deleteFile"],  
		          video:["downFile","play","deleteFile"], 
		          others:["downFile","deleteFile"]
 	 }
  	attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=contract.getSeqId()%>',selfdefMenu);
	}
}

//删除附件
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/delFloatFile.act?delAttachId=" + attachId + "&seqId=<%=contract.getSeqId()%>";
  //var json = getJsonRs(url);
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
<body class="bodycolor" topmargin="5" onLoad="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建合同信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<!--  
<form action="#" id="form1" method="post" name="form1">
-->
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/addStaffincentiveInfo.act"  method="post" name="form1" id="form1" onsubmit="">
<table class="TableBlock" width="80%" align="center">
   <tr>
      <td nowrap class="TableData">单位员工：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" name="userName" id="userName" size="12" class="BigStatic" readonly value="<%=userName==null?"":userName %>">&nbsp;
        <INPUT type="hidden" name="userId" id="userId" value="<%=contract.getStaffName()==null?"":contract.getStaffName() %>">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName'],null,null,1)">选择</a>  
        <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
      </td>
      <td nowrap class="TableData">合同编号：<font style="color:red">*</font></td>
      <td class="TableData" >
        <INPUT type="text"name="STAFF_CONTRACT_NO" id="STAFF_CONTRACT_NO" class=BigInput size="11" value="<%=contract.getStaffContractNo() %>">
     <input type="hidden" name="seqid" id = "seqid" value="<%=contract.getSeqId() %>"></input>
      </td> 
    </tr>
    <tr>
    	 <td nowrap class="TableData">合同类型：<font style="color:red">*</font></td>
      <td class="TableData">
        <select name="CONTRACT_TYPE" id="CONTRACT_TYPE" onchange="getvalue();" title="合同类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">请选择合同类型</option>
          
        </select>
        <input type="hidden" name="contractValue" id="contractValue" ></input>
      </td>
    	<td nowrap class="TableData">合同属性：</td>
      <td class="TableData">
        <select name="CONTRACT_SPECIALIZATION" id="CONTRACT_SPECIALIZATION" onchange="getSx();">
          <option value="有固定期限">有固定期限</option>
          <option value="无固定期限">无固定期限</option>
        </select>
        <input type="hidden" name="contractSX" id="contractSX" ></input>
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">合同状态：</td>
      <td class="TableData" colspan="3">
        <select name="STATUS" id="STATUS" onchange="getState();" title="合同状态可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">请选择合同状态</option>
        </select>
        <input type="hidden" name="contractState" id="contractState" ></input>
      </td>
    </tr>        
    <tr >
    	<td nowrap class="TableData">合同签订日期：<font style="color:red">*</font></td>
      <td class="TableData">
       <input type="text" name="MAKE_CONTRACT" id="MAKE_CONTRACT" size="12" maxlength="10" class="BigInput" readonly value="<%=makeSub==null?"":makeSub %>"> <img src="<%=imgPath%>/calendar.gif" id="date1" name="date1" align="absMiddle" border="0" style="cursor:pointer">
      </td>
      <td nowrap class="TableData">试用生效日期：</td>
      <td class="TableData">
        <input type="text" name="TRAIL_EFFECTIVE_TIME" id ="TRAIL_EFFECTIVE_TIME" size="12" maxlength="10" class="BigInput" readonly value="<%=trailSub==null?"":trailSub %>" > <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:pointer">
      </td>        
    </tr>
    <tr >
			<td nowrap class="TableData">试用天数：</td>
      <td class="TableData">
          <INPUT type="text"name="PROBATIONARY_PERIOD" id="PROBATIONARY_PERIOD" class="BigInput" size="12" value="<%=contract.getProbationaryPeriod()==null?"":contract.getProbationaryPeriod() %>">
      </td>      	
			<td nowrap class="TableData">试用到期日期：</td>
      <td class="TableData">     
        <input type="text" name="TRAIL_OVER_TIME" id="TRAIL_OVER_TIME" size="12" maxlength="10" class="BigInput" readonly value="<%=trailOver==null?"":trailOver %>"/>
     <img src="<%=imgPath%>/calendar.gif" id="date3" name="date3" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr >
      <td nowrap class="TableData">合同是否转正：</td>
      <td class="TableData">
        <INPUT type="radio" name="PASS_OR_NOT" id="PASS_OR_NOT1" value="1" onclick="expandIt()"> 是&nbsp;&nbsp; 
			  <INPUT type="radio" name="PASS_OR_NOT" id="PASS_OR_NOT2" value="0" onclick="expandIt()" checked> 否 
			</td>
    	<td nowrap class="TableData">
    	   <span id="id_msg1" style="display:none">合同转正日期：</span> </td>
      <td class="TableData">
      	<span id="id_msg2" style="display:none">
         <input type="text" name="PROBATION_END_DATE" id="PROBATION_END_DATE" size="12" maxlength="10" class="BigInput" readonly value="<%=probation==null?"":probation %>"/>
              <img src="<%=imgPath%>/calendar.gif" id="date4" name="date4" align="absMiddle" border="0" style="cursor:pointer">
       </span> 
      </td>
    </tr>
    <tr style="display:none" id="menu">    
      <td nowrap class="TableData">合同生效日期：</td>
      <td class="TableData">
        <input type="text" name="PROBATION_EFFECTIVE_DATE" id="PROBATION_EFFECTIVE_DATE"  size="12" maxlength="10" class="BigInput" readonly value="<%=effective==null?"":effective %>"/>
             <img src="<%=imgPath%>/calendar.gif" id="date5" name="date5" align="absMiddle" border="0" style="cursor:pointer">
      </td>
			<td nowrap class="TableData">合同到期日期：</td>
      <td class="TableData">   
        <input type="text" name="CONTRACT_END_TIME" id="CONTRACT_END_TIME" size="12" maxlength="10" class="BigInput" readonly value="<%=contractEt==null?"":contractEt %>"/>
             <img src="<%=imgPath%>/calendar.gif" id="date6" name="date6" align="absMiddle" border="0" style="cursor:pointer">
      </td>      
    </tr>
    <tr >
      <td nowrap class="TableData">合同是否解除：</td>
      <td class="TableData">
        <INPUT type="radio" name="REMOVE_OR_NOT" id="REMOVE_OR_NOT1" value="1" onclick="expandIt2()"> 是&nbsp;&nbsp;  
			  <INPUT type="radio" name="REMOVE_OR_NOT" id="REMOVE_OR_NOT2" value="0" onclick="expandIt2()" checked> 否 
			</td>
			<td nowrap class="TableData">
			  <span id="id_msg4" style="display:none">合同解除日期：</span>
			</td>
      <td class="TableData">
	      <span id="id_msg5" style="display:none">
	        <input type="text" name="CONTRACT_REMOVE_TIME" id="CONTRACT_REMOVE_TIME" size="12" maxlength="10" class="BigInput" readonly value="<%=contractRt==null?"":contractRt %>"/>
	                     <img src="<%=imgPath%>/calendar.gif" id="date7" name="date7" align="absMiddle" border="0" style="cursor:pointer">
	      </span>
      </td>
    </tr>    
     <tr >
    	<td nowrap class="TableData">签约次数：</td>
      <td class="TableData">
        <input type="text"name="SIGN_TIMES" id="SIGN_TIMES" class="BigInput" size="12" value="<%=contract.getSignTimes()==null?"":contract.getSignTimes() %>">&nbsp;次
       <%System.out.print(contract.getSignTimes()); %>
      </td>     	

      <td nowrap class="TableData"> 提醒时间：</td>
      <td class="TableData">
        <input type="text" name="REMIND_TIME" id="REMIND_TIME" size="20" maxlength="20" class="BigInput" readonly value=""/>
                	                     <img src="<%=imgPath%>/calendar.gif" id="date8" name="date8" align="absMiddle" border="0" style="cursor:pointer">
        &nbsp;为空则不提醒
        
      </td>
    </tr>
    </tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="REMARK" id="REMARK" cols="70" rows="3" class="BigInput"><%=contract.getRemark()==null?"":contract.getRemark() %></textarea>
      </td>
    </tr> 
    
  <tr id="attr_tr">
      <td noWrap="nowrap" class="TableData">附件文档: </td>
      <td class="TableData" noWrap="nowrap" colspan="3">
       	<input type = "hidden" id="returnAttId" name="returnAttId"></input>
				<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
      </td>
    </tr>
    <tr id="fileShowId">
      <td nowrap class="TableContent">附件上传：</td>
      <td class="TableData" id="fsUploadRow" colspan="3">
          <script>ShowAddFile();</script>
	    <script></script>
	    <script></script> 
			<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
			<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			<%--插入图片 --%>
			<input type="hidden" id="moduel" name="moduel" value="">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
			<input type="hidden" id="imgattachmentName" name="imgattachmentName">
      &nbsp;</td>
    </tr>
 
   <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData" colspan=3>
      <input id="SMS_REMIND"  type="checkbox" name="SMS_REMIND" value="1"><label for="SMS_REMIND">使用内部短信提醒</label>&nbsp;&nbsp;
      </td>
   </tr>

    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" onclick="javascript:CheckForm(); return false;" value="保存" name="button"  class="BigButton">
     <input type="button" value="返回" class="BigButton" onClick="gotos();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
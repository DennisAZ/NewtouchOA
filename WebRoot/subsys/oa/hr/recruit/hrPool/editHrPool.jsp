<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.subsys.oa.hr.recruit.hrPool.act.YHHrRecruitPoolAct"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑人才档案</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
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
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/hrPool/js/hrPoolLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
function doInit(){
//初始化日期
	setDate();
//籍贯	
	getSelectedCode("AREA","employeeNativePlace");
//政治面貌
	getSelectedCode("STAFF_POLITICAL_STATUS","employeePoliticalStatus");
//期望工作性质
	getSelectedCode("JOB_CATEGORY","jobCategory");
//应聘岗位
	getSelectedCode("POOL_POSITION","position");
//所学专业
	getSelectedCode("POOL_EMPLOYEE_MAJOR","employeeMajor");
//学历
	getSelectedCode("STAFF_HIGHEST_SCHOOL","employeeHighestSchool");
//学位
	getSelectedCode("EMPLOYEE_HIGHEST_DEGREE","employeeHighestDegree");
	getHrPoolInfo();
	
}

function getHrPoolInfo(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/hrPool/act/YHHrRecruitPoolAct/getHrPoolnDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	//alert(rsText);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		if(data.planName){
			$("PLAN_NAME").value = data.planName;
		}
		if(data.planNo){
			$("PLAN_NO").value = data.planNo;
		}
		if(data.employeeName){
			$("EMPLOYEE_NAME").value = data.employeeName;
		}
		if(data.employeeBirth){
			$("employeeBirth").value = data.employeeBirth.substr(0,10);
		}
		if(data.jobBeginning){
			$("jobBeginning").value = data.jobBeginning.substr(0,10);
		}
		if(data.graduationDate){
			$("graduationDate").value = data.graduationDate.substr(0,10);
		}
		if(data.resume){
			fckContentStr = data.resume; 
		}
		//alert(data.residencePlace);
		if(data.photoName){
			var photoName = data.photoName;
			var filePathStr = "<%=YHSysProps.getAttachPath().replace('\\','/') + "/" + YHHrRecruitPoolAct.headPicFolder%>" ;
			var imgStr = "<img src=<%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePathStr) + "/" +  encodeURIComponent(photoName) + " border='0'  width='150'  ></img>";
			$("showImageSpan").innerHTML = imgStr;
			$("showPicOpt").innerHTML = "照片更改：";
			var poolSeqId = '<%=seqId%>';
			var delPicStr = "<a href=javascript:deletePhoto('" + poolSeqId +"')> 删除照片</a>";
			$("delPicSpan").innerHTML = delPicStr;
		}else{
			$("showImageSpan").innerHTML = "暂无照片";
			$("showPicOpt").innerHTML = "照片上传：";
		}
		
		if(data.attachmentId){
			$("returnAttId").value = data.attachmentId;
			$("returnAttName").value = data.attachmentName;
			var selfdefMenu = {
          office:["downFile","dump","read","edit","deleteFile"], 
          img:["downFile","dump","play","deleteFile","insertImg"],  
          music:["downFile","play"],  
          video:["downFile","play"], 
          others:["downFile","dump","deleteFile"]
      }
      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
		}else{
			$('attr').innerHTML = "无附件";
		}

	}else{
		alert(rtJson.rtMsrg);
	}
}
function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}
function deletePhoto(seqId){
	var msg="确定要删除上传的照片吗?";
	if(window.confirm(msg)){
		var requestURLStr = contextPath+ "/yh/subsys/oa/hr/recruit/hrPool/act/YHHrRecruitPoolAct";
		var url = requestURLStr + "/deletePhoto.act";
		var rtJson = getJsonRs(url, "seqId=" + seqId);
		if (rtJson.rtState == "0") {
			window.location.reload();
		} else {
			alert(rtJson.rtMsrg);
		}
	}
}
//插入图片
function InsertImage(src){
	var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
	if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )  	{
		oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
	}
}
//删除附件(浮动菜单)
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
	var requestURL = "<%=contextPath%>/yh/subsys/oa/hr/recruit/hrPool/act/YHHrRecruitPoolAct";
	var url = requestURL + "/delFloatFile.act?delAttachId=" + attachId;
//var json = getJsonRs(url);
	var json=getJsonRs(url,"&seqId=<%=seqId%>");
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
		if(updateFlag == "1"){
		  return true;
		  //window.location.reload();
		}else{
			return false;
		}
	}
}

//日期
function setDate(){
	var date1Parameters = {
		inputId:'employeeBirth',
		property:{isHaveTime:false},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
		inputId:'jobBeginning',
		property:{isHaveTime:false},
		bindToBtn:'date2'
	};
	new Calendar(date2Parameters);
	
	var date3Parameters = {
		inputId:'graduationDate',
		property:{isHaveTime:false},
		bindToBtn:'date3'
	};
	new Calendar(date3Parameters);
}
function clearValue(str){
	if(str){
		str.value = "";
	}
}
function LoadWindow2(){
	var url= contextPath + "/subsys/oa/hr/recruit/hrPool/plannoinfo/index.jsp";
	loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
	loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
	window.showModalDialog(url,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}
function checkForm(){
	if(document.form1.PLAN_NAME.value.trim() == ""){
		alert("计划名称不能为空");
		$('PLAN_NAME').focus();
		$('PLAN_NAME').select();
		return false;
	}
	if(document.form1.EMPLOYEE_NAME.value.trim() == ""){
		alert("应聘人姓名不能为空");
		$('EMPLOYEE_NAME').focus();
		$('EMPLOYEE_NAME').select();
		return false;
	}
	if(document.form1.employeeSex.value.trim() == ""){
		alert("应聘人性别不能为空");
		$('employeeSex').focus();
		$('employeeSex').select();
		return false;
	}
	if(document.form1.employeeBirth.value.trim() == ""){
		alert("应聘人出生日期不能为空");
		$('employeeBirth').focus();
		$('employeeBirth').select();
		return false;
	}
	if(document.form1.employeePhone.value.trim() == ""){
		alert("应聘人联系电话不能为空");
		$('employeePhone').focus();
		$('employeePhone').select();
		return false;
	}
		if(document.form1.employeeEmail.value.trim() == ""){
		alert("应聘人E_mail不能为空");
		$('employeeEmail').focus();
		$('employeeEmail').select();
		return false;
	}
	if(document.form1.jobCategory.value.trim() == ""){
		alert("期望工作性质不能为空");
		$('jobCategory').focus();
		$('jobCategory').select();
		return false;
	}
	if(document.form1.expectedSalary.value.trim() == ""){
		alert("期望薪水不能为空");
		$('expectedSalary').focus();
		$('expectedSalary').select();
		return false;
	}
	if(!isNumbers($('expectedSalary').value)){
		alert("期望薪水格式错误,应形如 10000.00"); 
		$('expectedSalary').focus();
		$('expectedSalary').select();
		return false;
	}
	if(document.form1.employeeHighestSchool.value.trim() == ""){
		alert("学历不能为空");
		$('employeeHighestSchool').focus();
		$('employeeHighestSchool').select();
		return false;
	}
	if(document.form1.employeeMajor.value.trim() == ""){
		alert("专业不能为空");
		$('employeeMajor').focus();
		$('employeeMajor').select();
		return false;
	}
	if(document.form1.position.value.trim() == ""){
		alert("应聘岗位不能为空");
		$('position').focus();
		$('position').select();
		return false;
	}
	return true;
}
function doSubmit(){
	if(checkForm()){
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		//alert(oEditor.GetXHTML());
		$("resume").value = oEditor.GetXHTML();
		$("form1").submit();
	}
}

</script>

</head>
<body onload="doInit();">
<table border="0" width="770" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑人才档案 </span>&nbsp;&nbsp;</td>
  </tr>
</table>
<form enctype="multipart/form-data" action="<%=contextPath %>/yh/subsys/oa/hr/recruit/hrPool/act/YHHrRecruitPoolAct/updateHrPoolInfo.act?seqId=<%=seqId %>" method="post" name="form1" id="form1" >
<table class="TableBlock" width="770" align="center">
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;基本信息：</b></td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">计划名称：<font color="red">*</font></td>
    <td class="TableData" width="180">
      <INPUT type="text"name="PLAN_NAME" id="PLAN_NAME" readonly="readonly" class=BigInput size="12">
      <INPUT type="hidden" name="PLAN_NO" id="PLAN_NO" value="">
      <a href="javascript:;" class="orgAdd" onClick="LoadWindow2()">选择</a>	
    </td>
    <td nowrap class="TableData" width="100">应聘人姓名：<font color="red">*</font></td>
    <td class="TableData" width="180" colspan="2">
    	<input type="text" name="EMPLOYEE_NAME" id="EMPLOYEE_NAME" class="BigInput"  value="">
    </td>
    <td class="TableData" rowspan="7" colspan="1" align="center"><span id="showImageSpan"></span>
   </td> 
  </tr>
  <tr>
   <td nowrap class="TableData">性别：<font color="red">*</font></td>
   <td class="TableData">
    <select name="employeeSex" id="employeeSex" >
         <option value="0">男</option>
         <option value="1">女</option>
     </select>
   </td>
   <td nowrap class="TableData" width="100">出生日期：<font color="red">*</font></td>
    <td class="TableData" width="180" colspan="2">
      <input type="text" name="employeeBirth" id="employeeBirth" size="12" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">民族：</td>
    <td class="TableData"  width="180">
      <input type="text" name="employeeNationality" id="employeeNationality" class="BigInput" value="">
    </td> 	
    <td nowrap class="TableData" width="100">现居住城市：</td>
    <td class="TableData" width="180" colspan="2">
    	<input type="text" name="residencePlace" id="residencePlace" class="BigInput" value="">
    </td>           
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">联系电话：<font color="red">*</font></td>
    <td class="TableData"  width="180">
    	<input type="text" name="employeePhone" id="employeePhone" class="BigInput">
    </td>
    <td nowrap class="TableData" >E_mail：<font color="red">*</font></td>
     <td class="TableData"  width="180" colspan="2">
     	<input type="text" name="employeeEmail" id="employeeEmail" class="BigInput">
     </td>
  </tr>
  <tr>
    <td nowrap class="TableData">籍贯：</td>
    <td class="TableData">
    	<select name="employeeNativePlace" id="employeeNativePlace" >
      </select>
    </td>
     <td nowrap class="TableData" width="100">户口所在地：</td>
    <td class="TableData"  width="180" colspan="2">
    	<input type="text" name="employeeDomicilePlace" id="employeeDomicilePlace" size="35" class="BigInput">
    </td>             
  </tr>
  <tr>
    <td nowrap class="TableData">婚姻状况：</td>
    <td class="TableData">
      <select name="employeeMaritalStatus" id="employeeMaritalStatus" >
        <option value="" ></option>
        <option value="0" >未婚&nbsp;&nbsp;</option>
        <option value="1" >已婚</option>
        <option value="2" >离异</option>
      </select>    	
    </td>
    <td nowrap class="TableData">政治面貌：</td>
    <td class="TableData" colspan="2">
        <select name="employeePoliticalStatus" id="employeePoliticalStatus" >
          <option value="">政治面貌</option>
        </select>
    </td>    	
  </tr>
  <tr>
  	<td nowrap class="TableData" width="100">健康状况：</td>
    <td class="TableData"  width="180">
    	<input type="text" name="employeeHealth" id="employeeHealth" class="BigInput" value=>
    </td>
     <td nowrap class="TableData" width="100">参加工作时间：</td>
    <td class="TableData"  width="180" colspan="2">
      <input type="text" name="jobBeginning" id="jobBeginning" size="12" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);"  />
      <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
  </tr>   
 <tr>
  <td nowrap class="TableData">期望工作性质：<font color="red">*</font></td>
    <td class="TableData">
    	<select name="jobCategory" id="jobCategory" >
       </select>
    </td> 
    <td nowrap class="TableData" width="100"><span id="showPicOpt"></span></td>
    <td class="TableData"  width="180" colspan="3">
       <input type="file" name="headPic" id="headPic" size="40"  class="BigInput" title="选择附件文件" >
       <br><span id="delPicSpan"></span>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">期望从事行业：</td>
    <td class="TableData">
    	<input type="text" name="jobIndustry" id="jobIndustry" class="BigInput">
    </td> 
    <td nowrap class="TableData">期望从事职业：</td>
    <td class="TableData">
    	<input type="text" name=jobIntension id="jobIntension" class="BigInput">
    </td>
    <td nowrap class="TableData">期望工作城市：</td>
    <td class="TableData">
    	<input type="text" name="workCity" id="workCity" class="BigInput">
    </td>                  
  </tr>   
    <tr>
    <td nowrap class="TableData" width="110">期望薪水(税前)：<font color="red">*</font></td>
    <td class="TableData"  width="180">
    	<input type="text" name="expectedSalary" id="expectedSalary" size="10" maxlength="10" class="BigInput">&nbsp;元
    </td>
    <td nowrap class="TableData" width="100">应聘岗位：<font color="red">*</font></td>
    <td class="TableData">
     <select name="position" id="position" >
			<option value="">应聘岗位</option>
     </select>
    </td> 
    <td nowrap class="TableData" width="100">到岗时间：</td>
    <td class="TableData"  width="180">
      <select name="startWorking" id="startWorking" >
        <option value="" ></option>
        <option value="0" >1周以内</option>
        <option value="1" >1个月内</option>
        <option value="2" >1~3个月</option>
        <option value="3" >3个月后</option>
        <option value="4" >随时到岗</option>
      </select>     
    </td>              
  </tr> 
  <tr>
    <td nowrap class="TableData" width="100">毕业时间：</td>
    <td class="TableData"  width="180">
      <input type="text" name="graduationDate" id="graduationDate" size="10" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);"  />
      <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
    <td nowrap class="TableData" width="100">毕业学校：</td>
    <td class="TableData"  width="180" colspan="3">
    	<input type="text" name="graduationSchool" size="60" id="graduationSchool" class="BigInput">
    </td>                
  </tr> 
    <tr>
    <td nowrap class="TableData" width="100">所学专业：<font color="red">*</font></td>
    <td class="TableData"  width="180">
    	<select name="employeeMajor" id="employeeMajor">
			<option value="">所学专业</option>
     </select>
    </td>   
    <td nowrap class="TableData" width="100">学历：<font color="red">*</font></td>
    <td class="TableData"  width="180">
        <select name="employeeHighestSchool" id="employeeHighestSchool" >
        	<option value="">学历</option>
        </select>
    </td>
    <td nowrap class="TableData" width="100">学位：</td>
    <td class="TableData"  width="180">
    	<select name="employeeHighestDegree" id="employeeHighestDegree" >
    		<option value="">学位</option>
      </select>
    </td>              
  </tr>   
  <tr>
    <td nowrap class="TableData" width="100">外语语种1：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage1" id="foreignLanguage1" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种2：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage2" id="foreignLanguage2" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种3：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage3" id=""foreignLanguage3"" class="BigInput"></td>                 
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
    <td nowrap class="TableData" width="100">计算机水平：</td>
    <td class="TableData"  width="180">
    	<input type="text" name="computerLevel" id="computerLevel" class="BigInput">
    </td>
    <td nowrap class="TableData">年龄：</td>
    <td class="TableData" width="180" colspan="3">
    	<input type="text" name="employeeAge" id="employeeAge" size="8" maxlength="3" class="BigInput">&nbsp;岁
    </td>          
  </tr>  
  <tr>
    <td nowrap class="TableData" width="100">特长：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="employeeSkills" id="employeeSkills" cols="100" rows="3" class="BigInput" value=""></textarea>             
   </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">职业技能：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="careerSkills" id="careerSkills" cols="100" rows="3" class="BigInput" value=""></textarea>
    </td>            
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">工作经验：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="workExperience" id="workExperience" cols="100" rows="3" class="BigInput" value=""></textarea>            
   </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">项目经验：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="projectExperience" id="projectExperience" cols="100" rows="3" class="BigInput" value=""></textarea>             
   </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">备注：</td>
    <td class="TableData"  width="180" colspan="5"><textarea name="remark" id="remark" cols="100" rows="3" class="BigInput" value=""></textarea>             
    </td>
  </tr>
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;附件简历：</b></td>
  </tr>
  <tr style="">
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
			<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			<%--插入图片 --%>
    </td>
  </tr>     
  <tr>
    <td nowrap class="TableData" colspan="6">简历：</td> 
  </tr>
    <tr>
    <td nowrap class="TableData" colspan="6">
			<div>
		     <script language=JavaScript>    
		      var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
		      var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
		      oFCKeditor.BasePath = sBasePath ;
		      oFCKeditor.Height = 400;
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
  <tr align="center" class="TableControl">
    <td colspan=6 nowrap>
			<input type="hidden" name="toId" id="toId" value="">
			<input type="hidden" name="resume" id="resume" value="">
			<input type="button" value="保存" onclick="doSubmit();" class="BigButton" >&nbsp;&nbsp;
			<input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath %>/subsys/oa/hr/recruit/hrPool/hrPoolManage.jsp'">
    </td>
  </tr>             
</table>
<form>



</body>
</html>
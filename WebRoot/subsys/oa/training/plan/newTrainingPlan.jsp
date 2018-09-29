<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建培训计划</title>
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/trainingPlanllogic.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/training/act/YHHrTrainingPlanAct";
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;


function doInit(){
	$("tPlanNo").focus();
  deptFunc();
  getSecretFlag("T_COURSE_TYPE","tCourseTypes");
  setDate();
  getSysRemind("smsRemindDiv","smsRemind");
  moblieSmsRemind("sms2RemindDiv","sms2Remind");
}
//获取全体部门列表
function deptFunc(){
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("sponsoringDepartment");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}
//日期
function setDate(){
var date1Parameters = {
   inputId:'courseStartDate',
   property:{isHaveTime:false}
   ,bindToBtn:'date1'
};
new Calendar(date1Parameters);
var date2Parameters = {
   inputId:'courseEndDate',
   property:{isHaveTime:false}
   ,bindToBtn:'date2'
};
new Calendar(date2Parameters);
}

function checkForm(){
	if($("tPlanNo").value == ""){
		alert("培训计划编号不能为空");
		$("tPlanNo").focus();
    return false;
	}
	if($("tPlanName").value == ""){
		alert("培训计划名称不能为空");
		$("tPlanName").focus();
    return false;
	}
	if($("chargePerson").value == ""){
		alert("负责人不能为空");
		$("chargePersonDesc").focus();
    return false;
	}
	if(!checkNumber()){
		return false;
	}
	if(!checkDate("courseStartDate","courseEndDate")){
		return false;
	}
	return true;
}

function checkDate(startDateStr,endDateStr){
	var startDate = $(startDateStr);
	var endDate = $(endDateStr);
	if(startDate.value && endDate.value){
    if($(startDate).value > $(endDate).value){
      alert("开课日期不能 大于 结课日期 ！");
      startDate.focus(); 
      startDate.select(); 
      return false;
    }
  }
  if(startDate.value){
    if(!isValidDateStr(startDate.value)){
      alert("开课日期格式不对，应形如  2010-01-02");
      startDate.focus();
      startDate.select();
      return false;
    }
  }
  if(endDate.value){
    if(!isValidDateStr(endDate.value)){
      alert("结课日期格式不对，应形如  2010-01-02");
      endDate.focus();
      endDate.select();
      return false;
    }
  }
  return true;
}
function checkNumber(){
	var reg = /^[0-9]*$/;
	if($("tJoinNum").value){
 	 if(!reg.test($("tJoinNum").value)){
 			alert("计划参与培训人数应为整数！");
 			$("tJoinNum").focus();
 			$('tJoinNum').select();		
 			return false;
 		}
  }
  if($("courseHours").value){
 	 if(!reg.test($("courseHours").value)){
 			alert("总课时应为整数！");
 			$("courseHours").focus();
 			$('courseHours').select();		
 			return false;
 		}
  }
  if($("tBcws").value){
 	 if(!isNumbers($("tBcws").value)){
 		 alert("培训预算格式错误,应形如  10000.00");
 		 $("tBcws").focus();
		 $('tBcws').select();		
 		 return false;
 	 }
 }
	return true;
}

function doSubmit(){
	if(checkForm()){
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		$("tContent").value= oEditor.GetXHTML();
		if($("smsRemind").checked == true){
			$("smsRemind").value =1;
		}else{
			$("smsRemind").value = 0;
		}
		if($("sms2Remind").checked == true){
			$("sms2Remind").value =1;
		}else{
			$("sms2Remind").value = 0;
		}
		 document.form1.action = requestURL + "/addTrainingPlanInfo.act";
		$("form1").submit();
	}
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建培训计划</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1" onsubmit="return CheckForm();">
<table class="TableBlock" width="60%" align="center">
	<tr>
	  <td nowrap class="TableData">计划编号：<font color="red">*</font> </td>
	  <td class="TableData" >
	    <INPUT type="text" name="tPlanNo" id="tPlanNo" maxlength="50" class=BigInput size="12" onblur="checkPlanNo(this.value,'')">&nbsp;<span id="plan_no_msg"></span>
	  </td>
	   <td nowrap class="TableData" >计划名称：<font color="red">*</font> </td>
	  <td class="TableData">
	    <INPUT type="text" name="tPlanName" id="tPlanName" class=BigInput size="12" maxlength="100">
	  </td>
	</tr>
  <tr>
    <td nowrap class="TableData">培训渠道：</td>
    <td class="TableData" >
      <select name="tChannel" id="tChannel" title="">
        <option value="" >请选择</option>
        <option value="0">内部培训</option>
        <option value="1">渠道培训</option>
      </select>
    </td>
     <td nowrap class="TableData">培训形式：</td>
    <td class="TableData">
      <select name="tCourseTypes" id="tCourseTypes" title="培训形式可在“系统管理设置”->“分类码管理”模块设置。">
        <option value="">请选择</option>      
      </select>
    </td>
  </tr>
  <tr>
      <td nowrap class="TableData">主办部门：</td>
    <td class="TableData">
  	  <select name="sponsoringDepartment" id="sponsoringDepartment" class="inputSelect">
				<option value="" >请选择</option>
			</select>               
    </td>
     <td nowrap class="TableData">负责人：<font color="red">*</font> </td>
     <td class="TableData">
     	<input type="hidden" name="chargePerson" id="chargePerson" value="">
      <input type="text" name="chargePersonDesc" id="chargePersonDesc" size="12" readonly class="BigStatic" maxlength="20"  value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['chargePerson', 'chargePersonDesc'],null,null,1);">添加</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">计划参与培训人数：</td>
    <td class="TableData" >
      <INPUT type="text" name="tJoinNum" id="tJoinNum" class=BigInput size="12" maxlength="9">&nbsp;人
    </td>
    <td nowrap class="TableData">培训地点：</td>
    <td class="TableData" >
      <INPUT type="text" name="tAddress" id="tAddress" class=BigInput size="12">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">培训机构名称：</td>
    <td class="TableData" >
      <INPUT type="text" name="tInstitutionName" id="tInstitutionName" class=BigInput size="12" >
    </td>
    <td nowrap class="TableData">培训机构联系人：</td>
    <td class="TableData" >
      <INPUT type="text" name="tInstitutionContact" id="tInstitutionContact" class=BigInput size="12" >
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">培训课程名称：</td>
    <td class="TableData" >
      <INPUT type="text" name="tCourseName" id="tCourseName" class=BigInput size="12" >
    </td>
     <td nowrap class="TableData">总课时：</td>
    <td class="TableData">
      <INPUT type="text" name="courseHours" id="courseHours" class=BigInput size="12" maxlength="9">&nbsp;小时
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">开课日期：</td>
    <td class="TableData">
     	<input type="text" name="courseStartDate" id="courseStartDate" size="12" maxlength="10" class="BigInput" value="" onClick="">
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
     <td nowrap class="TableData">结课日期：</td>
    <td class="TableData">
     	<input type="text" name="courseEndDate" id="courseEndDate" size="12" maxlength="10" class="BigInput" value="" onClick="">
    	<img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">培训预算：</td>
    <td class="TableData" colspan=3>
      <INPUT type="text" name="tBcws" id="tBcws" class=BigInput size="12" maxlength="12">&nbsp;元
    </td>
  </tr>
  <tr>
  	<td nowrap class="TableData">参与培训部门: </td>
    <td class="TableData" colspan=3>
      <input type="hidden" name="tJoinDept" id="tJoinDept" value="">
      <textarea cols=50 name="tJoinDeptDesc" id="tJoinDeptDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['tJoinDept', 'tJoinDeptDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('tJoinDept').value='';$('tJoinDeptDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">参与培训人员：</td>
    <td class="TableData" colspan=3>
      <input type="hidden" name="tJoinPerson" id="tJoinPerson" value="">
      <textarea cols=50 name="tJoinPersonDesc" id="tJoinPersonDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['tJoinPerson', 'tJoinPersonDesc'],null,null,1);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('tJoinPerson').value='';$('tJoinPersonDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">培训机构相关信息：</td>
    <td class="TableData" colspan=3>
      <textarea name="tInstitutionInfo" id="tInstitutionInfo" cols="62" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr> 
  <tr>
    <td nowrap class="TableData">培训机构联系人相关信息：</td>
    <td class="TableData" colspan=3>
      <textarea name="tInstituContactInfo" id="tInstituContactInfo" cols="62" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr> 
  <tr>
  <tr>
    <td nowrap class="TableData">培训要求：</td>
    <td class="TableData" colspan=3>
      <textarea name="tRequires" id="tRequires" cols="62" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr> 
  <tr>
    <td nowrap class="TableData">培训说明：</td>
    <td class="TableData" colspan=3>
      <textarea name="tDescription" id="tDescription" cols="62" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr> 
  <tr>
    <td nowrap class="TableData">备注：</td>
    <td class="TableData" colspan=3>
      <textarea name="remark" id="remark" cols="62" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr>
  <tr height="25" id="">
    <td nowrap class="TableData" ><span id="ATTACH_LABEL">附件上传：</span></td>
    <td class="TableData"colspan=3>
      <script>ShowAddFile();</script>
      <input type="hidden" name="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" value="">
    </td>
	</tr>
  <tr>
    <td nowrap class="TableData"> 提醒：</td>
    <td class="TableData" colspan=3>
			<span id="smsRemindDiv" style="float:left;"><input type="checkbox" name="smsRemind" id="smsRemind" checked><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>
			<span id="sms2RemindDiv" style="float:left;"><input type="checkbox" name="sms2Remind" id="sms2Remind" checked><label for="sms2Remind">使用手机短信提醒</label> </span>
		</td>
  </tr>
  <tr id="EDITOR">
    <td class="TableData" colspan="4"> 培训内容：
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
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
    	<input type="hidden" id="tContent" name="tContent" value="">
      <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
    </td>
  </tr>
</table>
</form>

</body>
</html>
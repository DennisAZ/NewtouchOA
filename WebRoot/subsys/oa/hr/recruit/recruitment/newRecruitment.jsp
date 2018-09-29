<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建招聘录用</title>
<link rel="stylesheet" href = "<%=contextPath %>/core/styles/style1/css/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/recruitment/js/recruitmentListLogic.js"></script>
<script type="text/javascript">
function doInit(){
//初始化日期	setDate();
//员工类型
	getSelectedCode("STAFF_OCCUPATION","type");
//职称
	getSelectedCode("PRESENT_POSITION","presentPosition");
//部门
	deptFunc("department");
}
//日期
function setDate(){
	var date1Parameters = {
		inputId:'assPassTime',
		property:{isHaveTime:false},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
		inputId:'onBoardingTime',
		property:{isHaveTime:false},
		bindToBtn:'date2'
	};
	new Calendar(date2Parameters);
	
	var date3Parameters = {
		inputId:'startingSalaryTime',
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
//获取全体部门列表
function deptFunc(deptDiv){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById(deptDiv);
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}



function LoadWindow2(){
	var url= contextPath + "/subsys/oa/hr/recruit/recruitment/plannoinfo/index.jsp";
	loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
	loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
	window.showModalDialog(url,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}
function loadWindow(){
	var url= contextPath + "/subsys/oa/hr/recruit/recruitment/employeeNameSelect/index.jsp";
	var loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
	var loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
	window.showModalDialog(url,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}

function newStaffInfo(expertId){
	//alert(expertId);
	var url = "<%=contextPath%>/subsys/oa/hr/recruit/recruitment/newStaff.jsp?expertId=" + expertId;
	newWindow(url,900,700)
}



function checkForm() {
	if ($("planName").value.trim() == "") {
		alert("计划名称不能为空");
		$("planName").focus();
		$("planName").select();
		return false;
	}
	if ($("applyerName").value.trim() == "") {
		alert("应聘人姓名不能为空");
		$("applyerName").focus();
		$("applyerName").select();
		return false;
	}
	if ($("jobStatus").value.trim() == "") {
		alert("招聘岗位不能为空");
		$("jobStatus").focus();
		$("jobStatus").select();
		return false;
	}
	if ($("assessingOfficer").value.trim() == "") {
		alert("录用负责人不能为空");
		$("assessingOfficerDesc").focus();
		$("assessingOfficerDesc").select();
		return false;
	}
	if ($("department").value.trim() == "") {
		alert("招聘部门不能为空");
		$("department").focus();
		$("department").select();
		return false;
	}
	if ($("onBoardingTime").value.trim() == "") {
		alert("正式入职时间不能为空");
		$("onBoardingTime").focus();
		$("onBoardingTime").select();
		return false;
	}
	if ($("startingSalaryTime").value.trim() == "") {
		alert("正式起薪时间不能为空");
		$("startingSalaryTime").focus();
		$("startingSalaryTime").select();
		return false;
	}
	return true;
}

function doSubmit(){
	if(checkForm()){
		var pars = Form.serialize($('form1'));
		var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct/addHrRecruitInfo.act";
		var rtJson = getJsonRs(url,pars);
		//alert(rsText);
		if(rtJson.rtState == "0"){
			//window.location.href = contextPath + "/subsys/oa/hr/recruit/filter/hrFilterManage.jsp";
			$("formDiv").hide();
			$("remindDiv").show();
			var isHave = rtJson.rtData.isHave;
			var dbExpertId = rtJson.rtData.expertId;
			//$("dbExpertId").value = dbExpertId;
			if(isHave =="0"){
				$("newStaffDiv").show();
			}
			
		}else{
			alert(rtJson.rtMsrg); 
		}
	}
}

</script>

</head>
<body onLoad="doInit();">
<div id="formDiv">
<table border="0" width="770" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建招聘录用信息</span>&nbsp;&nbsp;</td>
  </tr>
</table>
<form  action=""  method="post" name="form1" id="form1" >
<table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData">计划名称：<font color="red">*</font> </td>
      <td class="TableData" >
        <INPUT type="text"name="planName" id="planName" class=BigStatic size="15"  readonly>
        <INPUT type="hidden" name="planNo" id="planNo" value="">
        <a href="javascript:;" class="orgAdd" onClick="LoadWindow2()">选择</a>
      </td>
      <td nowrap class="TableData">应聘者姓名：<font color="red">*</font></td>
      <td class="TableData" >
        <INPUT type="text" name="applyerName" id="applyerName" class=BigStatic size="15"  value="" >
        <INPUT type="hidden" name="expertId" id="expertId" value="">
        <a href="javascript:;" class="orgAdd" onClick="loadWindow()">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">招聘岗位：<font color="red">*</font></td>
      <td class="TableData" >
        <INPUT type="text"name="jobStatus" id="jobStatus" class=BigStatic size="15" readonly>
      </td>
      <td nowrap class="TableData">OA中用户名</td>
      <td class="TableData" colspan=3>
        <INPUT type="text"name="oaName" id="oaName" maxlength="100" class=BigInput size="15" >
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">录用负责人：<font color="red">*</font></td>
      <td class="TableData">
       <input type="hidden" name="assessingOfficer" id="assessingOfficer" value="">
       <INPUT type="text"name="assessingOfficerDesc" id="assessingOfficerDesc" class="BigStatic"  size="15" readonly value="">
         <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['assessingOfficer','assessingOfficerDesc']);">选择</a>       
      </td>
    	<td nowrap class="TableData">录入日期：</td>
      <td class="TableData">
	  <input type="text" name="assPassTime" id="assPassTime" size="15" maxlength="10" class="BigInput" onfocus="clearValue(this);" value="" readonly="readonly" />
	  <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
	  </td>
    </tr>
    <tr>
      <td nowrap class="TableData">招聘部门：<font color="red">*</font></td>
    	<td class="TableData" colspan=3>
    	<select id="department" name="department">
    		<option>请选择</option>
    	</select>
      </td>
    </tr> 
    <tr>
    	<td nowrap class="TableData" width="100">员工类型</td>
    	<td class="TableData">
          <select name="type" id="type" >
        </select>  	
    	</td>   
      <td nowrap class="TableData">行政等级：</td>
      <td class="TableData" colspan=3>
        <INPUT type="text"name="administrationLevel" id="administrationLevel" class=BigInput size="15" >
      </td>
    </tr> 
    <tr>
    	<td nowrap class="TableData">职务：</td>
      <td class="TableData" >
        <INPUT type="text"name="jobPosition" id="jobPosition" class=BigInput size="15" >
      <td nowrap class="TableData" width="100">职称：</td>
      <td class="TableData"  width="180">
        <select name="presentPosition" id="presentPosition" >
				  <option value="">选择职称</option>
        </select>
    	</td>
    </tr> 
    <tr>
    	<td nowrap class="TableData">正式入职时间：<font color="red">*</font></td>
      <td class="TableData">
      	<input type="text" name="onBoardingTime" id="onBoardingTime" size="15" maxlength="10" readonly="readonly" onfocus="clearValue(this);" class="BigInput" value="" />
		 <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">正式起薪时间：<font color="red">*</font></td>
      <td class="TableData">
	  <input type="text" name="startingSalaryTime" id="startingSalaryTime"  size="15" readonly="readonly" maxlength="10" onfocus="clearValue(this);" class="BigInput" value="" />
	   <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
	  </td>
    </tr>   
     <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="remark" id="remark" cols="66" rows="5" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>
</div>

<div id="remindDiv" style="display: none">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">招聘录用信息录入成功！</div>
    </td>
  </tr>
</table>
<br><center>
	<input type="button" class="BigButton" value="返回" title="继续新建招聘录用信息" onclick="window.location.reload();"> &nbsp;&nbsp;
	<input id="newStaffDiv" name="newStaffDiv" style="display: none;" type="button" class="BigButtonC" value="建立人事档案" title="建立人事档案" onclick="newStaffInfo($('expertId').value);"> &nbsp;&nbsp;
</center>
</div>


</body>
</html>
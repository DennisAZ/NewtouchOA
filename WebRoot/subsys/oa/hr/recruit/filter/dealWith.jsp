<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>筛选办理 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=contextPath %>/core/styles/style1/css/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/filter/js/dealWithLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getHrFilterInfo();
	setDate();
}
var change = 0;
function getHrFilterInfo(){
	var requestURLStr = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getHrFilterDetail.act";
	var rtJson = getJsonRs(url, "seqId=<%=seqId%>" );
	//alert(rsText);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		if(data.planNo){
			$("planName").innerHTML = getPlanNameByPlanNo(data.planNo);
		}
		if(data.transactorStep){
			$("transactorStep").innerHTML = getPersonName(data.transactorStep);
		}
		if(data.nextTransaStep){
			$("nextTransaStep").innerHTML = getPersonName(data.nextTransaStep);
		}
		if(data.endFlag == "2"){
			$("endFlagInfo").innerHTML = "(已通过筛选)";
		}else if(data.endFlag == "1"){
			$("endFlagInfo").innerHTML = "(未通过筛选)";
			
		}else{
			$("endFlagInfo").innerHTML = "(待筛选)";
		}
		$("stepFlag").value = data.stepFlag;
		
		var stepFlag = data.stepFlag;
		var nextTransaStep = data.nextTransaStep;
		var transactorStep1 = data.transactorStep1;
		var loginUserSeqId = '<%=person.getSeqId()%>';
		//alert(data.transactorStep1);
		if(stepFlag == 1 && nextTransaStep == loginUserSeqId){
			getSelectedCode("HR_RECRUIT_FILTER", "filterMethod1");
			change = 1;
			$("stepFlag1").show();
		}else if(transactorStep1.trim() ){
			$("transactorStep1Div").show();
			$("dbFilterDateTime1").innerHTML = data.filterDateTime1;
			$("dbFilterMethod1").innerHTML = selectCodeById(data.filterMethod1);
			$("dbFirstContent1").innerHTML = data.firstContent1;
			$("dbFirstView1").innerHTML = data.firstView1;
			$("dbTransactorStep1").innerHTML = getPersonName(data.transactorStep1);
			if(data.passOrNot1 == "1"){
				$("dbPassOrNot1").innerHTML = "通过";
			}else{
				$("dbPassOrNot1").innerHTML = "未通过";
			}
			$("dbNextTransaStep1").innerHTML = getPersonName(data.nextTransaStep1);
			$("dbNextDateTime1").innerHTML = data.nextDateTime1;
		}
		if(stepFlag == 2 && data.nextTransaStep1 == loginUserSeqId){
			getSelectedCode("HR_RECRUIT_FILTER", "dbFilterMethod2") ;
			
			change = 1;
			$("stepFlag2").show();
		}else if(data.transactorStep2.trim()){
			$("transactorStep2Div").show();
			$("filterMethod2").innerHTML = selectCodeById(data.filterMethod2);
			$("transactorStep2").innerHTML = getPersonName(data.transactorStep2);
			$("nextTransaStep2").innerHTML = getPersonName(data.nextTransaStep2);
			if(data.passOrNot2 == "1"){
				$("passOrNot2").innerHTML = "通过";
			}else{
				$("passOrNot2").innerHTML = "未通过";
			}
		}
		if(stepFlag == 3 && data.nextTransaStep2 == loginUserSeqId){
			getSelectedCode("HR_RECRUIT_FILTER", "dbFilterMethod3") ;
			change = 1;
			$("stepFlag3").show();
		}else if(data.transactorStep3.trim()){
			$("transactorStep3Div").show();
			$("filterMethod3").innerHTML = selectCodeById(data.filterMethod3);
			$("transactorStep3").innerHTML = getPersonName(data.transactorStep3);
			$("nextTransaStep3").innerHTML = getPersonName(data.nextTransaStep3);
			if(data.passOrNot3 == "1"){
				$("passOrNot3").innerHTML = "通过";
			}else{
				$("passOrNot3").innerHTML = "未通过";
			}
		}
		if(stepFlag == 4 && data.nextTransaStep3 == loginUserSeqId){
			getSelectedCode("HR_RECRUIT_FILTER", "dbFilterMethod4") ;
			change = 1;
			$("stepFlag4").show();
		}else if(data.transactorStep4.trim()){
			$("transactorStep4Div").show();
			$("filterMethod4").innerHTML = selectCodeById(data.filterMethod4);
			$("transactorStep4").innerHTML = getPersonName(data.transactorStep4);
			$("transactorStep4").innerHTML = getPersonName(data.transactorStep4);
			if(data.passOrNot4 == "1"){
				$("passOrNot4").innerHTML = "通过";
			}else{
				$("passOrNot4").innerHTML = "未通过";
			}
		}
	}
	if(change == "1"){
		$("overDiv").show();
	}else{
		$("backDiv").show();
	}
}
function getPlanNameByPlanNo(planNo){
	var str = "";
	var requestURLStr = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getPlanNameByPlanNo.act";
	var rtJson = getJsonRs(url, "planNo=" + encodeURIComponent(planNo) );
	//alert(rsText);
	if(rtJson.rtState == "0"){
		str = rtJson.rtData.planName;
	}else{
		alert(rtJson.rtMsrg);
	}
	return str;
}
function getPersonName(seqId){
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getPersonName.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		str = rtJson.rtData.userName;
	}else {
		alert(rtJson.rtMsrg); 
	}
	return str;
}

function next_play(STEP_FLAG) {
	//alert(STEP_FLAG);
	if (STEP_FLAG == 1) {
		if (document.form1.PASS_OR_NOT1.value == 0) {
			document.getElementById("NEXT1").style.display = 'none';
			document.getElementById("BTN_NEXT").style.display = 'none';
		} else {
			document.getElementById("NEXT1").style.display = '';
			document.getElementById("BTN_NEXT").style.display = '';
		}
	}
	if (STEP_FLAG == 2) {
		if (document.form1.PASS_OR_NOT2.value == 0) {
			document.getElementById("NEXT2").style.display = 'none';
			document.getElementById("BTN_NEXT").style.display = 'none';
		} else {
			document.getElementById("NEXT2").style.display = '';
			document.getElementById("BTN_NEXT").style.display = '';
		}
	}
	if (STEP_FLAG == 3) {
		if (document.form1.PASS_OR_NOT3.value == 0) {
			document.getElementById("NEXT3").style.display = 'none';
			document.getElementById("BTN_NEXT").style.display = 'none';
		} else {
			document.getElementById("NEXT3").style.display = '';
			document.getElementById("BTN_NEXT").style.display = '';
		}
	}
}


function sendform(flag, step1){
	var step = $("stepFlag").value.trim();
	//alert("step>>"+step);
	if (flag==1)	{
		document.form1.IS_FINISH.value='1';
		//alert($("IS_FINISH").value);
	}	else if (!checkform(step)){
		return;
	}
	doSubmit();
	//document.form1.submit();
}
function checkform(STEP_FLAG) {
	if (STEP_FLAG == 1) {
		if (document.form1.PASS_OR_NOT1.value == 1) {
			if ($("nextTransaStep1").value.trim() == "") {
				alert("下一步筛选人不能为空");
				$("nextTransaStep1Desc").focus();
				$("nextTransaStep1Desc").select();
				return false;
			}
			if ($("nextDateTime1").value.trim() == "") {
				alert("下一步筛选时间不能为空");
				$("nextDateTime1").focus();
				$("nextDateTime1").select();
				return false;
			}
		}
	}
	if (STEP_FLAG == 2) {
		if (document.form1.PASS_OR_NOT2.value == 1) {
			if ($("dbNextTransaStep2").value.trim() == "") {
				alert("下一步筛选人不能为空");
				$("dbNextTransaStep2Desc").focus();
				$("dbNextTransaStep2Desc").select();
				return false;
			}
			if ($("dbNextDateTime2").value.trim() == "") {
				alert("下一步筛选时间不能为空");
				$("dbNextDateTime2").focus();
				$("dbNextDateTime2").select();
				return false;
			}
		}

	}
	if (STEP_FLAG == 3) {
		if (document.form1.PASS_OR_NOT3.value == 1) {
			if ($("dbNextTransaStep3").value.trim() == "") {
				alert("下一步筛选人不能为空");
				$("dbNextTransaStep3Desc").focus();
				$("dbNextTransaStep3Desc").select();
				return false;
			}
			if ($("dbNextDateTime3").value.trim() == "") {
				alert("下一步筛选时间不能为空");
				$("dbNextDateTime3").focus();
				$("dbNextDateTime3").select();
				return false;
			}
		}
	}
	if (STEP_FLAG == 4) {
		if (document.form1.PASS_OR_NOT4.value == "") {
			alert("请选择是否通过");
			return false;
		}
	}
	return true;
}



function doSubmit(){
	var pars = Form.serialize($('form1'));
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/dealWithFilter1.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url,pars);
	if(rtJson.rtState == "0"){
		window.location.href = contextPath + "/subsys/oa/hr/recruit/filter/hrFilterManage.jsp";
	}else{
		alert(rtJson.rtMsrg); 
	}
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 筛选办理</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="80%" align="center">
  <tr>
    <td nowrap class="TableContent" colspan="4" align="center">基本信息<div id="endFlagInfo" style='color:red'></div> </td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">应聘者姓名：</td>
    <td class="TableData" ><span id="employeeName"></span> </td>
   <td nowrap align="left" width="120" class="TableContent">计划名称：</td>
    <td class="TableData"><span id="planName"></span> </td>  
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">应聘岗位：</td>
    <td class="TableData" ><span id="position"></span> </td> 
    <td nowrap align="left" width="120" class="TableContent">所学专业：</td>
    <td class="TableData" ><span id="employeeMajor"></span> </td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">联系电话：</td>
    <td class="TableData"><span id="employeePhone"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">发起人：</td>
    <td class="TableData" ><span id="transactorStep"></span> </td> 
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">下一次筛选办理人：</td>
    <td class="TableData"><span id="nextTransaStep"></span>    </td>
    <td nowrap align="left" width="120" class="TableContent">下一次筛选时间：</td>
    <td class="TableData"><span id="nextDateTime"></span>    </td> 
  </tr>
</table>

<div id="stepFlag1" style="display: none">
<br>
<table class="TableBlock" width="80%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤一</td>
  </tr>
 <tr>
    <td nowrap class="TableData">初选时间：</td>
    <td class="TableData">
      <input type="text" name="filterDateTime1" id="filterDateTime1" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
     	<img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;&nbsp;<a href="javascript:resetEndTime('filterDateTime1');">置为当前时间</a>
    </td>
    <td nowrap class="TableData">初选方式：</td>
    <td class="TableData" >
      <select name="filterMethod1" id="filterMethod1" title="筛选方式可在“人力资源设置”->“HRMS代码设置”模块设置。">
      </select>
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">初选内容：</td>
    <td class="TableData" colspan=3>
      <textarea name="firstContent1" id="firstContent1" cols="77" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">初选意见：</td>
    <td class="TableData" colspan=3>
      <textarea name="firstView1" id="firstView1" cols="77" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">初选办理人：</td>
    <td class="TableData" >
      <INPUT type="text" name="fTransactorStep1Desc" id="fTransactorStep1Name" size="15" class="BigStatic" readonly value="<%=person.getUserName() %>">
      <INPUT type="hidden" name="fTransactorStep1" id="fTransactorStep1" value="<%=person.getSeqId() %>">
    </td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData">
      <select name="PASS_OR_NOT1" id="PASS_OR_NOT1" style="background: white;" title="" onchange="next_play($('stepFlag').value );">
        <option value="0" >未通过</option>
        <option value="1" >通过</option>
      </select>
    </td>
  </tr>
  <tr id="NEXT1" style="display:none">
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData">
      <input type="hidden" name="nextTransaStep1" id="nextTransaStep1" value="">
       <INPUT type="text" name="nextTransaStep1Desc" id="nextTransaStep1Desc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['nextTransaStep1', 'nextTransaStep1Desc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('nextTransaStep1').value='';$('nextTransaStep1Desc').value='';">清空</a>
    </td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData">
      <input type="text" name="nextDateTime1" id="nextDateTime1" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
     	<img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;&nbsp;<a href="javascript:resetEndTime('nextDateTime1');">置为当前时间</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 提醒：</td>
    <td class="TableData" colspan="3"><?=sms_remind(50);?></td>
  </tr>
</table>
</div>

<div id="transactorStep1Div" style="display: none;">
<br>
<table class="TableBlock" width="80%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤一</td>
  </tr>
 <tr>
    <td nowrap class="TableData">初选时间：</td>
    <td class="TableData"><span id="dbFilterDateTime1"></span> </td>
    <td nowrap class="TableData">初选方式：</td>
    <td class="TableData" ><span id="dbFilterMethod1"></span> </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">初选内容：</td>
    <td class="TableData" colspan=3><span id="dbFirstContent1"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">初选意见：</td>
    <td class="TableData" colspan=3><span id="dbFirstView1"></span> </td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">初选办理人：</td>
    <td class="TableData" ><span id="dbTransactorStep1"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="dbPassOrNot1"></span></td>
  </tr>
  <tr>
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData"><span id="dbNextTransaStep1"></span></td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData"><span id="dbNextDateTime1"></span> </td>
  </tr>
</table>
</div>

<div id="stepFlag2" style="display: none;">
<br>
<table class="TableBlock" width="80%" align="center" id="table2">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤二：</td>
  </tr>
  <tr>
    <td nowrap class="TableData">复选日期：</td>
    <td class="TableData">
      <input type="text" name="dbFilterDateTime2" id="dbFilterDateTime2" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
     	<img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;&nbsp;<a href="javascript:resetEndTime('dbFilterDateTime2');">置为当前时间</a>
    </td>
    <td nowrap class="TableData">复选方式：</td>
    <td class="TableData" >
      <select name="dbFilterMethod2" id="dbFilterMethod2" style="background: white;" title="筛选方式可在“人力资源设置”->“HRMS代码设置”模块设置。">
      </select>
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">复选内容：</td>
    <td class="TableData" colspan=3>
      <textarea name="dbFirstContent2" id="dbFirstContent2" cols="67" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">复选意见：</td>
    <td class="TableData" colspan=3>
      <textarea name="dbFirstView2" id="dbFirstView2" cols="67" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">当前办理人：</td>
    <td class="TableData" >
      <INPUT type="text" name="dbTransactorStep2Desc" id="dbTransactorStep2Desc" size="15" class="BigStatic" readonly value="<%=person.getUserName() %>">&nbsp;
      <INPUT type="hidden" name="dbTransactorStep2" value="<%=person.getSeqId() %>">
      
    </td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData">
      <select name="PASS_OR_NOT2" style="background: white;" title="" onchange="next_play($('stepFlag').value );">
        <option value="0" >未通过</option>
        <option value="1" >通过</option>
      </select>
    </td>
  </tr>
  <tr id="NEXT2" style="display:none">
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData">
      <input type="hidden" name="dbNextTransaStep2" id="dbNextTransaStep2" value="">
       <INPUT type="text" name="dbNextTransaStep2Desc" id="dbNextTransaStep2Desc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['dbNextTransaStep2', 'dbNextTransaStep2Desc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('dbNextTransaStep2').value='';$('dbNextTransaStep2Desc').value='';">清空</a>
    </td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData">
      <input type="text" name="dbNextDateTime2" id="dbNextDateTime2" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
     	<img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;&nbsp;<a href="javascript:resetEndTime('dbNextDateTime2');">置为当前时间</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 提醒：</td>
    <td class="TableData" colspan=3><?=sms_remind(50);?></td>
  </tr>
</table>
</div>
<div id="transactorStep2Div" style="display: none;">
<br>
<table class="TableBlock" width="80%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤二</td>
  </tr>
 <tr>
    <td nowrap class="TableData">复选时间：</td>
    <td class="TableData"><span id="filterDateTime2"></span></td>
    <td nowrap class="TableData">复选方式：</td>
    <td class="TableData" > <span id="filterMethod2"></span> </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">复选内容：</td>
    <td class="TableData" colspan=3><span id="firstContent2"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">复选意见：</td>
    <td class="TableData" colspan=3><span id="firstView2"></span></td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">复选办理人：</td>
    <td class="TableData" ><span id="transactorStep2"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="passOrNot2"></span></td>
  </tr>
  <tr>
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData"><span id="nextTransaStep2"></span></td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData"><span id="nextDateTime2"></span></td>
  </tr>
</table>
</div>

<div id="stepFlag3" style="display: none;">
<br>
<table class="TableBlock" width="80%" align="center" id="table3">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤三：</td>
  </tr>
 <tr>
    <td nowrap class="TableData">决选日期：</td>
    <td class="TableData">
        <input type="text" name="dbFilterDateTime3" id="dbFilterDateTime3" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
	     	<img id="date5" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
	      &nbsp;&nbsp;<a href="javascript:resetEndTime('dbFilterDateTime3');">置为当前时间</a>
        
    </td>
    <td nowrap class="TableData">决选方式：</td>
    <td class="TableData" >
      <select name="dbFilterMethod3" id="dbFilterMethod3" style="background: white;" title="筛选方式可在“人力资源设置”->“HRMS代码设置”模块设置。">
      </select>
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">决选内容：</td>
    <td class="TableData" colspan=3>
      <textarea name="dbFirstContent3" id="dbFirstContent3" cols="67" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">决选意见：</td>
    <td class="TableData" colspan=3>
      <textarea name="dbFirstView3" id="dbFirstView3" cols="67" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">当前办理人：</td>
    <td class="TableData" >
      <INPUT type="text" name="dbTransactorStep3Desc" id="dbTransactorStep3Desc" size="15" class="BigStatic" readonly value="<%=person.getUserName() %>">&nbsp;
      <INPUT type="hidden" name="dbTransactorStep3" id="dbTransactorStep3" value="<%=person.getSeqId() %>">
    </td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData">
      <select name="PASS_OR_NOT3" style="background: white;" title="" onchange="next_play($('stepFlag').value );">
        <option value="0" >未通过</option>
        <option value="1" >通过</option>
      </select>
    </td>
  </tr>
  <tr id="NEXT3" style="display:none">
    <td nowrap class="TableData">下一步骤办理人：<font color="red">*</font> </td>
    <td class="TableData">
      <input type="hidden" name="dbNextTransaStep3" id="dbNextTransaStep3" value="">
       <INPUT type="text" name="dbNextTransaStep3Desc" id="dbNextTransaStep3Desc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['dbNextTransaStep3', 'dbNextTransaStep3Desc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('dbNextTransaStep3').value='';$('dbNextTransaStep3Desc').value='';">清空</a>
    </td>
    <td nowrap class="TableData">下一次筛选时间：<font color="red">*</font></td>
    <td class="TableData">
       <input type="text" name="dbNextDateTime3" id="dbNextDateTime3" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
     	<img id="date6" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;&nbsp;<a href="javascript:resetEndTime('dbNextDateTime3');">置为当前时间</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 提醒：</td>
    <td class="TableData" colspan=3><?=sms_remind(50);?></td>
  </tr>
</table>
</div>
<div id="transactorStep3Div" style="display: none;">
<br>
<table class="TableBlock" width="80%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤三</td>
  </tr>
 <tr>
    <td nowrap class="TableData">决选时间：</td>
    <td class="TableData"> <span id="filterDateTime3"></span> </td>
    <td nowrap class="TableData">决选方式：</td>
    <td class="TableData" ><span id="filterMethod3"></span></td> 
  </tr>
  <tr>
    <td nowrap class="TableData">决选内容：</td>
    <td class="TableData" colspan=3><span id="firstContent3"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">决选意见：</td>
    <td class="TableData" colspan=3><span id="firstView3"></span></td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">决选办理人：</td>
    <td class="TableData" ><span id="transactorStep3"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="passOrNot3"></span></td>
  </tr>
  <tr>
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData"><span id="nextTransaStep3"></span></td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData"><span id="nextDateTime3"></span> </td>
  </tr>
</table>
</div>
<div id="stepFlag4" style="display: none;">
<br>
<table class="TableBlock" width="80%" align="center" id="table4">
 <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤四：</td>
  </tr>
  <tr>
    <td nowrap class="TableData">加试日期：</td>
    <td class="TableData">
      <input type="text" name="dbFilterDateTime4" id="dbFilterDateTime4" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
     	<img id="date7" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;&nbsp;<a href="javascript:resetEndTime('dbFilterDateTime4');">置为当前时间</a>
    </td>
    <td nowrap class="TableData">加试方式：</td>
    <td class="TableData" >
      <select name="dbFilterMethod4" id="dbFilterMethod4" style="background: white;" title="筛选方式可在“人力资源设置”->“HRMS代码设置”模块设置。">
      </select>
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">加试内容：</td>
    <td class="TableData" colspan=3>
      <textarea name="dbFirstContent4" id="dbFirstContent4" id="" cols="67" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">加试意见：</td>
    <td class="TableData" colspan=3>
      <textarea name="dbFirstView4" id="dbFirstView4" cols="67" rows="4" class="BigInput" value=""></textarea>
    </td>
  </tr>  
  	<tr>
  	<td nowrap class="TableData">当前办理人：</td>
    <td class="TableData" >
      <INPUT type="text" name="dbTransactorStep4Desc" id="dbTransactorStep4Desc" size="15" class="BigStatic" readonly value="<%=person.getUserName() %>">&nbsp;
      <INPUT type="hidden" name="dbTransactorStep4" id="dbTransactorStep4" value="<%=person.getSeqId() %>">
    </td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData">
      <select name="PASS_OR_NOT4" style="background: white;" title="">
        <option value="0" >未通过</option>
        <option value="1" >通过</option>
      </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 提醒：</td>
    <td class="TableData" colspan=3><?=sms_remind(50);?></td>
  </tr>
</table>
</div>
<div id="transactorStep4Div" style="display: none;">
<br>
<table class="TableBlock" width="80%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤四</td>
  </tr>
 <tr>
    <td nowrap class="TableData">加试时间：</td>
    <td class="TableData"><span id="filterDateTime4"></span> </td>
    <td nowrap class="TableData">加试方式：</td>
    <td class="TableData" ><span id="filterMethod4"></span>  </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">加试内容：</td>
    <td class="TableData" colspan=3><span id="firstContent4"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">加试意见：</td>
    <td class="TableData" colspan=3><span id="firstView4"></span></td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">加试办理人：</td>
    <td class="TableData" ><span id="transactorStep4"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="passOrNot4"></span></td>
  </tr>
</table>

</div>

<div id="stepFlag5">
</div>
<div id="transactorStep5">
</div>


<div id="overDiv" style="display: none;">
<table class="TableBlock" width="80%" align="center">
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
    	<input type="hidden" id="stepFlag" name="stepFlag" value="">
      <input type="button" id="BTN_NEXT" value="下一步骤" class="BigButton" style="display:none" onclick="sendform('0', '');">&nbsp;
	  <input type="button" id="BTN_FINISH" value="结束筛选" class="BigButton"  onclick="sendform('1', '');">&nbsp;
	  <input type="hidden" id="IS_FINISH" name="IS_FINISH" value="0">
    </td>
  </tr>
</table>
</div>
<div id="backDiv" align="center" style="display: none;">
<br>
	 <input type="button" value="返回" onclick="location.href = '<%=contextPath %>/subsys/oa/hr/recruit/filter/hrFilterManage.jsp';" class="BigButton">
</div>



</form>
</body>
</html>
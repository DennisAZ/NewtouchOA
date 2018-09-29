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
<title>编辑招聘筛选信息 </title>
<link rel="stylesheet" href = "<%=contextPath %>/core/styles/style1/css/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit(){
	getHrFilterInfo();
	setDate();
	getSysRemind("smsRemindDiv","smsRemind",65);
	moblieSmsRemind("sms2RemindDiv","sms2Remind",65);
}

//日期
function setDate(){
	var date1Parameters = {
		inputId:'nextDateTime',
		property:{isHaveTime:true},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
}
function clearValue(str){
	if(str){
		str.value = "";
	}
}

function getHrFilterInfo(){
	var requestURLStr = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getHrFilterDetail.act";
	var rtJson = getJsonRs(url, "seqId=<%=seqId%>" );
	//alert(rsText);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		if(data.planNo){
			$("planName").value = getPlanNameByPlanNo(data.planNo);
		}
		if(data.transactorStep){
			getPersonName("transactorStep");
		}else{
			$("transactorStep").value = "<%=person.getSeqId() %>";
			$("transactorStepDesc").value = "<%=person.getUserName() %>";
		}
		if(data.nextTransaStep){
			getPersonName("nextTransaStep");
		}

	}
}
//根据人员seqId获取绑定值的人员名称getPersonName("proCreator");
function getPersonName(personIdDiv){
	if($(personIdDiv) && $(personIdDiv).value.trim()){
		bindDesc([{cntrlId:personIdDiv, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
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
function resetEndTime(){
	var date = new Date();
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	m = (m > 9) ? m : '0' + m;
	var d = date.getDate();
	d = (d > 9) ? d : '0' + d;
	var time = date.toLocaleTimeString();
	$('nextDateTime').value = y + '-' + m + '-' + d + ' ' + time;
}
function loadWindow(){
	var url= contextPath + "/subsys/oa/hr/recruit/filter/employeeNameSelect/index.jsp";
	var loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
	var loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
	window.showModalDialog(url,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}
//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind,type){ 
	var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=" + type; 
	var rtJson = getJsonRs(requestUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	//alert(rsText);
	var allowRemind = prc.allowRemind;;//是否允许显示 
	var defaultRemind = prc.defaultRemind;//是否默认选中 
	var mobileRemind = prc.mobileRemind;//手机默认选中 
	if(allowRemind=='2'){ 
		$(remidDiv).style.display = 'none'; 
	}else{
		$(remidDiv).style.display = ''; 
		if(defaultRemind=='1'){ 
			$(remind).checked = true; 
		} 
	}
	if(document.getElementById(remind).checked){
		document.getElementById(remind).value = "1";
	}else{
		document.getElementById(remind).value = "0";
	}
}
//设置提醒值
function checkBox(ramCheck){
	if(document.getElementById(ramCheck).checked){
		document.getElementById(ramCheck).value = "1";
	}else{
		document.getElementById(ramCheck).value = "0";
	}
}
//判断是否要显示手机短信提醒 
function moblieSmsRemind(remidDiv,remind,type){
	var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=" + type; 
	var rtJson = getJsonRs(requestUrl); 
	if(rtJson.rtState == "1"){ 
		alert(rtJson.rtMsrg); 
		return ; 
	} 
	var prc = rtJson.rtData; 
	//alert(rsText);
	var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
	if(moblieRemindFlag == '2'){ 
		$(remidDiv).style.display = '';
		$(remind).checked = true;
	}else if(moblieRemindFlag == '1'){ 
		$(remidDiv).style.display = '';
		$(remind).checked = false;
	}else{
		$(remidDiv).style.display = 'none'; 
	}
	if(document.getElementById(remind).checked){
		document.getElementById(remind).value = "1";
	}else{
		document.getElementById(remind).value = "0";
	}
}

function checkForm(){
	if($("planName").value.trim()==""){
		alert("请选择计划名称！");
		$("planName").focus();
		$("planName").select();
		return false;
	}
	if($("employeeName").value.trim()==""){
		alert("请选择应聘人姓名！");
		$("employeeName").focus();
		$("employeeName").select();
		return false;
	}
	if($("employeePhone").value.trim()==""){
		alert("联系电话不能为空！");
		$("employeePhone").focus();
		$("employeePhone").select();
		return false;
	}
	if($("nextTransaStep").value.trim()==""){
		alert("下一次筛选办理人不能为空！");
		$("nextTransaStepDesc").focus();
		$("nextTransaStepDesc").select();
		return false;
	}
	
	return true;
}

function doSubmit(){
	if(checkForm()){
		var pars = Form.serialize($('form1'));
		var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/updateHrFilterInfo.act?seqId=<%=seqId%>";
		var rtJson = getJsonRs(url,pars);
		if(rtJson.rtState == "0"){
			window.location.href = contextPath + "/subsys/oa/hr/recruit/filter/hrFilterManage.jsp";
		}else{
			alert(rtJson.rtMsrg); 
		}
	}
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑招聘筛选信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form  action=""  method="post" name="form1" id="form1" >
<table class="TableBlock" width="100%" align="center">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center">筛选准备</td>
  </tr>
  <tr>
    <td nowrap class="TableData">应聘者姓名：<font color="red">*</font> </td>
    <td nowrap class="TableData" >
      <INPUT type="text" name="employeeName" id="employeeName" class=BigInput size="15"  value="">
      <INPUT type="hidden" name="expertId" id="expertId" value="">
      <a href="javascript:;" class="orgAdd" onClick="loadWindow()">选择</a>
    </td>
   <td nowrap class="TableData">计划名称：<font color="red">*</font></td>
    <td class="TableData">
      <INPUT type="text" name="planName" id="planName" class=BigInput size="15" value="">
      <INPUT type="hidden" name="planNo" id="planNo" value="">
    </td>  
  </tr>
  <tr>
    <td nowrap class="TableData">应聘岗位：</td>
    <td class="TableData" >
      <INPUT type="text" name="position" id="position" class=BigInput size="15" value="">
    </td> 
    <td nowrap class="TableData">所学专业：</td>
    <td class="TableData" >
      <INPUT type="text"name="employeeMajor" id="employeeMajor" class=BigInput size="15" value="">
    </td>    
  </tr>
  <tr>
    <td nowrap class="TableData">联系电话：<font color="red">*</font></td>
    <td class="TableData">
      <INPUT type="text"name="employeePhone" id="employeePhone" class=BigInput size="15" value="">
    </td>
    <td nowrap class="TableData">发起人：</td>
    <td class="TableData" >
      <INPUT type="text" name="transactorStepDesc" id="transactorStepDesc" size="15" class="BigStatic" readonly value="">&nbsp;
      <INPUT type="hidden" name="transactorStep" id="transactorStep" value="">
    </td>     
  </tr>
  <tr>
    <td nowrap class="TableData">下一次筛选办理人：<font color="red">*</font></td>
    <td nowrap class="TableData">
      <input type="hidden" name="nextTransaStep" id="nextTransaStep" value="">
       <INPUT type="text" name="nextTransaStepDesc" id="nextTransaStepDesc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['nextTransaStep', 'nextTransaStepDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('nextTransaStep').value='';$('nextTransaStepDesc').value='';">清空</a>
      
      
    </td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData">
      <input type="text" name="nextDateTime" id="nextDateTime" size="20" maxlength="20" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
       &nbsp;&nbsp;<a href="javascript:resetEndTime();">置为当前时间</a>
    </td>    
  </tr>
  <tr>
    <td nowrap class="TableData"> 提醒：</td>
    <td class="TableData" colspan=3>
	    <span id="smsRemindDiv" style="display: none"><input type="checkbox" name="smsRemind" id="smsRemind" value="" onclick="checkBox('smsRemind')"><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>
			<span id="sms2RemindDiv" style="display: none"><input type="checkbox" name="sms2Remind" id="sms2Remind" value="" onclick="checkBox('sms2Remind')"><label for="sms2Remind">使用手机短信提醒 </label>&nbsp;&nbsp;</span>
    
    </td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
      <input type="button" value="确定" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" onclick="location.href = '<%=contextPath %>/subsys/oa/hr/recruit/filter/hrFilterManage.jsp';" class="BigButton">
    </td>
  </tr>
</table>
</form>

</body>
</html>
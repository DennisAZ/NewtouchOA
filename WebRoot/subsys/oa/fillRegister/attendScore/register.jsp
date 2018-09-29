<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>补登记</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function doInit(){
	setDate();
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'beginDate',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
    inputId:'endDate',
    property:{isHaveTime:false}
    ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}

function doSubmit(){
	if(checkForm()){
		var pars = Form.serialize($('form1'));
	  var url = "<%=contextPath %>/yh/subsys/oa/fillRegister/act/YHAttendFillAct/addAttendScore.act";
	  var rtJson = getJsonRs(url,pars);
	  if(rtJson.rtState == "0"){
	  	$("form1Div").hide();
	  	$("remindDiv").show();
	  }else{
	    alert(rtJson.rtMsrg); 
	  }
	}
}
function checkForm(){
  if($("proposer").value == ""){ 
    alert("申请人不能为空！");
    $("proposerDesc").focus();
    return false;
  }
	if($("beginDate").value == ""){
		alert("补登记开始日期不能为空！");
		$("beginDate").focus();
		return false;
	}
	if($("endDate").value == ""){
		alert("补登记结束日期不能为空！");
		$("endDate").focus();
		return false;
	}
	if(!checkDate($("beginDate"),$("endDate"))){
		return false;
	}
	return true;
}
//验证是否为日期(不包含时间)
function checkDate(leaveDate1,leaveDate2){
	var leaveDate1Array = leaveDate1.value.trim().split(" "); 
	var leaveDate2Array = leaveDate1.value.trim().split(" "); 
	if(!leaveDate1.value  || !leaveDate2.value){
		if(leaveDate1.value){
			if(!isValidDateStr(leaveDate1.value)){
				alert("日期格式不对，应形如 1999-01-01"); 
				leaveDate1.focus(); 
				leaveDate1.select(); 
				return false; 
			}
		}else if(leaveDate2.value){
			if(!isValidDateStr(leaveDate2.value)){
				alert("日期格式不对，应形如1999-01-01"); 
				leaveDate2.focus(); 
				leaveDate2.select(); 
				return false; 
			}
		}
		return true;
	}
	if(!isValidDateStr(leaveDate1.value)){
		alert("日期格式不对，应形如 1999-01-01"); 
		leaveDate1.focus(); 
		leaveDate1.select(); 
		return false; 
	}
	if(!isValidDateStr(leaveDate2.value)){
		alert("日期格式不对，应形如 1999-01-01"); 
		leaveDate2.focus(); 
		leaveDate2.select();  
		return false; 
	}
	if(leaveDate1.value > leaveDate2.value){ 
		alert("开始日期不能大于结束日期！"); 
		leaveDate1.select(); 
		leaveDate1.focus(); 
		return (false); 
	}
	return true;
}

function backFuncs(){
	window.location.reload();
}

</script>
</head>
<body onload="doInit();">
<div id="form1Div" style="display:">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 出国培训补登记</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
<tr>
      <td nowrap class="TableData">申请人：<font color="red">*</font> </td>
     <td class="TableData">
     	<input type="hidden" name="proposer" id="proposer" value="">
      <input type="text" name="proposerDesc" id="proposerDesc" size="12" readonly class="BigStatic" maxlength="20"  value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['proposer', 'proposerDesc']);">添加</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">补登记日期：</td>
    <td class="TableData" colspan=3>
     	<input type="text" name="beginDate" id="beginDate" size="12" maxlength="10" class="BigInput" value="" onClick="">
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    至
      <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" onClick="">
     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
   </tr>
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
      <input type="button" value="补登记" onclick="doSubmit();" class="BigButton">
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
      <div class="content" style="font-size:12pt">补登记提交成功！</div>
    </td>
  </tr>
</table>
<br><center>
	<input type="button" value="返回" class="BigButton" onClick="backFuncs();">	
</center>
</div>
</body>
</html>
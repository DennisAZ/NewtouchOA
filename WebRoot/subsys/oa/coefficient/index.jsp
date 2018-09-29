<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>考核系数</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/coefficient/js/util.js"></script>
<script type="text/javascript">
function doSubmit(){
	if(checkForm()){
		var pars = $('form1').serialize();
		var requestURL = "<%=contextPath %>/yh/subsys/oa/coefficient/act/YHCoefficientAct/addCoefficient.act";
		var rtJson = getJsonRs(requestURL,pars);
		if (rtJson.rtState == '0'){
			$("form1Div").hide();
			$("remindDiv").show();
		}else{
			alert(rtJson.rtMsrg); 
			return ;
		}
	}
}
function doInit(){
	var requestURL = "<%=contextPath %>/yh/subsys/oa/coefficient/act/YHCoefficientAct/getCoefficient.act";
	var rtJson = getJsonRs(requestURL);
	if (rtJson.rtState == '0'){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		if(data.yearScore == "0"){
			$("yearScore").value = "";
		}
		if(data.monthScore == "0"){
			$("monthScore").value = "";
		}
		if(data.chiefScore == "0"){
			$("chiefScore").value = "";
		}
		
		if(data.checkScore == "0"){
			$("checkScore").value = "";
		}
		if(data.awardScore == "0"){
			$("awardScore").value = "";
		}
		
	}else{
		alert(rtJson.rtMsrg); 
		return ;
	}
}
function backFuncs(){
	window.location.reload();
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 考核系数</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<div id="form1Div" style="display:">
<form  action=""  method="post" name="form1" id="form1" onsubmit="return CheckForm();">
<table class="TableBlock" width="40%" align="center">
	<tr>
		<td nowrap class="TableData" width="50%" >年终考核分系数 ：</td>
   	<td class="TableData" >
     	<input type="text" name="yearScore" id="yearScore" size="8" maxlength="6" value="">
    </td>
  </tr>
	<tr>
		<td nowrap class="TableData">月考核平均分系数 ：</td>
   	<td class="TableData">
     	<input type="text" name="monthScore" id="monthScore" size="8" maxlength="6" value="">
    </td>
  </tr>
	<tr>
		<td nowrap class="TableData">处长主观分系数 ：</td>
   	<td class="TableData">
     	<input type="text" name="chiefScore" id="chiefScore" size="8" maxlength="6" value="">
    </td>
  </tr>
	<tr>
		<td nowrap class="TableData">考勤分数（系统自动算出）系数 ：</td>
   	<td class="TableData">
     	<input type="text" name="checkScore" id="checkScore" size="8" maxlength="6" value="">
    </td>
  </tr>
	<tr>
		<td nowrap class="TableData">奖惩分系数 ：</td>
   	<td class="TableData">
     	<input type="text" name="awardScore" id="awardScore" size="8" maxlength="6" value="">
    </td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
      <input type="button" value="保存设置" onclick="doSubmit();" class="BigButton">
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
      <div class="content" style="font-size:12pt">系数设置成功！</div>
    </td>
  </tr>
</table>
<br><center>
	<input type="button" value="返回" class="BigButton" onClick="backFuncs();">	
</center>
</div>

</body>
</html>
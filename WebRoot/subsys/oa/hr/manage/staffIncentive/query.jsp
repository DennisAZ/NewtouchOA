<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>奖惩信息查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffIncentive/js/staffIncentiveLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getSecretFlag("HR_STAFF_INCENTIVE1","incentiveItem");
	setDate();
}

function setDate(){
//日期
var date1Parameters = {
   inputId:'incentiveTime1',
   property:{isHaveTime:false}
   ,bindToBtn:'date1'
};
new Calendar(date1Parameters);
var date2Parameters = {
   inputId:'incentiveTime2',
   property:{isHaveTime:false}
   ,bindToBtn:'date2'
};
new Calendar(date2Parameters);
}
function doSubmit(){
	if(checkDate()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/oa/hr/manage/staffIncentive/search.jsp?" + query
	}
}
function checkDate(){
	var leaveDate1 = document.getElementById("incentiveTime1"); 
	var leaveDate2 = document.getElementById("incentiveTime2"); 
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
	if(leaveDate1.value >= leaveDate2.value){ 
		alert("开始日期不能大于结束日期！"); 
		leaveDate1.select(); 
		leaveDate1.focus(); 
		return (false); 
	}
	return true;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 奖惩信息查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData">单位员工：</td>
      <td class="TableData">
        <input type="hidden" name="staffName" id="staffName" value="">
         <input type="text" name="staffNameDesc" id="staffNameDesc" size="10" class="BigStatic" readonly value="">&nbsp;
	      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['staffName', 'staffNameDesc'],null,null,1);">选择</a>
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData"> 奖惩日期：</td>
      <td class="TableData">
        <input type="text" name="incentiveTime1" id="incentiveTime1" size="10" maxlength="10" class="BigInput" value="">
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >&nbsp;至&nbsp;
        <input type="text" name="incentiveTime2" id="incentiveTime2" size="10" maxlength="10" class="BigInput" value="">
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
             
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 奖惩项目：</td>
      <td class="TableData" >
        <select name="incentiveItem" id="incentiveItem" title="奖惩项目名称可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">请选择</option>
        </select>
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData">奖惩属性：</td>
      <td class="TableData">
        <select name="incentiveType" id="incentiveType">
        	<option value="">请选择</option>
          <option value="1">奖励</option>
          <option value="2">惩罚</option>
        </select>
      </td> 
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</body>
</html>
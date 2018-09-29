<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
		seqId="0";
}
  String curDate = YHUtility.getCurDateTimeStr();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>全局搜索</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />

<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>

<script type="text/javascript">
//alert("<%=seqId%>");
var seqId='<%=seqId%>';
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";

function checkForm(){
	if($("SUBJECT").value=="" && $("CONTENT_NO").value=="" && $("ATTACHMENT_DESC").value=="" && $("ATTACHMENT_NAME").value=="" && $("sendTime_Min").value=="" && $("sendTime_Max").value==""){
		alert("请指定至少一个查询条件！");
		return false;
	}
	if(checkDate() == false ){
		return false;
	}
	return true;
}

function sendForm(){
	if(checkForm()){
		$("form1").submit();
	}
}

function checkDate2(){
	var leaveDate1 = document.getElementById("sendTime_Min"); 
	var leaveDate2 = document.getElementById("sendTime_Max"); 
	var leaveDate1Array = leaveDate1.value.trim().split(" "); 
	var leaveDate2Array = leaveDate2.value.trim().split(" "); 
	var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
	var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
	var re1 = new RegExp(type1); 
	var re2 = new RegExp(type2); 
	if(leaveDate1.value == "" || leaveDate2.value == ""){
		return true;
	}else{
		if(leaveDate1Array.length!=2){ 
			alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate1.focus(); 
			leaveDate1.select(); 
			return false; 
		}else{ 
			if(!isValidDateStr(leaveDate1Array[0])||leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){ 
				alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
				leaveDate1.focus(); 
				leaveDate1.select(); 
				return false; 
			} 
		} 
		if(leaveDate2Array.length!=2){ 
			alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate1.focus(); 
			leaveDate1.select(); 
			return false; 
		}else{ 
			if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
				alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
				leaveDate2.focus(); 
				leaveDate2.select(); 
				return false; 
			} 
		} 
		if(leaveDate1.value >= leaveDate2.value){ 
			alert("开始时间不能大于结束时间！"); 
			leaveDate1.focus(); 
			leaveDate1.select(); 
			return (false); 
		}
		return true;
	}
}
function checkDate(){
	var leaveDate1 = document.getElementById("sendTime_Min"); 
	var leaveDate2 = document.getElementById("sendTime_Max"); 
	var leaveDate1Array = leaveDate1.value.trim().split(" "); 
	var leaveDate2Array = leaveDate2.value.trim().split(" "); 
	var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
	var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
	var re1 = new RegExp(type1); 
	var re2 = new RegExp(type2); 
	if(leaveDate1.value){
		if(leaveDate1Array.length!=2){ 
			alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate1.focus(); 
			leaveDate1.select(); 
			return false; 
		}
		if(!isValidDateStr(leaveDate1Array[0])||leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){
			alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate1.focus(); 
			leaveDate1.select(); 
			return false; 
		}
	}
	if(leaveDate2.value){
		if(leaveDate2Array.length!=2){ 
			alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate2.focus(); 
			leaveDate2.select(); 
			return false;
		}
		if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){
			alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate2.focus(); 
			leaveDate2.select(); 
			return false; 
		}
	}
	if(leaveDate1.value && leaveDate2.value ){
		if(leaveDate1Array.length!=2){ 
			alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate1.focus(); 
			leaveDate1.select(); 
			return false; 
		}
		if(leaveDate2Array.length!=2){ 
			alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate2.focus(); 
			leaveDate2.select(); 
			return false;
		}
		if(!isValidDateStr(leaveDate1Array[0])||leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){
			alert("开始时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate1.focus(); 
			leaveDate1.select(); 
			return false; 
		}
		if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){
			alert("结束时间格式不对，应形如 1999-01-01 12:12:12"); 
			leaveDate2.focus(); 
			leaveDate2.select(); 
			return false; 
		}
		if(leaveDate1.value >= leaveDate2.value){ 
			alert("开始时间不能大于结束时间！"); 
			leaveDate1.select(); 
			leaveDate1.focus(); 
			return (false); 
		}
	}
	return true;
}





function setDate(){
//日期
var date1Parameters = {
   inputId:'sendTime_Min',
   property:{isHaveTime:true}
   ,bindToBtn:'date1'
};
new Calendar(date1Parameters);
var date2Parameters = {
   inputId:'sendTime_Max',
   property:{isHaveTime:true}
   ,bindToBtn:'date2'
};
new Calendar(date2Parameters);

}


function doInit(){
	$("SUBJECT").focus();
	setDate();
}

</script>
</head>

<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_search.gif" align="middle"><b>&nbsp;<span class="Big1" id="folderName">全局搜索</span></b><br>
    </td>
  </tr>
</table>

<br>

 
<form action="<%=contextPath %>/core/funcs/filefolder/globalSearch.jsp" name="form1" id="form1"  >
<table class="TableBlock" width="450" align="center">
  <tr class="TableData">
      <td nowrap align="center">标题包含文字：</td>
      <td nowrap><input type="text" name="SUBJECT" id="SUBJECT" class="BigInput" size="20"></td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">排序号：</td>
      <td nowrap><input type="text" name="CONTENT_NO" id="CONTENT_NO" class="BigInput" size="10"></td>
  </tr>
  <tr>
    <td nowrap class="TableData" align="center">内容[关键词1]：</td>
    <td class="TableData"><input type="text" name="KEY1" id="KEY1" class="BigInput" size="20"></td>
  </tr>
  <tr>
    <td nowrap class="TableData" align="center">内容[关键词2]：</td>
    <td class="TableData"><input type="text" name="KEY2" id="KEY2" class="BigInput" size="20"></td>
  </tr>
  <tr>
    <td nowrap class="TableData" align="center">内容[关键词3]：</td>
    <td class="TableData"><input type="text" name="KEY3" id="KEY3" class="BigInput" size="20"></td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">附件说明包含文字：</td>
      <td nowrap><input type="text" name="ATTACHMENT_DESC" id="ATTACHMENT_DESC" class="BigInput" size="20"></td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">附件文件名包含文字：</td>
      <td nowrap><input type="text" name="ATTACHMENT_NAME" id="ATTACHMENT_NAME" class="BigInput" size="20"></td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">附件内容包含文字：</td>
      <td nowrap><input type="text" name="ATTACHMENT_DATA" class="BigInput" size="20">&nbsp;仅限txt和html文件 </td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">日期：</td>
      <td nowrap>
        <input type="text" name="sendTime_Min" id="sendTime_Min" size="19" maxlength="19" class="BigInput" value="">

        <img id="date1" align="middle" src="<%=contextPath %>/core/funcs/filefolder/images/calendar.gif" align="middle" border="0" style="cursor:pointer" >&nbsp;至&nbsp;

        <input type="text" name="sendTime_Max" id="sendTime_Max" size="20" maxlength="19" class="BigInput" value="<%=curDate %>">
        <img id="date2" align="middle" src="<%=contextPath %>/core/funcs/filefolder/images/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
  </tr>
  <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="hidden" name="seqId" id ="seqId" value="<%=seqId %>">
          <input type="hidden" name="FILE_SORT" value="1">
          <input type="button" value="搜索" onclick="sendForm();" class="BigButton" title="进行文件搜索">&nbsp;&nbsp;
      </td>
  </tr>
</table>
</form>



</body>
</html>
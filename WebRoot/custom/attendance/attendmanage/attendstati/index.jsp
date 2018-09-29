<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String userId = request.getParameter("userId");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");
  Date curDate = new Date();
  String dateStr = dateFormat.format(curDate);
  String dateStr1 = dateFormat1.format(curDate);
  String dateStr2 = dateFormat2.format(curDate);
  dateStr2 = dateStr2 + "-01";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人员考勤记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function CheckForm(){
  var date1 = document.getElementById("date1");
  var date2 = document.getElementById("date2");
  if(date1.value==""){ 
    alert("起始日期不能为空！");
    date1.focus();
    date1.select();
    return (false);
  }
  if(date2.value==""){ 
    alert("截止日期不能为空！");
    date2.focus();
    date2.select();
    return (false);
  }
  if(!isValidDateStr(date1.value)){
    alert("起始日期格式不对,应形如 2010-02-01");
    date1.focus();
    date1.select();
    return false;
  }
  if(!isValidDateStr(date2.value)){
    alert("截止日期格式不对,应形如 2010-02-01");
    date2.focus();
    date2.select();
    return false;
  }
  var beginInt;
  var endInt;
  var beginArray = date1.value.split("-");
  var endArray = date2.value.split("-");
  for(var i = 0 ; i<beginArray.length; i++){
    beginInt = parseInt(" " + beginArray[i]+ "",10);  
    endInt = parseInt(" " + endArray[i]+ "",10);
    if((beginInt - endInt) > 0){
      alert("起始日期不能大于截止日期!");
      date2.focus();
      date2.select();
      return false;
    }else if(beginInt - endInt<0){
      return true;
    }  
  }
   return (true);
}
function doOnload(){
  var userId = '<%=userId%>';
  //按用户的管理权限得到所有部门

  var deparement = document.getElementById("deparement").value;
  dept(deparement);

  //初始化日期

  var date1Parameters = {
      inputId:'date1',
      property:{isHaveTime:false}
      ,bindToBtn:'dateImg1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'date2',
      property:{isHaveTime:false}
      ,bindToBtn:'dateImg2'
  };
  new Calendar(date2Parameters);
}
//查询部门
function dept(deptId){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptToAttendance.act?deptId="+deptId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId_priv = rtJson.rtMsrg;
  var userId = userId_priv.split(",")[0];
  var priv = userId_priv.split(",")[1];
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deparement");
  if(priv=='1'){
    var optionAll = document.createElement("option"); 
    optionAll.value = '0'; 
    optionAll.innerHTML = '所有部门'; 
    selects.appendChild(optionAll);
  }
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

function set_date(str){    
  document.getElementById("date1").value = str;
}
function Init(){
  var userId = '<%=userId%>';
  var deptId = document.getElementById("deparement").value;
  var beginTime = document.getElementById("date1").value;
  var endTime = document.getElementById("date2").value;
  if(CheckForm()){
    window.location.href = "<%=contextPath%>/custom/attendance/attendmanage/attendstati/search.jsp?userId="+userId+"&beginTime="+beginTime+"&endTime="+endTime+"&deptId="+deptId;
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">

<table border="0" width="100%" cellspacing="0" cellpadding="3"
	class="small">
	<tr>
		<td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22"
			HEIGHT="20" align="absmiddle"><span class="big3"> 考勤统计</span><br>
		</td>
	</tr>
</table>
<div align="center">
<form action="search.php" name="form1" onsubmit="return CheckForm1();">
<table  class="TableList" width=450>

	<tr class=TableHeader>
		<td colspan=2>考勤统计</td>
	</tr>
	<tr>
		<td class="TableContent">部门</td>
		<td class="TableData" align="left"><select id="deparement" name="deparement"
			style="height: 22px; FONT-SIZE: 12pt;">
		</select></td>
		</tr>
	<tr style="display:none"> 
		<td class="TableContent">人员</td>
    <td class="TableData" align="left" >
	    <input type="hidden" name="userId" id="userId" value=""  />
      <textarea name="userIdDesc" id="userIdDesc"  rows="2" cols="30" readonly="readonly" ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId','userIdDesc']);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userIdDesc').value='';">清空</a>		
      </td>
	</tr>
	<tr>
		<td class="TableContent">起始日期</td>
		<td class="TableData" align="left"><input type="text" id="date1" name="date1"
			class="BigInput" size="10" maxlength="10" value="<%=dateStr2 %>">
		<img id="dateImg1" src="<%=imgPath%>/calendar.gif" align="absMiddle"
			border="0" style="cursor: hand"> <a
			href="javascript:set_date('<%=dateStr1 %>')">设为今日</a></td>
	</tr>
	<tr>
		<td class="TableContent">截止日期</td>
		<td class="TableData" align="left"><input type="text" id="date2" name="date2"
			class="BigInput" size="10" maxlength="10" value="<%=dateStr1 %>">
		<img id="dateImg2" src="<%=imgPath%>/calendar.gif" align="absMiddle"
			border="0" style="cursor: hand"><br>
		</td>
	</tr>
	<tr class="TableControl">
		<td colspan=2 align=center><input type="button" value="考勤统计"
			class="BigButton" title="考勤统计" onclick="Init();"></td>
	</tr>
</table>
</form>

</div>

</body>
</html>
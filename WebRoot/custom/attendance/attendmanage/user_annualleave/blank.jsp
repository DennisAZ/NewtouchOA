<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String userId = request.getParameter("userId");
  if(userId==null){
    userId = "";
  }
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
<html class="hiddenRoll">
<head>
<title>人员考勤年休假设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function CheckForm(){
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt2 =   "^\\d+$";//整数
  var re2 = new RegExp(IsInt2);
  if(($("annualDays").value).match(re2)==null){
     alert("天数应为整数！");
     $("annualDays").focus();
     $("annualDays").select();
     return false ;      
  }
   var changeDate = document.getElementById("changeDate");
   if(changeDate.value!=''&&!isValidDateStr(changeDate.value)){
     alert("起始日期格式不对,应形如 2010-02-01");
     changeDate.focus();
     changeDate.select();
     return false;
   }
   
   return (true);
}
var userId = '<%=userId%>';
function doOnload(){

  if(userId==''){
    document.getElementById("nullList").style.display = "";
    document.getElementById("attendanDiv").style.display = "none";
    document.getElementById("type").style.display = "none";
  }else{
    document.getElementById("nullList").style.display = "none";
    document.getElementById("attendanDiv").style.display = "";
    document.getElementById("type").style.display = "none";
    //得到用户的信息
    selectUserInfo(userId);
    //查询用户年休假信息
    selectAnnualUserInfo(userId);
  }
  
  //初始化时间  var date1Parameters = {
      inputId:'changeDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date'
  };
  new Calendar(date1Parameters);
}
function selectUserInfo(userId){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectUserInfo.act?userId="+userId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var userName = prc.userName;
  var annualLeave = prc.annualLeave;
  document.getElementById("userName").innerHTML = userName;
  //document.getElementById("annualLeave").innerHTML = annualLeave; 
}
function selectAnnualUserInfo(userId){
  var URL = "<%=contextPath%>/yh/custom/attendance/act/YHPersonAnnualParaAct/selectAnnualLeavePara.act?userId="+userId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var annualSumDays = rtJson.rtMsrg;
  $("annualSumDays").innerHTML = annualSumDays;
  var prc = rtJson.rtData;
  if(prc.seqId){
    $("annualDays").value = prc.annualDays;
    $("changeDate").value = prc.changeDate.substring(0,10);
    $("annual").innerHTML = (parseInt(prc.annualDays,10)-parseInt(annualSumDays,10));
  }
}

//得到整数时间秒HH:mm:ss
function strInt(str){
  var min = 0;
  var max = 24*3600;
  var strInt;
  var strInt1;
  var strInt2;
  var strArray = str.split(":");
  for(var i = 0 ; i<strArray.length; i++){
    if(i==0){
      strInt1 = parseInt(strArray[i]*3600,10);
    }else if(i==1){
      strInt2 = parseInt(strArray[i]*60,10); 
    }  
  }
  strInt = strInt1+strInt2+parseInt(strArray[2]);
  return strInt;
}
function doInit(){
  if(CheckForm()){
    var URL = "<%=contextPath %>/yh/custom/attendance/act/YHPersonAnnualParaAct/addUpdateAnnualLeavePara.act?userId="+userId;
    var rtJson = getJsonRs(URL,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    document.getElementById("nullList").style.display = "none";
    document.getElementById("attendanDiv").style.display = "none";
    document.getElementById("type").style.display = "";
  }
}
 
</script>
</head>

<body class="" topmargin="5" onload="doOnload();">
<div id="attendanDiv"  style="display: none">
<table class="MessageBox" align="center" width="500">
	<tr>
		<td class="msg info">
		<div class="content" style="font-size: 12pt"><span id="userName"></span>本年度已请假<span id="annualSumDays"></span>天，年休假剩余<font
			color="red" id="annual">0</font>天</div>
		</td>
	</tr>
</table>

<br></br>
<form action="#"  method="post" onsubmit="return CheckForm()" id="form1">
<table align="center" class="TableList" width=650>
	<tr class=TableHeader>
		<td colspan=2>年休假天数设置</td>
	</tr>
	<tr>
		<td nowrap class=TableData>天数：<input type="text" name="annualDays" id="annualDays" value="0" class="BigInput" size="10" maxlength="9"></td>
		<td nowrap class=TableData>下次调整时间：<input type="text"
			name="changeDate" id="changeDate" class="BigInput" size="10"
			maxlength="10" value="<%=dateStr1 %>"> <img id="date"
			src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0"
			style="cursor: hand"></td>
	</tr>
	<tr>
		<td nowrap class=TableData align="center" colspan="2">
		<input type="button" value="确定" class="BigButton" onclick="doInit();"></input>
		<input type="hidden" id="userId" name="userId" value="<%=userId %>"></input>
		</td>
	</tr>
</table>
</form>
</div>
<div id="nullList">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
	class="small">
	<tr>
		<td class="Big"><img src="<%=imgPath%>/hrms.gif" HEIGHT="20"><span
			class="big3"> 人员考勤年休假设定</span></td>
	</tr>
</table>
<br>

<table class="MessageBox" align="center" width="240">
	<tr>
		<td class="msg info">
		<h4 class="title">提示</h4>
		<div class="content" style="font-size: 12pt">请选择人员</div>
		</td>
	</tr>
</table>
</div>
<div id="type" style="display: none">
<table class="MessageBox" align="center" width="240">
	<tr>
		<td class="msg info">
		<h4 class="title">提示</h4>
		<div class="content" style="font-size: 12pt">保存成功！</div>
		</td>
	</tr>
</table>
<div align="center"><input type="button" value="返回" class="BigButton" onclick="window.location.reload();"></div>
</div>
</body>
</html>


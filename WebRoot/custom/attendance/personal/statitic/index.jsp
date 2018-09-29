<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>本月考勤统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doOnload(){
  var requestUrl = "<%=contextPath%>/yh/custom/attendance/act/YHAttendManageAct/selectPersonAttend.act";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  //alert(rsText);
  var prc = rtJson.rtData;
  //得到用户的名称
  var userName = prc.userName;
  $("userName").innerHTML = userName;
  //加班统计
  var overtimeTotal = prc.overtimeTotal;
  var overtimeTotal = getStr2(overtimeTotal);
  $("overtimeTotal").innerHTML = overtimeTotal;
  //请假统计时间
  var leaveTotal = prc.leaveTotal;
  var leaveTotal = getStr2(leaveTotal);
  $("leaveTotal").innerHTML = leaveTotal;
  //加班时长-请假时长
  var overtime_leave = prc.overtime_leave;
  var overtime_leave_opt = "";
  if(overtime_leave.substring(0,1)=='-'){
    overtime_leave = overtime_leave.substring(1,overtime_leave.length);
    overtime_leave_opt = "-";
  }
  var overtime_leave = getStr2(overtime_leave);
  $("overtime_leave").innerHTML = overtime_leave_opt + overtime_leave;
}


//计算时长天——时——分
function getStr2(str){
  var day = parseInt(str/(24*60),10);
  var hour = parseInt((str-(day*24*60))/60,10);
  var minute = parseInt((str -(day*24*60) - (hour*60)),10);
  var str = "";
  if(day>0){
    str = str + day + "天";
  }
  if(day>0&&minute>0&&hour==0){
    str = str + hour+"小时";
  }
  if(hour>0){
    str = str + hour+"小时";
  }
  if(minute>0){
    str =  str + minute + "分";
  }
  return str;
 }

</script>
<head>
<title>上下班登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
 
<body topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  本月个人考勤统计<span id="timetable"></span></span><br>
    </td>
  </tr>
</table>
<br>
<table class="TableList" width="95%">
    <tr class="TableHeader">
      <td nowrap align="center">员工</td>
      <td nowrap align="center">累计加班时长</td>
      <td nowrap align="center">累计请假时长</td>
      <td nowrap align="center">加班时长-请假时长</td>
    </tr>
 <tr >
   <td nowrap align="center" id="userName"></td>
   <td nowrap align="center" id="overtimeTotal"></td>
   <td nowrap align="center" id="leaveTotal"></td>
   <td nowrap align="center" id="overtime_leave"></td>
 </tr>
</table>
 
</body>
</html> 
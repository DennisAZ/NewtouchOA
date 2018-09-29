<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("userId");
  String deptId = request.getParameter("deptId");
  String beginTime = request.getParameter("beginTime");
  String endTime = request.getParameter("endTime");
  String dutyType = request.getParameter("dutyType");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
  String beginTimeStr = dateFormat2.format(dateFormat1.parse(beginTime));
  String endTimeStr = dateFormat2.format(dateFormat1.parse(endTime));
  //相隔多少天

  long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginTime),dateFormat1.parse(endTime))+1;
  //得到到之间的天数数组
  List daysList = new ArrayList();
  String days = "";
  Calendar calendar = new GregorianCalendar();
  for(int i = 0;i<daySpace;i++){
    calendar.setTime(dateFormat1.parse(beginTime));
    calendar.add(Calendar.DATE,+i) ;
    Date dateTemp = calendar.getTime();
    String dateTempStr = dateFormat1.format(dateTemp);
    daysList.add(dateTempStr);
    days = days + dateTempStr + ",";
  }
  if(daySpace>0){
    days = days.substring(0,days.length()-1);
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="hiddenRoll">
<head>
<title>上下班登记修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" >
function doOnload(){
  var userId = '<%=userId%>';
  var deptId = '<%=deptId%>';
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>';
  var dutyType = '<%=dutyType%>';
  var days = '<%=days%>';
  days = days.split(",");
  //得到部门的Id 可能还有子部门的所有 人员
  var userIds = selectUserIds(userId,deptId);
  //alert(userIds);
  getAttendInfo(userIds,beginTime,endTime);
}
//得到部门的Id 可能还有子部门的所有 人员
function selectUserIds(userId,deptId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectUserIds.act?userId="+userId+"&deptId="+deptId;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  $("userIds").value = prc.userIds;
  return prc.userIds;
}

//根据用户 得到加班和请假的时长
function getAttendInfo(userIds,beginDate,endDate){
  var requestURL = "<%=contextPath%>/yh/custom/attendance/act/YHAttendManageAct/selectAttendByUserDate.act?userIds="+userIds+"&beginDate="+beginDate+"&endDate="+endDate;
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  var trTotalStr = "";
  for(var i = 0; i<prcs.length;i++){
    var prc = prcs[i];
    var deptName = prc.deptName;
    var userName = prc.userName;
    var overtimeTotal = prc.overtimeTotal;
    var leaveTotal = prc.leaveTotal;
    var overtime_leave = prc.overtime_leave;
    var overtime_leave_opt ="";
    if(overtime_leave.substring(0,1)=='-'){
      overtime_leave = overtime_leave.substring(1,overtime_leave.length);
      overtime_leave_opt = "-";
    }
    overtimeTotal = getStr2(overtimeTotal);
    overtime_leave = getStr2(overtime_leave);
    leaveTotal = getStr2(leaveTotal);
    newTrElement( $("userTable"),[{className:"TableLine1"}],5,[deptName,userName,overtimeTotal,leaveTotal,overtime_leave_opt+ overtime_leave]);
  }
}
function newTrElement(tbObj,tbAttri,index,config){
  var currentRows = tbObj.rows.length;//原来的行数
  var mynewrow = tbObj.insertRow(currentRows);

  for(var i = 0; i<tbAttri.length ; i ++){
    if(tbAttri[i].className){
      mynewrow.className = tbAttri[i].className;
    }
    if(tbAttri[i].width){
      mynewrow.width = tbAttri[i].width;
    }
  }
  for(var i = 0 ; i<index ;i++){
    var currentCells = mynewrow.cells.length;
    var mynewcell=mynewrow.insertCell(currentCells);
    mynewcell.align = 'center';
    mynewcell.nowrap = true;
    mynewcell.innerHTML = config[i];
  }
}

function returnBefore(){
  window.location.href = "<%=contextPath%>/custom/attendance/attendmanage/attendstati/index.jsp";
}
function exprotAttend(){
  var beginTime = '<%=beginTime%>';
  var endTime = '<%=endTime%>'; 
  var userIds = $("userIds").value;
  var dutyType = '<%=dutyType%>';
  var days = '<%=days%>';
  window.location.href = "<%=contextPath%>/yh/custom/attendance/act/YHAttendManageAct/exprotAttendExl.act?userId="+userIds+"&beginTime="+beginTime+"&endTime="+endTime+"&days="+days;
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
</head>

<body class="" topmargin="5" onload="doOnload();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 上下班统计（从<%=beginTimeStr %>至<%=endTimeStr %>共<%=daySpace %>天）
    </span>
    <input type="button" value="导出Excel" class="BigButtonW" onclick="exprotAttend();"  title="导出统计信息" name="button">
    </td>
  </tr>
</table>

<table class="TableList" width="95%"  id="userTable" >
 <tbody id="userTbody">
  <tr class="TableHeader">
    <td nowrap align="center">部门</td>
    <td nowrap align="center" >姓名</td>
    <td nowrap align="center">累计加班时长</td>
    <td nowrap align="center">累计请假时长</td>
    <td nowrap align="center">加班时长-请假时长</td>
  </tr>
  </tbody>
</table>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<div align="center">
  <input type="hidden" id="userIds" name = "userIds" value=""></input>
  <input type="button"  value="返回" class="BigButton" onClick="returnBefore();">
</div>

</body>
</html>
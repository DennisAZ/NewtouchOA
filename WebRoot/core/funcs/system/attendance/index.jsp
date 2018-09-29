<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考勤管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function dutyType(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/attendtype.jsp";
}
function duty_interval(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/attendinterval.jsp";
}
function no_duty(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/noduty.jsp";
}
function holiday(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/holiday.jsp";
}
function manager(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/manager.jsp";
}
function deleteData(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/deletedata.jsp";
}
function Test(){
  window.location.href="<%=contextPath%>/core/funcs/system/attendance/test.jsp";
}
</script>
</head>
<body class="" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;上下班考勤登记设置</span><br>
    </td>
  </tr>
</table>
<br>
<input type="button"  value="排班类型" class="BigButton" onClick="dutyType();">&nbsp;&nbsp;&nbsp;
<input type="button"  value="登记时间段" class="BigButton" onClick="duty_interval();">&nbsp;&nbsp;&nbsp;
<input type="button"  value="免签人员" class="BigButton" onClick="no_duty();">&nbsp;&nbsp;&nbsp;
<input type="button"  value="免签节假日" class="BigButton" onClick="holiday();">
<br>
<br>
<br>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;考勤数据管理</span><br>
    </td>
  </tr>
</table>
<br>
<input type="button"  value="删除考勤数据" class="BigButtonC" onClick="deleteData();">
<br>
<br>
<br>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;考勤审批权限</span><br>
    </td>
  </tr>
</table>
<br>
<input type="button"  value="考勤审批人员" class="BigButtonC" onClick="manager();">
<br>
<br>
<br>
</html>
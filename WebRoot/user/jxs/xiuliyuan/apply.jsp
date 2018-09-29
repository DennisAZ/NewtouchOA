<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
String date=curTime.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审批通知单</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>

<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript">

function doInit(){


}

</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">申领器材&nbsp;&nbsp;    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1" >
<table class="TableList" align="center">
    <tr>
    <td nowrap class="TableData">装备名称：</td>
     <td class="TableContent" >
   <input value="" type="text">
      </td>
  </tr>   <tr>
      <td nowrap class="TableData">装备编号：</td>
     <td class="TableContent" >
 <input value="" type="text">
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">物理级别：</td>
     <td class="TableContent" colspan="1">
    <select>
   <option value="0">小修</option>
     <option value="1">中修</option>
       <option value="2">大修</option>
    </select>
     </td>
     </tr>
     <tr>
    <td nowrap class="TableData">修理人员：</td>
     <td class="TableContent" colspan="1">
     修理人员1
     </td>
     </tr>
      <tr>
    <td nowrap class="TableData">申请数量：</td>
     <td class="TableContent" colspan="1">
     <input type="text" size=3></input>
     </td>
     </tr>
     <tr>
    <td nowrap class="TableData">损坏器材名称：</td>
     <td class="TableContent" colspan="1">
     <input value="" type="text">
     </td>
     </tr><tr>
    <td nowrap class="TableData">更换原因：</td>
     <td class="TableContent" colspan="1">
     <input value="" type="text">
     </td>
     </tr>
     <tr>
    <td nowrap class="TableData">申领时间：</td>
     <td class="TableContent" colspan="1">
     <input value="<%=date %>" type="text">
     </td>
     </tr>
     <tr>
     <td class="TableControl" align="center" colspan="2">
     <input value="申领" onclick="window.close()" type="button">
     </td>
     </tr>
     <tr>
     </tr>
</table>
</form>
</body>
</html>
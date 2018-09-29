<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>财务预算管理</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript">
function Test(num){
  myleft = (screen.availWidth-800)/2;
  if (num == '1') {
    var url = "<%=contextPath%>/subsys/oa/finance/budget/plan/description/北京市财政局关于调整北京市党政机关工作人员在公务活动中接待标准的通知.html";
    window.open(encodeURI(url),"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
  }
  if (num == '2') {
    var url = "<%=contextPath%>/subsys/oa/finance/budget/plan/description/北京市市级行政事业单位会议费管理办法.html";
    window.open(encodeURI(url),"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
  }
  if (num == '3') {
    var url = "<%=contextPath%>/subsys/oa/finance/budget/plan/description/北京市财政局2004年非贸易外汇管理工作会议材料.html";
    window.open(encodeURI(url),"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
  }
  if (num == '4') {
    var url = "<%=contextPath%>/subsys/oa/finance/budget/plan/description/北京市市级党政机关日常办公设备配置标准.html";
    window.open(encodeURI(url),"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
  }
  if (num == '5') {
    var url = "<%=contextPath%>/subsys/oa/finance/budget/plan/description/北京市国家机关和事业单位差旅费管理办法.html";
    window.open(encodeURI(url),"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sys_config.gif" HEIGHT="20"><span class="big3"> 财务预算各种标准详细说明 </span>
    </td>
  </tr>
</table>
<table class="TableList" width="60%">
<tr class="TableHeader">
	<td align="center"><a href="#" onclick="Test('1');">北京市党政机关工作人员在公务活动中接待标准</a></td>
</tr>
<tr class="TableHeader">
	<td align="center"><a onclick="Test('2');" href="#">北京市市级行政事业单位会议费管理办法</a></td>
</tr>
<tr class="TableHeader">
	<td align="center"><a onclick="Test('3');" href="#">北京市财政局2004年非贸易外汇管理工作会议材料</a></td>
</tr>
<tr class="TableHeader">
	<td align="center"><a onclick="Test('4');" href="#">北京市市级党政机关日常办公设备配置标准</a></td>
</tr>
<tr class="TableHeader">
	<td align="center"><a onclick="Test('5');" href="#">北京市国家机关和事业单位差旅费管理办法</a></td>
</tr>
</table>
</body>
</html>
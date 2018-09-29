<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
String P_VER = (String)session.getAttribute("P_VER");
%>
<!doctype html>
<html>
<head>
<title>ps火星手机管理平台</title>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/pda/style/main.css"/>
<style type="text/css">
html, body {
	height: 100%;
}
</style>
<script type="text/javascript">
function doInit() {
  doHeight();
}
window.onresize = doHeight;
function doHeight() {
  var height = (document.body || document.documentELement).scrollHeight;
	var ctx = document.getElementById("main");
	var borderHeight = document.getElementById("main_top").scrollHeight + 11;
	var bottom = document.getElementById("main_bottom");
	borderHeight += (bottom ? bottom.scrollHeight : 0);
	if (ctx.scrollHeight < (height - borderHeight)) {
	  ctx.style.height = height - borderHeight + "px";
	}
}
function backTo(){
  var url="message:backtomain";   
  document.location = url;   
}
</script>
</head>
<body onload="doInit();">
<div id="main_top" class="product" style="font-size:24px;"></div>
<div id="main">
<table>
<tr>
  <td><a href="<%=contextPath %>/yh/pda/sms/act/YHPdaSmsAct/doint.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/sms.jpg" /><div>内部短信</div></a></td>
  <td><a href="<%=contextPath %>/yh/pda/email/act/YHPdaEmailAct/search.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/email.jpg" /><div>内部邮件</div></a></td>
  <td><a href="<%=contextPath %>/yh/pda/notify/act/YHPdaNotifyAct/search.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/notify.jpg" /><div>公告通知</div></a></td>
  <td><a href="<%=contextPath %>/yh/pda/news/act/YHPdaNewsAct/search.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/news.jpg" /><div>内部新闻</div></a></td>
</tr>
<tr>
  <td><a href="<%=contextPath %>/yh/pda/calendar/act/YHPdaCalendarAct/doint.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/calendar.jpg" /><div>今日日程</div></a></td>
  <td><a href="<%=contextPath %>/yh/pda/diary/act/YHPdaDiaryAct/doint.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/diary.jpg" /><div>工作日志</div></a></td>
  <td><a href="<%=contextPath %>/yh/pda/fileFolder/act/YHPdaFileFolderAct/search.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/folder.jpg" /><div>我的文件</div></a></td>
  <td><a href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/search.act?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/workflow.jpg" /><div>工作流</div></a></td>
</tr>
<tr>
  <td><a href="<%=contextPath %>/pda/userInfo?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/query.jpg" /><div>人员查询</div></a></td>
  <td><a href="<%=contextPath %>/pda/address?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/address.jpg" /><div>通讯簿</div></a></td>
  <td><a href="<%=contextPath %>/pda/telNo?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/zipcode.jpg" /><div>区号邮编</div></a></td>
  <td><a href="<%=contextPath %>/pda/weather?P=<%= loginPerson.getSeqId()%>"><img src="<%=contextPath %>/pda/style/images/icon/weather.jpg" /><div>天气预报</div></a></td>
</tr>

</table>
</div>
<div id="main_bottom">
	<div class="user_name">用户：<%=loginPerson.getUserName() %></div>
   <%if("5".equals(P_VER)){ %>
   	 <a class="relogin" href="javascript:void(0);" onclick="backTo();"><img src="<%=contextPath %>/pda/style/images/relogin.jpg" alt="重新登录" /></a>
   <%} 
     else if(!"6".equals(P_VER)){ %>
   	 <a class="relogin" href="<%=contextPath %>/pda/index.jsp"><img src="<%=contextPath %>/pda/style/images/relogin.jpg" alt="重新登录" /></a>
   <%} %>
</div>
</body>
</html>

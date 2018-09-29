<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String uid = request.getParameter("uid");
	if(uid == null){
	  uid = "";
	}
  String userName = request.getParameter("userName");
  //userName = new String(userName.getBytes("iso-8859-1"), "utf-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript"> 
  var uid= "<%=uid%>";
  var userName= "<%=userName%>";
  var jsArray = [['在线人员','','','','','','',''],
               ['全部人员','','','','','','',''],
               ['日程安排查询','','','','','','',''],
               ['工作日志查询','','','','','','',''],
               ['公告通知发布','','','','','','',''],
               ['新闻发布','','','','','','',''],
               ['投票发布','','','','','','',''],
               ['管理简报','','','','','','',''],
               ['人事档案管理','','','','','','',''],
               ['人事档案查询','','','','','','',''],
               ['发送邮件','','','','','','',''],
               ['发送短信','','','','','','',''],
               ['人力资源统计','','','','','','',''],
               ['薪酬统计','','','','','','','']];
  function init() {
	  var ul = document.getElementById("privDiv");
	  var templiall = '';
	  for(var i = 0;i < jsArray.length;i++) {
		  var templi = '<li><a href="javascript:clickMenu('+i+');"  title='+jsArray[i][4]+' id='+i+'><span>'+jsArray[i][0]+'</span></a></li>';
		  templiall += templi;
		  ul.innerHTML='<ul>'+templiall+'</ul>';
	  }     
  }
  function clickMenu(i) {
	  var tempid = i+1;
      window.parent.user_main.location='<%=contextPath %>/yh/core/funcs/modulepriv/act/YHModuleprivAct/beforepriv.act?id='+tempid+'&uid=<%=uid%>&userName=<%=userName%>';
  }

</script>

</head>
<body onload="init();">
  <div id="privDiv">
  
  </div>
</body>
</html>

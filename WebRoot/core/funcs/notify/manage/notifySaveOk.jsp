<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ include file="/core/inc/header.jsp" %>
  <%
  String publish = request.getParameter("publish");
  if (publish == null) {
    publish = "";
  }
  %>
   
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>

<title>发送提示</title>

<script type="text/javascript">
var publish = '<%=publish%>';
var url = null;
function doInit() {
//	$('msgInfo').update(msg);
 if(publish == '1'){
   $('msg').update('公告通知发布成功');
   url = '/yh/core/funcs/notify/manage/notifyList.jsp';
 }
 if(publish == '2'){
	 $('msg').update('公告通知提交审批成功');
	  url = '/yh/core/funcs/notify/manage/notifyList.jsp';
 }
 if(publish == '0'){
	 $('msg').update('公告通知保存成功');
	  url = '/yh/core/funcs/notify/manage/notifyAdd.jsp';
 }
 
}

function to0back() {	
  if (top.dispParts) {
	  //top.dispParts("<%=contextPath%>/core/funcs/notify/manage/index.jsp");
	  window.location.href = "<%=contextPath%>/core/funcs/notify/manage/notifyAdd.jsp";
  }else {
    window.close();
  }
}
</script>
</head>
<body onload="doInit()">
 <TABLE class=MessageBox width=300 align=center>
<TBODY>
<TR>
<TD id="msgInfo" class="msg info">
<H4 class=title>提示</H4>
<DIV class=content id="msg"
style="FONT-SIZE: 12pt">公告通知保存成功！</DIV></TD></TR></TBODY></TABLE><BR>
<DIV align=center>
<INPUT class=BigButton onclick="to0back();" type=button value=返回> 
</DIV>
</body>
</html>
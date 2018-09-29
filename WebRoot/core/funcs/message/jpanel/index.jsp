<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String type = request.getParameter("type");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<title>Insert title here</title>
</head>
<script type="text/javascript">
var type = "<%=type%>";
function doInit(){
  if(type == "1"){
   location = "<%=contextPath %>/core/funcs/sms/jpanel/inboxSms.jsp?pageNo=0&pageSize=5";
  }else{
    location = "<%=contextPath %>/core/funcs/sms/jpanel/sentboxSms.jsp?pageNo=0&pageSize=5";
  }
}
</script>
<body onload="doInit()">

</body>
</html>
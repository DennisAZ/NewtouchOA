<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
  <%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
    seqId = "";
  }
  String userNameStr = request.getParameter("userNameStr");
  if (userNameStr == null) {
    userNameStr = "";
  }
  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>temp</title>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var userNameStr = "<%=userNameStr%>";

  function doInit() {
   var uid = seqId;
   var userName = userNameStr;
   var width=Math.round((window.screen.width-400)/2);
   var height=Math.round((window.screen.height-200)/2);
   window.open('<%=contextPath%>/core/funcs/modulepriv/index.jsp?uid='+uid+'&userName='+userName
		       , 'newwindow', 
		       'height=300, width=600, top='+height+', left='+width+', toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
   }
</script>
</head>
<body onload="doInit()">
  
</body>
</html>
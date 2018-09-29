<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
            <%@ include file="/core/inc/header.jsp" %>
    <%
    String msg = request.getParameter("msg") == null ? "" : request.getParameter("msg");  
    String sendFlag = request.getParameter("sendFlag") == null ? "" : request.getParameter("sendFlag");  

    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>Insert title here</title>
</head>
<script type="text/javascript">
var sendFlag = "<%=sendFlag%>";
function doInit(){
  var url = "";
  if(sendFlag == 1){
    url = contextPath + "/core/funcs/email/new/sendOk.jsp?msg=<%=msg%>";
   }else{
     url = contextPath + "/core/funcs/email/new/saveOk.jsp?msg=<%=msg%>";
   }
  location = url;
}
</script>
<body onload="doInit()">
</body>
</html>
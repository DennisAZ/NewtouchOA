<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  List<Map<String,String>> data = (List<Map<String,String>>)request.getAttribute("pageData");
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
function doInit() {
  var jso = [
       <% for(int i = 0 ; i < data.size(); i++){
          Map<String,String> m = data.get(i);
          %>
          <%if(i != 0){%>,<%}%>{title:"<%=m.get("text")%>", useTextContent:false , imgUrl:"<%=imgPath%>/sms/sms_type<%=m.get("img")%>.gif", contentUrl:"<%=contextPath %><%=m.get("url") %>" ,useIframe:true}
       <%}%>
     ];

  buildTab(jso, 'contentDiv', 800);
}
</script>
</head>
<body onload="doInit()">

</body>
</html>
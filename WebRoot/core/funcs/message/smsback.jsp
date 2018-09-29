<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null) {
    seqId = "";
  }
  
  String fromId = request.getParameter("fromId");
  if(fromId == null) {
    fromId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发微讯 </title>
<script type="text/javascript">
try {
	if(typeof (window.external.OA_SMS) != 'undefined') {
	  window.external.OA_SMS(700, 340, "SET_SIZE");
	  window.external.OA_SMS(document.title, "", "NAV_TITLE");
	}
} catch (e) {
  
} finally {
  
}
</script>
</head>
<body>
<iframe style="border: none;" src="<%=contextPath %>/core/funcs/message/smsBackIf.jsp?seqId=<%=seqId %>&fromId=<%=fromId %>" scrolling="no" width="700" height="350"></iframe>
</body>
</html>
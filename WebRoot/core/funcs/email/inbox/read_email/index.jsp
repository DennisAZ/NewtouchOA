<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>内部邮件</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/cmp/grid.css" />
<html>
<head>
<title>读邮件</title>
</head>
<% 
String boxId = request.getParameter("boxId") == null ? "0" : request.getParameter("boxId");
String mailId = request.getParameter("mailId") == null ? "" : request.getParameter("mailId");
String seqId = request.getParameter("seqId");
String isQuery = request.getParameter("isQuery");
if(isQuery == null){
  isQuery = "0";
}
String total = request.getParameter("total") == null ? "0" : request.getParameter("total");
String recordIndex = request.getParameter("recordIndex")  == null ? "0" : request.getParameter("recordIndex");
%>
<frameset rows="*,40" cols="*" frameborder="no" framespacing="0">
    <frame src="readMail.jsp?isQuery=<%=isQuery%>&pageStart=&seqId=<%=seqId%>&mailId=<%=mailId%>&boxId=<%=boxId%>&field=&ascDec=" name="mail_view" scrolling="auto">
    <frame src="control.jsp?isQuery=<%=isQuery%>&pageStart=&seqId=<%=seqId%>&boxId=<%=boxId%>&field=&ascDec=&total=<%=total%>&recordIndex=<%=recordIndex%>" name="mail_control" scrolling="no" FRAMEBORDER="no">
</frameset>

<noframes>
</html>
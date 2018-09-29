<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("sortId");
	if(seqId==null){
	  seqId="0";
	}
	//桌面传过来	String showFlag=request.getParameter("showFlag");
	if(showFlag==null){
		showFlag="";
	}
	String contentId=request.getParameter("contentId");
	if(contentId==null){
		contentId="0";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机要文件</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
</head>
<frameset rows="30,*"  cols="*" frameborder="0" framespacing="0" id="frame1">
    <frame name="file_title" scrolling="no" noresize src="menuTop.jsp" frameborder="NO">
    <frameset rows="*"  cols="200,*" frameborder="no" framespacing="0" name="frame2" id="frame2">
      <frame name="file_tree" scrolling="auto" src="left.jsp?seqId=<%=seqId %>" frameborder="yes">
     <%
   		if(showFlag!=null && "CONTENT".equals(showFlag)){
     %>
     		<frame name="file_main" scrolling="auto" src="read.jsp?sortId=<%=seqId %>&contentId=<%=contentId %>&showFlag=<%=showFlag %>" frameborder="yes">
     <%
   		}else{
     %>
      	<frame name="file_main" scrolling="auto" src="<% if("0".equals(seqId)){out.print("blank.jsp");}else{out.print("privacyFlag.jsp?seqId=" + seqId);} %>  " frameborder="yes">
     <%
   		}
     %>  
    </frameset>
</frameset>
</html>
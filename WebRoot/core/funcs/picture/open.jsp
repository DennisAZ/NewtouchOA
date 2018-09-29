<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqIdStr=request.getParameter("PIC_ID");
	String subDir=request.getParameter("SUB_DIR");
	String fileName=request.getParameter("URL_FILE_NAME");
	String viewType=request.getParameter("VIEW_TYPE");
	String ascDesc=request.getParameter("ASC_DESC");
	String picFilePath=request.getParameter("picFilePath");
	
	int seqId=0;
	if(seqIdStr!=null){
	  seqId=Integer.parseInt(seqIdStr);
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.net.URLEncoder"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>浏览图片</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<frameset rows="*,50"  cols="*" frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="open_main" scrolling="yes" src="openMain.jsp" frameborder="0">
    <frame name="open_control" scrolling="no" noresize src="openControl.jsp?PIC_ID=<%=seqId %>&SUB_DIR=<%=URLEncoder.encode(subDir,"UTF-8") %>&FILE_NAME=<%=URLEncoder.encode(fileName,"UTF-8") %>&picFilePath=<%=URLEncoder.encode(picFilePath,"UTF-8") %>&VIEW_TYPE=<%=viewType %>&ASC_DESC=<%=ascDesc %>" frameborder="0">
</frameset>
</html>
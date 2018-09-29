<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<title>工作日志</title>
</head>
<frameset  cols="210,*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="diaryLeft" scrolling="no" src="<%=contextPath%>/core/funcs/diary/info/left.jsp" frameborder="0">
    <frame name="diaryBody" scrolling="auto" src="<%=contextPath%>/core/funcs/diary/info/blank.jsp" frameborder="0">
</frameset>
</html>
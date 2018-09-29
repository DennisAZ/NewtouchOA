<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主题词管理</title>
</head>
<frameset rows="40,*"  cols="*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="wordTitle" scrolling="no" noresize src="<%=contextPath%>/core/funcs/doc/docword/title.jsp" frameborder="NO">
    <frameset rows="*"  cols="230,*" frameborder="0" border="0" framespacing="0" id="frame2"  scrolling="no">
       <frame id="wordListTree" name="wordListTree" src="<%=contextPath%>/core/funcs/doc/docword/wordListTree.jsp" scrolling="auto" noresize />
       <frame name="wordInput" src="<%=contextPath%>/core/funcs/doc/docword/wordInput.jsp" scrolling="auto" frameborder="NO">
    </frameset>
</frameset>
</html>



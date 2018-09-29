<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
</head>
<frameset rows="40,*"  cols="*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="deptTitle" scrolling="no" noresize src="<%=contextPath%>/core/funcs/dept/title.jsp" frameborder="NO">
    <frameset rows="*"  cols="230,*" frameborder="0" border="0" framespacing="0" id="frame2"  scrolling="no">
       <frame name="deptListTree" src="<%=contextPath%>/core/funcs/dept/deptListTree.jsp" scrolling="auto" noresize />
       <frame name="deptinput" src="<%=contextPath%>/core/funcs/dept/deptinput.jsp" scrolling="auto" frameborder="NO">
    </frameset>
</frameset>
</html>



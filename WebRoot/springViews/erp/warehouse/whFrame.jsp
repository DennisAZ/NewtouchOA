<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产品信息管理</title>
</head>
<frameset rows="75,*"  cols="*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="deptTitle" scrolling="no" noresize src="whQuery.jsp" frameborder="NO">
    <frameset rows="*"  cols="*" frameborder="0" border="0" framespacing="0" id="frame2"  scrolling="no">
       <frame name="wh" src="dbManage.jsp" scrolling="auto" noresize />
    </frameset>
</frameset>
</html>



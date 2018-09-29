<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产品管理</title>
</head>
<frameset rows="50,*"  cols="*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="deptTitle" scrolling="no" noresize src="nquery.jsp" frameborder="NO">
    <frameset rows="*"  cols="*" frameborder="0" border="0" framespacing="0" id="frame2"  scrolling="no">
       <frame name="product" src="chooseProduct1.jsp" scrolling="auto" noresize />
    </frameset>
</frameset>
</html>



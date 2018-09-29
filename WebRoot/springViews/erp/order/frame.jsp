<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<title>产品管理</title>
</head>
<frameset rows="50,*"  cols="*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="deptTitle" scrolling="no" noresize src="query.jsp" frameborder="NO">
    <frameset rows="*"  cols="*" frameborder="0" border="0" framespacing="0" id="frame2"  scrolling="no">
       <frame name="product" src="selectProduct.jsp" scrolling="auto" noresize />
    </frameset>
</frameset>
</html>



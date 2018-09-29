<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单分类设置</title>
</head>
<frameset cols="300,*" cols="200,*" frameborder="NO" border="0" framespacing="0" >	
    <frame name="navigateFrame" scrolling="auto" src="<%=contextPath %>/subsys/oa/hr/setting/hrCode/codeList.jsp" frameborder="NO" />
    <frame name="contentFrame" scrolling="auto" src="<%=contextPath %>/subsys/oa/hr/setting/hrCode/blanks.jsp" frameborder="NO" /> 
</frameset>
</html>
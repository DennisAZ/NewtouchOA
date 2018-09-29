<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单分类设置</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript">
function doInit() {
  doFramesetScroll("navigateFrame", "contentFrame", 580, "<%=contextPath %>/core/funcs/menu/blank.jsp");
}
window.onload = doInit;
</script>
</head>
<frameset cols="300,*" frameborder="NO" framespacing="0">	
	<frame name="navigateFrame" id="navigateFrame" scrolling="no" src="<%=contextPath %>/core/funcs/menu/menu.jsp" />
	<frame name="contentFrame" id="contentFrame" src="about:blank" />	
</frameset>
</html>
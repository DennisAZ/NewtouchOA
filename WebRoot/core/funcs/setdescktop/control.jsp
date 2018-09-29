<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的帐户</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href="<%=cssPath %>/style.css">
<title>控制面板</title>
<%  
  String path = request.getParameter("path");
  if (path != null && !"".equals(path)){
    path = contextPath + "/core/funcs/setdescktop/" + path + "/index.jsp";
  }
  else {
    path = contextPath + "/core/funcs/setdescktop/theme/index.jsp";
  }
%>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script>
function doInit() {
  doFramesetScroll("c_menu", "c_main", 550, "<%=path %>");
}
window.onload = doInit;
</script>
</head>
<frameset framespacing="0" frameborder="no" cols="220,*">
    <frame scrolling="no" frameborder="0" src="<%=contextPath  %>/core/funcs/setdescktop/menu.jsp" noresize="" name="c_menu" id="c_menu"></frame>
    <frame scrolling="auto" src="about:blank" frameborder="0" noresize="" name="c_main" id="c_main"></frame>
</frameset>
</html>
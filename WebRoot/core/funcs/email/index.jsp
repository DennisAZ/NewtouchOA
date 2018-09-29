<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>InnerMail</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript">
function doInit() {
  doFramesetScroll("mail_menu", "mail_main", 450, "<%=contextPath%>/core/funcs/email/new/new.jsp?boxId=0");
}
window.onload = doInit;
</script>
</head>
<frameset cols="210,*" frameborder="no" border="0" framespacing="0" id="frame1">
  <frame id="mail_menu" name="mail_menu" scrolling="no" src="menu.jsp" frameborder="0">
  <frame id="mail_main" name="mail_main" scrolling="auto" src="about:blank" frameborder="0">
</frameset>
</html>
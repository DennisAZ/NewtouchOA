<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作日志</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script>
function doInit() {
  doFramesetScroll("diaryLeft", "diaryBody", 550, "<%=contextPath %>/core/funcs/diary/last.jsp");
}
window.onload = doInit;
</script>
</head>
<frameset rows="*"  cols="190,*" frameborder="no" border="0" framespacing="0" id="frame1">
    <frame name="diaryLeft" id="diaryLeft" scrolling="no" src="<%=contextPath %>/core/funcs/diary/diaryLeft.jsp" frameborder="0">
    <frame name="diaryBody" id="diaryBody" scrolling="yes" src="about:blank" frameborder="0">
</frameset>
</html>
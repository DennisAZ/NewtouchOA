<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String commentId = request.getParameter("commentId");
  if(commentId == null){
    commentId = "";
  }
  String replyId = request.getParameter("replyId");
  if(replyId == null){
    replyId = "";
  }
  String type = request.getParameter("type");
  if(type == null){
    type = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评论回复</title>
</head>
<body>
<iframe src="<%=contextPath %>/core/funcs/diary/comment/replyComment.jsp?commentId=<%=commentId %>&type=<%=type %>&replyId=<%=replyId %>" scrolling="auto" width="700" height="500"></iframe>
</body>
</html>
<%@ page language="java" import="java.util.*,java.io.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BBSUtil,oa.core.funcs.bbs.act.BbsService" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
String boardName=request.getParameter("boardName");
String boardId=request.getParameter("boardId");
String commentId=request.getParameter("commentId");
new BbsService().mOrUpdateComments(request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'deleteComment.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="/yh/core/styles/style1/css/style.css">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
     <div id="msrg"><table class="MessageBox" align="center" width="290">
     <tbody><tr>  <td class="msg info">
     <div class="content" style="font-size:12pt">
    操作成功!<a href='core/funcs/bbs/client/bbsBoardComment.jsp?boardId=<%=boardId%>&&boardName=<%=boardName%>'>返回版区</a> 

</div> </td> </tr> </tbody></table></div>
       </body>
</html>

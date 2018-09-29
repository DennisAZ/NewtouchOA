<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'addBoard.jsp' starting page</title>
    
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
    
    <%
        new BbsService().addBoard(request);
    	String boardId=request.getParameter("boardId");
    %>
    
    <div id="msrg"><table class="MessageBox" align="center" width="290">
     <tbody><tr>  <td class="msg info">
     <div class="content" style="font-size:12pt">
    
    <%if(boardId!=null && !boardId.equals("") && Integer.parseInt(boardId)>0){ %>  
   <br>修改成功！<a href='/yh/core/funcs/bbs/system/index.jsp'>返回讨论区</a><Br>
   <%}else{ %>
      <br>新建成功！<a href='/yh/core/funcs/bbs/system/index.jsp'>返回讨论区</a><Br>
      <%} %>

</div> </td> </tr> </tbody></table></div>
    

  </body>
</html>

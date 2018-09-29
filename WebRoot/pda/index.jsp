<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
String username = (String) request.getAttribute("username") == null ? "" : (String) request.getAttribute("username");
String errorNo = (String) request.getAttribute("errorNo");
String errorMsg = (String) request.getAttribute("errorMsg");
%>
<!doctype html>
<html>
<head>
<title><%=StaticData.SOFTTITLE %></title>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/pda/style/index.css" />
</head>
<body>
<div id="logo">
   <div id="product"></div>
   <div id="form">
      <form name="form" method="post" action="<%=contextPath %>/yh/pda/login/act/YHPdaLoginAct/doLogin.act">
      <div id="form_input">
         <div class="user"><input type="text" class="text" name="username" maxlength="20" value="<%=username %>" /></div>
         <div class="pwd"><input type="password" class="text" name="pwd" value="" /></div>
      </div>
      <div id="form_submit">
         <input type="image" src="<%=contextPath %>/pda/style/images/submit.png" class="submit" title="登录" value=" " />
      </div>
      </form>
   </div>
   <div id="msg">
<% 
    if(!YHUtility.isNullorEmpty(errorNo)){
      out.println(errorMsg);
    }
%>
   </div>
</div>
</body>
</html>

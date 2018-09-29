<%@ page language="java" contentType="text/html;charset=utf-8"%>
<html>
<head>
<title>request</title>
</head>

<body>
<h1 style="color:red">request</h1>
<hr>
<% 
        out.println("Protocol:<font color=red>"+ request.getProtocol()+"</font><br>");
        out.println("Scheme:<font color=red>"+request.getScheme()+"</font><br>");
        out.println("ServerName:<font color=red>"+request.getServerName()+"</font><br>");
 	out.println("ServerPort:<font color=red>"+ request.getServerPort()+"</font><br>");
        out.println("Remote Addr:<font color=red>"+request.getRemoteAddr()+"</font><br>");
        out.println("Server Info:<font color=red>"+getServletConfig().getServletContext()+"</font><br>");
 	
        out.println("Remote Host:<font color=red>"+ request.getRemoteHost()+"</font><br>");
        out.println("Character Encoding:<font color=red>"+request.getCharacterEncoding()+"</font><br>");
        out.println("Content Length:<font color=red>"+request.getContentLength()+"</font><br>");
 	out.println("Content Type:<font color=red>"+ request.getContentType()+"</font><br>");
        out.println("Auth type:<font color=red>"+request.getAuthType()+"</font><br>");
        out.println("Http Method:<font color=red>"+request.getMethod()+"</font><br>");

	out.println("Path Info:<font color=red>"+request.getPathInfo()+"</font><br>");
        out.println("Path Trans:<font color=red>"+request.getPathTranslated()+"</font><br>");
	out.println("Query String :<font color=red>"+request.getQueryString()+"</font><br>");
	out.println("Remote User :<font color=red>"+request.getRemoteUser()+"</font><br>");
	out.println("Session Id :<font color=red>"+request.getRequestedSessionId()+"</font><br>");
	out.println("Request URI :<font color=red>"+request.getRequestURI()+"</font><br>");
	out.println("Servlet Path :<font color=red>"+request.getServletPath()+"</font><br>");	
	out.println("Referer :<font color=red>"+request.getHeader("Referer")+"</font><br>");
	out.println("RealPath :<font color=red>"+request.getRealPath("/")+"</font><br>");
	out.println("Accept :<font color=red>"+request.getHeader("Accept")+"</font><br>");
	out.println("Accept-Language :<font color=red>"+request.getHeader("Accept-Language")+"</font><br>");
	out.println("Accept-Encoding :<font color=red>"+request.getHeader("Accept-Encoding")+"</font><br>");	
	out.println("User-Agent :<font color=red>"+request.getHeader("User-Agent")+"</font><br>");
	out.println("Connection :<font color=red>"+request.getHeader("Connection")+"</font><br>");
	out.println("Cookie :<font color=red>"+request.getHeader("Cookie")+"</font><br>");
	out.println("Created :<font color=red>"+session.getCreationTime()+"</font><br>");
	out.println("LastAccessed :<font color=red>"+session.getLastAccessedTime()+"</font><br>");		
	//request.getRequestDispatcher("/default.jsp").forward(request,response);
	//response.sendRedirect("/newsManage/default.jsp");
%>

</body>
</html>

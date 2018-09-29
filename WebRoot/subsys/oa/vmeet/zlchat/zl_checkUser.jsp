<%@ page language="java" contentType="text/html;charset=utf-8"%>
<% /*文件作用: 处理zlchat中的用户登录验证 */ %>
<%!
	//这里定义认证用户的过程
	boolean authentication(String userName,String password){
		return true;
	}
%>

<%
//以下参数是zlchat传过来的
String userName=request.getParameter("userName")!=null?request.getParameter("userName"):"";
String password=request.getParameter("password")!=null?request.getParameter("password"):"";
//String roomID=request.getParameter("roomID")!=null?request.getParameter("roomID"):"";
//String role=request.getParameter("role")!=null?request.getParameter("role"):"4";

//验证是否通过,自己定义的一个变量,在下面你可以添加自己的验证代码,规则可以自己决定
boolean isAllowedToLogin=authentication(userName,password);
   


//验证结果返回给zlchat客户端处理
if(isAllowedToLogin)
{
	out.print("<Result isUser='true'></Result>");
}
else
{
	out.print("<Result isUser='false'></Result>");
}

%>
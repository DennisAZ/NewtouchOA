<%@ page language="java" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>zlchat Demo</title>
<Link Rel="STYLESHEET" Href="css.css" Type="text/css">

</head>

<body leftmargin="0" topmargin="0">

<form id="form1" name="form1" method="post" action="zlchat.jsp">
<table  border=1>
  <tr>
    <td colspan="2"><h3>zlchat V2.1 演示版登录</h3></td>
    </tr>
  <tr>
    <td>姓名(UserName):</td>
    <td><input name="userName" type="text" id="userName" /></td>
  </tr>
  <tr>
    <td>密码(Password):</td>
    <td><input name="password" type="password" id="password" /></td>
  </tr>
  <tr>
    <td>角色(Role)</td>
    <td>
   <input type="radio" name="role" value="4" checked/>
								听众/普通用户
	 <input type="radio" name="role" value="3" />
								演讲者/发言用户
		<input type="radio" name="role" value="2"/>
								主持人 </td>
  </tr>
  <tr align="center">
    <td colspan="2"><input type="submit" name="submit" value="登 录(Login)" />&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="清 空(Reset)" onclick="document.form1.userName.value='';"/>	</td>
    </tr>
  
</table>





</form>

</body>
</html>
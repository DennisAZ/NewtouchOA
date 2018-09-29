<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("isHavLogin", "1");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>登录系统</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    #toLogin{
		width:350px;
		margin-top:5%;
		padding:15px;
		background:#fff;
	}
	#toLoginTxt {
		line-height:25px;
	}
    </style>
    <script type="text/javascript">
		function toLogin(){
			var count=parseInt(document.getElementById("count").innerText);
			if(count<=0)
				top.location.href="login.jsp";
			else{
				count-=1;
				document.getElementById("count").innerText=count.toString();
			}
		}
		window.onload=function(){
			document.getElementById("count").innerText="1";//设置等待时间（秒）
			setInterval(toLogin,1000);
		}
  	</script>
  </head>
  
  <body>
    <div id="toLogin" class="warnBox">
        <div id="toLoginTxt">
    		<div class="bold red middle">您尚未登录或长时间无操作已自动退出系统</div>
            <div class="gray">还有&nbsp;<span id="count" class="middle"></span>&nbsp;秒转入登录页面...</div>
            如果您的浏览器长时间没有跳转，<a href="login.jsp" target="_top">请点此登录</a>
        </div>
  	</div>
  </body>
</html>

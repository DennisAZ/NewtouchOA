<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>系统成功</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	.warnBox {
			margin-top:30px;
			width:350px;
		}
		#warnContent1 {
			background-image:url(crm/images/content/bigSuc.gif);
			
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
	 <script type="text/javascript">
		function toLogin(){
			var count=parseInt($("count").innerText);
			if(count<=0)
				window.location.href="login.jsp";
			else{
				count-=1;
				$("count").innerText=count.toString();
			}
		}
		window.onload=function(){
			$("count").innerText="3";//设置等待时间（秒）
			setInterval(toLogin,1000)
		}
  	</script>
  </head>
  
  <body> 
  	<div class="warnBox">
        <div id="warnContent1">	
            <div id="warnTitle">系统已成功激活!</div>
            <div class="deepGray">
                还有<span id="count" class="middle"></span>&nbsp;秒转入登录页面...
            </div>
        </div>
    </div> 
	</body>
</html>

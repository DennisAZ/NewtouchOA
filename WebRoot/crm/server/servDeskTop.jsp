<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>客服管理</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <link rel="stylesheet" type="text/css" href="css/guide.css">
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript">
   	 	function menuForward(index){
			switch(index){
				case 1:
					self.location.href="../cusServAction.do?op=toListServ&range=0";
					//self.location.href="../cusServAction.do?op=listServ&range=0";
					break;
				case 2:
					self.location.href="../cusServAction.do?op=toListServ&range=1";
					//self.location.href="../cusServAction.do?op=listServ&range=1";
					break;
			}
		}
	
	createProgressBar();
	window.onload=function(){
		loadGuideStyle();
		closeProgressBar();
	}
  </script></head>
  
  <body> 
  <div id="deskBox">
  	<div id="deskTitle">客服管理 > 导航</div>
    <div id="deskPanel">
    	<div id="topWords">您要使用哪个功能？</div>
        <div class="guideDiv">
        <span class="guideLayer" style="background:url(images/icon/server/un_server.gif) no-repeat;"  onClick="menuForward(1)">
            <div class="guideTitle">待办客服</div>
            <ul>
                <li>&nbsp;</li>
            </ul>
        </span>
        <span class="guideLayer" style="background:url(images/icon/server/done_server.gif) no-repeat;"  onClick="menuForward(2)">
            <div class="guideTitle">客服记录</div>
            <ul>
                <li>&nbsp;</li>
            </ul>
        </span>
        </div>
    </div>
    
 </div>
</body>
</html>

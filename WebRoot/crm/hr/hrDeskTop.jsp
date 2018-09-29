<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>人员管理</title>
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
					self.location.href="../empAction.do?op=toListEmps&workstate=0";
					break;
				 case 2:
					self.location.href="../empAction.do?op=toListEmps&workstate=1";
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
  	<div id="deskTitle">人员管理 > 导航</div>
    <div id="deskPanel">
    	<div id="topWords">您要使用哪个功能？</div>
        <div class="guideDiv">
            <span class="guideLayer" style="background:url(images/icon/hr/emp.gif) no-repeat;" onClick="menuForward(1)">
                <div class="guideTitle">在职员工</div>
                <ul>
                    <li></li>
                </ul>
            </span>
             <span class="guideLayer" style="background:url(images/icon/hr/emp2.gif) no-repeat;" onClick="menuForward(2)">
                <div class="guideTitle">离职员工</div>
                <ul>
                    <li></li>
                </ul>
            </span>
        </div>
    </div>
</div>
    
</body>
</html>

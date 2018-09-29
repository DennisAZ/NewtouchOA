<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>项目管理</title>
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
					self.location.href="../projectAction.do?op=getMyProject";		
					break;
				case 2:
					self.location.href="../projectAction.do?op=getMyProTask";				
					break;
				case 3:
					self.location.href="../projectAction.do?op=findMyHisTask";
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
  	<div id="title">项目管理 > 导航</div>
    <div id="topWords">
    	您要使用哪个功能？
    </div>
  	<div class="guideDiv">
        <span class="guideLayer" style="background:url(images/icon/pro/project.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(1)">项目管理</div>
            <ul>
                <li></li>
            </ul>
        </span>
        <span class="guideLayer" style="background:url(images/icon/pro/pro_task.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(2)">项目工作</div>
            <ul>
                <li></li>
            </ul>
        </span>
    </div>
    
   	<div class="guideDiv">
        <span class="guideLayer" style="background:url(images/icon/pro/pro_act.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(3)">项目日志</div>
            <ul>
                <li></li>
            </ul>
        </span>
       <!-- <span class="guideLayer" style="background:url(images/icon/pro/pro_sta.gif) no-repeat;">
            <div class="guideTitle">项目统计</div>
            <ul>
                <li></li>
            </ul>
        </span>-->
    </div>
</div>
    
</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>协同办公</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css">
    <link rel="stylesheet" type="text/css" href="crm/css/guide.css">
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">
    	function menuForward(index){
			switch(index){
				case 1:
					self.location.href="../messageAction.do?op=toListAllNews&category=0";
					break;
				case 2:
					self.location.href="../messageAction.do?op=toShowSchList";			
					break;
				case 3:
					self.location.href="../messageAction.do?op=toListHaveSenRep";			
					break;
				case 4:
					self.location.href="../messageAction.do?op=toListAllMess";				
					break;
				/*case 5:
					self.location.href="../salTaskAction.do?op=toListMyTaskSearch";
					break;	*/
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
  	<div id="deskTitle">协同办公 > 导航</div>
    <div id="deskPanel">
    	<div id="topWords">您要使用哪个功能？</div>
        <div class="guideDiv">
            <span class="guideLayer" style="background:url(crm/images/icon/oa/mes.gif) no-repeat;" onClick="menuForward(4)">
                <div class="guideTitle">内部消息</div>
                <ul>
                    <li></li>
                </ul>
            </span>
            <span class="guideLayer" style="background:url(crm/images/icon/oa/news.gif) no-repeat;" onClick="menuForward(1)">
                <div class="guideTitle">新闻公告</div>
                <ul>
                    <li></li>
                </ul>
            </span>
        </div>
        
        <div class="guideDiv">
            <span class="guideLayer" style="background:url(crm/images/icon/oa/sch.gif) no-repeat;" onClick="menuForward(2)">
                <div class="guideTitle">我的日程</div>
                <ul>
                    <li></li>
                </ul>
            </span>
            <!--<span class="guideLayer" style="background:url(crm/images/icon/oa/task.gif) no-repeat;" onClick="menuForward(5)">
                <div class="guideTitle">工作安排</div>
                <ul>
                    <li></li>
                </ul>
            </span>-->
            <span class="guideLayer" style="background:url(crm/images/icon/oa/rep.gif) no-repeat;" onClick="menuForward(3)">
                <div class="guideTitle">报告管理</div>
                <ul>
                    <li></li>
                </ul>
            </span>
        </div>
    </div>
    
 </div>
</body>
</html>

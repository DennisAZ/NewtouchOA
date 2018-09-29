<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>系统设置</title>
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
					self.location.href="../userAction.do?op=userManage";			
					break;
			    case 2:
					self.location.href="../userAction.do?op=searchLimRole";
					break;
				case 3:
					self.location.href="../empAction.do?op=findForwardOrg";
					break;
				case 4:
					self.location.href="../typeAction.do?op=findTypeList&typType=1";
					break;
				case 5:
					self.location.href="../customAction.do?op=toListDelCus";
					break;
				
			}
		}
	
		createProgressBar();
		window.onload=function(){
			loadGuideStyle();
			closeProgressBar();
		}
  </script>
  </head>
  
  <body> 
  <div id="deskBox">
  <div id="deskTitle">系统设置 > 导航</div>
  <div id="deskPanel">
    <div id="topWords">您要使用哪个功能？</div>
  	<div class="guideDiv">
        <span class="guideLayer" style="background:url(crm/images/icon/sys/limUser.gif) no-repeat;" onClick="menuForward(1)">
            <div class="guideTitle">账号设置</div>
            <ul>
                <li></li>
            </ul>
        </span>
        <span class="guideLayer" style="background:url(crm/images/icon/sys/role.gif) no-repeat;" onClick="menuForward(2)">
            <div class="guideTitle">职位设置</div>
            <ul>
                <li></li>
            </ul>
        </span>
    </div>
    
   	<div class="guideDiv">
        <span class="guideLayer" style="background:url(crm/images/icon/sys/org.gif) no-repeat;" onClick="menuForward(3)">
            <div class="guideTitle">部门设置</div>
            <ul>
                <li></li>
            </ul>
        </span>
        <span class="guideLayer" style="background:url(crm/images/icon/sys/type.gif) no-repeat;" onClick="menuForward(4)">
            <div class="guideTitle">类别设置</div>
            <ul>
                <li></li>
            </ul>
        </span>
    </div>
	
    <div class="guideDiv">
        <span class="guideLayer" style="background:url(crm/images/icon/sys/recycle.gif) no-repeat;" onClick="menuForward(5)">
            <div class="guideTitle">回收站</div>
            <ul>
                <li></li>
            </ul>
        </span>
    </div>
    </div>
 </div>
</body>
</html>

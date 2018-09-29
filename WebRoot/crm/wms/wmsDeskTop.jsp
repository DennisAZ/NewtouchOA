<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>库存管理导航</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <link rel="stylesheet" type="text/css" href="css/guide.css">
     <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript">
   		function menuForward(index){
			
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
  	<div id="deskTitle">库存管理 > 导航</div>
    <div id="deskPanel">
    	<div id="topWords">您要使用哪个功能？</div>
        <div class="guideDiv">
            <span class="guideLayer" style="background:url(images/icon/wms/wms_show.gif) no-repeat;">
                <div class="guideTitle">库存列表</div>
                <ul>
                    <li></li>
                </ul>
            </span>
            <span class="guideLayer" style="background:url(images/icon/wms/wms_in.gif) no-repeat;">
                <div class="guideTitle">入库管理</div>
                <ul>
                    <li></li>
                </ul>
            </span>
        </div>
        
        <div class="guideDiv">
            <span class="guideLayer" style="background:url(images/icon/wms/wms_out.gif) no-repeat;">
                <div class="guideTitle">出库管理</div>
                <ul>
                    <li></li>
                </ul>
            </span>
        </div>
    </div>
</div>
</body>
</html>


<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>销售管理导航</title>
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
					self.location.href="../orderAction.do?op=toListOrders&range=0";		
					break;
					
				/*case 2:
					self.location.href="../paidAction.do?op=toListPaidPlan&range=0";		
					break;*/
				/*
				case 2:
					self.location.href="../statAction.do?op=showSalTarget&range=1&tarName=1&isEmpty=yes";
					break;*/
				case 3:
					self.location.href="../paidAction.do?op=toListPaidPast";
					break;
				case 4:
					self.location.href="../prodAction.do?op=toListProd";
					break;
				case 5:
					self.location.href="../orderAction.do?op=toListProdShipment";
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
  	<div id="deskTitle">销售管理 > 导航</div>
    <div id="deskPanel">
    	<div id="topWords">您要使用哪个功能？</div>
        <div class="guideDiv">
            <span class="guideLayer" style="background:url(crm/images/icon/sal/ord.gif) no-repeat;" onClick="menuForward(1)">
                <div class="guideTitle">订单合同</div>
                <ul>
                    <li></li>
                </ul>
            </span>
            <span class="guideLayer" style="background:url(crm/images/icon/acc/account_in.gif) no-repeat;" onClick="menuForward(3)">
                <div class="guideTitle">回款记录</div>
                <ul>
                    <li></li>
                </ul>
            </span>
            <!--<span class="guideLayer" style="background:url(crm/images/icon/sal/contract.gif) no-repeat;" onClick="menuForward(2)">
                <div class="guideTitle">回款计划</div>
                <ul>
                    <li></li>
                </ul>
            </span>-->
        </div>
        
        <div class="guideDiv">
            <span class="guideLayer" style="background:url(crm/images/icon/sal/product.gif) no-repeat;" onClick="menuForward(4)">
                <div class="guideTitle">产品管理</div>
                <ul>
                    <li></li>
                </ul>
            </span>
            <span class="guideLayer" style="background:url(crm/images/icon/sal/delivery.gif) no-repeat;" onClick="menuForward(5)">
                <div class="guideTitle">发货记录</div>
                <ul>
                    <li></li>
                </ul>
            </span>
        </div>
    </div>
</div>
</body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>采购管理</title>
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
					self.location.href="../salSupplyAction.do?op=getAllSupply";			
					break;
			    case 2:
					self.location.href="../salSupplyAction.do?op=getAllSupContact";
					break;
				case 3:
					self.location.href="../salPurAction.do?op=showSpo&isAll=0&isok=0";
					break;
				case 4:
					self.location.href="../salPurAction.do?op=showSpo&isAll=0&isok=1";
					break;
				case 5:
					self.location.href="../salPurAction.do?op=showInp";
					break;
				case 6:
					self.location.href="../salPurAction.do?op=listSpoPaidPlan&range=0";
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
  	<div id="title">采购管理 > 导航</div>
    <div id="topWords">
    	您要使用哪个功能？
    </div>
     <div class="guideDiv">
        <span class="guideLayer" style="background:url(crm/images/icon/sup/pur_app.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(4)">采购单<br/>[待审核]</div>
            <ul>
                <li>&nbsp;</li>
            </ul>
        </span>
        <span class="guideLayer" style="background:url(crm/images/icon/sup/pur_suc.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(5)">采购单<br/>[已审核]</div>
            <ul>
                <li>&nbsp;</li>
            </ul>
        </span>
    </div>
  	<div class="guideDiv">
        <span class="guideLayer" style="background:url(crm/images/icon/sal/contract.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(6)">付款计划</div>
            <ul>
                <li></li>
            </ul>
        </span>
         <span class="guideLayer" style="background:url(crm/images/icon/sup/sup.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(1)">供应商资料</div>
            <ul>
                <li>&nbsp;</li>
            </ul>
        </span>
    </div>
    <div class="guideDiv">
    <span class="guideLayer" style="background:url(crm/images/icon/sup/sup_con.gif) no-repeat;">
            <div class="guideTitle" onClick="menuForward(2)">联系人</div>
            <ul>
                <li>&nbsp;</li>
            </ul>
    </span>
    <span class="guideLayer" style="background:url(crm/images/icon/sup/inq.gif) no-repeat;">
        <div class="guideTitle" onClick="menuForward(3)">询价管理</div>
        <ul>
            <li>&nbsp;</li>
        </ul>
    </span>
    </div>
 </div>
</body>
</html>

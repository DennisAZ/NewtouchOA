<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>客户管理</title>
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
					self.location.href="../customAction.do?op=toListCustomers&range=0&cusState=0";		
					break;
				case 11:
					self.location.href="../customAction.do?op=toListCustomers&range=1&cusState=a";		
					break;
				case 2:
					self.location.href="../customAction.do?op=toListContacts&range=0";
					break;
				case 21:
					self.location.href="../customAction.do?op=toListContacts&range=1";
					break;
				case 3:
					self.location.href="../cusServAction.do?op=toListSalOpps&range=0";
					break;
				case 4:
					self.location.href="../cusServAction.do?op=toListSalPra&range=0";
					break;
			}
		}
		//显示全部选项（无权限则显示灰色按钮）
		function showAllitem(resp){
			if(resp!=undefined){
				if(resp[0]=='true'){
					$("listAllCus").onclick=function(){menuForward(11);};
				}
				else{
					$("listAllCus").style.background="url(crm/images/icon/cus/allCusGray.gif) no-repeat";
					$("listAllCus").className="guideLayerDis";
				}
				if(resp[1]=='true'){
					$("listAllCon").onclick=function(){menuForward(21);};
				}
				else{
					$("listAllCon").style.background="url(crm/images/icon/cus/allContactGray.gif) no-repeat";
					$("listAllCon").className="guideLayerDis";
				}
			}
		}
		
		createProgressBar();
		window.onload=function(){
			invokeFuncLimAllow("r032|r033",showAllitem);
			loadGuideStyle();
			closeProgressBar();
		}
  </script>
  </head>
  
  <body> 
  <div id="deskBox">
  	<div id="deskTitle">客户管理 > 导航</div>
    <div id="deskPanel">
    	<div id="topWords">您要使用哪个功能？</div>
  		<div class="guideDiv">
        	<span class="guideLayer" style="background:url(crm/images/icon/cus/cus.gif) no-repeat;" onClick="menuForward(1)">
            	<div>我的客户</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
            <span id="listAllCus" class="guideLayer" style="background:url(crm/images/icon/cus/allCus.gif) no-repeat;">
            	<div>全部客户</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
        </div>
        
        <div class="guideDiv">
        	<span class="guideLayer" style="background:url(crm/images/icon/cus/contact.gif) no-repeat;" onClick="menuForward(2)">
                <div>我的联系人</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
            <span id="listAllCon" class="guideLayer" style="background:url(crm/images/icon/cus/allContact.gif) no-repeat;">
            	<div>全部联系人</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
        </div>
        
        <!--<div class="guideDiv">
        	<span class="guideLayer" style="background:url(crm/images/icon/cus/cus_opp.gif) no-repeat;" onClick="menuForward(3)">
                <div>销售机会</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
            <span class="guideLayer" style="background:url(crm/images/icon/cus/cus_act.gif) no-repeat;" onClick="menuForward(4)">
                <div>来往记录</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
        </div>-->
    </div>
 </div>
</body>
</html>

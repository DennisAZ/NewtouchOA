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
					self.location.href="../supAction.do?op=toListSup";			
					break;
			    case 2:
					self.location.href="../supAction.do?op=toListPurOrd";
					break;
				case 3:
					self.location.href="../supAction.do?op=toListProdStore";
					break;
				case 4:
					self.location.href="../supAction.do?op=toListProdIn";
					break;
				case 5:
					self.location.href="../supAction.do?op=toListProdOut";
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
  	<div id="deskTitle">采购管理 > 导航</div>
    <div id="deskPanel">
    	<div id="topWords">您要使用哪个功能？</div>
  		<div class="guideDiv">
        	<span class="guideLayer" style="background:url(crm/images/icon/sup/sup.gif) no-repeat;" onClick="menuForward(1)">
            	<div>供应商</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
            <span class="guideLayer" style="background:url(crm/images/icon/sup/pur_suc.gif) no-repeat;" onClick="menuForward(2)">
            	<div>采购单</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
        </div>
        <div class="guideDiv">
        	<span class="guideLayer" style="background:url(crm/images/icon/wms/wms_show.gif) no-repeat;" onClick="menuForward(3)">
            	<div>库存列表</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
            <span class="guideLayer" style="background:url(crm/images/icon/wms/wms_in.gif) no-repeat;" onClick="menuForward(4)">
            	<div>入库单</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
        </div>
        <div class="guideDiv">
        	<span class="guideLayer" style="background:url(crm/images/icon/wms/wms_out.gif) no-repeat;" onClick="menuForward(5)">
            	<div>出库单</div>
                <ul>
                    <li>&nbsp;</li>
                </ul>
            </span>
        </div>
   </div>
 </div>
</body>
</html>

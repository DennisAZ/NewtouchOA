<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>回收站</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
		body{
			background-color:#fff;
		}
    	
		.deepBlueBg,.grayBack {
			padding:4px;
		}
		.menuLis{
			padding:5px;
			color:#003366;
		}
		.menuText{
			color:#999999;
			text-align:left;
			padding:2px;
		}
		
		.scrollBar{
			width:120px;
			height:500px;
			
			overflow:auto;
			scrollbar-face-color:#A6CFF4;
			scrollbar-highlight-color:#f9fbff;
			scrollbar-shadow-color:#f2f5fd;
			scrollbar-3dlight-color:#7DB4DB;
			scrollbar-arrow-color:#3243A3;
			scrollbar-track-color:#f0f0f1;
			scrollbar-darkshadow-color:#7DB4DB;
			text-align:center;  
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/type.js"></script>
    <script type="text/javascript">
    	function menuForward(i){
			switch(i){
				//-------------客户---------------
				case 1://客户
					parent.location.href="<%=basePath%>/customAction.do?op=toListDelCus";
					break;
				/*case 3://联系人
					parent.location.href="../customAction.do?op=toListDelContact";
					break;*/
				/*case 4://销售机会
					parent.location.href="/cusServAction.do?op=toListDelOpp";
					break;*/
				/*case 5://报价记录
					parent.location.href="/cusServAction.do?op=findDelQuote";
					break;*/
				case 6://来往记录
					parent.location.href="<%=basePath%>/cusServAction.do?op=toListDelPra";
					break;
				/*case 7://客户服务
					parent.location.href="/cusServAction.do?op=toListDelServ";
					break;*/
				//-------------销售-------------
				case 11://订单
					parent.location.href="<%=basePath%>/orderAction.do?op=toListDelOrder";
					break;
				/*case 13://回款计划
					parent.location.href="../paidAction.do?op=toListDelPaidPlan";
					break;*/
				/*case 14://回款记录
					parent.location.href="/paidAction.do?op=toListDelPaidPast";
					break;*/
				/*case 15://开票记录
					parent.location.href="/invAction.do?op=findDelInvoice&invType=0";
					break;*/
				case 16://产品
					parent.location.href="<%=basePath%>/prodAction.do?op=toListDelProduct";
					break;
				//-------------协同办公-------------
				case 21://已发报告
					parent.location.href="<%=basePath%>/messageAction.do?op=toListDelReport";
					break;
				/*case 22://工作安排
					parent.location.href="../salTaskAction.do?op=toListDelSalTask";
					break;*/
				/*case 23://消息
					parent.location.href="../messageAction.do?op=findDelMail";
					break;*/
				//-------------仓储-------------
				//case 31://入库单
//					parent.location.href="../wmsManageAction.do?op=findDelWarIn";
//					break;
//				case 32://出库单
//					parent.location.href="../wwoAction.do?op=findDelWarOut";
//					break;
//				case 33://仓库调拨
//					parent.location.href="../wwoAction.do?op=findDelWmsChange";
//					break;
//				case 34://盘点记录
//					parent.location.href="../wwoAction.do?op=findDelWmsCheck";
//					break;
				/*case 35://库存流水
					parent.location.href="../wwoAction.do?op=findDelWmsLine";
					break;*/
				//-------------人员管理-------------
				case 41://员工档案
					parent.location.href="<%=basePath%>/empAction.do?op=toListDelSalEmp";
					break;
				//-------------项目管理-------------
				/*case 51://项目
					parent.location.href="../projectAction.do?op=findDelProject";
					break;*/
			    /*case 52://子项目
					parent.location.href="../projectAction.do?op=findDelSubPro";
					break;
				case 53://项目工作
					parent.location.href="../projectAction.do?op=findDelProTask";
					break;
			    case 54://项目日志
					parent.location.href="../projectAction.do?op=findDelHisTask";
					break;*/
				//-------------采购管理-------------
				/*case 61://供应商
					parent.location.href="../salSupplyAction.do?op=findDelSup";
					break;
			    case 62://供应商联系人
					parent.location.href="../salSupplyAction.do?op=findDelSupCon";
					break;
                case 63://询价管理
					parent.location.href="../salPurAction.do?op=findDelInq";
					break;
				case 64://采购单
					parent.location.href="../salPurAction.do?op=findDelSpo";
					break;
				case 65://付款计划
					parent.location.href="../salPurAction.do?op=findDelPaidPlan";
					break;*/
				/*case 66://付款记录
					parent.location.href="../salPurAction.do?op=findDelPaidPast";
					break;*/
				/*case 67://收票记录
					parent.location.href="../invAction.do?op=findDelInvoice&invType=1";
					break;*/
			}
			
		}
		
		//载入选中样式
		function loadStyle(){
			var typeId=parent.document.getElementById("typeId").value;
			$("type"+typeId).className="deepBlueBg";
			
			var lis = document.getElementsByTagName("li");
			for(var j=0;j<lis.length;j++){
				if(lis[j].className!="deepBlueBg"){
					lis[j].className="menuLis";
					lis[j].onmouseover=function(){
							this.className="grayBack";
					};
					lis[j].onmouseout=function(){
							this.className="menuLis";
					}; 
				}
				lis[j].style.cursor="pointer";
			}
		}
		
		window.onload=function(){
			loadStyle();
		}
    </script>
	
</head>

<body>
	<div style="padding:5px;border:#ccc 1px solid; background-color:#f0eeee; vertical-align:top" onMouseOver="this.style.backgroundColor='#f6f6f6'" onMouseOut="this.style.backgroundColor='#f0eeee'" class="scrollBar">
        <ul class="listtxt" style="padding:0px; margin:0px">
         	<li id="type1" onClick="menuForward(1)">客户</li>
			<!--<li id="type3" onClick="menuForward(3)">联系人</li>-->
			<!--<li id="type4" onClick="menuForward(4)">销售机会</li>-->
			<!--<li id="type5" onClick="menuForward(5)">报价记录</li>-->
			<li id="type6" onClick="menuForward(6)">来往记录</li>
             <!--<li id="type11" onClick="menuForward(11)">订单</li>-->
            <!--<li id="type13" onClick="menuForward(13)">回款计划</li>-->
            <!--<li id="type14" onClick="menuForward(14)">回款记录</li>-->
            <!-- <li id="type16" onClick="menuForward(16)">产品信息</li>-->
        
        <!--<div class="menuText">客服管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
        	<li id="type7" onClick="menuForward(7)">客户服务</li>
        </ul>-->
        <!--<div class="menuText">库存管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type31" onClick="menuForward(31)">入库单</li>
			<li id="type32" onClick="menuForward(32)">出库单</li>
			<li id="type33" onClick="menuForward(33)">仓库调拨</li>
			<li id="type34" onClick="menuForward(34)">盘点记录</li>
			<li id="type35" onClick="menuForward(35)">库存流水</li>
        </ul>-->
        
       <!--<div class="menuText">项目管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type51" onClick="menuForward(51)">项目</li>-->
			<!--<li id="type52" onClick="menuForward(52)">子项目</li>
			<li id="type53" onClick="menuForward(53)">项目工作</li>
			<li id="type54" onClick="menuForward(54)">项目日志</li>-->
        <!--</ul>-->
            <!-- <li id="type21" onClick="menuForward(21)">已发报告</li>-->
           	<!--<li id="type22" onClick="menuForward(22)">工作安排</li>-->
            <!--<li id="type23" onClick="menuForward(23)">已收消息</li>-->
        
        <!--<div class="menuText">采购管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type61" onClick="menuForward(61)">供应商</li>
			<li id="type62" onClick="menuForward(62)">供应商联系人</li>
			<li id="type63" onClick="menuForward(63)">询价管理</li>
			<li id="type64" onClick="menuForward(64)">采购单</li>
			<li id="type65" onClick="menuForward(65)">付款计划</li>-->
			<!--<li id="type66" onClick="menuForward(66)">付款记录</li>-->
       <!-- </ul>
        
        <div class="menuText">财务管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
        	<li id="type15" onClick="menuForward(15)">开票记录</li>
			<li id="type67" onClick="menuForward(67)">收票记录</li>
        </ul>-->
            <!-- <li id="type41" onClick="menuForward(41)">员工档案</li>-->
        </ul>
        
    </div>
</body>
</html>

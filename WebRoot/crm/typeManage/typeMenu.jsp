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
	<title>类别菜单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
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
			height:600px;
			
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
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/type.js"></script>
    <script type="text/javascript">
    	function menuForward(i){
			var typeTips="";
			switch(i){
				//-------------客户---------------
				case 1://行业
				case 2://客户类型
				case 4://客户来源
				//-------------销售-------------
				case 11://订单
				case 12://订单状态
				case 15://产品单位
				case 'express'://物流公司
				case 'ordSou'://订单来源
				//-------------采购管理--------------
				case 'supType':
				case 'puoType':
				//-------------协同办公-------------
				case 21://日程类别
				//case 22://工作
				case 22://报告
				//-------------仓储-------------
				case 31://仓库
				//-------------项目--------------
				//case 51://项目
				
				//case 62://付款类别
				//-------------财务类别--------------
				case 13://票据
				//case 73://成本类别
					//parent.location.href="../typeAction.do?op=findTypeList&typType="+i;
					//break;
				//case 71://入账类别
				//case 72://出账类别
					parent.location.href="../typeAction.do?op=findTypeList&typType="+i;
					break;
				
				/*case 3://机会阶段
					parent.location.href="../typeAction.do?op=findTypeList&typType="+i+"&isInsert=0";
					break;*/

				case 14://产品类别
					parent.location.href="../typeManage/showProType.jsp";
					break;
				//-------------地区-------------
				case 41://国家
					parent.location.href="../customAction.do?op=getAllCountry";
					break;
				case 42://省份
					parent.location.href="../customAction.do?op=getAllProvince";
					break;
				case 43://城市
					parent.location.href="../customAction.do?op=getAllCity";
					break;
			}
			
		}
		
		//载入选中样式
		function loadStyle(){
			var typeId=parent.document.getElementById("typeId").value;
			if(parent.document.getElementById("typTxt")!=null)
				parent.document.getElementById("typTxt").value = $("type"+typeId).innerHTML;
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
	<div style="padding:5px; margin-top:15px; border:#ccc 1px solid; background-color:#f0eeee" onMouseOver="this.style.backgroundColor='#f6f6f6'" onMouseOut="this.style.backgroundColor='#f0eeee'" class="scrollBar">
    	<div class="menuText">客户管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
         	<li id="type1" onClick="menuForward(1)">客户行业</li>
            <li id="type4" onClick="menuForward(4)">客户来源</li>
         	<li id="type2" onClick="menuForward(2)">客户类型</li>
         	<!--<li id="type3" onClick="menuForward(3)">机会阶段</li>-->
        </ul>
        
        <div class="menuText">销售管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type11" onClick="menuForward(11)">订单类别</li>
            <li id="typeordSou" onClick="menuForward('ordSou')">订单来源</li>
            <li id="type12" onClick="menuForward(12)">订单状态</li> 
            <li id="type14" onClick="menuForward(14)">产品类别</li>
            <li id="type15" onClick="menuForward(15)">产品单位</li>
            <li id="typeexpress" onClick="menuForward('express')">物流公司</li>
        </ul>
        
        <div class="menuText">库存管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type31" onClick="menuForward(31)">仓库</li>
        </ul>
        
         <!--<div class="menuText">项目管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type51" onClick="menuForward(51)">项目类别</li>
        </ul>-->
        <div class="menuText">采购管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="typesupType" onClick="menuForward('supType')">供应商类别</li>
            <li id="typepuoType" onClick="menuForward('puoType')">采购单类别</li>
        </ul>
        <div class="menuText">协同办公</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type21" onClick="menuForward(21)">日程类别</li>
           	<!--<li id="type22" onClick="menuForward(22)">工作类别</li>-->
            <li id="type22" onClick="menuForward(22)">报告类别</li>
        </ul>
       	
        <!-- <ul class="listtxt" style="padding:0px; margin:0px">
            <!--<li id="type62" onClick="menuForward(62)">付款类别</li>--><!--
        </ul>-->
        <div class="menuText">财务管理</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <!--<li id="type71" onClick="menuForward(71)">入账类别</li>
            <li id="type72" onClick="menuForward(72)">出账类别</li>-->
            <li id="type13" onClick="menuForward(13)">票据类别</li>
           <!-- <li id="type73" onClick="menuForward(73)">成本类别</li>-->
        </ul>
        <div class="menuText">地区</div>
        <ul class="listtxt" style="padding:0px; margin:0px">
            <li id="type41" onClick="menuForward(41)">国家或地区</li>
            <li id="type42" onClick="menuForward(42)">省份</li>
            <li id="type43" onClick="menuForward(43)">城市</li>
        </ul>	
    </div>
</body>
</html>

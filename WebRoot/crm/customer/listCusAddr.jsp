<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>客户地址(客户详情)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<style type="text/css">
    	body{
			background-color:#FFFFFF
		}
    </style>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.cadId;
			var prvName = obj.cadProv?obj.cadProv.areName:"";
			var cityName =  obj.cadCity?obj.cadCity.prvName:"";
			var districtName = obj.cadDistrict?obj.cadDistrict.cityName:"";
			var location = prvName+"&nbsp;"+cityName+"&nbsp;"+districtName;
			var funcCol = "<img onClick=\"parent.cusPopDiv(109,"+dataId+")\" class='hand' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"parent.cusDelDiv(13,"+dataId+",'1')\" class='hand' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [obj.cadAddr, location, obj.cadPostCode, obj.cadContact, obj.cadPho, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = [];
			pars.op = "listCusAddr";
			pars.cusId="${cusId}";
			
			var sortFunc = "loadList";
			var cols=[
				{name:"地址",align:"left"},
				{name:"地区"},
				{name:"邮编"},
				{name:"收货人"},
				{name:"联系电话"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
    	var gridEl = new MGrid("cusAddrListTab","dataList");
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
    	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>收货地址&nbsp;</th>
                <td>
                <a href="javascript:void(0)" onClick="parent.cusPopDiv(108,'${cusId}');return false;" class="newBlueButton">新建地址</a>
                </td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   		</div>
  </body>
</html>


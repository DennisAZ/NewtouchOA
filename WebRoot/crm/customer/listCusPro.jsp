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
    <title>产品历史(客户详情)</title>
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
    <script type="text/javascript">
		//createIfmLoad('ifmLoad');//进度条
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
  			var dblFunc="descPop('orderAction.do?op=showOrdDesc&code="+obj.salOrdCon.sodCode+"')";
  			var relFunc="descPop('prodAction.do?op=wmsProDesc&wprId="+obj.wmsProduct.wprId+"')";
  			var title = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.salOrdCon.sodTil+"</a>";
  			var name = "<a href='javascript:void(0)' onClick=\""+relFunc+"\">"+obj.wmsProduct.wprName+"</a>";
			datas = [ name,title,obj.ropPrice, obj.ropNum, obj.ropRealPrice, obj.salOrdCon?obj.salOrdCon.sodConDate:"",obj.ropRemark];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "orderAction.do";
			var pars = [];
			pars.op = "listCusPro";
			pars.cusId="${cusId}";
			var sortFunc = "loadList";
			var cols=[
				{name:"品名规格",align:"left"},
				{name:"对应订单",align:"left"},
				{name:"销售单价",align:"right",renderer:"money"},
				{name:"销售数量"},
				{name:"销售总价",align:"right",renderer:"money"},
				{name:"销售时间",renderer:"date"},
				{name:"备注"}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
    	var gridEl = new MGrid("ordProd","dataList");
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
                <th>产品历史</th>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   		</div>
  </body>
</html>


<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>产品销售明细</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			
			var pshNum = "";
			var cusName = "";
			var empName = "";
			if(obj.rshpShipment){
				pshNum="<a href='javascript:void(0)' onclick=\"descPop('orderAction.do?op=showShipmentDesc&pshId="+obj.rshpShipment.pshId+"')\">"+obj.rshpShipment.pshNum+"</a>";
			}
			if(obj.rshpShipment.pshOrder&&obj.rshpShipment.pshOrder.cusCorCus){
				cusName = obj.rshpShipment.pshOrder.cusCorCus.corName;
				empName = obj.rshpShipment.pshOrder.cusCorCus.salEmp?obj.rshpShipment.pshOrder.cusCorCus.salEmp.seName:"";
			}
			datas = [pshNum,cusName,empName,obj.rshpCount, obj.rshpPrice, obj.rshpProdAmt, obj.rshpSalBack,obj.rshpShipment.pshDeliveryDate];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "prodAction.do";
			var pars = [];
			pars.op = "listProdHis";
			pars.prodId = "${prodId}";
			var loadFunc = "loadList";
			var cols=[
				{name:"发货单编号"},
				{name:"客户名称",align:"left"},
				{name:"业务员"},
				{name:"数量",align:"right"},
				{name:"单价",align:"right",renderer:"money"},
				{name:"金额",align:"right",renderer:"money"},
				{name:"提成金额",align:"right",renderer:"money"},
				{name:"发货日期",renderer:"date"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("salHistoryListTab","dataList");
    	gridEl.config.sortable = false;
		createProgressBar();
		window.onload=function(){
			loadList();
		}
    </script>
  </head>
  
  <body>
  		<div class="divWithScroll2 innerIfm">
		<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>销售明细</th>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>
</html>

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
    <title>统计中发货记录列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/sal.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
	
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.pshId;
			var dblFunc="descPop('orderAction.do?op=showShipmentDesc&pshId="+dataId+"')";

			var pshNum="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.pshNum+"</a>";
			var pshOrd="";
			if(obj.pshOrder != undefined){
				var relFunc="descPop('orderAction.do?op=showOrdDesc&code="+obj.pshOrder.sodCode+"')";
                pshOrd="<a href='javascript:void(0)' title=\""+obj.pshOrder.sodNum+"\" onclick=\""+relFunc+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看订单详情' style='border:0;'>"+obj.pshOrder.sodTil+"</a>";
			}

			datas = [pshNum,pshOrd,obj.pshConsignee,obj.pshAddr, obj.pshProdAmt, obj.pshSalBack, obj.pshExpress, obj.pshAmt, obj.pshDeliveryDate, obj.pshShipper, obj.pshRemark ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "statAction.do";
			var pars = [];
			pars.op = "listStatProd";
			pars.cusId = "${cusId}";
			pars.startDate = "${startDate}";
			pars.endDate = "${endDate}";
			
			
			var loadFunc = "loadList";
			var cols=[
				{name:"编号"},
				{name:"对应订单"},
				{name:"收货方",align:"left"},
				{name:"收货地址"},
				{name:"发货金额",align:"right",renderer:"money"},
				{name:"提成金额",align:"right",renderer:"money"},
				{name:"物流公司"},
				{name:"总运费",align:"right",renderer:"money"},
				{name:"发货日期",renderer:"date"},
				{name:"发货人"},
				{name:"备注",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("pshipmentListStatTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
  		<div id="contentbox" style="padding:10px;">
	        <div style="background-color:#2D5188; color:#FFF; padding:5px 5px 5px 10px; text-align:left">
	            <span class="bold bigger" title="统计对象">
	            </span>&nbsp;&nbsp;&nbsp;<span title="客户名称">[${cusName}]</span>&nbsp;&nbsp;&nbsp;
	        </div>
	        <div id="dataList" class="dataList"></div>
   		</div>
   	</div>
  </body>
</html>

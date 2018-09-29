<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>发货记录列表(订单详情)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
    
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.pshId;
			var dblFunc="descPop('orderAction.do?op=showShipmentDesc&pshId="+dataId+"')";

			var pshNum="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.pshNum+"</a>";
			var funcCol = "<img src='crm/images/content/detail.gif' alt='查看详细' style='cursor:pointer' onclick=\""+dblFunc+"\"/>&nbsp;&nbsp;<img onClick=\"ordDelDiv(9,"+dataId+")\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [pshNum,obj.pshConsignee,obj.pshAddr, obj.pshProdAmt, obj.pshSalBack, obj.pshExpress, obj.pshAmt, obj.pshDeliveryDate, obj.pshShipper, obj.pshRemark, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "orderAction.do";
			var pars = [];
			pars.op = "listOrdPShipment";
			pars.ordId = "${ordId}";
			var loadFunc = "loadList";
			var cols=[
				{name:"编号"},
				{name:"收货方",align:"left"},
				{name:"收货地址"},
				{name:"发货金额",align:"right",renderer:"money"},
				{name:"提成金额",align:"right",renderer:"money"},
				{name:"物流公司"},
				{name:"总运费",align:"right",renderer:"money"},
				{name:"发货日期",renderer:"date"},
				{name:"发货人"},
				{name:"备注",isSort:false},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("ordShipmentListTab","dataList");
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
                <th>发货记录</th>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>

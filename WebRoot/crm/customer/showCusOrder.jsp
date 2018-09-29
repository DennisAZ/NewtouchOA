<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>客户订单</title>
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
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	
    <script type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
     		var dblFunc = "descPop('orderAction.do?op=showOrdDesc&code="+obj.sodCode+"')";
  			//var relFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var state = "";
			if(obj.erpSalOrder.orderStatus == '0'){
			state = "新建";
			}
			if(obj.erpSalOrder.orderStatus == '1'){
			state = "审核中";
			}else if(obj.erpSalOrder.orderStatus == '4'){
				state = "执行中";
			}else if(obj.erpSalOrder.orderStatus == '5'){
				state = "完成";
			}else {
				state = "未知";
			}
			var sodTil = obj.erpSalOrder.orderTitle;
			var ordCus=obj.erpSalOrder.erpOrderCus.cusName;
			//if(obj.cusCorCus != null){
	        //  	ordCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			//}
			var shipName = "";
			//if(obj.sodShipState !=null ){
			//	shipName= obj.sodShipState.typName;
			//}
			var orderTypName= "";
			//if(obj.salOrderType !=null){
			//	orderTypName = obj.salOrderType.typName;
			//}
			var funcCol = "<img src='crm/images/content/detail.gif' alt='查看详细' style='cursor:pointer' onclick=\""+dblFunc+"\"/>";
			//if(obj.sodAppIsok =="0"){
			//	funcCol += "&nbsp;&nbsp;<img onClick=\"parent.cusPopDiv(104,"+obj.sodCode+")\" style='cursor:pointer' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"parent.cusDelDiv(7,"+obj.sodCode+",'1')\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			//}
			datas = [state, sodTil, obj.erpSalOrder.orderCode, ordCus,  obj.alreadyTotal, obj.total,obj.salePaid,obj.erpSalOrder.signDate,obj.erpSalOrder.salesPerson];
			return [datas,className,null,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "orderCusAction.do";
			var pars = [];
			pars.op = "listOrders";
			pars.cusId="${cusId}";
			pars.cusName="${cusName}";
			var loadFunc = "loadList";
			var cols=[
				{name:"审核"},
				//{name:"物流状态"},
				{name:"订单主题",align:"center"},
				{name:"订单号",align:"center"},
				{name:"对应客户",align:"center"},
				{name:"已回款",align:"center",renderer:"money"},
				{name:"总金额",align:"center",renderer:"money"},
				{name:"运营花费",align:"center",renderer:"money"},
				{name:"签订日期",renderer:"date"},
				//{name:"交货期",renderer:"date"},
				{name:"签单人"}
				//{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
    	var gridEl = new MGrid("cusOrderTab","dataList");
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//closeProgressBar();
		}
	</script>
  </head>
  
  <body>
     <div class="divWithScroll2 innerIfm">
     	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
				<th>对应订单</th>
                <td>
                  <!-- <a href="javascript:void(0)" onClick="parent.cusPopDiv(103,'${cusId}');return false;" class="newBlueButton">新建订单合同</a> -->
               </td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
	</div>			
  </body>

</html>

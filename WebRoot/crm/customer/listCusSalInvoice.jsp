<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应开票记录</title>
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
		//createIfmLoad('ifmLoad');//进度条
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var relFunc1="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var relFunc2 = "";
			if(obj.erpSalOrder != null){
				relFunc2 ="descPop('orderAction.do?op=showOrdDesc&code="+obj.erpSalOrder.orderId+"')";
			}
			var sinCus="";
			if(obj.cusCorCus != null){
	            sinCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc1+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			}
			var sinOrd="";
			if(obj.erpSalOrder != null){
	            sinOrd="<a href='javascript:void(0)' title=\""+obj.erpSalOrder.orderTitle+"\" onclick=\""+relFunc2+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看订单详情' style='border:0;'>"+obj.erpSalOrder.orderTitle+"</a>";
			}
			var sinIsrecieve = "";
			if(obj.sinIsrecieve == "1"){
				sinIsrecieve="是";
			}
			else {
				sinIsrecieve="否";
			}
			
			var funcCol= "";
			if(obj.salOrdCon != null ){
				funcCol = "<img class='imgAlign' onClick=\"parent.cusPopDiv(100,"+obj.sinId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.cusDelDiv(14,'"+obj.sinId+"','1')\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/";
			}
			else{
				funcCol = "<img class='imgAlign' onClick=\"parent.cusPopDiv(100,"+obj.sinId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.cusDelDiv(14,'"+obj.sinId+"','1')\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/";
			}
			datas = [sinIsrecieve, sinCus, sinOrd, obj.sinMon, obj.typeList?obj.typeList.typName:"", obj.sinDate, obj.person?obj.person.userName:"", obj.sinRemark, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = [];
			pars.op = "listCusSalInvoice";
			pars.cusId = "${cusId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否收票记录"},
				{name:"对应客户",align:"left"},
				{name:"对应订单",align:"left"},
				{name:"票据金额",renderer:"money",align:"right"},
				{name:"票据类型"},
				{name:"开票日期",renderer:"date"},
				{name:"开票人"},
				{name:"备注",isSort:false,align:"left"},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("cusSalInvoiceTab","dataList");
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
                <th>开票记录</th>
                <td><a href="javascript:void(0)" onClick="parent.cusPopDiv(99,'${cusId}');return false;" class="newBlueButton">新建开票记录</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>

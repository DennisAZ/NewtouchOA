<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应回款记录</title>
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
			if(obj.salOrdCon != null){
				relFunc2 ="descPop('orderAction.do?op=showOrdDesc&code="+obj.salOrdCon.sodCode+"')";
			}
			var psCus="";
			if(obj.cusCorCus != null){
	            psCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc1+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			}
			var psOrd="";
			if(obj.salOrdCon != null){
	            psOrd="<a href='javascript:void(0)' title=\""+obj.salOrdCon.sodTil+"\" onclick=\""+relFunc2+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看订单详情' style='border:0;'>"+obj.salOrdCon.sodTil+"</a>";
			}
			var psIsInv = "";
			if(obj.spsIsinv == "1"){
				psIsInv="已开票";
			}
			else {
				psIsInv="<span class='gray'>未开票</span>";
			}
			
			var funcCol= "";
			if(obj.salOrdCon != null ){
				funcCol = "<img class='imgAlign' onClick=\"parent.cusPopDiv(102,"+obj.spsId+")\" src='images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.cusDelDiv(10,'"+obj.spsId+"','1')\" style='cursor:pointer' src='images/content/del.gif' alt='删除'/";
			}
			else{
				funcCol = "<img class='imgAlign' onClick=\"parent.cusPopDiv(102,"+obj.spsId+")\" src='images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.cusDelDiv(10,'"+obj.spsId+"','1')\" style='cursor:pointer' src='images/content/del.gif' alt='删除'/";
			}
			datas = [psIsInv, psCus, psOrd, obj.spsPayMon, obj.spsPayType, obj.spsFctDate, obj.salEmp?obj.salEmp.seName:"", obj.spsContent, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "paidAction.do";
			var pars = [];
			pars.op = "listCusPaidPast";
			pars.cusId = "${cusId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已开票"},
				{name:"对应客户",align:"left"},
				{name:"对应订单",align:"left"},
				{name:"回款金额",renderer:"money",align:"right"},
				{name:"付款方式"},
				{name:"回款日期",renderer:"date"},
				{name:"回款人"},
				{name:"备注",isSort:false,align:"left"},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("cusPaidPastTab","dataList");
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
                <th>回款记录</th>
                <td><!-- <a href="javascript:void(0)" onClick="parent.cusPopDiv(101,'${cusId}');return false;" class="newBlueButton">新建回款记录</a> --></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>

<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>客户统计数据列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/cus.js"></script>
	<script type="text/javascript">
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
			datas = [psIsInv, psCus, psOrd, obj.spsPayMon, obj.spsPayType, obj.spsFctDate, obj.salEmp?obj.salEmp.seName:"", obj.spsContent];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "statAction.do";
			var pars = [];
			pars.op = "listStatSalPast";
			pars.cusId = "${cusId}";
			pars.startDate = "${startDate}";
			pars.endDate = "${endDate}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已开票"},
				{name:"对应客户",align:"left"},
				{name:"对应订单",align:"left"},
				{name:"回款金额",renderer:"money",align:"right"},
				{name:"付款方式"},
				{name:"回款日期",renderer:"date"},
				{name:"回款人"},
				{name:"备注",isSort:false,align:"left"}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
		var gridEl = new MGrid("pastStatListTab","dataList");
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

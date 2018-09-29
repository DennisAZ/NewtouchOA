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
    <title>发货明细统计数据列表</title>
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
			var dblFunc="descPop('prodAction.do?op=wmsProDesc&wprId="+obj.rshpProd.wprId+"')";
			var proName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.rshpProd.wprName+"</a>";
			datas = [proName, obj.rshpShipment.pshDeliveryDate, obj.rshpCount, obj.rshpProdAmt, obj.rshpPrice, obj.rshpSalBack, obj.rshpOutCount];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "statAction.do";
			var pars = [];
			pars.op = "liststatPsalBack";
			pars.wprId = "${wprId}";
			pars.startDate = "${startDate}";
			pars.endDate = "${endDate}";
			pars.empId = "${empId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"名称",align:"left"},
				{name:"发货日期",renderer:"date"},
				{name:"发货数量",align:"left"},
				{name:"发货金额",renderer:"money",align:"right"},
				{name:"单价",renderer:"money",align:"right"},
				{name:"提成",renderer:"money",align:"right"},
				{name:"外购件数量"}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
		var gridEl = new MGrid("pastStatBackListTab","dataList");
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
            </span>&nbsp;&nbsp;&nbsp;<span title="员工姓名">[${empName}]</span>&nbsp;&nbsp;&nbsp;
        </div>
    	<div id="dataList" class="dataList"></div>
    </div>
    </div>
  </body>
</html>

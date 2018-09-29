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
    <title>订单统计数据列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    
	<script type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dblFunc="descPop('orderAction.do?op=showOrdDesc&code="+obj.sodCode+"')";
			var appState ;
			if(obj.sodAppIsok=="0"){
				appState = "<img src='images/content/fail.gif' alt='未审核'>";
			}
			else{
				appState = "<img src='images/content/suc.gif' alt='已审核'>";
			}
			var ordTil="<a href='javascript:void(0)' onclick=\""+dblFunc+";return false;\">"+obj.sodTil+"</a>";
			var ordCus="";
			if(obj.cusCorCus!=undefined){
				var relFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
				ordCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			}
			datas = [appState, obj.sodShipState?obj.sodShipState.typName:'', obj.salOrderType?obj.salOrderType.typName:'', ordTil, obj.sodNum, ordCus, obj.sodPaidMon, obj.sodSumMon, obj.sodConDate, obj.salEmp?obj.salEmp.seName:'' ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "statAction.do";
			var pars = [];
			pars.op="listStatOrd";
			pars.statType="${statType}";
			pars.statItem="<c:out value="${statItem}"/>";
			pars.startDate="${startDate}";
			pars.endDate="${endDate}";
			pars.empId="${empId}";
			pars.nodeIds="${nodeIds}";	
			var loadFunc = "loadList";
			var cols=[
				{name:"审核"},
				{name:"订单状态"},
				{name:"类别"},
				{name:"订单主题",align:"left"},
				{name:"订单号",align:"left"},
				{name:"对应客户",align:"left"},
				{name:"已回款",renderer:"money",align:"right"},
				{name:"总金额",renderer:"money",align:"right"},
				{name:"签订日期",renderer:"date"},
				{name:"签单人"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		var gridEl = new MGrid("ordStatListTab","dataList");
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
                <c:if test="${!empty statItem}">${statItem}</c:if>
                <c:if test="${empty statItem}">合计</c:if>
            </span>&nbsp;&nbsp;&nbsp;
            <c:if test="${!empty empName}"><span title="负责人">[${empName}]</span>&nbsp;&nbsp;&nbsp;</c:if>
        </div>
    	<div id="dataList" class="dataList"></div>
    </div>
    </div>
  </body>
</html>

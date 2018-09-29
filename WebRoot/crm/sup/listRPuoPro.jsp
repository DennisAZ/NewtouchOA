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
    <title>采购单明细(采购单详情)</title>
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
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/sup/sup.js"></script>
    <script type="text/javascript">
		//createIfmLoad('ifmLoad');//进度条
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.rppId;
			var relFunc1 = "";
			var puo = "";
			if(obj.purOrd != undefined){
				relFunc1 = "descPop('supAction.do?op=toDescPurOrd&puoId="+obj.purOrd.puoId+"')";
				puo = "<a href='javascript:void(0)' onClick=\""+relFunc1+"\" >"+obj.purOrd.puoCode+"</a>";
			}
			datas = [puo,obj.rppNum,obj.rppPrice, obj.rppSumMon, obj.rppOutNum, obj.rppRealNum, obj.rppRemark];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "supAction.do";
			var pars = [];
			pars.op = "listRPuoPro";
			pars.wprId="${wprId}";
			var sortFunc = "loadList";
			var cols=[
				{name:"采购单号",align:"left"},
				{name:"采购数量"},
				{name:"采购单价",renderer:"money",align:"right"},
				{name:"总金额",renderer:"money",align:"right"},
				{name:"已安排入库数量"},
				{name:"实际入库数量"},
				{name:"备注"}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
    	var gridEl = new MGrid("rPuoPro","dataList");
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
                <th>采购单明细</th>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   		</div>
  </body>
</html>


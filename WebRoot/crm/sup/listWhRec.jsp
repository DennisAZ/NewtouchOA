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
    <title>库存变更记录列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript">
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.wreId;
			var dblFunc="";
			var type="";
			if(obj.wreType=="0"){
				type = "出库";
			}else if(obj.wreType=="1"){
				type = "入库";
			}else{
				type = "库存数量变更";
			}
			datas = [obj.wreCount, type, obj.wreTime, obj.wreMan, obj.wreLeftCount];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "supAction.do";
			var pars = [];
			pars.op = "listWhRec";
			pars.pstId = "${pstId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"变更数量"},
				{name:"变更类型"},
				{name:"变更时间",renderer:"time"},
				{name:"经手人"},
				{name:"剩余库存"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("listWhRecTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
  		<div id="contentbox" style="padding:10px;">
	        <div id="dataList" class="dataList"></div>
   		</div>
   	</div>
  </body>
</html>

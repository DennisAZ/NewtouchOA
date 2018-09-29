<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>选择产品</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#FFFFFF
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dblFunc="chooseProduct('"+obj.wprId+"','"+escape(obj.wprName)+"','${type}')";
			var wprName = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.wprName+"</a>";
			var prodPrc=0;
			if(obj.wprSalePrc>0){
				prodPrc = "￥"+changeTwoDecimal(obj.wprSalePrc)+"&nbsp;<span class='deepGray'>(不含税"+changeTwoDecimal(obj.wprSalePrc/(1+getSalTaxRate()))+")</span>";
			}
			else{
				prodPrc = "￥"+changeTwoDecimal(prodPrc);
			}
			datas = [obj.wprCode, wprName, prodPrc, obj.proType,""];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "prodAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listProductToChoose";
			var loadFunc = "loadList";
			var cols=[
				{name:"编号",align:"left"},
				{name:"产品名称"},
				{name:"标准价格"},
				{name:"品名规格",align:"left"},
				{name:"单位"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("selProdListTab","dataList");
    	createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-130,5);
		}
	  </script>
  </head>
    <body>
  	<div class="browLayer">
    	<div class="listSearch">
        	<form id="searchForm" onSubmit="loadList();return false;">
                产品编号：<input class="inputSize2 inputBoxAlign" type="text"name="proCode" id="proCode"/>&nbsp;&nbsp;
                品名规格：<input class="inputSize2 inputBoxAlign" type="text"name="proName" id="proName"/>&nbsp;&nbsp;
              	<input class="inputBoxAlign butSize3" type="submit" value="查询" />&nbsp;&nbsp;
            </form>
        </div>
        <div id="dataList" class="dataList"></div>
    </div>
  </body>
</html>

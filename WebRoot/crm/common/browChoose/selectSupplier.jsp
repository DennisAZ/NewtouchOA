<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>选择供应商</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#FFFFFF
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.supId;
			dblFunc="chooseSupplier('"+obj.supId+"','"+escape(obj.supName)+"')";
			var supName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.supName+"</a>";
			datas = [supName, obj.supCode, obj.supType?obj.supType.typName:"", obj.supContactMan];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "supAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listSupplierToChoose";
			var loadFunc = "loadList";
			var cols=[
				{name:"名称",align:"left"},
				{name:"编号",align:"left"},
				{name:"类别"},
				{name:"联系人"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("selSupListTab","dataList");
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
                供应商编号：<input class="inputSize2 inputBoxAlign" type="text" name="supCode" id="supCode"/>&nbsp;&nbsp;
                供应商名称：<input class="inputSize2 inputBoxAlign" type="text" name="supName" id="supName"/>&nbsp;&nbsp;
              	<input class="inputBoxAlign butSize3" type="submit" value="查询" />&nbsp;&nbsp;
            </form>
        </div>
        <div id="dataList" class="dataList"></div>
    </div>
  </body>
</html>

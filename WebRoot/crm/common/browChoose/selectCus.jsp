<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>选择客户</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/cus.js"></script>
    
    <script type="text/javascript" src="js/chooseBrow.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dblFunc="chooseCus('"+obj.corCode+"','"+escape(obj.corName)+"')";
			var corName = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.corName+"</a>";
			datas = [obj.corNum, corName, obj.salEmp?obj.salEmp.seName:""];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listCustomersToChoose";
			var loadFunc = "loadList";
			var cols=[
				{name:"编号",align:"left"},
				{name:"客户名称",align:"left"},
				{name:"负责人"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("selCusListTab","dataList");
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
                客户编号：<input class="inputSize2 inputBoxAlign" type="text"name="corNum" id="corNum"/>&nbsp;&nbsp;
                客户名称：<input class="inputSize2 inputBoxAlign" type="text"name="corName" id="corName"/>&nbsp;&nbsp;
              	<input class="inputBoxAlign butSize3" type="submit" value="查询" />&nbsp;&nbsp;
            </form>
        </div>
        <div id="dataList" class="dataList"></div>
    </div>
  </body>
</html>

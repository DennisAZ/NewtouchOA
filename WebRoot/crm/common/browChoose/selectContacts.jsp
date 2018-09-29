<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>选择联系人</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dblFunc="chooseContact('"+obj.conId+"','"+escape(obj.conName)+"')";
			var conName = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.conName+"</a>";
			datas = [conName, obj.conSex,obj.conLev, obj.conDep,obj.conPos];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listContactsToChoose";
			pars.cusId = "${cusId}";
			var loadFunc = "loadList";
			var cols=[
				{name:"姓名",align:"left"},
				{name:"性别"},
				{name:"分类"},
				{name:"部门"},
				{name:"职务"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("selContactListTab","dataList");
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
                姓名：<input class="inputSize2 inputBoxAlign" type="text"name="conName" id="conName"/>&nbsp;&nbsp;
              	<input class="inputBoxAlign butSize3" type="submit" value="查询" />&nbsp;&nbsp;
            </form>
        </div>
        <div id="dataList" class="dataList"></div>
    </div>
  </body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>选择采购单</title>
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
			dataId = obj.id;
			dblFunc="choosePurOrd('"+obj.id+"','"+escape(obj.code)+"')";
			var puoCode="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.code+"</a>";
			var supplier="";
			if(obj.puoSup != null){
				var relFunc1="descPop('supAction.do?op=toDescSup&supId="+obj.puoSup.supId+"')";
	            supplier="<a href='javascript:void(0)' title=\""+obj.puoSup.supName+"\" onclick=\""+relFunc1+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.puoSup.supName+"</a>";
			}
			datas = [puoCode, obj.supplier[0].supName,obj.paidPlan[0].total,obj.signDate,obj.purchaser];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "supAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listPurOrdToChooseERP";
			var loadFunc = "loadList";
			var cols=[
				{name:"编号",align:"left"},
				//{name:"类别"},
				{name:"供应商",align:"left"},
				{name:"采购金额",renderer:"money",align:"right"},
				{name:"采购日期",renderer:"date"},
				{name:"采购人"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("selPurOrdListTab","dataList");
    	createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			//createCancelButton(loadList,'searchForm',-130,5);
		}
	  </script>
  </head>
    <body>
  	<div class="browLayer">
  	
    	<div >
        	<form id="searchForm" onSubmit="loadList();return false;">
                <!-- 采购单编号：<input class="inputSize2 inputBoxAlign" type="text" name="puoCode" id="puoCode"/>&nbsp;&nbsp;
                供应商名称：<input class="inputSize2 inputBoxAlign" type="text" name="supName" id="supName"/>&nbsp;&nbsp;
              	<input class="inputBoxAlign butSize3" type="submit" value="查询" />&nbsp;&nbsp;-->
            </form>
        </div> 
        <div id="dataList" class="dataList"></div>
    </div>
  </body>
</html>

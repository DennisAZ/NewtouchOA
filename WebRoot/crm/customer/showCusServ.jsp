<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应客户</title>
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
    <script type="text/javascript" src="crm/js/cus.js"></script>
    
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
  			var dblFunc = "descPop('cusServAction.do?op=showCusServInfo&serCode="+obj.serCode+"')";
			var descFunc1 = "descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var title = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\" style='cursor:'>"+obj.serTitle+"</a>";
			var customer = "<span class='mLink'><a title=\""+obj.cusCorCus.corName+"\" href='javascript:void(0)' class='hand' onClick=\""+descFunc1+"\"><img src='crm/images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0;'>"+obj.cusCorCus.corName+"</a></span>";
			var date ="";
			if(obj.serExeDate != undefined){
				date = obj.serExeDate.substring(0,10);
			}
			var funcCol = "<img src='crm/images/content/detail.gif' onClick=\""+dblFunc+"\" class='hand' alt='查看详细'/>&nbsp;&nbsp;<img onClick=\"parent.cusPopDiv(51,"+obj.serCode+",'1')\" style='cursor:pointer' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"parent.cusDelDiv(5,"+obj.serCode+",'1')\" style='cursor:pointer' src='crm/images/content/del.gif' border='0' alt='删除'/>";
			datas = [obj.serState, title, customer, obj.serMethod, obj.serContent, obj.salEmp?obj.salEmp.seName:"", date, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "cusServAction.do";
			var pars = [];
			pars.op = "getCusServ";
			pars.cusId="${cusId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"状态"},
				{name:"主题",align:"left"},
				{name:"对应客户",align:"left"},
				{name:"服务方式"},
				{name:"客服内容"},
				{name:"处理人"},
				{name:"处理日期"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("cusServTab","dataList");
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//closeProgressBar();
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
    	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>客户服务</th>
                <td><a href="javascript:void(0)" onClick="parent.cusPopDiv(5,'${cusId}');return false;" class="newBlueButton">新建客户服务</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>
</html>

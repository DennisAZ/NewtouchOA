<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应付款记录</title>
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
    <script type="text/javascript" src="crm/sup.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	
    <script type="text/javascript">
		//createIfmLoad('ifmLoad');//进度条
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var relFunc1="descPop('supAction.do?op=toDescSup&supId="+obj.supplier.supId+"')";
			var relFunc2 = "";
			if(obj.purOrd != null){
				relFunc2 ="descPop('supAction.do?op=toDescPurOrd&puoId="+obj.purOrd.puoId+"')";
			}
			var psSup="";
			if(obj.supplier != null){
	            psSup="<a href='javascript:void(0)' title=\""+obj.supplier.supName+"\" onclick=\""+relFunc1+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.supplier.supName+"</a>";
			}
			var psPurOrd="";
			if(obj.purOrd != null){
	            psPurOrd="<a href='javascript:void(0)' title=\""+obj.purOrd.puoCode+"\" onclick=\""+relFunc2+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看采购单详情' style='border:0;'>"+obj.purOrd.puoCode+"</a>";
			}
			var psIsInv = "";
			if(obj.sppIsinv == "1"){
				psIsInv="已开票";
			}
			else {
				psIsInv="<span class='gray'>未开票</span>";
			}
			
			var funcCol = "<img class='imgAlign' onClick=\"parent.supPopDiv(15,"+obj.supplier.supId+","+obj.sppId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.supDelDiv(7,'"+obj.sppId+"','1')\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/";
			datas = [psIsInv, psSup, psPurOrd, obj.sppPayMon, obj.sppPayType, obj.sppFctDate, obj.salEmp?obj.salEmp.seName:"", obj.sppContent, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "supAction.do";
			var pars = [];
			pars.op = "listSupPaidPast";
			pars.supId = "${supId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已开票"},
				{name:"对应供应商",align:"left"},
				{name:"对应采购单",align:"left"},
				{name:"付款金额",renderer:"money",align:"right"},
				{name:"付款方式"},
				{name:"付款日期",renderer:"date"},
				{name:"付款人"},
				{name:"备注",isSort:false,align:"left"},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("supPaidPastTab","dataList");
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
                <th>付款记录</th>
                <td><a href="javascript:void(0)" onClick="parent.supPopDiv(14,'${supId}');return false;" class="newBlueButton">新建付款记录</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>

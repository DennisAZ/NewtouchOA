<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>回款计划列表(订单详情)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
    
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
    	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var plIsP="";
            if(obj.spdIsp =="1"){
                plIsP ="<img class='imgAlign' src='crm/images/content/suc.gif' alt='已回款'/>";
            }
            else {
                plIsP ="<span id='toPayDiv"+obj.spdId+"'><span class='red'>未回</span>&nbsp;<img onClick=\"completePaid("+obj.spdId+")\" style='cursor:pointer' class='imgAlign' src='crm/images/content/execute.gif' alt='回款'/></span><span id='paidDiv"+obj.spdId+"' style='display:none'><img class='imgAlign' src='crm/images/content/suc.gif' alt='已回款'/></span>";
            }
			var funcCol = "<img class='imgAlign' onClick=\"parent.ordPopDiv(31,"+obj.spdId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.ordDelDiv(1,"+obj.spdId+",'1')\" src='crm/images/content/del.gif' style='cursor:pointer' alt='删除'/>";
			datas = [plIsP, obj.spdPayMon, obj.spdPrmDate, obj.spdResp.seName, obj.spdContent, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "paidAction.do";
			var pars = [];
			pars.op = "listOrdSpp";
			pars.ordId="${ordId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已回款"},
				{name:"回款金额",renderer:"money",align:"right"},
				{name:"回款日期",renderer:"date"},
				{name:"负责人"},
				{name:"备注",isSort:false},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("ordPpListTab","dataList");
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
                <th>回款计划</th>
                <td><a href="javascript:void(0)" class="newBlueButton"  onClick="parent.ordPopDiv(3);return false;">新建回款计划</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>

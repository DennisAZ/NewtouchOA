<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>回款记录列表(订单详情)</title>
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
			var psIsInv ="";
            if(obj.spsIsinv =="1"){
                psIsInv="已开票";
            }
            else {
                psIsInv ="<span class='gray'>未开票</span>";
            }
            var funcCol = "";
            if(obj.salOrdCon !=null){
            	funcCol = "<img class='imgAlign' onClick=\"parent.ordPopDiv(42,"+obj.spsId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.ordDelDiv(2,'"+obj.spsId+"','1')\" src='crm/images/content/del.gif' style='cursor:pointer' alt='删除'/>";
            }
            else{
            	funcCol = "<img class='imgAlign' onClick=\"parent.ordPopDiv(42,"+obj.spsId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"parent.ordDelDiv(2,'"+obj.spsId+"','1')\" src='crm/images/content/del.gif' style='cursor:pointer' alt='删除'/>";
            }
			datas = [psIsInv, obj.spsPayMon, obj.spsPayType, obj.spsFctDate, obj.salEmp.seName, obj.spsContent, funcCol ];

			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "paidAction.do";
			var pars = [];
			pars.op = "listOrdSps";
			pars.ordId="${ordId}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已开票"},
				{name:"回款金额",align:"right",renderer:"money"},
				{name:"付款方式"},
				{name:"回款日期",renderer:"date"},
				{name:"回款人"},
				{name:"备注",isSort:false},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("ordPsListTab","dataList");
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
                <th>回款记录</th>
                <td><a href="javascript:void(0)" class="newBlueButton"  onClick="parent.ordPopDiv(4);return false;">新建回款记录</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应销售机会</title>
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
		//createIfmLoad('ifmLoad');//进度条
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.oppId;
	        var dblFunc="descPop('cusServAction.do?op=showSalOppInfo&id="+dataId+"')";
			var relFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var oppTil="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.oppTitle+"</a>";
			var oppCus="<span class='mLink'><a href='javascript:void(0)' onclick=\""+relFunc+"\"><img src='images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0;'>"+obj.cusCorCus.corName+"</a></span>";
			var oppExeDate = "";
			if(obj.oppExeDate !=undefined){
				oppExeDate = obj.oppExeDate.substring(0,10);
			}
			else{
				oppExeDate = "<span class='gray'>未设定</span>";
			}
			var oppPos="";
			if(obj.oppPossible != undefined){
				oppPos=obj.oppPossible+"%";
			}
			var oppDCount="";
			if(obj.oppState =="跟踪"){
				oppDCount=obj.oppDayCount+"天";
			}
			var oppStage = "";
			if(obj.oppStage != undefined){
				oppStage = obj.oppStage.typName;
			}
			var funcCol = "<img src='images/content/detail.gif' class='hand' onClick=\""+dblFunc+"\"alt='查看详细'/>";
			if(obj.salEmp&&obj.salEmp.seNo=="${CUR_EMP.seNo}"){
				funcCol += "&nbsp;&nbsp;<img onClick=\"parent.cusPopDiv(31,"+dataId+",'1')\" style='cursor:pointer' src='images/content/edit.gif' border='0' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"parent.cusDelDiv(3,"+dataId+",'1')\" style='cursor:pointer' src='images/content/del.gif' alt='删除'/>";
			}
			datas = [obj.oppState, oppTil, obj.oppLev, oppCus, oppExeDate,obj.oppFindDate,oppStage ,oppPos, oppDCount, obj.salEmp1?obj.salEmp1.seName:"", funcCol];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "cusServAction.do";
			var pars = [];
			pars.op = "getCusOpp";
			pars.cusId="${cusId}";
			var sortFunc = "loadList";
			var cols=[
				{name:"状态"},
				{name:"机会主题",align:"left"},
				{name:"机会热度"},
				{name:"对应客户",align:"left"},
				{name:"关注日期"},
				{name:"发现日期",renderer:"date"},
				{name:"阶段"},
				{name:"可能性"},
				{name:"停留阶段",isSort:false},
				{name:"负责人"},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("cusOppTab","dataList");
    	createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//closeProgressBar();
			//loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
    	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>销售机会</th>
                <td><a href="javascript:void(0)" onClick="parent.cusPopDiv(3,'${cusId}');return false;" class="newBlueButton">新建销售机会</a></td>
            </tr>
        </table>
        <div id="dataList" class="dataList"></div>
   </div>
  </body>

</html>

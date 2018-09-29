<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>回款计划列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
    
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		function loadFilter(){
			setCuritemStyle($("filter").value,["7","15","30","expired","all"]);
		}
		//传入ajax的回调方法
		function toLoadTab(){
			//加载头部标签样式
			loadListTab('${range}',['0','1']);
		}
		
		//标签跳转链接
		function tabReload(range){
			self.location.href = "paidAction.do?op=toListPaidPlan&range=" + range;
		}
		//列表筛选按钮链接
		function filterList(filter){
			$("filter").value=filter;
			loadList();
		}
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var relFunc1="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var relFunc2 = "";
			if(obj.salOrdCon != null){
				relFunc2="descPop('orderAction.do?op=showOrdDesc&code="+obj.salOrdCon.sodCode+"')";
			}
			var plCus="";
			if(obj.cusCorCus !=null){
               plCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc1+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			}
			var plOrd="";
			if(obj.salOrdCon != null){
               plOrd="<a href='javascript:void(0)' title=\""+obj.salOrdCon.sodTil+"\" onclick=\""+relFunc2+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看订单详情' style='border:0;'>"+obj.salOrdCon.sodTil+"</a>";
			}
			var plIsP ="";
			if(obj.spdIsp =="1"){
				plIsP ="<img class='imgAlign' src='crm/images/content/suc.gif' alt='已回款'/>";
			}
			else {
				plIsP ="<span id='toPayDiv"+obj.spdId+"'><span class='red'>未回</span>&nbsp;<img onClick=\"completePaid("+obj.spdId+")\" style='cursor:pointer' class='imgAlign' src='crm/images/content/execute.gif' alt='回款'/></span><span id='paidDiv"+obj.spdId+"' style='display:none'><img class='imgAlign' src='crm/images/content/suc.gif' alt='已回款'/></span>";
			}
			
			var funcCol = "<img class='imgAlign' onClick=\"ordPopDiv(31,"+obj.spdId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"ordDelDiv(1,"+obj.spdId+")\" src='crm/images/content/del.gif' style='cursor:pointer' alt='删除'/>";
			datas = [plIsP, plCus, plOrd, obj.spdPayMon, obj.spdPrmDate, obj.spdResp?obj.spdResp.seName:"", obj.spdContent, funcCol];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "paidAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listPaidPlan";
			pars.range="${range}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已回款"},
				{name:"对应客户",align:"left"},
				{name:"对应订单",align:"left"},
				{name:"回款金额",align:"right",renderer:"money"},
				{name:"回款日期",renderer:"date"},
				{name:"负责人"},
				{name:"备注",isSort:false,align:"left"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
			loadFilter();
		}
		
    	var gridEl = new MGrid("ppListTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;销售管理 > 回款计划
            </div>
  	   		<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" onClick="tabReload(0)">我的回款计划</div> 
                            <script type="text/javascript">writeLimAllow("s036","<div id='tabType2' onClick=\"tabReload(1)\">全部回款计划</div>","tabType","bottom",toLoadTab);</script>
                        </div>
                     </th>
                     <td>
                    	<a href="javascript:void(0)" onClick="ordPopDiv(3);return false;" class="newBlueButton">新建回款计划</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
                <div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                        <input type="text" id="filter" name="filter" style="display:none"/>
                        回款日期：<input name="startDate" id="pid" type="text" class="inputSize2 Wdate inputBoxAlign" style="cursor:hand; width:100px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                        到&nbsp;<input name="endDate" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 Wdate inputBoxAlign" style="cursor:hand; width:100px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                         <input type="submit" class="butSize3 inputBoxAlign" value="查询"/>&nbsp;&nbsp;
                   </form>
                </div>
                <div id="topChoose" class="listTopBox">
                    <a href="javascript:void(0)" onClick="filterList('7')">&nbsp;<span class="bold">7</span>天内待回款&nbsp;</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('15')"><span class="bold">15</span>天内待回款</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('30')"><span class="bold">30</span>天内待回款</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('expired')">&nbsp;过期未回款&nbsp;</a>
                    <a href="javascript:void(0)" onClick="filterList('all')">&nbsp;所有未回款&nbsp;</a>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
  		</div> 
	</div>
  </body>
</html>

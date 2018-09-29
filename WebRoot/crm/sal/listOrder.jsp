<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>订单列表</title>
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
			setCuritemStyle($("filter").value,["leftMon","d15","d30","e15","e30","mon","year"]);
		}
		//标签跳转链接
		function reloadTab(range){
			self.location.href = "orderAction.do?op=toListOrders&range=" + range;
		}
		//列表筛选按钮链接
		function filterList(filter){
			$("filter").value=filter;
			loadList();
		}
		
		function chooseCus(){
			addDivBrow(2);
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.sodCode;
			var dblFunc="descPop('orderAction.do?op=showOrdDesc&code="+dataId+"')";
			var appState = "";
			if(obj.sodAppIsok == '0'){
				appState = "<img src='crm/images/content/fail.gif' alt='未审核'>";
			}else if(obj.sodAppIsok == '1'){
				appState = "<img src='crm/images/content/suc.gif' alt='已审核'>";
			}
			var ordTil="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.sodTil+"</a>";
			var ordCus="";
			if(obj.cusCorCus != undefined){
				var relFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
                ordCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			}
			var funcCol = "<img src='crm/images/content/detail.gif' alt='查看详细' style='cursor:pointer' onclick=\""+dblFunc+"\"/>";
			if(obj.sodAppIsok =="0"){
				funcCol+= "&nbsp;&nbsp;<img onClick=\"ordPopDiv(11,"+dataId+")\" style='cursor:pointer' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"ordDelDiv(4,"+dataId+")\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			}
			datas = [appState, ordTil,obj.sodNum, obj.sodShipState?obj.sodShipState.typName:"", obj.salOrderType?obj.salOrderType.typName:"", ordCus, obj.sodPaidMon, obj.sodSumMon, obj.sodConDate, obj.sodComiteDate ,obj.salEmp?obj.salEmp.seName:"", funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "orderAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listOrders";
			pars.range = "${range}";
			var loadFunc = "loadList";
			var cols=[
				{name:"审核"},
				{name:"订单主题",align:"left"},
				{name:"订单号",align:"left"},
				{name:"订单状态"},
				{name:"类别"},
				{name:"对应客户",align:"left"},
				{name:"已回款",align:"right",renderer:"money"},
				{name:"总金额",align:"right",renderer:"money"},
				{name:"签订日期",renderer:"date"},
				{name:"交货期",renderer:"date"},
				{name:"签单人"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
			loadFilter();
		}
		
    	var gridEl = new MGrid("ordListTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadListTab('${range}',['0','1']);
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;销售管理 > 订单合同
            </div>
  	   		<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                        	<div id="tabType1" onClick="reloadTab(0)" class="tabTypeWhite">订单合同</div>                   
                            <div id="tabType2" onClick="reloadTab(1)">历史订单</div>
                        </div>
                    </th>
                    <td>
                    	<a href="javascript:void(0)" onClick="ordPopDiv(1);return false;" class="newBlueButton">新建订单合同</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
           	<div id="listContent">
			 	<div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                        <input type="text" id="filter" name="filter" style="display:none"/>
                        主题：<input name="ordTil" class="inputSize2 inputBoxAlign" style="width:80px" type="text" onBlur="autoShort(this,50)"/>&nbsp;&nbsp;
                        订单号：<input name="ordNum" class="inputSize2 inputBoxAlign" style="width:80px" type="text" onBlur="autoShort(this,30)"/>&nbsp;&nbsp;
                        对应客户：<input id="cusName" name="cusName" class="inputSize2 inputBoxAlign lockBack" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" style="cursor:hand" type="text" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    		 <input id="cusId" name="cusId" type="text" style="display:none"/>
                    <c:if test="${range==0}">
                        订单状态：<c:if test="${!empty stateList}"><select id="stateId" name="stateId" class="inputBoxAlign">
                         			<option  value="">请选择</option>
                                    <c:forEach var="state" items="${stateList}">
                         				<option value="${state.typId}">${state.typName}</option>
                         			</c:forEach>
                             </select></c:if>
                          	<c:if test="${empty stateList}"><select id="stateId" class="inputBoxAlign" disabled><option value="">未添加</option></select></c:if>&nbsp;
                     </c:if>
                        签单人：<input name="seName" class="inputSize2 inputBoxAlign" style="width:100px" type="text" onBlur="autoShort(this,50)"/>&nbsp;&nbsp;  	
                        签订日期：<input name="startDate" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:82px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                          到&nbsp;<input name="endDate" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:82px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                         <input id="searButton" class="butSize3 inputBoxAlign" type="submit" value="查询"/>&nbsp;&nbsp;&nbsp;&nbsp;
                         <!--<a class="supSearButton inputBoxAlign" href="orderAction.do?op=ordSupSearch&isAll=${isAll}&isEmpty=isEmpty" target="_blank" style="float:none">高级查询</a>-->
                   </form>
                </div>
                <div id="topChoose" class="listTopBox">
                	<a href="javascript:void(0)" onClick="filterList('leftMon')">&nbsp;未尽回款</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('d15')" ><span class="bold">15</span>天内需交付</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('d30')" ><span class="bold">30</span>天内需交付</a>&nbsp;
                    <!-- <a href="javascript:void(0)" onClick="filterList('dp')" >&nbsp;过期未全部交付&nbsp;</a>&nbsp; -->
                    <a href="javascript:void(0)" onClick="filterList('e15')" ><span class="bold">15</span>天内结束</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('e30')" ><span class="bold">30</span>天内结束</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('mon')" >&nbsp;本月签订订单&nbsp;</a>&nbsp;
                    <a href="javascript:void(0)" onClick="filterList('year')" >&nbsp;本年签订订单&nbsp;</a>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
  		</div> 
	</div>
  </body>
</html>

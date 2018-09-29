<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>回款记录列表</title>
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
    
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		function chooseCus(){
			addDivBrow(2);
		}
		//标签跳转链接
		function reloadTab(){
			self.location.href = "paidAction.do?op=toListPaidPast";
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var relFunc1="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var relFunc2 = "";
			if(obj.salOrdCon != null){
				relFunc2 ="descPop('orderAction.do?op=showOrdDesc&code="+obj.salOrdCon.sodCode+"')";
			}
			var psCus="";
			if(obj.cusCorCus != null){
	            psCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc1+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			}
			var psOrd="";
			if(obj.salOrdCon != null){
	            psOrd="<a href='javascript:void(0)' title=\""+obj.salOrdCon.sodTil+"\" onclick=\""+relFunc2+"\"><img class='imgAlign' src='crm/images/content/showDesc.gif' alt='查看订单详情' style='border:0;'>"+obj.salOrdCon.sodTil+"</a>";
			}
			var psIsInv = "";
			if(obj.spsIsinv == "1"){
				psIsInv="已开票";
			}
			else {
				psIsInv="<span class='gray'>未开票</span>";
			}
			
			var funcCol= "";
			if(obj.salOrdCon != null ){
				funcCol = "<img class='imgAlign' onClick=\"ordPopDiv(42,"+obj.spsId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"ordDelDiv(2,'"+obj.spsId+"')\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/";
			}
			else{
				funcCol = "<img class='imgAlign' onClick=\"ordPopDiv(42,"+obj.spsId+")\" src='crm/images/content/edit.gif' style='cursor:pointer' alt='编辑'/>&nbsp;&nbsp;<img class='imgAlign' onclick=\"ordDelDiv(2,'"+obj.spsId+"')\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/";
			}
			datas = [psIsInv, psCus, psOrd, obj.spsPayMon, obj.spsPayType, obj.spsFctDate, obj.salEmp?obj.salEmp.seName:"", obj.spsContent, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "paidAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listPaidPast";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已开票"},
				{name:"对应客户",align:"left"},
				{name:"对应订单",align:"left"},
				{name:"回款金额",renderer:"money",align:"right"},
				{name:"付款方式"},
				{name:"回款日期",renderer:"date"},
				{name:"回款人"},
				{name:"备注",isSort:false,align:"left"},
				{name:"操作",isSort:false}
			];
			
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("psListTab","dataList");
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
			//closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;销售管理 > 回款记录
            </div>
  	   		<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" onClick="reloadTab()" class="tabTypeWhite">回款记录1</div>
                        </div>
                     </th>
                     <td>
                    	<a href="javascript:void(0)" onClick="ordPopDiv(4);return false;" class="newBlueButton">新建回款记录1</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
                <div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                          对应客户：<input id="cusId" name="cusName" class="inputSize2 inputBoxAlign lockBack" type="text" style="cursor:hand" ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>&nbsp;&nbsp;
                       回款人：<input type="text" name="seName"  class="inputSize2 inputBoxAlign" style="width:100px"/>&nbsp;&nbsp;
                        回款日期：<input name="startDate" id="pid" type="text" class="inputSize2 Wdate inputBoxAlign" style="cursor:hand; width:100px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                        到&nbsp;<input name="endDate" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 Wdate inputBoxAlign" style="cursor:hand; width:100px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                         <input type="submit" class="butSize3 inputBoxAlign" value="查询"/>&nbsp;&nbsp;
                   </form>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
  		</div> 
	</div>
  </body>
</html>

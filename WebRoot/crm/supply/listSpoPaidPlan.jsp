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
    <title>付款计划列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/spo.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
	
	<script type="text/javascript">
		function completeSpoPaid(id){
			if(confirm("是否确定已付款？")){
				var url = "salPurAction.do";
				var pars = "op=completeSpoPaid&paidId="+id+"&ran=" + Math.random();
				new Ajax.Request(url, {
					method: 'get',
					parameters: pars,
					onSuccess: function(transport) {
						 var response = transport.responseText;
						 if(response == "1"){//更新成功
							$("toPayDiv"+id).hide();
							$("paidDiv"+id).show();
						 }
					},
					
					onFailure: function(transport){
						if (transport.status == 404)
							alert("您访问的url地址不存在！");
						else
							alert("Error: status code is " + transport.status);
					}	
				});
			}
			else{
				return;
			}
		}
	
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			//加载头部标签样式
			loadListTab('${range}',['0','1','2']);
			//增加清空按钮
			createCancelButton('searchForm',-50,5);
			//设置判断选中按钮条件
			setCurItemStyle(new Array("${dateRange}","${dateRange}","${dateRange}","${dateRange}","${dateRange}"),new Array("7","15","30","expired","all"));
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">
            	<table>
                	<tr>
                    	<th>采购管理 > 付款计划</th>
                        <td><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="刷新">&nbsp;</a></td>
                    </tr>
                </table>
            </div>
  	   		<table id="mainTop" class="normal nopd" style="width:100%">
                <tr>
                    <td style="height:35px">
                        <div id="tabType">
                            <div id="tabType1" onClick="self.location.href='salPurAction.do?op=listSpoPaidPlan&range=0'">我的付款计划</div>    
                            <div id="tabType2" onClick="self.location.href='salPurAction.do?op=listSpoPaidPlan&range=1'">下属付款计划</div>   
                            <div id="tabType3" onClick="self.location.href='salPurAction.do?op=listSpoPaidPlan&range=2'">所有付款计划</div>
                        </div>
                     </td> 
                     <td style="width:110px">
                    	<a href="javascript:void(0)" onClick="addDivNew(3);return false;" class="newBlueButton">新建付款计划</a>
                    </td>
                </tr>
            </table>
            <div id="listContent">
                <div class="listSearch">
                	<form id="searchForm" action="salPurAction.do" method="post" >
                        <input type="hidden" name="op" value="listSpoPaidPlan" />
                        <input type="hidden" name="range" value="${range}"/>
                        <input type="hidden" name="dateRange" value="${dateRange}"/>
                        <input type="hidden" id="curPage" name="paidPlanSForm.p"/>
                        摘要：<input name="paidPlanSForm.planContent" class="inputSize2 inputBoxAlign" type="text" value="${paidPlanSForm.planContent}" style="width:100px"/>&nbsp;&nbsp;
                        付款日期：<input name="paidPlanSForm.startDate" value="${paidPlanSForm.startDate}" id="pid" type="text" class="inputSize2 Wdate inputBoxAlign" style="cursor:hand; width:85px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                        到&nbsp;<input name="paidPlanSForm.endDate" value="${paidPlanSForm.endDate}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 Wdate inputBoxAlign" style="cursor:hand; width:85px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                         <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   </form>
                </div>
                <div id="topChoose" class="listTopBox">
                    <a href="salPurAction.do?op=listSpoPaidPlan&range=${range}&dateRange=7">&nbsp;<span class="bold">7</span>天内待付款&nbsp;</a>&nbsp;
                    <a href="salPurAction.do?op=listSpoPaidPlan&range=${range}&dateRange=15"><span class="bold">15</span>天内待付款</a>&nbsp;
                    <a href="salPurAction.do?op=listSpoPaidPlan&range=${range}&dateRange=30"><span class="bold">30</span>天内待付款</a>&nbsp;
                    <a href="salPurAction.do?op=listSpoPaidPlan&range=${range}&dateRange=expired">&nbsp;过期未付款&nbsp;</a>
                    <a href="salPurAction.do?op=listSpoPaidPlan&range=${range}&dateRange=all">&nbsp;所有未付款&nbsp;</a>
                </div>
                <div class="dataList">			
                    <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                        <tr>
                            <th style="width:20%">摘要</th>
                            <th style="width:15%">对应采购单</th>
                            <th style="width:15%">付款金额</th>
                            <th style="width:15%">付款日期</th>
                            <th style="width:10%">负责账号</th>
                            <th style="width:10%">是否已付款</th>
                            <th style="width:15%; border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="paidPlanList">
                        	<logic:iterate id="paidPlan" name="paidPlanList" indexId="index">
                            <tr id="tr${index}" onMouseOver="focusTr('tr',${index},1)" onMouseOut="focusTr('tr',${index},0)" onDblClick="addDivNew(8,${paidPlan.sppId})">
                                <td><a href="javascript:void(0)" onClick="addDivNew(8,${paidPlan.sppId});return false;">${paidPlan.sppContent}</a>&nbsp;</td>
                                <td class="mLink">
                                    <logic:notEmpty name="paidPlan" property="salPurOrd">
                                        <a title="${paidPlan.salPurOrd.spoTil}" href="salPurAction.do?op=spoDesc&spoId=${paidPlan.salPurOrd.spoId}" target="_blank" style="cursor:pointer"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看采购单详情" style="border:0px;">${paidPlan.salPurOrd.spoTil}</a>
                                    </logic:notEmpty>&nbsp;
                                </td>
                                <td>￥<bean:write name="paidPlan" property="sppPayMon" format="###,##0.00"/></td>
                                <td><span id="planDate${index}"></span>&nbsp;</td>
                                <td>${paidPlan.limUser.userSeName}&nbsp;</td>
                                <td>
                                    <logic:equal name="paidPlan" property="sppIsp" value="1">
                                        <img class="imgAlign" src="crm/images/content/suc.gif" alt="已付款"/>
                                    </logic:equal> 
                                    <logic:equal name="paidPlan" property="sppIsp" value="0">
                                    <div id="toPayDiv${paidPlan.sppId}">
                                    	<span class="red">未付</span>&nbsp;<a href="javascript:void(0)" onClick="completeSpoPaid(${paidPlan.sppId});return false;"><img style="border:0px" class="imgAlign" src="crm/images/content/execute.gif" alt="付款"/></a>
                                    </div>
                                    <div id="paidDiv${paidPlan.sppId}" style="display:none">
                                    	<img class="imgAlign" src="crm/images/content/suc.gif" alt="已付款"/>
                                    </div>
                                    </logic:equal> 
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                    <a href="javascript:void(0)" onClick="addDivNew(8,${paidPlan.sppId});return false;"><img class="imgAlign" src="crm/images/content/detail.gif" border="0" alt="查看"/></a>&nbsp;&nbsp;
                                    <a href="javascript:void(0)" onClick="addDivNew(6,${paidPlan.sppId});return false;"><img class="imgAlign" src="crm/images/content/edit.gif" border="0" alt="编辑"/></a>&nbsp;&nbsp;
                                    <a href="javascript:void(0)" onClick="delDiv(2,'${paidPlan.sppId}');return false;"><img class="imgAlign" src="crm/images/content/del.gif" border="0" alt="删除"/></a>
                                </td>
                            </tr>
                            <script type="text/javascript">
                                dateFormat("planDate","${paidPlan.sppPrmDate}",${index});
                                rowsBg('tr',${index});
                            </script>
                            </logic:iterate>
                        </logic:notEmpty>
                        <logic:empty name="paidPlanList">
                            <tr>
                                <td class="noDataTd" colspan="7">未找到相关数据!</td>
                            </tr>
                        </logic:empty>
                    </table>
                    <logic:notEmpty name="paidPlanList">
						<script type="text/javascript">
							var url = 'searchForm';
                            createPage(url,'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                        </script>
                    </logic:notEmpty>
                </div>
            </div>
  		</div> 
	</div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

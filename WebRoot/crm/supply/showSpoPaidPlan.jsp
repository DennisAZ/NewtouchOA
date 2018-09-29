<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>对应付款计划</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
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
		createIfmLoad('ifmLoad');//进度条
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
    	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>付款计划</th>
                <td><a href="javascript:void(0)" onClick="parent.addDivNew(3);return false;" class="newBlueButton">新建付款计划</a></td>
            </tr>
        </table>
        <table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
            <tr>
                <th style="width:20%">摘要</th>
				<th style="width:15%">付款金额</th>
				<th style="width:15%">预计付款日期</th>
				<th style="width:20%">负责账号</th>
				<th style="width:10%">是否已付款</th>
				<th style="width:10%; border-right:0px">操作</th>
			</tr>
			<logic:notEmpty name="planList">
			<logic:iterate id="paidPlan" name="planList">
			<tr id="plantr<%= count%>" onMouseOver="focusTr('plantr',<%= count%>,1)" onMouseOut="focusTr('plantr',<%= count%>,0)" onDblClick="parent.addDivNew(8,${paidPlan.sppId})">
				<td><a href="javascript:void(0)" onClick="parent.addDivNew(8,${paidPlan.sppId});return false;">${paidPlan.sppContent}</a>&nbsp;</td>
				<td>￥<bean:write name="paidPlan" property="sppPayMon" format="###,##0.00"/></td>
				<td><span id="planDate<%=count%>"></span>&nbsp;</td>
				<td>${paidPlan.limUser.userSeName}&nbsp;</td>
				<td>
					<logic:equal name="paidPlan" property="sppIsp" value="1">
						<img class="imgAlign" src="crm/images/content/uploadSuc.gif" alt="已付款"/>
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
					<a href="javascript:void(0)" onClick="parent.addDivNew(8,${paidPlan.sppId});return false;"><img class="imgAlign" src="crm/images/content/detail.gif" border="0" alt="查看"/></a>&nbsp;&nbsp;
					<a href="javascript:void(0)" onClick="parent.addDivNew(6,${paidPlan.sppId});return false;"><img class="imgAlign" src="crm/images/content/edit.gif" border="0" alt="编辑"/></a>&nbsp;&nbsp;
					<a href="javascript:void(0)" onClick="parent.delDiv(2,'${paidPlan.sppId}');return false;"><img class="imgAlign" src="crm/images/content/del.gif" border="0" alt="删除"/></a>
				</td>
			</tr>
			<script type="text/javascript">
				dateFormat("planDate","${paidPlan.sppPrmDate}",<%=count%>);
				rowsBg('plantr',<%=count%>);
			</script>
			<% count++; %>
			</logic:iterate>
		</logic:notEmpty>                
            <logic:empty name="planList">
                <tr>
                    <td colspan="6" class="noDataTd">暂未添加付款计划...</td>
                </tr>
            </logic:empty>
        </table>
			<logic:notEmpty name="planList">	
                 	<script type="text/javascript">
						createPage('salPurAction.do?op=getSpoPaidPlan&id=${id}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
           </logic:notEmpty>
   </div>
  </body>

</html>

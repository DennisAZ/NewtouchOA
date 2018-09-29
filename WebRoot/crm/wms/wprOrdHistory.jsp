<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>销售历史记录</title>
    <link rel="shortcut icon" href="favicon.ico"/> 
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">

    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
    <script type="text/javascript">
		
		createProgressBar();	
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			closeProgressBar();
		}
    </script>
  </head>
  
  <body>
  	<div style="padding:10px; width:100%;" >
  		<div style="padding:2px; padding-left:0px; text-align:left; color:#666666">
        	<img src="images/content/wpr_pro.gif" class="imgAlign"/>&nbsp;产品&nbsp;<span class="bold blue">${wmsProduct.wprName}<logic:notEmpty name="wmsProduct" property="wprModel">/${wmsProduct.wprModel}</logic:notEmpty><logic:notEmpty name="wmsProduct" property="typeList"><span class="brown">[${wmsProduct.typeList.typName}]</span></logic:notEmpty></span>&nbsp;的销售记录，共${count}条(该处只统计已审核生效的订单)
        </div>
		<div class="dataList">
        <table id="tab" class="normal rowstable" cellpadding="0px" cellspacing="0px">
				<tr>
					<th style="width:25%">订单</th>
					<th style="width:15%">对应客户</th>
					<th style="width:15%">销售单价</th>
					<th style="width:15%">销售数量</th>
					<th style="width:15%">销售总额</th>
					<th style=" width:15%;border-right:0px">签订日期</th>
				</tr>
				<logic:notEmpty name="rordPro" >
				<logic:iterate id="rop" name="rordPro">
					<tr id="tr<%=count1%>">
						<td><a href="orderAction.do?op=showOrdDesc&code=${rop.salOrdCon.sodCode}" target="_blank">${rop.salOrdCon.sodTil}</a>&nbsp;</td>
						<td class="mlink">
							<logic:notEmpty name="rop" property="salOrdCon.cusCorCus">
								<a title="${rop.salOrdCon.cusCorCus.corName}" href="customAction.do?op=showCompanyCusDesc&corCode=${rop.salOrdCon.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看客户详情" style="border:0px;"/>${rop.salOrdCon.cusCorCus.corName}</a>
							</logic:notEmpty>&nbsp;
						</td>
						<td>￥<bean:write name="rop" property="ropPrice" format="###,##0.00"/>&nbsp;</td>
						<td><bean:write name="rop" property="ropNum" format="###,##0.00"/>&nbsp;</td>
						<td>￥<bean:write name="rop" property="ropRealPrice" format="###,##0.00"/>&nbsp;</td>
						<td><span id="conDate<%=count1%>"></span>&nbsp;</td>
					</tr>
					<script type="text/javascript">
						rowsBg('tr',<%=count1%>);
						dateFormat('conDate','${rop.salOrdCon.sodConDate}',<%=count1%>);
					</script>
					<%count1++;%>
				</logic:iterate>
				</logic:notEmpty>
				<logic:empty name="rordPro">
					<tr>
						<td colspan="6" style="text-align:center;" class="gray">
						该产品暂无销售历史</td>
					</tr>
				</logic:empty>
              </table>
			<logic:notEmpty name="rordPro">
              <script type="text/javascript">
							createPage('wmsManageAction.do?op=findOrdByWpr&wprId=${wmsProduct.wprId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
			 </script>      	
         </logic:notEmpty>
		 </div>
  	</div>
  </body>
</html>

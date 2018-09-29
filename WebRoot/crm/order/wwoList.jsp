<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

int j=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>出库安排</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
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
    	<logic:empty name="isnone">
            <logic:notEmpty name="order" property="ROrdPros">
            	<div id="creWWoLayer" class="blueBg" style="text-align:center; padding:2px;">
                    <button id="button" class="butSize3" onClick="parent.window.open('orderAction.do?op=wmsProOut&sodCode=${order.sodCode}')">生成出库单</button>
           		</div>
            </logic:notEmpty>
            <logic:empty name="order" property="ROrdPros">
                 <div class="orangeBox" style="padding:10px"><img src="images/content/warn.gif" class="imgAlign">&nbsp;订单中没有可以出库的产品</div>
            </logic:empty>
		</logic:empty>
		<logic:notEmpty name="isnone">
            <div class="orangeBox" style="padding:10px"><img src="images/content/warn.gif" class="imgAlign">&nbsp;订单中没有可以出库的产品</div>
        </logic:notEmpty>
		<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>出库安排</th>
            </tr>
        </table>
		<table id="tab" class="normal rowstable" style="width:100%" cellpadding="0" cellspacing="0">
			<tr>
				<th style="width:14%">出库单号</th>
				<th style="width:24%">主题</th>
				<th style="width:14%">仓库</th>
				<th style="width:12%">录入时间</th>
				<th style="width:8%">审核状态</th>
				<th style="width:8%">出库状态</th>
				<th style="width:10%">领料人</th>
				<th style="width:10%; border-right:0px">操作</th>
			</tr>
			<logic:notEmpty name="wwoList">
			<logic:iterate id="wwo" name="wwoList">
			<%//int i=1;%>
			<tr id="tr<%= j%>" onMouseOver="focusTr('tr',<%= j%>,1)" onMouseOut="focusTr('tr',<%= j%>,0)" onDblClick="parent.descPop('wwoAction.do?op=wwoDesc&wwoId=${wwo.wwoId}')">
				<td>${wwo.wwoCode}</td>
				<td ><a href="wwoAction.do?op=wwoDesc&wwoId=${wwo.wwoId}" target="_blank">${wwo.wwoTitle}</a></td>	
				<td>${wwo.wmsStro.wmsName}&nbsp;</td>
				<td ><label id="deadLine<%=j%>"></label>&nbsp;</td>
				<td style="text-align:center">
					<logic:notEmpty name="wwo" property="wwoAppIsok">
						<logic:equal value="0" name="wwo" property="wwoAppIsok"><img src="images/content/fail.gif" alt="未通过"></logic:equal>
                        <logic:equal value="2" name="wwo" property="wwoAppIsok"><img src="images/content/time.gif" alt="审核中"></logic:equal>
                        <logic:equal value="1" name="wwo" property="wwoAppIsok"><img src="images/content/suc.gif" alt="已通过"></logic:equal>
					</logic:notEmpty>
					<logic:empty name="wwo" property="wwoAppIsok">
						<span class="gray" title="未审核(请添加产品)">未审核</span>
					</logic:empty>
				</td>
				<td style="text-align:center">
				<logic:equal value="0" name="wwo" property="wwoState"><img src="images/content/database.gif" alt="未出库"></logic:equal>
               	<logic:equal value="1" name="wwo" property="wwoState"><img src="images/content/db_ok.gif" alt="已出库"></logic:equal>
               	<logic:equal value="2" name="wwo" property="wwoState"><img src="images/content/db_ok.gif" alt="已发货"></logic:equal>
             	<logic:equal value="3" name="wwo" property="wwoState"><img src="images/content/dbError.gif" alt="已撤销"></logic:equal>
				</td>
				<td>${wwo.wwoUserName}&nbsp;</td>
				<td>
                	<a href="wwoAction.do?op=wwoDesc&wwoId=${wwo.wwoId}" target="_blank">
                    <img src="images/content/detail.gif" border="0" alt="查看详情"/></a>&nbsp;&nbsp;
					<logic:equal value="0" name="wwo" property="wwoState">
						<logic:notEqual value="1" name="wwo" property="wwoAppIsok">
							<img onClick="parent.ordPopDiv(6,'${wwo.wwoId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑出库单"/>&nbsp;&nbsp;
							<img onClick="parent.ordDelDiv(7,'${wwo.wwoId}',1)" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>	
						</logic:notEqual>
				   </logic:equal>&nbsp;
				</td>
			</tr>
			
			<script type="text/javascript">
			removeTime("deadLine<%=j%>","${wwo.wwoInpDate}",2);
			rowsBg('tr',<%=j%>);
			</script>
			<%j++;%>
			</logic:iterate>
			</logic:notEmpty>
			<%if(j==0){%>
					<tr>
						<td class="noDataTd" colspan="8">未找到相关数据!</td>
					</tr>
				 <%}%>
		</table>
		
			<logic:notEmpty name="wwoList">		
				<script type="text/javascript">
                    createPage('orderAction.do?op=getWwoList&sodCode=${sodCode}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                </script>
           	</logic:notEmpty>		
   </div>
  </body>

</html>

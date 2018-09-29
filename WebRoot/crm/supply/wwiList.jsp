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
    <title>入库安排</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
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
 	<logic:empty name="isnone">
		<logic:notEmpty name="salPurOrd" property="rspoPros">
          	<div id="creWwiLayer" class="blueBg" style="display:none; text-align:center; padding:2px;">
            	<button id="button" class="butSize3" onClick="parent.window.open('salPurAction.do?op=wmsProIn&spoId=${salPurOrd.spoId}')">生成入库单</button>
         	</div>
    		<script type="text/javascript">
				displayLimAllow("pu009","creWwiLayer");
			</script>
		</logic:notEmpty>
	</logic:empty>
    <logic:notEmpty name="isnone">
        <div class="orangeBox" style="padding:10px"><img src="crm/images/content/warn.gif" class="imgAlign">&nbsp;采购单中没有可以入库的产品</div>
	</logic:notEmpty>

  	<div class="divWithScroll2 innerIfm">
		<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>入库安排</th>
            </tr>
        </table>
		<table id="tab" class="normal rowstable" style="width:100%" cellpadding="0" cellspacing="0">
			<tr>
				<th style="width:14%">入库单号</th>
				<th style="width:24%">主题</th>
				<th style="width:14%">仓库</th>
				<th style="width:12%">录入时间</th>
				<th style="width:8%">审核状态</th>
				<th style="width:8%">入库状态</th>
				<th style="width:10%">填单人</th>
				<th style="width:10%;border-right:0px">操作</th>
			</tr>
			<logic:notEmpty name="wwiList">
			<logic:iterate id="wwi" name="wwiList">
			<%//int i=1;%>
			<tr id="tr<%= j%>" onMouseOver="focusTr('tr',<%= j%>,1)" onMouseOut="focusTr('tr',<%= j%>,0)" onDblClick="parent.descPop('wmsManageAction.do?op=wwiDesc&wwiId=${wwi.wwiId}')">
				<td>${wwi.wwiCode}</td>
				<td ><a href="wmsManageAction.do?op=wwiDesc&wwiId=${wwi.wwiId}" target="_blank">${wwi.wwiTitle}</a></td>	
				<td>${wwi.wmsStro.wmsName}&nbsp;</td>
				<td ><label id="deadLine<%=j%>"></label>&nbsp;</td>
				<td style="text-align:center">
					<logic:notEmpty name="wwi" property="wwiAppIsok">
					<logic:equal value="0" name="wwi" property="wwiAppIsok"><img src="crm/images/content/fail.gif" alt="未通过"></logic:equal>
					<logic:equal value="2" name="wwi" property="wwiAppIsok"><img src="crm/images/content/time.gif" alt="审核中"></logic:equal>
					<logic:equal value="1" name="wwi" property="wwiAppIsok"><img src="crm/images/content/suc.gif" alt="已通过"></logic:equal>		</logic:notEmpty>
		<logic:empty name="wwi" property="wwiAppIsok">
			<img src="crm/images/content/tofinish.gif" alt="未审核(请添加产品)">
		</logic:empty>
				</td>
				<td style="text-align:center">
				  <logic:equal value="0" name="wwi" property="wwiState"><img src="crm/images/content/database.gif" alt="未入库"></logic:equal>
				  <logic:equal value="1" name="wwi" property="wwiState"><img src="crm/images/content/db_ok.gif" alt="已入库"></logic:equal>
				  <logic:equal value="2" name="wwi" property="wwiState"><img src="crm/images/content/dbError.gif" alt="已撤销"></logic:equal>
				</td>
				<td>${wwi.wwiInpName}&nbsp;</td>
				<td>
                <a href="wmsManageAction.do?op=wwiDesc&wwiId=${wwi.wwiId}" target="_blank">
                <img src="crm/images/content/detail.gif" border="0" alt="查看详情"/></a>&nbsp;&nbsp;
				<logic:equal value="0" name="wwi" property="wwiState">
				  	<logic:notEqual value="1" name="wwi" property="wwiAppIsok">
					<img onClick="parent.addDivNew(15,'${wwi.wwiId}','${wmsCode}')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑入库单"/>&nbsp;&nbsp;
					<img onClick="parent.delDiv(6,'${wwi.wwiId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>
                </logic:notEqual>
                </logic:equal>
				</td>
			</tr>
			<script type="text/javascript">
			removeTime("deadLine<%=j%>","${wwi.wwiInpTime}",2);
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
		<logic:notEmpty name="wwiList">		
			<script type="text/javascript">
				createPage('salPurAction.do?op=getSpoWwi&id=${spoId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
			</script>
		</logic:notEmpty>		
   </div>
  </body>

</html>

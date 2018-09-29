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
    <title>对应收票记录</title>
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
            	<th>收票记录</th>
            </tr>
        </table>
        <table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
           <tr>
           		<th style="width:20%">开票内容</th>
				<th style="width:10%">票据类别</th>
				<th style="width:16%">票据编号</th>
				<th style="width:12%">金额</th>
				<th style="width:12%">开票日期</th>
				<th style="width:8%">开票人</th>
				<th style="width:15%">是否已付款</th>
				<th style="width:5%; border-right:0px">操作</th>
		  </tr>
			<logic:notEmpty name="invList">
			<logic:iterate id="inv" name="invList">
			<tr id="invtr<%= count%>" onMouseOver="focusTr('invtr',<%= count%>,1)" onMouseOut="focusTr('invtr',<%= count%>,0)" onDblClick="parent.addDivNew(10,'${inv.sinId}')">
            	<td><a href="javascript:void(0)" onClick="parent.addDivNew(10,'${inv.sinId}');return false;">${inv.sinCon}&nbsp;</a></td>
				<td>${inv.salInvType.typName}&nbsp;</td>
				<td>${inv.sinCode}&nbsp;</td>
				<td>￥<bean:write name="inv" property="sinMon" format="###,##0.00"/></td>
				<!-- <td>${inv.sinMonType}&nbsp;</td> -->   
				<td><span id="invDate<%=count%>"></span>&nbsp;</td>
				<td>${inv.sinResp}&nbsp;</td>
				<td>
					<logic:equal name="inv" property="sinIsPaid" value="1">
						已付款
					</logic:equal>
					<logic:equal name="inv" property="sinIsPaid" value="0">
						未付款
					</logic:equal>
					<logic:notEmpty name="inv" property="sinIsPlaned">
						<span class="gray">[${inv.sinIsPlaned}]</span>
					</logic:notEmpty>
				</td>
				<td>
					&nbsp;&nbsp;
					<a href="javascript:void(0)" onClick="parent.addDivNew(10,'${inv.sinId}');return false;"><img class="imgAlign" src="crm/images/content/detail.gif" border="0" alt="查看"/></a>&nbsp;&nbsp;
				</td>
			</tr>    
			<script type="text/javascript">
				dateFormat("invDate","${inv.sinDate}",<%=count%>);
				rowsBg('invtr',<%=count%>);
			</script>
			<% count++;%>
			</logic:iterate>
			</logic:notEmpty>
            <logic:empty name="invList">
                <tr>
                    <td colspan="8" class="noDataTd">暂未添加收票记录...</td>
                </tr>
            </logic:empty>
        </table>
			<logic:notEmpty name="invList">	
                 	<script type="text/javascript">
						createPage('salPurAction.do?op=getSpoInv&id=${id}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
           </logic:notEmpty>
   </div>
  </body>

</html>

<%@ page language="java" pageEncoding="UTF-8"%>
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
    <title>报价记录(项目详情)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/pro.js"></script>
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
		<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>报价记录</th>
                <td><a href="javascript:void(0)" class="newBlueButton"  onClick="parent.projPopDiv(4,'${proId}','${proTitle}');return false;">新建报价记录</a></td>
            </tr>
        </table>
		<table id="tab" class="normal rowstable" style="width:100%" cellpadding="0" cellspacing="0">
            <tr>
                <th style="width:60%">主题</th>
                <th style="width:10%">总报价</th>
                <th style="width:10%">报价人</th>
                <th style="width:10%">报价日期</th>
                <th style=" width:10%;border-right:0px">操作</th>
            </tr>
	
            <logic:notEmpty name="quoteList">	
            	<logic:iterate id="quote" name="quoteList">
            		<tr id="trQuo<%= count1%>" onMouseOver="focusTr('trQuo',<%= count1%>,1)" onMouseOut="focusTr('trQuo',<%= count1%>,0)" onDblClick="descPop('cusServAction.do?op=showQuoteInfo&id=${quote.quoId}&isPro=yes&isEdit=0')">
                      	<td><a href="cusServAction.do?op=showQuoteInfo&id=${quote.quoId}&isPro=yes&isEdit=0" style="cursor:pointer" target="_blank">${quote.quoTitle}</a></td>
                      	<td><span>￥<bean:write name="quote" property="quoPrice" format="###,###.00"/>&nbsp;</span></td>
                      	<td>${quote.salEmp.seName}&nbsp;</td>
                      	<td><label id="quoDate<%=count1%>"></label>&nbsp;</td>
                      	<td>&nbsp;
                          	<a href="cusServAction.do?op=showQuoteInfo&id=${quote.quoId}&isPro=yes&isEdit=0" target="_blank"><img src="images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                          	<img onClick="parent.projPopDiv(41,'${quote.quoId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                          	<img onClick="parent.projDelDiv(4,'${quote.quoId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>&nbsp;&nbsp;
                      	</td>
                  	</tr>
                    <script type="text/javascript">
                        dateFormat("quoDate","${quote.quoDate}",<%=count1%>);
                        rowsBg('trQuo',<%=count1%>);
                    </script>
                    <% count1++; %>
               	</logic:iterate>
         	</logic:notEmpty>
            <logic:empty name="quoteList">		
                <tr>
                    <td colspan="5" class="noDataTd">未找到相关数据!</td>
                </tr>
            </logic:empty>
			</table>
			<logic:notEmpty name="quoteList">		
				<script type="text/javascript">
                    createPage('cusServAction.do?op=getProQuo&proId=${proId}&proTitle='+ encodeURIComponent('${proTitle}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                </script>
           	</logic:notEmpty>		
   </div>
  </body>

</html>

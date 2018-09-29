<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
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
    <title>发货提醒</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/sal.js"></script>
        <style type="text/css">
    	body{
			background-color:#fff;
			text-align:left;
		}
    </style>
    <script type="text/javascript">
    	//列表筛选按钮链接
		function filterList(filter){
			self.location.href="orderAction.do?op=getProdTip&filter="+filter;
		}
		
		function loadFilter(){
			setCuritemStyle("${filter}",["date3","date7","date15"]);	
		}
		
		window.onload=function(){
			loadFilter();
		}
    </script>
  </head>
  
  <body>
  				<div id="topChoose" class="listTopBox">
               		<a href="javascript:void(0)" onClick="filterList('date3')" >&nbsp;3天内发货&nbsp;</a>
  					<a href="javascript:void(0)" onClick="filterList('date7')" > 7天内发货&nbsp;</a>
               		<a href="javascript:void(0)" onClick="filterList('date15')" >&nbsp;15天内发货&nbsp;</a>
                </div>
                <logic:notEmpty name="orders">
                <div style="padding:10px; text-align:center;">
                  <table  class="normal rowstable" cellpadding="0" cellspacing="0" style="width:95%">		
                    <tr>
	                    <th style="width:20%">客户名称</th>
	                    <th style="width:20%">订单主题</th>
	                    <th style="width:30%;align:center">签订日期</th>
	                    <th style="border-right:0px;width:30%;align:center">发货日期</th>
                	</tr>
                    <logic:iterate id="ord" name="orders">
                        <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                            <td><a href='javascript:void(0)' id="cusNameA<%= count%>" onclick="descPop('customAction.do?op=showCompanyCusDesc&corCode=${ord.cusCorCus.corCode}')">${ord.cusCorCus.corName}</a>&nbsp;</td>
							<td><a href='javascript:void(0)' onclick="descPop('orderAction.do?op=showOrdDesc&code=${ord.sodCode}')">${ord.sodTil}</a>&nbsp;</td>
							<td><span id="sodConDate<%= count%>">&nbsp;</span></td>
							<td><span id="sodComiteDate<%= count%>"></span>&nbsp;</td>
                        </tr>
                        <script type="text/javascript">
                            rowsBg('tr',<%=count%>);
                            if('${ord.sodComiteDate}'.substring(0,10)<'${TODAY}'){ $("tr<%= count%>").className="red"; }
                            dateInitToShow("sodConDate<%= count%>",'${ord.sodConDate}');
							dateInitToShow("sodComiteDate<%= count%>",'${ord.sodComiteDate}');
                        </script>
                     <%count++;%>
                    </logic:iterate>
                  </table>
                  </div>
                </logic:notEmpty>
                
                <logic:empty name="orders">
        			<div class="gray">&nbsp;&nbsp;最近没有发货提醒</div>
                </logic:empty>
  </body>

</html>

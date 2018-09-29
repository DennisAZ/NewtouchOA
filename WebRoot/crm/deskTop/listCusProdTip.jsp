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
    <title>最后发货日期提醒</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
        <style type="text/css">
    	body{
			background-color:#fff;
			text-align:left;
		}
    </style>
    <script type="text/javascript">
    	window.onload=function(){
    		
    	}
    </script>
  </head>
  
  <body>
                <logic:notEmpty name="cusProdList">
                <div style="padding:10px; text-align:center;">
                  <table  class="normal rowstable" cellpadding="0" cellspacing="0" style="width:95%">		
                    <tr>
	                    <th style="width:25%">客户名称</th>
	                    <th style="width:25%">产品名称</th>
	                    <th style="width:20%">提前提醒天数</th>
	                    <th style="width:30%;align:center">最后发货日期</th>
                	</tr>
                    <logic:iterate id="cusProd" name="cusProdList">
                        <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                            <td><a href='javascript:void(0)' id="cusNameA<%= count%>" onclick="descPop('customAction.do?op=showCompanyCusDesc&corCode=${cusProd.cusCorCus.corCode}')">${cusProd.cusCorCus.corName}</a>&nbsp;</td>
							<td><a href='javascript:void(0)' onclick="descPop('prodAction.do?op=wmsProDesc&wprId=${cusProd.wmsProduct.wprId}')">${cusProd.wmsProduct.wprName}</a>&nbsp;</td>
							<td>${cusProd.cpWarnDay}</td>
							<td><span id="cpSentDate<%= count%>">&nbsp;</span></td>
                        </tr>
                        <script type="text/javascript">
                            rowsBg('tr',<%=count%>);
                            dateInitToShow("cpSentDate<%= count%>",'${cusProd.cpSentDate}');
                        </script>
                     <%count++;%>
                    </logic:iterate>
                  </table>
                  </div>
                </logic:notEmpty>
                
                <logic:empty name="cusProdList">
        			<div class="gray">&nbsp;&nbsp;最近没有最后发货日期提醒</div>
                </logic:empty>
  </body>

</html>

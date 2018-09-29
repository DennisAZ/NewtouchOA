<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0,count2=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>上月末未达到规定金额的单位</title>
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
  </head>
  
  <body>
                <logic:notEmpty name="statSal">
                <div style="padding:10px; text-align:center;">
                  <table  class="normal rowstable" cellpadding="0" cellspacing="0" style="width:95%">		
                    <tr>
	                    <th style="width:20%">客户名称</th>
	                    <th style="width:20%">规定月销售额</th>
	                    <th style="width:30%;align:center">最近有效三个月的平均销售额</th>
	                    <th style="border-right:0px;width:30%;align:center">业务员</th>
                	</tr>
                    <logic:iterate id="ss" name="statSal">
                        <tr id="tr<%= count2%>" onMouseOver="focusTr('tr',<%= count2%>,1)" onMouseOut="focusTr('tr',<%= count2%>,0)">
                            <td>&nbsp;<a href='javascript:void(0)' onclick="descPop('customAction.do?op=showCompanyCusDesc&corCode=${ss.cusId}')">${ss.cusName}</a></td>
							<td>&nbsp;<bean:write name="ss" property="monSum1" format="0.00"/></td>
							<td>&nbsp;<bean:write name="ss" property="monSum2" format="0.00"/></td>
							<td>&nbsp;${ss.empName}</td>
                        </tr>
                        <script type="text/javascript">
                            rowsBg('tr',<%=count2%>);
                        </script>
                     <%count2++;%>
                    </logic:iterate>
                  </table>
                  </div>
                </logic:notEmpty>
                
                <logic:empty name="statSal">
        			<div class="gray">&nbsp;&nbsp;最近没有上月末未达到规定金额的单位</div>
                </logic:empty>
  </body>

</html>

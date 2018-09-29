<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    <title>我6个月的销售业绩统计</title>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
    </style>
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/FusionCharts.js"></script>
	 <script language="javascript">
		
		function changeToTab(){
			if($("flashChart").style.display==""){
				$("flashChart").hide();
				$("tableChart").show();
			}
			else{
				$("flashChart").show();
				$("tableChart").hide();
			}
		}
		function changeChar(n){	
			$("flashChart").show();
			$("tableChart").hide();
            var url='statAction.do?op=getLastSalXML&type='+n+'';
            renderChar(n,url,'chartDiv');
        }

		window.onload=function(){
			$("tableChart").style.display='none';
			changeChar(2);
		}
     </script>
</head>
  <body>
  		<div style="text-align:left;">
        	<a href="javascript:void(0)" onClick="changeChar(1);return false;">2D柱图</a>&nbsp;
             <a href="javascript:void(0)" onClick="changeChar(2);return false;">3D柱图</a>&nbsp;
             <a href="javascript:void(0)" onClick="changeChar(3);return false;">2D条形图</a>&nbsp;
             <a href="javascript:void(0)" onClick="changeChar(4);return false;">折线图</a>&nbsp;
             <a href="javascript:void(0)" onClick="changeChar(5);return false;">2D面积图</a>&nbsp;
             <a href="javascript:void(0)" onClick="changeChar(6);return false;">2D饼图</a>&nbsp;
             <a href="javascript:void(0)" onClick="changeChar(7);return false;">3D饼图</a>&nbsp;
             <a href="javascript:void(0)" onClick="changeChar(8);return false;">圆环图</a>
             <a href="javascript:void(0)" onClick="changeToTab();return false;">表格</a>
         </div>
        <div id="tableChart" style="padding:10px; text-align:center;">
            <table  class="normal rowstable" cellpadding="0" cellspacing="0" style="width:95%">		
                <tr>
                    <th>日期</th>
                    <th style="border-right:0px">金额</th>
                </tr>
                <logic:notEmpty name="ordStatistic">
                    <logic:iterate id="st" name="ordStatistic">
                        <tr id="tr<%= count2%>" onMouseOver="focusTr('tr',<%= count2%>,1)" onMouseOut="focusTr('tr',<%= count2%>,0)">
                            <td>&nbsp;${st.name}</td>
                            <td>&nbsp;
                            <bean:write name="st" property="dnum" format="0.00"/></td>
                        </tr>
                        <script type="text/javascript">
                            rowsBg('tr',<%=count2%>);
                        </script>
                     <%count2++;%>
                    </logic:iterate>
                        <tr class="lightBlueBg bold">
                            <td>&nbsp;合计</td>
                            <td>&nbsp;<bean:write name="countAll" format="0.00"/></td>
                        </tr>
                </logic:notEmpty>
                <logic:empty name="ordStatistic">
                    <tr>
                        <td colspan="2" class="noDataTd">未找到相关数据!</td>
                    </tr>
                </logic:empty>
            </table>
        </div>
        <div id="flashChart">
            <div id="chartDiv" style="text-align:left; width:99%; height:240px;"></div>
            
        </div>
  </body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean"  prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>回款计划</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:left;
		}
		.modTop{
			text-align:left;
			padding:5px;
			padding-left:2px;
			padding-top:2px;
		}
		
		.modTitle{
			padding:3px;
			padding-top:15px; 
		}
		
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>

    </head>
  
  <body>
    <div class="gray bold" style="padding-top:5px">30天内待执行的回款计划</div>
    <logic:notEmpty name='planList1' >
        <logic:iterate id='planList1' name='planList1'>
            <div class="modCon">
                <img src="images/content/blueCircle.gif"/>
                    <span id="planMon<%=count%>"></span>
                    &nbsp;[<a href="orderAction.do?op=showOrdDesc&code=${planList1.salOrdCon.sodCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看订单详情" style="border:0px;">${planList1.salOrdCon.sodTil}</a>]
                    <span id="planDate<%=count%>" class="gray small">&nbsp;</span>
            </div>
            <script type="text/javascript">
				dateFormat("planDate","${planList1.spdPrmDate}",<%=count%>);
            	$("planMon"+<%=count%>).innerHTML="￥"+changeTwoDecimal("${planList1.spdPayMon}");
            </script>
            <%count++;%>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='planList1'>
        <div class="gray">&nbsp;&nbsp;没有回款计划</div>
    </logic:empty>
    
    <div class="gray bold" style=" padding-top:8px;border-top:#d3d1d1 1px dashed;">过期未执行的回款计划</div>
    <logic:notEmpty name='planList2' >
        <logic:iterate id='planList2' name='planList2'>
            <div class="modCon">
                <img src="images/content/blueCircle.gif"/>
                <span id="planMon<%=count%>"></span>
                &nbsp;[<a href="orderAction.do?op=showOrdDesc&code=${planList2.salOrdCon.sodCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看订单详情" style="border:0px;">${planList2.salOrdCon.sodTil}</a>]
                <span id="planDate<%=count%>" class="gray small">&nbsp;</span>
            </div>
            <script type="text/javascript">
				dateFormat3("planDate","${planList2.spdPrmDate}",<%=count%>);
            	$("planMon"+<%=count%>).innerHTML="￥"+changeTwoDecimal("${planList2.spdPayMon}");
            </script>
            <%count++;%>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='planList2'>
        <div class="gray">&nbsp;&nbsp;没有回款计划</div>
    </logic:empty>
  </body>
</html>

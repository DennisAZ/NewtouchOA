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
    <title>最近的回款计划</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:center;
		}
		.tipTitle {
			font-size:14px;
			font-weight:600;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/ord.js"></script>
	<script type="text/javascript">
    	function showPast(){
			if($("pastPlan").style.display=="none"){
				$("pastPlan").show();
			}
			else{
				$("pastPlan").hide();
			}
		}
    </script>
    </head>
  
  <body>
  	<div class="tipBox">
    	<div class="tipTitle">&nbsp;&nbsp;30天内待执行的回款计划</div>
        <div class="bold" style="padding:3px; background-color:#fdf3cb; text-align:left; border-bottom:#FF99001px solid; font-size:13px;"><img class="imgAlign" src="images/content/coins.gif" alt="应收款">&nbsp;当前应收款为&nbsp;<span id="leftMon" class="red bold">￥<bean:write name="leftMon" scope="request" format="###,##0.00"/></span>&nbsp;</div>
        <div class="divWithScroll2" style="height:118px; padding-left:5px;">
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
                <div class="gray" style="padding:10px;">&nbsp;&nbsp;最近没有待执行的回款计划</div>
            </logic:empty>
        
            <div class="lightBg gray" style=" marin-top:10px; margin-right:10px;cursor:pointer; width:auto; padding:2px;" onClick="showPast()" onMouseOver="this.className='grayBack blue'" onMouseOut="this.className='lightBg gray'">&nbsp;有<span id="pastCount">0</span>条过期未执行的回款计划</div>
            <div id="pastPlan" style="display:none">
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
						$("pastCount").innerHTML=parseInt($("pastCount").innerHTML)+1;
                    </script>
                    <%count++;%>
                </logic:iterate>
            </logic:notEmpty>
            <logic:empty name='planList2'>
                <div class="gray">&nbsp;&nbsp;没有回款计划</div>
            </logic:empty>
            </div>
        </div>
    </div>
  </body>
</html>

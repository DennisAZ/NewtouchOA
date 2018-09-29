<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
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
    <title>回款计划详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
  </head>
  
  <body>
  	<div class="inputDiv">
    <logic:notEmpty name="paidPlan">
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th class="descTitleLNoR">摘要：</th>
                    <th class="descTitleR" colspan="3" style="height:75px; vertical-align:top">${paidPlan.spdContent}&nbsp;</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th class="blue">对应客户：</th>
                    <td class="blue mlink">
                        <logic:notEmpty name="paidPlan" property="cusCorCus">
                                <a href="customAction.do?op=showCompanyCusDesc&corCode=${paidPlan.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${paidPlan.cusCorCus.corName}</a>
                        </logic:notEmpty>&nbsp;
                    </td>
                    <th>负责账号：</th>
                    <td>${paidPlan.spdResp.userSeName}&nbsp;</td>
                </tr>
                <tr>
                    <th class="blue">对应订单：</th>
                    <td class="blue mlink">
                    	<logic:notEmpty name="paidPlan" property="salOrdCon">
                        <a href="orderAction.do?op=showOrdDesc&code=${paidPlan.salOrdCon.sodCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看订单详情" style="border:0px;">${paidPlan.salOrdCon.sodTil}</a>
                        </logic:notEmpty>&nbsp;
                    </td>
                    <th>是否已回款：</th>
                    <td>
                        <logic:equal name="paidPlan" property="spdIsp" value="1">
                            是
                        </logic:equal> 
                        <logic:equal name="paidPlan" property="spdIsp" value="0">
                            否
                        </logic:equal> 
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>回款金额：</th>
                    <td>￥<bean:write name="paidPlan" property="spdPayMon" format="###,##0.00"/>&nbsp;</td>
                    <th>回款日期：</th>
                    <td><span id="planDate"></span>&nbsp;</td>
                </tr>
            </tbody>
      	</table>
        <div class="descStamp">
            由
            <span>${paidPlan.spdUserCode}</span>
            于
            <span id="creDate"></span>&nbsp;
            录入
            <logic:notEmpty name="paidPlan" property="spdAltUser"><br/>
            最近由
            <span>${paidPlan.spdAltUser}</span>
            于
            <span id="altDate"></span>&nbsp;
            修改
            </logic:notEmpty>
       </div>
		<script type="text/javascript">
            removeTime("planDate","${paidPlan.spdPrmDate}",1);
            removeTime("creDate","${paidPlan.spdCreDate}",2)
            removeTime("altDate","${paidPlan.spdAltDate}",2);
        </script>
    </logic:notEmpty>
	<logic:empty name="paidPlan">
		<div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该回款计划已被删除</div>
	</logic:empty>
    </div>
  </body>
</html>

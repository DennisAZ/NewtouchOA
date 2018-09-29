<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>付款计划详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
  </head>
  
  <body>
   	<logic:notEmpty name="paidPlan">
    	<div class="inputDiv">
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
                <tr>
                    <th class="descTitleLNoR">摘要：</th>
                    <th class="descTitleR" colspan="3" style="height:75px; vertical-align:top">${paidPlan.sppContent}&nbsp;</th>
                </tr>
            </thead>
        	<tbody>
            	<tr>
                    <th class="blue">对应采购单：</th>
                    <td class="blue mlink">
                    	<logic:notEmpty name="paidPlan" property="salPurOrd">
                        <a class="textOverflowS" title="${paidPlan.salPurOrd.spoTil}" href="salPurAction.do?op=spoDesc&spoId=${paidPlan.salPurOrd.spoId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看合同详情" style="border:0px;">${paidPlan.salPurOrd.spoTil}</a></logic:notEmpty>
                    </td>
                   	<th>负责账号：</th>
                    <td>${paidPlan.limUser.userSeName}&nbsp;</td>
                </tr>
                 <tr>
                    <th>是否已付款：</th>
                    <td colspan="3" class="longTd">
                        <logic:equal name="paidPlan" property="sppIsp" value="1">
                            是
                        </logic:equal> 
                        <logic:equal name="paidPlan" property="sppIsp" value="0">
                            否
                        </logic:equal> 
                    </td>
                </tr>
                <tr>
                	<th>付款金额：</th>
                    <td>￥<bean:write name="paidPlan" property="sppPayMon" format="###,##0.00"/>&nbsp;</td>
                    <th>预计付款日期：</th>
                    <td><span id="planDate"></span>&nbsp;</td>
                </tr>
            </tbody>
        </table>
        <div class="descStamp">
            由
            <span>${paidPlan.sppInpUser}</span>
            于
            <span id="creDate"></span>&nbsp;
            录入
            <logic:notEmpty name="paidPlan" property="sppAltUser"><br/>
            最近由
            <span>${paidPlan.sppAltUser}</span>
            于
            <span id="altDate"></span>&nbsp;
            修改
            </logic:notEmpty>
        </div>
        <script type="text/javascript">
            removeTime("planDate","${paidPlan.sppPrmDate}",1);
            removeTime("creDate","${paidPlan.sppCreDate}",2)
            removeTime("altDate","${paidPlan.sppAltDate}",2);
        </script>
        </div>
    </logic:notEmpty>
	<logic:empty name="paidPlan">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该付款计划已被删除</div>
	</logic:empty>
  </body>
</html>

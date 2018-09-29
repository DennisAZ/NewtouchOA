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
    <title>回款记录详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
  </head>
  
  <body>
   	<logic:notEmpty name="paidPast">
    <div class="inputDiv">
        <table class="normal dashTab" style="width:98%" cellpadding="0" cellspacing="0">
        	<tbody>
                <tr>
                    <th class="blue"><nobr>对应订单：</nobr></th>
                    <td class="blue mlink" colspan="3">
                        <a class="textOverflow" style="width:360px" title="${paidPast.salOrdCon.sodTil}" href="orderAction.do?op=showOrdDesc&code=${paidPast.salOrdCon.sodCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看订单详情" style="border:0px;">${paidPast.salOrdCon.sodTil}</a>&nbsp;
                    </td>
                </tr>
                <tr>
                    <th>状态：</th>
                    <td>
                        <logic:equal name="paidPast" property="spsIsdel" value="0">
                            <img class="imgAlign" src="images/content/suc.gif" alt="已入账"/>已入账
                        </logic:equal>
                        <logic:equal name="paidPast" property="spsIsdel" value="2">
                            <img class="imgAlign" src="images/content/time.gif" alt="待入账"/>待入账
                        </logic:equal>
                        <logic:equal name="paidPast" property="spsIsdel" value="3">
                            <img class="imgAlign" src="images/content/warn.gif" alt="已撤销"/>已撤销
                        </logic:equal>&nbsp;</td>
                    <th><nobr>是否已开票：</nobr></th>
                    <td>
                        <logic:equal name="paidPast" property="spsIsinv" value="1">
                            是
                        </logic:equal> 
                        <logic:equal name="paidPast" property="spsIsinv" value="0">
                            否
                        </logic:equal> 
                    </td>
                </tr>
                <tr>
                    <th><nobr>回款日期：</nobr></th>
                    <td style="width:40%"><span id="pastDate"></span>&nbsp;</td>
                    <th><nobr>回款类别：</nobr></th>
                    <td style="width:40%">${paidPast.salPaidType.typName}&nbsp;</td>
                </tr>
                <tr>
                    <th>回款金额：</th>
                    <td>￥<bean:write name="paidPast" property="spsPayMon" format="###,##0.00"/>&nbsp;</td>
                    <th><nobr>付款方式：</nobr></th>
                    <td>${paidPast.spsPayType}&nbsp;</td>
                </tr>
                <tr>
                    <th>负责账号：</th>
                    <td colspan="3">${paidPast.spsResp.userSeName}&nbsp;</td>
                </tr>
                <tr>
                    <th style="vertical-align:top;">备注：</th>
                    <td colspan="3">
                        ${paidPast.spsRemark}&nbsp;
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="descStamp" style="border-bottom:0px">
                        由
                        <span>${paidPast.spsUserCode}</span>
                        于
                        <span id="creDate"></span>&nbsp;
                        录入
                        <logic:notEmpty name="paidPast" property="spsAltUser"><br/>
                        最近由
                        <span>${paidPast.spsAltUser}</span>
                        于
                        <span id="altDate"></span>&nbsp;
                        <logic:equal name="paidPast" property="spsIsdel" value="0">
                            确认入账
                        </logic:equal>
                        <logic:equal name="paidPast" property="spsIsdel" value="2">
                            修改
                        </logic:equal> 
                        <logic:equal name="paidPast" property="spsIsdel" value="3">
                            撤销入账
                        </logic:equal>
                        </logic:notEmpty>
                    </td>
                </tr>
            </tbody>
            <script type="text/javascript">
				removeTime("pastDate","${paidPast.spsFctDate}",1);
                removeTime("creDate","${paidPast.spsCreDate}",2)
                removeTime("altDate","${paidPast.spsAltDate}",2);
            </script>
        </table>
    </div>
    </logic:notEmpty>
	<logic:empty name="paidPast">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该回款记录已被删除</div>
	</logic:empty>
  </body>
</html>

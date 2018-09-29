<%@ page language="java" pageEncoding="UTF-8"%>
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
    <title>付款记录详情(无效)</title>
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
   	<logic:notEmpty name="paidPast">
    	<div class="inputDiv">
        <table class="normal dashTab" style="width:98%" cellpadding="0" cellspacing="0">
        	<tbody>
            	<tr>
                    <th class="blue"><nobr>对应采购单：</nobr></th>
                    <td class="blue mlink" colspan="3">
                    	<logic:notEmpty name="paidPast" property="salPurOrd">
                        <a class="textOverflow" style="width:360px" title="${paidPast.salPurOrd.spoTil}" href="salPurAction.do?op=spoDesc&spoId=${paidPast.salPurOrd.spoId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看采购单详情" style="border:0px;">${paidPast.salPurOrd.spoTil}</a></logic:notEmpty>&nbsp;
                    </td>
                </tr>
                <tr>
                    <th>状态：</th>
                    <td>
                        <logic:equal name="paidPast" property="spaIsdel" value="0">
                            <img class="imgAlign" src="crm/images/content/suc.gif" alt="已出账"/>已出账
                        </logic:equal>
                        <logic:equal name="paidPast" property="spaIsdel" value="2">
                            <img class="imgAlign" src="crm/images/content/time.gif" alt="未出账"/>未出账
                        </logic:equal>
                        <logic:equal name="paidPast" property="spaIsdel" value="3">
                            <img class="imgAlign" src="crm/images/content/fail.gif" alt="已撤销"/>已撤销
                        </logic:equal>&nbsp;</td>
                    <th><nobr>是否已开票：</nobr></th>
                    <td>
                        <logic:equal name="paidPast" property="spaIsinv" value="1">
                            是
                        </logic:equal> 
                        <logic:equal name="paidPast" property="spaIsinv" value="0">
                            否
                        </logic:equal> 
                    </td>
                </tr>
                <tr>
                    <th><nobr>付款日期：</nobr></th>
                    <td style="width:40%"><span id="pastDate"></span>&nbsp;</td>
                    <th><nobr>付款类别：</nobr></th>
                    <td style="width:40%">${paidPast.typeList.typName}&nbsp;</td>
                </tr>
                <tr>
                    <th>付款金额：</th>
                    <td>￥<bean:write name="paidPast" property="spaPayMon" format="###,##0.00"/>&nbsp;</td>
                    <th><nobr>付款方式：</nobr></th>
                    <td>${paidPast.spaPayType}&nbsp;</td>
                </tr>
                   
                
                <tr>
                    <th>负责账号：</th>
                    <td colspan="3">${paidPast.limUser.userSeName}&nbsp;</td>
                </tr>
                <tr>
                    <th style="vertical-align:top;">备注：</th>
                    <td colspan="3">
                        ${paidPast.spaRemark}&nbsp;
                    </td>
                </tr>
                
                <tr>
                    <td colspan="4" class="descStamp" style="border-bottom:0px">
                        由
                        <span>${paidPast.spaInpUser}</span>
                        于
                        <span id="creDate"></span>&nbsp;
                        录入
                        <logic:notEmpty name="paidPast" property="spaAltUser"><br/>
                        最近由
                        <span>${paidPast.spaAltUser}</span>
                        于
                        <span id="altDate"></span>&nbsp;
                        修改
                        </logic:notEmpty>
                    </td>
                </tr>
            </tbody>
            
            <script type="text/javascript">
				removeTime("pastDate","${paidPast.spaFctDate}",1);
                removeTime("creDate","${paidPast.spaCreDate}",2)
                removeTime("altDate","${paidPast.spaAltDate}",2);
            </script>
        </table>
        </div>
    </logic:notEmpty>
	<logic:empty name="paidPast">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该付款记录已被删除</div>
	</logic:empty>
  </body>
</html>

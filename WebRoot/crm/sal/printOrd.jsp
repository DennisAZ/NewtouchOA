<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int c=0;
	int j=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>打印订单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript">
		function printdiv(printpage){
			var headstr = "<html><head><title></title></head><body>";
			var footstr = "</body>";
			var newstr = document.all.item(printpage).innerHTML;
			var oldstr = document.body.innerHTML;
			document.body.innerHTML = headstr+newstr+footstr;
			window.print(); 
			document.body.innerHTML = oldstr;
			return false;
		}
		
	</script>
  </head>
  <body>
  	<table>
  		<tr>
  			<td align="center">
  				<button onClick="printdiv('printDiv')">打印</button> &nbsp; <button onClick="window.close()">取消</button>
  			</td>
  		</tr>
  		
		<tr> 
			<td>	
				<div id="printDiv">	
				<table style="border-top:#000 1px solid;width:100%;"  cellspacing="0" cellpadding="0">
						<tr>
							<td width="12%" height="32" style="border-bottom:#000 1px solid;width:13%;">订单主题：</td>
							<td style="border-bottom:#000 1px solid;" colspan="3">${order.sodTil}&nbsp;</td>
						</tr>
                    	<tr>
                            <td width="10%">对应客户：</td>
                            <td width="40%">
                                <logic:notEmpty name="order" property="cusCorCus">
                                    ${order.cusCorCus.corName}&nbsp;
                                </logic:notEmpty>&nbsp;
                          </td>
							<td width="10%">客户电话：</td>
							<td width="40%" >
								<logic:notEmpty name="order" property="cusCorCus">
                                    ${order.cusCorCus.corPhone}&nbsp;
                                </logic:notEmpty>
						  </td>
						</tr>
						<tr>
							<td>客户邮编：</td>
							<td>
								<logic:notEmpty name="order" property="cusCorCus">
                                    ${order.cusCorCus.corZipCode}&nbsp;
                                </logic:notEmpty>
							</td>
							<td>客户地址：</td>
							<td>
								<logic:notEmpty name="order" property="cusCorCus">
                                    ${order.cusCorCus.corAddress}&nbsp;
                                </logic:notEmpty>
							</td>
						</tr>  
                        <tr> 
							<td>订单号：</td>
							<td>${order.sodNum}&nbsp;</td>
                        	<td>总金额：</td>
                            <td>￥<bean:write name="order" property="sodSumMon" format="###,##0.00"/>&nbsp;
                        </tr>
                        <tr>
                            <td>签订日期：</td>
                            <td><span id="conDate"></span>&nbsp;</td>
                            <td>签单人：</td>
                            <td>${order.salEmp.seName}&nbsp;</td>
                        </tr>
                        <tr>
							<td>条件与条款：</td>
							<td colspan="3">
                            	${order.sodContent}&nbsp;
							</td>
						</tr>
						<tr> 
                            <td style="border-bottom:#000 1px solid;">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
                            <td colspan="3" style="border-bottom:#000 1px solid;">${order.sodRemark}&nbsp;</td>
                        </tr>
  </table>
                	<table style="border-bottom:#000 1px solid;width:100%;" cellspacing="0" cellpadding="0">
						<tr>
							<td height="29" colspan="7" style="border-bottom:#000 1px solid;">订单明细：</td>
						</tr>
						<tr>
							<td align="center" style="border-bottom:#000 1px solid;width:15%;">名称</td>
							<td align="center" style="border-bottom:#000 1px solid;width:15%;">编号</td>
							<td align="center" style="border-bottom:#000 1px solid;width:10%;">价格</td>
							<td align="center" style="border-bottom:#000 1px solid;width:10%;">数量</td>
							<td align="center" style="border-bottom:#000 1px solid;width:10%;">单位</td>
							<td align="center" style="border-bottom:#000 1px solid;width:10%;">总价</td>
							<td align="center" style="border-bottom:#000 1px solid;width:30%;">备注</td>
						</tr>
						<logic:notEmpty name="order" property="ROrdPros">
							<logic:iterate id="rop" name="order" property="ROrdPros">
								<tr>
									<td align="center">${rop.wmsProduct.wprName}&nbsp;</td>
									<td align="center">${rop.wmsProduct.wprCode}&nbsp;</td>
									<td>${rop.ropPrice}&nbsp;</td>
									<td align="center">${rop.ropNum}&nbsp;</td>
									<td align="center">${rop.wmsProduct.typeList.typName}&nbsp;</td>
									<td>${rop.ropRealPrice}&nbsp;</td>
									<td>${rop.ropRemark}&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
			  </table>
			  </div>
			</td>
			</tr>
		</table>
  </body>
</html>

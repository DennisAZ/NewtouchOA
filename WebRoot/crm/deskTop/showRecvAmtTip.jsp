<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0,count2=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>显示应收款信息</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/cus.js"></script>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
        <style type="text/css">
    	body{
			background-color:#fff;
			text-align:left;
		}
    </style>
     <script type="text/javascript">
    	function initForm(){
			$("recDate").value = "${recDate}";	
			$("time").innerHTML = "${time}";
			$("num").innerHTML = "${num}";
			${"totalNum"}.innerHTML =  "${totalRecvNum}";
			${"ticketNum"}.innerHTML = "${totalTicketNum}";
		}
		
    	window.onload=function(){
			initForm();
			//增加清空按钮
			//createCancelButton("",'searchForm',-50,5,'searButton','after');
		}
    </script>
  </head>
  
  <body>
  			   <div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm"  action="customAction.do">
                	    <input type="hidden" name="op" value="toShowRecvAmt" />
                	    当前总应收开票余额<span id="ticketNum"></span>元，当前总应收发货余额<span id="totalNum"></span>元<br/>
                		截止到<span id="time"></span>，到期应收余额:<span id="num"></span>元<br/>
						收款日期：<input name="recDate" id="recDate" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand"
                          readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="submit" class="butSize3 inputBoxAlign" id="searButton" value="查询" />&nbsp;&nbsp;
                    </form>
                </div>
                <logic:notEmpty name="list">
                <div style="padding:10px; text-align:center;">
                  <table  class="normal rowstable" cellpadding="0" cellspacing="0" style="width:95%">		
                    <tr>
	                    <th style="width:40%">单位名称</th>
	                    <th style="width:30%">到期金额</th>
	                    <th style="width:30%;align:center">业务员</th>
                	</tr>
                    <logic:iterate id="l" name="list">
                        <tr id="tr<%= count2%>" onMouseOver="focusTr('tr',<%= count2%>,1)" onMouseOut="focusTr('tr',<%= count2%>,0)">
                            <td>&nbsp;<a href='javascript:void(0)' onclick="descPop('customAction.do?op=showCompanyCusDesc&corCode=${l.cusId}')">${l.cusName}</a></td>
							<td>&nbsp;<bean:write name="l" property="recvNum" format="0.00"/></td>
							<td>&nbsp;${l.empName}</td>
                        </tr>
                        <script type="text/javascript">
                            rowsBg('tr',<%=count2%>);
                        </script>
                     <%count2++;%>
                    </logic:iterate>
                  </table>
                  </div>
                </logic:notEmpty>
                
                <logic:empty name="list">
        			<div class="gray">&nbsp;&nbsp;没有数据</div>
                </logic:empty>
  </body>

</html>

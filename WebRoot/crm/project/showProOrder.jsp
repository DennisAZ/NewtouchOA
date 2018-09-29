<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>项目订单</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/ord.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
		createIfmLoad('ifmLoad');//进度条
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
     <div class="divWithScroll2 innerIfm">
     	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
				<th>项目订单</th>
                <th style="font-size:12px; font-weight:normal;text-align:right">
                 该项目签单数:<span class="blue bold">${count}</span>&nbsp;&nbsp;&nbsp;总金额:<span class="brown bold">￥<bean:write name="sumMon" format="###,##0.00"/></span>&nbsp;&nbsp;&nbsp;已回款:<span class="brown bold">￥<bean:write name="paidMon" format="###,##0.00"/></span>
                </th>
            </tr>
        </table>
        
        <table id="tab" class="normal rowstable" style="width:100%" cellpadding="0" cellspacing="0">
            <tr>
                <th style="width:5%">状态</th>
                <th style="width:25%">订单主题</th>
                <th style="width:12%">订单号</th>
                <th style="width:8%">类别</th>
                <th style="width:12%">已回款</th>
                <th style="width:12%">总金额</th>
                <th style="width:10%">签订时间</th>
                <th style="width:10%">交付日期</th>
                <th style="width:6%; border-right:0px">签单人</th>
            </tr>
            <logic:notEmpty name="ordList">
            	<logic:iterate id="ordList" name="ordList" indexId="count">
                	<tr id="tr${count}" onMouseOver="focusTr('tr','${count}',1)" onMouseOut="focusTr('tr','${count}',0)" onDblClick="descPop('orderAction.do?op=showOrdDesc&code=${ordList.sodCode}')">
                       	<td style="text-align:center">
                            <logic:notEmpty name="ordList" property="sodAppIsok">
                                <logic:equal value="0" name="ordList" property="sodAppIsok"><img src="images/content/fail.gif" alt="未通过"></logic:equal>
                                <logic:equal value="1" name="ordList" property="sodAppIsok"><img src="images/content/suc.gif" alt="已通过"></logic:equal>
                            </logic:notEmpty>
                            <logic:empty name="ordList" property="sodAppIsok">
                                <img src="images/content/time.gif" alt="未审核(未添加产品)">
                            </logic:empty>
                        </td>
                       	<td>
                          <a href="orderAction.do?op=showOrdDesc&code=${ordList.sodCode}" target="_blank" style=" cursor:pointer">${ordList.sodTil}</a></td>  
                       	<td>${ordList.sodNum}&nbsp;</td>
                       	<td>${ordList.salOrderType.typName}&nbsp;</td>
						<td>
							<logic:notEmpty name="ordList" property="sodPaidMon">
                              ￥<bean:write name="ordList" property="sodPaidMon" format="###,##0.00"/>
                            </logic:notEmpty>
						  	<logic:empty name="ordList" property="sodPaidMon">￥0.00</logic:empty>
						</td>
                        <td>￥<bean:write name="ordList" property="sodSumMon" format="###,##0.00"/></td>
						<td><span id="conDate${count}"></span>&nbsp;</td>
                      	<td><span id="deadLine${count}"></span>
                        	<logic:empty name="ordList" property="sodDeadline">
                                <span class="gray">未设定</span>
                            </logic:empty>
                          	<logic:equal value="未交付" name="ordList" property="sodShipState">
                            	<img src="images/content/tofinish.gif" alt="未交付" style="vertical-align:middle"/>
                            </logic:equal>
							<logic:equal value="部分交付" name="ordList" property="sodShipState">
                            	<img src="images/content/doing.gif" alt="部分交付" style="vertical-align:middle"/>
                            </logic:equal>
                            <logic:equal value="全部交付" name="ordList" property="sodShipState">
                            	<img src="images/content/finish.gif" alt="全部交付" style="vertical-align:middle"/>
                            </logic:equal>
                        </td>
                        <td>${ordList.salEmp.seName}&nbsp;</td>
                 </tr>
					<script type="text/javascript">
                        dateFormat('deadLine','${ordList.sodDeadline}','${count}');
                        dateFormat('conDate','${ordList.sodConDate}','${count}');
                        rowsBg('tr','${count}');
                    </script>
                </logic:iterate>
            </logic:notEmpty>
            <logic:empty name="ordList">
                    <tr>
                        <td class="noDataTd" colspan="9">未找到相关数据!</td>
                    </tr>    
            </logic:empty>
        </table>
     	<logic:notEmpty name="ordList">
            <script type="text/javascript">
                createPage('projectAction.do?op=getProOrder&proId=${proId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
            </script>
      	</logic:notEmpty>
    </div>			
   
  </body>

</html>

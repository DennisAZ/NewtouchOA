<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
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
    <title>采购单(项目详情)</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/pro.js"></script>
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
                <th>采购单</th>
				<th style="font-size:12px; font-weight:normal; text-align:right">
                 该项目采购单数:<span class="blue bold">${count}</span>&nbsp;&nbsp;&nbsp;总金额:<span class="brown bold">￥<bean:write name="sumMon" format="###,##0.00"/></span>&nbsp;&nbsp;&nbsp;已付款:<span class="brown bold">￥<bean:write name="paidMon" format="###,##0.00"/></span>
                </th>
            </tr>
        </table>
		<table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
                        <tr>
                            <th style="width:4%; text-align:center;">状态</th>
                            <th style="width:12%">采购单主题</th>
                            <th style="width:12%">采购单号</th>
                            <th style="width:8%">类别</th>
                            <th style="width:10%">对应供应商</th>
							<th style="width:14%">已付款/总金额</th>
							<th style="width:10%">采购日期</th>
							<th style="width:12%">交付状态</th>
                            <th style="width:6%; border-right:0px">负责账号</th>
                            <!--<th style=" width:10%; border-right:0px">操作</th>-->
                        </tr>
                        <logic:notEmpty name="spoList">
                         <logic:iterate id="spo" name="spoList">
                         <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('salPurAction.do?op=spoDesc&spoId=${spo.spoId}')">
                          <td style="text-align:center">
                           <logic:notEmpty name="spo" property="spoAppIsok">
                                <logic:equal value="0" name="spo" property="spoAppIsok"><img src="images/content/fail.gif" alt="未通过"></logic:equal>
                                <logic:equal value="1" name="spo" property="spoAppIsok"><img src="images/content/suc.gif" alt="已通过"></logic:equal>
                            </logic:notEmpty>
                            <logic:empty name="spo" property="spoAppIsok">
                                <img src="images/content/time.gif" alt="未审核(未添加产品)">
                            </logic:empty>
                          </td>
                          <td>
                          <a href="salPurAction.do?op=spoDesc&spoId=${spo.spoId}" target="_blank" style=" cursor:pointer">${spo.spoTil}</a></td>  
                          <td>${spo.spoCode}&nbsp;</td>
                          <td>${spo.typeList.typName }&nbsp;</td>
                          <td class="mLink">
                          	<logic:notEmpty name="spo" property="salSupplier">
                                <a title="${spo.salSupplier.ssuName}" href="salSupplyAction.do?op=showSupplyDesc&supId=${spo.salSupplier.ssuId}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${spo.salSupplier.ssuName}</a>
                            </logic:notEmpty>&nbsp;
                          </td>
						  <td>
						   <logic:notEmpty name="spo" property="spoPaidMon">
						  ￥<bean:write name="spo" property="spoPaidMon" format="###,##0.00"/>
						  </logic:notEmpty>
						  <logic:empty name="spo" property="spoPaidMon">￥0.00</logic:empty>
						  /￥<bean:write name="spo" property="spoSumMon" format="###,##0.00"/></td>
						  <td><label id="conDate<%=count%>"></label>&nbsp;</td>
                          <td>
                          	<logic:equal value="0" name="spo" property="spoIsend">未交付
                            	<img src="images/content/tofinish.gif" alt="未交付" style="vertical-align:middle"/>
                            </logic:equal>
							<logic:equal value="1" name="spo" property="spoIsend">部分交付
                            	<img src="images/content/doing.gif" alt="部分交付" style="vertical-align:middle"/>
                            </logic:equal>
                            <logic:equal value="2" name="spo" property="spoIsend">全部交付
                            	<img src="images/content/finish.gif" alt="全部交付" style="vertical-align:middle"/>
                            </logic:equal>
                          </td>
						  
                          <td>${spo.limUser.userSeName}&nbsp;</td>
                         </tr>
                        <script type="text/javascript">
							dateFormat('conDate','${spo.spoConDate}',<%=count%>);
                            rowsBg('tr',<%=count%>);
                        </script>
                            <% count++; %>
                         </logic:iterate>
                         </logic:notEmpty>
                         <logic:empty name="spoList">
                            <tr>
                                <td class="noDataTd" colspan="9">未找到相关数据!</td>
                            </tr>	
                                    
                        </logic:empty>
                    </table>
					<logic:notEmpty name="spoList">		
                 	<script type="text/javascript">
						createPage('salPurAction.do?op=getProSpo&proId=${proId}&proTitle='+ encodeURIComponent('${proTitle}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
           			</logic:notEmpty>			
   </div>
  </body>

</html>

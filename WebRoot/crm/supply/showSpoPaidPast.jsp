<%@ page language="java" pageEncoding="UTF-8"%>
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
    <title>付款记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
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
                <th>付款记录</th>
            </tr>
        </table>
        <table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
        	<tr>
                <th style="width:6%">状态</th>
                <th style="width:10%">出账类别</th>
                <th style="width:20%">摘要</th>
                <th style="width:18%">入方</th>
                <th style="width:18%">出方(账户)</th>
                <th style="width:12%">出账金额</th>
                <th style="width:6%">经手人</th>
                <th style="width:10%;border-right:0px">出账日期</th>
            </tr>
			<logic:notEmpty name="pastList">
                <logic:iterate id="paidPast" name="pastList" indexId="listId">
                <tr id="pasttr${listId}" onMouseOver="focusTr('pasttr',${listId},1)" onMouseOut="focusTr('pasttr',${listId},0)" onDblClick="parent.addDivNew(9,${paidPast.spaId})">
                    <td style="text-align:center">
                        <logic:equal name="paidPast" property="spaIsdel" value="0">
                            <img src="crm/images/content/suc.gif" alt="已出账"/>
                        </logic:equal>
                        <logic:equal name="paidPast" property="spaIsdel" value="2">
                            <img src="crm/images/content/time.gif" alt="待出账"/>
                        </logic:equal>
                        <logic:equal name="paidPast" property="spaIsdel" value="3">
                            <img src="crm/images/content/warn.gif" alt="已撤销"/>
                        </logic:equal>&nbsp;
                    </td>
                    <td>${paidPast.outTypeList.typName}&nbsp;</td>
                    <td><a href="javascript:void(0)" onClick="parent.addDivNew(9,${paidPast.spaId});return false;">${paidPast.spaContent}</a>&nbsp;</td>
                    <td>${paidPast.spaInName}&nbsp;</td>
                    <td>${paidPast.account.acoName}&nbsp;</td>
                    <td>
                    <logic:notEmpty name="paidPast" property="spaPayMon">
                        ￥<bean:write name="paidPast" property="spaPayMon" format="###,##0.00"/>
                    </logic:notEmpty>
                    <logic:empty name="paidPast" property="spaPayMon">￥0.00</logic:empty>
                    </td>
                    <td>
                        <logic:notEqual value="2" name="paidPast" property="spaIsdel">
                            ${paidPast.spaAltUser}
                        </logic:notEqual>&nbsp;
                    </td>
                    <td><span id="outDate${listId}"></span>&nbsp;</td>
                </tr>
                <script type="text/javascript">
                    if('${paidPast.spaIsdel}'=='3'){
                        $('pasttr'+'${listId}').style.color="#999";
                    }
                    dateFormat('outDate','${paidPast.spaFctDate}','${listId}');
                    rowsBg('pasttr','${listId}');
                </script>
				</logic:iterate>
			</logic:notEmpty>
            <logic:empty name="pastList">
                <tr>
                    <td colspan="8" class="gray" style=" text-align:center;">暂未添加付款记录...</td>
                </tr>
            </logic:empty>
        </table>
		<logic:notEmpty name="pastList">	
			<script type="text/javascript">
                createPage('salPurAction.do?op=getSpoPaidPast&id=${id}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
            </script>
        </logic:notEmpty>
   </div>
  </body>
</html>

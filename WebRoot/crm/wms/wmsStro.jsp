<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
Page pageobj=(Page)request.getAttribute("page"); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>选择产品</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/wms.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/choosePro.js"></script>
    <script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript">
		
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			//增加清空按钮
			createCancelButton('searchForm',-50,5);
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
  		<div id="mainbox">
    	<div id="contentbox">
        	<div id="title">库存管理 > 选择仓库中产品</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" style="cursor:auto">库存列表</div>
                    </div>
                    </th>
                </tr>
            </table>
            <div id="listContent">
            	<div class="listSearch">
                	<form id="searchForm" action="wwoAction.do" method="get">
                         <input type="hidden" name="op" value="stroSearch" />
                         <input type="hidden" name="wmsCode" value="${wmsCode}">
                         产品名：<input class="inputSize2 inputBoxAlign" type="text" name="wprName" value="${wprName}" id="wprName"/>&nbsp;&nbsp;
                         <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   </form>
                </div>
                <div class="dataList">	
                    <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0" style="table-layout:fixed">
                        <tr>
                            <th style="width:35%">产品名称/型号</th>
                            <th style="width:20%">产品编号</th>
                            <th style="width:15%">库存数量[单位]</th>
                            <th style="width:30%;border-right:0px">所在仓库</th>
                        </tr>
                        <logic:notEmpty name="rstroPro">
                        <logic:iterate id="rsp" name="rstroPro">
                        <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                            <td><a href="javascript:void(0);return false;" onClick="chooseStroPro('${rsp.wmsProduct.wprId}','${rsp.wmsProduct.wprName}','${rsp.wmsProduct.wprModel}','<bean:write name='rsp' property='rspProNum' format='###,##0.00'/>');return false;">${rsp.wmsProduct.wprName}<logic:notEmpty name="rsp" property="wmsProduct.wprModel">/${rsp.wmsProduct.wprModel}</logic:notEmpty></a>
                            </td>
                            <td>${rsp.wmsProduct.wprCode}&nbsp;</td>
                            <td><span id="num<%=count%>"></span>&nbsp;<logic:notEmpty name="rsp" property="wmsProduct.typeList"><span class="brown">[${rsp.wmsProduct.typeList.typName}]</span></logic:notEmpty></td>
                            <td style="border-right:0px;">${rsp.wmsStro.wmsName}</td>
                        </tr>
                              <script type="text/javascript">
                              var num="<bean:write name='rsp' property='rspProNum' format='###,##0.00'/>";
                              if(parseFloat(num)<=0.0){
                                $("num"+<%=count%>).innerHTML="<span class='red'>"+num+"</span>";
                              }else{
                                $("num"+<%=count%>).innerText=num;
                              }
                                rowsBg('tr',<%=count%>);
                                </script>
                         <% count++; %>
                    	</logic:iterate>
                    	</logic:notEmpty>
                     	<logic:empty name="rstroPro">
                        <tr>
                            <td colspan="4" class="noDataTd">未找到相关数据!</td>
                        </tr>
                		</logic:empty> 
                    </table>
                    	<logic:notEmpty name="rstroPro">
                    		<script type="text/javascript">
                                createPage('wwoAction.do?op=stroSearch&wprName='+ encodeURIComponent('${wprName}') +'&wmsCode=${wmsCode}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                            </script>
                       </logic:notEmpty>
                </div>		
            </div>
        </div> 
        </div>
  </body>
</html>

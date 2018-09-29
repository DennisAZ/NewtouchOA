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
    
    <title>选择供应商</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
    <script type="text/jscript">
	window.onload=function(){
		//表格内容省略
		loadTabShort("tab");
		//增加清空按钮
		createCancelButton('sear',-120,5);
	}
  </script>
  </head>
  
  <body>
  	<div class="browLayer">
        <div class="listSearch">
            <form action="salSupplyAction.do" id="sear" method="get">
                <input type="hidden" name="op" value="getSelSupByNameCode" />
                供应商名称：<input class="inputSize2 inputBoxAlign" type="text" name="supName" value="${supName}"/>
                &nbsp;
                编号：<input class="inputSize2 inputBoxAlign" type="text" name="supCode" value="${supCode}"/>
                <input class="inputBoxAlign butSize3" type="button" value="查询" onClick="formSubmit('sear')"/>
            </form>
        </div>
           
        <div class="dataList">		
            <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                <tr>
                     <th style="width:35%">供应商名称</th>
                     <th style="width:50%">供应产品</th>
                     <th style="width:15%;border-right:0px">电话</th>
                </tr>
                <logic:notEmpty name="allSupList">	
                <logic:iterate id="supply" name="allSupList">
                <tr id="tr${count}" onMouseOver="focusTr('tr','${count}',1)" onMouseOut="focusTr('tr','${count}',0)">
                    <td><a href="javascript:void(0)" onClick="chooseSupComplete('${supply.ssuId}','${supply.ssuName}');return false;">${supply.ssuName}</a>&nbsp;</td>
                    <td>${supply.ssuPrd}&nbsp;</td>
                    <td>${supply.ssuPhone}&nbsp;</td>
                </tr>
                <script type="text/javascript">
                    rowsBg('tr','${count}');
                </script>
                 <% count++; %>
                 </logic:iterate>
                 </logic:notEmpty>
                 <logic:empty name="allSupList">
                    <tr>
                        <td colspan="3" class="noDataTd">未找到相关数据!</td>
                    </tr>
                </logic:empty>
           </table> 
           <logic:notEmpty name="allSupList">
				<script type="text/javascript">
                    var url;
                    switch('${pageType}'){
                        case 'default':
                            url = 'salSupplyAction.do?op=getSelectSup';
                            break;
                        case 'searSup':
                            url = 'salSupplyAction.do?op=getSelSupByNameCode&supName='+ encodeURIComponent('${supName}') +'&supCode=${supCode}';
                            break;
                    }
                    createPage(url,'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}',false);
                </script>
            </logic:notEmpty>
        </div>   
   	</div>
  </body>
</html>

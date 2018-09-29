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
    <title>询价单(项目详情)</title>
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
                <th>询价单</th>
                <td><a href="javascript:void(0)" onClick="parent.projPopDiv(3,'${proId}','${proTitle}');return false;" class="newBlueButton">新建询价单</a></td>
            </tr>
        </table>		
        <table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
            <tr>
                <th style="width:12%">询价主题</th>
                <th style="width:12%">对应供应商</th>
                <th style="width:12%">对应项目</th>
                <th style="width:10%">询价金额</th>
                <th style="width:14%">询价日期</th>
                <th style="width:10%">询价人</th>
                <th style=" width:10%; border-right:0px">操作</th>
            </tr>
            <logic:notEmpty name="inqList">
             <logic:iterate id="inq" name="inqList">
             <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('salPurAction.do?op=inqDesc&inqId=${inq.inqId}')">
              <td>
              <a href="salPurAction.do?op=inqDesc&inqId=${inq.inqId}" target="_blank" style=" cursor:pointer">${inq.inqTitle}</a></td>  
              <td class="mLink">
                <logic:notEmpty name="inq" property="salSupplier">
                    <a title="${inq.salSupplier.ssuName}" href="salSupplyAction.do?op=showSupplyDesc&supId=${inq.salSupplier.ssuId}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${inq.salSupplier.ssuName}</a>
                </logic:notEmpty>&nbsp;
              </td>
              <td class="mLink">
                <logic:notEmpty name="inq" property="project">
                    <a title="${inq.project.proTitle}" href="projectAction.do?op=showProDesc&proId=${inq.project.proId}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看项目详情" style="border:0px;">${inq.project.proTitle}</a>
                </logic:notEmpty>&nbsp;
              </td>
              <td>
               <logic:notEmpty name="inq" property="inqPrice">
             ￥<bean:write name="inq" property="inqPrice" format="###,##0.00"/>
              </logic:notEmpty>
              <logic:empty name="inq" property="inqPrice">￥0.00</logic:empty>
              </td>
              <td><label id="inqDate<%=count%>"></label>&nbsp;</td>
              <td>${inq.salEmp.seName}&nbsp;</td>
              <td>&nbsp;
                <a href="salPurAction.do?op=inqDesc&inqId=${inq.inqId}" target="_blank">
                <img src="images/content/detail.gif" border="0" alt="查看"/></a>&nbsp;&nbsp;
                <img onClick="parent.projPopDiv(31,'${inq.inqId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                <img onClick="parent.projDelDiv(3,'${inq.inqId}',1)" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>&nbsp;
             </td>
             </tr>
            <script type="text/javascript">
                dateFormat('inqDate','${inq.inqDate}',<%=count%>);
                rowsBg('tr',<%=count%>);
            </script>
                <% count++; %>
             </logic:iterate>
             </logic:notEmpty>
             <logic:empty name="inqList">
                <tr>
                    <td class="noDataTd" colspan="7">未找到相关数据!</td>
                </tr>	
                        
            </logic:empty>
        </table>
        <logic:notEmpty name="inqList">		
        <script type="text/javascript">
            createPage('salPurAction.do?op=getProInq&proId=${proId}&proTitle='+ encodeURIComponent('${proTitle}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
        </script>
        </logic:notEmpty>	
   </div>
  </body>

</html>

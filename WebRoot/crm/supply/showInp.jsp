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
    <title>询价单列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/spo.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
		function cusEmpty(obj){
			obj.value="";
			$("supId").value="";
			
		}

		
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
        	<div id="title">
            	<table>
                	<tr>
                    	<th>采购管理 >询价单</th>
                        <td><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="刷新">&nbsp;</a></td>
                    </tr>
                </table>
            </div>
  	   		<table id="mainTop" class="normal" width="100%" cellpadding="0px" cellspacing="0px">
                <tr>
                    <td height="35px">
                        <div id="tabType">
                                <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='salPurAction.do?op=showInp'">询价单</div>    
                        </div>
                     </td> 
                     <td width="110px">
                    	<a href="javascript:void(0)" onClick="addDivNew(11);return false;" class="newBlueButton">新建询价单</a>
                    </td>
                </tr>
            </table>
             <div id="listContent">
             	<div class="listSearch">
                	<form id="searchForm" action="salPurAction.do" method="get" >
                        <input type="hidden" name="op" value="showInp" />
                        主题：<input class="inputSize2 inputBoxAlign" type="text" name="inqTitle" value="${inqTitle}" style="width:120px"/>&nbsp;
                        <input type="text" name="supId" id="supId" value="${supId}" style="display:none" />
                        对应供应商：<input id="supName" name="supName" class="inputSize2 inputBoxAlign lockBack" style=" width:100px;cursor:hand" type="text" value="${supName}" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="addDivBrow(8)">选择</button>&nbsp;&nbsp;&nbsp;&nbsp;
                         询价日期：<input name="startTime" value="${startTime}" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px;" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                          到&nbsp;<input name="endTime" value="${endTime}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                          <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   	</form>
                </div>
                <div class="dataList">			
                    <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                        <tr>
                            <th style="width:12%">询价主题</th>
                            <th style="width:12%">对应供应商</th>
                            <th style="width:12%">对应项目</th>
                            <th style="width:10%">询价金额</th>
							<th style="width:14%">询价日期</th>
							<th style="width:10%">询价人</th>
                            <th style=" width:10%; border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="inquiry">
                         <logic:iterate id="inq" name="inquiry">
                         <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('salPurAction.do?op=inqDesc&inqId=${inq.inqId}')">
                          <td>
                          <a href="salPurAction.do?op=inqDesc&inqId=${inq.inqId}" target="_blank" style=" cursor:pointer">${inq.inqTitle}</a></td>  
                          <td class="mLink">
                          	<logic:notEmpty name="inq" property="salSupplier">
                                <a title="${inq.salSupplier.ssuName}" href="salSupplyAction.do?op=showSupplyDesc&supId=${inq.salSupplier.ssuId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${inq.salSupplier.ssuName}</a>
                            </logic:notEmpty>&nbsp;
                          </td>
						  <td class="mLink">
                          	<logic:notEmpty name="inq" property="project">
                                <a title="${inq.project.proTitle}" href="projectAction.do?op=showProDesc&proId=${inq.project.proId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看项目详情" style="border:0px;">${inq.project.proTitle}</a></logic:notEmpty>&nbsp;
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
                            <img src="crm/images/content/detail.gif" border="0" alt="查看"/></a>&nbsp;&nbsp;
                            <img onClick="addDivNew(12,'${inq.inqId}')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                            <img onClick="delDiv(5,'${inq.inqId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>&nbsp;
                         </td>
                         </tr>
                        <script type="text/javascript">
							dateFormat('inqDate','${inq.inqDate}',<%=count%>);
                            rowsBg('tr',<%=count%>);
                        </script>
                            <% count++; %>
                         </logic:iterate>
                         </logic:notEmpty>
                         <logic:empty name="inquiry">
                            <tr>
                                <td class="noDataTd" colspan="7">未找到相关数据!</td>
                            </tr>	
                                    
                        </logic:empty>
                    </table>
                    <logic:notEmpty name="inquiry">
                            <script type="text/javascript">
                                createPage('salPurAction.do?op=showInp&inqTitle='+encodeURIComponent('${inqTitle}')+'&startTime=${startTime}&endTime=${endTime}&supId=${supId}&supName='+ encodeURIComponent('${supName}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                            </script>
                    </logic:notEmpty>
                </div>
            </div>
  		</div> 
	</div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

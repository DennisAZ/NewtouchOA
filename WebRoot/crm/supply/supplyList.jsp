 <%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//清缓存
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires", 0);  
	
	int count=0;
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>供应商列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sup.js"></script>
    <script type="text/jscript">
     
	 function allSupply(){
	 	self.location.href="salSupplyAction.do?op=getAllSupply";
	 }
	
	createProgressBar();
	window.onload=function(){
		
		//表格内容省略
		loadTabShort("tab");
		//增加清空按钮
		createCancelButton('searNameCode',-50,5);
		closeProgressBar();
	}
  </script></head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">
            	<table>
                	<tr>
                    	<th>采购管理 > 供应商</th>
                        <td><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="刷新">&nbsp;</a></td>
                    </tr>
                </table>
            </div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
            <tr>
            	<th>
                <div id="tabType">
                    <div id="tabType1" class="tabTypeWhite" onClick="allSupply()">
                    	供应商
                    </div>
           		</div>
                </th>
                <td>
                <a href="javascript:void(0)" onClick="supPopDiv(1);return false;" class="newBlueButton">新建供应商</a>
                </td>
            </tr>
            </table>
            
            <div id="listContent">
                <div class="listSearch">
                	<form id="searNameCode" action="salSupplyAction.do" method="get" >
                         <input type="hidden" name="op" value="findSupByNameCode" />
                         名称/编号：<input class="inputSize2 inputBoxAlign" type="text" name="codeName" value="${codeName}"/>&nbsp;&nbsp;
                         <button class="butSize3 inputBoxAlign" onClick="formSubmit('searNameCode')">查询</button>&nbsp;&nbsp;			
                    </form>
                </div>
                <div class="dataList">			
                	<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                     	<tr>
                            <th style="width:20%">供应商名称</th>
                            <th style="width:8%">类别</th>
                            <th style="width:38%">供应产品</th>
                            <th style="width:12%">电话</th>
                            <th style="width:12%">传真</th>
                            <th style="width:10%;border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="supList">	
                       	<logic:iterate id="supply" name="supList">
                       	<tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('salSupplyAction.do?op=showSupplyDesc&supId=${supply.ssuId}')">
                            <td><a href="salSupplyAction.do?op=showSupplyDesc&supId=${supply.ssuId}" target="_blank" style="cursor:pointer">${supply.ssuName}</a></td>
                            <td>${supply.typeList.typName}&nbsp;</td>
                            <td>${supply.ssuPrd}&nbsp;</td>
                            <td>${supply.ssuPhone}&nbsp;</td>
                            <td>${supply.ssuFex}&nbsp;</td>
                            <td>&nbsp;
                                <a href="salSupplyAction.do?op=showSupplyDesc&supId=${supply.ssuId}" target="_blank"><img src="crm/images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                                <img onClick="supPopDiv(11,'${supply.ssuId}')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                                <img onClick="supDelDiv(1,'${supply.ssuId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>&nbsp;
                            </td>
                        </tr>
                        <script type="text/javascript">
							rowsBg('tr',<%=count%>);
                        </script>
                        <% count++; %>
                      	</logic:iterate>
                        </logic:notEmpty> 
                        <logic:empty name="supList">
                            <tr>
                                <td colspan="6" class="noDataTd">未找到相关数据!</td>
                            </tr>
                        </logic:empty>
                  </table>
               		 <logic:notEmpty name="supList">
               		 <logic:equal name="pageType" value="default">
                        <script type="text/javascript">
							createPage('salSupplyAction.do?op=getAllSupply','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
						</script>
                        </logic:equal>
                        	
                     <logic:equal name="pageType" value="codeName">
                     	<script type="text/javascript">
							createPage('salSupplyAction.do?op=findSupByNameCode&codeName='+encodeURIComponent('${codeName}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
						</script>
                     </logic:equal>
                     </logic:notEmpty>
                </div>
            </div>
        </div>
  	</div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>


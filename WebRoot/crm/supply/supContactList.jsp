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
    
    <title>供应商联系人列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sup.js"></script>
    <script type="text/jscript">
     	function chooseSup(){
			   addDivBrow(8);
		}

		function supEmpty(obj){
			obj.value="";
			$("supId").value="";
			
		}
	 function allSupCon(){
	 	self.location.href="salSupplyAction.do?op=getAllSupContact";
	 }
	
	createProgressBar();
	window.onload=function(){
		
		//表格内容省略
		loadTabShort("tab");
		//增加清空按钮
		createCancelButton('searchForm',-50,5);
		closeProgressBar();
	}
  </script></head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">
            	<table>
                	<tr>
                    	<th>采购管理 > 供应商联系人</th>
                        <td><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="刷新">&nbsp;</a></td>
                    </tr>
                </table>
            </div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
            <tr>
            	<th>
                <div id="tabType">
                    <div id="tabType1" class="tabTypeWhite" onClick="allSupCon()">
                    	联系人
                    </div>
           		</div>
                </th>
                <td>
                <a href="javascript:void(0)" onClick="supPopDiv(2);return false;" class="newBlueButton">新建联系人</a>
                </td>
            </tr>
           	</table>	
            <div id="listContent">
                <div class="listSearch">
                	<form id="searchForm" action="salSupplyAction.do" method="get" >
                        <input type="hidden" name="op" value="searCon" />
                        姓名：<input class="inputSize2 inputBoxAlign" type="text"name="conName" value="${conName}"/>&nbsp;
                        <input type="text" style="display:none" name="supId" id="supId" value="${supId}" />
                        对应供应商：<input id="supName" name="supName" class="inputSize2 inputBoxAlign lockBack" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" style=" width:120px;cursor:hand" type="text" value="${supName}"  readonly/>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="chooseSup()">选择</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   </form>
                </div>
                <div class="dataList">			
                	<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                     	<tr>
                        	<th style="width:3%"><input name="allCheck" id="allCheck" type="checkbox" onClick="selectAll()"></th>
                            <th style="width:8%">姓名</th>
                            <th style="width:10%">对应供应商</th>
                            <th style="width:8%">部门</th>
                            <th style="width:10%">职务</th>
                            <th style="width:4%">性别</th>
                            <th style="width:12%">手机</th>
                            <th style="width:12%">办公电话</th>
                            <th style="width:13%">电子邮件</th>
                            <th style="width:10%">QQ</th>
                            <th style="width:10%;border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="contactList">	
                       	<logic:iterate id="supContact" name="contactList">
                       	<tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('salSupplyAction.do?op=showSupConInfo&conId=${supContact.scnId}&isEdit=0')">
                            <td><input name="priKey" type="checkbox" value="${supContact.scnId}"></td>
                            <td><a href="salSupplyAction.do?op=showSupConInfo&conId=${supContact.scnId}&isEdit=0" target="_blank" style="cursor:pointer">${supContact.scnName}</a></td>
                            <td class="mLink">
                            <logic:notEmpty name="supContact" property="salSupplier">
                            	<a title="${supContact.salSupplier.ssuName}" href="salSupplyAction.do?op=showSupplyDesc&supId=${supContact.salSupplier.ssuId}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看供应商详情" class="imgAlign" style="border:0px;">${supContact.salSupplier.ssuName}</a>
                            </logic:notEmpty>&nbsp;
                           </td>
                            <td>${supContact.scnDep}&nbsp;</td>
                            <td>${supContact.scnService}&nbsp;</td>
                            <td>${supContact.scnSex}&nbsp;</td>
                            <td>${supContact.scnPhone}&nbsp;</td>
                            <td>${supContact.scnWorkPho}&nbsp;</td>
                            <td><logic:notEmpty name="supContact" property="scnEmail"><img src="crm/images/content/email.gif"  title="点击发送电子邮件"/><a href="javascript:void(0)" onClick="mailTo('${supContact.scnEmail}');return false;">${supContact.scnEmail}</a></logic:notEmpty>&nbsp;</td>
                            <td><logic:notEmpty name="supContact" property="scnQq"><img src="crm/images/content/qq.gif"  title="点击开始qq对话"/><a href="javascript:void(0)" onClick="qqTo('${supContact.scnQq}');return false;">${supContact.scnQq}</a></logic:notEmpty>&nbsp;</td>
                            <td>&nbsp;
                                <a href="salSupplyAction.do?op=showSupConInfo&conId=${supContact.scnId}" target="_blank"><img src="crm/images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                                <img onClick="supPopDiv(22,'${supContact.scnId}')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                                <img onClick="supDelDiv(2,'${supContact.scnId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>&nbsp;
                            </td>
                        </tr>
                        <script type="text/javascript">
							rowsBg('tr',<%=count%>);
                        </script>
                        <% count++; %>
                      	</logic:iterate>
                        </logic:notEmpty> 
                        <logic:empty name="contactList">
                            <tr>
                                <td colspan="11" class="noDataTd">未找到相关数据!</td>
                            </tr>
                        </logic:empty>
                        <tr>
                        	<td colspan="11" style="vertical-align:top">
                            <div class="bottomBar">
                                <span class="gray" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='gray'" onClick="checkBatchDel(7,'联系人')"><img src="crm/images/content/del.gif" border="0" alt="批量删除" style="vertical-align:middle; margin-bottom:2px;"/>&nbsp;批量删除</span>
                            </div>
                    		</td>
                        </tr>
                  </table>
               		 <logic:notEmpty name="contactList">
               		 <logic:equal name="pageType" value="default">
                        <script type="text/javascript">
							createPage('salSupplyAction.do?op=getAllSupContact','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
						</script>
                        </logic:equal>
                        	
                     <logic:equal name="pageType" value="sear">
                     	<script type="text/javascript">
							createPage('salSupplyAction.do?op=searCon&conName='+encodeURIComponent('${conName}')+'&supId=${supId}&supName='+ encodeURIComponent('${supName}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
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


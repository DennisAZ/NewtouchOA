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
    <title>采购单列表（未审核）</title>
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
		//传入ajax的回调方法
		function toLoadTab(){
			//加载头部标签样式
			loadListTab('${isAll}',['0','1','2']);
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
                    	<th>采购管理 > 未审核采购单</th>
                        <td><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="刷新">&nbsp;</a></td>
                    </tr>
                </table>
            </div>
  	   		<table id="mainTop" class="normal" style="width:100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td style="height:35px">
                        <div id="tabType">
                             <div id="tabType1" onClick="self.location.href='salPurAction.do?op=showSpo&isAll=0&isok=0'">我的采购单</div>                   
                             <div id="tabType2" onClick="self.location.href='salPurAction.do?op=showSpo&isAll=1&isok=0'">下属采购单</div>
                             <script type="text/javascript">writeLimAllow("pu005","<div id=\"tabType3\" onClick=\"self.location.href='salPurAction.do?op=showSpo&isAll=2&isok=0'\">全部采购单</div>","tabType","bottom",toLoadTab);</script>
                        </div>
                     </td> 
                     <td style="width:110px">
                    	<a href="javascript:void(0)" onClick="addDivNew(1);return false;" class="newBlueButton">新建采购单</a>
                    </td>
                </tr>
            </table>
             <div id="listContent">
			 	<div class="listSearch">
			 		<form id="searchForm" action="salPurAction.do" method="get" >
                        <input type="hidden" name="op" value="showSpo" />
                        <input type="hidden" name="isAll" id="isAll" value="${isAll}"/>
                        <input type="hidden" name="isok" value="${isok}" />
                        <input type="text" name="supId" id="supId" value="${supId}" style="display:none" />
                        主题/单据号：<input class="inputSize2 inputBoxAlign" type="text" name="spoTil" value="${spoTil}" style="width:100px"/>&nbsp;
                        对应供应商：<input class="inputSize2 inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" id="supName" name="supName" ondblClick="clearInput(this,'supId')" style=" width:90px;" type="text" value="${supName}" readonly/>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="addDivBrow(8)">选择</button>&nbsp;&nbsp;&nbsp;&nbsp;
                         采购日期：<input name="startTime" value="${startTime}" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                          到&nbsp;<input name="endTime" value="${endTime}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                          <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   </form>
				</div>
                <div class="dataList">
					<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                        <tr>
                            <th style="width:4%; text-align:center;">附件</th>
                            <th style="width:12%">采购单主题</th>
                            <th style="width:12%">采购单号</th>
                            <th style="width:8%">类别</th>
                            <th style="width:10%">对应供应商</th>
							<th style="width:14%">已付款/总金额</th>
							<th style="width:10%">采购日期</th>
                            <th style="width:12%">交付状态</th>
                            <th style="width:6%">负责账号</th>
                            <th style=" width:10%; border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="salPurOrd">
                        <logic:iterate id="spo" name="salPurOrd">
                        <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('salPurAction.do?op=spoDesc&spoId=${spo.spoId}')">
                          <td style="text-align:center">
                            <logic:notEmpty name="spo" property="attachments">
                                <img id="${spo.spoId}y" style="vertical-align:middle; cursor:pointer" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${spo.spoId}','spo','doc')"/>
                             </logic:notEmpty>
                             <logic:empty name="spo" property="attachments">
                                <img id="${spo.spoId}n" style="vertical-align:middle; cursor:pointer" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${spo.spoId}','spo','doc')"/>
                             </logic:empty>
                          </td>
                          <td>
                          <a href="salPurAction.do?op=spoDesc&spoId=${spo.spoId}" target="_blank" style=" cursor:pointer">${spo.spoTil}</a></td>  
                          <td>${spo.spoCode}&nbsp;</td>
                          <td>${spo.typeList.typName }&nbsp;</td>
                          <td class="mLink">
                          	<logic:notEmpty name="spo" property="salSupplier">
                                <a title="${spo.salSupplier.ssuName}" href="salSupplyAction.do?op=showSupplyDesc&supId=${spo.salSupplier.ssuId}" target="_blank" style="cursor:pointer"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${spo.salSupplier.ssuName}</a></logic:notEmpty>&nbsp;
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
                            	<img src="crm/images/content/tofinish.gif" alt="未交付" style="vertical-align:middle"/>
                            </logic:equal>
							<logic:equal value="1" name="spo" property="spoIsend">部分交付
                            	<img src="crm/images/content/doing.gif" alt="部分交付" style="vertical-align:middle"/>
                            </logic:equal>
                            <logic:equal value="2" name="spo" property="spoIsend">全部交付
                            	<img src="crm/images/content/finish.gif" alt="全部交付" style="vertical-align:middle"/>
                            </logic:equal>
                          </td>
                          <td>${spo.limUser.userSeName}&nbsp;</td>
                          <td>&nbsp;
                             <a href="salPurAction.do?op=spoDesc&spoId=${spo.spoId}" target="_blank">
                            <img src="crm/images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                            <img onClick="addDivNew(2,'${spo.spoId}')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                            <img onClick="delDiv(1,'${spo.spoId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>&nbsp;
                         </td>
                         </tr>
                        <script type="text/javascript">
							dateFormat('conDate','${spo.spoConDate}',<%=count%>);
                            rowsBg('tr',<%=count%>);
                        </script>
                            <% count++; %>
                         </logic:iterate>
                         </logic:notEmpty>
                         <logic:empty name="salPurOrd">
                            <tr>
                                <td class="noDataTd" colspan="10">未找到相关数据!</td>
                            </tr>	
                                    
                        </logic:empty>
                    </table>
                    <logic:notEmpty name="salPurOrd">
                            <script type="text/javascript">
                                createPage('salPurAction.do?op=showSpo&isAll=${isAll}&isok=${isok}&spoTil='+encodeURIComponent('${spoTil}')+'&startTime=${startTime}&endTime=${endTime}&supId=${supId}&supName='+ encodeURIComponent('${supName}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                            </script>
                    </logic:notEmpty>		
                    
                  
                </div>
            </div>
  		</div> 
	</div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

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
    
    <title>入库管理</title>
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
    <script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
		
		function loadTypes(){
			if("${wwiState1}"==""){
				$("types").value="";
			}
			else{
				$("types").value="${wwiState1}";
			}
		}
		createProgressBar();
		window.onload=function(){
			loadWmsName("${wmsName}");
			//设置判断选中按钮条件
			setCurItemStyle(new Array("${wwiAppIsok}","${wwiAppIsok}","${wwiState}","${wwiState}","${wwiState}"),new Array("2","1","0","1","2"));
			//表格内容省略
			loadTabShort("tab");
			//增加清空按钮
			createCancelButton('searchForm',-50,5);
			closeProgressBar();
			loadTypes();
		}
	</script>
  </head>

  <body>
  	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;库存管理 > <span id="wmsName">&nbsp;</span> > 入库管理</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    	<div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="window.location.href='wmsManageAction.do?op=wwiSearch&wmsCode=${wmsCode}&isok=${isok}'">入库管理</div>
                    	</div>
                    </th>
                    <td><a href="javascript:void(0)" onClick="wmsPopDiv(2,'${wmsCode}');return false;" class="newBlueButton">新建入库单</a></td>
                </tr>
            </table>
			 <div id="listContent">
             	<div class="listSearch">
                	<table class="normal" style="width:98%" cellpadding="0" cellspacing="0">
                    	<tr>
                        	<td>
                            <form id="searchForm" action="wmsManageAction.do" method="get" >
                                 <input type="hidden" name="op" value="wwiSearch" />
                                 <input type="hidden" name="wmsCode" value="${wmsCode}"/>
                                 <input type="hidden" name="isok" value="${isok}"/>
                                 主题/单据号：<input class="inputSize2 inputBoxAlign" style="width:100px;"  type="text" name="wwiTitle" value="${wwiTitle}"/>&nbsp;&nbsp;
								  日期：<input name="startTime" value="${startTime}" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                  到&nbsp;<input name="endTime" value="${endTime}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
				  				  入库状态：<select name="wwiState1" id="types" class="inputBoxAlign">
                                        <option value="">全部</option>
                                        <option value="0">未入库</option>
                                        <option value="1">已入库</option>
                                        <option value="2">已撤销</option>
                                     </select>
                                 &nbsp;&nbsp;<button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                            </form>
                            </td>
                            <td style="text-align:right"><span id="sName"></span></td>
                        </tr>
                    </table>
                </div>
             	<div id="topChoose" class="listTopBox">
                	<a href="wmsManageAction.do?op=wwiSearch&wmsCode=${wmsCode}&isok=${isok}&wwiAppIsok=2"><span class="bold">${appCount}</span>条单据等待审核</a>&nbsp;
                    <a href="wmsManageAction.do?op=wwiSearch&wmsCode=${wmsCode}&isok=${isok}&wwiAppIsok=1"><span class="bold">${inWmsCount}</span>条已审单据待入库</a>&nbsp;
                    <a href="wmsManageAction.do?op=wwiSearch&wmsCode=${wmsCode}&isok=${isok}&wwiState=0">&nbsp;未入库&nbsp;</a>&nbsp;
                    <a href="wmsManageAction.do?op=wwiSearch&wmsCode=${wmsCode}&isok=${isok}&wwiState=1">&nbsp;已入库&nbsp;</a>&nbsp;
                    <a href="wmsManageAction.do?op=wwiSearch&wmsCode=${wmsCode}&isok=${isok}&wwiState=2">&nbsp;已撤销&nbsp;</a>&nbsp;
                </div>
             	<div class="dataList">	
					<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
						<tr>
							<th style="width:12%">入库单号</th>
							<th style="width:20%">主题</th>
							<th style="width:12%">仓库</th>
							<th style="width:12%">对应采购单</th>
							<th style="width:12%">录入时间</th>
							<th style="width:6%">审核状态</th>
							<th style="width:6%">入库状态</th>
							<th style="width:10%">填单人</th>
							<th style="width:10%;border-right:0px">操作</th>
						</tr>
						<logic:notEmpty name="wmsWarIn">
						<logic:iterate id="wwi" name="wmsWarIn" scope="request">
						<tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('wmsManageAction.do?op=wwiDesc&wwiId=${wwi.wwiId}')">
							<td>${wwi.wwiCode}</td>
							<td><a href="wmsManageAction.do?op=wwiDesc&wwiId=${wwi.wwiId}" target="_blank">${wwi.wwiTitle}</a></td>
							<td>${wwi.wmsStro.wmsName}&nbsp;</td>
							<td class="mLink"><logic:notEmpty name="wwi" property="salPurOrd"><a href="salPurAction.do?op=spoDesc&spoId=${wwi.salPurOrd.spoId}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看采购单详情" style="border:0px;">${wwi.salPurOrd.spoTil}</a></logic:notEmpty>&nbsp;</td>
							<td><span id="stRelDate<%=count %>"></span>&nbsp;</td>
							<td style="text-align:center">
                            <logic:notEmpty name="wwi" property="wwiAppIsok">
                            	<logic:equal value="0" name="wwi" property="wwiAppIsok"><img src="images/content/fail.gif" alt="未通过"></logic:equal>
                                <logic:equal value="2" name="wwi" property="wwiAppIsok"><img src="images/content/time.gif" alt="审核中"></logic:equal>
                                <logic:equal value="1" name="wwi" property="wwiAppIsok"><img src="images/content/suc.gif" alt="已通过"></logic:equal></logic:notEmpty>
                            <logic:empty name="wwi" property="wwiAppIsok">
                            	<span class="gray" title="未审核(请添加产品)">未审核</span>
                            </logic:empty>
                            </td>
							<td style="text-align:center">
                            <logic:equal value="0" name="wwi" property="wwiState"><img src="images/content/database.gif" alt="未入库"></logic:equal>
                            <logic:equal value="1" name="wwi" property="wwiState"><img src="images/content/db_ok.gif" alt="已入库"></logic:equal>
                            <logic:equal value="2" name="wwi" property="wwiState"><img src="images/content/dbError.gif" alt="已撤销"></logic:equal>
							</td>
							<td>${wwi.wwiInpName}&nbsp;</td>
							<td>
                            	<a href="wmsManageAction.do?op=wwiDesc&wwiId=${wwi.wwiId}" target="_blank">
                                <img src="images/content/detail.gif" border="0" alt="查看详情"/></a>&nbsp;&nbsp;
							<logic:equal value="0" name="wwi" property="wwiState">
                                <logic:notEqual value="1" name="wwi" property="wwiAppIsok">
									<img onClick="wmsPopDiv(21,'${wwi.wwiId}','${wmsCode}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑入库单"/>&nbsp;&nbsp;
									<img onClick="wmsDelDiv(2,'${wwi.wwiId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
								</logic:notEqual>
							</logic:equal>
							</td>
						</tr>
					  	<script type="text/javascript">
							dateFormat2('stRelDate','${wwi.wwiInpTime}',<%=count%>);
	   						rowsBg('tr',<%=count%>);
                        </script>
						<% count++; %>
					</logic:iterate>
					</logic:notEmpty>
						<logic:empty name="wmsWarIn">
                    	<tr>
                            <td colspan="9" class="noDataTd">未找到相关数据!</td>
                   		</tr>
                   		</logic:empty>
					</table>
					<logic:notEmpty name="wmsWarIn">
                    	<script type="text/javascript">
							createPage('wmsManageAction.do?op=wwiSearch&wwiTitle='+ encodeURIComponent('${wwiTitle}') +'&wmsCode=${wmsCode}&isok=${isok}&wwiAppIsok=${wwiAppIsok}&wwiState=${wwiState}&wwiState1=${wwiState1}&startTime=${startTime}&endTime=${endTime}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
						</script>
                    </logic:notEmpty>  
				</div>
        	</div>
  		</div> 
	 </div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

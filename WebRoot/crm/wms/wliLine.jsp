<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
int count1=1;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>库存流水</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/wms.css"/>
    <style type="text/css">
    	.topButton{
			padding:3px 6px 3px 6px;
			cursor:pointer;
			border:1px #fff solid;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="js/choosePro.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
		//下拉列表切换类型
		function changeTypes(){
			var selValue = $("types").value;
			if(selValue=="all"){
				self.location.href="wwoAction.do?op=wliSearch";
			}
			else{
				self.location.href="wwoAction.do?op=wliSearch&wmsCode=${wmsCode}&wliType="+selValue;
			}
		}
		
		//载入页面初始化状态下拉列表
		function loadTypes(){
			if("${wliType}"==""){
				$("types").value="";
			}
			else{
				$("types").value="${wliType}";
			}
		}
		
		
		createProgressBar();
		window.onload=function(){
			
			loadWmsName("${wmsName}");
			loadTypes();
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
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;库存管理 > <span id="wmsName">&nbsp;</span> > 库存流水</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
            <tr>
            	<th>
                <div id="tabType">
                    <div id="tabType1" class="tabTypeWhite" onClick="window.location.href='wwoAction.do?op=wliSearch&wmsCode=${wmsCode}'">库存流水</div>
				</div>
				</th>
            </tr>
            </table>
			 <div id="listContent">
				 <div class="listSearch">
                 	<table class="normal" style="width:98%" cellpadding="0" cellspacing="0">
                    	<tr>
                        	<td>
				 				<form id="searchForm" action="wwoAction.do" method="get">
                                     <input type="hidden" name="op" value="wliSearch" />
                                     <input type="hidden" id="wprId" value="${wprId}">
                                     <input type="hidden" name="wmsCode" value="${wmsCode}">
                                     产品名：<input class="inputSize2 inputBoxAlign" style="width:80px" type="text" name="wprName" value="${wprName}" id="wprName"/>&nbsp;<input type="button" class="inputBoxAlign butSize2" onClick="forwardToChoose(1)" value="选择"/>&nbsp;&nbsp;
                                     日期：<input name="startTime" value="${startTime}" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:80px" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                  到&nbsp;<input name="endTime" value="${endTime}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:80px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                                    类型：<select name="wliType" id="types" class="inputBoxAlign">
                                        <option value="">全部</option>
                                        <option value="0">入库</option>
                                        <option value="1">出库</option>
                                        <option value="2">调入</option>
                                        <option value="3">调出</option>
                                        <option value="4">盘点</option>
                                     </select>
                                     &nbsp;&nbsp;<button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                               </form>
                               </td>
                            <td style="text-align:right"><span id="sName"></span></td>
                        </tr>
                    </table>
				 </div>
				 <div class="dataList">	
					<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
						<tr>
                        	<th style="width:6%">类型 </th>
							<th style="width:10%">仓库</th>
							<th style="width:16%">产品名/型号</th>
							<th style="width:10%">入库量</th>
							<th style="width:10%">出库量</th>
                            <th style="width:6%">单位</th>
							<th style="width:10%">库存余量</th>
                            <th style="width:6%">状态</th>
							<th style="width:10%">日期</th>
							<th style="width:8%">经手人</th>
							<th style="width:8%;border-right:0px">对应单据</th>
						</tr>
						<logic:notEmpty name="wmsLine">
						<logic:iterate id="wli" name="wmsLine" scope="request">
						<tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                        	<td>&nbsp;
								<logic:equal value="0" name="wli" property="wliType">入库</logic:equal>
								<logic:equal value="1" name="wli" property="wliType">出库</logic:equal>
								<logic:equal value="2" name="wli" property="wliType">调入</logic:equal>
								<logic:equal value="3" name="wli" property="wliType">调出</logic:equal>
								<logic:equal value="4" name="wli" property="wliType">盘点</logic:equal>
							</td>
							<td>${wli.wmsStro.wmsName}&nbsp;</td>
							<td><a href="wmsManageAction.do?op=wmsProDesc&wprId=${wli.wmsProduct.wprId}" target="_blank" style="cursor:pointer">${wli.wmsProduct.wprName}<logic:notEmpty name="wli" property="wmsProduct.wprModel">/${wli.wmsProduct.wprModel}</logic:notEmpty></a></td>
							<td>
                            	<logic:notEmpty name="wli" property="wliInNum"><bean:write name="wli" property="wliInNum" format="###,##0.00"/></logic:notEmpty>
                                <logic:empty name="wli" property="wliInNum"><span class="gray">&nbsp;—</span></logic:empty>
                            </td>
							<td>
                            	<logic:notEmpty name="wli" property="wliOutNum"><bean:write name="wli" property="wliOutNum" format="###,##0.00"/></logic:notEmpty>
                                <logic:empty name="wli" property="wliOutNum"><span class="gray">&nbsp;—</span></logic:empty>
                            </td>
                            <td>
                            	<logic:notEmpty name="wli" property="wmsProduct.typeList"><span class="brown">${wli.wmsProduct.typeList.typName}</span></logic:notEmpty>&nbsp;
                            </td>
							<td>
                            	<logic:notEmpty name="wli" property="wliNum"><bean:write name="wli" property="wliNum" format="###,##0.00"/></logic:notEmpty>
                                <logic:empty name="wli" property="wliNum">0.0</logic:empty> 
                            </td>
                            <td style="text-align:center">
								<logic:equal value="0" name="wli" property="wliState"><img src="images/content/database.gif" alt="执行中"></logic:equal>
								<logic:equal value="1" name="wli" property="wliState"><img src="images/content/db_ok.gif" alt="已完成"></logic:equal>
								<logic:equal value="2" name="wli" property="wliState"><img src="images/content/dbError.gif" alt="已撤销"></logic:equal>
							</td>
							<td><span id="stRelDate<%=count %>">${wli.wliDate}</span></td>
							
							
							<td>${wli.wliMan}&nbsp;</td>
						    <td>
							<logic:equal value="0" name="wli" property="wliType"><a href="wmsManageAction.do?op=wwiDesc&wwiId=${wli.wliTypeCode}" target="_blank">对应入库单</a></logic:equal>
							<logic:equal value="1" name="wli" property="wliType"><a href="wwoAction.do?op=wwoDesc&wwoId=${wli.wliTypeCode}" target="_blank">对应出库单</a></logic:equal>
							<logic:equal value="2" name="wli" property="wliType"><a href="wwoAction.do?op=wchDesc&wchId=${wli.wliTypeCode}&wmsCode=${wmsCode}" target="_blank">对应调拨单</a></logic:equal>
							<logic:equal value="3" name="wli" property="wliType"><a href="wwoAction.do?op=wchDesc&wchId=${wli.wliTypeCode}&wmsCode=${wmsCode}" target="_blank">对应调拨单</a></logic:equal>
							<logic:equal value="4" name="wli" property="wliType"><a href="wwoAction.do?op=wmcDesc&wmcId=${wli.wliTypeCode}" target="_blank">对应盘点单</a></logic:equal>
							</td>
						</tr>
					  	<script type="text/javascript">
                            dateFormat('stRelDate','${wli.wliDate}',<%=count%>)
                            rowsBg('tr',<%=count%>);
                        </script>
						<% count++; %>
						</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="wmsLine">
                    	<tr>
                            <td colspan="11" class="noDataTd">未找到相关数据!</td>
                   		</tr>
                   		</logic:empty>
					</table>
                    <logic:notEmpty name="wmsLine">
                    	<script type="text/javascript">
                            createPage('wwoAction.do?op=wliSearch&wliType=${wliType}&wmsCode=${wmsCode}&startTime=${startTime}&endTime=${endTime}&wprName='+encodeURIComponent('${wprName}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                        </script>
					</logic:notEmpty>  
				</div>		
        	</div>
  		</div> 
	 </div>
  </body>
</html>

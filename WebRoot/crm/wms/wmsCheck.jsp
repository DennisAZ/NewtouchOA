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
    <title>库存盘点</title>
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
			if("${wmcState1}"==""){
				$("types").value="";
			}
			else{
				$("types").value="${wmcState1}";
			}
				
		}
		
		createProgressBar();
		window.onload=function(){
			
			loadWmsName("${wmsName}");
			//设置判断选中按钮条件
			setCurItemStyle(new Array("${wmcAppIsok}","${wmcAppIsok}","${wmcState}","${wmcState}","${wmcState}"),new Array("2","1","0","1","2"));
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
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;库存管理 > <span id="wmsName">&nbsp;</span> > 库存盘点</div>
				<table class="mainTab" cellpadding="0" cellspacing="0">
                    <tr>
                        <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="window.location.href='wwoAction.do?op=wmcSearch&wmsCode=${wmsCode}&isok=${isok}'">库存盘点</div>
                        </div>
                        </th>
                        <td>
                            <a href="javascript:void(0)" onClick="wmsPopDiv(5,'${wmsCode}');return false;" class="newBlueButton">新建库存盘点</a>
                        </td>
                    </tr>
                </table>
			 <div id="listContent">
             	<div class="listSearch">
                	<table class="normal" style="width:98%" cellpadding="0" cellspacing="0">
                    	<tr>
                        	<td>
                            <form style="margin:0px; padding:0px;" id="searchForm" action="wwoAction.do" method="get" >
                                <input type="hidden" name="op" value="wmcSearch" />
                                <input type="hidden" name="wmsCode" value="${wmsCode}"/>
                                <input type="hidden" name="isok" value="${isok}"/>
                                主题/单据号：<input style="width:100px;" class="inputSize2 inputBoxAlign" type="text" name="wmcTitle" value="${wmcTitle}"/>&nbsp;&nbsp;
								日期：<input name="startTime" value="${startTime}" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                  到&nbsp;<input name="endTime" value="${endTime}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
				  				  盘点状态：<select name="wmcState1" id="types" class="inputBoxAlign">
                                        <option value="">全部</option>
										<option value="0">盘点中</option>
                                        <option value="1">盘点结束</option>
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
                    <a href="wwoAction.do?op=wmcSearch&wmsCode=${wmsCode}&wmcAppIsok=2&isok=${isok}"><span class="bold">${appCount}</span>条单据等待审核</a>&nbsp;
                    <a href="wwoAction.do?op=wmcSearch&wmsCode=${wmsCode}&wmcAppIsok=1&isok=${isok}"><span class="bold">${wmcWmsCount}</span>条已审单据待盘点</a>&nbsp;
                    <a href="wwoAction.do?op=wmcSearch&wmsCode=${wmsCode}&isok=${isok}&wmcState=0">&nbsp;盘点中&nbsp;</a>&nbsp;
                    <a href="wwoAction.do?op=wmcSearch&wmsCode=${wmsCode}&isok=${isok}&wmcState=1">&nbsp;盘点结束&nbsp;</a>&nbsp;
                    <a href="wwoAction.do?op=wmcSearch&wmsCode=${wmsCode}&isok=${isok}&wmcState=2">&nbsp;已撤销&nbsp;</a>&nbsp;
                </div>
				<div class="dataList">	
					<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
						<tr>
							<th style="width:12%">盘点单号</th>
							<th style="width:24%">主题</th>
							<th style="width:20%">仓库</th>
							<th style="width:10%">录入时间</th>
							<th style="width:8%">审核状态</th>
							<th style="width:8%">盘点状态</th>
							<th style="width:8%">填单人</th>
							<th style="width:10%;border-right:0px">操作</th>
						</tr>
						<logic:notEmpty name="wmsCheck">
						<logic:iterate id="wmc" name="wmsCheck" scope="request">
						<tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('wwoAction.do?op=wmcDesc&wmcId=${wmc.wmcId}')">
							<td>&nbsp;${wmc.wmcCode}</td>
							<td><a href="wwoAction.do?op=wmcDesc&wmcId=${wmc.wmcId}" target="_blank">${wmc.wmcTitle}&nbsp;</a></td>
							<td>${wmc.wmsStro.wmsName}&nbsp;</td>
							<td><span id="stRelDate<%=count %>"></span>&nbsp;</td>
							<td style="text-align:center">
                            	<logic:notEmpty name="wmc" property="wmcAppIsok">
                                    <logic:equal value="0" name="wmc" property="wmcAppIsok"><img src="images/content/fail.gif" alt="未通过"></logic:equal>
                                    <logic:equal value="2" name="wmc" property="wmcAppIsok"><img src="images/content/time.gif" alt="审核中"></logic:equal>
                                    <logic:equal value="1" name="wmc" property="wmcAppIsok"><img src="images/content/suc.gif" alt="已通过"></logic:equal></logic:notEmpty>
                                <logic:empty name="wmc" property="wmcAppIsok">
                                    <span class="gray" title="未审核(请添加产品)">未审核</span>
                                </logic:empty>
							</td>
							<td style="text-align:center">
							<logic:equal value="0" name="wmc" property="wmcState"><img src="images/content/database.gif" alt="盘点中"></logic:equal>
							<logic:equal value="1" name="wmc" property="wmcState"><img src="images/content/db_ok.gif" alt="盘点结束"></logic:equal>
							<logic:equal value="2" name="wmc" property="wmcState"><img src="images/content/dbError.gif" alt="已撤销"></logic:equal>
							</td>
							<td>${wmc.wmcInpName}&nbsp;</td>
						  	<td><nobr>
                            <a href="wwoAction.do?op=wmcDesc&wmcId=${wmc.wmcId}" target="_blank">
                            <img src="images/content/detail.gif" border="0" alt="查看明细"/></a>&nbsp;&nbsp;
                            <logic:equal value="0" name="wmc" property="wmcState">
                                <logic:notEqual value="1" name="wmc" property="wmcAppIsok">
									<img onClick="wmsPopDiv(51,'${wmc.wmcId}','${wmsCode}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑盘点单"/>&nbsp;&nbsp;
									<img onClick="wmsDelDiv(13,'${wmc.wmcId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
								</logic:notEqual>
							</logic:equal>
                    		</nobr>&nbsp;</td>
						</tr>
						    <script type="text/javascript">
							  	dateFormat2('stRelDate','${wmc.wmcInpDate}',<%=count%>)
	   							rowsBg('tr',<%=count%>);
	 	              		</script>
						 <% count++; %>
					</logic:iterate>
					</logic:notEmpty>
                    <logic:empty name="wmsCheck">
                    	<tr>
                            <td colspan="8" class="noDataTd">未找到相关数据!</td>
                   		</tr>
                   	</logic:empty>
					</table>
					<logic:notEmpty name="wmsCheck">
                    	<script type="text/javascript">
                            createPage('wwoAction.do?op=wmcSearch&wmcTitle='+ encodeURIComponent('${wmcTitle}') +'&wmsCode=${wmsCode}&wmcAppIsok=${wmcAppIsok}&isok=${isok}&wmcState=${wmcState}&startTime=${startTime}&endTime=${endTime}&wmcState1=${wmcState1}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                        </script>
					</logic:notEmpty>  
				</div>		
        	</div>
  		</div> 
	</div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

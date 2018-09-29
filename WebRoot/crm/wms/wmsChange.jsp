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
    
    <title>仓库调拨</title>
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
			if("${wchState1}"==""){
				$("types").value="";
			}
			else{
				$("types").value="${wchState1}";
			}
		}
		
		createProgressBar();
		window.onload=function(){
			
			loadWmsName("${wmsName}");
			//设置判断选中按钮条件
			setCurItemStyle(new Array("${wchAppIsok}","${wchAppIsok}${wchState}","${wchAppIsok}${wchState}","${wchState}","${wchState}","${wchState}","${wchState}"),new Array("2","10","12","0","2","3","4"));
			//增加清空按钮
			createCancelButton('searchForm',-50,5);
			//表格内容省略
			loadTabShort("tab");
			closeProgressBar();
			loadTypes();
		}
	</script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;库存管理 > <span id="wmsName">&nbsp;</span> > 仓库调拨</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
            <tr>
            	<th>
                <div id="tabType">
                    <div id="tabType1" class="tabTypeWhite" onClick="window.location.href='wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&isok=${isok}'">仓库调拨</div>
           		</div>
                </th>
                <td>
                	<a href="javascript:void(0)" onClick="wmsPopDiv(4,'${wmsCode}');return false;" class="newBlueButton">新建调拨单</a>
                </td>
            </tr>
            </table>
			 <div id="listContent">
             	<div class="listSearch">
                	<table class="normal" style="width:98%" cellpadding="0" cellspacing="0">
                    	<tr>
                        	<td>
                            <form id="searchForm" action="wwoAction.do" method="get" >
                                <input type="hidden" name="op" value="wchSearch" />
                                <input type="hidden" name="wmsCode" value="${wmsCode}"/>
                                <input type="hidden" name="isok" value="${isok}"/>
                                主题/单据号：<input class="inputSize2 inputBoxAlign" style="width:100px;"  type="text"name="wchTitle" value="${wchTitle}"/>&nbsp;&nbsp;
								日期：<input name="startTime" value="${startTime}" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                  到&nbsp;<input name="endTime" value="${endTime}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
				  				  调拨状态：<select name="wchState1" id="types" class="inputBoxAlign">
                                        <option value="">全部</option>
										<option value="0">未执行</option>
                                        <option value="2">已出库</option>
                                        <option value="3">已入库</option>
                                        <option value="4">已撤销</option>
                                     </select>
                                 &nbsp;&nbsp;<button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                            </form>
                            </td>
                            <td style="text-align:right"><span id="sName"></span></td>
                        </tr>
                    </table>
                </div>
                <div id="topChoose" class="listTopBox">
                    <a href="wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&isok=${isok}&wchAppIsok=2"><span class="bold">${appCount}</span>条单据等待审核</a>&nbsp;
                    <a href="wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&wchAppIsok=1&isok=${isok}&wchState=0"><span class="bold">${wchWmsCount}</span>条已审单据待调出</a>&nbsp;
                    <a href="wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&wchAppIsok=1&isok=${isok}&wchState=2"><span class="bold">${wchWmsCount2}</span>条已审单据待调入</a>&nbsp;
                    <a href="wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&isok=${isok}&wchState=0">&nbsp;未执行&nbsp;</a>&nbsp;
                    <a href="wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&isok=${isok}&wchState=2">&nbsp;已出库未入库&nbsp;</a>&nbsp;
                    <a href="wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&isok=${isok}&wchState=3">&nbsp;已入库&nbsp;</a>&nbsp;
                    <a href="wwoAction.do?op=wchSearch&wmsCode=${wmsCode}&isok=${isok}&wchState=4">&nbsp;已撤销&nbsp;</a>&nbsp;
                </div>
				 <div class="dataList">	
					<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
						<tr>
							<th style="width:12%">调拨单号</th>
							<th style="width:16%">主题</th>
							<th style="width:14%">调出仓库</th>
							<th style="width:14%">调入仓库</th>
							<th style="width:10%">录入时间</th>
							<th style="width:6%">审核状态</th>
							<th style="width:6%">调拨状态</th>
							<th style="width:6%">调出人</th>
							<th style="width:6%">调入人</th>
							<th style="width:10%;border-right:0px">操作</th>
						</tr>
						<logic:notEmpty name="wmsChange">
						<logic:iterate id="wch" name="wmsChange" scope="request">
						<tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('wwoAction.do?op=wchDesc&wchId=${wch.wchId}&wmsCode=${wmsCode}')">
							<td>&nbsp;${wch.wchCode}</td>
							<td><a href="wwoAction.do?op=wchDesc&wchId=${wch.wchId}&wmsCode=${wmsCode}" target="_blank">${wch.wchTitle}&nbsp;</a></td>
							<td><span id="outw<%=count%>">${wch.wmsStroByWchOutWms.wmsName}&nbsp;</span><img src='images/content/rightArr.gif' style='border:0px;' alt='调出'></td>
							<td><span id="title2<%= count%>"></span><span id="inw<%=count%>">${wch.wmsStroByWchInWms.wmsName}&nbsp;</span></td>
							<td><span id="stRelDate<%=count %>"></span>&nbsp;</td>
							<td style="text-align:center">
                            <logic:notEmpty name="wch" property="wchAppIsok">
                            	<logic:equal value="0" name="wch" property="wchAppIsok"><img src="images/content/fail.gif" alt="未通过"></logic:equal>
                                <logic:equal value="2" name="wch" property="wchAppIsok"><img src="images/content/time.gif" alt="审核中"></logic:equal>
                                <logic:equal value="1" name="wch" property="wchAppIsok"><img src="images/content/suc.gif" alt="已通过"></logic:equal>
                            </logic:notEmpty>
                            <logic:empty name="wch" property="wchAppIsok">
                            	<span class="gray" title="未审核(请添加产品)">未审核</span>
                            </logic:empty>
                            </td>
							<td style="text-align:center">
								<logic:equal value="0" name="wch" property="wchState"><img src="images/content/database.gif" alt="未执行"/></logic:equal>
                                <logic:equal value="2" name="wch" property="wchState"><img src="images/content/database_go.gif" alt="已出库"/></logic:equal>
                                <logic:equal value="3" name="wch" property="wchState"><img src="images/content/db_ok.gif" alt="已入库"/></logic:equal>
								<logic:equal value="4" name="wch" property="wchState"><img src="images/content/dbError.gif" alt="已撤销"/></logic:equal>
							</td>
							<td>${wch.checkOutName}&nbsp;</td>
							<td>${wch.checkInName}&nbsp;</td>
							<td><nobr><a href="wwoAction.do?op=wchDesc&wchId=${wch.wchId}&wmsCode=${wmsCode}" target="_blank">
                                <img src="images/content/detail.gif" border="0" alt="查看明细"/></a>&nbsp;&nbsp;
                                <logic:equal value="0" name="wch" property="wchState">
                                    <logic:notEqual value="1" name="wch" property="wchAppIsok">
                                        <img onClick="wmsPopDiv(41,'${wch.wchId}','${wmsCode}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑调拨单"/>&nbsp;&nbsp;
                                        <img onClick="wmsDelDiv(9,'${wch.wchId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
                                    </logic:notEqual>
                                </logic:equal>
							</nobr>&nbsp;</td>
						</tr>
						  <script type="text/javascript">
							 var wmsCode='${wmsCode}';
							 if(wmsCode!=null&&wmsCode!=""){
							 	 if(wmsCode=='${wch.wmsStroByWchOutWms.wmsCode}'){
							 	 	 $("outw"+<%=count%>).className='brown';
								 }
								 if(wmsCode=='${wch.wmsStroByWchInWms.wmsCode}'){
							 		 $("inw"+<%=count%>).className='brown';
								 }
							 }
							rowsBg('tr',<%=count%>);
							dateFormat2('stRelDate','${wch.wchInpDate}',<%=count%>)
                        </script>
					 <% count++; %>
					</logic:iterate>
					</logic:notEmpty>
					<logic:empty name="wmsChange">
                    	<tr>
                            <td colspan="10" class="noDataTd">未找到相关数据!</td>
                   		</tr>
                  </logic:empty>
			</table>
					<logic:notEmpty name="wmsChange">
                    	<script type="text/javascript">
                            createPage('wwoAction.do?op=wchSearch&wchTitle='+ encodeURIComponent('${wchTitle}') +'&wmsCode=${wmsCode}&wchAppIsok=${wchAppIsok}&isok=${isok}&wchState=${wchState}&wchState1=${wchState1}&startTime=${startTime}&endTime=${endTime}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                        </script>
					</logic:notEmpty>
				</div>           
        	</div>
  		</div> 
	 </div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

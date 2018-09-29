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
    
    <title>库存列表</title>
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
	<script type="text/javascript" src="js/choosePro.js"></script>
    <script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript">
		createProgressBar();
		window.onload=function(){
			loadWmsName("${wmsName}");
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
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;库存管理 > <span id="wmsName">&nbsp;</span> > 库存列表
            </div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th id="tabTd">
                    <div id="tabType">
                    	<logic:equal name="isAll" value="0">
                            <div id="tabType1" class="tabTypeWhite" onClick="window.location.href='wmsManageAction.do?op=rwpSearch&wmsCode=${wmsCode}'">库存列表</div>
                        </logic:equal>
                        <logic:equal name="isAll" value="1">
                        	<div id="tabType1" class="tabTypeWhite" onClick="window.location.href='wmsManageAction.do?op=rStroProSearch'">库存列表</div>
                        </logic:equal>
                    </div>
                    </th>
                    <script type="text/javascript">
						writeLimAllow("w002","<td style=\"width:110px\"><a href=\"javascript:void(0)\" onClick=\"wmsDelDiv(5,'${wmsCode}');return false;\" class=\"newBlueButton\">数据整理</a></td>","tabTd","after");
					</script>
                </tr>
            </table>
            <div id="listContent">
            	<div class="listSearch">
                	<table class="normal" style="width:98%" cellpadding="0" cellspacing="0">
                    	<tr>
                        	<td>
                            	<form id="searchForm" action="wmsManageAction.do" method="get">
                                     <input type="hidden" name="op" value="rwpSearch" />
                                     <input type="hidden" name="wprId" id="wprId" value="${wprId}">
                                     <input type="hidden" name="wmsCode" value="${wmsCode}">
                                     产品名：<input class="inputBoxAlign inputSize2" style="width:120px" type="text" name="wprName" value="${wprName}" id="wprName"/>&nbsp;<input type="button" class="inputBoxAlign butSize2" onClick="forwardToChoose(1)" value="选择"/>&nbsp;&nbsp;
                                     <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;					
                               </form>
                            </td>
                            <td style="text-align:right"><span id="sName"></span></td>
                        </tr>
                    </table>
                </div>
                <div class="dataList">	
                    <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0" style="table-layout:fixed">
                        <tr>
                            <th style="width:40%">产品名称/型号</th>
                            <th style="width:20%">产品编号</th>
                            <th style="width:20%">库存数量[单位]</th>
                            <th style="width:20%;border-right:0px">所在仓库</th>
                        </tr>
                        <logic:notEmpty name="rStroPro">
                        <logic:iterate id="rsp" name="rStroPro">
                        <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('wmsManageAction.do?op=wmsProDesc&wprId=${rsp.wmsProduct.wprId}')">
                            <td>
                            &nbsp;<a title="${rsp.wmsProduct.wprName}" href="wmsManageAction.do?op=wmsProDesc&wprId=${rsp.wmsProduct.wprId}" target="_blank" style="cursor:pointer">${rsp.wmsProduct.wprName}<logic:notEmpty name="rsp" property="wmsProduct.wprModel">/${rsp.wmsProduct.wprModel}</logic:notEmpty></a>
                            </td>
                            <td>${rsp.wmsProduct.wprCode}&nbsp;</td>
                            <td><span id="num<%=count%>"></span>&nbsp;<logic:notEmpty name="rsp" property="wmsProduct.typeList"><span class="brown">[${rsp.wmsProduct.typeList.typName}]</span></logic:notEmpty></td>
                            <td>${rsp.wmsStro.wmsName}</td>
                        </tr>
                              <script type="text/javascript">
                              var num="<bean:write name='rsp' property='rspProNum' format='###,##0.00'/>";
                              if(parseFloat(num)<=0.0){
                                $("num"+<%=count%>).innerHTML="<span class='red'>"+num+"</font >";
                              }else{
                                $("num"+<%=count%>).innerHTML=num;
                              }
                                rowsBg('tr',<%=count%>);
                              </script>
                         <% count++; %>
                    </logic:iterate>
                    </logic:notEmpty>
                     <logic:empty name="rStroPro">
                        <tr>
                            <td colspan="4" class="noDataTd">未找到相关数据!</td>
                        </tr>
                	</logic:empty> 
            </table>
            		<logic:notEmpty name="rStroPro">
					<script type="text/javascript">
                        var url ;
                        switch('${isAll}'){
                            case '0':
                                url = 'wmsManageAction.do?op=rwpSearch&wprName='+ encodeURIComponent('${wprName}') +'&wmsCode=${wmsCode}';
                                break;
                            case '1':
                                url = 'wmsManageAction.do?op=rStroProSearch';
                                break;
                                
                        }
                        createPage(url,'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                    </script>
                    </logic:notEmpty>
                </div>		
            </div>
        </div> 
	 </div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

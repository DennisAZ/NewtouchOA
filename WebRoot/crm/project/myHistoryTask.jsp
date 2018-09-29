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
    <title>我的项目日志(未使用)</title>
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
	
   		
		createProgressBar();
		window.onload=function(){
			
			//表格内容省略
			loadTabShort("tab");
			createCancelButton('searchForm',-50,5);
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
      <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;项目管理 > 我的项目日志
            </div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite"  onClick="selectHisTask(1)">我的项目日志</div>
                            <script type="text/javascript">writeLimAllow("p023","<div id='tabType2' class='tabTypeBlue1' onMouseOver='changeTypeBg(this,1,1)' onMouseOut='changeTypeBg(this,0,1)' onClick='selectHisTask(2)'>所有项目日志</div>","tabType");</script>
                          
                        </div>
                    </th>
                    <td>
                        <a href="javascript:void(0)" onClick="addDivNew(4);return false;" class="newBlueButton">新建项目日志</a>
                    </td>
                </tr>
            </table>
            <div id="listContent">     
             <div class="listSearch">
                    <form id="searchForm" action="projectAction.do" method="get" >
                        <input type="hidden" name="op" value="findMyHisTask" />
                        日志主题：<input class="inputSize2 inputBoxAlign" type="text" name="prtaTitle" value="${prtaTitle}" style="width:100px"/>&nbsp;&nbsp;
                        对应项目：<input id="proTitle" name="proTitle" class="inputSize2 inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" style="width:142px" type="text" value="${proTitle}" readonly ondblClick="clearInput(this,'proId')" />&nbsp;
                          <button class="butSize2 inputBoxAlign" onClick="addDivBrow(17)">选择</button>
		                  <input type="text" name="proId" value="${proId}" id="proId" style="display:none"/>&nbsp;   
                        提交时间：<input name="startTime" value="${startTime}" id="pid" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" ondblClick="clearInput(this)" onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>
                          到&nbsp;<input name="endTime" value="${endTime}" id="pid1" ondblClick="clearInput(this)" type="text" class="inputSize2 inputBoxAlign Wdate" style="cursor:hand; width:85px" readonly="readonly" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                          <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   </form>
                </div>	           
                <div class="dataList">
                <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                    <tr>
                        <th style="width:30%">主题</th>
                        <th style="width:20%">对应项目</th>
                        <th style="width:18%">对应子项目</th>
                        <th style="width:13%">提交时间</th>
                        <th style="width:9%">提交人</th>
                        <th style=" width:10%;border-right:0px">操作</th>
                  </tr>
                     <logic:notEmpty name="myHisTaskList">
                        <logic:iterate id="hisProTask" name="myHisTaskList">
                         <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="addDiv(20,'${hisProTask.prtaId}')">
                            <td>
                             <a href="javascript:void(0)" onClick="addDiv(20,'${hisProTask.prtaId}');return false;" style="cursor:pointer" target="_blank"><span class="textOverflow4" title="${hisProTask.prtaTitle}">${hisProTask.prtaTitle}</span></a>                             </td>
                             <td><span class="textOverflow4" title="${hisProTask.project.proTitle}">${hisProTask.project.proTitle}&nbsp;</span></td>
                            <td>${hisProTask.proStage.staTitle}&nbsp;</td>
                            <td><span id="prtaRelDate<%=count%>"></span>&nbsp;</td>
                            
                         <td>${hisProTask.prtaName}&nbsp;</td>
                          <td>
                            <img onClick="addDiv(20,'${hisProTask.prtaId}')" style="cursor:pointer" src="images/content/detail.gif" border="0" alt="查看详细"/>&nbsp;&nbsp;
                            <img onClick="addDiv(18,'${hisProTask.prtaId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                            <img onClick="addDiv(19,'${hisProTask.prtaId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>							                         </td>
                         </tr>
                         <script type="text/javascript">
                        dateFormat2('prtaRelDate','${hisProTask.prtaRelDate}',<%=count%>);
                        rowsBg('tr',<%=count%>);
                        </script>
                         <%count++; %>
                        </logic:iterate>
                        </logic:notEmpty>
                     <logic:empty name="myHisTaskList">
                         <tr>
                             <td colspan="6" class="noDataTd">未找到相关数据!</td>
                         </tr>
                     </logic:empty>
                 </table>
                 <logic:notEmpty name="myHisTaskList">
                    <logic:equal name="pageType" value="default">
                        <script type="text/javascript">
                            createPage('projectAction.do?op=findMyHisTask&proId=${proId}&proTitle='+encodeURIComponent('${proTitle}') +'&startTime=${startTime}&endTime=${endTime}&prtaTitle='+encodeURIComponent('${prtaTitle}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
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

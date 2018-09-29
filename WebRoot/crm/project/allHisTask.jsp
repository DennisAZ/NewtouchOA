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
    <title>全部项目日志(项目详情)</title>
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
		//初始化下拉框
		//function loadStates(){
			//if("${ptlIsfin}"!=null&&"${ptlIsfin}"!=""){
				//$("states").value="${ptlIsfin}";
			//}
		//}
		
   		
		createIfmLoad('ifmLoad');//进度条
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			//loadStates();
			loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
		<div class="divWithScroll2 innerIfm">
            <table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>全部项目日志</th>
                    <td><a href="javascript:void(0)" onClick="parent.projPopDiv(2,'${proId}');return false;" class="newBlueButton">新建项目日志</a></td>
                </tr>
            </table>
            <table id="tab" class="normal rowstable noBr" style="width:100%;" cellpadding="0" cellspacing="0">
                <tr>
                    <th style="width:40%">主题</th>
                    <th style="width:30%">对应子项目</th>
                    <th style="width:15%">提交时间</th>
                    <th style=" width:15%;border-right:0px">提交人</th>
                </tr>
                <logic:notEmpty name="myHisTaskList">
                    <logic:iterate id="hisProTask" name="myHisTaskList">
                    <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)"   onDblClick="parent.projPopDiv(21,'${hisProTask.prtaId}')">
                        <td>
                         <a href="javascript:void(0)" onClick="parent.projPopDiv(21,'${hisProTask.prtaId}');return false;" style="cursor:pointer" target="_blank">${hisProTask.prtaTitle}</a>
                         </td>
                        <td>${hisProTask.prtaStaName}&nbsp;</td>
                        <td><span id="prtaRelDate<%=count%>"></span>&nbsp;</td>
                        <td>${hisProTask.prtaName}&nbsp;</td>
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
                         <td colspan="4" class="noDataTd">未找到相关数据!</td>
                     </tr>
                 </logic:empty>
             </table>
            <logic:notEmpty name="myHisTaskList">
                <logic:equal name="pageType" value="allProDefault">
                    <script type="text/javascript">
                        createPage('projectAction.do?op=getAllProHisTask&proId=${proId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                    </script>
                </logic:equal>
            </logic:notEmpty>
		</div>			
   
  </body>

</html>

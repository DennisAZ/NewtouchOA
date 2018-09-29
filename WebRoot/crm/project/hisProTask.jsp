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
    <title>我的项目日志(项目详情)(无效)</title>
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
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
        <div class="divWithScroll2" style="width:100%; height:100%; padding:0px; margin:0px;">
        	<table class="normal nopd" style="width:98%; margin-bottom:5px; height:25px;">
                <tr>
                    <td>
                        <span class="orgBlock"></span>&nbsp;
                        <span class="blue middle bold">我的项目日志</span>
                    </td>
                    <td style="text-align:right; width:110px;"><a href="javascript:void(0)" onClick="parent.projPopDiv(2,'${proId}');return false;" class="newBlueButton">新建项目日志</a></td>
                </tr>
            </table>
        
            <table id="tab" class="normal rowstable noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tr>
                    <th style="width:35%">主题</th>
                    <th style="width:30%">对应子项目</th>
                    <th style="width:14%">提交时间</th>
                    <th style="width:10%">提交人</th>
                    <th style=" width:11%;border-right:0px">操作</th>
              	</tr>
                <logic:notEmpty name="myHisTaskList">
               	<logic:iterate id="hisProTask" name="myHisTaskList">
                <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="addDiv(20,'${hisProTask.prtaId}')">
                	<td>
                 		<a href="javascript:void(0)" onClick="addDiv(20,'${hisProTask.prtaId}');return false;" style="cursor:pointer" target="_blank">${hisProTask.prtaTitle}</a>                             
                    </td>
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
                     <td colspan="5" class="noDataTd">未找到相关数据!</td>
                 </tr>
                </logic:empty>
          </table>
          <logic:notEmpty name="myHisTaskList">
		 		<logic:equal name="pageType" value="default">
         			<script type="text/javascript">
						createPage('projectAction.do?op=getMyHisTask&proId=${proId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
				</logic:equal>
            </logic:notEmpty>
  		</div>			
   
  </body>

</html>

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
    <title>发布的项目工作(项目详情)(无效)</title>
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
		//下拉框切换状态
		function changeStates(){
			if($("states").value!="a"){
				self.location.href="projectAction.do?op=getSenProTaskByStage&proId=${proId}&staTitle="+ encodeURIComponent("${staTitle}") +"&prtaState="+encodeURIComponent($("states").value);
			}
			else{
				self.location.href="projectAction.do?op=getSenProTaskByStage&proId=${proId}&staTitle="+ encodeURIComponent("${staTitle}");
			}
		}
		
		//初始化下拉框
		function loadStates(){
			if("${prtaState}"!=null&&"${prtaState}"!=""){
				$("states").value="${prtaState}";
			}
		}
		
   		
		createProgressBar();
		window.onload=function(){
			loadStates();
			closeProgressBar();
		}
	</script>
  </head>
  
  <body> 
     <div class="divWithScroll2" style="width:100%; height:100%; padding:0px; margin:0px;">
     	<table class="normal nopd" style="width:98%; margin-bottom:5px; height:25px;">
            <tr>
                <td style="width:130px;">
                    <span class="orgBlock"></span>&nbsp;
                    <span class="blue middle bold">已发项目工作</span>
                </td>
                <td style=" text-align:left">
                    <form style="margin:0px; text-align:left;" id="searchForm" action="projectAction.do" method="get" >
                        <input type="hidden" name="op" value="getSenProTaskByStage" />
                        <input type="hidden" name="proId" value="${proId}"/>
                        根据子项目查询：<input class="inputSize2 inputBoxAlign"  type="text" name="staTitle" value="${staTitle}"/>				
                       	&nbsp;<input type="button" class="smallBut inputBoxAlign" onClick="formSubmit('searchForm')" value="搜索" />
                    </form>  
                </td>
                <td style="text-align:right; width:110px;"><a href="javascript:void(0)" onClick="addDiv(8,'${proId}');return false;" class="newBlueButton">新建项目工作</a></td>
            </tr>
        </table>
        
        <table class="normal rowstable noBr" style="width:98%" cellpadding="0" cellspacing="0">
            <tr>
                <th width="30px">附件</th>	
                <th style="width:68px">
                    <select id="states" onChange="changeStates()">
                        <option value="a" selected>全部</option>
                        <option value="0">执行中</option>
                        <option value="1">已结束</option>
                        <option value="2">已取消</option>
                    </select>                            
                </th>
                <th style="width:150px">[优先级]工作名称</th>
                <th style="width:130px">对应子项目</th>
                <th>发布日期</th>
                <th>完成期限</th>
                <th>执行人</th>
                <th style="border-right:0px">操作</th>
            </tr>
            <logic:notEmpty name="mySenTaskSearchList">
                <logic:iterate id="proTask" name="mySenTaskSearchList">
                <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('projectAction.do?op=showProTaskDesc&prtaId=${proTask.prtaId}')">
                    <td style="text-align:center">
                    	<logic:notEmpty name="proTask" property="attachments">
                            <img id="${proTask.prtaId}y" style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTask.prtaId}','ptask','doc')"/>
                        </logic:notEmpty>
                        <logic:empty name="proTask" property="attachments">
                            <img id="${proTask.prtaId}n" style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTask.prtaId}','ptask','doc')"/>
                        </logic:empty>
                    </td>
                    <td style="text-align:center">
                        <logic:equal value="0" name="proTask" property="prtaState"> 
                            <img src="images/content/time.gif" alt="执行中"/>                                  	
                        </logic:equal>
                        <logic:equal value="1" name="proTask" property="prtaState">
                            <img src="images/content/suc.gif" alt="已结束"/>                                  	
                        </logic:equal>
                        <logic:equal value="2" name="proTask" property="prtaState">
                            <img src="images/content/fail.gif" alt="已取消"/>                                  	
                        </logic:equal>
                    </td>
                    <td>
                    <a href="projectAction.do?op=showProTaskDesc&prtaId=${proTask.prtaId}" style="cursor:pointer" target="_blank"><span class="textOverflow3a" title="${proTask.prtaTitle}">[<logic:equal value="0" name="proTask" property="prtaLev">低</logic:equal><logic:equal value="1" name="proTask" property="prtaLev">中</logic:equal><logic:equal value="2" name="proTask" property="prtaLev">高</logic:equal>]${proTask.prtaTitle}</span></a>                                
                    </td>
                    <td><span class="textOverflow3" title="${proTask.proStage.staTitle}">${proTask.proStage.staTitle}&nbsp;</span></td>
                    <td><span id="prtaRelDate<%=count%>"></span>&nbsp;</td>
                    <td>
                        <logic:notEmpty  name="proTask" property="prtaFinDate">
                            <span id="prtaFinDate<%=count%>"></span>                                    
                        </logic:notEmpty>
                        <logic:empty  name="proTask" property="prtaFinDate">
                            <span class="gray">未指定</span>                                    
                        </logic:empty>                                
                    </td>
                    <td style="white-space:normal">
                    &nbsp;
                    <logic:notEmpty name="proTask" property="proTaskLims">
                    <logic:iterate id="proTaskLim" name="proTask" property="proTaskLims">
                    <logic:equal value="1" name="proTaskLim" property="ptlIsdel">
                        <logic:equal value="0" name="proTaskLim" property="ptlIsfin">
                        <img style=" vertical-align:middle; margin-bottom:4px;" src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="proTaskLim" property="ptlIsfin">
                        <img style="vertical-align:middle; margin-bottom:4px;" src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="2" name="proTaskLim" property="ptlIsfin">
                        <img style="vertical-align:middle; margin-bottom:4px;" src="images/content/alert.gif" alt="被退回"/></logic:equal><logic:equal value="3" name="proTaskLim" property="ptlIsfin">
                        <img style="vertical-align:middle; margin-bottom:4px;" src="images/content/finish.gif" alt="已完成"/></logic:equal>${proTaskLim.ptlName}</logic:equal>
                    </logic:iterate>
                    </logic:notEmpty>								
                    </td>
                    <td>
                	<a href="projectAction.do?op=showProTaskDesc&prtaId=${proTask.prtaId}" target="_blank">
                	<img style="cursor:pointer" src="images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                    <logic:notEqual name="proTask" property="prtaState" value="1">
                    <img onClick="addDiv(5,'${proTask.prtaId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;</logic:notEqual>
                    <img onClick="addDiv(6,'${proTask.prtaId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>				
                    </td>
                </tr>
                <script type="text/javascript">
                    dateFormat2('prtaRelDate','${proTask.prtaRelDate}',<%=count%>);
                    dateFormat2('prtaFinDate','${proTask.prtaFinDate}',<%=count%>);
                    
                    
                    rowsBg('tr',<%=count%>);
                </script>
                <%count++; %>
                </logic:iterate>
            </logic:notEmpty>
            <logic:empty name="mySenTaskSearchList">
                <tr>
                    <td colspan="8" class="noDataTd">未找到相关数据!</td>
                </tr>
            </logic:empty>
         </table>
         <logic:notEmpty name="mySenTaskSearchList">
         	<logic:equal name="pageType" value="default">
         		<script type="text/javascript">
					createPage('projectAction.do?op=getMySenProTask&proId=${proId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
				</script>
            </logic:equal>
         	<logic:equal name="pageType" value="mySenTaskSearch">
         		<script type="text/javascript">
					createPage('projectAction.do?op=getSenProTaskByStage&proId=${proId}&staTitle='+ encodeURIComponent('${staTitle}') +'&prtaState='+ encodeURIComponent('${prtaState}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
				</script>
        	</logic:equal>
        </logic:notEmpty>
	</div>		
  </body>

</html>

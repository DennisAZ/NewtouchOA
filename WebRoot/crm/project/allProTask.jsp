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
    <title>所有的项目工作(项目详情)(未使用)</title>
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
       //子项目下拉框切换状态
        function changeSubPro(){
			if($F("pStage")!=""){
				self.location.href="projectAction.do?op=getAllProTask&proId=${proId}&staTitle="+encodeURIComponent($("pStage").value);
			}
			else{
				self.location.href="projectAction.do?op=getAllProTask&proId=${proId}";
			}
		}
		//下拉框切换状态
		function changeStates(){
			if($("states").value!="a"){
				self.location.href="projectAction.do?op=getAllProTask&proId=${proId}&staTitle="+ encodeURIComponent("${staTitle}") +"&prtaState="+encodeURIComponent($("states").value);
			}
			else{
				self.location.href="projectAction.do?op=getAllProTask&proId=${proId}&staTitle="+ encodeURIComponent("${staTitle}");
			}
		}
		
		//初始化下拉框
		function loadStates(){
			if("${prtaState}"!=null&&"${prtaState}"!=""){
				$("states").value="${prtaState}";
			}
			if("${staTitle}"!=null&&"${staTitle}"!=""){
				$("pStage").value="${staTitle}";
			}
		}
		
		createIfmLoad('ifmLoad');//进度条
		window.onload=function(){
			loadStates();
			//表格内容省略
			loadTabShort("tab");
			loadEnd('ifmLoad');
		}
	</script>
  </head>
  
  <body>
  	<div class="divWithScroll2 innerIfm">
    	<table class="normal ifmTopTab" cellpadding="0" cellspacing="0">
            <tr>
                <th>
                   全部项目工作 <logic:notEmpty name="project" property="proStages">
                                子项目：<select id="pStage" onChange="changeSubPro()" class="inputBoxAlign" style="width:90px">
                                  <option value="">全部</option>
                                  <logic:iterate id="proStage" name="project" property="proStages">
                                  <option value="${proStage.staTitle}">${proStage.staTitle}</option>
                                  </logic:iterate>
                                  </select>
                           </logic:notEmpty>
                </th>
                <td><a href="javascript:void(0)" onClick="parent.addDiv(8,'${proId}');return false;" class="newBlueButton">新建项目工作</a></td>
            </tr>
        </table>
        <!--<div class="listSearch">
            <form id="searchForm" action="projectAction.do" method="get" >
                <input type="hidden" name="op" value="getAllProTask" />
                <input type="hidden" name="proId" value="${proId}"/>
                子项目名称：<input class="inputSize2 inputBoxAlign"  type="text" name="staTitle" value="${staTitle}"/>				
                &nbsp;&nbsp;<input type="button" class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')" value="查询" />
            </form>  
        </div>  -->
        <table id="tab" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0">
            <tr>
                <th style="width:4%">附件</th>	
                <th style="width:6%">
                    <select id="states" onChange="changeStates()" style="width:95%">
                        <option value="a" selected>全部</option>
                        <option value="0">执行中</option>
                        <option value="1">已结束</option>
                        <option value="2">已取消</option>
                    </select>
                </th>
                <th style="width:35%">[优先级]工作名称</th>
                <th style="width:25%">对应子项目</th>
                <th style="width:10%">发布日期</th>
                <th style="width:10%">完成期限</th>
                <th style=" width:5%;border-right:0px">发布人</th>
            </tr>
            <logic:notEmpty name="allProTaskList">
                <logic:iterate id="proTask" name="allProTaskList">
                <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('projectAction.do?op=showProTaskDesc&prtaId=${proTask.prtaId}')">
                    <td style="text-align:center">
                    	<logic:notEmpty name="proTask" property="attachments">
                            <img id="${proTask.prtaId}y" style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理" onClick="parent.commonPopDiv(1,'${proTask.prtaId}','ptask','doc')"/>                              		
                        </logic:notEmpty>
                        <logic:empty name="proTask" property="attachments">
                            <img id="${proTask.prtaId}n" style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理" onClick="parent.commonPopDiv(1,'${proTask.prtaId}','ptask','doc')"/>
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
                    <td>${proTask.proStage.staTitle}&nbsp;</td>
                    <td><span id="prtaRelDate<%=count%>"></span>&nbsp;</td>
                    <td>
                        <logic:notEmpty  name="proTask" property="prtaFinDate">
                            <span id="prtaFinDate<%=count%>"></span>
                        </logic:notEmpty>
                        <logic:empty  name="proTask" property="prtaFinDate">
                            <span class="gray">未指定</span>
                        </logic:empty>
                    </td>
                    <td>${proTask.prtaName}&nbsp;</td>
                </tr>
                <script type="text/javascript">
                    dateFormat2('prtaRelDate','${proTask.prtaRelDate}',<%=count%>);
                    dateFormat2('prtaFinDate','${proTask.prtaFinDate}',<%=count%>);
                    
                    rowsBg('tr',<%=count%>);
                </script>
                <%count++; %>
                </logic:iterate>
            </logic:notEmpty>
            <logic:empty name="allProTaskList">
                <tr>
                    <td colspan="7" class="noDataTd">未找到相关数据!</td>
                </tr>
            </logic:empty>
        </table>
			<logic:notEmpty name="allProTaskList">	
				 <logic:equal name="pageType" value="allSenTaskSearch">
                 	<script type="text/javascript">
						createPage('projectAction.do?op=getAllProTask&proId=${proId}&staTitle='+ encodeURIComponent('${staTitle}') +'&prtaState='+ encodeURIComponent('${prtaState}'),'${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
				</logic:equal>	
           </logic:notEmpty>
   </div>
  </body>

</html>

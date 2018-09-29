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
    <title>我的项目工作(项目详情)(未使用)</title>
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
				self.location.href="projectAction.do?op=getProTaskByStage&proId=${proId}&staTitle="+ encodeURIComponent("${staTitle}") +"&ptlIsfin="+encodeURIComponent($("states").value);
			}
			else{
				self.location.href="projectAction.do?op=getProTaskByStage&proId=${proId}&staTitle="+ encodeURIComponent("${staTitle}");
			}
		}
		
		//初始化下拉框
		function loadStates(){
			if("${ptlIsfin}"!=null&&"${ptlIsfin}"!=""){
				$("states").value="${ptlIsfin}";
			}
		}
		
   		
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
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
                    <span class="blue middle bold">我的项目工作</span>
                </td>
                <td style=" text-align:left">
                    <form style="margin:0px; text-align:left;" id="searchForm" action="projectAction.do" method="get" >
                        <input type="hidden" name="op" value="getProTaskByStage" />
                        <input type="hidden" name="proId" value="${proId}"/>
                        根据子项目查询：<input class="inputSize2 inputBoxAlign"  type="text" name="staTitle" value="${staTitle}"/>				
                        &nbsp;<input type="button" class="smallBut inputBoxAlign" onClick="formSubmit('searchForm')" value="搜索" />
                    </form> 
                </td>
            </tr>
        </table>
        
        <table id="tab" class="normal rowstable noBr" cellpadding="0" cellspacing="0" width="98%">
            <tr>
                <!--<th style="width:4%">附件</th>-->	
                <th style="width:8%">
                    <select id="states" onChange="changeStates()" style="width:98%">
                        <option value="a" selected>全部</option>
                        <option value="0">执行中</option>
                        <option value="1">已提交</option>
                        <option value="2">被退回</option>
                        <option value="3">已完成</option>
                     </select>                         </th>
                <th style="width:31%">工作状态/[优先级]工作名称</th>
                <th style="width:20%">对应子项目</th>
                <th style="width:15%">发布时间</th>
                <th style="width:12%">完成期限</th>
                <th style="width:10%;border-right:0px">发布人</th>
             </tr>
             <logic:notEmpty name="myProTaskList">
             <logic:iterate id="proTaskLim" name="myProTaskList">
             <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('projectAction.do?op=showProTaskDesc&prtaId=${proTaskLim.proTask.prtaId}')">
                <!--<td style="text-align:center">
                	<logic:notEmpty name="proTaskLim" property="proTask.attachments">
                        <img id="${proTaskLim.proTask.prtaId}y" style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTaskLim.proTask.prtaId}','ptask','doc')"/>
                    </logic:notEmpty>
                    <logic:empty name="proTaskLim" property="proTask.attachments">
                        <img id="${proTaskLim.proTask.prtaId}n" style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTaskLim.proTask.prtaId}','ptask','doc')"/>
                    </logic:empty>
                </td>-->
                <td><div style="text-align:center">
                    <logic:equal value="0" name="proTaskLim" property="ptlIsfin"> 
                        <img src="images/content/doing.gif" alt="执行中"/></logic:equal>
                    <logic:equal value="1" name="proTaskLim" property="ptlIsfin"><img src="images/content/tofinish.gif" alt="已提交"/></logic:equal>
                    <logic:equal value="2" name="proTaskLim" property="ptlIsfin">
                        <img src="images/content/alert.gif" alt="被退回"/></logic:equal>
                    <logic:equal value="3" name="proTaskLim" property="ptlIsfin">
                        <img src="images/content/finish.gif" alt="已完成"/></logic:equal>
                    </div>                           	
                </td>
                <td>
                 <logic:equal name="proTaskLim" property="proTask.prtaState" value="0">
                    <img src="images/content/time.gif" alt="工作执行中"/></logic:equal> 
                 <logic:equal name="proTaskLim" property="proTask.prtaState" value="1">
                    <img src="images/content/suc.gif" alt="工作已完成"/></logic:equal> 
                 <logic:equal name="proTaskLim" property="proTask.prtaState" value="2">
                    <img src="images/content/fail.gif" alt="工作已取消"/></logic:equal> 
                 <a href="projectAction.do?op=showProTaskDesc&prtaId=${proTaskLim.proTask.prtaId}" style="cursor:pointer" target="_blank"><span class="textOverflow4" title="${proTaskLim.proTask.prtaTitle}">[<logic:equal value="0" name="proTaskLim" property="proTask.prtaLev">低</logic:equal><logic:equal value="1" name="proTaskLim" property="proTask.prtaLev">中</logic:equal><logic:equal value="2" name="proTaskLim" property="proTask.prtaLev">高</logic:equal>]${proTaskLim.proTask.prtaTitle}</span></a>                </td>
                <td>${proTaskLim.proTask.proStage.staTitle}&nbsp;</td>
                <td><span id="prtaRelDate<%=count%>"></span>&nbsp;</td>
                <td>
                    <logic:notEmpty  name="proTaskLim" property="proTask.prtaFinDate">
                        <span id="prtaFinDate<%=count%>"></span>                                 
                    </logic:notEmpty>
                     <logic:empty  name="proTaskLim" property="proTask.prtaFinDate">
                        <span class="gray">未指定</span>                                 
                     </logic:empty>
                     <logic:equal value="1" name="proTaskLim" property="ptlIsfin"> 
                         <span id="finDate<%=count%>" class="deepGreen">&nbsp;[已完成]</span>                                 
                     </logic:equal>                             
                </td>
                <td>${proTaskLim.proTask.prtaName}&nbsp;</td>
             </tr>
                 <script type="text/javascript">
            dateFormat2('prtaRelDate','${proTaskLim.proTask.prtaRelDate}',<%=count%>);
            dateFormat2('prtaFinDate','${proTaskLim.proTask.prtaFinDate}',<%=count%>);
            
            if($("finDate<%=count%>")!=null){
                var finDate="完成时间为 "+"${proTaskLim.ptlFinDate}".substring(0,16);
                $("finDate<%=count%>").setAttribute("title",finDate);
            }
            
            rowsBg('tr',<%=count%>);
            </script>
                 <%count++; %>
                </logic:iterate>
                </logic:notEmpty>
             <logic:empty name="myProTaskList">
                 <tr>
                     <td colspan="6" class="noDataTd">未找到相关数据!</td>
                 </tr>
             </logic:empty>
         </table>
         	 <logic:notEmpty name="myProTaskList">
 				<logic:equal name="pageType" value="default">
					<script type="text/javascript">
						createPage('projectAction.do?op=getProTask&proId=${proId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
				</logic:equal>
                
				<logic:equal name="pageType" value="searMyProTask">
                	<script type="text/javascript">
						createPage('projectAction.do?op=getProTaskByStage&proId=${proId}&staTitle='+ encodeURIComponent('${staTitle}') +'&ptlIsfin=${ptlIsfin}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
					</script>
  				</logic:equal>
              </logic:notEmpty>
		</div>			
   
  </body>

</html>

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
    <title>收到的项目工作(未使用)</title>
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
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
		//初始化下拉框
		function loadStates(){
			if("${prtaState}"!=null&&"${prtaState}"!=""){
				$("prtaState").value="${prtaState}";
			}
		}
		createProgressBar();
		window.onload=function(){
			
			//表格内容省略
			loadTabShort("tab");
			createCancelButton('searchForm',-50,5);
			loadStates();
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
      <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;项目管理 > 收到的项目工作</div>
  	     	<table id="cusTop" class="normal" width="100%" cellpadding="0px" cellspacing="0px">
                <tr>
                    <td height="35px">
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite"  onClick="selectProTask(1)">收到的工作</div> 
                            <div id="tabType2" class="tabTypeBlue1" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" onClick="selectProTask(2)">我发布的工作</div>
                            <script type="text/javascript">writeLimAllow("p017","<div id='tabType3' class='tabTypeBlue2' onMouseOver='changeTypeBg(this,1,2)' onMouseOut='changeTypeBg(this,0,2)' onClick='selectProTask(3)'>所有项目工作</div>","tabType");</script>                          
                        </div>
                     </td>
					 <td width="110px">
					<a href="javascript:void(0)" onClick="addDivNew(3);return false;" class="newBlueButton">新建项目工作</a>
					</td>
                </tr>
            </table>
		 	<div id="listContent">       
                <div class="listSearch">
			 		<form id="searchForm" action="projectAction.do" method="get" >
                        <input type="hidden" name="op" value="searMyProTask" />
                     工作名称：<input style="width:100px" class="inputSize2 inputBoxAlign" type="text" id="prtaTitle" name="prtaTitle" value="${prtaTitle}" onBlur="autoShort(this,300)"/>&nbsp;
                     对应项目：<input id="proTitle" name="proTitle" class="inputSize2 inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" style="width:142px" type="text" value="${proTitle}" readonly ondblClick="clearInput(this,'proId')" />&nbsp;
                          <button class="butSize2 inputBoxAlign" onClick="addDivBrow(17)">选择</button>
		                  <input type="text" name="proId" value="${proId}" id="proId" style="display:none"/>&nbsp;
                     工作状态：<select id="prtaState" name="prtaState" class="inputBoxAlign" style="width:90px">
                              <option value="">全部</option>
                              <option value="0">执行中</option>
                              <option value="1">已提交</option>
                              <option value="2">被退回</option>
						      <option value="3">已完成</option>
                          </select>
					    &nbsp;&nbsp;
                        <button class="butSize3 inputBoxAlign" onClick="formSubmit('searchForm')">查询</button>&nbsp;&nbsp;
                   </form>
				</div>
				<div class="dataList">
            		<table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0">
                    	<tr>
                          	<th style="width:4%; text-align:center">附件</th>
                          	<th style="width:4%; text-align:center">状态</th>
                          	<th style="width:18%">[优先级]工作名称</th>
                         	<th style="width:18%">对应项目</th>
                            <th style="width:10%">子项目</th>
                            <th style="width:10%">执行情况</th>
                            <th style="width:14%">完成期限</th>
                          	<th style="width:12%">发布日期</th>
                          	<th style=" width:10%;border-right:0px">操作</th>
                        </tr>
                        <logic:notEmpty name="myProTaskList">
                        	<logic:iterate id="proTaskLim" name="myProTaskList">
                            <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)" onDblClick="descPop('projectAction.do?op=showProTaskDesc&prtaId=${proTaskLim.proTask.prtaId}')">
                              	<td style="text-align:center">
                                	<logic:notEmpty name="proTaskLim" property="proTask.attachments">
                                        <img id="${proTaskLim.proTask.prtaId}y" style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTaskLim.proTask.prtaId}','ptask','doc')"/>
                                    </logic:notEmpty>
                                    <logic:empty name="proTaskLim" property="proTask.attachments">
                                        <img id="${proTaskLim.proTask.prtaId}n" style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTaskLim.proTask.prtaId}','ptask','doc')"/>
                                    </logic:empty>
                                </td>
                              	<!--<td><div style="text-align:center">
                                	<logic:equal value="0" name="proTaskLim" property="ptlIsfin"> 
                                    	<img src="images/content/doing.gif" alt="执行中"/>
                                  	</logic:equal>
                                  	<logic:equal value="1" name="proTaskLim" property="ptlIsfin">
                                    	<img src="images/content/tofinish.gif" alt="已提交"/>
                                  	</logic:equal>
                                  	<logic:equal value="2" name="proTaskLim" property="ptlIsfin">
                                    	<img src="images/content/alert.gif" alt="被退回"/>
                                  	</logic:equal>
									<logic:equal value="3" name="proTaskLim" property="ptlIsfin">
                                    	<img src="images/content/finish.gif" alt="已完成"/>
                                  	</logic:equal>
									</div>
                              	</td>  -->
                              	<td style="text-align:center">
                                <logic:equal name="proTaskLim" property="proTask.prtaState" value="0">
                                	<img class="imgAlign" src="images/content/time.gif" alt="工作执行中"/></logic:equal> 
                                <logic:equal name="proTaskLim" property="proTask.prtaState" value="1">
                                	<img class="imgAlign" src="images/content/suc.gif" alt="工作已完成"/></logic:equal> 
                                <logic:equal name="proTaskLim" property="proTask.prtaState" value="2">
                                	<img class="imgAlign" src="images/content/fail.gif" alt="工作已取消"/></logic:equal>
                                </td>
                                <td> 
                                <a href="projectAction.do?op=showProTaskDesc&prtaId=${proTaskLim.proTask.prtaId}" style="cursor:pointer" target="_blank">[<logic:equal value="0" name="proTaskLim" property="proTask.prtaLev">低</logic:equal><logic:equal value="1" name="proTaskLim" property="proTask.prtaLev">中</logic:equal><logic:equal value="2" name="proTaskLim" property="proTask.prtaLev">高</logic:equal>]${proTaskLim.proTask.prtaTitle}</a>
                                </td>
                                <td>${proTaskLim.proTask.project.proTitle}&nbsp;</td>
                              	<td>${proTaskLim.proTask.proStage.staTitle}&nbsp;</td>
                              	<td><a href="javascript:void(0)" onClick="getUserDiv('projectAction.do?op=getProTaskUsers&taskId=${proTaskLim.proTask.prtaId}','proTaUsers')">查看执行情况</a></td>
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
                                    <logic:equal value="3" name="proTaskLim" property="ptlIsfin"> 
                                        <span id="finDate<%=count%>" class="deepGreen">&nbsp;[已完成]</span>
                                    </logic:equal>
                                </td>
                              	<td><span id="prtaRelDate<%=count%>"></span>&nbsp;</td>
                              	
							  <td>
							  	<a href="projectAction.do?op=showProTaskDesc&prtaId=${proTaskLim.proTask.prtaId}" target="_blank">
								<img style="cursor:pointer" src="images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
	                            <logic:notEqual name="proTaskLim" property="proTask.prtaState" value="1">
								<img onClick="addDiv(5,'${proTaskLim.proTask.prtaId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;</logic:notEqual>
							  </td>
                            </tr>
                            <script type="text/javascript">
						  		dateFormat('prtaRelDate','${proTaskLim.proTask.prtaRelDate}',<%=count%>);
								if($("prtaFinDate<%=count%>")!=null)
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
                                <td colspan="9" class="noDataTd">未找到相关数据!</td>
                            </tr>
                        </logic:empty>
                    </table>
                    
                    <logic:notEmpty name="myProTaskList">
                    	<script type="text/javascript">
							var url;
							switch('${pageType}'){
								case 'default':
									url = 'projectAction.do?op=getMyProTask';
									break;
								case 'searMyProTask':
									url = 'projectAction.do?op=searMyProTask&proId=${proId}&proTitle='+encodeURIComponent('${proTitle}') + '&prtaTitle='+ encodeURIComponent('${prtaTitle}') +'&prtaState='+ encodeURIComponent('${prtaState}');
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

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int c=0;
int d=0;
int e=0;
int nofinCount=0;//执行成功人数
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>项目工作详情(未使用)</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />    
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    
	<link rel="stylesheet" type="text/css" href="css/att.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/pro.js"></script>
	<script type="text/javascript">
		
		function showDiv(){
			$("wunDiv").show();
		}
		
		/*
			更新工作状态
			arg:type为0,2时为工作id
				type为1时为工作未执行人数
		*/
		function controlTask(type,arg){
			var toSubmit = false;
			var url = 'projectAction.do';
			var pars;
			
			switch(type){
				case 0://重新开始工作
					pars = "op=startTask&prtaId="+arg;
					waitSubmit("reStart","提交中...");
					toSubmit = true;
					break;
					
				case 1://结束工作
					pars = $('stTagForm').serialize(true);
					if($("stTag")==null||$("stTag").value==""){
						alert("请填写工作总结！");
						return false;
					}
					else{
						if(arg>0){
							if(confirm("还有"+arg+"人未完成工作，工作完成后不可再编辑，确定完成工作吗？")){
								waitSubmit("doend","提交中...");
								waitSubmit("docancel");
								if($("dosave")!=null)
									waitSubmit("dosave");
								toSubmit = true;
							}
						}
						else if(confirm("完成工作后不可再编辑,确定完成工作吗？")){
							waitSubmit("doend","提交中...");
							waitSubmit("docancel");
							if($("dosave")!=null)
								waitSubmit("dosave");
							toSubmit = true;
						}
						break;
					}
					
				case 2://取消工作
					pars = "op=cancelTask&prtaId="+arg;
					if(confirm("确定要取消工作吗？")){
						waitSubmit("docancel","取消中...");
						waitSubmit("doend");
						if($("dosave")!=null)
							waitSubmit("dosave");
						toSubmit = true;
					}
					break;
					
				case 3://提交工作完成情况
					pars = $('completeForm').serialize(true);
					if(confirm("提交工作后不可再编辑,确定提交工作吗？")){
						waitSubmit("dosave","提交中...");
						if($("doend")){
							waitSubmit("doend");
							waitSubmit("docancel");
						}
						toSubmit = true;
					}
					break;
			}
			//ajax更新工作状态
			if(toSubmit&&pars!=undefined){
				new Ajax.Request(url,{
					method:'post',
					parameters: pars,
					onSuccess: function(response){
						history.go(0);
					},
					onFailure: function(response){
						if (response.status == 404)
							alert("您访问的地址不存在！");
						else
							alert("Error: status code is " + response.status);
					}
				});
			}
		}
		
		
		createProgressBar();
		window.onload=function(){
			if($("prtaLog")!=null){
				//工作日志收缩层
				if(shortDiv("prtaLog",150)){
					$("moreInfButton").show();
				}
			}
			closeProgressBar();
		}
	</script>
</head>
  
  <body>
    <logic:notEmpty name="proTask">
	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title">项目管理 > 项目工作 > 项目工作详情</div>
            <div class="descInf">
                <table class="normal dashTab" width="100%" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th class="descTitleL">工作名称</th>
							<th class="descTitleR" colspan="3">
								${proTask.prtaTitle}&nbsp;
							</th>
						</tr>
					</thead>
					<tbody>
                    <tr>
                        <th>对应项目：</th>
                        <td style="width:40%">${proTask.project.proTitle}&nbsp;</td>
                        <th>对应子项目：</th>
                        <td style="width:40%">${proTask.proStage.staTitle}&nbsp; </td>
                    </tr>
                    <tr>
                       	<th>工作状态：</th>
                        <td><logic:notEmpty name="proTask" property="prtaState">
                            <logic:equal value="0" name="proTask" property="prtaState"><img src="images/content/time.gif" alt="执行中" class="imgAlign">执行中</logic:equal>
                            <logic:equal value="1" name="proTask" property="prtaState"><img src="images/content/suc.gif" alt="已完成" class="imgAlign">已完成<logic:notEmpty name="proTask" property="prtaFctDate">&nbsp;&nbsp;<span class="blue">[结束时间：<span id="prtaFctDate"></span>]</span></logic:notEmpty></logic:equal>	
                            <logic:equal value="2" name="proTask" property="prtaState"><img src="images/content/fail.gif" alt="已取消" class="imgAlign"/>已取消</logic:equal>
                        </logic:notEmpty></td>
                        <th>优先级：</th>
                        <td >
                            <logic:equal value="0" name="proTask" property="prtaLev">低</logic:equal>
                            <logic:equal value="1" name="proTask" property="prtaLev">中</logic:equal>	
                            <logic:equal value="2" name="proTask" property="prtaLev">高</logic:equal>
                        </td>
                    </tr>
                    <tr>
                        <th><nobr>完成期限：</nobr></th>
                        <td colspan="3"><span id="prtaFinDate"></span>&nbsp;</td>
                    </tr>
                    <tr>
                        <th>执行人：</th>
                        <td colspan="3" style="font-weight:normal">
                            <logic:notEmpty name="proTask" property="proTaskLims">
                                <logic:iterate id="pTaskLim" name="proTask" property="proTaskLims">
                                    <logic:equal value="1" name="pTaskLim" property="ptlIsdel">
                                        <logic:equal value="3" name="pTaskLim" property="ptlIsfin">
                                            <img src="images/content/finish.gif" style="vertical-align:middle" alt="已完成"></logic:equal>
                                        <logic:equal value="1" name="pTaskLim" property="ptlIsfin">
                                            <img src="images/content/tofinish.gif" style="vertical-align:middle" alt="已提交"></logic:equal><logic:equal value="0" name="pTaskLim" property="ptlIsfin"><img src="images/content/doing.gif" style="vertical-align:middle" alt="执行中"><% nofinCount++;%></logic:equal><logic:equal value="2" name="pTaskLim" property="ptlIsfin"><img src="images/content/alert.gif" style="vertical-align:middle" alt="被退回"><% nofinCount++;%></logic:equal><logic:equal value="${limUser.salEmp.seNo}" name="pTaskLim" property="salEmp.seNo"><span style="font-weight:bold;"><a href="javascript:void(0);" onClick="addDiv(7,'${pTaskLim.ptlId}');return false;">${pTaskLim.ptlName}</a></span></logic:equal><logic:notEqual value="${limUser.salEmp.seNo}" name="pTaskLim" property="salEmp.seNo"><a href="javascript:void(0);" onClick="addDiv(7,'${pTaskLim.ptlId}');return false;">${pTaskLim.ptlName}</a></logic:notEqual><logic:notEmpty name="pTaskLim" property="attachments"><img class="imgAlign" src="images/content/attIconS.gif" alt="附件"/></logic:notEmpty>&nbsp;&nbsp;
                                    </logic:equal>	
                                    <% e++;%>
                                </logic:iterate> 
                            </logic:notEmpty>&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <th style="vertical-align:top"><nobr>工作描述：</nobr></th>
                        <td colspan="3">&nbsp;${proTask.prtaDesc}</td>
                    </tr>
<!--                    <tr>
                        <th style="vertical-align:top"><nobr>工作备注：</nobr></th>
                        <td colspan="3">&nbsp;${proTask.prtaRemark}</td>
                    </tr>-->
                    <tr>
                        <th style="vertical-align:top;"><nobr>工作日志：</nobr></th>
                        <td colspan="3" style="font-weight:normal">
                        <logic:notEmpty name="proTask" property="prtaLog">
                        <div id="allLog" style="display:none;">
                            ${proTask.prtaLog}
                            <a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someLog','allLog')">-&nbsp;点击收起</a>
                        </div>
                        <div id="someLog">
                            <div id="prtaLog">${proTask.prtaLog}</div>
                            <a id="moreInfButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allLog','someLog')">+&nbsp;点击展开</a>
                        </div>
                        </logic:notEmpty>
                        <logic:empty name="proTask" property="prtaLog">&nbsp;</logic:empty>
                        </td>
                    </tr>
                    <tr>
                        <th style="vertical-align:top;">工作总结：</th>
                        <td colspan="3">${proTask.prtaTag}&nbsp;</td>
                   	</tr>
                    <tr>
                        <th style="border-bottom:0px;">
                        	<logic:greaterEqual value="2" name="isInTask">
                                <logic:notEmpty name="proTask" property="attachments">
                                    <img style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTask.prtaId}','ptask','reload')"/>
                                 </logic:notEmpty>
                                 <logic:empty name="proTask" property="attachments">
                                    <img style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proTask.prtaId}','ptask','reload')"/>
                                 </logic:empty>
                             </logic:greaterEqual>
                             附件：
                        </th>
                        <td colspan="3" style="border-bottom:0px">
                             <logic:iterate id="attList" name="proTask" property="attachments">
                                <span id="fileDLLi<%=count%>"></span>
                    			<script type="text/javascript">createFileToDL(<%=count%>,'${attList.attId}','${attList.attPath}','${attList.attName}','${attList.attSize}','${attList.attDate}')</script>
                                <% count++;%>
                            </logic:iterate>&nbsp;
                        </td>
                    </tr>
					
                    <tr>
                        <td colspan="4" style="text-align:center; border:0">
                        <logic:equal value="0" name="proTask" property="prtaState">
                            <logic:greaterEqual value="2" name="isInTask">
                                <div style="background-color:#FFCC00; padding:4px">
                                    <input type="button" class="butSize1" value="结束工作" style="width:120px" onClick="showDiv()"><span style="width:20%"></span>
                                    <input id="docancel" type="button" class="butSize1" value="取消工作" style="width:120px" onClick="controlTask(2,'${proTask.prtaId}')">
                                </div>
								<div id="wunDiv" class="appLayer" style="display:none;">
									<div class="appLayerTitle">工作总结</div>
									<div style="text-align:center; padding-bottom:5px">
                                    	<form id="stTagForm" style="margin:0; padding:0;">
                                        	<input type="hidden" name="op" value="finishTask"/>
                                           	<input type="hidden" name="prtaId" value="${proTask.prtaId}"/>
                                            <textarea id="stTag" name="stTag" style="width:100%" rows="5" onBlur="autoShort(this,500)"></textarea>
                                        </form>
                                    </div>
                                    <div style="text-align:center">
									<input type="button" class="butSize3 inputBoxAlign" id="doend" onClick="controlTask(1,<%=nofinCount%>)" value="提交" style="width:80px">
                                    </div>
                  			  	</div>
                             </logic:greaterEqual>
                             <logic:notEmpty name="taLim">
                                <logic:notEqual value="1" name="taLim" property="ptlIsfin">
                                <logic:notEqual value="3" name="taLim" property="ptlIsfin">
                                <div id="submitTa" class="appLayer">
                                    <div class="appLayerTitle">
                                        我的完成情况
                                    </div>
                                    <div style="padding:2px;">
                                    	<span onClick="commonPopDiv(1,'${taLim.ptlId}','prta','doc')" onMouseOver="this.className='lightBlueBg'" onMouseOut="this.className=''" style="cursor:pointer; padding:2px; width:80px;">
                                            <logic:notEmpty name="taLim" property="attachments"><img id="${taLim.ptlId}y" style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理"/></logic:notEmpty><logic:empty name="taLim" property="attachments"><img id="${taLim.ptlId}n" style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理"/></logic:empty>&nbsp;上传附件
                                        </span>
                                    </div>
                                    <form id="completeForm" style="padding:0; margin:0;"/>
                                        <input type="hidden" name="op" value="exeTask"/>
                                        <input type="hidden" name="prtaId" value="${taLim.proTask.prtaId}">
                                        <input type="hidden" name="ptlId" value="${taLim.ptlId}">
                                        <div style="text-align:center"><textarea name="finDesc" style="width:100%" rows="4"></textarea></div>
                                        <div style="text-align:center; padding:5px">
                                        <input id="dosave" onClick="controlTask(3)" type="button" style="width:100px" value="提交完成情况" class="butSize1"/></div>
                                    </form>
                                </div>
                                </logic:notEqual>
                                </logic:notEqual>
                            </logic:notEmpty>
                            <logic:equal value="0" name="isInTask">
                            	<div style="background-color:#dbdddb; padding:4px" class="bold middle">
                                    &nbsp;工作正在执行中...
                                </div>
                            </logic:equal>
                        </logic:equal>
                        <logic:equal value="1" name="proTask" property="prtaState">
                            <div style="background-color:#dbdddb; padding:4px" class="bold middle">
                                <img src="images/content/bigSuc.gif" alt="已完成" style="vertical-align:middle"/>&nbsp;工作已完成
                            </div>
                        </logic:equal>
                        <logic:equal value="2" name="proTask" property="prtaState">
                            <div style="background-color:#dbdddb; padding:4px" class="bold middle">
                                <img src="images/content/bigFail.gif" alt="已取消" style="vertical-align:middle"/>&nbsp;工作已取消&nbsp;&nbsp;&nbsp;&nbsp;<input id="reStart" type="button" class="butSize1 inputBoxAlign" value="重新开始工作" style="width:150px" onClick="controlTask(0,'${proTask.prtaId}')">
                            </div>
                        </logic:equal>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" class="descStamp" style="border-bottom:0px">
                            由
                            <span>${proTask.prtaName}</span>
                            于
                            <span id="inpDate"></span>&nbsp;
                            发布
                            <logic:notEmpty name="proTask" property="prtaChangeDate">，最近由
                                <span>${proTask.prtaUpdUser}</span>
                                于
                            	<span id="changeDate"></span>&nbsp;
                            </logic:notEmpty>
                        </td>
                    </tr>
					</tbody>
					<script type="text/javascript">
                         removeTime('prtaFinDate','${proTask.prtaFinDate}',2);
                         removeTime('prtaFctDate','${proTask.prtaFctDate}',2);
                         removeTime('inpDate','${proTask.prtaRelDate}',2);
                         removeTime("changeDate","${proTask.prtaChangeDate}",2);
                    </script>
             	</table>
            </div>
        </div>
  	</div>
  	 </logic:notEmpty>
  <logic:empty name="proTask">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该项目工作已被删除</div>
	  </logic:empty>
  </body>
</html>

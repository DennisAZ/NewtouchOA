<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int c=0;
int d=0;
int e=0;
int nofinCount=0;//未执行执行人数
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>工作安排详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />    
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript" src="crm/js/oa.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
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
			var url = 'salTaskAction.do';
			var pars;
			switch(type){
				case 0://重新开始工作
					pars = "op=startTask&stId="+arg;
					waitSubmit('reStart','提交中...');
					toSubmit = true;
					break;
					
				case 1://结束工作
					pars = $('stTagForm').serialize(true);
					if($("stTag")==null||$("stTag").value==""){
						alert("请填写总结！");
						return false;
					}
					else{
						
						if(arg>0){
							if(confirm("还有"+arg+"人未提交工作，工作完成后不可再编辑，确定完成该工作吗？")){
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
					}
					break;
					
				case 2://取消工作
					pars = "op=cancelTask&stId="+arg;
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
			if($("stLog")!=null){
				//工作日志收缩层
				if(shortDiv("stLog",150)){
					$("moreInfButton").show();
				}
			}
			closeProgressBar();
			
		}
	</script>
</head>
  
  <body>
  	<logic:notEmpty name="salTask">
    <div id="mainbox">
    	<div id="contentbox">
            <div class="descInf">
            	<div id="descTop">工作安排详细信息&nbsp;<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">工作名称：</th>
                            <th class="descTitleR" colspan="3">
                                ${salTask.stTitle}&nbsp;
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <th>类别：</th>
                            <td>${salTask.salTaskType.typName}&nbsp;</td>
                            <th>优先级：</th>
                            <td>
                                <logic:equal value="0" name="salTask" property="stLev">低</logic:equal>
                                <logic:equal value="1" name="salTask" property="stLev">中</logic:equal>	
                                <logic:equal value="2" name="salTask" property="stLev">高</logic:equal>
                            </td>
                        </tr>
                        <tr>
                            <th>状态：</th>
                            <td class="longTd" colspan="3">
                                <logic:notEmpty name="salTask" property="stStu">
                                    <logic:equal value="0" name="salTask" property="stStu"><img src="crm/images/content/time.gif" alt="执行中" class="imgAlign">执行中</logic:equal>
                                    <logic:equal value="1" name="salTask" property="stStu"><img src="crm/images/content/suc.gif" alt="已完成" class="imgAlign">已完成<logic:notEmpty name="salTask" property="stFctDate">&nbsp;&nbsp;<span class="blue">[完成时间：<span id="str"></span>]</span></logic:notEmpty></logic:equal>	
                                    <logic:equal value="2" name="salTask" property="stStu"><img src="crm/images/content/fail.gif" alt="已取消" class="imgAlign"/>已取消</logic:equal>	
                                </logic:notEmpty>
                            </td>
                        </tr>
                     	<tr>
                        	<th>开始日期：</th>
                            <td><span id="sts"></span>&nbsp;</td>
                            <th>结束日期：</th>
                            <td><span id="stf"></span>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>执行人：</th>
                            <td colspan="3" style="font-weight:normal">
                                <logic:notEmpty name="salTask" property="taLims">
                                    <logic:iterate id="ta" name="salTask" property="taLims">
                                        <logic:equal value="1" name="ta" property="taIsfin">
                                        <logic:equal value="3" name="ta" property="taIsdel"><img src="crm/images/content/finish.gif" class="imgAlign" alt="已确认提交"></logic:equal><logic:equal value="1" name="ta" property="taIsdel"><img src="crm/images/content/tofinish.gif" class="imgAlign" alt="已提交"></logic:equal><logic:equal value="0" name="ta" property="taIsdel"><img src="crm/images/content/doing.gif" class="imgAlign" alt="执行中"><% nofinCount++;%></logic:equal><logic:equal value="2" name="ta" property="taIsdel"><img src="crm/images/content/alert.gif" class="imgAlign" alt="被退回"><% nofinCount++;%></logic:equal><logic:equal value="${limUser.salEmp.seNo}" name="ta" property="salEmp.seNo"><span style="font-weight:bold;"><a href="javascript:void(0);" onClick="oaPopDiv(52,'${ta.taLimId}');return false;">${ta.taName}</a></span></logic:equal><logic:notEqual value="${limUser.salEmp.seNo}" name="ta" property="salEmp.seNo"><a href="javascript:void(0);" onClick="oaPopDiv(52,'${ta.taLimId}');return false;">${ta.taName}</a></logic:notEqual><logic:notEmpty name="ta" property="attachments"><img class="imgAlign" src="crm/images/content/attIconS.gif" alt="附件"/></logic:notEmpty>&nbsp;&nbsp;
                                        </logic:equal>	
                                        <% e++;%>
                                    </logic:iterate> 
                                </logic:notEmpty>&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <th>描述：</th>
                            <td colspan="3">${salTask.stRemark}&nbsp;</td>
                        </tr>
                    
                        <tr>
                            <th>工作日志：</th>
                            <td colspan="3" style="font-weight:normal">
                            <logic:notEmpty name="salTask" property="stLog">
							<div id="allLog" style="display:none;">
                            	 ${salTask.stLog}
								<a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someLog','allLog')">-&nbsp;点击收起</a>
							</div>
							<div id="someLog">
                                <div id="stLog">${salTask.stLog}</div>
                                <a id="moreInfButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allLog','someLog')">+&nbsp;点击展开</a>
                            </div>
                            </logic:notEmpty>
                            <logic:empty name="salTask" property="stLog">&nbsp;</logic:empty>
                            </td>
                        </tr>
                        <tr>
                            <th>总结：</th>
                            <td colspan="3">${salTask.stTag}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                            	<logic:greaterEqual value="2" name="isInTask">
                                    <logic:notEmpty name="salTask" property="attachments">
                                        <img class="imgAlign" style="cursor:pointer" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${salTask.stId}','task','reload')"/>
                                     </logic:notEmpty>
                                     <logic:empty name="salTask" property="attachments">
                                        <img class="imgAlign" style="cursor:pointer" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${salTask.stId}','task','reload')"/>
                                     </logic:empty>
                                 </logic:greaterEqual>
                                 附件：
                            </th>
                            <td colspan="3">
                                 <logic:iterate id="attList" name="salTask" property="attachments">
                                    <span id="fileDLLi<%=c%>"></span>
                    				<script type="text/javascript">createFileToDL(<%=c%>,"${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                                    <% c++;%>
                                </logic:iterate>&nbsp;
                            </td>
                        </tr>
                        
                        <tr class="noBorderBot">
                            <td colspan="4" style="text-align:center">
                            <logic:equal value="0" name="salTask" property="stStu">
                            	<logic:greaterEqual value="2" name="isInTask">
                                    <div style="background-color:#FFCC00; padding:4px">
                                        <input type="button" class="butSize1" value="完成工作" style="width:120px" onClick="showDiv()"><span style="width:20%"></span>
                                        <input id="docancel" type="button" class="butSize1" value="取消工作" style="width:120px" onClick="controlTask(2,'${salTask.stId}')">
                                    </div>
                                    <div id="wunDiv" class="appLayer" style="display:none;">
                                        <div class="appLayerTitle">总结</div>
                                        <div style="text-align:center; padding-bottom:5px">
                                        <form id="stTagForm" style="margin:0; padding:0;">
                                        	<input type="hidden" name="op" value="finishTask"/>
                                           	<input type="hidden" name="stId" value="${salTask.stId}"/>
                                            <textarea id="stTag" name="stTag" style="width:100%" rows="5" onBlur="autoShort(this,500)"></textarea></form></div>
                                        <div style="text-align:center"><input type="button" class="butSize1 inputBoxAlign" id="doend"  onClick="controlTask(1,<%=nofinCount%>)" style="width:80px"  value="提交"></div>
                                  	</div>
                           		</logic:greaterEqual>
                                <logic:notEmpty name="taLim">
                                	<logic:notEqual value="1" name="taLim" property="taIsdel">
                                    <logic:notEqual value="3" name="taLim" property="taIsdel">
                                    <div id="submitTa" class="appLayer">
                                    	<div class="appLayerTitle">
                                        	我的完成情况
                                        </div>
                                        <div style="padding:2px;">
                                        	<span onClick="commonPopDiv(1,'${taLim.taLimId}','ta','doc')" onMouseOver="this.className='lightBlueBg'" onMouseOut="this.className=''" style="cursor:pointer; padding:2px; width:80px;">
                                                <logic:notEmpty name="taLim" property="attachments"><img id="${taLim.taLimId}y" class="imgAlign" style="cursor:pointer" src="crm/images/content/attach.gif" border="0px" alt="附件管理"/></logic:notEmpty><logic:empty name="taLim" property="attachments"><img id="${taLim.taLimId}n" class="imgAlign" style="cursor:pointer" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理"/></logic:empty>&nbsp;上传附件
                                            </span>
                                        </div>
                                     	<form id="completeForm" style="padding:0; margin:0;"/>
                                            <input type="hidden" name="op" value="optionTask"/>
                                            <input type="hidden" name="stId" value="${taLim.salTask.stId}"/>
                                            <input type="hidden" name="taId" value="${taLim.taLimId}"/>
                                            <div style="text-align:center"><textarea name="taskDesc" style="width:100%" rows="4"></textarea></div>
                                            <div style="text-align:center; padding:5px">
                                            <input id="dosave" onClick="controlTask(3)" type="button" style="width:100px" value="提交完成情况" class="butSize1"/></div>
                                     	</form>
                                    </div>
                                    </logic:notEqual>
                                    </logic:notEqual>
                                </logic:notEmpty>
                           	</logic:equal>
                            <logic:equal value="1" name="salTask" property="stStu">
                                <div style="background-color:#dbdddb; padding:4px" class="bold middle">
                                    <img src="crm/images/content/bigSuc.gif" alt="已完成" style="vertical-align:middle"/>&nbsp;工作已完成
                                </div>
                            </logic:equal>
                            <logic:equal value="2" name="salTask" property="stStu">
                                <div style="background-color:#dbdddb; padding:4px" class="bold middle">
                                    <img src="crm/images/content/bigFail.gif" alt="已取消" style="vertical-align:middle"/>&nbsp;工作已取消&nbsp;&nbsp;&nbsp;&nbsp;<input id="reStart" type="button" class="butSize1 inputBoxAlign" value="重新开始工作" style="width:150px" onClick="controlTask(0,'${salTask.stId}')">
                                </div>
                            </logic:equal>
                            </td>
                        </tr>
                    </tbody>
             	</table>
                <div class="descStamp">
                    由
                    <span>${salTask.stName}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    发布
                    <logic:notEmpty name="salTask" property="stChangeDate">，最近由
                    <span>${salTask.stUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;修改
                    </logic:notEmpty>
                </div>
                <script type="text/javascript">
					removeTime('sts','${salTask.stStartDate}',1);
                    removeTime('stf','${salTask.stFinDate}',1);
                    removeTime('str','${salTask.stFctDate}',2);
                    removeTime('inpDate','${salTask.stRelDate}',2);
                    removeTime("changeDate","${salTask.stChangeDate}",2);
                </script>
            </div>
        </div>
  	</div>
    </logic:notEmpty>
    <logic:empty name="salTask">
        <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该工作安排已被删除</div>
    </logic:empty>
  </body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>收到的工作</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/desk.css"/>
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:left;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    </head>
  
  <body>
  	<div class="modTitle" style="margin-top:0px;">七天内</div>
    	<logic:notEmpty name='lastTask' >
            <logic:iterate id='lastTask' name='lastTask'>
            	<div class="modCon">
            		<logic:equal value="2" name="lastTask" property="taIsdel">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <logic:notEmpty name="lastTask" property="salTask.salTaskType">
                            <span class="red"><nobr>[${lastTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <span class="red"><a href="salTaskAction.do?op=salTaskDesc&stId=${lastTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${lastTask.salTask.stTitle}
                        </a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="lastTask" property="taIsdel">
                        <logic:equal value="0" name="lastTask" property="taIsdel"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="lastTask" property="taIsdel">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="lastTask" property="taIsdel">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <logic:notEmpty name="lastTask" property="salTask.salTaskType">
                            <span class="brown"><nobr>[${lastTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <a href="salTaskAction.do?op=salTaskDesc&stId=${lastTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${lastTask.salTask.stTitle}</a>
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="lastTask" property="salTask.stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="lastTask" property="salTask.stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="lastTask" property="salTask.stLev"><span class="red">高</span></logic:equal></span>
                     <span title="发布人" class="deepGray">${lastTask.salTask.stName}</span>
                    <span class='gray small' title="结束日期"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
                        dateFormat('endDate','${lastTask.salTask.stFinDate}',<%=count%>);
                    </script>
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='lastTask'>
            <div class="gray">&nbsp;&nbsp;没有待办工作</div>
        </logic:empty>
        
        <div class="modTitle">七天后</div>
        <logic:notEmpty name='laterTask'>
            <logic:iterate id='laterTask' name='laterTask'>
                <div class="modCon">
            		<logic:equal value="2" name="laterTask" property="taIsdel">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <logic:notEmpty name="laterTask" property="salTask.salTaskType">
                            <span class="red"><nobr>[${laterTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <span class="red"><a href="salTaskAction.do?op=salTaskDesc&stId==${laterTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${laterTask.salTask.stTitle}
                        </a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="laterTask" property="taIsdel">
                        <logic:equal value="0" name="laterTask" property="taIsdel"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="laterTask" property="taIsdel">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="laterTask" property="taIsdel">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <logic:notEmpty name="laterTask" property="salTask.salTaskType">
                            <span class="brown"><nobr>[${laterTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <a href="salTaskAction.do?op=salTaskDesc&stId=${laterTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${laterTask.salTask.stTitle}
                        </a>
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="laterTask" property="salTask.stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="laterTask" property="salTask.stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="laterTask" property="salTask.stLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${laterTask.salTask.stName}</span>
                    <span class='gray small' title="结束日期"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
                        dateFormat('endDate','${laterTask.salTask.stFinDate}',<%=count%>);
                    </script>
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='laterTask'>
            <div class="gray">&nbsp;&nbsp;没有待办工作</div>
        </logic:empty>
        
        <div class="modTitle">无结束日期</div>
        <logic:notEmpty name='noTimeTask'>
            <logic:iterate id='noTimeTask' name='noTimeTask'>
                <div class="modCon">
            		<logic:equal value="2" name="noTimeTask" property="taIsdel">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <logic:notEmpty name="noTimeTask" property="salTask.salTaskType">
                            <span class="red"><nobr>[${noTimeTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <span class="red"><a href="salTaskAction.do?op=salTaskDesc&stId==${noTimeTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${noTimeTask.salTask.stTitle}</a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="noTimeTask" property="taIsdel">
                        <logic:equal value="0" name="noTimeTask" property="taIsdel"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="noTimeTask" property="taIsdel">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="noTimeTask" property="taIsdel">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <logic:notEmpty name="noTimeTask" property="salTask.salTaskType">
                            <span class="brown"><nobr>[${noTimeTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <a href="salTaskAction.do?op=salTaskDesc&stId=${noTimeTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${noTimeTask.salTask.stTitle}</a>
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="noTimeTask" property="salTask.stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="noTimeTask" property="salTask.stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="noTimeTask" property="salTask.stLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${noTimeTask.salTask.stName}</span>
                    
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='noTimeTask'>
            <div class="gray">&nbsp;&nbsp;没有待办工作</div>
        </logic:empty>
        
        <div class="modTitle">过期</div>
        <logic:notEmpty name='timeOutTask'>
            <logic:iterate id='timeOutTask' name='timeOutTask'>
               <div class="modCon">
            		<logic:equal value="2" name="timeOutTask" property="taIsdel">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <logic:notEmpty name="timeOutTask" property="salTask.salTaskType">
                            <span class="red"><nobr>[${timeOutTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <span class="red"><a href="salTaskAction.do?op=salTaskDesc&stId=${timeOutTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${timeOutTask.salTask.stTitle}
                        </a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="timeOutTask" property="taIsdel">
                        <logic:equal value="0" name="timeOutTask" property="taIsdel"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="timeOutTask" property="taIsdel">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="timeOutTask" property="taIsdel">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <logic:notEmpty name="timeOutTask" property="salTask.salTaskType">
                            <span class="brown"><nobr>[${timeOutTask.salTask.salTaskType.typName}]</nobr></span>
                        </logic:notEmpty>
                        <a href="salTaskAction.do?op=salTaskDesc&stId=${timeOutTask.salTask.stId}" style="cursor:pointer" target="_blank">
                        ${timeOutTask.salTask.stTitle}</a>
                        
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="timeOutTask" property="salTask.stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="timeOutTask" property="salTask.stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="timeOutTask" property="salTask.stLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${timeOutTask.salTask.stName}</span>
                    <span class='gray small' title="结束日期"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
                        dateFormat('endDate','${timeOutTask.salTask.stFinDate}',<%=count%>);
                    </script>
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='timeOutTask'>
            <div class="gray">&nbsp;&nbsp;没有待办工作</div>
        </logic:empty>
        
        
  </body>
</html>

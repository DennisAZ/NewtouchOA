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
    <title>收到项目工作</title>
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
    	<logic:notEmpty name='lastProTask' >
            <logic:iterate id='lastProTask' name='lastProTask'>
            	<div class="modCon">
            		<logic:equal value="2" name="lastProTask" property="ptlIsfin">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <span class="red"><a href="projectAction.do?op=showProTaskDesc&prtaId=${lastProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${lastProTask.proTask.prtaTitle}
                        </a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="lastProTask" property="ptlIsfin">
                        <logic:equal value="0" name="lastProTask" property="ptlIsfin"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="lastProTask" property="ptlIsfin">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="lastProTask" property="ptlIsfin">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <a href="projectAction.do?op=showProTaskDesc&prtaId=${lastProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${lastProTask.proTask.prtaTitle}</a>
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="lastProTask" property="proTask.prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="lastProTask" property="proTask.prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="lastProTask" property="proTask.prtaLev"><span class="red">高</span></logic:equal></span>
                     <span title="发布人" class="deepGray">${lastProTask.proTask.prtaName}</span>
                    <span class='gray small' title="完成期限"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
                        dateFormat('endDate','${lastProTask.proTask.prtaFinDate}',<%=count%>);
                    </script>
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='lastProTask'>
            <div class="gray">&nbsp;&nbsp;没有待办项目工作</div>
        </logic:empty>
        
        <div class="modTitle">七天后</div>
        <logic:notEmpty name='laterProTask'>
            <logic:iterate id='laterProTask' name='laterProTask'>
                <div class="modCon">
            		<logic:equal value="2" name="laterProTask" property="ptlIsfin">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <span class="red"><a href="projectAction.do?op=showProTaskDesc&prtaId=${laterProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${laterProTask.proTask.prtaTitle}
                        </a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="laterProTask" property="ptlIsfin">
                        <logic:equal value="0" name="laterProTask" property="ptlIsfin"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="laterProTask" property="ptlIsfin">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="laterProTask" property="ptlIsfin">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <a href="projectAction.do?op=showProTaskDesc&prtaId=${laterProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${laterProTask.proTask.prtaTitle}
                        </a>
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="laterProTask" property="proTask.prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="laterProTask" property="proTask.prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="laterProTask" property="proTask.prtaLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${laterProTask.proTask.prtaName}</span>
                    <span class='gray small' title="完成期限"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
                        dateFormat('endDate','${laterProTask.proTask.prtaFinDate}',<%=count%>);
                    </script>
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='laterProTask'>
            <div class="gray">&nbsp;&nbsp;没有待办项目工作</div>
        </logic:empty>
        
        <div class="modTitle">无完成期限</div>
        <logic:notEmpty name='noTimeProTask'>
            <logic:iterate id='noTimeProTask' name='noTimeProTask'>
                <div class="modCon">
            		<logic:equal value="2" name="noTimeProTask" property="ptlIsfin">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <span class="red"><a href="projectAction.do?op=showProTaskDesc&prtaId=${noTimeProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${noTimeProTask.proTask.prtaTitle}</a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="noTimeProTask" property="ptlIsfin">
                        <logic:equal value="0" name="noTimeProTask" property="ptlIsfin"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="noTimeProTask" property="ptlIsfin">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="noTimeProTask" property="ptlIsfin">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <a href="projectAction.do?op=showProTaskDesc&prtaId=${noTimeProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${noTimeProTask.proTask.prtaTitle}</a>
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="noTimeProTask" property="proTask.prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="noTimeProTask" property="proTask.prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="noTimeProTask" property="proTask.prtaLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${noTimeProTask.proTask.prtaName}</span>
                    
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='noTimeProTask'>
            <div class="gray">&nbsp;&nbsp;没有待办项目工作</div>
        </logic:empty>
        
        <div class="modTitle">过期</div>
        <logic:notEmpty name='timeOutProTask'>
            <logic:iterate id='timeOutProTask' name='timeOutProTask'>
               <div class="modCon">
            		<logic:equal value="2" name="timeOutProTask" property="ptlIsfin">
                        <img src="images/content/alert.gif" alt="被退回"/>
                        <span class="red"><a href="projectAction.do?op=showProTaskDesc&prtaId=${timeOutProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${timeOutProTask.proTask.prtaTitle}
                        </a></span>
                	</logic:equal>
                	<logic:notEqual value="2" name="timeOutProTask" property="ptlIsfin">
                        <logic:equal value="0" name="timeOutProTask" property="ptlIsfin"> 
                            <img src="images/content/doing.gif" alt="执行中"/>
                        </logic:equal>
                        <logic:equal value="1" name="timeOutProTask" property="ptlIsfin">
                            <img src="images/content/tofinish.gif" alt="已提交"/>
                        </logic:equal>
                        <logic:equal value="3" name="timeOutProTask" property="ptlIsfin">
                            <img src="images/content/finish.gif" alt="已完成"/>
                        </logic:equal>
                        <a href="projectAction.do?op=showProTaskDesc&prtaId=${timeOutProTask.proTask.prtaId}" style="cursor:pointer" target="_blank">
                        ${timeOutProTask.proTask.prtaTitle}</a>
                	</logic:notEqual>
                    &nbsp;
                	<span title="优先级"><logic:equal value="0" name="timeOutProTask" property="proTask.prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="timeOutProTask" property="proTask.prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="timeOutProTask" property="proTask.prtaLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${timeOutProTask.proTask.prtaName}</span>
                    <span class='gray small' title="完成期限"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
                        dateFormat('endDate','${timeOutProTask.proTask.prtaFinDate}',<%=count%>);
                    </script>
                    <% count++; %>
                </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='timeOutProTask'>
            <div class="gray">&nbsp;&nbsp;没有待办项目工作</div>
        </logic:empty>
        
        
  </body>
</html>

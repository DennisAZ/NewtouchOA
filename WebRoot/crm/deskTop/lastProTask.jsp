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
    <title>发布项目工作</title>
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
    	<logic:notEmpty name='lastProTask'>
            <logic:iterate id='lastProTask' name='lastProTask'>
                <div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                	<a href="projectAction.do?op=showProTaskDesc&prtaId=${lastProTask.prtaId}" target="_blank">
                    ${lastProTask.prtaTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="lastProTask" property="prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="lastProTask" property="prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="lastProTask" property="prtaLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${lastProTask.prtaName}</span>
                    <span class='gray small' title="完成期限"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
						dateFormat('endDate','${lastProTask.prtaFinDate}',<%=count%>);
					</script>
					<% count++; %>
               	</div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='lastProTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办项目工作</div>
        </logic:empty>
        
        <div class="modTitle">七天后</div>
        <logic:notEmpty name='laterProTask'>
            <logic:iterate id='laterProTask' name='laterProTask'>
                <div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                	<a href="projectAction.do?op=showProTaskDesc&prtaId=${laterProTask.prtaId}" target="_blank">
                    ${laterProTask.prtaTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="laterProTask" property="prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="laterProTask" property="prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="laterProTask" property="prtaLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${laterProTask.prtaName}</span>
                    <span class='gray small' title="完成期限"><nobr><label id="endDate1<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
						dateFormat('endDate1','${laterProTask.prtaFinDate}',<%=count%>);
					</script>
					<% count++; %>
               </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='laterProTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办项目工作</div>
        </logic:empty>
        
        <div class="modTitle">无完成期限</div>
        <logic:notEmpty name='noTimeProTask'>
            <logic:iterate id='noTimeProTask' name='noTimeProTask'>
            	<div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                	<a href="projectAction.do?op=showProTaskDesc&prtaId=${noTimeProTask.prtaId}" target="_blank">
                    ${noTimeProTask.prtaTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="noTimeProTask" property="prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="noTimeProTask" property="prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="noTimeProTask" property="prtaLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${noTimeProTask.prtaName}</span>
					<% count++; %>
               </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='noTimeProTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办项目工作</div>
        </logic:empty>
        
        <div class="modTitle">过期</div>
        <logic:notEmpty name='timeOutProTask'>
            <logic:iterate id='timeOutProTask' name='timeOutProTask'>
            	<div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                	<a href="projectAction.do?op=showProTaskDesc&prtaId=${timeOutProTask.prtaId}" target="_blank">
                    ${timeOutProTask.prtaTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="timeOutProTask" property="prtaLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="timeOutProTask" property="prtaLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="timeOutProTask" property="prtaLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${timeOutProTask.prtaName}</span>
                    <span class='gray small' title="完成期限"><nobr><label id="endDate1<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
						dateFormat('endDate1','${timeOutProTask.prtaFinDate}',<%=count%>);
					</script>
					<% count++; %>
               </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='timeOutProTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办项目工作</div>
        </logic:empty>
        
        
  </body>
</html>

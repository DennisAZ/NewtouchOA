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
    <title>发布的工作</title>
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
    	<logic:notEmpty name='lastTask'>
            <logic:iterate id='lastTask' name='lastTask'>
                <div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                    <logic:notEmpty name="lastTask" property="salTaskType">
                        <span class="brown"><nobr>[${lastTask.salTaskType.typName}]</nobr></span>
                    </logic:notEmpty>
                	<a href="salTaskAction.do?op=salTaskDesc&stId=${lastTask.stId}" target="_blank">
                    ${lastTask.stTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="lastTask" property="stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="lastTask" property="stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="lastTask" property="stLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${lastTask.stName}</span>
                    <span class='gray small' title="结束日期"><nobr><label id="endDate<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
						dateFormat('endDate','${lastTask.stFinDate}',<%=count%>);
					</script>
					<% count++; %>
               	</div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='lastTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办工作</div>
        </logic:empty>
        
        <div class="modTitle">七天后</div>
        <logic:notEmpty name='laterTask'>
            <logic:iterate id='laterTask' name='laterTask'>
                <div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                    <logic:notEmpty name="laterTask" property="salTaskType">
                        <span class="brown"><nobr>[${laterTask.salTaskType.typName}]</nobr></span>
                    </logic:notEmpty>
                	<a href="salTaskAction.do?op=salTaskDesc&stId=${laterTask.stId}" target="_blank">
                    ${laterTask.stTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="laterTask" property="stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="laterTask" property="stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="laterTask" property="stLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${laterTask.stName}</span>
                    <span class='gray small' title="结束日期"><nobr><label id="endDate1<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
						dateFormat('endDate1','${laterTask.stFinDate}',<%=count%>);
					</script>
					<% count++; %>
               </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='laterTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办工作</div>
        </logic:empty>
        
        <div class="modTitle">无结束日期</div>
        <logic:notEmpty name='noTimeTask'>
            <logic:iterate id='noTimeTask' name='noTimeTask'>
            	<div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                    <logic:notEmpty name="noTimeTask" property="salTaskType">
                        <span class="brown"><nobr>[${noTimeTask.salTaskType.typName}]</nobr></span>
                    </logic:notEmpty>
                	<a href="salTaskAction.do?op=salTaskDesc&stId=${noTimeTask.stId}" target="_blank">
                    ${noTimeTask.stTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="noTimeTask" property="stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="noTimeTask" property="stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="noTimeTask" property="stLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${noTimeTask.stName}</span>
					<% count++; %>
               </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='noTimeTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办工作</div>
        </logic:empty>
        
        <div class="modTitle">过期</div>
        <logic:notEmpty name='timeOutTask'>
            <logic:iterate id='timeOutTask' name='timeOutTask'>
            	<div class="modCon">
                	<img src="images/content/blueCircle.gif"/>&nbsp;
                    <logic:notEmpty name="timeOutTask" property="salTaskType">
                        <span class="brown"><nobr>[${timeOutTask.salTaskType.typName}]</nobr></span>
                    </logic:notEmpty>
                	<a href="salTaskAction.do?op=salTaskDesc&stId=${timeOutTask.stId}" target="_blank">
                    ${timeOutTask.stTitle}
                    </a>
                    &nbsp;
                    <span title="优先级"><logic:equal value="0" name="timeOutTask" property="stLev"><span class="deepGray">低</span></logic:equal><logic:equal value="1" name="timeOutTask" property="stLev"><span class="orange">中</span></logic:equal><logic:equal value="2" name="timeOutTask" property="stLev"><span class="red">高</span></logic:equal></span>
                    <span title="发布人" class="deepGray">${timeOutTask.stName}</span>
                    <span class='gray small' title="结束日期"><nobr><label id="endDate1<%=count%>"></label>&nbsp;</nobr></span>
                     <script type="text/javascript">
						dateFormat('endDate1','${timeOutTask.stFinDate}',<%=count%>);
					</script>
					<% count++; %>
               </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='timeOutTask'>
            <div class="gray">&nbsp;&nbsp;未发布待办工作</div>
        </logic:empty>
        
        
  </body>
</html>

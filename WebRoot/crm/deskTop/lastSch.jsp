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
    
    <title>我的日程列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body {
			padding:8px;
			background-color:#fff;
			text-align:left;
			overflow:auto;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
 	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/oa.js"></script>
    </head>
  
  <body>
  	<div style="padding:2px; background-color:#fff; border-bottom:#dadada 1px solid;">
    	<logic:notEmpty name='curSchList'>
        	<div class="orange bold">今天的日程安排</div>
            <logic:iterate id='schedules' name='curSchList'>
                <div class="modCon">
                	<img src="images/content/blueArr.gif"/>&nbsp;
                    <logic:equal value='已完成' name='schedules' property='schState'>
                    	<logic:notEmpty name='schedules' property='schType'>
                    		<span class='brown'>[${schedules.schType.typName}]</span>
                        </logic:notEmpty>
                        <a href="javascript:void(0)" onClick="oaPopDiv(32,${schedules.schId},1);return false;">${schedules.schTitle}</a>
                        <span><img class="imgAlign" src='images/content/suc.gif' alt='已完成'/></span>
                   </logic:equal>
                   <logic:equal value='未完成' name='schedules' property='schState'>
                        <logic:notEmpty name='schedules' property='schType'>
                            <span class='brown'>[${schedules.schType.typName}]</span>
                        </logic:notEmpty><a href="javascript:void(0)" onClick="oaPopDiv(32,${schedules.schId},1);return false;">${schedules.schTitle}</a>
                        <span id='${schedules.schId}'><img class="imgAlign" src='images/content/execute.gif' border='0' style='cursor:pointer' onClick="modifySchState('${schedules.schId}')" alt='点击完成'/></span>
                        <span id='y${schedules.schId}' style='display:none'><img class="imgAlign" src='images/content/suc.gif' alt='已完成'/></span>
                    </logic:equal>
                    <span class='gray small'><nobr>${schedules.schStartTime}-${schedules.schEndTime}</nobr></span>
               </div>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='curSchList'>
            <span class="gray">&nbsp;&nbsp;今天没有日程安排</span>
        </logic:empty>
    </div>  
    <div style="padding:2px;">
  	<div class="gray bold" style="padding:3px; padding-top:8px;">七天内</div>
    <logic:notEmpty name='lastSchList'>
        <logic:iterate id='lastSch' name='lastSchList'>
            <div class="modCon">
                <img src="images/content/blueArr.gif"/>&nbsp;
                <logic:notEmpty name='lastSch' property='schType'>
                    <span class='brown'>[${lastSch.schType.typName}]</span>
                </logic:notEmpty>
                <a href="javascript:void(0)" onClick="oaPopDiv(32,'${lastSch.schId}',1);return false;">${lastSch.schTitle}</a>&nbsp;
                <span class='gray small'><nobr><label id="sdate1<%=count%>"></label>&nbsp;</nobr></span>
                <script type="text/javascript">
                    dateFormat('sdate1','${lastSch.schStartDate}',<%=count%>);
                </script>
                <% count++; %>
           </div>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='lastSchList'>
        <span class="gray">&nbsp;&nbsp;七天内没有待办日程</span>
    </logic:empty>
    </div>
  </body>
</html>

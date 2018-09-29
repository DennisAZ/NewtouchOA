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
    <title>最近报告列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
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
    	<logic:notEmpty name='appRepList' >
            <logic:iterate id='lastRep' name='appRepList'>
                <div class="modCon">
                	<img src="images/content/blueArr.gif"/>
                    <logic:notEmpty name="lastRep" property="report.repType">
                        <span class="brown">[${lastRep.report.repType.typName}]</span>
                    </logic:notEmpty><a href="messageAction.do?op=showRepInfo&repCode=${lastRep.report.repCode}&mark=appRepInfo" target="_blank">${lastRep.report.repTitle}</a>
                   	<span class="gray">由&nbsp;<span class="deepgray">${lastRep.report.repInsUser}</span>&nbsp;提交于&nbsp;<span class="small"><label id="sendDate<%=count%>"></label></span></span>
               </div>
               <script type="text/javascript">
                    dateFormat('sendDate','${lastRep.rrlDate}',<%=count%>);
                </script>
            	<% count++; %>
            </logic:iterate>
        </logic:notEmpty>
        <logic:empty name='appRepList'>
            <div class="gray">没有待阅报告</div>
        </logic:empty>
  </body>
</html>

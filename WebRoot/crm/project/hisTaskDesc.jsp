<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>项目日志详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />    
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    
    <style type="text/css">
    	body {
			background:#fff;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/pro.js"></script>

</head>
  
  <body>
    <logic:notEmpty name="proTask">
    	<div class="inputDiv">
            <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                        <th class="descTitleLNoR">日志主题：</th>
                        <th class="descTitleR" colspan="3">
                             ${proTask.prtaTitle}&nbsp;
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>对应项目：</th>
                        <td colspan="3">${proTask.project.proTitle}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>对应子项目：</th>
                        <td colspan="3">${proTask.proStage.staTitle}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>日志内容：</th>
                        <td colspan="3" style="height:100px; vertical-align:top">&nbsp;${proTask.prtaDesc}</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>提交人：</th>
                        <td>${proTask.prtaName}&nbsp;</td>
                        <th>提交时间：</th>
                        <td><span id="inpDate"></span>&nbsp;</td>
                    </tr>
                </tbody>
                <script type="text/javascript">
                     removeTime('inpDate','${proTask.prtaRelDate}',2);
                </script>
            </table>
        </div>
  </logic:notEmpty>
  <logic:empty name="proTask">
    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该项目日志已被删除！</div>
   </logic:empty>
  </body>
</html>

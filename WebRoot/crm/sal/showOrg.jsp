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
    <title>部门管理</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	#rightDesc{
			padding:10px;
			float:right;
			text-align:left;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/hr.js"></script>
    <script type="text/javascript" >
     
	 createProgressBar();	
	 window.onload=function(){
	 	
		closeProgressBar();
	 }
	</script>
</head>
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 部门设置</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='empAction.do?op=findForwardOrg'">
                                部门设置
                            </div>
                        </div>
                    </th>
                    <td>
                        <a href="javascript:void(0)" onClick="addDivNew(1);return false;" class="newBlueButton">添加新部门</a>
                    </td>
                </tr>
            </table>
			<div id="listContent">
				<table class="normal" align="left" cellpadding="0" width="30%" cellspacing="0">
					<tr>
						<td style="padding:15px; padding-right:10px; vertical-align:top">
                       	<script type="text/javascript">createIfmLoad('ifmLoad1');</script>
                        <iframe frameborder="0" style="border:#CCCCCC 1px solid;" onload="loadAutoH(this,'ifmLoad1')" scrolling="no" width="100%" src="empAction.do?op=getAllOrg&mark=orgTree"></iframe></td>
                    </tr>
                </table>
                <div id="rightDesc">
                	<script type="text/javascript">createIfmLoad('ifmLoad2');</script>
                	<iframe id="rightList" onload="loadAutoH(this,'ifmLoad2')" frameborder="0" scrolling="auto" width="100%" style="height:500px" src="empAction.do?op=getAllOrg&mark=orgList"></iframe>
                </div>
        	</div>
  		</div> 
	 </div>
  </body>
  	<script type="text/javascript">loadTabTypeWidth();</script>
</html>

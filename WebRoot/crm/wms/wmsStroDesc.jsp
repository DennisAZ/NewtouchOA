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
    
    <title>查看仓库</title>
    <link rel="shortcut icon" href="favicon.ico"/> 
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
  </head>
  
  <body>
  	<div class="inputDiv">
  	<logic:notEmpty name="wmsStro">
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleLNoR">仓库名称：</th>
                    <th class="descTitleR" colspan="3">${wmsStro.wmsName}&nbsp;</th>				
                </tr>
            </thead>
			<tbody>
            	<tr>
                    <th>仓库编号：</th>
                    <td>${wmsStro.wmsCode}&nbsp;</td>	
                    <th>仓库类别：</th>
                    <td>${wmsStro.wmsStroType.typName}&nbsp;</td>			
                </tr>
                <tr>
                    <th>仓库地点：</th>
                    <td colspan="3">${wmsStro.wmsLoc}&nbsp;</td>				
                </tr>
                <tr>
                    <th>负责账号：</th>
                    <td>${wmsStro.limUser.userSeName}&nbsp;</td>
                    <th class="orangeBack">当前总库存：</th>
                    <td>${count}&nbsp;</td>
                </tr>
                <tr>
                    <th>备注：</th>
                    <td colspan="3">${wmsStro.wmsRemark}&nbsp;</td>
                </tr>
                <tr class="noBorderBot">
                    <th>创建时间：</th>
                    <td colspan="3">&nbsp;${wmsStro.wmsCreDate}</td>
                </tr>
            </tbody>	
	  	</table>
    </logic:notEmpty>
    <logic:empty name="wmsStro">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该仓库已被关闭</div>
	</logic:empty>
    </div>
  </body>
</html>

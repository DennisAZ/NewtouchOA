<%@ page language="java" pageEncoding="UTF-8"%>
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
    <title>最近被关注的联系人</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css">
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:center;
		}

		#todayBir td,#lastBir td {
			text-align:center;
			height:20px;
		}
		#todayBir ul,#lastBir ul {
			padding:0px;
			margin:0px;
		}
		#todayBir li,#lastBir li {
			padding:2px;
		}
		#todayBir span,#lastBir span{
			text-align:left;
		}
		
		#todayBir{
			padding:5px;
			border:#fbb01f 1px dashed;
			background-color:#fdf9e8;
			margin:4px;
			text-align:center;
		}
		#lastBir {
			width:100%;
			text-align:left;
			padding-left:5px;
			padding-right:5px;
		}
		#lastBir li {
			border-bottom:#999999 1px dotted;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
   	<script type="text/javascript" src="crm/js/common.js"></script>
  </head>
  
  <body>
  	<div class="tipBox">
        <div class="tipTitle">
            &nbsp;&nbsp;&nbsp;<img src="crm/images/content/cake.gif" alt="最近关注的联系人" class="imgAlign" />&nbsp;&nbsp;最近共有&nbsp;<span class="bigger">${num2}</span>&nbsp;个联系人需要关注
        </div>
        <div class="divWithScroll2" style="height:166px;">
            <table class="normal" cellpadding="0px" cellspacing="0px" width="100%">
                <tr>
                    <td width="50%" valign="top">
                        <div id="todayBir">
                            <div class="orange bold" style="height:25px">今天需要关注的有&nbsp;<span class="bigger">${num}</span>&nbsp;个</div>
                            <logic:notEmpty name="conBirList">
                                <ul class="ulHor">
                                    <logic:iterate id="conBirList" name="conBirList" >
                                    <li style="vertical-align:baseline;">
                                    &nbsp;<a href="customAction.do?op=showContactInfo&id=${conBirList.conId}" target="_blank">${conBirList.conName}</a>
                                    <span class="brown">[${conBirList.conType}]</span>
                                    </li>
                                    </logic:iterate>
                                </ul>
                            </logic:notEmpty>
                            <div class="HackBox"></div>
                        </div>
                    </td>
                    <td style="vertical-align:top">
                        <div id="lastBir">
                            <logic:empty name="conLastBirList">
                                <div align="center" class="gray" style="padding-top:30px">
                                    最近十天需要关注的联系人
                                </div>
                            </logic:empty>
                            <logic:notEmpty name="conLastBirList">
                                <ul class="listtxt">
                                    <li class="gray" style="border:0px; text-align:left;">最近十天内关注的联系人</li>
                                    <logic:iterate id="conLastBirList" name="conLastBirList" >
                                        <li>
                                        	<img src="crm/images/content/blueCircle.gif"/>&nbsp;
                                        	<label id="lastbir<%=count%>" class="brown"></label>
                                            &nbsp;&nbsp;<a href="customAction.do?op=showContactInfo&id=${conLastBirList.conId}" target="_blank">${conLastBirList.conName}</a>
                                            <span class="brown">[${conLastBirList.conType}]</span>
                                        </li>
                                        <script type="text/javascript">
                                            dateFormat3('lastbir','${conLastBirList.conBir}',<%=count%>);
                                        </script>
                                        <% count++; %>
                                    </logic:iterate>
                                </ul>
                            </logic:notEmpty>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
  </body>
</html>

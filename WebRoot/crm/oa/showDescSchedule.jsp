<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>日程安排详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body {
			background:#fff;
		}
		div {
			padding:5px;
		}
		table {
			width:96%;
			border-collapse:collapse;
		}
		td {
			padding:0px;
		}
		.noBorderBot .gray {
			border:0;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
		}
    </script>
  </head>
  
  <body>
    	<logic:notEmpty name="schedule">
        	<div>
                <table class="dashTab inputForm">
                	<tbody>
                	<tr>
                		<th>对应客户：</th>
                		<td class="blue mlink" colspan="3">
                		   <span id="cusName">
                                <logic:notEmpty name="schedule" property="cusCorCus">
                                    <a href="customAction.do?op=showCompanyCusDesc&corCode=${schedule.cusCorCus.corCode}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${schedule.cusCorCus.corName}</a>
                                </logic:notEmpty>&nbsp;
                            </span>
                		</td>
                	</tr>
                    <tr>
                        <th>日程类别：</th>
                        <td>${schedule.schType.typName}&nbsp;</td>
                        <th>日程状态：</th>
                        <td>${schedule.schState}&nbsp;</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>时间：</th>
                        <td colspan="3"><span id="startDate"></span>&nbsp;&nbsp;${schedule.schStartTime}-${schedule.schEndTime}</td>
                    </tr>
                    <tr>
                        <td colspan="4" style=" vertical-align:top; width:100%;height:180px;border:#fcd796 1px solid;background:#fefed6;">
                        	${schedule.schTitle }&nbsp;
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                        <th class="gray">创建时间：</th>
                        <td class="gray"><span id="insDate"></span>&nbsp;</td>
                        <th class="gray">修改时间：</th>
                        <td class="gray"><span id="updDate"></span>&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
                <script type="text/javascript">
					$("startDate").innerHTML='${schedule.schStartDate}'.substring(0,10);
					removeTime("insDate","${schedule.schDate}",2);
					removeTime("updDate","${schedule.schUpdDate}",2);
				</script>
            </div>
       	</logic:notEmpty>
        <logic:empty name="schedule">
            <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该日程已被删除</div>
        </logic:empty>
  </body>
</html>

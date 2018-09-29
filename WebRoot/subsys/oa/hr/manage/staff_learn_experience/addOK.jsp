<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<script type="text/javascript">
function gotos(){
	window.location.href= contextPath + "/subsys/oa/hr/manage/staff_learn_experience/new.jsp";
	
}
</script>
<title>新建学习经历信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>

</head>
<body topmargin="5" class="bodycolor">
<div id="noData" align=center >
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">
成功增加学习经历信息！
            </td>
        </tr>
    </tbody>
</table>
</div>
   <br><center><input type="button" onclick="gotos();" value="返回" class="BigButton"></center>
</body>
</html>
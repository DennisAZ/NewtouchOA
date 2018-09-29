<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<body>
<div id="noFormData" align=center style='display:'>
<table class="MessageBox" width="300" >
    <tbody>
        <tr>
            <td class="msg info">操作成功！</td>
        </tr>
    </tbody>
</table>
<input type="button" value="返回" onclick="history.go(-1)">
</div>

</body>
</html>
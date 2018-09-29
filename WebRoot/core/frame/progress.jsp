<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta name="author" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="keywords" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="description" content="<%=StaticData.SOFTKEYWORD%>" />
<link rel="stylesheet" type="text/css" href="<%=cssPath %>/style.css" />
<style type="text/css">
*{padding:0px;margin:0px;}
.progressBar {background-color:#FFFFFF;border:1px solid #0000FF;text-align:center;padding:5px;font-size:14px;}
</style>
<script type="text/javascript">
</script>
</head>
<body>
<table align="center">
  <tr>
    <td align="center" height="100">
      &nbsp;
    </td>
  </tr>
  <tr>
    <td align="center" class="progressBar">
      <img src="<%=imgPath %>/frame/loading.gif"/>&nbsp;&nbsp;正在进入请稍后...
    </td>
  </tr>
</table>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<title>提示</title>
</head>
<style type="text/css" >
  .MessageBox {
    border: 1px dashed #919191;
    margin-bottom: 15px;
    margin-top: 100px;
    height: 227px;
    width: 400px;
   }
   
.MessageBox td.msg {
    color: #000000;
	padding-left:150px;
   
}
.MessageBox td.msg h4.title {
    border-bottom: 1px dotted #B8D1E2;
    color:#3399FF;
    font-size: 18pt;
    margin-bottom: 10px;
}
.MessageBox td.msg div.content {
    border: medium none;
    color: #FF6633;
}

.MessageBox td.info {
    background: url("css/images/exit.gif") no-repeat scroll -5px -10px transparent;
    font-size: 11pt;
}
</style>
<body topmargin="5">

<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">很抱歉，该链接已经过期！</div>
    </td>
  </tr>
</table>
</html>
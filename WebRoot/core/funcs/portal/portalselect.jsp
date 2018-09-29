<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<link  rel="stylesheet"  href  ="<%=cssPath  %>/style.css">
<script  type="text/javascript">
</script>
</head>

<body onLoad="">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small" style="margin:5px 0px;">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/control_theme.gif" align="abstop"/><span class="big3"> 门户选择</span><br>
    </td>
  </tr>
</table>
<br/>
<form action="<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/setTheme.act"  method="post" name="form1" id="form1" enctype="multipart/form-data">
<table class="TableBlock" width="80%" cellpadding="1" align="left">
    <tr>
      <td colspan=2 class="TableHeader">可用门户</td>
    </tr>
    <tr>
      <td colspan=2 class="TableData">
        <div style="text-align: center;float: left;padding: 2px;">
          <span>部门主管</span><br>
          <img width="300" height="300" src="<%=contextPath %>/core/frame/webos/styles/style1/images/bg6.jpg">
          </img><br>
          <input name="sel" type="radio"/>
        </div>
        <div style="text-align: center;float: left;padding: 2px;">
          <span>员工</span><br>
          <img width="300" height="300" src="<%=contextPath %>/core/frame/webos/styles/style1/images/bg7.jpg">
          </img><br>
          <input name="sel" type="radio"/>
        </div>
      </td>
    </tr>
    <tr class="TableHeader" colspan=2 >
      <td>
        自定义
      </td>
    </tr>
    <td>
      <a href="desktopTable.jsp">新建门户</a>
    </td>
  </table>
</form>

<br>

</body>
</html>
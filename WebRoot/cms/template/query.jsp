<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模板查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/cms/template/js/templateLogic.js"></script>
<script type="text/javascript">
function doInit(){
  getTemplateType();
}

function doSubmit(){
	var query = $("form1").serialize(); 
	location.href = "<%=contextPath %>/cms/template/search.jsp?" + query
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 模板查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
  <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData"> 模板名称：</td>
      <td class="TableData">
        <input type="text" name="templateName" id="templateName" size="12" maxlength="10" class="BigInput" value="" >      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 文件名：</td>
      <td class="TableData">
        <input type="text" name="templateFileName" id="templateFileName" size="12" maxlength="10" class="BigInput" value="" >
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData" width="100"> 模板类型：</td>
      <td class="TableData" >
        <select name="templateType" id="templateType" title="模板类型可在“系统管理”->“分类码管理”模块设置。">
          <option value="">所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td> 
   </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</body>
</html>
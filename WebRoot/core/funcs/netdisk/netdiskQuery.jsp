<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>     
<%
	String pathId = request.getParameter("DISK_ID");
	String seqIdStr = request.getParameter("seqId");
	int seqId = 0;
	if (seqIdStr != null) {
		seqId = Integer.parseInt(seqIdStr);
	}	


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件检索</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">

function checkForm(){
	if($("fileName").value == "" && $("key").value == ""){
		alert("请指定至少一个查询条件！");
	  return (false);
	}
	return true;
}

function doInit(){
	$("fileName").focus();
}


</script>


</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="middle"><b><span class="Big1"> <%=pathId %> - 文件检索</span></b><br>
    </td>
  </tr>
</table>
 
<br>
 
<form action="<%=contextPath %>/core/funcs/netdisk/netdiskSearch.jsp" name="form1" id="form1" onsubmit="return checkForm();">
<table class="TableBlock" width="450" align="center">
  <tr class="TableData">
      <td nowrap align="center">文件名包含文字：</td>
      <td nowrap><input type="text" name="fileName" id="fileName" class="BigInput" size="20"></td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">文件内容包含文字：</td>
      <td nowrap><input type="text" name="key" id="key" class="BigInput" size="20"><br>  默认只检查txt及html文件      </td>
  </tr>
  <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="hidden" name="DISK_ID" id="DISK_ID" value="<%=pathId %>">
          <input type="hidden" name="seqId"  id="seqId" value="<%=seqId %>">
          <input type="hidden" name="ORDER_BY" value="NAME">
          <input type="hidden" name="ASC_DESC" value="4">
          <input type="hidden" name="start" value="0">
          <input type="submit" value="查询" class="BigButton" title="进行文件查询">&nbsp;&nbsp;
          <input type="button" value="返回" class="BigButton" onclick="history.back();">
      </td>
  </tr>
</table>
</form>






</body>
</html>
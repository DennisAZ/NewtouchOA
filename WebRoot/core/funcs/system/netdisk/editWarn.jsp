<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String flag=request.getParameter("flag");
	String diskName=request.getParameter("diskName");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改共享目录</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript">
var flag='<%=flag%>';
//alert("flag:"+flag +"  disName:"+'<%=diskName%>');
function doInit(){
	if(flag=="isHaveName"){
		$("diskName").innerHTML='<%=diskName%>';
		$("isHaveName").style.display='';	
	}
	if(flag=="createFlag"){
		$('createWarn').style.display='';
	}
}

</script>
</head>
<body onload="doInit();">
<div id="isHaveName" style="display: none">
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">指定的开放范围中名为 <span id="diskName"></span> 的共享目录已存在</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="history.back();"></center>
</div>

<div id="createWarn" style="display: none">
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg error">
      <h4 class="title">错误</h4>
      <div class="content" style="font-size:12pt">共享目录路径不正确，且不能新建该目录！</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="history.back();"></center>
</div>



</body>
</html>
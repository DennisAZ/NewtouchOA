<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,raw.ljf.curd.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑CODE_CLASS</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<%
  YHCodeClass code = (YHCodeClass)request.getAttribute("codeClass");
%>
<script type="text/javascript">
function goback() {
  window.location.href="/yh/raw/ljf/html/list.html";
}

function commitItem() {
  var url = "/yh/raw/ljf/curd/YHUpdateTableAct/updateTable.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form2")));
  
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
    parent.navigateFrame.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function init() {
  var arr = document.getElementsByName("classLevel");
  
  for(var i=0; i<arr.length; i++) {
	if("<%=code.getClassLevel() %>"==arr[i].value) {
		arr[i].checked="true";
    }
  }
}
</script>
</head>
<body onload="init()">
<form method="post" name="form2" id="form2">
  <h2>修改CODE_CLASS的信息</h2>
  <input type="hidden" name="classNofirst" value="<%=code.getClassNo() %>"/><br/>
  classNo:&nbsp;&nbsp;&nbsp;<input type="text" name="classNo" value="<%=code.getClassNo() %>"/><br/>
  sortNo:&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="sortNo" value="<%=code.getSortNo() %>"/><br/> 
  classDesc:&nbsp;<input type="text" name="classDesc" value="<%=code.getClassDesc() %>"/><br/>

  classLevel:1:<input type="radio"  name="classLevel" value="1"/>
  			0:<input type="radio" name="classLevel" value="0"/>
 
  <br/>
  <input type="button" value="提交"  onclick="commitItem()">
  <input type="button" value="返回" onclick="goback()">
</form>
</body>
</html>
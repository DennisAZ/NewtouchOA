<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,raw.ljf.curd.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加条目</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function goback() {
  window.location.href="/yh/raw/ljf/html/list.html";
}

function check() {
  var cntrl = document.getElementById("classNo");
  if (!cntrl.value) {
	alert("classNo不能为空！");
	cntrl.focus();
	return false;
  }
  cntrl = document.getElementById("sortNo");
  if (!cntrl.value) {
	alert("sortNo不能为空！");
	cntrl.focus();
	return false;
  }
  return true;
}

function commitItem() {
  if(!check()){
    return;
  }
  var url = "/yh/raw/ljf/curd/YHAddTableAct/addTable.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
    parent.navigateFrame.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body>
<form name="form1" id="form1" method="post">
  <h2>请输入CODE_CLASS的信息</h2>
  classNo:&nbsp;&nbsp;&nbsp;<input type="text" name="classNo" id="classNo" value=""/><br/>
  sortNo:&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="sortNo" value=""/><br/>
  classDesc:&nbsp;<input type="text" name="classDesc" value=""/><br/>
  classLevel:1:<input type="radio" name="classLevel" value="1" checked/>
  			0:<input type="radio" name="classLevel" value="0"/>
  <br/>
  <input type="button" value="提交" onclick="commitItem()">
  <input type="button" value="返回" onclick="goback()">
</form>
</body>
</html>
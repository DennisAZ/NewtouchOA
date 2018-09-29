<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String classNo = request.getParameter("classNo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加CLASS_ITEM</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
function doInit() {
  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "classNo", tableName: "CODE_CLASS", codeField: "CLASS_NO", nameField: "CLASS_DESC", value: "<%=classNo%>", isMustFill: "1", filterField: "", filterValue: "", order: "", reloadBy: "", actionUrl: ""});
  mgr.loadData();
  mgr.bindData2Cntrl();
}

function check() {
  if(document.getElementById("classCode").value=="") {
	alert("classCode不能为空！");
	document.getElementById("classCode").focus();
	return false;
  }

  if(document.getElementById("sortNo").value=="") {
  	alert("sortNo不能为空！");
  	document.getElementById("sortNo").focus();
  	return false;
  }

  if(document.getElementById("classDesc").value=="") {
  	alert("classDesc不能为空！");
  	document.getElementById("classDesc").focus();
  	return false;
  }
  return true;
}

function commitItem() {
  if(!check()){
    return;
  }
  var url = "/yh/raw/ljf/curd/YHAddSonTableAct/addSonTable.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form4")));
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function goback() {
  window.location.href="/yh/raw/ljf/curd/YHSonTableAct/getSonTable.act?classNo=<%=classNo%>";
}

</script>
</head>
<body onload="doInit()">
<form method="post" name="form4" id="form4" >
  <h2>请输入CODE_ITEM的信息</h2>
      主分类:&nbsp;&nbsp;&nbsp;<select name="classNo" id="classNo"></select><br></br>
  classCode:<input type="text" id="classCode" name="classCode"  value=""/><br/>
  sortNo:&nbsp;&nbsp;&nbsp;<input type="text" id="sortNo" name="sortNo" value=""/><br/>
  classDesc:<input type="text" id="classDesc" name="classDesc" value=""/><br/>

  <input type="button" value="提交" onclick="commitItem()">
  <input type="button" value="返回" onclick="goback()">
</form>
</body>
</html>
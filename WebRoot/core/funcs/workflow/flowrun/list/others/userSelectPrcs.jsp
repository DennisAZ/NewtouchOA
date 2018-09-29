<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%
 String runId = request.getParameter("runId");
 String prcsId = request.getParameter("prcsId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择人员</title>
<link rel="stylesheet" href="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var runId = "<%=runId%>";
var prcsId = "<%=prcsId%>";

var rootUrl = contextPath + "/yh/core/funcs/workflow/act/YHDelegateAct";
function doInit() {
  loadData();
}
function loadData(text){
  var par = "runId=" + runId + "&prcsId=" + prcsId;
  if (text) {
	par += "&search=" +text;
  }
  var url = rootUrl + "/getPrcsOpUsers.act";
  var json = getJsonRs(url , par);
 if (json.rtState == "0") {
	$('userList').update("");
	var users = json.rtData;
	if (users.length >0 ) {
	  users.each(function(data) {addRow(data);});
	} else {
	  $('userList').update("<tr><td>没有符合条件的人员</td></tr>");
	}
  } else {
	alert(json.rtMsrg);
	return ;
  }
}
function addRow(data) {
  var tr = new Element("tr");
  $('userList').appendChild(tr);
  var td = new Element("td" , {'class':'TableData'}).update(data.userName + "(" + data.dept + ")");
  addClickHander( td , data.userId , data.userName);
  tr.appendChild(td);
}
function addClickHander( td , userId , userName) {
  td.onclick = function() {
	addOther(userId , userName);
  }
}
function search() {
  loadData($F("userNameStr"));
}
parentWindow = window.dialogArguments;
function addOther(userId,userName){
  parentWindow.$('user').value = userId;
  parentWindow.$('userDesc').value = userName;
  window.close();
}
</script>
</head>
<body onload="doInit()">
<div style="padding-top:4px;text-align:center">
<b>姓名</b>
  <input type="text" name="userNameStr" id="userNameStr" size="8" class="BigInput">
  <input type="button" onclick="search()" value="查询" class="SmallButtonW" title="模糊查询">

<hr style="height:1px;background-Color:#FFF">

<table class=TableList width=100%>
<tr class="TableHeader"><td>选择人员</td></tr>
<tbody id="userList"></tbody>
</table>

</div>
</body>
</html>
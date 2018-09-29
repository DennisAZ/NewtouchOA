<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if(treeId == null){
	treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var deptParent = "<%=deptParent%>";
var treeId = "<%=treeId%>";

var TO_ID = "<%=TO_ID%>";
var TO_NAME = "<%=TO_NAME%>";
function doInit(){
  var parentWindowObj = window.parent.dialogArguments;
  var pramValue = trim(parentWindowObj.document.getElementById(TO_NAME).value);
  if(pramValue != ""){
    var URL = "<%=contextPath%>/core/funcs/userpriv/userOther.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
    $('user').src = URL;
  }
}
</script>
</head>
<frameset rows="300,*" frameborder="0" onload="doInit()" >	
 <frameset cols="200,*" frameborder="0">
	<frame id="dept" scrolling="no" name="dept" src="<%=contextPath%>/core/funcs/userpriv/dept.jsp?TO_ID=<%=TO_ID%>&TO_NAME=<%=TO_NAME%>" />
	<frame id="user" name="user" />
    </frameset>
    <frame name="dept" src="<%=contextPath%>/core/funcs/userpriv/control.jsp" />
</frameset>
</html>

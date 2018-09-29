<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String MODULE_ID = request.getParameter("MODULE_ID");
  String FORM_NAME = request.getParameter("FORM_NAME");
  String MANAGE_FLAG = request.getParameter("MANAGE_FLAG");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.menulines{}
</style>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
</head>
<script src="/inc/js/utility.js"></script>
<script src="/inc/js/user_select.js"></script>
<script Language="JavaScript">
//var parent_window = getOpenner();
var to_id = "<%=TO_ID%>";
var to_name = "<%=TO_NAME%>";

function click_tr()
{
   var theEvent = window.event || arguments[0];
   var el = theEvent.srcElement || theEvent.target;
   if(el.tagName.toUpperCase()=="TD" && el.id)
      click_user(el.id);
}
function get_selected()
{
   var TO_ID_STR = to_id.value;
   if(TO_ID_STR)
   {
      var args="TO_ID_STR="+encodeURIComponent(TO_ID_STR);
      _post("selected.php",args, show_selected);
   }
   else
   	body.innerHTML="<br>暂无选择人员";
}
function show_selected(req)
{
   if(req.responseText != "")
      body.innerHTML=req.responseText;
   else
   	  body.innerHTML="<br>暂无选择人员";
   begin_set();
}
</script>
<body class="" topmargin="0" leftmargin="0" onload="get_selected();">
<div id="body" align="center" class="small"></div>
</body>
</html>
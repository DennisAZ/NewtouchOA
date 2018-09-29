<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String deptId = request.getParameter("deptId");
  if(deptId == null){
    deptId = "";
  }
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
var deptId = "<%=deptId%>";
function doInit(){

}
</script>
</head>
 <frameset rows="250,*" frameborder="no" border="0" framespacing="0">
	<frame id="dept" scrolling="auto" name="dept" src="<%=contextPath%>/core/funcs/dept/deptedittree.jsp?deptId=<%=deptId%>" scrolling="auto" />
    <frame name="dept" src="<%=contextPath%>/core/funcs/dept/control.jsp" />
 </frameset>
</html>

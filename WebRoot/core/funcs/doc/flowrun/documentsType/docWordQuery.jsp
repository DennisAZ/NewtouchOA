<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script>
function searchPlanNoInfo(){	 
	//获取上个页面的窗口对象
  var parent_window = parent.dialogArguments;
  var condition = $("keyWord").value;  
  window.parent.frames["plannoinfo"].getTrainingAjaxFunc(condition);
}
</script>
</head>

<body bgcolor="#E8E8E8" topmargin="5">

<center>
 <form method="post" action=""  name="form1" id="form1">
文件字名称：
  <input type="text" name="keyWord" id="keyWord" size="10" class="BigInput">
  <input type="button" name="search" onclick="javascript:searchPlanNoInfo();return false;" value="模糊查询" class="BigButton">
  &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" onclick="window.close()" value="关闭" class="BigButton">
 </form>
</center>
</body>
</html>
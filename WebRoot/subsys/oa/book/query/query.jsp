<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
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
	function searchBookNo(){	 
	  var parent_window = parent.dialogArguments;
    var condition = $("bookNo").value;  
    var userId =  parent_window.form1.toId.value;	 
    if(! userId){
      userId = "";
    }
    window.parent.frames["bookinfo"].bookAjax(condition, userId);
	}
</script>
</head>

<body bgcolor="#E8E8E8" topmargin="5">

<center>
 <form method="post" action=""  name="form1">
  编号/书名：
  <input type="text" name="bookNo" id="bookNo" size="10" class="BigInput">
  <input type="button" name="search" onclick="javascript:searchBookNo();return false;" value="模糊查询" class="BigButton">
 </form>
</center>

</body>
</html>
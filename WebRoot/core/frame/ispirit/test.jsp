<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>发布新闻</title>
<script type="text/javascript">


</script>
</head>
<body onload="">
<form id="formFile" action="<%=contextPath %>/yh/core/funcs/system/ispirit/n12/group/act/YHImGroupAct/uploadFile.act" method="post" enctype="multipart/form-data" >
  <input id="btnFormFile" name="btnFormFile" type="submit" ></input>
  <input type="file" name="ATTACHMENT"> 
  
     <input type="text" name="MSG_CONTENT" value="文件"> 
     <input type="text" name="MSG_GROUP_ID" value="1"> 
      <input type="text" name="MIX_FLAG" value="2"> 
  </form>
</body>
</html>
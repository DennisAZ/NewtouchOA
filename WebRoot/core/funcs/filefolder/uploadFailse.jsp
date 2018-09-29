<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String seqId = request.getParameter("seqId");
	String actionFlag = request.getParameter("actionFlag");
	String contentId = request.getParameter("contentId");
	if(YHUtility.isNullorEmpty(seqId)){
		seqId = "0";
	}
	if(YHUtility.isNullorEmpty(contentId)){
		contentId = "0";
	}
	if(YHUtility.isNullorEmpty(actionFlag)){
		actionFlag = "";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传失败</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
</head>
<body>
<div id="remindDiv" style="display: ">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg error">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">可能文件过大，上传失败！</div>
    </td>
  </tr>
</table>
<br><center>
<%
	if("edit".equalsIgnoreCase(actionFlag)){

%>
	<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/core/funcs/filefolder/edit.jsp?seqId=<%=seqId %>&contentId=<%=contentId %>';">
<%
	}else if("new".equalsIgnoreCase(actionFlag)){
%>
	<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/core/funcs/filefolder/new/newFile.jsp?actionFlag=<%=actionFlag %>&contentId=<%= contentId%>&seqId=<%=seqId %>';">
<%
	}else{
%>	
	<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/core/funcs/filefolder/folder.jsp?seqId=<%=seqId %>';">
<%
	}
%>
	
	
</center>
</div>

</body>
</html>
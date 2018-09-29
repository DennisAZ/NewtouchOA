<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String commentId = request.getParameter("commentId");
  if(commentId == null){
    commentId = "";
  }
  String replyId = request.getParameter("replyId");
  if(replyId == null){
    replyId = "";
  }
  String type = request.getParameter("type");
  if(type == null){
    type = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<title>Insert title here</title>
<script type="text/javascript">
function doBack(){
  location = "<%=contextPath %>/core/funcs/diary/comment/replyComment.jsp?commentId=<%=commentId %>&type=<%=type %>&replyId=<%=replyId %>";
}
</script>
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" HEIGHT="20" width="20" align="absmiddle"><span class="big3"> 回复点评</span>
    </td>
  </tr>
</table>
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">保存成功</div>
    </td>
  </tr>
</table>
<script language=JavaScript> 
   window.opener.location.reload();    
</script>
<br><br>
<center> 
<input type="button" value="返回" class="SmallButton" onClick="doBack()">
&nbsp;&nbsp;<input type="button" value="关闭" class="SmallButton" onClick="window.close();">
</center> 
</body>
</html>
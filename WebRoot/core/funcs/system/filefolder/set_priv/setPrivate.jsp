<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
	  seqId="";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件柜设置</title>
<script type="text/javascript">

//alert('seqId:<%=seqId%>');
</script>


</head>

<frameset rows="30"  cols="*" frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="menu_top" scrolling="no" noresize src="menuTop.jsp?seqId=<%=seqId %>" frameborder="0">
    
</frameset>

</html>
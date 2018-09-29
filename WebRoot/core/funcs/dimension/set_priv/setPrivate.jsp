<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %> 
    
<%
	String seqId=request.getParameter("seqId");
    String topSeqId = request.getParameter("topSeqId");
	if(seqId==null){
	  seqId="";
	}
	if(topSeqId==null){
	  topSeqId="";
	}
	String ownerPriv = request.getParameter("ownerPriv");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件柜设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">

//alert('seqId:<%=seqId%>');


</script>


</head>
<%if(ownerPriv.equals("1")){ %>
<frameset rows="30"  cols="*" frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="menu_top" scrolling="no" noresize src="menuTop.jsp?seqId=<%=seqId %>&topSeqId=<%=topSeqId %>" frameborder="0">
    
</frameset>
<%}else{ %>
<table class="MessageBox" align="center" width="360">
  <tr>
    <td class="msg warning">
      <h4 class="title">警告</h4>
      <div class="content" style="font-size:12pt">您无该维度的设置权限</div>
    </td>
  </tr>
</table>

<%} %>
</html>
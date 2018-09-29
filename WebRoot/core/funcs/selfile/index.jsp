<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String ext_filter=request.getParameter("EXT_FILTER");
	if(ext_filter==null){
		ext_filter="";
	}
	
	String div_id=request.getParameter("DIV_ID");
	if(div_id==null){
		div_id="";
	}
	String dir_field=request.getParameter("DIR_FIELD");
	if(dir_field==null){
		dir_field="";
	}
	String name_field=request.getParameter("NAME_FIELD");
	if(name_field==null){
		name_field="";
	}
	String type_field=request.getParameter("TYPE_FIELD");
	if(type_field==null){
		type_field="";
	}
	String multi_select=request.getParameter("MULTI_SELECT");
	if(multi_select==null){
		multi_select="";
	}
	

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择文件</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<script type="text/javascript">
//alert("ext_filter:"+'<%=ext_filter%>' +"  div_id:"+'<%=div_id%>' +"  dir_field:"+ '<%=dir_field%>' +"  name_field:"+ '<%=name_field%>' +"  type_field:"+ '<%=type_field%>' +"  multi_select:"+ '<%=multi_select%>');

</script>
<frameset rows="*"  cols="180,*" frameborder="YES" border="0" framespacing="0" id="frame2">
   <frame name="file_tree" scrolling="auto" src="left.jsp?EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>" frameborder="YES">
   <frame name="file_main" scrolling="auto" src="selected.jsp?EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>" frameborder="YES">
</frameset>
</html>
<%@ page language="java" contentType="text/xml; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="raw.ljf.YHCategory,java.util.*" %>
<?xml version="1.0" encoding="UTF-8"?>
<Categorys>
 <%
 	List<YHCategory> catList = (ArrayList<YHCategory>)request.getAttribute("catList");
	for(YHCategory t : catList){	
 %>
<Category>
<title><%=t.getTitle()%></title>
<imgUrl><%=t.getImgUrl()%></imgUrl>
</Category>
<%
}
%>
</Categorys><br></br>
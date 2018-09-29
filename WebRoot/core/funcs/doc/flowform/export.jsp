<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.net.URLEncoder" %>
<%
String formName = (String)request.getAttribute("formName");
formName = URLEncoder.encode(formName, "UTF-8");
String printModel = (String)request.getAttribute("printModel");
response.setContentType("text/plain");
response.setHeader("Cache-control","private");
response.setHeader("Accept-Ranges","bytes");
response.setHeader("Accept-Length",printModel.length() + "");
response.setHeader("Content-Disposition", "attachment;filename="+formName+".html");
%>
<%=printModel%>
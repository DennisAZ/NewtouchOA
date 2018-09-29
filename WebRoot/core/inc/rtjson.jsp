<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="yh.core.global.YHActionKeys"%>

<%
String rtState = (String)request.getAttribute(YHActionKeys.RET_STATE);
String rtMsrg = (String)request.getAttribute(YHActionKeys.RET_MSRG);
String rtData = (String)request.getAttribute(YHActionKeys.RET_DATA);
if (rtState == null) {
  rtState = "0";
}
if (rtMsrg == null) {
  rtMsrg = "";
}else {
  rtMsrg = rtMsrg.replace("\"", "\\\"").replace("\r", "").replace("\n", "");
}
if (rtData == null) {
  rtData = "\"\"";
}

%>
{"rtState":"<%=rtState %>", "rtMsrg":"<%=rtMsrg %>", "rtData":<%=rtData %>}
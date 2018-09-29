<%@page import="yh.core.global.YHActionKeys"%>
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String rtState = (String)request.getAttribute(YHActionKeys.RET_STATE);
String rtMsrg = (String)request.getAttribute(YHActionKeys.RET_MSRG);
String rtData = (String)request.getAttribute(YHActionKeys.RET_DATA);
if (rtState == null) {
  rtState = "0";
}
if (rtMsrg == null) {
  rtMsrg = "";
}
if (rtData == null) {
  rtData = "";
}
%>
<response>
  <rtState><%=rtState %></rtState>
  <rtMsrg><%=rtMsrg %></rtMsrg>
  <rtData><%=rtData %></rtData>
</response>
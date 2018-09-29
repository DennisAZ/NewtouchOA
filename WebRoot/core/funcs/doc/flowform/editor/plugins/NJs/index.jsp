<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
    <% 
    String seqId = request.getParameter("seqId");
    String url = moduleSrcPath + "/util/YHFlowFormUtilAct/getJsForm.act?seqId=" + seqId; 
    RequestDispatcher rd = request.getRequestDispatcher(url);
    rd.forward(request,response);
    %>

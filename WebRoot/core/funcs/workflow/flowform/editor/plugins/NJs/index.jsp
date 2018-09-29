<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <% 
    String seqId = request.getParameter("seqId");
    String url = "/yh/core/funcs/workflow/util/YHFlowFormUtilAct/getJsForm.act?seqId=" + seqId; 
    RequestDispatcher rd = request.getRequestDispatcher(url);
    rd.forward(request,response);
    %>

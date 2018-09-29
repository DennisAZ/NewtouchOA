<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="yh.core.funcs.email.data.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<title>查看邮件状态 ：</title>
<%
  List<YHEmail> statusList = (List<YHEmail>)request.getAttribute("statusList");
  int bodyId = (Integer)request.getAttribute("bodyId");
%>
</head>
<body onload="doInit();">
	<table cellscpacing="1" cellpadding="3" width="100%">
	  <tr class="TableHeader">
	    <td nowrap align="center">状态</td>
	    <td nowrap align="center">收信人</td>
	  </tr>
	  <%
     int k = 0;
     for(int i = 0; i < statusList.size(); i++) {
       YHEmail em = statusList.get(i);
   %>
	  <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
	    <td nowrap align="center">
	      <span>
        <%if(em.getDeleteFlag().equals("1")){ %><img vspace="0" hspace="2" src="<%=imgPath%>/email_delete.gif" title="收信人已删除">
        <%}else{
            if(em.getReadFlag().equals("0")){ %><img  vspace="0" hspace="2" src="<%=imgPath%>/cmp/email/email_close.gif" title="未提醒">
            <%}else{ %> <img  vspace="0" hspace="2" src="<%=imgPath%>/cmp/email/inbox/email_open.gif" title="收信人已读">
            <%}
           }%>
        </span>
      </td>
	    <td nowrap align="center">
	      <input type="hidden" id="toId<%=k%>" name="toId<%=k%>" value="<%=em.getToId()%>">
        <span id="toId<%=k%>Desc"></span>
	    </td>
	  </tr>
 <% k++;
 } %>
	</table>
</body>
<script type="text/javascript">
function doInit(){
  for(var i =0 ; i < <%=k%> ;i++){
    bindDesc([{cntrlId:"toId"+i, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}
</script>
</html>
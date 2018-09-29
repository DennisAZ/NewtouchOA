<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
%>
<html>
<head>
<title>审批意见</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit(){
   var seqId = "<%=seqId%>";
   var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/getNoteById.act?seqId='+seqId;
   var json = getJsonRs(url);
   if(json.rtState == "0"){
	   var data = json.rtData;
	   var auditierName = data.auditierName;
	   var reason = data.reason;
	  
	   $("auditierName").innerHTML = auditierName;
	   $("reason").innerHTML = reason.replace("<", "&lt;").replace(">", "&gt;");
    }else{
	    document.body.innerHTML = json.rtMsrg;
	} 
}
</script>
</head>
<body onload="doInit();" topmargin="3">

  
   <div id="auditierName"></div>
       对此公告通知的意见
    </br>
    <div id="reason"></div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
String dayTime = sf.format(new Date());
String seqIds = request.getParameter("seqIds") == null ? "" : request.getParameter("seqIds");
%>
<html>

<head>
<title>退回原因</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" >
var seqIds = "<%=seqIds%>";
function doInit(){
  var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/updateStatus.act?seqId=" + seqIds + "&status=4"  ; 
  var json=getJsonRs(requestURL,{returnReason:$("returnReason").value}); 
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  }
  alert("退回成功" );
  window.opener.location.reload();
   window.close();
}


</script> 
</head>
<body class="" topmargin="5" onload="">


<table class="TableBlock" width="100%;" height="100%" align="center" >
<tr>
           <td nowrap class="TableContent">退回原因： </td>
      <td class="TableData">
      <textarea rows="5" cols="30" name="returnReason" id="returnReason"></textarea>
 
      </td>
  
   </tr>
   
   <tr class="TableControl">
   	<td colspan="6" align="center" >  &nbsp;   &nbsp;  &nbsp;
   		<input type="button"  value="确认" class="BigButton" onClick="doInit();"> &nbsp;  &nbsp; &nbsp;
   		<input type="button"  value="取消" class="BigButton" onClick="window.close();">
   	</td>
   </tr>
 </table>

</body>
 
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>不准原因</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function Init(){
  var url="<%=contextPath%>/yh/custom/attendance/act/YHAnnualLeaveAct/updateAllowReason.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  if(rtJson.rtState == "0"){
    parent.opener.location.reload();
    window.close();
  }
}
</script>
<body class="" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 不准原因</span>
    </td>
  </tr>
</table>
<br>
<form action="#"  method="post" id="form1" name="form1">  
<table class="TableBlock"  width="450" align="center" >
   <tr>
     <td nowrap class="TableContent"> 不准原因：</td>
     <td class="TableData" colspan="1">
       <textarea id="notReason" name="notReason" class="BigInput" cols="50" rows="16"></textarea>
     </td>
   </tr> 
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" id="seqId" value="<%=seqId %>" name="seqId">
      <input type="hidden" id="allow" value="<%=request.getParameter("allow") %>" name="allow">
      <input type="hidden" id="userId" value="<%=request.getParameter("userId") %>" name="userId">
             <input type="hidden" id="checkEvection" value="<%=request.getParameter("checkEvection") %>" name="checkEvection">
      <input type="button" value="确定" onclick="Init();" class="BigButton">&nbsp;&nbsp;
      <input type="button" class="BigButton" value="关闭" onClick="window.close();" title="关闭窗口">
    </td>
</table>
</form>
</body>
</html>
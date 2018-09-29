<%@ page import="java.net.URLDecoder" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>指定考勤审批人员</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doOnload(){
  var requestURL; 
  var prcsJson; 
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendManagerAct/selectManager.act"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  if(prcsJson.user != ""){
    document.getElementById("user").value = prcsJson.user;
    
    document.getElementById("userDesc").value = prcsJson.userDesc;
  }
}
</script>
</head>
<body class="" topmargin="5" onload = "doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;指定考勤审批人员</span>
    </td>
  </tr>
</table>
<br><br>
<form action="<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendManagerAct/add_updateManager.act" method="post" id = "form1" name="form1">
<table class="TableBlock" width="600" align="center">
  <tr class="TableLine1">
    <td >
      <input type="hidden" name="user" id="user" value=""/>
      <textarea name="userDesc" id="userDesc"  rows="6" class="BigStatic"  cols="50" readonly="readonly" ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
    </td>
  </tr>
  <tr class="TableControl">
    <td align="center" valign="top" colspan="2">
      <input type="submit" class="BigButton" value="保 存">&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" class="BigButton" value="返 回" onclick="history.go(-1);">
      <input type="hidden" name="MANAGER_ID" value="1">
    </td>
  </tr>
</table>
</form>

</body>
</html>

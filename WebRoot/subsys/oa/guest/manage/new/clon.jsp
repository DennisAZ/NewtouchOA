<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String seqId = request.getParameter("seqId")==null ? "" :request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>克隆贵宾信息</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script Language="JavaScript"> 
function checkForm(){
  if ($("guestNum").value == "") {
    alert("贵宾编号必填!");
    $("guestNum").focus();
    $("guestNum").select();
    return false;
  }
  if ($("guestName").value == "") {
    alert("贵宾名称必填!");
    $("guestName").focus();
    $("guestName").select();
    return false;
  }
  return true;
}
function doInit(){
  var seqId = '<%=seqId%>';
  if(checkForm()){
    var pars = $("form1").serialize();
    var requestURL = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/clonGuest.act?seqId="+seqId; 
    var json=getJsonRs(requestURL,pars); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    }
    //alert("克隆成功！");
    window.location.href="<%=contextPath%>/subsys/oa/guest/manage/new/success.jsp";
  }
}
</script>
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 贵宾信息克隆</span><br>
    </td>
  </tr>
</table>
 
<br>
<form action="#" method="post" name="form1" id="form1">
<table width="450" align="center" class="TableBlock">

    <tr>
      <td nowrap class="TableData">新贵宾编号：<span style="color:red">*</span></td>
      <td class="TableData">
        <input type="text" id="guestNum" name="guestNum" size="20" maxlength="100" class="BigInput" value=""> 
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">新贵宾名称：<span style="color:red">*</span></td>
      <td class="TableData">
        <input type="text" id="guestName" name="guestName" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type='hidden' value="7" name="seqId" id="seqId" value="<%=seqId %>">
        <input type="button"  value="保存" class="BigButton" onclick="doInit();">&nbsp;&nbsp;
      	<input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
      </td>
    </tr>

</table>
  </form>
</body>
</html>
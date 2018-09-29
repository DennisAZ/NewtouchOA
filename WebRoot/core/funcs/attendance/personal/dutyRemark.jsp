<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>说明情况</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function doOnload(){
  var seqId = '<%=request.getParameter("seqId")%>';
  // alert(seqId);
  var requestURLDuty = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/selectDutyById.act?seqId=" + seqId; 
  var rtjson = getJsonRs(requestURLDuty); 
  //alert(rsText);
  if(rtjson.rtState == '1'){ 
    alert(rtjson.rtMsrg); 
    return ; 
  }
  var dutyJson = rtjson.rtData;
  var remark = dutyJson.remark;
  
  document.getElementById("remark").value = remark ;
}
function Init(){
  var seqId = document.getElementById("seqId").value;
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/updateDutyById.act?seqId=" + seqId; 
  var rtjson = getJsonRs(requestURL,mergeQueryString($("form1"))); 
  if(rtjson.rtState == '1'){ 
    alert(rtjson.rtMsrg); 
    return ; 
  }
  parent.opener.location.reload();
  if(rtjson.rtState == '0'){ 
    window.close();
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/calendar.gif" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 说明情况</span>
    </td>
  </tr>
</table>
<br>
 <form action="<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendDutyAct/updateDutyById.act"  method="post" id = "form1" name="form1" onsubmit="">
 <table class="TableBlock"  width="400" align="center">
    <tr>
      <td nowrap class="TableHeader"> 请说明有关情况（如迟到或早退原因，加班情况等）:</td>
    </tr>
    <tr>
      <td class="TableData">
        <textarea  id = "remark" name = "remark" cols="50" rows="7" class="BigInput"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap style = "display : none"><input type="text" id = "seqId" name="seqId" value = "<%=request.getParameter("seqId") %>"/></td>
      <td nowrap>
        <input type="button" value="确定" onclick = "Init();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>


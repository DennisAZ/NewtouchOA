<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑日期</title>
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
function CheckForm(){
  if(document.getElementById("beginDate").value==""){ 
    document.getElementById("beginDate").focus();
    document.getElementById("beginDate").select();
    alert("起始日期不能为空！");
    return (false);
  }
  if(document.getElementById("endDate").value==""){
    document.getElementById("endDate").focus();
    document.getElementById("endDate").select(); 
    alert("结束日期不能为空！");
    return (false);
  }
  if(!isValidDateStr(document.getElementById("beginDate").value)){
    alert("起始日期格式不对,应形如 2010-02-01");
    document.getElementById("beginDate").focus();
    document.getElementById("beginDate").select();
    return false;
    }
  if(!isValidDateStr(document.getElementById("endDate").value)){
    alert("结束日期格式不对,应形如 2010-02-01");
    document.getElementById("endDate").focus();
    document.getElementById("endDate").select(); 
    return false;
    }
  var beginDate = document.getElementById("beginDate").value;
  var endDate = document.getElementById("endDate").value;
  var beginInt;
  var endInt;
  var beginArray = (document.getElementById("beginDate").value).split("-");
  var endArray = (document.getElementById("endDate").value).split("-");
  for(var i = 0 ; i<beginArray.length; i++){
    beginInt = parseInt(" " + beginArray[i]+ "",10);  
    endInt = parseInt(" " + endArray[i]+ "",10);
    if((beginInt - endInt) > 0){
      alert("起始日期不能大于结束日期!");
      document.getElementById("endDate").focus();
      document.getElementById("endDate").select(); 
      return false;
    }else if(beginInt<endInt){
      return true;
    }  
  }
  return true;
}
function returnBefore(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/holiday.jsp";
}
function doOnload(){
  var requestURL; 
  var prcsJson; 
  var seqId = '<%=request.getParameter("seqId")%>';
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendHolidayAct/selectHolidayById.act?seqId=" + seqId; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  prcsJson = json.rtData;
  document.getElementById("seqId").value = prcsJson.seqId;
  document.getElementById("beginDate").value = prcsJson.beginDate.substr(0,10);
  document.getElementById("endDate").value = prcsJson.endDate.substr(0,10);
  var beginParameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
}
</script>
</head>
<body class="" topmargin="5" onload = "doOnload()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;编辑日期</span>
    </td>
  </tr>
</table>
<form action="<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendHolidayAct/updateHolidayById.act"  method="post" id = "form1" name="form1" onsubmit="return CheckForm();">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.attendance.data.YHAttendHoliday"/>
<table class="TableBlock"  width="450" align="center" >
  <tr>
    <td nowrap class="TableData">起始日期：</td>
    <td nowrap class="TableData">
        <input type="text" id = "beginDate" name="beginDate" class="BigInput" size="10" maxlength="10" value="">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    </td>
   <tr>
    <td nowrap class="TableData">结束日期：</td>
    <td nowrap class="TableData">
        <input type="text" id = "endDate" name="endDate" class="BigInput" size="10" maxlength="10" value="">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" id = "seqId" value="" name="seqId">
        <input type="submit" value="确定" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="returnBefore();">
    </td>
</table>
</form>
</body>
</html>
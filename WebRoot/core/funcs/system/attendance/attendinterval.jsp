<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设置上下班登记时间段<</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function CheckForm(){
  if(document.getElementById("dutyIntervalBefore1").value==""){ 
     alert("上班提前间隔时间不能为空！");
     document.getElementById("dutyIntervalBefore1").focus();
     document.getElementById("dutyIntervalBefore1").select();
     return (false);
   } 

  if(document.getElementById("dutyIntervalAfter1").value==""){ 
    alert("上班延后间隔时间不能为空！");
    document.getElementById("dutyIntervalAfter1").focus();
    document.getElementById("dutyIntervalAfter1").select();
    return (false);
   } 
 if(document.getElementById("dutyIntervalBefore2").value==""){ 
    alert("下班提前间隔时间不能为空！");
    document.getElementById("dutyIntervalBefore2").focus();
    document.getElementById("dutyIntervalBefore2").select();
    return (false);
  }   
 if(document.getElementById("dutyIntervalAfter2").value==""){ 
    alert("下班延后间隔时间不能为空！");
    document.getElementById("dutyIntervalAfter2").focus();
    document.getElementById("dutyIntervalAfter2").select();
    return (false);
  }
 var   type=   "^[0-9]*[1-9][0-9]*$"　; 
 var   re   =   new   RegExp(type); 
 if(document.getElementById("dutyIntervalBefore1").value.match(re)==null) { 
   alert( "你输入的上班提前间隔时间不是整数! "); 
   document.getElementById("dutyIntervalBefore1").focus();
   document.getElementById("dutyIntervalBefore1").select();
   return false;
 } 
 if(document.getElementById("dutyIntervalAfter1").value.match(re)==null) { 
   alert( "你输入的上班延后间隔时间不是整数! ");
   document.getElementById("dutyIntervalAfter1").focus();
   document.getElementById("dutyIntervalAfter1").select(); 
   return false;
 } 
 if(document.getElementById("dutyIntervalBefore2").value.match(re)==null) { 
   alert( "你输入的下班提前间隔时间不是整数! "); 
   document.getElementById("dutyIntervalBefore2").focus();
   document.getElementById("dutyIntervalBefore2").select();
   return false;
 } 
 if(document.getElementById("dutyIntervalAfter2").value.match(re)==null) { 
   alert( "你输入的下班延后间隔时间不是整数! "); 
   document.getElementById("dutyIntervalAfter2").focus();
   document.getElementById("dutyIntervalAfter2").select();
   return false;
 } 
  return true;
}
function returnBefore(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/index.jsp";
}
function doOnload(){
  var requestURL; 
  var prcsJson; 
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHSysParaAct/selectParaInteval.act"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  document.getElementById("dutyIntervalBefore1").value = prcsJson.dutyIntervalBefore1;
  document.getElementById("dutyIntervalAfter1").value = prcsJson.dutyIntervalAfter1;
  document.getElementById("dutyIntervalBefore2").value = prcsJson.dutyIntervalBefore2;
  document.getElementById("dutyIntervalAfter2").value = prcsJson.dutyIntervalAfter2;
}
</script>
</head>
<body onload = "doOnload()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;设置上下班登记时间段</span><br>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/core/funcs/system/attendance/act/YHSysParaAct/update_addInteval.act" name="form1" id = "form1" onsubmit="return CheckForm();">
  <table class="TableBlock"  width="400" align="center">
    <tr>
      <td class="TableHeader"><span style="color:green">设置上班登记时间段</span></td>
    </tr>
    <tr>
      <td class="TableData">提前 <input type="text" id = "dutyIntervalBefore1" name="dutyIntervalBefore1" class="SmallInput" size="5" value="" > 分钟允许登记</td>
    </tr>
    <tr>
      <td class="TableData">延后 <input type="text" id = "dutyIntervalAfter1" name="dutyIntervalAfter1" class="SmallInput" size="5" value="" > 分钟允许登记</td>
    </tr>
     <tr>
      <td class="TableHeader"><span style="color:green">设置下班登记时间段</span></td>
    </tr>
    <tr>
      <td class="TableData">提前 <input type="text"  id = "dutyIntervalBefore2" name="dutyIntervalBefore2" class="SmallInput" size="5" value="" > 分钟允许登记</td>
    </tr>
    <tr>
      <td class="TableData">延后 <input type="text" id = "dutyIntervalAfter2"  name="dutyIntervalAfter2" class="SmallInput" size="5" value="" > 分钟允许登记</td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="submit" value="确定" class="BigButton" title="确定" name="button">&nbsp;
          <input type="button" value="返回" class="BigButton" title="返回" onclick="returnBefore();">
      </td>
    </tr>
  </table>
 </form>
</body>
</html>
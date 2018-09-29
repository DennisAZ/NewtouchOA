<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String find = request.getParameter("findSum");
  if (find == null){
    find = "";
  }
  String sum = request.getParameter("sum");
  if (sum == null){
    sum = "";
  }
  String replacement = request.getParameter("replacementSum");
  if (replacement == null){
    replacement = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>短信提醒设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var find = "<%=find%>";
var replacement = "<%=replacement%>";
var sum = "<%=sum%>";
function doInit(){
  var censorVal = sum.split(',');  
  //for(var i = 0; i < censorVal.length-1; i++){
   //var find = censorVal[i].substr(0,censorVal[i].indexOf("="));
   //var replacement = censorVal[i].substr(censorVal[i].indexOf("=")+1, censorVal[i].length-1);
  var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/updateMore0Words.act?find="+encodeURIComponent(censorVal);
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++) {
      if(rtJson.rtData[i].find==""||rtJson.rtData[i].find=="null"){
        document.getElementById("findRe").style.display = "none";
      }
   	  document.getElementById("cut").innerHTML += rtJson.rtData[i].find+" ";
   	  document.getElementById("errCount").innerHTML = rtJson.rtData[i].errCount;
   	  document.getElementById("okCount").innerHTML = rtJson.rtData[i].okCount;
    }
  }else{
 	alert(rtJson.rtMsrg);
  }
 //}
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<form method="post" name="form1" id="form1">
<table class="MessageBox" align="center" width="330">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">成功添加<span id="okCount"></span>条，跳过 <span id="errCount"></span> 条</div>
    </td>
  </tr>
</table>
<div id="findRe">
<table class="TableBlock" width="90%" align="center">
    <tr class="TableHeader">
      <td>以下规则被跳过(词语已存在)</td>
    </tr>
    <tr class="TableData">
      <td id="cut"></td>
    </tr>
 </table>
 </div>
 </form>
<center>
  <Input type="button" name="button" class="BigButton" value="返回" onclick="window.history.back();">
</center>
</body>
</html>
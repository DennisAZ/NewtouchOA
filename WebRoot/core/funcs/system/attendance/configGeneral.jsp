<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设置公休日</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function Init(){
  var requestURL; 
  var prcsJson; 
  var seqId = '<%=request.getParameter("seqId")%>';
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendConfigAct/updateConfigGenaralById.act?seqId=" + seqId; 
  var json = getJsonRs(requestURL,mergeQueryString($("form1"))); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  history.go(-1);
}
function doOnload(){
  var prcs ;
  var seqId = '<%=request.getParameter("seqId")%>'; 
  if(seqId != "undefined"){
    var requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendConfigAct/selectConfigById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL); 
    var prcs = json.rtData; 
    var general = prcs.general;
    var configName = prcs.dutyName;
    $("dutyName1").innerHTML = configName;
    $("dutyName2").innerHTML = configName;
    var strs= new Array(); 
    var str = "";
    if(general.trim().length>0){
      strs = general.trim().split(",");    
    }
    for (i=0;i<strs.length ;i++ ){
      str = strs[i];
      if(str=='0'){
         str=7;
      }
      if(str != ""){
        //alert(str);
        document.getElementById("week" + str).checked = 'checked';
        }  
     }     
  }
}
</script>
</head>
<body onload = "doOnload()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/form.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;设置公休日(<span id="dutyName1"></span>)</span>
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" id = "form1" name="form1">
 <table class="TableList" width="450" align="center">
    <tr class="TableHeader">
      <td nowrap align="center"> 请选择 <span id="dutyName2"></span>&nbsp;的公休日</td>
    </tr>
    <tr>
      <td class="TableData">
          <input type="checkbox" name="week1" id="week1" value = "1"><label for="week1">星期一</label><br>
          <input type="checkbox" name="week2" id="week2" value = "2"><label for="week2">星期二</label><br>
          <input type="checkbox" name="week3" id="week3" value = "3"><label for="week3">星期三</label><br>
          <input type="checkbox" name="week4" id="week4" value = "4"><label for="week4">星期四</label><br>
          <input type="checkbox" name="week5" id="week5" value = "5"><label for="week5">星期五</label><br>
          <input type="checkbox" name="week6" id="week6" value = "6"><label for="week6">星期六</label><br>
          <input type="checkbox" name="week7" id="week7" value = "7"><label for="week0">星期日</label><br>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap>
        <input type="hidden" value="1" name="">
        <input type="button" value="确定" class="BigButton" onclick = "Init();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="history.go(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>